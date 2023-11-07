<template>
  <div class="full-screen-blur-background" v-if="props.visible">
    <div class="content">
      <div class="close-button" @click="closeFullScreen">X</div>
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
const props = defineProps({
  visible: Boolean
})
const emits = defineEmits(["update:visible"])

const closeFullScreen = () => {
  emits("update:visible", false)
}
</script>

<style scoped>
.full-screen-blur-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5); /* 半透明背景 */
  backdrop-filter: blur(5px); /* 背景虚化效果 */
  z-index: 99;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.content {
  width: 90%;
  background: #fff; /* 内容区域背景色，可以根据需要修改 */
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  position: relative;
}

.close-button {
  width: 64px;
  height: 64px;
  background-color: rgba(0, 0, 0, 0.18); 
  border-radius: 50%; 
  border: 0.5px solid rgba(255, 255, 255, 0.15);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 100000;
  color: #1c1f23;
  user-select: none;
  opacity: 0.5;
  font-size: 30px;
  color: #fff;
}

.close-button:hover {
  background-color: #424246; 
  opacity: 1;
}
</style>
