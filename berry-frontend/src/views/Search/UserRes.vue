<template>
  <div class="user-container">
    <div class="user-card" v-for="user in 12" :key="user">
      <div class="avatar">
        <img src="" alt="">
      </div>
      <div class="userinfo">
        <div class="username">韩剧TV</div>
        <div class="position">中国</div>
        <div class="videoInfo">
          <div>7237.2万赞</div>
          <div>47万粉丝</div>
        </div>
      </div>
      <div>
        <div class="button-plain">已关注</div>
        <div class="button-primary">关注</div>
        <div class="button-plain">互关</div>
      </div>
    </div>
 </div>
</template>

<script setup>
import { defineProps } from 'vue'
const props = defineProps(['userList'])
// 检查浏览器是否支持 Geolocation API
if ("geolocation" in navigator) {
  // 获取位置信息
  navigator.geolocation.getCurrentPosition(function(position) {
    // 获取经纬度信息
    const latitude = position.coords.latitude;
    const longitude = position.coords.longitude;
    
    // 输出经纬度信息
    console.log("纬度: " + latitude);
    console.log("经度: " + longitude);
  }, function(error) {
    // 处理错误
    switch (error.code) {
      case error.PERMISSION_DENIED:
        console.log("用户拒绝共享位置信息");
        break;
      case error.POSITION_UNAVAILABLE:
        console.log("位置信息不可用");
        break;
      case error.TIMEOUT:
        console.log("获取位置信息超时");
        break;
      case error.UNKNOWN_ERROR:
        console.log("发生未知错误");
        break;
    }
  });
} else {
  console.log("浏览器不支持 Geolocation API");
}

</script>

<style scoped lang="scss">
.user-container {
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;

  .user-card {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 10px;
    height: 112px;
    cursor: pointer;
    border-radius: 10px;

    &:hover {
      background-color: #f7f7f7;
    }

    .avatar {
      height: 72px;
      width: 72px;
      border-radius: 50%;
      overflow: hidden;
      margin-right: 20px;
      
      img {
        height: 100%;
        width: 100%;
      }
    }

    .userinfo {
      flex: 1;

      .videoInfo {
        display: flex;

        div {
          margin: 0 10px 0 0;
        }
      }
    }


    
  }
}
</style>