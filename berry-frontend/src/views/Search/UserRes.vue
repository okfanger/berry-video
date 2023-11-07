<template>
  <div class="user-container">
    <div class="userList">
        <div class="user-card-container" v-for="user in userList" :key="user.authorId">
          <div class="avatar">
            <img :src="`${user.authorAvatar}?t=${new Date().getTime()}`" alt="">
          </div>
          <div class="userinfo">
            <div class="username">{{ user.authorNickName }}</div>
            <div class="videoInfo">
              <div>{{ user.action.fansCount }} 粉丝</div>
              <div>{{ user.action.followCount }} 关注</div>
            </div>
          </div>
          <div>
            <div class="button-primary" @click="toUserMain(user)">个人页面</div>
          </div>
      </div>
    </div>

    <div class="pagination">
      <el-pagination layout="prev, pager, next" 
          v-model:current-page="pageQuery.current"
          @current-change="handleCurrentChange"
         :total="pageQuery.total"  :page-size="pageQuery.size" />
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { userStore } from '@/store'
import { useRoute, useRouter} from 'vue-router'
import {searchAggregateApi} from '@/api/search'
import { emitter } from '@/utils'
const route = useRoute()
const router = useRouter()
const data = reactive({
  keyword: route.query.searchKey,
  current: 1
})
const userData = ref({})
const userList = ref([])

const pageQuery = reactive({
  current: parseInt(userData.value.current),
  total: parseInt(userData.value.total),
  size: 10
})
onMounted(() => {
  emitter.on("search", (keyword) =>{
    data.keyword = keyword;
    fetchData()
  })
  fetchData()
})

function handleCurrentChange(val) {
  pageQuery.current = val;
  fetchData()
}

const fetchData = () => {
  searchAggregateApi(data).then(res=>{
    if(res.success) {
      const {user} = res.data
      userData.value = user;
      userList.value = userData.value.records;
    }
  })
}

function getRelationShip(action) {
  const { followed, friend } = action;
  if(friend) return {
    className: "button-plain",
    text: "互关"
  }; else {
    if(followed)
      return {
        className: "button-plain",
        text: "已关注"
      };
    else return {
      className: "button-primary",
      text: "关注"
    }
  }
}

// 去到用户个人首页
function toUserMain(user) {
  const userId = user.authorId;
  const myUserId = userStore.userInfo.authorId;
  let hrefUrl = '';
  if(userId === myUserId) {
    hrefUrl = `/user/self`
  } else {
    const {href} = router.resolve({
      path: `/user/${userId}`
    });
    hrefUrl = href;
  }
  window.open(hrefUrl, '_blank')
}

</script>

<style scoped lang="scss">
.user-container {
  .userList {
    height: calc(100vh - 200px);
    display: flex;
    flex-wrap: wrap;
    flex-direction: column;
    overflow: auto;
    .user-card-container {
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 10px;
      height: 112px;
      cursor: pointer;
      border-radius: 10px;

      &:hover {
        background-color: #f7f7f7;
      }

      .avatar {
        height: 72px;
        width: 72px;
        border-radius: 50%;
        overflow: hidden;
        margin-right: 20px;
        
        img {
          height: 100%;
          width: 100%;
        }
      }

      .userinfo {
        flex: 1;

        .videoInfo {
          display: flex;

          div {
            margin: 0 10px 0 0;
          }
        }
      }
    }
  }

  .pagination {
    display: flex;
    justify-content: end;
  }
}
</style>