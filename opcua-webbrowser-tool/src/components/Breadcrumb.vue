<template>
   <div>
    deawfwefweaew
   </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const breadcrumbList = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.breadcrumb)
  return matched
})
</script>

<style scoped>
.el-breadcrumb {
  margin: 20px 0;
  padding: 0 20px;
}
</style> 