import Drawer from './main'

Drawer.install = function (Vue) {
    Vue.component(Drawer.name, Drawer)
    return Vue
}

export default Drawer