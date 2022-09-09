import CopyCell from './main'

CopyCell.install = function (Vue) {
    Vue.component(CopyCell.name, CopyCell)
    return Vue
}

export default CopyCell