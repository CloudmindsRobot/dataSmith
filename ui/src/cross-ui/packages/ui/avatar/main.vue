<template>
  <div class="cross-avatar">
    <a-dropdown :trigger="['click']">
      <div class="pointer">
        <a-avatar :size="24">
          <template slot="icon">
            <img v-if="user.photo"
                 :src="user.photo"/>
            <a-icon v-else
                    type="user"/>
          </template>
        </a-avatar>
        <span class="cross-user">
          &nbsp;{{ numberFormat(user.account) }}
        </span>
      </div>
      <a-menu class="cross-avatar-menu"
              slot="overlay"
              selectable
              v-model="tool.model"
              @select="onSelect"
              @click="command">
        <a-menu-item @click="openUserCenter"
                     class="text-center lh32">
          <a-avatar :size="24">
            <template slot="icon">
              <img v-if="user.photo"
                   :src="user.photo"/>
              <a-icon v-else
                      type="user"
                      style="margin: 0;"/>
            </template>
          </a-avatar>
          <br>
          {{ numberFormat(user.account) }}
          <br>
          <span class="gray font12">
            {{ numberFormat(user.name) }}
          </span>
          <br/>
          <span class="gray font12">
            {{ $t('version') }}: {{ version }}
          </span>
        </a-menu-item>
        <a-menu-divider class="more"
                        v-if="tools.length"/>
        <template v-for="t in tools">
          <template v-if="t.visible">
            <a-sub-menu v-if="t.children"
                        :key="t.key"
                        class="more">
              <template slot="title">
                <i v-if="t.icon.startsWith('fa')"
                   :class="['mr fa', t.icon]"/>
                <a-icon v-else
                        :type="t.icon"/>
                {{ $t(t.key) }}
              </template>
              <a-menu-item v-for="l in t.children"
                           :key="l.code">
                {{ l.label }}
              </a-menu-item>
            </a-sub-menu>
            <a-menu-item v-else
                         :key="t.key"
                         class="more">
              <i v-if="t.icon.startsWith('fa')"
                 :class="['mr fa', t.icon]"/>
              <a-icon v-else
                      :type="t.icon"/>
              {{ $t(t.key) }}
            </a-menu-item>
          </template>
        </template>
        <a-menu-divider/>
        <template v-for="s in setings">
          <a-menu-item v-if="s.visible"
                       :key="s.key">
            <i v-if="s.icon.startsWith('fa')"
               :class="['mr fa', s.icon]"/>
            <a-icon v-else
                    :type="s.icon"/>
            {{ $t(s.key) }}
          </a-menu-item>
        </template>
      </a-menu>
    </a-dropdown>
    <!-- @slot 自定义用户信息 -->
    <slot>
      <cross-user-center :user="user"
                         :api="api"
                         :modal="modal"/>
    </slot>
  </div>
</template>

<i18n src="../../locale/share/tool.json"/>
<i18n>
{
  "zh-CN": {
    "version": "版本"
  }
}
</i18n>

<script>
import Config from '../../tools/config'
// import Seting from '../../tools/seting'
import empty from '../../mixin/empty'
import format from '../../mixin/format'
import userCenter from '../../mixin/userCenter'
import {isFunction} from '../../utils/types'
import logout from '../../mixin/logout'

/**
 * 账户头像(包含账户信息，注销，修改密码及其他工具)
 *
 * @displayName Avatar
 * @version 1.0
 */
export default {
  name: 'cross-avatar',
  mixins: [empty, format, userCenter, logout],
  props: {
    /**
     * 菜单
     *
     * [{key:键名,icon:图标,visible:可见,children[{code:值,label:名}],event:选择后执行的事件}]
     */
    tools: {
      type: Array,
      default: () => {
        return []
      }
    },
    /**
     * 显示个人中心面板回调函数
     */
    userCenterFunc: Function
  },
  data() {
    return {
      setings: [{
        key: 'logout',
        icon: 'logout',
        visible: true,
        event: this.logout
      }],
      tool: this.tools.find(t => !!t.model) || {model: []},
      modal: {
        visible: false
      }
    }
  },
  methods: {
    /**
     * 菜单项命令
     *
     * @param {object} item 菜单项
     */
    command(item) {
      const tool = this.tools.find(t => t.key === item.key) || this.tools.find(t => t.children && t.children.some(c => c.code === item.key)) || this.setings.find(s => s.key === item.key)
      tool && tool.event()
    },
    /**
     * 修改语言与区域信息
     *
     * @param {object} item 菜单项
     */
    onSelect(item) {
      if (Config.languages.find(l => l.code === item.key)) {
        return
      }
      this.$nextTick(() => {
        this.tool.model = [this.$i18n.locale]
      })
    },
    /**
     * 显示个人中心面板
     *
     * @param {-}
     */
    openUserCenter() {
      isFunction(this.userCenterFunc) ? this.userCenterFunc() : this.modal.visible = true
    }
  }
}
</script>

<style lang="scss">
.cross-avatar-menu {
  .ant-dropdown-menu-submenu-selected .ant-dropdown-menu-submenu-title {
    color: inherit;
    padding: 5px 16px;
  }

  .more {
    display: none;
  }

  .mr {
    margin-right: 6px;
  }
}

.cross-avatar {
  .cross-user {
    display: inline;
  }
}

@media screen and (max-width: 992px) {
  .cross-avatar {
    .cross-user {
      display: none;
    }
  }
  .cross-avatar-menu {
    .more {
      display: block;
    }
  }
}
</style>
