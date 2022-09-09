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
         */
        data: {
            type: Array,
            default: () => {
                return []
            }
        },
        /**
         * 主键
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
         *
         * @values true,false
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