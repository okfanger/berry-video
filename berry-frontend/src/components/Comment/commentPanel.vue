<template>
 <div  class="comment-panel">
    <div class="comment-header">
        <span class="comment-count">1234 条评论</span>
        <button class="close-btn" @click="changeCommentState">关闭</button>
    </div>
    <div class="comment-list">
        <!-- 这里将显示用户的评论 -->
        <commentItem 
          :username="commentInfo.username"
          :id="commentInfo.id"
          :datetime="commentInfo.datetime"
          :content="commentInfo.content"
          :avatar="commentInfo.avatar"
        />
    </div>
    <div class="comment-input-area">
        <input type="text" placeholder="留下你精彩的评论吧...">
        <button>发布</button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { isCommoning } from '@/utils'
import commentItem from '@/components/Comment/commentItem'
import avatar from '@/assets/avatar-ddai.png'

const commentInfo = reactive({
  id: "001",
  datetime: "2020-12-1 09:23:01",
  username: "ddai",
  avatar,
  content: "发送错了人"
})

const changeCommentState = () => {
  isCommoning.value = false;
}
</script>


<!-- comment-panel -->
<style scoped>
.comment-panel {
  height: 100%;
  width: 400px;
  background-color: #e2e2e2;
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px; 
  background-color: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.comment-count {
    font-weight: bold;
}

.close-btn {
    background: none;
    border: none;
    cursor: pointer;
}

.comment-list {
  flex: 1;
}
.comment-input-area {
    display: flex;
    padding: 10px;
    border-top: 1px solid #e0e0e0;
}

.comment-input-area input {
    flex: 1;
    padding: 5px;
    border: none;
    border-radius: 5px;
    margin-right: 10px;
}

.comment-input-area button {
    background-color: #ff2d5a; /* 抖音主题色 */
    color: #fff;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
}

</style>
