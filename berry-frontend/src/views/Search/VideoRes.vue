<template>
  <div>
    <div v-for="item, index in videoList" :key="index">
      <VideoComponent 
        :video-props-obj="item"
        class="video-item"
      />
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { emitter } from '@/utils';
import { useRoute } from 'vue-router'
import VideoComponent from '@/components/VideoComponent/index.vue';
import {searchAggregateApi} from '@/api/search'

const route = useRoute()
const data = reactive({
  keyword: route.query.searchKey,
  current: 1
})
const videoData = ref({})
const videoList = ref([])

onMounted(()=>{
  emitter.on("search", (keyword) =>{
    data.keyword = keyword;
    fetchVideoData()
  })
  fetchVideoData()
})

function fetchVideoData () {
  searchAggregateApi(data).then(res=>{
    if(res.success) {
      const {video} = res.data
      videoData.value = video;
      videoList.value = videoData.value.records;
    }
  })
}

</script>

<style scoped>
.video-item {
  margin: 0 0 20px;
}
</style>