<template>
  <div class="search-container">
    <div class="tabs">
      <div class="tab" v-for="tab in tabs" 
        :class="{actived: currentTab == tab.key}"
        :key="tab.key" @click="changeTab(tab)">
        {{ tab.value }}
      </div>
    </div>

    <div class="info">
      <VideoRes v-show="currentTab == 'video'" />
      <UserRes v-show="currentTab == 'user'" />
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted,reactive } from 'vue';
import { emitter } from '@/utils'
import UserRes from './UserRes.vue';
import VideoRes from './VideoRes.vue';

const tabs = reactive([
  {key:"video", value: "视频"},
  {key:"user", value: "用户"},
])
const currentTab = ref("video")

onMounted(() => {
  emitter.on("resultList", (val) =>{
    console.log(val);
  })
})

const changeTab = (tab) => {
  currentTab.value = tab.key;
}


</script>

<style scoped lang="scss">
.search-container {
  width: 100%;
  height: calc(100vh - 120px);
  overflow: auto;

  .tabs {
    display: flex;
    border-bottom: 1px solid #e2e2e2;
    padding: 0 00 10px 0;
    margin-bottom: 10px;
    background-color: #fffbfe;
    .tab {
      width: 76px;
      color: #1c1f23;
      font-weight: 400;
      font-size: 17px;
      cursor: pointer;

      &.actived {
        color: #ec656b;
      }
    }
  }

}
</style>