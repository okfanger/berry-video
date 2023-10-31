<template>
 <div  class="comment-panel">
    <div class="comment-header">
        <span class="comment-count">1234 条评论</span>
        <button class="close-btn" @click="changeCommentState">关闭</button>
    </div>
    <div class="comment-list">
        <!-- 这里将显示用户的评论 -->
        <commentItem
          class="commentItem"
          :username="commentInfo.username"
          :id="commentInfo.id"
          :datetime="commentInfo.datetime"
          :content="commentInfo.content"
          :userAvatar="commentInfo.userAvatar"
          v-for="item in 6"
          :key="item"
        />
    </div>
    <div class="comment-input-area">
        <input type="text" v-model="content" placeholder="留下你精彩的评论吧...">
        <button @click="publish">发布</button>
    </div>
  </div>
</template>

<script setup>
import { reactive,ref, defineProps, onMounted } from 'vue'
import { isCommoning } from '@/utils'
import commentItem from '@/components/Comment/commentItem'
import userAvatar from '@/assets/avatar-boy.png'
import { publisComment, getVideoCommentList } from '@/api/video'
const props = defineProps(['videoId'])
const commentInfo = reactive({
  id: "001",
  datetime: "2020-12-1 09:23:01",
  username: "默认用户",
  userAvatar,
  content: "🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子🐒猴子"
})
const content = ref("")

onMounted(()=>{
  fetchCommentList()
})


const changeCommentState = () => {
  isCommoning.value = false;
}

const publish = () => {
  publisComment(props.videoId, content.value).then(res=>{
    if(res.status == 200) {
      content.value = ""
    }
  })
}

const fetchCommentList = () => {
  getVideoCommentList({
    videoId: props.videoId,
    current: 1,
  }).then(res=>{
    console.log(res);
  })
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
  flex: 1;
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
}

.commentItem {
  margin-bottom: 5px;
}
</style>
