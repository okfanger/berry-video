<template>
  <div>
    <div class="upload" ref="upload">
      <input type="file" ref="fileInput" accept="video/*">
    </div>
    <div class="icon center" v-if="!beginUpload" ref="icon">
      <el-icon> <Plus /> </el-icon>
    </div>
    <div class="progress center" v-else>
      <div class="bar">
        <div class="progress-bar" ref="progressBox">
          <div class="progressLine" ref="progressLine" :style="{width: percent + '%' }"></div>
        </div>
        <div class="percent">{{ percent }}%</div>
        <div class="result" v-show="haveResult">
          <span v-if="result">✅</span>
          <span v-else>❌</span>
        </div>
      </div>
      <div style="width: 100px;text-align: center;cursor: pointer;margin: 0 auto;" 
        class="button-plain"
        @click="reUpload">重新上传</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineEmits } from 'vue';
import { Plus } from '@element-plus/icons-vue'
import { getUpTokenApi } from '@/api/video.js'
import { createUuid, getUpToken, setUpToken, clearUpToken } from '@/utils'
import * as qiniu from 'qiniu-js'

const emits = defineEmits(['getFileInfo'])

const percent = ref(0)
const haveResult = ref(false)
const result = ref(false)
const beginUpload = ref(false)

const upload = ref()
const fileInput = ref()
const icon = ref()
onMounted(()=>{
  initDragMethods()
})

onBeforeUnmount(()=>{
  clearUpToken()
})


const initDragMethods = () => {
  upload.value.addEventListener("click", () => {
    fileInput.value.click();
  })
  upload.value.ondragenter = (e) => {
    e.preventDefault()
    e.target.classList.add("drag")
  }
  upload.value.ondragover = (e) => {
    e.preventDefault()
  }
  upload.value.ondrop = (e) => {
    e.preventDefault()
    handlerFiles(e)
  }
  upload.value.ondragleave = (e) => {
    e.target.classList.remove("drag")
  }
  fileInput.value.onchange = ()=>{
    handleUpload()
  }
  icon.value.addEventListener("click", () => {
    fileInput.value.click();
  })
  icon.value.ondragenter = (e) => {
    e.preventDefault()
  }
  icon.value.ondragover = (e) => {
    e.preventDefault()
  }
  icon.value.ondrop = (e)=> {
    handlerFiles(e)
  }
}
const handlerFiles = (e) =>{
  const files = e.dataTransfer.files;
    if(files.length >1) {
      console.log('只能上传一个文件');
    } else {
      fileInput.value.files = files;
      handleUpload()
    }
}


const fetchUpToken = () => {
  return new Promise((resolve)=>{
    const uuid = createUuid()
    getUpTokenApi(uuid).then(res=>{
      const {data, status} = res;
      if(status === 200) {
        setUpToken(data.upToken)
        resolve(true)
      } else {
        resolve(false)
      }
    })
  })
  
  
}

const handleUpload = () => {
  beginUpload.value = true
  let file = fileInput.value.files[0]
  fetchUpToken().then(res=>{
    if(res) {
      upVideo(file)
    }
  })
}
const reUpload = () => {
  beginUpload.value = false;
  haveResult.value = false;
  result.value = false;
  percent.value = 0;
}
const upVideo = (file) => {
  const putExtra = {
    fname: "",
    params: {},
    mimeType: file.type
  };
  const upToken = getUpToken()
  const config = {
    useCdnDomain: true,
    region: qiniu.region.z1, 
  };

  const observable = qiniu.upload(
    file,
    null, // 使用文件hash作为 key
    upToken,
    putExtra,
    config
  );

  const observer = {
    next(res) {
      let number = res.total.percent;
      if(number!==100)
        percent.value = number.toFixed(2);
      else {
        percent.value = 100;
      }
    },
    error(err) {
      // 上传失败
      haveResult.value = true;
      result.value = false;
    },
    complete(res) {
      // 上传成功
      haveResult.value = true;
      result.value = true;
      emits("getFileInfo", res.data)
    },
  };
  observable.subscribe(observer);
}
</script>


<style>
:root {
--style-color: #fbfcfd;
--back-color: #ec656b;
--progress-color: #d78d8d;
}
</style>

<style scoped>
/* 树莓风格样式 */
.upload {
  border: 2px dashed #ec656b;
  height: 200px;
  width: 200px;
  border-radius: 12px;
  background-color: rgba(79, 5, 8, 0.05); /* 树莓色的淡化版本作为背景 */
  transition: background-color 0.3s ease;
  text-align: center;
  user-select: none;
  position: relative;  
  cursor: pointer;
}
.upload input {
  /* height: 100%;
  width: 100%;
  opacity: 0;
  cursor: pointer; */
 opacity: 0;
}
div.icon {
  position: absolute;
  top: 100px;
  left: 100px;
  transform: translate(-50%, -50%);
  font-size: 80px;
  color: #ec656b;
  font-weight: bold;
  user-select: none;
  cursor: pointer;
}
.center {
  position: absolute;
  top: 100px;
  left: 100px;
  transform: translate(-50%, -50%);
}

/* 上传进度条 */
.progress-bar {
  height: 5px;
  width: 140px;
  background: var(--progress-color);
  opacity: 1;
  position: relative;
  
}
.bar {
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
}

.progress-bar .progressLine {
  position: absolute;
  height: 100%;
  background: var(--back-color);
}
.drag {
  background-color: #ffe0e2;
}
</style>
