import Codemirror from './main'


Codemirror.install = function (Vue) {
    Vue.component(Codemirror.name, Codemirror)
    return Vue
};

export default Codemirror