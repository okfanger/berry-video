import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/style/reset.css'
import '@/style/user-defined.css'

import router from './router'
import { createPinia } from 'pinia'
import { vScroll, vSlideIn } from '@/directive'

const pinia = createPinia()
const app = createApp(App)
app.use(ElementPlus)


app.directive("scroll", vScroll)
app.directive("slide-in", vSlideIn)
app.use(router)
app.use(pinia)
app.mount('#app')
