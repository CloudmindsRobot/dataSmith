import Form from './main'

Form.install = function (Vue) {
    Vue.component(Form.name, Form)
    return Vue
}

export default Form