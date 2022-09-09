import CrossList from './main'

CrossList.install = function (Vue) {
    Vue.component(CrossList.name, CrossList)
    return Vue
}

export default CrossList
