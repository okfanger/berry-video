<template>
  <div class="vedio-container">
    <div class="dplayer-container">
      <!-- 视频 -->
      <OriginVideo
        :id="videoObj.id"
        v-model:volume="volume"
        v-model:speed="speed"
        :speedList="speedList"
        :url="videoObj.url"
        :cover="videoObj.cover"
        :streamLoad="streamLoad"
        :continuous="continuous"
        :content="videoObj.content"
        :openPrintScreen="openPrintScreen"
        :create-time="videoObj.createTime"
        @printscreen="handlerPrintscreen"
        @videoEnd="videoEnd"
        @play="handlerPlay"
        @mute="handlerMute"
        @likeOrDisLike="handlerLiked"
      />
     
    </div>
    
    <!-- 评论面板 -->
    <div v-if="isCommoning">
      <commentPanel 
        :videoId="videoObj.id" 
        v-model:videoPropsPanel="videoObj"
      />
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, defineProps } from 'vue'
import OriginVideo from './OriginIndex.vue';
import { continuous,isCommoning } from '@/utils'
import commentPanel from '@/components/Comment/commentPanel'

const props = defineProps({
  videoPropsObj: Object
})
const volume = ref(1);
const speed = ref(1);
const speedList = ref([0.25, 0.5, 0.75, 1, 1.25, 1.5, 2])

const openPrintScreen = ref(false)
const streamLoad = ref(false)

const videoObj = ref(props.videoPropsObj)

onMounted(()=>{
  
})


const handlerPlay = (e) => {
}
const handlerMute = (e) => {
}
const videoEnd = () => {
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

</style>



