<template>
  <div>
    <cross-drawer :config="modal">
      <cross-panel class="mb15"
                   :title="userInfoTitle">
        <cross-descriptions :data-model="userInfo"
                            :view="userInfoView"
                            :columns="columns"
                            size="small"/>
      </cross-panel>
      <template v-if="platform === 'smart'">
        <cross-panel class="mb15"
                    :title="userContactTitle">
          <a-tooltip :title="$t('edit')"
                    slot="extra">
            <a-icon class="iconfont"
                    type="edit"
                    @click="contactModal.visible = true" />
          </a-tooltip>
          <cross-descriptions :data-model="userInfo"
                              :view="userContactView"
                              :columns="columns"
                              size="small"/>
        </cross-panel>
        <cross-panel v-if="showLogo"
                     :title="$t('logo')">
          <a-tooltip :title="$t('edit')"
                    slot="extra">
            <a-icon class="iconfont"
                    type="edit"
                    @click="logoModal.visible = true" />
          </a-tooltip>
          <cross-descriptions :data-model="userInfo"
                              :view="userLogoView"
                              :columns="columns"
                              size="small"/>
        </cross-panel>
      </template>
    </cross-drawer>
    <cross-operate-dialog :modal="contactModal"
                          :rule="contactRule"
                          :api="api.update"
                          :data-model="userInfo"
                          :callback="callbackFunc"
                          @complete="updateComplete"/>
    <cross-operate-dialog :modal="logoModal"
                          :rule="logoRule"
                          :api="api.update"
                          :data-model="userInfo"
                          :callback="callbackFunc"
                          @complete="updateComplete"/>
    <cross-change-password :modal="changePasswordModal"
                           :api="api.changePassword"
                           @change="logout"/>
  </div>
</template>

<i18n>
{
  "zh-CN": {
    "name": "姓名",
    "account": "账户",
    "personalCenter": "个人中心",
    "signOut": "退出登录",
    "tenant": "租户",
    "userInfo": "用户资料",
    "contact": "联系电话",
    "changeContact": "修改联系方式",
    "changeLogo": "修改Logo",
    "changePassword": "修改密码"
  }
}
</i18n>

<script>
import Seting from '../../tools/seting'
import Config from '../../tools/config'
import Form from '../../tools/form'
import File from '../../tools/file'
import logout from '../../mixin/logout'
import userCenter from '../../mixin/userCenter'
import types from '../../mixin/types'

/**
 * 用户信息
 *
 * @displayName User Center
 * @version 1.0
 */
