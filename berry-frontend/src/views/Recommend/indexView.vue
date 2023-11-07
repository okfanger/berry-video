<template>
  <div class="video_list" v-scroll>
    <div v-for="(item,index) in videoList" :key="videoList[index].id">
      <VideoComponent  v-if='index == currentIndex'
        v-model:videoPropsObj="videoList[index]" 
      />
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import VideoComponent from '@/components/VideoComponent'
import { getVideoFeed } from '@/api/video';
import { emitter } from '@/utils';


const videoList = ref([])
const currentIndex = ref(0)


onMounted(() => {
  fetchVideoFeed()
  emitter.on("scrollVideo", (direction) => {
    if(direction == 'down') {
      currentIndex.value++;
      
    } else if(direction == 'up'){
      if(currentIndex.value >= 1) {
        currentIndex.value--;
      }
    }
  })
})

watch(() => currentIndex.value, (index)=>{
  // 视频即将刷完的时候 就再次请求数据
  if(index == videoList.value.length - 2) {
    fetchVideoFeed()
  }
})



const fetchVideoFeed = () => {
  getVideoFeed().then(res=>{
    let {data, success} = res;
    if(success) {

      videoList.value = videoList.value.concat(data.records);
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
.blank {
  height: 646px;
  width: 100%;
}
</style>

