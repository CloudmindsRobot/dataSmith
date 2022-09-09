<template>
  <div class="cross-search">
    <template v-if="mobileShow">
      <form-create class="search-form"
                   v-model="fApi"
                   :rule="rule"
                   :option="option"/>
      <div class="search-btn text-right">
        <a-space size="middle">
          <a-button icon="filter"
                    @click="visible = true"/>
        </a-space>
      </div>
      <a-drawer class="cross-drawer"
                placement="right"
                :visible="visible"
                height="auto"
                width="90%"
                @close="onClose">
        <template v-if="title"
                  slot="title">
          {{ $t(title) }}
        </template>
        <br v-else>
        <form-create v-model="dApi"
                     :rule="rule"
                     :option="option"/>
      </a-drawer>
    </template>
    <form-create v-else
                 v-model="fApi"
                 :rule="rule"
                 :option="option"/>
  </div>
</template>

<i18n>
{
  "zh-CN": {
    "search": "搜索",
    "filter": "筛选"
  }
}
</i18n>

<script>
import formCreator from '../../mixin/formCreator'
import Form from '../../tools/form'
import Device from '../../tools/device'
import Screen from '../../utils/screen'
import Clean from '../../utils/clean'

/**
 * 搜索栏
 *
 * @displayName Search
 * @version 1.0
 */
export default {
  name: 'cross-search',
  mixins: [formCreator],
  props: {
    /**
     * 移动端适配
     *
     * @values true,false
     */
    mobileShow: {
      type: Boolean,
      default: true
    },
    /**
     * 抽屉标题
     */
    title: {
      type: String,
      default: 'filter'
    }
  },
  data() {
    const mOption = Form.options.default()
    mOption.form.layout = this.option.form.layout = 'inline'
    mOption.resetBtn.show = mOption.submitBtn.show = this.option.resetBtn.show = this.option.submitBtn.show = false
    Clean.clearUndefined(mOption)

    this.rule.forEach(item => {
      if (['select','a-tree-select'].includes(item.type) && !item.stopAutoSearch) {
        if (!item.on.change) {
          item.on.change = () => {
            this.defaultSearch()
          }
        }
      } else if (item.type === 'input' && !item.stopAutoSearch) {
        item.props.type = 'search'
        item.on.pressEnter = () => {
          this.defaultSearch()
        }
        item.on.search = () => {
          this.defaultSearch()
        }
      }
    })
    return {
      visible: false,
      mOption,
      dApi: null
    }
  },
  methods: {
    /**
     * 关闭搜索面板
     *
     * @param {-}
     */
    onClose() {
      this.visible = false
    },
    /**
     * 查询触发的回调
     *
     * @param {-}
     */
    defaultSearch () {
      /**
       * 搜索触发事件
       *
       * @property {object} formData 表单数据键值对
       */
      this.$emit('search', Device.isMobile() ? this.dApi.formData() : this.fApi.formData())
      this.visible = false
    }
  },
  watch: {
    visible(newVal) {
      this.oldRule.forEach(r => r.title = newVal && r.field !== 'search' ? (r.title || (r.props && r.props.placeholder ? r.props.placeholder : r.field)) : '')
      this.updateRules()
      if (newVal) {
        this.dApi && this.dApi.setValue(this.fApi.formData())
      } else {
        this.fApi && this.fApi.setValue(this.dApi.formData())
      }
    }
  },
  mounted() {
    this.updateLayout()
    this.updateRules()
    Screen.listen(() => {
      !Device.isMobile() && this.onClose()
      this.updateOptions()
    }, true, 'cross-search')
    // 如果还有传参this.buttons， 提示使用方把该配置项挪到table上配置
    if( this.$props.buttons && process.env.NODE_ENV === "development") {
      console.warn('当前5.0.0版本已不支持search配置buttons，请将4.0.0版本的buttons配置放到table上。')
    }
  },
  beforeDestroy() {
    Screen.removeListen('cross-search')
  }
}
</script>

<style lang="scss">
.cross-search {
  .ant-select {
    min-width: 150px;
  }

  .search-btn {
    display: none;
  }
}

@media screen and (max-width: 768px) {
  .cross-search {
    .search-form {
      display: none;
    }

    .search-btn {
      display: block;
    }
  }
  .cross-drawer {
    .ant-btn-block {
      width: 100% !important;
    }
  }
}
</style>
