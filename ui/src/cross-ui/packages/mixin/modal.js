export default {
    props: {
        /**
         * 弹窗配置项
         *
         * {title:标题,visible:显示',width:宽度,preview:含有图片预览,src:预览图URL,style:样式,zIndex:层级,destroyOnClose:关闭时销毁实例,maskClosable:遮罩点击关闭}
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
                    confirmLoading: false,
                    maskClosable: false
                }
            }
        }
    },
    methods: {
        /**
         * 关闭弹窗
         *
         * @param {-}
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
         */
         onOk() {
            /**
             * 点击确定触发事件
            */
            this.$emit('ok')
        }
    }
}