import CrossCard from './main'

CrossCard.install = function (Vue) {
  Vue.component(CrossCard.name, CrossCard)
  return Vue
}

export default CrossCard
