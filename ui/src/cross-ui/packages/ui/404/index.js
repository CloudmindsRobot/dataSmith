import E404 from './main'

E404.install = function (Vue) {
    Vue.component(E404.name, E404)
    return Vue
}

export default E404
