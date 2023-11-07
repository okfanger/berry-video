<template>
  <div class="container">
    <div class="initial-video" ref="videoBox" :style="{backgroundImage: `url(${props.cover})` }">
      <video ref="video"
        style="z-index: 3"
        webkit-playsinline="true"
        playsinline="true"
        controlslist="nodownload"
        :poster="props.cover"
        crossorigin="anonymous">
        <source :src="url" type="video/mp4">
          您的浏览器不支持video标签，请使用google浏览器浏览
      </video>
    </div>

    <div class="video-info">
      <div class="createtime">{{ fromTime(props.createTime) }}</div>
      <div class="content">{{ props.content }}</div>
    </div>

    <div class="video-controls">
      <div class="progress-bar" ref="progressBox" @click="checkAnyTime" @mousedown="handleMousedown">
        <div class="progressLine" ref="progressLine" :style="{width: progress * 100 + '%'}"></div>
        <div class="draggable-point" :style="{left: progress * 100 + '%'}" ></div>
      </div>
      <div class="top">
        <div class="left">
          <div class="play-btn" @click="play">
            <i class="iconfont icon-bofangqi-zantingxiaodianshi setting" v-if="!paused"></i>
            <i class="iconfont icon-bofangqi-bofangxiaodianshi setting" v-else></i>
            <span class="time">{{ progressTime }}</span>
          </div>
        </div>
        <div class="right">
          <div @click="mute"  style="display: flex;">
            <i class="iconfont icon-1 setting" v-if="!muted"></i>
            <i class="iconfont icon-jingyinmute31 setting" style="font-size: 14px;" v-else></i>
          </div>
          <div>
            <el-dropdown :hide-on-click="false">
              <span class="el-dropdown-link">{{ speed == '1' ? '倍速' : `${speed}x` }}</span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-for="item in speedList" :key="item" @click="changeSpeed(item)">{{ item }}x</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div>
            <button v-if="openPrintScreen" @click="openPrintScreen" style="font-size: 14px;">截图</button>
          </div>
          <div>
            <i class="setting iconfont icon-quanpingmu" style="font-size: 14px;" @click="FullScreen"></i>
          </div>
        </div>
      </div>
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, defineEmits, defineProps, onBeforeUnmount } from 'vue'
import { muted } from '@/utils'
import Hls from 'hls.js';
import { fromTime } from '@/utils'
const emits = defineEmits(['play', 'mute', 'update:speed',
   'printscreen', 'videoEnd', "likeOrDisLike",
  'upadte:volume'])

const props = defineProps(['volume', 'speed', 'speedList',
   'continuous', 'openPrintScreen', 'url', 'cover',
    'streamLoad', 'id', 'content', 'createTime'])

const video = ref()
const progressBox = ref()
const progressLine = ref() // 视频进度条
const progress = ref(0)  // 视频当前进度
const dragging = ref(false)  // 是否正在拖拽状态
let progressTimer = null // 进度 timer

const paused = ref(false) // true 暂停  false 播放
const videoBox = ref()
const progressTime = ref("00:00 / 00:00")
let hls;
let clickTimer = null;
onMounted(() => {
  initVideo();
  if (video.value.videoWidth > video.value.videoHeight) {
      video.value.classList.add('widthGreaterThanHeight');
  } else {
    video.value.classList.add('heightGreaterThanWidth');
  }
  window.addEventListener('mousemove', handleMousemove)
  window.addEventListener('mouseup', handleMouseup)
  // 监听视频进度条拖拽 全屏后进度条拖拽,自定义进度条也要到同样位置
  video.value.addEventListener('timeupdate', ()=>{
    changeProgress()
  })
  // 单点视频 播放/暂停
  videoBox.value.addEventListener("click", ()=>{
    clickTimer && clearTimeout(clickTimer);
    clickTimer = setTimeout(()=>{
      play()
    }, 300)
  })
  // 双击视频 点赞
  videoBox.value.addEventListener("dblclick", (e)=>{
    clickTimer && clearTimeout(clickTimer);
    emits("likeOrDisLike")
  })
});

