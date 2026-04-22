/**
 * MCP服务层
 * 整合所有AI功能的核心服务
 */
import DeepSeekClient from './deepseekApi.js'
import ContextManager from './contextManager.js'
import PromptBuilder from './promptBuilder.js'

class MCPService {
  constructor() {
    this.contextManager = new ContextManager()
    this.promptBuilder = new PromptBuilder()
    this.deepseekClient = null
    this.isInitialized = false
    this.sessions = new Map() // 存储会话信息
  }

  /**
   * 初始化MCP服务
   * @param {string} apiKey - DeepSeek API密钥
   * @param {Object} options - 配置选项
   */
  async initialize(apiKey, options = {}) {
    try {
      this.deepseekClient = new DeepSeekClient(apiKey, options.baseUrl)
      
      // 验证API密钥
      const isValid = await this.deepseekClient.validateApiKey()
      if (!isValid) {
        throw new Error('DeepSeek API密钥验证失败')
      }

      this.isInitialized = true
      console.log('MCP服务初始化成功')
    } catch (error) {
      console.error('MCP服务初始化失败:', error)
      throw error
    }
  }

  /**
   * 检查服务是否已初始化
   */
  checkInitialized() {
    if (!this.isInitialized) {
      throw new Error('MCP服务未初始化，请先调用initialize方法')
    }
  }

  /**
   * 创建新会话
   * @param {string} sessionId - 会话ID
   * @param {Object} initialContext - 初始上下文
   */
  createSession(sessionId, initialContext = {}) {
    this.sessions.set(sessionId, {
      id: sessionId,
      createdAt: Date.now(),
      context: initialContext,
      messageCount: 0
    })
  }

  /**
   * 获取会话信息
   * @param {string} sessionId - 会话ID
   */
  getSession(sessionId) {
    return this.sessions.get(sessionId)
  }

  /**
   * 删除会话
   * @param {string} sessionId - 会话ID
   */
  deleteSession(sessionId) {
    this.sessions.delete(sessionId)
    this.contextManager.clearContext(sessionId)
  }

  /**
   * 分析代码
   * @param {string} code - 代码内容
   * @param {Object} context - 上下文信息
   * @param {string} sessionId - 会话ID
   * @returns {Promise<Object>} 分析结果
   */
  async analyzeCode(code, context = {}, sessionId = 'default') {
    this.checkInitialized()

    try {
      // 添加上下文
      const codeContext = this.contextManager.getCodeContext(code, context)
      this.contextManager.addContext(sessionId, codeContext)

      // 构建提示词
      const messages = this.promptBuilder.buildCodeAnalysisPrompt(code, context)

      // 调用DeepSeek API
      const response = await this.deepseekClient.chat(messages, {
        temperature: 0.3,
        maxTokens: 3000
      })

      return {
        success: true,
        analysis: response.choices[0].message.content,
        context: codeContext,
        timestamp: Date.now()
      }
    } catch (error) {
      console.error('代码分析失败:', error)
      return {
        success: false,
        error: error.message,
        timestamp: Date.now()
      }
    }
  }

  /**
   * 诊断问题
   * @param {Object} issueData - 问题数据
   * @param {string} sessionId - 会话ID
   * @returns {Promise<Object>} 诊断结果
   */
  async diagnoseIssue(issueData, sessionId = 'default') {
    this.checkInitialized()

    try {
      // 添加上下文
      const errorContext = this.contextManager.getErrorContext(
        issueData.error || { message: issueData.description },
        issueData.environment
      )
      this.contextManager.addContext(sessionId, errorContext)

      // 构建提示词
      const messages = this.promptBuilder.buildDiagnosticPrompt(issueData)

      // 调用DeepSeek API
      const response = await this.deepseekClient.chat(messages, {
        temperature: 0.2,
        maxTokens: 2500
      })

      return {
        success: true,
        diagnosis: response.choices[0].message.content,
        context: errorContext,
        timestamp: Date.now()
      }
    } catch (error) {
      console.error('问题诊断失败:', error)
      return {
        success: false,
        error: error.message,
        timestamp: Date.now()
      }
    }
  }

