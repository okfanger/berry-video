<template>
  <div >
    <div class="input-box custom-input">
      <input type="text" 
        placeholder="搜索"
        v-model="searchQuery"
        @input="handleInput"
        @focus="visibleSuggestions"
        >
      <div class="button-primary search">搜索</div>
    </div>
    
    <div v-show="hasVal" class="search-suggestions">
      <div v-if="suggestions.length > 0">
        <div
          v-for="(suggestion, index) in suggestions"
          :key="index"
          class="suggestion-item"
          @click="selectSuggestion(suggestion)"
        >
          {{ suggestion }}
        </div>
      </div>
    </div>

    <!-- 历史搜索记录 -->
    <div v-show="!hasVal">
      
    </div>
 </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const searchQuery = ref('');
const showSuggestions = ref(false);

const suggestions = ref([]);

const hasVal = computed(() =>{
  return searchQuery.value.trim() !== ''
})

const visibleSuggestions = (e) => {
  showSuggestions.value = true;
}


const handleInput = () => {
  // 这里调用 api 展示数据
  console.log(searchQuery);
  setTimeout(()=>{
    suggestions.value = ['池昌旭', '李钟硕', '宋仲基', '车银优', '朴宝剑']
  }, 2000)
}
function selectSuggestion(suggestion) {
  console.log(suggestion);
  searchQuery.value = suggestion;
  showSuggestions.value = false;
}

</script>

<!-- input样式 -->
<style scoped>
.input-box {
  display: flex;
  align-items: center;
  width: 400px;
  padding: 0 2px 0 10px;
}
.input-box input {
  flex: 1;
  padding: 0 10px 0 0;
}
.search {
  opacity: 1;    
  padding: 0px 20px;
}
</style>

<style scoped>
/* 弹出提示面板容器 */
.search-suggestions {
  position: absolute;
  background-color: #fff;
  border: 1px solid rgba(236, 101, 107, 0.2);
  box-shadow: 0 4px 6px rgba(236, 101, 107, 0.1);
  width: 400px;
  max-height: 300px;
  overflow-y: auto;
  display: block;
  z-index: 9999;
  border-radius: 5px;
}

/* 搜索提示项 */
.suggestion-item {
  padding: 10px 15px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  color: #1c1f23;
  background-color: #fff;
}

.suggestion-item:hover {
  background-color: rgba(236, 101, 107, 0.15);
  box-shadow: 0 6px 10px rgba(236, 101, 107, 0.2);
}

/* 分隔线 */
.suggestion-divider {
  border-top: 1px solid rgba(236, 101, 107, 0.2);
  margin: 5px 0;
}

/* 没有结果时的消息 */
.no-results-message {
  padding: 10px 15px;
  color: #ec656b;
  opacity: 0.7;
  background-color: #fff;
}



</style>