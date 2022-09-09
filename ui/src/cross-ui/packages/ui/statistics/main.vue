<template>
  <a-row :gutter="15">
    <a-col v-for="v in view"
           :key="v.prop"
           :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" :xs="24"
           class="mb15">
      <a-statistic :precision="v.precision || 0"
                   :group-separator="v.groupSeparator || ','"
                   :decimal-separator="v.decimalSeparator || '.'"
                   :value-style="v.style || undefined"
                   :value="numberFormat(data[v.prop])">
        <!-- @slot 自定义标题 -->
        <slot name="title"
              slot="title">
          <div class="ant-statistic-title">
            {{ isFunction(v.locale) ? v.locale() : $t(v.prop) }}
          </div>
        </slot>
        <!-- @slot 自定义数值前缀 -->
        <slot v-if="v.prefix"
              name="prefix"
              slot="prefix">
          {{ v.prefix }}
        </slot>
        <!-- @slot 自定义数值后缀 -->
        <slot v-if="v.suffix"
              name="suffix"
              slot="suffix">
          {{ v.suffix }}
        </slot>
        <!-- @slot 自定义数值格式化 -->
        <slot v-if="v.format"
              name="format"
              slot="formatter">
          <div :style="v.style"
               v-html="isFunction(v.format) ? v.format(data[v.prop]) : numberFormat(data[v.prop])"/>
        </slot>
      </a-statistic>
    </a-col>
  </a-row>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import types from '../../mixin/types'
import format from '../../mixin/format'

/**
 * 统计数值
 *
 * @displayName Statistics
 * @version 1.0
 */
export default {
  name: 'cross-statistics',
  mixins: [types, format],
  props: {
    /**
     * 数据视图
     *
     * [{prop:属性字段,format:格式化函数,locale:本地化函数,prefix:数值前缀,suffix:数值后缀,groupSeparator:千分位标识符,decimalSeparator:小数点,precision:数值精度}]
     */
    view: {
      type: Array,
      default: () => {
        return []
      }
    },
    /**
     * 数据
     */
    data: {
      type: Object,
      default: () => {
        return {}
      }
    }
  }
}
</script>