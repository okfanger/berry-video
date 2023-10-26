<template>
  <div class="video_list" v-scroll>
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


</script>

<style scoped>
.video_list {
  width: 100%;
  height: calc(100vh - 120px);
  overflow-y: hidden;
  transition: transform 0.3s ease;
}
.videoComponent {
  height: 100%; 
  width: 100%;
}

</style>