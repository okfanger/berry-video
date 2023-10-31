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
          MenuName: "推荐"
        },
        component: () => import("@/views/Recommend/indexView.vue")
      },
      {
        path: "classify",
        name: 'classify',
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "",
          MenuName: "分类"
        },
      },
      {
        path: "mine",
        name: 'mine',
        component: () => import("@/views/Mine/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "",
          MenuName: "我的"
        },
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
        component: () => import("@/views/File/updateFile.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "",
          MenuName: "内容管理"
        },
      },
      {
        path: "discount",
        name: "discount",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "Discount",
          MenuName: "娱乐"
        },
      },
      {
        path: "trophyBase",
        name: "trophyBase",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "TrophyBase",
          MenuName: "二次元"
        },
      },
      {
        path: "knifeFork",
        name: "knifeFork",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "KnifeFork",
          MenuName: "美食"
        },
      },
      {
        path: "music",
        name: "music",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "Service",
          MenuName: "音乐"
        },
      },
      {
        path: "basketball",
        name: "basketball",
        component: () => import("@/views/Classify/indexView.vue"),
        meta: {
          isMenu: true,
          MenuIcon: "Basketball",
          MenuName: "体育"
        },
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
