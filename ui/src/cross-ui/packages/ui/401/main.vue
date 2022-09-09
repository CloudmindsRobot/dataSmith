<template>
  <a-result status="500"
            :title="stats"
            :sub-title="$t(stats === '401' ? 'invalid' :  error)">
    <a-icon slot="icon"
            :type="stats === '401' ? 'lock' : 'close-circle'"/>
    <a-button v-if="showBtn"
              slot="extra"
              ghost
              icon="login"
              type="primary"
              @click="logout">
      {{ $t('login') }}
    </a-button>
    <div class="desc text-center">
      {{ `${$t('account')}: ${user ? numberFormat(user.account) : emptyText}` }}
      &nbsp;&nbsp;
      {{ `${$t('platform')}: ${platform}` }}
    </div>
  </a-result>
</template>

<i18n src="../../locale/share/page.json"/>
<i18n>
{
  "zh-CN": {
    "invalid": "您的账户凭证已失效，请重新登录。"
  },
  "zh-HK": {
    "invalid": "您的賬戶憑證已失效，請重新登錄。"
  },
  "en-US": {
    "invalid": "Your account credentials have expired, please log in again."
  },
  "ja-JP": {
    "invalid": "アカウントのクレデンシャルの有効期限が切れています。もう一度ログインしてください。"
  },
  "th-TH": {
    "invalid": "ข้อมูลประจำตัวบัญชีของคุณหมดอายุ โปรดเข้าสู่ระบบอีกครั้ง"
  }
}
</i18n>

<script>
import logout from '../../mixin/logout'
import Config from '../../tools/config'
import Seting from '../../tools/seting'
import format from '../../mixin/format'

/**
 * 用户凭证已失效
 *
 * @displayName Page 401
 * @version 1.0
 * @example [none]
 */
export default {
  name: 'cross-401',
  mixins: [logout, format],
  data() {
    return {
      stats: this.$route.query ? (this.$route.query.code || '401') : '401',
      error: this.$route.query.code ? `error.${this.$route.query.code}` : Config.ERROR,
      user: Seting.get(Config.user)
    }
  }
}
</script>