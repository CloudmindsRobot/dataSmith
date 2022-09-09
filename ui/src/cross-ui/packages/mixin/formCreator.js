import Device from '../tools/device'
import Form from '../tools/form'
import Clean from '../utils/clean'
import Clone from '../utils/clone'
import types from './types'
import Config from '../tools/config'
import Seting from '../tools/seting'

export default {
    mixins: [types],
    props: {
        /**
         * 表单数据
         *
         * {model:数据键值对}
         */
        dataModel: {
            type: Object,
            default: () => {
                return {model: {}}
            }
        },
        /**
         * 表单配置选项
         */
        option: {
            type: Object,
            default: () => {
                return Form.options.default()
            }
        },
        /**
         * 表单项规则
         */
        rule: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 表单项规则
         */
        oldRule: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 表单按钮规则
         */
        buttons: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 布局特殊处理
         *
         * @values true,false
         */
        special: {
            type: Boolean,
            default: false
        },
        /**
         * 改变布局
         *
         * @values true,false
         */
        changeLayout: {
            type: Boolean,
            default: true
        }
    },
    data() {
        Clean.clearUndefined(this.option)
        const bool = !this.oldRule.length
        this.rule.forEach(r => {
            try {
                Clean.clearUndefined(r)
                bool && this.oldRule.push(Clone.copy(r))
            } catch {
            }
        })
        return {
            oldOption: Clone.copy(this.option),
            fApi: null,
            // ruleTimer: null,
            formDataTimer: null
        }
    },
    watch: {
        '$i18n.locale'(newVal, oldVal) {
            (newVal !== oldVal) && this.updateRules()
        }
    },
    methods: {
        /**
         * 更新表单配置项
         *
         * @param {-}
         */
        updateOptions(bool) {
            if (!this.changeLayout) {
                return
            }
            const layout = Device.isMobile() ? 'vertical' : (this.oldOption && this.oldOption.form ? this.oldOption.form.layout : 'vertical')
            if(this.option.submitBtn) {
                this.option.submitBtn.block = Device.isMobile() && layout === 'vertical'
            }
            if(this.option.resetBtn) {
                this.option.resetBtn.block = Device.isMobile() && layout === 'vertical'
            }
            if (!this.option.form || layout === this.option.form.layout) {
                return
            }
            if (bool && this.special) {
                this.option.form.layout = 'inline'
                this.formDataTimer && clearTimeout(this.formDataTimer)
                this.formDataTimer = setTimeout(() => {
                    this.option.form.layout = layout
                    this.option.submitBtn.block = this.option.resetBtn.block = layout === 'vertical'
                })
                return
            }
            this.option.form.layout = layout
        },
        /**
         * 迭代更新表项规则
         *
         * @param {boolean} isLayout 布局规则
         * @param {object} model 新规则
         * @param {object} oldRule 旧规则
         */
        ruleIterator(isLayout, model, oldRule) {
            if (!oldRule) {
                return
            }
            if (isLayout) {
                model.wrap && (model.wrap = Device.isMobile() ? this.option.wrap : oldRule.wrap)
            } else {
                const names = {label: 'label', title: oldRule.props && oldRule.props.replaceFields ? oldRule.props.replaceFields.title : 'title', value: 'value', children: 'children'}
                const fieldNames = oldRule.props ? (oldRule.props.fieldNames || oldRule.props.replaceFields || names) : names
                this.isObject(model.title) ? this.ruleIterator(isLayout, model.title, oldRule.title) : model.title = this.$t(oldRule.title)
                this.isObject(model.prefix) ? this.ruleIterator(isLayout, model.prefix, oldRule.prefix) : model.prefix = this.$t(oldRule.prefix)
                this.isObject(model.suffix) ? this.ruleIterator(isLayout, model.suffix, oldRule.suffix) : model.suffix = this.$t(oldRule.suffix)
                this.isObject(model.info) ? model.info.info = this.$t(oldRule.info.info) : model.info = this.$t(oldRule.info)
                this.isArray(model.children) && (model.children = model.children.map((c, i) => {
                    this.isObject(c) ? this.ruleIterator(isLayout, c, oldRule.children ? oldRule.children[i] : JSON.parse(JSON.stringify(c))) : (c = this.$t(oldRule.children[i]))
                    return c
                }))
                model.options && this.childrenLocale(model.options, oldRule.options, fieldNames)
                model.treeData && this.childrenLocale(model.treeData, oldRule.treeData, fieldNames)
                model.effect && model.effect.fetch && (model.effect.fetch.headers = {Authorization: Seting.get(Config.tokenKey)})
                if (model.props) {
                    model.props.title && (model.props.title = this.$t(oldRule.props.title))
                    model.props.placeholder && (model.props.placeholder = this.$t(oldRule.props.placeholder))
                    model.props.notFoundContent && (model.props.notFoundContent = this.$t(oldRule.props.notFoundContent))
                    model.props.searchPlaceholder && (model.props.searchPlaceholder = this.$t(oldRule.props.searchPlaceholder))
                    model.props.unCheckedChildren && (model.props.unCheckedChildren = this.$t(oldRule.props.unCheckedChildren))
                    model.props.checkedChildren && (model.props.checkedChildren = this.$t(oldRule.props.checkedChildren))
                    model.props.addonBefore && (model.props.addonBefore = this.$t(oldRule.props.addonBefore))
                    model.props.addonAfter && (model.props.addonAfter = this.$t(oldRule.props.addonAfter))
                    model.props.options && this.childrenLocale(model.props.options, oldRule.props.options, fieldNames)
                    model.props.treeData && this.childrenLocale(model.props.treeData, oldRule.props.treeData, fieldNames)
                    this.isArray(model.props.rule) && model.props.rule.filter(r => r.field).forEach((r, index) => {
                        this.ruleIterator(isLayout, r, oldRule.props.rule[index])
                    })
                }
                this.isArray(model.validate) && model.validate.forEach((v, i) => {
                    v.message && (v.message = this.$t(oldRule.validate && oldRule.validate[i] ? oldRule.validate[i].message : ''))
                })
                this.isArray(model.control) && model.control.forEach((ctl, i) => {
                    ctl.rule.forEach((r, index) => this.isObject(r) && this.ruleIterator(isLayout, r, oldRule.control[i].rule[index]))
                })
            }
        },
        /**
         * 迭代更新表单项子集规则
         *
         * @param {array,object} mlist 新子集规则
         * @param {array,object} olist 旧子集规则
         * @param {object} fieldNames 规则字段
         */
        childrenLocale(mlist, olist, fieldNames) {
            this.isArray(mlist) && mlist.forEach(m => {
                const n = olist.find(o => m[fieldNames.value] === o[fieldNames.value])
                n && m[fieldNames.label] && (m[fieldNames.label] = this.$t(n[fieldNames.label]))
                n && m[fieldNames.title] && (m[fieldNames.title] = this.$t(n[fieldNames.title]))
                n && m[fieldNames.children] && this.childrenLocale(m[fieldNames.children], n[fieldNames.children], fieldNames)
            })
            this.isObject(mlist) && mlist.placeholder && (mlist.placeholder = this.$t(olist.placeholder))
        },
        /**
         * 更新表单项规则
         *
         * @param {boolean} isLayout 布局规则
         */
        updateRules(isLayout) {
            this.updateFormBtn()
            if (!this.fApi || isLayout) {
                return
            }
            const formRule = this.fApi.model()
            // const effect = this.rule.find(r => (r.effect && r.effect.fetch.action) || r.type === 'group')
            // if (!effect) {
            Object.keys(formRule).forEach(key => {
                const oldRule = this.oldRule.find(o => o.field === key)
                oldRule && this.ruleIterator(isLayout, formRule[key], oldRule)
            })
            // this.rule.forEach((r, index) => this.ruleIterator(isLayout, r, this.oldRule[index]))
            // return
            // }
            this.fApi.updateRules(formRule)
            // const rule = this.rule.concat()
            // this.rule.length = 0
            // rule.forEach(r => this.rule.push({...r}))
            // this.ruleTimer && clearTimeout(this.ruleTimer)
            // this.ruleTimer = setTimeout(() => {
            //     this.rule.length = 0
            //     rule.forEach((r, index) => {
            //         this.ruleIterator(isLayout, r, this.oldRule[index])
            //         this.rule.push(r)
            //     })
            // })
        },
        /**
         * 更新表单提交与重置按钮规则
         *
         * @param {-}
         */
        updateFormBtn() {
            // const form = Form.options.default()
            const form = this.option
            this.option.submitBtn && (this.option.submitBtn.innerText = this.$t(form.submitBtn.innerText))
            this.option.resetBtn && (this.option.resetBtn.innerText = this.$t(form.resetBtn.innerText))
        },
        /**
         * 更新表单布局
         *
         * @param {-}
         */
        updateLayout() {
            this.updateOptions(true)
            this.updateRules(true)
        },
        /**
         * 清除表单项已验证状态
         *
         * @param {-}
         */
        clearValidateStates() {
            if (!this.fApi) {
                return
            }
            this.fApi.resetFields()
            this.fApi.clearValidateState()
        },
        /**
         * 设置表单项值
         *
         * @param {object} dataModel 表单项键值对
         */
        setFormValues(dataModel) {
            if (!this.fApi) {
                return
            }
            const formData = this.fApi.formData()
            Object.assign(formData, dataModel)
            this.fApi.setValue(formData)
            this.formDataTimer && clearTimeout(this.formDataTimer)
            this.formDataTimer = setTimeout(() => {
                this.fApi.setValue(formData)
            })
        }
    }
}