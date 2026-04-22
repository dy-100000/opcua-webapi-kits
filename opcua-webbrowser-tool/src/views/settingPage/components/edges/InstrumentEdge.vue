<template>
  <path
    :id="id"
    class="vue-flow__edge-path instrument-edge"
    :d="path"
    :marker-end="markerEnd"
  />
</template>

<script setup>
import { computed } from 'vue'
import { getStraightPath } from '@vue-flow/core'

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
  const [edgePath] = getStraightPath({
    sourceX: props.sourceX,
    sourceY: props.sourceY,
    targetX: props.targetX,
    targetY: props.targetY
  })
  return edgePath
})
</script>

<style>
.instrument-edge {
  stroke: #666;
  stroke-width: 1;
  stroke-dasharray: 5, 5;
  fill: none;
  stroke-linecap: round;
}
</style>
