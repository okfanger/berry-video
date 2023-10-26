<template>
  <div class="login-container" >
    <div class="qrcode" v-loading="loading">
      <img :src="url" alt="">
    </div>
    <div class="message">
      <div >请使用微信扫描二维码登录</div>
      <div>首次使用需先关注公众号</div>
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted} from 'vue'
import { getQrCode, getLoginState } from '@/api/user'
const url = ref("")
const loading = ref(false)
const id = ref("")
onMounted(() => {
  getQrCode().then(res=>{
    const { data, status} = res;
    if(status === 200) {
      const str = `https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${data.ticket}`
      id.value = data.id
      url.value = str
      // loading.value = true
      poll(fetchUserLoginState).then((res, rej)=>{
        console.log(res);
      })
    }
  }).catch(e=>{
    throw e;
  })
})

function poll(fn, timeout = 30000, interval = 100) {
  let startTime = Date.now();
  return new Promise((resolve, reject) => {
    const checkCondition = () => {
      const result = fn();
      if (result) {
        resolve(result);
      } else if (Date.now() - startTime >= timeout) {
        reject("二维码已失效")
      } else {
        setTimeout(checkCondition, interval);
      }
    };
    checkCondition();
  });
}

const fetchUserLoginState = () => {
  getLoginState(id.value).then(res=>{
    return res;
    /*
      0: 用户未扫码
      token: 用户已登录
      -1: 二维码过期
    */
  })
}

</script>

<style scoped>
.login-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
.qrcode {
  height: 60%;
  width: 60%;
}
.qrcode img {
  height: 100%;
  width: 100%;
}
</style>