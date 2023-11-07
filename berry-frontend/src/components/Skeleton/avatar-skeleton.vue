<template>
  <div>
    <el-skeleton :style="`--el-skeleton-circle-size: ${props.size}`">
      <template #template>
        <el-skeleton-item variant="circle" />
      </template>
    </el-skeleton>
 </div>
</template>

<script setup>
import { defineProps } from 'vue';
const props = defineProps({
  size: String
})
</script>

<style scoped>

</style>