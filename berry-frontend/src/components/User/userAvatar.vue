<template>
  <div>
    <div class="user-info-head" @click="editCropper()"><img v-bind:src="options.img" title="点击上传头像" class="img-circle img-lg" /></div>
    <Model v-model:visble="open" >
      <el-row>
        <el-col :xs="24" :md="12" :style="{height: '350px'}">
          <vue-cropper
            ref="cropper"
            :img="options.img"
            :info="true"
            :autoCrop="options.autoCrop"
            :autoCropWidth="options.autoCropWidth"
            :autoCropHeight="options.autoCropHeight"
            :fixedBox="options.fixedBox"
            :outputType="options.outputType"
            @realTime="realTime"
            v-if="visible"
          />
        </el-col>
        <el-col :xs="24" :md="12" :style="{height: '350px'}">
         <div style="display: flex;align-items: center;justify-content: center;height: 350px;width: 230px;">
          <div class="avatar-upload-preview">
            <img :src="previews.url" :style="previews.img"/>
          </div>
         </div>
        </el-col>
      </el-row>
      <br />
      <el-row>
        <el-col :lg="2" :sm="3" :xs="3">
          <el-upload action="#" :http-request="requestUpload" :show-file-list="false" :before-upload="beforeUpload">
            <el-button size="small">
              选择
              <el-icon>
                <UploadFilled />
              </el-icon>
            </el-button>
          </el-upload>
        </el-col>
        <el-col :lg="{span: 1, offset: 2}" :sm="2" :xs="2">
          <el-button :icon="Plus" size="small" @click="changeScale(1)"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :sm="2" :xs="2">
          <el-button :icon="Minus" size="small" @click="changeScale(-1)"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :sm="2" :xs="2">
          <el-button :icon="RefreshLeft" size="small" @click="rotateLeft()"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :sm="2" :xs="2">
          <el-button :icon="RefreshRight" size="small" @click="rotateRight()"></el-button>
        </el-col>
        <el-col :lg="{span: 2, offset: 6}" :sm="2" :xs="2">
          <el-button type="primary" size="small" @click="uploadImg()">提 交</el-button>
        </el-col>
      </el-row>
    </Model>
 </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { VueCropper } from "vue-cropper";
import 'vue-cropper/dist/index.css'
import { updateAvatar } from '@/api/user.js'
import Model from '@/components/Model/indexCom'
import img from '@/assets/avatar-ddai.png'
import { Plus, Minus,RefreshRight, RefreshLeft, UploadFilled } from '@element-plus/icons-vue'
import { userStore } from '@/store/user.store.js'
import { isLogin } from '@/utils'
const options = reactive({
  img, // 从store.user中读取
  autoCrop: true, // 是否默认生成截图框
  autoCropWidth: 200, // 默认生成截图框宽度
  autoCropHeight: 200, // 默认生成截图框高度
  fixedBox: true, // 固定截图框大小 不允许改变
  outputType:"png" // 默认生成截图为PNG格式
})
const open = ref(false)
const visible = ref(false)
const previews = ref({})
const cropper = ref() 

const editCropper = () => {
  open.value = true;
  visible.value = true
}
const rotateLeft = () => {
  cropper.value.rotateLeft();
}
const rotateRight = () => {
  cropper.value.rotateRight();
}
const changeScale = (num) => {
  num = num || 1;
  cropper.value.changeScale(num);
}
// 实时预览
const realTime = (data) => {
  previews.value = data
}
const requestUpload = () => {}
const beforeUpload = (file) => {
  if (file.type.indexOf("image/") == -1) {
    // this.$modal.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
  } else {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      options.img = reader.result;
    };
  }
}
const uploadImg = () => {
  cropper.value.getCropBlob(data => {
    let formData = new FormData();
    formData.append("file", data);
    updateAvatar(formData).then(res=>{
      if(res.status === 200) {
        // 
        userStore.fetchUserInfo()
      }
    })
  });
}
</script>

<style scoped lang="scss">
.user-info-head {
  position: relative;
  display: inline-block;
  height: 110px;
  width: 110px;
  border-radius: 50%;
  overflow: hidden;

  img {
    height: 100%;
    width: 100%;
  }
}

.user-info-head:hover:after {
  content: '+';
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  color: #fee;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  line-height: 110px;
  border-radius: 50%;
  text-align: center;
}



.avatar-upload-preview {
  height: 200px;
  width: 200px;
  border-radius: 50%;
  overflow: hidden;

  img {
    height: 100%;
    width: 100%;
  }
}
</style>