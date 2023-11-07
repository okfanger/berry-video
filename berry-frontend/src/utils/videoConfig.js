import { ref } from 'vue'
const continuous = ref(false)
const muted = ref(true)
const isCommoning = ref(true)
export {
  // 是否连播
  continuous,
  // 是否静音
  muted,
  // 是否打开评论面板
  isCommoning,
}