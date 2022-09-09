import UserCenter from './main'

UserCenter.install = function (Vue) {
    Vue.component(UserCenter.name, UserCenter)
    return Vue
}

export default UserCenter