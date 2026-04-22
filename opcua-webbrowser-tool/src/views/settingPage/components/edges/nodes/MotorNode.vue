<template>
  <div class="motor-node" :class="statusClass">
    <Handle type="source" :position="Position.Bottom" />
    <div class="node-body">
      <svg class="motor-svg" viewBox="0 0 50 50" xmlns="http://www.w3.org/2000/svg">
        <!-- 渐变定义 -->
        <defs>
          <linearGradient :id="`motor-gradient-${id}`" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" :stop-color="lighterColor" />
            <stop offset="50%" :stop-color="midColor" />
            <stop offset="100%" :stop-color="statusColor" />
          </linearGradient>
        </defs>

        <!-- 阴影 -->
        <rect
          x="4"
          y="4"
          width="46"
          height="46"
          rx="5"
          ry="5"
          fill="rgba(0,0,0,0.2)"
        />

        <!-- 主体圆角矩形 -->
        <rect
          x="0"
          y="0"
          width="50"
          height="50"
          rx="5"
          ry="5"
          :fill="`url(#motor-gradient-${id})`"
          stroke="#333"
          stroke-width="3"
        />

        <!-- 内部边框 -->
        <rect
          x="2"
          y="2"
          width="46"
          height="46"
          rx="4"
          ry="4"
          fill="none"
          stroke="rgba(0,0,0,0.1)"
          stroke-width="1"
        />

        <!-- M字母 -->
        <text
          x="25"
          y="33"
          text-anchor="middle"
          font-size="30"
          font-weight="bold"
          font-family="Arial, sans-serif"
          fill="#1a1a1a"
        >M</text>

        <!-- 字母阴影效果 -->
        <text
          x="24"
          y="32"
          text-anchor="middle"
          font-size="30"
          font-weight="bold"
          font-family="Arial, sans-serif"
          fill="rgba(255,255,255,0.5)"
        >M</text>
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

const midColor = computed(() => {
  switch (props.data.status) {
    case 'warning': return '#ffb74d'
    case 'error': return '#e57373'
    default: return '#66bb6a'
  }
})

const lighterColor = computed(() => {
  switch (props.data.status) {
    case 'warning': return '#ffe0b2'
    case 'error': return '#ffcdd2'
    default: return '#c8e6c9'
  }
})
</script>

<style scoped>
.motor-node {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.node-body {
  width: 50px;
  height: 50px;
  filter: drop-shadow(2px 2px 6px rgba(0, 0, 0, 0.25));
}

.motor-svg {
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
