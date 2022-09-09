<template>
  <span>
    <a-button class="ml16"
              :ghost="ghost || false"
              :type="type || ''"
              :disabled="disabled"
              @click="startImport">
      {{ $t(title) }}
    </a-button>
    <cross-operate-dialog :modal="modal"
                          :data-model="dataModel"
                          :api="api"
                          :option="option"
                          :rule="rule"
                          :callback="callback"
                          @dataComplete="onFormComplete">
      <template slot="extend">
        <p v-if="!!exportTemplate">
          <b>
            {{$t('download')}}
            <a @click="downloadTemplate">
              {{$t('template')}}
            </a>
            {{$t('importTips')}}
          </b>
        </p>
      </template>
    </cross-operate-dialog>
    <cross-result-panel :visible="resultVisible"
                        :resultColumns="resultColumns"
                        :resultData="result"
                        @close="closeResultPanel"/>
  </span>
</template>

<i18n src="../../locale/share/common.json"></i18n>
<i18n>
{
  "zh-CN": {
    "download": "下载",
    "import": "批量导入",
    "template": "示例模板",
    "importTips": "以查看所需格式的示例。",
    "uploadFile": "上传文件"
  }
}
</i18n>

<script>
import forms from '../../tools/form'
import exportBtn from '../../mixin/exportBtn'
/**
 * 导入按钮，常见如下：
 *
 * @displayName Import Button
 * @version 1.0
 * @example [none]
 */
export default {
  name: 'cross-import-btn',
  mixins: [exportBtn],
  props: {
    /**
     * 提交表单提交的API
     */
    api: {
        type: String,
        default: ''
    },
    /**
     * 提交表单配置选项
     */
    option: {
        type: Object,
        default: () => {
            return forms.options.default()
        }
    },
    /**
     * 按钮是否禁用
     */
    disabled: {
      type: [Boolean, Function],
      default: false
    },
    /**
     * 提交表单回调函数
     */
    callback: Function,
    /**
     * 按钮样式,是否镂空
     */
    ghost: Boolean,
    /**
     * 按钮风格
     */
    type: String,
    /**
     * 弹窗与按钮的标题
     */
    title: {
      type: String,
      default: 'import'
    },
    /**
     * 提交表单项规则
     */
    rule: {
      type: Array,
      default: () => {
        return []
      }
    },
    /**
     * 表单数据
     *
     * {model:数据项}
    */
    dataModel: {
      type: Object,
      default: () => {
        return {
          model: {}
        }
      }
    },
    /**
     * 自定义按钮点击事件
     *
     * @values true,false
     */
    onClickHandle: {
      type: Function,
      required: false
    },
    /**
     * 导入模板文件的下载配置
     *
     * {model:两种模板下载模式(api\url)api模式通过接口请求得到,url模式直接通过地址拿到模板资源,fileName:模板文件导出名称,api:模板文件请求接口,fileUrl:模板文件资源地址}
     */
    exportTemplate: {
      type: Object,
      default: () => {
        return {
          model: 'api',
          fileName: '',
          api: '',
          fileUrl: ''
        }
      }
    },
    /**
     * 导入结果的错误信息展示格式
     */
    resultColumns: {
      type: Array
    },
    /**
     * 提交表单后的数据面板格式化
     */
    onResultHandle: {
      type: Function,
      required: false
    },
    /**
     * 关闭结果面板的回调，常规操作是刷新表格。
     */
    onCloseHandle: {
      type: Function,
      required: false
    }
  },
  data () {
    return {
      resultVisible: false,
      result: {
        successNum: 0,
        failureList: []
      },
      modal: {
        title: this.$t(this.title),
        visible: false
      }
    }
  },
  methods: {
    /**
     * 点击导入按钮事件
     *
     * @param {-}
     */
    startImport () {
      // 如果没传自定义的点击操作，就会默认展开导入弹框， 包含模板下载和上传按键
      if (this.onClickHandle) {
        this.onClickHandle.call(this)
      } else {
        this.defaultClick()
      }
    },
    /**
     * 点击导入按钮之后的默认操作---弹出上传表单
     *
     * @param {-}
     */
    defaultClick () {
      this.modal.visible = true
    },
    /**
     * 上传文件前的格式模板下载配置
     *
     * @param {-}
     */
    downloadTemplate () {
      if (this.exportTemplate.model === 'api') {
        this.exportData(this.exportTemplate.api, {}, this.exportTemplate.fileName)
      } else {
        if (!this.exportTemplate.fileUrl) {
          process.env.NODE_ENV === "development" && console.error('没有给导入按钮传exportTemplate.fileUrl，模板文件地址')
          return
        }
        window.open(`${window.location.protocol + this.exportTemplate.fileUrl}`)
      }
    },
    /**
     * 点击确定按钮返回数据完成事件
     *
     * @param {object|blob} response 后端返回的数据
     */
    onFormComplete (response) {
      process.env.NODE_ENV === "development" && console.log('提交结果', response)
      if (this.onResultHandle) {
        this.result= this.onResultHandle(response)
        process.env.NODE_ENV === "development" && console.warn('结果面板数据', this.result)
        this.resultVisible = true
        this.modal.visible = false
        return
      }
      process.env.NODE_ENV === "development" && console.warn('请传参onResultHandle， 根据返回值去定制化错误面板的数据,返回值：', response)
    },
    /**
     * 关闭结果面板
     *
     * @param {-}
     */
    closeResultPanel () {
      this.resultVisible = false
      this.onCloseHandle()
    }
  }
}
</script>
