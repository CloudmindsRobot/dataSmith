### tools

> 提供内部组件以及开放给第三方使用的工具类。
> 主要包括config(配置)，device(设备端判断)，file(文件处理)，form(表单及表单组件)，http(axios封装)，httpCatch(封装http异常处理)，i18n(vue-i18n封装)，locale(语言环境)，router（vue-router封装），seting(Cookie封装)，storage(本地存储),theme（组件主题），timer(日期格式化)，treeData(树型数据处理)，ws(websocket)，utils(综合方法)等

```text
┌──config.js       
├──device.js    
├──file.js    
├──form.js  
├──http.js 
├──httpCatch.js     
├──i18n.js  
├──index.js         * 对外API
├──locale.js   
├──router.js   
├──seting.js
├──storage.js   
├──theme.js   
├──timer.js   
├──treeData.js      * 配置为树形结构数据，节点插槽及筛选
├──user.js          * 登录账户信息类   
├──ws.js
└──utils.js        
```
### index.js

> 对外接口(API)

### config.js

> 配置项，包括组件及应用程序的基本配置信息

*代码*

```javascript
// 配置信息类
export default {
    cookieExpires: 1, // cookie过期时间
    pageSize: 12, // 翻页数量
    pageSizeOptions: ['12', '24', '48', '96'], // 一页数量
    millseconds: 86400000, // 毫米换算单位（天）
    ERROR: 'serverError', // 统一的调用服务错误标识（本地化标签）
    baseUrl: process.env.VUE_APP_BASE_API,// styleguide:build,
    baseUrlPrefix: {
        datasmith: '/bossapi/cade/v1/',
    },
    timeout: 30000, // 服务接口调用超时毫秒,
    language: 'language', //language(语言)key
    theme: 'theme',  //theme(主题)key
    seting: { // cookie
        datasmith: 'datasmith_setting'
    },
    socketUrl: `wss://${window.location.host}/ws`, //'wss://cross-fit86.harix.iamidata.com/ws',
    socketHeader: {
        login: 'root',
        passcode: 'crss123456',
        host: 'vhost_frontend'
    },
    commonError: 'commonError', //统一的处理失败错误提示
    systemId: 1, //系统ID
    emitPrefix: 'datasmith',
    onceSize: 10000, // 一次请求数量
    user: 'user', // cookie的user(登录用户)key
    menu: { // 菜单
        openKeys: [], // 展开菜单的key
        selectedKeys: [], //选中菜单的key
        name: 'datasmith_menu',
        props: {  // 菜单源属性配置
            key: 'id',
            parent: 'parentId',
            icon: 'icon',
            route: 'component',
            children: 'children',
            master: 'master'
        }
    },
    languages: [{label: '中文(简体)', code: 'zh-CN'}],
    helpUrl: '', // 帮助文档路径
    remember:{
        datasmith: 'datasmith_remember'
    }, // cookie的记住密码key
    version: { // 版本号
        boss: '1.8.0.1'
    },
    systems: {
        datasmith: ['datasmith']
    }, // 系统编码
    emptyText: '-', // 空值显示字符
    home: '/#/master/home',
    invalid: '/#/401',
    page403: '/#/403',
    loginUrl: '/#/login',
    platform: 'datasmith', // 平台
    pageCode: 'pageCode',
    tokenKey: 'token',
    session: 'sessionId',
    requestFormData: false,
    systemLogoKey:'systemLogo',
    css: 'light' // 主题
}
```

### device.js

> 设备端判断

*代码*

```javascript
import $ from 'jquery'

const language = (navigator.browserLanguage || navigator.language).replace(/-[a-z]{2}$/, (word) => {
    return word.toUpperCase()
})

export default {
    language: language,
    isIE() {
       return  navigator.userAgent.indexOf('Trident') > -1
    },
    isOpera(){
        return navigator.userAgent.indexOf('Presto') > -1 //opera内核
    },
    isChrome(){
        return navigator.userAgent.indexOf('AppleWebKit') > -1
    },
    isFirefox() {
        return navigator.userAgent.indexOf('Gecko') > -1 && navigator.userAgent.indexOf('KHTML') === -1
    },
    isMobile(width = 768) {
        return !!navigator.userAgent.match(/AppleWebKit.*Mobile.*/) || $(window).width() <= width
    },
    isIos(){
        return !!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)
    },
    isAndroid(){
        return navigator.userAgent.indexOf('Android') > -1 || navigator.userAgent.indexOf('Linux') > -1
    },
    isIPhone() {
        return navigator.userAgent.indexOf('iPhone') > -1
    },
    isIPad() {
        return navigator.userAgent.indexOf('iPad') > -1
    },
    isSafari() {
        return navigator.userAgent.indexOf('Safari') === -1
    },
    isWeiXin() {
        return !!navigator.userAgent.match(/MicroMessenger/i)
    },
    isWeiBo() {
        return !!navigator.userAgent.match(/WeiBo/i)
    },
    isQQ() {
        return !!navigator.userAgent.match(/QQ/i)
    },
    inMobile () {
        return !!navigator.userAgent.match(/AppleWebKit.*Mobile.*/)
    }
}
```

*属性*

|名称|说明|
|:-|:-|
|language|当前浏览器地域环境编码(如：zh-CN，en-US,...)|

*方法*

|名称| 参数|返回|说明|
|:-|:-|:-|:-|
|isIE|-|Boolean|是否为IE浏览器|
|isOpera|-|Boolean|是否为Opera浏览器|
|isChrome|-|Boolean|是否为Chrome浏览器|
|isFirefox|-|Boolean|是否为Firefox浏览器|
|isMobile|width(Number)|Boolean|是否为移动端或屏幕宽度最大为width|
|isIos|-|Boolean|是否为Ios设备|
|isAndroid|-|Boolean|是否为Android设备-|
|isIPhone|-|Boolean|是否为iPhone手机-|
|isIPad|-| Boolean|是否为iPad平板|
|isSafari|-|Boolean|是否为Safari浏览器|
|isWeiBo|-|Boolean|是否为WeiBo浏览器|
|isWeiXin|-|Boolean|是否为WeiXin浏览器|
|isQQ|-|Boolean|是否为QQ浏览器|
|inMobile|-|Boolean|是否在移动设备端|

### file.js

> 文件处理类，提供文件导入导出等方法

*代码*

```javascript
import jschardet from 'jschardet'
import Papa from 'papaparse'

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
const fileToBase64 = (file) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    return new Promise((resolve, reject) => {
        reader.onload = (evt) => {
            resolve(evt.target.result)
        }
        reader.onerror = (error) => {
            reject(error)
        }
    })
}

