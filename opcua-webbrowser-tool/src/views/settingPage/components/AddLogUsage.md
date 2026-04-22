# 父组件中使用 addLog 方法指南

## 📋 基本用法

### 1. 组件引用设置

```vue
<template>
  <div>
    <!-- 控制台组件 -->
    <ConsolePanel ref="consoleRef" />
    
    <!-- 触发按钮 -->
    <el-button @click="testAddLog">测试日志</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ConsolePanel from './components/ConsolePanel.vue'

// 创建组件引用
const consoleRef = ref(null)
</script>
```

### 2. 基本 addLog 调用

```javascript
// 添加信息日志
const addInfoLog = () => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', '这是一条信息日志', { data: 'test' })
  }
}

// 添加警告日志
const addWarnLog = () => {
  if (consoleRef.value) {
    consoleRef.value.addLog('warn', '这是一条警告', '警告详情')
  }
}

// 添加错误日志
const addErrorLog = () => {
  if (consoleRef.value) {
    consoleRef.value.addLog('error', '发生错误:', new Error('示例错误'))
  }
}

// 添加调试日志
const addDebugLog = () => {
  if (consoleRef.value) {
    consoleRef.value.addLog('debug', '调试信息', { user: 'admin', action: 'test' })
  }
}
```

## 🔧 实际应用场景

### 1. 在异步操作中记录日志

```javascript
const fetchData = async () => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', '开始获取数据')
  }
  
  try {
    const response = await fetch('/api/data')
    const data = await response.json()
    
    if (consoleRef.value) {
      consoleRef.value.addLog('info', '数据获取成功', data)
    }
    
    return data
  } catch (error) {
    if (consoleRef.value) {
      consoleRef.value.addLog('error', '数据获取失败:', error)
    }
    throw error
  }
}
```

### 2. 在表单提交中记录操作

```javascript
const handleSubmit = async (formData) => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', '开始提交表单', {
      formData,
      timestamp: Date.now(),
      user: 'currentUser'
    })
  }
  
  try {
    await submitForm(formData)
    
    if (consoleRef.value) {
      consoleRef.value.addLog('info', '表单提交成功')
    }
  } catch (error) {
    if (consoleRef.value) {
      consoleRef.value.addLog('error', '表单提交失败:', error)
    }
  }
}
```

### 3. 在用户操作中记录行为

```javascript
const handleUserAction = (action, data) => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', `用户执行操作: ${action}`, {
      action,
      data,
      timestamp: Date.now(),
      userId: getCurrentUserId()
    })
  }
  
  // 执行具体操作
  executeAction(action, data)
}
```

### 4. 在错误处理中记录异常

```javascript
const handleError = (error, context) => {
  if (consoleRef.value) {
    consoleRef.value.addLog('error', `错误发生在: ${context}`, error, {
      stack: error.stack,
      context,
      timestamp: Date.now(),
      userAgent: navigator.userAgent
    })
  }
  
  // 发送错误报告
  reportError(error, context)
}
```

## 🎯 高级用法

### 1. 创建日志工具函数

```javascript
// 创建统一的日志工具
const logger = {
  info: (message, ...data) => {
    if (consoleRef.value) {
      consoleRef.value.addLog('info', message, ...data)
    }
  },
  
  warn: (message, ...data) => {
    if (consoleRef.value) {
      consoleRef.value.addLog('warn', message, ...data)
    }
  },
  
  error: (message, ...data) => {
    if (consoleRef.value) {
      consoleRef.value.addLog('error', message, ...data)
    }
  },
  
  debug: (message, ...data) => {
    if (consoleRef.value) {
      consoleRef.value.addLog('debug', message, ...data)
    }
  }
}

// 使用示例
logger.info('用户登录', { userId: 123, timestamp: Date.now() })
logger.error('API调用失败', error, { endpoint: '/api/users' })
```

### 2. 在 Vue 生命周期中使用

```javascript
import { onMounted, onErrorCaptured } from 'vue'

// 组件挂载时记录
onMounted(() => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', '组件已挂载', {
      component: 'SettingPage',
      timestamp: Date.now()
    })
  }
})

// 捕获组件错误
onErrorCaptured((error, instance, info) => {
  if (consoleRef.value) {
    consoleRef.value.addLog('error', '组件错误:', error, {
      instance: instance?.$options.name,
      info,
      timestamp: Date.now()
    })
  }
})
```

### 3. 在路由守卫中使用

```javascript
// 路由变化时记录
watch(() => route.path, (newPath, oldPath) => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', '路由变化', {
      from: oldPath,
      to: newPath,
      timestamp: Date.now()
    })
  }
})
```

## 📝 最佳实践

### 1. 安全检查
```javascript
const safeAddLog = (level, message, ...data) => {
  if (consoleRef.value && consoleRef.value.addLog) {
    consoleRef.value.addLog(level, message, ...data)
  }
}
```

### 2. 日志格式化
```javascript
const logOperation = (operation, result, data = {}) => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', `${operation} - ${result}`, {
      operation,
      result,
      data,
      timestamp: Date.now(),
      duration: data.duration || 'N/A'
    })
  }
}
```

### 3. 性能监控
```javascript
const logPerformance = (operation, startTime) => {
  const duration = Date.now() - startTime
  
  if (consoleRef.value) {
    consoleRef.value.addLog('debug', `性能监控: ${operation}`, {
      operation,
      duration: `${duration}ms`,
      timestamp: Date.now()
    })
  }
}
```

## ⚠️ 注意事项

1. **组件引用检查**: 始终检查 `consoleRef.value` 是否存在
2. **性能考虑**: 避免在频繁调用的函数中添加过多日志
3. **敏感信息**: 不要在日志中记录密码等敏感信息
4. **日志级别**: 合理使用不同的日志级别
5. **错误处理**: 确保日志记录不会影响主要业务逻辑

## 🚀 完整示例

```vue
<template>
  <div>
    <ConsolePanel ref="consoleRef" />
    
    <div class="controls">
      <el-button @click="testBasicLogs">基本日志测试</el-button>
      <el-button @click="testAsyncLogs">异步操作日志</el-button>
      <el-button @click="testErrorLogs">错误日志测试</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ConsolePanel from './components/ConsolePanel.vue'

const consoleRef = ref(null)

// 基本日志测试
const testBasicLogs = () => {
  if (!consoleRef.value) return
  
  consoleRef.value.addLog('info', '开始基本日志测试')
  consoleRef.value.addLog('warn', '这是一条警告信息')
  consoleRef.value.addLog('debug', '调试信息', { test: true })
}

// 异步操作日志
const testAsyncLogs = async () => {
  if (!consoleRef.value) return
  
  consoleRef.value.addLog('info', '开始异步操作')
  
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    consoleRef.value.addLog('info', '异步操作完成')
  } catch (error) {
    consoleRef.value.addLog('error', '异步操作失败:', error)
  }
}

// 错误日志测试
const testErrorLogs = () => {
  if (!consoleRef.value) return
  
  try {
    throw new Error('这是一个测试错误')
  } catch (error) {
    consoleRef.value.addLog('error', '捕获到错误:', error, {
      context: '测试环境',
      timestamp: Date.now()
    })
  }
}
</script>
```

这样你就可以在父组件中灵活使用 `addLog` 方法来记录各种类型的日志了！
