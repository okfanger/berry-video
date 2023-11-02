<template>
  <div >
    <div class="input-box custom-input">
      <input type="text" placeholder="搜索">
      <div class="button-primary search">搜索</div>
    </div>
 </div>
</template>

<script setup>

</script>

<style scoped>
.input-box {
  display: flex;
  align-items: center;
  width: 400px;
  padding: 0 2px 0 10px;
}
.input-box input {
  flex: 1;
  padding: 0 10px 0 0;
}
.input-box:focus-within  .search{
  opacity: 1;
  visibility: visible;
  transition: 1s;
}
.search {
  transform: none;
  opacity: 1;    
  visibility: visible; 
  transition: opacity 0.1s, visibility 0.1s; 
  padding: 0px 20px;
}
</style>