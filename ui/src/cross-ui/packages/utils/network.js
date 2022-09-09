export default {
    init(offline, online) {
        window.addEventListener("offline", offline)
        window.addEventListener("online", online)
    },
    uninit(offline, online) {
        window.removeEventListener("offline", offline)
        window.removeEventListener("online", online)
    }
}

