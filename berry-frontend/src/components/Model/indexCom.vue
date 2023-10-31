<template>
  <div class="model">
    <el-dialog 
      v-model="innerVisble"
      align-center
      :close-on-click-modal="false"
      @close="closeModel"
    >
      <slot></slot>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref,watch, defineProps, defineEmits} from 'vue'
const props = defineProps(['visble'])
const emits = defineEmits("update:visble")

const innerVisble = ref(props.visble)

watch(() => props.visble, (val)=>{
  innerVisble.value = val;
})
const closeModel = () => {
  emits("update:visble", false)
}

</script>

<style scoped>
::v-deep .el-dialog{
  border-radius: 12px;
  min-width: 489px;
  min-height: 480px;
  background-color: #fdfdfd;
}
</style>