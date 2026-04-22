<template>
  <div class="process-diagram-container">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button @click="resetView" size="small">
        <el-icon><Refresh /></el-icon>
        重置视图
      </el-button>
      <el-button @click="zoomIn" size="small">
        <el-icon><ZoomIn /></el-icon>
        放大
      </el-button>
      <el-button @click="zoomOut" size="small">
        <el-icon><ZoomOut /></el-icon>
        缩小
      </el-button>
      <el-button @click="refreshData" size="small" :loading="isLoading">
        <el-icon><RefreshRight /></el-icon>
        刷新数据
      </el-button>
    </div>

    <!-- VueFlow容器 -->
    <div class="flow-wrapper">
      <VueFlow
        v-model:nodes="nodes"
        v-model:edges="edges"
        :node-types="nodeTypes"
        :edge-types="edgeTypes"
        :default-viewport="{ zoom: 1, x: 0, y: 0 }"
        :min-zoom="0.5"
        :max-zoom="3"
        :snap-to-grid="true"
        :snap-grid="[10, 10]"
        fit-view-on-init
        @node-double-click="handleNodeDoubleClick"
      >
        <Background pattern-color="#e0e0e0" :gap="20" />
        <Controls />
        <MiniMap />
      </VueFlow>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑节点属性"
      width="500px"
      @close="closeEditDialog"
    >
      <el-form :model="editingNode" label-width="100px">
        <el-form-item label="标签">
          <el-input v-model="editingNode.label" />
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="editingNode.type" disabled />
        </el-form-item>
        <el-form-item label="数值" v-if="editingNode.hasValue">
          <el-input v-model="editingNode.value" />
        </el-form-item>
        <el-form-item label="状态" v-if="editingNode.hasStatus">
          <el-select v-model="editingNode.status">
            <el-option label="正常" value="normal" />
            <el-option label="警告" value="warning" />
            <el-option label="故障" value="error" />
          </el-select>
        </el-form-item>
        <el-form-item label="节点ID" v-if="editingNode.nodeId">
          <el-input v-model="editingNode.nodeId" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, watch, markRaw } from 'vue'
import { VueFlow, useVueFlow } from '@vue-flow/core'
import { Background } from '@vue-flow/background'
import { Controls } from '@vue-flow/controls'
import { MiniMap } from '@vue-flow/minimap'
import { ElMessage } from 'element-plus'
import { Refresh, ZoomIn, ZoomOut, RefreshRight } from '@element-plus/icons-vue'

// 导入自定义节点组件
import SourceNode from './nodes/SourceNode.vue'
import SinkNode from './nodes/SinkNode.vue'
import ValveNode from './nodes/ValveNode.vue'
import ButterflyValveNode from './nodes/ButterflyValveNode.vue'
import TankNode from './nodes/TankNode.vue'
import PumpNode from './nodes/PumpNode.vue'
import MotorNode from './nodes/MotorNode.vue'
import InstrumentNode from './nodes/InstrumentNode.vue'
import DefaultNode from './nodes/DefaultNode.vue'

// 导入自定义边组件
import PipelineEdge from './edges/PipelineEdge.vue'
import InstrumentEdge from './edges/InstrumentEdge.vue'
import InstrumentToEdgeMidpoint from './edges/InstrumentToEdgeMidpoint.vue'
// Props
const props = defineProps({
  nodeDataList: {
    type: Array,
    default: () => []
  },
  linkDataList: {
    type: Array,
    default: () => []
  },
  nodeTypeMap: {
    type: Object,
    default: () => ({})
  }
})

// VueFlow实例
const { zoomIn: flowZoomIn, zoomOut: flowZoomOut, setViewport } = useVueFlow()

// 注册自定义节点类型
const nodeTypes = {
  source: markRaw(SourceNode),
  sink: markRaw(SinkNode),
  valve: markRaw(ValveNode),
  'butterfly-valve': markRaw(ButterflyValveNode),
  tank: markRaw(TankNode),
  pump: markRaw(PumpNode),
  motor: markRaw(MotorNode),
  instrument: markRaw(InstrumentNode),
  default: markRaw(DefaultNode)
}

// 注册自定义边类型
const edgeTypes = {
  pipeline: markRaw(PipelineEdge),
  instrument: markRaw(InstrumentEdge),
  'instrument-to-midpoint': markRaw(InstrumentToEdgeMidpoint)
}

