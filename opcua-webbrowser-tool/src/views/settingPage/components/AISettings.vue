<template>
  <div class="ai-settings">
    <!-- API配置区域 -->
    <div class="api-section">
      <div class="section-header">
        <h3>API配置</h3>
        <p>配置DeepSeek API密钥和相关设置</p>
      </div>
      
      <div class="api-form">
        <el-form :model="apiConfig" label-width="120px">
          <el-form-item label="API密钥" required>
            <el-input
              v-model="apiConfig.apiKey"
              type="password"
              placeholder="请输入DeepSeek API密钥"
              show-password
              @blur="validateApiKey"
            />
            <div class="form-tip">
              <el-link href="https://platform.deepseek.com/api_keys" target="_blank" type="primary">
                获取API密钥
              </el-link>
            </div>
          </el-form-item>
          
          <el-form-item label="API地址">
            <el-input
              v-model="apiConfig.baseUrl"
              placeholder="https://api.deepseek.com"
            />
          </el-form-item>
          
          <el-form-item label="模型">
            <el-select v-model="apiConfig.model" placeholder="选择模型">
              <el-option label="deepseek-chat" value="deepseek-chat" />
              <el-option label="deepseek-coder" value="deepseek-coder" />
            </el-select>
          </el-form-item>
          
          <el-form-item>
            <el-button 
              @click="testConnection" 
              :loading="testingConnection"
              type="primary"
            >
              <el-icon><Connection /></el-icon>
              测试连接
            </el-button>
            <el-button @click="saveApiConfig" :disabled="!apiConfig.apiKey">
              <el-icon><Check /></el-icon>
              保存配置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- AI参数设置 -->
    <div class="params-section">
      <div class="section-header">
        <h3>AI参数设置</h3>
        <p>调整AI回答的参数以获得最佳效果</p>
      </div>
      
      <div class="params-form">
        <el-form :model="aiParams" label-width="120px">
          <el-form-item label="温度值">
            <el-slider
              v-model="aiParams.temperature"
              :min="0"
              :max="1"
              :step="0.1"
              show-input
              :format-tooltip="formatTemperature"
            />
            <div class="param-description">
              控制回答的随机性，0表示最确定，1表示最随机
            </div>
          </el-form-item>
          
          <el-form-item label="最大令牌数">
            <el-input-number
              v-model="aiParams.maxTokens"
              :min="100"
              :max="4000"
              :step="100"
              controls-position="right"
            />
            <div class="param-description">
              控制回答的最大长度，建议1000-2000
            </div>
          </el-form-item>
          
          <el-form-item label="启用流式响应">
            <el-switch v-model="aiParams.enableStreaming" />
            <div class="param-description">
              启用后可以实时显示AI回答过程
            </div>
          </el-form-item>
          
          <el-form-item label="自动保存上下文">
            <el-switch v-model="aiParams.autoSaveContext" />
            <div class="param-description">
              自动保存对话上下文，提供更好的连续性
            </div>
          </el-form-item>
          
          <el-form-item label="最大上下文长度">
            <el-input-number
              v-model="aiParams.maxContextLength"
              :min="5"
              :max="50"
              :step="5"
              controls-position="right"
            />
            <div class="param-description">
              控制保存的上下文消息数量
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 功能设置 -->
    <div class="features-section">
      <div class="section-header">
        <h3>功能设置</h3>
        <p>配置AI功能的使用偏好</p>
      </div>
      
      <div class="features-form">
        <el-form :model="featureSettings" label-width="120px">
          <el-form-item label="代码分析">
            <el-switch v-model="featureSettings.enableCodeAnalysis" />
            <div class="feature-description">
              启用代码分析功能
            </div>
          </el-form-item>
          
          <el-form-item label="问题诊断">
            <el-switch v-model="featureSettings.enableDiagnostic" />
            <div class="feature-description">
              启用问题诊断功能
            </div>
          </el-form-item>
          
          <el-form-item label="智能建议">
            <el-switch v-model="featureSettings.enableSuggestion" />
            <div class="feature-description">
              启用智能建议功能
            </div>
          </el-form-item>
          
          <el-form-item label="自动分析">
            <el-switch v-model="featureSettings.enableAutoAnalysis" />
            <div class="feature-description">
              在节点选择时自动分析
            </div>
          </el-form-item>
          
          <el-form-item label="声音提示">
            <el-switch v-model="featureSettings.enableSoundAlert" />
            <div class="feature-description">
              在AI回答完成时播放提示音
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 会话管理 -->
    <div class="session-section">
      <div class="section-header">
        <h3>会话管理</h3>
        <p>管理AI对话会话</p>
      </div>
      
      <div class="session-management">
        <div class="session-list">
          <div 
            v-for="session in (aiStore.getAllSessions() || [])" 
            :key="session.id"
            :class="['session-item', { active: session.id === aiStore.currentSessionId }]"
            @click="switchSession(session.id)"
          >
            <div class="session-info">
              <div class="session-name">{{ session.id }}</div>
              <div class="session-meta">
                消息数: {{ session.messageCount }} | 
                创建时间: {{ formatTime(session.createdAt) }}
              </div>
            </div>
            <div class="session-actions">
              <el-button 
                @click.stop="deleteSession(session.id)"
                size="small"
                type="danger"
                text
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
        </div>
        
        <div class="session-actions">
          <el-button @click="createNewSession" type="primary" plain>
            <el-icon><Plus /></el-icon>
            新建会话
          </el-button>
          <el-button @click="clearAllSessions" type="danger" plain>
            <el-icon><Delete /></el-icon>
            清空所有
          </el-button>
        </div>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="action-section">
      <el-button @click="saveAllSettings" type="primary" size="large">
        <el-icon><Check /></el-icon>
        保存所有设置
      </el-button>
      <el-button @click="resetToDefault" type="warning" size="large">
        <el-icon><Refresh /></el-icon>
        恢复默认
      </el-button>
      <el-button @click="exportSettings" size="large">
        <el-icon><Edit /></el-icon>
        导出设置
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Connection, Check, Delete, Plus, Refresh, Edit } from '@element-plus/icons-vue'
import { useAIStore } from '../stores/aiStore.js'

