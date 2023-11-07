<template>
  <div class="userInfo-card-container">
    <el-popover
      :width="180"
      placement="bottom" 
      trigger="hover"
    >
      <template #reference>
        <div class="avatar"
          :style="avatarStyle">
          <img :src="`${props.user.authorAvatar}?t=${new Date().getTime()}`" alt="User Avatar" />
        </div>
      </template>
      <div class="user-card">
        <div class="info">
          <div class="card-avatar">
            <img :src="`${props.user.authorAvatar}?t=${new Date().getTime()}`" alt="User Avatar" @click="toMineSelf()" />
          </div>
          <div class="username">
            {{ props.user.authorNickName }}
          </div>
        </div>
        <div>
          
        </div>
      </div>
    </el-popover>
 </div>
</template>

<script setup>
import { defineProps } from 'vue'
import { userStore } from '@/store'
import { useRouter } from 'vue-router'

const props = defineProps({
  user: Object,
  height: Number,
  width: Number,
}) 

const router = useRouter()
const avatarStyle = {
  height: `${props.height}px`,
  width: `${props.width}px`
}

const toMineSelf = () => {
  const userId = props.user.authorId;
  const myUserId = userStore.userInfo.authorId;
  if(userId === myUserId) {
    router.push(`/user/self`)
  } else {
    const {href} = router.resolve({
      path: `/user/${userId}`
    });
    window.open(href, '_blank')
  }
} 
</script>

<style scoped lang="scss">
.userInfo-card-container {
  .avatar {
    overflow: hidden;
    cursor: pointer;
    border-radius: 50%;

    img {
      height: 100%;
      width: 100%;
    }
  }
}
</style>

<style lang="scss" >
 .user-card {
  z-index: 999;
  display: flex;
  flex-direction: column;
  .info {
    display: flex;

    .card-avatar {
      height: 60px;
      width: 60px;
      cursor: pointer;
      overflow: hidden;
      border-radius: 50%;

      img {
        height: 100%;
        width: 100%;
      }
    }
    .username {
      font-weight: bold;
      padding: 10px;
    }
  }
}
</style>