<template>
 <div  class="comment-panel">
    <!-- header -->
    <div class="comment-header">
      <div class="userAvatar" >
        <img :src="`${props.videoPropsPanel.author.authorAvatar}?t=${new Date().getTime()}`" alt="">
      </div>
      <div class="username">{{ videoPropsPanel.author.authorNickName }}</div>
    </div>

    <!-- comment list -->
    <el-scrollbar class="comment-list-box" >
      <div class="comment-count">共 {{ commentList.length }} 条评论</div>
     
      <!-- <el-scrollbar class="comment-list" > -->
        <!-- 这里将显示用户的评论 -->
        <div v-infinite-scroll="load" infinite-scroll-delay="500">
          <div v-if="commentLoading">
            <commentSkeleton v-for="item in 8"  :key="item"  />
          </div>
          <div v-else>
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
              :user="comment.author"
              :videoOwnerId="props.videoPropsPanel.author.authorId"
            />
          </div>
        </div>
    </el-scrollbar>
    

    <div class="comment-input-area">
        <div class="func">
          <div class="like" @click="handlerLiked()" >
            <i class="iconfont icon-aixin videoIcon" v-if="!funcInfo.like.value"></i>
            <i class="iconfont icon-aixin1 videoIcon" style="color:#ff2e56" v-if="funcInfo.like.value"></i>
            <div>{{ funcInfo.like.num }}</div>
          </div>
          <div class="collect" @click="handlerCollected()">
            <i class="iconfont icon-shoucang videoIcon" v-if="!funcInfo.collect.value"></i>
            <i class="iconfont icon-jiaxingshoucangtianchong videoIcon" style="color: #feba28;" v-else></i>
            <div>{{ funcInfo.collect.num }}</div>
          </div>
          <div class="common">
            <i class="iconfont icon-pinglun videoIcon"></i>
            <div>{{ funcInfo.common.num }}</div>
          </div>
          <div class="transpond">
            <i class="iconfont icon-zhuanfa_3 videoIcon"></i>
            <div>{{ funcInfo.transpond.num }}</div>
          </div>
        </div>
       <div class="input">
        <input type="text" v-model="content" placeholder="留下你精彩的评论吧..." @keyup.enter="publish">
        <button @click="publish" :disabled="disabled">发布</button>
       </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, onMounted, watch, reactive} from 'vue'
import commentItem from '@/components/Comment/commentItem'
import commentSkeleton from '@/components/Skeleton/comment-skeleton'
import { publisComment } from '@/api/video'

import { userStore } from '@/store'
import { doLikeApi,unLikeApi, doCollectApi, unCollectApi, getVideoCommentList } from '@/api/video'
const props = defineProps({
  videoId: String,
  videoPropsPanel: Object
})
const commentList = ref([])
const funcInfo = reactive({
  like: {
    value: props.videoPropsPanel.liked,
    num: props.videoPropsPanel.likeCount
  },
  common: {
    num: props.videoPropsPanel.commentCount
  },
  collect: {
    value: props.videoPropsPanel.favored,
    num: props.videoPropsPanel.favorCount,
  },
  transpond: {
    num: 0
  }
})

const content = ref("")
const disabled = ref(true)
const commentListCurrent = ref(1)
const commentLoading = ref(false)

const videoUserId = ref(props.videoPropsPanel.author.authorId)
const mineUserId = ref(userStore.userInfo.authorId)

onMounted(()=>{
  fetchCommentList()
})

watch(() => content.value, (val) => {
  disabled.value = val.trim() !== '' ? false : true;
})



// 懒加载触发事件
const load = () => {
  fetchCommentList()
}

// 发布新评论
const publish = () => {
  if(content.value.trim() !== '') {
    publisComment(props.videoId, content.value.trim()).then(res=>{
      if(res.status == 200) {
        commentList.value.unshift(res.data)
        content.value = ""
      }
    })
  }
}

// 视频点赞
const handlerLiked = () => {
  let liked = funcInfo.like.value;
  if(liked) {
    unLikeApi(props.videoId).then(res=>{
      if(res.status === 200) {
        funcInfo.like.num--;
      }
    }) 
  } else {
    doLikeApi(props.videoId).then(res=>{
      if(res.status === 200) {
        funcInfo.like.num++;
      }
    })
  }
  funcInfo.like.value = !liked;
}
// 视频关注
const handlerCollected = () => {
  let collectValue = funcInfo.collect.value;
  if(collectValue) {
    // 如果已经收藏 就取消收藏
    unCollectApi(props.videoId).then(res=>{
      if(res.status == 200) {
        funcInfo.collect.num--;
        funcInfo.collect.value = !collectValue;
      }
    })
  } else {
    doCollectApi(props.videoId).then(res=>{
      if(res.status==200) {
        funcInfo.collect.num++;
        funcInfo.collect.value = !collectValue;
      }
    })
  }
}

// 获取评论列表
const fetchCommentList = () => {
  if(commentListCurrent.value == 1) {
    commentLoading.value = true;
  }
  if((funcInfo.common.num > commentList.value.length) 
    || (funcInfo.common.num == 0) && commentListCurrent.value == 1) {
    getVideoCommentList({
      videoId: props.videoId,
      current: commentListCurrent.value++,
    }).then(res=>{
      if(res.success) {
        commentList.value = commentList.value.concat(res.data.records);
        funcInfo.common.num = parseInt(res.data.total);
        commentLoading.value = false;
      }
    })
  } 
}


</script>


<!-- comment-panel -->
<style scoped>
.comment-panel {
  height: 100%;
  width: 300px;
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
.userAvatar {
  height: 40px;
  width: 40px;
  overflow: hidden;
  border-radius: 50%;
  margin-right: 10px;
}
.userAvatar img {
  height: 100%;
  width: 100%;
}
.username {
  flex: 1;
}
.focus {
  border-radius: 30px;
}
.comment-count {
  color: #d5d5d5;
  font-size: 12px;
}

.close-btn {
  background: none;
  border: none;
  cursor: pointer;
}

.comment-list-box {
  padding: 10px 0 20px 0;
  height: 100%;
  overflow: auto;
}


.comment-input-area {
  display: flex;
  padding: 10px;
  border-top: 1px solid #e0e0e0;
  flex-direction: column;
}

.comment-input-area .input {
  display: flex;
}
.comment-input-area .input input{
    flex: 1;
    padding: 5px;
    border: none;
    border-radius: 5px;
    margin-right: 10px;
}

.comment-input-area .input button {
    background-color: #eb4553; 
    color: #fff;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
}
.comment-input-area .input button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}
.commentItem {
  margin-bottom: 5px;
}
</style>


<style scoped lang="scss">

.func {
  display: flex;
  justify-content: space-between;

  div {
    display: flex;
    align-items: center; 
    margin: 3px 0;
    text-align: center;
    user-select: none;
    cursor: pointer;
  }

  .videoIcon {
    font-size: 22px;
    margin-right: 5px;
  }
}
</style>
