import Main from './main'

Main.install = function (Vue) {
    Vue.component(Main.name, Main)
    return Vue
}

export default Main