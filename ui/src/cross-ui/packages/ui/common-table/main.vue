<template>
  <div>
    <a-table v-if="config.expanded"
             :ref="dragSortable.ref"
             :row-key="primaryKey"
             size="middle"
             :loading="config.loading"
             :data-source="data"
             :pagination="pagination"
             :row-selection="rowSelection">
      <!-- @slot 自定义行展开区域 -->
      <template slot="expandedRowRender"
                slot-scope="record">
        <slot name="expandedRow"
              :slot-scope="record"/>
      </template>
      <a-table-column v-for="v in view"
                      :key="v.prop"
                      :title="isFunction(v.locale) ? v.locale() : $t(v.prop)"
                      :data-index="v.prop"
                      :ellipsis="isDefined(v.ellipsis) ? v.ellipsis : false"
                      :width="v.width"
                      class="cross-table-column">
        <template slot="customRender"
                  slot-scope="text, record">
          <template v-if="v.type === 'media'">
            <a v-if="text"
               class="image text-center"
               :data-fancybox="fancyboxGroup"
               :data-type="record.icon ? 'video' : 'image'"
               :data-src="isFunction(v.format) ? v.format(text) : text"
               :style="record.icon ? record.thumb : {backgroundImage: `url(${record.thumb ? record.thumb : (isFunction(v.format) ? v.format(text) : text)})`}">
              <div v-if="record.icon"
                   class="play">
                <i v-if="record.icon.indexOf('fa')!==-1"
                   :class="record.icon"/>
                <a-icon v-else
                        class="font42"
                        :type="record.icon"/>
              </div>
            </a>
            <a-icon v-else
                    type="picture"
                    class="font42"/>
          </template>
          <a v-else-if="v.type === 'link'"
             @click="e => {e.preventDefault();e.stopPropagation();isFunction(v.click) && v.click(record)}"
             v-html="isFunction(v.format) ? v.format(text) : numberFormat(text)"/>
          <div v-else-if="v.type === 'scale'"
               class="scale scale-bg text-center font12"
               :style="{width: `${text[0]/text[1]*42}px`, height: '42px'}"
               v-html="isFunction(v.format) ? v.format(text) : numberFormat(text)"/>
          <a-tag v-else-if="v.type === 'tag'"
                 :color="isFunction(v.color) ? v.color(text) : (v.color || 'blue')">
            {{ isFunction(v.format) ? v.format(text) : numberFormat(text) }}
          </a-tag>
          <!-- @slot 自定义数据展示 -->
          <slot v-else-if="v.type === 'custom'"
                :name="v.prop"
                :slot-scope="record"/>
          <cross-copy-cell v-else-if="v.type === 'copy'" :copyVal="isFunction(v.format) ? v.format(text) : text" :classKey="`copy-${v.prop}-${index}`"> </cross-copy-cell>
          <a-tooltip v-else-if="v.ellipsis"
                     placement="topLeft">
            <template slot="title">
              {{ isFunction(v.format) ? v.format(text) : numberFormat(text) }}
            </template>
            <span v-html="isFunction(v.format) ? v.format(text) : numberFormat(text)"/>
          </a-tooltip>
          <span v-else
                v-html="isFunction(v.format) ? v.format(text) : numberFormat(text)"/>
        </template>
      </a-table-column>
      <a-table-column v-if="dragSortable.enable"
                      :with="50"
                      key="drag">
        <a-tooltip slot-scope="text"
                   :title="$t('drag')">
          <a-icon class="iconfont icon-bg"
                  type="drag"
                  @mouseenter="drag(false)"
                  @mouseleave="drag(true)"/>
        </a-tooltip>
      </a-table-column>
      <a-table-column v-if="actions.length"
                      key="action"
                      fixed="right"
                      class="cross-table-column"
                      :width="actions.length > 2 ? 130 : 90"
                      :title="$t('operate')">
        <template slot-scope="text, record">
          <span v-for="(a, index) in (actions.length > 3 ? actions.slice(0, 2) : actions)"
            :key="a.key">
            <a-divider v-if="index != 0"
                       type="vertical" />
            <a-tooltip>
              <template slot="title">
                {{ isFunction(a.locale) ? a.locale() : $t(a.key) }}
              </template>
              <i v-if="a.icon && (a.icon.startsWith('fa') || a.icon.startsWith('icon'))"
                @click="onClick({item: {value: record}, key: a.key})"
                :class="['iconfont', 'icon-bg', a.icon.startsWith('fa') ? 'fa' : '', a.icon, (isFunction(a.disabled) ? a.disabled(record) : a.disabled) ? 'disabled' : '']"/>
              <a-icon v-else
                @click="onClick({item: {value: record}, key: a.key})"
                :class="['iconfont', 'icon-bg', (isFunction(a.disabled) ? a.disabled(record) : a.disabled) ? 'disabled' : '']"
                :type="a.icon"/>
            </a-tooltip>
          </span>
          <template v-if="actions.length > 3">
            <a-divider type="vertical" />
            <a-dropdown :trigger="['click']">
              <span class="ant-dropdown-link"
                @click="e => e.preventDefault()">
                <a-tooltip>
                  <template slot="title">
                    {{ $t('select') }}
                  </template>
                  <a-icon type="ellipsis"
                          class="iconfont icon-bg"/>
                </a-tooltip>
              </span>
              <a-menu slot="overlay"
                      @click="onClick">
                <a-menu-item v-for="m in actions.slice(2)"
                            :disabled="isFunction(m.disabled) ? m.disabled(record) : !!m.disabled"
                            :value="record"
                            :key="m.key">
                  {{ isFunction(m.locale) ? m.locale() : $t(m.key) }}
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </template>
        </template>
      </a-table-column>
    </a-table>
    <a-table v-else
             :ref="dragSortable.ref"
             :row-key="primaryKey"
             size="middle"
             :loading="config.loading"
             :data-source="data"
             :pagination="pagination"
             :row-selection="rowSelection"
             :scroll="config.xScroll ? undefined : {x: 992}">
      <a-table-column v-for="(v, index) in view"
                      :key="v.prop"
                      :title="isFunction(v.locale) ? v.locale() : $t(v.prop)"
                      :data-index="v.prop"
                      :ellipsis="isDefined(v.ellipsis) ? v.ellipsis : false"
                      :width="v.width"
                      class="cross-table-column">
        <template slot="customRender"
                  slot-scope="text, record">
          <template v-if="v.type === 'media'">
            <a v-if="text"
               class="image text-center"
               :data-fancybox="fancyboxGroup"
               :data-type="record.icon ? 'video' : 'image'"
               :data-src="isFunction(v.format) ? v.format(text) : text"
               :style="record.icon ? record.thumb : {backgroundImage: `url(${record.thumb ? record.thumb : (isFunction(v.format) ? v.format(text) : text)})`}">
              <div v-if="record.icon"
                   class="play">
                <i v-if="record.icon.indexOf('fa')!==-1"
                   :class="record.icon"/>
                <a-icon v-else
                        class="font42"
                        :type="record.icon"/>
              </div>
            </a>
            <a-icon v-else
                    type="picture"
                    class="font42"/>
          </template>
          <a v-else-if="v.type === 'link'"
             @click="e => {e.preventDefault();e.stopPropagation();isFunction(v.click) && v.click(record)}"
             v-html="isFunction(v.format) ? v.format(text) : numberFormat(text)"/>
          <div v-else-if="v.type === 'scale'"
               class="scale scale-bg text-center font12"
               :style="{width: `${text[0]/text[1]*42}px`, height: '42px'}"
               v-html="isFunction(v.format) ? v.format(text) : numberFormat(text)"/>
          <a-tag v-else-if="v.type === 'tag'"
                 :color="isFunction(v.color) ? v.color(text) : (v.color || 'blue')">
            {{ isFunction(v.format) ? v.format(text) : numberFormat(text) }}
          </a-tag>
          <!-- @slot 自定义数据展示 -->
          <slot v-else-if="v.type === 'custom'"
                :name="v.prop"
                :slot-scope="record"/>
          <cross-copy-cell v-else-if="v.type === 'copy'" :copyVal="isFunction(v.format) ? v.format(text) : text" :classKey="`copy-${v.prop}-${index}`"> </cross-copy-cell>
          <a-tooltip v-else-if="v.ellipsis">
            <template slot="title">
              {{ v.ellipsis ? (isFunction(v.format) ? v.format(text) : numberFormat(text)) : '' }}
            </template>
            <span v-html="isFunction(v.format) ? v.format(text) : numberFormat(text)"/>
          </a-tooltip>
          <span v-else
                v-html="isFunction(v.format) ? v.format(text) : numberFormat(text)"/>
          </template>
      </a-table-column>
      <a-table-column v-if="dragSortable.enable"
                      key="drag"
                      :with="50">
        <a-tooltip slot-scope="text"
                   :title="$t('drag')">
          <a-icon class="iconfont icon-bg"
                  type="drag"
                  @mouseenter="drag(false)"
                  @mouseleave="drag(true)"/>
        </a-tooltip>
      </a-table-column>
      <a-table-column v-if="actions.length"
                      key="action"
                      fixed="right"
                      class="cross-table-column"
                      :width="actions.length > 2 ? 130 : 90"
                      :title="$t('operate')">
        <template slot-scope="text, record">
          <span v-for="(a, index) in (actions.length > 3 ? actions.slice(0, 2) : actions)"
            :key="a.key">
            <a-divider v-if="index != 0"
                       type="vertical" />
            <a-tooltip>
              <template slot="title">
                {{ isFunction(a.locale) ? a.locale() : $t(a.key) }}
              </template>
              <i v-if="a.icon && (a.icon.startsWith('fa') || a.icon.startsWith('icon'))"
                @click="onClick({item: {value: record}, key: a.key})"
                :class="['iconfont', 'icon-bg', a.icon.startsWith('fa') ? 'fa' : '', a.icon, (isFunction(a.disabled) ? a.disabled(record) : a.disabled) ? 'disabled' : '']"/>
              <a-icon v-else
                @click="onClick({item: {value: record}, key: a.key})"
                :class="['iconfont', 'icon-bg', (isFunction(a.disabled) ? a.disabled(record) : a.disabled) ? 'disabled' : ''] "
                :type="a.icon"/>
            </a-tooltip>
          </span>
          <template v-if="actions.length > 3">
            <a-divider type="vertical" />
            <a-dropdown :trigger="['click']">
              <span class="ant-dropdown-link"
                @click="e => e.preventDefault()">
                <a-tooltip>
                  <template slot="title">
                    {{ $t('select') }}
                  </template>
                  <a-icon type="ellipsis"
                          class="iconfont icon-bg"/>
                </a-tooltip>
              </span>
              <a-menu slot="overlay"
                      @click="onClick">
                <a-menu-item v-for="m in actions.slice(2)"
                            :disabled="isFunction(m.disabled) ? m.disabled(record) : !!m.disabled"
                            :value="record"
                            :key="m.key">
                  {{ isFunction(m.locale) ? m.locale() : $t(m.key) }}
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </template>
        </template>
      </a-table-column>
    </a-table>
  </div>
