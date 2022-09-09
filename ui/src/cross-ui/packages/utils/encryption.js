// 字符串转base64
export default {
    encode(str) {
        return str ? btoa(encodeURI(str)) : null
    },
    decode(base64) {
        return base64 ? decodeURI(atob(base64)) : null
    }
}