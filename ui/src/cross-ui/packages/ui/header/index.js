import Header from './main'

Header.install = function (Vue) {
    Vue.component(Header.name, Header)
    return Vue
}

export default Header