<template>
  <a-drawer :visible="isBoolean(config.visible) ? config.visible : false"
            :destroyOnClose="isBoolean(config.destroyOnClose) ? config.destroyOnClose : false"
            :placement="config.placement || 'right'"
            :height="config.height || '30vh'"
            :width="config.width || '30vw'"
            :body-style="{ maxHeight: config.buttons && config.buttons.length ? 'calc(100vh - 108px)' : 'calc(100vh - 56px)', overflowY: 'auto' }"
            :mask="isBoolean(config.mask) ? config.mask : true"
            wrapClassName="cross-drawer"
            @close="onClose">
    <div slot="title"
         v-html="isFunction(config.title) ? config.title() : (isString(config.title) ? $t(config.title) : '')"></div>
    <!-- @slot 自定义抽屉extra -->
    <slot name="extra"
          slot="extra"/>
    <!-- @slot 自定义抽屉内容 -->
    <slot/>
    <div v-if="config.buttons && config.buttons.length"
         class="drawer-footer">
      <a-button v-for="(btn,index) in config.buttons"
                :style="{ marginRight: index !== config.buttons.length - 1 ? '8px' : 0 }"
                :key="index"
                :type="btn.type || 'default'"
                :loading="btn.loading || false"
                @click="isFunction(btn.click) ? btn.click() : () => {}">
        {{ isFunction(btn.label) ? btn.label() : $t(btn.label) }}
      </a-button>
    </div>
  </a-drawer>
</template>

<i18n src="../../locale/share/page.json"></i18n>

<script>
import types from '../../mixin/types'
import Events from '../../utils/events'

/**
 * 抽屉
 *
 * @displayName Drawer
 * @version 1.0
 */
export default {
  name: 'cross-drawer',
  mixins: [types],
  /**
   * 配置项目
   *
   * {visible:Drawer是否可见,destroyOnClose:关闭时销毁Drawer里的子元素,buttons:[{type:类型,label:文本,loading:加载}],placement:抽屉的方向,height:高度, 在placement为top或bottom时使用,width:宽度,title:标题}
   */
  props: {
    config: {
      type: Object,
      default: () => {
        return {
          visible: false,
          destroyOnClose: false,
          buttons: [],
          placement: 'right',
          height: '30vh',
          width: '30vw',
          mask: true,
          title: ''
        }
      }
    }
  },
  data() {
    return {}
  },
  methods: {
    /**
     * 关闭抽屉
     *
     * @param {-}
     */
    onClose() {
      this.config.visible = false
      /**
       * 关闭触发事件
       */
      this.$emit('close')
    }
  },
  created() {
    const namespace = this.isFunction(this.config.title) ? this.config.title() : (this.isString(this.config.title) ? this.config.title : 'cross-drawer')
    Events.bind(this.onClose, namespace)
  },
  beforeDestroy() {
    const namespace = this.isFunction(this.config.title) ? this.config.title() : (this.isString(this.config.title) ? this.config.title : 'cross-drawer')
    Events.remove(namespace)
  }
}
</script>

<style lang="scss">
.cross-drawer {
  .ant-drawer-content-wrapper {
    min-width: auto;
  }
}

.ant-drawer-title {
  margin: 0 0 0 30px !important;
}

.ant-drawer-close {
  right: auto;
  left: 0;
}

.drawer-footer {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 10px 16px;
  text-align: left;
  left: 0;
  border-radius: 0 0 4px 4px
}

@media screen and (max-width: 992px) {
  .cross-drawer {
    .ant-drawer-content-wrapper {
      width: 50vw !important;
    }
  }
}

@media screen and (max-width: 768px) {
  .cross-drawer {
    .ant-drawer-content-wrapper {
      width: 90vw !important;
    }
  }

  .ant-drawer-title {
    margin: 0 !important;
  }

  .ant-drawer-close {
    right: 0;
    left: auto;
  }

  .drawer-footer {
    text-align: right;
  }
}
</style>
