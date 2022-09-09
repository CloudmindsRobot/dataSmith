<template>
  <a-result status="404"
            title="404"
            :sub-title="$t('title')">
    <a-icon slot="icon"
            type="question-circle"/>
    <a-button v-if="showBtn"
              slot="extra"
              ghost
              icon="home"
              type="primary"
              @click="path('master')">
      {{ $t('home') }}
    </a-button>
  </a-result>
</template>

<i18n>
{
  "zh-CN": {
    "title": "抱歉，您访问的页面不存在。",
    "home": "返回主页"
  },
  "zh-HK": {
    "title": "抱歉，您訪問的頁面不存在。",
    "home": "返回主頁"
  },
  "en-US": {
    "title": "Sorry, the page you visited does not exist.",
    "home": "Back Home"
  },
  "ja-JP": {
    "title": "申し訳ありませんが、アクセスしたページは存在しません。",
    "home": "ホームページに戻る"
  },
  "th-TH": {
    "title": "ขออภัย หน้าที่คุณเยี่ยมชมไม่มีอยู่",
    "home": "กลับหน้าแรก"
  }
}
</i18n>

<script>
import logout from '../../mixin/logout'

/**
 * 访问的页面不存在
 *
 * @displayName Page 404
 * @version 1.0
 * @example [none]
 */
export default {
  name: 'cross-404',
  mixins: [logout],
  data() {
    return {}
  }
}
</script>