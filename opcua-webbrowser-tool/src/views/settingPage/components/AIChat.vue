<template>
  <div class="ai-chat">
    <!-- 聊天消息区域 -->
    <div class="chat-messages" ref="messagesContainer">
      <div v-if="!aiStore.hasMessages" class="empty-state">
        <div class="empty-icon">
          <el-icon size="48"><ChatDotRound /></el-icon>
        </div>
        <div class="empty-text">
          <h3>欢迎使用AI助手</h3>
          <p>我可以帮助您分析OPC UA配置、诊断问题并提供专业建议</p>
        </div>
        <div class="quick-actions">
          <el-button @click="sendQuickMessage('帮我分析当前的OPC UA配置')" type="primary" plain>
            分析配置
          </el-button>
          <el-button @click="sendQuickMessage('检查是否有连接问题')" type="success" plain>
            诊断问题
          </el-button>
          <el-button @click="sendQuickMessage('提供优化建议')" type="warning" plain>
            优化建议
          </el-button>
        </div>
      </div>
      
      <div v-else class="messages-list">
        <div 
          v-for="message in aiStore.messages" 
          :key="message.id"
          :class="['message', `message-${message.role}`]"
        >
          <div class="message-avatar">
            <el-icon v-if="message.role === 'user'"><User /></el-icon>
            <el-icon v-else><Edit /></el-icon>
          </div>
          <div class="message-content">
            <div class="message-text" v-html="formatMessage(message.content)"></div>
            <div class="message-time">
              {{ formatTime(message.timestamp) }}
            </div>
            <div v-if="message.isStreaming" class="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input">
      <div class="input-container">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="2"
          placeholder="输入您的问题..."
          :disabled="aiStore.isLoading"
          @keydown.enter.exact.prevent="sendMessage"
          @keydown.enter.shift.exact="inputMessage += '\n'"
          ref="inputRef"
        />
        <div class="input-actions">
          <el-button 
            @click="clearChat" 
            :disabled="aiStore.isLoading"
            size="small"
            plain
          >
            清空
          </el-button>
          <el-button 
            @click="sendMessage" 
            :loading="aiStore.isLoading"
            type="primary"
            size="small"
          >
            发送
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound, User, Edit } from '@element-plus/icons-vue'
import { useAIStore } from '../stores/aiStore.js'

// Props
const props = defineProps({
  sessionId: {
    type: String,
    default: 'default'
  }
})

// Store
const aiStore = useAIStore()

// 响应式数据
const inputMessage = ref('')
const messagesContainer = ref(null)
const inputRef = ref(null)

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim() || aiStore.isLoading) return

  const message = inputMessage.value.trim()
  inputMessage.value = ''

  try {
    if (aiStore.settings?.enableStreaming) {
      await sendStreamMessage(message)
    } else {
      await aiStore.sendMessage(message)
    }
    
    // 滚动到底部
    await nextTick()
    scrollToBottom()
  } catch (error) {
    ElMessage.error('发送消息失败: ' + error.message)
  }
}

// 发送流式消息
const sendStreamMessage = async (message) => {
  await aiStore.sendStreamMessage(message, (chunk, fullContent) => {
    // 实时更新消息内容
    nextTick(() => {
      scrollToBottom()
    })
  })
}

// 发送快速消息
const sendQuickMessage = async (message) => {
  inputMessage.value = message
  await sendMessage()
}

// 清空聊天
const clearChat = () => {
  aiStore.clearMessages()
}

// 滚动到底部
const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

// 格式化消息内容
const formatMessage = (content) => {
  // 简单的Markdown渲染
  return content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/`(.*?)`/g, '<code>$1</code>')
    .replace(/\n/g, '<br>')
}

// 格式化时间
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleTimeString('zh-CN', { 
    hour: '2-digit', 
    minute: '2-digit' 
  })
}

// 监听消息变化，自动滚动
watch(() => aiStore.messages.length, () => {
  nextTick(() => {
    scrollToBottom()
  })
})

// 组件挂载时聚焦输入框
onMounted(() => {
  if (inputRef.value) {
    inputRef.value.focus()
  }
})
</script>

<style scoped>
.ai-chat {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f5f5f5;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #fff;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  text-align: center;
  color: #666;
}

.empty-icon {
  margin-bottom: 16px;
  color: #409eff;
}

.empty-text h3 {
  margin: 0 0 8px 0;
  color: #333;
}

.empty-text p {
  margin: 0 0 24px 0;
  color: #666;
}

.quick-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  justify-content: center;
}

.messages-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.message-user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  flex-shrink: 0;
}

.message-user .message-avatar {
  background: #409eff;
  color: white;
}

.message-assistant .message-avatar {
  background: #67c23a;
  color: white;
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-user .message-content {
  text-align: right;
}

.message-text {
  background: #f0f0f0;
  padding: 12px 16px;
  border-radius: 12px;
  word-wrap: break-word;
  line-height: 1.5;
}

.message-user .message-text {
  background: #409eff;
  color: white;
}

.message-assistant .message-text {
  background: #f0f0f0;
  color: #333;
}

.message-text :deep(code) {
  background: rgba(0, 0, 0, 0.1);
  padding: 2px 4px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}

.message-user .message-text :deep(code) {
  background: rgba(255, 255, 255, 0.2);
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.message-user .message-time {
  text-align: right;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  margin-top: 8px;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #999;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

.chat-input {
  padding: 16px;
  background: #fff;
  border-top: 1px solid #e0e0e0;
}

.input-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* 滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
