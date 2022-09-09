<template>
  <div class="pd10">
    <!-- @slot 自定义面包屑(breadcrumb) -->
    <slot name="breadcrumb">
      <div class="mb10 mt10">
        <a-breadcrumb>
          <a-breadcrumb-item v-for="item in breadcrumb"
                             :key="item">
            <span class="font16">
              {{ $t(`menu.${item}`) }}
            </span>
          </a-breadcrumb-item>
        </a-breadcrumb>
      </div>
    </slot>
    <!-- @slot 自定义路由(router) -->
    <slot>
      <router-view v-if="serve"/>
    </slot>
  </div>
</template>

<i18n>
{
  "zh-CN": {
    "offline": "当前网络不可用，请检查网络设置。"
  }
}
</i18n>

<script>
import Seting from '../../tools/seting'
import Config from '../../tools/config'
import Network from '../../utils/network'

/**
 * Main主内容区域
 *
 * @displayName Main
 * @version 1.0
 */
export default {
  name: 'cross-main',
  computed: {
    /**
     * 面包屑
     *
     * @returns {*[]|*}
     */
    breadcrumb() {
      if (!this.$route) {
        return
      }
      if (this.$route.meta.breadcrumb) {
        return this.$route.meta.breadcrumb.split(',')
      }
      const res = []
      const localMenu = Seting.get(Config.menu.name)
      if (!localMenu) {
        return res
      }
      //fix
      let menus = localMenu||[]
       if(typeof menus === 'string'){
         try{
            menus = JSON.parse(menus)
         }catch(e){
           process.env.NODE_ENV === "development" && console.error(e)
         }
      }
      for (let i = 0; i < menus.length; i++) {
        if (menus[i][Config.menu.props.route] === this.$route.name) {
          res.push(menus[i][Config.menu.props.key])
          break
        } else if (menus[i][Config.menu.props.children] && menus[i][Config.menu.props.children].length) {
          for (let j = 0; j < menus[i].children.length; j++) {
            if (menus[i][Config.menu.props.children][j][Config.menu.props.route] === this.$route.name) {
              res.push(menus[i][Config.menu.props.key], menus[i][Config.menu.props.children][j][Config.menu.props.key])
              break
            }
          }
        }
      }
      return res
    }
  },
  data() {
    return {
      offline: true,
      serve: typeof process.env.VUE_APP_BASE_API === 'string'
    }
  },
  methods: {
    /**
     * 离线事件
     *
     * @param {-}
     */
    offlineHandler() {
      this.$message.error(this.$t('offline'), 0)
    },
    /**
     * 连线事件
     *
     * @param {-}
     */
    onlineHandler() {
      this.$message.destroy()
    }
  },
  mounted() {
    Network.init(this.offlineHandler, this.onlineHandler)
  },
  beforeDestroy() {
    Network.uninit(this.offlineHandler, this.onlineHandler)
  }
}
</script>
