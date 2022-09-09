<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  created() {
    // 初始化主题
    this.$tools.theme.init()
  }
}
</script>
