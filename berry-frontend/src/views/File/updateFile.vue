<template>
  <div class="container">
    <div class="button-primary" @click="showModel">发布视频</div>
    <!-- 我的创作列表 -->
    <div class="list">
      <div class="item" v-for="item in tableData" :key="item.id" v-show="!loadingTableData">
        <div class="cover">
          <img :src="item.cover" alt="">
        </div>
        <div class="info">
          <div class="content"> {{ item.content }} </div>
          <div class="createTime"> {{ parseTime(item.createTime) }}</div>
          <div class="counter">
            <div>
              <i class="iconfont icon-aixin2"></i>
              <span class="num">{{ item.likeCount }}</span>
            </div>
            <div>
              <i class="iconfont icon-pinglun1"></i>
              <span class="num">{{ item.commentCount }}</span>
            </div>
            <div>
              <i class="iconfont icon-jiaxingshoucangtianchong"></i>
              <span class="num">{{ item.favorCount }}</span>
            </div>
          </div>
        </div>
        <div class="actions">
          <el-popconfirm 
            title="确认删除该视频?"
            confirm-button-text="删除"
            cancel-button-text="取消"
            width="160"
            @confirm="deleteVideo(item.id)">
            <template #reference>
              <div >删除</div>  
            </template>
          </el-popconfirm>
        </div>
      </div>
      <div v-show="loadingTableData">
        <productManagerSkeleton v-for="item in 5" :key="item" />
      </div>
    </div>
    <div style="display: flex;justify-content: end;">
      <el-pagination layout="prev, pager, next" 
          v-model:current-page="pageQuery.current"
         :total="pageQuery.total"  :page-size="pageQuery.size" />
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
          <!-- <select class="select-raspberry" v-model="form.channelId" placeholder="请选择一个分类">
              <option :value="item.id" v-for="item in channelList" :key="item.id">{{ item.label }}</option>
          </select> -->
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
import { publishVideo, getMyselfVideoApi,deleteVideoApi } from '@/api/video'
import {ref, reactive, onMounted} from 'vue'
import { videoStore } from '@/store'
import { parseTime } from '@/utils'
import productManagerSkeleton from '@/components/Skeleton/productManager-skeleton'

const form = reactive({
  content: "",
  tags: [],
  visible: 1, // 0私密 1公开
  channelId: null,
  fileId: ""
})

const pageQuery = reactive({
  size: 10,
  total: 0,
  current: 1
})
const tableData = ref([])
const tagInput = ref("")
const visble = ref(false)
const channelList = ref(videoStore.channelList || [])
const loading = ref(false)
const loadingTableData = ref(false)

onMounted(()=>{
  fetchMyselfVideo();
})

const fetchMyselfVideo = () => {
  loadingTableData.value = true;
  getMyselfVideoApi(pageQuery.current).then(res=>{
    const {data, success} = res;
    if(success) {
      loadingTableData.value = false;
      tableData.value = data.records;
      pageQuery.total = parseInt(data.total);
      pageQuery.size = parseInt(data.size);
    }
  })
}
const deleteVideo = (videoId) => {
  deleteVideoApi(videoId).then(res=>{
    if(res.success) {
      fetchMyselfVideo()
    }
  })
}
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


<style lang="scss" scoped>
.container {
  display: flex;
  flex-direction: column;
  padding-bottom: 10px;

  .list {
    height: calc(100vh - 200px);
    overflow: auto;
    .item {
      border-bottom: 1px solid #e7e7e7;
      display: flex;
      height: 100px;
      margin: 10px 0 20px 0;
      padding-bottom: 10px;
      align-items: center;

      .cover {
        width: 164px;
        height: 100%;
        img {
          height: 100%;
          width: 100%;
          border-radius: 5px;
        }
      }

      .info {
        display: flex;
        flex-direction: column;
        user-select: none;
        padding-left: 20px;
        flex: 1;

        .content {
          height: 24px;
          font-size: 16px;
          color: #212121;
          line-height: 20px;
          vertical-align: middle;
        }

        .createTime {
          font-size: 13px;
          color: #7f7f7f;
          height: 35px;
        }

        .counter {
          color: #999999;
          height: 18px;
          display: flex;

          & > div{
            width: 50px;
          }
        }
      }

      .actions {
        width: 200px;
        display: flex;
        justify-content: end;

        & > div {
          width: 84px;
          height: 32px;
          display: -webkit-box;
          display: -webkit-flex;
          display: flex;
          -webkit-box-align: center;
          -webkit-align-items: center;
          align-items: center;
          border: 1px solid #e7e7e7;
          border-radius: 2px;
          font-size: 14px;
          color: #505050;
          line-height: 18px;
          padding: 0;
          -webkit-box-pack: center;
          -webkit-justify-content: center;
          justify-content: center;
          margin-right: 12px;
          cursor: pointer;
        }

        & > div:hover {
          border-color: #ec656b;
          color: #ec656b;
        }
      }
    }

  }
}

</style>


