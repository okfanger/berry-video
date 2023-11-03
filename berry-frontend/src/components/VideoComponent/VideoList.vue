<template>
  <div class="list-container" v-show="!open">
    <div class="item shadow" v-for="item in props.list" :key="item.id" @click="showVideo(item)" >
      <div class="video">
        <img :src="item.cover" alt="">
      </div>
      <div class="content">
        {{ item.content }} 
      </div>
    </div>
 </div>

 <!-- 全屏 背景虚拟化 -->
 <!-- :cover="currentVideo.cover" -->
 <FullScreenModel v-model:visible="open">
  <videoCom v-if="open"
    :videoSrc="currentVideo.url"
    :likeCount="currentVideo.likeCount"
    :id="currentVideo.id"
    :liked="currentVideo.liked"
    :favorCount="currentVideo.favorCount"
    :favored="currentVideo.favored"
    :commentCount="currentVideo.commentCount"
    :author-avatar="currentVideo.author.authorAvatar"
    :content="currentVideo.content"
  />
 </FullScreenModel>
    
</template>

<script setup>
import { ref, defineProps } from 'vue'
import videoCom from '@/components/VideoComponent/index.vue'
import FullScreenModel from './FullScreenModel.vue';

const props = defineProps(['list'])
const open = ref(false)
const currentVideo = ref({})


const showVideo = (item) => {
  currentVideo.value = item;
  open.value = true;
}

</script>

<style scoped lang="scss">
.list-container {
  display: grid;
  grid-template-columns: repeat(6, 1fr); 
  gap: 10px; 
  max-height: calc(100vh - 120px); 
  // overflow-y: auto; 

  .item {
    border-radius: 12px;
    overflow: hidden;
    aspect-ratio: 0.68;
    display: flex;
    flex-direction: column;
    cursor: pointer;
    background-color: transparent;

    

    .content {
      height: 51px;
      width: 100%;
      line-height: 25px;
      overflow: hidden;
      -webkit-line-clamp: 2;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      color: #3e3c3c;
      height: 51px;
      font-weight: bold;
    }
    .video {
      flex: 1;
      overflow: hidden;
      width: 100%;
      height: 100%;
    }
    .video img{
      width: 100%;
      height: 100%;
      transition: transform 0.3s;
    }
    .video img:hover{
      transform: scale(1.1);
    }
  }
}

.shadow {
  box-shadow:
  3.2px 4.4px 44.3px -95px rgba(0, 0, 0, 0.71),
  14px 19px 354px -95px rgba(0, 0, 0, 0.519);
}

@media (max-width: 1260px) {
  .list-container  {
    grid-template-columns: repeat(3, 1fr);
  }
  .item {
    aspect-ratio: 0.78;
  }
}
</style>