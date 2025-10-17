import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const urlData = defineStore('urlData', () => {
  // 存储所有动态页面，使用对象按key存储
  const urlData = ref({});
  
  // 根据key获取数据
  const getDataByKey = (key) => {
    return urlData.value[key];
  };
  
  // 根据key设置数据
  const setDataByKey = (key, data) => {
    urlData.value[key] = data;
  };
  
  // 根据key删除数据
  const removeDataByKey = (key) => {
    delete urlData.value[key];
  };
  
  // 获取所有数据
  const getAllData = computed(() => urlData.value);
  
  // 检查key是否存在
  const hasKey = (key) => {
    return key in urlData.value;
  };
  
  // 清空所有数据
  const clearAllData = () => {
    urlData.value = {};
  };
  
  return {
    urlData,
    getDataByKey,
    setDataByKey,
    removeDataByKey,
    getAllData,
    hasKey,
    clearAllData
  };
});