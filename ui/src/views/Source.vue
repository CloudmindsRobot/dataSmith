<template>
  <cross-container-details>
    <cross-form v-show="formConfig.visible"
                :rule="formRule"
                :data-model="data"
                :modal="formConfig"
                :api="formApi"
                @complete="onComplete"/>
    <div v-show="!formConfig.visible">
      <cross-search :rule="searchRule"
                    @search="searchHandle"/>
      <cross-page-table :api="api"
                        :config="config"
                        :buttons="buttons"/>
    </div>
    <cross-drawer :config="modal">
      <cross-panel class="mb15"
                   title="basicInfo">
        <cross-descriptions :data-model="data"
                            :view="basicInfoView"
                            :columns="columns"
                            size="small"/>
      </cross-panel>
      <cross-panel class="mb15"
                   title="urlInfo">
        <cross-descriptions :data-model="data"
                            :view="data.model.type === 2 ? urlHttpInfoView : urlInfoView"
                            :columns="columns"
                            size="small">
          <template slot="password"
                    slot-scope="{ slotScope }">
            {{ showPassword ? slotScope.password : '******' }}
            <a-icon :type="showPassword ? 'eye-invisible' : 'eye'"
                    @click="showPassword = !showPassword"/>
          </template>
        </cross-descriptions>
      </cross-panel>
      <cross-panel title="remark">
        {{ this.data.model.remark || $tools.config.emptyText }}
      </cross-panel>
    </cross-drawer>
  </cross-container-details>
</template>

<script>
import request from '../api'
import dataList from '../mixins/datalist'

/**
 * 数据源
 */
export default {
  name: 'source',
  mixins: [dataList],
  data() {
    const required = [{required: true, message: 'required', trigger: 'change'}]
    const name = this.$tools.forms.components.input()
    name.field = 'name'
    name.props.placeholder = 'placeholder.name'
    name.props.maxLength = 32
    const category = this.$tools.forms.components.select()
    const remark = this.$tools.forms.components.input()
    remark.field = remark.title = 'remark'
    remark.props.placeholder = 'placeholder.remark'
    remark.props.maxLength = 200
    const url = this.$tools.forms.components.input()
    url.field = 'url'
    url.title = 'server'
    url.props.placeholder = 'placeholder.url'
    url.props.maxLength = 255
    const host = this.$tools.forms.components.input()
    host.field = host.title = 'host'
    host.props.placeholder = 'placeholder.host'
    host.props.maxLength = 100
    const port = this.$tools.forms.components.inputNumber()
    port.field = port.title = 'port'
    port.props.placeholder = 'placeholder.port'
    port.props.precision = 0
    port.props.col = { span: 12 }
    const account = this.$tools.forms.components.input()
    account.field = account.title = 'account'
    account.props.placeholder = 'placeholder.account'
    account.props.maxLength = 32
    const password = this.$tools.forms.components.password()
    password.field = password.title = 'password'
    password.props.placeholder = 'placeholder.password'
    password.props.maxLength = 16
    password.validate = account.validate = url.validate = host.validate = port.validate = required
    const validateBtn = this.$tools.forms.components.button()
    validateBtn.props.type = 'default'
    validateBtn.props.icon = 'api'
    validateBtn.children = ['testDataSource']
    validateBtn.suffix = {}
    validateBtn.on.click = (evt) => {
      const flag = host.value || port.value || account.value || password.value
      const suffix = {
        type: 'a-alert',
        props: {
          type: '',
          message: '',
          banner: true
        },
        style: {
          display: 'inline',
          fontSize: '13px'
        }
      }
      const formData = evt.api.formData()
      const error = 'error'
      const errorMsg = this.$t('linkError')
      if (!flag) {
        suffix.props.type = error
        suffix.props.message = errorMsg
        evt.self.suffix = suffix
        return
      }
      evt.self.props.loading = true
      this._http({
        method: 'POST',
        url: request.dataSourceValidate,
        data: {
          host: formData.host,
          port: formData.port,
          account: formData.account,
          password: formData.password,
          type: formData.type
        },
        params: null
      }, undefined, false).then(res => {
        evt.self.props.loading = false
        if (res.code !== 0) {
          suffix.props.type = error
          suffix.props.message = errorMsg
          evt.self.suffix = suffix
          return
        }
        suffix.props.type = 'success'
        suffix.props.message = this.$t('linkSuccess')
        evt.self.suffix = suffix
      }).catch(err => {
        evt.self.props.loading = false
        suffix.props.type = error
        suffix.props.message = errorMsg
        evt.self.suffix = suffix
        this.$message.error(this.$t(err ? `error.${err.status}` : this.$tools.config.ERROR))
      })
    }
    const type = this.$tools.forms.components.select()
    type.validate = required
    type.control = [{
      handle: (val, fApi) => {
        validateBtn.suffix = {}
        return val === 1 || val === 3
      },
      rule: [host, port, account, password, validateBtn]
    }, {
      value: 2,
      rule: [url]
    }]
    type.title = type.field = category.field = 'type'
    type.props.placeholder = category.props.placeholder = 'placeholder.type'
    type.options = category.options = [{
      value: 1,
      label: 'MySQL'
    }, {
      value: 2,
      label: 'HTTP'
    }, {
      value: 3,
      label: 'ClickHouse'
    }]
    const id = this.$tools.forms.components.hidden()
    id.field = 'id'
    const step1 = this.$tools.forms.components.label()
    step1.field = step1.props.title = 'dataSource.step1'
    const step2 = this.$tools.forms.components.label()
    step2.field = step2.props.title = 'dataSource.step2'
    return {
      searchRule: [category, name],
      formRule: [id, step1, {...this.clone(name), title: 'name', validate: required}, remark, step2, type],
      formApi: request.dataSourceSave,
      urlInfoView: [{
        prop: 'host'
      }, {
        prop: 'port'
      }, {
        prop: 'account'
      }, {
        prop: 'password',
        type: 'custom'
      }],
      urlHttpInfoView: [{
        prop: 'url',
        locale: () => {
          return this.$t('server')
        }
      }],
      showPassword: false
    }
  },
  methods: {
    /**
     * 显示编辑面板
     *
     * @param {object} data 数据源
     */
    showEdit(data) {
      if (data) {
        this.data.model = this.clone(data)
        this.setCurrentRow(data)
      }
      this.setFormConfig()
    }
  },
  created() {
    this.api.url = request.dataSourceList
    this.api.dataCallback = (data) => {
      data.records.forEach(d => d.urlOrHostPort = d.type === 2 ? d.url : `${d.host}:${d.port}`)
    }
    this.delApi = request.dataSourceDelete
    const view = [{
      prop: 'type',
      format: (value) => {
        switch (value) {
          case 1 :
            return 'MySQL'
          case 2 :
            return 'HTTP'
          case 3 :
            return 'ClickHouse'
        }
      }
    }, {
      prop: 'createTime',
      format: this.toLocalDateTime
    }]
    this.basicInfoView = [{
      prop: 'name'
    }, ...view]
    this.config.props.view = [{
      prop: 'name',
      type: 'link',
      click: this.showDetails
    }, view[0], {
      prop: 'urlOrHostPort',
      ellipsis: true
    }, view[1]]
    this.formTitle.new = 'newDataSource'
    this.formTitle.edit = 'editDataSource'
  }
}
</script>

<style lang="scss">
.ant-input-number{
  width: 100%;
}
</style>
