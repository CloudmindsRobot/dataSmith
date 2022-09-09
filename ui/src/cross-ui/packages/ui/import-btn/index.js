import ImportBtn from './main'

ImportBtn.install = function (Vue) {
  Vue.component(ImportBtn.name, ImportBtn)
  return Vue
}

export default ImportBtn
