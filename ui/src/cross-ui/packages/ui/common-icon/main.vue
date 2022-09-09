<template>
  <span :style="'font-size:' + fontSize" class="cross-common-icon">
    <template v-if="type === 'battery'">
      <span class="iconfont icon-battery text-color" :class="!robot.chargeFlag ? `battery${Math.round(+robot.battery / 10)}` : ''">
        <svg class="svg-icon charging-icon" v-if="!!robot.chargeFlag" t="1656659464095" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3502" width="200" height="200"><path d="M128 490.656v174.56l224-106.688V704l416-232.736-304 48.48V384L128 490.656z" fill="#52C41A" p-id="3503"></path></svg>
      </span>
      <span v-if="showText" class="icon-text"> {{isDefined(robot.battery) ? robot.battery : '-'}}%</span>
    </template>
    <template v-else-if="type === 'network'">
      <svg class="svg-icon" aria-hidden="true">
        <use :xlink:href="(robot.networkFlag && (robot.networkFlag).toUpperCase() === 'WIFI' ? '#icon-wifi-' : '#icon-signal-') + (robot.robotType === 'ginger' ? Math.round(+robot.networkSignal / 20) : +robot.networkSignal)"></use>
      </svg>
      <span v-if="showText" class="icon-text">
        {{isDefined(robot.networkSignal) ?
          (robot.robotType === 'ginger' ?
            (robot.networkSignal > 60 ? $t('networkSignal')[0] : robot.networkSignal > 20 ? $t('networkSignal')[1] : $t('networkSignal')[2])
            : (robot.networkSignal > 3 ? $t('networkSignal')[0] : robot.networkSignal > 1 ? $t('networkSignal')[1] : $t('networkSignal')[2])
          ) : '-'
        }}
      </span>
    </template>
    <template v-else-if="type === 'online'">
      <svg t="1656664490276" v-if="!robot.onlineFlag" class="svg-icon" style="fill: #BFBFBF;" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5662" width="200" height="200"><path d="M832.6 191.4c-84.6-84.6-221.5-84.6-306 0l-96.9 96.9 51 51 96.9-96.9c53.8-53.8 144.6-59.5 204 0 59.5 59.5 53.8 150.2 0 204l-96.9 96.9 51.1 51.1 96.9-96.9c84.4-84.6 84.4-221.5-0.1-306.1zM446.5 781.6c-53.8 53.8-144.6 59.5-204 0-59.5-59.5-53.8-150.2 0-204l96.9-96.9-51.1-51.1-96.9 96.9c-84.6 84.6-84.6 221.5 0 306s221.5 84.6 306 0l96.9-96.9-51-51-96.8 97zM260.3 209.4c-3.1-3.1-8.2-3.1-11.3 0L209.4 249c-3.1 3.1-3.1 8.2 0 11.3l554.4 554.4c3.1 3.1 8.2 3.1 11.3 0l39.6-39.6c3.1-3.1 3.1-8.2 0-11.3L260.3 209.4z" p-id="5663"></path></svg>
      <svg t="1656664455712" v-else class="svg-icon" viewBox="0 0 1024 1024" style="fill: #13C2C2;" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5514" width="200" height="200"><path d="M574 665.4c-3.1-3.1-8.2-3.1-11.3 0L446.5 781.6c-53.8 53.8-144.6 59.5-204 0-59.5-59.5-53.8-150.2 0-204l116.2-116.2c3.1-3.1 3.1-8.2 0-11.3l-39.8-39.8c-3.1-3.1-8.2-3.1-11.3 0L191.4 526.5c-84.6 84.6-84.6 221.5 0 306s221.5 84.6 306 0l116.2-116.2c3.1-3.1 3.1-8.2 0-11.3L574 665.4z m258.6-474c-84.6-84.6-221.5-84.6-306 0L410.3 307.6c-3.1 3.1-3.1 8.2 0 11.3l39.7 39.7c3.1 3.1 8.2 3.1 11.3 0l116.2-116.2c53.8-53.8 144.6-59.5 204 0 59.5 59.5 53.8 150.2 0 204L665.3 562.6c-3.1 3.1-3.1 8.2 0 11.3l39.8 39.8c3.1 3.1 8.2 3.1 11.3 0l116.2-116.2c84.5-84.6 84.5-221.5 0-306.1zM610.1 372.3c-3.1-3.1-8.2-3.1-11.3 0L372.3 598.7c-3.1 3.1-3.1 8.2 0 11.3l39.6 39.6c3.1 3.1 8.2 3.1 11.3 0l226.4-226.4c3.1-3.1 3.1-8.2 0-11.3l-39.5-39.6z" p-id="5515"></path></svg>
      <span v-if="showText" class="icon-text">
        {{isDefined(robot.onlineFlag) ? (!robot.onlineFlag ? $t('offline') : $t('online')) : '-'}}
      </span>
    </template>
    <template v-else-if="type === 'robotStatus'">
      <a-tag :color="robotStatusColors[+robot.multipartStatus || getRobotStauts(robot)]" class="status-tag">
        <span class="dot"></span>{{$t('robotStatus')[+robot.multipartStatus || getRobotStauts(robot)]}}
      </a-tag>
    </template>
    <template v-else>
      <a-alert :message="$t('unSupprtTips', {type: type})" banner />
    </template>
  </span>
