<template>
  <codemirror v-bind="$attrs"
              v-on="$listeners"
              :options="options"/>
</template>

<script>
import Theme from '../../tools/theme'
import {codemirror} from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/theme/3024-night.css'
import 'codemirror/addon/display/placeholder.js'
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/mode/sql/sql.js'

/**
 * 图表
 *
 * @displayName Codemirror
 * @version 1.0
 */
export default {
  name: 'cross-codemirror',
  components: {
    codemirror
  },
  props: {
    /**
     * 事件名字空间
     */
    ns: {
      type: String,
      default: 'cross-codemirror'
    },
    /**
     * 配置项
     *
     * {tabSize:tab缩进,theme:主题,lineNumbers:行号,lineWrapping:行,placeholder:空内容提示字符,mode:模式,viewportMargin:视图间距}
     */
    options: {
      type: Object,
      default: () => {
        return {
          tabSize: 2,
          theme: 'default',
          lineNumbers: false,
          placeholder: '',
          mode: 'application/json',
          lineWrapping: true,
          viewportMargin: Infinity
        }
      }
    }
  },
  data() {
    return {}
  },
  methods: {
    /**
     * 修改主题
     *
     * @param {string} theme 主题
     */
    changeTheme() {
      this.options.theme = Theme.getCurrentTheme() === 'dark' ? '3024-night' : 'default'
    }
  },
  created() {
    Theme.bindEvent(this.changeTheme, this.ns || 'cross-codemirror')
  },
  mounted() {
    this.changeTheme()
  },
  beforeDestroy() {
    Theme.removeEvent(this.ns || 'cross-codemirror')
  }
}
</script>
