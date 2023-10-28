<template>
  <div class="mine-container"> 
   
    <div class="info"></div>

    <div class="tabs">
      <div class="tab" v-for="item in tabList" 
      :key="item.key" @click="changeTab(item)"
      :class="{activated: item.key === currentTab}">
        {{ item.name }}
      </div>
    </div>

   <div class="list">
    <product  v-if="currentTab == 'product'"/>
    <upvote  v-if="currentTab == 'upvote'"/>
    <collect  v-if="currentTab == 'collect'"/>
   </div>
   
 </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import product from '@/views/Product/indexView.vue'
import upvote from '@/views/Upvote/indexView.vue'
import collect from '@/views/Collect/indexView.vue'
import create from '@/views/File/updateFile.vue'

const route = useRoute()
const router = useRouter()

const tabList = ref([
  {
    key:"product",
    name:"作品"
  },
  {
    key:"upvote",
    name:"喜欢"
  },
  {
    key:"collect",
    name:"收藏"
  },
])
const currentTab = ref(route.name || 'upvote') // 默认是喜欢列表

const VideoList = ref([])

const changeTab = (e) => {
  router.push({
    name: e.key
  })
  currentTab.value = e.key
  if(currentTab.value === 'create') {
    console.log();
  }
}

const fetchVideoList = () => {

}

</script>

<style scoped>
.love {
  width: 80px;
  
}

.mine-container {
  
}

.info {
  height: 120px;
}
.tabs {
  display: flex;
}
.tab {
  width: 76px;
  color: #1c1f23;
  font-weight: 400;
  font-size: 19px;
  cursor: pointer;
}
.activated {
  color: #ec656b;
}
.list {
  height: 200px;
  width: 100%;
  background-color: #f7f9fa;
}
</style>