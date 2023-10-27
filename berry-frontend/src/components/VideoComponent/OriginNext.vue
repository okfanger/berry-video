<template>
 <div class="video-container">
    <video >
      <source src="http://toffee-private-oss.akfang.cn/4585_1697538264.mp4" type="video/mp4">
    </video>
    <controlsBar />
  </div>
</template>

<script setup>
import controlsBar from './controlsBar.vue';
</script>

<style scoped>
/* 视频容器 */
.video-container {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  width: 100%;
  height: 100%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 视频元素 */
.video-container video {
  height: 100%;
  width: 100%;
  object-fit: fill;
  border-radius: 8px;
  transform: scale(1);
  transition: transform .5s ease-out;
}
.video-container:hover video {
  height: 100%;
  object-fit: fill;
  transform: scale(1.1);
}


</style>