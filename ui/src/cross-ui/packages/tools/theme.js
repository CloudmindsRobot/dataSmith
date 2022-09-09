import $ from 'jquery'
import Config from './config'
import Events from '../utils/events'

export default {
    init() {
        const theme = this.getCurrentTheme()
        $(`link[rel="stylesheet"][href*="${this.getNextTheme()}"]`).each(function () {
            this.href = this.href.replace(/(dark|light)/g, theme)
        })
    },
    change() {
        const newTheme = this.getNextTheme()
        $(`link[rel="stylesheet"][href*="${this.getCurrentTheme()}"]`).each(function () {
            this.href = this.href.replace(/(dark|light)/g, newTheme)
        })
        window.localStorage.setItem(Config.theme, newTheme)
        Events.emit(newTheme)
    },
    getNextTheme() {
        return (this.getCurrentTheme() === 'light' ? 'dark' : 'light')
    },
    getCurrentTheme() {
        return window.localStorage.getItem(Config.theme) || Config.css
    },
    bindEvent(func, ns = 'cross') {
        Events.bind(func, ns)
    },
    removeEvent(ns = 'cross') {
        Events.remove(ns)
    }
}