import E403 from './main'

E403.install = function (Vue) {
    Vue.component(E403.name, E403)
    return Vue
}

export default E403
