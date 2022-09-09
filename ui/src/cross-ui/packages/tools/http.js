import Axios from 'axios'
import Config from './config'
import Seting from './seting'

class httpRequest {
    constructor() {
        this.options = {
            method: 'POST',
            url: ''
        }
        this.queue = {}
    }

    destroy(url) {
        delete this.queue[url]
        const queue = Object.keys(this.queue)
        return queue.length
    }

    // 实现request请求拦截机制
    interceptors(instance, url) {
        instance.interceptors.request.use((config) => {
            Config.requestFormData && (config.headers['Content-Type'] = 'multipart/form-data')
            return config
        })
        instance.interceptors.response.use((response) => {
            this.destroy(url)
            const code = response.data.subCode || response.data.code
            if (response.data.code === 4003) {
                const flag = code === 'auth.no_permission'
                window.location.replace(`${flag ? Config.page403 : Config.invalid}?code=${flag ? '403' : code}`)
                return
            }
            return response.data
        }, (err) => {
            if (err.response.status === 403 || err.response.status === 401) {
                window.location.replace(`${err.response.status === 403 ? Config.page403 : Config.invalid}?code=${err.response.status}`)
                return
            }
            return Promise.reject(err.response)
        })
    }

    // 实现2种数据请求格式
    create(formData) {
        let conf = formData ? {
            baseURL: Config.baseUrl,
            timeout: Config.timeout,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
                'X-URL-PATH': location.pathname,
                'Authorization': Config.token,
                sysId: Config.systemId
            },
            transformRequest: [function (data) {
                function param(obj) {
                    let query = '';
                    let name;
                    let value;
                    let fullSubName;
                    let subName;
                    let subValue;
                    let innerObj;
                    let i
                    for (name in obj) {
                        value = obj[name]
                        if (value instanceof Array) {
                            for (i = 0; i < value.length; ++i) {
                                subValue = value[i]
                                fullSubName = name + '[]'
                                innerObj = {}
                                innerObj[fullSubName] = subValue
                                query += param(innerObj) + '&'
                            }
                        } else if (value instanceof Object) {
                            for (subName in value) {
                                subValue = value[subName]
                                fullSubName = name + '[' + subName + ']'
                                innerObj = {}
                                innerObj[fullSubName] = subValue
                                query += param(innerObj) + '&'
                            }
                        } else if (value !== undefined && value !== null) {
                            query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&'
                        }
                    }
                    return query.length ? query.substr(0, query.length - 1) : query
                }

                // 可以对data做任何操作
                return typeof data === 'object' && String(data) !== '[object File]' ? param(data) : data
            }]
        } : {
            baseURL: Config.baseUrl,
            timeout: Config.timeout,
            headers: {
                'Content-Type': 'application/json;charset=utf-8',
                'Authorization': Seting.get(Config.tokenKey),
                sysId: Config.systemId
            }
        }
        return Axios.create(conf)
    }

    // mergeRequest (instances = []) {
    //
    // }

    request(options, formData) {
        let instance = this.create(formData)
        this.interceptors(instance, this.options.url)
        if (options) {
            Object.assign(this.options, options)
        }
        this.queue[this.options.url] = instance
        return instance(this.options)
    }
}

export default {
    instance: null,
    init() {
        !this.instance && (this.instance = new httpRequest())
        return this.instance
    }
}
