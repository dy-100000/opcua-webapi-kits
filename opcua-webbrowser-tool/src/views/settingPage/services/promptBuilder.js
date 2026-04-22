/**
 * 提示词构建器
 * 根据不同的功能类型构建相应的提示词
 */
class PromptBuilder {
  constructor() {
    this.templates = {
      codeAnalysis: this.getCodeAnalysisTemplate(),
      diagnostic: this.getDiagnosticTemplate(),
      suggestion: this.getSuggestionTemplate(),
      chat: this.getChatTemplate()
    }
  }

  /**
   * 构建代码分析提示词
   * @param {string} code - 代码内容
   * @param {Object} context - 上下文信息
   * @returns {Array} 消息数组
   */
  buildCodeAnalysisPrompt(code, context = {}) {
    const messages = [
      {
        role: 'system',
        content: this.templates.codeAnalysis
      },
      {
        role: 'user',
        content: `请分析以下${context.language || 'JavaScript'}代码：

\`\`\`${context.language || 'javascript'}
${code}
\`\`\`

${context.description ? `代码描述：${context.description}` : ''}
${context.filePath ? `文件路径：${context.filePath}` : ''}

请从以下几个方面进行分析：
1. 代码结构和逻辑
2. 潜在问题和改进建议
3. 性能优化建议
4. 最佳实践建议`
      }
    ]

    return messages
  }

  /**
   * 构建诊断提示词
   * @param {Object} issueData - 问题数据
   * @returns {Array} 消息数组
   */
  buildDiagnosticPrompt(issueData) {
    const messages = [
      {
        role: 'system',
        content: this.templates.diagnostic
      },
      {
        role: 'user',
        content: `请帮我诊断以下OPC UA相关问题：

**问题描述：**
${issueData.description || '无描述'}

**错误信息：**
${issueData.error ? issueData.error.message : '无错误信息'}

**环境信息：**
- 浏览器：${issueData.environment?.browser || '未知'}
- OPC UA客户端版本：${issueData.environment?.clientVersion || '未知'}
- 连接状态：${issueData.environment?.connectionStatus || '未知'}

**相关配置：**
${issueData.config ? JSON.stringify(issueData.config, null, 2) : '无配置信息'}

**日志信息：**
${issueData.logs ? issueData.logs.join('\n') : '无日志信息'}

请提供详细的诊断分析和解决方案。`
      }
    ]

    return messages
  }

  /**
   * 构建建议提示词
   * @param {Object} currentConfig - 当前配置
   * @returns {Array} 消息数组
   */
  buildSuggestionPrompt(currentConfig) {
    const messages = [
      {
        role: 'system',
        content: this.templates.suggestion
      },
      {
        role: 'user',
        content: `基于当前的OPC UA配置，请提供优化建议：

**当前配置：**
${JSON.stringify(currentConfig, null, 2)}

**使用场景：**
${currentConfig.scenario || '通用OPC UA应用'}

**性能要求：**
${currentConfig.performanceRequirements || '标准性能要求'}

请从以下几个方面提供建议：
1. 配置优化
2. 性能提升
3. 安全性增强
4. 最佳实践应用`
      }
    ]

    return messages
  }

  /**
   * 构建聊天提示词
   * @param {string} message - 用户消息
   * @param {Array} context - 上下文信息
   * @returns {Array} 消息数组
   */
  buildChatPrompt(message, context = []) {
    const messages = [
      {
        role: 'system',
        content: this.templates.chat
      }
    ]

    // 添加上下文信息
    if (context.length > 0) {
      const contextSummary = context.map(ctx => {
        switch (ctx.type) {
          case 'opcua':
            return `当前OPC UA节点：${ctx.browseName} (${ctx.nodeId})`
          case 'code':
            return `正在分析代码：${ctx.filePath || '未知文件'}`
          case 'error':
            return `遇到错误：${ctx.message}`
          default:
            return `上下文：${JSON.stringify(ctx)}`
        }
      }).join('\n')

      messages.push({
        role: 'assistant',
        content: `当前上下文信息：\n${contextSummary}`
      })
    }

    messages.push({
      role: 'user',
      content: message
    })

    return messages
  }

  /**
   * 获取代码分析模板
   * @returns {string} 模板内容
   */
  getCodeAnalysisTemplate() {
    return `你是一个专业的代码分析专家，专门分析OPC UA相关的JavaScript/TypeScript代码。

**分析重点：**
1. **OPC UA特定问题**：
   - 节点ID格式和命名规范
   - 数据类型定义和使用
   - 引用类型和层次结构
   - 客户端连接和会话管理

2. **代码质量**：
   - 错误处理和异常管理
   - 异步操作和Promise使用
   - 内存泄漏和性能问题
   - 代码可读性和维护性

3. **最佳实践**：
   - OPC UA规范遵循
   - 安全性和权限控制
   - 日志记录和调试
   - 测试和验证

**回答格式：**
- 使用清晰的中文回答
- 提供具体的代码示例
- 包含改进前后的对比
- 给出优先级建议`
  }

  /**
   * 获取诊断模板
   * @returns {string} 模板内容
   */
  getDiagnosticTemplate() {
    return `你是一个专业的OPC UA故障诊断专家，能够快速识别和解决各种OPC UA相关问题。

**诊断能力：**
1. **连接问题**：
   - 网络连接和防火墙配置
   - 证书和安全性问题
   - 服务器配置和权限

2. **数据问题**：
   - 数据类型不匹配
   - 值范围错误
   - 访问权限问题

3. **性能问题**：
   - 订阅和监控效率
   - 内存使用和泄漏
   - 网络带宽优化

**诊断流程：**
1. 分析错误信息和日志
2. 识别根本原因
3. 提供具体解决方案
4. 给出预防措施

**回答要求：**
- 提供详细的诊断步骤
- 包含具体的配置修改
- 给出测试和验证方法
- 提供相关文档链接`
  }

  /**
   * 获取建议模板
   * @returns {string} 模板内容
   */
  getSuggestionTemplate() {
    return `你是一个OPC UA架构和配置专家，能够提供专业的优化建议和最佳实践。

**建议范围：**
1. **架构设计**：
   - 节点层次结构设计
   - 数据类型定义策略
   - 引用关系优化

2. **性能优化**：
   - 订阅策略优化
   - 数据采样频率调整
   - 缓存和批处理策略

3. **安全增强**：
   - 访问控制配置
   - 证书管理策略
   - 审计日志设置

4. **运维改进**：
   - 监控和告警设置
   - 备份和恢复策略
   - 版本管理流程

**建议格式：**
- 按优先级排序
- 提供实施步骤
- 包含风险评估
- 给出预期效果`
  }

  /**
   * 获取聊天模板
   * @returns {string} 模板内容
   */
  getChatTemplate() {
    return `你是一个友好的OPC UA技术助手，能够回答各种技术问题并提供专业建议。

**专业领域：**
- OPC UA规范和标准
- JavaScript/TypeScript开发
- 工业自动化和物联网
- 网络通信和安全

**回答风格：**
- 专业但易懂
- 提供实用建议
- 包含代码示例
- 鼓励进一步学习

**注意事项：**
- 如果问题超出专业范围，请诚实说明
- 提供相关资源链接
- 鼓励用户实践和测试
- 保持积极和建设性的态度`
  }
}

export default PromptBuilder
