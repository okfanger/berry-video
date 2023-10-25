<template>
  <div class="aside-container">
      <el-menu
        :default-active="activate"
        class="el-menu-vertical-demo"
        @select="changeMenu"
      >
        <el-menu-item index="recommend">
          <el-icon><icon-menu /></el-icon>
          <span>推荐</span>
        </el-menu-item>
        <el-menu-item index="classify">
          <el-icon><icon-menu /></el-icon>
          <span>分类</span>
        </el-menu-item>
        <el-menu-item index="upvote">
          <el-icon><icon-menu /></el-icon>
          <span>喜欢</span>
        </el-menu-item>
        <el-menu-item index="collect">
          <el-icon><icon-menu /></el-icon>
          <span>收藏</span>
        </el-menu-item>
      </el-menu>
    
 </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()

const activate = ref(route.name)

// const path = 
const changeMenu = (pathName) => {
  router.push({
    name: pathName
  })
}

</script>

<style scoped>
.aside-container {
  height: 100%;
}
</style>