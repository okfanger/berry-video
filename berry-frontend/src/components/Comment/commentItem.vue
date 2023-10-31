<template>
  <div class="comment-item">
    <div class="avatar">
      <img :src="props.userAvatar" alt="User Avatar" />
    </div>
    <div class="content">
      <div class="username">{{ props.username }}</div>
      <div class="text">{{ props.content }}</div>
      <div class="date">{{ props.datetime }}</div>
      <div>
        <div>
          <i class="iconfont icon-aixin" style="cursor: pointer;"></i> 
          <span>0</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref } from 'vue';
// import avatar from '@/assets/avatar-ddai.png'
const props = defineProps({
  userAvatar: String,
  username: String,
  content: String,
  datetime: String,
  id: String,
});
const showReplyBox = ref(false);
const replyText = ref('');

function submitReply() {
  // Handle reply submission
  console.log(replyText.value);
  showReplyBox.value = false;
}
</script>

<style scoped lang="scss">
.comment-item {
  display: flex;
  flex-direction: row;

  .avatar img {
    width: 35px;
    height: 35px;
    border-radius: 50%;
  }
  .content {
    margin-left: 10px;
    display: flex;
    flex-direction: column;

    .username {
      font-size: 13px;
    }
    .text {
      font-size: 16px;
    }
    .date{
      font-size: 12px;
    }
  }
}




</style>