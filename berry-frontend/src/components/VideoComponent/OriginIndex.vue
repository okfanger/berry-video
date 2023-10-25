<template>
  <div class="container">
    <div class="initial-video">
      <video ref="video"
        crossorigin="anonymous">
        <source :src="videoSrc" type="video/mp4">
        您的浏览器不支持video标签，请使用google浏览器浏览
      </video>
    </div>

    <div class="controls">
      <div class="ctrl-box">
        <!-- 进度条 -->
        <div class="progress-box" ref="progressBox" @click="checkAnyTime">
          <div class="progress" ref="progress"></div>
        </div>
      </div>
      <div class="settings">
        <div class="left">
          <div class="play-btn" @click="play">
            <i class="iconfont icon-bofangqi-zantingxiaodianshi setting" v-if="!paused"></i>
            <i class="iconfont icon-bofangqi-bofangxiaodianshi setting" v-else></i>
            <span class="progress-time">{{ progressTime }}</span>
          </div>
          
        </div>
        <div class="right">
          <div @click="mute">
            <i class="iconfont icon-1 setting" v-if="!muted"></i>
            <i class="iconfont icon-jingyinmute31 setting" style="font-size: 17px;" v-else></i>
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
            <button v-if="openPrintScreen" @click="openPrintScreen">截图</button>
          </div>
          <div>
            是否开启连播 <button>1</button>
          </div>
          <div>
            <i class="setting iconfont icon-quanpingmu" @click="FullScreen"></i>
          </div>
        </div>
      </div>
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, defineEmits, defineProps } from 'vue'

const emits = defineEmits(['play', 'mute', 'changeSpeed',
   'printscreen', 'videoEnd'])

const props = defineProps(['volume', 'speed', 'speedList',
   'continuous', 'openPrintScreen', 'videoSrc', 'coverSrc',
    'bgImg', 'streamLoad', ])

const video = ref()
const progressBox = ref()
const progress = ref()
let progressTimer = null // 进度 timer

const paused = ref(false) // true 暂停  false 播放
const muted = ref(false) // true 静音  false 开启声音
const progressTime = ref("00:00/00:00")
onMounted(() => {
  initVideo()
});

const initVideo = () => {
  // video.value.play();
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
  var timeStr = parseTime(video.value.currentTime)  + '/' + parseTime(video.value.duration)
  progressTime.value = timeStr
  var percent = video.value.currentTime / video.value.duration
  if(percent >= 1) {
    videoEnd();
  }
  progress.value.style.width = percent * 100 + '%'
}
// 点击进度条的任意地方
const checkAnyTime = (e) => {
  clearInterval(progressTimer)
  var length = e.clientX - progressBox.value.offsetLeft
  var percent = length / progressBox.value.offsetWidth
  video.value.currentTime = percent * video.value.duration
  console.log(e.clientX, progressBox.value.offsetWidth, length, );
  video.value.play()
  progressTimer = setInterval(changeProgress, 60)
  // 显示视频在播放的样式
  // ...
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
  emits("changeSpeed", value)
}

// 视频截图
const openPrintScreen = () => {
  const canvas = document.createElement("canvas")
  let ctx = canvas.getContext("2d")
  canvas.width = video.value.width;
  canvas.height = video.value.height;
  ctx.drawImage(video.value, 0, 0)

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
    emits("videoEnd", true)
  }
}
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
  background: rgba(0, 0, 0, 0.1);
  border-radius: 2px;
  border: 1px solid rgb(131, 175, 155);
  overflow: hidden;
  cursor: pointer;
}
.ctrl-box .progress-box .progress {
  position: absolute;
  top: 0;
  left: 0;
  width: 0%;
  height: 100%;
  background: rgb(131, 175, 155);
}
.ctrl-box .progress-time {
  display: inline-block;
}
</style>



<style>
.container {
  width: 100%;
  height: 100%;
  background-color: wheat;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.initial-video {
  width: 100%;
  height: calc(100% - 40px);
  background-color: #000;
}
.initial-video video {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

.controls {
  height: 40px;
  display: flex;
  flex-direction: column;
  padding: 0 10px;
  user-select: none;
}
.controls .settings {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.controls .settings .right {
  display: flex;
  min-width: 200px;
  justify-content: space-between;
  align-items: center;
}
.right div {
  min-width: 50px;
  height: 24px;
  line-height: 24px;
}



i.setting {
  font-size: 20px;
  cursor: pointer;
}
</style>



