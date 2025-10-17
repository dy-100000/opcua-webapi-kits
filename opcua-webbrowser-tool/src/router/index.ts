import { createRouter, createWebHistory } from 'vue-router';

// 定义路由组件  
import FirstPage from '../views/settingPage/index.vue';

// 动态路由配置


// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: FirstPage,
      meta: {
        title: '设置页面',
        breadcrumb: '首页'
      }
    },
  ]
});

// 动态添加路由的方法
export function addRoute(route: any) {
  router.addRoute(route);
}

// 动态移除路由的方法
export function removeRoute(name: string) {
  router.removeRoute(name);
}

export default router