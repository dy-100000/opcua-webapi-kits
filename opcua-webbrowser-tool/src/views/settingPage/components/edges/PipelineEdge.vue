<template>
  <path
    :id="id"
    class="vue-flow__edge-path pipeline-edge"
    :d="path"
    :marker-end="markerEnd"
  />
</template>

<script setup>
import { computed } from 'vue'
import { getSmoothStepPath } from '@vue-flow/core'

const props = defineProps({
  id: {
    type: String,
    required: true
  },
  sourceX: {
    type: Number,
    required: true
  },
  sourceY: {
    type: Number,
    required: true
  },
  targetX: {
    type: Number,
    required: true
  },
  targetY: {
    type: Number,
    required: true
  },
  sourcePosition: {
    type: String,
    required: true
  },
  targetPosition: {
    type: String,
    required: true
  },
  data: {
    type: Object,
    default: () => ({})
  },
  markerEnd: {
    type: String,
    default: ''
  }
})

const path = computed(() => {
  const [edgePath] = getSmoothStepPath({
    sourceX: props.sourceX,
    sourceY: props.sourceY,
    targetX: props.targetX,
    targetY: props.targetY,
    sourcePosition: props.sourcePosition,
    targetPosition: props.targetPosition,
    borderRadius: 10
  })
  return edgePath
})
</script>

<style>
.pipeline-edge {
  stroke: #333;
  stroke-width: 3;
  fill: none;
  stroke-linecap: round;
  stroke-linejoin: round;
}
</style>
