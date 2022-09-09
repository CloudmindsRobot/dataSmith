<template>
  <cross-container-details>
    <cross-form v-show="formConfig.visible"
                :rule="formRule"
                :data-model="data"
                :modal="formConfig"
                @cancel="setShowTable(false)">
      <div v-if="showTable">
        <strong>
          {{ $t('target.step3') }}
        </strong>
        <div class="mb5" style="margin-top: 32px">
          <!--          <a-button class="fr">-->
          <!--            {{ $t('preview') }}-->
          <!--          </a-button>-->
          <a-button class="fr"
                    :disabled="!fieldKeys.length"
                    @click="addTableField(0)">
            {{ $t('add') }}
          </a-button>
          <label class="ant-form-item-required">
            <span>
                {{ $t('fields') }}
            </span>
          </label>
          <br/>
          <span class="font12 gray">
            {{ $t('fieldsInfo') }}
          </span>
        </div>
        <cross-table :view="tableView"
                     :data="tableData"
                     primary-key="sortNo"
                     :pagination="false"
                     :actions="actions"
                     :config="tableConfig">
          <template slot="fieldKey"
                    slot-scope="{ slotScope }">
            <a-select class="w100"
                      v-if="slotScope.editable"
                      v-model="slotScope.fieldKey"
                      showSearch
                      :options="fieldKeys"/>
            <template v-else>
              {{ slotScope.fieldKey }}
            </template>
          </template>
          <a-input slot="fieldName"
                   slot-scope="{ slotScope }"
                   v-model="slotScope.fieldName"
                   :maxLength="32"
                   :disabled="!slotScope.editable"
                   :allowClear="true"/>
          <a-popover v-if="['3', '4'].includes(slotScope.property[0])"
                     slot="property"
                     slot-scope="{ slotScope }"
                     :title="$t('options')">
            <template slot="content">
              <a-icon v-if="!slotScope.options.length"
                      class="iconfont icon-bg"
                      @click="slotScope.options.push({})"
                      type="plus-circle"/>
              <div v-else
                   v-for="(opt, index) in slotScope.options"
                   :key="index">
                <a-space :class="{'mt10': index !== 0}">
                  <a-input v-model="opt.value"
                           :placeholder="$t('key')"
                           :allowClear="true"/>
                  <a-input v-model="opt.name"
                           :placeholder="$t('value')"
                           :allowClear="true"/>
                  <!-- <a-select v-model="opt.color" size="small"
                            :allowClear="true" >
                      <a-select-option v-for="color in colorOptions"
                                       :key="color"
                                       :value="color">
                          <span :class="['bg-color', `color${color}`]"/>
                      </a-select-option>
                  </a-select> -->
                  <span>
                    <a-icon v-if="index === 0"
                            class="iconfont icon-bg"
                            @click="slotScope.options.push({})"
                            type="plus-circle"/>
                    <a-icon class="iconfont icon-bg"
                            @click="slotScope.options.splice(index, 1)"
                            type="minus-circle"/>
                  </span>
                </a-space>
              </div>
            </template>
            <a-cascader :options="options"
                        :disabled="!slotScope.editable"
                        :placeholder="$t('custom')"
                        v-model="slotScope.property"
                        :allowClear="false"
                        :showSearch="true"/>
          </a-popover>
          <a-cascader v-else
                      slot="property"
                      :disabled="!slotScope.editable"
                      slot-scope="{ slotScope }"
                      :options="options"
                      :placeholder="$t('custom')"
                      v-model="slotScope.property"
                      :allowClear="false"
                      :showSearch="true"/>
          <a-checkbox slot="primaryFlag"
                      :disabled="!slotScope.editable"
                      slot-scope="{ slotScope }"
                      @change="(e) => {slotScope.primaryFlag = e.target.checked}"
                      :checked="slotScope.primaryFlag"/>
        </cross-table>
      </div>
    </cross-form>
    <cross-form v-show="nextFormConfig.visible"
                :rule="nextFormRule"
                :data-model="data"
                :modal="nextFormConfig"
                @cancel="setShowTable(false)"/>
    <div v-show="!formConfig.visible && !nextFormConfig.visible">
      <cross-search :rule="searchRule"
                    @search="searchHandle"/>
      <cross-page-table :api="api"
                        :config="config"
                        :buttons="buttons">
        <template slot="tableName"
                  slot-scope="{ slotScope }">
          <a-icon type="link"
                  class="iconfont icon-bg"
                  @click="previewTable(slotScope)"></a-icon>
          <a @click="e => {e.preventDefault();e.stopPropagation();showDetails(slotScope)}">
            {{ slotScope.tableName }}
          </a>
        </template>
        <template slot="cron"
                  slot-scope="{ slotScope }">
          <a-tooltip v-if="slotScope.cron"
                     v-model="slotScope.visible"
                     @visibleChange="(visible) => tooltipVisible(visible, slotScope.cron)">
            <div slot="title">
              {{ $t('execTime') }}:
              <a-spin size="small"
                      :spinning="popover.loading"/>
              <ul class="cross-ul"
                  v-for="t in popover.times"
                  :key="t">
                <li>
                  {{ t }}
                </li>
              </ul>
            </div>
            <span class="text-blue">
              {{ slotScope.cron }}
            </span>
          </a-tooltip>
          <template v-else>
            -
          </template>
        </template>
      </cross-page-table>
    </div>
    <cross-drawer :config="modal">
      <cross-panel title="basicInfo">
        <cross-descriptions :data-model="data"
                            :view="basicInfoView"
                            :columns="columns"
                            size="small">
          <template slot="tableName"
                    slot-scope="{ slotScope }">
            <a-icon type="link"
                    class="icon-bg iconfont"
                    @click="previewTable(slotScope)"></a-icon>
            {{ slotScope.tableName }}
          </template>
          <template slot="cron"
                    slot-scope="{ slotScope }">
            <a-tooltip v-if="slotScope.cron"
                       v-model="slotScope.visible"
                       @visibleChange="(visible) => tooltipVisible(visible, slotScope.cron)">
              <div slot="title">
                {{ $t('execTime') }}:
                <a-spin size="small"
                        :spinning="popover.loading"/>
                <ul class="cross-ul"
                    v-for="t in popover.times"
                    :key="t">
                  <li>
                    {{ t }}
                  </li>
                </ul>
              </div>
              <span class="text-blue">
              {{ slotScope.cron }}
            </span>
            </a-tooltip>
            <template v-else>
              -
            </template>
          </template>
        </cross-descriptions>
      </cross-panel>
    </cross-drawer>
    <cross-drawer :config="modelModal">
      <cross-search :rule="modelSearchRule"
                    @search="modelSearchHandle"/>
      <cross-page-table :api="modelApi"
                        :config="modelConfig">
        <a-radio-group slot="select"
                       slot-scope="{ slotScope }"
                       name="model"
                       v-model="data.model.dataModelId"
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
 * 目的地
 */
