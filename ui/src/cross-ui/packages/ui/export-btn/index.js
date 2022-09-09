import ExportBtn from './main'

ExportBtn.install = function (Vue) {
  Vue.component(ExportBtn.name, ExportBtn)
  return Vue
}

export default ExportBtn
