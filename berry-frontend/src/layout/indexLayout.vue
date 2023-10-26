<template>
  <div class="common-layout">
    <el-container>
      <el-header style="padding: 0;height: 80px;">
        <LHeader  />
      </el-header>
      <el-container>
        <el-aside width="200px" style="height: calc(100vh - 80px);background-color: aqua;">
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
  background-color: rgb(250, 167, 115);
}
</style>