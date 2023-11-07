<template>
  <div class="mine-container"> 
    <div class="info">
      <el-row>
        <el-col :span="24">
          <div class="userInfo">
            <div class="left">
              <userAvatar v-if="isOwnSelf" />
              <div class="avatar" v-else>
                <img :src="otherUser.authorAvatar" alt="">
              </div>
            </div>
            <div class="center">
              <div class="username">
                {{ nickName }}
              </div>
              <div class="action" v-if="isOwnSelf">
                <div>关注 <span>{{ userStore.userInfo.action.followCount }}</span></div>
                <div>粉丝 <span>{{ userStore.userInfo.action.fansCount }}</span></div>
              </div>
              <div class="action" v-else>
                <div>关注 <span>{{ otherUser.action.followCount }}</span></div>
                <div>粉丝 <span>{{ otherUser.action.fansCount }}</span></div>
              </div>
            </div>
            <div class="right">
              <div v-if="params.userId !== 'self'">
                <div class="button-plain button" v-if="followed"
                  @click="handlerUnFollow">取消关注</div>
                <div class="button-primary button" v-else @click="handlerFollow">关注</div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <el-affix :offset="80">
      <div class="tabs">
        <div class="tab" v-for="item in tabList"
        :key="item.key" @click="changeTab(item.key)"
        :class="{activated: item.key === currentTab}">
          {{ item.name }}
        </div>
      </div>
    </el-affix>


   <div class="listBox">
    <LikeFaverOrProduct :type="currentTab" :userId="params.userId" />
   </div>
 </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import userAvatar from '@/components/User/userAvatar'
import { userStore } from '@/store'
import LikeFaverOrProduct from '@/views/LikeFaoverOrProduct/indexView.vue'
import { useRoute } from 'vue-router'
import { getUserInfoVo } from '@/api/user'
import { isLogin } from '@/utils'
import { followUserApi, unfollowUserApi } from '@/api/user'

const { params } = useRoute()
const tabList = ref([
  {
    key:"product",
    name:"作品"
  },
  {
    key:"like",
    name:"喜欢"
  },
  {
    key:"favor",
    name:"收藏"
  },
])

const nickName = ref(userStore.userInfo.authorNickName);
const otherUser = ref({})
const isOwnSelf = ref(true);

const currentTab = ref('like') // 默认是喜欢列表

const {userId} = params;
const authorId = userId === 'self' ? userStore.userInfo.authorId : userId;
onMounted(() => {
  fetchUserInfo()
})

const changeTab = (key) => {
  currentTab.value = key
}

const followed = ref(false) // 是否已经关注

// 关注
const handlerFollow = () => {
  followUserApi(authorId).then(res=>{
    if(res.success) {
      followed.value = true;
    }
  })
}

// 取消关注
const handlerUnFollow = () => {
  unfollowUserApi(authorId).then(res=>{
    console.log(res);
    if(res.success) {
      followed.value = false;
    }
  })
}


const fetchUserInfo = () => {
  if( isLogin() ) {
    if(userId === 'self') {
      isOwnSelf.value = true;
    } else {
      isOwnSelf.value = false;
      getUserInfoVo(authorId).then(res=>{
        if(res.status===200) {
          nickName.value = res.data.authorNickName;
          otherUser.value = res.data;
          followed.value = res.data.action.followed;
        }
      })
    }
    
    
  }
}
</script>

<style scoped lang="scss">
.mine-container {
  display: flex;
  flex-direction: column;

  .info {
    height: 120px;
    width: 100%;
    background-color: #fffbfe;

    .userInfo {
      display: flex;

      .left {
        .avatar {
          height: 110px;
          width: 110px;
          border-radius: 50%;
          overflow: hidden; 

          img {
            height: 100%;
            width: 100%;
          }
        }
      }

      .center {
        padding: 10px;
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-around;

        .username {
          font-weight: bold;
          font-size: 18px;
          color: #1c1f23;
          user-select: none;
        }


        .action {
          display: flex;
          width: 200px;
          div {
            margin-right: 15px;
            span {
              font-weight: 800;
            }
          }
          
        }
      }
      
    }

  }

}




.tabs {
  display: flex;
  border-bottom: 1px solid #e2e2e2;
  padding: 0 00 10px 0;
  margin-bottom: 10px;
  background-color: #fffbfe;
}
.tab {
  width: 76px;
  color: #1c1f23;
  font-weight: 400;
  font-size: 17px;
  cursor: pointer;
}
.activated {
  color: #ec656b;
}
.listBox {
  max-height: calc(100vh - 289px);
  width: 100%;
  background-color: #f7f9fa;
  flex: 1;
  overflow-y: auto;
}



</style>