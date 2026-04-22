<template>
  <div class="instrument-node" :class="statusClass">
    <Handle type="source" :position="Position.Bottom" />
    <div class="node-body">
      <svg class="instrument-svg" viewBox="0 0 50 30" xmlns="http://www.w3.org/2000/svg">
        <!-- 渐变定义 -->
        <defs>
          <radialGradient :id="`instrument-gradient-${id}`" cx="30%" cy="30%" r="70%">
            <stop offset="0%" :stop-color="lighterColor" />
            <stop offset="60%" :stop-color="midColor" />
            <stop offset="100%" :stop-color="statusColor" />
          </radialGradient>
        </defs>

        <!-- 阴影 -->
        <ellipse
          cx="26"
          cy="16"
          rx="24"
          ry="14"
          fill="rgba(0,0,0,0.15)"
        />

        <!-- 椭圆主体 -->
        <ellipse
          cx="25"
          cy="15"
          rx="24"
          ry="14"
          :fill="`url(#instrument-gradient-${id})`"
          stroke="#333"
          stroke-width="2.5"
        />

        <!-- 中心点 -->
        <circle cx="25" cy="15" r="2" fill="#333" />

        <!-- 高光 -->
        <ellipse
          cx="18"
          cy="10"
          rx="7"
          ry="4"
          fill="rgba(255,255,255,0.4)"
        />

        <!-- 流量计标记 (FI类型) -->
        <rect
          v-if="isFlowInstrument"
          x="23"
          y="22"
          width="4"
          height="4"
          fill="#333"
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

const isFlowInstrument = computed(() => {
  return props.data.label && props.data.label.includes('FI')
})

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
.instrument-node {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.node-body {
  width: 50px;
  height: 30px;
  filter: drop-shadow(1px 1px 5px rgba(0, 0, 0, 0.2));
}

.instrument-svg {
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
