### utils

> 提供组件内部使用的一些工具类。
> 主要包括clean(清除对象)，clone(拷贝对象)，encryption(加/解密)，network(网络状态)，preview(图片预览)，screen(窗体控制)，types（类型判断），events（事件管理）等。

```text
┌──animateClass.js          * 图标动画             
├──claen.js         
├──clone.js      
├──encryption.js     
├──events.js     
├──format.js                * 格式化，实现占位符"{}"的内容替换
├──network.js  
├──preview.js  
├──query.js                 * 数据查询  
├──screen.js
├──trajectory.js
├──types.js            
└──util.js                  * 通用帮助类
```

### animateClass.js

> 位置图标动画

*代码*

```javascript
// 位置图标弹跳动画类
export class IconJumpAnimate {
    constructor() {
        // 动画相关参数
        this.gravity = 0.2 // 重力参数，影响跳跃速度
        this.buffer = 0 //
        this.startSpeed = -2 // 初速度
        this.currentSpeed = -2 // 瞬时速度
        this.top = 400 // 弹跳高度
        this.maxTop = 400 // 最高高度
        this.w = 30
        this.h = 30
    }

    // canvas会调用draw方法绘制该动画，传入context参数
    draw(context) {
        if (!context.activeInterestSpot || !context.hightLightIcon) return
        const {spot, name} = context.activeInterestSpot
        if (!spot) return
        let [x, y] = spot
        x = x * context.scale + context.ax
        y = y * context.scale + context.ay
        this.currentSpeed += this.gravity
        this.top += this.currentSpeed
        if (this.top > this.maxTop) {
            this.top -= this.currentSpeed
        }
        context.ctx.strokeStyle = 'green'
        context.ctx.setLineDash([])
        context.ctx.beginPath()
        context.ctx.ellipse(
            x,
            y,  // 开始的 x,y 坐标位置。
            4,
            2,
            0,
            0,
            Math.PI * 2,
            true
        )
        context.ctx.closePath()
        context.ctx.stroke() // 调用样式
        if (name) {
            context.ctx.fillStyle = 'green'
            context.ctx.fillText(name, x - this.w / 2, y + this.top - this.maxTop - this.h)
        }
        context.ctx.drawImage(
            context.hightLightIcon, // 规定要使用的图像、画布或视频。
            x - this.w / 2,
            y + this.top - this.maxTop - this.h,
            this.w,
            this.h // 剪裁大小x,y
        )
        if (this.top >= this.maxTop) {
            this.startSpeed += this.buffer
            this.currentSpeed = this.startSpeed
        }
    }
}

// 加载图片loading动画类
export class LoadingAnimate {
    constructor() {
        this.isShow = false
        this.base = 0 //使用base来指示最大的圆点所在的位置, 实现旋转动画的效果
    }

    show() {
        this.base = 0
        this.isShow = true
    }

    close() {
        this.isShow = false
    }

    draw(context) {
        if (!this.isShow) return
        const ctx = context.ctx
        ctx.fillStyle = 'transparent'
        ctx.fillStyle = '#cdcdcd' //定义点的颜色
        ctx.translate(context.canvas.width / 2, context.canvas.height / 3)//把坐标原点移动到画布中央
        this.base = (++this.base === 13) ? (this.base - 12) : this.base//使用this.base来指示最大的圆点所在的位置, 实现旋转动画的效果
        const angle = Math.PI / 6//画12个点, 所以每个点之间的角度是 1/6 PI
        const beginAngle = angle * this.base
        for (let i = 1; i <= 12; i++) {
            ctx.beginPath()//开始一个路径
            if (i === 1) {
                ctx.rotate(beginAngle)
            } else {
                ctx.rotate(angle)//每次调用rotate之后, 其旋转角度并不会还原, 而是接着上一次的位置
            }
            ctx.arc(0, -30, i * 0.5 + 1, 0, 2 * Math.PI, true)//绘制一个圆点
            ctx.closePath()//闭合路径
            //如果不是用beginPath和closePath, 在调用fill方法时, 会导致画布上的所有圆都重叠在一起了
            ctx.fill()//填充(使用上面最后定义的fillStyle)
        }
    }
}

// 当前位置图标动画类
export class CurrentPosAnimate {
    constructor() {
        this.size = 80 // 当前位置图标大小
        this.duration = 1000
        this.currentPosition = null
    }

    setCurrentPosition(currentPosition) {
        this.currentPosition = currentPosition
        // console.log('MapControl drawCurrentPosition >>>', this.currentPosition)
    }

    draw(context) {
        if (!this.currentPosition || !context.mapIsLoaded) {
            return
        }
        let [x, y, angle] = this.currentPosition
        if (typeof x !== 'number') {
            x = Number(x)
            x = isNaN(x) ? 0 : x
        }
        if (typeof y !== 'number') {
            y = Number(y)
            y = isNaN(y) ? 0 : y
        }
        if (typeof angle !== 'number') {
            angle = Number(angle)
            angle = isNaN(angle) ? 0 : angle
        }
        const t = (performance.now() % this.duration) / this.duration
        const radius = this.size / 2 * 0.2
        const outerRadius = this.size / 2 * 0.7 * t + radius
        const positionX = x * context.scale + context.x
        const positionY = y * context.scale + context.y
        // 旋转中心点移到绘制点
        context.ctx.translate(positionX, positionY)
        // 将画布旋转angle角度
        context.ctx.rotate((angle - 90) / 180 * Math.PI)
        // 绘制初始点
        context.ctx.moveTo(positionX, positionY)
        // 设置直线
        context.ctx.setLineDash([1, 0])
        // 绘制外圆
        context.ctx.beginPath()
        context.ctx.arc(0, 0, outerRadius, 0, Math.PI * 2)
        context.ctx.fillStyle = 'rgba(100, 100, 255,' + (1 - t) + ')'
        context.ctx.fill()
        // 绘制内圆
        context.ctx.beginPath()
        context.ctx.arc(0, 0, radius, Math.PI / 4, Math.PI * 7 / 4)
        context.ctx.fillStyle = 'rgba(100, 100, 255, 1)'
        context.ctx.strokeStyle = 'white'
        context.ctx.lineWidth = 2 + (1 - t)
        // 表示方向的角
        context.ctx.lineTo(1.6 * radius, 0)
        context.ctx.closePath()
        context.ctx.fill()
        context.ctx.stroke()
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|IconJumpAnimate.draw|context(Object)|-|canvas会调用draw方法绘制该动画|
|LoadingAnimate.show|-|-|显示加载图片|
|LoadingAnimate.close|-|-|关闭加载图片|
|LoadingAnimate.draw|context(Object)|-|绘制loading动画|
|CurrentPosAnimate.setCurrentPosition|currentPosition(Object)|-|设置当前位置|
|CurrentPosAnimate.draw|context(Object)|-|绘制当前位置动画|

### clean.js

> 清除对象属性

*代码*

```javascript
export default {
    clearUndefined(obj) {
        Object.keys(obj).forEach(k => {
            obj[k] === undefined && delete obj[k]
            obj[k] && typeof obj[k] === 'object' && this.clearUndefined(obj[k])
        })
    },
    clearAll(obj) {
        Object.keys(obj).forEach(k => obj[k] = null)
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|clearUndefined|obj(Object)|-|删除对象中值为undefined的属性|
|clearAll|obj(Object)|-|设置在对象中所有属性值为null|

### clone.js

> 拷贝对象

*代码*

```javascript
import cloneDeep from 'lodash/cloneDeep'

export default {
    copy(obj) {
        return JSON.parse(JSON.stringify(obj))
    },
    merge(target) {
        for (let i = 1, j = arguments.length; i < j; i++) {
            const source = arguments[i] || {};
            for (const prop in source) {
                if (source.hasOwnProperty(prop)) {
                    const value = source[prop];
                    if (value !== undefined) {
                        target[prop] = value;
                    }
                }
            }
        }
        return target
    },
    deepCopy(obj) {
        return cloneDeep(obj)
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|copy|obj(Object)|Object|复制obj并返回。
|merge|target(Object,可变参数列表)|Object|多个对象合并为一个对象并返回|

注：此拷贝不会复制obj中的任何函数(方法)

### encryption.js

> 加/解密(基于base64的字符编码)

*代码*

```javascript
// 字符串转base64
export default {
    encode(str) {
        return str ? btoa(encodeURI(str)) : null
    },
    decode(base64) {
        return base64 ? decodeURI(atob(base64)) : null
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|encode|str(String)|String / null|对str编码并返回加密后base64字符串|
|decode|base64(String)|String / null|对base64解码并返回解密后的字符串|

注：当参数为null或undefined时返回值为null

### events.js

> 事件管理（事件注册/注销/触发)

*代码*

```javascript
import {isFunction} from './types'

export default {
    events: [],
    bind(func, ns = 'cross') {
        isFunction(func) && this.events.push({event: func, namespace: ns})
    },
    remove(ns = 'cross') {
        const index = this.events.findIndex(item => item.namespace === ns)
        index !== -1 && this.events.splice(index, 1)
    },
    clear() {
        this.events.length = 0
    },
    emit(arg) {
        this.events.forEach(item => item.event(arg))
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|bind|func(Function),ns(String)|null|向事件队列注册事件|
|remove|ns(String)|null|从事件队列移除事件|
|clear|null|null|清除事件队列所有事件|
|emit|arg(Any)|null|触发已注册的事件|

### format.js

> 格式化，实现占位符"{}"的内容替换，在locale/index.js被引用(目前没有实际使用)。

*代码*

```javascript
import { hasOwn } from './util';

const RE_NARGS = /(%|)\{([0-9a-zA-Z_]+)\}/g;
/**
 *  String format template
 *  - Inspired:
 *    https://github.com/Matt-Esch/string-template/index.js
 */
export default function(Vue) {

  /**
   * template
   *
   * @param {String} string
   * @param {Array} ...args
   * @return {String}
   */

  function template(string, ...args) {
    if (args.length === 1 && typeof args[0] === 'object') {
      args = args[0];
    }

    if (!args || !args.hasOwnProperty) {
      args = {};
    }

    return string.replace(RE_NARGS, (match, prefix, i, index) => {
      let result;

      if (string[index - 1] === '{' &&
        string[index + match.length] === '}') {
        return i;
      } else {
        result = hasOwn(args, i) ? args[i] : null;
        if (result === null || result === undefined) {
          return '';
        }

        return result;
      }
    });
  }

  return template;
}
```

### network.js

> 网络状态处理(为监听联网状态事件注册回调函数)

*代码*

```javascript
export default {
    init(offline, online) {
        window.addEventListener("offline", offline)
        window.addEventListener("online", online)
    },
    uninit(offline, online) {
        window.removeEventListener("offline", offline)
        window.removeEventListener("online", online)
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|init|offline(Function),online(Function)|-|监听网络断开和连接事件，并注册回调函数|
|uninit|offline(Function,online(Function))|-|移除网络断开和连监听事件|

### preview.js

> 图片预览

*代码*

```javascript
import $ from 'jquery'
import '@fancyapps/fancybox'

export default {
    fancyBox (group = '') {
        $(`[data-fancybox${ group ? ('="'+ group + '"') : '' }]`).fancybox({
            buttons: ['zoom', 'slideShow', 'thumbs', 'close']
        })
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|fancyBox|-|-|开启图片预览功能|

注：该方法针对具有'data-fancybox'属性，并配置有data-src属性或href,src属性的元素提供图片预览。

### query.js

> 数据查询

*代码*

```javascript
import {isObject, isString, isArray} from './types'

export const getQueryObj = (value) => {
    const obj = {}

    function setParams(val) {
        const params = val.split('=')
        if (params.length > 1) {
            obj[params[0]] = params[1]
        }
    }

    if (isString(value)) {
        if (value.indexOf('&') !== -1) {
            value.split('&').forEach(item => {
                setParams(item)
            })
        } else {
            setParams(value)
        }
    }
    return obj
}

export const getValueInArray = (list, key, source, children, result) => {
    if (!isArray(list)) {
        return
    }
    for (let i = 0; i < list.length; i++) {
        if (list[i][source] === key) {
            Object.assign(result, list[i])
            break
        }
        if (list[i][children]) {
            getValueInArray(list[i][children], key, source, children, result)
        }
    }
}

export const setObjQuery = (obj) => {
    let url = ''
    isObject(obj) && Object.keys(obj).forEach(k => {
        url += `${k}=${obj[k]}`
    })
    return url
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|getQueryObj|value(String)|Object|转换url中的query(键值对字符串)为Object对象|
|getValueInArray|list(Array),key(String),source(String),children(Array),result(Object)|-|筛选list中各项及其children各子项的source属性值为key的项，并赋值给result对象|
|setObjQuery|obj(Object)|String|转换Object对象为query(键值对字符串)|

### screen.js

> 窗体控制

*代码*

```javascript
import $ from 'jquery'

export default {
    launch () {
        if (window.ActiveXObject) {
            const WsShell = new window.ActiveXObject('WScript.Shell')
            WsShell.SendKeys('{F11}')
        } else if (document.documentElement.requestFullscreen) {
            document.documentElement.requestFullscreen()
        } else if (document.documentElement.mozRequestFullScreen) {
            document.documentElement.mozRequestFullScreen()
        } else if (document.documentElement.msRequestFullscreen) {
            document.documentElement.msRequestFullscreen()
        } else if (document.documentElement.webkitRequestFullscreen) {
            document.documentElement.webkitRequestFullscreen()
        }
    },
    exit () {
        if (!this.isFullScreen()) {
            return
        }
        if (window.ActiveXObject) {
            const WsShell = new window.ActiveXObject('WScript.Shell')
            WsShell.SendKeys('{F11}')
        } else if (document.cancelFullScreen) {
            document.cancelFullScreen()
        } else if (document.exitFullscreen) {
            document.exitFullscreen()
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
            document.msExitFullscreen()
        } else if (document.webkitExitFullscreen) {
            document.webkitExitFullscreen()
        }
    },
    isFullScreen () {
        return document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement
    },
    // 监听全屏状态改变后的回调
    listen (callback, flag, namespace) {
        const events = 'fullscreenchange webkitfullscreenchange mozfullscreenchange MSFullscreenChange resize '
        $(window).on(namespace ? events.replace(/\s/g, '.' + namespace + ' ') : events, () => {
            const fullscreenElement = document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement
            if (!fullscreenElement || flag) {
                callback()
            }
        })
    },
    removeListen (namespace) {
        $(window).off('.' + namespace)
    }
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|launch|-|-|开启window全屏模式|
|exit|-|-|退出window全屏模式|
|isFullScreen|-|Boolean|当前window是否在全屏模式下|
|listen|callback(Function),flag(Boolean),namespace(String)|-|注册监听window全屏模式事件回调函数，支持函数的名字空间，回调函数默认在退出全面模式时才调用，如果flage为true，则不管是否在全屏模式下回调函数都会调用|
|removeListen|namespace(String)|-|移除window全屏模式监听事件的回调函数,支持函数的名字空间

### trajectory.js

> 地图坐标轨迹

*代码*

```javascript
class Trajectory {
    constructor(coordinate) {
        this.coordinate = coordinate || []
        this.curentLocationXY = null // 设备当前位置xyyaw time坐标
    }

    clear() {
        this.coordinate = []
    }

    transformSportXY(sport) {
        // 兴趣点为像素坐标时
        if (Array.isArray(sport)) {
            const [x, y, orientation] = sport
            return [x, y, orientation]
        } else if (typeof sport === "object") {
            const {x, y, orientation} = sport
            return [x, y, orientation]
        }
        return []
    }
    // 地图轨迹线段记录
    addCoordinate(sport) {
        // sport为空但list不为空时将list赋值coordinate
        if (!sport || !sport.length) {
            return
        }
        if (this.coordinate.length) {
            const lastIndex = this.coordinate.length - 1
            this.coordinate = this.coordinate.concat([[this.coordinate[lastIndex][1], this.transformSportXY(sport)]])
        } else {
            this.coordinate = this.coordinate.concat([[this.transformSportXY(sport), this.transformSportXY(sport)]])
        }
        // 每次更新设备轨迹时记录当前点
        this.curentLocationXY = sport
    }
}

export default Trajectory
```


### types.js

> 类型判断

*代码*

```javascript
export const isString = (obj) => {
  return Object.prototype.toString.call(obj) === '[object String]';
}

export const isObject = (obj) => {
  return Object.prototype.toString.call(obj) === '[object Object]';
}

export const isNumber = (obj) => {
  return Object.prototype.toString.call(obj) === '[object Number]';
}

export const isArray = (obj) => {
  return obj instanceof Array
}

export const isHtmlElement = (node) => {
  return node && node.nodeType === Node.ELEMENT_NODE;
}

export const isFunction = (functionToCheck) => {
  const getType = {};
  return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
}

export const isUndefined = (val)=> {
  return val === void 0;
}

export const isDefined = (val) => {
  return val !== undefined && val !== null;
}

export const isDate = (val) => {
    return val instanceof Date
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|isString|obj(-)|Boolean|obj是否为String|
|isObject|obj(-)|Boolean|obj是否为Object|
|isNumber|obj(-)|Boolean|obj是否为Number|
|isArray|obj(-)|Boolean|obj是否为Array|
|isHtmlElement|node(-)|Boolean|node是否为HTML元素|
|isFunction|functionToCheck(-)|Boolean|functionToCheck是否为Function|
|isUndefined|val(-)|Boolean|val是否为undefined|
|isDefined|val(-)|Boolean|val是否不为undefined和null|

### util.js

> 通用帮助类

*代码*

```javascript
import { isString, isObject } from './types';

const hasOwnProperty = Object.prototype.hasOwnProperty;

export function noop() {}

export function hasOwn(obj, key) {
  return hasOwnProperty.call(obj, key);
}

function extend(to, _from) {
  for (let key in _from) {
    to[key] = _from[key];
  }
  return to;
}

export function toObject(arr) {
  var res = {};
  for (let i = 0; i < arr.length; i++) {
    if (arr[i]) {
      extend(res, arr[i]);
    }
  }
  return res;
}

export const getValueByPath = function(object, prop) {
  prop = prop || '';
  const paths = prop.split('.');
  let current = object;
  let result = null;
  for (let i = 0, j = paths.length; i < j; i++) {
    const path = paths[i];
    if (!current) break;

    if (i === j - 1) {
      result = current[path];
      break;
    }
    current = current[path];
  }
  return result;
};

export function getPropByPath(obj, path, strict) {
  let tempObj = obj;
  path = path.replace(/\[(\w+)\]/g, '.$1');
  path = path.replace(/^\./, '');

  let keyArr = path.split('.');
  let i = 0;
  for (let len = keyArr.length; i < len - 1; ++i) {
    if (!tempObj && !strict) break;
    let key = keyArr[i];
    if (key in tempObj) {
      tempObj = tempObj[key];
    } else {
      if (strict) {
        throw new Error('please transfer a valid prop path to form item!');
      }
      break;
    }
  }
  return {
    o: tempObj,
    k: keyArr[i],
    v: tempObj ? tempObj[keyArr[i]] : null
  };
}

export const generateId = function() {
  return Math.floor(Math.random() * 10000);
};

export const valueEquals = (a, b) => {
  // see: https://stackoverflow.com/questions/3115982/how-to-check-if-two-arrays-are-equal-with-javascript
  if (a === b) return true;
  if (!(a instanceof Array)) return false;
  if (!(b instanceof Array)) return false;
  if (a.length !== b.length) return false;
  for (let i = 0; i !== a.length; ++i) {
    if (a[i] !== b[i]) return false;
  }
  return true;
};

export const escapeRegexpString = (value = '') => String(value).replace(/[|\\{}()[\]^$+*?.]/g, '\\$&');

// TODO: use native Array.find, Array.findIndex when IE support is dropped
export const arrayFindIndex = function(arr, pred) {
  for (let i = 0; i !== arr.length; ++i) {
    if (pred(arr[i])) {
      return i;
    }
  }
  return -1;
};

export const arrayFind = function(arr, pred) {
  const idx = arrayFindIndex(arr, pred);
  return idx !== -1 ? arr[idx] : undefined;
};

// coerce truthy value to array
export const coerceTruthyValueToArray = function(val) {
  if (Array.isArray(val)) {
    return val;
  } else if (val) {
    return [val];
  } else {
    return [];
  }
};

export const autoprefixer = function(style) {
  if (typeof style !== 'object') return style;
  const rules = ['transform', 'transition', 'animation'];
  const prefixes = ['ms-', 'webkit-'];
  rules.forEach(rule => {
    const value = style[rule];
    if (rule && value) {
      prefixes.forEach(prefix => {
        style[prefix + rule] = value;
      });
    }
  });
  return style;
};

export const kebabCase = function(str) {
  const hyphenateRE = /([^-])([A-Z])/g;
  return str
    .replace(hyphenateRE, '$1-$2')
    .replace(hyphenateRE, '$1-$2')
    .toLowerCase();
};

export const capitalize = function(str) {
  if (!isString(str)) return str;
  return str.charAt(0).toUpperCase() + str.slice(1);
};

export const looseEqual = function(a, b) {
  const isObjectA = isObject(a);
  const isObjectB = isObject(b);
  if (isObjectA && isObjectB) {
    return JSON.stringify(a) === JSON.stringify(b);
  } else if (!isObjectA && !isObjectB) {
    return String(a) === String(b);
  } else {
    return false;
  }
};

export const arrayEquals = function(arrayA, arrayB) {
  arrayA = arrayA || [];
  arrayB = arrayB || [];

  if (arrayA.length !== arrayB.length) {
    return false;
  }

  for (let i = 0; i < arrayA.length; i++) {
    if (!looseEqual(arrayA[i], arrayB[i])) {
      return false;
    }
  }

  return true;
};

export const isEqual = function(value1, value2) {
  if (Array.isArray(value1) && Array.isArray(value2)) {
    return arrayEquals(value1, value2);
  }
  return looseEqual(value1, value2);
};

export const isEmpty = function(val) {
  // null or undefined
  if (val == null) return true;

  if (typeof val === 'boolean') return false;

  if (typeof val === 'number') return !val;

  if (val instanceof Error) return val.message === '';

  switch (Object.prototype.toString.call(val)) {
    // String or Array
    case '[object String]':
    case '[object Array]':
      return !val.length;

    // Map or Set or File
    case '[object File]':
    case '[object Map]':
    case '[object Set]': {
      return !val.size;
    }
    // Plain Object
    case '[object Object]': {
      return !Object.keys(val).length;
    }
  }

  return false;
};

export function rafThrottle(fn) {
  let locked = false;
  return function(...args) {
    if (locked) return;
    locked = true;
    window.requestAnimationFrame(_ => {
      fn.apply(this, args);
      locked = false;
    });
  };
}

export function objToArray(obj) {
  if (Array.isArray(obj)) {
    return obj;
  }
  return isEmpty(obj) ? [] : [obj];
}
```

*方法*

|名称|参数|返回|说明|
|:-|:-|:-|:-|
|noop|-|-|不错任何处理|
|hasOwn|obj(Object),key(String)|Boolean|key是否为obj的属性|
|getValueByPath|obj(Obje),prop(String)|obj[prop]|返回porp属性值|
|toObject|arr(Array)|Object|递归arr生成新对象|
|getPropByPath|obj(Object),path(String),strict(Boolean)|Object({o,k,v})|根据path返回obj对象的属性说明信息|
|generateId|-|Number|生成10000以内的随机数|
|valueEquals|a(-),b(-)|Boolean|a，b为数组时是否相同，或a===b|
|escapeRegexpString|value(String)|String|去除value中所有正则表达式字符|
|arrayFindIndex|arr(Array),pred(Function)|Number|根据pred函数(参数为arr的项)判度，返回arr索引或-1|
|arrayFind|arr(Array),pred(Function)|arr[index] / undefined|根据pred函数(参数为arr的项)判度，返回arr索引的值或undefined|
|coerceTruthyValueToArray|val(-)|Array|转换val为数组|
|autoprefixer|style(String)|String|为style中'transform', 'transition', 'animation'加上特定前缀('ms-', 'webkit-')|
|kebabCase|str(String)|String|转str为小写字符串|
|capitalize|str(String)|String|转str首字符为大写的字符串|
|looseEqual|a(-),b(-)|Boolean|a,b为对象或字符时是否相同|
|arrayEquals|a(Array),b(Array)|Boolean|a,b两数组是否相等|
|isEqual|a(-),b(-)|Boolean|a,b为对象或字符串或数组时是否相等|
|isEmpty|val(-)|Boolean|val是否无值|
|rafThrottle|fn(Function)|-|顺序执行fu|
|objToArray|obj(Object)|Array|转obj为数组|
