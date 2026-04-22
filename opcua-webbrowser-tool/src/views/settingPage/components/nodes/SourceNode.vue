<template>
  <div class="source-node" :class="statusClass">
    <Handle type="source" :position="Position.Right" />
    <div class="node-body">
      <div class="arrow-right"></div>
    </div>
    <div class="node-label">{{ data.label }}</div>
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
</script>

<style scoped>
.source-node {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.node-body {
  position: relative;
  width: 80px;
  height: 40px;
  background: linear-gradient(180deg, #e8f5e9 0%, #a5d6a7 50%, #4caf50 100%);
  border: 2.5px solid #333;
  border-radius: 3px;
  box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.2);
}

.arrow-right {
  position: absolute;
  right: -20px;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-top: 14px solid transparent;
  border-bottom: 14px solid transparent;
  border-left: 20px solid #4caf50;
}

.arrow-right::after {
  content: '';
  position: absolute;
  left: -22px;
  top: -14px;
  width: 0;
  height: 0;
  border-top: 14px solid transparent;
  border-bottom: 14px solid transparent;
  border-left: 20px solid #333;
  z-index: -1;
}

.status-normal .node-body {
  background: linear-gradient(180deg, #e8f5e9 0%, #a5d6a7 50%, #4caf50 100%);
}

.status-normal .arrow-right {
  border-left-color: #4caf50;
}

.status-warning .node-body {
  background: linear-gradient(180deg, #fff3e0 0%, #ffcc80 50%, #ff9800 100%);
}

.status-warning .arrow-right {
  border-left-color: #ff9800;
}

.status-error .node-body {
  background: linear-gradient(180deg, #ffebee 0%, #ef9a9a 50%, #f44336 100%);
}

.status-error .arrow-right {
  border-left-color: #f44336;
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
