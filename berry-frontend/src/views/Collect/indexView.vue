<template>
  <div>
    

    
  </div>
</template>

<script setup>
import { userStore } from '@/store';

userStore.fetchUserInfo()
console.log(userStore);

</script>

<style scoped>
.love {
  width: 80px;
}
</style>