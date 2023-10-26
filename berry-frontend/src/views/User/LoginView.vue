<template>
  <div class="login-container" >
    {{ LogindialogVisble }}
    <div class="qrcode" v-loading="loading">
      <img :src="url || qrcodeImg" alt="" >
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
      <div >请使用微信扫描二维码登录</div>
      <div>首次使用需先关注公众号</div>
    </div>
 </div>
</template>

<script setup>
import { ref, onMounted, watchEffect, defineEmits, defineProps} from 'vue'
import { getQrCode, login } from '@/api/user'
import qrcodeImg from '@/assets/qrcode.png'
import { setToken } from '@/utils'

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

.otp-input { 
  width: 40px;
  height: 40px;
  text-align: center;
  margin: 0 5px;
  border: 1px solid #ccc;
}
.code  {
  display: flex;
  justify-content: space-around;
}
</style>