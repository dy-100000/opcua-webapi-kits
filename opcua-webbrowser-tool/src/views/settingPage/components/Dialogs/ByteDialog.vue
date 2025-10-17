<template>
  <el-dialog 
    :model-value="visible"
    @update:model-value="(val) => emit('update:visible', val)"
    title="Edit Byte Values" 
    width="500px"
    destroy-on-close 
    class="el-dialog-self"
  >
    <div class="byte-input-container">
      <div v-for="(value, index) in localData" :key="index" class="input-item">
        <el-form-item :label="`Value ${index + 1}`">
          <el-input
            :model-value="localData[index]"
            @update:model-value="(val) => handleChange(index, val)"
            placeholder="请输入0-99的整数"
            @keypress="isByteNumber"
            @keydown="isByteNumber"
            maxlength="2"
            style="width: 100%"
          ></el-input>
        </el-form-item>
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
import { isByteNumber, validateByteInput } from '../../composables/useDataProcessing';

interface Props {
  visible: boolean;
  data: number[];
}

interface Emits {
  (e: 'update:visible', value: boolean): void;
  (e: 'change', index: number, value: number): void;
  (e: 'confirm', data: number[]): void;
  (e: 'cancel'): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

// 创建本地数据副本
const localData = ref<number[]>([...props.data]);

// 监听 props.data 变化，同步到本地数据
watch(() => props.data, (newData) => {
  localData.value = [...newData];
}, { deep: true });

const handleChange = (index: number, value: number) => {
  validateByteInput(index, value.toString(), (idx, val) => {
    localData.value[idx] = Number(val);
  });
  localData.value[index] = value;
  emit('change', index, value);
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
.byte-input-container {
  padding: 20px 0;
  max-height: 400px;
  overflow-y: auto;
}

.byte-input-container .input-item {
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fafafa;
}

.byte-input-container .input-item:hover {
  background-color: #f0f9ff;
  border-color: #409eff;
}

.byte-input-container .input-item:last-child {
  margin-bottom: 0;
}

.byte-input-container .input-item .el-form-item {
  margin-bottom: 0;
}

.byte-input-container .input-item .el-form-item__label {
  font-weight: 500;
  color: #606266;
}
</style>
