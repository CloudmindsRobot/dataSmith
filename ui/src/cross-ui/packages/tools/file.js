import jschardet from 'jschardet'
import Papa from 'papaparse'
import html2canvas from 'html2canvas'
import jsPDF from 'jspdf'

const exportFile = (content, filename, type) => {
    const link = document.createElement('a')
    link.href = type ? type + encodeURIComponent(content) : content
    link.download = filename
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
}
const checkEncoding = (base64Str) => {
    // 这种方式得到的是一种二进制串
    const str = atob(base64Str.split(';base64,')[1])
    // console.log(str);
    // 要用二进制格式
    let encoding = jschardet.detect(str)
    encoding = encoding.encoding
    if (encoding === 'windows-1252') {
        encoding = 'ANSI'
    }
    return encoding
}

export default {
    import(file, success, failed) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader()
            reader.readAsDataURL(file)
            reader.onload = (evt) => {
                Papa.parse(file, {
                    encoding: checkEncoding(evt.target.result),
                    skipEmptyLines: true,
                    complete: success,
                    error: failed
                })
                resolve('success')
            }
            reader.onerror = (error) => {
                reject(error)
            }
        })
    },
    export(file, fileName, fileType) {
        exportFile(Papa.unparse(file), fileName, fileType)
    },
    download: exportFile,
    size(url, type, callback) {
        if (type.indexOf('image') !== -1) {
            const img = new Image()
            img.src = url
            img.onload = () => {
                callback(img.width, img.height)
            }
        } else if (type.indexOf('video') !== -1) {
            const video = document.createElement('video')
            video.src = url
            video.addEventListener('canplay', () => {
                callback(video.videoWidth, video.videoHeight)
            })
        } else if (type.indexOf('audio') !== -1) {
            const audio = document.createElement('audio')
            audio.src = url
            audio.addEventListener('canplay', () => {
                callback('', '')
            })
        }
    },
    validVideoFileType(file) {
        return file.type.indexOf('video') !== -1 && (file.type.indexOf('mp4') !== -1 || file.type.indexOf('webm') !== -1 || file.type.indexOf('ogg') !== -1)
    },
    validAudioFileType(file) {
        return file.type.indexOf('audio') !== -1 && (file.type.indexOf('mp3') !== -1 || file.type.indexOf('wav') !== -1 || file.type.indexOf('ogg') !== -1 || file.type.indexOf('mpeg') !== -1)
    },
    validImageFileType(file) {
        return file.type.indexOf('image') !== -1
    },
    validExcelFileType(file) {
        return ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet','application/vnd.ms-excel'].includes(file.type)
    },
    image2pdf(images, filename, outPut) {
        let contentHeight = 0
        images.forEach(img => {
            contentHeight +=  img.height
        });
        const pdfX = (images[0].width + 10) / 2 * 0.75
        const pdfY = (contentHeight + 10) / 2 * 0.75
        const pdf = new jsPDF('', 'pt', [pdfX, pdfY])
        images.forEach(img => {
            pdf.addImage(img.data, 'jpeg', 0, 0, img.width, img.height)
        })
        if (outPut) {
            return (pdf.output('arraybuffer', {filename}))
        }
        pdf.save(filename)
    },
    html2image(dom) {
        return new Promise((resolve, reject) => {
            html2canvas(dom, {
                allowTaint: true,
                useCORS: true,
                Scale: 2
            }).then(canvas => {
                resolve({
                    data: canvas.toDataURL('image/jpeg', 1.0), 
                    width: canvas.width, 
                    height: canvas.height
                })
            }).catch(error => {
                reject(error)
            })
        })
    }
}