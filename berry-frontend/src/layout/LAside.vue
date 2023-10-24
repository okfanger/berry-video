<template>
  <div class="aside-container">
      <el-menu
        :default-active="activatedMenu"
        class="el-menu-vertical-demo"
      >
        <el-menu-item index="1">
          <el-icon><icon-menu /></el-icon>
          <span>推荐</span>
        </el-menu-item>
        <el-menu-item index="2">
          <el-icon><icon-menu /></el-icon>
          <span>分类</span>
        </el-menu-item>
        
      </el-menu>
    
 </div>
</template>

<script setup>
import { ref } from 'vue'
const activatedMenu = ref(1)
</script>

<style scoped>
.aside-container {
  height: 100%;
}
</style>