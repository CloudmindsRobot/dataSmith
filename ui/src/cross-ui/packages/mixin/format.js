import {isNumber} from '../utils/types'
import Config from '../tools/config'
import DateTimeTranslate from '../tools/timer'

export default {
    methods: {
        /**
         * 处理数值类型
         *
         * @param {string|number} value 值
         * @returns {number|string}
         */
        numberFormat(value) {
            return isNumber(value) ? value : (value || Config.emptyText)
        },
        /**
         * 格式化日期
         *
         * @param {date} date 日期
         * @returns {string}
         */
        dateFormat(date) {
            return DateTimeTranslate.formatDateTime(date) || Config.emptyText
        },
        /**
         * 格式化时间
         *
         * @param {string} time 时间
         * @returns {string}
         */
        timeFormat(time) {
            return time ?  DateTimeTranslate.formatDateTime(new Date(time)) : Config.emptyText
        }
    }
}