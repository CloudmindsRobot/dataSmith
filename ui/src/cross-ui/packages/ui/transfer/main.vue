<template>
  <cross-modal :modal="modal"
               @cancel="onCancel"
               @ok="onOk">
    <a-transfer :locale="transfer.locale"
                :data-source="transfer.dataSource"
                :disabled="transfer.disabled"
                :filter-option="transfer.filterOption"
                :operations="transfer.operations"
                :render="transfer.render"
                :selected-keys="transfer.selectedKeys"
                :show-search="transfer.showSearch"
                :list-style="transfer.listStyle"
                :target-keys="transfer.targetKeys"
                :titles="transfer.titles"
                @change="change"
                @scroll="scroll"
                @search="search"
                @selectChange="selectChange">
      <div v-if="transfer.customListFlag"
           slot="children"
           slot-scope="slotData">
        <!-- @slot 自定义渲染列表(customList) -->
        <slot name="customList"
              :slotData="slotData"></slot>
      </div>
      <div v-if="transfer.customFooterFlag"
           slot="footer"
           slot-scope="props">
        <!-- @slot 对底部进行自定义渲染(customFooter) -->
        <slot name="customFooter"
              :props="props"></slot>
      </div>
    </a-transfer>
  </cross-modal>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import types from '../../mixin/types'
import transfer from '../../mixin/modal'

/**
 * 穿梭框
 *
 * @displayName Transfer
 * @version 1.0
 */
export default {
  name: 'cross-transfer',
  mixins: [types, transfer],
  props: {
    /**
     * 穿梭框对象配置项
     *
     * {
     *  locale: 各种语言
     * 	customListFlag: 是否启用自定义渲染列表,
     *  customFooterFlag: 是否启用底部自定义插槽,
     *  dataSource: 数据源，其中的数据将会被渲染到左边一栏中(targetKeys中指定的除外),
     *  disabled: 是否禁用整个穿梭框,
     *  filterOption: 自定义搜索函数,
     *  listStyle: 自定义穿梭框样式,
     *  operations: 操作文案集合，顺序从上至下,
     *  render: 每行数据渲染函数，该函数的入参为dataSource中的项，返回值为element。或者返回一个普通对象，其中label字段为element，value字段为title,
     *  selectedKeys: 设置哪些项应该被选中,
     *  showSearch: 是否显示搜索框,
     *  showSelectAll: 是否显示全选勾选框,
     *  targetKeys: 显示在右侧框数据的key集合,
     *  title: 标题集合，顺序从左至右
     * }
     */
    transfer: {
      type: Object,
      default: () => {
        return {
          locale: {itemUnit: '项', itemsUnit: '项', notFoundContent: '列表为空', searchPlaceholder: '请输入搜索内容'},
          customListFlag: false,
          customFooterFlag: false,
          dataSource: [],
          disabled: false,
          filterOption: Function,
          listStyle: {},
          operations: ['>', '<'],
          render: Function,
          selectedKeys: [],
          showSearch: false,
          showSelectAll: true,
          targetKeys: [],
          titles: ['', '']
        }
      }
    }
  },
  data() {
    return {}
  },
  methods: {
    /**
     * 选项在两栏之间转移时的回调函数
     *
     * @param {array} targetKeys 显示在右侧框数据的key集合
     * @param {string} direction 渲染列表的方向
     * @param {array} moveKeys 移动数据的key集合
     */
    change(targetKeys, direction, moveKeys) {
      /**
       * 选项在两栏之间转移时的触发事件
       *
       * @property {array} targetKeys 显示在右侧框数据的key集合
       * @property {string} direction 渲染列表的方向
       * @property {array} moveKeys 移动数据的key集合
       */
      this.$emit('change', targetKeys, direction, moveKeys)
    },
    /**
     * 选项列表滚动时的回调函数
     *
     * @param {string} direction 渲染列表的方向
     * @param {event} event 事件对象
     */
    scroll(direction, event) {
      /**
       * 选项列表滚动时的触发事件
       *
       * @property {string} direction 渲染列表的方向
       * @property {event} event 事件对象
       */
      this.$emit('scroll', direction, event)
    },
    /**
     * 搜索内容
     *
     * @param {string} direction 渲染列表的方向
     * @param {string} value 搜索内容
     */
    search(direction, value) {
      /**
       * 搜索框内容改变时的触发事件
       *
       * @property {string} direction 渲染列表的方向
       * @property {string} value 搜索内容
       */
      this.$emit('search', direction, value)
    },
    /**
     * 两侧改变选中项
     *
     * @param {array} sourceSelectedKeys 左侧框选中数据Key集合
     * @param {array} targetSelectedKeys 右侧框选中数据Key集合
     */
    selectChange(sourceSelectedKeys, targetSelectedKeys) {
      /**
       * 两侧选中项发生改变时触发事件
       *
       * @property {array} sourceSelectedKeys 左侧框选中数据Key集合
       * @property {array} targetSelectedKeys 右侧框选中数据Key集合
       */
      this.$emit('selectChange', sourceSelectedKeys, targetSelectedKeys)
    }
  }
}
</script>

<style lang="scss">
.cross-transfer-modal {
  .ant-modal {
    min-width: auto;
  }

  .ant-transfer {
    display: flex;
    align-items: center;
  }

  .ant-transfer-list-body {
    display: flex;
    flex-direction: column;
  }

  .ant-transfer-list,
  .ant-transfer-customize-list .ant-transfer-list {
    flex: 1;
    width: auto;
    height: 362px;
  }

  .ant-transfer-customize-list .ant-transfer-list-body-customize-wrapper {
    overflow: auto;
  }
}

@media screen and (max-width: 768px) {
  .cross-transfer-modal {
    .ant-modal {
      width: 100% !important;
    }
  }
}
</style>