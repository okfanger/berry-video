<template>
   <div class="emoji-picker">
    <emoji-mart-picker @emoji="handleEmojiSelect" />
    <input type="text" v-model="text">
  </div>
</template>

<script setup>
import 'vue-emoji-mart/css/emoji-mart.css';
import { EmojiMartPicker } from 'vue-emoji-mart';
import {ref} from 'vue'
const text =ref("")
const handleEmojiSelect = (emoji) => {
  // emit('emoji-selected', emoji.native);
  console.log(emoji);
};
</script>

<style scoped>
.emoji-picker {
  text-align: center;
}
</style>
