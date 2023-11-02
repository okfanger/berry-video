<template>
  <div class="mine-container"> 
    <div class="info">
      <el-row>
        <el-col :span="12">
          <div class="userInfo">
            <userAvatar />
            <div class="username">
              {{ nickName }}
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
    <LikeFaverOrProduct :type="currentTab"  />
   </div>
 </div>
</template>

<script setup>
import { ref } from 'vue'
import userAvatar from '@/components/User/userAvatar'
import { userStore } from '@/store'
import LikeFaverOrProduct from '@/views/LikeFaoverOrProduct/indexView.vue'

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
const nickName = userStore.userInfo.nickName;

const currentTab = ref('like') // 默认是喜欢列表




const changeTab = (key) => {
  currentTab.value = key
}



</script>

<style scoped>
.mine-container {
  display: flex;
  flex-direction: column;
}

.username {
  height: 110px;
  min-width: 50px;
  line-height: 110px;
  text-align: center;
  font-weight: bold;
  font-size: 18px;
  color: #1c1f23;
  user-select: none;
}

.info {
  height: 120px;
  background-color: #fffbfe;
}
.userInfo {
  display: flex;
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
  font-size: 19px;
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