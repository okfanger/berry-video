<template>
  <div>
    <div class="button-primary" @click="showModel">发布视频</div>
    <!-- 我的创作列表 -->
    <div class="list">
      
    </div>

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
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <div>
            <div>
              <div class="tags">
                <div v-for="tag in form.tags" :key="tag" class="tag">
                  {{ tag }}
                  <span class="delete" @click="removeTag(tag)">x</span>
                </div>
              </div>
              <div style="display: flex; align-items: center;">
                <input type="text" class="custom-input tags-input" placeholder="请输入标签名" v-model="tagInput">
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
import Model from '@/components/Model/indexCom'
import FileUpload from '@/components/FileUpload/index'
import { Check, Close } from '@element-plus/icons-vue'
import { getChannelList, publishVideo, getMyselfVideoApi } from '@/api/video'
import {ref, reactive, onMounted} from 'vue'

const form = reactive({
  content: "",
  tags: [],
  visible: 1, // 0私密 1公开
  channelId: null,
  fileId: ""
})
const tagInput = ref("")
const visble = ref(false)
const channelList = ref([])
const loading = ref(false)
const current = ref(1)

onMounted(()=>{
  fetchChannelList();
  fetchMyselfVideo();
})
const fetchChannelList = async () => {
  try {
    let res = await getChannelList()
    const { data, status } = res;
    if (status == 200) {
      channelList.value = data;
    }
  } catch(e) {
    console.warn(e);
  }
}
const fetchMyselfVideo = () => {
  getMyselfVideoApi(current.value).then(res=>{
    console.log(res);
  })
}
// 我开发使用的技术栈使用的是Vue3 <scipt setup>方式,现在有这样一个场景,我封装了一个视频组件,里面包括视频播放器,评论组件,视频点赞,收藏,评论数,分享等数据, 现在需要上下滚动来翻看视频; 方案1: 根据数据videoList来渲染多个视频组件,但是性能不好; 方案2: 使用当前播放index,配合xxx, 方案2不太会
const publish = () => {
  loading.value = true;
  publishVideo(form).then(res=>{
    if(res.status === 200) {
      loading.value = false;
      visble.value = false;
    }
  })
}
const showModel = () => {
  visble.value = true;
}
const removeTag = (tag) => {
  form.tags = form.tags.filter(t => t !== tag);
}
const addTag = () => {
  console.log(tagInput);
  if (tagInput.value && !form.tags.includes(tagInput.value)) {
    form.tags.push(tagInput.value);
    tagInput.value = "";
  }
}
const getFileInfo = (info) => {
  const { fileId } = info;
  form.fileId = fileId
}
</script>

<style scoped>
::v-deep .el-textarea:focus-within {
  border-color: pink;
}
.tag {
  display: inline-block;
  padding: 1px 3px;
  margin: 1px 2px;
  background-color: #ff8e8e;
  border-radius: 5px;
  border-radius: 5px;
  color: white;
  cursor: pointer;
  user-select: none;
}
.delete {
  color: white;
  cursor: pointer;
  height: 20px;
  width: 20px;
  background-color: rgb(207, 91, 91);
  border-radius: 50%;
  display: inline-block;
  text-align: center;
  line-height: 20px;
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


<style scoped>
::v-deep .select-trigger:active {
  border: pink;
}

</style>