</template>

<i18n src="../../locale/share/common.json"></i18n>

<script>
import types from '../../mixin/types'
import cardTable from '../../mixin/cardTable'
import tableList from '../../mixin/tableList'
import format from '../../mixin/format'
import Sortable from 'sortablejs'

/**
 * 表格
 *
 * @displayName Common Table
 * @version 1.0
 */
export default {
  name: 'cross-table',
  mixins: [types, cardTable, tableList, format],
  data() {
    return {
      fancyboxGroup: `table-gallery-${parseInt(Math.random() * 10000)}`,
      sortTable: null
    }
  },
  props: {
    /**
     * 可拖拽
     *
     * {ref:表格引用,enable:开启拖拽排序}
     */
    dragSortable: {
      type: Object,
      default: () => {
        return {
          ref: '',
          enable: false
        }
      }
    }
  },
  computed: {
    /**
     * 选中行
     *
     * @returns {{onChange: onChange, selectedRowKeys: *[], fixed: boolean, getCheckboxProps: (function(*): {props: {disabled: *}})}|null}
     */
    rowSelection() {
      return this.data.some(d => this.isDefined(d.checked)) ? {
        fixed: !this.config.expanded,
        selectedRowKeys: this.data.filter(d => d.checked).map(d => d[this.primaryKey]),
        onChange: (selectedRowKeys) => {
          this.data.forEach(d => {
            d.checked = selectedRowKeys.includes(d[this.primaryKey])
          })
          this.selectedRowKeys = selectedRowKeys
          /**
           * 操作触发事件
           */
          this.$emit('checked')
        },
        getCheckboxProps: record => ({
          props: {
            disabled: record.disabled
          }
        })
      } : null
    }
  },
  methods: {
    /**
     * 拖拽事件
     *
     * @param {-}
     */
    triggerDropEvent () {
      document.body.ondrop = function (event) {
        event.preventDefault()
        event.stopPropagation()
      }
      console.log(this.$refs[this.dragSortable.ref].$el)
      const tbody = this.$refs[this.dragSortable.ref].$el.querySelector('.ant-table-tbody')
      const _this = this
      this.sortTable = Sortable.create(tbody, {
        disabled: true,
        onEnd ({ newIndex, oldIndex }) {
          const currRow = _this.data.splice(oldIndex, 1)[0]
          _this.data.splice(newIndex, 0, currRow)
          _this.sortFormTableData()
        }
      })
    },
    /**
     * 重置数据排序
     *
     * @param {-}
     */
    sortFormTableData () {
      this.drag(true)
      const total = this.data.length
      const array = this.data
      for (let index = array.length - 1; index !== -1; index--) {
        this.$set(this.data[index], 'sort', total - index - 1)
      }
    },
    /**
     * 拖拽
     *
     * @param {boolean} flag 标识
     */
    drag (flag) {
      this.sortTable && this.sortTable.option('disabled', flag)
    }
  },
  mounted () {
    this.dragSortable.ref && this.triggerDropEvent()
  }
}
</script>

<style lang="scss">
.cross-table-column {
  position: relative;
  .image {
    width: 42px;
    height: 42px;
    line-height: 42px;
    display: block;
    outline: none;
    text-decoration: none;
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center;
    color: unset;
    position: relative;
  }

  .scale {
    line-height: 42px;
  }
  // .iconfont{
  //   margin: 0 6px;
  // }
  .act-btn{
    position: relative;
    &:after{
      position: absolute;
      content: '';
      width: 0;
      height: 12px;
      border-right: 1px solid rgba(0, 0, 0, 0.06);
      right: 0;
      top: 50%;
      transform: translateY(-50%);
    }
  }

  .ant-tag{
    margin-right: 0;
  }
}
</style>