const initVideo = () => {
  hls = new Hls();
  hls.loadSource(props.url);
  hls.attachMedia(video.value);
  video.value.currentTime = 0;
  muted.value = video.value.muted = props.volume == 0 ? true : false;
  video.value.playbackRate = props.speed;
}

const parseTime = (value) => {
    if (!value) return '00:00'
    let interval = Math.floor(value)
    let minute = (Math.floor(interval / 60)).toString().padStart(2, '0')
    let second = (interval % 60).toString().padStart(2, '0')
    return `${minute}:${second}`
}
// 推进进度条
function changeProgress() {
  if(video.value) {
    var timeStr = parseTime(video.value.currentTime)  + '/' + parseTime(video.value.duration)
    progressTime.value = timeStr
    var percent = video.value.currentTime / video.value.duration
    if(!dragging.value){
      progress.value = Math.min(Math.max(percent, 0), 1); // 让进度条在0-1之间
    }
    progressLine.value.style.width = percent * 100 + '%'
  }
}
// 点击进度条的任意地方
const checkAnyTime = (e) => {
  clearInterval(progressTimer)
  var length = e.clientX - progressBox.value.offsetLeft - 200;
  var percent = length / progressBox.value.offsetWidth
  video.value.currentTime = percent * video.value.duration
  video.value.play()
  paused.value = false
  progressTimer = setInterval(changeProgress, 60)
}

// 重置视频播放器
const clearVideo = () => {
  paused.value = true;
  video.value.pause();
  video.value.muted = false;
  clearInterval(progressTimer)
  progressTimer = null;
}

// 播放&暂停点击
const play = () => {
  if (video.value.paused) {
    video.value.play();
    progressTimer = setInterval(changeProgress, 60)
  } else {
    video.value.pause();
    clearInterval(progressTimer)
    progressTimer = null;
  }
  paused.value = video.value.paused;
  emits("play", !paused.value)
}

// 静音
const mute = () => {
  let mute = video.value.muted;
  muted.value = !mute
  video.value.muted = muted.value;
  emits("mute", mute)
}

// 调整播放速度
const changeSpeed = (value) => {
  video.value.playbackRate = value;
  emits("update:speed", value)
}

// 视频截图
const openPrintScreen = () => {
  const canvas = document.createElement("canvas")
  let ctx = canvas.getContext("2d")
  let width = video.value.width
  let height = video.value.height
  console.log(width, height);
  canvas.width = width;
  canvas.height = height;
  ctx.drawImage(video.value, 0, 0, width, height)

  let imgUrl = canvas.toDataURL("image/png")
  const a = document.createElement("a")
  a.href = imgUrl;
  a.download = `shumaipai`;
  a.click();
}

// 全屏
const FullScreen = () => {
  if (video.value.requestFullscreen) {
		video.value.requestFullscreen()
	} else if (video.value.webkitRequestFullScreen) {
		video.value.webkitRequestFullScreen()
	} else if (video.value.mozRequestFullScreen) {
		video.value.mozRequestFullScreen()
	}
}

// 视频结束
const videoEnd = () => {
  // 如果连播就调用play  否则通知父组件切换下一个视频
  if(props.continuous) {
    play()
  } else {
    clearVideo()
    // emits("videoEnd", true)
  }
}


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

  const progressBar = progressBox.value
  const rect = progressBar.getBoundingClientRect()
  const newProgress = (e.clientX - rect.left) / rect.width

  progress.value = Math.min(Math.max(newProgress, 0), 1) // 保证进度在0-1之间

  video.value.currentTime = video.value.duration * progress.value
}

onBeforeUnmount(() => {
  if (hls) {
    hls.destroy();
  }
  window.removeEventListener('mousemove', handleMousemove)
  window.removeEventListener('mouseup', handleMouseup)
});
</script>

<style>
.video-box {
  width: 100%;
  height: auto;
}

