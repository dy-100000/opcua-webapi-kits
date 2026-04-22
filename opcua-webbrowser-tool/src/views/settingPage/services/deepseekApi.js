/**
 * DeepSeek API客户端
 * 用于与DeepSeek API进行通信
 */
class DeepSeekClient {
  constructor(apiKey, baseUrl = 'https://api.deepseek.com') {
    this.apiKey = apiKey
    this.baseUrl = baseUrl
    this.model = 'deepseek-chat'
    this.maxRetries = 3
    this.retryDelay = 1000
  }

  /**
   * 发送聊天请求到DeepSeek API
   * @param {Array} messages - 消息数组
   * @param {Object} options - 可选参数
   * @returns {Promise<Object>} API响应
   */
  async chat(messages, options = {}) {
    const requestOptions = {
      model: this.model,
      messages: messages,
      temperature: options.temperature || 0.7,
      max_tokens: options.maxTokens || 2000,
      stream: options.stream || false
    }

    for (let attempt = 1; attempt <= this.maxRetries; attempt++) {
      try {
        const response = await fetch(`${this.baseUrl}/v1/chat/completions`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${this.apiKey}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(requestOptions)
        })

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }

        const data = await response.json()
        return data
      } catch (error) {
        console.error(`DeepSeek API请求失败 (尝试 ${attempt}/${this.maxRetries}):`, error)
        
        if (attempt === this.maxRetries) {
          throw new Error(`DeepSeek API请求失败: ${error.message}`)
        }
        
        // 等待后重试
        await new Promise(resolve => setTimeout(resolve, this.retryDelay * attempt))
      }
    }
  }

  /**
   * 流式聊天请求
   * @param {Array} messages - 消息数组
   * @param {Function} onChunk - 处理数据块的函数
   * @param {Object} options - 可选参数
   */
  async chatStream(messages, onChunk, options = {}) {
    const requestOptions = {
      model: this.model,
      messages: messages,
      temperature: options.temperature || 0.7,
      max_tokens: options.maxTokens || 2000,
      stream: true
    }

    try {
      const response = await fetch(`${this.baseUrl}/v1/chat/completions`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${this.apiKey}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestOptions)
      })

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }

      const reader = response.body.getReader()
      const decoder = new TextDecoder()

      while (true) {
        const { done, value } = await reader.read()
        if (done) break

        const chunk = decoder.decode(value)
        const lines = chunk.split('\n')

        for (const line of lines) {
          if (line.startsWith('data: ')) {
            const data = line.slice(6)
            if (data === '[DONE]') return

            try {
              const parsed = JSON.parse(data)
              if (parsed.choices && parsed.choices[0] && parsed.choices[0].delta) {
                onChunk(parsed.choices[0].delta)
              }
            } catch (e) {
              // 忽略解析错误
            }
          }
        }
      }
    } catch (error) {
      console.error('DeepSeek流式请求失败:', error)
      throw error
    }
  }

  /**
   * 验证API密钥
   * @returns {Promise<boolean>} 验证结果
   */
  async validateApiKey() {
    try {
      await this.chat([{ role: 'user', content: 'test' }], { maxTokens: 1 })
      return true
    } catch (error) {
      return false
    }
  }
}

export default DeepSeekClient
