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
        meta: {
          isMenu: true,
          MenuIcon: "",
          MenuName: "推荐",
          index: 1
        },
        component: () => import("@/views/Recommend/indexView.vue")
      },
      {
        path: "user/:userId?",
        name: 'user',
        component: () => import("@/views/Mine/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "",
          MenuName: "我的",
          index: 2
        },
      },
      {
        path: "manager",
        name: "manager",
        component: () => import("@/views/Mine/ManagerView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "",
          MenuName: "内容管理",
          index: 3
        },
      },
      {
        path: "discount",
        name: "discount",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "Discount",
          MenuName: "娱乐",
          index: 4
        },
      },
      {
        path: "trophybase",
        name: "trophybase",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "TrophyBase",
          MenuName: "二次元",
          index: 5
        },
      },
      {
        path: "service",
        name: "service",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "Service",
          MenuName: "美食",
          index: 6
        },
      },
      {
        path: "knifefork",
        name: "knifefork",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "KnifeFork",
          MenuName: "音乐",
          index: 7
        },
      },
      {
        path: "basketball",
        name: "basketball",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "Basketball",
          MenuName: "体育",
          index: 8
        },
      },
      {
        path: "search",
        name: "search",
        component: () => import("@/views/Search/searchIndex.vue"),
        meta: {
          isMenu: false,
        },
      },
      {
        path: "/",
        redirect: () => "/recommend"
      }
    ]
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
