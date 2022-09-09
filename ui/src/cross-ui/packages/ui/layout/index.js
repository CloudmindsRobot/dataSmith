import Layout from './main'

Layout.install = function (Vue) {
    Vue.component(Layout.name, Layout)
    return Vue
}

export default Layout