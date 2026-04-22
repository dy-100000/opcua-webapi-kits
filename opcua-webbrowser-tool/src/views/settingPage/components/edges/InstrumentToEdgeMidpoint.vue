
<template>
    <BaseEdge :id="id" :path="edgePath" :style="edgeStyle" />
  </template>

  <script setup>
  import { computed } from 'vue'
  import { BaseEdge, useVueFlow } from '@vue-flow/core'

  const props = defineProps({
    id: { type: String, required: true },
    sourceX: { type: Number, required: true },
    sourceY: { type: Number, required: true },
    sourcePosition: { type: String, required: true },
    targetX: { type: Number, required: true },
    targetY: { type: Number, required: true },
    targetPosition: { type: String, required: true },
    data: { type: Object, default: () => ({}) },
    markerEnd: { type: String, default: '' },
    style: { type: Object, default: () => ({}) }
  })

  const { findNode } = useVueFlow()

  // 计算目标边的中点坐标
  const midpoint = computed(() => {
    const { targetEdgeSource, targetEdgeTarget } = props.data || {}

    if (!targetEdgeSource || !targetEdgeTarget) {
      // 如果没有指定目标边，使用默认的 targetX, targetY
      return { x: props.targetX, y: props.targetY }
    }

    const sourceNode = findNode(targetEdgeSource)
    const targetNode = findNode(targetEdgeTarget)

    if (!sourceNode || !targetNode) {
      return { x: props.targetX, y: props.targetY }
    }

    // 获取节点中心点（考虑节点尺寸）
    const sourceWidth = sourceNode.dimensions?.width || 60
    const sourceHeight = sourceNode.dimensions?.height || 40
    const targetWidth = targetNode.dimensions?.width || 60
    const targetHeight = targetNode.dimensions?.height || 40

    const sourceCenterX = sourceNode.position.x + sourceWidth / 2
    const sourceCenterY = sourceNode.position.y + sourceHeight / 2
    const targetCenterX = targetNode.position.x + targetWidth / 2
    const targetCenterY = targetNode.position.y + targetHeight / 2

    // 计算两个节点中心连线的中点
    return {
      x: (sourceCenterX + targetCenterX) / 2,
      y: (sourceCenterY + targetCenterY) / 2
    }
  })

  // 生成直线路径
  const edgePath = computed(() => {
    const { x: endX, y: endY } = midpoint.value
    return `M ${props.sourceX} ${props.sourceY} L ${endX} ${endY}`
  })

  // 虚线样式
  const edgeStyle = computed(() => ({
    stroke: '#666',
    strokeWidth: 1,
    strokeDasharray: '5, 5',
    fill: 'none',
    ...props.style
  }))
  </script>