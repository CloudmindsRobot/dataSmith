<template>
  <div class="cross-menu">
    <a-menu v-model="selectedKeys"
            :mode="mode || 'inline'"
            :open-keys.sync="openKeys"
            @select="onSelect"
            :class="styleFlag">
      <template v-for="m in menus">
        <a-sub-menu v-if="m[props.children] && m[props.children].length"
                    :key="m[props.key]">
          <template slot="title">
            <template v-if="m[props.icon]">
              <i v-if="m[props.icon].startsWith('fa')"
                 :class="['anticon fa', m[props.icon]]"/>
              <i v-else-if="m[props.icon].startsWith('iconfont')"
                 :class="m[props.icon]"/>
              <a-icon v-else
                      :type="m[props.icon]"/>
            </template>
            <span>
              {{ $t(`menu.${m[props.key]}`) }}
            </span>
          </template>
          <a-menu-item v-for="submenu in m[props.children]"
                       :key="submenu[props.key]">
            <template v-if="submenu[props.icon]">
              <i v-if="submenu[props.icon].startsWith('fa')"
                 :class="['anticon fa', submenu[props.icon]]"/>
              <i v-else-if="m[props.icon].startsWith('iconfont')"
                 :class="m[props.icon]"/>
              <a-icon v-else
                      :type="submenu[props.icon]"/>
            </template>
            <span>
              {{ $t(`menu.${submenu[props.key]}`) }}
            </span>
          </a-menu-item>
        </a-sub-menu>
        <a-menu-item v-else
                     :key="m[props.key]">
          <template v-if="m[props.icon]">
            <i v-if="m[props.icon].startsWith('fa')"
               :class="['anticon fa', m[props.icon]]"/>
            <i v-else-if="m[props.icon].startsWith('iconfont')"
                 :class="m[props.icon]"/>
            <a-icon v-else
                    :type="m[props.icon]"/>
          </template>
          <span>
            {{ $t(`menu.${m[props.key]}`) }}
          </span>
        </a-menu-item>
      </template>
    </a-menu>
  </div>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import Seting from '../../tools/seting'
import Config from '../../tools/config'
import { getValueInArray } from '../../utils/query'

/**
 * ???????????????vertical???inline???horizontal????????????
 *
 * @displayName Menu
 * @version 1.0
 */
export default {
  name: 'cross-menu',
  props: {
    /**
     * ??????
     *
     * @values h100,hCalc
     */
    styleFlag: {
      type: String,
      default: ''
    },
    /**
     * ????????????
     *
     *  @values vertical,inline,horizontal
     */
    mode: {
      type: String,
      default: 'inline'
    },
    /**
     * ????????????API
     */
    api: {
      type: String,
      default: `${Config.baseUrlPrefix[Config.platform]}/upms/v1/privilege/list`
    },
    /**
     * ??????(?????????????????????????????????)
     */
    menus: {
      type: Array,
      default: () => {
        return Seting.get(Config.menu.name) || []
      }
    }
  },
  data() {
    return {
      openKeys: Config.menu.openKeys,
      selectedKeys: Config.menu.selectedKeys,
      props: Config.menu.props
    }
  },
  methods: {
    /**
     * ????????????
     *
     * @param {object} menu ?????????
     */
    onSelect(menu) {
      /**
       * ????????????????????????
       *
       * @property {string} key ?????????
       */
      this.$emit('select', menu.key)
      const route = {}
      getValueInArray(this.menus, menu.key, this.props.key, this.props.children, route)
      this.$router && this.$router.push({name: route[this.props.route] || route['code']})
      this.$nextTick(() => {
        this.selectedKeys = Config.menu.selectedKeys
      })
    },
    /**
     * ????????????????????????
     *
     * @param {-}
     */
    activeMenu() {
      if (!this.$route || this.$route.name !== this.props.master || !this.menus.length) {
        return
      }
      this.onSelect({key: this.menus[0][this.props.children] && this.menus[0][this.props.children].length ? this.menus[0][this.props.children][0][this.props.key] : this.menus[0][this.props.key]})
    }
  },
  mounted() {
    if (this.menus.length) {
      !Seting.get(Config.menu.name) && Seting.set(this.menus, Config.menu.name)
      this.activeMenu()
    }
  }
}
</script>

<style lang="scss">
.cross-menu {
  .ant-menu-horizontal {
    line-height: 51px;
    border-bottom: none;
  }

  .h100 {
    height: 100vh;
    overflow-x: hidden;
    overflow-y: auto;
  }

  .hCalc {
    height: calc(100vh - 53px);
    overflow-x: hidden;
    overflow-y: auto;
  }
}

.anticon.fa::before {
  display: block;
}

@media screen and (max-width: 768px) {
  .cross-menu {
    .ant-menu-horizontal {
      line-height: 31px;
    }

    .anticon {
      display: none;
    }
  }
}
</style>
