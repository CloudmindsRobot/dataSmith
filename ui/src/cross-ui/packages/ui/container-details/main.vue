<template>
  <div class="cross-container-details">
    <a-card :bordered="false" 
            size="small">
      <slot/>      
    </a-card>
  </div>
</template>

<script>
/**
 * 容器详情
 *
 * @displayName Container Details
 * @version 1.0
 */
export default {
  name: 'cross-container-details',
  data() {
    return {}
  }
}
</script>

<style lang="scss">
.cross-container-details {
  .ant-card{
    border-radius: 0;
  }
}
</style>