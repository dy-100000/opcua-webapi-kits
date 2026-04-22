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
      
    <!-- Canvas容器 -->
    <div class="canvas-wrapper" ref="canvasWrapper">
      <canvas
        ref="canvasRef"
        @dblclick="handleDoubleClick"
        @mousemove="handleMouseMove"
        @mousedown="handleMouseDown"
        @mouseup="handleMouseUp"
        @wheel="handleWheel"
      ></canvas>
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
import { ref, reactive, onMounted, onUnmounted, nextTick, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, ZoomIn, ZoomOut, RefreshRight } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  /**
   * 节点数据数组
   * 格式: [{ nodeId, name, type, display: { posX, posY, sizeX, sizeY }, value, status }, ...]
   */
  nodeDataList: {
    type: Array,
    default: () => []
  },
  /**
   * 连线数据数组  
   * 格式: [{ fromState/toState/from/to, isInstrument? }, ...]
   */
  linkDataList: {
    type: Array,
    default: () => []
  },
  /**
   * 节点类型映射: { 'node-id': 'tank', ... }
   */
  nodeTypeMap: {
    type: Object,
    default: () => ({})
  }
})

// Canvas相关
const canvasRef = ref(null)
const canvasWrapper = ref(null)
const ctx = ref(null)
const scale = ref(1)
const offsetX = ref(0)
const offsetY = ref(0)
const isDragging = ref(false)
const dragStart = ref({ x: 0, y: 0 })
const isLoading = ref(false)

// 编辑对话框
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

// 节点映射表（用于快速查找）
const nodeMap = reactive({})

// 动态数据（存储节点的值、状态等）
const nodeValues = reactive({})

// 节点编辑缓存（保存编辑后的节点信息，避免computed重新计算时丢失）
const nodeEditCache = reactive({})

// 将外部数据转换为内部节点格式
const nodes = computed(() => {
  // 清空nodeMap，重新构建
  Object.keys(nodeMap).forEach(key => delete nodeMap[key])
  
  if (!props.nodeDataList || props.nodeDataList.length === 0) {
    // 如果没有传入数据，使用默认数据
    return getDefaultNodes()
  }
  
  return props.nodeDataList.map(item => {
    const display = item.display || {}
    const nodeId = item.nodeId || item.id || item.key
    
    // 检查是否有编辑缓存
    const cached = nodeEditCache[nodeId]
    
    const node = {
      id: nodeId,
      nodeId: cached?.nodeId || nodeId,
      type: cached?.type || item.type || props.nodeTypeMap[nodeId] || 'default',
      label: cached?.label || item.name || item.label || item.text || nodeId,
      x: display.posX || 0,
      y: display.posY || 0,
      width: display.sizeX || 60,
      height: display.sizeY || 40,
      originalData: item // 保存原始数据
    }
    
    // 更新节点映射表
    nodeMap[nodeId] = node
    
    // 初始化节点值
    if (!nodeValues[nodeId]) {
      nodeValues[nodeId] = {
        value: item.value || '',
        status: item.status || 'normal'
      }
    }
    
    return node
  })
})

// 默认节点数据（根据P&ID图精确布局）
const getDefaultNodes = () => {
  // 根据图片描述，精确设置节点位置
  // TANK 为中心，其他组件围绕它布局
  const tankX = 400
  const tankY = 150
  const tankWidth = 120
  const tankHeight = 200
  
  const defaultNodes = [
    // 入口系统（右侧上方）
    { 
      id: 'source0', 
      type: 'source', 
      x: 100, 
      y: 190, 
      label: 'Source0', 
      width: 80, 
      height: 40,
      nodeId: 'source0'
    },
    { 
      id: 'valve1', 
      type: 'valve', 
      x: 220, 
      y: 190, 
      label: 'H001', 
      width: 40, 
      height: 40,
      nodeId: 'valve1'
    },
    
    // 储罐（中心）
    { 
      id: 'tank', 
      type: 'tank', 
      x: tankX, 
      y: tankY, 
      label: 'TANK', 
      width: tankWidth, 
      height: tankHeight,
      nodeId: 'tank'
    },
    
    // 出口系统（右侧下方）
    { 
      id: 'valve2', 
      type: 'valve', 
      x: tankX + tankWidth + 30, 
      y: tankY + tankHeight - 20, 
      label: 'H002', 
      width: 40, 
      height: 40,
      nodeId: 'valve2'
    },
    { 
      id: 'pump', 
      type: 'pump', 
      x: tankX + tankWidth + 90, 
      y: tankY + tankHeight - 30, 
      label: 'P001', 
      width: 60, 
      height: 60,
      nodeId: 'pump'
    },
    { 
      id: 'motor', 
      type: 'motor', 
      x: tankX + tankWidth + 90, 
      y: tankY + tankHeight - 120, 
      label: 'M001', 
      width: 50, 
      height: 50,
      nodeId: 'motor'
    },
    // FI 006 流量计：位于 P001 和 Sink0 之间管道的正上方
    { 
      id: 'flowmeter', 
      type: 'instrument',
      // 计算位置：P001 和 Sink0 之间管道的中点上方
      // P001 中心点: x = tankX + tankWidth + 90 + 30 = tankX + tankWidth + 120
      // Sink0 入口点: x = tankX + tankWidth + 260, y = tankY + tankHeight - 20 + 20 = tankY + tankHeight
      // 管道中点X: (P001右侧 + Sink0左侧) / 2
      // 管道Y坐标: tankY + tankHeight (水平管道)
      x: ((tankX + tankWidth + 90 + 60) + (tankX + tankWidth + 260)) / 2 - 25,  // 中点X减去仪表宽度的一半
      y: tankY + tankHeight - 85,  // 管道上方约35px
      label: 'FI 006', 
      width: 50, 
      height: 30,
      nodeId: 'flowmeter'
    },
    { 
      id: 'sink0', 
      type: 'sink', 
      x: tankX + tankWidth + 260, 
      y: tankY + tankHeight - 20, 
      label: 'Sink0', 
      width: 80, 
      height: 40,
      nodeId: 'sink0'
    },
    
    // 左侧仪表（连接到储罐左侧）
    { 
      id: 'lc003', 
      type: 'instrument', 
      x: tankX - 80, 
      y: tankY + 30, 
      label: 'LC 003', 
      width: 50, 
      height: 30,
      nodeId: 'lc003'
    },
    { 
      id: 'l002', 
      type: 'instrument', 
      x: tankX - 80, 
      y: tankY + 90, 
      label: 'L 002', 
      width: 50, 
      height: 30,
      nodeId: 'l002'
    },
    { 
      id: 'lc001', 
      type: 'instrument', 
      x: tankX - 80, 
      y: tankY + 150, 
      label: 'LC 001', 
      width: 50, 
      height: 30,
      nodeId: 'lc001'
    },
    
    // 顶部仪表（连接到储罐顶部）
    { 
      id: 'li004', 
      type: 'instrument', 
      x: tankX + 35, 
      y: tankY - 100, 
      label: 'LI 004', 
      width: 50, 
      height: 30,
      nodeId: 'li004'
    },
    
    // 右侧仪表（连接到储罐右侧）
    { 
      id: 'ti005', 
      type: 'instrument', 
      x: tankX + tankWidth + 30, 
      y: tankY + 90, 
      label: 'TI 005', 
      width: 50, 
      height: 30,
      nodeId: 'ti005'
    }
  ]
  
  // 应用编辑缓存（如果有编辑过的节点，使用缓存的数据）
  const finalNodes = defaultNodes.map(node => {
    const cached = nodeEditCache[node.id]
    if (cached) {
      return {
        ...node,
        label: cached.label !== undefined ? cached.label : node.label,
        nodeId: cached.nodeId !== undefined ? cached.nodeId : node.nodeId,
        type: cached.type !== undefined ? cached.type : node.type
      }
    }
    return node
  })
  
  // 更新nodeMap
  finalNodes.forEach(node => {
    nodeMap[node.id] = node
    if (!nodeValues[node.id]) {
      nodeValues[node.id] = { 
        value: node.type === 'tank' ? '75%' : '', 
        status: 'normal' 
      }
    }
  })
  
  return finalNodes
}

