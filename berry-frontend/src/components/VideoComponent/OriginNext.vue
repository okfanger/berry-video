<template>
 <div class="video-container">
    <div class="initial-video">
      <video ref="video"
        webkit-playsinline="true"
        playsinline="true"
        controlslist="nodownload"
        crossorigin="anonymous">
        <source :src="videoSrc" type="video/mp4">
        您的浏览器不支持video标签，请使用google浏览器浏览
      </video>
    </div>
    <controlsBar  />
  </div>
</template>

<script setup>
import { ref, onMounted, defineEmits, defineProps } from 'vue'
import controlsBar from './controlsBar.vue';
const emits = defineEmits(['play', 'mute', 'update:speed',
   'printscreen', 'videoEnd', "likeOrDisLike",
  'upadte:volume'])

const props = defineProps(['volume', 'speed', 'speedList',
   'continuous', 'openPrintScreen', 'videoSrc', 'coverSrc',
    'bgImg', 'streamLoad', ])


const video = ref(); // 视频
const muted = ref(false) // 是否静音

onMounted(()=>{
  initVideo()
})

const initVideo = () => {
  video.value.currentTime = 0;
  muted.value = video.value.muted = props.volume == 0 ? true : false;
  video.value.playbackRate = props.speed;
}
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