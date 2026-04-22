<template>
  <div class="diagnostic-panel">
    <!-- 问题描述区域 -->
    <div class="problem-section">
      <div class="section-header">
        <h3>问题诊断</h3>
        <p>描述您遇到的问题，AI将为您提供专业的诊断和解决方案</p>
      </div>
      
      <div class="problem-input">
        <el-input
          v-model="problemDescription"
          type="textarea"
          :rows="4"
          placeholder="请详细描述您遇到的问题..."
          class="description-input"
        />
      </div>
      
      <div class="quick-problems">
        <h4>常见问题快速选择：</h4>
        <div class="problem-tags">
          <el-tag 
            v-for="problem in commonProblems" 
            :key="problem.id"
            @click="selectProblem(problem)"
            :type="selectedProblem?.id === problem.id ? 'primary' : undefined"
            class="problem-tag"
            effect="plain"
          >
            {{ problem.name }}
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 环境信息区域 -->
    <div class="environment-section">
      <div class="section-header">
        <h3>环境信息</h3>
        <p>提供环境信息有助于更准确的诊断</p>
      </div>
      
      <div class="environment-form">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="浏览器">
              <el-input v-model="environmentInfo.browser" placeholder="如: Chrome 120.0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="OPC UA客户端版本">
              <el-input v-model="environmentInfo.clientVersion" placeholder="如: 1.0.3" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="连接状态">
              <el-select v-model="environmentInfo.connectionStatus" placeholder="选择状态">
                <el-option label="已连接" value="connected" />
                <el-option label="连接失败" value="failed" />
                <el-option label="连接超时" value="timeout" />
                <el-option label="未连接" value="disconnected" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="错误代码">
              <el-input v-model="environmentInfo.errorCode" placeholder="如: 0x80000000" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="错误日志">
          <el-input
            v-model="environmentInfo.logs"
            type="textarea"
            :rows="3"
            placeholder="粘贴错误日志..."
          />
        </el-form-item>
      </div>
    </div>

    <!-- 配置信息区域 -->
    <div class="config-section">
      <div class="section-header">
        <h3>相关配置</h3>
        <p>提供相关配置信息（可选）</p>
      </div>
      
      <div class="config-input">
        <el-input
          v-model="configInfo"
          type="textarea"
          :rows="6"
          placeholder="粘贴相关配置信息（JSON格式）..."
          class="config-textarea"
        />
        <div class="config-actions">
          <el-button @click="loadCurrentConfig" size="small" plain>
            <el-icon><Refresh /></el-icon>
            加载当前配置
          </el-button>
          <el-button @click="validateConfig" size="small" plain>
            <el-icon><Check /></el-icon>
            验证格式
          </el-button>
        </div>
      </div>
    </div>

    <!-- 诊断按钮 -->
    <div class="diagnostic-actions">
      <el-button 
        @click="startDiagnosis" 
        :loading="aiStore.isLoading"
        type="primary"
        size="large"
        :disabled="!problemDescription.trim()"
      >
        <el-icon><Search /></el-icon>
        开始诊断
      </el-button>
      <el-button @click="clearAll" :disabled="aiStore.isLoading">
        <el-icon><Delete /></el-icon>
        清空所有
      </el-button>
    </div>

    <!-- 诊断结果区域 -->
    <div class="result-section" v-if="aiStore.diagnosticResult">
      <div class="section-header">
        <h3>诊断结果</h3>
        <div class="result-actions">
          <el-button @click="copyDiagnosis" size="small" plain>
            <el-icon><DocumentCopy /></el-icon>
            复制结果
          </el-button>
          <el-button @click="exportDiagnosis" size="small" plain>
            <el-icon><Edit /></el-icon>
            导出报告
          </el-button>
        </div>
      </div>
      
      <div class="diagnostic-result">
        <div v-if="aiStore.diagnosticResult.success" class="success-result">
          <div class="result-content" v-html="formatDiagnosticResult(aiStore.diagnosticResult.diagnosis)"></div>
          <div class="result-meta">
            <span class="diagnosis-time">
              诊断时间: {{ formatTime(aiStore.diagnosticResult.timestamp) }}
            </span>
          </div>
        </div>
        
        <div v-else class="error-result">
          <el-alert
            :title="aiStore.diagnosticResult.error"
            type="error"
            :closable="false"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Delete, Refresh, Check, DocumentCopy, Edit } from '@element-plus/icons-vue'
import { useAIStore } from '../stores/aiStore.js'

// Store
const aiStore = useAIStore()

// 响应式数据
const problemDescription = ref('')
const selectedProblem = ref(null)
const environmentInfo = reactive({
  browser: '',
  clientVersion: '',
  connectionStatus: '',
  errorCode: '',
  logs: ''
})
const configInfo = ref('')

// 常见问题
const commonProblems = ref([
  { id: 'connection', name: '连接失败' },
  { id: 'timeout', name: '连接超时' },
  { id: 'certificate', name: '证书问题' },
  { id: 'permission', name: '权限错误' },
  { id: 'dataType', name: '数据类型错误' },
  { id: 'subscription', name: '订阅失败' },
  { id: 'browse', name: '节点浏览失败' },
  { id: 'read', name: '数据读取失败' },
  { id: 'write', name: '数据写入失败' },
  { id: 'performance', name: '性能问题' }
])

// 选择问题
const selectProblem = (problem) => {
  selectedProblem.value = problem
  problemDescription.value = getProblemDescription(problem.id)
}

