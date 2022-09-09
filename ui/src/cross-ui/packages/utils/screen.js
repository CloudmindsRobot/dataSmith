import $ from 'jquery'

export default {
    launch () {
        if (window.ActiveXObject) {
            const WsShell = new window.ActiveXObject('WScript.Shell')
            WsShell.SendKeys('{F11}')
        } else if (document.documentElement.requestFullscreen) {
            document.documentElement.requestFullscreen()
        } else if (document.documentElement.mozRequestFullScreen) {
            document.documentElement.mozRequestFullScreen()
        } else if (document.documentElement.msRequestFullscreen) {
            document.documentElement.msRequestFullscreen()
        } else if (document.documentElement.webkitRequestFullscreen) {
            document.documentElement.webkitRequestFullscreen()
        }
    },
    exit () {
        if (!this.isFullScreen()) {
            return
        }
        if (window.ActiveXObject) {
            const WsShell = new window.ActiveXObject('WScript.Shell')
            WsShell.SendKeys('{F11}')
        } else if (document.cancelFullScreen) {
            document.cancelFullScreen()
        } else if (document.exitFullscreen) {
            document.exitFullscreen()
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
            document.msExitFullscreen()
        } else if (document.webkitExitFullscreen) {
            document.webkitExitFullscreen()
        }
    },
    isFullScreen () {
        return document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement
    },
    // 监听全屏状态改变后的回调
    listen (callback, flag, namespace) {
        const events = 'fullscreenchange webkitfullscreenchange mozfullscreenchange MSFullscreenChange resize'
        $(window).on(namespace ? events.replace(/\s/g, '.' + namespace + ' ') : events, () => {
            const fullscreenElement = document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement
            if (!fullscreenElement || flag) {
                callback()
            }
        })
    },
    removeListen (namespace) {
        $(window).off('.' + namespace)
    }
}