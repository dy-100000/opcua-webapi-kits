<template>
  <div class="console-panel">
    <!-- 控制台头部 -->
    <div class="console-header">
      <div class="console-title">
        <el-icon class="console-icon"><Monitor /></el-icon>
        <span>控制台</span>
        <el-badge 
          v-if="logCount > 0" 
          :value="logCount" 
          class="log-badge"
          type="info"
        />
      </div>
      
      <div class="console-controls">
        <!-- 日志级别过滤 -->
        <el-select
          v-model="selectedLevel"
          placeholder="日志级别"
          size="small"
          style="width: 120px; margin-right: 8px"
          @change="filterLogs"
        >
          <el-option label="全部" value="all" />
          <el-option label="错误" value="error" />
          <el-option label="警告" value="warn" />
          <el-option label="信息" value="info" />
          <el-option label="调试" value="debug" />
        </el-select>
        
        <!-- 搜索框 -->
        <el-input
          v-model="searchText"
          placeholder="搜索日志..."
          size="small"
          style="width: 200px; margin-right: 8px"
          clearable
          @input="filterLogs"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <!-- 控制按钮 -->
        <el-button-group size="small">
          <el-button @click="clearLogs" :icon="Delete">清空</el-button>
          <el-button @click="toggleAutoScroll" :type="autoScroll ? 'primary' : 'default'" :icon="ArrowDown">
            {{ autoScroll ? '自动滚动' : '停止滚动' }}
          </el-button>
          <el-button @click="exportLogs" :icon="Download">导出</el-button>
        </el-button-group>
      </div>
    </div>
    
    <!-- 控制台内容区域 -->
    <div class="console-content" ref="consoleContent">
      <div 
        v-if="filteredLogs.length === 0" 
        class="console-empty"
      >
        <el-icon class="empty-icon"><Document /></el-icon>
        <p>暂无日志信息</p>
      </div>
      
      <!-- 表格形式展示日志 -->
      <el-table
        v-else
        :data="filteredLogs"
        class="log-table"
        :row-class-name="getRowClassName"
        @row-click="selectLog"
        highlight-current-row
        :current-row-key="selectedLogId"
        stripe
        size="small"
        style="width: 100%"
      >
        <el-table-column prop="timestamp" label="Timestamp" width="140">
          <template #default="{ row }">
            <span class="log-timestamp-cell">{{ formatTime(row.timestamp) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="source" label="Source" width="180">
          <template #default="{ row }">
            <div class="log-source-cell">
              <el-icon v-if="row.level === 'error'" class="error-icon"><CircleCloseFilled /></el-icon>
              <el-icon v-else-if="row.level === 'warn'" class="warn-icon"><WarningFilled /></el-icon>
              <el-icon v-else-if="row.level === 'info'" class="info-icon"><InfoFilled /></el-icon>
              <el-icon v-else class="debug-icon"><Tools /></el-icon>
              <span>{{ row.source || '-' }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="server" label="Server" width="100">
          <template #default="{ row }">
            <span class="log-server-cell">{{ row.server || '-' }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="message" label="Message" min-width="300">
          <template #default="{ row }">
            <div class="log-message-cell" v-html="formatLogMessage(row.message)"></div>
            <div v-if="row.expanded && row.stack" class="log-stack" v-html="formatStack(row.stack)"></div>
            <div v-if="row.expanded && row.data && row.data.length > 0" class="log-data">
              <div v-for="(item, idx) in row.data" :key="idx" class="log-data-item">
                <pre>{{ formatData(item) }}</pre>
              </div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <div class="log-actions">
              <el-button 
                size="small" 
                text 
                @click.stop="copyLog(row)"
                :icon="CopyDocument"
                title="复制"
              />
              <el-button 
                size="small" 
                text 
                @click.stop="expandLog(row)"
                :icon="row.expanded ? 'ArrowUp' : 'ArrowDown'"
                :title="row.expanded ? '收起' : '展开'"
              />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 控制台输入区域 -->
    <div class="console-input">
      <el-input
        v-model="inputCommand"
        placeholder="输入命令或表达式..."
        @keyup.enter="executeCommand"
        ref="commandInput"
      >
        <template #prefix>
          <el-icon><Edit /></el-icon>
        </template>
        <template #append>
          <el-button @click="executeCommand" :icon="Right">执行</el-button>
        </template>
      </el-input>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { 
  Monitor, 
  Delete, 
  ArrowDown, 
  Download, 
  Search, 
  Document,
  CircleCloseFilled,
  WarningFilled,
  InfoFilled,
  Tools,
  CopyDocument,
  Edit,
  Right
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 日志接口定义
interface LogEntry {
  id: string
  timestamp: number
  level: 'error' | 'warn' | 'info' | 'debug'
  message: string
  source?: string
  server?: string
  stack?: string
  data?: any[]
  expanded?: boolean
}

// 全局日志存储，让多个 ConsolePanel 实例共享同一个日志数据源
const globalLogsStore = (() => {
  const logs = ref<LogEntry[]>([])
  const logCount = ref(0)
  return { logs, logCount }
})()

// 响应式数据
const consoleContent = ref<HTMLElement>()
const commandInput = ref()
const inputCommand = ref('')
const selectedLevel = ref('all')
const searchText = ref('')
const autoScroll = ref(true)
const selectedLogId = ref<string | null>(null)

// 日志数据 - 使用全局存储
const logs = globalLogsStore.logs
const logCount = globalLogsStore.logCount
// 初始化 filteredLogs 时，同步 logs 的值，确保有数据时能立即显示
const filteredLogs = ref<LogEntry[]>([])

// 原始控制台方法备份
let originalConsole: {
  log: typeof console.log
  error: typeof console.error
  warn: typeof console.warn
  info: typeof console.info
  debug: typeof console.debug
} | null = null

// 全局标志，确保拦截控制台的方法只被调用一次
let isConsoleIntercepted = false

// 生成唯一ID
const generateId = () => {
  return Date.now().toString(36) + Math.random().toString(36).substr(2)
}

// 格式化时间
const formatTime = (timestamp: number) => {
  const date = new Date(timestamp)
  const timeStr = date.toLocaleTimeString('zh-CN', { 
    hour12: false,
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
  const ms = date.getMilliseconds().toString().padStart(3, '0')
  return `${timeStr}.${ms}`
}

// 格式化日志消息
const formatLogMessage = (message: string) => {
  if (!searchText.value) return message
  
  const regex = new RegExp(`(${searchText.value})`, 'gi')
  return message.replace(regex, '<mark>$1</mark>')
}

// 格式化堆栈信息
const formatStack = (stack: string) => {
  return stack.replace(/\n/g, '<br>').replace(/ /g, '&nbsp;')
}

// 格式化数据
const formatData = (data: any) => {
  try {
    if (typeof data === 'object') {
      return JSON.stringify(data, null, 2)
    }
    return String(data)
  } catch (e) {
    return String(data)
  }
}

// 解析日志消息，提取 source 和 server
const parseLogMessage = (message: string): { source: string; server: string; cleanMessage: string } => {
  let source = ''
  let server = ''
  let cleanMessage = message.trim()
  
  // 解析常见的 OPC UA 日志格式
  // 例如: "Discovery FindServers on opc.tcp://localhost:4840 failed (BadCommunicationError)"
  if (message.includes('Discovery') || message.includes('FindServers')) {
    source = 'DiscoveryWidget'
    server = ''
  } else if (message.includes('Endpoint:') || message.includes('Security policy:') || 
             message.includes('ApplicationUri:') || message.includes('Used UserTokenType:') ||
             message.includes('Connection status') || message.includes('Revised values:')) {
    source = 'Server Node'
    // 提取服务器标识
    if (message.includes("server '") || message.includes("server '@'")) {
      const serverMatch = message.match(/server\s+['"]?([^'"]+)['"]?/i)
      if (serverMatch) {
        server = serverMatch[1] || '@'
      } else {
        server = '@'
      }
    } else {
      // 对于 Server Node 类型的日志，默认使用 '@'
      server = '@'
    }
  } else if (message.includes('Browse on node') || message.includes('Registered for')) {
    source = 'AddressSpaceMonitor'
    // 提取服务器标识
    if (message.includes("server '") || message.includes("server '@'")) {
      const serverMatch = message.match(/server\s+['"]?([^'"]+)['"]?/i)
      if (serverMatch) {
        server = serverMatch[1] || '@'
      } else {
        server = '@'
      }
    } else {
      // 对于 AddressSpaceMonitor 类型的日志，默认使用 '@'
      server = '@'
    }
  }
  
  return { source, server, cleanMessage }
}

// 添加日志
const addLog = (level: LogEntry['level'], message: string, ...data: any[]) => {
  // 确保 message 是字符串
  let messageStr = ''
  if (typeof message === 'string') {
    messageStr = message
  } else if (message === null) {
    messageStr = 'null'
  } else if (message === undefined) {
    messageStr = 'undefined'
  } else {
    try {
      messageStr = JSON.stringify(message, null, 2)
    } catch {
      messageStr = String(message)
    }
  }
  
  // 解析消息，提取 source 和 server
  const { source, server, cleanMessage } = parseLogMessage(messageStr)
  
  const log: LogEntry = {
    id: generateId(),
    timestamp: Date.now(),
    level,
    message: cleanMessage,
    source: source || undefined,
    server: server || undefined,
    data: data.length > 0 ? data : undefined,
    expanded: false
  }
  
  // 如果是错误，尝试获取堆栈信息
  if (level === 'error' && data.length > 0) {
    const error = data[0]
    if (error instanceof Error && error.stack) {
      log.stack = error.stack
    }
  }
  
  // 直接添加到 logs，watch 会自动触发 filterLogs
  logs.value.push(log)
  logCount.value = logs.value.length
  
  // 立即调用 filterLogs 确保当前实例更新
  // watch 会在所有实例中触发，但这里立即调用可以确保当前实例立即更新
  filterLogs()
  
  // 使用 nextTick 确保 DOM 更新后再滚动
  nextTick(() => {
    if (autoScroll.value) {
      scrollToBottom()
    }
  })
}

// 格式化时间戳（用于表格）- 已移除，直接在模板中使用 formatTime

// 获取表格行类名
const getRowClassName = ({ row }: { row: LogEntry }) => {
  return `log-row log-${row.level}`
}

// 过滤日志
const filterLogs = () => {
  // 确保 logs.value 存在
  if (!logs.value || !Array.isArray(logs.value)) {
    filteredLogs.value = []
    logCount.value = 0
    return
  }
  
  let filtered = [...logs.value] // 创建副本避免直接修改原数组
  
  // 按级别过滤
  if (selectedLevel.value !== 'all') {
    filtered = filtered.filter(log => log.level === selectedLevel.value)
  }
  
  // 按搜索文本过滤
  if (searchText.value && searchText.value.trim()) {
    const searchLower = searchText.value.toLowerCase().trim()
    filtered = filtered.filter(log => 
      log.message.toLowerCase().includes(searchLower) ||
      (log.stack && log.stack.toLowerCase().includes(searchLower)) ||
      (log.data && log.data.some(item => 
        String(item).toLowerCase().includes(searchLower)
      ))
    )
  }
  
  // 更新 filteredLogs
  filteredLogs.value = filtered
  // 确保 logCount 也更新
  logCount.value = logs.value.length
}

// 清空日志
const clearLogs = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有日志吗？', '确认清空', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 清空全局日志存储
    logs.value = []
    filteredLogs.value = []
    logCount.value = 0
    selectedLogId.value = null
    
    // 确保过滤逻辑也更新
    filterLogs()
    
    ElMessage.success('日志已清空')
  } catch {
    // 用户取消清空
  }
}

// 切换自动滚动
const toggleAutoScroll = () => {
  autoScroll.value = !autoScroll.value
  if (autoScroll.value) {
    scrollToBottom()
  }
}

// 滚动到底部
const scrollToBottom = () => {
  if (consoleContent.value) {
    const tableBody = consoleContent.value.querySelector('.el-table__body-wrapper')
    if (tableBody) {
      tableBody.scrollTop = tableBody.scrollHeight
    } else {
      consoleContent.value.scrollTop = consoleContent.value.scrollHeight
    }
  }
}

// 选择日志
const selectLog = (log: LogEntry) => {
  if (log && log.id) {
    selectedLogId.value = selectedLogId.value === log.id ? null : log.id
  }
}

// 复制日志
const copyLog = async (log: LogEntry) => {
  try {
    let text = `[${formatTime(log.timestamp)}] ${log.level.toUpperCase()}: ${log.message}`
    if (log.stack) {
      text += `\n${log.stack}`
    }
    if (log.data && log.data.length > 0) {
      text += `\n${log.data.map(item => formatData(item)).join('\n')}`
    }
    
    await navigator.clipboard.writeText(text)
    ElMessage.success('日志已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

// 展开/收起日志
const expandLog = (log: LogEntry) => {
  log.expanded = !log.expanded
}

// 导出日志
const exportLogs = () => {
  try {
    const exportData = filteredLogs.value.map(log => ({
      timestamp: formatTime(log.timestamp),
      level: log.level,
      message: log.message,
      stack: log.stack,
      data: log.data
    }))
    
    const blob = new Blob([JSON.stringify(exportData, null, 2)], {
      type: 'application/json'
    })
    
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `console-logs-${new Date().toISOString().slice(0, 19)}.json`
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    URL.revokeObjectURL(url)
    
    ElMessage.success('日志已导出')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 执行命令
const executeCommand = () => {
  if (!inputCommand.value.trim()) return
  
  const command = inputCommand.value.trim()
  
  try {
    // 记录执行的命令
    addLog('info', `> ${command}`)
    
    // 尝试执行命令
    const result = eval(command)
    addLog('info', `← ${formatData(result)}`)
    
    inputCommand.value = ''
  } catch (error) {
    addLog('error', `执行命令失败: ${error instanceof Error ? error.message : String(error)}`, error)
    inputCommand.value = ''
  }
}

// 拦截控制台方法
const interceptConsole = () => {
  // 如果已经拦截过，直接返回
  if (isConsoleIntercepted) {
    return
  }
  
  originalConsole = {
    log: console.log,
    error: console.error,
    warn: console.warn,
    info: console.info,
    debug: console.debug
  }
  
  // 格式化参数为字符串消息
  const formatMessage = (args: any[]): string => {
    if (args.length === 0) return ''
    if (args.length === 1) {
      if (typeof args[0] === 'string') return args[0]
      if (args[0] === null) return 'null'
      if (args[0] === undefined) return 'undefined'
      try {
        return JSON.stringify(args[0], null, 2)
      } catch {
        return String(args[0])
      }
    }
    // 多个参数时，格式化每个参数
    return args.map(arg => {
      if (typeof arg === 'string') return arg
      if (arg === null) return 'null'
      if (arg === undefined) return 'undefined'
      try {
        if (typeof arg === 'object') {
          return JSON.stringify(arg, null, 2)
        }
        return String(arg)
      } catch {
        return String(arg)
      }
    }).join(' ')
  }
  
  console.log = (...args) => {
    originalConsole!.log(...args)
    const message = formatMessage(args)
    if (message) {
      addLog('info', message, ...args.slice(1))
    }
  }
  
  console.error = (...args) => {
    originalConsole!.error(...args)
    const message = formatMessage(args)
    if (message) {
      addLog('error', message, ...args.slice(1))
    }
  }
  
  console.warn = (...args) => {
    originalConsole!.warn(...args)
    const message = formatMessage(args)
    if (message) {
      addLog('warn', message, ...args.slice(1))
    }
  }
  
  console.info = (...args) => {
    originalConsole!.info(...args)
    const message = formatMessage(args)
    if (message) {
      addLog('info', message, ...args.slice(1))
    }
  }
  
  console.debug = (...args) => {
    originalConsole!.debug(...args)
    const message = formatMessage(args)
    if (message) {
      addLog('debug', message, ...args.slice(1))
    }
  }
  
  isConsoleIntercepted = true
}

// 恢复控制台方法
const restoreConsole = () => {
  // 注意：由于多个实例共享，这里不恢复控制台方法
  // 只有在所有 ConsolePanel 实例都卸载时才恢复
  // 暂时不实现恢复逻辑，保持拦截状态
}

// 监听日志变化，自动过滤
watch([selectedLevel, searchText], () => {
  filterLogs()
})

// 监听 logs 变化，自动更新 filteredLogs（确保数据同步）
watch(
  () => logs.value.length, // 监听数组长度变化，更可靠
  (newLen, oldLen) => {
    if (newLen !== oldLen) {
      filterLogs()
    }
  },
  { immediate: true }
)

// 同时监听 logs 数组内容变化（使用 flush: 'post' 确保在 DOM 更新后执行）
watch(
  logs,
  () => {
    filterLogs()
  },
  { deep: true, flush: 'post' }
)

// 组件挂载
onMounted(() => {
  interceptConsole()
  
  // 立即初始化过滤日志
  filterLogs()
  
  // 使用多个 nextTick 确保组件完全渲染后再刷新
  nextTick(() => {
    filterLogs()
    nextTick(() => {
      filterLogs()
      // 如果已有日志，再次刷新确保显示
      if (logs.value.length > 0) {
        filterLogs()
      }
    })
  })
  
  // 延迟初始化，确保 DOM 完全渲染
  setTimeout(() => {
    filterLogs()
  }, 100)
  
  // 再次延迟刷新，确保所有日志都能显示
  setTimeout(() => {
    filterLogs()
  }, 300)
})

// 组件卸载
onUnmounted(() => {
  restoreConsole()
})

// 暴露方法给父组件
defineExpose({
  addLog,
  clearLogs,
  exportLogs,
  filterLogs
})
</script>

<style scoped>
.console-panel {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #ffffff;
  border-radius: 8px;
  overflow: hidden;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
}

.console-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
  flex-shrink: 0;
}

.console-title {
  display: flex;
  align-items: center;
  color: #303133;
  font-weight: 600;
  font-size: 14px;
}

.console-icon {
  margin-right: 8px;
  color: #4fc3f7;
}

.log-badge {
  margin-left: 8px;
}

.console-controls {
  display: flex;
  align-items: center;
}

.console-content {
  flex: 1;
  overflow: hidden;
  padding: 0;
  background: #ffffff;
  display: flex;
  flex-direction: column;
}

.console-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #909399;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  color: #c0c4cc;
}

.log-item {
  display: flex;
  align-items: flex-start;
  padding: 8px 12px;
  margin-bottom: 4px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  border-left: 3px solid transparent;
}

.log-item:hover {
  background: #2a2a2a;
}

.log-item.log-selected {
  background: #2a2a2a;
  border-left-color: #4fc3f7;
}

.log-error {
  border-left-color: #f56565;
}

.log-warn {
  border-left-color: #ed8936;
}

.log-info {
  border-left-color: #4299e1;
}

.log-debug {
  border-left-color: #68d391;
}

.log-timestamp {
  color: #888;
  font-size: 12px;
  margin-right: 12px;
  min-width: 80px;
  flex-shrink: 0;
}

.log-level-icon {
  margin-right: 12px;
  flex-shrink: 0;
}

.error-icon {
  color: #f56565;
}

.warn-icon {
  color: #ed8936;
}

.info-icon {
  color: #4299e1;
}

.debug-icon {
  color: #68d391;
}

.log-content {
  flex: 1;
  min-width: 0;
}

.log-message {
  color: #ffffff;
  font-size: 13px;
  line-height: 1.4;
  word-break: break-word;
}

.log-message :deep(mark) {
  background: #ffeb3b;
  color: #000;
  padding: 2px 4px;
  border-radius: 2px;
}

.log-stack {
  margin-top: 8px;
  padding: 8px;
  background: #fef0f0;
  border-radius: 4px;
  color: #f56c6c;
  font-size: 12px;
  line-height: 1.4;
  white-space: pre-wrap;
}

.log-data {
  margin-top: 8px;
}

.log-data-item {
  margin-bottom: 4px;
}

.log-data-item pre {
  background: #f5f7fa;
  padding: 8px;
  border-radius: 4px;
  color: #606266;
  font-size: 12px;
  line-height: 1.4;
  overflow-x: auto;
  margin: 0;
}

.log-actions {
  display: flex;
  gap: 4px;
  margin-left: 12px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.log-item:hover .log-actions {
  opacity: 1;
}

.console-input {
  padding: 12px 16px;
  background: #f5f7fa;
  border-top: 1px solid #e4e7ed;
  flex-shrink: 0;
}

/* 滚动条样式 */
.console-content::-webkit-scrollbar {
  width: 8px;
}

.console-content::-webkit-scrollbar-track {
  background: #f5f7fa;
}

.console-content::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 4px;
}

.console-content::-webkit-scrollbar-thumb:hover {
  background: #a8abb2;
}

/* 表格样式 */
.log-table {
  flex: 1;
  height: 100%;
}

.log-table :deep(.el-table) {
  background: #ffffff;
  color: #303133;
}

.log-table :deep(.el-table__header-wrapper) {
  background: #f5f7fa;
}

.log-table :deep(.el-table__header th) {
  background: #f5f7fa;
  color: #303133;
  border-bottom: 1px solid #e4e7ed;
  font-weight: 600;
}

.log-table :deep(.el-table__body-wrapper) {
  background: #ffffff;
}

.log-table :deep(.el-table__body tr) {
  background: #ffffff;
  color: #303133;
}

.log-table :deep(.el-table__body tr:hover) {
  background: #e6f7ff !important;
}

.log-table :deep(.el-table__body tr.current-row) {
  background: #e6f7ff;
}

.log-table :deep(.el-table__body td) {
  border-bottom: 1px solid #ebeef5;
  padding: 8px 0;
}

.log-table :deep(.el-table--striped .el-table__body tr.el-table__row--striped) {
  background: #fafafa;
}

.log-table :deep(.el-table--striped .el-table__body tr.el-table__row--striped:hover) {
  background: #e6f7ff !important;
}

.log-row.log-error {
  background: rgba(245, 101, 101, 0.05) !important;
}

.log-row.log-error:hover {
  background: #e6f7ff !important;
}

.log-row.log-warn {
  background: rgba(237, 137, 54, 0.05) !important;
}

.log-row.log-warn:hover {
  background: #e6f7ff !important;
}

.log-timestamp-cell {
  color: #909399;
  font-size: 12px;
}

.log-source-cell {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #303133;
  font-size: 13px;
}

.log-source-cell .el-icon {
  font-size: 14px;
}

.log-server-cell {
  color: #303133;
  font-size: 13px;
}

.log-message-cell {
  color: #303133;
  font-size: 13px;
  line-height: 1.4;
  word-break: break-word;
}

.log-message-cell :deep(mark) {
  background: #ffeb3b;
  color: #000;
  padding: 2px 4px;
  border-radius: 2px;
}

.log-actions {
  display: flex;
  gap: 4px;
  opacity: 0.7;
  transition: opacity 0.2s ease;
}

.log-table :deep(.el-table__body tr:hover .log-actions) {
  opacity: 1;
}

/* Element Plus 组件样式覆盖 */
.console-panel :deep(.el-input__wrapper) {
  background: #ffffff;
  border-color: #dcdfe6;
}

.console-panel :deep(.el-input__inner) {
  color: #303133;
  background: transparent;
}

.console-panel :deep(.el-input__inner::placeholder) {
  color: #c0c4cc;
}

.console-panel :deep(.el-select .el-input__wrapper) {
  background: #ffffff;
}

.console-panel :deep(.el-button) {
  background: #ffffff;
  border-color: #dcdfe6;
  color: #303133;
}

.console-panel :deep(.el-button:hover) {
  background: #ecf5ff;
  border-color: #b3d8ff;
  color: #409eff;
}

.console-panel :deep(.el-button--primary) {
  background: #409eff;
  border-color: #409eff;
  color: #ffffff;
}

.console-panel :deep(.el-button--primary:hover) {
  background: #66b1ff;
  border-color: #66b1ff;
}

.console-panel :deep(.el-badge__content) {
  background: #409eff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .console-header {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }
  
  .console-controls {
    flex-wrap: wrap;
    gap: 8px;
  }
  
  .log-item {
    flex-direction: column;
    align-items: stretch;
  }
  
  .log-timestamp {
    margin-bottom: 4px;
  }
  
  .log-actions {
    margin-left: 0;
    margin-top: 8px;
    opacity: 1;
  }
}
</style>
