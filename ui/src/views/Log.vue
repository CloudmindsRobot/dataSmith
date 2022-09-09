<template>
  <cross-container-details>
    <cross-search :rule="searchRule"
                  @search="searchHandle"/>
    <cross-page-table :api="api"
                      :config="config">
      <a-tag slot="result"
             slot-scope="{ slotScope }"
             :color="slotScope.result === 1 ? 'green' : 'red'"
             @click="showCause(slotScope)"
             :class="{'pointer': slotScope.result === 2}">
        {{ $t(slotScope.result === 1 ? 'success' : 'failed') }}
      </a-tag>
    </cross-page-table>
    <cross-drawer :config="modal">
      <cross-panel title="basicInfo">
        <cross-descriptions :data-model="data"
                            :view="basicInfoView"
                            :columns="columns"
                            size="small">
          <a-tag slot="result"
                 slot-scope="{ slotScope }"
                 :color="slotScope.result === 1 ? 'green' : 'red'"
                 @click="causeModal.visible = slotScope.result === 2"
                 :class="{'pointer': slotScope.result === 2}">
            {{ $t(slotScope.result === 1 ? 'success' : 'failed') }}
          </a-tag>
        </cross-descriptions>
      </cross-panel>
    </cross-drawer>
    <cross-modal :modal="causeModal">
      <div style="white-space: pre">
        {{ data.model.cause }}
      </div>
    </cross-modal>
  </cross-container-details>
</template>

<script>
import request from '../api'
import dataList from '../mixins/datalist'

/**
 * 调度日志
 */
export default {
  name: 'log',
  mixins: [dataList],
  data() {
    const triggerType = this.$tools.forms.components.select()
    triggerType.field = 'triggerType'
    triggerType.props.placeholder = 'cronType'
    triggerType.options = [{
      value: 1,
      label: 'normal'
    }, {
      value: 2,
      label: 'auto'
    }]
    const createStartTime = this.$tools.forms.components.hidden()
    createStartTime.field = 'createStartTime'
    const createEndTime = this.$tools.forms.components.hidden()
    createEndTime.field = 'createEndTime'
    const dateTime = this.$tools.forms.components.datePicker()
    dateTime.field = 'datetime'
    dateTime.props.type = 'range'
    dateTime.props.mode = undefined
    dateTime.props.showTime = true
    dateTime.props.format = 'YYYY-MM-DD HH:mm:ss'
    dateTime.on.change = (evt) => {
      if (evt.self.value && evt.self.value.length) {
        createStartTime.value = this.toTimespan(evt.self.value[0])
        createEndTime.value = this.toTimespan(evt.self.value[1])
      } else {
        createStartTime.value = createEndTime.value = undefined
      }
      this.searchHandle(evt.api.form)
    }
    const status = this.$tools.forms.components.select()
    status.field = 'result'
    status.props.placeholder = 'status'
    status.options = [{
      value: 1,
      label: 'success'
    }, {
      value: 2,
      label: 'failed'
    }]
    const handlerName = this.$tools.forms.components.input()
    handlerName.field = 'handlerName'
    handlerName.props.placeholder = 'menu.2'
    handlerName.props.maxLength = 32
    return {
      searchRule: [triggerType, dateTime, status, handlerName, createStartTime, createEndTime],
      causeModal: {
        visible: false,
        title: 'cause',
        width: '65%',
        zIndex: 1500,
        maskClosable: true,
        footer: null
      }
    }
  },
  methods: {
    /**
     * 显示失败原因
     *
     * @param {object} data
     */
    showCause(data) {
      if (data.result === 1) {
        return
      }
      this.data.model = this.clone(data)
      this.setCurrentRow(data)
      this.causeModal.visible = true
    }
  },
  created() {
    this.api.url = request.logList
    const commonView = [{
      prop: 'triggerType',
      locale: () => {
        return this.$t('cronType')
      },
      format: (value) => {
        return this.$t(value === 1 ? 'normal' : 'auto')
      }
    }, {
      prop: 'createTime',
      locale: () => {
        return this.$t('startDate')
      },
      format: this.toLocalDateTime
    }, {
      prop: 'updateTime',
      locale: () => {
        return this.$t('endDate')
      },
      format: this.toLocalDateTime
    }, {
      prop: 'costTime',
      format: (value) => {
        return `${value}ms`
      }
    }, {
      prop: 'result',
      type: 'custom'
    }]
    this.config.props.view = [{
      prop: 'handlerName',
      type: 'link',
      locale: () => {
        return this.$t('name')
      },
      click: this.showDetails
    }, ...commonView]
    this.basicInfoView = [{
      prop: 'handlerName',
      locale: () => {
        return this.$t('name')
      }
    }, ...commonView]
    this.config.props.actions = []
    this.modal.buttons = []
  }
}
</script>
