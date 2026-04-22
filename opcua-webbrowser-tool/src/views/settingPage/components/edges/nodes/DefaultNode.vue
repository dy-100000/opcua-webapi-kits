<template>
  <div class="default-node" :class="statusClass">
    <Handle type="target" :position="Position.Left" />
    <Handle type="source" :position="Position.Right" />
    <div class="node-body" :style="nodeStyle">
      <span class="node-text">{{ data.label }}</span>
    </div>
    <div v-if="data.value" class="node-value">{{ data.value }}</div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Handle, Position } from '@vue-flow/core'

const props = defineProps({
  data: {
    type: Object,
    required: true
  }
})

const statusClass = computed(() => `status-${props.data.status || 'normal'}`)

const nodeStyle = computed(() => ({
  width: `${props.data.width || 60}px`,
  height: `${props.data.height || 40}px`
}))
</script>

<style scoped>
.default-node {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.node-body {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, #e3f2fd 0%, #90caf9 50%, #2196f3 100%);
  border: 2px solid #333;
  border-radius: 4px;
  box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
  padding: 8px;
}

.status-normal .node-body {
  background: linear-gradient(180deg, #e8f5e9 0%, #a5d6a7 50%, #4caf50 100%);
}

.status-warning .node-body {
  background: linear-gradient(180deg, #fff3e0 0%, #ffcc80 50%, #ff9800 100%);
}

.status-error .node-body {
  background: linear-gradient(180deg, #ffebee 0%, #ef9a9a 50%, #f44336 100%);
}

.node-text {
  font-size: 12px;
  font-weight: bold;
  color: #333;
  text-align: center;
  word-break: break-word;
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
