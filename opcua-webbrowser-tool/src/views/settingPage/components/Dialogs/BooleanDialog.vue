<template>
  <el-dialog 
    :model-value="visible"
    @update:model-value="(val) => emit('update:visible', val)"
    title="Edit Value" 
    width="500px"
    destroy-on-close 
    class="el-dialog-self"
  >
    <div class="boolean-checkbox-container">
      <div v-for="(value, index) in localData" :key="index" class="checkbox-item">
        <el-checkbox 
          :model-value="localData[index]"
          @update:model-value="(val) => handleChange(index, val)"
          :label="` ${index + 1}`"
        >
          {{ ` ${index + 1}` }}
        </el-checkbox>
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

interface Props {
  visible: boolean;
  data: boolean[];
}

interface Emits {
  (e: 'update:visible', value: boolean): void;
  (e: 'change', index: number, value: boolean): void;
  (e: 'confirm', data: boolean[]): void;
  (e: 'cancel'): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

// 创建本地数据副本
const localData = ref<boolean[]>([...props.data]);

// 监听 props.data 变化，同步到本地数据
watch(() => props.data, (newData) => {
  localData.value = [...newData];
}, { deep: true });

const handleChange = (index: number, value: boolean) => {
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
.boolean-checkbox-container {
  padding: 20px 0;
}

.checkbox-item {
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fafafa;
}

.checkbox-item:hover {
  background-color: #f0f9ff;
  border-color: #409eff;
}

.checkbox-item:last-child {
  margin-bottom: 0;
}
</style>
