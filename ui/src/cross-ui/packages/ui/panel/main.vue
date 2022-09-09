<template>
  <div class="cross-panel">
      <a-card :headStyle="headStyle" 
              :bodyStyle="bodyStyle"
              :bordered="bordered">
        <div v-if="title"
             slot="title">
          <div class="cross-panel-title"></div>  
          <div v-html="isFunction(title) ? title() : $t(title)"></div>
        </div>        
        <slot name="extra" 
              slot="extra"/>
        <slot/>      
      </a-card>
  </div>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import types from '../../mixin/types'

/**
 * 面板
 *
 * @displayName Panel
 * @version 1.0
 */
export default {
  name: 'cross-panel',
  mixins: [types],
  props: {
    /**
     * 主体样式
     *
     * {padding:内间距}
     */
    bodyStyle: {
      type: Object,
      default: () => {
        return {padding: '15px'}
      }
    },
    /** 
     * 标题
    */
    title: {
      type: String,Function,
      default: ''
    },
    /** 
     * 边框
     * 
     * @values true,false
    */
    bordered: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      headStyle: {
        padding: '0px 15px'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.cross-panel-title {
  position: absolute; 
  width: 4px; 
  height: 16px; 
  margin: 5px 0 0 -16px; 
  background-color:#1890ff;
  border: #1890ff solid 1px;
  border-radius: 2px;
}
</style>
