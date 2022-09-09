<template>
  <div class="cross-descriptions">
    <template v-if="viewGroup.length">
       <div class="text-right">
        <a-button v-if="editing" type="link" icon="edit" @click="editMode">
          {{ $t('edit') }}
        </a-button>
      </div>
      <a-descriptions v-for="(view, index) in viewGroup"
                      :key="`view${index}`"
                      :size="size"
                      :colon="colon"
                      :column="{ xs: columns[0], sm: columns[0], md: columns[0], lg: columns[1], xl: columns[1], xxl: columns[2]}"
                      :class="{'mb15': index < viewGroup.length - 1, 'h-flex': columns.toString() == [1,1,1].toString()}">
        <a-descriptions-item v-for="v in view" :key="v.prop" :label="isFunction(v.locale) ? v.locale() : $t(v.prop)">
          <a v-if="v.type === 'link'"
            @click="isFunction(v.click) ? v.click(dataGroups[index]) : e => e.preventDefault()"
            v-html="isFunction(v.format) ? v.format(dataGroups[index][v.prop]) : numberFormat(dataGroups[index][v.prop])"/>
          <a-tag v-else-if="v.type === 'tag'"
            :color="isFunction(v.color) ? v.color(dataGroups[index][v.prop]) : (v.color || 'blue')">
            {{ isFunction(v.format) ? v.format(dataGroups[index][v.prop]) : numberFormat(dataGroups[index][v.prop]) }}
          </a-tag>
          <!-- @slot 自定义数据展示 -->
          <slot v-else-if="v.type === 'custom'" :name="v.prop" :slot-scope="dataGroups[index]"/>
          <cross-copy-cell v-else-if="v.type === 'copy'" :position="columns.toString() == [1,1,1].toString() ? 'left' : 'right'" :copyVal="isFunction(v.format) ? v.format(dataGroups[index][v.prop], v.prop) : numberFormat(dataGroups[index][v.prop])" :classKey="`copy-${v.prop}-${index}`"/>
          <a-tooltip v-else-if="v.ellipsis">
            <template slot="title">
              {{ isFunction(v.format) ? v.format(dataGroups[index][v.prop], v.prop) : numberFormat(dataGroups[index][v.prop]) }}
            </template>
            <div class="text-ellipsis" v-html="isFunction(v.format) ? v.format(dataGroups[index][v.prop], v.prop) : numberFormat(dataGroups[index][v.prop])"/>
          </a-tooltip>
          <span v-else :class="{'pre': v.type==='pre'}" v-html="isFunction(v.format) ? v.format(dataGroups[index][v.prop], v.prop) : numberFormat(dataGroups[index][v.prop])"/>
        </a-descriptions-item>
      </a-descriptions>
    </template>

    <a-descriptions v-else-if="view.length"
      :colon="colon"
      :column="{ xs: columns[0], sm: columns[0], md: columns[0], lg: columns[1], xl: columns[1], xxl: columns[2]}"
      :class="{'h-flex': columns.toString() == [1,1,1].toString() }"
      :size="size">
      <slot v-if="title" 
            name="title"
            slot="title">
            {{ isFunction(title) ? title() : $t(title) }}
      </slot>
      <a-descriptions-item
        v-for="(v, index) in view"
        :key="v.prop"
        :class="{'h-flex': columns.toString() == [1,1,1].toString(), 'over-h': v.ellipsis }"
        :label="isFunction(v.locale) ? v.locale() : $t(v.prop)">
        <template v-if="v.type === 'link'">
          <a-tooltip v-if="v.ellipsis">
            <template slot="title">
              {{ isFunction(v.format) ? v.format(dataModel.model[v.prop]) : numberFormat(dataModel.model[v.prop]) }}
            </template>
            <a @click="isFunction(v.click) ? v.click(dataModel.model) : e => e.preventDefault()"
            :class="text-ellipsis"
            v-html="isFunction(v.format) ? v.format(dataModel.model[v.prop]) : numberFormat(dataModel.model[v.prop])"/>
          </a-tooltip>
          <a v-else
            @click="isFunction(v.click) ? v.click(dataModel.model) : e => e.preventDefault()"
            v-html="isFunction(v.format) ? v.format(dataModel.model[v.prop]) : numberFormat(dataModel.model[v.prop])"/>
        </template>
        <a-tag v-else-if="v.type === 'tag'"
               :color="isFunction(v.color) ? v.color(dataModel.model[v.prop]) : (v.color || 'blue')">
          {{ isFunction(v.format) ? v.format(dataModel.model[v.prop]) : numberFormat(dataModel.model[v.prop]) }}
        </a-tag>
        <!-- @slot 自定义数据展示 -->
        <slot v-else-if="v.type === 'custom'" :name="v.prop" :slot-scope="dataModel.model"/>
        <cross-copy-cell v-else-if="v.type === 'copy'" :position="columns.toString() == [1,1,1].toString() ? 'left' : 'right'" :copyVal="isFunction(v.format) ? v.format(dataModel.model[v.prop], v.prop) : numberFormat(dataModel.model[v.prop])" :classKey="`copy-${v.prop}-${index}`"/>
        <a-tooltip v-else-if="v.ellipsis">
          <template slot="title">
            {{ isFunction(v.format) ? v.format(dataModel.model[v.prop], v.prop) :numberFormat(dataModel.model[v.prop]) }}
          </template>
          <div class="text-ellipsis" v-html="isFunction(v.format) ? v.format(dataModel.model[v.prop], v.prop) : numberFormat(dataModel.model[v.prop])"/>
        </a-tooltip>
        <span v-else :class="{'pre': v.type==='pre'}" v-html="isFunction(v.format) ? v.format(dataModel.model[v.prop], v.prop) : numberFormat(dataModel.model[v.prop])"/>
      </a-descriptions-item>
      <a-descriptions-item v-if="editing" :label="$t('operate')">
        <a @click="editMode">
          {{ $t('edit') }}
        </a>
      </a-descriptions-item>
    </a-descriptions>
    <template v-else>
      <a-empty :image="emptyImage"/>
      <div class="text-right" v-if="editing">
        <a-button type="link" icon="edit" @click="editMode">
          {{ $t('edit') }}
        </a-button>
      </div>
    </template>
  </div>
