import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("@/layout/indexLayout.vue"),
    children: [
      {
        path: "recommend",
        name: 'recommend',
        component: () => import("@/views/Recommend/indexView.vue")
      },
      {
        path: "classify",
        name: 'classify',
        component: () => import("@/views/Classify/indexView.vue")
      },
      {
        path: "mine",
        name: 'mine',
        component: () => import("@/views/Mine/indexView.vue"),
        children: [
          {
            path: "collect",
            name: 'collect',
            component: () => import("@/views/Collect/indexView.vue")
          },
          {
            path: "upvote",
            name: 'upvote',
            component: () => import("@/views/Upvote/indexView.vue")
          },
          {
            path: "product",
            name: "product",
            component: () => import("@/views/Product/indexView.vue")
          },
          {
            path: "/mine",
            redirect: () => "/mine/upvote"
          }
        ]
      },
      {
        path: "create",
        name: "create",
        component: () => import("@/views/File/updateFile.vue")
      },
      {
        path: "/",
        redirect: () => "/recommend"
      }
    ]
  },
  {
    path: "/WxLogin",
    name: "WxLogin",
    component: () => import("@/views/User/WxView.vue")
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
