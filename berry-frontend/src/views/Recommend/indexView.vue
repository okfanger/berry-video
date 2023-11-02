<template>
  <div class="video_list" v-scroll>
    <VideoComponent v-for="item,index in videoList" 
      v-slide-in="test"
      :key="index" class="videoComponent"
      :videoSrc="item.url"
      :likeCount="item.likeCount"
      :id="item.id"
      :liked="item.liked"
      :favorCount="item.favorCount"
      :isFavored="item.isFavored"
      :commentCount="item.commentCount"
      />
 </div>
 <!-- <div class="video_list" >
  <vue-virtual-scroller class="video-list" :items="videoList" :item-height="height">
    <template #default="{ item: item, index }">
      <video-component 
        :key="index" class="videoComponent"
        :videoSrc="item.url"
        :likeCount="item.likeCount"
        :id="item.id"
        :liked="item.liked"
        :favorCount="item.favorCount"
        :isFavored="item.isFavored"
        :commentCount="item.commentCount"
      />
    </template>
  </vue-virtual-scroller>
 </div> -->
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VideoComponent from '@/components/VideoComponent'
import VueVirtualScroller from 'vue3-virtual-scroller'
import 'vue3-virtual-scroller/dist/vue3-virtual-scroller.css'
import { getVideoFeed } from '@/api/video';

const videoList = ref([])
const height = ref(800)

onMounted(() => {
  fetchVideoFeed()
})
const test = () => {}
const currentIndex = ref(0)

console.log(VueVirtualScroller);
const fetchVideoFeed = () => {
  getVideoFeed().then(res=>{
    let {data, success} = res;
    if(success) {
      videoList.value = data.records;
      console.log(videoList);
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

