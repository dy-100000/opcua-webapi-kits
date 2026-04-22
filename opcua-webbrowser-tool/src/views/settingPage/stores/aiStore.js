/**
 * AI功能状态管理
 * 使用Pinia管理AI相关的状态
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import mcpService from '../services/mcpService.js'

export const useAIStore = defineStore('ai', () => {
  // 状态
  const isInitialized = ref(false)
  const isLoading = ref(false)
  const currentSessionId = ref('default')
  const apiKey = ref('')
  const error = ref(null)
  
  // 聊天相关状态
  const messages = ref([])
  const isTyping = ref(false)
  
  // 分析结果状态
  const codeAnalysisResult = ref(null)
  const diagnosticResult = ref(null)
  const suggestionResult = ref(null)
  
  // 设置状态
  const settings = ref({
    temperature: 0.7,
    maxTokens: 2000,
    enableStreaming: true,
    autoSaveContext: true,
    maxContextLength: 10
  })

  // 计算属性
  const hasMessages = computed(() => messages.value.length > 0)
  const lastMessage = computed(() => messages.value[messages.value.length - 1])
  const sessionCount = computed(() => mcpService.getAllSessions().length)

  // 动作
  const initialize = async (apiKeyValue) => {
    try {
      isLoading.value = true
      error.value = null
      
      await mcpService.initialize(apiKeyValue)
      apiKey.value = apiKeyValue
      isInitialized.value = true
      
      // 创建默认会话
      mcpService.createSession(currentSessionId.value)
      
      console.log('AI Store初始化成功')
    } catch (err) {
      error.value = err.message
      console.error('AI Store初始化失败:', err)
    } finally {
      isLoading.value = false
    }
  }

  const sendMessage = async (content, options = {}) => {
    if (!isInitialized.value) {
      throw new Error('AI服务未初始化')
    }

    try {
      isLoading.value = true
      isTyping.value = true
      error.value = null

      // 添加用户消息
      const userMessage = {
        id: Date.now(),
        role: 'user',
        content: content,
        timestamp: Date.now()
      }
      messages.value.push(userMessage)

      // 调用AI服务
      const result = await mcpService.chat(content, currentSessionId.value, {
        temperature: settings.value.temperature,
        maxTokens: settings.value.maxTokens
      })

      if (result.success) {
        // 添加AI回复
        const aiMessage = {
          id: Date.now() + 1,
          role: 'assistant',
          content: result.response,
          timestamp: result.timestamp
        }
        messages.value.push(aiMessage)
      } else {
        throw new Error(result.error)
      }
    } catch (err) {
      error.value = err.message
      console.error('发送消息失败:', err)
    } finally {
      isLoading.value = false
      isTyping.value = false
    }
  }

  const sendStreamMessage = async (content, onChunk, options = {}) => {
    if (!isInitialized.value) {
      throw new Error('AI服务未初始化')
    }

    try {
      isLoading.value = true
      isTyping.value = true
      error.value = null

      // 添加用户消息
      const userMessage = {
        id: Date.now(),
        role: 'user',
        content: content,
        timestamp: Date.now()
      }
      messages.value.push(userMessage)

      // 创建AI消息占位符
      const aiMessage = {
        id: Date.now() + 1,
        role: 'assistant',
        content: '',
        timestamp: Date.now(),
        isStreaming: true
      }
      messages.value.push(aiMessage)

      // 处理流式响应
      let fullContent = ''
      const handleChunk = (chunk) => {
        if (chunk.error) {
          throw new Error(chunk.error)
        }
        
        if (chunk.content) {
          fullContent += chunk.content
          aiMessage.content = fullContent
          
          // 调用外部处理函数
          if (onChunk) {
            onChunk(chunk, fullContent)
          }
        }
      }

      // 调用流式API
      await mcpService.chatStream(content, handleChunk, currentSessionId.value, {
        temperature: settings.value.temperature,
        maxTokens: settings.value.maxTokens
      })

      // 完成流式响应
      aiMessage.isStreaming = false
    } catch (err) {
      error.value = err.message
      console.error('流式消息发送失败:', err)
    } finally {
      isLoading.value = false
      isTyping.value = false
    }
  }

  const analyzeCode = async (code, context = {}) => {
    if (!isInitialized.value) {
      throw new Error('AI服务未初始化')
    }

    try {
      isLoading.value = true
      error.value = null

      const result = await mcpService.analyzeCode(code, context, currentSessionId.value)
      codeAnalysisResult.value = result

      return result
    } catch (err) {
      error.value = err.message
      console.error('代码分析失败:', err)
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const diagnoseIssue = async (issueData) => {
    if (!isInitialized.value) {
      throw new Error('AI服务未初始化')
    }

    try {
      isLoading.value = true
      error.value = null

      const result = await mcpService.diagnoseIssue(issueData, currentSessionId.value)
      diagnosticResult.value = result

      return result
    } catch (err) {
      error.value = err.message
      console.error('问题诊断失败:', err)
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const generateSuggestion = async (currentConfig) => {
    if (!isInitialized.value) {
      throw new Error('AI服务未初始化')
    }

    try {
      isLoading.value = true
      error.value = null

      const result = await mcpService.generateSuggestion(currentConfig, currentSessionId.value)
      suggestionResult.value = result

      return result
    } catch (err) {
      error.value = err.message
      console.error('建议生成失败:', err)
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const addOPCUAContext = (opcuaData) => {
    if (isInitialized.value) {
      mcpService.addOPCUAContext(opcuaData, currentSessionId.value)
    }
  }

  const clearMessages = () => {
    messages.value = []
  }

  const clearResults = () => {
    codeAnalysisResult.value = null
    diagnosticResult.value = null
    suggestionResult.value = null
  }

  const updateSettings = (newSettings) => {
    settings.value = { ...settings.value, ...newSettings }
  }

  const createNewSession = (sessionId) => {
    mcpService.createSession(sessionId)
    currentSessionId.value = sessionId
    clearMessages()
    clearResults()
  }

  const switchSession = (sessionId) => {
    const session = mcpService.getSession(sessionId)
    if (session) {
      currentSessionId.value = sessionId
      // 这里可以加载会话的历史消息
      clearMessages()
      clearResults()
    }
  }

  const deleteSession = (sessionId) => {
    mcpService.deleteSession(sessionId)
    if (currentSessionId.value === sessionId) {
      currentSessionId.value = 'default'
      clearMessages()
      clearResults()
    }
  }

  const cleanup = () => {
    mcpService.cleanupSessions()
  }

  const getAllSessions = () => {
    return mcpService.getAllSessions() || []
  }

  return {
    // 状态
    isInitialized,
    isLoading,
    currentSessionId,
    apiKey,
    error,
    messages,
    isTyping,
    codeAnalysisResult,
    diagnosticResult,
    suggestionResult,
    settings,
    
    // 计算属性
    hasMessages,
    lastMessage,
    sessionCount,
    
    // 动作
    initialize,
    sendMessage,
    sendStreamMessage,
    analyzeCode,
    diagnoseIssue,
    generateSuggestion,
    addOPCUAContext,
    clearMessages,
    clearResults,
    updateSettings,
    createNewSession,
    switchSession,
    deleteSession,
    cleanup,
    getAllSessions
  }
})
