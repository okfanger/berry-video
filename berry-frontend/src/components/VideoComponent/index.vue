<template>
  <div class="vedio-container">
    <div class="dplayer-container">
      <div id="dplayer" ref="player" class="dplayer video-box"></div>
      <div class="func">
        <div class="like" @click="liked()">
          <i class="iconfont icon-aixin2" v-if="!funcInfo.like.value"></i>
          <i class="iconfont icon-aixin1" style="color:#ff2e56;" v-else></i>
          <div>{{ funcInfo.like.num }}</div>
        </div>
        <div class="common">
          <i class="iconfont icon-pinglun1" @click="changeCommonState"></i>
          <div>{{ funcInfo.common.num }}</div>
        </div>
        <div class="collect" @click="collected">
          <i class="iconfont icon-shoucangfill" v-if="!funcInfo.collect.value"></i>
          <i class="iconfont icon-jiaxingshoucangtianchong" style="color: #feba28;" v-else></i>
          <div>{{ funcInfo.collect.num }}</div>
        </div>
        <div class="transpond">
          <i class="iconfont icon-zhuanfa"></i>
          <div>{{ funcInfo.transpond.num }}</div>
        </div>
      </div>
    </div>
    
    <div class="commonPane" v-if="isComming">

    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import Dplayer from 'dplayer'
import Hls from "hls.js";

const video = {
  url: "https://e-sign.dms.t.cn-np.com/files/m3u8_file/c4b94118-3c8d-4410-9987-985c2b44c278/c4b94118-3c8d-4410-9987-985c2b44c278.m3u8",
  cover: "https://cn.bing.com/th?id=OHR.MeotoIwa_ZH-CN3126370410_1920x1080.jpg&rf=LaDigue_1920x1080.jpg"
}
const player = ref();
const dp = ref();
const funcInfo = reactive({
  like: {
    value: false,
    num: "5.8万"
  },
  common: {
    num: '2800'
  },
  collect: {
    value: false,
    num: 1760,
  },
  transpond: {
    num: 7434
  }
})
const isComming = ref(false)
const liked = () => {
  funcInfo.like.value = !funcInfo.like.value;
}
const changeCommonState = () => {
  isComming.value = !isComming.value
}
const collected = () => {
  funcInfo.collect.value = !funcInfo.collect.value;
}
onMounted(()=>{
  loadVedio()
})


const loadVedio = () => {
  dp.value = new Dplayer({          //初始化视频对象
    container:player.value,   //指定视频容器节点
    hotkey: true, // 支持快进
    autoplay: false, // 自动播放视频
    screenshot: true, // 截图
    theme: "#fe5c71", // 主题色
    video:{
      url: video.url,   // 指定视频播放链接
      pic: video.cover,  // 指定视频封面图
      type: "customHls",
      thumbnails: "",  // 视频缩略图
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
.vedio-container {
  height: calc(100vh - 120px);
  width: 100%;
  display: flex;
  border-radius: 20px;
  overflow: hidden;
  border: 1px solid;
  min-width: 700px;
}
.dplayer-container{
  width: 100%;
  overflow: hidden;
  position: relative;
}
.dplayer-container .dplayer {
  width: 100%;
  height: 100%;
}
.func {
  display: flex;
  flex-direction: column;
  width: 100px;  
  justify-content: end;
  align-items: center;
  padding-bottom: 90px;
  z-index: 999 !important;
  position: absolute;
  bottom: 40px;
  right: 0;
}
.func i {
  font-size: 30px;
  color: white;

}
.func > div {
  margin: 10px 0;
  text-align: center;
  color: white;
  user-select: none;
}


.commonPane {
  height: 100%;
  width: 400px;
  background-color: #e2e2e2;
  border-top-right-radius: 20px;
  border-bottom-right-radius: 20px;
}
</style>