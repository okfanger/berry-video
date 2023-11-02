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
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.content {
  height: 90%;
  width: 90%;
  background: #fff; /* 内容区域背景色，可以根据需要修改 */
  border-radius: 10px;
  padding: 20px 10px 0 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  position: relative;
}

.close-button {
  width: 40px;
  height: 40px;
  background-color: #ee7b87; /* 设置按钮背景颜色 */
  border-radius: 50%; /* 使按钮呈现圆形 */
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: absolute;
  top: 40px;
  left: 40px;
  z-index: 100000;
  color: #1c1f23;
  user-select: none;
}

/* 可以自定义按钮的样式，如颜色和图标 */
.close-button:hover {
  background-color: #ec656b; /* 鼠标悬停时的颜色 */
  color: #fff; /* 文本颜色 */
}
</style>
