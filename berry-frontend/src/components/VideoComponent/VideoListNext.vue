<template>
  <div class="list-container">
    <div class="card-item" v-for="item in props.list" :key="item.id">
      <img :src="item.cover" alt="">
      <div class="content">{{ item.content }}</div>
    </div>
 </div>
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
 
  @media (max-width: 1800px) {
   column-count: 5;
  }
  @media (max-width: 1260px) {
   column-count: 4;
  }
  @media (max-width: 1000px) {
   column-count: 3;
  }
  width: 100%;
  
  // display: grid;
  // grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  // gap: 10px;

  .card-item {
    background: #fff;
    border-radius: 5px;
    margin-bottom: 20px;
    height: auto;
    

    img {
      width: 100%;
      display: block;
      border-radius: 12px;
      margin-bottom: 0.5rem;
    }

    .content {
      padding: 10px;
      font-size: 16px;
      display: block;
      text-overflow: ellipsis;
      overflow: hidden;
    }
  }
}


</style>
