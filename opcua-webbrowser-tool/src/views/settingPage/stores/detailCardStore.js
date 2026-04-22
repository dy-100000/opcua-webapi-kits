/**
 * 详情卡片数据状态管理
 * 使用Pinia管理getDetialCard获取的数据
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useDetailCardStore = defineStore('detailCard', () => {
  // 状态：存储每个节点的展开数据，key为节点ID，value为子数据数组
  const nodeDetailData = ref(new Map())
  
  // 当前节点ID，用于切换节点时清空数据
  const currentNodeId = ref(null)

  /**
   * 保存节点的展开数据
   * @param {string} nodeId - 节点ID
   * @param {Array} data - 子数据数组
   */
  const saveNodeDetailData = (nodeId, data) => {
    if (!nodeId) return
    
    const nodeIdStr = nodeId.toString()
    nodeDetailData.value.set(nodeIdStr, data)
  }

  /**
   * 获取节点的展开数据
   * @param {string} nodeId - 节点ID
   * @returns {Array|null} 子数据数组，如果不存在则返回null
   */
  const getNodeDetailData = (nodeId) => {
    if (!nodeId) return null
    
    const nodeIdStr = nodeId.toString()
    return nodeDetailData.value.get(nodeIdStr) || null
  }

  /**
   * 检查节点是否有保存的数据
   * @param {string} nodeId - 节点ID
   * @returns {boolean}
   */
  const hasNodeDetailData = (nodeId) => {
    if (!nodeId) return false
    
    const nodeIdStr = nodeId.toString()
    return nodeDetailData.value.has(nodeIdStr)
  }

  /**
   * 清空指定节点的数据
   * @param {string} nodeId - 节点ID，如果为空则清空所有数据
   */
  const clearNodeDetailData = (nodeId = null) => {
    if (nodeId) {
      const nodeIdStr = nodeId.toString()
      nodeDetailData.value.delete(nodeIdStr)
    } else {
      nodeDetailData.value.clear()
    }
  }

  /**
   * 切换节点时清空数据
   * @param {string} newNodeId - 新节点ID
   */
  const switchNode = (newNodeId) => {
    const newNodeIdStr = newNodeId?.toString()
    
    // 如果切换到了不同的节点，清空之前的数据
    if (currentNodeId.value && currentNodeId.value !== newNodeIdStr) {
      clearNodeDetailData()
    }
    
    currentNodeId.value = newNodeIdStr
  }

  /**
   * 获取所有保存的节点ID列表
   * @returns {Array} 节点ID数组
   */
  const getAllNodeIds = () => {
    return Array.from(nodeDetailData.value.keys())
  }

  /**
   * 获取保存的数据总数
   * @returns {number}
   */
  const getTotalDataCount = () => {
    let total = 0
    nodeDetailData.value.forEach((data) => {
      total += data.length
    })
    return total
  }

  return {
    // 状态
    nodeDetailData,
    currentNodeId,
    
    // 方法
    saveNodeDetailData,
    getNodeDetailData,
    hasNodeDetailData,
    clearNodeDetailData,
    switchNode,
    getAllNodeIds,
    getTotalDataCount
  }
})

