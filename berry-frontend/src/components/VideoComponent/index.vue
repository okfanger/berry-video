<template>
  <div class="vedio-container">
    <div class="dplayer-container">
      
      <!-- 视频 -->
      <!-- <div id="dplayer" ref="player" class="dplayer video-box"></div> -->
      <OriginVideo 
        @play="handlerPlay"
        @mute="handlerMute"
        :volume="volume"
        :speed="speed"
        :speedList="speedList"
        @changeSpeed="handlerChangeSpeed"
        :videoSrc="videoSrc"
        :coverSrc="coverSrc"
        :streamLoad="streamLoad"


        :continuous="continuous"
        @printscreen="handlerPrintscreen"
        :openPrintScreen="openPrintScreen"
        @videoEnd="videoEnd"
        
      />
      <!-- 点赞,评论,收藏,分享 -->
      <div class="func">
        <div class="like" :class="{ liked: funcInfo.like.value  }" @click="liked()">
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
    
    <!-- 评论面板 -->
    <div class="commonPane" v-if="isCommoning">

    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import Dplayer from 'dplayer'
import Hls from "hls.js";
import OriginVideo from './OriginIndex.vue';

const video = {
  url: "https://e-sign.dms.t.cn-np.com/files/m3u8_file/c4b94118-3c8d-4410-9987-985c2b44c278/c4b94118-3c8d-4410-9987-985c2b44c278.m3u8",
  cover: "https://cn.bing.com/th?id=OHR.MeotoIwa_ZH-CN3126370410_1920x1080.jpg&rf=LaDigue_1920x1080.jpg"
}
const player = ref();
const dp = ref();
const volume = ref(1);
const speed = ref(10);
const speedList = ref([0.25, 0.5, 0.75, 1, 1.25, 1.5, 2, 3, 4, 5, 10])
const continuous = ref(true) 
const openPrintScreen = ref(false)
const videoSrc = ref("http://toffee-private-oss.akfang.cn/4585_1697538264.mp4")
const coverSrc = ref("coverSrc")
const streamLoad = ref(false)

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


const isCommoning = ref(false)
const liked = () => {
  funcInfo.like.value = !funcInfo.like.value;
}
const changeCommonState = () => {
  isCommoning.value = !isCommoning.value
}
const collected = () => {
  funcInfo.collect.value = !funcInfo.collect.value;
}
onMounted(()=>{
  // loadVedio()
})

const handlerPlay = (e) => {
  console.log("handlerPlay", e);
}
const handlerMute = (e) => {
  console.log("handlerMute", e);
}
const handlerChangeSpeed = (val) => {
  console.log("changeSpeed", val);
  speed.value = val;
}
const videoEnd = () => {
  console.log("end 父组件");
}
const handlerPrintscreen = () => {}
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
  height: 50px;
}


/* 爱心点击效果 */

.liked i.icon-aixin1 {
	animation:  blink 1s forwards;
}
.func i:hover {
  font-size: 35px;
}

@keyframes blink{
	10%{
    font-size: 35px;
		color: #c23802 ;    
		/* box-shadow: 0 0 250px #f1e794;  */
	}
	100% {
    font-size: 30px;
		color: #ff2e56;     
		/* box-shadow: 0 0 250px #FFEB3B  */
	}
}



.commonPane {
  height: 100%;
  width: 400px;
  background-color: #e2e2e2;
  border-top-right-radius: 20px;
  border-bottom-right-radius: 20px;
}


</style>


