<template>
  <div class="cross-toolbar">
    <a-space class="tool"
             size="middle"
             align="center">
      <!-- @slot 添加自定义标签的工具项 -->
      <slot name="tool" />
      <template v-for="t in tools">
        <a-tooltip v-if="t.visible"
                   :key="t.key">
          <template v-if="tooltip"
                    slot="title">
            {{ $t(t.key) }}
          </template>
          <a-dropdown v-if="t.children"
                      :trigger="['click']">
            <span class="pointer font16">
              <i v-if="t.icon.startsWith('fa')"
                 :class="['fa', t.icon]" />
              <a-icon v-else :type="t.icon" />
            </span>
            <a-menu slot="overlay"
                    selectable
                    v-model="t.model"
                    @click="command">
              <a-menu-item v-for="l in t.children"
                           :key="l.code">
                {{ l.label }}
              </a-menu-item>
            </a-menu>
          </a-dropdown>
          <span class="pointer font16"
                v-else
                @click="command({ key: t.key })">
            <i v-if="t.icon.startsWith('fa')"
               :class="['fa', t.icon]" />
            <a-icon v-else
                    :type="t.icon" />
          </span>
        </a-tooltip>
      </template>
      <!-- @slot 自定义账户头像 -->
      <slot name="avatar">
        <cross-avatar :tools="tools"
                      :user="user"
                      :api="api" />
      </slot>
    </a-space>
    <a-space class="no-tool"
             size="middle"
             align="center">
      <!-- @slot 添加自定义标签的工具项 -->
      <slot name="tool" />
      <!-- @slot 自定义账户头像 -->
      <slot name="avatar">
        <cross-avatar :tools="tools"
                      :user="user"
                      :api="api" />
      </slot>
    </a-space>
  </div>

</template>

<i18n src="../../locale/share/tool.json"/>

<script>
import Vue from 'vue'
import Config from '../../tools/config'
import Theme from '../../tools/theme'
import userCenter from '../../mixin/userCenter'

/**
 * 工具栏包括(主题，多语言，设置，帮助等)
 *
 * @displayName Toolbar
 * @version 1.0
 */
export default {
  name: 'cross-toolbar',
  mixins: [userCenter],
  props: {
    /**
     * 帮助文档URL，可以在配置项中设置
     */
    helpUrl: {
      type: String,
      default: Config.helpUrl
    },
    /**
     * 开启提示文字
     *
     * @values true,false
     */
    tooltip: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const _this = this
    return {
      tools: [{
        key: 'language',
        icon: 'fa-language',
        tooltip: _this.tooltip,
        children: Config.languages,
        model: [_this.$i18n.locale],
        visible: Config.languages.length > 1,
        event() {
          _this.$nextTick(() => {
            _this.$root.$i18n.locale = this.model[0]
            Vue.localStorage.set(Config.language, this.model[0])
          })
        }
      }, {
        key: 'help',
        icon: 'question',
        tooltip: _this.tooltip,
        visible: !!_this.helpUrl,
        event() {
          window.open(_this.helpUrl)
        }
      }, {
        key: 'theme',
        icon: Theme.getCurrentTheme() === 'light' ? 'fa-moon-o' : 'fa-sun-o',
        tooltip: _this.tooltip,
        visible: true,
        event() {
          Theme.change()
          const theme = Theme.getCurrentTheme()
          /**
           * 当主题变更时触发
           *
           * @property {string} theme 主题
           */
          _this.$emit('theme', theme)
          this.icon = theme === 'light' ? 'fa-moon-o' : 'fa-sun-o'
        }
      }],
      config: {
        param: {
          deviceType: Config.alarmingDeviceType
        }
      }
    }
  },
  methods: {
    /**
     * 工具栏命令
     *
     * @param {object} item 工具项
     */
    command(item) {
      const tool = this.tools.find(t => t.key === item.key) || this.tools.find(t => t.children && t.children.some(c => c.code === item.key))
      tool && tool.event()
    }
  }
}
</script>

<style lang="scss">
.cross-toolbar {
  .tool {
    display: inline-flex;
  }

  .no-tool{
    display: none;
  }
}

@media screen and (max-width: 992px) {
  .cross-toolbar {
    .tool {
      display: none;
    }

    .no-tool{
      display: inline-flex;
    }
  }
}
</style>
