export default {
    methods: {
        /**
         * 确认提示
         *
         * @param {function} callback 确认回调
         * @param {string} title 标题
         * @param {string} content 内容
         * @param {string} type 类别(primary,danger)
         */
        confirm(callback, title = 'deleteConfirm', content = 'deleteDes', type = 'danger') {
            this.$confirm({
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
                },
            })
        },
        /**
         * 错误提示
         * 
         * @param {object} message 错误信息
         */
        error(message) {
            const code = message.subCode || message.code
            const config = this.$tools ? this.$tools.config : this.$config
            this.$message.error(`${this.$t(code ? `error.${code}` : (message.status ? config.ERROR : config.commonError))}`)
        }
    }
}