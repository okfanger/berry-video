<template>
  <div>
    <el-skeleton class="card-video-skeneton" animated :throttle="100">
      <template #template>
        <el-skeleton-item variant="image" style="width: 100%;height: 80%; aspect-ratio: 0.68;" />
        <el-skeleton-item variant="text" style="width: 50%" />
      </template>
    </el-skeleton>
 </div>
</template>

<script setup>

</script>

<style scoped lang="scss">
.card-video-skeneton {
  height: 100%;
  width: 100%;
}
</style>