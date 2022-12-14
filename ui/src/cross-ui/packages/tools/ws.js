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

// mq ?????????????????????https://10.14.32.231:10443/doc/Internal/#/MQ/?id=_1%e3%80%81%e8%ae%be%e5%a4%87%e5%91%8a%e8%ad%a6%e4%bf%a1%e6%81%af
export const deviceTopic = new Topic('topic.device.portal') // ??????
export const talkingTopic = new Topic('topic.robot.talking') // ??????
export const taskTopic = new Topic('topic.task.portal') // ??????
/**
 * 
 * @param {*} event ?????????????????? status talking taskStatusUpdate ...
 * @param {*} code  ??????????????????????????????caos???cxos ???????????????????????? tenantCode
 * @returns 
 */
export const getTopic = (event,code)=>{
    switch(event){
        case 'status':// ??????????????????
        case 'alarm':// ??????
        case 'battery': // ??????
        case 'onlineFlag': // ??????????????????
        case 'location': // ????????????
        case 'network':// ????????????
        case 'event':// ??????
        case 'sprayStatus': // ??????
        case 'emergencyStop':// ??????????????????
        case 'selfChecking': // ????????????
            return deviceTopic[`${code}_${event}`]
           break;
        case "talking":// ??????????????????
            return talkingTopic[`signal.tenant.${code}`]
            break;
        case 'taskStatusUpdate':// ??????????????????
            return taskTopic[`${code}_${event}`]
            break
        default:
            break
    }
    return ''
}

// es6 ?????????????????????????????????????????????????????????
// ???????????? layout??????ws
/**
 *  ???????????????
 * 1 ??????ws??????
 * 2 ws.on(...,event)
 * 3 ws.listen(...)
 * 4 ?????????beforeDestroyed:ws.off(event)
 * 5 APP beforeDestroyed:ws.disconnect()
 */
const ws = new WS()
export default ws

