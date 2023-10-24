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
      <div class="login">登录</div>
      <div class="regiseter">注册</div>
    </div>
 </div>
</template>

<script setup>
import avatar from '@/assets/avatar-do.png'
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