<template>
  <span>
    <a-button class="ml16" :ghost="ghost || false"
              :type="type || ''"
              @click="!action ? defaultClick() : action()"
              :disabled="!!disabled">{{ $t(title) }}</a-button>
    <a-modal :title="$t(title)"
             :visible="visible"
             :confirm-loading="confirmLoading"
             :width="640"
             @cancel="clearValidateStates">
      <p>
        <b>
        {{ $t('exportTips', {total: pageTotal, max: form.size, maxP: Math.ceil(pageTotal / form.size)}) }}
        </b>
      </p>
      <a-form-model :model="form">
        <a-form-model-item :label="$t('selectSize')">
          <a-radio-group :options="sizeOptions"
                         v-model="formSize"
                         @change="changeSize"/>
          <a-tooltip>
            <template slot="title">
              {{ $t('sizeLimitTip') }}
            </template>
            <a-input type="number"
                     v-show="formSize === $t('customize')"
                     v-model="form.size"
                     max="10000" min="500"
                     style="width: 200px"
                     :placeholder="$t('linePlaceholder')"/>
          </a-tooltip>
        </a-form-model-item>
        <a-form-model-item :label="$t('selectPage')">
          <a-select v-model="form.current">
            <a-select-option :value="index"
                             v-for="index in totalPage"
                             :key="index">
              {{ index }}
            </a-select-option>
          </a-select>
          <!-- <a-input type="number" :max="Math.ceil(pageTotal / form.size)" v-model="form.current" min="1" /> -->
        </a-form-model-item>
      </a-form-model>
      <template slot="footer">
        <a-button key="submit"
                  type="primary"
                  :loading="confirmLoading"
                  @click="handleOk">
          {{ $t('confirm') }}
        </a-button>
      </template>
    </a-modal>
  </span>
</template>

<i18n>
{
  "zh-CN": {
    "export": "导出",
    "selectPage": "选择导出第几页",
    "selectSize": "设置每一页的行数",
    "exportTips": "共{total}行数据，根据行数/每页分次下载",
    "defaultExportTitle": "导出文件",
    "sizeLimitTip": "分页数量限制在500-10000之间",
    "linePlaceholder": "请输入行数",
    "confirm": "确认导出",
    "customize": "自定义"
  }
}
</i18n>

<script>
import exportBtn from '../../mixin/exportBtn'
import confirm from '../../mixin/confirm'

/**
 * 导出按钮，常见如下：
 *
 *
 * @displayName Export Button
 * @version 1.0
 * @example [none]
 */

export default {
  name: 'cross-export-btn',
  mixins: [exportBtn, confirm],
  props: {
    /**
     * 按钮样式,是否镂空
     */
    ghost: Boolean,
    /**
     * 按钮风格
     */
    type: String,
    /**
     * 按钮是否禁用
     */
    disabled: {
      type: [Boolean, Function],
      default: false
    },
    /**
     * 按钮标题
     */
    title: {
      type: String,
      default: 'export'
    },
    /**
     * 根据表单当前的查询参数
     */
    params: Object,
    /**
     * 查询出来的总页数，为了方便做分页导出
     */
    pageTotal: {
      type: Number,
      require: true
    },
    /**
     * 导出功能调用的api接口
     */
    apiUrl: String, //
    /**
     * 导出表单调用的api方法
     *
     * @values POST,GET
     */
    apiMethod: {
      type: String,
      default: 'POST'
    },
    /**
     * 导出文件生成的标题
     */
    exportTitle: {
      type: String,
      default: 'defaultExportTitle'
    },
    /**
     * 自定义的点击事件
     */
    action: {
      type: Function
    }
  },
  data() {
    return {
      visible: false,
      confirmLoading: false,
      form: {
        size: '1000',
        current: '1'
      },
      formSize: '1000',
      param: null,
    }
  },
  components: {},
  computed: {
    /**
     * 尺寸选项
     *
     * @returns {*[]|(string|string|VueI18n.LocaleMessages)[]}
     */
    sizeOptions() {
      return this.defaultOptions ? [...this.defaultOptions, this.$t('customize')] : ['1000', '2000', '5000', '10000', this.$t('customize')]
    },
    /**
     * 总共可以分成的页数
     *
     * @returns {number|number}
     */
    totalPage() {
      const num = Math.ceil(this.pageTotal / this.form.size)
      if (num !== 0 && this.form.current > num) {
        this.form.current = num
      }
      return num || 1
    }
  },
  methods: {
    /**
     * 点击导出按钮之后的默认操作---显示分页导出选择面板
     *
     * @param {-}
     */
    defaultClick() {
      this.visible = true
    },
    /**
     * 修改导出数量的回调
     *
     * @param {Object} e 事件类
     */
    changeSize(e) {
      let num = e.target.value
      !isNaN(num) && (this.form.size = +num)
    },
    /**
     * 点击提交的回调
     *
     * @param {-}
     */
    handleOk() {
      let exportParams = Object.assign({}, this.params, this.form)
      process.env.NODE_ENV === "development" && console.warn('点击导出, 参数:', exportParams)
      this.exportData(this.apiUrl, exportParams, this.exportTitle)
      this.form.size = this.formSize = '1000'
      // this.visible = false
    },
    /**
     * 关闭面板的清空处理
     *
     * @param {-}
     */
    clearValidateStates() {
      this.form.size = this.formSize = '1000'
      this.form.current = 1
      this.visible = false
    }
  }
}
</script>
