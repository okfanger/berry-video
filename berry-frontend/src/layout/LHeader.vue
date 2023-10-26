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
      <LoginView v-if="LogindialogVisble" />
    </el-dialog>
   
 </div>
</template>

<script setup>
import avatar from '@/assets/avatar-do.png'
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
  background-color: #fe5c71;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px
}

.user {
  display: flex;
  align-items: center;
}
.avatar img {
  height: 70px;
  width: 70px;
  border-radius: 50%;
}

</style>