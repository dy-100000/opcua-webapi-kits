<template>
  <div class="data-table-container">
    <el-table
      :data="tableData"
      style="width: 100%"
      @row-dblclick="handleRowDblClick"
      @row-click="handleRowClick"
      highlight-current-row
    >
      <el-table-column prop="name" label="Name" width="200" />
      <el-table-column prop="dataTypes" label="Data Type" width="150" />
      <el-table-column prop="value" label="Value" min-width="300">
        <template #default="{ row }">
          <div class="value-cell">
            <div 
              v-if="!row._isEditing" 
              class="value-display"
              @dblclick="startEditValue(row)"
            >
              <span class="value-text">{{ formatDisplayValue(row._editValue, row) }}</span>
              <el-icon class="edit-hint"><Edit /></el-icon>
            </div>
            
            <!-- Boolean 编辑 -->
            <el-card
              v-if="row.dataTypes === 'Boolean' && row._isEditing"
              class="bas-car"
              :prop="row.nodeId"
            >
              <div class="bas-inp">
                <el-form-item :prop="row.nodeId">
                  <el-select
                    v-model="row._editValue"
                    placeholder="请输入类型"
                    style="width: 240px"
                    @change="onchangeAll"
                  >
                    <el-option
                      v-for="item in row.enumStrings"
                      :key="item"
                      :label="item"
                      :value="item"
                    >
                      <span style="float: left">{{ item }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </div>
            </el-card>

            <!-- Double 编辑 -->
            <el-card
              v-if="row.dataTypes === 'Double' && row._isEditing"
              class="bas-car"
              :prop="row.nodeId"
            >
              <div class="bas-inp">
                <el-form-item :prop="row.nodeId">
                  <el-input
                    v-model="row._editValue"
                    placeholder="请输入数字"
                    style="width: 240px"
                    @input="validateNumberInput"
                    @keypress="isNumber"
                    @keydown="isNumber"
                    @focus="changeNodeId(row.nodeId)"
                    @compositionstart="compositionStart(row.nodeId)"
                    @compositionend="compositionEnd"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </div>
            </el-card>

            <!-- Byte 编辑 -->
            <el-card
              v-if="row.dataTypes === 'Byte' && row._isEditing"
              class="bas-car"
              :prop="row.nodeId"
            >
              <div class="bas-inp">
                <el-form-item :prop="row.nodeId">
                  <el-input
                    v-model="row._editValue"
                    placeholder="请输入0-99的整数"
                    style="width: 240px"
                    @input="(val) => validateByteInput(0, val, () => {})"
                    @keypress="isByteNumber"
                    @keydown="isByteNumber"
                    @focus="changeNodeId(row.nodeId)"
                    @compositionstart="compositionStart(row.nodeId)"
                    @compositionend="compositionEnd"
                    maxlength="2"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </div>
            </el-card>

            <!-- 其他类型的编辑 -->
            <el-card
              v-if="!['Boolean', 'Double', 'Byte'].includes(row.dataTypes) && row._isEditing"
              class="bas-car"
              :prop="row.nodeId"
            >
              <div class="bas-inp">
                <el-form-item :prop="row.nodeId">
                  <el-input
                    v-model="row._editValue"
                    placeholder="请输入值"
                    style="width: 240px"
                    @focus="changeNodeId(row.nodeId)"
                    @compositionstart="compositionStart(row.nodeId)"
                    @compositionend="compositionEnd"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </div>
            </el-card>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { Edit } from '@element-plus/icons-vue';
import { 
  isNumber, 
  validateNumberInput, 
  isByteNumber, 
  validateByteInput 
} from '../composables/useDataProcessing';
import type { EditingItem } from '../types';

interface Props {
  tableData: EditingItem[];
}

interface Emits {
  (e: 'row-dblclick', item: EditingItem, index: number): void;
  (e: 'row-click', item: EditingItem, index: number): void;
  (e: 'start-edit', item: EditingItem): void;
  (e: 'change-node-id', nodeId: string): void;
  (e: 'composition-start', nodeId: string): void;
  (e: 'composition-end'): void;
  (e: 'change-all'): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

const handleRowDblClick = (item: EditingItem, index: number) => {
  emit('row-dblclick', item, index);
};

const handleRowClick = (item: EditingItem, index: number) => {
  emit('row-click', item, index);
};

const startEditValue = (item: EditingItem) => {
  emit('start-edit', item);
};

const changeNodeId = (nodeId: string) => {
  emit('change-node-id', nodeId);
};

const compositionStart = (nodeId: string) => {
  emit('composition-start', nodeId);
};

const compositionEnd = () => {
  emit('composition-end');
};

const onchangeAll = () => {
  emit('change-all');
};

const formatDisplayValue = (value: any, item: EditingItem): string => {
  if (value === null || value === undefined) return '';
  
  if (Array.isArray(value)) {
    return value.join(', ');
  }
  
  if (typeof value === 'object') {
    return JSON.stringify(value);
  }
  
  return String(value);
};
</script>

<style scoped>
.data-table-container {
  width: 100%;
}

.value-cell {
  position: relative;
}

.value-display {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border: 1px solid transparent;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.value-display:hover {
  background-color: #f5f7fa;
  border-color: #dcdfe6;
}

.value-text {
  flex: 1;
  word-break: break-all;
}

.edit-hint {
  opacity: 0;
  transition: opacity 0.3s;
  margin-left: 8px;
}

.value-display:hover .edit-hint {
  opacity: 1;
}

.bas-car {
  margin: 0;
  border: 1px solid #409eff;
  box-shadow: 0 2px 4px rgba(64, 158, 255, 0.2);
}

.bas-inp {
  padding: 0;
}

.bas-inp .el-form-item {
  margin-bottom: 0;
}
</style>
