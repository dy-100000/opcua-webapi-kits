<template>
  <div class="dashboard-page">
    <h1>仪表盘</h1>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>系统概览</span>
          </template>
          <p>系统运行状态良好</p>
          <p>CPU使用率: 45%</p>
          <p>内存使用率: 62%</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>数据统计</span>
          </template>
          <p>今日访问量: 1234</p>
          <p>在线用户: 89</p>
          <p>系统响应时间: 120ms</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>快速操作</span>
          </template>
          <el-button type="primary" @click="goBack" style="margin-bottom: 10px;">返回</el-button>
          <br>
          <el-button type="success">刷新数据</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.push('/')
}
</script>

<style scoped>
.dashboard-page {
  height: 100%;
  overflow: auto;
}

.dashboard-page h1 {
  margin-bottom: 20px;
  color: #303133;
}

.el-card {
  height: 200px;
  margin-bottom: 20px;
}

.el-card p {
  margin: 8px 0;
  color: #606266;
}
</style> 



