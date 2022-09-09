import Config from './config'
import http from './http'

const _http = http.init()

export const httpCatch = async function(option, formData, isCatch = true) {
    const result = await _http.request(option, formData).catch(err => {
        return err
    })
    if (isCatch && (result.status || result.code !== 0)) {
        const code = result.subCode || result.code
        this.$message && this.$message.error(this.$t(code ? `error.${code}` : (result.status ? Config.ERROR : Config.commonError)))
    }
    return result
}

export const confirm = async function(callback, title = 'deleteConfirm', content = 'deleteDes', type = 'danger') {
    const result = await new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve({status: 0})
        })
    })
    result.status === 0 && this.$confirm({
        title: this.$t(title),
        content: this.$t(content),
        centered: true,
        okType: type,
        okText: this.$t('ok'),
        cancelText: this.$t('cancel'),
        onOk: () => {
            callback && callback(true)
        },
        onCancel() {
            callback && callback(false)
        }
    })
    return result
}