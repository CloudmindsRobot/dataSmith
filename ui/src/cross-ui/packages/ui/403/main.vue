<template>
  <a-result status="403"
            :title="stats"
            :sub-title="$t(stats === '403' ? 'invalid' : error)">
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
    "invalid": "抱歉，您的账号无权访问本系统。",
    "logged": "您的账户已经登录了。"
  },
  "zh-HK": {
    "invalid": "抱歉，您的賬號無權訪問本系統。",
    "logged": "您的賬戶已經登錄了。"
  },
  "en-US": {
    "invalid": "Sorry, your account is not authorized to access this system.",
    "logged": "Your account is already logged in."
  },
  "ja-JP": {
    "invalid": "申し訳ありませんが、アカウントにはこのシステムにアクセスする権限がありません。",
    "logged": "アカウントはすでにログインしています。"
  },
  "th-TH": {
    "invalid": "ขออภัย บัญชีของคุณไม่ได้รับอนุญาตให้เข้าถึงระบบนี้",
    "logged": "บัญชีของคุณเข้าสู่ระบบแล้ว"
  }
}
</i18n>

<script>
import logout from '../../mixin/logout'
import Seting from '../../tools/seting'
import Config from '../../tools/config'
import format from '../../mixin/format'

/**
 * 无权访问
 *
 * @displayName Page 403
 * @version 1.0
 * @example [none]
 */
export default {
  name: 'cross-403',
  mixins: [logout, format],
  data() {
    return {
      stats: this.$route.query ? (this.$route.query.code || '403') : '403',
      error: this.$route.query.code ? `error.${this.$route.query.code}` : 'logged',
      user: Seting.get(Config.user)
    }
  }
}
</script>