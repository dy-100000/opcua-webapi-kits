<template>
  <div class="code-analysis">
    <!-- 代码输入区域 -->
    <div class="input-section">
      <div class="section-header">
        <h3>代码分析</h3>
        <p>输入您的OPC UA相关代码，AI将为您提供专业的分析建议</p>
      </div>
      
      <div class="code-input">
        <el-input
          v-model="codeInput"
          type="textarea"
          :rows="8"
          placeholder="请粘贴您的代码..."
          class="code-textarea"
        />
        <div class="input-options">
          <el-select v-model="selectedLanguage" placeholder="选择语言" style="width: 120px">
            <el-option label="JavaScript" value="javascript" />
            <el-option label="TypeScript" value="typescript" />
            <el-option label="Python" value="python" />
            <el-option label="C#" value="csharp" />
          </el-select>
          <el-input
            v-model="filePath"
            placeholder="文件路径 (可选)"
            style="width: 200px"
          />
        </div>
      </div>
      
      <div class="action-buttons">
        <el-button 
          @click="analyzeCode" 
          :loading="aiStore.isLoading"
          type="primary"
          :disabled="!codeInput.trim()"
        >
          <el-icon><Search /></el-icon>
          分析代码
        </el-button>
        <el-button @click="clearInput" :disabled="aiStore.isLoading">
          <el-icon><Delete /></el-icon>
          清空
        </el-button>
        <el-button @click="loadSampleCode" :disabled="aiStore.isLoading">
          <el-icon><Document /></el-icon>
          加载示例
        </el-button>
      </div>
    </div>

    <!-- 分析结果区域 -->
    <div class="result-section" v-if="aiStore.codeAnalysisResult">
      <div class="section-header">
        <h3>分析结果</h3>
        <div class="result-actions">
          <el-button @click="copyResult" size="small" plain>
            <el-icon><DocumentCopy /></el-icon>
            复制结果
          </el-button>
          <el-button @click="exportResult" size="small" plain>
            <el-icon><Edit /></el-icon>
            导出
          </el-button>
        </div>
      </div>
      
      <div class="analysis-result">
        <div v-if="aiStore.codeAnalysisResult.success" class="success-result">
          <div class="result-content" v-html="formatAnalysisResult(aiStore.codeAnalysisResult.analysis)"></div>
          <div class="result-meta">
            <span class="analysis-time">
              分析时间: {{ formatTime(aiStore.codeAnalysisResult.timestamp) }}
            </span>
          </div>
        </div>
        
        <div v-else class="error-result">
          <el-alert
            :title="aiStore.codeAnalysisResult.error"
            type="error"
            :closable="false"
          />
        </div>
      </div>
    </div>

    <!-- 示例代码区域 -->
    <div class="sample-section" v-if="showSamples">
      <div class="section-header">
        <h3>示例代码</h3>
        <el-button @click="showSamples = false" size="small" text>
          <el-icon><Close /></el-icon>
        </el-button>
      </div>
      
      <div class="sample-tabs">
        <el-tabs v-model="activeSample" @tab-click="loadSample">
          <el-tab-pane label="OPC UA客户端连接" name="client">
            <pre class="sample-code">{{ sampleCodes.client }}</pre>
          </el-tab-pane>
          <el-tab-pane label="节点浏览" name="browse">
            <pre class="sample-code">{{ sampleCodes.browse }}</pre>
          </el-tab-pane>
          <el-tab-pane label="数据订阅" name="subscription">
            <pre class="sample-code">{{ sampleCodes.subscription }}</pre>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Delete, Document, DocumentCopy, Edit, Close } from '@element-plus/icons-vue'
import { useAIStore } from '../stores/aiStore.js'

// Store
const aiStore = useAIStore()

// 响应式数据
const codeInput = ref('')
const selectedLanguage = ref('javascript')
const filePath = ref('')
const showSamples = ref(false)
const activeSample = ref('client')

