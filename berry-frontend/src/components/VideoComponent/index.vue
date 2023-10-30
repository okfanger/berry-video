<template>
  <div class="vedio-container">
    <div class="dplayer-container">
      <!-- 视频 -->
      <OriginVideo
        :id="props.id"
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
        @likeOrDisLike="handlerLiked"
      />
      <!-- 点赞,评论,收藏,分享 -->
      <div class="func">
        <div class="like" @click="handlerLiked()">
          <i class="iconfont icon-aixin1" :style="{color: funcInfo.like.value ?  '#ff2e56' : '#fff'}"></i>
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
    <div class="comment-panel" v-if="isCommoning">
      <div class="comment-header">
          <span class="comment-count">1234 条评论</span>
          <button class="close-btn" @click="changeCommonState">关闭</button>
      </div>
      <div class="comment-list">
          <!-- 这里将显示用户的评论 -->
      </div>
      <div class="comment-input-area">
          <input type="text" placeholder="留下你精彩的评论吧...">
          <button>发布</button>
      </div>
  </div>
 </div>
</template>

<script setup>
import { ref, onMounted, reactive, defineProps } from 'vue'
import OriginVideo from './OriginIndex.vue';
import { continuous, isCommoning } from '@/utils'
import { doLikeApi,unLikeApi } from '@/api/video'

const props = defineProps(['videoSrc', 'id', 'likeCount', 'liked'])
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
    value: props.liked,
    num: props.likeCount
  },
  common: {
    num: 0
  },
  collect: {
    value: false,
    num: 0,
  },
  transpond: {
    num: 0
  }
})


const handlerLiked = () => {
  let liked = funcInfo.like.value;
  if(liked) {
    unLikeApi(props.id).then(res=>{
      if(res.status === 200) {
        funcInfo.like.num--;
      }
    }) 
  } else {
    doLikeApi(props.id).then(res=>{
      if(res.status === 200) {
        funcInfo.like.num++;
      }
    })
  }
  funcInfo.like.value = !liked;
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

.comment-panel {
  
}
</style>



<!-- comment-panel -->
<style scoped>
.comment-panel {
  height: 100%;
  width: 400px;
  background-color: #e2e2e2;
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px; 
  background-color: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.comment-count {
    font-weight: bold;
}

.close-btn {
    background: none;
    border: none;
    cursor: pointer;
}

.comment-list {
  flex: 1;
}
.comment-input-area {
    display: flex;
    padding: 10px;
    border-top: 1px solid #e0e0e0;
}

.comment-input-area input {
    flex: 1;
    padding: 5px;
    border: none;
    border-radius: 5px;
    margin-right: 10px;
}

.comment-input-area button {
    background-color: #ff2d5a; /* 抖音主题色 */
    color: #fff;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
}

</style>

