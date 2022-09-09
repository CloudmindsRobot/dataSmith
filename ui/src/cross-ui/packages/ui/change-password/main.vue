<template>
  <cross-modal :modal="modal"
               @cancel="onClose"
               @ok="submit">
    <form-create v-model="fApi"
                 :rule="rule"
                 :option="option"/>
    <div class="font12 text-orange">
      {{ $t('passwordComplete') }}
    </div>
  </cross-modal>
</template>

<i18n>
{
  "zh-CN": {
    "submit": "提交",
    "reset": "重置",
    "changePassword": "修改密码",
    "rePassRequired": "两次密码输入不一致",
    "password": "确认密码",
    "newPassword": "新密码",
    "checkPasswordLengthError": "5-32个字符，不含空格",
    "passwordRequired": "密码不能为空",
    "passwordComplete": "注：修改密码后需重新登录系统。"
  }
}
</i18n>

<script>
import formCreator from '../../mixin/formCreator'
import Form from '../../tools/form'
import Config from '../../tools/config'
import encryption from '../../utils/encryption'
import {isFunction} from '../../utils/types'

/**
 * 修改密码
 *
 * @displayName Change Password
 * @version 1.0
 */
export default {
  name: 'cross-change-password',
  props: {
    /**
     * 开启修改密码面板
     *
     * {visible:可见}
     */
    modal: {
      type: Object,
      default: () => {
        return {
          visible: false
        }
      }
    },
    /**
     * 修改密码API
     */
    api: {
      type: String,
      default: `${Config.baseUrlPrefix[Config.platform]}/upms/v1/staff/update_my_pwd`
    },
    /**
     * 修改密码前回调函数
     */
    callback: Function
  },
  mixins: [formCreator],
  data() {
    const passwordRule = {required: true, message: 'passwordRequired', trigger: 'change'}
    const passwordExp = {type: 'string', pattern: /^\S{5,32}$/, message: 'checkPasswordLengthError', trigger: 'change'}

    const newPass = (rule, value, callback) => {
      if (this.fApi.getValue('password') !== '') {
        this.fApi.validateField('password')
      }
      callback()
    }
    const confirm = (rule, value, callback) => {
      if (value !== this.fApi.getValue('newPassword')) {
        callback(new Error(this.$t('rePassRequired')))
        return
      }
      callback()
    }

    const components = [{
      field: 'newPassword',
      validate: [passwordRule, passwordExp, {validator: newPass, trigger: 'change'}]
    },
      {field: 'password', validate: [passwordRule, {validator: confirm, trigger: 'change'}]}]

    components.forEach(c => {
      const _pwd = Form.components.password()
      _pwd.field = _pwd.title = c.field
      _pwd.props.visibilityToggle = true
      _pwd.props.maxLength = 32
      _pwd.validate = c.validate
      this.rule.push(_pwd)
    })
    return {
      config: {
        title: () => {
          return this.$t('changePassword')
        },
        width: '40%',
        confirmLoading: false
      }
    }
  },
  watch: {
    'modal.visible'(newVal) {
      Object.assign(this.modal, this.config)
      newVal && this.$nextTick(() => {
        this.updateLayout()
        this.updateRules()
      })
    }
  },
  methods: {
    /**
     * 关闭修改密码面板
     *
     * @param {-}
     */
    onClose() {
      this.modal.visible = false
      this.clearValidateStates()
    },
    /**
     * 提交
     *
     * @param {-}
     */
    submit() {
      this.fApi && this.fApi.submit(formData => {
        const password = encryption.encode(formData['newPassword']);
        ['newPassword', 'password'].forEach(c => {
          formData[c.field] = password
        })
        isFunction(this.callback) && this.callback(formData)
        if (!this.api) {
          return
        }
        this.config.confirmLoading = true
        this._http({
          url: this.api,
          method: 'POST',
          data: formData,
          params: null
        }).then(response => {
          this.config.confirmLoading = false
          if (response.code !== 0) {
            return
          }
          /**
           * 修改密码完成触发事件
           */
          this.$emit('change')
        }).catch(() => {
          this.config.confirmLoading = false
        })
      })
    }
  }
}
</script>
