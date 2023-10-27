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
            <div class="progress" style="width: 50%;"></div> <!-- For demonstration, it's set to 50%. In a real scenario, it should reflect the video's playback progress -->
        </div>
    </div>
 </div>
</template>

<script setup>

</script>

<style>
:root {
  /* --style-color: #3a4544;
  --back-color: #fff;
  --progress-color: #b3b3b3; */


  --style-color: #fbfcfd;
  --back-color: #ff4349;
  --progress-color: #c03035;
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

</style>