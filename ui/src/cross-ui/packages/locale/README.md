### locale

> 本地化文件
> 主要提供组件间共用的本地化文件，以及全局环境下的通用本地化文件。

```text
┌──lang                 * 全局环境共用
│   ├──en-US.js    
│   ├──ja-JP.js    
│   ├──th-TH.js    
│   ├──zh-CN.js    
│   └──zh-HK.js           
├──share                * 组件间共用
│   ├──common.json          
│   ├──form.json
|   ├──page.json   
│   └──tool.json         
├──entry.js             * 组件本地化入口文件
└──index.js             * 组件本地化处理API
```

### entry.js

> 组件本地化入口文件

*代码*

```javascript
import cnLocal from './lang/zh-CN'
import enLocal from './lang/en-US'
import hkLocal from './lang/zh-HK'
import jpLocal from './lang/ja-JP'
import thLocal from './lang/th-TH'

export default {
  'zh-CN': cnLocal,
  'zh-HK': hkLocal,
  'en-US': enLocal,
  'ja-JP': jpLocal,
  'th-TH': thLocal
}
```

#### 扩展阅读

[Vue I18n](https://kazupon.github.io/vue-i18n/zh/guide/formatting.html "Vue I18n")
