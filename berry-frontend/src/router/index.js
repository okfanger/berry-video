import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("@/layout/indexLayout.vue"),
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
