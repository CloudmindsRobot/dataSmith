import AdPreview from './main'

AdPreview.install = function (Vue) {
  Vue.component(AdPreview.name, AdPreview)
  return Vue
}

export default AdPreview
