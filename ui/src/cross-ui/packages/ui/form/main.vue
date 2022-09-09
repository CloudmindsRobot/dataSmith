<template>
  <div class="cross-form">
    <a-page-header v-if="modal.title"
                   :title="isFunction(modal.title) ? modal.title() : $t(modal.title)"
                   @back="isFunction(modal.back) ? modal.back() : cancel()"
                   class="steps-content">
      <slot slot="backIcon"
            name="backIcon">
        <a-button icon="arrow-left"/>
      </slot>
    </a-page-header>
    <div class="form-panel"
         :style="style">
      <form-create v-model="fApi"
                   :rule="rule"
                   :option="option"/>
      <slot/>
    </div>
    <template v-if="modal.height">
      <a-divider/>
      <div class="text-right">
        <a-button @click="cancel"
                  class="mr16">
          {{$t('cancel')}}
        </a-button>
        <a-button type="primary"
                  @click="isFunction(modal.ok) ? modal.ok(fApi) : ok()"
                  :disabled="modal.disabled"
                  :loading="modal.confirmLoading">
          {{$t(modal.submit || 'submit')}}
        </a-button>
      </div>
    </template>
  </div>
</template>

<i18n src="../../locale/share/form.json"/>

<script>
import formCreator from '../../mixin/formCreator'
import dialog from '../../mixin/dialog'

/**
 * 表单
 *
 * @displayName Form
 * @version 1.0
 */
export default {
  name: 'cross-form',
  mixins: [formCreator, dialog],
  props: {
    /**
     * 表单样式
     *
     * {height:高度(number),visible:显示(boolean),title:标题(string,function),confirmLoading:提交等待(boolean),disabled:禁用提交(boolean),submit:提交按钮名称(string),back:返回事件,ok:提交事件}
     */
    modal: {
      type: Object,
      default: () => {
        return {
          height: 0,
          visible: false,
          title: '',
          confirmLoading: false,
          disabled: false,
          submit: '',
          back: undefined,
          ok: undefined
        }
      }
    }
  },
  data() {
    return {
      style: {
        height: 'auto'
      }
    }
  },
  watch: {
    'dataModel.model'(newVal, oldVal) {
      newVal !== oldVal && this.setFormValues(newVal)
    },
    'modal.height'(newVal, oldVal) {
      newVal !== oldVal && this.setHeight()
    }
  },
  methods: {
    /**
     * 设置表单高度
     *
     * @param {-}
     */
    setHeight() {
      this.style = {height: this.modal.height ? `${this.modal.height - 60}px` : 'auto'}
    }
  },
  mounted() {
    this.updateLayout()
    this.updateRules()
    this.setHeight()
  }
}
</script>

<style lang="scss">
.cross-form {
  .form-panel{
    overflow-x: hidden;
    overflow-y: auto;
  }

  .ant-divider-horizontal{
    margin: 10px 0;
  }
  .ant-page-header {
    padding: 0 0 10px 0;
    margin-bottom: 24px;

    .ant-page-header-back{
      margin: 0;
      margin-right: 16px;
    }
  }
}
</style>
