<template>
  <div class="comment-item">
    <div class="avatar">
      <img :src="props.avatar" alt="User Avatar" />
    </div>
    <div class="content">
      <div class="username">{{ props.username }}</div>
      <div class="text">{{ props.content }}</div>
      <div class="date">{{ props.datetime }}</div>
      <button @click="showReplyBox = !showReplyBox">Reply</button>
      <div v-if="showReplyBox">
        <textarea v-model="replyText"></textarea>
        <button @click="submitReply">Submit</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref } from 'vue';
// import avatar from '@/assets/avatar-ddai.png'
const props = defineProps({
  avatar: String,
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

<style scoped>
.comment-item {
  display: flex;
  /* Add more styles... */
}

.avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.content {
  margin-left: 10px;
}
</style>