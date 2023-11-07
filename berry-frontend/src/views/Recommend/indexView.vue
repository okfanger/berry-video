<template>
  <div class="video_list" v-scroll>
      <VideoComponent  
        v-for="(item,index) in videoList" 
        v-model:videoPropsObj="videoList[index]" 
        v-slide-in="test"
        :key="videoList[index].id"
      />
 </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import VideoComponent from '@/components/VideoComponent'
import 'vue3-virtual-scroller/dist/vue3-virtual-scroller.css'
import { getVideoFeed } from '@/api/video';



const videoList = ref([])
const currentIndex = ref(0)
const currentVideo = ref({})
const prevVideo = ref({})
const nextVideo = ref({})


onMounted(() => {
  fetchVideoFeed()
  currentVideo.value = videoList.value.length ? videoList.value[currentIndex.value] : {}
})

watch(() => currentIndex.value, (index)=>{
  // index>=1 且有数据的时候
  if(index >= 1 && videoList.value.length >=2) {
    prevVideo.value = videoList.value[--index]
  }
  if(videoList.value.length <= index+1) {
    nextVideo.value = videoList.value[++index]
  }  

  // 视频即将刷完的时候 就再次请求数据
})

function test() {}

const fetchVideoFeed = () => {
  getVideoFeed().then(res=>{
    let {data, success} = res;
    if(success) {
      videoList.value = data.records;
    }
  })
}

</script>

<style scoped>
.video_list {
  width: 100%;
  height: calc(100vh - 120px);
  overflow-y: hidden;
  transition: transform 0.3s ease;
  border-radius: 10px;
}
.videoComponent {
  height: 100%; 
  width: 100%;
}

</style>

