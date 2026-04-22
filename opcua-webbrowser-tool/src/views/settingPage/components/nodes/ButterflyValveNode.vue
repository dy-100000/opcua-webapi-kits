<template>
  <div class="butterfly-valve-node" :class="statusClass">
    <Handle type="target" :position="Position.Left" />
    <Handle type="source" :position="Position.Right" />
    <div class="node-body">
      <svg class="butterfly-svg" viewBox="0 0 50 40" xmlns="http://www.w3.org/2000/svg">
        <!-- 左侧三角形 -->
        <polygon
          :points="leftTriangle"
          :fill="gradientUrl('left')"
          stroke="#333"
          stroke-width="2.5"
        />
        <!-- 右侧三角形 -->
        <polygon
          :points="rightTriangle"
          :fill="gradientUrl('right')"
          stroke="#333"
          stroke-width="2.5"
        />
        <!-- 中心点 -->
        <circle cx="25" cy="20" r="3" fill="#333" />
        <!-- 高光效果 -->
        <polygon :points="leftHighlight" fill="rgba(255,255,255,0.3)" />
        <polygon :points="rightHighlight" fill="rgba(255,255,255,0.3)" />
        <!-- 渐变定义 -->
        <defs>
          <linearGradient :id="`butterfly-grad-left-${id}`" x1="0%" y1="0%" x2="100%" y2="50%">
            <stop offset="0%" :stop-color="lighterColor" />
            <stop offset="100%" :stop-color="statusColor" />
          </linearGradient>
          <linearGradient :id="`butterfly-grad-right-${id}`" x1="100%" y1="0%" x2="0%" y2="50%">
            <stop offset="0%" :stop-color="lighterColor" />
            <stop offset="100%" :stop-color="statusColor" />
          </linearGradient>
        </defs>
      </svg>
    </div>
    <div class="node-label">{{ data.label }}</div>
    <div v-if="data.value" class="node-value">{{ data.value }}</div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Handle, Position } from '@vue-flow/core'

const props = defineProps({
  id: {
    type: String,
    required: true
  },
  data: {
    type: Object,
    required: true
  }
})

const statusClass = computed(() => `status-${props.data.status || 'normal'}`)

const statusColor = computed(() => {
  switch (props.data.status) {
    case 'warning': return '#ff9800'
    case 'error': return '#f44336'
    default: return '#4caf50'
  }
})

const lighterColor = computed(() => {
  switch (props.data.status) {
    case 'warning': return '#ffcc80'
    case 'error': return '#ef9a9a'
    default: return '#a5d6a7'
  }
})

const gradientUrl = (side) => `url(#butterfly-grad-${side}-${props.id})`

// 三角形顶点
const leftTriangle = '0,5 22,20 0,35'
const rightTriangle = '50,5 28,20 50,35'
const leftHighlight = '3,8 15,18 3,12'
const rightHighlight = '47,8 35,18 47,12'
</script>

<style scoped>
.butterfly-valve-node {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.node-body {
  width: 50px;
  height: 40px;
  filter: drop-shadow(2px 2px 5px rgba(0, 0, 0, 0.2));
}

.butterfly-svg {
  width: 100%;
  height: 100%;
}

.node-label {
  margin-top: 4px;
  padding: 2px 8px;
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid #ddd;
  border-radius: 3px;
  font-size: 12px;
  font-weight: bold;
  color: #333;
}

.node-value {
  margin-top: 4px;
  padding: 2px 8px;
  background: rgba(240, 240, 240, 0.9);
  border: 1px solid #ddd;
  border-radius: 3px;
  font-size: 10px;
  color: #666;
}
</style>
