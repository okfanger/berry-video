<template>
  <div class="container">
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
          @current-change="handleCurrentChange"
         :total="pageQuery.total"  :page-size="pageQuery.size" />
    </div>
 </div>
</template>

<script setup>

import { getMyselfVideoApi,deleteVideoApi } from '@/api/video'
import {ref, reactive, onMounted} from 'vue'
import { parseTime } from '@/utils'
import productManagerSkeleton from '@/components/Skeleton/productManager-skeleton'



const pageQuery = reactive({
  size: 10,
  total: 0,
  current: 1
})
const tableData = ref([])

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

const handleCurrentChange = (currentPage) => {
  pageQuery.current = currentPage;
  fetchMyselfVideo()
}

</script>


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
    height: calc(100vh - 150px);
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
          object-fit: cover;
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


