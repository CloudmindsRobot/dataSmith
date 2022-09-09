import Vue from 'vue'
import App from './App.vue'
import routes from './routes'
import locale from './locale'
// 获取cross-ui库的ui组件及工具类
import {CrossUI, CrossTool} from './cross-ui/packages'

// 初始化i18n及路由器
const i18n = new CrossTool.i18n(locale)
const router = new CrossTool.router(routes)

Vue.use(CrossUI, {
    locale: i18n.locale,
    i18n: (key, value) => i18n.t(key, value)
})

Vue.config.productionTip = false;
// 注册全局工具类
Vue.prototype.$tools = {
    ...CrossTool
}

new Vue({
    router,
    i18n,
    render: h => h(App),
    data: {
        eventHub: new Vue()
    }
}).$mount('#app')