// Store
const aiStore = useAIStore()

// 响应式数据
const testingConnection = ref(false)

// API配置
const apiConfig = reactive({
  apiKey: '',
  baseUrl: 'https://api.deepseek.com',
  model: 'deepseek-chat'
})

// AI参数
const aiParams = reactive({
  temperature: 0.7,
  maxTokens: 2000,
  enableStreaming: true,
  autoSaveContext: true,
  maxContextLength: 10
})

// 功能设置
const featureSettings = reactive({
  enableCodeAnalysis: true,
  enableDiagnostic: true,
  enableSuggestion: true,
  enableAutoAnalysis: false,
  enableSoundAlert: false
})

// 验证API密钥
const validateApiKey = async () => {
  if (!apiConfig.apiKey) return
  
  try {
    testingConnection.value = true
    const isValid = await aiStore.deepseekClient?.validateApiKey()
    if (isValid) {
      ElMessage.success('API密钥验证成功')
    } else {
      ElMessage.error('API密钥验证失败')
    }
  } catch (error) {
    ElMessage.error('验证失败: ' + error.message)
  } finally {
    testingConnection.value = false
  }
}

// 测试连接
const testConnection = async () => {
  if (!apiConfig.apiKey) {
    ElMessage.warning('请先输入API密钥')
    return
  }

  try {
    testingConnection.value = true
    await aiStore.initialize(apiConfig.apiKey, { baseUrl: apiConfig.baseUrl })
    ElMessage.success('连接测试成功')
  } catch (error) {
    ElMessage.error('连接测试失败: ' + error.message)
  } finally {
    testingConnection.value = false
  }
}

// 保存API配置
const saveApiConfig = async () => {
  try {
    await aiStore.initialize(apiConfig.apiKey, { 
      baseUrl: apiConfig.baseUrl,
      model: apiConfig.model
    })
    
    // 保存到本地存储
    localStorage.setItem('ai-api-config', JSON.stringify(apiConfig))
    ElMessage.success('API配置已保存')
  } catch (error) {
    ElMessage.error('保存失败: ' + error.message)
  }
}

// 格式化温度值
const formatTemperature = (value) => {
  const descriptions = {
    0: '最确定',
    0.3: '较确定',
    0.7: '平衡',
    1: '最随机'
  }
  return descriptions[value] || value.toString()
}

// 切换会话
const switchSession = (sessionId) => {
  aiStore.switchSession(sessionId)
  ElMessage.success(`已切换到会话: ${sessionId}`)
}

