import Config from './config'
import storage from './storage'
import {isObject} from '../utils/types'

export default {
    set(value, flag, seting = Config.seting[Config.platform]) {
        storage.set(value, flag, seting)
    },
    get(flag, seting = Config.seting[Config.platform]) {
        return storage.get(flag, seting)
    },
    remove(seting = Config.seting[Config.platform]) {
        storage.remove(seting)
        this.removeSession()
    },
    setSession(token) {
        !this.getSession() && window.sessionStorage.setItem(Config.session, isObject(token) ? JSON.stringify(token) : token)
    },
    getSession() {
        let token = window.sessionStorage.getItem(Config.session)
        try {
            token = JSON.parse(token)
        } catch (er) {
            process.env.NODE_ENV === "development" && console.log(er)
        }
        return token || false
    },
    removeSession() {
        window.sessionStorage.removeItem(Config.session)
    }
}