</template>

<i18n src="../../locale/share/common.json"></i18n>

<script>
import descriptions from '../../mixin/descriptions'
import types from '../../mixin/types'
import empty from '../../mixin/empty'
import format from '../../mixin/format'

/**
 * 详情
 *
 * @displayName Descriptions
 * @version 1.0
 */
export default {
  name: 'cross-descriptions',
  mixins: [descriptions, types, empty, format],
  props: {
    /**
     * 表单数据
     *
     * {model:数据键值对}
     */
    dataModel: {
      type: Object,
      default: () => {
        return {model: {}}
      }
    },
    /**
     * 标题
     */
    title: {
      type: String|Function
    },
    /**
     * 配置 Descriptions.Item 的 colon 的默认值
     *
     * @values true,false
     */
    colon: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    /**
     * 编辑模式
     *
     * @param {-}
     */
    editMode() {
      /**
       * 触发编辑事件
       *
       * @property {boolean} arg 参数
       */
      this.$emit('edit', true)
    }
  }
}
</script>
<style lang="scss">
.h-flex .ant-descriptions-row > th, .h-flex .ant-descriptions-row > td{
  display: flex;
  justify-content: space-between;
  .ant-descriptions-item-label{
    flex: 0 0 auto;
  }
  .ant-descriptions-item-content{
    // flex: 1 1 auto;
    text-align: right;
    word-break: break-all;
    overflow: hidden;
    height: auto;
  }
}

.cross-descriptions{
  .ant-tag{
    margin-right: 0;
  }
}
</style>
