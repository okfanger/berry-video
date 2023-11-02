<template>
  <div class="comment-item">
    <div class="avatar">
      <img :src="props.authorAvatar" alt="User Avatar" />
    </div>
    <div class="content">
      <div class="username">{{ props.authorNickName }}</div>
      <div class="text">{{ props.content }}</div>
      <div class="date">{{ parseTime(props.createTime) }}</div>
      <div>
        <div>
          <i class="iconfont icon-aixin" style="cursor: pointer;" v-if="!likeInfo.isLiked" @click="handlerLike()"></i> 
          <i class="iconfont icon-aixin1" style="cursor: pointer;color:#ff2f57" v-else
             @click="handlerLike()"></i> 
          <span>{{ likeInfo.likeCount }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref, reactive } from 'vue';
import { doLikeForCommentApi, unLikeForCommentApi } from '@/api/video'
import { parseTime } from '@/utils'
const props = defineProps({
  authorNickName: String,
  authorAvatar: String,
  isLiked: String,
  likeCount: Number,
  createTime: String,
  id: String,
  content: String
});
const likeInfo = reactive({
  isLiked: props.isLiked,
  likeCount: props.likeCount
})

const handlerLike = () => {
  if(likeInfo.isLiked) {
    unLikeForCommentApi(props.id).then(res=>{
      if(res.success) {
        likeInfo.isLiked = false;
        likeInfo.likeCount--;
      }
    })
  } else {
    doLikeForCommentApi(props.id).then(res=>{
      if(res.success) {
        likeInfo.isLiked = true;
        likeInfo.likeCount++;
      }
    })
  }
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