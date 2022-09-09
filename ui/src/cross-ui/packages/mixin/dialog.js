export default {
    props: {
        /**
         * 含有图标
         *
         * {show:显示}
         */
        hasIcon: {
            type: Object,
            default: () => {
                return {
                    show: false
                }
            }
        },
        /**
         * API
         */
        api: {
            type: String,
            default: ''
        },
        /**
         * 额外的参数
         */
        extraParam: {
            type: Object,
            default: () => {
                return {}
            }
        },
        /**
         * API的请求方式
         *
         * @values: POST,GET
         */
        apiMethod: {
            type: String,
            default: 'POST'
        },
        /**
         * 回调函数
         */
        callback: Function
    },
    methods: {
        /**
         * 关闭弹窗
         *
         * @param {-}
         */
        cancel() {
            this.modal.visible = false
            /**
             * 清除表单项已验证状态
             */
            this.clearValidateStates()
            /**
             * 关闭弹窗触发事件
             */
            this.$emit('cancel')
        },
        /**
         * 点击确定
         *
         * @param {-}
         */
        ok() {
            this.fApi.validate(valid => {
                if (!valid) {
                    /**
                     * 表单校验未通过触发事件
                     */
                    this.$emit('validateFailed')
                    return
                } 
                this.submit()
            })
        },
        /**
         * 提交表单
         *
         * @param {-}
         */
        submit() {
            this.fApi.submit(formData => {
                /**
                 * 允许父组件通过回调修改要提交的数据
                 */
                let changeDataBeforeSubmit;
                this.callback && (changeDataBeforeSubmit = this.callback(formData));
                if(changeDataBeforeSubmit!==undefined){
                    formData = changeDataBeforeSubmit
                }
                if(!formData || !this.api) {
                    return
                }
                /** end */
                this.modal.confirmLoading = true
                this._http({
                    url: this.api,
                    method: this.apiMethod || 'POST',
                    data: formData,
                    params: null
                }).then(response => {
                    this.modal.confirmLoading = false
                    if (response.code !== 0) {
                        return
                    }
                    /**
                     * 表单提交完成事件
                     *
                     * @property {object} formData 表单数据键值对
                    */
                    this.$emit('complete', formData)
                    /**
                     * 数据返回完成事件
                     *
                     * @property {object || blob} response 后端返回的数据
                    */
                    this.$emit('dataComplete', response)
                    /**
                     * 清除表单项已验证状态
                     *
                    */
                    this.modal.visible = false
                    this.fApi.clearValidateState()
                    this.fApi.resetFields()
                }).catch(() => {
                    this.modal.confirmLoading = false
                })
            })
        }
    }
}