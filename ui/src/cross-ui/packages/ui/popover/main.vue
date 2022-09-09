<template>
  <a-popover :trigger="popover.trigger || 'hover'"
             :placement="popover.placement || 'top'"
             :overlayStyle="popover.overlayStyle"
             :overlayClassName="popover.overlayClassName"
             :defaultVisible="popover.defaultVisible || false"
             :autoAdjustOverflow="popover.autoAdjustOverflow || true"
             @visibleChange="togglePopover">
    <template slot="title">
      <!-- @slot 自定义气泡卡片标题(title) -->
      <slot name="title"></slot>
    </template>
    <template slot="content">
      <!-- @slot 自定义气泡卡片主体内容(content) -->
      <slot name="content"></slot>
    </template>
    <!-- @slot 自定义气泡卡片生效的元素(popover) -->
    <slot name="popover"></slot>
  </a-popover>
</template>

<script>
import types from '../../mixin/types'

/**
 * Popover气泡卡片
 *
 * @displayName Popover
 * @version 1.0
 */
export default {
  name: 'cross-popover',
  mixins: [types],
  props: {
    /**
     * Popover气泡卡片配置项
     *
     * {
     * title: 标题,
     * arrowPointAtCenter: 箭头是否指向目标元素中心(默认false),
     * autoAdjustOverflow: 气泡被遮挡时自动调整位置(默认true),
     * defaultVisible: 默认是否显隐(默认false),
     * getPopupContainer: 浮层渲染父节点，默认渲染到 body 上,
     * mouseEnterDelay: 鼠标移入后延时多少才显示 Tooltip，单位：秒,
     * mouseLeaveDelay: 鼠标移出后延时多少才隐藏 Tooltip，单位：秒,
     * overlayClassName: 卡片类名,
     * overlayStyle: 卡片样式,
     * placement: 气泡框位置，可选 top/left/right/bottom/topLeft/topRight/bottomLeft/bottomRight/leftTop/leftBottom/rightTop/rightBottom,
     * trigger: 触发行为，可选 hover/focus/click/contextmenu,
     * visible(v-model): 用于手动控制浮层显隐(默认false),
     * destroyTooltipOnHide: 隐藏后是否销毁 tooltip
      }
     */
    popover: {
      type: Object,
      default: () => {
        return {
          title: '',
          arrowPointAtCenter: false,
          autoAdjustOverflow: true,
          defaultVisible: false,
          getPopupContainer: Function,
          mouseEnterDelay: 0,
          mouseLeaveDelay: 0.1,
          overlayClassName: 'cross-popover',
          overlayStyle: {},
          placement: 'top',
          trigger: 'hover',
          visible: false,
          destroyTooltipOnHide: false
        }
      }
    }
  },
  data() {
    return {}
  },
  methods: {
    /**
     * 显示/隐藏的回调
     *
     * @param {boolean} visible 显示/隐藏值
     */
    togglePopover(visible) {
      /**
       * 显示/隐藏触发事件
       *
       * @property {boolean} visible 显示/隐藏值
       */
      this.$emit('togglePopover', visible)
    }
  }
}
</script>