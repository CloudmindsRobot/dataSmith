import transfer from './main'

transfer.install = function (Vue) {
    Vue.component(transfer.name, transfer)
    return Vue
}

export default transfer