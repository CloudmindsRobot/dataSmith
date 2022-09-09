import Panel from './main'

Panel.install = function (Vue) {
    Vue.component(Panel.name, Panel)
    return Vue
}

export default Panel