// 默认连线数据（根据P&ID图）
const getDefaultLinks = () => {
  return [
    // 主要管道连接
    { from: 'source0', to: 'valve1' },
    { from: 'valve1', to: 'tank' },
    { from: 'tank', to: 'valve2' },
    { from: 'valve2', to: 'pump' },
    { from: 'pump', to: 'sink0' },  // 泵直接连接到Sink0，流量计在管道上方
    
    // 仪表连接（虚线连接到储罐）
    { from: 'li004', to: 'tank', isInstrument: true },  // 顶部
    { from: 'lc003', to: 'tank', isInstrument: true },  // 左侧上部
    { from: 'l002', to: 'tank', isInstrument: true },   // 左侧中部
    { from: 'lc001', to: 'tank', isInstrument: true },  // 左侧下部
    { from: 'ti005', to: 'tank', isInstrument: true },  // 右侧
    // 流量计连接到 P001 和 Sink0 之间的管道中点（使用特殊标记）
    { from: 'flowmeter', to: 'pump', toMidpoint: 'sink0', isInstrument: true },
    // 电机连接到泵的管道
    { from: 'motor', to: 'pump', isInstrument: true }
  ]
}

// 连线数据
const links = computed(() => {
  if (!props.linkDataList || props.linkDataList.length === 0) {
    // 如果没有传入连线数据，使用默认连线
    return getDefaultLinks()
  }
  
  return props.linkDataList.map(item => {
    const fromId = item.fromState || item.from || item.source
    const toId = item.toState || item.to || item.target
    
    // 判断是否为仪表连线（根据节点类型或自定义属性）
    const fromNode = nodeMap[fromId]
    const toNode = nodeMap[toId]
    const isInstrument = item.isInstrument || 
                        item.isInstrumentLink ||
                        (fromNode && fromNode.type === 'instrument') ||
                        (toNode && toNode.type === 'instrument')
    
    return {
      from: fromId,
      to: toId,
      isInstrument: isInstrument,
      originalData: item
    }
  }).filter(link => link.from && link.to)
})

// 监听数据变化，重新绘制
watch([nodes, links], () => {
  nextTick(() => {
    draw()
  })
}, { deep: true })

// 初始化Canvas
const initCanvas = () => {
  const canvas = canvasRef.value
  if (!canvas) return
  
  const wrapper = canvasWrapper.value
  canvas.width = wrapper.clientWidth
  canvas.height = wrapper.clientHeight
  
  ctx.value = canvas.getContext('2d')
  draw()
}

// 绘制函数
const draw = () => {
  if (!ctx.value) return
  
  const canvas = canvasRef.value
  ctx.value.clearRect(0, 0, canvas.width, canvas.height)
  
  // 应用变换
  ctx.value.save()
  ctx.value.translate(offsetX.value, offsetY.value)
  ctx.value.scale(scale.value, scale.value)
  
  // 绘制连线
  drawLinks()
  
  // 绘制节点
  nodes.value.forEach(node => {
    drawNode(node)
  })
  
  ctx.value.restore()
}

// 计算连接点（智能选择节点边缘或中心）
const getConnectionPoint = (fromNode, toNode, isInstrument = false) => {
  const fromCenterX = fromNode.x + fromNode.width / 2
  const fromCenterY = fromNode.y + fromNode.height / 2
  const toCenterX = toNode.x + toNode.width / 2
  const toCenterY = toNode.y + toNode.height / 2
  
  // 仪表连线直接使用中心点
  if (isInstrument) {
    return {
      fromX: fromCenterX,
      fromY: fromCenterY,
      toX: toCenterX,
      toY: toCenterY
    }
  }
  
  // 主要管道连接使用边缘点
  let fromX = fromCenterX
  let fromY = fromCenterY
  let toX = toCenterX
  let toY = toCenterY
  
  // 计算方向
  const dx = toCenterX - fromCenterX
  const dy = toCenterY - fromCenterY
  
  // Source/Sink 使用箭头方向
  if (fromNode.type === 'source') {
    fromX = fromNode.x + fromNode.width + 15 // 箭头尖端
    fromY = fromCenterY
  } else if (fromNode.type === 'sink') {
    fromX = fromNode.x - 15
    fromY = fromCenterY
  } else if (fromNode.type === 'tank') {
    // 储罐出口在底部中心
    if (toCenterY > fromCenterY) {
      fromX = fromCenterX
      fromY = fromNode.y + fromNode.height
    } else {
      // 入口在顶部右侧
      fromX = fromNode.x + fromNode.width
      fromY = fromCenterY
    }
  } else {
    // 其他节点根据方向选择边缘
    if (Math.abs(dx) > Math.abs(dy)) {
      // 水平连接
      fromX = dx > 0 ? fromNode.x + fromNode.width : fromNode.x
      fromY = fromCenterY
    } else {
      // 垂直连接
      fromX = fromCenterX
      fromY = dy > 0 ? fromNode.y + fromNode.height : fromNode.y
    }
  }
  
  // 目标节点连接点
  if (toNode.type === 'sink') {
    toX = toNode.x - 15
    toY = toCenterY
  } else if (toNode.type === 'tank') {
    // 储罐入口在顶部右侧
    if (fromCenterY < toCenterY) {
      toX = toNode.x + toNode.width
      toY = toNode.y + 20 // 顶部附近
    } else {
      toX = toCenterX
      toY = toNode.y
    }
  } else {
    // 其他节点根据方向选择边缘
    if (Math.abs(dx) > Math.abs(dy)) {
      toX = dx > 0 ? toNode.x : toNode.x + toNode.width
      toY = toCenterY
    } else {
      toX = toCenterX
      toY = dy > 0 ? toNode.y : toNode.y + toNode.height
    }
  }
  
  return { fromX, fromY, toX, toY }
}

