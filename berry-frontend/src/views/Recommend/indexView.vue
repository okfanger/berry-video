<template>
  <div class="video_list" v-scroll>
    <VideoComponent v-for="item,index in list" 
      v-slide-in="test"
      :key="index" class="videoComponent" ref="cards"
        :videoSrc="item.src"
      />
 </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VideoComponent from '@/components/VideoComponent'
import { getVideoFeed } from '@/api/video';

const list = ref([
  {
    // 其他参数省略号
    src: "sysndhy.flv",
    pused: false,
  },
  {
    // 其他参数省略号
    src: "http://toffee-private-oss.akfang.cn/4585_1697538264.mp4",
    pused: false,
  },
  {
    // 其他参数省略号
    src: "sysndhy.flv",
    pused: false,
  },
  {
    // 其他参数省略号
    src: "http://toffee-private-oss.akfang.cn/4585_1697538264.mp4",
    pused: false,
  },
  {
    // 其他参数省略号
    src: "sysndhy.flv",
    pused: false,
  },
  {
    // 其他参数省略号
    src: "http://toffee-private-oss.akfang.cn/4585_1697538264.mp4",
    pused: false,
  },
  {
    // 其他参数省略号
    src: "sysndhy.flv",
    pused: false,
  },
])

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
  if(index == list.value.length -2 ){
    // 模拟请求数据
    list.value = list.value.concat(list.value)
  }
    
}

const fetchVideoFeed = () => {
  getVideoFeed().then(res=>{
    console.log(res);
  })
}

</script>

<style scoped>
.video_list {
  width: 100%;
  height: calc(100vh - 120px);
  overflow-y: hidden;
  transition: transform 0.3s ease;
  border-radius: 20px;
}
.videoComponent {
  height: 100%; 
  width: 100%;
}

</style>