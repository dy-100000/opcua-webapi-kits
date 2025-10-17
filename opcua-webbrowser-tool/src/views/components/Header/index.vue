<template>
  <div class=" ">
    <!-- <el-icon  class="closeMenu left" :size="16" 
           @click="$emit('updateNarBar')"><Fold /></el-icon>
           <span class="welcome left" ></span> -->
    <div class="top-box">
      <div class="left-select">
        <p class="label">工程类型</p>
        <el-select v-show="props.changeNums ==1" v-model="value" placeholder="请选择" style="width: 240px" @change="onchange">
          <el-option v-for="item in selectDatas" :key="item.id" :label="item.name" :value="item.id">
            <span style="float: left">{{ item.name }}</span>
          </el-option>
        </el-select>
        <el-select v-show="props.changeNums ==2" v-model="valueProject" placeholder="请选择" style="width: 240px" @change="onchangeProject">
          <el-option v-for="item in selectProjectDatas" :key="item.typeId" :label="item.name" :value="item.typeId">
            <span style="float: left">{{ item.name }}</span>
          </el-option>
        </el-select>
      </div>
      <div v-show="props.changeNums ==1" class="right-icon">
        <p @click="dialogVisible = true">
          <el-tooltip class="item" effect="dark" content="新建工程" placement="top">
            <el-icon :size="size" :color="color">
              <CirclePlus />
            </el-icon>
          </el-tooltip>
        </p>
        <p @click="deleteRow">
          <el-tooltip class="item" effect="dark" content="删除工程" placement="top">
            <el-icon :size="size" :color="color">
              <Remove />
            </el-icon>
          </el-tooltip>
        </p>
        <p @click="clickSetting">
          <el-tooltip class="item" effect="dark" content="设置端口" placement="top">
            <el-icon :size="size" :color="color">
              <Setting />
            </el-icon>
          </el-tooltip>
        </p>
        <p>
          <el-icon :size="size" :color="color">
            <Document />
          </el-icon>
        </p>
      </div>
      <div v-show="props.changeNums ==2" class="right-icon">
        <p @click="dialogTwoVisible = true">
          <el-tooltip class="item" effect="dark" content="创建类型" placement="top">
            <el-icon :size="size" :color="color">
              <CirclePlus />
            </el-icon>
          </el-tooltip>
        </p>
        <p @click="deleteRowNode">
          <el-tooltip class="item" effect="dark" content="删除类型" placement="top">
            <el-icon :size="size" :color="color">
              <Remove />
            </el-icon>
          </el-tooltip>
        </p>
        <p @click="clickSetting">
          <el-tooltip class="item" effect="dark" content="设置端口" placement="top">
            <el-icon :size="size" :color="color">
              <Setting />
            </el-icon>
          </el-tooltip>
        </p>
         
      </div>
    </div>
  </div>
  <el-dialog v-model="dialogVisible" title="" width="500">
    <div class="modal-content">
      <!-- 这里是模态框内容，它会被动态替换 -->
      <!-- <component :is="modalContent" /> -->
      <el-form :model="form" ref="formRef" :rules="rules">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="&nbsp;&nbsp;描述" prop="desc">
          <el-input v-model="form.desc" placeholder="请输入描述"></el-input>
        </el-form-item>
        <el-form-item class="right-btn">
          <el-button type="primary" @click="submitForm">提交</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
  <el-dialog v-model="dialogTwoVisible" title="" width="500">
    <div class="modal-content">
      <!-- 这里是模态框内容，它会被动态替换 -->
      <!-- <component :is="modalContent" /> -->
      <el-form :model="form" ref="formRef" :rules="rules">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item class="right-btn">
          <el-button type="primary" @click="submitTwoForm">提交</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
  <el-dialog v-model="settingVisible" title="" width="500">
    <div class="modal-content">
      <el-form :model="form" ref="formRef" :rules="rules">
        <el-form-item label="地址" prop="adress">
          <el-input v-model="form.adress" placeholder="请输入地址"></el-input>
        </el-form-item>
        <el-form-item label="端口" prop="port">
          <el-input v-model="form.port" placeholder="请输入端口"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button class="right-btn" type="primary" @click="submitPort">确定</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>

<script setup>
import { ModellingServiceApi } from '../../../api/home/modellingserviceapi';
import { ElMessage } from 'element-plus';
// import { CirclePlus,Remove, Setting, Document } from '@element-plus/icons-vue'
import { ref, computed, reactive, defineAsyncComponent } from 'vue';
const value = ref('');
const valueProject = ref('');

const size = ref(16);
const color = ref('rgb(0,0,0)');
const dialogVisible = ref(false);
const dialogTwoVisible = ref(false);

const settingVisible = ref(false);
const modalContent = ref(null);
const settingData = reactive({});
const emit = defineEmits(['submitData','submitTwoData', 'selectKey','selectProjectKey', 'refreshTableData','refreshTableDataTwo']);
const props = defineProps({
  selectData: Object,
  selectProjectData: Object,
  rowData: Object,
  changeNums: Number,
});

let selectDatas = computed(() => {
  value.value = props.selectData[0]?.name;
  return props.selectData;
});
let selectProjectDatas = computed(() => {
  valueProject.value = props.selectProjectData[0]?.name;
  return props.selectProjectData;
});
const onchange = (e) => {
  value.value = e;
  emit('selectKey', value);
};

