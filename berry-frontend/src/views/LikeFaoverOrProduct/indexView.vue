<template>
  <div>
    <VideoList :list="list" />
    <!-- <VideoListNext :list="list"/> -->
 </div>
</template>

<script setup>
import { ref, defineProps,watch } from 'vue'
import { videoStore } from '@/store'
import VideoList from '@/components/VideoComponent/VideoList.vue';
const props = defineProps({
  type: String,
  userId: String
})
const list = ref([])
watch(() => props.type, () =>{
  list.value = []
  let data = {
    type: props.type,
  }
  if(props.userId !== 'self') {
    data.authorId = props.userId;
  }
  videoStore.fetchVideoFeedByType(data).then(res=>{
    list.value =  res
  })
}, {
  immediate: true
})


</script>

<style scoped>

</style>