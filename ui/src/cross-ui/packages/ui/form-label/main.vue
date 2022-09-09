<template>
  <div :class="cssClass"
       :style="cssStyle">
    <template v-if="icon">
      <i v-if="icon.indexOf('fa')!==-1"
         :class="item.icon"/>
      <a-icon v-else
              :type="icon"/>
    </template>
    <template v-if="cssStyle || cssClass">
      {{ isFunction(title) ? title() : $t(title) }}
    </template>
    <strong v-else>
      {{ isFunction(title) ? title() : $t(title) }}
    </strong>
  </div>
</template>

<script>
import types from '../../mixin/types'

/**
 * 表单
 *
 * @displayName FormLabel
 * @version 1.0
 * @example [none]
 */
export default {
  name: 'cross-form-label',
  mixins: [types],
  props: {
    /**
     * 标题
     */
    title: {
      type: String | Function,
      default: ''
    },
    /**
     * 图标
     */
    icon: {
      type: String,
      default: ''
    },
    /**
     * 样式
     */
    cssStyle: Object,
    /**
     * 样式类名
     */
    cssClass: {
      type: String,
      default: ''
    }
  },
  data() {
    return {}
  }
}
</script>