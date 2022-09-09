export default {
    clearUndefined(obj) {
        Object.keys(obj).forEach(k => {
            obj[k] === undefined && delete obj[k]
            obj[k] && typeof obj[k] === 'object' && this.clearUndefined(obj[k])
        })
    },
    clearAll(obj) {
        Object.keys(obj).forEach(k => obj[k] = null)
    }
}