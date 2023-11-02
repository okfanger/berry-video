<template>
  <div class="common-layout gradient-background">
    <el-container>
      <el-header style="padding: 0;height: 80px;">
        <el-affix :offset="0">
          <LHeader  />
        </el-affix>
      </el-header>
      <el-container>
        <el-aside width="120px" style="height: calc(100vh - 80px);">
          <LAside />
        </el-aside>
        <el-main>
          <LMain />
        </el-main>
      </el-container>
    </el-container>

   
  </div>
</template>

<script setup>
import LHeader from './LHeader.vue';
import LMain from './LMain.vue';
import LAside from '@/layout/LAside'


</script>

<style scoped>
.common-layout {
  height: 100vh;
  min-width: 800px;
}
</style>