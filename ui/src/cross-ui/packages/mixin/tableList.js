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