// 状态
const isLoading = ref(false)
const editDialogVisible = ref(false)
const editingNode = reactive({
  id: '',
  label: '',
  type: '',
  value: '',
  status: 'normal',
  nodeId: '',
  hasValue: false,
  hasStatus: false
})

// 节点数据
const nodes = ref([])
const edges = ref([])

// 默认节点数据
const getDefaultNodes = () => {
  const tankX = 400
  const tankY = 150
  const tankWidth = 120
  const tankHeight = 200

  return [
    {
      id: 'source0',
      type: 'source',
      position: { x: 100, y: 190 },
      data: { label: 'Source0', value: '', status: 'normal', nodeId: 'source0' }
    },
    {
      id: 'valve1',
      type: 'butterfly-valve',
      position: { x: 220, y: 190 },
      data: { label: 'H001', value: '', status: 'normal', nodeId: 'valve1' }
    },
    {
      id: 'tank',
      type: 'tank',
      position: { x: tankX, y: tankY },
      data: { label: 'TANK', value: '75%', status: 'normal', nodeId: 'tank', width: tankWidth, height: tankHeight }
    },
    {
      id: 'valve2',
      type: 'butterfly-valve',
      position: { x: tankX + tankWidth / 2 + 40, y: tankY + tankHeight + 30 },
      data: { label: 'H002', value: '', status: 'normal', nodeId: 'valve2' }
    },
    {
      id: 'pump',
      type: 'pump',
      position: { x: tankX + tankWidth + 90, y: tankY + tankHeight - 30 },
      data: { label: 'P001', value: '', status: 'normal', nodeId: 'pump' }
    },
    {
      id: 'motor',
      type: 'motor',
      position: { x: tankX + tankWidth + 95, y: tankY + tankHeight - 120 },
      data: { label: 'M001', value: '', status: 'normal', nodeId: 'motor' }
    },
    {
      id: 'flowmeter',
      type: 'instrument',
      position: { x: ((tankX + tankWidth + 150) + (tankX + tankWidth + 260)) / 2 - 25, y: tankY + tankHeight - 85 },
      data: { label: 'FI 006', value: '', status: 'normal', nodeId: 'flowmeter' }
    },
    {
      id: 'sink0',
      type: 'sink',
      position: { x: tankX + tankWidth + 260, y: tankY + tankHeight - 20 },
      data: { label: 'Sink0', value: '', status: 'normal', nodeId: 'sink0' }
    },
    {
      id: 'lc003',
      type: 'instrument',
      position: { x: tankX - 80, y: tankY + 30 },
      data: { label: 'LC 003', value: '', status: 'normal', nodeId: 'lc003' }
    },
    {
      id: 'l002',
      type: 'instrument',
      position: { x: tankX - 80, y: tankY + 90 },
      data: { label: 'L 002', value: '', status: 'normal', nodeId: 'l002' }
    },
    {
      id: 'lc001',
      type: 'instrument',
      position: { x: tankX - 80, y: tankY + 150 },
      data: { label: 'LC 001', value: '', status: 'normal', nodeId: 'lc001' }
    },
    {
      id: 'li004',
      type: 'instrument',
      position: { x: tankX + 35, y: tankY - 100 },
      data: { label: 'LI 004', value: '', status: 'normal', nodeId: 'li004' }
    },
    {
      id: 'ti005',
      type: 'instrument',
      position: { x: tankX + tankWidth + 30, y: tankY + 90 },
      data: { label: 'TI 005', value: '', status: 'normal', nodeId: 'ti005' }
    }
  ]
}

// 默认边数据
// const getDefaultEdges = () => {
//   return [
//     // 主要管道连接
//     { id: 'e-source0-valve1', source: 'source0', target: 'valve1', type: 'pipeline' },
//     { id: 'e-valve1-tank', source: 'valve1', target: 'tank', type: 'pipeline', targetHandle: 'right-top' },
//     { id: 'e-tank-valve2', source: 'tank', target: 'valve2', type: 'pipeline', sourceHandle: 'bottom', data: { isTankBottom: true } },
//     { id: 'e-valve2-pump', source: 'valve2', target: 'pump', type: 'pipeline' },
//     { id: 'e-pump-sink0', source: 'pump', target: 'sink0', type: 'pipeline' },

