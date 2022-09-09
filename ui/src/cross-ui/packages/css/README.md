### css

> 组件主题样式及通用样式

```text
┌──fonts                   * fa-icon字体         
├──base.css                * 通用样式        
├──font-awesome.min.css    * fa-icon图标
├──iconfont.css            * iconfont图标
└──index.css               * 样式主文件
```

### index.css

> 样式主文件

*代码*

```css
@import "font-awesome.min.css";
@import "~@fancyapps/fancybox/dist/jquery.fancybox.min.css";
@import "base.css";
@import "./iconfont.css";
```
