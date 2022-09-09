<template>
  <div class="cross-layout"
       @click="closeDrawer">
    <a-back-top v-if="backTop"/>
    <a-config-provider :locale="zhCN">
      <a-layout v-if="mode === 'horizontal'">
        <a-affix v-if="affix">
          <a-layout-header>
            <!-- @slot Header区域 -->
            <slot name="header">
              <cross-header/>
            </slot>
          </a-layout-header>
        </a-affix>
        <a-layout-header v-else>
          <!-- @slot Header区域 -->
          <slot name="header">
            <cross-header/>
          </slot>
        </a-layout-header>
        <a-layout-content>
          <!-- @slot Main区域 -->
          <slot name="main">
            <cross-main/>
          </slot>
        </a-layout-content>
      </a-layout>
      <a-layout v-else-if="mode === 'vertical'">
        <a-layout-sider v-model="collapsed"
                        :style="{ position: 'fixed', left: 0 }"
                        :width="sider.width !== undefined ? sider.width : 200"
                        :collapsed-width="sider.collapsedWidth !== undefined ? sider.collapsedWidth : 80"
                        collapsible
                        breakpoint="lg"
                        @breakpoint="onBreakpoint">
          <!-- @slot 侧边栏菜单 -->
          <slot name="sider">
            <cross-menu style-flag="hCalc"/>
          </slot>
        </a-layout-sider>
        <a-layout>
          <a-affix v-if="affix">
            <a-layout-header>
              <!-- @slot Header区域 -->
              <slot name="header">
                <cross-header>
                  <div v-if="sider.collapsedWidth"
                       slot="menu"/>
                </cross-header>
              </slot>
            </a-layout-header>
          </a-affix>
          <a-layout-header v-else>
            <!-- @slot Header区域 -->
            <slot name="header">
              <cross-header>
                <div v-if="sider.collapsedWidth"
                     slot="menu"/>
              </cross-header>
            </slot>
          </a-layout-header>
          <a-layout-content>
            <!-- @slot Main区域 -->
            <slot name="main">
              <cross-main/>
            </slot>
          </a-layout-content>
        </a-layout>
      </a-layout>
      <a-layout v-else>
        <a-affix v-if="affix">
          <a-layout-header>
            <!-- @slot Header区域 -->
            <slot name="header">
              <cross-header>
                <div v-if="sider.collapsedWidth"
                     slot="menu"/>
              </cross-header>
            </slot>
          </a-layout-header>
        </a-affix>
        <a-layout-header v-else>
          <!-- @slot Header区域 -->
          <slot name="header">
            <cross-header>
              <div v-if="sider.collapsedWidth"
                   slot="menu"/>
            </cross-header>
          </slot>
        </a-layout-header>
        <a-layout class="cross-layout-inline">
          <a-layout-sider v-model="collapsed"
                          :width="sider.width !== undefined ? sider.width : 200"
                          :collapsed-width="sider.collapsedWidth !== undefined ? sider.collapsedWidth : 80"
                          collapsible
                          breakpoint="lg"
                          @breakpoint="onBreakpoint">
            <!-- @slot 侧边栏 -->
            <slot name="sider">
              <cross-menu style-flag="hCalc"/>
            </slot>
          </a-layout-sider>
          <a-layout-content class="vh">
            <!-- @slot Main区域 -->
            <slot name="main">
              <cross-main/>
            </slot>
          </a-layout-content>
        </a-layout>
      </a-layout>
    </a-config-provider>
  </div>
</template>

<i18n>
{
  "zh-CN": {
    "understood": "知道了"
  },
  "zh-HK": {
    "understood": "知道了"
  },
  "en-US": {
    "understood": "Understood"
  },
  "ja-JP": {
    "understood": "了解した"
  },
  "th-TH": {
    "understood": "เข้าใจ"
  }
}
</i18n>

<script>
import moment from 'moment'
import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'
import 'moment/locale/zh-cn'
import ws from '../../tools/ws'
import format from '../../mixin/format'
import { isFunction } from '../../utils/types'
import Events from '../../utils/events'

/**
 * Layout布局(包含Header区域，Main区域,菜单等)
 *
 * @displayName Layout
 * @version 1.0
 */
