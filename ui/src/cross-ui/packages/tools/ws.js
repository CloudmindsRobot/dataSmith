import Stomp from 'stompjs'
import {isObject, isFunction} from '../utils/types'
import Config from './config'

class Connection {
    constructor(option = {}) {
        this.url = option.url || Config.socketUrl
        this.reconnectInterval = option.reconnectInterval || 5 * 1000
        this.client = null
        this.reconnectTimer = null
        this.subscribes = {}
    }

    on(event, fn) {
        if (!isFunction(fn)) {
            return
        }
        if (this.subscribes[event]) {
            this.subscribes[event].push(fn)
        } else {
            this.subscribes[event] = [fn]
            this.subscribe(event)
        }
    }

    off(event, fn) {
        const listeners = this.subscribes[event]
        if (listeners) {
            const index = listeners.indexOf(fn)
            if (index > -1) {
                listeners.splice(index, 1)
                process.env.NODE_ENV === "development" && console.log(`[unsubscribe: <${event}>] `, fn)
                if (!listeners.length && this.client && this.client.connected) {
                    this.client.unsubscribe(event)
                    this.subscribes[event] = null
                }
            }
        }
    }

    emit(event, data) {
        const listeners = this.subscribes[event]
        if (listeners) {
            for (let i = 0; i < listeners.length; i++) {
                listeners[i](data)
            }
        }
    }

    subscribe(event) {
        if (this.client && this.client.connected) {
            this.client.subscribe(event, frame => {
                const obj = {id: event}
                try {
                    obj.data = JSON.parse(frame.body)
                } catch {
                    obj.data = frame.body
                }
                // console.log(`event: <${event}> `, data)
                this.emit(event, obj)
            }, {id: event})
            process.env.NODE_ENV === "development" && console.log(`[subscribe: <${event}>]`)
        } else {
            process.env.NODE_ENV === "development" && console.warn('[stomp not connected] waiting for connecting...')
        }
    }

    connect() {
        if (this.client && this.client.connected) {
            return Promise.resolve(this.client)
        }
        this.client = Stomp.client(this.url)
        // this.client.debug = true
        return new Promise((resolve, reject) => {
            this.client.connect(Config.socketHeader, frame => {
                    process.env.NODE_ENV === "development" && console.log('[stomp connected]', frame)
                    Object.keys(this.subscribes).forEach(event => {
                        this.subscribe(event)
                    })
                    resolve(frame)
                },
                error => {
                    process.env.NODE_ENV === "development" && console.log('[stomp connect error] ', error)
                    this.reconnect()
                    reject(error)
                }
            )
        })
    }

    reconnect() {
        this.reconnectTimer && clearTimeout(this.reconnectTimer)
        this.reconnectTimer = setTimeout(() => {
            this.connect()
            process.env.NODE_ENV === "development" && console.log('[ws reconnect] ', this.reconnectTimer)
        }, this.reconnectInterval)
    }

    disconnect(callback) {
        this.client && isFunction(callback) && this.client.disconnect(callback)
        if (this.reconnectTimer) {
            clearTimeout(this.reconnectTimer)
            this.reconnectTimer = null
        }
        this.subscribes = {}
        process.env.NODE_ENV === "development" && console.log('[ws disconnect]')
    }
}

class Topic {
    constructor(topic) {
        return topic ? new Proxy({}, {
            get(target, route) {
                return `/exchange/${topic}/${route}`
            }
        }) : null
    }
}

export class WS {
    constructor(option) {
        this.client = new Connection(option)
    }
    /**
     * 
     * @param {String|Object} option 
     * @param {*} event 
     * @returns 
     */
    on(option, event) {
        if (!isFunction(event)) {
            return
        }
        if(isObject(option)){
            const topic = new Topic(option.topic)
            option = topic && topic[option.route]
        }
        option && this.client.on(option, event)
    }
    /**
     * 
     * @param {String|Object} option 
     * @param {*} event 
     * @returns 
     */
    off(option, event) {
        if (!isFunction(event)) {
            return
        }
        if(isObject(option)){
            const topic = new Topic(option.topic)
            option = topic && topic[option.route]
        }
        option && this.client.off(option, event)
    }

    listen (callback) {
        this.client.connect().then(frame => {
            isFunction(callback) && callback(frame)
        })
    }

    disconnect(callback) {
        this.client.disconnect(callback)
    }
}

// mq 前后端链接文档https://10.14.32.231:10443/doc/Internal/#/MQ/?id=_1%e3%80%81%e8%ae%be%e5%a4%87%e5%91%8a%e8%ad%a6%e4%bf%a1%e6%81%af
export const deviceTopic = new Topic('topic.device.portal') // 设备
export const talkingTopic = new Topic('topic.robot.talking') // 通话
export const taskTopic = new Topic('topic.task.portal') // 任务
/**
 * 
 * @param {*} event 订阅消息类型 status talking taskStatusUpdate ...
 * @param {*} code  运营系统使用编码如：caos，cxos 等，租户系统使用 tenantCode
 * @returns 
 */
export const getTopic = (event,code)=>{
    switch(event){
        case 'status':// 设备状态汇总
        case 'alarm':// 告警
        case 'battery': // 电量
        case 'onlineFlag': // 是否在线状态
        case 'location': // 位置上报
        case 'network':// 网络状态
        case 'event':// 事件
        case 'sprayStatus': // 喷雾
        case 'emergencyStop':// 一键急停状态
        case 'selfChecking': // 设备自检
            return deviceTopic[`${code}_${event}`]
           break;
        case "talking":// 通话呼叫消息
            return talkingTopic[`signal.tenant.${code}`]
            break;
        case 'taskStatusUpdate':// 任务状态更新
            return taskTopic[`${code}_${event}`]
            break
        default:
            break
    }
    return ''
}

// es6 引用单实例，每个应用应该只存在一个实例
// 关联影响 layout组件ws
/**
 *  使用步骤：
 * 1 引入ws实例
 * 2 ws.on(...,event)
 * 3 ws.listen(...)
 * 4 子组件beforeDestroyed:ws.off(event)
 * 5 APP beforeDestroyed:ws.disconnect()
 */
const ws = new WS()
export default ws

