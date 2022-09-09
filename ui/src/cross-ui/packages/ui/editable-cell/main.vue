<template>
  <div class="editable-cell">
    <div class="editable-cell-text-wrapper">
      <!-- @slot 编辑内容插槽 -->
      <slot></slot>
    </div>
    <span style="flex: 0 0 auto;">
      <template v-if="editable">
         <a-tooltip>
          <template slot="title">
            {{ $t('submit') }}
          </template>
          <a-icon type="check"
                  class="iconfont icon-bg icon-check"
                  @click="check"/>
          </a-tooltip>
          <span class="dividing-line"></span>
          <a-tooltip>
            <template slot="title">
              {{ $t('cancel') }}
            </template>
            <a-icon type="close"
                    class="iconfont icon-bg icon-close"
                    @click="cancel"/>
        </a-tooltip>
      </template>
      <a-tooltip v-show="!editable">
        <template slot="title">
          {{ $t('edit') }}
        </template>
        <a-icon type="edit"
                :class="['iconfont', 'icon-bg', 'icon-edit', {'show': autoShow}]"
                @click="edit"/>
      </a-tooltip>
    </span>
  </div>
</template>

<i18n src="../../locale/share/form.json"></i18n>

<script>
/**
 * 编辑
 *
 * @displayName Editable Cell
 * @version 1.0
 * @example [none]
 */
export default {
  name: 'cross-editable-cell',
  props: {
    /**
     * 当前组件是否是编辑状态
     *
     * @values false,true
     */
    editStatus: { //
      type: Boolean,
      default: false
    },
    /**
     * 是否显示编辑按钮，false时为鼠标上衣显示编辑按钮
     *
     * @values false,true
     */
    autoShow: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      editable: this.editStatus
    }
  },
  methods: {
    /**
     * 提交
     *
     * @param {-}
     */
    check() {
      this.editable = false
      /**
       * 触发提交事件
       *
       * @property {boolean} checked 勾选中
       */
      this.$emit('confirm', true)
    },
    /**
     * 切换编辑状态
     *
     * @param {-}
     */
    edit() {
      this.editable = true
      /**
       * 触发编辑事件
       */
      this.$emit('edit')
    },
    /**
     * 取消
     *
     * @param {-}
     */
    cancel() {
      this.editable = false
      /**
       * 触发取消事件
       */
      this.$emit('cancel')
    }
  }
}
</script>

<style lang="scss">
.editable-cell {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .editable-cell-text-wrapper {
    flex: 1 1 auto;
  }

  .dividing-line {
    border-right: 1px solid #F0F0F0;
  }

  .iconfont {
    display: inline-block;
    font-size: 14px;
    margin: 0 4px;
    cursor: pointer;
  }

  .icon-check {
    color: #108ee9;
  }

  .icon-edit {
    visibility: hidden;

    &.show {
      visibility: visible;
    }
  }

  &:hover .icon-edit {
    visibility: visible;
  }
}

</style>