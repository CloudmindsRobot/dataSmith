import Statistics from './main'

Statistics.install = function (Vue) {
    Vue.component(Statistics.name, Statistics)
    return Vue
}

export default Statistics