import PageTable from './main'

PageTable.install = function (Vue) {
  Vue.component(PageTable.name, PageTable)
  return Vue
}

export default PageTable
