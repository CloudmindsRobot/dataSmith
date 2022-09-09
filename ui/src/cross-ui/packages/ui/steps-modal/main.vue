<template>
  <cross-modal :modal="modal"
               @cancel="cancel">
    <template>
      <a-steps :current="currentIndex"
               v-if="!modal.noSteps"
               size="small">
        <a-step v-for="(item,index) in steps"
                :key="item.title + index"
                :title="isFunction(item.title) ? item.title() : $t(item.title)"/>
      </a-steps>
      <br/>
      <!-- @slot 自定义步骤切换内容(stepsContent)，通过事件抛出的当前步骤索引切换各步骤要对应显示的内容 -->
      <slot name="stepsContent"></slot>
    </template>
    <template slot="footer">
      <a-button v-if="currentIndex == 0"
                @click="cancel">
        {{ $t('cancel') }}
      </a-button>
      <a-button v-if="!modal.noPrev&&currentIndex > 0"
                @click="prev">
        {{ $t('previous') }}
      </a-button>
      <a-button v-if="currentIndex < steps.length - 1"
                type="primary"
                @click="next">
        {{ $t('next') }}
      </a-button>
      <a-button v-if="currentIndex == steps.length - 1"
                type="primary"
                @click="complete">
        {{ $t('done') }}
      </a-button>
    </template>
  </cross-modal>
</template>

<i18n>
{
  "zh-CN": {
    "cancel": "取消",
    "done": "完成",
    "next": "下一步",
    "previous": "返回上一步"
  }
}
</i18n>

<script>
import types from '../../mixin/types'
/**
 * 步骤弹窗
 *
 * @displayName Steps Modal
 * @version 1.0
*/
export default {
  name: 'cross-steps-modal',
  mixins: [types],
  props: {
		/**
		 * 当前步骤索引
		 */
		currentIndex: {
			type: Number,
			default: 0
		},
		/**
		 * 步骤对象配置项
		 *
		 * [{title:步骤标题}]
		 */
		steps: {
			type: Array,
			default: () => {
				return []
			}
		},
		/**
		 * 步骤弹窗配置项
		 *
		 * {noSteps:是否展示步骤条,noPrev:是否展示上一步,title:标题,visible:显示,width:宽度,style:样式,mask:是否展示遮罩,zIndex:层级,destroyOnClose:关闭时销毁 Modal 里的子元素}
		 */
		modal: {
			type: Object,
			default: () => {
				return {
					noSteps: false,
					noPrev: false,
					title: '',
					visible: false,
					width: '50%',
					style: {},
					mask: true,
					zIndex: 1000,
					destroyOnClose: false
				}
			}
		}
	},
  data() {
    return {}
  },
  methods: {
		/**
		 * 点击取消
		 *
		 * @param {-}
		 */
		cancel() {
			this.modal.visible = false
      /**
       * 取消触发事件
       *
       * @property {number} arg 当前步骤索引
       */
      this.$emit('cancel', 0)
		},
		/**
		 * 点击上一步
		 *
		 * @param {-}
		 */
		prev() {
			/**
			 * 上一步触发事件
			 *
			 * @property {number} currentIndex 当前步骤索引
			 */
			this.$emit('prev', this.currentIndex)
        },
		/**
		 * 点击下一步
		 *
		 * @param {-}
		 */
		next() {
			/**
			 * 下一步触发事件
			 *
			 * @property {number} currentIndex 当前步骤索引
			 */
			this.$emit('next', this.currentIndex)
    },
		/**
		 * 点击完成
		 *
		 * @param {-}
		 */
		complete() {
      /**
       * 完成触发事件
       *
       * @property {number} arg 当前步骤索引
       */
      this.$emit('complete', 0)
    }
	}
}
</script>
