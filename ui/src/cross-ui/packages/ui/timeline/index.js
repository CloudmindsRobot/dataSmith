import Timeline from './main'

Timeline.install = function (Vue) {
    Vue.component(Timeline.name, Timeline)
    return Vue
}

export default Timeline