// 示例代码
const sampleCodes = reactive({
  client: `// OPC UA客户端连接示例
import { OPCUAClient } from 'node-opcua';

async function connectToOPCUAServer() {
  const client = OPCUAClient.create({
    applicationName: "MyOPCUAClient",
    connectionStrategy: {
      initialDelay: 1000,
      maxDelay: 2000,
      maxRetry: 5
    }
  });

  try {
    await client.connect("opc.tcp://localhost:4840");
    console.log("连接成功");
    
    const session = await client.createSession();
    console.log("会话创建成功");
    
    return { client, session };
  } catch (error) {
    console.error("连接失败:", error);
    throw error;
  }
}`,
  
  browse: `// 节点浏览示例
async function browseNodes(session, nodeId = "i=85") {
  try {
    const browseResult = await session.browse({
      nodeId: nodeId,
      browseDirection: BrowseDirection.Forward,
      referenceTypeId: ReferenceTypeId.HierarchicalReferences,
      includeSubtypes: true,
      nodeClassMask: 0,
      resultMask: 0x3f
    });

    console.log("浏览结果:", browseResult.references);
    
    // 处理每个引用
    for (const reference of browseResult.references) {
      console.log(\`节点: \${reference.browseName.name}, ID: \${reference.nodeId.toString()}\`);
    }
    
    return browseResult.references;
  } catch (error) {
    console.error("浏览失败:", error);
    throw error;
  }
}`,
  
  subscription: `// 数据订阅示例
async function createSubscription(session) {
  try {
    const subscription = await session.createSubscription2({
      requestedPublishingInterval: 1000,
      requestedLifetimeCount: 100,
      requestedMaxKeepAliveCount: 10,
      maxNotificationsPerPublish: 100,
      publishingEnabled: true,
      priority: 10
    });

    // 添加监控项
    const monitoredItem = await subscription.monitor({
      nodeId: "ns=2;s=Temperature",
      attributeId: AttributeIds.Value,
      monitoringMode: MonitoringMode.Reporting
    }, {
      samplingInterval: 1000,
      discardOldest: true,
      queueSize: 10
    });

    // 监听数据变化
    monitoredItem.on("changed", (dataValue) => {
      console.log("数据变化:", dataValue.value.value);
    });

    return subscription;
  } catch (error) {
    console.error("订阅创建失败:", error);
    throw error;
  }
}`
})

// 分析代码
const analyzeCode = async () => {
  if (!codeInput.value.trim()) {
    ElMessage.warning('请输入要分析的代码')
    return
  }

  try {
    const context = {
      language: selectedLanguage.value,
      filePath: filePath.value,
      description: 'OPC UA相关代码分析'
    }

    await aiStore.analyzeCode(codeInput.value, context)
    ElMessage.success('代码分析完成')
  } catch (error) {
    ElMessage.error('代码分析失败: ' + error.message)
  }
}

// 清空输入
const clearInput = () => {
  codeInput.value = ''
  filePath.value = ''
}

// 加载示例代码
const loadSampleCode = () => {
  showSamples.value = true
}

// 加载选中的示例
const loadSample = (tab) => {
  codeInput.value = sampleCodes[tab.name]
  selectedLanguage.value = 'javascript'
}

// 复制结果
const copyResult = async () => {
  if (aiStore.codeAnalysisResult?.analysis) {
    try {
      await navigator.clipboard.writeText(aiStore.codeAnalysisResult.analysis)
      ElMessage.success('结果已复制到剪贴板')
    } catch (error) {
      ElMessage.error('复制失败')
    }
  }
}

// 导出结果
const exportResult = () => {
  if (aiStore.codeAnalysisResult?.analysis) {
    const blob = new Blob([aiStore.codeAnalysisResult.analysis], { type: 'text/plain' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `code-analysis-${Date.now()}.txt`
    a.click()
    URL.revokeObjectURL(url)
    ElMessage.success('结果已导出')
  }
}

// 格式化分析结果
const formatAnalysisResult = (content) => {
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
.code-analysis {
  display: flex;
  flex-direction: column;
  gap: 24px;
  height: 100%;
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

.input-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.code-input {
  margin-bottom: 16px;
}

.code-textarea {
  margin-bottom: 12px;
}

.code-textarea :deep(.el-textarea__inner) {
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
}

.input-options {
  display: flex;
  gap: 12px;
  align-items: center;
}

.action-buttons {
  display: flex;
  gap: 12px;
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

.analysis-result {
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

.sample-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.sample-tabs {
  margin-top: 16px;
}

.sample-code {
  background: #f5f5f5;
  padding: 16px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
  overflow-x: auto;
  white-space: pre-wrap;
  margin: 0;
}
</style>
