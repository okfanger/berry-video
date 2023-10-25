<template>
  <div class="video_list" @wheel="handlerScroll">
    <VideoComponent v-for="item,index in list" 
    :key="index" class="videoComponent" ref="cards"/>
 </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VideoComponent from '@/components/VideoComponent'
import { onKeyStroke } from '@vueuse/core'
const list = ref([
  {},{},{},{},{}
])
onMounted(() => {
  onKeyStroke(['ArrowDown', 'ArrowUp'],(e)=>{
    let type = e.key;
    if(type == "ArrowDown") {
      currentIndex.value++;
    } else if(type == "ArrowUp" && currentIndex.value >= 1){
      currentIndex.value--;
    }
  })
})
const currentIndex = ref(0)
const cards = ref()
// const current = cards.value[currentIndex.value]

const handlerScroll = (e) => {
  // console.log(e, e.deltay);
  // const direction = e.deltaY > 0 ? 1 : -1;
  // currentIndex.value += direction;
  // if (currentIndex.value < 0) currentIndex.value = 0;
  // if (currentIndex.value >= list.value.length) currentIndex.value = list.value.length - 1;
  // console.log(current);
  // // container.style.transform = `translateY(-${currentIndex.value * 100}vh)`;
  // e.preventDefault();
}

</script>

<style scoped>
.video_list {
  width: 100%;
  height: calc(100vh - 120px);
  overflow-y: scroll;
  position: relative;
  transition: transform 0.3s ease;
}
.videoComponent {
  height: 100%; 
  /* position: absolute;  */
  width: 100%;
  top: 0;
}

</style>