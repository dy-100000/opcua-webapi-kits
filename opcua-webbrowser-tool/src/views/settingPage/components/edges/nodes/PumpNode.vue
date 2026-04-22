<template>
  <div class="pump-node" :class="statusClass">
    <Handle type="target" :position="Position.Left" />
    <Handle type="source" :position="Position.Right" />
    <Handle id="motor" type="target" :position="Position.Top" />
    <div class="node-body">
      <svg class="pump-svg" viewBox="0 0 60 60" xmlns="http://www.w3.org/2000/svg">
        <!-- 渐变定义 -->
        <defs>
          <radialGradient :id="`pump-gradient-${id}`" cx="30%" cy="30%" r="70%">
            <stop offset="0%" :stop-color="lighterColor" />
            <stop offset="50%" :stop-color="midColor" />
            <stop offset="100%" :stop-color="statusColor" />
          </radialGradient>
          <linearGradient :id="`triangle-gradient-${id}`" x1="0%" y1="0%" x2="100%" y2="50%">
            <stop offset="0%" :stop-color="lighterColor" />
            <stop offset="100%" :stop-color="statusColor" />
          </linearGradient>
        </defs>

        <!-- 外圆阴影 -->
        <circle cx="32" cy="32" r="24" fill="rgba(0,0,0,0.15)" />

        <!-- 外圆 -->
        <circle
          cx="30"
          cy="30"
          r="24"
          :fill="`url(#pump-gradient-${id})`"
          stroke="#333"
          stroke-width="3.5"
        />

        <!-- 内圆 -->
        <circle
          cx="30"
          cy="30"
          r="12"
          fill="#fff"
          :stroke="statusColor"
          stroke-width="2.5"
        />

        <!-- 三角形箭头 -->
        <polygon
          points="48,30 35,20 35,40"
          :fill="`url(#triangle-gradient-${id})`"
          stroke="#333"
          stroke-width="2"
        />

        <!-- 三角形高光 -->
        <polygon
          points="45,27 42,30 45,33"
          fill="rgba(255,255,255,0.4)"
        />
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
.pump-node {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.node-body {
  width: 60px;
  height: 60px;
  filter: drop-shadow(2px 2px 7px rgba(0, 0, 0, 0.25));
}

.pump-svg {
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
