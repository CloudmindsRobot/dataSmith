import OperateDialog from './main'

OperateDialog.install = function (Vue) {
    Vue.component(OperateDialog.name, OperateDialog)
    return Vue
}

export default OperateDialog