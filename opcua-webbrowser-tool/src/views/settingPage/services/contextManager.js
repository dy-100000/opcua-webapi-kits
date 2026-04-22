/**
 * 上下文管理器
 * 管理AI对话的上下文信息
 */
class ContextManager {
  constructor() {
    this.contexts = new Map()
    this.maxContextLength = 10
  }

  /**
   * 添加上下文
   * @param {string} sessionId - 会话ID
   * @param {Object} context - 上下文信息
   */
  addContext(sessionId, context) {
    if (!this.contexts.has(sessionId)) {
      this.contexts.set(sessionId, [])
    }

    const contexts = this.contexts.get(sessionId)
    contexts.push({
      ...context,
      timestamp: Date.now()
    })

    // 保持上下文数量在限制内
    if (contexts.length > this.maxContextLength) {
      contexts.shift()
    }
  }

  /**
   * 获取上下文
   * @param {string} sessionId - 会话ID
   * @returns {Array} 上下文数组
   */
  getContext(sessionId) {
    return this.contexts.get(sessionId) || []
  }

  /**
   * 清除上下文
   * @param {string} sessionId - 会话ID
   */
  clearContext(sessionId) {
    this.contexts.delete(sessionId)
  }

  /**
   * 获取OPC UA相关上下文
   * @param {Object} opcuaData - OPC UA数据
   * @returns {Object} 格式化的上下文
   */
  getOPCUAContext(opcuaData) {
    return {
      type: 'opcua',
      nodeId: opcuaData.nodeId,
      browseName: opcuaData.browseName,
      nodeClass: opcuaData.nodeClass,
      dataType: opcuaData.dataType,
      value: opcuaData.value,
      description: opcuaData.description,
      timestamp: Date.now()
    }
  }

  /**
   * 获取代码分析上下文
   * @param {string} code - 代码内容
   * @param {Object} metadata - 元数据
   * @returns {Object} 格式化的上下文
   */
  getCodeContext(code, metadata = {}) {
    return {
      type: 'code',
      code: code,
      language: metadata.language || 'javascript',
      filePath: metadata.filePath,
      functionName: metadata.functionName,
      timestamp: Date.now()
    }
  }

  /**
   * 获取错误诊断上下文
   * @param {Object} error - 错误信息
   * @param {Object} environment - 环境信息
   * @returns {Object} 格式化的上下文
   */
  getErrorContext(error, environment = {}) {
    return {
      type: 'error',
      message: error.message,
      stack: error.stack,
      code: error.code,
      environment: environment,
      timestamp: Date.now()
    }
  }

  /**
   * 构建完整的上下文消息
   * @param {string} sessionId - 会话ID
   * @param {string} currentMessage - 当前消息
   * @returns {Array} 完整的消息数组
   */
  buildContextMessages(sessionId, currentMessage) {
    const contexts = this.getContext(sessionId)
    const messages = []

    // 添加系统提示
    messages.push({
      role: 'system',
      content: this.getSystemPrompt()
    })

    // 添加上下文信息
    if (contexts.length > 0) {
      const contextSummary = this.summarizeContexts(contexts)
      messages.push({
        role: 'assistant',
        content: `之前的对话上下文：\n${contextSummary}`
      })
    }

    // 添加当前消息
    messages.push({
      role: 'user',
      content: currentMessage
    })

    return messages
  }

  /**
   * 获取系统提示
   * @returns {string} 系统提示内容
   */
  getSystemPrompt() {
    return `你是一个专业的OPC UA技术助手，具有以下能力：

1. **OPC UA专业知识**：
   - 熟悉OPC UA规范和技术标准
   - 了解节点类型、数据类型、引用类型等概念
   - 掌握OPC UA客户端和服务器的配置

2. **代码分析能力**：
   - 分析JavaScript/TypeScript代码
   - 识别OPC UA相关的代码模式
   - 提供代码优化建议

3. **问题诊断能力**：
   - 分析连接问题和配置错误
   - 提供故障排除建议
   - 解释错误信息和解决方案

4. **回答风格**：
   - 提供准确、专业的技术建议
   - 使用清晰的中文回答
   - 包含具体的代码示例和配置说明
   - 优先考虑实用性和可操作性

请根据用户的问题和上下文信息，提供最有帮助的回答。`
  }

  /**
   * 总结上下文信息
   * @param {Array} contexts - 上下文数组
   * @returns {string} 总结内容
   */
  summarizeContexts(contexts) {
    return contexts.map(ctx => {
      switch (ctx.type) {
        case 'opcua':
          return `OPC UA节点: ${ctx.browseName} (${ctx.nodeId}), 类型: ${ctx.nodeClass}`
        case 'code':
          return `代码分析: ${ctx.language}文件${ctx.filePath ? ` (${ctx.filePath})` : ''}`
        case 'error':
          return `错误诊断: ${ctx.message}`
        default:
          return `上下文: ${JSON.stringify(ctx)}`
      }
    }).join('\n')
  }
}

export default ContextManager
