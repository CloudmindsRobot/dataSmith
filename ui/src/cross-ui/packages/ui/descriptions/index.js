import Descriptions from './main'

Descriptions.install = function (Vue) {
    Vue.component(Descriptions.name, Descriptions)
    return Vue
}

export default Descriptions