// 获取问题描述
const getProblemDescription = (problemId) => {
  const descriptions = {
    connection: 'OPC UA客户端无法连接到服务器，请检查网络连接和服务器地址。',
    timeout: '连接请求超时，可能是网络延迟或服务器响应慢。',
    certificate: '证书验证失败，请检查客户端和服务器的证书配置。',
    permission: '访问权限不足，请检查用户权限和节点访问权限。',
    dataType: '数据类型不匹配，请检查数据类型定义和转换。',
    subscription: '数据订阅创建失败，请检查订阅参数和节点状态。',
    browse: '节点浏览失败，请检查节点ID和浏览参数。',
    read: '数据读取失败，请检查节点状态和读取权限。',
    write: '数据写入失败，请检查节点状态和写入权限。',
    performance: '系统性能问题，请检查资源使用和优化配置。'
  }
  return descriptions[problemId] || ''
}

// 加载当前配置
const loadCurrentConfig = () => {
  // 这里可以从父组件获取当前配置
  const currentConfig = {
    serverUrl: 'opc.tcp://localhost:4840',
    securityMode: 'None',
    securityPolicy: 'None',
    sessionTimeout: 60000,
    connectionTimeout: 10000
  }
  
  configInfo.value = JSON.stringify(currentConfig, null, 2)
  ElMessage.success('已加载当前配置')
}

// 验证配置格式
const validateConfig = () => {
  if (!configInfo.value.trim()) {
    ElMessage.warning('请先输入配置信息')
    return
  }

  try {
    JSON.parse(configInfo.value)
    ElMessage.success('配置格式正确')
  } catch (error) {
    ElMessage.error('配置格式错误: ' + error.message)
  }
}

// 开始诊断
const startDiagnosis = async () => {
  if (!problemDescription.value.trim()) {
    ElMessage.warning('请描述您遇到的问题')
    return
  }

  try {
    const issueData = {
      description: problemDescription.value,
      environment: environmentInfo,
      config: configInfo.value ? JSON.parse(configInfo.value) : null,
      logs: environmentInfo.logs ? environmentInfo.logs.split('\n') : []
    }

    await aiStore.diagnoseIssue(issueData)
    ElMessage.success('诊断完成')
  } catch (error) {
    ElMessage.error('诊断失败: ' + error.message)
  }
}

// 清空所有
const clearAll = () => {
  problemDescription.value = ''
  selectedProblem.value = null
  Object.keys(environmentInfo).forEach(key => {
    environmentInfo[key] = ''
  })
  configInfo.value = ''
}

// 复制诊断结果
const copyDiagnosis = async () => {
  if (aiStore.diagnosticResult?.diagnosis) {
    try {
      await navigator.clipboard.writeText(aiStore.diagnosticResult.diagnosis)
      ElMessage.success('诊断结果已复制到剪贴板')
    } catch (error) {
      ElMessage.error('复制失败')
    }
  }
}

// 导出诊断报告
const exportDiagnosis = () => {
  if (aiStore.diagnosticResult?.diagnosis) {
    const report = `# OPC UA问题诊断报告

## 问题描述
${problemDescription.value}

## 环境信息
- 浏览器: ${environmentInfo.browser}
- 客户端版本: ${environmentInfo.clientVersion}
- 连接状态: ${environmentInfo.connectionStatus}
- 错误代码: ${environmentInfo.errorCode}

## 诊断结果
${aiStore.diagnosticResult.diagnosis}

## 诊断时间
${formatTime(aiStore.diagnosticResult.timestamp)}
`

    const blob = new Blob([report], { type: 'text/plain' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `diagnosis-report-${Date.now()}.md`
    a.click()
    URL.revokeObjectURL(url)
    ElMessage.success('诊断报告已导出')
  }
}

// 格式化诊断结果
const formatDiagnosticResult = (content) => {
  return content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/`(.*?)`/g, '<code>$1</code>')
    .replace(/\n/g, '<br>')
}

// 格式化时间
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN')
}
</script>

<style scoped>
.diagnostic-panel {
  display: flex;
  flex-direction: column;
  gap: 24px;
  height: calc(100% - 500px);
  overflow: auto;
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

.problem-section,
.environment-section,
.config-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.problem-input {
  margin-bottom: 16px;
}

.description-input {
  margin-bottom: 16px;
}

.quick-problems h4 {
  margin: 0 0 12px 0;
  color: #333;
  font-size: 14px;
}

.problem-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.problem-tag {
  cursor: pointer;
  transition: all 0.3s;
}

.problem-tag:hover {
  transform: translateY(-1px);
}

.environment-form {
  margin-top: 16px;
}

.config-input {
  margin-top: 16px;
}

.config-textarea {
  margin-bottom: 12px;
}

.config-textarea :deep(.el-textarea__inner) {
  font-family: 'Courier New', monospace;
  font-size: 14px;
}

.config-actions {
  display: flex;
  gap: 8px;
}

.diagnostic-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 8px;
}

.result-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.result-actions {
  display: flex;
  gap: 8px;
}

.diagnostic-result {
  margin-top: 16px;
}

.success-result {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px;
  background: #fafafa;
}

.result-content {
  line-height: 1.6;
  color: #333;
}

.result-content :deep(code) {
  background: #f0f0f0;
  padding: 2px 4px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}

.result-meta {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #e0e0e0;
  font-size: 12px;
  color: #999;
}

.error-result {
  margin-top: 16px;
}
</style>
