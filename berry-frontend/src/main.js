import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/utils/reset.css'
import router from './router'

import { vScroll, vSlideIn } from '@/directive'


const app = createApp(App)
app.use(ElementPlus)


app.directive("scroll", vScroll)
app.directive("slide-in", vSlideIn)
app.use(router)
app.mount('#app')
