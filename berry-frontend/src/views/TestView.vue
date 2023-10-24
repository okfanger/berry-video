<template>
  <div class="dplayer-container">
    <div id="dplayer" ref="player" class="dplayer video-box"></div>
 </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Dplayer from 'dplayer'
import Hls from "hls.js";

const video = {
  url: "https://e-sign.dms.t.cn-np.com/files/m3u8_file/c4b94118-3c8d-4410-9987-985c2b44c278/c4b94118-3c8d-4410-9987-985c2b44c278.m3u8",
  cover: "https://cn.bing.com/th?id=OHR.MeotoIwa_ZH-CN3126370410_1920x1080.jpg&rf=LaDigue_1920x1080.jpg"
}
const player = ref();
const dp = ref();

onMounted(()=>{
  loadVedio()
})


const loadVedio = () => {
  dp.value = new Dplayer({          //初始化视频对象
    container:player.value,   //指定视频容器节点
    video:{
      url: video.url,   // 指定视频播放链接
      pic: video.cover,  // 指定视频封面图
      type: "customHls",
      customType: {
        customHls: function (video) {
          const hls = new Hls();
          hls.loadSource(video.src);
          hls.attachMedia(video);
        },
      },
    }
  })
}


</script>

<style scoped>
.dplayer-container {
  height: 500px;
  width: 500px;
}
.dplayer-container  {
 
}
</style>