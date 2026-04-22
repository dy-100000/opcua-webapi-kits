<template>
  <div class="console-demo">
    <h2>控制台组件使用示例</h2>
    
    <!-- 控制台组件 -->
    <div class="console-container">
      <ConsolePanel ref="consoleRef" />
    </div>
    
    <!-- 测试按钮区域 -->
    <div class="test-controls">
      <h3>测试控制台功能</h3>
      <div class="button-group">
        <el-button @click="testLog" type="primary">测试普通日志</el-button>
        <el-button @click="testWarn" type="warning">测试警告</el-button>
        <el-button @click="testError" type="danger">测试错误</el-button>
        <el-button @click="testDebug" type="info">测试调试</el-button>
        <el-button @click="testObject" type="success">测试对象</el-button>
        <el-button @click="testMultiple" type="default">测试多条</el-button>
      </div>
      
      <div class="button-group">
        <el-button @click="clearConsole" :icon="Delete">清空控制台</el-button>
        <el-button @click="exportConsole" :icon="Download">导出日志</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import ConsolePanel from './ConsolePanel.vue'
import { Delete, Download } from '@element-plus/icons-vue'

// 控制台组件引用
const consoleRef = ref()

// 测试普通日志
const testLog = () => {
  console.log('这是一条普通的信息日志')
  console.log('包含多个参数:', '字符串', 123, true, { name: 'test' })
}

// 测试警告
const testWarn = () => {
  console.warn('这是一条警告信息')
  console.warn('警告：检测到潜在问题', { code: 'W001', message: '资源使用率过高' })
}

// 测试错误
const testError = () => {
  console.error('这是一条错误信息')
  console.error('发生错误:', new Error('这是一个示例错误'))
  
  // 测试异步错误
  setTimeout(() => {
    try {
      throw new Error('异步错误示例')
    } catch (error) {
      console.error('捕获到异步错误:', error)
    }
  }, 1000)
}

// 测试调试
const testDebug = () => {
  console.debug('这是一条调试信息')
  console.debug('调试数据:', {
    timestamp: Date.now(),
    user: 'admin',
    action: 'test_debug',
    metadata: {
      version: '1.0.0',
      environment: 'development'
    }
  })
}

// 测试对象
const testObject = () => {
  const complexObject = {
    user: {
      id: 1,
      name: '张三',
      email: 'zhangsan@example.com',
      profile: {
        avatar: 'https://example.com/avatar.jpg',
        bio: '这是一个测试用户',
        settings: {
          theme: 'dark',
          language: 'zh-CN',
          notifications: true
        }
      }
    },
    permissions: ['read', 'write', 'admin'],
    lastLogin: new Date(),
    isActive: true
  }
  
  console.log('复杂对象示例:', complexObject)
}

// 测试多条日志
const testMultiple = () => {
  for (let i = 1; i <= 5; i++) {
    console.log(`批量日志 ${i}:`, {
      index: i,
      message: `这是第 ${i} 条日志`,
      timestamp: Date.now() + i
    })
  }
  
  console.warn('批量测试完成')
}

// 清空控制台
const clearConsole = () => {
  if (consoleRef.value) {
    consoleRef.value.clearLogs()
  }
}

// 导出控制台
const exportConsole = () => {
  if (consoleRef.value) {
    consoleRef.value.exportLogs()
  }
}
</script>

<style scoped>
.console-demo {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.console-demo h2 {
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.console-container {
  height: 500px;
  margin-bottom: 30px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
}

.test-controls {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.test-controls h3 {
  color: #333;
  margin-bottom: 15px;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.button-group:last-child {
  margin-bottom: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .console-demo {
    padding: 10px;
  }
  
  .console-container {
    height: 400px;
  }
  
  .button-group {
    flex-direction: column;
  }
  
  .button-group .el-button {
    width: 100%;
  }
}
</style>