// 绘制连线（根据动态数据）
const drawLinks = () => {
  if (!links.value || links.value.length === 0) {
    return
  }

  // 先绘制主要管道（实线）
  links.value.filter(link => !link.isInstrument).forEach(link => {
    const fromNode = nodeMap[link.from]
    const toNode = nodeMap[link.to]
    
    if (fromNode && toNode) {
      const points = getConnectionPoint(fromNode, toNode, false)
      
      ctx.value.setLineDash([])
      ctx.value.strokeStyle = '#333'
      ctx.value.lineWidth = 3
      ctx.value.lineCap = 'round'
      ctx.value.lineJoin = 'round'
      
      drawLine(points.fromX, points.fromY, points.toX, points.toY)
    }
  })
  
  // 再绘制仪表连线（虚线）
  links.value.filter(link => link.isInstrument).forEach(link => {
    const fromNode = nodeMap[link.from]
    const toNode = nodeMap[link.to]
    
    if (fromNode && toNode) {
      let toX, toY
      
      // 如果指定了 toMidpoint，连接到两个节点之间的中点
      if (link.toMidpoint) {
        const midNode = nodeMap[link.toMidpoint]
        if (midNode) {
          // 计算 toNode 和 midNode 之间管道的中点
          const fromPoint = getConnectionPoint(toNode, midNode, false)
          toX = (fromPoint.fromX + fromPoint.toX) / 2
          toY = (fromPoint.fromY + fromPoint.toY) / 2
        } else {
          // 如果没有 midNode，使用默认连接点
          const points = getConnectionPoint(fromNode, toNode, true)
          toX = points.toX
          toY = points.toY
        }
      } else {
        // 普通仪表连线
        const points = getConnectionPoint(fromNode, toNode, true)
        toX = points.toX
        toY = points.toY
      }
      
      // 从仪表中心连接到目标点
      const fromX = fromNode.x + fromNode.width / 2
      const fromY = fromNode.y + fromNode.height / 2
      
      ctx.value.setLineDash([5, 5])
      ctx.value.strokeStyle = '#666'
      ctx.value.lineWidth = 1
      ctx.value.lineCap = 'round'
      
      drawLine(fromX, fromY, toX, toY)
    }
  })
  
  ctx.value.setLineDash([])
}

// 绘制直线
const drawLine = (x1, y1, x2, y2) => {
  ctx.value.beginPath()
  ctx.value.moveTo(x1, y1)
  ctx.value.lineTo(x2, y2)
  ctx.value.stroke()
}

// 绘制节点
const drawNode = (node) => {
  const data = nodeValues[node.id] || { value: '', status: 'normal' }
  
  // 如果没有定义类型，使用默认绘制方法
  if (!node.type || node.type === 'default') {
    drawDefaultNode(node, data)
    drawLabel(node, data)
    return
  }

  switch (node.type) {
    case 'source':
    case 'sink':
      drawSourceSink(node, data)
      break
    case 'valve':
      drawValve(node, data)
      break
    case 'tank':
      drawTank(node, data)
      break
    case 'pump':
      drawPump(node, data)
      break
    case 'motor':
      drawMotor(node, data)
      break
    case 'instrument':
      drawInstrument(node, data)
      break
    default:
      drawDefaultNode(node, data)
      break
  }
  
  // 绘制标签
  drawLabel(node, data)
}

// 绘制默认节点（圆角矩形，带阴影效果）
const drawDefaultNode = (node, data) => {
  const { x, y, width, height } = node
  const statusColor = getStatusColor(data.status)
  const radius = 4
  
  // 绘制阴影
  ctx.value.save()
  ctx.value.shadowColor = 'rgba(0, 0, 0, 0.1)'
  ctx.value.shadowBlur = 4
  ctx.value.shadowOffsetX = 2
  ctx.value.shadowOffsetY = 2
  
  // 绘制圆角矩形
  ctx.value.fillStyle = statusColor
  ctx.value.strokeStyle = '#333'
  ctx.value.lineWidth = 2
  
  roundRect(ctx.value, x, y, width, height, radius)
  ctx.value.fill()
  ctx.value.stroke()
  
  ctx.value.restore()
}

// 绘制圆角矩形的辅助函数
const roundRect = (ctx, x, y, width, height, radius) => {
  ctx.beginPath()
  ctx.moveTo(x + radius, y)
  ctx.lineTo(x + width - radius, y)
  ctx.quadraticCurveTo(x + width, y, x + width, y + radius)
  ctx.lineTo(x + width, y + height - radius)
  ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height)
  ctx.lineTo(x + radius, y + height)
  ctx.quadraticCurveTo(x, y + height, x, y + height - radius)
  ctx.lineTo(x, y + radius)
  ctx.quadraticCurveTo(x, y, x + radius, y)
  ctx.closePath()
}

