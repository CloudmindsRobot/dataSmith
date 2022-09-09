### mixin

> 组件间共用的mixins类
> 主要包括cardList(卡片与列表组件)，cardTable(卡片与表格组件)，tableList(表格与列表组件)，confirm(确认提示框)，modal/dialog(弹窗)，exportBtn(导出按钮)，empty(空数据)，formCreateor(表单生成)，logout(注销)，types(对utils下types在vue中使用封装),descriptions（详情视图）,userCenter(个人中心)等

```text
┌──cardList.js       
├──cardTable.js    
├──confirm.js    
├──descriptions.js    
├──dialog.js     
├──empty.js
├──exportBtn.js    
├──format.js 
├──formCreateor.js 
├──logout.js    
├──modal.js
├──tableList.js   
├──types.js          
└──userCenter.js  
```

### cardList.js

> 卡片与列表组件共用属性及方法

*代码*

```javascript
import infiniteScroll from 'vue-infinite-scroll'
import Device from '../tools/device'
import $ from 'jquery'

export default {
    directives: {infiniteScroll},
    props: {
        /**
         * 选择对象
         *
         * {all:全选状态,disabled:禁用状态,indeterminate:半选状态}
         */
        checked: {
            type: Object,
            default: () => {
                return {
                    all: false,
                    disabled: false,
                    indeterminate: false
                }
            }
        }
    },
    data() {
        return {
            isMobile: Device.isMobile()
        }
    },
    watch: {
        checked: {
            handler(newVal, oldVal) {
                if (newVal !== oldVal) {
                    return
                }
                !newVal.indeterminate && this.data.forEach(d => d.checked = newVal.all)
                this.data.forEach(d => d.disabled = newVal.disabled)
            },
            deep: true
        }
    },
    methods: {
        /**
         * 勾选改变
         *
         * @param {event} e 事件对象
         * @param {object} d 触发对象
         */
        onChange (e, d) {
            d.checked = e.target.checked
            /**
             * 勾选更改触发事件
             *
             * @property {boolean} arg 勾选状态
             */
            this.$emit('checked', e.target.checked)
        },
        /**
         * 勾选
         *
         * @param {object} c 勾选项
         */
        onSwitch (c) {
            /**
             * 勾选更改触发事件
             *
             * @property {object} c 勾选项
             */
            this.$emit('checked', c)
        },
        /**
         * 滚动加载完成
         *
         * @param {-}
         */
        handleInfiniteOnLoad() {
            /**
             * 滚动加载完成触发事件
             */
            this.$emit('infinite')
        },
        /**
         * 设置滚动加载区域高度
         *
         * @param {-}
         */
        setScrollHeight() {
            const height = $('body').height() - $(this.outer).offset().top - 10
            $('.scroll').addClass('.hide-scroll-bar').height(height)
        }
    },
    mounted() {
       Device.inMobile() && this.$nextTick(this.setScrollHeight)
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|onChange|e(event),d(Object)|-|改变d.checked状态值，并向父组件emit|
|onSwitch|c(String)|-|切换checked状态并向父组件emit|
|handleInfiniteOnLoad|-|-| 滚动加载时向父组件emit|
|setScrollHeight|-|-| 在移动设备模式下，设置滚动加载组件的高度|

### cardTable.js

> 卡片与表格组件共用属性及方法

*代码*

```javascript
export default {
    props: {
        /**
         * 数据视图
         *
         * [{prop:属性字段,locale:本地化函数,format:格式化函数,type:字段类型,color:颜色,click:点击事件}]
         */
        view: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 操作组
         *
         * [{icon:图标,key:键,locale:本地化函数,disabled:禁用函数,event:函数}]
         */
        actions: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 数据
         *
         * @values []
         */
        data: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 主键
         *
         * @values id
         */
        primaryKey: {
            type: String,
            default: 'id'
        },
        /**
         * 配置项
         *
         * {
         *  loading:加载中,
         *  expanded:展开,
         *  busy:滚动加载中,
         *  isList:列表模式,
         *  isScroll:滚动加载模式,
         *  xScroll:含有横向滚动
         * }
         */
        config: {
            type: Object,
            default: () => {
                return {
                    loading: false,
                    expanded: false,
                    busy: true,
                    isList: false,
                    isScroll: false,
                    xScroll: false
                }
            }
        },
        /**
         * 启用预览
         */
        preview: {
            type: Boolean,
            default: true
        }
    },
    methods: {
        /**
         * 操作(点击操作项时触发)
         *
         * @param {object} obj 操作项
         */
        onClick(obj) {
            const action = this.actions.find(a => a.key === obj.key)
            const data = {data: obj.item.value, key: obj.key}
            const bool = this.isFunction(action.disabled) ? !action.disabled(data.data) : !action.disabled
            action && bool && this.isFunction(action.event) ? action.event(data) : this.emit(data)
        },
        /**
         * 操作触发事件
         *
         * @param {object} data 操作项
         */
        emit (data) {
            /**
             * 操作触发事件
             *
             * @property {object} data 数据及操作键
             */
            this.$emit('action', data)
        }
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|onClick|obj(Object)|-|点击操作列表的按钮，触发相应事件并向父组件emit|

### confirm.js

> 确认提示框

*代码*

```javascript
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
```

*方法*

| 名称 | 参数 | 返回 | 说明 |
| :-------- | :------------------- | :----- | :--------------------------------------- |
| confirm | callback(Function),title(String),content(String),type(String) | - | 确认提示框，注册确认回调函数(callback),设置标题（title)，内容（content),确认按钮类型 |
| error | message(Object) | - | 错误提示 |