export default {
  name: 'cross-layout',
  mixins: [format],
  props: {
    /**
     * 布局模式
     *
     * @values vertical,inline,horizontal
     */
    mode: {
      type: String,
      default: 'horizontal'
    },
    /**
     * 侧边栏(在horizontal模式下无效)
     *
     * {width:宽度,collapsedWidth:则叠后宽度}
     */
    sider: {
      type: Object,
      default: () => {
        return {
          width: 200,
          collapsedWidth: 80
        }
      }
    },
    /**
     * 固定Header区域
     *
     * @values true,false
     */
    affix: {
      type: Boolean,
      default: true
    },
    /**
     * 启用返回顶部
     *
     * @values true,false
     */
    backTop: {
      type: Boolean,
      default: true
    },
    /**
     * 消息订阅
     *
     * {topic:订阅主题,route:订阅消息,event:自定义消息监听事件}
     */
    subscribes: {
      type: Array,
      default: () => {
        return []
      }
    },
    /**
     * 消息通知
     *
     * {title:标题(可提供方法),content:内容(可提供方法),placement:布局位置,duration:持续时长(秒),type:消息类型}
     */
    notification: {
      type: Object,
      default: () => {
        return {
          title: '',
          content: '',
          placement: 'bottomRight',
          duration: 4.5,
          type: 'info'
        }
      }
    }
  },
  data() {
    return {
      collapsed: false,
      zhCN
    }
  },
  methods: {
    /**
     * 触发侧边栏缩进
     *
     * @param {number} broken 缩进闸值
     */
    onBreakpoint(broken) {
      this.sider.collapsedWidth = broken ? 0 : 80
    },
    /**
     * 组件本地化函数
     *
     * @param {-}
     */
    locale() {
      moment.locale('zh-cn')
    },
    /**
     * 通知
     *
     * @param {object} mes 消息
     */
    message(mes) {
      const query = Array.isArray(mes.data) ? mes.data : [mes.data]
      /**
       * 通知事件
       *
       * @property {object} query 消息
       */
      this.$emit('message', query)
      if((mes.id + '').indexOf('talking') !== -1) {
        return
      }
      this.queryCall(query, mes.id || Date.now(), this.notification.duration || 4.5)
    },
    /**
     * 消息通知
     *
     * @param {array} query 队列
     * @param {string} id 消息ID
     * @param {number} duration 持续时长
     */
    queryCall(query, id, duration) {
      const [query0] = query
      //fix 队列清除的时候及时关闭通知弹框
      if(query0) {
        this.notificationKey = id
        this.$notification[this.notification.type || 'info']({
          message: h => {
            return isFunction(this.notification.title) ? this.notification.title(h, query0) : this.numberFormat(this.notification.title)
          },
          description: h => {
            return isFunction(this.notification.content) ? this.notification.content(h, query0) : this.numberFormat(this.notification.content)
          },
          placement: this.notification.placement || 'bottomRight',
          duration: duration * query.length,
          style: {whiteSpace: 'pre'},
          btn: h => {
            return h('a-button', {
              props: {
                size: 'small',
                ghost: true
              },
              on: {
                click: () => {
                  /**
                   * 确定事件
                   *
                   * @property {number|string} id 事件标识
                   */
                  this.$emit('confirm', id)
                  this.$notification.close(id)
                  this.notificationKey = null
                }
              }
            }, this.$t('understood'))
          },
          key: id
        })
      } else {
        this.notificationKey && this.$notification.close(this.notificationKey)
        this.notificationKey = null
      }
    },
    /**
     * 关闭抽屉
     *
     * @param {-}
     */
    closeDrawer() {
      Events.emit()
    }
  },
  created() {
    if (!this.subscribes.length) {
      return
    }
    this.subscribes.forEach(sub => {
      ws.on({topic: sub.topic, route: sub.route}, isFunction(sub.event) ? sub.event : this.message)
    })
    ws.listen()
  },
  mounted() {
    this.locale()
  },
  beforeDestroy() {
    Events.clear()
    this.subscribes.forEach(sub => {
      ws.off({topic: sub.topic, route: sub.route}, isFunction(sub.event) ? sub.event : this.message)
    })
    ws.disconnect()
  }
}
</script>

<style lang="scss">
.cross-layout {
  .ant-layout-header {
    padding: 0 10px;
    height: 52px;
    line-height: 52px;
    width: 100%;
    z-index: 100;
  }
  .ant-back-top {
    z-index: 2000;
  }

  .vh{
    overflow: auto;
    height: calc(100vh - 53px);
  }

  .ant-layout-sider-zero-width-trigger{
    display: none;
  }
}

@media screen and (max-width: 768px) {
  .cross-layout {
    .ant-layout-header {
      height: 32px;
      line-height: 32px;
    }

    .vh{
      overflow: hidden;
      height: auto;
    }
  }
}
</style>