// 绘制源/汇（符合P&ID标准：矩形+三角形箭头）
const drawSourceSink = (node, data) => {
  const { x, y, width, height } = node
  const statusColor = getStatusColor(data.status)
  const radius = 3
  
  // 创建渐变（模拟容器表面）
  const gradient = ctx.value.createLinearGradient(x, y, x, y + height)
  gradient.addColorStop(0, lightenColor(statusColor, 25))
  gradient.addColorStop(0.5, lightenColor(statusColor, 10))
  gradient.addColorStop(1, statusColor)
  
  // 绘制阴影
  ctx.value.save()
  ctx.value.shadowColor = 'rgba(0, 0, 0, 0.2)'
  ctx.value.shadowBlur = 6
  ctx.value.shadowOffsetX = 2
  ctx.value.shadowOffsetY = 2
  
  // 绘制圆角矩形主体（符合P&ID标准）
  ctx.value.fillStyle = gradient
  ctx.value.strokeStyle = '#333'
  ctx.value.lineWidth = 2.5
  roundRect(ctx.value, x, y, width, height, radius)
  ctx.value.fill()
  ctx.value.stroke()
  
  ctx.value.restore()
  
  // 内部细节线（增强立体感）
  ctx.value.strokeStyle = 'rgba(0, 0, 0, 0.1)'
  ctx.value.lineWidth = 1
  ctx.value.beginPath()
  ctx.value.moveTo(x + 3, y + height / 2)
  ctx.value.lineTo(x + width - 3, y + height / 2)
  ctx.value.stroke()
  
  // 绘制三角形箭头（符合P&ID标准，更标准的角度）
  const arrowSize = 20
  const arrowY = y + height / 2
  
  if (node.type === 'source') {
    // 源：箭头向右（指向系统）
    const arrowX = x + width
    const arrowGradient = ctx.value.createLinearGradient(
      arrowX, arrowY - arrowSize / 1.5,
      arrowX + arrowSize, arrowY
    )
    arrowGradient.addColorStop(0, lightenColor(statusColor, 20))
    arrowGradient.addColorStop(1, statusColor)
    
    ctx.value.fillStyle = arrowGradient
    ctx.value.strokeStyle = '#333'
    ctx.value.lineWidth = 2
    
    ctx.value.beginPath()
    ctx.value.moveTo(arrowX, arrowY)
    ctx.value.lineTo(arrowX + arrowSize, arrowY - arrowSize / 1.4)
    ctx.value.lineTo(arrowX + arrowSize, arrowY + arrowSize / 1.4)
    ctx.value.closePath()
    ctx.value.fill()
    ctx.value.stroke()
    
    // 箭头内部高光
    ctx.value.fillStyle = 'rgba(255, 255, 255, 0.3)'
    ctx.value.beginPath()
    ctx.value.moveTo(arrowX + arrowSize * 0.2, arrowY - arrowSize / 3)
    ctx.value.lineTo(arrowX + arrowSize * 0.8, arrowY)
    ctx.value.lineTo(arrowX + arrowSize * 0.2, arrowY + arrowSize / 3)
    ctx.value.closePath()
    ctx.value.fill()
  } else {
    // 汇：箭头向左（离开系统）
    const arrowX = x
    const arrowGradient = ctx.value.createLinearGradient(
      arrowX - arrowSize, arrowY - arrowSize / 1.5,
      arrowX, arrowY
    )
    arrowGradient.addColorStop(0, statusColor)
    arrowGradient.addColorStop(1, lightenColor(statusColor, 20))
    
    ctx.value.fillStyle = arrowGradient
    ctx.value.strokeStyle = '#333'
    ctx.value.lineWidth = 2
    
    ctx.value.beginPath()
    ctx.value.moveTo(arrowX, arrowY)
    ctx.value.lineTo(arrowX - arrowSize, arrowY - arrowSize / 1.4)
    ctx.value.lineTo(arrowX - arrowSize, arrowY + arrowSize / 1.4)
    ctx.value.closePath()
    ctx.value.fill()
    ctx.value.stroke()
    
    // 箭头内部高光
    ctx.value.fillStyle = 'rgba(255, 255, 255, 0.3)'
    ctx.value.beginPath()
    ctx.value.moveTo(arrowX - arrowSize * 0.2, arrowY - arrowSize / 3)
    ctx.value.lineTo(arrowX - arrowSize * 0.8, arrowY)
    ctx.value.lineTo(arrowX - arrowSize * 0.2, arrowY + arrowSize / 3)
    ctx.value.closePath()
    ctx.value.fill()
  }
}

// 绘制阀门（符合P&ID标准：圆形外框，X符号）
const drawValve = (node, data) => {
  const { x, y, width, height } = node
  const statusColor = getStatusColor(data.status)
  const centerX = x + width / 2
  const centerY = y + height / 2
  const radius = Math.min(width, height) / 2 + 6
  
  // 绘制圆形外框（符合P&ID标准）
  ctx.value.save()
  ctx.value.shadowColor = 'rgba(0, 0, 0, 0.15)'
  ctx.value.shadowBlur = 5
  ctx.value.shadowOffsetX = 2
  ctx.value.shadowOffsetY = 2
  
  // 外圆背景（浅灰色，模拟金属质感）
  const circleGradient = ctx.value.createRadialGradient(
    centerX - radius * 0.3, centerY - radius * 0.3, 0,
    centerX, centerY, radius
  )
  circleGradient.addColorStop(0, '#FAFAFA')
  circleGradient.addColorStop(1, '#E8E8E8')
  
  ctx.value.fillStyle = circleGradient
  ctx.value.beginPath()
  ctx.value.arc(centerX, centerY, radius, 0, Math.PI * 2)
  ctx.value.fill()
  
  // 外圆边框（双层边框效果）
  ctx.value.strokeStyle = '#666'
  ctx.value.lineWidth = 3
  ctx.value.stroke()
  
  ctx.value.strokeStyle = '#333'
  ctx.value.lineWidth = 2
  ctx.value.stroke()
  
  ctx.value.restore()
  
  // 绘制X符号（符合P&ID标准，更粗更清晰）
  ctx.value.strokeStyle = statusColor
  ctx.value.lineWidth = 6
  ctx.value.lineCap = 'round'
  ctx.value.lineJoin = 'round'
  
  // X符号的角度和长度
  const xOffset = width * 0.4
  const yOffset = height * 0.4
  
  ctx.value.beginPath()
  // 第一根线：左上到右下（从左上角到右下角）
  ctx.value.moveTo(centerX - xOffset, centerY - yOffset)
  ctx.value.lineTo(centerX + xOffset, centerY + yOffset)
  // 第二根线：右上到左下（从右上角到左下角）
  ctx.value.moveTo(centerX + xOffset, centerY - yOffset)
  ctx.value.lineTo(centerX - xOffset, centerY + yOffset)
  ctx.value.stroke()
  
  // 添加X符号的内阴影效果（增强立体感）
  ctx.value.strokeStyle = 'rgba(0, 0, 0, 0.2)'
  ctx.value.lineWidth = 2
  ctx.value.globalCompositeOperation = 'source-over'
  ctx.value.beginPath()
  ctx.value.moveTo(centerX - xOffset * 0.8, centerY - yOffset * 0.8)
  ctx.value.lineTo(centerX + xOffset * 0.8, centerY + yOffset * 0.8)
  ctx.value.moveTo(centerX + xOffset * 0.8, centerY - yOffset * 0.8)
  ctx.value.lineTo(centerX - xOffset * 0.8, centerY + yOffset * 0.8)
  ctx.value.stroke()
  ctx.value.globalCompositeOperation = 'source-over'
}

