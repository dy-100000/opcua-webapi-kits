<template>
  <div class="tank-node" :class="statusClass">
    <Handle id="top" type="target" :position="Position.Top" />
    <Handle id="left-top" type="target" :position="Position.Left" :style="{ top: '30%' }" />
    <Handle id="left-middle" type="target" :position="Position.Left" :style="{ top: '50%' }" />
    <Handle id="left-bottom" type="target" :position="Position.Left" :style="{ top: '70%' }" />
    <Handle id="right-top" type="target" :position="Position.Right" :style="{ top: '25%' }" />
    <Handle id="right" type="target" :position="Position.Right" :style="{ top: '50%' }" />
    <Handle id="bottom" type="source" :position="Position.Bottom" />
    <div class="node-body">
      <svg class="tank-svg" :viewBox="`0 0 ${width} ${height + 40}`" xmlns="http://www.w3.org/2000/svg">
        <!-- 渐变定义 -->
        <defs>
          <linearGradient id="tank-gradient" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stop-color="#F0F8FF" />
            <stop offset="50%" stop-color="#E8F4F8" />
            <stop offset="100%" stop-color="#E0EFF4" />
          </linearGradient>
          <linearGradient :id="`liquid-gradient-${id}`" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" :stop-color="lighterColor" />
            <stop offset="100%" :stop-color="statusColor" />
          </linearGradient>
        </defs>

        <!-- 阴影 -->
        <ellipse
          :cx="width / 2"
          :cy="height + 25"
          :rx="width / 2 - 5"
          ry="10"
          fill="rgba(0,0,0,0.1)"
        />

        <!-- 顶部椭圆 -->
        <ellipse
          :cx="width / 2"
          cy="20"
          :rx="width / 2"
          ry="20"
          fill="url(#tank-gradient)"
          stroke="#333"
          stroke-width="3"
        />

        <!-- 圆柱侧面 -->
        <rect
          x="0"
          y="20"
          :width="width"
          :height="height"
          fill="url(#tank-gradient)"
          stroke="#333"
          stroke-width="3"
        />

        <!-- 底部椭圆 -->
        <ellipse
          :cx="width / 2"
          :cy="height + 20"
          :rx="width / 2"
          ry="20"
          fill="url(#tank-gradient)"
          stroke="#333"
          stroke-width="3"
        />

        <!-- 液位填充 -->
        <g v-if="levelHeight > 0">
          <rect
            x="5"
            :y="20 + height - levelHeight"
            :width="width - 10"
            :height="Math.max(0, levelHeight - 20)"
            :fill="`url(#liquid-gradient-${id})`"
            opacity="0.7"
          />
          <!-- 液位表面高光 -->
          <rect
            x="5"
            :y="20 + height - levelHeight"
            :width="width - 10"
            height="3"
            fill="rgba(255,255,255,0.5)"
          />
        </g>

        <!-- 高光效果 -->
        <ellipse
          :cx="width / 2 - width * 0.2"
          cy="12"
          :rx="width * 0.2"
          ry="6"
          fill="rgba(255,255,255,0.4)"
        />

        <!-- 侧边高光 -->
        <rect
          :x="width - 8"
          y="25"
          width="3"
          :height="height - 10"
          fill="rgba(255,255,255,0.25)"
        />

        <!-- 结构线 -->
        <line
          :x1="width / 4"
          :y1="20 + height * 0.3"
          :x2="width / 4"
          :y2="20 + height * 0.7"
          stroke="rgba(0,0,0,0.08)"
          stroke-width="1"
        />
        <line
          :x1="width * 3 / 4"
          :y1="20 + height * 0.3"
          :x2="width * 3 / 4"
          :y2="20 + height * 0.7"
          stroke="rgba(0,0,0,0.08)"
          stroke-width="1"
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

const width = computed(() => props.data.width || 120)
const height = computed(() => props.data.height || 200)

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

const levelHeight = computed(() => {
  const value = props.data.value
  if (!value) return 0
  const level = parseFloat(value) || 0
  return (height.value * Math.max(0, Math.min(100, level))) / 100
})
</script>

<style scoped>
.tank-node {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.node-body {
  filter: drop-shadow(3px 3px 8px rgba(0, 0, 0, 0.2));
}

.tank-svg {
  width: 120px;
  height: 240px;
}

.node-label {
  margin-top: 8px;
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
