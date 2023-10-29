<template>
  <div>
    <div class="video-controls">
        <div class="top">
          <div class="left">
          <button >▶︎ ❚❚</button> 
            <div class="time">1:10 / 3:00</div>
          </div>
          <div class="right">
            <button >🔊</button> 
          </div>
        </div>
        <div class="progress-bar">
          <div class="progress" :style="{width: progress * 100 + '%'}"></div>
          <div class="draggable-point" :style="{left: progress * 100 + '%'}" @mousedown="handleMousedown"></div>
        </div>
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, defineEmits, defineProps } from 'vue'
const emits = defineEmits([])
const props = defineProps([])

const progress = ref(0.5) // 初始进度设置为50%
const dragging = ref(false)

// 处理拖拽开始
const handleMousedown = () => {
  dragging.value = true
}

// 处理拖拽结束
const handleMouseup = () => {
  dragging.value = false
}

// 处理拖拽
const handleMousemove = (e) => {
  if (!dragging.value) return

  const progressBar = document.querySelector('.progress-bar')
  const rect = progressBar.getBoundingClientRect()
  const newProgress = (e.clientX - rect.left) / rect.width

  progress.value = Math.min(Math.max(newProgress, 0), 1) // 保证进度在0-1之间
}

onMounted(() => {
  window.addEventListener('mousemove', handleMousemove)
  window.addEventListener('mouseup', handleMouseup)
})

onUnmounted(() => {
  window.removeEventListener('mousemove', handleMousemove)
  window.removeEventListener('mouseup', handleMouseup)
})

</script>

<style>
:root {
  /* --style-color: #3a4544;
  --back-color: #fff;
  --progress-color: #b3b3b3; */


  --style-color: #fbfcfd;
  --back-color: #ec656b;
  --progress-color: #ee7b87;
}
</style>

<style scoped>

/* 控制条容器 */
.video-controls {
  position: absolute;
  bottom: 0;
  width: 100%;
  background: var(--back-color);
  padding: 8px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex-wrap: nowrap;
  border-radius: 0 0 8px 8px;
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.3s, visibility 0.3s;
}

/* 鼠标悬停时显示控制条 */
.video-container:hover .video-controls {
  opacity: 1;
  visibility: visible;
}

.top {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.video-controls .left {
  display: flex;
  align-items: center;
  font-weight: bold;
  color: var(--style-color);
}

/* 播放、暂停、音量等按钮 */
.video-controls button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
  color: var(--style-color);
  border-radius: 50%;
  padding: 6px 12px;
  transition: background-color 0.3s;
}

.video-controls button:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

/* 进度条 */
.video-controls .progress-bar {
  flex-grow: 1;
  height: 4px;
  background: var(--progress-color);
  border-radius: 2px;
  margin: 0 12px;
  position: relative;
}

.video-controls .progress-bar .progress {
  position: absolute;
  height: 100%;
  background: var(--style-color);
  border-radius: 2px;
}

.draggable-point {
  width: 12px;
  height: 12px;
  background-color: var(--style-color);
  border-radius: 50%;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
}


</style>