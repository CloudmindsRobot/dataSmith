// 日期格式转换类
export default {
  setDayStart (date) {
    if (date instanceof Date) {
      date.setHours(0)
      date.setMinutes(0)
      date.setSeconds(0)
      return date
    }
    return null
  },
  setDayEnd (date) {
    if (date instanceof Date) {
      date.setHours(23)
      date.setMinutes(59)
      date.setSeconds(59)
      return date
    }
    return null
  },
  // 用户自定义的日期格式
  customTimeFormat (time, fmt, utc = false) {
    if (time instanceof Date) {
      let o = {
        'y+': utc ? time.getUTCFullYear() : time.getFullYear(),
        'M+': (utc ? time.getUTCMonth() : time.getMonth()) + 1, // 月份
        'd+': utc ? time.getUTCDate() : time.getDate(), // 日
        'h+': utc ? time.getUTCHours() : time.getHours(), // 小时
        'm+': ((utc ? time.getUTCMinutes() : time.getMinutes()) + '').padStart(2, '0'), // 分
        's+': utc ? time.getUTCSeconds() : time.getSeconds(), // 秒
        'q+': Math.floor(((utc ? time.getUTCMonth() : time.getMonth()) + 3) / 3), // 季度
        'S': utc ? time.getUTCMilliseconds() : time.getMilliseconds() // 毫秒
      }
      if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, ((utc ? time.getUTCFullYear() : time.getFullYear()) + '').substr(4 - RegExp.$1.length))
      for (const k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
      }
      return fmt
    }
    return null
  },
  // 常见的日期格式转换
  formatDateTime (data, utc = false) {
    return this.customTimeFormat(data, 'yyyy-MM-dd hh:mm:ss', utc)
  },
  // 计算时间戳之间的间隔时长， 返回结果的格式 【时：分：秒】
  formatDuration (startTime, endTime) {
    if (!endTime || !startTime) {
      return '-'
    } else {
      const diffVal = endTime - startTime
      const formatTime = function (num, level) {
        const units = [60 * 60 * 1000, 60 * 1000, 1000]
        const result = parseInt(num / units[level]) + ''
        const remainder = num % units[level]
        return result.padStart(2, 0) + (level < 2 ? (':' + formatTime(remainder, level + 1)) : '')
      }
      return diffVal < 1000 ? '00:00:00' : formatTime(diffVal, 0)
    }
  },
  // 日期字符串转日期对象
  getDateTime (timeString, timespan = false) {
    return new Date(timespan ? parseInt(timeString) : timeString.replace('-', '/'))
  },
  // 日期字符串转UTC日期对象
  getUTCDate (timeString) {
    const date = this.getDateTime(timeString)
    const utc = Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds())
    return new Date(utc)
  }
}
