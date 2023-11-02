<template>
  <div class="login-container" >
    <div class="title">微信扫码登录</div>
    <div class="qrcode" >
      <div class="img" v-loading="loading">
        <img :src="url || qrcodeImg" alt="" >
      </div>
      <div style="user-select: none;">微信扫码</div>
    </div>
    <div class="code" ref="inputs">
      <input
        v-for="(item, index) in 6"
        :key="index"
        ref="otpInput"
        v-model="otpCodes[index]"
        @input="handleInput($event, index)"
        type="text"
        maxlength="1"
        class="otp-input"
      />
    </div>
    <div class="message">
      <div >注册登录即代表同意《数莓用户协议》《数莓隐私政策》</div>
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, watchEffect, defineEmits, defineProps} from 'vue'
import { getQrCode, login } from '@/api/user'
import qrcodeImg from '@/assets/qrcode.png'
import { setToken } from '@/utils'
import { userStore, videoStore } from '@/store'

const props = defineProps(['LogindialogVisble'])
const emits = defineEmits(['update:LogindialogVisble'])


const url = ref("")
const otpCodes = ref(Array(6).fill(''));
const otpInput = ref([]);
const loading = ref(true)
const clientId = ref("")
const inputs = ref()

onMounted(() => {
  fetchQrCode()  
})

const fetchQrCode = () => {
  getQrCode().then(res=>{
    const { data, status} = res;
    if(status === 200) {
      const str = `https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${data.ticket}`
      url.value = str
      clientId.value = data.id
      loading.value = false;
      inputs.value.children[0].focus()
    }
  })
}

const handlerLogin = (code) => {
  const loginForm = {
    clientId:clientId.value,
    code,
  }
  login(loginForm).then(res=>{
    const {data, status} = res;
    if(status == 200) {
      setToken(data.token);
      emits("update:LogindialogVisble", false)
      userStore.fetchUserInfo();
      videoStore.fetchChannelList()
    }
  })
}



// 输入验证码
const handleInput = (event, index) => {
  const input = event.target;
  if (input.value) {
    const nextInput = otpInput.value[index + 1];
    nextInput && nextInput.focus();
  }
};

const submitOTP = () => {
  const otpValue = otpCodes.value.join('');
  handlerLogin(otpValue)
};

watchEffect(() => {
  if (otpCodes.value.every(code => code !== '')) {
    submitOTP();
  }
});

</script>

<style scoped>
.title {
  color: #1e2023;
  font-size: 20px;
  font-weight: bold;
  user-select: none;
}
.login-container {
  width: 100%;
  height: 364px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  text-align: center;
}
.qrcode {
  height: 220px;
  width: 200px;
  padding: 24px;
  background-color: #fff;
  box-shadow: 0 0 1px rgba(28,31,35,.16), 0 6px 32px 4px rgba(28,31,35,.08);
  border-radius: 12px;
}
.qrcode .img {
  height: 156px;
  width: 156px;
}
.qrcode .img img {
  height: 100%;
  width: 100%;
}

.otp-input { 
  width: 40px;
  height: 40px;
  text-align: center;
  margin: 0 5px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 0 1px rgba(28,31,35,.16), 0 6px 32px 4px rgba(28,31,35,.08);
}
.otp-input:focus {
  border: 1px solid #ec656b;
}
.code  {
  display: flex;
  justify-content: space-around;
}
.message {
  color: rgba(30,32,35,.45);
}
</style>