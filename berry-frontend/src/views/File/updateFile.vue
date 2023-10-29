<template>
  <div>
    <div class="button-primary" @click="showModel">发布视频</div>
    <div class="list">
      
    </div>


    <Model v-model:visble="visble">
      <el-form
        ref="ruleFormRef"
        :model="form"
        status-icon
        :rules="rules"
        label-width="60px"
        class="upload-demo"
        accept="png"
        :limit="1"
      >
        <el-form-item label="视频">
          <file-upload @getFileInfo="getFileInfo" v-if="visble"></file-upload>
        </el-form-item>
        <el-form-item label="文案" prop="title">
          <textarea class="textarea-raspberry" placeholder="给视频搭配一个文案" v-model="form.title"></textarea>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <!-- <el-input v-model.number="form.tags" /> -->
          <div>
            <div class="custom-input tags-input">
              <div v-for="tag in form.tags" :key="tag" class="tag">
                {{ tag }}
                <el-button size="mini" @click="removeTag(tag)">删除</el-button>
              </div>
              <input type="text" @keyup.enter="addTag" placeholder="请输入标签名" v-model="tagInput">
            </div>
          </div>
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
            <div class="button-primary" @click="publish">发布</div>
            <div class="button-plain" @click="resetForm">重置</div>
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
import {ref, reactive} from 'vue'
const form = reactive({
  title: "",
  tags: [],
  key: "",
  hash: "",
  visible: 1, // 0私密 1公开
})
const tagInput = ref("")
const visble = ref(false)


const publish = () => {}
const resetForm = () => {}
const showModel = () => {
  visble.value = true;
}
const removeTag = (tag) => {
  form.tags.value = form.tags.value.filter(t => t !== tag);
}
const addTag = () => {
  console.log(tagInput);
  if (tagInput.value && !form.tags.value.includes(tagInput.value)) {
    form.tags.value.push(tagInput.value);
    tagInput.value = "";
  }
}

const getFileInfo = (info) => {
  const {hash, key} = info
  form.hash = hash;
  form.key = key;
  console.log(form);
}
</script>

<style scoped>
::v-deep .el-textarea:focus-within {
  border-color: pink;
}
.tag {
  display: inline-block;
  padding: 5px 10px;
  margin: 5px;
  background-color: #f0f0f0;
  border-radius: 5px;
}
.tags-input {
  background-color: #fdebef;
}
.form-buttons {
  display:flex;
  justify-content: end;
  width: 100%;
}
</style>


