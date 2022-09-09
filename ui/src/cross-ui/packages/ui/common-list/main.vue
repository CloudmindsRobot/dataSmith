<template>
  <div class="cross-list">
    <div class="scroll"
         v-infinite-scroll="handleInfiniteOnLoad"
         :infinite-scroll-disabled="config.busy"
         :infinite-scroll-distance="5">
      <a-list v-if="!isMobile || config.isList"
              :pagination="pagination"
              :data-source="data"
              :loading="config.loading">
        <a-list-item slot="renderItem"
                     slot-scope="item">
          <a-list-item-meta>
            <!-- @slot 自定义图示 -->
            <slot v-if="view.length > 0"
                  slot="avatar"
                  name="avatar"
                  :slot-scope="item">
              <template v-if="isFunction(view[0].format) ? view[0].format(item[view[0].prop]) : item[view[0].prop]">
                <div v-if="view[0].type === 'scale'"
                     class="scale scale-bg text-center font12"
                     :style="{height: `${item[view[0].prop][1]/item[view[0].prop][0]*52}px`, width: '52px'}"
                     v-html="isFunction(view[0].format) ? view[0].format(item[view[0].prop]) : numberFormat(item[view[0].prop])"/>
                <a v-else-if="view[0].type === 'media'"
                   class="image text-center"
                   :data-fancybox="item.icon ? 'video-gallery' : 'gallery'"
                   :data-type="item.icon ? 'video' : 'image'"
                   :data-src="isFunction(view[0].format) ? view[0].format(item[view[0].prop]) : item[view[0].prop]"
                   :style="item.icon ? item.thumb : {backgroundImage: `url(${item.thumb ? item.thumb : (isFunction(view[0].format) ? view[0].format(item[view[0].prop]) : item[view[0].prop])})`}">
                  <div v-if="item.icon"
                       class="play">
                    <i v-if="item.icon.indexOf('fa')!==-1"
                       :class="item.icon"/>
                    <a-icon v-else
                            class="font52"
                            :type="item.icon"/>
                  </div>
                </a>
                <span v-else></span>
              </template>
              <a-icon v-else
                      type="picture"
                      class="font52"/>
            </slot>
            <!-- @slot 自定义标题 -->
            <slot v-if="view.length > 1"
                  slot="title"
                  name="title"
                  :slot-scope="item">
              <a v-if="view[1].type === 'link'"
                 @click="e => {e.preventDefault();e.stopPropagation();e.isFunction(view[1].click) && view[1].click(item)}"
                 v-html="isFunction(view[1].format) ? view[1].format(item[view[1].prop]) : numberFormat(item[view[1].prop])"/>
              <p v-else-if="view[1].type === 'parent'">
                <strong class="font16"
                        v-html="isFunction(view[0].format) ? view[0].format(item[view[0].prop]) : numberFormat(item[view[0].prop])"/>
                <br>
                <span
                    v-html="isFunction(view[1].format) ? view[1].format(item[view[1].prop]) : numberFormat(item[view[1].prop])"/>
              </p>
              <span v-else
                    v-html="isFunction(view[1].format) ? view[1].format(item[view[1].prop]) : `<strong class='font16'>${numberFormat(item[view[1].prop])}</strong>`"/>
            </slot>
            <!-- @slot 自定义描述 -->
            <slot v-if="view.length > 2"
                  slot="description"
                  name="description"
                  :slot-scope="item">
              <cross-descriptions :view="view.filter((a, i) => i > 1)"
                                  :data-model="{model: item}"
                                  :columns="[1,1,2]"
                                  :editing="false"
                                  size="small">
                <slot v-for="v in view.filter(vw => vw.type === 'custom')"
                      :slot="v.prop"
                      :name="v.prop"
                      :slot-scope="item"/>
              </cross-descriptions>
            </slot>
          </a-list-item-meta>
          <a-checkbox v-if="isDefined(item.checked)"
                      :checked="item.checked"
                      :disabled="item.disabled"
                      @change="onChange($event, item)"/>
          <a-dropdown v-if="actions.length > 1"
                      :trigger="['click']">
            <a-button type="link">
              <a-icon type="ellipsis" />
            </a-button>
            <a-menu slot="overlay"
                    @click="onClick">
              <a-menu-item v-for="m in actions"
                           :value="item"
                           :disabled="isFunction(m.disabled) ? m.disabled(item) : !!m.disabled"
                           :key="m.key">
                {{ isFunction(m.locale) ? m.locale() : $t(m.key) }}
              </a-menu-item>
            </a-menu>
          </a-dropdown>
          <template v-else>
            <a-button v-for="a in actions"
                      size="small"
                      type="primary"
                      :key="a.key"
                      :disabled="isFunction(a.disabled) ? a.disabled(item) : !!a.disabled"
                      @click="onClick({item: {value: item}, key: a.key})">
              {{ isFunction(a.locale) ? a.locale() : $t(a.key) }}
            </a-button>
          </template>
        </a-list-item>
      </a-list>
      <a-list v-else
              :data-source="data">
        <a-list-item slot="renderItem"
                     slot-scope="item">
          <a-list-item-meta>
            <!-- @slot 自定义图示 -->
            <slot v-if="view.length > 0"
                  slot="avatar"
                  name="avatar"
                  :slot-scope="item">
              <template v-if="item[view[0].prop]">
                <div v-if="view[0].type === 'scale'"
                     class="scale scale-bg text-center font12"
                     :style="{height: `${item[view[0].prop][1]/item[view[0].prop][0]*52}px`, width: '52px'}"
                     v-html="isFunction(view[0].format) ? view[0].format(item[view[0].prop]) : numberFormat(item[view[0].prop])"/>
                <a v-else-if="view[0].type === 'media'"
                   class="image text-center"
                   :data-fancybox="item.icon ? 'video-gallery' : 'gallery'"
                   :data-type="item.icon ? 'video' : 'image'"
                   :data-src="isFunction(view[0].format) ? view[0].format(item[view[0].prop]) : item[view[0].prop]"
                   :style="item.icon ? item.thumb : {backgroundImage: `url(${item.thumb ? item.thumb : (isFunction(view[0].format) ? view[0].format(item[view[0].prop]) : item[view[0].prop])})`}">
                  <div v-if="item.icon"
                       class="play">
                    <i v-if="item.icon.indexOf('fa')!==-1"
                       :class="item.icon"/>
                    <a-icon v-else
                            class="font42"
                            :type="item.icon"/>
                  </div>
                </a>
                <span v-else></span>
              </template>
              <a-empty v-else
                       :image="emptyImage"
                       :description="false"/>
            </slot>
            <!-- @slot 自定义标题 -->
            <slot v-if="view.length > 1"
                  slot="title"
                  name="title"
                  :slot-scope="item">
              <a v-if="view[1].type === 'link'"
                 @click="e => {e.preventDefault();e.stopPropagation();isFunction(view[1].click) && view[1].click(item)}"
                 v-html="isFunction(view[1].format) ? view[1].format(item[view[1].prop]) : numberFormat(item[view[1].prop])"/>
              <p v-else-if="view[1].type === 'parent'">
                <strong class="font16"
                        v-html="isFunction(view[0].format) ? view[0].format(item[view[0].prop]) : numberFormat(item[view[0].prop])"/>
                <br>
                <span
                    v-html="isFunction(view[1].format) ? view[1].format(item[view[1].prop]) : numberFormat(item[view[1].prop])"/>
              </p>
              <span v-else
                    v-html="isFunction(view[1].format) ? view[1].format(item[view[1].prop]) : numberFormat(item[view[1].prop])"/>
            </slot>
            <!-- @slot 自定义描述 -->
            <slot v-if="view.length > 2"
                  slot="description"
                  name="description"
                  :slot-scope="item">
              <cross-descriptions :view="view.filter((a, i) => i > 1)"
                                  :data-model="{model: item}"
                                  :columns="[1,1,2]"
                                  :editing="false"
                                  size="small">
                <slot v-for="v in view.filter(vw => vw.type === 'custom')"
                      :slot="v.prop"
                      :name="v.prop"
                      :slot-scope="item"/>
              </cross-descriptions>
            </slot>
          </a-list-item-meta>
          <a-switch class="mr10"
                    v-if="isDefined(item.checked)"
                    v-model="item.checked"
                    :disabled="item.disabled"
                    @change="onSwitch"/>
          <a-dropdown v-if="actions.length > 1"
                      :trigger="['click']">
            <a-button type="link">
              <a-icon type="ellipsis" />
            </a-button>
            <a-menu slot="overlay"
                    @click="onClick">
              <a-menu-item v-for="m in actions"
                           :value="item"
                           :disabled="isFunction(m.disabled) ? m.disabled(item) : !!m.disabled"
                           :key="m.key">
                {{ isFunction(m.locale) ? m.locale() : $t(m.key) }}
              </a-menu-item>
            </a-menu>
          </a-dropdown>
          <template v-else>
            <a-button v-for="a in actions"
                      type="primary"
                      :key="a.key"
                      :disabled="isFunction(a.disabled) ? a.disabled(item) : !!a.disabled"
                      @click="onClick({item: {value: item}, key: a.key})">
              {{ isFunction(a.locale) ? a.locale() : $t(a.key) }}
            </a-button>
          </template>
        </a-list-item>
        <div v-if="config.loading && !config.busy"
             class="w100 text-center">
          <a-skeleton v-if="inMobile"
                      :avatar="skeletonAvatar"
                      :paragraph="{ rows: 2 }"/>
          <a-spin v-else/>
        </div>
      </a-list>
    </div>
  </div>
</template>

<i18n src="../../locale/share/common.json"/>

<script>
import types from '../../mixin/types'
import cardTable from '../../mixin/cardTable'
import tableList from '../../mixin/tableList'
import cardList from '../../mixin/cardList'
import empty from '../../mixin/empty'
import Device from '../../tools/device'
import format from '../../mixin/format'

/**
 * 列表
 *
 * @displayName Common List
 * @version 1.0
 */
export default {
  name: 'cross-list',
  mixins: [types, cardTable, tableList, cardList, empty, format],
  directives: {},
  props: {
    /**
     * 骨架屏显示头像占位图
     *
     * @values true,false
     */
    skeletonAvatar: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      outer: '.cross-list',
      inMobile: Device.inMobile()
    }
  }
}
</script>

<style lang="scss">
.cross-list {
  .scroll {
    overflow-y: auto;
    overflow-x: hidden;
  }

  .ant-list-item-meta {
    font-size: inherit;

    a {
      color: #1890ff;
    }
  }

  .image {
    width: 52px;
    height: 52px;
    line-height: 52px;
    display: block;
    outline: none;
    text-decoration: none;
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center;
    color: unset;
    position: relative;
  }

  .scale {
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: justify;
  }
}
</style>