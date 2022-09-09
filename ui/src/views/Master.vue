<template>
  <div>
    <cross-layout mode="inline">
      <cross-menu slot="sider"
                  :menus="menus"
                  style-flag="hCalc"></cross-menu>
    </cross-layout>
  </div>
</template>

<script>
/**
 * 母版页
 */
export default {
  name: "Master",
  data() {
    const user = this.$tools.seting.get(this.$tools.config.user);
    return {
      menus: [{
        id: 1,
        parentId: 0,
        icon: 'profile',
        component: 'log'
      }, {
        id: 2,
        parentId: 0,
        icon: 'fund',
        component: 'target'
      }, {
        id: 3,
        parentId: 0,
        icon: 'table',
        component: 'model'
      }, {
        id: 4,
        parentId: 0,
        icon: 'database',
        component: 'source'
      }],
      user: user
    };
  }
}
</script>
