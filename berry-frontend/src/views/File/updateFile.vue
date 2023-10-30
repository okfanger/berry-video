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
            <div>
              <div class="tags">
                <div v-for="tag in form.tags" :key="tag" class="tag">
                  {{ tag }}
                  <span class="delete" @click="removeTag(tag)">x</span>
                </div>
              </div>
              <div style="display: flex; align-items: center;">
                <input type="text" class="custom-input tags-input" placeholder="请输入标签名" v-model="tagInput">
                <div class="button-primary" @click="addTag" style="width: 80px;margin-left: 10px;">添加</div>
              </div>
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
  tags: ["大学生", "大学生", "大学生","大学生","大学","大学生","大学生","大学生","大学生","大学"],
  key: "",
  hash: "",
  visible: 1, // 0私密 1公开
})
const tagInput = ref("")
const visble = ref(true)


const publish = () => {}
const resetForm = () => {}
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


