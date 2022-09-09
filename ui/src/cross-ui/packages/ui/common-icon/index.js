import CommonIcon from './main';

CommonIcon.install = function (Vue) {
  Vue.component(CommonIcon.name, CommonIcon);
  return Vue
};

export default CommonIcon;
