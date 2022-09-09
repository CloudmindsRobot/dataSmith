<template>
  <cross-container-details>
    <cross-form v-show="formConfig.visible"
                :rule="formRule"
                :data-model="data"
                :modal="formConfig"
                :api="formApi"
                @complete="onComplete"
                @cancel="sourceModal.visible = false"/>
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
      <cross-panel title="fields">
        <ul v-if="data.model.fieldData"
            class="cross-ul">
          <li v-for="field in JSON.parse(data.model.fieldData)"
              :key="field.key">
            {{ field.key || field.name }}
          </li>
        </ul>
      </cross-panel>
    </cross-drawer>
    <cross-drawer :config="sourceModal">
      <cross-search :rule="sourceSearchRule"
                    @search="sourceSearchHandle"/>
      <cross-page-table :api="sourceApi"
                        :config="sourceConfig">
        <a-radio-group slot="select"
                       slot-scope="{ slotScope }"
                       name="source"
                       v-model="data.model.sourceId"
                       @change="onChange(slotScope)">
          <a-radio :value="slotScope.id"/>
        </a-radio-group>
      </cross-page-table>
    </cross-drawer>
  </cross-container-details>
</template>

<script>
import request from '../api'
import dataList from '../mixins/datalist'

/**
 * 模型
 */
