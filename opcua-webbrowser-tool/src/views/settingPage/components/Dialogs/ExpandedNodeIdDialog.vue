<template>
  <el-dialog 
    :model-value="visible"
    @update:model-value="(val) => emit('update:visible', val)"
    title="Edit ExpandedNodeId Values" 
    width="800px"
    destroy-on-close 
    class="el-dialog-self"
  >
    <div class="expandednodeid-input-container">
      <div v-for="(value, index) in localData" :key="index" class="expandednodeid-item">
        <div class="expandednodeid-header">
          <span class="expandednodeid-label">Value {{ index + 1 }}</span>
        </div>
        <div class="expandednodeid-fields">
          <div class="field-row">
            <div class="field-group">
              <label class="field-label">IdentifierType</label>
              <el-select
                :model-value="localData[index].identifierType"
                @update:model-value="(val) => handleChange(index, 'identifierType', val)"
                placeholder="请选择 IdentifierType"
                style="width: 100%"
              >
                <el-option
                  v-for="option in identifierTypeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </div>
            <div class="field-group">
              <label class="field-label">NsIndex</label>
              <el-input
                :model-value="localData[index].nsIndex"
                @update:model-value="(val) => handleChange(index, 'nsIndex', val)"
                placeholder="请输入 NsIndex"
                style="width: 100%"
              ></el-input>
            </div>
          </div>
          <div class="field-row">
            <div class="field-group">
              <label class="field-label">Value</label>
              <el-input
                :model-value="localData[index].value"
                @update:model-value="(val) => handleChange(index, 'value', val)"
                placeholder="请输入 Value"
                style="width: 100%"
              ></el-input>
            </div>
            <div class="field-group">
              <label class="field-label">ServerIndex</label>
              <el-input
                :model-value="localData[index].serverIndex"
                @update:model-value="(val) => handleChange(index, 'serverIndex', val)"
                placeholder="请输入 ServerIndex"
                style="width: 100%"
              ></el-input>
            </div>
          </div>
          <div class="field-row">
            <div class="field-group full-width">
              <label class="field-label">NamespaceUri</label>
              <el-input
                :model-value="localData[index].namespaceUri"
                @update:model-value="(val) => handleChange(index, 'namespaceUri', val)"
                placeholder="请输入 NamespaceUri"
                style="width: 100%"
              ></el-input>
            </div>
          </div>
        </div>
      </div>
    </div>
       
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { identifierTypeOptions } from '../../composables/useDataProcessing';
import type { ExpandedNodeIdData } from '../../types';

interface Props {
  visible: boolean;
  data: ExpandedNodeIdData[];
}

interface Emits {
  (e: 'update:visible', value: boolean): void;
  (e: 'change', index: number, field: string, value: any): void;
  (e: 'confirm', data: ExpandedNodeIdData[]): void;
  (e: 'cancel'): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

// 创建本地数据副本
const localData = ref<ExpandedNodeIdData[]>([...props.data]);

// 监听 props.data 变化，同步到本地数据
watch(() => props.data, (newData) => {
  localData.value = [...newData];
}, { deep: true });

const handleChange = (index: number, field: string, value: any) => {
  localData.value[index][field as keyof ExpandedNodeIdData] = value;
  emit('change', index, field, value);
};

const handleConfirm = () => {
  emit('confirm', localData.value);
  emit('update:visible', false);
};

const handleCancel = () => {
  // 重置为原始数据
  localData.value = [...props.data];
  emit('cancel');
  emit('update:visible', false);
};
</script>

<style scoped>
.expandednodeid-input-container {
  padding: 20px 0;
  max-height: 500px;
  overflow-y: auto;
}

.expandednodeid-item {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  background-color: #fafafa;
}

.expandednodeid-item:hover {
  background-color: #f0f9ff;
  border-color: #409eff;
}

.expandednodeid-item:last-child {
  margin-bottom: 0;
}

.expandednodeid-header {
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e7ed;
}

.expandednodeid-label {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.expandednodeid-fields {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.field-row {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.field-group {
  flex: 1;
  min-width: 200px;
}

.field-group.full-width {
  flex: 1 1 100%;
  min-width: 100%;
}

.field-label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  font-size: 12px;
  color: #606266;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.field-group .el-input {
  width: 100%;
}

.field-group .el-input__inner {
  font-size: 13px;
  padding: 8px 12px;
}

.field-group .el-select {
  width: 100%;
}

.field-group .el-select .el-input__inner {
  font-size: 13px;
  padding: 8px 12px;
}
</style>
