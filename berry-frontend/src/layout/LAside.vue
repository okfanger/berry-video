<template>
  <div class="aside-container">
      <el-menu
        :default-active="activate"
        class="el-menu-vertical-demo"
        @select="changeMenu"
        height="100%"
      >
        <el-menu-item  v-for="item in routes" 
          :key="item.name"
          :index="item.name"
          >
          <span>{{ item.meta.MenuName }}</span>
        </el-menu-item>
      </el-menu>
 </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { userStore } from '@/store';
const router = useRouter()
const route = useRoute()

const activate = ref(route.name)
const routes = computed(()=>{
  return router.getRoutes()
          .filter(route=>route.meta.isMenu)
          .sort((a,b)=>a.meta.index - b.meta.index)
})
const changeMenu = (pathName) => {
  if(pathName=='user') {
    const userId = userStore.userInfo.authorId;
    router.push(`/user/self`)
  } else {
    router.push({
      name: pathName
    })
  }
 
}

</script>

<style scoped>
.aside-container {
  height: 100%;
}
::v-deep .is-active {
  color: #ec656b;
}
::v-deep .el-menu-item:hover {
  background-color: #f9b7be;
}
</style>