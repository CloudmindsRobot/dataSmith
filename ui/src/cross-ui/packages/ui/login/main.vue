<template>
  <div>
    <h2 class="text-center">
      {{ $t(title) }}
    </h2>
    <br>
    <form-create v-model="fApi"
                 :rule="rule"
                 :option="option"/>
  </div>
</template>

<i18n>
{
  "zh-CN": {
    "account": "账号",
    "accountRequired": "账号不能为空",
    "login": "登录",
    "password": "密码",
    "passwordRequired": "密码不能为空",
    "remember_password": "记住密码 ",
    "valid": "用户凭证已失效，请重新登录。",
    "datasmith": "Data Smith",
    "loading": "加载中..."
  }
}
</i18n>

<script>
import Vue from 'vue'
import formCreator from '../../mixin/formCreator'
import Form from '../../tools/form'
import Seting from '../../tools/seting'
import Config from '../../tools/config'
import User from '../../tools/user'
// import Encryption from '../../utils/encryption'
import confirm from '../../mixin/confirm'

/**
 * 登录
 *
 * @displayName Login
 * @version 1.0
 * @example [none]
 */
export default {
  name: 'cross-login',
  mixins: [formCreator, confirm],
  props: {
    /**
     * 登录API
     */
    api: {
      type: String,
      default: `${Config.baseUrlPrefix[Config.platform]}auth/userLogin`
    },
    /**
     * 默认路由(登录后路由地址)
     */
    path: {
      type: String,
      default: 'master'
    },
    /**
     * 标题(系统名称)
     */
    title: {
      type: String,
      default: 'datasmith'
    }
  },
  data() {
    const localRemember = Vue.localStorage.get(Config.remember)
    const remembers = new Map(localRemember ? JSON.parse(localRemember) : [])
    this.option.form.layout = 'vertical'
    this.option.resetBtn.show = false
    this.option.submitBtn.innerText = 'login'
    this.option.submitBtn.icon = undefined
    this.option.submitBtn.block = this.option.submitBtn.show = true
    this.option.onSubmit = (formData) => {
      if (!this.api) {
        return
      }
      // const password = Encryption.encode(formData.password)
      this.option.submitBtn.loading = true
      this._http({
        url: this.api,
        method: 'POST',
        data: {
          ...formData
          // grant_type: 'password'
        },
        params: null
      }, undefined, false).then(response => {
        this.option.submitBtn.loading = false
        if (!response.data) {
          this.$message.error(response.message)
          return
        }
        const token = response.data.userName + ' ' + response.data.loginTime
        if (Seting.get(Config.tokenKey) !== token) {
          Config.systems[Config.platform].forEach((item) => {
            Vue.localStorage.remove(item + '_menu')
          })
        }
        Seting.set(token, Config.tokenKey)
        Seting.set(new User(response.data.userName, response.data.userName), Config.user)
        if (formData.remember) {
          remembers.set(formData.username, password)
          Vue.localStorage.set(Config.remember, JSON.stringify([...remembers]))
        }
        this.$router && this.$router.push({name: this.path})
      }).catch((error) => {
        this.option.submitBtn.loading = false
        this.error({status: error.status})
      })
    }
    const account = Form.components.autoComplete()
    account.value = 'admin'
    account.field = account.props.placeholder = 'account'
    account.children = [{
      type: 'input',
      children: [{type: 'i', class: 'fa fa-user', slot: 'prefix'}],
      props: {maxLength: 32}
    }]
    account.validate = [{required: true, message: 'accountRequired', trigger: 'change'}]
    // account.props.dataSource = []
    // account.on.search = (inject, value) => {
    //   const keys = [...remembers.keys()]
    //   inject.self.props.dataSource = value ? keys.filter(k => k.indexOf(value) !== -1) : []
    // }
    // account.on.change = (inject, value) => {
    //   this.fApi.setValue({password: Encryption.decode(remembers.get(value))})
    // }
    const password = Form.components.password()
    password.value = '123456'
    password.field = 'password'
    password.props.placeholder = 'password'
    password.props.visibilityToggle = true
    password.props.maxLength = 32
    password.children = [{type: 'i', class: 'fa fa-key', slot: 'prefix'}]
    password.validate = [{required: true, message: 'passwordRequired', trigger: 'change'}]
    const remember = Form.components.switch()
    remember.field = 'remember'
    remember.prefix = 'remember_password'
    this.rule.push(account)
    this.rule.push(password)
    // this.rule.push(remember)
    return {}
  },
  mounted() {
    this.updateRules()
    if (Seting.get(Config.user)) {
      this.$router && this.$router.push({name: this.path})
    }
  }
}
</script>
