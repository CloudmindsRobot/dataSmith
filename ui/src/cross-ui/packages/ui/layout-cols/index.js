import LayoutCols from './main'

LayoutCols.install = function (Vue) {
    Vue.component(LayoutCols.name, LayoutCols)
    return Vue
}

export default LayoutCols