.ctrl-box {
  width: 100%;
  margin: 0 auto;
}
.ctrl-box .progress-box {
  position: relative;
  height: 4px;
  margin-bottom: 10px;
  background: rgba(10, 183, 102, 0.1);
  border-radius: 2px;
  border: 1px solid #ff2e56;
  overflow: hidden;
  cursor: pointer;
}
.ctrl-box .progress-box .progress {
  position: absolute;
  top: 0;
  left: 0;
  width: 0%;
  height: 100%;
  background: #ff2e56;
}
.ctrl-box .progress-time {
  display: inline-block;
}
</style>



<style scoped>
.el-dropdown-link {
  font-family: "SimSun", sans-serif;
}
.container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.initial-video {
  width: 100%;
  height: 100%;
  background-repeat: no-repeat;
  background-position: center center;
  background-color: rgba(0, 0, 0, 0.5);
  background-size: 100% 100%;
  flex: 1;
  display: flex;   
  justify-content: center; 
  align-items: center; 
  backdrop-filter: blur(30px);
}


video.widthGreaterThanHeight {
  height: auto;
  width: 100%;
}
video.heightGreaterThanWidth {
  height: 100%;
  width: auto;
}

.blur {    
    -webkit-filter: blur(10px);
       -moz-filter: blur(10px);
        -ms-filter: blur(10px);    
            filter: blur(10px);  
}
</style>
<!-- 定义颜色 -->
<style>
:root {
  /* --style-color: #3a4544;
  --back-color: #fff;
  --progress-color: #b3b3b3; */


  --style-color: #fbfcfd;
  --back-color: #eb4553;
  --progress-color: #f66c75;
}
</style>

<style>

/* 控制条容器 */
.video-controls {
  position: absolute;
  bottom: 0;
  width: 100%;
  background: var(--back-color);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex-wrap: nowrap;
  border-radius: 0 0 0px 8px;
  opacity: 1;
  visibility: visible;
  transition: opacity 0.3s, visibility 0.3s;
  user-select: none;
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
  padding: 0 8px;
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

/* 进度条 */
.video-controls .progress-bar {
  flex-grow: 1;
  height: 5px;
  background: var(--progress-color);
  opacity: 0.5;
  position: relative;
  cursor: pointer;
}
.video-controls .progress-bar:hover {
  background-color: var(--progress-color);
  opacity: 1;
}
.video-controls .progress-bar:hover .draggable-point{
  opacity: 1;
  visibility: visible;
}

.video-controls .progress-bar .progressLine {
  position: absolute;
  height: 100%;
  background: var(--style-color);
}

.draggable-point {
  width: 12px;
  height: 12px;
  background-color: var(--style-color);
  border-radius: 50%;
  position: absolute;
  top: 50%;
  transform: translate(-2px, -50%);
  cursor: pointer;
  visibility: hidden;
  opacity: 0;
  transition: opacity .3s;
}

.setting {
  font-size: 20px;
  color: white;
}
.time {
  margin-left: 5px;
  font-size: 15px;
}


.right {
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
}
.right > div {
  min-width: 40px;
  display: flex;
  flex-wrap: nowrap;
  justify-content: center;
}
</style>


<style scoped>
::v-deep .el-tooltip__trigger {
  color: white;
}
</style>


<style scoped>
.heart {
  position: relative;
  width: 30px;
  height: 24px;
  background-color: #ff6b81;  /* 你可以选择你喜欢的颜色 */
  transform: rotate(-45deg);
  margin-top: 15px;
}

.heart::before,
.heart::after {
  content: "";
  position: absolute;
  top: -15px;
  width: 30px;
  height: 30px;
  background-color: #ff6b81;  /* 与爱心的其他部分颜色相同 */
  border-radius: 50%;
}

.heart::before {
  left: -15px;
}

.heart::after {
  right: -15px;
}

</style>


<style lang="scss" scoped>
.video-info {
  position: absolute;
  bottom: 40px;
  padding: 0 0 0 10px;
  user-select: none;

  .createtime {
    font-size: 12px;
    line-height: 22px;
    color: #eeeeed;
  }
  .content {
    font-size: 14px;
    line-height: 22px;
    text-shadow: 0 1px 1px rgba(0,0,0,.2);
    color: white;
  }
}
</style>




