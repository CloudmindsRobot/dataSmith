import types from '../cross-ui/packages/mixin/types'

/**
 * 数据表格
 */
export default {
    mixins: [types],
    data() {
        return {
            buttons: [{
                key: 'new',
                title: 'new',
                btnConfig: {
                    type: 'primary'
                },
                action: () => {
                    const data = {}
                    this.formRule.forEach(r => {
                        data[r.field] = undefined
                    })
                    this.data.model = data
                    this.formConfig.title = this.formTitle.new
                    this.switchVisible()
                }
            }],
            config: {
                showMode: false,
                mode: 'table',
                props: {
                    actions: [{
                        key: 'edit',
                        icon: 'icon-edit',
                        event: (row) => {
                            this.showEdit(row.data)
                        }
                    }, {
                        key: 'delete',
                        icon: 'icon-delete',
                        event: (row) => {
                            this.currentRow = row.data
                            this._confirm(this.delete)
                        }
                    }]
                }
            },
            data: {
                model: {}
            },
            api: {
                url: '',
                method: 'GET',
                params: {}
            },
            formConfig: {
                visible: false,
                height: 0,
                title: '',
                confirmLoading: false,
                submit: 'save'
            },
            currentRow: null,
            delApi: '',
            modal: {
                visible: false,
                mask: false,
                title: 'details',
                buttons: [{
                    label: 'edit',
                    click: this.showEdit
                }]
            },
            columns: [1, 1, 1],
            formTitle: {
                new: '',
                edit: ''
            }
        }
    },
    methods: {
        /**
         * 时间戳转为字符格式
         *
         * @param value 时间戳
         * @returns {*}
         */
        toLocalDateTime(value) {
            return value ? this.$tools.timer.formatDateTime(this.$tools.timer.getDateTime(value, true)) : this.$tools.config.emptyText
        },
        /**
         * 时间字符格式转时间戳
         *
         * @param value 时间字符格式
         * @returns {number|undefined}
         */
        toTimespan(value) {
            return value ? this.$tools.timer.getDateTime(value).getTime() : undefined
        },
        /**
         * 设置数据过滤参数
         *
         * @param formData 搜索栏提交数据
         */
        searchHandle(formData) {
            const data = {...formData}
            Object.keys(data).forEach(k => {
                this.isArray(data[k]) && (data[k] = undefined)
            })
            this.api.params = { current: 1, ...data }
        },
        /**
         * 数据处理完成
         *
         * @param formData 提交数据
         */
        onComplete(formData) {
            this.showSuccess()
            formData.id ? Object.assign(this.currentRow, formData) : this.searchHandle()
        },
        /**
         * 删除
         *
         * @param flag 开启api调用
         */
        delete (flag) {
            flag && this._http({
                url: this.delApi,
                method: 'POST',
                params: {id: this.currentRow.id}
            }, undefined, false).then(res => {
                if(res.code !== 0) {
                    this.$message.error(res.message)
                    return
                }
                this.showSuccess()
                this.searchHandle()
            }).catch(err => {
                this.$message.error(this.$t(err ? `error.${err.status}` : this.$tools.config.ERROR))
            })
        },
        /**
         * 拷贝
         *
         * @param source 拷贝源对象
         * @returns {any}
         */
        clone(source) {
            return JSON.parse(JSON.stringify(source))
        },
        /**
         * 显示成功提示
         */
        showSuccess() {
            this.$message.success(this.$t('operateSuccess'))
        },
        /**
         * 更新当前行
         *
         * @param data 行数据
         */
        setCurrentRow (data) {
            this.currentRow = data
        },
        /**
         * 显示详情
         *
         * @param data 详情数据
         */
        showDetails (data) {
            this.data.model = this.clone(data)
            this.setCurrentRow(data)
            this.modal.visible = true
        },
        /**
         * 切换显示
         */
        switchVisible() {
            this.formConfig.visible = true
            this.modal.visible = false
        },
        /**
         * 设置表单配置信息
         */
        setFormConfig () {
            this.formConfig.title = this.formTitle.edit
            this.switchVisible()
        }
    },
    mounted() {
        this.formConfig.height = $('body').height() - 202
    }
}
