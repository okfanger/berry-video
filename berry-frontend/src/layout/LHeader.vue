<template>
  <div class="container-header">
    <div class="icon">树莓拍</div>
    <div class="search">
      <el-input width="500px" />
    </div>
    <div class="user">
      <div class="userinfo">
        <div class="avatar">
          <img :src="avatar" alt="">
        </div>
      </div>
      <div class="login" @click="handlerLogin">登录</div>
    </div>

     <!-- 登录框 -->
     <el-dialog
      v-model="LogindialogVisble"
      title="登录"
      width="30%"
      align-center
      :close-on-click-modal="false"
    >
      <LoginView v-if="LogindialogVisble" v-model:LogindialogVisble="LogindialogVisble" />
    </el-dialog>
   
 </div>
</template>

<script setup>
import avatar from '@/assets/avatar-ddai.png'
import LoginView from '@/views/User/LoginView.vue';
import { ref } from 'vue'
const LogindialogVisble = ref(false)

const handlerLogin = () => {
  LogindialogVisble.value = true
}
</script>

<style scoped>
.container-header {
  height: 80px;
  background-color: #050505;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  color: white;
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
.login {
  background-color: #fe2c55;
  height: 40px;
  width: 70px;
  border-radius: 10px;
  color: white;
  text-align: center;
  line-height: 40px;
  font-weight: bold;
  font-size: 14px;
  cursor: pointer;
  user-select: none;
}
</style>