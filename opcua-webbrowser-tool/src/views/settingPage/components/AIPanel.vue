<template>
  <div class="ai-panel">
    <!-- AI状态指示器 -->
    <div class="ai-status-bar">
      <div class="status-indicator">
        <el-icon :class="['status-icon', aiStore.isInitialized ? 'connected' : 'disconnected']">
          <Connection />
        </el-icon>
        <span class="status-text">
          {{ aiStore.isInitialized ? 'AI服务已连接' : 'AI服务未连接' }}
        </span>
      </div>
      
      <div class="status-actions" v-if="!aiStore.isInitialized">
        <el-button @click="showApiKeyDialog = true" type="primary" size="small">
          <el-icon><Setting /></el-icon>
          配置API
        </el-button>
      </div>
    </div>

    <!-- AI功能标签页 -->
    <el-tabs v-model="activeTab" type="card" class="ai-tabs">
      <el-tab-pane label="AI助手" name="chat">
        <AIChat :session-id="aiStore.currentSessionId" />
      </el-tab-pane>
      
      <el-tab-pane label="代码分析" name="analysis">
        <CodeAnalysis />
      </el-tab-pane>
      
      <el-tab-pane label="问题诊断" name="diagnostic">
        <DiagnosticPanel />
      </el-tab-pane>
      
      <el-tab-pane label="AI设置" name="settings">
        <AISettings />
      </el-tab-pane>
    </el-tabs>

    <!-- API密钥配置对话框 -->
    <el-dialog
      v-model="showApiKeyDialog"
      title="配置DeepSeek API"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="api-config-dialog">
        <el-form :model="tempApiConfig" label-width="100px">
          <el-form-item label="API密钥" required>
            <el-input
              v-model="tempApiConfig.apiKey"
              type="password"
              placeholder="请输入DeepSeek API密钥"
              show-password
            />
            <div class="form-tip">
              <el-link href="https://platform.deepseek.com/api_keys" target="_blank" type="primary">
                获取API密钥
              </el-link>
            </div>
          </el-form-item>
          
          <el-form-item label="API地址">
            <el-input
              v-model="tempApiConfig.baseUrl"
              placeholder="https://api.deepseek.com"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showApiKeyDialog = false">取消</el-button>
          <el-button 
            @click="initializeAI" 
            :loading="aiStore.isLoading"
            type="primary"
            :disabled="!tempApiConfig.apiKey"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 快速操作浮动按钮 -->
    <div class="quick-actions" v-if="aiStore.isInitialized">
      <el-tooltip content="分析当前节点" placement="left">
        <el-button 
          @click="analyzeCurrentNode"
          :loading="aiStore.isLoading"
          type="primary"
          circle
          size="large"
          class="quick-action-btn"
        >
          <el-icon><Search /></el-icon>
        </el-button>
      </el-tooltip>
      
      <el-tooltip content="诊断问题" placement="left">
        <el-button 
          @click="quickDiagnose"
          :loading="aiStore.isLoading"
          type="success"
          circle
          size="large"
          class="quick-action-btn"
        >
          <el-icon><Warning /></el-icon>
        </el-button>
      </el-tooltip>
      
      <el-tooltip content="获取建议" placement="left">
        <el-button 
          @click="getSuggestions"
          :loading="aiStore.isLoading"
          type="warning"
          circle
          size="large"
          class="quick-action-btn"
        >
          <el-icon><CircleCheck /></el-icon>
        </el-button>
      </el-tooltip>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Connection, Setting, Search, Warning, CircleCheck } from '@element-plus/icons-vue'
import { useAIStore } from '../stores/aiStore.js'
import AIChat from './AIChat.vue'
import CodeAnalysis from './CodeAnalysis.vue'
import DiagnosticPanel from './DiagnosticPanel.vue'
import AISettings from './AISettings.vue'

// Props
const props = defineProps({
  currentNode: {
    type: Object,
    default: () => ({})
  },
  currentConfig: {
    type: Object,
    default: () => ({})
  },
  treeData: {
    type: Array,
    default: () => []
  }
})

// Store
const aiStore = useAIStore()

// 响应式数据
const activeTab = ref('chat')
const showApiKeyDialog = ref(false)
const tempApiConfig = reactive({
  apiKey: '',
  baseUrl: 'https://api.deepseek.com'
})

// 初始化AI服务
const initializeAI = async () => {
  try {
    await aiStore.initialize(tempApiConfig.apiKey, { baseUrl: tempApiConfig.baseUrl })
    showApiKeyDialog.value = false
    ElMessage.success('AI服务初始化成功')
  } catch (error) {
    ElMessage.error('AI服务初始化失败: ' + error.message)
  }
}

