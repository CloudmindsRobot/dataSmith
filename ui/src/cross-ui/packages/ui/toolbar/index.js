import Toolbar from './main'

Toolbar.install = function (Vue) {
    Vue.component(Toolbar.name, Toolbar)
    return Vue
}

export default Toolbar