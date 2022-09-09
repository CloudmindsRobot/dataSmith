<template>
  <a-row class="cross-container"
         :gutter="hasIcon.show ? 5 : 0">
    <a-col v-if="hasIcon.show && (!hasIcon.align || hasIcon.align === 'left')"
           :xxl="columns[0]" :xl="columns[0]" :lg="columns[0]" :md="0" :sm="0" :xs="0">
      <!-- @slot 自定义图示 -->
      <slot name="icon"/>
    </a-col>
    <a-col :xxl="hasIcon.show ? columns[1] : 24" :xl="hasIcon.show ? columns[1] : 24"
           :lg="hasIcon.show ? columns[1] : 24" :md="24" :sm="24" :xs="24">
      <a-row>
        <a-col :span="hasIcon.treePanel ? 24 : 0">
          <!-- @slot 自定义内容 -->
          <slot name="top"/>
        </a-col>
        <a-col :span="24">
          <a-button v-if="hasIcon.show && !hasIcon.treePanel"
                    class="icon-button"
                    :style="{right: hasIcon.align === 'right' ? '0' : 'auto'}"
                    @click="onClick"
                    :icon="hasIcon.icon || 'picture'"/>
          <a-drawer :visible="visible"
                    @close="visible = false"
                    :get-container="false"
                    :wrap-style="{left: placement === 'left' ? 0 : 'auto'}"
                    :width="'90%'"
                    :placement="placement || 'right'">
            <template v-if="title"
                      slot="title">
              {{ $t(title) }}
            </template>
            <!-- @slot 自定义图示 -->
            <slot name="icon"/>
          </a-drawer>
          <!-- @slot 自定义内容 -->
          <slot name="content"/>
        </a-col>
      </a-row>
    </a-col>
    <a-col v-if="hasIcon.show && hasIcon.align === 'right'"
           :xxl="columns[0]" :xl="columns[0]" :lg="columns[0]" :md="0" :sm="0" :xs="0">
      <!-- @slot 自定义图示 -->
      <slot name="icon"/>
    </a-col>
  </a-row>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import Screen from '../../utils/screen'
import Device from '../../tools/device'

/**
 * 容器
 *
 * @displayName Container
 * @version 1.0
 */
export default {
  name: 'cross-container',
  props: {
    /**
     * 含有图示
     *
     * {show:显示,icon:图标,align:对齐,treePanel:针对树结构数据面板}
     */
    hasIcon: {
      type: Object,
      default: () => {
        return {
          show: false,
          icon: 'picture',
          align: 'left',
          treePanel: false
        }
      }
    },
    /**
     * 分列大小
     *
     * @values [8,16]
     */
    columns: {
      type: Array,
      default: () => {
        return [8, 16]
      }
    },
    /**
     * 折向
     *
     * @values right,left
     */
    placement: {
      type: String,
      default: 'right'
    },
    /**
     * 抽屉标题
     */
    title: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      visible: false
    }
  },
  methods: {
    /**
     * 关闭面板
     *
     * @param {-}
     */
    onClick() {
      this.visible = true
    }
  },
  mounted() {
    Screen.listen(() => {
      const isMobile = Device.isMobile(992)
      !isMobile && this.visible && (this.visible = false)
      /**
       * 布局更改触发事件
       *
       * @property {boolean} isMobile 是否移动端
       */
      this.$emit('layout', isMobile)
    }, true, 'cross-container')
  },
  beforeDestroy() {
    Screen.removeListen('cross-container')
  }
}
</script>

<style lang="scss">
.cross-container {
  .icon-button {
    position: absolute;
    z-index: 5;
    display: none;
  }
}

@media screen and (max-width: 992px) {
  .cross-container {
    .icon-button {
      display: block;
    }
  }
}

//@media screen and (max-width: 768px) {
//  .cross-container {
//    .icon-button {
//      right: 0;
//    }
//  }
//}
</style>