// 删除会话
const deleteSession = async (sessionId) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除会话 "${sessionId}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    aiStore.deleteSession(sessionId)
    ElMessage.success('会话已删除')
  } catch {
    // 用户取消
  }
}

// 创建新会话
const createNewSession = () => {
  const sessionId = `session-${Date.now()}`
  aiStore.createNewSession(sessionId)
  ElMessage.success(`已创建新会话: ${sessionId}`)
}

// 清空所有会话
const clearAllSessions = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空所有会话吗？此操作不可恢复。',
      '确认清空',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    aiStore.cleanup()
    ElMessage.success('所有会话已清空')
  } catch {
    // 用户取消
  }
}

// 保存所有设置
const saveAllSettings = () => {
  try {
    // 更新AI store设置
    aiStore.updateSettings(aiParams)
    
    // 保存到本地存储
    const settings = {
      apiConfig,
      aiParams,
      featureSettings
    }
    localStorage.setItem('ai-settings', JSON.stringify(settings))
    
    ElMessage.success('所有设置已保存')
  } catch (error) {
    ElMessage.error('保存失败: ' + error.message)
  }
}

// 恢复默认设置
const resetToDefault = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要恢复默认设置吗？当前设置将被覆盖。',
      '确认重置',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 重置设置
    Object.assign(apiConfig, {
      apiKey: '',
      baseUrl: 'https://api.deepseek.com',
      model: 'deepseek-chat'
    })
    
    Object.assign(aiParams, {
      temperature: 0.7,
      maxTokens: 2000,
      enableStreaming: true,
      autoSaveContext: true,
      maxContextLength: 10
    })
    
    Object.assign(featureSettings, {
      enableCodeAnalysis: true,
      enableDiagnostic: true,
      enableSuggestion: true,
      enableAutoAnalysis: false,
      enableSoundAlert: false
    })
    
    ElMessage.success('已恢复默认设置')
  } catch {
    // 用户取消
  }
}

// 导出设置
const exportSettings = () => {
  const settings = {
    apiConfig,
    aiParams,
    featureSettings,
    exportTime: new Date().toISOString()
  }
  
  const blob = new Blob([JSON.stringify(settings, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `ai-settings-${Date.now()}.json`
  a.click()
  URL.revokeObjectURL(url)
  
  ElMessage.success('设置已导出')
}

// 格式化时间
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN')
}

// 加载保存的设置
const loadSettings = () => {
  try {
    const savedSettings = localStorage.getItem('ai-settings')
    if (savedSettings) {
      const settings = JSON.parse(savedSettings)
      Object.assign(apiConfig, settings.apiConfig || {})
      Object.assign(aiParams, settings.aiParams || {})
      Object.assign(featureSettings, settings.featureSettings || {})
    }
    
    const savedApiConfig = localStorage.getItem('ai-api-config')
    if (savedApiConfig) {
      const config = JSON.parse(savedApiConfig)
      Object.assign(apiConfig, config)
    }
  } catch (error) {
    console.error('加载设置失败:', error)
  }
}

// 组件挂载时加载设置
onMounted(() => {
  loadSettings()
})
</script>

<style scoped>
.ai-settings {
  display: flex;
  flex-direction: column;
  gap: 24px;
  height: calc(100% - 850px);
  overflow-y: auto;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-header h3 {
  margin: 0;
  color: #333;
}

.section-header p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.api-section,
.params-section,
.features-section,
.session-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.api-form,
.params-form,
.features-form {
  margin-top: 16px;
}

.form-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #666;
}

.param-description,
.feature-description {
  margin-top: 8px;
  font-size: 12px;
  color: #666;
  line-height: 1.4;
}

.session-management {
  margin-top: 16px;
}

.session-list {
  margin-bottom: 16px;
}

.session-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.session-item:hover {
  background: #f5f5f5;
}

.session-item.active {
  border-color: #409eff;
  background: #f0f8ff;
}

.session-info {
  flex: 1;
}

.session-name {
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.session-meta {
  font-size: 12px;
  color: #666;
}

.session-actions {
  display: flex;
  gap: 8px;
}

.action-section {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 8px;
}

/* 滚动条样式 */
.ai-settings::-webkit-scrollbar {
  width: 6px;
}

.ai-settings::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.ai-settings::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.ai-settings::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
