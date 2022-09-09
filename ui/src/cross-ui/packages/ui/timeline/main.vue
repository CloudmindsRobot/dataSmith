<template>
  <div>
    <a-timeline v-if="timeline.length"
                :reverse="config.reverse || false"
                :mode="config.mode || 'left'">
      <!-- @slot 指定最后一个幽灵节点是否存在或内容-->
      <slot v-if="config.pending"
            slot="pending"
            name="pending">
        {{ $t(isString(config.pending) ? config.pending : 'loading') }}
      </slot>
      <!-- @slot 当最后一个幽灵节点存在時，指定其时间图点 -->
      <slot slot="pendingDot"
            name="pendingDot">
        <a-icon type="loading"/>
      </slot>
      <a-timeline-item v-for="item in timeline"
                       :color="item.color || 'blue'"
                       :key="item.date">
        <!-- @slot 自定义时间轴点 -->
        <slot v-if="item.icon"
              slot="dot"
              name="dot">
          <i v-if="item.icon.indexOf('fa')!==-1"
             :class="['fa', item.icon]"/>
          <a-icon v-else
                  :type="item.icon"/>
        </slot>
        <div class="font12">
          {{ numberFormat(item.date) }}
        </div>
        <strong class="font16">
          {{ numberFormat(item.title) }}
        </strong>
        <div v-html="isFunction(item.format) ? item.format(item.content) : numberFormat(item.content)"/>
        <!-- @slot 自定义扩展内容 -->
        <slot name="extension"
              :slot-scope="item"/>
      </a-timeline-item>
    </a-timeline>
    <a-empty v-else-if="!config.pending && !timeline.length"
             :image="emptyImage"/>
  </div>
</template>

<i18n>
{
  "zh-CN": {
    "loading": "加载中..."
  }
}
</i18n>

<script>
import types from '../../mixin/types'
import empty from '../../mixin/empty'
import format from '../../mixin/format'

/**
 * 时间线
 *
 * @displayName Timeline
 * @version 1.0
 */
export default {
  name: 'cross-timeline',
  mixins: [types, empty, format],
  props: {
    /**
     * 时间线数据集合
     *
     * [{date:日期,content:内容,icon:图标,color:图标颜色,format:格式化函数}]
     */
    timeline: {
      type: Array,
      default: () => {
        return []
      }
    },
    /**
     * 时间线配置项
     *
     * {pending:最后一个幽灵节点是否存在或内容,reverse:倒序,mode:模式}
     */
    config: {
      type: Object,
      default: () => {
        return {
          pending: false,
          reverse: false,
          mode: 'left'
        }
      }
    }
  }
}
</script>
