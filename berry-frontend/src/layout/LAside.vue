<template>
  <div class="aside-container">
      <el-menu
        :default-active="activate"
        class="el-menu-vertical-demo"
        @select="changeMenu"
        height="100%"
      >
        <el-menu-item index="recommend">
          <el-icon><icon-menu /></el-icon>
          <span>推荐</span>
        </el-menu-item>
        <el-menu-item index="classify">
          <el-icon><icon-menu /></el-icon>
          <span>分类</span>
        </el-menu-item>
        <el-menu-item index="mine">
          <el-icon><icon-menu /></el-icon>
          <span>我的</span>
        </el-menu-item>
        <el-menu-item index="create">
          <el-icon><icon-menu /></el-icon>
          <span>创作</span>
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