### descriptions.js

> 详情视图

*代码*

```javascript
export default {
    props: {
        /**
         * 数据视图
         *
         * [{prop:属性字段,locale:本地化函数,format:格式化函数,type:字段类型(link,tag,custom),color:颜色(配合type为tag时指定，类型string或function),click:点击事件}]
         */
        view: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 数据视图组
         */
        viewGroup: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 表单数据组[表单数据]
         */
        dataGroups: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 开启编辑模式
         *
         * @values true,false
         */
        editing: {
            type: Boolean,
            default: false
        },
        /**
         * 分列
         */
        columns: {
            type: Array,
            default: () => {
                return [1, 2, 3]
            }
        },
        /**
         * 尺寸
         * 
         * @values default,middle,small
         */
        size: {
            type: String,
            default: 'default'
        }
    }
}
```

### dialog.js

> 弹窗

*代码*

```javascript
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
        submit() {
            this.fApi.submit(formData => {
                if (!this.api) {
                    return
                }
                /**
                 * 允许父组件通过回调修改要提交的数据
                 */
                let changeDataBeforeSubmit;
                this.callback && (changeDataBeforeSubmit = this.callback(formData));
                if(changeDataBeforeSubmit!==undefined){
                    formData = changeDataBeforeSubmit
                }
                if(!formData) return
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
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|submit|-|-|表单提交|

### empty.js

> 空数据，处理没有数据时的组件显示

*代码*

```javascript
import {Empty} from 'ant-design-vue'
import Config from '../tools/config'

