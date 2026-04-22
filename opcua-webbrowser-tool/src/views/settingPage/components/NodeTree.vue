<template>
  <div class="node-tree-container">
    <el-tree 
      ref="treeRef" 
      :props="treeProps"
      :data="treeData"
      :node-key="nodeKey"
      :accordion="false"
      :highlight-current="true"
      :empty-text="emptyText"
      :style="{ height: treeHeight + 'px', overflow: 'auto' }"
      @node-click="handleNodeClick"
      @node-contextmenu="handleContextMenu"
    >
      <template #default="{ node, data }">
        <div class="custom-tree-node" @dblclick="() => handleNodeDblClick(node, data)">
          <el-dropdown
            v-if="node.level !== 1"
            trigger="contextmenu"
            :ref="'dropdown' + node.data.id"
            @visible-change="(visible) => handleVisibleChange(node, visible)"
          >
            <div class="node-content">
              <el-icon v-if="node.level === 1" class="cus-icons">
                <FolderOpened />
              </el-icon>
              <el-icon v-else-if="connectFlag" class="cus-icons-connect">
                <Connection />
              </el-icon>
              <el-icon v-else class="cus-icons-connect">
                <SwitchButton />
              </el-icon>
              <span class="cus-label">
                {{ getDisplayName(data) || data.BrowseName }}
              </span>
            </div>
            <template #dropdown v-if="currentDrop === node.id">
              <el-dropdown-menu>
                <el-dropdown-item @click="handleProperties(data)">
                  Properties
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          
          <div v-else class="node-content">
            <img :src="objectImg" v-if="data.nodeClass === 1" class="svg-icon" />
            <img :src="typeImg" v-if="[8, 16, 32, 64].includes(data.nodeClass)" class="svg-icon" />
            <img :src="groupImg" v-if="data.nodeClass === 128" class="svg-icon" />
            <img :src="playImg" v-if="data.nodeClass === 4" class="svg-icon" />
            <span class="node-label cus-label">
              {{ getDisplayName(data) || data.displayName?._text }}
            </span>
          </div>
        </div>
      </template>
    </el-tree>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FolderOpened, Connection, SwitchButton } from '@element-plus/icons-vue';
import type { NodeData } from '../types';

// 导入图片资源 - 使用正确的 public 路径
const groupImg = '/assets/treeIcon/magnifying-glass-backup-svgrepo-com.svg';
const objectImg = '/assets/treeIcon/object-group-svgrepo-com.svg';
const playImg = '/assets/treeIcon/play-circle-svgrepo-com.svg';
const typeImg = '/assets/treeIcon/type-square-svgrepo-com.svg';

interface Props {
  treeData: NodeData[];
  connectFlag: boolean;
  currentDrop: string | null;
  treeHeight?: number;
  nodeKey?: string;
  emptyText?: string;
}

interface Emits {
  (e: 'node-click', data: NodeData): void;
  (e: 'node-dblclick', node: any, data: NodeData): void;
  (e: 'context-menu', event: MouseEvent, data: NodeData): void;
  (e: 'visible-change', node: any, visible: boolean): void;
  (e: 'properties', data: NodeData): void;
}

const props = withDefaults(defineProps<Props>(), {
  treeHeight: 600,
  nodeKey: 'NodeId',
  emptyText: '无数据'
});

const emit = defineEmits<Emits>();

const treeRef = ref();

// 辅助函数：获取显示名称（优先使用 displayName）
const getDisplayName = (item: any) => {
  if (!item) return '';
  
  // 优先使用 displayName
  if (item.displayName) {
    if (typeof item.displayName === 'string') {
      return item.displayName;
    }
    if (item.displayName._text) {
      return item.displayName._text;
    }
    if (item.displayName.text) {
      return item.displayName.text;
    }
    if (item.displayName.name) {
      return item.displayName.name;
    }
  }
  
  // 备选：使用 targetDisplayName（用于引用关系）
  if (item.targetDisplayName) {
    return item.targetDisplayName;
  }
  
  // 最后备选：使用 browseName 或 name
  return item.browseName || item.BrowseName || item.name || '';
};

const treeProps = {
  children: 'children',
  label: 'label',
  hasChildren: 'hasChildren'
};

const handleNodeClick = (data: NodeData) => {
  emit('node-click', data);
};

const handleNodeDblClick = (node: any, data: NodeData) => {
  emit('node-dblclick', node, data);
};

const handleContextMenu = (event: MouseEvent, data: NodeData) => {
  emit('context-menu', event, data);
};

const handleVisibleChange = (node: any, visible: boolean) => {
  emit('visible-change', node, visible);
};

const handleProperties = (data: NodeData) => {
  emit('properties', data);
};
</script>

<style scoped>
.node-tree-container {
  height: 100%;
  overflow: hidden;
}

.custom-tree-node {
  display: flex;
  align-items: center;
  width: 100%;
}

.node-content {
  display: flex;
  align-items: center;
  width: 100%;
}

.cus-icons {
  margin-right: 8px;
  color: #409eff;
}

.cus-icons-connect {
  margin-right: 8px;
  color: #67c23a;
}

.cus-label {
  flex: 1;
  font-size: 14px;
}

.svg-icon {
  width: 16px;
  height: 16px;
  margin-right: 8px;
}

.node-label {
  font-size: 14px;
}
</style>
