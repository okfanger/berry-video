<template>
 <div>
  <div class="container-header">
    <div class="icon">树莓拍</div>
      <div class="search">
        <searchComponent />
      </div>
      <div class="user">
        <div class="userinfo">
          <div class="avatar">
            <img :src="isLogin() ? avatar : avatarBoy" alt="">
          </div>
        </div>
        <div class="button-primary login" @click="handlerLogin">登录</div>
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

const LogindialogVisble = ref(false)
const avatar = userStore.userInfo.avatar;
const handlerLogin = () => {
  LogindialogVisble.value = true
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
}

.icon {
  color: #1c1f23;
}
.user {
  display: flex;
  align-items: center;
  width: 150px;
  justify-content: space-around;
}
.avatar img {
  height: 50px;
  width: 50px;
  border-radius: 50%;
}

</style>