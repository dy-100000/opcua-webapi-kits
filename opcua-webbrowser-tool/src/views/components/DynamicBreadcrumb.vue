<template>
  <el-breadcrumb separator="/">
    <!-- 首页面包屑 -->
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    
    <!-- 动态页面面包屑 -->
    <el-breadcrumb-item 
      v-if="currentPage"
      :to="{ path: currentPage.path }"
    >
      {{ currentPage.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { usePageStore } from '@/stores/pageStore';
import { storeToRefs } from 'pinia';

const pageStore = usePageStore();
const { currentPage } = storeToRefs(pageStore);
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