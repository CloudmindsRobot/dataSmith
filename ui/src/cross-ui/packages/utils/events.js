import {isFunction} from './types'

export default {
    events: [],
    bind(func, ns = 'cross') {
        isFunction(func) && this.events.push({event: func, namespace: ns})
    },
    remove(ns = 'cross') {
        const index = this.events.findIndex(item => item.namespace === ns)
        index !== -1 && this.events.splice(index, 1)
    },
    clear() {
        this.events.length = 0
    },
    emit(arg) {
        this.events.forEach(item => item.event(arg))
    }
}