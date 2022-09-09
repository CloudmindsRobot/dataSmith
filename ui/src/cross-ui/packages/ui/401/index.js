import E401 from './main'

E401.install = function (Vue) {
    Vue.component(E401.name, E401)
    return Vue
}

export default E401
