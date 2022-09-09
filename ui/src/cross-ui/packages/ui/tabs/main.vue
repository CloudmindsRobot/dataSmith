<template>
  <div>
    <a-tabs v-model="tabPanel.key"
            :tab-position="tabPanel.mode || 'top'"
            :type="tabPanel.type || 'line'"
            size="small">
      <a-tab-pane v-for="tab in tabPanel.tabs"
                  :disabled="tab.disabled"
                  :key="tab.key">
        <slot :name="`${tab.key}_tab`"
              slot="tab">
          {{isFunction(tab.locale) ? tab.locale() : $t(tab.key)}}
        </slot>
        <!-- @slot 自定义Tab内容 -->
        <slot v-if="!tabPanel.dynamic"
              :name="tab.key"/>
      </a-tab-pane>
      <slot name="tabBarExtraContent"
            slot="tabBarExtraContent"/>
    </a-tabs>
    <keep-alive v-if="tabPanel.dynamic">
      <component :is="tabPanel.key"/>
    </keep-alive>
  </div>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import types from '../../mixin/types'

/**
 * Tabs页签
 *
 * @displayName Tabs
 * @version 1.0
 */
export default {
  name: 'cross-tabs',
  mixins: [types],
  props: {
    /**
     * Tabs页签配置
     *
     *{dynamic:动态加载组件,key:当前选中的Tab页签,tabs:[{key:页签键,disabled:禁用,locale:本地化语言函数}],mode:页签位置,type:类型}
     */
    tabPanel: {
      type: Object,
      default: () => {
        return {
          dynamic: false,
          key: '',
          tabs: [],
          mode: 'top',
          type: 'line'
        }
      }
    }
  }
}
</script>