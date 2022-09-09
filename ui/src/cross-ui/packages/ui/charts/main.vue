<template>
  <a-row :gutter="30">
    <a-col class="mb20"
           v-for="v in view"
           :key="v.key"
           :xxl="columns[0]" :xl="columns[1]" :lg="columns[2]" :md="24" :sm="24" :xs="24">
      <div class="mb15">
        <!-- @slot 自定义图表工具栏 -->
        <slot name="tool"/>
      </div>
      <div class="chart-container"
           :style="v.bodyStyle">
        <canvas :id="v.key"/>
      </div>
    </a-col>
  </a-row>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import Chart from 'chart.js/auto'
import Theme from '../../tools/theme'

/**
 * 图表
 *
 * @displayName Charts
 * @version 1.0
 */
export default {
  name: 'cross-charts',
  props: {
    /**
     * 图表列宽(依次为大中小屏幕上的列列宽)
     *
     * @values [8,12,24]
     */
    columns: {
      type: Array,
      default: () => {
        return [8, 12, 24]
      }
    },
    /**
     * 图表数据视图
     *
     * {key:图表键,type:图表类型,data:图表数据,options:图表配置项,bodyStyle:样式}
     */
    view: {
      type: Array,
      default: () => {
        return [{
          key: String,
          type: String,
          data: Object,
          options: Object,
          bodyStyle: Object
        }]
      }
    },
    /**
     * 更新图表
     *
     * {key:图表键}
     */
    updateChart: {
      type: Object,
      default: () => {
        return {
          key: String
        }
      }
    }
  },
  data() {
    return {
      charts: new Map()
    }
  },
  watch: {
    updateChart(newVal) {
      newVal.key ? this.update(newVal.key) : this.updateAll()
    },
    '$i18n.locale'(newVal, oldVal) {
      newVal !== oldVal && this.updateAll()
    }
  },
  methods: {
    /**
     * 初始化图表
     *
     * @param {-}
     */
    init() {
      this.view.forEach(v => {
        this.charts.set(v.key, new Chart(v.key, {
          type: v.type,
          data: v.data,
          options: {
            ...v.options,
            responsive: true,
            maintainAspectRatio: false,
            locale: this.$i18n.locale,
            interaction: {
              intersect: false,
            },
            scales: {
              x: {
                grid: {
                  borderWidth: 0,
                  lineWidth: 0,
                  drawBorder: false,
                  tickWidth: 1
                }
              },
              y: {
                grid: {
                  borderWidth: 0,
                  tickWidth: 0,
                  color: function () {
                    return Theme.getCurrentTheme() === 'dark' ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.1)'
                  }
                }
              }
            }
          }
        }))
      })
    },
    /**
     * 更新图表
     *
     * @param {string} key 图表键
     */
    update(key) {
      const chart = this.charts.get(key)
      const view = this.view.find(v => v.key === key)
      if (!chart || !view) {
        return
      }
      view.options.locale = this.$i18n.locale
      Object.assign(chart.options, view.options)
      Object.assign(chart.data, view.data)
      chart.update()
    },
    /**
     * 更新图表
     *
     * @param {-}
     */
    updateAll() {
      this.charts.forEach((c, k) => this.update(k))
    },
    /**
     * 修改图表主题
     *
     * @param {string} theme 主题
     */
    theme(theme) {
      if (!theme) {
        return
      }
      Chart.defaults.borderColor = theme === 'dark' ? 'rgba(255,255,255,0.25)' : 'rgba(0,0,0,0.25)'
      Chart.defaults.color = theme === 'dark' ? 'rgba(255,255,255,.85)' : 'rgba(0,0,0,.85)'
    }
  },
  created() {
    Theme.bindEvent(this.theme, this.view.key || 'cross-chart')
  },
  mounted() {
    Chart.defaults.font.size = 14
    Chart.defaults.plugins.title.font = {weight: 'bold', size: 18}
    Chart.defaults.plugins.title.padding = {top: 0, bottom: 20}
    Chart.defaults.layout.padding = 0
    Chart.defaults.elements.point.radius = 0
    Chart.defaults.elements.line.borderWidth = 1
    Chart.defaults.elements.line.tension = 0.1
    Chart.defaults.plugins.legend.labels.boxWidth = 10
    Chart.defaults.plugins.legend.labels.boxHeight = 10
    Chart.defaults.plugins.legend.labels.font = {size: 12}
    this.theme(Theme.getCurrentTheme())
    this.$nextTick(this.init)
  },
  beforeDestroy() {
    Theme.removeEvent(this.view.key || 'cross-chart')
  }
}
</script>

<style lang="scss">
.chart-container {
  position: relative;
  width: 100%;
  height: 300px;
}
</style>