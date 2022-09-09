<template>
  <div :style="viewStyle" class="cross-ad-preview">
    <template v-for="(item, index) in data">
      <div :key="item.id" v-if="index < 4" :class="dataNum > 1 ? 'ad-item ad-col-cell' : 'ad-item ad-col-full'">
        <a class="cover text-center"
           :data-src="mockUrl + item.url"
           :data-fancybox="fancyboxGroup"
           :data-type="item.type !== 0 ? 'video': 'image'">
          <div class="w100 h100p">
            <template v-if="index < (dataNum > 4 ? 3 : 4)">
              <div class="ad-img h100p" v-if="item.type === 0"
                   :style="`background-image: url(${mockUrl + item.thumbImage});`"></div>
              <!-- 音频或者视频，设置深色的背景和上浮的图标 -->
              <i v-else class="iconfont center-icon icon-bg"
                 :style="'font-size:'+ fontSize">{{ item.type === 1 ? '视频' : '音频' }}</i>
            </template>
            <!-- 多出四个时，展现剩余数量 -->
            <i v-else class="iconfont center-icon icon-bg" :style="'font-size:'+ fontSize">+ {{ dataNum - 3 }}</i>
          </div>
        </a>
      </div>
      <a v-else
         :key="item.id"
         style="visibility: hidden;"
         :data-src="mockUrl + item.url"
         :data-fancybox="fancyboxGroup"
         :data-type="item.type !== 0 ? 'video': 'image'">
      </a>
    </template>
  </div>
</template>

<script>
/**
 * 广告数据缩略图及预览功能
 * 支持1-多于四个数量的缩略显示
 *
 * @displayName Ad Preview
 * @version 1.0
 */
export default {
  name: 'cross-ad-preview',
  props: {
    /**
     * 设置的固定尺寸
     *
     * {width:宽度,height:高度}
     */
    viewStyle: {
      type: Object,
      default: () => {
        return {
          width: '100%',
          height: '100%'
        }
      }
    },
    /**
     * 内容
     */
    data: {
      type: Array,
      default: () => {
        return []
      }
    },
    /**
     * 图标与字体大小
     */
    fontSize: {
      type: String,
      default: '14px'
    },
    /**
     * 配置项
     *
     * {tooltipText:提示文本,listIcon:通知图标,iconStyle:图标样式,listRules:通知内容显示规则}
     */
    config: {
      type: Object,
      default: () => {
        return {
          tooltipText: '',
          listIcon: '',
          iconStyle: '',
          listRules: {
            title: 'title',
            desc: 'desc',
            time: (data) => {
              return data.time
            }
          },
          remote: true,
          dot: false
        }
      }
    }
  },
  computed: {
    /**
     * 数据集数量
     *
     * @returns {number} 数量
     */
    dataNum() {
      return this.data.length || 0
    }
  },
  data() {
    return {
      fancyboxGroup: `gallery-${parseInt(Math.random() * 10000)}`,
      mockUrl: process.env.NODE_ENV === 'production' ? '' : 'https://172.16.23.85:31495'
    }
  }
}
</script>

<style lang="scss">
.cross-ad-preview {
  background-color: #E6E6E6;
  display: flex;
  text-align: center;
  flex-wrap: wrap;
  align-content: center;
  align-items: center;
  flex-direction: column;

  .ad-img {
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
  }

  .ad-item {
    overflow: hidden;
    margin: 0 1px;
    box-sizing: border-box;

    .icon-bg {
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .center-icon {
      color: #fff;
      cursor: pointer;
    }
  }

  .ad-col-full {
    flex: 1 1 100%;
    height: 100%;
    width: 100%;
    font-size: 26px;
  }

  .ad-col-cell {
    height: 50%;
    width: 40%;
    font-size: 14px;

    &:nth-child(2n) {
      border-top: 1px solid transparent;
    }

    &:nth-child(2n + 1) {
      border-bottom: 1px solid transparent;
    }
  }
}
</style>