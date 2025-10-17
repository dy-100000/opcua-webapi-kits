<template>
  <el-menu default-active="1" class="el-menu-vertical" :collapse="isCollapse" @open="handleOpen" @close="handleClose">
    <el-menu-item index="1" class="menu" @click="$emit('changeNum', 1)">
      <template #title>
        <el-icon>
          <Management />
        </el-icon>
        <span>工程管理</span>
      </template>
    </el-menu-item>

    <el-menu-item index="2" class="menu" @click="$emit('changeNum', 2)">
      <template #title>
        <el-icon>
          <Management />
        </el-icon>
        <span>模版管理</span>

      </template>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { ref } from 'vue'
import { Management } from '@element-plus/icons-vue'

 const isCollapse = ref(false)

 const handleOpen = (key, keyPath) => {
  // console.log(key, keyPath)
}
 const handleClose = (key, keyPath) => {
  // console.log(key, keyPath);
}

defineExpose({
  isCollapse,
})
</script>

<style scoped>
.el-menu-vertical,
.el-menu-vertical:not(.el-menu--collapse) {
  height: 100vh;
}

.titleName {
  font-size: 24px;
  font-weight: 600;
}

.menu {
  font-size: 14px;

}
</style>