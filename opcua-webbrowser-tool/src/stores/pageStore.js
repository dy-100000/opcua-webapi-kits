import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
export const usePageStore = defineStore('pages', () => {
  // 存储所有动态页面
  const pages = ref([
    { 
      id: 1, 
      title: '仪表盘', 
      path: '/dashboard', 
      content: '这是仪表盘页面，显示系统概览信息。' 
    }
  ]);
  
  // 当前活动页面ID
  const currentPageId = ref(null);
  
  // 当前页面对象
  const currentPage = computed(() => {
    return pages.value.find(page => page.id === currentPageId.value);
  });
  
  // 添加新页面
  function addPage(page) {
    const newPage = {
      ...page,
      id: Date.now(), // 使用时间戳作为唯一ID
      createdAt: new Date().toLocaleString()
    };
    pages.value.push(newPage);
    return newPage;
  }
  
  // 删除页面
  function removePage(id) {
    const index = pages.value.findIndex(page => page.id === id);
    if (index !== -1) {
      pages.value.splice(index, 1);
      if (currentPageId.value === id) {
        currentPageId.value = null;
      }
    }
  }
  
  // 设置当前页面
  function setCurrentPage(id) {
    currentPageId.value = id;
  }
  
  // 重置所有页面
  function resetPages() {
    pages.value = [];
    currentPageId.value = null;
  }
  
  return {
    pages,
    currentPageId,
    currentPage,
    addPage,
    removePage,
    setCurrentPage,
    resetPages
  };
});