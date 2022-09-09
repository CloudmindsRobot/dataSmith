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
        click: undefined,
        col: undefined,
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
                layout: 'vertical',
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
                innerText: 'submit',
                show: false
            },
            resetBtn: {
                ..._defaultConfig.props,
                ..._defaultConfig.btn,
                htmlType: 'reset',
                ghost: true,
                icon: 'reload',
                innerText: 'reset',
                show: false
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
    label() {
        return {
            type: 'cross-form-label',
            field: undefined,
            props: {
                title: '',
                cssStyle: undefined,
                cssClass: undefined
            }
        }
    },
    hidden() {
        return {
            type: 'hidden',
            field: undefined,
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
    password() {
        return {
            ..._defaultConfig.rule,
            type: 'aInputPassword',
            props: {
                ..._defaultConfig.props,
                addonAfter: undefined,
                addonBefore: undefined,
                allowClear: true,
                autosize: undefined,
                enterButton: undefined,
                id: '',
                maxLength: 64,
                placeholder: undefined,
                prefix: undefined,
                suffix: undefined,
                visibilityToggle: true,
            },
            on: {
                ..._defaultConfig.on,
                pressEnter: undefined
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
        return {
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
    rangePicker() {
        return {
            ..._defaultConfig.rule,
            type: 'RangePicker',
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
                ..._defaultConfig.on,
                click: undefined
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
    },
    table() {
        return {
            ..._defaultConfig.rule,
            type: 'aTable',
            props: {
                tableLayout: 'auto',
                dataSource: [],
                columns: [],
                bordered: true,
                pagination: false,
                size: 'middle',
                rowKey: 'id',
                scroll: {x: 992},
                showHeader: true,
                loading: false,
                defaultExpandAllRows: false,
                expandRowByClick: false
            },
            on: {
                ..._defaultConfig.on,
                expand: undefined
            }
        }
    },
    codemirror() {
        return {
            ..._defaultConfig.rule,
            type: 'cross-codemirror',
            props: {
                ns: 'cross-codemirror',
                options: {
                    tabSize: 2,
                    theme: 'default',
                    lineNumbers: false,
                    line: true,
                    placeholder: '',
                    mode: 'application/json',
                    viewportMargin: Infinity
                }
            },
            on: {
                input: undefined,
                ready: undefined
            }
        }
    }
}

export default {
    options: _options,
    components: _components
}
