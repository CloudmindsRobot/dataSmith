<template>
  <cross-modal :modal="modal">
    <a-spin :spinning="loading">
      <div class="text-center w100"
           style="position: relative">
        <img v-if="file.type === 0"
             :src="file.url"
             style="max-height: 60vh"/>
        <video v-else
               id="cross-video"
               class="w100"
               controls="controls"
               :src="file.url"/>
      </div>
    </a-spin>
  </cross-modal>
</template>

<script>
import video from '../../tools/video'
import $ from 'jquery'

/**
 * 文件预览
 *
 * @displayName File Preview
 * @version 1.0
 */
export default {
  name: 'cross-file-preview',
  props: {
    /**
     * 弹窗配置项
     *
     * {visible:可见,width:宽度}
     */
    modal: {
      type: Object,
      default: () => {
        return {
          visible: false,
          width: 600
        }
      }
    },
    /**
     * 文件
     *
     * {url:文件地址,type:文件类型}
     */
    file: {
      type: Object,
      default: () => {
        return {
          url: '',
          type: 0
        }
      }
    }
  },
  data() {
    return {
      loading: false
    }
  },
  watch: {
    'modal.visible'(newVal) {
      if (newVal) {
        this.loading = true
        video.canplay(this.file.url, (url) => {
          this.loading = false
          this.file.type = url ? 1 : 0
          const ele = $('#cross-video')[0]
          url && ele && ele.play()
        })
      } else {
        const ele = $('#cross-video')[0]
        if (ele) {
          ele.pause()
          ele.currentTime = 0
        }
      }
    }
  }
}
</script>