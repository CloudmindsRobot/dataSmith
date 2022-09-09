import Modal from './main'

Modal.install = function (Vue) {
    Vue.component(Modal.name, Modal)
    return Vue
}

export default Modal