export default {
    data() {
        return {
            emptyImage: Empty.PRESENTED_IMAGE_SIMPLE,
            emptyText: Config.emptyText
        }
    }
}
```

### formCreateor.js

> 表单公共属性及方法

*代码*

```javascript
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
            Clean.clearUndefined(r)
            bool && this.oldRule.push(Clone.copy(r))
        })
        return {
            oldOption: Clone.copy(this.option),
            fApi: null,
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
            if(this.option.submitBtn){
                this.option.submitBtn.block = Device.isMobile() && layout === 'vertical'
            }
           if(this.option.resetBtn){
            this.option.resetBtn.block = Device.isMobile() && layout === 'vertical'
           }
            if (!this.option.form||layout === this.option.form.layout) {
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
         * @param {array} mlist 新子集规则
         * @param {array} olist 旧子集规则
         * @param {object} fieldNames 规则字段
         */
        childrenLocale(mlist, olist, fieldNames) {
            this.isArray(mlist) && mlist.forEach(m => {
                const n = olist.find(o => m[fieldNames.value] === o[fieldNames.value])
                n && m[fieldNames.label] && (m[fieldNames.label] = this.$t(n[fieldNames.label]))
                n && m[fieldNames.title] && (m[fieldNames.title] = this.$t(n[fieldNames.title]))
                n && m[fieldNames.children] && this.childrenLocale(m[fieldNames.children], n[fieldNames.children], fieldNames)
            })
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
            Object.keys(formRule).forEach(key => {
                const oldRule = this.oldRule.find(o => o.field === key)
                oldRule && this.ruleIterator(isLayout, formRule[key], oldRule)
            })
            this.fApi.updateRules(formRule)
        },
        /**
         * 更新表单提交与重置按钮规则
         *
         * @param {-}
         */
        updateFormBtn() {
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
         * @param {object} formData 表单项键值对
         */
        setFormValues(formData) {
            if (!this.fApi) {
                return
            }
            this.fApi.setValue(formData)
            this.formDataTimer && clearTimeout(this.formDataTimer)
            this.formDataTimer = setTimeout(() => {
                this.fApi.setValue(formData)
            })
        }
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|updateOptions|-|-|更新表单规则|
|updateRules|isLayout(Boolean)|-|更新表单组件规则,isLayout表明是否更新表单布局|
|updateFormBtn|-|-| 更新表单提交和重置按钮|
|updateLayout|-|-|更新表单布局同时更新表达组件规则|
|clearValidateStates|-|-|清除表单组件验证规则状态|
|setFormValues|formData(Object)|-|根据formData设置表单各项的值|
|ruleIterator|isLayout(Boolean), model(Object), oldRule(Object)|-|递归处理规则属性|
|childrenLocale|mlist(Array), olist(Array), fieldNames(Object)|-|递归处理options等内容的属性值|

#### 扩展阅读

[form-create](http://www.form-create.com/v2/guide/ "form-create")

### logout.js

> 注销逻辑

*代码*

```javascript
import Seting from '../tools/seting'
import Config from '../tools/config'
import empty from '../mixin/empty'

export default {
    mixins: [empty],
    data() {
        return {
            showBtn: true, //Seting.getSession() === Seting.get(Config.session)
            platform: Config.platform
        }
    },
    methods: {
        /**
         * 清理账户Cookie及其他信息(菜单项)
         *
         * @param {-}
         * @public
         */
        logout() {
            Seting.remove()
            window.localStorage.removeItem(Config.menu.name)
            window.location.replace(Config.loginUrl)
        },
        /**
         * 路由跳转
         *
         * @param {string} name 路由名称
         * @public
         */
        path(name) {
            this.$router && this.$router.push({name: name})
        }
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|logout|-|-|执行账号注销并跳转登录|
|path|name(String)|-|路由跳转|

### tableList.js

> 表格与列表组件共用属性和方法

*代码*

```javascript
import Config from '../tools/config'

export default {
    props: {
        /**
         * 翻页(为false则不显示)
         *
         * {current:页码,total:总数,pageSize:一页数量}
         */
        pagination: {
            type: [Object, Boolean],
            default: () => {
                return {
                    current: 1,
                    total: 0,
                    pageSize: Config.pageSize
                }
            }
        }
    },
    data () {
        return {
            paginationExt: {
                showQuickJumper: true,
                showSizeChanger: true,
                showLessItems: true,
                size: 'small',
                pageSizeOptions: Config.pageSizeOptions,
                showTotal: (total) => {
                    return this.$t('total') + ': ' + total
                }
            }
        }
    },
    mounted() {
        this.pagination && Object.assign(this.pagination, this.paginationExt)
    }
}
```

### types.js

> 对utils下types在vue中使用封装

*代码*

```javascript
import {isFunction, isString, isDefined, isNumber, isArray, isObject, isDate, isBoolean} from '../utils/types'

export default {
    methods: {
        isFunction,
        isString,
        isDefined,
        isNumber,
        isArray,
        isObject,
        isDate,
        isBoolean
    }
}
```

### format.js

> 处理数值类型字段

*代码*

```javascript
import {isNumber} from '../utils/types'
import Config from '../tools/config'
import DateTimeTranslate from '../tools/timer'

export default {
    methods: {
        /**
         * 处理数值类型
         *
         * @param {string|number} value 值
         * @returns {number|string}
         * @public
         */
        numberFormat(value) {
            return isNumber(value) ? value : (value || Config.emptyText)
        },
        /**
         * 格式化日期
         *
         * @param {date} date 日期
         * @returns {string}
         * @public
         */
        dateFormat(date) {
            return DateTimeTranslate.formatDateTime(date) || Config.emptyText
        },
        /**
         * 格式化时间
         *
         * @param {string} time 时间
         * @returns {string}
         * @public
         */
        timeFormat(time) {
            return time ?  DateTimeTranslate.formatDateTime(new Date(time)) : Config.emptyText
        }
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|numberFormat|value(String/Number)|-|字符格式化|
|dateFormat|date(String)|-|日期格式化|
|timeFormat|time(String)|-|时间格式化|

### exportBtn.js

> 导出组件配置

*代码*

```javascript
import Http from '../tools/http'

export default {
  data () {
    return {
    }
  },
  methods: {
    /**
     * 调用接口获取文档流
    */
    exportData (api, params, title) {
      if (!api) {
        return
      }
      this.confirmLoading = true
      Http.init().request({
        url: api,
        method: this.apiMethod,
        params: this.apiMethod === 'POST' ? null : params,
        data: this.apiMethod === 'POST' ? params : null,
        responseType: 'blob'
      }).then(response => {
        this.confirmLoading = false
        // Http.options.responseType = 'json'
        console.log('')
        this.downloadFile(response, title)
        this.visible = false
      }).catch(error => {
        // Http.options.responseType = 'json'
        this.confirmLoading = false
        console.error(error)
        this.$message.error(error)
        this.visible = false
      })
    },
    /**
     * 把文档流转成xls文件
    */
    downloadFile(data, title) {
      console.log('title', title)
      const urlTarget = window.URL || window.webkitURL // 后者是webkit内核  一般给手机端的
      const url = urlTarget.createObjectURL(new Blob([data]))
      const link = document.createElement('a')
      link.style.display = 'none'
      link.href = url
      link.setAttribute(
        'download',
        `${this.$t(title)}.xls`
      )
      document.body.appendChild(link)
      link.click()
      // 移除下载链接
      document.body.removeChild(link)
      this.clearValidateStates()
    }
  }
}
```

### modal.js

> 弹窗配置

*代码*

```javascript
export default {
    props: {
        /**
         * 弹窗配置项
         *
         * {title:标题,visible:显示',width:宽度,preview:含有图片预览,src:预览图URL,style:样式,zIndex:层级,destroyOnClose:关闭时销毁实例}
         */
        modal: {
            type: Object,
            default: () => {
                return {
                    title: '',
                    visible: false,
                    footer: null,
                    width: '50%',
                    preview: false,
                    src: '',
                    style: {},
                    mask: true,
                    zIndex: 1000,
                    destroyOnClose: false,
                    confirmLoading: false
                }
            }
        }
    },
    methods: {
        /**
         * 关闭弹窗
         *
         * @param {-}
         * @public
         */
        onCancel() {
            this.modal.visible = false
            /**
             * 关闭弹窗触发事件
             */
            this.$emit('cancel')
        },
        /**
         * 点击确定
         *
         * @param {-}
         * @public
         */
         onOk() {
            /**
             * 点击确定触发事件
            */
            this.$emit('ok')
        }
    }
}
```

### userCenter.js

> 用户中心

*代码*

```javascript
import Config from '../tools/config'
import Seting from '../tools/seting'

export default{
    props: {
        /**
         * 用户API
         *
         * {load:加载租户信息,update:更新租户信息,remove:移除商标,changePassword:修改密码}
         */
        api: {
            type: Object,
            default: () => {
                return {
                    load: '/upms/v1/tenant/list',
                    update: '/upms/v1/tenant/update',
                    remove: '/ceph/v1/delete',
                    changePassword: '/upms/v1/staff/update_my_pwd'
                }
            }
        },
        /**
         * 用户
         *
         */
        user: {
            type: Object,
            default: () => {
                return Seting.get(Config.user) || {}
            }
        }
    },
    data () {
        return {
            version: ''
        }
    },
    created() {
        this._http({
            url: '/common/version.json',
            method: 'GET'
        }, undefined, false).then(versionObj => {
            Config.version.cross = Config.version.smart = versionObj.crss_version
            Config.version.boss = versionObj.boss_version
            this.version = Config.version[Config.platform]
        }).catch(() => {
            this.version = Config.version[Config.platform]
        })
    }
}
```