export default {
    import(file, success, failed) {
        fileToBase64(file).then(result => {
            const encoding = checkEncoding(result)
            Papa.parse(file, {
                encoding: encoding,
                skipEmptyLines: true,
                complete: success,
                error: failed
            })
        }).catch(e => {
            console.log(e)
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
        }
    },
    validVideoFileType(file) {
        return file.type.indexOf('video') !== -1 && (file.type.indexOf('mp4') !== -1 || file.type.indexOf('webm') !== -1 || file.type.indexOf('ogg') !== -1)
    },
    validAudioFileType(file) {
        return file.type.indexOf('audio') !== -1 && (file.type.indexOf('mp3') !== -1 || file.type.indexOf('wav') !== -1 || file.type.indexOf('ogg') !== -1)
    },
    validImageFileType(file) {
        return file.type.indexOf('image') !== -1
    },
    validExcelFileType(file) {
        return ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet','application/vnd.ms-excel'].includes(file.type)
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|import|file(File),success(Function),failed(Function)|-|导入文件，支持注册导入成功和失败时的回调函数(success/failed)|
|export|file(File),fileName(String),fileType(String)|-|导出文件，可设置导出文件名及文件类型|
|download|content(String),fileName(String),type(String)|-|将content导出为文件，可设置导出文件名及文件类型|
|size|url(String),type(String),callback(Function)|-|根据文件url获取文件的尺寸,支持注册获取回调函数(callback)|

### form.js

> 表单及表单组件
> 
*代码*

```javascript
import Config from './config'
import Seting from './seting'
import i18next from 'i18next'
import Locale from './locale'
import Device from './device'

const _defaultConfig = {
    btn: {
        loading: false,
        shape: undefined,
        block: false,
        show: true,
        col: undefined,
        click: undefined,
        type: 'primary'
    },
    fetch: {
        action: '',
        to: 'options',
        parse: undefined,
        method: 'GET',
        data: undefined,
        dataType: 'json',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Authorization': Seting.get(Config.tokenKey),
            'sysId': Config.systemId
        },
        //接口请求失败回调
        onError: undefined
    },
    rule: {
        title: '',
        field: '',
        value: undefined,
        validate: undefined,
        wrap: undefined,
        emit: undefined,
        emitPrefix: Config.emitPrefix,
        col: undefined,
        children: undefined
    },
    props: {
        size: 'default',
        disabled: false
    },
    on: {
        change: undefined
    },
    hkLang: {
        wangEditor: {
            插入: '插入',
            默认: '默認',
            创建: '創建',
            修改: '修改',
            如: '如',
            请输入正文: '請輸入正文',
            menus: {
                title: {
                    标题: '標題',
                    加粗: '加粗',
                    字号: '字號',
                    字体: '字體',
                    斜体: '斜體',
                    下划线: '下劃線',
                    删除线: '刪除線',
                    缩进: '縮進',
                    行高: '行高',
                    文字颜色: '文字顏色',
                    背景色: '背景色',
                    链接: '鏈接',
                    序列: '序列',
                    对齐: '對齊',
                    引用: '引用',
                    表情: '表情',
                    图片: '圖片',
                    视频: '視頻',
                    表格: '表格',
                    代码: '代碼',
                    分割线: '分割線',
                    恢复: '恢復',
                    撤销: '撤銷',
                    全屏: '全屏',
                    待办事项: '待辦事項',
                },
                dropListMenu: {
                    设置标题: '設置標題',
                    背景颜色: '背景顏色',
                    文字颜色: '文字顏色',
                    设置字号: '設置字號',
                    设置字体: '設置字體',
                    设置缩进: '設置縮進',
                    对齐方式: '對齊方式',
                    设置行高: '設置行高',
                    序列: '序列',
                    head: {
                        正文: '正文',
                    },
                    indent: {
                        增加缩进: '增加縮進',
                        减少缩进: '減少縮進',
                    },
                    justify: {
                        靠左: '靠左',
                        居中: '居中',
                        靠右: '靠右',
                        两端: '兩端',
                    },
                    list: {
                        无序列表: '無序列表',
                        有序列表: '有序列表',
                    },
                },
                panelMenus: {
                    emoticon: {
                        默认: '默認',
                        新浪: '新浪',
                        表情: '表情',
                        手势: '手勢',
                    },
                    image: {
                        图片链接: '圖片鏈接',
                        上传图片: '上傳圖片',
                        网络图片: '網絡圖片',
                        图片地址: '圖片地址',
                        图片文字说明: '圖片文字說明',
                        跳转链接: '跳轉鏈接',
                    },
                    link: {
                        链接: '鏈接',
                        链接文字: '鏈接文字',
                        删除链接: '刪除鏈接',
                        查看链接: '查看鏈接',
                    },
                    video: {
                        插入视频: '插入視頻',
                    },
                    table: {
                        行: '行',
                        列: '列',
                        的: ' ',
                        表格: '表格',
                        添加行: '添加行',
                        删除行: '删除行',
                        添加列: '添加列',
                        删除列: '删除列',
                        设置表头: '設置表頭',
                        取消表头: '取消表頭',
                        插入表格: '插入表格',
                        删除表格: '删除表格',
                    },
                    code: {
                        删除代码: '刪除代碼',
                        修改代码: '修改代碼',
                        插入代码: '插入代碼',
                    },
                },
            },
            validate: {
                张图片: '張圖片',
                大于: '大於',
                图片链接: '圖片鏈接',
                不是图片: '不是圖片',
                返回结果: '返回結果',
                上传图片超时: '上傳圖片超時',
                上传图片错误: '上傳圖片錯誤',
                上传图片失败: '上傳圖片失敗',
                插入图片错误: '插入圖片錯誤',
                一次最多上传: '一次最多上傳',
                下载链接失败: '下載鏈接失敗',
                图片验证未通过: '圖片驗證未通過',
                服务器返回状态: '服務器返回狀態',
                上传图片返回结果错误: '上傳圖片返回結果錯誤',
                请替换为支持的图片类型: '請替換為支持的圖片類型',
                您插入的网络图片无法识别: '您插入的網絡圖片無法識別',
                您刚才插入的图片链接未通过编辑器校验: '您剛才插入的圖片鏈接未通過編輯器校驗',
            }
        }
    }
}

const _options = {
    default() {
        return {
            el: undefined,
            onSubmit: undefined,
            onReload: undefined,
            mounted: undefined,
            injectEvent: true,
            formData: undefined,
            global: {span: 24},
            page: undefined,
            form: {
                hideRequiredMark: false,
                layout: 'horizontal',
                labelAlign: 'right',
                labelCol: {
                    span: 5
                },
                wrapperCol: {
                    span: 19
                },
                colon: undefined,
                validateOnRuleChange: true,
                title: true
            },
            row: {
                gutter: 0,
                type: undefined,
                align: undefined,
                justify: undefined
            },
            submitBtn: {
                ..._defaultConfig.props,
                ..._defaultConfig.btn,
                htmlType: 'submit',
                ghost: false,
                icon: 'save',
                innerText: 'submit'
            },
            resetBtn: {
                ..._defaultConfig.props,
                ..._defaultConfig.btn,
                htmlType: 'reset',
                ghost: true,
                icon: 'reload',
                innerText: 'reset',
            },
            info: {
                type: "popover",
                placement: 'topLeft',
                icon: 'question-circle-o'
            },
            wrap: undefined
        }
    }
}

const _components = {
    hidden() {
        return {
            type: 'hidden',
            field: '',
            value: undefined
        }
    },
    input() {
        return {
            ..._defaultConfig.rule,
            type: 'input',
            props: {
                ..._defaultConfig.props,
                addonAfter: undefined,
                addonBefore: undefined,
                allowClear: true,
                autosize: undefined,
                enterButton: undefined,
                id: '',
                loading: false,
                maxLength: 64,
                placeholder: undefined,
                prefix: undefined,
                suffix: undefined,
                type: 'text',
                visibilityToggle: undefined,
                compact: undefined
            },
            on: {
                ..._defaultConfig.on,
                pressEnter: undefined,
                search: undefined,
            }
        }
    },
    inputNumber() {
        return {
            ..._defaultConfig.rule,
            type: 'InputNumber',
            value: undefined,
            props: {
                ..._defaultConfig.props,
                autoFocus: false,
                decimalSeparator: undefined,
                formatter: undefined,
                max: 9999999,
                min: 0,
                parser: undefined,
                precision: 2,
                step: 1
            },
            on: {
                ..._defaultConfig.on,
                pressEnter: undefined,
            }
        }
    },
    autoComplete() {
        return {
            ..._defaultConfig.rule,
            type: 'autoComplete',
            inject: true,
            props: {
                ..._defaultConfig.props,
                allowClear: true,
                autoFocus: false,
                backfill: false,
                dataSource: undefined,
                defaultOpen: false,
                dropdownMenuStyle: undefined,
                defaultActiveFirstOption: true,
                filterOption: true,
                open: undefined,
                optionLabelProp: 'children',
                placeholder: undefined
            },
            on: {
                ..._defaultConfig.on,
                search: undefined,
                blur: undefined,
                focus: undefined,
                select: undefined,
                dropdownVisibleChange: undefined
            },
            effect: {
                fetch: {
                    ..._defaultConfig.fetch,
                    to: 'props.dataSource'
                }
            }
        }
    },
    radio() {
        return {
            ..._defaultConfig.rule,
            type: 'radio',
            props: {
                ..._defaultConfig.props,
                name: undefined,
                buttonStyle: 'outline',
                options: undefined
            },
            options: [],
            on: {
                ..._defaultConfig.on
            },
            effect: {
                fetch: {
                    ..._defaultConfig.fetch
                }
            }
        }
    },
    checkbox() {
        return {
            ..._defaultConfig.rule,
            type: 'checkbox',
            props: {
                ..._defaultConfig.props,
                name: undefined,
                indeterminate: false,
                options: undefined
            },
            options: [],
            on: {
                ..._defaultConfig.on
            },
            effect: {
                fetch: {
                    ..._defaultConfig.fetch
                }
            }
        }
    },
    select() {
        return {
            ..._defaultConfig.rule,
            type: 'select',
            props: {
                ..._defaultConfig.props,
                allowClear: true,
                autoClearSearchValue: true,
                autoFocus: false,
                defaultActiveFirstOption: true,
                dropdownClassName: undefined,
                dropdownMatchSelectWidth: true,
                dropdownRender: undefined,
                dropdownStyle: {maxHeight: '400px', overflow: 'auto'},
                dropdownMenuStyle: undefined,
                filterOption: true,
                firstActiveValue: undefined,
                getPopupContainer: () => {
                    return document.body
                },
                labelInValue: false,
                maxTagCount: undefined,
                maxTagPlaceholder: undefined,
                maxTagTextLength: undefined,
                mode: 'default',
                notFoundContent: 'Not Found',
                optionFilterProp: 'children',
                optionLabelProp: 'children',
                placeholder: undefined,
                showSearch: true,
                showArrow: true,
                suffixIcon: undefined,
                removeIcon: undefined,
                clearIcon: undefined,
                menuItemSelectedIcon: undefined,
                tokenSeparators: [','],
                defaultOpen: undefined,
                open: undefined
            },
            options: [],
            on: {
                ..._defaultConfig.on,
                blur: undefined,
                deselect: undefined,
                focus: undefined,
                inputKeydown: undefined,
                mouseenter: undefined,
                mouseleave: undefined,
                popupScroll: undefined,
                search: undefined,
                select: undefined,
                dropdownVisibleChange: undefined
            },
            effect: {
                fetch: {
                    ..._defaultConfig.fetch
                }
            }
        }
    },
    switch() {
        return {
            ..._defaultConfig.rule,
            type: 'switch',
            props: {
                ..._defaultConfig.props,
                autoFocus: false,
                checked: undefined,
                checkedChildren: undefined,
                defaultChecked: false,
                loading: false,
                unCheckedChildren: undefined
            },
            on: {
                ..._defaultConfig.on,
                click: undefined
            }
        }
    },
    cascader() {
        return {
            ..._defaultConfig.rule,
            type: 'cascader',
            props: {
                ..._defaultConfig.props,
                allowClear: true,
                autoFocus: false,
                changeOnSelect: false,
                displayRender: labels => labels.join(' / '),
                expandTrigger: 'click',
                fieldNames: {
                    label: 'label',
                    value: 'value',
                    children: 'children'
                },
                getPopupContainer: () => document.body,
                loadData: undefined,
                notFoundContent: '',
                options: [],
                placeholder: undefined,
                popupClassName: '',
                popupStyle: undefined,
                popupPlacement: 'bottomLeft',
                popupVisible: undefined,
                showSearch: false,
                suffixIcon: undefined
            },
            on: {
                ..._defaultConfig.on,
                popupVisibleChange: undefined
            },
            fetch: {
                ..._defaultConfig.fetch,
                to: 'props.options'
            }
        }
    },
    colorPicker() {
        return{
            ..._defaultConfig.rule,
            type: 'ColorPicker',
            props: {
                ..._defaultConfig.props,
                showAlpha: false,
                colorFormat: 'hex'
            },
            on: {
                activeChange: undefined
            }
        }
    },
    datePicker() {
        return {
            ..._defaultConfig.rule,
            type: 'DatePicker',
            props: {
                ..._defaultConfig.props,
                type: 'date',
                allowClear: true,
                autoFocus: false,
                dateRender: undefined,
                disabledDate: undefined,
                disabledTime: undefined,
                getCalendarContainer: undefined,
                locale: undefined,
                mode: 'date',
                open: undefined,
                popupStyle: undefined,
                dropdownClassName: '',
                suffixIcon: undefined,
                format: 'YYYY-MM-DD',
                showTime: false,
                showToday: true,
                ranges: undefined,
                renderExtraFooter: undefined,
                separator: ['~']
            },
            on: {
                ..._defaultConfig.on,
                openChange: undefined,
                panelChange: undefined,
                calendarChange: undefined
            }
        }
    },
    timePicker() {
        return {
            ..._defaultConfig.rule,
            type: 'TimePicker',
            props: {
                ..._defaultConfig.props,
                addon: undefined,
                allowClear: true,
                autoFocus: false,
                clearText: '',
                defaultOpenValue: undefined,
                disabledHours: undefined,
                disabledMinutes: undefined,
                disabledSeconds: undefined,
                format: 'HH:mm:ss',
                getPopupContainer: undefined,
                hideDisabledOptions: false,
                hourStep: 1,
                inputReadOnly: false,
                minuteStep: 1,
                open: undefined,
                popupClassName: '',
                popupStyle: undefined,
                secondStep: 1,
                suffixIcon: undefined,
                clearIcon: undefined,
                use12Hours: false,
                align: undefined,
                valueFormat: undefined
            },
            on: {
                ..._defaultConfig.on,
                openChange: undefined
            }
        }
    },
    upload() {
        return {
            ..._defaultConfig.rule,
            type: 'upload',
            props: {
                ..._defaultConfig.props,
                onSuccess: undefined,
                limit: undefined,
                accept: undefined,
                action: undefined,
                method: 'POST',
                directory: false,
                beforeUpload: undefined,
                customRequest: undefined,
                data: undefined,
                fileList: undefined,
                headers: undefined,
                listType: 'text',
                multiple: false,
                name: 'file',
                previewFile: undefined,
                showUploadList: true,
                supportServerRender: false,
                withCredentials: false,
                openFileDialogOnClick: true,
                remove: undefined,
                transformFile: undefined,
                onHandle: undefined
            },
            on: {
                ..._defaultConfig.on,
                preview: undefined,
                download: undefined,
                reject: undefined
            }
        }
    },
    rate() {
        return {
            ..._defaultConfig.rule,
            type: 'rate',
            props: {
                ..._defaultConfig.props,
                allowClear: true,
                allowHalf: false,
                autoFocus: false,
                character: '<Icon type="start" />',
                count: 5,
                tooltips: undefined
            },
            on: {
                ..._defaultConfig.on,
                blur: undefined,
                focus: undefined,
                hoverChange: undefined,
                keydown: undefined
            }
        }
    },
    slider() {
        return {
            ..._defaultConfig.rule,
            type: 'slider',
            props: {
                ..._defaultConfig.props,
                autoFocus: false,
                dots: false,
                included: true,
                marks: undefined,
                max: 100,
                min: 0,
                range: false,
                reverse: false,
                step: 1,
                tipFormatter: undefined,
                vertical: false,
                tooltipPlacement: '',
                tooltipVisible: undefined,
                getTooltipPopupContainer: () => document.body
            },
            on: {
                ..._defaultConfig.on,
                afterChange: undefined
            }
        }
    },
    tree() {
        return {
            ..._defaultConfig.rule,
            type: 'tree',
            props: {
                ..._defaultConfig.props,
                blockNode: false,
                treeData: [],
                replaceFields: {
                    children: 'children',
                    title: 'title',
                    key: 'key'
                },
                autoExpandParent: true,
                checkable: false,
                checkedKeys: undefined,
                checkStrictly: false,
                defaultCheckedKeys: undefined,
                defaultExpandAll: false,
                defaultExpandedKeys: undefined,
                defaultExpandParent: true,
                defaultSelectedKeys: undefined,
                draggable: false,
                expandedKeys: undefined,
                filterTreeNode: undefined,
                loadData: undefined,
                loadedKeys: undefined,
                multiple: false,
                selectable: true,
                selectedKeys: undefined,
                showIcon: false,
                switcherIcon: undefined,
                showLine: false,
                props: {
                    class: undefined,
                    style: undefined,
                    checkable: false,
                    disableCheckbox: false,
                    disabled: false,
                    icon: undefined,
                    isLeaf: false,
                    key: undefined,
                    selectable: true,
                    title: '---',
                    slots: undefined,
                    scopedSlots: undefined,
                    on: {
                        click: undefined
                    }
                }
            },
            on: {
                ..._defaultConfig.on,
                check: undefined,
                dragend: undefined,
                dragenter: undefined,
                dragleave: undefined,
                dragover: undefined,
                dragstart: undefined,
                drop: undefined,
                expand: undefined,
                load: undefined,
                rightClick: undefined,
                select: undefined
            },
            effect: {
                fetch: {
                    ..._defaultConfig.fetch,
                    to: 'props.treeData'
                }
            }
        }
    },
    frame() {
        return {
            ..._defaultConfig.rule,
            type: 'frame',
            props: {
                ..._defaultConfig.props,
                type: 'image',
                src: '',
                helper: false,
                icon: undefined,
                srcKey: undefined,
                width: undefined,
                height: undefined,
                okBtnText: '',
                closeBtnText: '',
                modalTitle: '',
                handleIcon: undefined,
                title: '',
                modal: undefined,
                allowRemove: true,
                onChange: undefined,
                onOpen: undefined,
                onOk: undefined,
                onHandle: undefined,
                onBeforeRemove: undefined,
                onRemove: undefined,
                onCancel: undefined
            }
        }
    },
    button() {
        return {
            ..._defaultConfig.rule,
            type: 'a-button',
            props: {
                ..._defaultConfig.props,
                ..._defaultConfig.btn,
                ghost: false,
                htmlType: 'button',
                icon: undefined
            },
            on: {
                ..._defaultConfig.on
            }
        }
    },
    treeSelect() {
        return {
            ..._defaultConfig.rule,
            type: 'a-tree-select',
            props: {
                ..._defaultConfig.props,
                treeData: [],
                replaceFields: {
                    children: 'children',
                    title: 'title',
                    key: 'key',
                    value: 'value'
                },
                allowClear: true,
                dropdownStyle: {maxHeight: '400px', overflow: 'auto'},
                filterTreeNode: undefined,
                getPopupContainer: undefined,
                labelInValue: false,
                loadData: undefined,
                maxTagCount: undefined,
                maxTagPlaceholder: undefined,
                multiple: false,
                placeholder: undefined,
                searchPlaceholder: undefined,
                searchValue: undefined,
                treeIcon: false,
                showCheckedStrategy: undefined,
                showSearch: false,
                suffixIcon: undefined,
                treeCheckable: false,
                treeCheckStrictly: false,
                treeDataSimpleMode: false,
                treeDefaultExpandAll: false,
                treeDefaultExpandedKeys: undefined,
                treeExpandedKeys: undefined,
                treeNodeFilterProp: 'value',
                treeNodeLabelProp: 'title',
                props: {
                    checkable: undefined,
                    disableCheckbox: false,
                    disabled: false,
                    isLeaf: false,
                    key: undefined,
                    selectable: true,
                    title: '---',
                    scopedSlots: undefined
                }
            },
            on: {
                ..._defaultConfig.on,
                search: undefined,
                select: undefined,
                treeExpand: undefined
            },
            effect: {
                fetch: {
                    ..._defaultConfig.fetch,
                    to: 'props.treeData'
                }
            }
        }
    },
    group() {
        return{
            ..._defaultConfig.rule,
            type: 'group',
            props: {
                ..._defaultConfig.props,
                rule: [],
                field: undefined,
                min: undefined,
                max: undefined,
                expand: undefined,
                button: undefined,
                options: undefined,
                fontSize: 28,
                onBeforeAdd: undefined,
                onBeforeRemove: undefined
            },
            on: {
                ..._defaultConfig.on,
                add: undefined,
                remove: undefined,
                itemMounted: undefined
            }
        }
    },
    object () {
        return {
            ..._defaultConfig.rule,
            type: 'object',
            props: {
                ..._defaultConfig.props,
                rule: [],
                options: undefined
            },
            on: {
                ..._defaultConfig.on,
                itemMounted: undefined
            }
        }
    },
    editor () {
        return {
            ..._defaultConfig.rule,
            type: 'editor',
            props: {
                ..._defaultConfig.props,
                vModel: undefined,
                init: (e, editor) => {
                    const lang  = window.localStorage.getItem(Config.language) || Locale.language(Device.language)
                    editor.config.lang = lang.indexOf('en') !== -1 ? 'en' : lang
                    editor.config.languages['zh-HK'] = _defaultConfig.hkLang
                    editor.i18next = i18next
                    editor.create()
                }
            }
        }
    }
}

export default {
    options: _options,
    components: _components
}
```

*属性*

|名称|说明|
|:-|:-|
|options|form表单[选项配置](http://www.form-create.com/v2/guide/global-options.html "form-create options")|
|components|form表单[组件规则](http://www.form-create.com/v2/ant-design-vue/ "form-create components")|

### http.js

> axios封装, 实现ajax请求及主持请求拦截器

*代码*

```javascript
import Axios from 'axios'
import Config from './config'
import Seting from './seting'
import Error from './error'

class httpRequest {
    constructor() {
        this.options = {
            method: 'POST',
            url: ''
        }
        this.queue = {}
        this.capture = true
    }

    destroy(url) {
        delete this.queue[url]
        const queue = Object.keys(this.queue)
        return queue.length
    }

    // 实现request请求拦截机制
    interceptors(instance, url) {
        instance.interceptors.request.use((config) => {
            Config.requestFormData && (config.headers['Content-Type'] = 'multipart/form-data')
            return config
        })
        instance.interceptors.response.use((response) => {
            this.destroy(url)
            const code = response.data.subCode || response.data.code
            if (response.data.code === 4003) {
                const flag = code === 'auth.no_permission'
                window.location.replace(`${flag ? Config.page403 : Config.invalid}?code=${flag ? '403' : code}`)
                return
            }
            this.capture && response.data.code != 0 && Error.showMessage(response.data)
            return response.data
        }, (err) => {
            if (err.response.status === 403 || err.response.status === 401) {
                window.location.replace(`${err.response.status === 403 ? Config.page403 : Config.invalid}?code=${err.response.status}`)
                return
            }
            this.capture && Error.showMessage(err.response)
            return Promise.reject(err.response)
        })
    }

    // 实现2种数据请求格式
    create(formData) {
        let conf = formData ? {
            baseURL: Config.baseUrl,
            timeout: Config.timeout,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
                'X-URL-PATH': location.pathname,
                sysId: Config.systemId
            },
            transformRequest: [function (data) {
                function param(obj) {
                    let query = '';
                    let name;
                    let value;
                    let fullSubName;
                    let subName;
                    let subValue;
                    let innerObj;
                    let i
                    for (name in obj) {
                        value = obj[name]
                        if (value instanceof Array) {
                            for (i = 0; i < value.length; ++i) {
                                subValue = value[i]
                                fullSubName = name + '[]'
                                innerObj = {}
                                innerObj[fullSubName] = subValue
                                query += param(innerObj) + '&'
                            }
                        } else if (value instanceof Object) {
                            for (subName in value) {
                                subValue = value[subName]
                                fullSubName = name + '[' + subName + ']'
                                innerObj = {}
                                innerObj[fullSubName] = subValue
                                query += param(innerObj) + '&'
                            }
                        } else if (value !== undefined && value !== null) {
                            query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&'
                        }
                    }
                    return query.length ? query.substr(0, query.length - 1) : query
                }

                // 可以对data做任何操作
                return typeof data === 'object' && String(data) !== '[object File]' ? param(data) : data
            }]
        } : {
            baseURL: Config.baseUrl,
            timeout: Config.timeout,
            headers: {
                'Content-Type': 'application/json;charset=utf-8',
                sysId: Config.systemId
            }
        }
        return Axios.create(conf)
    }

    // mergeRequest (instances = []) {
    //
    // }

    request(options, formData, capture) {
        this.capture = typeof capture === 'boolean' ? capture : true
        let instance = this.create(formData)
        this.interceptors(instance, this.options.url)
        if (options) {
            Object.assign(this.options, options)
        }
        this.queue[this.options.url] = instance
        return instance(this.options)
    }
}

export default {
    instance: null,
    init() {
        !this.instance && (this.instance = new httpRequest())
        return this.instance
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|request|options(Object),json(Boolean)|Promise|调用axios发起ajax请求，可传递options参数。如果json不为空，则http请求数据格式为formData|

### i18n.js

> vue-i18n封装，提供组件本地化支持

*代码*

```javascript
import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Config from './config'
import Locale from './locale'
import Device from './device'
import Messages from '../locale/entry'

Vue.use(VueI18n)

class i18n extends VueI18n {
    constructor(messages = {}) {
        const locale = window.localStorage.getItem(Config.language)
        Config.languages.forEach(l => Object.assign(Messages[l.code], messages[l.code]))
        super({
            locale: locale || Locale.language(Device.language),
            messages: Messages,
            silentFallbackWarn: true,
            silentTranslationWarn: true
        })
    }
}

export default i18n
```

### locale.js

> 获取语言环境，如果当前语言环境不支持则转为en-US

*代码*

```javascript
import Config from './config'

export default {
    language(code, lower) {
      if (Config.languages.map(l => l.code).includes(code)) {
          return lower ? code.toLowerCase() : code
      }
      if (code.indexOf('zh') !== -1 && code !== Config.languages[1].code){
          return lower ? Config.languages[2].code.toLowerCase() : Config.languages[2].code
      }
      return lower ? Config.languages[0].code.toLowerCase() : Config.languages[0].code
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|language|code(String),lower(Boolean)|String|获取语言环境，如果当前语言环境不支持则转为en-US，如果lower为true，则反地域编码的小写字符|

### router

> vue-router封装，提供路由服务

*代码*

```javascript
import Vue from 'vue'
import Router from 'vue-router'
import Config from './config'
import Seting from './seting'
import Routes from '../routes'

const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router) //!!! 这里不会有效果，移步到cross-ui install 中

const activeMenu = (menus, name) => {
    for (let i = 0; i < menus.length; i++) {
        if (name === menus[i][Config.menu.props.route]) {
            Config.menu.openKeys.push(menus[i][Config.menu.props.parent])
            Config.menu.selectedKeys.push(menus[i][Config.menu.props.key])
            break
        }
        if (menus[i][Config.menu.props.children]) {
            activeMenu(menus[i][Config.menu.props.children], name)
        }
    }
}

class router extends Router {
    constructor(routes) {
        super({
            base: process.env.BASE_URL,
            routes: Routes.concat(routes)
        })
        super.beforeEach((to, from, next) => {
            document.title = to.meta.subTitle || 'DataSmith'
            if (!to.matched.length) {
                next({path: '/404'})
                return
            }
            if (!Seting.get(Config.tokenKey) && to.meta.isLogin) {
                next({name: 'login'})
                return
            }
            const user = Seting.get(Config.user)
            if (!user && to.meta.isLogin) {
                next({name: '403'})
                return
            }
            next()
        })
        super.afterEach(to => {
            Config.menu.openKeys.length = Config.menu.selectedKeys.length = 0
            activeMenu(Seting.get(Config.menu.name) || [], to.name)
            window.scrollTo(0, 0)
        })
    }
}

export default router
```

### seting.js

> Cookie封装

*代码*

```javascript
import Config from './config'
import storage from './storage'
import {isObject, isArray} from '../utils/types'

export default {
    set(value, flag, seting = Config.seting[Config.platform]) {
        storage.set(value, flag, seting)
    },
    get(flag, seting = Config.seting[Config.platform], value = true) {
        return storage.get(flag, seting)
    },
    remove(seting = Config.seting[Config.platform]) {
        storage.remove(seting)
        this.removeSession()
    },
    setSession(token) {
        !this.getSession() && window.sessionStorage.setItem(Config.session, isObject(token) ? JSON.stringify(token) : token)
    },
    getSession() {
        let token = window.sessionStorage.getItem(Config.session)
        try {
            token = JSON.parse(token)
        } catch (er) {
            console.log(er)
        }
        return token || false
    },
    removeSession() {
        window.sessionStorage.removeItem(Config.session)
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|set|value(-),flag(String),seting(String),domain(String),expires(Number)|-|设置Session中键(seting)的属性(falg)的值(value),并支持设置Session的域名(domain)和过期时间(expires)|
|get|flag(String),seting(String)|String|获取Session中键(flag)的属性(flag)值|
|remove|flags(Array),seting(String)|-|移除Session中的键(seting),如果存在flags则删除seting键中对应属性|
|getSession|-|token(String)|会话token信息|
|setSession|token(String)|-|设置会话token|
|removeSession|-|-|移除会话token|

### theme.js

> 组件主题, 处理组件的主题样式

*代码*

```javascript
import $ from 'jquery'
import Config from './config'
import Events from '../utils/events'

export default {
    init() {
        const theme = this.getCurrentTheme()
        $(`link[rel="stylesheet"][href*="${this.getNextTheme()}"]`).each(function () {
            this.href = this.href.replace(/(dark|light)/g, theme)
        })
    },
    change() {
        const newTheme = this.getNextTheme()
        $(`link[rel="stylesheet"][href*="${this.getCurrentTheme()}"]`).each(function () {
            this.href = this.href.replace(/(dark|light)/g, newTheme)
        })
        window.localStorage.setItem(Config.theme, newTheme)
        Events.emit(newTheme)
    },
    getNextTheme() {
        return (this.getCurrentTheme() === 'light' ? 'dark' : 'light')
    },
    getCurrentTheme() {
        return window.localStorage.getItem(Config.theme) || Config.css
    },
    bindEvent(func, ns = 'cross') {
        Events.bind(func, ns)
    },
    removeEvent(ns = 'cross') {
        Events.remove(ns)
    }
}
```

#方法#

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|init|-|-|初始当前主题|
|change|-|-|切换当前主题|
|getNextTheme|-|-|获取下一个主题|
|getCurrentTheme|-|-|获取当前主题|
|bindEvent|event(Function)|-|绑定事件|
|removeEvent|-|-|移除事件绑定|

### timer.js

> 日期格式化

*代码*

```javascript
// 日期格式转换类
export default {
  setDayStart (date) {
    if (date instanceof Date) {
      date.setHours(0)
      date.setMinutes(0)
      date.setSeconds(0)
      return date
    }
    return null
  },
  setDayEnd (date) {
    if (date instanceof Date) {
      date.setHours(23)
      date.setMinutes(59)
      date.setSeconds(59)
      return date
    }
    return null
  },
  // 用户自定义的日期格式
  customTimeFormat (time, fmt, utc = false) {
    if (time instanceof Date) {
      let o = {
        'y+': utc ? time.getUTCFullYear() : time.getFullYear(),
        'M+': (utc ? time.getUTCMonth() : time.getMonth()) + 1, // 月份
        'd+': utc ? time.getUTCDate() : time.getDate(), // 日
        'h+': utc ? time.getUTCHours() : time.getHours(), // 小时
        'm+': ((utc ? time.getUTCMinutes() : time.getMinutes()) + '').padStart(2, '0'), // 分
        's+': utc ? time.getUTCSeconds() : time.getSeconds(), // 秒
        'q+': Math.floor(((utc ? time.getUTCMonth() : time.getMonth()) + 3) / 3), // 季度
        'S': utc ? time.getUTCMilliseconds() : time.getMilliseconds() // 毫秒
      }
      if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, ((utc ? time.getUTCFullYear() : time.getFullYear()) + '').substr(4 - RegExp.$1.length))
      for (const k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
      }
      return fmt
    }
    return null
  },
  // 常见的日期格式转换
  formatDateTime (data, utc = false) {
    return this.customTimeFormat(data, 'yyyy-MM-dd hh:mm:ss', utc)
  },
  // 计算时间戳之间的间隔时长， 返回结果的格式 【时：分：秒】
  formatDuration (startTime, endTime) {
    if (!endTime || !startTime) {
      return '-'
    } else {
      const diffVal = endTime - startTime
      const formatTime = function (num, level) {
        const units = [60 * 60 * 1000, 60 * 1000, 1000]
        const result = parseInt(num / units[level]) + ''
        const remainder = num % units[level]
        return result.padStart(2, 0) + (level < 2 ? (':' + formatTime(remainder, level + 1)) : '')
      }
      return diffVal < 1000 ? '00:00:00' : formatTime(diffVal, 0)
    }
  }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|setDayStart|date(Date)|Date/null|设置Date日期的开始时间，精确到秒|
|setDayEnd|date(Date)|Date/null|设置Date日期的结束时间，精确到秒|
|customTimeFormat|time(Date),fmt(String),utc(Boolean)|String/null|格式化date，utc为true则支持utc日期，fmt提供格式字符串|
|formatDateTime|date(Date),utc(Boolean)|String|常见的日期格式转换|
|formatDuration|startTime(Date),endTime(Date)|String|计算时间戳之间的间隔时长， 返回结果的格式(时：分：秒)|

### treeData.js

> 配置为树形结构数据，节点插槽及筛选

*代码*

```javascript
export default {
    init(list, labelS, labelT, valueS, valueT, children) {
        return list.map(d => {
            const _data = {}
            _data[labelT] = d[labelS]
            _data[valueT] = d[valueS]
            _data[children] = d[children] ? this.init(d[children], labelS, labelT, valueS, valueT, children) : null
            return _data
        })
    },
    parse(list, labelS, labelT, children, prefix) {
        list.forEach(d => {
            d[labelT] = `${prefix}.${d[labelS]}`
            d[children] && this.parse(d[children], labelS, labelT, children, prefix)
        })
    },
    setSlot(list, prop, slot) {
        list.forEach(node => {
            node[prop] = slot
            node.children && this.setSlot(node.children, prop, slot)
        })
    },
    filter(tree, func) {
        return tree.map(node => ({...node})).filter(node => {
            node.children = node.children && this.filter(node.children, func)
            return func(node) || (node.children && node.children.length)
        })
    },
    filter2(tree, func) {
        return tree.map(node => ({...node})).filter(node => {
            node.children = node.children && this.filter2(node.children, func)
            return func(node)
        })
    },
    traverse(tree, id, path) {
        for (let i = 0; i < tree.length; i++) {
            const item = tree[i]
            path.push(item)
            if (item.id === id) {
                return path
            } else if (item.children && item.children.length) {
                const length = path.length
                this.traverse(item.children, id, path)
                if (length < path.length) {
                    return path
                } else {
                    path.pop()
                }
            } else {
                path.pop()
            }
        }
        return path
    },
    transfer(list, id, pid) {
        const _init = (node, list, id, pid) => {
            const children = list.filter(l => l[pid] === node[id])
            const orders = list.filter(l => l[pid] !== node[id])
            children.forEach(n => _init(n, orders, id, pid))
            node.children = children
        }
        const parents = list.filter(l => !l[pid])
        const children = list.filter(l => !!l[pid])
        parents.forEach(p => _init(p, children, id, pid))
        return parents
    },
    getNode(tree, id, value) {
        let node
        const _filter = (list, id, value) => {
            for (let i = 0; i < list.length; i++) {
                if (list[i][id] === value) {
                    node = list[i]
                    break
                }
                _filter(list[i].children, id, value)
            }
        }
        _filter(tree, id, value)
        return node
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|init|list(Array),lableS(String),labelT(String),valueS(-),valueT(-)|Object|递归设置树形接口数据(labelT替换labelS,valueT替换valueS的键值对)并返回设置后的树形数据|
|setSlot|list(Array),prop(String),slot(-)|-|递归设置list各项属性(prop)的值为slot|
|filter|list(Array),prop(String),value(-)|Array|筛选list各项属性(prop)的值含有value的项|
|filter2|tree(Array),func(Function)|Array|筛选list各项满足func函数要求|
|traverse|tree(Array),id(String/Number),path(Array)|Array|筛选满足id的数据|
|transfer|list(Array),id(String/Number),pid(String/Number)|Array|转换数组为树形结构|
|getNode|tree(Array),id(String/Number),value()|node(Object)|获取满足(id===value)条件的树节点|

### user.js

> 登录账户信息

*代码*

```javascript
// 用户类，记录当前登录用户信息
class User {
  constructor (account, name, systems, staffId, tenantCode, tenantName, tenantId, branchId, vopId, logo, mobile, email, adminFlag) {
    this.account = account
    this.name = name
    this.systems = systems
    this.staffId = staffId
    this.tenantCode = tenantCode
    this.tenantName = tenantName
    this.tenantId = tenantId
    this.branchId = branchId
    this.vopId = vopId
    this.logo = logo
    this.mobile = mobile
    this.email = email
    this.adminFlag = adminFlag
  }
}

export default User
```

### utils.js

> 综合方法

*代码*

```javascript
 // Promise.all 的问题是一旦有请求失败就会抛出错误，然而有时候希望某个请求失败后，其他请求的结果能够正常返回，针对这种情况引入Promise.allSettled，成功的promise将返回一个包含status和value的对象，失败的promise将返回一个包含status和reason的对象
 export const PromiseAllSettled =  function(promises){
    return new Promise(function(resolve,reject){
        if(!Array.isArray(promises)){
            return reject(new TypeError('参数不是数组'))
        }
        let resovedCounter = 0
        let res = new Array(promises.length)
        promises.forEach(function(p,index){
            p.then(function(data){
                resovedCounter++
                res[index] = {status:true,value:data}
                if(resovedCounter === promises.length){
                    return resolve(res)
                }
            },function(reason){
                resovedCounter++
                res[index] = {status:false,value:reason}
                if(resovedCounter === promises.length){
                    return resolve(res)
                }
            })
        })
    })
}
 // 路过路径字符串获取深层对象属性值
 export const getValueByPath=(obj,path,seprate='.') => {
    path = Array.isArray(path)?path:path.split(seprate);
    return path.reduce((pre,cur)=>pre&&pre[cur],obj)
}

export const debounce = {
    inserted: function (el, binding) {
      let timer
      el.addEventListener('click', (...args) => {
            if (timer) {
                clearTimeout(timer)
            }
            timer = setTimeout(() => {
                binding.value(...args)
            }, 1000)
      })
    }
}
```

*方法*
|名称|参数|返回|说明|
|:-|:-|:-|:-|
|PromiseAllSettled|promises(Array)|Promise|Promise.all 的问题是一旦有请求失败就会抛出错误，然而有时候希望某个请求失败后，其他请求的结果能够正常返回，针对这种情况引入Promise.allSettled|
|getValueByPath|obj(Object),path(String),seprate(String)|String|路过路径字符串获取深层对象属性值|
|debounce.inserted|el(Dom),binding(Event)|-|事件防抖|

### storage.js

> 存储

*代码*

```javascript
import {isObject} from '../utils/types'

const sessionId = new Date().getTime()

export default {
    set(value, name, seting, expires) {
        const options = {
            value: this.get(undefined, seting) || {sessionId: `${sessionId}`},
            expires: expires,
            startTime: new Date().getTime()
        }
        isObject(options.value) ? options.value[name] = value : options.value = value
        window.sessionStorage.setItem(seting, JSON.stringify(options))
    },
    get(name, seting) {
        let item = window.sessionStorage.getItem(seting)
        try {
            item = JSON.parse(item)
        } catch (er) {
            console.log(er)
        }
        if (item && item.startTime) {
            const date = new Date().getTime()
            if (date - item.startTime > item.expires) {
                this.remove(seting)
                return false
            }
            return name ? item.value[name] : item.value
        }
        return item || false
    },
    remove(seting) {
        window.sessionStorage.removeItem(seting)
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|set|callback(Function),router(vue-route),onload(Boolean),system(Boolean)|-|初始化统一登录管理|
|get|-|-|统一登录|
|remove|-|-|登出|

### httpCatch.js

> http异常捕获

*代码*

```javascript
import Config from './config'
import http from './http'

const _http = http.init()

export const httpCatch = async function(option, formData, isCatch = true) {
    const result = await _http.request(option, formData).catch(err => {
        return err
    })
    if (isCatch && (result.status || result.code !== 0)) {
        const code = result.subCode || result.code
        this.$message.error(this.$t(code ? `error.${code}` : (result.status ? Config.ERROR : Config.commonError)))
    }
    return result
}

export const confirm = (callback, title = 'deleteConfirm', content = 'deleteDes', type = 'danger') => {
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
}
```

### ws.js

> 消息通信

*代码*

```javascript
import Stomp from 'stompjs'
import {isObject, isFunction} from '../utils/types'
import Config from './config'

class Connection {
    constructor(option = {}) {
        this.url = option.url || Config.socketUrl
        this.reconnectInterval = option.reconnectInterval || 5 * 1000
        this.client = null
        this.reconnectTimer = null
        this.subscribes = {}
    }

    on(event, fn) {
        if (!isFunction(fn)) {
            return
        }
        if (this.subscribes[event]) {
            this.subscribes[event].push(fn)
        } else {
            this.subscribes[event] = [fn]
            this.subscribe(event)
        }
    }

    off(event, fn) {
        const listeners = this.subscribes[event]
        if (listeners) {
            const index = listeners.indexOf(fn)
            if (index > -1) {
                listeners.splice(index, 1)
                console.log(`[unsubscribe: <${event}>] `, fn)
                if (!listeners.length && this.client && this.client.connected) {
                    this.client.unsubscribe(event)
                    this.subscribes[event] = null
                }
            }
        }
    }

    emit(event, data) {
        const listeners = this.subscribes[event]
        if (listeners) {
            for (let i = 0; i < listeners.length; i++) {
                listeners[i](data)
            }
        }
    }

    subscribe(event) {
        if (this.client && this.client.connected) {
            this.client.subscribe(event, frame => {
                const obj = {id: event}
                try {
                    obj.data = JSON.parse(frame.body)
                } catch {
                    obj.data = frame.body
                }
                // console.log(`event: <${event}> `, data)
                this.emit(event, obj)
            }, {id: event})
            console.log(`[subscribe: <${event}>]`)
        } else {
            console.warn('[stomp not connected] waiting for connecting...')
        }
    }

    connect() {
        if (this.client && this.client.connected) {
            return Promise.resolve(this.client)
        }
        this.client = Stomp.client(this.url)
        // this.client.debug = true
        return new Promise((resolve, reject) => {
            this.client.connect(Config.socketHeader, frame => {
                    console.log('[stomp connected]', frame)
                    Object.keys(this.subscribes).forEach(event => {
                        this.subscribe(event)
                    })
                    resolve(frame)
                },
                error => {
                    console.log('[stomp connect error] ', error)
                    this.reconnect()
                    reject(error)
                }
            )
        })
    }

    reconnect() {
        this.reconnectTimer && clearTimeout(this.reconnectTimer)
        this.reconnectTimer = setTimeout(() => {
            this.connect()
            console.log('[ws reconnect] ', this.reconnectTimer)
        }, this.reconnectInterval)
    }

    disconnect(callback) {
        this.client && isFunction(callback) && this.client.disconnect(callback)
        if (this.reconnectTimer) {
            clearTimeout(this.reconnectTimer)
            this.reconnectTimer = null
        }
        this.subscribes = {}
        console.log('[ws disconnect]')
    }
}

class Topic {
    constructor(topic) {
        return topic ? new Proxy({}, {
            get(target, route) {
                return `/exchange/${topic}/${route}`
            }
        }) : null
    }
}

export class WS {
    constructor(option) {
        this.client = new Connection(option)
    }
    /**
     *
     * @param {String|Object} option
     * @param {*} event
     * @returns
     */
    on(option, event) {
        if (!isFunction(event)) {
            return
        }
        if(isObject(option)){
            const topic = new Topic(option.topic)
            option = topic && topic[option.route]
        }
        option && this.client.on(option, event)
    }
    /**
     *
     * @param {String|Object} option
     * @param {*} event
     * @returns
     */
    off(option, event) {
        if (!isFunction(event)) {
            return
        }
        if(isObject(option)){
            const topic = new Topic(option.topic)
            option = topic && topic[option.route]
        }
        option && this.client.off(option, event)
    }

    listen (callback) {
        this.client.connect().then(frame => {
            isFunction(callback) && callback(frame)
        })
    }

    disconnect(callback) {
        this.client.disconnect(callback)
    }
}

// mq 前后端链接文档https://10.14.32.231:10443/doc/Internal/#/MQ/?id=_1%e3%80%81%e8%ae%be%e5%a4%87%e5%91%8a%e8%ad%a6%e4%bf%a1%e6%81%af
export const deviceTopic = new Topic('topic.device.portal') // 设备
export const talkingTopic = new Topic('topic.robot.talking') // 通话
export const taskTopic = new Topic('topic.task.portal') // 任务
/**
 *
 * @param {*} event 订阅消息类型 status talking taskStatusUpdate ...
 * @param {*} code  运营系统使用编码如：caos，cxos 等，租户系统使用 tenantCode
 * @returns
 */
export const getTopic = (event,code)=>{
    switch(event){
        case 'status':// 设备状态汇总
        case 'alarm':// 告警
        case 'battery': // 电量
        case 'onlineFlag': // 是否在线状态
        case 'location': // 位置上报
        case 'network':// 网络状态
        case 'event':// 事件
        case 'sprayStatus': // 喷雾
        case 'emergencyStop':// 一键急停状态
        case 'selfChecking': // 设备自检
            return deviceTopic[`${code}_${event}`]
            break;
        case "talking":// 通话呼叫消息
            return talkingTopic[`signal.tenant.${code}`]
            break;
        case 'taskStatusUpdate':// 任务状态更新
            return taskTopic[`${code}_${event}`]
            break
        default:
            break
    }
    return ''
}

// es6 引用单实例，每个应用应该只存在一个实例
// 关联影响 layout组件ws
/**
 *  使用步骤：
 * 1 引入ws实例
 * 2 ws.on(...,event)
 * 3 ws.listen(...)
 * 4 子组件beforeDestroyed:ws.off(event)
 * 5 APP beforeDestroyed:ws.disconnect()
 */
// let ws = new WS()
export default  WS


```





