<template>
 <div>
  <div class="container-header">
    <div class="icon">
      <img :src="logo" alt="">
    </div>
      <div class="search">
        <searchComponent />
      </div>
      <div class="user">
        <div class="videoUpload" v-if="isLogin()"  @click="showModel">视频投稿</div>
        <div class="userinfo">
          <el-dropdown>
            <span class="el-dropdown-link">
              <div class="avatar">
                <img :src="isLogin() ? avatar : avatarBoy" alt="">
              </div>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="toMy()">我的</el-dropdown-item>
                <el-dropdown-item @click="logout()">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div class="button-primary login" v-if="!isLogin()" @click="handlerLogin">登录</div>
      </div>
  </div>
  <!-- 登录框 -->
  <Model
    v-model:visble="LogindialogVisble"
  >
    <login-model v-if="LogindialogVisble" v-model:LogindialogVisble="LogindialogVisble" />
  </Model>


  <!-- 发布视频模态框 -->
  <Model v-model:visble="visble">
    <el-form
      ref="ruleFormRef"
      :model="form"
      status-icon
      :rules="rules"
      label-width="60px"
    >
      <el-form-item label="视频">
        <file-upload @getFileInfo="getFileInfo" v-if="visble"></file-upload>
      </el-form-item>
      <el-form-item label="文案" prop="content">
        <textarea class="textarea-raspberry" placeholder="给视频搭配一个文案" v-model="form.content"></textarea>
        <div class="button-primary" @click="createTags">生成标签</div>
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <div>
          <div>
            <div class="tags">
              <div v-for="tag in form.tags" :key="tag.key" class="tag">
                {{ tag.value }}
                <span class="delete" @click="removeTag(tag)">x</span>
              </div>
            </div>
            <div style="display: flex; align-items: center;">
              <input type="text" class="input-raspberry" placeholder="请输入标签名" v-model="tagInput" />
              <div class="button-primary tag-button" @click="addTag" style="">添加</div>
            </div>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="频道">
        <el-select v-model="form.channelId" placeholder="请选择一个分类" size="large">
          <el-option
            v-for="item in channelList"
            :key="item.id"
            :label="item.label"
            :value="item.id"
          />
        </el-select>
        <el-icon name="el-icon-edit">
        </el-icon>
      </el-form-item>
      <el-form-item label="私密性" prop="visible">
        <el-switch
          v-model="form.visible"
          :active-icon="Check"
          :active-value="1"
          :inactive-value="0"
          :inactive-icon="Close"
          inline-prompt
          class="ml-2"
          style="--el-switch-on-color: #ec656b; --el-switch-off-color: #eef2f5"
        />
        <span style="color:#f49598; marginLeft: 6px; user-select:none">默认是对外公开的</span>
      </el-form-item>
      <el-form-item>
        <el-space class="form-buttons">
          <div class="button-primary" @click="publish" v-loading="loading">发布</div>
        </el-space>
      </el-form-item>
    </el-form>
  </Model>



 </div>
</template>

<script setup>
// import avatar from '@/assets/avatar-ddai.png'
import logo from '@/assets/berry-logo.png'
import FileUpload from '@/components/FileUpload/index'
import { Check, Close } from '@element-plus/icons-vue'
import avatarBoy from '@/assets/avatar-boy.png'
import LoginModel from '@/components/Login/LoginModel'
import Model from '@/components/Model/indexCom.vue'
import searchComponent from '@/components/Search/searchComponent'
import { ref, reactive } from 'vue'
import { isLogin, createUuid } from '@/utils'
import { userStore , videoStore} from '@/store'
import { useRouter } from 'vue-router'
import { publishVideo, createTagsByContentApi } from '@/api/video'

const router = useRouter()
const LogindialogVisble = ref(false)
const avatar = `${userStore.userInfo.authorAvatar}?t=${new Date().getTime()}`;
const channelList = ref(videoStore.channelList || [])
const form = reactive({
  content: "",
  tags: [],
  visible: 1, // 0私密 1公开
  channelId: null,
  fileId: ""
})
const tagInput = ref("")
const visble = ref(false)
const loading = ref(false)

const handlerLogin = () => {
  LogindialogVisble.value = true
}

const toMy = () => {
  router.push(`/user/self`)
}
const logout = () => {
  userStore.logout()
  window.location.reload()
  router.push("/")
}

const publish = () => {
  loading.value = true;
  let formData = {
    ...form,
    tags: form.tags.map(e => e.value)
  }
  publishVideo(formData).then(res=>{
    if(res.status === 200) {
      loading.value = false;
      visble.value = false;
    }
  })
}
const createTags = () => {
  createTagsByContentApi(form.content).then(res=>{
    const {tokens} = res;
    form.tags = form.tags.concat(tokens.map(e => {
      let tag = {
        key: createUuid(),
        value: e.token
      }
      return tag;
    }))
  })
}
const showModel = () => {
  visble.value = true;
}
const removeTag = (tag) => {
  form.tags = form.tags.filter(t => t.key !== tag.key);
}
const addTag = () => {
  if (tagInput.value && !form.tags.some(e=> e.value == tagInput.value)) {
    form.tags.push({
      key: createUuid(),
      value: tagInput.value,
    });
    tagInput.value = "";
  }
}
const getFileInfo = (info) => {
  const { fileId } = info;
  form.fileId = fileId
}


</script>

<style scoped>
.container-header {
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  color: white;
  z-index: 99999;
  background-color: #fefafe;
}

.icon {
  height: 71px;
  width: 89px;
}

.icon img {
  height: 100%;
  width: 100%;
}
.user {
  display: flex;
  align-items: center;
  min-width: 70px;
  justify-content: space-around;
}
.avatar img {
  height: 50px;
  width: 50px;
  border-radius: 50%;
  cursor: pointer;
}


.videoUpload {
  color: #5c5c5c;
  border-radius: 14px;
  padding: 10px ;
  user-select: none;
  cursor: pointer;
}
.videoUpload:hover {
  color: #333333;
  background-color: #f5f5f5;
}
</style>



<style scoped>
::v-deep .el-textarea:focus-within {
  border-color: pink;
}
.tag {
  display: inline-block;
  padding: 1px 6px;
  margin: 1px 2px;
  background-color: #e2e5e7;
  border-radius: 5px;
  border-radius: 5px;
  color: #171515;
  cursor: pointer;
  user-select: none;
}
.delete {
  color: white;
  cursor: pointer;
  height: 15px;
  width: 15px;
  background-color: rgb(132, 131, 131);
  border-radius: 50%;
  display: inline-block;
  text-align: center;
  line-height: 15px;
  opacity: 0;
  transition: opacity .4s;
}
.tag:hover .delete{
  opacity: 1;
}
.tag-button {
  width: 80px;
  margin-left: 10px;
}
.tags-input {
  background-color: #fdebef;
  flex: 1;
}
.form-buttons {
  display:flex;
  justify-content: end;
  width: 100%;
}
</style>