  /**
   * 生成建议
   * @param {Object} currentConfig - 当前配置
   * @param {string} sessionId - 会话ID
   * @returns {Promise<Object>} 建议结果
   */
  async generateSuggestion(currentConfig, sessionId = 'default') {
    this.checkInitialized()

    try {
      // 构建提示词
      const messages = this.promptBuilder.buildSuggestionPrompt(currentConfig)

      // 调用DeepSeek API
      const response = await this.deepseekClient.chat(messages, {
        temperature: 0.5,
        maxTokens: 2000
      })

      return {
        success: true,
        suggestions: response.choices[0].message.content,
        timestamp: Date.now()
      }
    } catch (error) {
      console.error('建议生成失败:', error)
      return {
        success: false,
        error: error.message,
        timestamp: Date.now()
      }
    }
  }

  /**
   * AI聊天
   * @param {string} message - 用户消息
   * @param {string} sessionId - 会话ID
   * @param {Object} options - 选项
   * @returns {Promise<Object>} 聊天结果
   */
  async chat(message, sessionId = 'default', options = {}) {
    this.checkInitialized()

    try {
      // 获取上下文
      const contexts = this.contextManager.getContext(sessionId)
      
      // 构建提示词
      const messages = this.promptBuilder.buildChatPrompt(message, contexts)

      // 调用DeepSeek API
      const response = await this.deepseekClient.chat(messages, {
        temperature: options.temperature || 0.7,
        maxTokens: options.maxTokens || 2000
      })

      // 更新会话信息
      const session = this.getSession(sessionId)
      if (session) {
        session.messageCount++
        session.lastActivity = Date.now()
      }

      return {
        success: true,
        response: response.choices[0].message.content,
        timestamp: Date.now()
      }
    } catch (error) {
      console.error('AI聊天失败:', error)
      return {
        success: false,
        error: error.message,
        timestamp: Date.now()
      }
    }
  }

  /**
   * 流式聊天
   * @param {string} message - 用户消息
   * @param {Function} onChunk - 处理数据块的函数
   * @param {string} sessionId - 会话ID
   * @param {Object} options - 选项
   */
  async chatStream(message, onChunk, sessionId = 'default', options = {}) {
    this.checkInitialized()

    try {
      // 获取上下文
      const contexts = this.contextManager.getContext(sessionId)
      
      // 构建提示词
      const messages = this.promptBuilder.buildChatPrompt(message, contexts)

      // 调用流式API
      await this.deepseekClient.chatStream(messages, onChunk, {
        temperature: options.temperature || 0.7,
        maxTokens: options.maxTokens || 2000
      })

      // 更新会话信息
      const session = this.getSession(sessionId)
      if (session) {
        session.messageCount++
        session.lastActivity = Date.now()
      }
    } catch (error) {
      console.error('流式聊天失败:', error)
      onChunk({ error: error.message })
    }
  }

  /**
   * 添加OPC UA上下文
   * @param {Object} opcuaData - OPC UA数据
   * @param {string} sessionId - 会话ID
   */
  addOPCUAContext(opcuaData, sessionId = 'default') {
    const context = this.contextManager.getOPCUAContext(opcuaData)
    this.contextManager.addContext(sessionId, context)
  }

  /**
   * 获取所有会话
   * @returns {Array} 会话列表
   */
  getAllSessions() {
    return Array.from(this.sessions.values())
  }

  /**
   * 清理过期会话
   * @param {number} maxAge - 最大年龄（毫秒）
   */
  cleanupSessions(maxAge = 24 * 60 * 60 * 1000) { // 默认24小时
    const now = Date.now()
    for (const [sessionId, session] of this.sessions) {
      if (now - session.createdAt > maxAge) {
        this.deleteSession(sessionId)
      }
    }
  }
}

// 创建单例实例
const mcpService = new MCPService()

export default mcpService
