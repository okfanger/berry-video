<template>
  <div>
    <VideoList :list="list" />
 </div>
</template>

<script setup>
import { ref, defineProps,watch } from 'vue'
import { videoStore } from '@/store'
import VideoList from '@/components/VideoComponent/VideoList.vue';
const props = defineProps({
  type: String,
})
const list = ref([])

watch(() => props.type, () =>{
  list.value = []
  videoStore.fetchVideoFeedByType(props.type).then(res=>{
    list.value =  res
  })
}, {
  immediate: true
})


</script>

<style scoped>

</style>