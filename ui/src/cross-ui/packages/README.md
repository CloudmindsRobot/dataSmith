### 目录结构
```text
┌──css               * 组件主题样式及通用样式  
│   └──font          * 图标字体文件
├──locale            * 本地化文件
│   ├──lang          * 全局共用的本地化文件
│   └──share         * 组件间共用的本地化文件
├──mixin             * 组件间共用的mixins类
├──routes            * 默认路由，主要提供401，403，404等错误码的路由
├──static            * 组件的静态资源
│   ├──audio         * 组件使用到的音频文件
│   ├──image         * 组件使用到的图片
│   └──logo          * 公司logo图，分为彩色和白色两种
├──tools             * 提供内部组件以及开放给第三方使用的工具类
├──utils             * 通用工具包主要是组件内部使用的一些工具类
├──ui                * UI组件包(业务UI组件库,基于ant-design-vue)
└──index.js          * 组件API（此接口文件由脚本生成）
```

### index.js

> UI组件API, 对外组件接口文件。主要提供UI组件及工具类.

*代码*

```javascript
import Antd from 'ant-design-vue'
import formCreate from '@form-create/ant-design-vue'
import FcEditor from '@form-create/component-wangeditor'
import VueLocalStorage from 'vue-localstorage'
import '../packages/css/index.css'
......
import tools from './tools/index.js'

const components = [
    Search,
    PageTable,
    Layout,
    ......
];

/*
* 组件注册到VUE中
*/
const install = function (Vue, opts = {}) {
    components.forEach(component => {
        Vue.component(component.name, component)
    });
    Vue.use(Antd)
    Vue.use(formCreate)
    Vue.use(VueI18n)
    Vue.use(VueLocalStorage)

    Vue.prototype._http = httpCatch
    Vue.prototype._confirm = confirm
};

if (typeof window !== 'undefined' && window.Vue) {
    install(window.Vue)
}

/*
* UI组件
*/
export const CrossUI = {
    install,
    Search,
    PageTable,
    Layout,
    ......
}

/*
* 工具类
*/
export const CrossTool = {
    ...tools
}
```
