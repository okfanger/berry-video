<template>
  <div>
    <!-- 上传组件 -->
    <el-upload
        class="raspberry-upload"
        drag
        :auto-upload="false"
        :accept="'video/*'"
        :on-change="handleUpload"
        :file-list="fileList"
    >
        <el-icon class="el-icon--upload"><upload-filled style="height: 30px; width: 30px;" /></el-icon>
        <div class="el-upload__text">
          拖拽 或 <em>点击这里上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            视频最大限制为100MB
          </div>
        </template>
    </el-upload>
    <!-- 视频预览 -->
    <video v-if="videoUrl" controls width="320" :src="videoUrl"></video>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineEmits } from 'vue';
import { UploadFilled } from '@element-plus/icons-vue'
import { getUpTokenApi } from '@/api/video.js'
import { createUuid, getUpToken, setUpToken, clearUpToken } from '@/utils'
import * as qiniu from 'qiniu-js'

const emits = defineEmits(['getFileInfo'])

const fileList = ref([]);
const videoUrl = ref('');
let observable = null;

onMounted(()=>{
  // fetchUpToken();
})

onBeforeUnmount(()=>{
  clearUpToken()
})

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


const handleUpload = (file) => {
  fetchUpToken().then(res=>{
    if(res) {
      upVideo(file)
    }
  })
}


const upVideo = (file) => {
  const putExtra = {
    fname: "",
    params: {},
    mimeType: ["video/mp4", "video/avi", "video/flv"],
  };
  const upToken = getUpToken()
  const config = {
    useCdnDomain: true,
    region: qiniu.region.z1, 
  };

  const observable = qiniu.upload(
    file.raw,
    null, // 使用文件hash作为 key
    upToken,
    putExtra,
    config
  );

  const observer = {
    next(res) {
      console.log(res);
    },
    error(err) {
      // 上传失败
      console.error("Upload error:", err);
      fileList.value = []; // 清除文件列表
    },
    complete(res) {
      // 上传成功
      emits("getFileInfo", res.data)
    },
  };

  observable.subscribe(observer);
}
</script>

<style scoped>
/* 树莓风格样式 */
.raspberry-upload {
    border: 2px dashed #ec656b;
    border-radius: 12px;
    background-color: rgba(236, 101, 107, 0.05); /* 树莓色的淡化版本作为背景 */
    transition: background-color 0.3s ease;
    padding: 10px 20px;
    text-align: center;
    user-select: none;
}

.raspberry-upload:hover {
    background-color: rgba(236, 101, 107, 0.1);
}

.el-upload__text {
    color: #f3777d;
    font-weight: 500;
}
        
.el-upload.is-drag {
  height: 125px;
}
.el-upload__text em {
    text-decoration: underline;
    cursor: pointer;
    color: #ed656c;
}

.el-upload__tip {
    margin-top: 20px;
    font-size: 12px;
    color: rgba(236, 101, 107, 0.7);
}

/* 树莓风格的icon样式 */
.el-icon--upload {
    font-size: 24px;
    color: #ec656b;
    margin-bottom: 16px;
}

::v-deep .el-upload-dragger {
  background-color: transparent;
  border: none;
}
</style>
