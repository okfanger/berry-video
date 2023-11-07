<template>
  <div>
    <VideoList :list="list" v-model:loading="loading" />
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
const loading = ref(false)
watch(() => props.type, () =>{
  list.value = []
  loading.value = true;
  let data = {
    type: props.type,
  }
  if(props.userId !== 'self') {
    data.authorId = props.userId;
  }
  videoStore.fetchVideoFeedByType(data).then(res=>{
    list.value =  res
    loading.value = false;
  })
}, {
  immediate: true
})


</script>

<style scoped>

</style>