export default {
  name: 'model',
  mixins: [dataList],
  data() {
    const required = [{required: true, message: 'required', trigger: 'change'}]
    const name = this.$tools.forms.components.input()
    name.field = 'name'
    name.props.placeholder = 'placeholder.name'
    name.props.maxLength = 32
    const sourceId = this.$tools.forms.components.hidden()
    sourceId.field = 'sourceId'
    const result = this.$tools.forms.components.label()
    result.props.title = 'result'
    result.props.cssStyle = {
      fontWeight: 'normal',
      marginBottom: '5px'
    }
    const resultTip = this.$tools.forms.components.label()
    resultTip.props.title = 'resultTip'
    resultTip.props.cssStyle = {
      marginBottom: '10px'
    }
    resultTip.props.cssClass = 'font12 gray'
    const fieldData = this.$tools.forms.components.hidden()
    fieldData.field = 'fieldData'
    const table = this.$tools.forms.components.table()
    table.props.rowKey = 'dataId'
    const run = this.$tools.forms.components.button()
    run.field = 'run'
    run.suffix = {
      type: 'span',
      children: ['runInfo'],
      class: 'font12 gray'
    }
    run.children = ['run']
    run.props.block = true
    run.control = [{
      value: 'over',
      rule: [result, resultTip, table]
    }]
    run.on.click = (evt) => {
      evt.self.props.loading = true
      table.props.columns = table.props.dataSource = []
      const formData = evt.api.formData()
      this._http({
        method: 'POST',
        url: request.dataModelValidate,
        data: {
          content: formData.content,
          parameter: formData.parameter,
          sourceId: formData.sourceId,
          jsonPath: formData.jsonPath
        },
        params: null
      }, undefined, false).then(res => {
        evt.self.props.loading = false
        evt.self.value = 'over'
        if (res.code !== 0) {
          this.$message.error(res.message)
          return
        }
        const fields = []
        table.props.columns = res.data ? Object.keys(res.data[0]).map(k => {
          fields.push({key: k, name: k})
          return {
            title: k,
            key: k,
            dataIndex: k
          }
        }) : []
        table.props.dataSource = res.data.map((d, index) => {
          d.dataId = index
          return d
        }) || []
        fieldData.value = JSON.stringify(fields)
      }).catch(err => {
        evt.self.props.loading = false
        evt.self.value = 'over'
        this.$message.error(this.$t(err ? `error.${err.status}` : this.$tools.config.ERROR))
      })
    }
    const content = {
      type: 'cross-sql-pad',
      title: 'sql',
      props: {
        tree: {
          dataSourceId: ''
        }
      },
      on: {
        input: undefined,
        select: undefined
      }
    }
    content.on.select = (evt, key, obj) => {
      evt.self.value = obj.selectedNodes[0].data.props.dataRef.children ? `SELECT ${obj.selectedNodes[0].data.props.dataRef.children.length ? obj.selectedNodes[0].data.props.dataRef.children.map(k=>k.title).join(',') : '*'} FROM ${obj.selectedNodes[0].data.props.dataRef.parent}.${obj.selectedNodes[0].data.props.dataRef.title}` : `SELECT ${obj.selectedNodes[0].data.props.dataRef.title} FROM ${obj.selectedNodes[0].data.props.dataRef.parent}`
    }
    content.on.input = (evt) => {
      run.props.disabled = !evt.self.value
    }
    const url = this.$tools.forms.components.input()
    url.title = 'url'
    url.props.placeholder = 'placeholder.url'
    url.props.maxLength = 255
    url.field = content.field = 'content'
    const parameter = this.$tools.forms.components.codemirror()
    parameter.field = parameter.title = 'parameter'
    parameter.props.options.placeholder = 'placeholder.parameter'
    parameter.prefix = {
      type: 'span',
      children: ['parameterInfo'],
      class: 'font12 gray'
    }
    const json = this.$tools.forms.components.input()
    json.field = json.title = 'jsonPath'
    json.props.placeholder = 'placeholder.jsonPath'
    json.prefix = {
      type: 'span',
      children: ['jsonPathInfo'],
      class: 'font12 gray'
    }
    json.props.maxLength = parameter.props.maxLength = 100
    json.validate = url.validate = content.validate = required
    json.on.change = url.on.change = (evt) => {
      run.props.disabled = !json.value || !url.value
    }
    const type = this.$tools.forms.components.hidden()
    type.field = 'sourceType'
    type.control = [{
      handle: (val, fApi) => {
        return val === 1 || val === 3
      },
      rule: [content, run]
    }, {
      value: 2,
      rule: [url, parameter, json, run]
    }]
    const source = this.$tools.forms.components.input()
    source.field = 'sourceName'
    source.title = 'menu.4'
    source.props.placeholder = 'placeholder.sourceId'
    source.children = [{
      type: 'aIcon',
      props: {
        type: 'folder-open'
      },
      slot: 'addonAfter'
    }]
    source.props.allowClear = false
    source.validate = required
    source.on.focus = () => {
      this.sourceModal.visible = true
    }
    source.on.click = (e) => {
      e.args[0].preventDefault()
      e.args[0].stopPropagation()
    }
    const id = this.$tools.forms.components.hidden()
    id.field = 'id'
    const step1 = this.$tools.forms.components.label()
    step1.field = step1.props.title = 'dataModel.step1'
    const step2 = this.$tools.forms.components.label()
    step2.field = step2.props.title = 'dataModel.step2'
    const category = this.$tools.forms.components.select()
    category.field = 'type'
    category.props.placeholder = 'placeholder.type'
    category.autoSearch = true
    category.options = [{
      value: 1,
      label: 'MySQL'
    }, {
      value: 2,
      label: 'HTTP'
    }, {
      value: 2,
      label: 'ClickHouse'
    }]
    return {
      searchRule: [name],
      formRule: [id, step1, {
        ...this.clone(name),
        title: 'name',
        validate: required
      }, step2, sourceId, source, type, fieldData],
      formApi: request.dataModelSave,
      sourceModal: {
        visible: false,
        title: 'selectDataSource',
        width: '50vw',
        mask: false
      },
      sourceSearchRule: [this.clone(name), category],
      sourceConfig: {
        showMode: false,
        mode: 'table',
        xScroll: true,
        props: {
          view: [{
            prop: 'select',
            type: 'custom'
          }, {
            prop: 'name',
          }, {
            prop: 'type',
            format: (value) => {
              switch (value) {
                case 1:
                  return 'MySQL'
                case 2:
                  return 'HTTP'
                case 3:
                  return 'ClickHouse'
              }
            }
          }, {
            prop: 'createTime',
            format: this.toLocalDateTime
          }]
        }
      },
      sourceApi: {
        url: request.dataSourceList,
        method: 'GET',
        params: {}
      },
      basicInfoViewOfType: [{
        prop: 'name'
      }, {
        prop: 'sourceName',
        locale: () => {
          return this.$t('menu.4')
        }
      }, {
        prop: 'content'
      }]
    }
  },
  methods: {
    /**
     * 查询数据源
     *
     * @param {object} formData 过滤表单数据
     */
    sourceSearchHandle(formData) {
      this.sourceApi.params = {current: 1, ...formData}
    },
    /**
     * 切换数据源
     *
     * @param {object} dataSource 数据源
     */
    onChange(dataSource) {
      this.sourceModal.visible = false
      this.formRule.forEach(rule => {
        if (rule.field === 'fieldData') {
          rule.value = ''
        } else if (rule.field === 'sourceType') {
          rule.control.forEach(ctl => {
            ctl.rule.forEach(r => {
              if (r.field === 'run') {
                r.props.disabled = true
                r.value = ''
              } else if (r.props.tree && dataSource.type !== 2) {
                r.props.tree.dataSourceId = dataSource.id
              }
            })
          })
        }
      })
      this.data.model = {
        sourceType: dataSource.type,
        sourceName: dataSource.name,
        sourceId: this.data.model.sourceId,
        content: '',
        parameter: '',
        fieldData: '',
      }
    },
    /**
     * 显示模型编辑面板
     *
     * @param {object} data 模型
     */
    showEdit(data) {
      const model = data || this.data.model
      this.data.model = {...model, sourceType: 0}
      data && this.setCurrentRow(data)
      this.setFormConfig()
      this.formRule.forEach(rule => {
        if (rule.field === 'sourceType') {
          setTimeout(() => {
            rule.value = model.sourceType
          }, 100)
          rule.control.forEach(ctl => {
            ctl.rule.forEach(r => {
              if (r.field === 'run') {
                r.value = 'over'
                r.control.forEach(cl => {
                  cl.rule.forEach(t => {
                    if (t.type === 'aTable') {
                      if (model.fieldData) {
                        const _data = {}
                        t.props.columns = JSON.parse(model.fieldData).map((dt, index) => {
                          _data.dataId = index
                          _data[dt.key] = '***'
                          return {title: dt.key, dataIndex: dt.key, key: dt.key}
                        })
                        t.props.dataSource = [_data]
                      }
                    }
                  })
                })
              } else if (r.props.tree && model.sourceType !== 2) {
                r.props.tree.dataSourceId = model.sourceId
              }
            })
          })
        }
      })
    }
  },
  created() {
    this.api.url = request.dataModelList
    this.delApi = request.dataModelDelete
    this.config.props.view = [{
      prop: 'name',
      type: 'link',
      click: (data) => {
        const createTime = {
          prop: 'createTime',
          format: this.toLocalDateTime
        }
        this.basicInfoView = data.sourceType !== 2 ? [...this.basicInfoViewOfType, createTime] : [...this.basicInfoViewOfType, {
          prop: 'parameter'
        }, {
          prop: 'jsonPath'
        }, createTime]
        this.showDetails(data)
      }
    }, {
      prop: 'sourceName',
      locale: () => {
        return this.$t('menu.4')
      }
    }, {
      prop: 'content',
      ellipsis: true
    }, {
      prop: 'createTime',
      format: this.toLocalDateTime
    }]
    this.formTitle.new = 'newDataModel'
    this.formTitle.edit = 'editDataModel'
  }
}
</script>

<style lang="scss" scoped>
.cross-ul {
  margin: 0;
  padding: 0;
  list-style: none;

  li {
    list-style-type: none;
    line-height: 26px;
  }
}
</style>