</template>

<i18n>
{
  "zh-CN": {
    "robotStatus":  ["", "工作中", "空闲中", "充电中", "离线"],
    "online": "在线",
    "offline": "离线",
    "networkSignal": ["强", "良好", "弱"],
    "unSupprtTips": "当前类型[{type}]暂不支持"
  }
}
</i18n>

<script>
import types from '../../mixin/types'

/**
 * 常见通用图标组
 *
 * @displayName Common Icon
 * @version 1.0
 */
export default {
  name: 'cross-icon',
  mixins: [types],
  props: {
    /**
     * 按钮组类型
     *
     * @values battery,network,online,robotStatus
     */
    type: {
      type: String,
      required: true
    },
    /**
     * 显示文本
     *
     * @values true,false
     */
    showText: {
      type: Boolean,
      default: true
    },
    /**
     * 字体大小
     */
    fontSize: {
      type: String,
      default: '16px'
    },
    /**
     * 机器人
     */
    robot: {
      type: Object,
      required: true,
      default: () => {
        return {}
      }
    }
  },
  data () {
    return {
      // 分别代表 ["", "工作中", "空闲中", "充电中", "离线"]
      robotStatusColors: ['', 'cyan', 'blue', 'green', '']
    }
  },
  methods: {
    /**
     * 组合多属性计算机器人呈现的状态
     *
     * @param {object} robot 机器人
     * @returns {number}
     */
    getRobotStauts (robot) {
      // 优先以
      // if (!robot.onlineFlag) {
      //   return 4
      // } else
      // ["", "工作中", "空闲中", "充电中", "离线"]
      if (robot.workFlag) {
        return 1
      } else if (robot.chargingFlag) {
        return 3
      } else {
        return 2
      }
    }
  }
}
</script>
<style lang="scss">
.cross-common-icon{
  display: flex;
  align-items: center;
  .iconfont{
    font-size: 1em;
    line-height: 1em;
  }
  .icon-text{
    font-size: 0.75em;
    margin-left: 4px;
  }
  .icon-battery{
    position: relative;
    color: rgba(0, 0, 0, .8);
    &::after{
      display: block;
      position: absolute;
      content: '';
      left: calc(0.125em + 4px);
      top: calc(0.375em + 4px);
      height: 0.3125em;
      background-color: #434343;
    }
    @for $i from 0 through 10 {
      &.battery#{$i} {
        &::after{
          width: 0.0624 * $i * 1em;
          @if $i < 4 { background-color: #FAAD14; }
          @if $i < 2 { background-color: #FF4D4F; }
        }
      }
    }
  }
  .svg-icon{
    width: 1em;
    height: 1em;
    margin: 4px;
  }
  .status-tag{
    border: none;
    .dot{
      border: 2px solid;
      border-radius: 50%;
      font-size: 0;
      display: inline-block;
      margin: 0 3px 3px 0;
    }
  }
  .charging-icon{
    position: absolute;
    left: 0;
    top: 0;
  }
}
</style>
