<template>
  <el-breadcrumb separator="/">
    <!-- 首页面包屑 -->
    <!-- <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item> -->
    
    <!-- 路由匹配的面包屑 -->
    <el-breadcrumb-item 
      v-for="(item, index) in routeBreadcrumbs" 
      :key="item.path"
      :to="index < routeBreadcrumbs.length - 1 ? { path: item.path } : undefined"
    >
      {{ item.meta?.breadcrumb || item.name || '页面' }}
    </el-breadcrumb-item>
    
    <!-- 动态页面面包屑 -->
    <el-breadcrumb-item 
      v-if="currentPage && !routeBreadcrumbs.length"
      :to="{ path: currentPage.path }"
    >
      {{ currentPage.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { usePageStore } from '@/stores/pageStore';
import { storeToRefs } from 'pinia';

const route = useRoute();
const pageStore = usePageStore();
const { currentPage } = storeToRefs(pageStore);

// 计算路由面包屑
const routeBreadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta?.breadcrumb);
  console.log('Route matched:', route.matched);
  console.log('Route breadcrumbs:', matched);
  return matched;
});
</script>

<style scoped>
.el-breadcrumb {
  padding: 15px 20px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  margin-bottom: 20px;
}
</style>