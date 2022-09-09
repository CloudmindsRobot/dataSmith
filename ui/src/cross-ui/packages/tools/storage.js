import {isObject} from '../utils/types'

const sessionId = new Date().getTime()

export default {
    set(value, name, seting, expires) {
        const options = {
            value: this.get(undefined, seting) || {sessionId: `${sessionId}`},
            expires: expires,
            startTime: new Date().getTime()
        }
        isObject(options.value) ? options.value[name] = value : options.value = value
        window.sessionStorage.setItem(seting, JSON.stringify(options))
    },
    get(name, seting) {
        let item = window.sessionStorage.getItem(seting)
        // window.localStorage.getItem(seting)
        try {
            item = JSON.parse(item)
        } catch (er) {
            process.env.NODE_ENV === "development" && console.log(er)
        }
        if (item && item.startTime) {
            const date = new Date().getTime()
            if (date - item.startTime > item.expires) {
                this.remove(seting)
                return false
            }
            return name ? item.value[name] : item.value
        }
        return item || false
    },
    remove(seting) {
        window.sessionStorage.removeItem(seting)
        //window.localStorage.removeItem(seting)
    }
}