// 绘制储罐（垂直圆柱形，带圆顶和圆底，带阴影和渐变）
const drawTank = (node, data) => {
  const { x, y, width, height } = node
  const statusColor = getStatusColor(data.status)
  const centerX = x + width / 2
  const ellipseRadius = width / 2
  const ellipseHeight = 20
  
  // 绘制阴影
  ctx.value.save()
  ctx.value.shadowColor = 'rgba(0, 0, 0, 0.2)'
  ctx.value.shadowBlur = 8
  ctx.value.shadowOffsetX = 3
  ctx.value.shadowOffsetY = 3
  
  // 创建渐变（从上到下）
  const gradient = ctx.value.createLinearGradient(x, y, x, y + height)
  gradient.addColorStop(0, '#F0F8FF')  // 浅蓝白色（顶部）
  gradient.addColorStop(0.5, '#E8F4F8') // 浅蓝灰色（中部）
  gradient.addColorStop(1, '#E0EFF4')   // 稍深的蓝灰色（底部）
  
  // 罐体填充色（使用渐变）
  ctx.value.fillStyle = gradient
  ctx.value.strokeStyle = '#333'
  ctx.value.lineWidth = 3
  
  // 上端圆顶（椭圆）
  ctx.value.beginPath()
  ctx.value.ellipse(centerX, y, ellipseRadius, ellipseHeight, 0, 0, Math.PI * 2)
  ctx.value.fill()
  ctx.value.stroke()
  
  // 矩形主体（圆柱侧面）
  ctx.value.fillRect(x, y, width, height)
  ctx.value.strokeRect(x, y, width, height)
  
  // 下端圆底（椭圆）
  ctx.value.beginPath()
  ctx.value.ellipse(centerX, y + height, ellipseRadius, ellipseHeight, 0, 0, Math.PI * 2)
  ctx.value.fill()
  ctx.value.stroke()
  
  ctx.value.restore()
  
  // 液位指示（根据值绘制，填充在罐体内）
  if (data.value) {
    const level = parseFloat(data.value) || 0
    const levelPercent = Math.max(0, Math.min(100, level))
    const levelHeight = (height * levelPercent) / 100
    
    if (levelHeight > 0) {
      // 创建液位渐变
      const liquidGradient = ctx.value.createLinearGradient(
        x, 
        y + height - levelHeight, 
        x, 
        y + height
      )
      liquidGradient.addColorStop(0, lightenColor(statusColor, 20))
      liquidGradient.addColorStop(1, statusColor)
      
      ctx.value.fillStyle = liquidGradient
      ctx.value.globalAlpha = 0.7
      
      // 绘制液位（考虑圆底）
      const liquidY = y + height - levelHeight
      
      // 如果液位超过圆底部分
      if (levelHeight > ellipseHeight) {
        // 矩形部分
        ctx.value.fillRect(x + 5, liquidY, width - 10, levelHeight - ellipseHeight)
        
        // 圆底部分
        ctx.value.beginPath()
        ctx.value.ellipse(
          centerX, 
          y + height - ellipseHeight, 
          ellipseRadius - 5, 
          ellipseHeight, 
          0, 
          Math.PI, 
          Math.PI * 2,
          false
        )
        ctx.value.fill()
        
        // 液位表面高光效果
        ctx.value.fillStyle = 'rgba(255, 255, 255, 0.3)'
        ctx.value.fillRect(x + 5, liquidY, width - 10, 3)
      } else {
        // 只绘制圆底部分
        const angle = Math.acos((ellipseHeight - levelHeight) / ellipseHeight)
        ctx.value.beginPath()
        ctx.value.ellipse(
          centerX, 
          y + height - ellipseHeight, 
          ellipseRadius - 5, 
          ellipseHeight, 
          0, 
          Math.PI + angle,
          Math.PI * 2 - angle,
          false
        )
        ctx.value.fill()
      }
      
      ctx.value.globalAlpha = 1.0
      
      // 液位刻度线（可选）
      if (levelPercent > 10) {
        ctx.value.strokeStyle = statusColor
        ctx.value.lineWidth = 1
        ctx.value.beginPath()
        ctx.value.moveTo(x + width + 5, liquidY)
        ctx.value.lineTo(x + width + 10, liquidY)
        ctx.value.stroke()
      }
    }
  }
  
  // 重新绘制边框确保清晰（不带阴影）
  ctx.value.strokeStyle = '#333'
  ctx.value.lineWidth = 3
  ctx.value.strokeRect(x, y, width, height)
  
  // 添加高光效果（顶部，模拟光照）
  ctx.value.fillStyle = 'rgba(255, 255, 255, 0.4)'
  ctx.value.beginPath()
  ctx.value.ellipse(
    centerX - ellipseRadius * 0.3, 
    y - ellipseHeight * 0.2, 
    ellipseRadius * 0.4, 
    ellipseHeight * 0.3, 
    0, 
    0, 
    Math.PI * 2
  )
  ctx.value.fill()
  
  // 添加侧边高光（增强圆柱体立体感）
  ctx.value.fillStyle = 'rgba(255, 255, 255, 0.25)'
  ctx.value.fillRect(x + width - 8, y + 5, 3, height - 10)
  
  // 添加底部阴影线（增强圆底立体感）
  ctx.value.strokeStyle = 'rgba(0, 0, 0, 0.2)'
  ctx.value.lineWidth = 1
  ctx.value.beginPath()
  ctx.value.ellipse(
    centerX, 
    y + height + ellipseHeight * 0.3, 
    ellipseRadius * 0.9, 
    ellipseHeight * 0.5, 
    0, 
    0, 
    Math.PI * 2
  )
  ctx.value.stroke()
  
  // 添加罐体内部细节线（模拟焊缝或结构线）
  ctx.value.strokeStyle = 'rgba(0, 0, 0, 0.08)'
  ctx.value.lineWidth = 1
  ctx.value.beginPath()
  ctx.value.moveTo(x + width / 4, y + height * 0.3)
  ctx.value.lineTo(x + width / 4, y + height * 0.7)
  ctx.value.moveTo(x + width * 3 / 4, y + height * 0.3)
  ctx.value.lineTo(x + width * 3 / 4, y + height * 0.7)
  ctx.value.stroke()
}

