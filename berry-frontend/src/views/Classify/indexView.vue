<template>
  <div class="classify-container">
    <VideoList :list="channelVideoList" />
 </div>
</template>

<script setup>
import { onMounted, watch,ref } from 'vue'
import { useRoute } from 'vue-router'
import { getVideoFeed } from '@/api/video'
import { videoStore } from '@/store'
import VideoList from '@/components/VideoComponent/VideoList.vue'

const route = useRoute()

const channelList = ref(videoStore.channelList || [])
const channelVideoList = ref([])
const channelId = ref(0)

onMounted(() => {
  fetchChannelVideoList();
})

watch(() => route, (value) =>{
  // 使用route 找到
  let icon = value.meta.MenuIcon
  let channel = channelList.value.find(item => item.icon == icon) || {}
  channelId.value =  channel.id
  fetchChannelVideoList()
}, {
  deep: true
})




const fetchChannelVideoList = async () => {
  console.log(channelList);
  console.log(channelId.value);
  let res = await getVideoFeed(channelId.value)
  const { success, data} = res;
  if(success) {
    channelVideoList.value = data.records;
  }
}

</script>

<style scoped>

</style>