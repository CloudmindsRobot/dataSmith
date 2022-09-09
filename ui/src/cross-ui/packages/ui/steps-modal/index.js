import stepsModal from './main'

stepsModal.install = function (Vue) {
    Vue.component(stepsModal.name, stepsModal)
    return Vue
}

export default stepsModal