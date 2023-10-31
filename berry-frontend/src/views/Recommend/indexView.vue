<template>
  <div class="video_list" v-scroll>
    <VideoComponent v-for="item,index in list" 
      v-slide-in="test"
      :key="index" class="videoComponent" ref="cards"
      :videoSrc="item.url"
      :likeCount="item.likeCount"
      :id="item.id"
      :liked="item.liked"
      :favorCount="item.favorCount"
      :isFavored="item.isFavored"
      :commentCount="item.commentCount"
      
      />
 </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VideoComponent from '@/components/VideoComponent'
import { getVideoFeed } from '@/api/video';

const list = ref([])

onMounted(() => {
  fetchVideoFeed()
})
const currentIndex = ref(0)
const cards = ref()
const test = (index) => {
 
    
}

const fetchVideoFeed = () => {
  getVideoFeed().then(res=>{
    let {data, success} = res;
    if(success) {
      list.value = data.records;
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

