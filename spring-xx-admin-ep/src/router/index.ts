// Composables
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes:RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Authenticated',
    // authenticated
    component: () => import('@/layouts/authenticated/Authenticated.vue'),
    children: [
      {
        path: '',
        name: '首页',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "home" */ '@/views/Home.vue'),
      },
      {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('@/views/NotFound.vue'),
      },
    ],
  },
  {
    path: '/login',
    name: '登录',
    component: () => import("@/views/Login.vue"),
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router
