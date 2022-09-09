<template>
  <div class="container">
    <div v-for="(col, i) in layouts"
         :key="i"
         v-bind="col"
         class="col"
         :class="[col.shrink ? 'transition' : '']"
         :ref="col.slot">
      <span v-if="col.shrink"
            @click="col.onFold&&col.onFold(col,cols)"
            class="shrink-btn">
        <a-icon :type="col.isFold ? 'left' : 'right'"
                class="icon"/>
      </span>
      <!-- @slot 接收col配置的slot名称 -->
      <slot :name="col.slot">
        {{ col.slot }}
      </slot>
      <template v-if="col.inserts">
        <div v-for="(insertCol,index) in col.inserts"
             :key="index"
             v-bind=" insertCol "
             :class="[insertCol.shrink ? 'transition' : '']"
             :ref="insertCol.slot">
        <span v-if="insertCol.shrink"
              @click="insertCol.onFold&&insertCol.onFold(insertCol,cols)"
              class="shrink-btn">
          <a-icon :type="insertCol.isFold ? 'left' : 'right'"
                  class="icon"/>
        </span>
          <!-- @slot 接收内嵌的col配置的slot名称 -->
          <slot :name="insertCol.slot">
            {{ insertCol.slot }}
          </slot>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
/**
 * 多列布局组件，可嵌套
 *
 * @displayName Layout Cols
 * @version 1.0
 */
export default {
  name: "cross-layout-cols",
  props: {
    /**
     * 设置多列布局
     */
    cols: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      layouts: []
    };
  },
  watch: {
    cols: {
      immediate: true,
      handler: function (v) {
        // 将cols变为响应式
        this.layouts = v
      }
    }
  }
}
</script>

<style lang='scss' scoped type='text/css'>
.container {
  width: 100%;
  height: 100%;
  min-height: 50vh;
  position: relative;
  overflow: hidden;
  display: flex;

  .col {
    height: 100%;
    position: relative;
  }

  .transition {
    transition: all 0.3s;
  }
}

.shrink-btn {
  position: absolute;
  display: inline-block;
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  height: 4.125rem;
  width: 0;
  border: solid;
  border-width: 0.45rem 0.85rem;
  border-color: transparent rgba(158, 158, 158, 0.2) transparent transparent;
  left: 0;
  top: 50%;
  transform: translate(-100%, -50%);
  z-index: 1;
  cursor: pointer;

  .icon {
    position: relative;
    transform: translate(0, 80%);
  }
}
</style>