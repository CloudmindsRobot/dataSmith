<template>
  <cross-modal :modal="modal"
               @cancel="onCancel">
    <a-result :status="(failureNum || resultData.msg) ? 'info' : 'success'"
              :title="(failureNum || resultData.msg) ? resultData.msg ? resultData.msg : $t('failureTips', {successNum: resultData.successNum, failureNum: failureNum} ) : $t('successTips', {successNum: resultData.successNum} )">
      <template #extra v-if="failureNum">
        <p class="text-left"
           style="margin-right: 0;">
          {{$t('reason')}}
          <a-button class="fr"
                    size="small"
                    @click="copyTable">
            {{$t('copy')}}
          </a-button>
        </p>
        <cross-table :pagination="{ pageSize: 10 }"
                     :data="dataSource"
                     :view="resultColumns"
                     :config="{ loading: false, expanded: false, busy: true, isList: true, isScroll: false, xScroll: true}">
        </cross-table>
      </template>
    </a-result>
    <a-button slot="footer"
              key="submit"
              type="primary"
              @click="onCancel">
      {{$t('ok')}}
    </a-button>
  </cross-modal>
</template>

<i18n src="../../locale/share/common.json"/>
<i18n>
{
  "zh-CN": {
    "import": "批量导入",
    "reason": "失败原因",
    "copy": "复制",
    "copySuccess": "复制成功",
    "copyFail": "复制失败",
    "copyUnSupport": "当前浏览器不支持点击复制事件",
    "successTips": "{successNum}行上传成功！",
    "failureTips": "{successNum}行上传成功，{failureNum}行上传失败。"
  }
}
</i18n>

<script>
import modal from '../../mixin/modal'
/**
 * 错误面板
 *
 * @displayName Result Panel
 * @version 1.0
 */
export default {
  name: 'cross-result-panel',
  mixins: [modal],
  props: {
    /**
     * 面板显示错误时的表格内容
     */
    resultColumns: { // 导入失败的错误面板--表格格式
      type: Array,
      default: () => {
        return [
          {
            locale: () => { return '设备名' },
            prop: 'name'
          },
          {
            locale: () => { return '失败原因' },
            prop: 'reason',
          }
        ]
      }
    },
    /**
     * 要显示的数据结果，包含正确和错误的数量
    */
    resultData: {
      type: Object,
      default: () => {
        return {
          successNum: 0,
          failureList: [],
          msg: '' // 当成功与失败数量都拿不到时的报错信息
        }
      }
    }
  },
  computed: {
    /**
     * 有错误结果的时候要显示的数据量
     *
     * @returns {[]|[{reason: string, name: string, key: number},{reason: string, name: string, key: number}]|[{reason: string, deviceName: string},{reason: string, deviceName: string}]|[{reason: string, deviceName: string},{reason: string, deviceName: string}]|*|*[]}
     */
    dataSource () {
      return this.resultData && this.resultData.failureList ? this.resultData.failureList : []
    },
    /**
     * 失败数量
     *
     * @returns {number}
     */
    failureNum () {
      return this.dataSource.length
    }
  },
  data() {
    return {}
  },
  methods: {
    /**
     * 拷贝表格
     *
     * @param {-}
     * @returns {Promise<boolean>}
     */
    async copyTable () {
      const clipboardTarget = navigator.clipboard
      if (clipboardTarget) {
        // 拼接数据
        const title = this.resultColumns.map(item => item.locale ? item.locale() : this.$t(item.prop)).join('\t')
        const titleList = this.resultColumns.map(item => item.prop)
        const tableData = this.dataSource.map(item => {
          return titleList.map(key => item.format ? item.format(item[key]) : item[key]).join('\t')
        }).join('\n')
        const selection = `${title}\n${tableData}`
        try {
          await clipboardTarget.writeText(selection)
          this.$message.success(this.$t('copySuccess'))
        } catch (err) {
          process.env.NODE_ENV === "development" && console.error('Failed to copy: ', err)
          this.$message.error(this.$t('copyFail'))
        }
      } else {
        process.env.NODE_ENV === "development" && console.error("拷贝失败，无clipboardData对象")
        this.$message.error(this.$t('copyUnSupport'))
        return false
      }
    }
  },
  created() {
    if(!this.modal.title) {
      this.modal.title = this.$t('import')
    }
  }
}
</script>
