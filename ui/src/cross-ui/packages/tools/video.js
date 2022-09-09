import {isFunction} from '../utils/types'

export default {
    drawFrame(data, scale = 0.5, callback) {
        const video = document.createElement('video')
        video.src = data.url
        video.addEventListener('canplay', () => {
            const canvas = document.createElement('canvas')
            canvas.width = video.videoWidth * scale
            canvas.height = video.videoHeight * scale
            canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height)
            const image = canvas.toDataURL("image/png")
            data.thumb && (data.thumb.backgroundImage = `url(${image})`)
            isFunction(callback) && callback(image)
        })
        video.addEventListener('error', () => {
            isFunction(callback) && callback()
        })
    },
    canplay(url, callback) {
        const video = document.createElement('video')
        video.src = url
        video.addEventListener('canplay', () => {
            isFunction(callback) && callback('yes')
        })
        video.addEventListener('error', () => {
            isFunction(callback) && callback()
        })
    }
}