// 绘制泵（符合P&ID标准：圆形，内部三角形箭头指向右侧）
const drawPump = (node, data) => {
  const { x, y, width, height } = node
  const statusColor = getStatusColor(data.status)
  const centerX = x + width / 2
  const centerY = y + height / 2
  const radius = Math.min(width, height) / 2 - 4
  
  // 创建径向渐变（模拟金属质感）
  const gradient = ctx.value.createRadialGradient(
    centerX - radius * 0.4, centerY - radius * 0.4, 0,
    centerX, centerY, radius
  )
  gradient.addColorStop(0, lightenColor(statusColor, 35))
  gradient.addColorStop(0.5, lightenColor(statusColor, 15))
  gradient.addColorStop(1, statusColor)
  
  // 绘制阴影
  ctx.value.save()
  ctx.value.shadowColor = 'rgba(0, 0, 0, 0.25)'
  ctx.value.shadowBlur = 7
  ctx.value.shadowOffsetX = 2
  ctx.value.shadowOffsetY = 2
  
  // 外圆（带渐变，符合P&ID标准）
  ctx.value.fillStyle = gradient
  ctx.value.strokeStyle = '#333'
  ctx.value.lineWidth = 3.5
  ctx.value.beginPath()
  ctx.value.arc(centerX, centerY, radius, 0, Math.PI * 2)
  ctx.value.fill()
  ctx.value.stroke()
  
  ctx.value.restore()
  
  // 内圆（中心圆，白色背景）
  const innerRadius = radius * 0.5
  ctx.value.fillStyle = '#FFFFFF'
  ctx.value.strokeStyle = statusColor
  ctx.value.lineWidth = 2.5
  ctx.value.beginPath()
  ctx.value.arc(centerX, centerY, innerRadius, 0, Math.PI * 2)
  ctx.value.fill()
  ctx.value.stroke()
  
  // 三角形箭头（指向右侧，符合P&ID标准）
  const triangleSize = radius * 0.45
  const triangleX = centerX + radius * 0.6
  const triangleY = centerY
  
  // 三角形填充（带渐变）
  const triangleGradient = ctx.value.createLinearGradient(
    centerX + radius * 0.3, centerY - triangleSize,
    triangleX, centerY
  )
  triangleGradient.addColorStop(0, lightenColor(statusColor, 20))
  triangleGradient.addColorStop(1, statusColor)
  
  ctx.value.fillStyle = triangleGradient
  ctx.value.strokeStyle = '#333'
  ctx.value.lineWidth = 2
  ctx.value.beginPath()
  // 三角形顶点（右侧）
  ctx.value.moveTo(triangleX, triangleY)
  // 三角形左下角
  ctx.value.lineTo(centerX + radius * 0.25, centerY - triangleSize)
  // 三角形右下角
  ctx.value.lineTo(centerX + radius * 0.25, centerY + triangleSize)
  ctx.value.closePath()
  ctx.value.fill()
  ctx.value.stroke()
  
  // 添加三角形高光
  ctx.value.fillStyle = 'rgba(255, 255, 255, 0.4)'
  ctx.value.beginPath()
  ctx.value.moveTo(triangleX - triangleSize * 0.3, triangleY - triangleSize * 0.3)
  ctx.value.lineTo(triangleX, triangleY)
  ctx.value.lineTo(triangleX - triangleSize * 0.3, triangleY + triangleSize * 0.3)
  ctx.value.closePath()
  ctx.value.fill()
}

// 绘制电机（符合P&ID标准：方形，带M字母，在泵上方）
const drawMotor = (node, data) => {
  const { x, y, width, height } = node
  const statusColor = getStatusColor(data.status)
  const radius = 5
  
  // 创建渐变（模拟金属电机外壳）
  const gradient = ctx.value.createLinearGradient(x, y, x + width, y + height)
  gradient.addColorStop(0, lightenColor(statusColor, 30))
  gradient.addColorStop(0.5, lightenColor(statusColor, 15))
  gradient.addColorStop(1, statusColor)
  
  // 绘制阴影
  ctx.value.save()
  ctx.value.shadowColor = 'rgba(0, 0, 0, 0.25)'
  ctx.value.shadowBlur = 6
  ctx.value.shadowOffsetX = 2
  ctx.value.shadowOffsetY = 2
  
  // 绘制圆角方形主体（符合P&ID标准）
  ctx.value.fillStyle = gradient
  ctx.value.strokeStyle = '#333'
  ctx.value.lineWidth = 3
  roundRect(ctx.value, x, y, width, height, radius)
  ctx.value.fill()
  ctx.value.stroke()
  
  ctx.value.restore()
  
  // 内部边框（增强立体感）
  ctx.value.strokeStyle = 'rgba(0, 0, 0, 0.1)'
  ctx.value.lineWidth = 1
  roundRect(ctx.value, x + 2, y + 2, width - 4, height - 4, radius - 1)
  ctx.value.stroke()
  
  // M字母（更大更醒目，符合P&ID标准）
  ctx.value.fillStyle = '#1a1a1a'
  ctx.value.font = 'bold 30px Arial, sans-serif'
  ctx.value.textAlign = 'center'
  ctx.value.textBaseline = 'middle'
  
  // 文字阴影
  ctx.value.shadowColor = 'rgba(255, 255, 255, 0.5)'
  ctx.value.shadowBlur = 2
  ctx.value.shadowOffsetX = -1
  ctx.value.shadowOffsetY = -1
  ctx.value.fillText('M', x + width / 2, y + height / 2)
  ctx.value.shadowBlur = 0
  ctx.value.shadowOffsetX = 0
  ctx.value.shadowOffsetY = 0
}

