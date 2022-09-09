import ResultPanel from './main'

ResultPanel.install = function (Vue) {
    Vue.component(ResultPanel.name, ResultPanel)
    return Vue
}

export default ResultPanel