<template>
  <a-modal :visible="modal.visible"
           :footer="modal.footer"
           :width="modal.width || '50%'"
           :mask="isBoolean(modal.mask) ? modal.mask : true"
           :z-index="modal.zIndex || 1000"
           centered
           wrapClassName="cross-modal"
           :body-style="modal.style"
           :destroyOnClose="isBoolean(modal.destroyOnClose) ? modal.destroyOnClose : false"
           :confirmLoading="isBoolean(modal.confirmLoading) ? modal.confirmLoading : false"
           :maskClosable="isBoolean(modal.maskClosable) ? modal.maskClosable : false"
           :cancelText="$t('cancel')"
           :okText="$t('ok')"
           @cancel="onCancel"
           @ok="onOk">
    <template v-if="modal.title" slot="title">
      {{ isFunction(modal.title) ? modal.title() : $t(modal.title) }}
    </template>
    <!-- @slot 自定义内容 -->
    <slot>
      <img class="w100"
           style="max-height: 500px"
           v-if="modal.preview"
           :src="modal.src"/>
    </slot>
    <slot name="footer"
          slot="footer"/>
  </a-modal>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import types from '../../mixin/types'
import modal from '../../mixin/modal'

/**
 * 弹窗
 *
 * @displayName Modal
 * @version 1.0
 */
export default {
  name: 'cross-modal',
  mixins: [modal, types],
  props: {},
  methods: {}
}
</script>

<style lang="scss">
.cross-modal {
  .ant-modal {
    min-width: auto;
    padding-bottom: 0;

    .ant-modal-body {
      max-height: calc(100vh - 160px);
      overflow-x: hidden;
      overflow-y: auto;
    }

    .ant-result{
      padding: 0;
    }
  }
}

@media screen and (max-width: 768px) {
  .cross-modal {
    .ant-modal {
      width: 90% !important;
    }
  }
}
</style>