//     // 仪表连接（虚线）
//     { id: 'e-li004-tank', source: 'li004', target: 'tank', type: 'instrument', targetHandle: 'top' },
//     { id: 'e-lc003-tank', source: 'lc003', target: 'tank', type: 'instrument', targetHandle: 'left-top' },
//     { id: 'e-l002-tank', source: 'l002', target: 'tank', type: 'instrument', targetHandle: 'left-middle' },
//     { id: 'e-lc001-tank', source: 'lc001', target: 'tank', type: 'instrument', targetHandle: 'left-bottom' },
//     { id: 'e-ti005-tank', source: 'ti005', target: 'tank', type: 'instrument', targetHandle: 'right' },
//     { id: 'e-flowmeter-pump', source: 'flowmeter', target: 'pump', type: 'instrument' },
//     { id: 'e-motor-pump', source: 'motor', target: 'pump', type: 'instrument', sourceHandle: 'bottom', targetHandle: 'motor' }
//   ]
// }
const getDefaultEdges = () => {
    return [
      // 主要管道连接
      { id: 'e-source0-valve1', source: 'source0', target: 'valve1', type: 'pipeline' },
      { id: 'e-valve1-tank', source: 'valve1', target: 'tank', type: 'pipeline', targetHandle: 'right-top' },
      { id: 'e-tank-valve2', source: 'tank', target: 'valve2', type: 'pipeline', sourceHandle: 'bottom', data: { isTankBottom: true } },
      { id: 'e-valve2-pump', source: 'valve2', target: 'pump', type: 'pipeline' },
      { id: 'e-pump-sink0', source: 'pump', target: 'sink0', type: 'pipeline' },

      // flowmeter 连接到 pump-sink0 边的中点
      {
        id: 'e-flowmeter-pipe',
        source: 'flowmeter',
        target: 'sink0',  // 这里 target 随便指定一个，实际终点由 data 计算
        type: 'instrument-to-midpoint',
        data: {
          targetEdgeSource: 'pump',    // 目标边的起点节点
          targetEdgeTarget: 'sink0'    // 目标边的终点节点
        }
      },

      // 其他仪表连接
      { id: 'e-li004-tank', source: 'li004', target: 'tank', type: 'instrument', targetHandle: 'top' },
      { id: 'e-lc003-tank', source: 'lc003', target: 'tank', type: 'instrument', targetHandle: 'left-top' },
      { id: 'e-l002-tank', source: 'l002', target: 'tank', type: 'instrument', targetHandle: 'left-middle' },
      { id: 'e-lc001-tank', source: 'lc001', target: 'tank', type: 'instrument', targetHandle: 'left-bottom' },
      { id: 'e-ti005-tank', source: 'ti005', target: 'tank', type: 'instrument', targetHandle: 'right' },
      { id: 'e-motor-pump', source: 'motor', target: 'pump', type: 'instrument', sourceHandle: 'bottom', targetHandle: 'motor' }
    ]
  }

// 初始化数据
const initData = () => {
  if (props.nodeDataList && props.nodeDataList.length > 0) {
    // 使用传入的数据
    nodes.value = props.nodeDataList.map(item => {
      const display = item.display || {}
      const nodeId = item.nodeId || item.id || item.key
      const nodeType = item.type || props.nodeTypeMap[nodeId] || 'default'

      return {
        id: nodeId,
        type: nodeType,
        position: { x: display.posX || 0, y: display.posY || 0 },
        data: {
          label: item.name || item.label || item.text || nodeId,
          value: item.value || '',
          status: item.status || 'normal',
          nodeId: nodeId,
          width: display.sizeX || 60,
          height: display.sizeY || 40
        }
      }
    })

    if (props.linkDataList && props.linkDataList.length > 0) {
      edges.value = props.linkDataList.map((item, index) => {
        const fromId = item.fromState || item.from || item.source
        const toId = item.toState || item.to || item.target
        const isInstrument = item.isInstrument || item.isInstrumentLink

        return {
          id: `e-${fromId}-${toId}-${index}`,
          source: fromId,
          target: toId,
          type: isInstrument ? 'instrument' : 'pipeline',
          data: {
            isTankBottom: item.isTankBottom || false
          }
        }
      }).filter(edge => edge.source && edge.target)
    } else {
      edges.value = getDefaultEdges()
    }
  } else {
    // 使用默认数据
    nodes.value = getDefaultNodes()
    edges.value = getDefaultEdges()
  }
}

// 监听props变化
watch([() => props.nodeDataList, () => props.linkDataList], () => {
  initData()
}, { deep: true, immediate: true })

