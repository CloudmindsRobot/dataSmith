import ContainerDetails from './main'

ContainerDetails.install = function (Vue) {
    Vue.component(ContainerDetails.name, ContainerDetails)
    return Vue
}

export default ContainerDetails