export default {
  name: 'target',
  mixins: [dataList],
  data() {
    const required = [{required: true, message: 'required', trigger: 'change'}]
    const name = this.$tools.forms.components.input()
    name.field = 'tableName'
    name.props.placeholder = 'placeholder.name'
    name.props.maxLength = 32
    const appId = this.$tools.forms.components.input()
    appId.field = appId.title = 'appId'
    appId.props.placeholder = 'placeholder.appId'
    appId.props.maxLength = 32
    appId.validate = required
    const appName = this.$tools.forms.components.input()
    appName.field = appName.title = 'appName'
    appName.props.placeholder = 'placeholder.appName'
    appName.props.maxLength = 32
    const model = this.$tools.forms.components.input()
    model.field = 'dataModelName'
    model.title = 'menu.3'
    model.props.placeholder = 'placeholder.modelId'
    model.props.allowClear = false
    model.validate = required
    model.children = [{
      type: 'aIcon',
      props: {
        type: 'folder-open'
      },
      slot: 'addonAfter'
    }]
    model.on.focus = () => {
      this.modelModal.visible = true
    }
    model.on.click = (e) => {
      e.args[0].preventDefault()
      e.args[0].stopPropagation()
    }
    const modelId = this.$tools.forms.components.hidden()
    modelId.field = 'dataModelId'
    const id = this.$tools.forms.components.hidden()
    id.field = 'id'
    const step1 = this.$tools.forms.components.label()
    step1.field = step1.props.title = 'target.step1'
    const step2 = this.$tools.forms.components.label()
    step2.field = step2.props.title = 'target.step2'
    const step3 = this.$tools.forms.components.label()
    step3.field = step3.props.title = 'target.step4'
    const cron = this.$tools.forms.components.input()
    cron.field = cron.title = 'cron'
    cron.props.type = 'search'
    cron.props.maxLength = 50
    cron.prefix = {
      type: 'span',
      children: ['cronInfo'],
      class: 'font12 gray'
    }
    cron.children = [{
      type: 'AButton',
      children: ['validate'],
      props: {
        loading: false
      },
      slot: 'enterButton'
    }]
    cron.validate = [...required, {message: 'cronInvalid', trigger: 'blur', validator: this.validateCron}]
    cron.info = {
      info: this.$t('execTime') + ': -',
      // align: 'left',
      style: 'margin-right:5px'
    }
    cron.on.change = (e) => {
      this.disable(false)
      e.self.info.info = this.$t('execTime') + ': -'
      e.self.info.style = 'margin-right:5px'
      !e.self.value && e.api.validateField(e.self.field)
    }
    cron.on.search = (e) => {
      e.api.trigger(e.field, 'change')
      e.self.value && this.cronValidate(e.self.props, e.self.value, (records) => {
        this.disable(true)
        e.api.clearValidateState(e.self.field)
        e.self.info.info = this.$t('execTime') + ': ' + records.map(d => {
          return this.toLocalDateTime(d)
        }).join(' | ')
        e.self.info.style = 'color:#67C23A; margin-right:5px'
      }, () => {
        e.api.validateField(e.self.field)
      })
    }
    const cronType = this.$tools.forms.components.radio()
    cronType.field = cronType.title = 'cronType'
    cronType.value = 0
    cronType.options = [{
      label: 'normal',
      value: 0
    }, {
      label: 'auto',
      value: 1
    }]
    cronType.prefix = {
      type: 'div',
      children: ['cronTypeInfo'],
      class: 'font12 gray'
    }
    cronType.control = [{
      handle: (val, fApi) => {
        setTimeout(() => {
          cron.value && fApi.trigger(cron.field, 'search')
        }, 500)
        // this.nextFormConfig.disabled = val === 1
        return val === 1
      },
      rule: [cron]
    }]
    return {
      searchRule: [name],
      formRule: [id, step1, {
        ...this.clone(name),
        title: 'name',
        validate: required
      }, appId, appName, step2, model, modelId],
      formDelApi: request.dataTableDeleteFields,
      modelModal: {
        visible: false,
        title: 'selectDataModel',
        width: '50vw',
        mask: false
      },
      nextFormRule: [step3, cronType],
      nextFormConfig: {
        visible: false,
        height: 0,
        title: '',
        confirmLoading: false,
        // disabled: false,
        submit: 'save',
        back: () => {
          this.switchForm(false)
        },
        ok: (fApi) => {
          fApi.validate(valid => {
            if (!valid) {
              return
            }
            const formData = fApi.formData()
            this.onCallback(formData)
            if (this.removeFields.length) {
              this.dataTableSaveOrDeleteFields(true, {
                fieldIds: this.removeFields,
                tableId: this.data.model.id
              }, () => {
                this.dataTableSaveOrDeleteFields(false, formData, this.onAllComplete)
              })
            } else {
              this.dataTableSaveOrDeleteFields(false, formData, this.onAllComplete)
            }
          })
        }
      },
      modelSearchRule: [this.clone(name)],
      modelConfig: {
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
            prop: 'sourceName',
            locale: () => {
              return this.$t('menu.4')
            }
          }, {
            prop: 'createTime',
            format: this.toLocalDateTime
          }]
        }
      },
      modelApi: {
        url: request.dataModelList,
        method: 'GET',
        params: {}
      },
      tableView: [{
        prop: 'fieldKey',
        type: 'custom',
        locale: () => {
          return this.$t('dataField')
        }
      }, {
        prop: 'fieldName',
        type: 'custom',
        locale: () => {
          return this.$t('header')
        }
      }, {
        prop: 'property',
        type: 'custom',
        locale: () => {
          return this.$t('format')
        }
      }, {
        prop: 'primaryFlag',
        type: 'custom',
        locale: () => {
          return this.$t('primaryKey')
        }
      }],
      tableData: [],
      showTable: false,
      primaryKey: '',
      options: [{
        label: '多行文本',
        value: '1'
      }, {
        label: '数字',
        value: '2',
        children: [{
          label: '整数',
          value: '0'
        }, {
          label: '保留1位小数',
          value: '0.0'
        }, {
          label: '保留2位小数',
          value: '0.00'
        }, {
          label: '保留3位小数',
          value: '0.000'
        }, {
          label: '保留4位小数',
          value: '0.0000'
        }, {
          label: '千分位',
          value: '1,000'
        }, {
          label: '千分位（小数点）',
          value: '1,000.00'
        }, {
          label: '百分比',
          value: '%'
        }, {
          label: '百分比（小数点）',
          value: '0.00%'
        }, {
          label: '人民币',
          value: '¥'
        }, {
          label: '人民币（小数点）',
          value: '¥0.00'
        }, {
          label: '美元',
          value: '$'
        }, {
          label: '美元（小数点）',
          value: '$0.00'
        }]
      }, {
        label: '单选',
        value: '3'
      }, {
        label: '多选',
        value: '4'
      }, {
        label: '日期',
        value: '5',
        children: [{
          label: '2021/01/30',
          value: 'yyyy/MM/dd'
        }, {
          label: '2021/01/30 14:00',
          value: 'yyyy/MM/dd HH:mm'
        }, {
          label: '2021-01-30',
          value: 'yyyy-MM-dd'
        }, {
          label: '2021-01-30 14:00',
          value: 'yyyy-MM-dd HH:mm'
        }, {
          label: '01-30',
          value: 'MM-dd'
        }, {
          label: '01/30/2021',
          value: 'MM/dd/yyyy'
        }, {
          label: '30/01/2021',
          value: 'dd/MM/yyyy'
        }]
      }],
      currentFormData: null,
      tableConfig: {
        loading: false
      },
      removeFields: [],
      actions: [{
        icon: 'minus-circle',
        key: 'remove',
        event: (row) => {
          this._confirm((flag) => {
            if (!flag) {
              return
            }
            const index = this.tableData.findIndex(d => d.fieldKey === row.data.fieldKey)
            this.tableData.splice(index, 1)
            row.data.id && this.removeFields.push(row.data.id)
          })
        }
      }],
      fieldKeys: [],
      isValidate: false,
      popover: {
        loading: false,
        times: []
      }
    }
  },
  watch: {
    'formConfig.title'(newVal) {
      this.nextFormConfig.title = newVal
    }
  },
  methods: {
    /**
     * 验证cron表达式并显示结果
     *
     * @param {boolean} visible 可见
     * @param {string} value cron表达式
     */
    tooltipVisible(visible, value) {
      this.popover.times = []
      visible && !this.popover.loading && this.cronValidate(this.popover, value, (recodes) => {
        this.popover.times = recodes.map(d => {
          return this.toLocalDateTime(d)
        })
      })
    },
    /**
     * 验证cron表达式
     *
     * @param {object} props 等待属性
     * @param {string} value cron表达式
     * @param {function} success 验证成功回调函数
     * @param {function} failed 验证失败回调函数
     */
    cronValidate(props, value, success, failed) {
      props.loading = true
      this._http({
        method: 'GET',
        url: request.validateCron,
        params: {
          cron: value
        },
        data: null
      }, undefined, false).then(res => {
        props.loading = false
        if (res.code !== 0 || !res.data.length) {
          failed()
          return
        }
        success(res.data)
      }).catch(err => {
        props.loading = false
        this.$message.error(this.$t(err ? `error.${err.status}` : this.$tools.config.ERROR))
      })
    },
    /**
     * cron表达式验证状态
     *
     * @param {boolean} flag 禁用
     */
    disable(flag) {
      this.isValidate = flag
      // this.nextFormConfig.disabled = !flag
    },
    /**
     * cron表达式验证状态
     *
     * @returns {boolean} 验证状态
     */
    validateCron() {
      return this.isValidate
    },
    /**
     * 表单提交前回调函数
     *
     * @param {object} formData 表单数据项
     */
    onCallback(formData) {
      formData.appType = 1
      formData.cronType === 0 && (formData.cron = '')
      formData.fields = this.tableData.filter(t => t.editable).map((d, i) => {
        return {
          ...d,
          primaryFlag: d.primaryFlag,
          type: parseInt(d.property[0]),
          property: JSON.stringify(this.getProperty(d.property, d.options)),
          sortNo: d.sortNo || i
        }
      })
      Object.assign(formData, this.currentFormData)
    },
    /**
     * 保存或删除数据表格字段
     *
     * @param {boolean} flag 标识
     * @param {object} formData 表单数据项
     * @param {function} callback 回调函数
     */
    dataTableSaveOrDeleteFields(flag, formData, callback) {
      this.tableConfig.loading = true
      this._http({
        method: 'POST',
        url: flag ? request.dataTableDeleteFields : request.dataTableSave,
        data: formData,
        params: null
      }, undefined, false).then(res => {
        if (res.code !== 0) {
          this.tableConfig.loading = false
          this.$message.error(res.message)
          return
        }
        callback(formData)
      }).catch(err => {
        this.tableConfig.loading = false
        this.$message.error(this.$t(err ? `error.${err.status}` : this.$tools.config.ERROR))
      })
    },
    /**
     * 获取属性格式
     *
     * @param {array} property 属性数组
     * @param {array} options 选项
     * @returns {{date_formatter: *}|{}|{options: *}|{formatter: *}}
     */
    getProperty(property, options) {
      if (property.length) {
        switch (property[0]) {
          case '1' :
            return {}
          case '2':
            return {
              formatter: property[1]
            }
          case '5':
            return {
              date_formatter: property[1]
            }
          case '3' :
          case '4' :
            return {
              options: options.map((opt, index) => {
                opt.color = this.isNumber(opt.color) ? opt.color : index
                return opt
              })
            }
        }
      }
      return {}
    },
    /**
     * 操作完成
     *
     * @param {object} formData 表单数据项
     */
    onAllComplete(formData) {
      this.onComplete(formData)
      this.setShowTable(false)
    },
    /**
     * 执行
     *
     * @param {object} data 数据
     */
    exec(data) {
      this._http({
        method: 'POST',
        url: request.dataTableExecuteJob,
        params: {
          tableId: data.id
        },
        data: null
      }, undefined, false).then(res => {
        if (res.code !== 0) {
          this.$message.error(res.message)
          return
        }
        this.showSuccess()
      }).catch(err => {
        this.$message.error(this.$t(err ? `error.${err.status}` : this.$tools.config.ERROR))
      })
    },
    /**
     * 切换表单
     *
     * @param {boolean} bool 可见
     */
    switchForm(bool) {
      this.formConfig.visible = !bool
      this.nextFormConfig.visible = bool
    },
    /**
     * 查询模型
     *
     * @param {object} formData 数据表项
     */
    modelSearchHandle(formData) {
      this.modelApi.params = {current: 1, ...formData}
    },
    /**
     * 切换选择模型
     *
     * @param {object} model 模型
     */
    onChange(model) {
      this.fieldKeys = JSON.parse(model.fieldData).map(d => {
        return {
          value: d.key,
          label: d.name
        }
      })
      this.modelModal.visible = false
      this.data.model = {
        dataModelName: model.name,
        dataModelId: this.data.model.dataModelId,
        cron: null
      }
      model.fieldData && this.setShowTable(true, JSON.parse(model.fieldData).map((f, index) => {
        return {
          sortNo: index,
          fieldKey: f.key,
          fieldName: f.key,
          property: ['1'],
          primaryFlag: f.primaryFlag,
          options: [{}],
          editable: true
        }
      }))
    },
    /**
     * 设置数据表格
     *
     * @param {boolean} bool 可见
     * @param {array} fields 字段
     */
    setShowTable(bool, fields = []) {
      this.nextFormConfig.visible = this.modelModal.visible = false
      this.tableData = fields
      this.showTable = bool
      fields.length && this.formConfig.title === this.formTitle.edit ? this.actions.splice(0, 0, {
        icon: 'icon-edit',
        key: 'edit',
        event: (row) => {
          row.data.editable = true
        }
      }, {
        icon: 'plus-circle',
        key: 'add',
        event: (row) => {
          const index = this.tableData.findIndex(d => d.sortNo === row.data.sortNo)
          this.addTableField(index)
        }
      }) : (this.actions.length > 1 && this.actions.splice(0, 1))
    },
    /**
     * 跳转飞书
     *
     * @param {object} data 目的地数据
     */
    previewTable(data) {
      window.open(`https://df54vg7fe0.feishu.cn/base/${data.appId}?table=${data.appTableId}`)
    },
    /**
     * 获取模型
     *
     * @param {string} id 模型唯一标识
     */
    getModel(id) {
      this.fieldKeys.length = 0
      this._http({
        method: 'GET',
        url: request.dataModelDetail,
        params: {
          id
        },
        data: null
      }, undefined, false).then(res => {
        if (res.code !== 0) {
          this.$message.error(res.message)
          return
        }
        this.fieldKeys = JSON.parse(res.data.fieldData).map(d => {
          return {
            value: d.key,
            label: d.name
          }
        })
      }).catch(err => {
        this.$message.error(this.$t(err ? `error.${err.status}` : this.$tools.config.ERROR))
      })
    },
    /**
     * 显示编辑面板
     *
     * @param {object} data 目的地
     */
    showEdit(data) {
      this.removeFields.length = 0
      this.formRule[6].props.disabled = true
      data && this.setCurrentRow(data)
      const target = data || this.data.model
      this.data.model = {...target, cronType: target.cron ? 1 : 0}
      this.setFormConfig()
      this.setShowTable(true)
      this.tableConfig.loading = true
      this._http({
        method: 'GET',
        url: request.dataTableDetails,
        params: {
          tableId: this.data.model.id
        },
        data: null
      }, undefined, false).then(res => {
        this.tableConfig.loading = false
        if (res.code !== 0) {
          this.$message.error(res.message)
          return
        }
        this.getModel(res.data.dataModelId)
        this.setShowTable(true, res.data.fields.map((d, index) => {
          const property = d.property ? JSON.parse(d.property) : {}
          switch (d.type) {
            case 1 :
              d.property = ['1']
              break
            case 3:
            case 4:
              d.property = [d.type + '']
              break
            case 2:
            case 5:
              d.property = [d.type + '', property.formatter]
              break
          }
          d.options = property.options || []
          d.sortNo = index
          d.editable = false
          return d;
        }))
      }).catch(err => {
        this.tableConfig.loading = false
        this.$message.error(this.$t(err ? `error.${err.status}` : this.$tools.config.ERROR))
      })
    },
    /**
     * 添加表格字段
     *
     * @param {number} index 索引
     */
    addTableField(index) {
      const value = this.fieldKeys[0].value
      this.tableData.splice(index, 0, {
        fieldKey: value,
        fieldName: value,
        property: ['1'],
        primaryFlag: false,
        options: [],
        editable: true
      })
      this.tableData.forEach((d, index) => {
        d.sortNo = index
      })
    }
  },
  created() {
    this.api.url = request.dataTableList
    this.delApi = request.dataTableDelete
    this.modal.buttons = [{
      label: 'exec',
      click: () => {
        this.exec(this.currentRow)
      }
    }, ...this.modal.buttons]
    //     , {
    //   label: 'preview',
    //   click: () => {
    //     this.previewTable(this.currentRow)
    //   }
    // }]
    this.config.props.actions = [{
      key: 'exec',
      icon: 'play-circle',
      event: (row) => {
        this.exec(row.data)
      }
    }, this.config.props.actions[0],
      //   {
      //   key: 'preview',
      //   icon: 'table',
      //   event: (row) => {
      //     this.previewTable(row.data)
      //   }
      // },
      this.config.props.actions[1]]
    this.basicInfoView = this.config.props.view = [{
      prop: 'tableName',
      type: 'custom',
      locale: () => {
        return this.$t('name')
      }
    }, {
      prop: 'appName',
    }, {
      prop: 'cronType',
      format: (value) => {
        return this.$t(value ? 'auto' : 'normal')
      }
    }, {
      prop: 'cron',
      type: 'custom'
    }, {
      prop: 'dataModelName',
      locale: () => {
        return this.$t('menu.3')
      }
    }, {
      prop: 'syncStatus',
      type: 'tag',
      format: (value) => {
        return this.$t(value === 1 ? 'sync' : 'async')
      },
      color: (value) => {
        return value === 1 ? 'green' : 'orange'
      }
    }, {
      prop: 'executeTime',
      format: this.toLocalDateTime
    }]
    this.formTitle.new = 'newTarget'
    this.formTitle.edit = 'editTarget'
    this.formConfig.submit = 'next'
    this.formConfig.ok = (fApi) => {
      fApi.validate(valid => {
        if (!valid) {
          return
        }
        this.switchForm(true)
        this.currentFormData = fApi.formData()
      })
    }
  },
  mounted() {
    this.nextFormConfig.height = $('body').height() - 202
  }
}
</script>

<style lang="scss">
.cross-ul {
  list-style: none;
  margin: 0;
  padding: 0;
}
</style>
