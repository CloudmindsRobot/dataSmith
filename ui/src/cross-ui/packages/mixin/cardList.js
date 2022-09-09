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
                !newVal.indeterminate && this.data.forEach(d => d.checked = (!d.disabled && newVal.all))
                this.data.forEach((d)=>{ !d.disabled && (d.disabled = newVal.disabled) })
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