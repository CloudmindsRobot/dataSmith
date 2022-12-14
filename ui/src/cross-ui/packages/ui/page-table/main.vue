<template>
  <div class="cross-page-table">
    <div v-if="buttons"
         class="cross-component">
      <span class="mr16 cross-button"
            v-for="item in buttons"
            :key="item.title">
        <a-tooltip>
          <template v-if="item.tooltip" slot="title">
            {{ $t(item.tooltip) }}
          </template>
          <a-button v-if="!item.component"
                    v-bind="item.btnConfig || {}"
                    :disabled="isFunction(item.disabled) ? item.disabled(pageTableList) : !!item.disabled"
                    @click="item.action">
            {{ $t(item.title) }}
          </a-button>
          <component
              v-else
              v-bind:is="item.component"
              v-bind="item.btnConfig || {}"
              @btnEmit="buttonEmit"
              :disabled="isFunction(item.disabled) ? item.disabled(pageTableList) : !!item.disabled"
              :key="item.key"
              :pageTotal="total"
              :params="api.params"/>
        </a-tooltip>
      </span>
    </div>
    <a-row>
      <a-col :span="24" class="h-flex-center">
        <span class="h-flex-center">
          <span v-if="config.selectAll && (config.mode === 'card' || isMobile)"
                class="cross-checkbox mr16">
            <a-checkbox
                :checked="checked.all"
                :indeterminate="checked.indeterminate"
                :disabled="checked.disabled"
                @change="onChange">
              {{ $t('selectAll') }}
            </a-checkbox>
          </span>
          <template v-if="buttons">
            <span class="mr16 cross-button"
                  v-for="item in buttons"
                  :key="item.title">
              <a-tooltip>
                <template v-if="item.tooltip" slot="title">
                  {{ $t(item.tooltip) }}
                </template>
                <a-button v-if="!item.component"
                          v-bind="item.btnConfig || {}"
                          :disabled="isFunction(item.disabled) ? item.disabled(pageTableList) : !!item.disabled"
                          @click="item.action">
                  {{ $t(item.title) }}
                </a-button>
                <component
                    v-else
                    v-bind:is="item.component"
                    v-bind="item.btnConfig || {}"
                    @btnEmit="buttonEmit"
                    :disabled="isFunction(item.disabled) ? item.disabled(pageTableList) : !!item.disabled"
                    :key="item.key"
                    :pageTotal="total"
                    :params="api.params"/>
              </a-tooltip>
            </span>
          </template>
        </span>
        <a-radio-group class="cross-radio-group"
                       v-if="config.showMode"
                       v-model="config.mode">
          <a-radio-button v-for="m in mode.filter(m => m.value !== 'list')"
                          :key="m.value"
                          :value="m.value">
            <template v-if="m.icon">
              <i v-if="m.icon.startsWith('fa')"
                 :class="['fa', m.icon]"/>
              <a-icon v-else
                      :type="m.icon"/>
            </template>
          </a-radio-button>
        </a-radio-group>
      </a-col>
    </a-row>
    <div class="mt10">
      <!-- @slot ????????????????????? -->
      <slot name="card">
        <cross-card v-if="config.mode === mode[0].value"
                    :data="pageTableList"
                    :primary-key="primaryKey"
                    :title="config.props.title"
                    :view="cardView"
                    :actions="config.props.actions"
                    :checked="checked"
                    :meta="config.props.meta"
                    :columns="config.props.columns || [6, 8, 12]"
                    :bordered="config.props.bordered"
                    :config="extend"
                    @checked="onChecked"
                    @infinite="onInfinite"
                    @action="onAction">
          <template slot="title"
                    slot-scope="{slotScope}">
            <slot name="title"
                  :slot-scope="slotScope"/>
          </template>
          <template slot="actions"
                    slot-scope="{slotScope}">
            <slot name="actions"
                  :slot-scope="slotScope"/>
          </template>
          <template v-if="config.props.meta = 'cover'"
                    slot="cover"
                    slot-scope="{slotScope}">
            <slot name="cover"
                  :slot-scope="slotScope"/>
          </template>
          <template v-else
                    slot-scope="{slotScope}">
            <slot :slot-scope="slotScope"/>
          </template>
          <template v-if="config.props.view.length > 1"
                    slot="description"
                    slot-scope="{slotScope}">
            <slot name="description"
                  :slot-scope="slotScope"/>
          </template>
          <template v-for="v in config.props.view.filter(vw => vw.type === 'custom')"
                    :slot="v.prop"
                    slot-scope="{slotScope}">
            <slot :name="v.prop"
                  :slot-scope="slotScope"/>
          </template>
        </cross-card>
      </slot>
      <!-- @slot ????????????????????? -->
      <slot name="table">
        <cross-table v-if="config.mode === mode[1].value && !isMobile"
                     :primary-key="primaryKey"
                     :data="pageTableList"
                     :view="tableView"
                     :actions="config.props.actions"
                     :config="extend"
                     :pagination="false"
                     :dragSortable="config.dragSortable"
                     @checked="onChecked"
                     @action="onAction">
          <template v-if="extend.expanded"
                    slot="expandedRow"
                    slot-scope="{slotScope}">
            <slot name="expandedRow"
                  :slot-scope="slotScope"/>
          </template>
          <template v-for="v in config.props.view.filter(vw => vw.type === 'custom')"
                    :slot="v.prop"
                    slot-scope="{slotScope}">
            <slot :name="v.prop"
                  :slot-scope="slotScope"/>
          </template>
        </cross-table>
      </slot>
      <!-- @slot ????????????????????? -->
      <slot name="list">
        <cross-list v-if="config.mode === mode[2].value || (config.mode === mode[1].value && isMobile)"
                    :data="pageTableList"
                    :primary-key="primaryKey"
                    :view="listView"
                    :actions="config.props.actions"
                    :checked="checked"
                    :config="extend"
                    :pagination="false"
                    @checked="onChecked"
                    @infinite="onInfinite"
                    @action="onAction">
          <template v-if="config.props.view.length > 0"
                    slot="avatar"
                    slot-scope="{slotScope}">
            <slot name="avatar"
                  :slot-scope="slotScope"/>
          </template>
          <template v-if="config.props.view.length>1"
                    slot="title"
                    slot-scope="{slotScope}">
            <slot name="title"
                  :slot-scope="slotScope"/>
          </template>
          <template v-if="config.props.view.length>2"
                    slot="description"
                    slot-scope="{slotScope}">
            <slot name="description"
                  :slot-scope="slotScope"/>
          </template>
          <template v-for="v in config.props.view.filter(vw => vw.type === 'custom')"
                    :slot="v.prop"
                    slot-scope="{slotScope}">
            <slot :name="v.prop"
                  :slot-scope="slotScope"/>
          </template>
        </cross-list>
      </slot>
    </div>
    <div v-if="pagination"
         class="text-right mt10">
      <a-pagination v-model="current"
                    size="small"
                    :total="total"
                    :show-total="total => `${$t('total')}: ${total}`"
                    :page-size.sync="pageSize"
                    :page-size-options="pageSizeOptions"
                    show-size-changer
                    showLess-items
                    show-quick-jumper/>
    </div>
  </div>