// 绘制仪表（符合P&ID标准：椭圆形，带内部细节）
const drawInstrument = (node, data) => {
  const { x, y, width, height } = node
  const statusColor = getStatusColor(data.status)
  const centerX = x + width / 2
  const centerY = y + height / 2
  const radiusX = width / 2 - 1
  const radiusY = height / 2 - 1
  
  // 创建径向渐变（模拟仪表表面）
  const gradient = ctx.value.createRadialGradient(
    centerX - radiusX * 0.4, centerY - radiusY * 0.4, 0,
    centerX, centerY, Math.max(radiusX, radiusY)
  )
  gradient.addColorStop(0, lightenColor(statusColor, 35))
  gradient.addColorStop(0.6, lightenColor(statusColor, 15))
  gradient.addColorStop(1, statusColor)
  
  // 绘制阴影
  ctx.value.save()
  ctx.value.shadowColor = 'rgba(0, 0, 0, 0.2)'
  ctx.value.shadowBlur = 5
  ctx.value.shadowOffsetX = 1
  ctx.value.shadowOffsetY = 1
  
  // 绘制椭圆形（符合P&ID标准）
  ctx.value.fillStyle = gradient
  ctx.value.strokeStyle = '#333'
  ctx.value.lineWidth = 2.5
  ctx.value.beginPath()
  ctx.value.ellipse(centerX, centerY, radiusX, radiusY, 0, 0, Math.PI * 2)
  ctx.value.fill()
  ctx.value.stroke()
  
  ctx.value.restore()
  
  // 内部细节：中心圆点（模拟仪表指针中心）
  ctx.value.fillStyle = '#333'
  ctx.value.beginPath()
  ctx.value.arc(centerX, centerY, 2, 0, Math.PI * 2)
  ctx.value.fill()
  
  // 内部高光（增强立体感）
  ctx.value.fillStyle = 'rgba(255, 255, 255, 0.4)'
  ctx.value.beginPath()
  ctx.value.ellipse(
    centerX - radiusX * 0.3, 
    centerY - radiusY * 0.3, 
    radiusX * 0.3, 
    radiusY * 0.3, 
    0, 
    0, 
    Math.PI * 2
  )
  ctx.value.fill()
  
  // 对于流量计（FI），添加小方块符号（根据P&ID标准）
  if (node.label && node.label.includes('FI')) {
    const squareSize = 4
    ctx.value.fillStyle = '#333'
    ctx.value.fillRect(
      centerX - squareSize / 2, 
      centerY + radiusY * 0.6, 
      squareSize, 
      squareSize
    )
  }
}

// 绘制标签（带背景和更好的样式）
const drawLabel = (node, data) => {
  const { x, y, width, height } = node
  const centerX = x + width / 2
  
  // 对于不同类型的节点，标签位置略有不同
  let labelY = y + height + 15
  
  // 阀门标签在下方
  if (node.type === 'valve') {
    labelY = y + height + 12
  }
  // 仪表标签在下方
  else if (node.type === 'instrument') {
    labelY = y + height + 12
  }
  // 储罐标签在底部
  else if (node.type === 'tank') {
    labelY = y + height + 20
  }
  
  // 测量文本宽度
  ctx.value.font = 'bold 12px Arial, sans-serif'
  ctx.value.textAlign = 'center'
  ctx.value.textBaseline = 'middle'
  const textMetrics = ctx.value.measureText(node.label)
  const textWidth = textMetrics.width
  const padding = 6
  const labelHeight = 18
  
  // 绘制标签背景（带圆角和半透明）
  ctx.value.fillStyle = 'rgba(255, 255, 255, 0.95)'
  ctx.value.strokeStyle = '#ddd'
  ctx.value.lineWidth = 1
  roundRect(
    ctx.value, 
    centerX - textWidth / 2 - padding, 
    labelY - labelHeight / 2, 
    textWidth + padding * 2, 
    labelHeight, 
    3
  )
  ctx.value.fill()
  ctx.value.stroke()
  
  // 标签文本（带阴影效果）
  ctx.value.shadowColor = 'rgba(0, 0, 0, 0.1)'
  ctx.value.shadowBlur = 2
  ctx.value.shadowOffsetX = 1
  ctx.value.shadowOffsetY = 1
  ctx.value.fillStyle = '#333'
  ctx.value.fillText(node.label, centerX, labelY)
  ctx.value.shadowBlur = 0
  ctx.value.shadowOffsetX = 0
  ctx.value.shadowOffsetY = 0
  
  // 数值文本（如果有）
  if (data.value) {
    ctx.value.font = '10px Arial, sans-serif'
    ctx.value.fillStyle = '#666'
    const valueMetrics = ctx.value.measureText(data.value)
    const valueWidth = valueMetrics.width
    
    // 数值标签背景
    ctx.value.fillStyle = 'rgba(240, 240, 240, 0.9)'
    ctx.value.strokeStyle = '#ddd'
    roundRect(
      ctx.value,
      centerX - valueWidth / 2 - padding,
      labelY + labelHeight / 2 + 5,
      valueWidth + padding * 2,
      16,
      3
    )
    ctx.value.fill()
    ctx.value.stroke()
    
    // 数值文本
    ctx.value.fillStyle = '#666'
    ctx.value.fillText(data.value, centerX, labelY + labelHeight / 2 + 13)
  }
  
  // 恢复字体设置
  ctx.value.font = '12px Arial'
  ctx.value.textBaseline = 'top'
}

// 获取状态颜色
const getStatusColor = (status) => {
  switch (status) {
    case 'normal':
      return '#4CAF50'
    case 'warning':
      return '#FF9800'
    case 'error':
      return '#F44336'
    default:
      return '#2196F3'
  }
}

// 颜色变亮函数
const lightenColor = (color, percent) => {
  // 支持 hex 颜色格式 #RRGGBB
  if (color.startsWith('#')) {
    const num = parseInt(color.replace('#', ''), 16)
    const r = Math.min(255, ((num >> 16) & 0xFF) + percent * 2.55)
    const g = Math.min(255, ((num >> 8) & 0xFF) + percent * 2.55)
    const b = Math.min(255, (num & 0xFF) + percent * 2.55)
    return `rgb(${Math.round(r)}, ${Math.round(g)}, ${Math.round(b)})`
  }
  return color
}

// 获取点击的节点
const getNodeAt = (x, y) => {
  // 转换坐标（考虑缩放和偏移）
  const canvasX = (x - offsetX.value) / scale.value
  const canvasY = (y - offsetY.value) / scale.value
  
  // 从后往前查找（最后绘制的在上层）
  const nodeList = nodes.value
  for (let i = nodeList.length - 1; i >= 0; i--) {
    const node = nodeList[i]
    if (
      canvasX >= node.x &&
      canvasX <= node.x + node.width &&
      canvasY >= node.y &&
      canvasY <= node.y + node.height
    ) {
      return node
    }
  }
  return null
}

