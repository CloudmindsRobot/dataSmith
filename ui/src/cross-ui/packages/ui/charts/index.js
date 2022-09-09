import Charts from './main'


Charts.install = function (Vue) {
    Vue.component(Charts.name, Charts)
    return Vue
};

export default Charts