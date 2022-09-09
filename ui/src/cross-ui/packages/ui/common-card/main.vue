<template>
  <div class="cross-card">
    <div :class="['scroll', {'h60': config.isScroll && data.length}]"
         v-infinite-scroll="handleInfiniteOnLoad"
         :infinite-scroll-disabled="config.busy"
         :infinite-scroll-distance="10">
      <template v-if="isMobile || config.isScroll">
        <a-row :gutter="15"
               class="cross-card-item">
          <a-col v-for="(d, index) in data"
                 :key="index"
                 class="mb15"
                 :xxl="columns[0]" :xl="columns[1]" :lg="columns[2]" :md="24" :sm="24" :xs="24">
            <a-card hoverable
                    size="small"
                    :bordered="bordered">
              <!-- @slot 自定义卡片meta -->
              <slot v-if="meta === 'cover'"
                    slot="cover"
                    name="cover"
                    :slot-scope="d">
                <template v-if="d[view[0].prop]">
                  <a v-if="isFunction(view[0].click)"
                     class="cover text-center"
                     @click="view[0].click(d[view[0].prop])"
                     :style="d.icon ? d.thumb : {backgroundImage: `url(${isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop]})`}"/>
                  <a v-else
                     class="cover text-center"
                     :data-src="isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop]"
                     :data-fancybox="fancyboxGroup"
                     :data-type="d.icon ? 'video': 'image'"
                     :style="d.icon ? d.thumb : {backgroundImage: `url(${d.thumb ? d.thumb : (isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop])})`}">
                    <div v-if="d.icon"
                         class="play">
                      <i v-if="d.icon.indexOf('fa')!==-1"
                         :class="d.icon"/>
                      <a-icon v-else
                              class="font42"
                              :type="d.icon"/>
                    </div>
                  </a>
                  <a-card-meta v-if="view.length > 1"
                               class="pd5">
                    <slot slot="description"
                          name="description"
                          :slot-scope="d">
                      <cross-descriptions :view="view.filter((a, i) => i > 0)"
                                          :data-model="{model: d}"
                                          :columns="[1,1,1]"
                                          :editing="false"
                                          size="small">
                        <slot v-for="v in view.filter(vw => vw.type === 'custom')"
                              :slot="v.prop"
                              :name="v.prop"
                              :slot-scope="d"/>
                      </cross-descriptions>
                    </slot>
                  </a-card-meta>
                </template>
                <div v-else
                     class="cover">
                  <a-empty class="vertical-center"
                           :description="false"/>
                </div>
              </slot>
              <!-- @slot 自定义标题组 -->
              <slot
                slot="title"
                name="title"
                v-if="isDefined(d.checked) || actions.length"
                :slot-scope="d">
                <a-row>
                  <a-checkbox
                    v-if="isDefined(d.checked)"
                    :checked="d.checked"
                    :disabled="d.disabled"
                    @change="onChange($event, d)"
                    class="mr10"/>
                  <a-dropdown :trigger="['click']" v-if="actions && actions.length"
                              size="small" class="fr">
                    <a @click="e => e.preventDefault()">
                      <a-icon type="ellipsis" />
                    </a>
                    <a-menu slot="overlay" @click="onClick">
                      <a-menu-item
                        v-for="m in actions"
                        :value="d"
                        :disabled="isFunction(m.disabled) ? m.disabled(d) : !!m.disabled"
                        :key="m.key">
                        {{ isFunction(m.locale) ? m.locale() : $t(m.key) }}
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                </a-row>
              </slot>
              <div v-if="meta !== 'cover'"
                   class="cross-card-body pd10">
                <!-- @slot 自定义卡片meta -->
                <slot :slot-scope="d">
                  <template v-if="meta === 'media' && d[view[0].prop]">
                    <div v-if="view[0].type === 'scale'"
                         class="scale scale-bg text-center font12"
                         :style="{width: `${d[view[0].prop][0]/d[view[0].prop][1]*70}px`, height: '70px'}">
                      {{ isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : numberFormat(d[view[0].prop]) }}
                    </div>
                    <a v-else
                       class="image text-center"
                       :data-fancybox="fancyboxGroup"
                       :data-src="isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop]"
                       :style="d.icon ? d.thumb : {backgroundImage: `url(${d.thumb ? d.thumb : (isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop])})`}">
                      <div v-if="d.icon"
                           class="play">
                        <i v-if="d.icon.indexOf('fa')!==-1"
                           :class="d.icon"/>
                        <a-icon v-else
                                class="font42"
                                :type="d.icon"/>
                      </div>
                    </a>
                  </template>
                  <cross-descriptions v-else-if="meta === 'text' && view.length"
                                      :view="view"
                                      :data-model="{model: d}"
                                      :columns="[1,1,1]"
                                      :editing="false"
                                      size="small">
                    <slot v-for="v in view.filter(vw => vw.type === 'custom')"
                          :slot="v.prop"
                          :name="v.prop"
                          :slot-scope="d"/>
                  </cross-descriptions>
                  <div v-else
                       class="image">
                    <a-empty class="vertical-center"
                             :description="false"/>
                  </div>
                </slot>
              </div>
              <!-- @slot 自定义操作组 -->
              <slot slot="actions"
                    name="actions"
                    :slot-scope="d">
                <template v-for="(a, index) in actions">
                  <a-button :key="a.key"
                            v-if="index < 2"
                            size="small"
                            type="link"
                            :disabled="isFunction(a.disabled) ? a.disabled(d) : !!a.disabled"
                            @click="onClick({item: {value: d}, key: a.key})">
                    <template v-if="a.icon">
                      <i v-if="a.icon.startsWith('fa')"
                         :class="['mr10 fa', a.icon]"/>
                      <a-icon v-else
                              :type="a.icon"/>
                    </template>
                    {{ isFunction(a.locale) ? a.locale() : $t(a.key) }}
                  </a-button>
                  <template v-if="index === 2">
                    <a-dropdown v-if="actions.length > 3"
                                :key="a.key"
                                :trigger="['click']"
                                size="small">
                      <a @click="e => e.preventDefault()">
                        <a-icon type="ellipsis"/>
                      </a>
                      <a-menu slot="overlay"
                              @click="onClick">
                        <a-menu-item v-for="m in actions.filter((a,i) => i>1)"
                                     :value="d"
                                     :disabled="isFunction(a.disabled) ? a.disabled(d) : !!a.disabled"
                                     :key="m.key">
                          <template v-if="m.icon">
                            <i v-if="m.icon.startsWith('fa')"
                               :class="['mr10 fa', m.icon]"/>
                            <a-icon v-else
                                    :type="m.icon"/>
                          </template>
                          {{ isFunction(m.locale) ? m.locale() : $t(m.key) }}
                        </a-menu-item>
                      </a-menu>
                    </a-dropdown>
                    <a-button v-else
                              :key="a.key"
                              size="small"
                              type="link"
                              :disabled="isFunction(a.disabled) ? a.disabled(d) : !!a.disabled"
                              @click="onClick({item: {value: d}, key: a.key})">
                      <template v-if="a.icon">
                        <i v-if="a.icon.startsWith('fa')"
                           :class="['mr10 fa', a.icon]"/>
                        <a-icon v-else
                                :type="a.icon"/>
                      </template>
                      {{ isFunction(a.locale) ? a.locale() : $t(a.key) }}
                    </a-button>
                  </template>
                </template>
              </slot>
            </a-card>
          </a-col>
        </a-row>
        <div v-if="config.loading && !config.busy"
             class="loading w100 text-center">
          <a-spin/>
        </div>
      </template>
      <a-spin v-else
              :spinning="config.loading || false">
        <a-row :gutter="15"
               class="cross-card-item">
          <a-col v-for="(d, index) in data"
                 :key="index"
                 class="mb15"
                 :xxl="columns[0]" :xl="columns[1]" :lg="columns[2]" :md="24" :sm="24" :xs="24">
            <a-card hoverable
                    size="small"
                    :bordered="bordered">
              <!-- @slot 自定义卡片meta -->
              <slot v-if="meta === 'cover'"
                    slot="cover"
                    name="cover"
                    :slot-scope="d">
                <template v-if="d[view[0].prop]">
                  <a v-if="isFunction(view[0].click)"
                     class="cover text-center"
                     @click="view[0].click(d[view[0].prop])"
                     :style="d.icon ? d.thumb : {backgroundImage: `url(${d.thumb ? d.thumb : isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop]})`}"/>
                  <a v-else
                     class="cover text-center"
                     :data-src="isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop]"
                     :data-fancybox="fancyboxGroup"
                     :data-type="d.icon ? 'video' : 'image'"
                     :style="d.icon ? d.thumb : {backgroundImage: `url(${d.thumb ? d.thumb : (isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop])})`}">
                    <div v-if="d.icon"
                         class="play">
                      <i v-if="d.icon.indexOf('fa')!==-1"
                         :class="d.icon"/>
                      <a-icon v-else
                              class="font42"
                              :type="d.icon"/>
                    </div>
                  </a>
                  <a-card-meta v-if="view.length > 1"
                               class="pd5">
                    <slot slot="description"
                          name="description"
                          :slot-scope="d">
                      <cross-descriptions :view="view.filter((a, i) => i > 0)"
                                          :data-model="{model: d}"
                                          :columns="[1,1,1]"
                                          :editing="false"
                                          size="small">
                        <slot v-for="v in view.filter(vw => vw.type === 'custom')"
                              :slot="v.prop"
                              :name="v.prop"
                              :slot-scope="d"/>
                      </cross-descriptions>
                    </slot>
                  </a-card-meta>
                </template>
                <div v-else
                     class="cover">
                  <a-empty class="vertical-center"
                           :description="false"/>
                </div>
              </slot>
              <!-- @slot 自定义标题组 -->
              <slot
                slot="title"
                name="title"
                v-if="isDefined(d.checked) || actions.length"
                :slot-scope="d">
                <a-row>
                  <a-checkbox
                    v-if="isDefined(d.checked)"
                    :checked="d.checked"
                    :disabled="d.disabled"
                    @change="onChange($event, d)"
                    class="mr10"/>
                  <a-dropdown :trigger="['click']" v-if="actions && actions.length"
                              size="small" class="fr">
                    <a @click="e => e.preventDefault()">
                      <a-icon type="ellipsis" />
                    </a>
                    <a-menu slot="overlay" @click="onClick">
                      <a-menu-item
                        v-for="m in actions"
                        :value="d"
                        :disabled="isFunction(m.disabled) ? m.disabled(d) : !!m.disabled"
                        :key="m.key">
                        {{ isFunction(m.locale) ? m.locale() : $t(m.key) }}
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                </a-row>
              </slot>
              <div v-if="meta !== 'cover'"
                   class="cross-card-body pd10">
                <slot :slot-scope="d">
                  <template v-if="meta === 'media' && d[view[0].prop]">
                    <div v-if="view[0].type === 'scale'"
                         class="scale scale-bg text-center font12"
                         :style="{width: `${d[view[0].prop][0]/d[view[0].prop][1]*70}px`, height: '70px'}">
                      {{ isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop] }}
                    </div>
                    <a v-else
                       class="image text-center"
                       :data-fancybox="fancyboxGroup"
                       :data-type="d.icon ? 'video' : 'image'"
                       :data-src="isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop]"
                       :style="d.icon ? d.thumb : {backgroundImage: `url(${d.thumb ? d.thumb : (isFunction(view[0].format) ? view[0].format(d[view[0].prop]) : d[view[0].prop])})`}">
                      <div v-if="d.icon"
                           class="play">
                        <i v-if="d.icon.indexOf('fa')!==-1"
                           :class="d.icon"/>
                        <a-icon v-else
                                class="font42"
                                :type="d.icon"/>
                      </div>
                    </a>
                  </template>
                  <cross-descriptions v-else-if="meta === 'text' && view.length"
                                      :view="view"
                                      :data-model="{model: d}"
                                      :columns="[1,1,1]"
                                      :editing="false"
                                      size="small">
                    <slot v-for="v in view.filter(vw => vw.type === 'custom')"
                          :slot="v.prop"
                          :name="v.prop"
                          :slot-scope="d"/>
                  </cross-descriptions>
                  <div v-else
                       class="image">
                    <a-empty class="vertical-center"
                             :description="false"/>
                  </div>
                </slot>
              </div>
            </a-card>
          </a-col>
        </a-row>
      </a-spin>
    </div>
    <a-empty v-if="!config.loading && !data.length"
             :image="emptyImage"/>
  </div>
