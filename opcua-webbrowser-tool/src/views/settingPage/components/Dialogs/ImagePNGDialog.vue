<template>
  <el-dialog 
    :model-value="visible"
    @update:model-value="(val) => emit('update:visible', val)"
    title="Edit ImagePNG Values" 
    width="800px"
    destroy-on-close 
    class="el-dialog-self"
  >
    <div class="imagepng-input-container">
      <div v-for="(value, index) in localData" :key="index" class="imagepng-item">
        <div class="imagepng-header">
          <span class="imagepng-label">Image {{ index + 1 }}</span>
        </div>
        <div class="imagepng-content">
          <!-- 图片预览 -->
          <div class="image-preview">
            <img 
              v-if="previewImages[index]" 
              :src="previewImages[index]" 
              :alt="`Image ${index + 1}`"
              class="preview-image"
              @error="handleImageError(index)"
            />
            <div v-else class="no-image">
              <el-icon class="no-image-icon"><Picture /></el-icon>
              <span>无法显示图片</span>
            </div>
          </div>
          
          <!-- 图片信息 -->
          <div class="image-info">
            <el-form-item label="Base64 数据">
              <el-input
                :model-value="localData[index]"
                @update:model-value="(val) => handleChange(index, val)"
                type="textarea"
                :rows="4"
                placeholder="Base64 编码的图片数据"
                style="width: 100%"
              ></el-input>
            </el-form-item>
            
            <el-form-item label="操作">
              <el-button @click="uploadImage(index)" type="primary" size="small">
                <el-icon><Upload /></el-icon>
                上传新图片
              </el-button>
              <el-button @click="downloadImage(index)" size="small">
                <el-icon><Download /></el-icon>
                下载图片
              </el-button>
              <el-button @click="clearImage(index)" size="small" type="danger">
                <el-icon><Delete /></el-icon>
                清除图片
              </el-button>
            </el-form-item>
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
    
    <!-- 隐藏的文件上传输入 -->
    <input 
      ref="fileInputRef" 
      type="file" 
      accept="image/png,image/jpeg,image/gif" 
      style="display: none"
      @change="handleFileUpload"
    />
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import { Picture, Upload, Download, Delete } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

interface Props {
  visible: boolean;
  data: string[];
}

interface Emits {
  (e: 'update:visible', value: boolean): void;
  (e: 'change', index: number, value: string): void;
  (e: 'confirm', data: string[]): void;
  (e: 'cancel'): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

// 创建本地数据副本
const localData = ref<string[]>([...props.data]);
const fileInputRef = ref<HTMLInputElement>();
const currentUploadIndex = ref<number>(-1);

// 监听 props.data 变化，同步到本地数据
watch(() => props.data, (newData) => {
  localData.value = [...newData];
}, { deep: true });

// 计算预览图片
const previewImages = computed(() => {
  return localData.value.map(base64Data => {
    if (!base64Data) return null;
    
    try {
      // 如果数据已经是 base64 格式
      if (base64Data.startsWith('data:image/')) {
        return base64Data;
      }
      
      // 如果是二进制字符串，转换为 base64
      const binaryString = base64Data;
      const base64 = btoa(binaryString);
      return `data:image/png;base64,${base64}`;
    } catch (error) {
      console.error('Error converting to base64:', error);
      return null;
    }
  });
});

const handleChange = (index: number, value: string) => {
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

// 处理图片错误
const handleImageError = (index: number) => {
  console.warn(`Image ${index + 1} failed to load`);
};

// 上传图片
const uploadImage = (index: number) => {
  currentUploadIndex.value = index;
  fileInputRef.value?.click();
};

// 处理文件上传
const handleFileUpload = (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  
  if (!file) return;
  
  if (currentUploadIndex.value === -1) return;
  
  // 检查文件类型
  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件');
    return;
  }
  
  // 检查文件大小 (限制为 5MB)
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片文件大小不能超过 5MB');
    return;
  }
  
  const reader = new FileReader();
  reader.onload = (e) => {
    const result = e.target?.result as string;
    if (result) {
      localData.value[currentUploadIndex.value] = result;
      ElMessage.success('图片上传成功');
    }
  };
  reader.onerror = () => {
    ElMessage.error('图片读取失败');
  };
  reader.readAsDataURL(file);
  
  // 清空文件输入
  target.value = '';
  currentUploadIndex.value = -1;
};

// 下载图片
const downloadImage = (index: number) => {
  const base64Data = localData.value[index];
  if (!base64Data) {
    ElMessage.warning('没有可下载的图片');
    return;
  }
  
  try {
    let dataUrl = base64Data;
    
    // 如果不是完整的 data URL，添加前缀
    if (!base64Data.startsWith('data:image/')) {
      dataUrl = `data:image/png;base64,${base64Data}`;
    }
    
    const link = document.createElement('a');
    link.href = dataUrl;
    link.download = `image_${index + 1}.png`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    
    ElMessage.success('图片下载成功');
  } catch (error) {
    console.error('Download error:', error);
    ElMessage.error('图片下载失败');
  }
};

// 清除图片
const clearImage = (index: number) => {
  localData.value[index] = '';
  ElMessage.success('图片已清除');
};
</script>

<style scoped>
.imagepng-input-container {
  padding: 20px 0;
  max-height: 500px;
  overflow-y: auto;
}

.imagepng-item {
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  background-color: #fafafa;
}

.imagepng-header {
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.imagepng-label {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.imagepng-content {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.image-preview {
  flex-shrink: 0;
  width: 200px;
  height: 150px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  overflow: hidden;
}

.preview-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.no-image {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #909399;
}

.no-image-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.image-info {
  flex: 1;
}

.image-info .el-form-item {
  margin-bottom: 16px;
}

.image-info .el-form-item:last-child {
  margin-bottom: 0;
}

.image-info .el-button {
  margin-right: 8px;
  margin-bottom: 8px;
}

.image-info .el-button:last-child {
  margin-right: 0;
}
</style>
