import $ from 'jquery'

const language = (navigator.browserLanguage || navigator.language).replace(/-[a-z]{2}$/, (word) => {
    return word.toUpperCase()
})

export default {
    language: language,
    isIE() {
       return  navigator.userAgent.indexOf('Trident') > -1
    },
    isOpera(){
        return navigator.userAgent.indexOf('Presto') > -1 //opera内核
    },
    isChrome(){
        return navigator.userAgent.indexOf('AppleWebKit') > -1
    },
    isFirefox() {
        return navigator.userAgent.indexOf('Gecko') > -1 && navigator.userAgent.indexOf('KHTML') === -1
    },
    isMobile(width = 768) {
        return !!navigator.userAgent.match(/AppleWebKit.*Mobile.*/) || $(window).width() <= width
    },
    isIos(){
        return !!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)
    },
    isAndroid(){
        return navigator.userAgent.indexOf('Android') > -1 || navigator.userAgent.indexOf('Linux') > -1
    },
    isIPhone() {
        return navigator.userAgent.indexOf('iPhone') > -1
    },
    isIPad() {
        return navigator.userAgent.indexOf('iPad') > -1
    },
    isSafari() {
        return navigator.userAgent.indexOf('Safari') === -1
    },
    isWeiXin() {
        return !!navigator.userAgent.match(/MicroMessenger/i)
    },
    isWeiBo() {
        return !!navigator.userAgent.match(/WeiBo/i)
    },
    isQQ() {
        return !!navigator.userAgent.match(/QQ/i)
    },
    inMobile () {
        return !!navigator.userAgent.match(/AppleWebKit.*Mobile.*/)
    }
}
