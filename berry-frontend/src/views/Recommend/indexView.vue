<template>
  <div class="video_list" v-scroll>
    <VideoComponent v-for="item,index in list" 
      v-slide-in="test"
      :key="index" class="videoComponent" ref="cards"
      :videoSrc="item.url"
      :likeCount="item.likeCount"
      :id="item.id"
      :liked="item.liked"
      />
 </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VideoComponent from '@/components/VideoComponent'
import { getVideoFeed } from '@/api/video';

const list = ref([])
// const item = {
//   authorId
//   commentCount
//   cover
//   createTime
//   id
//   likeCount
//   liked
//   title
//   updateTime
//   url
//   visible
// }

onMounted(() => {
  fetchVideoFeed()
  // onKeyStroke(['ArrowDown', 'ArrowUp'],(e)=>{
  //   let type = e.key;
  //   if(type == "ArrowDown") {
  //     currentIndex.value++;
  //   } else if(type == "ArrowUp" && currentIndex.value >= 1){
  //     currentIndex.value--;
  //   }
  // })
})
const currentIndex = ref(0)
const cards = ref()
// const current = cards.value[currentIndex.value]
const test = (index) => {
  // console.log('当前视频', index, '     视频总数', list.value.length);
  // 如果当前视频位于倒数第二个 就再请求10个视频
  // if(index == list.value.length -2 ){
  //   // 模拟请求数据
  //   list.value = list.value.concat(list.value)
  // }
    
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

