<template>
  <div class="classify-container">
    <VideoList :list="channelList" />
 </div>
</template>

<script setup>
import { onMounted, watch,ref } from 'vue'
import { useRoute } from 'vue-router'
import { getChannelList, getChannelFeedByChannelIdApi } from '@/api/video'
import VideoList from '@/components/VideoComponent/VideoList.vue'

const route = useRoute()

const channelList = ref([])
const channelVideoList = ref([])
const channelId = ref(0)

onMounted(() => {
  fetchChannelList();
  fetchChannelVideoList();
})

watch(() => route, (value) =>{
  // 使用route 找到
  let icon = value.meta.MenuIcon
  let channel = channelList.value.find(item => item.icon == icon) || {}
  channelId.value =  channel.id
}, {
  immediate: true,
  deep: true
})


const fetchChannelList = () => {
  getChannelList(channelId.value).then(res=>{
    const {status, data} = res;
    if(status) {
      channelList.value = data;
    }
  })
}

const fetchChannelVideoList = async () => {
  let res = await getChannelFeedByChannelIdApi(channelId.value)
  console.log(res);
}

</script>

<style scoped>

</style>