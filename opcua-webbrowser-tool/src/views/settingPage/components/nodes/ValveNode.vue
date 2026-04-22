<template>
  <div class="valve-node" :class="statusClass">
    <Handle type="target" :position="Position.Left" />
    <Handle type="source" :position="Position.Right" />
    <div class="node-body">
      <div class="valve-circle">
        <div class="valve-x">
          <div class="x-line x-line-1"></div>
          <div class="x-line x-line-2"></div>
        </div>
      </div>
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
.valve-node {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.node-body {
  position: relative;
  width: 52px;
  height: 52px;
}

.valve-circle {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 46px;
  height: 46px;
  background: radial-gradient(circle at 30% 30%, #fafafa 0%, #e8e8e8 100%);
  border: 3px solid #666;
  border-radius: 50%;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
}

.valve-x {
  position: relative;
  width: 26px;
  height: 26px;
}

.x-line {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 26px;
  height: 6px;
  border-radius: 3px;
}

.x-line-1 {
  transform: translate(-50%, -50%) rotate(45deg);
}

.x-line-2 {
  transform: translate(-50%, -50%) rotate(-45deg);
}

.status-normal .x-line {
  background: #4caf50;
}

.status-warning .x-line {
  background: #ff9800;
}

.status-error .x-line {
  background: #f44336;
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
