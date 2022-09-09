import Search from './main'

Search.install = function (Vue) {
    Vue.component(Search.name, Search)
    return Vue
}

export default Search