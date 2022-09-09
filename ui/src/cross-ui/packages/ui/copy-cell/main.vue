<template>
  <div class="copy-cell">
    <span style="flex: 0 0 auto;" v-if="position === 'left'">
      <a-tooltip>
        <template slot="title">
          {{ $t('copy') }}
        </template>
        <a-icon type="copy" class="cell-icon iconfont icon-bg" :class="(autoShow ? 'show ' : '' ) + classKey"
          data-clipboard-action="copy"
          @click="copy(classKey)" :data-clipboard-text="copyVal"/>
      </a-tooltip>
    </span>
    <div class="cell-text-wrapper">
      <slot>{{copyVal}}</slot>
    </div>
    <span style="flex: 0 0 auto;" v-if="position === 'right'">
      <a-tooltip>
        <template slot="title">
          {{ $t('copy') }}
        </template>
        <a-icon type="copy" class="cell-icon iconfont icon-bg" :class="(autoShow ? 'show ' : '' ) + classKey"
          data-clipboard-action="copy"
          @click="copy(classKey)" :data-clipboard-text="copyVal"/>
      </a-tooltip>
    </span>
  </div>
</template>

<i18n>
{
  "zh-CN": {
    "copy": "复制",
    "copySucccess": "已复制到粘贴板",
    "copyFailed": "复制出错"
  }
}
</i18n>

<script>
import Clipboard from 'clipboard'
/**
 * 复制按钮
 * 默认显示传入的拷贝内容，也可以插槽自定义与拷贝结果不同的显示内容。
 *
 * @displayName Copy Cell
 * @version 1.0
 */
export default {
  name: 'cross-copy-cell',
  props: {
    /**
     * 需要拷贝的数据值：默认也是元素显示的内容；如需显示与拷贝内容不一致的显示数据，支持插槽模式写入
     */
    copyVal: {
      type: String
    },
    /**
     * 是否显示，默认不显示，鼠标上移触发显示
     *
     * @values false,true
     */
    autoShow: {
      type: Boolean,
      default: false
    },
    /**
     * 拷贝图标显示的位置
     *
     * @values left,right
     */
    position: {
      type: String,
      default: 'right'
    }
  },
  data () {
    return {
      classKey: 'copy-cell-' + parseInt(Math.random() * 100000) // 触发拷贝的元素class，必须唯一性
    }
  },
  methods: {
    /**
     * 点击复制
     *
     * @param {string} cssClass 传入的定义的按钮class
     */
    copy (cssClass) {
      const clipboard = new Clipboard(`.${cssClass}`)
      clipboard.on('success', () => {
        this.$message.success(this.$t('copySucccess'))
        clipboard.destroy()
      })
      clipboard.on('error', () => {
        this.$message.error(this.$t('copyFailed'))
      })
    }
  }
}
</script>
<style lang="scss">
.copy-cell{
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .cell-text-wrapper{
    flex: 1 1 auto;
    word-break: break-all;
  }
  .cell-icon{
    display: inline-block;
    font-size: 14px;
    margin: 0 4px;
    cursor: pointer;
    visibility: hidden;
    &.show{
      visibility: visible;
    }

  }
  &:hover .cell-icon{
    visibility: visible;
  }
}

</style>