</template>

<i18n src="../../locale/share/common.json"></i18n>

<script>
import types from '../../mixin/types'
import cardTable from '../../mixin/cardTable'
import cardList from '../../mixin/cardList'
import empty from '../../mixin/empty'
import format from '../../mixin/format'

/**
 * 卡片表格
 *
 * @displayName Common Card
 * @version 1.0
 */
export default {
  name: 'cross-card',
  mixins: [types, cardTable, cardList, empty, format],
  directives: {},
  props: {
    /**
     * 标题组
     *
     * [{prop:属性字段,format:格式化函数}]
     */
    title: {
      type: Array,
      default: () => {
        return []
      }
    },
    /**
     * 数据视图模式
     *
     * @values text,cover,media
     */
    meta: {
      type: String,
      default: 'text'
    },
    /**
     * 卡片列数
     *
     * @values [6, 8, 12]
     */
    columns: {
      type: Array,
      default: () => {
        return [6, 8, 12]
      }
    },
    /**
     * 卡片边框
     *
     * @values true,false
     */
    bordered: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      outer: '.cross-card',
      fancyboxGroup: `card-gallery-${parseInt(Math.random() * 10000)}`
    }
  }
}
</script>

<style lang="scss">
.ant-dropdown-menu{
  padding: 7px 0;
  min-width: 80px;
  .ant-dropdown-menu-item{
    padding: 5px 16px;
  }
}
.cross-card {
  position: relative;
  height: 100%;
  .scroll {
    overflow-y: auto;
    overflow-x: hidden;

    .loading {
      position: absolute;
      bottom: 20px;
    }
  }

  .cross-card-item {

    .ant-card-small > .ant-card-body {
      padding: 12px 12px 0 12px;
    }

    .ant-card-actions {
      border-top: none;
      border-radius: 0 0 8px 8px;
    }

    .ant-card-hoverable {
      cursor: default;

      .switch, .checkbox {
        position: absolute;
        width: auto;
        z-index: 1;
      }
      &:hover{
        border-color: #1890FF;
      }
    }

    .image {
      width: 80%;
      margin: 0 auto;
      height: 140px;
      line-height: 140px;
      background-position: top;
      background-size: contain;
    }

    .cover {
      width: 100%;
      height: 180px;
      background-position: center;
      background-size: cover;
      line-height: 180px;
    }

    .image, .cover {
      display: block;
      outline: none;
      text-decoration: none;
      background-repeat: no-repeat;
      color: unset;
    }

    .scale {
      margin: 0 auto;
      line-height: 70px;
    }
  }
}
</style>