export default {
  name: 'cross-user-center',
  mixins: [logout, userCenter, types],
  props: {
    /**
     * 抽屉配置项
     *
     * {visible:显示抽屉}
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
     * 修改前回调函数
     */
    callback: Function
  },
  data() {
    const beforeUpload = (e, file) => {
      if (!File.validImageFileType(file)) {
        this.$message.error(this.$t('invalidFile'))
        file.status = 'error'
        return false
      }
    }
    const onSuccess = (self, file) => {
      file.name = file.response.data
      file.url = `/cephfile/v1/downloadFile?objectKey=${file.response.data}`
    }
    // const onRemove = (self, file) => {
    //   if (this.userInfo.model.id) {
    //     return
    //   }
    //   const index = file.name.lastIndexOf('=')
    //   this.removeFile(index !== -1 ? file.name.substring(index + 1) : file.name)
    // }
    const id = Form.components.hidden()
    id.field = 'id'
    const logo = Form.components.upload()
    logo.field = 'logo'
    logo.title = 'uploadLogo'
    logo.suffix = {type: 'span', children:['uploadLogoInfo'], class: 'font12'}
    logo.props.action = `${Config.baseUrl}/cephfile/v1/uploadFile`
    logo.props.listType = 'picture-card'
    logo.props.limit = 1
    logo.props.accept = 'image/*'
    logo.props.beforeUpload = beforeUpload
    logo.props.onSuccess = onSuccess
    // logo.props.remove = onRemove
    const phone = Form.components.input()
    phone.field = phone.title = 'phone'
    const notifyPhone = Form.components.input()
    notifyPhone.field = notifyPhone.title = 'notifyPhone'
    phone.validate = notifyPhone.validate = [{
      type: 'string',
      pattern: /^\d{8,11}$/,
      message: 'checkMobileFormatError',
      trigger: 'blur'
    }]
    const contactRule = [id, phone, notifyPhone]
    const logoRule = [id, logo]
    const modal = {
        visible: false,
        confirmLoading: false,
        width: '40%'
    }
    return {
      userInfoView: [{
        prop: 'account', locale: () => {
          return this.$t('account')
        }
      }, {
        prop: 'name', locale: () => {
          return this.$t('name')
        }
      }, {
        prop: 'tenant', locale: () => {
          return this.$t('tenant')
        }, format: () => {
          return this.user.tenantCode ? `${this.user.tenantName}(${this.user.tenantCode})` : Config.emptyText
        }
      }],
      userLogoView: [{
        prop: 'logo',
        format: (value) => {
          return value ? `<a data-fancybox="gallery" data-type="image" data-src="${value}"><img src="${value}" height="30"/></a>` : Config.emptyText
        }
      }],
      userContactView: [{
        prop: 'phone'
      }, {
        prop: 'notifyPhone'
      }],
      userInfo: {
        model: {}
      },
      contactRule,
      logoRule,
      columns: [1, 1, 1],
      contactModal: {
        title: () => {
          return this.$t('changeContact')
        },
        ...modal
      },
      logoModal: {
        title: () => {
          return this.$t('changeLogo')
        },
        ...modal
      },
      changePasswordModal: {
        ...modal
      }
    }
  },
  computed: {
    /**
     * 显示logo面板
     *
     * @returns {boolean}
     */
    showLogo() {
      return Config.platform !== 'boss'
    },
    /**
     * 个人中心基本标题
     *
     * @returns {VueI18n.TranslateResult}
     */
    userInfoTitle() {
      return this.$t('userInfo')
    },
    /**
     * 个人中心联系方式标题
     *
     * @returns {VueI18n.TranslateResult}
     */
    userContactTitle() {
      return this.$t('contact')
    }
  },
  methods: {
    /**
     * 加载
     *
     * @param {-}
     */
    load() {
      this.userInfo.model = {
        account: this.user.account,
        name: this.user.name,
        tenant: this.user.tenantCode,
        phone: this.user.mobile,
        notifyPhone: null,
        logo: this.user.logo
      }
      // this.api.load && this._http({
      //   url: this.api.load,
      //   method: 'GET',
      //   data: null,
      //   params: {
      //     current: 1,
      //     size: Config.pageSize,
      //     id: this.user.tenantId
      //   }
      // }).then(response => {
      //   if (response.code !== 0) {
      //     return
      //   }
      //   if (response.data.records) {
      //     this.userInfo.model = response.data.records[0] ? {
      //       id: response.data.records[0].id,
      //       account: response.data.records[0].name,
      //       name: response.data.records[0].name,
      //       tenant: response.data.records[0].code,
      //       phone: response.data.records[0].phone,
      //       notifyPhone: response.data.records[0].notifyPhone,
      //       logo: response.data.records[0].logo
      //     } : {
      //       account: this.user.account,
      //       name: this.user.name,
      //       tenant: this.user.tenantCode,
      //       phone: this.user.mobile,
      //       notifyPhone: null,
      //       logo: this.user.logo
      //     }
      //   }
      // })
    },
    /**
     * 请求前回调函数
     *
     * @param {object} formData 表单数据
     */
    callbackFunc(formData) {
      formData.name = this.user.tenantName
      formData.logoKey = formData.logo ? formData.logo.split('=')[1] : ''
      delete formData.logo
      delete formData.tenant
      delete formData.account
    },
    /**
     * 修复完成回调函数
     *
     * @param {object} formData 表单数据
     */
    updateComplete(formData) {
      formData.logoKey && (this.userInfo.model.logo = this.user.logo = `/cephfile/v1/downloadFile?objectKey=${formData.logoKey}`)
      this.userInfo.model.phone = this.user.mobile = formData.phone
      this.userInfo.model.notifyPhone = formData.notifyPhone
      this.userInfo.model.name = this.user.name
      Seting.set(this.user, Config.user)
    },
    /**
     * 删除文件
     *
     * @param {string} filename 文件Key
     */
    removeFile(filename) {
      this._http({
        url: this.api.remove,
        method: 'GET',
        data: {objectKey: filename},
      })
    }
  },
  created () {
    const config = {
      title: () => {
        return this.$t('personalCenter')
      },
      buttons: [
      // {
      //   label: () => {
      //     return this.$t('changePassword')
      //   },
      //   click: () => {
      //     this.changePasswordModal.visible = true
      //   }
      // },
      {
        type: 'primary',
        label: () => {
          return this.$t('signOut')
        },
        click: this.logout
      }]
    }
    Object.keys(config).forEach(k => {
      if(!this.isDefined(this.modal[k])) {
         this.modal[k] = config[k]
      }
    })
    this.load()
  }
}
</script>