// 分析当前节点
const analyzeCurrentNode = async () => {
  if (!props.currentNode || !props.currentNode.nodeId) {
    ElMessage.warning('请先选择一个节点')
    return
  }

  try {
    const nodeInfo = {
      nodeId: props.currentNode?.nodeId || '',
      browseName: props.currentNode?.browseName || '',
      nodeClass: props.currentNode?.nodeClass || '',
      dataType: props.currentNode?.dataType || '',
      value: props.currentNode?.value || null
    }

    // 添加OPC UA上下文
    aiStore.addOPCUAContext(nodeInfo)

    // 发送分析请求
    const message = `请分析当前选中的OPC UA节点：
- 节点ID: ${nodeInfo.nodeId}
- 浏览名称: ${nodeInfo.browseName}
- 节点类: ${nodeInfo.nodeClass}
- 数据类型: ${nodeInfo.dataType}
- 当前值: ${nodeInfo.value}

请从以下几个方面进行分析：
1. 节点配置是否正确
2. 数据类型是否合适
3. 值范围是否合理
4. 可能的优化建议`

    await aiStore.sendMessage(message)
    activeTab.value = 'chat'
  } catch (error) {
    ElMessage.error('节点分析失败: ' + error.message)
  }
}

// 快速诊断
const quickDiagnose = async () => {
  try {
    const diagnosticData = {
      description: '请检查当前OPC UA配置是否存在问题',
      environment: {
        browser: navigator.userAgent,
        clientVersion: '1.0.0',
        connectionStatus: 'unknown'
      },
      config: props.currentConfig
    }

    await aiStore.diagnoseIssue(diagnosticData)
    activeTab.value = 'diagnostic'
  } catch (error) {
    ElMessage.error('问题诊断失败: ' + error.message)
  }
}

// 获取建议
const getSuggestions = async () => {
  try {
    const configData = {
      ...props.currentConfig,
      currentNode: props.currentNode,
      treeStructure: props.treeData
    }

    await aiStore.generateSuggestion(configData)
    activeTab.value = 'chat'
    
    // 发送建议请求到聊天
    const message = `基于当前配置，请提供优化建议：
- 当前配置: ${JSON.stringify(props.currentConfig, null, 2)}
- 选中节点: ${props.currentNode?.browseName || '无'}
- 树结构节点数: ${props.treeData?.length || 0}

请提供具体的优化建议。`
    
    await aiStore.sendMessage(message)
  } catch (error) {
    ElMessage.error('获取建议失败: ' + error.message)
  }
}

// 监听当前节点变化，自动添加上下文
watch(() => props.currentNode, (newNode) => {
  if (newNode && newNode.nodeId && aiStore.isInitialized) {
    aiStore.addOPCUAContext(newNode)
  }
}, { deep: true })

// 组件挂载时检查是否需要显示API配置对话框
onMounted(() => {
  if (!aiStore.isInitialized) {
    // 检查是否有保存的API配置
    const savedConfig = localStorage.getItem('ai-api-config')
    if (savedConfig) {
      try {
        const config = JSON.parse(savedConfig)
        if (config.apiKey) {
          tempApiConfig.apiKey = config.apiKey
          tempApiConfig.baseUrl = config.baseUrl || 'https://api.deepseek.com'
          initializeAI()
        } else {
          showApiKeyDialog.value = true
        }
      } catch (error) {
        showApiKeyDialog.value = true
      }
    } else {
      showApiKeyDialog.value = true
    }
  }
})
</script>

<style scoped>
.ai-panel {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f5f5f5;
}

.ai-status-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-icon {
  font-size: 16px;
}

.status-icon.connected {
  color: #67c23a;
}

.status-icon.disconnected {
  color: #f56c6c;
}

.status-text {
  font-size: 14px;
  color: #333;
}

.status-actions {
  display: flex;
  gap: 8px;
}

.ai-tabs {
  flex: 1;
  background: #fff;
}

.ai-tabs :deep(.el-tabs__content) {
  height: calc(100% - 40px);
  overflow: auto;
}

.ai-tabs :deep(.el-tab-pane) {
  height: 100%;
}

.api-config-dialog {
  padding: 20px 0;
}

.form-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #666;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.quick-actions {
  position: fixed;
  right: 20px;
  bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  z-index: 1000;
}

.quick-action-btn {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s;
}

.quick-action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .quick-actions {
    right: 10px;
    bottom: 10px;
  }
  
  .quick-action-btn {
    width: 40px;
    height: 40px;
  }
}
</style>