// 双击节点处理
const handleNodeDoubleClick = ({ node }) => {
  editingNode.id = node.id
  editingNode.label = node.data.label
  editingNode.type = node.type || 'default'
  editingNode.value = node.data.value || ''
  editingNode.status = node.data.status || 'normal'
  editingNode.nodeId = node.data.nodeId || node.id
  editingNode.hasValue = true
  editingNode.hasStatus = true

  editDialogVisible.value = true
}

// 保存编辑
const saveEdit = () => {
  const nodeIndex = nodes.value.findIndex(n => n.id === editingNode.id)
  if (nodeIndex !== -1) {
    nodes.value[nodeIndex] = {
      ...nodes.value[nodeIndex],
      data: {
        ...nodes.value[nodeIndex].data,
        label: editingNode.label,
        value: editingNode.value,
        status: editingNode.status,
        nodeId: editingNode.nodeId
      }
    }
    ElMessage.success('保存成功')
  }
  editDialogVisible.value = false
}

// 关闭编辑对话框
const closeEditDialog = () => {
  Object.keys(editingNode).forEach(key => {
    if (typeof editingNode[key] === 'string') {
      editingNode[key] = ''
    } else if (typeof editingNode[key] === 'boolean') {
      editingNode[key] = false
    }
  })
}

// 重置视图
const resetView = () => {
  setViewport({ zoom: 1, x: 0, y: 0 })
}

// 放大
const zoomIn = () => {
  flowZoomIn()
}

// 缩小
const zoomOut = () => {
  flowZoomOut()
}

// 刷新数据
const refreshData = async () => {
  isLoading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 500))

    // 模拟数据更新
    nodes.value = nodes.value.map(node => {
      if (Math.random() > 0.7 && node.data.value && node.data.value.includes('%')) {
        const num = parseFloat(node.data.value) || 0
        return {
          ...node,
          data: {
            ...node.data,
            value: `${Math.max(0, Math.min(100, num + (Math.random() - 0.5) * 10)).toFixed(1)}%`
          }
        }
      }
      return node
    })

    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败: ' + error.message)
  } finally {
    isLoading.value = false
  }
}

// 更新单个节点的值
const updateNodeValue = (nodeId, value, status) => {
  const nodeIndex = nodes.value.findIndex(n => n.id === nodeId)
  if (nodeIndex !== -1) {
    nodes.value[nodeIndex] = {
      ...nodes.value[nodeIndex],
      data: {
        ...nodes.value[nodeIndex].data,
        ...(value !== undefined && { value }),
        ...(status !== undefined && { status })
      }
    }
    return true
  }
  return false
}

// 批量更新节点值
const updateNodeValues = (updates) => {
  let updated = 0
  updates.forEach(update => {
    if (updateNodeValue(update.nodeId, update.value, update.status)) {
      updated++
    }
  })
  return updated
}

// 获取当前节点数据
const getNodeData = () => {
  return nodes.value.map(node => ({
    id: node.id,
    nodeId: node.data.nodeId,
    label: node.data.label,
    type: node.type,
    display: {
      posX: node.position.x,
      posY: node.position.y,
      sizeX: node.data.width || 60,
      sizeY: node.data.height || 40
    },
    value: node.data.value || '',
    status: node.data.status || 'normal'
  }))
}

// 获取当前连线数据
const getLinkData = () => {
  return edges.value.map(edge => ({
    from: edge.source,
    to: edge.target,
    fromState: edge.source,
    toState: edge.target,
    isInstrument: edge.type === 'instrument'
  }))
}

// 暴露方法供父组件调用
defineExpose({
  refreshData,
  resetView,
  updateNodeValue,
  updateNodeValues,
  getNodeData,
  getLinkData
})
</script>

<style scoped>
.process-diagram-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f5f5f5;
}

.toolbar {
  display: flex;
  gap: 8px;
  padding: 12px;
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.flow-wrapper {
  flex: 1;
  position: relative;
  overflow: hidden;
}
</style>

<style>
/* VueFlow全局样式 */
@import '@vue-flow/core/dist/style.css';
@import '@vue-flow/core/dist/theme-default.css';
@import '@vue-flow/controls/dist/style.css';
@import '@vue-flow/minimap/dist/style.css';

.vue-flow {
  background: #fff;
  background-image:
    linear-gradient(rgba(0,0,0,.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0,0,0,.05) 1px, transparent 1px);
  background-size: 20px 20px;
}
</style>
