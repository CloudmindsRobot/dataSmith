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
      <!-- @slot 自定义卡片模式 -->
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
      <!-- @slot 自定义表格模式 -->
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
      <!-- @slot 自定义列表模式 -->
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
    "selectAll": "全选"
  }
}
</i18n>

<script>
import Config from '../../tools/config'
import Device from '../../tools/device'
import Screen from '../../utils/screen'
import types from '../../mixin/types'

/**
 * 表格页面
 *
 * @displayName Page Table
 * @version 2.0
 */
export default {
  name: 'cross-page-table',
  mixins: [types],
  props: {
    /**
     * 配置项
     *
     * {showMode:模式切换,mode:模式,selectAll:全选,isScroll:滚动加载,xScroll:横向滚动,dragSortable:表格拖拽{ref:表格引用,enable:开启拖拽},props:卡片配置{view:数据视图,actions:操作组,selected:勾选,meta:视图meta,columns:列数}}
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
     * 按钮组
     *
     * {key:键,title:文本,action:执行事件}
     */
    buttons: {
      type: Array,
      require: false
    },
    /**
     * 接口
     *
     * {url:接口地址,params:参数,method:接口请求方法,callback:回调函数,dataCallback:数据回调函数,formData:返回数据格式}
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
     * 主键
     *
     * @values id
     */
    primaryKey: {
      type: String,
      default: 'id'
    },
    /**
     * 显示翻页
     *
     * @values true,false
     */
    pagination: {
      type: Boolean,
      default: !Device.inMobile()
    },
    /**
     * 数据列表
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
       * 是否支持表格行 拖拽排序
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
     * 自定义按钮组的回调事件
     *
     * @param {string} key 按钮key，区分来源
     * @param {object} data 需要解析的数据
     */
    buttonEmit({key, data}) {
      /**
       * 自定义按钮组的回调触发事件
       *
       * @property {object} args {key, data}
       */
      this.$emit('buttonEmit', {key, data})
    },
    /**
     * 勾选变更
     *
     * @param {event} e 事件对象
     */
    onChange(e) {
      this.checked.indeterminate = false
      this.checked.all = e.target.checked
      /**
       * 操作触发事件
       *
       * @property {array} pageTableList 列表数据
       */
      this.$emit('checked', this.pageTableList)
    },
    /**
     * 全选变更
     *
     * @param {-}
     */
    onChecked() {
      this.checked.all = this.pageTableList.every(d => d.checked)
      this.checked.indeterminate = !this.checked.all && this.pageTableList.some(d => d.checked)
      /**
       * 操作触发事件
       *
       * @property {array} pageTableList 列表数据
       */
      this.$emit('checked', this.pageTableList)
    },
    /**
     * 操作
     *
     * @param {object} data 操作
     */
    onAction(data) {
      /**
       * 操作触发事件
       *
       * @property {object} data 操作
       */
      this.$emit('action', data)
    },
    /**
     * 加载数据
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
     * 滚动加载
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
       * 窗体大小改变触发事件
       *
       * @property {boolean} isMobile 操作
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
