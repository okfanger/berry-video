<template>
 <div  class="comment-panel">
    <div class="comment-header">
        <span class="comment-count">{{ props.total }} 条评论</span>
        <button class="close-btn" @click="changeCommentState">关闭</button>
    </div>
    <div class="comment-list">
        <!-- 这里将显示用户的评论 -->
        <commentItem
          class="commentItem"
          v-for="comment in commentList"
          :authorNickName="comment.author.authorNickName"
          :id="comment.id"
          :createTime="comment.createTime"
          :content="comment.content"
          :authorAvatar="`${comment.author.authorAvatar}?t=${new Date().getTime()}`"
          :isLiked="comment.isLiked"
          :likeCount="comment.likeCount"          
          :key="comment"
        />
    </div>
    <div class="comment-input-area">
        <input type="text" v-model="content" placeholder="留下你精彩的评论吧...">
        <button @click="publish" :disabled="disabled">发布</button>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, onMounted, defineEmits, watch } from 'vue'
import { createUuid } from '@/utils'
import commentItem from '@/components/Comment/commentItem'
import { publisComment } from '@/api/video'
import { userStore } from '@/store'
const props = defineProps(['videoId', 'commentList', 'total'])
const emits = defineEmits(['publishComment', 'update:isComming', "update:commentList"])

const content = ref("")
const disabled = ref(true)

onMounted(()=>{
})

watch(() => content.value, (val) => {
  disabled.value = val.trim() !== '' ? false : true;
})



const changeCommentState = () => {
  // isCommoning.value = false;
  emits("update:isComming", false)
}

const publish = () => {
  if(content.value.trim() !== '') {
    publisComment(props.videoId, content.value.trim()).then(res=>{
      if(res.status == 200) {
        const comment = {
          author: {
            authorAvatar: userStore.userInfo.userAvatar,
            authorNickName: userStore.userInfo.nickName
          },
          id: createUuid(),
          createTime: new Date().toString(),
          isLiked: false,
          likeCount: 0,
          content: content.value.trim(),
        }
        content.value = ""
        emits("update:commentList", [comment, ...props.commentList])
      }
    })
  }
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
  padding: 0 10px;
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
  height: 100%;
  overflow: scroll;
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
    background-color: #ec656b; 
    color: #fff;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
}
.comment-input-area button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}
.commentItem {
  margin-bottom: 5px;
}
</style>
