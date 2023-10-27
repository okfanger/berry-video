<template>
  <div class="classify-container">
    <div class="card" v-for="item in 12" :key="item">
      <VideoNext />
    </div>
    <!-- <controlsBar /> -->
 </div>
</template>

<script setup>
import VideoNext from '@/components/VideoComponent/OriginNext.vue'
import controlsBar from '@/components/VideoComponent/controlsBar.vue';
</script>

<style scoped>
.classify-container {
  height: calc(100vh - 120px);
  width: 100%;
  display: flex;
  overflow-y: scroll;
  flex-wrap: wrap;
  position: relative;
}
.card {
  height: 380px;
  width: 260px;
}
</style>