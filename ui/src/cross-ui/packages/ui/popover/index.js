import popover from './main'

popover.install = function (Vue) {
    Vue.component(popover.name, popover)
    return Vue
}

export default popover