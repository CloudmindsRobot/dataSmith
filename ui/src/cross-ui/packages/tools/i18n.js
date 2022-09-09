import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Config from './config'
import Locale from './locale'
import Device from './device'
import Messages from '../locale/entry'

Vue.use(VueI18n)

class i18n extends VueI18n {
    constructor(messages = {}) {
        const locale = window.localStorage.getItem(Config.language)
        Config.languages.forEach(l => Object.assign(Messages[l.code], messages[l.code]))
        super({
            locale: locale || Locale.language(Device.language),
            messages: Messages,
            silentFallbackWarn: true,
            silentTranslationWarn: true
        })
    }
}

export default i18n
