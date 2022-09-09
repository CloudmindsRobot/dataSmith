import Tabs from './main'

Tabs.install = function (Vue) {
    Vue.component(Tabs.name, Tabs)
    return Vue
}

export default Tabs