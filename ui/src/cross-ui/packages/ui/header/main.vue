<template>
  <a-row class="cross-header">
    <a-col :xxl="3" :xl="4" :lg="5" :md="3" :sm="4" :xs="5">
      <a-dropdown :trigger="['click']">
        <a v-if="user.logo || systemLogo"
           :style="{backgroundImage: `url(${user.logo||systemLogo})`}"
           class="logo"/>
        <a v-else
           :class="['logo', theme === 'light' ? 'logo-c' : 'logo-w']"/>
        <a-menu slot="overlay">
          <a-menu-item key="website">
            <a target="_blank"
               href="https://www.cloudminds.com/">
              {{ $t('about') }}
            </a>
          </a-menu-item>
          <a-menu-divider/>
          <a-menu-item>
            {{ $t('version') }}: {{ version }}
          </a-menu-item>
        </a-menu>
      </a-dropdown>
    </a-col>
    <a-col :xxl="14" :xl="12" :lg="10" :md="18" :sm="16" :xs="14">
      <!-- @slot 自定义菜单 -->
      <slot name="menu">
        <cross-menu mode="horizontal"/>
      </slot>
    </a-col>
    <a-col :xxl="7" :xl="8" :lg="9" :md="3" :sm="4" :xs="5"
           class="text-right">
      <!-- @slot 自定义工具栏 -->
      <slot name="toolbar">
        <cross-toolbar @theme="onTheme"
                       :user="user"
                       :api="api"/>
      </slot>
    </a-col>
  </a-row>
</template>

<i18n>
{
  "zh-CN": {
    "version": "版本",
    "about": "关于达闼"
  },
  "zh-HK": {
    "version": "版本",
    "about": "關於達闥"
  },
  "en-US": {
    "version": "Version",
    "about": "About"
  },
  "ja-JP": {
    "version": "バージョン",
    "about": "ダカールについて"
  },
  "th-TH": {
    "version": "เวอร์ชั่น",
    "about": "เกี่ยวกับดาการ์"
  }
}
</i18n>

<script>
import Seting from '../../tools/seting'
import Config from '../../tools/config'
import Theme from '../../tools/theme'
import userCenter from '../../mixin/userCenter'

/**
 * Header区域(包含Logo，菜单，工具栏，账户头像等)
 *
 * @displayName Header
 * @version 1.0
 */
export default {
  name: 'cross-header',
  mixins: [userCenter],
  data() {
    return {
      systemLogo: Seting.get(Config.systemLogoKey),
      theme: Theme.getCurrentTheme()
    }
  },
  methods: {
    /**
     * 切换主题触发此函数
     *
     * @param {string} theme 主题
     */
    onTheme(theme) {
      this.theme = theme
    }
  }
}
</script>

<style lang="scss">
.cross-header {
  .logo {
    display: block;
    background-repeat: no-repeat;
    background-size: contain;
    height: 32px;
    margin-top: 10px;
    max-width: 136px;
  }

  .logo-c {
    background-image: url('../../static/logo/logo.png');
  }

  .logo-w {
    background-image: url('../../static/logo/logo-w.png');
  }
}

@media screen and (max-width: 768px) {
  .cross-header {
    .logo {
      margin-top: 0;
    }
  }
}

@media screen and (max-width: 992px) {
  .cross-header {
    .logo-c {
      background-image: url('../../static/logo/logo-small.png');
      width: 32px;
    }

    .logo-w {
      background-image: url('../../static/logo/logo-small-w.png');
      width: 32px;
    }
  }
}
</style>
