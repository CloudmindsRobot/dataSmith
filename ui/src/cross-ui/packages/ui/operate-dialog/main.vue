<template>
  <cross-modal :modal="modal"  
               @cancel="cancel"
               @ok="ok">
    <cross-container :has-icon="hasIcon" 
                     @layout="updateLayout">
      <!-- @slot 自定义icon -->
      <slot name="icon"
            slot="icon"/>
      <template slot="content">
        <!-- @slot 自定义内容 -->
        <slot name="extend"/>
        <form-create v-model="fApi" 
                     :rule="rule" 
                     :option="option"/>
      </template>
    </cross-container>
  </cross-modal>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import formCreator from '../../mixin/formCreator'
import dialog from '../../mixin/dialog'

/**
 * 操作弹窗
 *
 * @displayName Operate Dialog
 * @version 1.0
 */
export default {
  name: 'cross-operate-dialog',
  mixins: [formCreator, dialog],
  props: {
    /**
     * 弹窗配置项
     *
     * {title:标题,visible:显示',footer: 底部内容(默认不显示),width:宽度,style:样式,mask:是否显示蒙层,zIndex:层级,destroyOnClose:关闭时销毁实例,confirmLoading:确定按钮loading}
     */
    modal: {
      type: Object,
      default: () => {
        return {
          title: '',
          visible: false,
          footer: null,
          width: '50%',
          style: {},
          mask: true,
          zIndex: 1000,
          destroyOnClose: false,
          confirmLoading: false
        }
      }
    }
  },
  data() {
    return {}
  },
  watch: {
    'modal.visible'(newVal) {
      newVal && this.$nextTick(() => {
        this.updateLayout()
        this.updateRules()
        this.setFormValues(this.dataModel.model)
      })
    }
  }
}
</script>