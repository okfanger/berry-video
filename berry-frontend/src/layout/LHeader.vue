<template>
 <div>
  <div class="container-header">
    <div class="icon">树莓拍</div>
      <div class="search">
        <searchComponent />
      </div>
      <div class="user">
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
                <!-- <el-popconfirm 
                  title="确认退出登录?"
                  confirm-button-text="退出"
                  cancel-button-text="取消"
                  width="160"
                  @confirm="">
                  <template #reference>
                    
                  </template>
                </el-popconfirm> -->
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
 </div>
</template>

<script setup>
// import avatar from '@/assets/avatar-ddai.png'
import avatarBoy from '@/assets/avatar-boy.png'
import LoginModel from '@/components/Login/LoginModel'
import Model from '@/components/Model/indexCom.vue'
import searchComponent from '@/components/Search/searchComponent'
import { ref } from 'vue'
import { isLogin } from '@/utils'
import { userStore } from '@/store'
import { useRouter } from 'vue-router'

const router = useRouter()
const LogindialogVisble = ref(false)
const avatar = `${userStore.userInfo.userAvatar}?t=${new Date().getTime()}`;
const handlerLogin = () => {
  LogindialogVisble.value = true
}
const toMy = () => {
  router.push({
    name: "mine"
  })
}
const logout = () => {
  userStore.logout()
  window.location.reload()
}
</script>

<style scoped>
.container-header {
  height: 80px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  color: white;
  z-index: 99999;
  background-color: #fefafe;
}

.icon {
  color: #1c1f23;
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

</style>