import { createApp, h, provide, DirectiveBinding } from "vue";
import { createPinia } from 'pinia'

import App from './App';
import './index.css';
import RightClickMenu from '@/views/components/Menu/index.vue'; // 导入组件
import VDrag from 'v-drag';
import process from 'process';
import { EventEmitter } from 'events';

// 修复 createFastUninitializedBuffer 问题
if (typeof window !== 'undefined') {
  window.Buffer = Buffer;
  window.process = process;
  window.global = window;
  window.EventEmitter = EventEmitter;

  // 手动修复缺失的函数
  if (typeof window.createFastUninitializedBuffer === 'undefined') {
    window.createFastUninitializedBuffer = function (size) {
      return Buffer.allocUnsafe(size);
    };
  }
}
// 如果您正在使用CDN引入，请删除下面一行。
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// import * as Icons from 'element-plus/lib/icons';
import { apolloClient } from "@/utils/applo_setting"
import * as go from 'gojs';
const gojsDirective = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    if (!binding.value) return;
    const $gojs = new go.Diagram(el);
    Object.assign($gojs, binding.value);
  }
};

import { DefaultApolloClient } from "@vue/apollo-composable";
import ElementPlus from 'element-plus';
import VXETable from 'vxe-table';
import 'vxe-table/lib/style.css';

import 'element-plus/dist/index.css';
import './iconfont.js';
// import Antd from 'ant-design-vue'
// import 'ant-design-vue/dist/antd.css'
// import VlTree from '@sangtian152/virtual-tree';
// import "@sangtian152/virtual-tree/lib/vl-tree.css";
import router from '@/router/index'
import { initGlobalErrorHandling } from '@/utils/utils'
import eruda from 'eruda'

// 开发环境一定展示（Vite 写法）
// if (import.meta.env.DEV) {   // 等价于 NODE_ENV === 'development'
//   eruda.init({
//     tool: ['console', 'elements', 'network'],
//     useShadowDom: true
//   })
// }
// 初始化全局错误处理
initGlobalErrorHandling();

const app = createApp({
  setup() {
    provide(DefaultApolloClient, apolloClient);
  },
  render: () => h(App),
})
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
// for (const [key, component] of Object.entries(Icons)) {
//   app.component(key, component)
// }
app.component('right-click-menu', RightClickMenu); // 全局注册
// 注册自定义指令
// app.directive('drags', (el, binding, vnode) => {
//   let dragging = false;
//   let offset = [0, 0];
//   el.style.position = 'absolute';
//   el.style.left = `${el.offsetLeft}px`;
//   el.style.top = `${el.offsetTop}px`;

//   el.addEventListener('mousedown', function(e) {
//     dragging = true;
//     offset = [e.clientX - el.offsetLeft, e.clientY - el.offsetTop];
//     document.addEventListener('mousemove', mouseMoveHandler);
//     document.addEventListener('mouseup', mouseUpHandler);
//   });

//   function mouseMoveHandler(e) {
//     if (dragging) {
//       let x = e.clientX - offset[0];
//       let y = e.clientY - offset[1];
//       el.style.left = `${x}px`;
//       el.style.top = `${y}px`;
//     }
//   }

//   function mouseUpHandler() {
//     dragging = false;
//     document.removeEventListener('mousemove', mouseMoveHandler);
//     document.removeEventListener('mouseup', mouseUpHandler);
//   }

//   // 移除边界检查函数
//   // function isOverlapping(el1, el2) {
//   //   const rect1 = el1.getBoundingClientRect();
//   //   const rect2 = el2.getBoundingClientRect();
//   //   return !(rect1.left + rect1.width < rect2.left ||
//   //            rect1.right > rect2.right ||
//   //            rect1.top + rect1.height < rect2.top ||
//   //            rect1.bottom > rect2.bottom);
//   // }
// });
app.directive('drags', (el) => {
  let dragging = false;
  let offset = [0, 0];
  const originalZIndex = el.style.zIndex ? parseInt(el.style.zIndex, 10) : 0;
  el.style.position = 'absolute';

  el.addEventListener('mousedown', function (e) {
    e.preventDefault(); // 阻止默认事件，例如文本选择
    dragging = true;
    offset = [e.clientX - el.offsetLeft, e.clientY - el.offsetTop];
    el.style.zIndex = '999999999999'; // 设置一个较高的z-index值，确保元素在顶层
    document.addEventListener('mousemove', mouseMoveHandler);
    document.addEventListener('mouseup', mouseUpHandler);
  });

  function mouseMoveHandler(e) {
    if (dragging) {
      let x = e.clientX - offset[0];
      let y = e.clientY - offset[1];
      el.style.left = `${x}px`;
      el.style.top = `${y}px`;
    }
  }

  function mouseUpHandler() {
    dragging = false;
    document.removeEventListener('mousemove', mouseMoveHandler);
    document.removeEventListener('mouseup', mouseUpHandler);
  }
});
app.use(ElementPlus);
app.use(VXETable);
app.use(createPinia());
app.use(VDrag);
// app.use(VlTree);
app.use(router);

app.directive('gojs', gojsDirective);
app.mount('#app');