const onchangeProject = (e) => {
  valueProject.value = e;
  console.log(e, 'e');
  emit('selectProjectKey', valueProject);
};
const handelVisible = () => {
  dialogVisible.value = true;
  console.log(dialogVisible, 'valuevalue');
};
const handelTwoVisible = () => {
  dialogTwoVisible.value = true;
  console.log(dialogVisible, 'valuevalue');
};

const clickSetting = async () => {
  if (!props.rowData.value) {
    ElMessage({
      message: '请选择一行数据',
      type: 'warning',
    });
    return;
  }
  const data = props.rowData;
  settingVisible.value = true;
  try {
    let api = new ModellingServiceApi();
    let res = await api.getUaServiceSettings({
      serviceId: data.value.id,
    });
    settingData.value = res?.servers[0];
    console.log(settingData, 'settingData');
    form.adress = res?.servers[0]?.configure?.netAddr;
    form.port = res?.servers[0]?.configure?.port;
  } catch (error) {
    console.log(error, 'error');
  }
};
// 动态导入组件，可以是任何Vue组件
const DynamicComponent = defineAsyncComponent(() => import('../Nav/index.vue'));

const form = reactive({
  name: '',
  desc: '',
  adress: '',
  port: '',
});

const formRef = ref(null);

const rules = {
  name: [
    { required: true, message: '请输入名称', trigger: 'blur' },
    // { pattern: /^[\u4e00-\u9fa5]+$/, message: '只能输入汉字', trigger: 'blur' }
    // { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
  ],
  desc: [
    { required: false, message: '请输入描述', trigger: 'blur' },
    // { pattern: /^[\u4e00-\u9fa5]+$/, message: '只能输入汉字', trigger: 'blur' }
    // { min: 3, max: 10, message: '描述长度在 3 到 10 个字符', trigger: 'blur' }
  ],
  adress: [
    { required: true, message: '请输入地址', trigger: 'blur' },
    {
      pattern:
        /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/,
      message: '地址格式不正确',
      trigger: 'blur',
    },
  ],
  port: [
    { required: true, message: '请输入端口', trigger: 'blur' },
    {
      pattern:
        /^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$/,
      message: '端口格式不正确',
      trigger: 'blur',
    },
  ],
};

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      emit('submitData', form);
      dialogVisible.value = false;
      form.name = '';
      form.desc = '';
      // ElMessage.success('提交成功');
    } else {
      dialogVisible.value = false;
      // ElMessage.error('表单验证失败');
      return false;
    }
  });
};
const submitTwoForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      emit('submitTwoData', form);
      dialogTwoVisible.value = false;
      form.name = '';
      form.desc = '';
      // ElMessage.success('提交成功');
    } else {
      dialogTwoVisible.value = false;
      // ElMessage.error('表单验证失败');
      return false;
    }
  });
};
const submitPort = async () => {
  if (!props.rowData.value) {
    ElMessage({
      message: '请选择一行数据',
      type: 'warning',
    });
    return;
  }
  const data = props.rowData;
  try {
    let api = new ModellingServiceApi();
    let port = Number(form.port);

    await api.setUaServer({
      serverId: settingData.value?.id,
      configure: {
        netAddr: form.adress,
        port: port,
      },
    });

    ElMessage.success('提交成功');
  } catch (error) {
    ElMessage.success('提交失败');
    console.log(error, 'error');
  }
  settingVisible.value = false;
};
const deleteRow = async () => {
  if (!props.rowData.value) {
    ElMessage({
      message: '请选择一行数据',
      type: 'warning',
    });
    return;
  }
  const data = props.rowData;
  try {
    let api = new ModellingServiceApi();
    await api.deleteUaService({ serviceId: data.value.id });
    // await getTableList()
    ElMessage.success('删除成功');
    emit('refreshTableData');
  } catch (error) {
    ElMessage.success('删除失败');
    console.log(error, 'error');
  }
};
const deleteRowNode = async () => {
  if (!props.rowData.value) {
    ElMessage({
      message: '请选择一行数据',
      type: 'warning',
    });
    return;
  }
  const data = props.rowData;
  console.log(data, 'data');
  try {
    let api = new ModellingServiceApi();
    await api.deleteNode({ sid: 0, nodeId: data.value.nodeId });
    ElMessage.success('删除成功');
    emit('refreshTableDataTwo');
  } catch (error) {
    ElMessage.success('删除失败');
    console.log(error, 'error');
  }
};
</script>

<style scoped>
.top-box {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .left-select {
    display: flex;
    align-items: center;

    .label {
      padding-right: 10px;
    }
  }

  .right-icon {
    width: 200px;
    display: flex;
    align-items: center;
    justify-content: space-around;
  }
}

.welcome {
  display: inline-block;
  color: rgb(166, 153, 153);
  font-size: 18px;
}

.left {
  text-align: left;
}

.right {
  text-align: right;
}

.header_toolbars {
  line-height: 64px;
  height: 64px;
}

.toolbar_left {
  text-align: left;
}

.toolbar_right {
  text-align: right;
}

.setting:hover {
  background-color: rgb(246, 246, 246);
  cursor: pointer;
}

.tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.closeMenu {
  cursor: pointer;
  margin-right: 10px;
  height: 64px;
  width: 50px;
}

.closeMenu:hover {
  background-color: rgb(242, 242, 242);
}

.right-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-self: flex-end;
  margin-left: 400px;
}
</style>
