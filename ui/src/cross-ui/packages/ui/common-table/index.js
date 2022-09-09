import CrossTable from './main'

CrossTable.install = function (Vue) {
  Vue.component(CrossTable.name, CrossTable)
  return Vue
}

export default CrossTable
