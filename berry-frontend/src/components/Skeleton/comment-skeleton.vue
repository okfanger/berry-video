<template>
  <div>
    <el-skeleton class="box" animated :throttle="100">
      <template #template>
        <div class="content">
          <div class="img">
            <el-skeleton-item variant="circle" style="width: 40px; height: 40px" />
          </div>
          <div class="info">
            <el-skeleton-item variant="p" style="width: 30px" />
            <el-skeleton-item variant="text" style="width: 200px;" />
            <el-skeleton-item variant="text" style="width: 100px;" />
          </div>
        </div>
      </template>
    </el-skeleton>
 </div>
</template>

<script setup>

</script>

<style scoped lang="scss">
.content {
  display: flex;
  height: 60px;
  margin-bottom: 10px;

  .info {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
  }
  
}
</style>