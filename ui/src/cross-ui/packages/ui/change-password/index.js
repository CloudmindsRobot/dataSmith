import ChangePassword from './main'


ChangePassword.install = function (Vue) {
    Vue.component(ChangePassword.name, ChangePassword)
    return Vue
};

export default ChangePassword