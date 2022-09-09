import Tree from './main'

Tree.install = function (Vue) {
    Vue.component(Tree.name, Tree)
    return Vue
}

export default Tree