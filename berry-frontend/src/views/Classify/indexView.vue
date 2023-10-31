<template>
  <div class="classify-container">
    <div class="card" v-for="item in 12" :key="item" @click="showVideo(item)">
      <!-- <VideoNext /> -->
      分类视频 {{ route.meta.MenuName }}
    </div>
    <Modal v-mode:visble="open">
      <videoCom v-if="open" />
    </Modal>
 </div>
</template>

<script setup>
import videoCom from '@/components/VideoComponent/index.vue'

import { onMounted, watch,ref } from 'vue'
import { useRoute } from 'vue-router'
import { getChannelList } from '@/api/video'
const route = useRoute()

const channelList = ref([])
const channelId = ref(0)
const open = ref(false)

onMounted(() => {
  fetchChannelList()
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

const showVideo = (item) => {
  console.log(item);
  open.value = true;
}



const fetchChannelList = () => {
  getChannelList(channelId.value).then(res=>{
    const {status, data} = res;
    if(status) {
      channelList.value = data;
    }
  })
}

</script>

<style scoped>
.classify-container {
  height: calc(100vh - 120px);
  width: 100%;
  display: flex;
  overflow-y: scroll;
  flex-wrap: wrap;
  position: relative;
}
.card {
  height: 380px;
  width: 260px;
}
</style>