// 双击处理
const handleDoubleClick = (e) => {
  const rect = canvasRef.value.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  
  const node = getNodeAt(x, y)
  if (node) {
    const data = nodeValues[node.id] || { value: '', status: 'normal' }
    
    editingNode.id = node.id
    editingNode.label = node.label
    editingNode.type = node.type || 'default'
    editingNode.value = data.value || ''
    editingNode.status = data.status || 'normal'
    editingNode.nodeId = node.nodeId || node.id
    editingNode.hasValue = true
    editingNode.hasStatus = true
    
    editDialogVisible.value = true
  }
}

// 保存编辑
const saveEdit = () => {
  const node = nodes.value.find(n => n.id === editingNode.id)
  if (node) {
    // 保存到编辑缓存（这样即使computed重新计算，修改也不会丢失）
    if (!nodeEditCache[editingNode.id]) {
      nodeEditCache[editingNode.id] = {}
    }
    
    // 更新缓存中的节点信息
    nodeEditCache[editingNode.id].label = editingNode.label
    if (editingNode.nodeId) {
      nodeEditCache[editingNode.id].nodeId = editingNode.nodeId
    }
    if (editingNode.type) {
      nodeEditCache[editingNode.id].type = editingNode.type
    }
    
    // 同时更新节点映射表中的节点（立即生效）
    if (nodeMap[editingNode.id]) {
      nodeMap[editingNode.id].label = editingNode.label
      if (editingNode.nodeId) {
        nodeMap[editingNode.id].nodeId = editingNode.nodeId
      }
    }
    
    // 更新节点值
    if (!nodeValues[editingNode.id]) {
      nodeValues[editingNode.id] = {}
    }
    nodeValues[editingNode.id].value = editingNode.value
    nodeValues[editingNode.id].status = editingNode.status
    
    // 触发重新绘制（nodes computed会自动重新计算，应用缓存的数据）
    draw()
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

// 鼠标移动
const handleMouseMove = (e) => {
  if (isDragging.value) {
    offsetX.value += e.movementX
    offsetY.value += e.movementY
    draw()
  }
}

// 鼠标按下
const handleMouseDown = (e) => {
  isDragging.value = true
  dragStart.value = { x: e.clientX, y: e.clientY }
}

// 鼠标释放
const handleMouseUp = () => {
  isDragging.value = false
}

// 鼠标滚轮缩放
const handleWheel = (e) => {
  e.preventDefault()
  const delta = e.deltaY > 0 ? 0.9 : 1.1
  scale.value = Math.max(0.5, Math.min(3, scale.value * delta))
  draw()
}

// 重置视图
const resetView = () => {
  scale.value = 1
  offsetX.value = 0
  offsetY.value = 0
  draw()
}

// 放大
const zoomIn = () => {
  scale.value = Math.min(3, scale.value * 1.2)
  draw()
}

// 缩小
const zoomOut = () => {
  scale.value = Math.max(0.5, scale.value / 1.2)
  draw()
}

// 刷新数据
const refreshData = async () => {
  isLoading.value = true
  try {
    // 这里可以从OPC UA服务器获取实际数据
    // 模拟数据更新
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 更新节点数据
    Object.keys(nodeValues).forEach(key => {
      // 模拟数据变化
      if (Math.random() > 0.7) {
        const data = nodeValues[key]
        if (data.value && typeof data.value === 'string' && data.value.includes('%')) {
          const num = parseFloat(data.value) || 0
          data.value = `${Math.max(0, Math.min(100, num + (Math.random() - 0.5) * 10))}%`
        }
      }
    })
    
    draw()
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败: ' + error.message)
  } finally {
    isLoading.value = false
  }
}

// 更新节点数据的方法（供外部调用）
const updateNodeData = (nodeDataList) => {
  // 这个方法可以用于外部动态更新节点数据
  // 注意：由于使用了props，建议直接通过props传递新数据
  ElMessage.info('请通过 props.nodeDataList 传递数据')
}

// 更新连线数据的方法（供外部调用）
const updateLinkData = (linkDataList) => {
  // 这个方法可以用于外部动态更新连线数据
  // 注意：由于使用了props，建议直接通过props传递新数据
  ElMessage.info('请通过 props.linkDataList 传递数据')
}

// 更新单个节点的值
const updateNodeValue = (nodeId, value, status) => {
  if (nodeValues[nodeId]) {
    if (value !== undefined) nodeValues[nodeId].value = value
    if (status !== undefined) nodeValues[nodeId].status = status
    draw()
    return true
  }
  return false
}

// 批量更新节点值
const updateNodeValues = (updates) => {
  // updates: [{ nodeId, value, status }, ...]
  let updated = 0
  updates.forEach(update => {
    if (updateNodeValue(update.nodeId, update.value, update.status)) {
      updated++
    }
  })
  if (updated > 0) {
    draw()
  }
  return updated
}

// 获取当前节点数据（用于导出）
const getNodeData = () => {
  return nodes.value.map(node => ({
    id: node.id,
    nodeId: node.nodeId,
    label: node.label,
    type: node.type,
    display: {
      posX: node.x,
      posY: node.y,
      sizeX: node.width,
      sizeY: node.height
    },
    value: nodeValues[node.id]?.value || '',
    status: nodeValues[node.id]?.status || 'normal'
  }))
}

// 获取当前连线数据（用于导出）
const getLinkData = () => {
  return links.value.map(link => ({
    from: link.from,
    to: link.to,
    fromState: link.from,
    toState: link.to,
    isInstrument: link.isInstrument || false
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

// 窗口大小改变时重新初始化
const handleResize = () => {
  nextTick(() => {
    initCanvas()
  })
}

onMounted(() => {
  nextTick(() => {
    initCanvas()
    window.addEventListener('resize', handleResize)
  })
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
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

.canvas-wrapper {
  flex: 1;
  position: relative;
  overflow: hidden;
  background: #fff;
  background-image: 
    linear-gradient(rgba(0,0,0,.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0,0,0,.05) 1px, transparent 1px);
  background-size: 20px 20px;
}

canvas {
  display: block;
  cursor: grab;
  width: 100%;
  height: 100%;
}

canvas:active {
  cursor: grabbing;
}
</style>
