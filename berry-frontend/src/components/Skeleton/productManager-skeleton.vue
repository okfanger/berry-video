<template>
  <div>
    <el-skeleton class="box" animated :throttle="100">
      <template #template>
        <div class="content">
          <div class="img">
            <el-skeleton-item variant="image" style="width: 164px; height: 90px" />
          </div>
          <div class="info">
            <el-skeleton-item variant="p" style="width: 30px" />
            <el-skeleton-item variant="text" style="width: 100px;" />
            <div class="counter">
              <el-skeleton-item variant="text" style="width: 25px" />
              <el-skeleton-item variant="text" style="width: 25px" />
              <el-skeleton-item variant="text" style="width: 25px" />
            </div>
          </div>
          <div class="actions"> 
            <el-skeleton-item variant="button" style="width: 80px; height: 30px" />
          </div>
        </div>
      </template>
    </el-skeleton>
 </div>
</template>

<script setup>

</script>

<style scoped lang="scss">
.box {
  height: 100px;
  border-bottom: 1px solid #e7e7e7;
  margin: 10px 0 20px 0;
  .content {
    display: flex;
    
    .img {
      width: 164px;
      margin-right: 20px;
    }
    .info {
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: start;
      flex: 1;

      .counter {
        width: 100px;
        display: flex;
        justify-content: space-around;
      }
    }

    .actions {
      width: 200px;
      display: flex;
      align-items: center;
      justify-content: end;
      padding-right: 10px;
    }
  
  }
}
</style>