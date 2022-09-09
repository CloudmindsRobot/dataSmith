<template>
  <a-row :class="['cross-smart-login', {'dark': $localStorage.get($tools.config.theme) === 'dark'}]">
    <div class="logo"></div>
    <a-col class="page-bg"
           :xxl="15" :xl="14" :lg="13" :md="12" :sm="0" :xs="0">
      <a-row class="banner pd-0-20 text-light lh32">
        <a-col :xxl="12" :xl="12" :lg="0" :md="0" :sm="0" :xs="0">
          <div class="logo-small fl"></div>
          &nbsp;&nbsp;{{ $t('copyright') }}
        </a-col>
        <a-col :xxl="12" :xl="12" :lg="24" :md="24" :sm="24" :xs="24"
               class="text-right">
          {{ $t('support') }}:
          <a class="text-light"
             href="mailto:robotsupport@cloudminds.com">
            robotsupport@cloudminds.com
          </a>
        </a-col>
        <a-col :xxl="0" :xl="0" :lg="24" :md="24" :sm="24" :xs="24">
          <div class="logo-small fl"></div>
          &nbsp;&nbsp;{{ $t('copyright') }}
        </a-col>
      </a-row>
    </a-col>
    <a-col class="login-panel"
           :xxl="9" :xl="10" :lg="11" :md="12" :sm="24" :xs="24">
      <div class="login vertical-center pd20">
        <cross-login/>
      </div>
      <div class="banner pd-0-20 text-light lh32">
        {{ $t('support') }}:
        <a class="text-light"
           href="mailto:robotsupport@cloudminds.com">
          robotsupport@cloudminds.com
        </a>
        <br>
        <div class="logo-small fl"></div>
        &nbsp;&nbsp;{{ $t('copyright') }}
      </div>
    </a-col>
  </a-row>
</template>

<i18n>
{
  "zh-CN": {
    "copyright": "© 2021 达闼科技",
    "support": "售后支持"
  },
  "zh-HK": {
    "copyright": "© 2021 達闥科技",
    "support": "售後支持"
  },
  "en-US": {
    "copyright": "Copyright 2021 © CloudMinds Technology Inc.",
    "support": "Support"
  }
}
</i18n>

<script>
/**
 * 登录
 */
export default {
  name: 'Login'
}
</script>

<style lang="scss">
.cross-smart-login {
  height: 100vh;

  .logo, .logo-small {
    background-position: center;
    background-size: contain;
    background-repeat: no-repeat;
  }

  .logo {
    position: absolute;
    top: 20px;
    left: 20px;
    width: 136px;
    height: 32px;
    z-index: 1;
    background-image: url("../cross-ui/packages/static/logo/logo-w.png");
  }

  .logo-small {
    display: inline-block;
    margin-top: 8px;
    width: 16px;
    height: 16px;
    background-image: url("../cross-ui/packages/static/logo/logo-small-w.png");
  }

  .page-bg, .login-panel {
    height: 100%;
    background-repeat: no-repeat;
    background-size: cover;
    background-position: center;
  }

  .banner {
    position: absolute;
    width: 100%;
    bottom: 0;
    background-color: rgba(255, 255, 255, 0.1);

    a:hover {
      color: rgba(255, 255, 255, 0.8);
      text-decoration: underline;
    }
  }

  .page-bg {
    border-top-right-radius: 30px;
    border-bottom-right-radius: 30px;
    background-image: url('../assets/img/login.png');
    box-shadow: 10px 0 20px 0 #b7d6ff;
  }

  .login-panel {
    background-image: none;

    .banner {
      display: none;
    }
  }

  .login {
    margin: 0 auto;
    width: 65%;
  }

  .ant-btn-primary {
    background-image: linear-gradient(90deg, #5cbdff 3%, #2f88ff 73%);
    box-shadow: 0 3px 15px 0 #b7d6ff;
    border: none;
    background-color: unset;
  }
}

.dark {
  background: rgba(21, 22, 26, 1);

  .banner {
    border-bottom-right-radius: 28px;
    background-color: rgba(0, 0, 0, 0.8);
  }

  .page-bg {
    background-image: url('../assets/img/login-dark.png');
    box-shadow: 10px 0 20px 0 rgba(12, 16, 30, 1);
  }

  .ant-btn-primary {
    box-shadow: 0 3px 15px 0 #0058cd;
  }
}

@media screen and (max-width: 1200px) {
  .cross-smart-login {
    .page-bg {
      .banner {
        .text-right {
          text-align: left;
        }
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .cross-smart-login {
    .page-bg {
      border-top-right-radius: 0;
      border-bottom-right-radius: 0;
      background-image: none;
      box-shadow: none;

      .banner {
        display: none;
      }
    }

    .login-panel {
      background-image: url('../assets/img/login.png');

      .banner {
        display: block;
      }
    }

    .login {
      width: 75%;
      background-color: rgba(255, 255, 255, 1);
      border-radius: 20px;
      box-shadow: 0 0 10px 0 #b7d6ff;
    }
  }

  .dark {
    .login-panel {
      background-image: url('../assets/img/login-dark.png');

      .banner {
        border-bottom-right-radius: 0;
      }
    }

    .login {
      background-color: rgba(0, 0, 0, 1);
      box-shadow: 0 0 10px 0 rgba(12, 16, 30, 1);
    }
  }
}
</style>
