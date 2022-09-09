import Menu from './main'

Menu.install = function (Vue) {
    Vue.component(Menu.name, Menu)
    return Vue
}

export default Menu