<template>
  <div class="comment-item">
    <!-- <div class="avatar">
      <img :src="props.authorAvatar" alt="User Avatar" />
    </div> -->
    <userCardInfo :width="30" :height="30" :user="props.user" />
    <div class="content">
      <div class="username">
        {{ props.authorNickName }} 
        <span class="authorTag tag" v-if="props.user.authorId === props.videoOwnerId">作者</span>
        <span class="friendTag tag" v-if="props.user.action.friend">朋友</span>
      </div>
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
import { defineProps, reactive } from 'vue';
import { doLikeForCommentApi, unLikeForCommentApi } from '@/api/video'
import { parseTime } from '@/utils'
import userCardInfo from '@/components/User/userCardInfo.vue';
const props = defineProps({
  authorNickName: String,
  authorAvatar: String,
  isLiked: String,
  likeCount: Number,
  createTime: String,
  id: String,
  content: String,
  user: Object,
  videoOwnerId: String
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
  margin-top: 10px;

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
      color: rgba(51,51,51,0.6);
      line-height: 18px;
      font-size: 13px;

      .tag {
        font-size: 10px;
        padding: 1px 3px;
        margin-left: 5px;
        border-radius: 2px;
      }
      .authorTag {
        background-color: rgb(254, 44, 85);
        color: white;
      }
      .friendTag {
        background-color: rgb(217, 218, 229);
        color: #333;
      }
    }
    .text {
      font-size: 16px;
      color: #333;
      margin-top: 4px;
    }
    .date{
      font-size: 12px;
      margin-top: 4px;
    }
  }
}




</style>