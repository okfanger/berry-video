<template>
  <div class="list-container">
    <div class="item" v-for="item in props.list" :key="item" @click="showVideo"></div>
    <Modal v-mode:visble="open">
      <videoCom v-if="open" />
    </Modal>
 </div>

 
</template>

<script setup>
import { ref, defineProps } from 'vue'
import videoCom from '@/components/VideoComponent/index.vue'

const props = defineProps(['list'])
const open = ref(false)


const showVideo = (item) => {
  console.log(item);
  open.value = true;
}

</script>

<style scoped lang="scss">
.list-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 创建四列布局 */
  gap: 10px; /* 设置列之间的间距 */
  max-height: calc(100vh - 120px); /* 设置容器最大高度 */
  overflow-y: auto; /* 当内容溢出时在竖直方向上显示滚动条 */

  .item {
    height: 380px;
    width: 260px;
    background: #e0e0e0;
    padding: 10px;
    border: 1px solid #ccc;
    text-align: center;
  }
}

</style>