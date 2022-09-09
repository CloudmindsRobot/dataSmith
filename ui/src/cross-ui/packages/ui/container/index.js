import Container from './main'

Container.install = function (Vue) {
    Vue.component(Container.name, Container)
    return Vue
}

export default Container