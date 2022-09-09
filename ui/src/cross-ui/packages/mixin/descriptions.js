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