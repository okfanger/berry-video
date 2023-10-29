<template>
  <div class="vedio-container">
    <div class="dplayer-container">
      <!-- 视频 -->
      <OriginVideo
        @play="handlerPlay"
        @mute="handlerMute"
        v-model:volume="volume"
        v-model:speed="speed"
        :speedList="speedList"
        :videoSrc="props.videoSrc"
        :coverSrc="coverSrc"
        :streamLoad="streamLoad"

        :continuous="continuous"
        @printscreen="handlerPrintscreen"
        :openPrintScreen="openPrintScreen"
        @videoEnd="videoEnd"
        @likeOrDisLike="liked"
      />
      <!-- 点赞,评论,收藏,分享 -->
      <div class="func">
        <div class="like" @click="liked()">
          <div class="heart" :style="{'--bgColor': funcInfo.like.value ? '#ff2e56': '#fff'}"></div>
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
import { ref, onMounted, reactive, defineProps } from 'vue'
import OriginVideo from './OriginIndex.vue';
import { continuous } from '@/utils'

const props = defineProps(['videoSrc'])
const volume = ref(1);
const speed = ref(1);
const speedList = ref([0.25, 0.5, 0.75, 1, 1.25, 1.5, 2])

const openPrintScreen = ref(false)
// const videoSrc = ref("http://toffee-private-oss.akfang.cn/4585_1697538264.mp4")
// const videoSrc = ref("sysndhy.flv")
const coverSrc = ref("coverSrc")
const streamLoad = ref(false)

const funcInfo = reactive({
  like: {
    value: true,
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
const videoEnd = () => {
  console.log("end 父组件");

}
const handlerPrintscreen = () => {}
</script>

<style scoped>
.vedio-container {
  height: calc(100vh - 120px);
  width: 100%;
  display: flex;
  border-radius: 10px;
  overflow: hidden;
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
}
.func > div {
  margin: 10px 0;
  text-align: center;
  color: white;
  user-select: none;
  height: 60px;
}


/* 爱心点击效果 */


.func i:hover {
  font-size: 35px;
}





.commonPane {
  height: 100%;
  width: 400px;
  background-color: #e2e2e2;
  border-top-right-radius: 20px;
  border-bottom-right-radius: 20px;
}


.heart {
  position: relative;
  width: 30px;
  height: 30px;
}
.heart:before,
.heart:after {
  content: "";
  position: absolute;
  background-color: white;
  width: 20px;
  height:29px;
  left: 20px;
  border-radius: 20px 20px 0 0 ;
  transform: rotate(-45deg);
  transform-origin: 0 100%;
}
.heart:after {
  left: 0;
  transform: rotate(45deg);
  transform-origin: 100% 100%;
}
.heart:hover {
  animation: heart 1s forwards;
}
@keyframes heart {
  0% {
    transform: scale(.95);
  }
  20% {
    transform: scale(1);
  }
  40% {
    transform: scale(1.15);
  }
  60% {
    transform: scale(1.20);
  }
  80% {
    transform: scale(1.15);
  }
  90% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

</style>