</template>

<i18n src="../../locale/share/common.json"/>
<i18n>
{
  "zh-CN": {
    "selectAll": "??????"
  }
}
</i18n>

<script>
import Config from '../../tools/config'
import Device from '../../tools/device'
import Screen from '../../utils/screen'
import types from '../../mixin/types'

/**
 * ????????????
 *
 * @displayName Page Table
 * @version 2.0
 */
export default {
  name: 'cross-page-table',
  mixins: [types],
  props: {
    /**
     * ?????????
     *
     * {showMode:????????????,mode:??????,selectAll:??????,isScroll:????????????,xScroll:????????????,dragSortable:????????????{ref:????????????,enable:????????????},props:????????????{view:????????????,actions:?????????,selected:??????,meta:??????meta,columns:??????}}
     */
    config: {
      type: Object,
      default: () => {
        return {
          showMode: true,
          mode: 'table',
          selectAll: false,
          isScroll: false,
          xScroll: false,
          dragSortable: {
            ref: '',
            enable: false
          },
          props: {
            view: [],
            actions: [],
            selected: [],
            meta: 'text',
            columns: [8, 12, 12]
          }
        }
      }
    },
    /**
     * ?????????
     *
     * {key:???,title:??????,action:????????????}
     */
    buttons: {
      type: Array,
      require: false
    },
    /**
     * ??????
     *
     * {url:????????????,params:??????,method:??????????????????,callback:????????????,dataCallback:??????????????????,formData:??????????????????}
     */
    api: {
      type: Object,
      default: () => {
        return {
          url: '',
          params: {
            size: Config.pageSize
          },
          method: 'GET',
          callback: null,
          dataCallback: null,
          formData: false
        }
      }
    },
    /**
     * ??????
     *
     * @values id
     */
    primaryKey: {
      type: String,
      default: 'id'
    },
    /**
     * ????????????
     *
     * @values true,false
     */
    pagination: {
      type: Boolean,
      default: !Device.inMobile()
    },
    /**
     * ????????????
     */
    list: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    const isMobile = Device.isMobile()
    return {
      mode: [
        {value: 'card', icon: 'fa-th-large'},
        {value: 'table', icon: 'fa-th-list'},
        {value: 'list', icon: 'fa-th-list'}
      ],
      checked: {
        all: false,
        disabled: false,
        indeterminate: false
      },
      tableView: [],
      listView: [],
      cardView: [],
      /**
       * ????????????????????? ????????????
       */
      pageTableList: this.list,
      extend: {
        loading: false,
        expanded: false,
        busy: true,
        isList: !isMobile,
        isScroll: !!this.config.isScroll,
        xScroll: !!this.config.xScroll
      },
      total: 0,
      current: 1,
      pageSize: 0,
      isMobile: isMobile,
      pageSizeOptions: Config.pageSizeOptions
    }
  },
  watch: {
    current(newVal, oldVal) {
      newVal !== oldVal && this.load()
    },
    pageSize(newVal, oldVal) {
      newVal !== oldVal && this.load()
    },
    'api.params'(newVal, oldVal) {
      if (newVal === oldVal) {
        return
      }
      this.current === 1 && this.load()
      this.current = 1
    },
    // 'config.props.view': {
    //   handler: function (newVal) {
    //     this.tableView = []
    //     this.listView = []
    //     this.cardView = []
    //     newVal.forEach(item => {
    //       if (!item.singleShow) {
    //         this.tableView.push(item)
    //         this.listView.push(item)
    //         this.cardView.push(item)
    //       } else if (item.singleShow === 'card') {
    //         this.cardView.push(item)
    //       } else if (item.singleShow === 'table') {
    //         this.tableView.push(item)
    //       } else {
    //         this.listView.push(item)
    //       }
    //     })
    //   },
    //   immediate: true
    // }
  },
  methods: {
    /**
     * ?????????????????????????????????
     *
     * @param {string} key ??????key???????????????
     * @param {object} data ?????????????????????
     */
    buttonEmit({key, data}) {
      /**
       * ???????????????????????????????????????
       *
       * @property {object} args {key, data}
       */
      this.$emit('buttonEmit', {key, data})
    },
    /**
     * ????????????
     *
     * @param {event} e ????????????
     */
    onChange(e) {
      this.checked.indeterminate = false
      this.checked.all = e.target.checked
      /**
       * ??????????????????
       *
       * @property {array} pageTableList ????????????
       */
      this.$emit('checked', this.pageTableList)
    },
    /**
     * ????????????
     *
     * @param {-}
     */
    onChecked() {
      this.checked.all = this.pageTableList.every(d => d.checked)
      this.checked.indeterminate = !this.checked.all && this.pageTableList.some(d => d.checked)
      /**
       * ??????????????????
       *
       * @property {array} pageTableList ????????????
       */
      this.$emit('checked', this.pageTableList)
    },
    /**
     * ??????
     *
     * @param {object} data ??????
     */
    onAction(data) {
      /**
       * ??????????????????
       *
       * @property {object} data ??????
       */
      this.$emit('action', data)
    },
    /**
     * ????????????
     *
     * @param {-}
     */
    load() {
      if (!this.api.url) {
        return
      }
      this.extend.loading = true
      this._http({
        url: this.api.url,
        method: this.api.method || 'GET',
        params: this.api.method === 'POST' ? null : {
          current: this.current,
          ...this.api.params,
          size: this.pageSize
        },
        data: this.api.method === 'POST' ? {
          ...this.api.params,
          requestBody: {
            ...this.api.params.requestBody,
            p: this.api.params.requestBody.p ? this.current : null,
            limit: this.api.params.requestBody.limit ? this.pageSize : null
          }
        } : null
      }, this.api.formData).then(response => {
        this.extend.loading = false
        if (response.code !== 0 || !response.data) {
          return
        }
        this.isFunction(this.api.dataCallback) && this.api.dataCallback(response.data)
        const data = response.data.list || response.data.records || (this.isArray(response.data) ? response.data : [])
        if (this.config.selectAll) {
          data.forEach(d => {
            d.checked = this.config.selected ? this.config.selected.includes(d[this.primaryKey]) : false
            this.isFunction(this.api.callback) && this.api.callback(d)
          })
        } else {
          this.isFunction(this.api.callback) && data.forEach(d => this.api.callback(d))
        }
        this.total = response.data.total ? parseInt(response.data.total) : 0
        this.current === 1 && (this.pageTableList = [])
        if (this.config.isScroll || (Device.inMobile() && !this.extend.isList)) {
          this.pageTableList = this.pageTableList.concat(data)
          this.extend.busy = this.total === this.pageTableList.length
          this.onChecked()
          return
        }
        this.pageTableList = data
        this.onChecked()
      }).catch(() => {
        this.extend.loading = false
      })
    },
    /**
     * ????????????
     *
     * @param {-}
     */
    onInfinite() {
      if (this.extend.busy) {
        return
      }
      this.current++
    }
  },
  mounted() {
    Screen.listen(() => {
      this.isMobile = this.extend.isList = Device.isMobile()
      /**
       * ??????????????????????????????
       *
       * @property {boolean} isMobile ??????
       */
      this.$emit('screen', this.isMobile)
    }, true, 'cross-page-table')
    this.pageSize = this.api.params && this.isNumber(this.api.params.size) ? this.api.params.size : Config.pageSize
    const views = this.config.props.view
    this.tableView = []
    this.listView = []
    this.cardView = []
    views.forEach(item => {
      if (!item.singleShow) {
        this.tableView.push(item)
        this.listView.push(item)
        this.cardView.push(item)
      } else if (item.singleShow === 'card') {
        this.cardView.push(item)
      } else if (item.singleShow === 'table') {
        this.tableView.push(item)
      } else {
        this.listView.push(item)
      }
    })
  },
  beforeDestroy() {
    Screen.removeListen('cross-page-table')
  }
}
</script>

<style lang="scss">

.cross-page-table {
  margin-top: 1em;

  .h-flex-center {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .ant-radio-button-wrapper {
    padding: 0 8px;
  }

  .cross-checkbox {
    float: left;
  }

  .cross-button {
    display: inline;
  }

  .cross-radio-group {
    float: right;
  }

  .cross-component {
    display: none;
  }
}

@media screen and (max-width: 768px) {
  .cross-page-table {
    .cross-checkbox {
      float: right;
    }

    .cross-button {
      display: none;
    }

    .cross-radio-group {
      float: left;
    }

    .cross-component {
      display: block;
    }
  }
}
</style>
