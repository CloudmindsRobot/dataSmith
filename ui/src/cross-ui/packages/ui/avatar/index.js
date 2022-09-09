import Avatar from './main'

Avatar.install = function (Vue) {
    Vue.component(Avatar.name, Avatar)
    return Vue
};

export default Avatar