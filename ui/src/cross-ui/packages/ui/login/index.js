import Login from './main'

Login.install = function (Vue) {
    Vue.component(Login.name, Login)
    return Vue
}

export default Login