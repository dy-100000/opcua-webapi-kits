# 自定义控制台组件 (ConsolePanel)

一个功能完整、美观的自定义控制台组件，类似浏览器开发者工具的控制台，支持日志显示、过滤、搜索、导出等功能。

## 功能特性

### 🎯 核心功能
- **日志拦截**: 自动拦截 `console.log`、`console.error`、`console.warn`、`console.info`、`console.debug`
- **实时显示**: 实时显示所有控制台输出
- **级别过滤**: 按日志级别（全部/错误/警告/信息/调试）过滤
- **搜索功能**: 支持关键词搜索日志内容
- **自动滚动**: 自动滚动到最新日志
- **日志清空**: 一键清空所有日志
- **日志导出**: 导出日志为JSON文件

### 🎨 UI特性
- **暗色主题**: 类似VS Code的暗色主题
- **语法高亮**: 支持JSON对象格式化显示
- **响应式设计**: 适配移动端和桌面端
- **交互友好**: 悬停效果、点击选择、展开收起
- **时间戳**: 精确到毫秒的时间显示

### 🔧 高级功能
- **命令执行**: 内置JavaScript命令执行器
- **错误堆栈**: 自动捕获和显示错误堆栈信息
- **日志复制**: 一键复制日志内容
- **批量操作**: 支持批量日志处理

## 使用方法

### 1. 基本使用

```vue
<template>
  <div class="app">
    <!-- 其他内容 -->
    
    <!-- 控制台组件 -->
    <ConsolePanel ref="consoleRef" />
  </div>
</template>

<script setup>
import ConsolePanel from '@/components/ConsolePanel.vue'
import { ref } from 'vue'

const consoleRef = ref()

// 程序化添加日志
const addCustomLog = () => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', '自定义日志消息', { data: 'test' })
  }
}
</script>

<style>
.app {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 为控制台预留空间 */
.console-panel {
  height: 400px; /* 根据需要调整高度 */
}
</style>
```

### 2. 在现有页面中集成

```vue
<template>
  <div class="page-container">
    <!-- 页面主要内容 -->
    <div class="main-content">
      <!-- 你的页面内容 -->
    </div>
    
    <!-- 控制台面板 -->
    <el-card class="console-card">
      <template #header>
        <div class="card-header">
          <span>调试控制台</span>
          <el-button @click="toggleConsole" size="small">
            {{ showConsole ? '隐藏' : '显示' }}
          </el-button>
        </div>
      </template>
      
      <ConsolePanel v-show="showConsole" ref="consoleRef" />
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ConsolePanel from '@/components/ConsolePanel.vue'

const showConsole = ref(false)
const consoleRef = ref()

const toggleConsole = () => {
  showConsole.value = !showConsole.value
}
</script>
```

### 3. 作为弹窗使用

```vue
<template>
  <div>
    <!-- 触发按钮 -->
    <el-button @click="showConsoleDialog = true">
      打开控制台
    </el-button>
    
    <!-- 控制台弹窗 -->
    <el-dialog
      v-model="showConsoleDialog"
      title="调试控制台"
      width="80%"
      :close-on-click-modal="false"
    >
      <ConsolePanel ref="consoleRef" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ConsolePanel from '@/components/ConsolePanel.vue'

const showConsoleDialog = ref(false)
const consoleRef = ref()
</script>
```

## API 参考

### Props
组件没有必需的props，所有功能都是内置的。

### Methods
通过 `ref` 可以调用以下方法：

```typescript
interface ConsolePanelRef {
  // 添加日志
  addLog(level: 'error' | 'warn' | 'info' | 'debug', message: string, ...data: any[]): void
  
  // 清空所有日志
  clearLogs(): Promise<void>
  
  // 导出日志
  exportLogs(): void
}
```

### 使用示例

```javascript
// 获取组件引用
const consoleRef = ref()

// 添加自定义日志
consoleRef.value.addLog('info', '用户登录成功', { userId: 123, timestamp: Date.now() })

// 添加错误日志
consoleRef.value.addLog('error', 'API请求失败', new Error('网络连接超时'))

// 清空日志
await consoleRef.value.clearLogs()

// 导出日志
consoleRef.value.exportLogs()
```

## 样式自定义

### 主题颜色
组件使用CSS变量，可以通过覆盖变量来自定义主题：

```css
.console-panel {
  /* 主背景色 */
  --console-bg: #1e1e1e;
  
  /* 头部背景色 */
  --console-header-bg: #2d2d2d;
  
  /* 边框颜色 */
  --console-border: #404040;
  
  /* 文本颜色 */
  --console-text: #ffffff;
  
  /* 次要文本颜色 */
  --console-text-secondary: #888;
  
  /* 错误颜色 */
  --console-error: #f56565;
  
  /* 警告颜色 */
  --console-warn: #ed8936;
  
  /* 信息颜色 */
  --console-info: #4299e1;
  
  /* 调试颜色 */
  --console-debug: #68d391;
}
```

### 高度调整
```css
.console-panel {
  height: 500px; /* 固定高度 */
}

/* 或者使用视口高度 */
.console-panel {
  height: 50vh; /* 视口高度的50% */
}
```

## 注意事项

1. **性能考虑**: 大量日志可能影响性能，建议定期清空日志
2. **内存使用**: 日志会保存在内存中，长时间运行可能占用较多内存
3. **控制台拦截**: 组件会拦截原生console方法，卸载时会自动恢复
4. **浏览器兼容**: 需要支持ES6+的现代浏览器
5. **安全考虑**: 命令执行功能会执行任意JavaScript代码，生产环境请谨慎使用

## 故障排除

### 常见问题

**Q: 控制台没有显示日志？**
A: 确保组件已正确挂载，并且console方法调用在组件挂载之后。

**Q: 样式显示异常？**
A: 检查Element Plus是否正确引入，确保CSS样式没有被覆盖。

**Q: 导出功能不工作？**
A: 检查浏览器是否支持Blob API，某些旧版本浏览器可能不支持。

**Q: 命令执行失败？**
A: 确保执行的代码语法正确，避免使用浏览器不支持的API。

## 更新日志

### v1.0.0
- 初始版本发布
- 支持基本的日志显示和过滤功能
- 实现控制台API拦截
- 添加美观的暗色主题UI
- 支持日志导出和清空功能
- 内置命令执行器
- 响应式设计支持

## 许可证

MIT License
