<template>
  <div class="main-container">
    <div class="left-section">
      <div class="table-container">
        <el-tree 
          ref="eltreeTop" 
          :props="{
            children: 'children',
            label: 'label',
            hasChildren: 'hasChildren',
          }" 
          :data="state.templateTreeData"
          @node-click="handleNodeClick"
          :accordion="false" 
          :highlight-current="true" 
          empty-text="无数据" 
           node-key="NodeId"
          style="height: 150px; overflow-y: scroll">
          <template class="custom-tree-node" #default="{ node, data }">
            <p class="node-label" @dblclick="() => handleNodeDblClick(node, data)">
              <el-dropdown
                v-if="node.level != 1"
                trigger="contextmenu"
                :ref="'dropdown' + (data.nodeIdNum || data.nodeId || data.id)"
                @visible-change="(visible) => handleVisibleChange(node, visible)"
              >
              <p class="node-label-p">
                <el-icon v-if="node.level == 1" class="cus-icons">
                  <FolderOpened />
                </el-icon>
                <el-icon class="cus-icons-connect" v-else-if="connectFlag"><Connection /></el-icon>
                <el-icon class="cus-icons-connect" v-else="!connectFlag"><SwitchButton /></el-icon>
                <span class="cus-label">
                  {{ data.BrowseName }}
                </span>
              </p>
                <template #dropdown v-if="currentDrop === node.id">
                  <el-dropdown-menu>
                    <!-- <el-dropdown-item :disabled="connectFlag" @click="handleBottomTree">
                      Connect
                    </el-dropdown-item>
                    <el-dropdown-item :disabled="!connectFlag" @click="handleBottomFlag" >
                      Disconnect
                    </el-dropdown-item> -->
                    <el-dropdown-item   @click="handleBottomDelete(data)" >
                      Delete
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <!-- 当node.level == 1时，直接显示内容，不使用下拉框 -->
              <p v-else>
                <el-icon v-if="node.level == 1" class="cus-icons">
                  <FolderOpened />
                </el-icon>
                <!-- <el-icon v-else><Link /></el-icon> -->
                <el-icon class="cus-icons-connect" v-else-if="connectFlag"><Connection /></el-icon>
                <el-icon class="cus-icons-connect" v-else="!connectFlag"><SwitchButton /></el-icon>
                <el-dropdown 
                  ref="addDropdownRef"
                  @command="(command) => handleAddCommand(command, node, data)" 
                  trigger="manual"
                  placement="bottom-start"
                >
                  <span class="cus-label" @contextmenu="showAddDropdown">
                  {{ data.BrowseName }}
                </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="add"  >Add</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </p>
            </p>
          </template>
        </el-tree>
      </div>
      <i class="el-icon-delete" icon="el-icon-folder"></i>
      <div class="tree-container">
      <el-tree
      ref="eltree"
      :props="{
        children: 'children',
        label: 'label',
        hasChildren: 'hasChildren',
        key: 'nodeIdNum',
      }"
      :lazy="true"
      :load="loadNode"
      @node-click="handleNodeClick2"
      :expand-on-click-node="false"
      :accordion="false"
      :highlight-current="true"
      @node-collapse="onNodeCollapse"
      @node-contextmenu="handleContextMenuAll"
      :data="state.bottomTreeData"
      empty-text="无数据"
      node-key="nodeIdNum"
      :virtual-scrolling="true"
      :virtual-scroll-item-height="32"
      :virtual-scroll-buffer-size="10"
      style="  overflow-y: auto; overflow-x: hidden;"
      class="optimized-tree">
            <template class="custom-tree-node" #default="{ node, data }">
                      <p class="node-label" @dblclick="() => handleNodeDblClick(node, data)">
                        <el-dropdown
                          trigger="contextmenu"
                          :ref="'dropdown' + (data.nodeIdNum || data.nodeId || data.id)"
                          @visible-change="(visible) => handleVisibleChange(node, visible)"
                        >
                        <p>
                          <i :class="getNodeIcon(data.nodeClass)"></i>
                          <span class="node-label cus-label">
                            {{ data.label}}
                          </span>
                        </p>
                        <template #dropdown v-if="currentDrop === node.id && Number(data.nodeClass) === 4">
                            <el-dropdown-menu>
                              <el-dropdown-item 
                                :disabled="isNewProject" 
                                @click="handleMethodCall(data)">
                                <!-- {{ data }} -->
                                Call
                              </el-dropdown-item>
                            </el-dropdown-menu>
                          </template>
                          <template #dropdown v-if="currentDrop === node.id && Number(data.nodeClass) === 4">
                            <el-dropdown-menu>
                              <el-dropdown-item 
                                :disabled="isNewProject" 
                                @click="handleMethodCall(data)"> 
                                Call
                              </el-dropdown-item>
                            </el-dropdown-menu>
                          </template>
                          <template #dropdown v-else>
                            <el-dropdown-menu>
                              <el-dropdown-item 
                                :disabled="isNewProject" 
                                @click="handleVariables(node,data)">
                                  Variables
                              </el-dropdown-item>
                            </el-dropdown-menu>
                          </template>
                        </el-dropdown>
                      </p>
                    </template>
      </el-tree>
      </div>
    </div>
    <div class="center-section">
      <div class="content-container">
        
        <el-tabs
          v-model="activeFolder"
          type="card"
          @tab-click="handleMenuClick"
        >
          <el-tab-pane
            v-for="(tab, index) in state.nodeDetailsData"
            :key="tab.key"
            :label="tab.value"
            :name="tab.key"
            class="adaptive-tab-pane"
          >
            <div v-if="activeFolder == tab.key">
              <div v-if="tab.type === 'Variables'" class="properties-panel">
                <div class="panel-header">
                  <span class="header-name">Name</span>
                  <span class="header-type">DataType</span>
                  <span class="header-value">Value</span>
                  
                </div>
                <!-- Loading 状态 -->
                <div v-if="state.loadingVariables" class="loading-overlay-table">
                  <div class="loading-content">
                    <div class="loading-spinner">
                      <el-icon class="loading-icon"><Loading /></el-icon>
                    </div>
                    <div class="loading-text">正在加载变量数据...</div>
                    <div class="loading-dots">
                      <span></span>
                      <span></span>
                      <span></span>
                    </div>
                  </div>
                </div>
                <!-- 空数据提示 -->
                
                <!-- 数据列表 -->
                <div v-else class="table-boxs smooth-scroll" @contextmenu="onCtx"  >
                  <div v-for="(item, index) in state.mergedNodeData" :key="item.nodeId" label-width="100px">
                    <div 
                      class="property-row bas-car" 
                      :data-node-id="item.nodeId" 
                      :class="{
                        'expanded': item._showCard,
                        'selected-row': item._isSelected
                      }"
                      @dblclick="handleRowDblClick(item, index)"
                    >
                      <div class="property-name property-name-col">
                        <div class="property-name-main">
                            <span class="property-name-main-text">{{ item.name }}</span>
                            <!-- 展开/收起按钮 -->
                            <!-- <span class="expand-button">
                              <span v-if="item._isLoading">
                                <el-icon class="is-loading"><Loading /></el-icon>
                              </span>
                              <span v-else-if="!item._showCard" @click="getDetialCard(item)">
                              <el-icon><ArrowDown /></el-icon>
                            </span>
                            <span v-else="item._showCard" @click="item._showCard = !item._showCard">
                              <el-icon><ArrowUp /></el-icon>
                            </span>
                          </span> -->
                        </div>
                      </div>
                      <div class="property-type property-name-col">
                        <div class="property-name-main">
                          {{ item.dataType }}
                         </div>
                      </div>
                      <div class="property-value">
                        <!-- 展示模式：显示当前值 -->
                        <div 
                          v-if="!item._isEditing" 
                          class="value-display"
                        >
                          <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                          <div class="value-actions">
                            <el-icon 
                              @click="startEditValue(item)"

                              class="access-status-icon" 
                              :style="{ color: getAccessStatus(item.UserWriteMask).color }"
                              :title="getAccessStatus(item.UserWriteMask).text"
                            >
                              <span v-if="getAccessStatus(item.UserWriteMask).icon === 'Lock'" />
                              <EditPen v-else-if="getAccessStatus(item.UserWriteMask).icon === 'EditPen'" />
                              <Edit v-else-if="getAccessStatus(item.UserWriteMask).icon === 'Edit'" />
                              <QuestionFilled v-else />
                            </el-icon>
                          </div>
                        </div>

                    <!-- Boolean 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Boolean' && item._isEditing"
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                      </div>

                    <!-- DateTime 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'DateTime' && item._isEditing" 
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                        </div>
                    <!-- Enumeration 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Enumeration' && item._isEditing"
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- Structure 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Structure' && item._isEditing"
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                          </div>
                    <!-- String 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'String' && item._isEditing" 
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                        </div>

                    <!-- UInteger 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'UInteger' && item._isEditing" 
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- Integer 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Integer' && item._isEditing" 
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- Double 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Double' && item._isEditing"
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- NodeId 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'NodeId' && item._isEditing" 
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- None 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'None' && item._isEditing" 
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- 其他类型改为弹窗编辑 -->
                    <div 
                      v-else-if="
                        item.dataTypes != 'DateTime' &&
                        item.dataTypes != 'Time' &&
                        item.dataTypes != 'Boolean' &&
                        item.dataTypes != 'Enumeration' &&
                        item.dataTypes != 'Structure' &&
                        item.dataTypes != 'UInteger' &&
                        item.dataTypes != 'Integer' &&
                        item.dataTypes != 'String' &&
                        item.dataTypes != 'Double' &&
                        item.dataTypes != 'NodeId' &&
                        item.dataTypes != 'None' &&
                        item._isEditing
                      "
                      class="value-display"
                      @click="startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon class="edit-hint"><Edit /></el-icon>
                      </div>
                    <el-card class="bas-car" v-if="item.specific">
                      <div class="tit-box">
                        <div class="bas-label">{{ item.name }}</div>
                      </div>
                      <div class="spe-bas-inp">
                        <el-form-item class="spec-inp" :prop="item.nodeId">
                          <el-input
                            :model-value="getVariableDisplayValue(item)"
                            autocomplete="off"
                            :placeholder="getInputPlaceholder(item.dataTypes)"
                            @input="(value) => handleVariableInputChange(item, value)"
                            @keydown="(event) => handleVariableKeyDown(event, item.dataTypes)"
                            @paste="(event) => handleVariablePaste(event, item.dataTypes)"
                            :ref="`input-${item.nodeId}`"
                          ></el-input>
                        </el-form-item>
                        <el-select
                          v-model="item._editValue"
                          placeholder="请输入类型"
                          @change="onchangeRight($event, item.nodeId)"
                        >
                          <el-option
                            v-for="item in item?.enumStrings"
                            :key="item"
                            :label="item"
                            :value="item"
                          >
                            <span style="float: left">{{ item }}</span>
                          </el-option>
                        </el-select>
                      </div>
                    </el-card>
                      </div>
                      <!-- <el-button
                        ref="childBtn"
                        type="primary"
                        v-if="newBtn"
                        :style="styleObject"
                        class="new-btn"
                        @click="detailsMessage"
                      >
                        详细信息
                      </el-button> -->
                      
                    </div>
                   
                    <!-- 子表显示区域 -->
                    <transition name="fade" v-if="item._showCard && item?._hasChildren && item?._hasChildren.length > 0">
                      <div class="sub-table-wrapper">
                        <!-- <div class="sub-table-header">
                          <span class="sub-table-title">子节点详情 ({{ item._hasChildren.length }} 项)</span>
                        </div> -->
                        <div class="sub-table-container smooth-scroll">
                          <el-table 
                            :data="item._hasChildren" 
                            style="width: 100%"
                            size="small"
                            border
                            stripe
                            :max-height="300"
                          >
                            <el-table-column 
                              prop="browseName" 
                              label="BrowseName" 
                              width="150"
                              show-overflow-tooltip
                            />
                            <el-table-column 
                              prop="nodeIdNum" 
                              label="nodeId" 
                              width="200"
                              show-overflow-tooltip
                            />
                            <el-table-column 
                              prop="changeNodeClass" 
                              label="NodeClass" 
                              width="120"
                              show-overflow-tooltip
                            />
                             
                          </el-table>
                        </div>
                      </div>
                    </transition>
                   
                  </div>
                </div>
              </div>
              <div v-else-if="tab.type === 'details'" class="details-panel">
                <div class="panel-header">
                  <span>节点详细信息</span>
                </div>
                <div class="details-content">
                  <div v-for="(detail, index) in state.nodeDetails" :key="index" class="detail-item">
                    <div class="detail-name" @click="detail._expanded = !detail._expanded">
                      {{ detail.name }}
                      <span class="expand-icon">{{ detail._expanded ? '▼' : '▶' }}</span>
                    </div>
                    <div v-if="detail._expanded" class="detail-children">
                      <div v-for="(child, childIndex) in detail.children" :key="childIndex" class="detail-child">
                        <div class="child-name">{{ child.name }}</div>
                        <div class="child-value">{{ child.value }}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else-if="tab.type === 'methods'" class="methods-panel">
                <div class="panel-header">
                  <span>可用方法</span>
                </div>
                <div class="methods-content">
                  <p>方法列表将在这里显示</p>
                </div>
              </div>
              <div v-else-if="tab.type === 'variables'" class="variables-panel">
                <div class="panel-header">
                  <span>变量列表</span>
                </div>
                <div class="variables-content">
                  <p>变量列表将在这里显示</p>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
        
      </div>
    </div>
    <div class="right-section">
      <!-- Node Info Panel -->
      <div class="info-panel">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>Node Information</span>
            </div>
          </template>
          <div class="  new-box">
             
            <div class="desc-wrapper">
              <div
                v-for="(value, key) in nodeDetailsData.value"
                :key="key"
                class="desc-row"
              >
                <!-- label -->
                <div class="desc-label" style="white-space: nowrap; min-width: max-content; font-weight: bold; margin-right: 10px;">
                  {{ key }}
                </div>

                <!-- content -->
                <div class="desc-content">
                  <el-dropdown 
                    v-if="key === 'displayName'" 
                    @command="handleCopyCommand"
                    trigger="contextmenu"
                    placement="bottom-start">
                    <p class="text-ellipsis">{{ value.text }}</p>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item :command="{ text: value.text, key: key }">
                          <el-icon><DocumentCopy /></el-icon>
                          复制
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                  <el-dropdown 
                    v-else 
                    @command="handleCopyCommand"
                    trigger="contextmenu"
                    placement="bottom-start">
                    <p class="text-ellipsis">{{ value }}</p>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item :command="{ text: value, key: key }">
                          <el-icon><DocumentCopy /></el-icon>
                          复制
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </div>
               
          </div>
        </el-card>
      </div>

       
    </div>
    <el-dialog v-model="urlDialogVisible" :title="isEditingConnection ? '创建连接' : '添加URL地址'" width="500px"
      destroy-on-close class="el-dialog-self">
      <!-- URL Form -->
      <div v-if="isEditingConnection">
        <el-form ref="urlRef" :model="urlForm" :rules="rules" label-width="100px">
          <el-form-item label="名称" required prop="urlName">
            <el-input  v-model="urlForm.urlName"   placeholder="请输入名称" />
          </el-form-item>
          <el-form-item label="URL地址" required  prop="url">
            <el-input v-model="urlForm.url" placeholder="请输入URL地址" />
          </el-form-item>
          
        </el-form>
      </div>

      <!-- Connection Form -->
      <div v-else>
        <el-form ref="urlRef" :model="connectionForm" label-width="100px">
          <el-form-item label="选择URL" required>
            <el-select v-model="connectionForm.selectedUrl" placeholder="请选择URL">
              <el-option v-for="url in availableUrls" :key="url.id" :label="url.description || url.url"
                :value="url.url" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="urlDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="FolderDialogVisible" :title="'Add Document'" width="500px"
      destroy-on-close class="el-dialog-self">
      <div >
        <el-form :model="connectionForm" label-width="100px">
          <el-form-item label="Add Document" >
            <el-select v-model="connectionForm.selectedUrl" placeholder="请选择">
              <el-option v-for="url in availableUrls" :key="url.id" :label="url.description || url.url"
                :value="url.url" />
            </el-select>
          
          </el-form-item>
          
         
          
        </el-form>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>Description</span>
            </div>
            <div   class="text item">
              {{"This document type is a simple data logger that subscribes for value changes of a given Node and logs the information into a comma separated file."}}
            </div>
          </el-card>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="FolderDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleDialogFolder">确定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="PropertyDialogVisible" :title="'Server Settings   -' +'   ' + urlForm.urlName" width="500px"
      destroy-on-close class="el-dialog-self">
      <div >
        <el-form ref="urlRef" :model="urlForm" :rules="rules"  label-width="100px">
          <el-form-item label="configName" prop="urlName" >
            <el-input v-model="urlForm.urlName"/>
          </el-form-item>
          <el-form-item label="endpointUrl" prop="url">
            <el-input v-model="urlForm.url"/>
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="PropertyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleChangeFormValue">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="showBooleanFlag" title="Edit Value" width="500px"
      destroy-on-close class="el-dialog-self">
      
      <div class="boolean-checkbox-container">
        <div v-for="(value, index) in showBooleanData" :key="index" class="checkbox-item">
          <el-checkbox 
            v-model="showBooleanData[index]" 
            :label="` ${index + 1}`"
            @change="handleBooleanChange(index, $event)"
          >
            {{ ` ${index + 1}` }}
          </el-checkbox>
        </div>
      </div>
       
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showBooleanFlag = false">取消</el-button>
          <el-button type="primary" @click="handleBooleanDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="showDoubleFlag" :title="getDialogTitle()" width="500px"
      destroy-on-close class="el-dialog-self">
      
      <div class="double-input-container">
        <div v-for="(value, index) in showDoubleData" :key="index" class="input-item">
          <el-form-item :label="`Value ${index + 1}`" :rules="getValidationRules()">
             
             <el-input
              v-if="isDateTimeType()"
              v-model="showDoubleData[index]"
              type="datetime"
              placeholder="请输入时间"
              style="width: 100%"
              @input="(value) => handleDoubleChange(index, value)"
            /> 
            <!-- String 类型使用文本输入框 -->
            <el-input
              v-else-if="isStringType()"
              v-model="showDoubleData[index]"
              placeholder="请输入文本"
              @input="(value) => handleDoubleChange(index, value)"
              style="width: 100%"
            ></el-input>
            <!-- 数字类型使用数字输入框 -->
            <el-input
              v-else
              v-model="showDoubleData[index]"
              :placeholder="getNumberPlaceholder()"
              @input="(value) => handleDoubleChange(index, value)"
              @keypress="isNumber"
              @keydown="isNumber"
              @paste="handleNumberPaste"
              style="width: 100%"
            ></el-input>
          </el-form-item>
        </div>
      </div>
       
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDoubleFlag = false">取消</el-button>
          <el-button type="primary" @click="handleDoubleDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    

    <el-dialog v-model="showUIntegerFlag" title="Edit UInteger Values" width="500px"
      destroy-on-close class="el-dialog-self">
      
      <div class="uinteger-input-container">
        <div v-for="(value, index) in showUIntegerData" :key="index" class="input-item">
          <el-form-item :label="`Value ${index + 1}`">
            <el-input
              v-model="showUIntegerData[index]"
              placeholder="请输入无符号整数"
              @input="(value) => handleUIntegerChange(index, value)"
              @keypress="isNumber"
              type="number"
              min="0"
            ></el-input>
          </el-form-item>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showUIntegerFlag = false">取消</el-button>
          <el-button type="primary" @click="handleUIntegerDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="showCarExtrasFlag" title="Edit CarExtras" width="500px"
      destroy-on-close class="el-dialog-self">
      
      <div class="carextras-input-container">
        <div v-for="(item, index) in showCarExtrasData" :key="index" class="carextras-item">
          <div class="carextras-row">
            <div class="carextras-name">
              <el-checkbox 
                v-model="item.selected"
                @change="handleCarExtrasSelectionChange(index)"
              ></el-checkbox>
              <span class="name-label">{{ item.name }}</span>
            </div>
            <div class="carextras-value">
              <el-checkbox 
                v-model="item.value"
                :disabled="!item.selected"
                @change="handleCarExtrasValueChange(index)"
              ></el-checkbox>
              <span class="value-label">{{ item.value ? 'true' : 'false' }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCarExtrasFlag = false">取消</el-button>
          <el-button type="primary" @click="handleCarExtrasDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="showUInt64Flag" title="Edit UInt64 OptionSet" width="600px"
      destroy-on-close class="el-dialog-self">
      
      <div class="uint64-input-container">
        <div class="uint64-header">
          <span class="header-name">Name</span>
          <span class="header-value">Value</span>
        </div>
        <div class="uint64-list">
          <div v-for="(item, index) in showUInt64Data" :key="index" class="uint64-item">
            <div class="uint64-name">{{ item.name }}</div>
            <div class="uint64-value">
              <el-checkbox 
                v-model="item.value"
                @change="handleUInt64ValueChange(index)"
              ></el-checkbox>
              <span class="value-label">{{ item.value ? 'true' : 'false' }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showUInt64Flag = false">取消</el-button>
          <el-button type="primary" @click="handleUInt64DialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="showByteStringFlag" title="Edit ByteString Values" width="600px"
      destroy-on-close class="el-dialog-self">
      
      <div class="bytestring-input-container">
        <div v-for="(value, index) in showByteStringData" :key="index" class="input-item">
          <el-form-item :label="`Value ${index + 1}`">
            <el-input
              v-model="showByteStringData[index]"
              placeholder="请输入十六进制字符串，如: 00 10 20 30"
              @input="(value) => { handleByteStringChange(index, value); validateHexInput(index, value); }"
              style="width: 100%"
              type="textarea"
              :rows="2"
            ></el-input>
          </el-form-item>
        </div>
      </div>
       
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showByteStringFlag = false">取消</el-button>
          <el-button type="primary" @click="handleByteStringDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="showExpandedNodeIdFlag" title="Edit ExpandedNodeId Values" width="800px"
      destroy-on-close class="el-dialog-self">
      
      <div class="expandednodeid-input-container">
        <div v-for="(value, index) in showExpandedNodeIdData" :key="index" class="expandednodeid-item">
          <div class="expandednodeid-header">
            <span class="expandednodeid-label">Value {{ index + 1 }}</span>
          </div>
          <div class="expandednodeid-fields">
            <div class="field-row">
              <div class="field-group">
                <label class="field-label">IdentifierType</label>
                <el-select
                  v-model="value.identifierType"
                  placeholder="请选择 IdentifierType"
                  @change="(val) => handleExpandedNodeIdChange(index, 'identifierType', val)"
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
                  v-model="value.nsIndex"
                  placeholder="请输入 NsIndex"
                  @input="(val) => handleExpandedNodeIdChange(index, 'nsIndex', val)"
                  style="width: 100%"
                ></el-input>
              </div>
            </div>
            <div class="field-row">
              <div class="field-group">
                <label class="field-label">Value</label>
                <el-input
                  v-model="value.value"
                  placeholder="请输入 Value"
                  @input="(val) => handleExpandedNodeIdChange(index, 'value', val)"
                  style="width: 100%"
                ></el-input>
              </div>
              <div class="field-group">
                <label class="field-label">ServerIndex</label>
                <el-input
                  v-model="value.serverIndex"
                  placeholder="请输入 ServerIndex"
                  @input="(val) => handleExpandedNodeIdChange(index, 'serverIndex', val)"
                  style="width: 100%"
                ></el-input>
              </div>
            </div>
            <div class="field-row">
              <div class="field-group full-width">
                <label class="field-label">NamespaceUri</label>
                <el-input
                  v-model="value.namespaceUri"
                  placeholder="请输入 NamespaceUri"
                  @input="(val) => handleExpandedNodeIdChange(index, 'namespaceUri', val)"
                  style="width: 100%"
                ></el-input>
              </div>
            </div>
          </div>
        </div>
      </div>
       
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showExpandedNodeIdFlag = false">取消</el-button>
          <el-button type="primary" @click="handleExpandedNodeIdDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="showImagePNGFlag" title="Edit ImagePNG Values" width="800px"
      destroy-on-close class="el-dialog-self">
      
      <div class="imagepng-input-container">
        <div v-for="(value, index) in showImagePNGData" :key="index" class="imagepng-item">
          <el-form-item :label="`十六进制 PNG ${index + 1}`">
            <el-input
              v-model="showImagePNGData[index]"
              placeholder="请输入连续的十六进制字符串（不含空格），如：89504E47..."
              @input="(val) => handleImagePNGChange(index, val)"
              type="textarea"
              :rows="3"
              style="width: 100%"
            ></el-input>
          </el-form-item>
  </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showImagePNGFlag = false">取消</el-button>
          <el-button type="primary" @click="handleImagePNGDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="showByteFlag" title="Edit Byte Values" width="400px"
      destroy-on-close class="el-dialog-self">
      
      <div class="byte-input-container">
        <div class="byte-bits">
          <div class="bit-row">
            <div class="bit-label">ERROR</div>
            <div class="bit-checkbox">
              <el-checkbox v-model="showByteData.bits[0]" @change="handleByteBitChange"></el-checkbox>
            </div>
          </div>
          <div class="bit-row">
            <div class="bit-label">WARNING</div>
            <div class="bit-checkbox">
              <el-checkbox v-model="showByteData.bits[1]" @change="handleByteBitChange"></el-checkbox>
            </div>
          </div>
          <div class="bit-row">
            <div class="bit-label">SYSTEM</div>
            <div class="bit-checkbox">
              <el-checkbox v-model="showByteData.bits[2]" @change="handleByteBitChange"></el-checkbox>
            </div>
          </div>
          <div class="bit-row">
            <div class="bit-label">INFO</div>
            <div class="bit-checkbox">
              <el-checkbox v-model="showByteData.bits[3]" @change="handleByteBitChange"></el-checkbox>
            </div>
          </div>
          <div class="bit-row">
            <div class="bit-label">DEBUG</div>
            <div class="bit-checkbox">
              <el-checkbox v-model="showByteData.bits[4]" @change="handleByteBitChange"></el-checkbox>
            </div>
          </div>
          <div class="bit-row">
            <div class="bit-label">CONTENT</div>
            <div class="bit-checkbox">
              <el-checkbox v-model="showByteData.bits[5]" @change="handleByteBitChange"></el-checkbox>
            </div>
          </div>
           
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showByteFlag = false">取消</el-button>
          <el-button type="primary" @click="handleByteDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Method Call Dialog -->
    <el-dialog v-model="showMethodCallFlag" :title="methodCallTitle" width="800px"
      destroy-on-close class="el-dialog-self">
      
      <div class="method-call-container">
        <!-- Input Arguments -->
        <div class="method-section">
          <div class="section-header">Input Arguments</div>
          <div class="method-table">
            <div class="table-header">
              <div class="header-name">Name</div>
              <div class="header-value">Value</div>
              <div class="header-datatype">DataType</div>
              <div class="header-description">Description</div>
            </div>
            <div v-for="(arg, index) in methodInputArgs" :key="'input-' + index" class="table-row">
              <div class="row-name">{{ arg.name }}</div>
              <div class="row-value">
                <!-- Boolean 类型 -->
                <el-select 
                  v-if="arg.dataType === 'Boolean'"
                  v-model="arg.value" 
                  placeholder="Select Boolean value"
                  style="width: 100%">
                  <el-option label="true" value="true"></el-option>
                  <el-option label="false" value="false"></el-option>
                </el-select>
                
                <!-- 枚举类型 (有选项的情况) -->
                <el-select 
                  v-else-if="arg.options && arg.options.length > 0"
                  v-model="arg.value" 
                  :placeholder="'Select ' + arg.dataType"
                  style="width: 100%">
                  <el-option
                    v-for="option in arg.options"
                    :key="option.value"
                    :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
                
                <!-- Enumeration 类型 -->
                <el-input 
                  v-else-if="arg.dataType === 'Enumeration'"
                  v-model="arg.value" 
                  placeholder="Enter enumeration value (number or name)"
                  @input="validateEnumerationInput(arg, $event)"
                  @keypress="preventInvalidEnumerationInput($event)"
                  style="width: 100%">
                </el-input>
                
                <!-- 数字类型 -->
                <el-input
                  v-else-if="isNumericType(arg.dataType)"
                  v-model="arg.value"
                  :placeholder="'Enter ' + arg.dataType"
                  @input="validateNumericInput(arg, $event)"
                  @keypress="preventNonNumericInput(arg, $event)"
                  style="width: 100%">
                </el-input>
                
                <!-- DateTime 类型 -->
                <el-date-picker
                  v-else-if="arg.dataType === 'DateTime'"
                  v-model="arg.value"
                  type="datetime"
                  placeholder="Select date and time"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  style="width: 100%">
                </el-date-picker>
                
                <!-- NodeId 类型 -->
                <el-input 
                  v-else-if="arg.dataType === 'NodeId'"
                  v-model="arg.value" 
                  placeholder="Enter NodeId (e.g., ns=2;i=123)"
                  @input="validateNodeIdInput(arg, $event)"
                  @keypress="preventInvalidNodeIdInput($event)"
                  style="width: 100%">
                </el-input>
                
                <!-- ByteString 类型 -->
                <el-input 
                  v-else-if="arg.dataType === 'ByteString'"
                  v-model="arg.value" 
                  placeholder="Enter hex string (e.g., 48656C6C6F)"
                  @input="validateByteStringInput(arg, $event)"
                  @keypress="preventInvalidHexInput($event)"
                  style="width: 100%">
                  <template #suffix>
                    <el-button 
                      size="small" 
                      @click="handleMethodArgClick(arg, 'input', index)"
                      type="primary">
                      Edit
                    </el-button>
                  </template>
                </el-input>
                
                <!-- OptionSet 类型 -->
                <el-input 
                  v-else-if="arg.dataType && arg.dataType.includes('OptionSet')"
                  v-model="arg.value" 
                  placeholder="Click to edit OptionSet"
                  readonly
                  @click="handleMethodArgClick(arg, 'input', index)"
                  style="width: 100%">
                  <template #suffix>
                    <el-button 
                      size="small" 
                      @click="handleMethodArgClick(arg, 'input', index)"
                      type="primary">
                      Edit
                    </el-button>
                  </template>
                </el-input>
                
                <!-- 默认文本输入 -->
                <el-input 
                  v-else
                  v-model="arg.value" 
                  :placeholder="'Enter ' + arg.dataType"
                  @input="validateStringInput(arg, $event)"
                  @keypress="preventInvalidStringInput(arg, $event)"
                  @click="handleMethodArgClick(arg, 'input', index)">
                </el-input>
              </div>
              <div class="row-datatype">{{ arg.dataType }}</div>
              <div class="row-description">{{ arg.description || '' }}</div>
            </div>
          </div>
        </div>

        <!-- Output Arguments -->
        <div class="method-section">
          <div class="section-header">Output Arguments</div>
          <div class="method-table">
            <div class="table-header">
              <div class="header-name">Name</div>
              <div class="header-value">Value</div>
              <div class="header-datatype">DataType</div>
              <div class="header-description">Description</div>
            </div>
            <div v-for="(arg, index) in methodOutputArgs" :key="'output-' + index" class="table-row">
              <div class="row-name">{{ arg.name }}</div>
              <div class="row-value">
                <!-- Enumeration 类型显示下拉选项 -->
                <el-select 
                  v-if="arg.dataType === 'Enumeration'"
                  v-model="arg.value " 
                  :placeholder="'Select ' + arg.dataType"
                  style="width: 100%"
                  disabled 
                  >
                  <el-option
                    v-for="option in getEnumerationOptions(arg.options)"
                    :key="option.value"
                    :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
                
                <!-- 其他类型显示普通输入框 -->
                <el-input 
                  v-else
                  v-model="arg.value" 
                  :placeholder="'Enter ' + arg.dataType"
                  readonly>
                </el-input>
              </div>
              <div class="row-datatype">{{ arg.dataType }}</div>
              <div class="row-description">{{ arg.description || '' }}</div>
            </div>
          </div>
        </div>

        <!-- Result -->
        <div class="method-section">
          <div class="section-header">Result</div>
          <div class="method-result">
            <el-input 
              v-model="methodResult" 
              type="textarea" 
              :rows="4" 
              placeholder="Method call result will appear here..."
              readonly>
            </el-input>
          </div>
        </div>
      </div>
       
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showMethodCallFlag = false">Close</el-button>
          <el-button type="primary" @click="handleMethodCallExecute($event,methodInputArgs)">Call</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- OptionSet Dialog for Method Arguments -->
    <el-dialog v-model="showMethodOptionSetFlag" title="Edit OptionSet" width="600px"
      destroy-on-close class="el-dialog-self">
      
      <div class="method-optionset-container">
        <div class="optionset-header">
          <div class="header-name">Name</div>
          <div class="header-value">Value</div>
        </div>
        <div class="optionset-list">
          <div v-for="(option, index) in methodOptionSetData" :key="index" class="optionset-item">
            <div class="optionset-name">
              <el-checkbox 
                v-model="option.selected" 
                @change="handleOptionSetSelectionChange(index)">
                {{ option.name }}
              </el-checkbox>
            </div>
            <div class="optionset-value">
              <el-checkbox 
                v-model="option.value" 
                @change="handleOptionSetValueChange(index)">
                {{ option.value ? 'True' : 'False' }}
              </el-checkbox>
            </div>
          </div>
        </div>
        <div class="optionset-value-display">
          <span>Current Value: {{ currentOptionSetValue }}</span>
        </div>
      </div>
       
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showMethodOptionSetFlag = false">取消</el-button>
          <el-button type="primary" @click="handleMethodOptionSetConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>

  <!-- Add 弹窗 -->
  <el-dialog
    v-model="addDialogVisible"
    title="添加新项目"
    width="500px"
    :before-close="handleAddDialogClose"
  >
    <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
      <el-form-item label="类型" prop="type">
        <el-select v-model="addForm.type" placeholder="请选择类型" style="width: 100%">
          <el-option label="变量" value="variable"></el-option>
          <el-option label="方法" value="method"></el-option>
          <el-option label="对象" value="object"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input v-model="addForm.name" placeholder="请输入名称"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
          v-model="addForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入描述"
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddConfirm">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import './index.css';
import { getBrowseData, getBrowseNextData, getOpcuaData } from '@/api/index.js';
import { onMounted, reactive, ref,computed, watch,nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessageBox, ElDialog, ElButton, ElMessage, stepProps } from 'element-plus';
import { Configuration, NodeClass } from "opcua-webapi";
import { Document, CircleCheck, ArrowDown, Edit, Check, Close, DocumentCopy, Loading, Lock, EditPen, QuestionFilled } from '@element-plus/icons-vue';
 
import { 
  UaWebClient,
  UaDataTypeDictionary,
  UaNodeIdType,
  UaNodeId,
  UaClientConfiguration,
  UaExpandedNodeId,
  UaVariant,
  UaVariantType,
  UaLocalizedText,
  makeUaStatusCode,
  DataTypeIds,
  ObjectIds,
  ObjectTypeIds,
  
}  
from 'opcua-webclient-ts'
import { usePageStore } from '@/stores/pageStore';
import { useThrottleFn }  from "@/utils/utils.ts"

const vFocus = {
  mounted: (el) => {
    const inputEl = el.querySelector('input') || el.querySelector('.el-input__inner') || el;
    if (inputEl) {
      nextTick(() => {
        inputEl.focus();
      });
    }
  }
};

const pageStore = usePageStore();
const fixedNodeId = ref('85');
const dbSelectRowId = ref('');

const addDialogVisible = ref(false);
const addDropdownRef = ref(null);
const addForm = ref({
  type: '',
  name: '',
  description: ''
});
const addFormRef = ref(null);
const addFormRules = ref({
  type: [
    { required: true, message: '请选择类型', trigger: 'change' }
  ],
  name: [
    { required: true, message: '请输入名称', trigger: 'blur' }
  ]
});

const expandedKeys = ref([fixedNodeId.value]);

import { urlData } from '@/stores/urlData';
const urlDatas = urlData();

const treeV2 = ref(null)
const treeHeight = ref(600) // 可动态计算的高度
const treeProps = ref({
  children: 'children',
  label: 'label',
  isLeaf: 'isLeaf',
  disabled: 'disabled'
})
const newPage = ref({
  title: '',
  path: '',
  content: ''
});
const loadingNodes = ref(new Set());
const expandedNodes = ref(new Set());
const route = useRoute();
const dialogVisible = ref(false);

  
const router = useRouter();

const narbar = ref(null);
const showCardDetail = ref(null);

const currentDrop = ref(null);
const loadValue = ref(null);

const changeNums = ref(1);
const urlRef = ref(null);

const activeName = ref('1');
const activeFolder = ref('1');
const eltreeTop = ref(null)
const eltree = ref(null)
const formDatas = reactive({});
const allValueForm = reactive({});
const firstFlagLoad = ref(true)
const visible = ref(false)
const showCarFalg = ref(false)

const selectData = reactive([]);
const selectProjectData = reactive([]);

const tableRef = ref(null);
const nodeClassType = {
    [NodeClass.DataType]:'DataType',
    [NodeClass.Method]:'Method',
    [NodeClass.Object]:'Object',
    [NodeClass.View]:'View',
    [NodeClass.ObjectType]:'ObjectType',
    [NodeClass.ReferenceType]:'ReferenceType',
    [NodeClass.Unspecified]:'Unspecified',
    [NodeClass.Variable]:'Variable',
    [NodeClass.VariableType]:'VariableType'
}
const state = reactive({
  allTypesData:[],
  allCardData:[],
  nodeDetailsArr:[],
  detailsArr:[],
  nodeDetailsData:[
    {key:'1',value:'Variables',type:'Variables'}
  ],
  folderArr:[],
  tempArr: [],
  tableData: [],
  modleData: [],
  treeData: [],
  bottomTreeData: [
    {
      nodeIdNum: 'i=87',
      label: 'Views',
      isLeaf: false,
      nodeClass: 1,
      children: []
    },
    {
      nodeIdNum: 'i=85',
      label: 'Objects',
      isLeaf: false,
      nodeClass: 1,
      children: []
    },
    {
      nodeIdNum: 'i=86',
      label: 'Types',
      isLeaf: false,
      nodeClass: 1,
      children: []
    }
  ],
  templateTreeData: [
    {
      ReferenceTypeId: 'i=38',
      IsForward: true,
      NodeId: 'i=88',
      BrowseName: 'Servers',
      DisplayName: { Text: 'New Node' },
      NodeClass: 1,
      TypeDefinition: 'i=61',
    },
  ],
  messageListBottom: [],
  mergedNodeData: [], // 存储合并后的数组
  loadingVariables: false, // Variables 界面加载状态
});
 
const rules =   {
                urlName: [
                    { required: true, message: '请输入名称', trigger: 'blur' },
                    {
                        pattern: /^[^\u4e00-\u9fa5]+$/,
                        message: '不能输入中文',
                        trigger: 'blur'
                      }
                ],
                desc: [
                    { required: false, trigger: 'blur' },
                    { max: 100, message: '描述不能超过100个字符', trigger: 'blur' }
                ],
                address: [
                    { required: true, message: '请输入地址', trigger: 'blur' },
                    {
                        pattern: /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/,
                        message: '地址格式不正确',
                        trigger: 'blur',
                    }
                ],
                url: [
                    { required: true, message: '请输入端口URL', trigger: 'blur' },
                    {
                        pattern: /^(https?:\/\/)[a-zA-Z0-9.-]+(:\d{1,5})?(\/\S*)?$/,
                        message: 'URL格式不正确',
                        trigger: 'blur',
                    }
                ]
            }
const form = reactive({
  name: '',
  desc: '',
  adress: '',
  port: '',
});
const userData = reactive([]);
const selectKey = reactive({ value: '' });
const selectProjectKey = reactive({ value: '' });
const rowData = reactive({});
const updateNarBar = () => {
};
const getNodeIcon = (nodeClass) => {
  const baseClass = 'node-icon';
  if (nodeClass == 1) return `${baseClass} icon-object`;
  if ([8,16,32,64].includes(nodeClass)) return `${baseClass} icon-type`;
  if (nodeClass == 128) return `${baseClass} icon-group`;
  if (nodeClass == 4) return `${baseClass} icon-play`;
  return baseClass;
};
const showAddDropdown = (event) => {
  event.preventDefault();
  event.stopPropagation();
  
  if (addDropdownRef.value) {
    addDropdownRef.value.handleOpen();
  }
};

const handleAddCommand = (command, node, data) => {
  
  if (command === 'add') {
    currentNode.value = { node, data };
    
    if (node.level === 1) {
      isEditingConnection.value = false;
      urlForm.value = { url: '', urlName: '' };
      
      if (node.data.BrowseName === 'Servers') {
        urlDialogVisible.value = true;
        isEditingConnection.value = true;
      } else if (node.data.BrowseName === 'FolderType') {
      }
    } else if (node.level === 2) {
      isEditingConnection.value = true;
      
      availableUrls.value = state.treeData
        .filter((node) => node.urls && node.urls.length)
        .flatMap((node) => node.urls);
      connectionForm.value = { selectedUrl: '' };
      
    } else {
    }
  }
};

const handleAddDialogClose = (done) => {
  addForm.value = {
    type: '',
    name: '',
    description: ''
  };
  if (addFormRef.value) {
    addFormRef.value.clearValidate();
  }
  done();
};

const handleAddConfirm = async () => {
  if (!addFormRef.value) return;
  
  try {
    await addFormRef.value.validate();
    
    
    ElMessage.success('添加成功！');
    addDialogVisible.value = false;
    
    addForm.value = {
      type: '',
      name: '',
      description: ''
    };
  } catch (error) {
  }
};

const getDialogTitle = () => {
  if (!currentEditingItem.value) return 'Edit Values';
  
  const dataType = currentEditingItem.value.dataTypes || currentEditingItem.value.dataType;
  
  switch (dataType) {
    case 'DateTime':
      return 'Edit DateTime Values';
    case 'Double':
      return 'Edit Double Values';
    case 'String':
      return 'Edit String Values';
    case 'Integer':
      return 'Edit Integer Values';
    case 'UInteger':
      return 'Edit UInteger Values';
    case 'Number':
      return 'Edit Number Values';
    case 'Int16':
    case 'Int32':
    case 'Int64':
      return 'Edit Integer Values';
    case 'None':
      return 'Edit None Values';
    case 'Enumeration':
      return 'Edit Enumeration Values';
    case 'Structure':
      return 'Edit Structure Values';
    case 'QualifiedName':
      return 'Edit QualifiedName Values';
    case 'NumericRange':
      return 'Edit NumericRange Values';
    case 'NodeId':
      return 'Edit NodeId Values';
    default:
      return 'Edit Values';
  }
};

const isDateTimeType = () => {
  if (!currentEditingItem.value) return false;
  
  const dataType = currentEditingItem.value.dataTypes || currentEditingItem.value.dataType;
  return dataType === 'DateTime';
};

const isStringType = () => {
  if (!currentEditingItem.value) return false;
  
  const dataType = currentEditingItem.value.dataTypes || currentEditingItem.value.dataType;
  return dataType === 'String';
};

const getNumberPlaceholder = () => {
  if (!currentEditingItem.value) return '请输入数字';
  
  const dataType = currentEditingItem.value.dataTypes || currentEditingItem.value.dataType;
  
  switch (dataType) {
    case 'Double':
      return '请输入小数';
    case 'Integer':
    case 'Int16':
    case 'Int32':
    case 'Int64':
      return '请输入整数';
    case 'UInteger':
      return '请输入正整数';
    case 'Number':
      return '请输入数字';
    case 'String':
      return '请输入文本';
    case 'NodeId':
      return '请输入NodeId';
    case 'None':
      return '请输入值';
    case 'Enumeration':
      return '请输入枚举值';
    case 'Structure':
      return '请输入结构体值';
    case 'QualifiedName':
      return '请输入限定名称';
    case 'NumericRange':
      return '请输入数值范围';
    default:
      return '请输入数字';
  }
};

const getValidationRules = () => {
  if (!currentEditingItem.value) return [];
  
  const dataType = currentEditingItem.value.dataTypes || currentEditingItem.value.dataType;
  
  switch (dataType) {
    case 'DateTime':
      return [
        { required: true, message: '请选择日期时间', trigger: 'change' },
        { 
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请选择日期时间'));
            } else if (!isValidDateTime(value)) {
              callback(new Error('日期时间格式不正确'));
            } else {
              callback();
            }
          }, 
          trigger: 'change' 
        }
      ];
    case 'String':
      return [
        { required: true, message: '请输入文本', trigger: 'blur' },
        { min: 1, max: 1000, message: '文本长度应在1-1000字符之间', trigger: 'blur' }
      ];
    case 'Double':
      return [
        { required: true, message: '请输入小数', trigger: 'blur' },
        { 
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入小数'));
            } else if (!isValidDouble(value)) {
              callback(new Error('请输入有效的小数'));
            } else {
              callback();
            }
          }, 
          trigger: 'blur' 
        }
      ];
    case 'Integer':
      return [
        { required: true, message: '请输入整数', trigger: 'blur' },
        { 
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入整数'));
            } else if (!isValidInteger(value)) {
              callback(new Error('请输入有效的整数'));
            } else {
              callback();
            }
          }, 
          trigger: 'blur' 
        }
      ];
    case 'UInteger':
      return [
        { required: true, message: '请输入正整数', trigger: 'blur' },
        { 
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入正整数'));
            } else if (!isValidUInteger(value)) {
              callback(new Error('请输入有效的正整数'));
            } else {
              callback();
            }
          }, 
          trigger: 'blur' 
        }
      ];
    case 'Number':
      return [
        { required: true, message: '请输入数字', trigger: 'blur' },
        { 
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入数字'));
            } else if (!isValidNumber(value)) {
              callback(new Error('请输入有效的数字'));
            } else {
              callback();
            }
          }, 
          trigger: 'blur' 
        }
      ];
    case 'Int16':
    case 'Int32':
    case 'Int64':
      return [
        { required: true, message: '请输入整数', trigger: 'blur' },
        { 
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入整数'));
            } else if (!isValidInteger(value)) {
              callback(new Error('请输入有效的整数'));
            } else {
              callback();
            }
          }, 
          trigger: 'blur' 
        }
      ];
    case 'None':
      return [
        { required: true, message: '请输入值', trigger: 'blur' }
      ];
    case 'Enumeration':
      return [
        { required: true, message: '请输入枚举值', trigger: 'blur' }
      ];
    case 'Structure':
      return [
        { required: true, message: '请输入结构体值', trigger: 'blur' }
      ];
    case 'QualifiedName':
      return [
        { required: true, message: '请输入限定名称', trigger: 'blur' }
      ];
    case 'NumericRange':
      return [
        { required: true, message: '请输入数值范围', trigger: 'blur' }
      ];
    case 'NodeId':
      return [
        { required: true, message: '请输入NodeId', trigger: 'blur' },
        { 
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请输入NodeId'));
            } else if (!isValidNodeId(value)) {
              callback(new Error('请输入有效的NodeId格式'));
            } else {
              callback();
            }
          }, 
          trigger: 'blur' 
        }
      ];
    default:
      return [
        { required: true, message: '请输入值', trigger: 'blur' }
      ];
  }
};

// 校验函数
const isValidDateTime = (value) => {
  if (!value) return false;
  const date = new Date(value);
  return !isNaN(date.getTime());
};

const isValidDouble = (value) => {
  if (!value) return false;
  const num = parseFloat(value);
  return !isNaN(num) && isFinite(num);
};

const isValidInteger = (value) => {
  if (!value) return false;
  const num = parseInt(value, 10);
  return !isNaN(num) && Number.isInteger(num);
};

const isValidUInteger = (value) => {
  if (!value) return false;
  const num = parseInt(value, 10);
  return !isNaN(num) && Number.isInteger(num) && num >= 0;
};

const isValidNumber = (value) => {
  if (!value) return false;
  const num = parseFloat(value);
  return !isNaN(num) && isFinite(num);
};

const isValidNodeId = (value) => {
  if (!value) return false;
  // NodeId 格式校验：可以是数字、字符串或 ns=数字;i=数字 格式
  // const nodeIdPattern = /^(ns=\d+;)?[isgb]=[0-9a-fA-F]+$|^[0-9]+$/;
  return  typeof value === 'string';
};

// 校验十六进制字符串
const isValidHexString = (value) => {
  if (!value) return false;
  // 移除空格和分隔符
  const cleanValue = value.replace(/[\s\-]/g, '');
  // 检查是否为有效的十六进制字符串
  const hexPattern = /^[0-9a-fA-F]+$/;
  return hexPattern.test(cleanValue) && cleanValue.length % 2 === 0;
};

// 校验PNG格式
const isValidPNGFormat = (value) => {
  if (!value) return false;
  // 移除空格和分隔符
  const cleanValue = value.replace(/[\s\-]/g, '');
  // PNG文件头：89 50 4E 47 0D 0A 1A 0A
  const pngHeader = '89504E470D0A1A0A';
  return cleanValue.toUpperCase().startsWith(pngHeader);
};

// Variables面板输入框校验函数
const getInputPlaceholder = (dataType) => {
  switch (dataType) {
    case 'Double':
    case 'Number':
      return '请输入数字';
    case 'Integer':
    case 'Int16':
    case 'Int32':
    case 'Int64':
      return '请输入整数';
    case 'UInteger':
    case 'UInt16':
    case 'UInt32':
    case 'UInt64':
      return '请输入正整数';
    case 'String':
      return '请输入文本';
    case 'Boolean':
      return '请输入true/false';
    case 'DateTime':
      return '请输入日期时间';
    case 'NodeId':
      return '请输入NodeId';
    default:
      return '请输入值';
  }
};

// 获取Variables面板输入框的显示值
const getVariableDisplayValue = (item) => {
  if (!item._editValue) {
    return '';
  }
  
  // 如果是数组，取第一个元素
  if (Array.isArray(item._editValue)) {
    return item._editValue.length > 0 ? String(item._editValue[0]) : '';
  }
  
  // 如果是对象，尝试获取值
  if (typeof item._editValue === 'object') {
    if (item._editValue._value !== undefined) {
      return String(item._editValue._value);
    }
    if (item._editValue.value !== undefined) {
      return String(item._editValue.value);
    }
    return '';
  }
  
  // 基本类型直接转换
  return String(item._editValue);
};

const handleVariableInputChange = (item, value) => {
  // 根据数据类型进行实时校验
  const dataType = item.dataTypes;
  
  // 如果输入为空，允许清空
  if (!value || value === '') {
    updateVariableValue(item, value);
    return;
  }
  
  // 检查是否包含汉字
  if (/[\u4e00-\u9fa5]/.test(value)) {
    // 静默阻止汉字输入，不更新值
    return;
  }
  
  // 根据数据类型进行格式校验
  let isValid = false;
  switch (dataType) {
    case 'Double':
    case 'Number':
      // 允许数字、小数点、负号
      isValid = /^-?\d*\.?\d*$/.test(value);
      break;
    case 'Integer':
    case 'Int16':
    case 'Int32':
    case 'Int64':
      // 只允许数字和负号
      isValid = /^-?\d*$/.test(value);
      break;
    case 'UInteger':
    case 'UInt16':
    case 'UInt32':
    case 'UInt64':
      // 只允许数字
      isValid = /^\d*$/.test(value);
      break;
    case 'String':
      // 允许字母、数字、空格
      isValid = /^[a-zA-Z0-9\s]*$/.test(value);
      break;
    case 'Boolean':
      // 只允许'true'/'false'
      isValid = /^(true|false)?$/.test(value);
      break;
    case 'DateTime':
      // 允许数字、冒号、连字符、空格、T
      isValid = /^[0-9:\-\sT]*$/.test(value);
      break;
    case 'NodeId':
      // 允许数字、字母、冒号、分号、等号、下划线
      isValid = /^[0-9a-zA-Z:;=_]*$/.test(value);
      break;
    default:
      isValid = true; // 其他类型暂时允许
  }
  
  // 如果校验通过，更新值
  if (isValid) {
    updateVariableValue(item, value);
  }
  // 如果校验不通过，不更新值，输入框会保持之前的值
};

// 更新Variables面板的值
const updateVariableValue = (item, value) => {
  // 根据数据类型转换值
  const dataType = item.dataTypes;
  let convertedValue = value;
  
  switch (dataType) {
    case 'Double':
    case 'Number':
      convertedValue = value ? parseFloat(value) : 0;
      break;
    case 'Integer':
    case 'Int16':
    case 'Int32':
    case 'Int64':
      convertedValue = value ? parseInt(value) : 0;
      break;
    case 'UInteger':
    case 'UInt16':
    case 'UInt32':
    case 'UInt64':
      convertedValue = value ? parseInt(value) : 0;
      break;
    case 'Boolean':
      convertedValue = value === 'true';
      break;
    case 'DateTime':
      convertedValue = value ? new Date(value) : new Date();
      break;
    default:
      convertedValue = value;
  }
  
  // 更新_editValue
  if (Array.isArray(item._editValue)) {
    // 如果是数组，更新第一个元素
    item._editValue[0] = convertedValue;
  } else if (typeof item._editValue === 'object' && item._editValue !== null) {
    // 如果是对象，更新_value或value属性
    if (item._editValue._value !== undefined) {
      item._editValue._value = convertedValue;
    } else if (item._editValue.value !== undefined) {
      item._editValue.value = convertedValue;
    } else {
      item._editValue = convertedValue;
    }
  } else {
    // 基本类型直接赋值
    item._editValue = convertedValue;
  }
};

const handleVariableKeyPress = (event, dataType) => {
  const charCode = event.charCode;
  const keyCode = event.keyCode;
  
  // 调试信息
  
  // 允许的控制键：退格 (8)、删除 (46)、Tab (9)、Enter (13)、Esc (27)、方向键等
  if (keyCode === 8 || keyCode === 46 || keyCode === 9 || keyCode === 13 || keyCode === 27 || 
      keyCode === 37 || keyCode === 38 || keyCode === 39 || keyCode === 40) {
    return true;
  }
  
  // 明确阻止汉字和其他非ASCII字符
  if (charCode === 0 || charCode > 127) {
    event.preventDefault();
    return false;
  }
  
  // 根据数据类型进行严格的字符限制
  switch (dataType) {
    case 'Double':
    case 'Number':
      // 允许数字、一个小数点、一个负号
      if (charCode >= 48 && charCode <= 57) {
        return true;
      }
      if (charCode === 46) {
        const currentValue = event.target.value;
        if (currentValue.indexOf('.') === -1) {
          return true;
        }
      }
      if (charCode === 45) {
        const currentValue = event.target.value;
        const cursorPosition = event.target.selectionStart;
        if (cursorPosition === 0 && currentValue.indexOf('-') === -1) {
          return true;
        }
      }
      break;
      
    case 'Integer':
    case 'Int16':
    case 'Int32':
    case 'Int64':
      // 只允许数字和一个负号
      if (charCode >= 48 && charCode <= 57) {
        return true;
      }
      if (charCode === 45) {
        const currentValue = event.target.value;
        const cursorPosition = event.target.selectionStart;
        if (cursorPosition === 0 && currentValue.indexOf('-') === -1) {
          return true;
        }
      }
      break;
      
    case 'UInteger':
    case 'UInt16':
    case 'UInt32':
    case 'UInt64':
      // 只允许数字
      if (charCode >= 48 && charCode <= 57) {
        return true;
      }
      break;
      
    case 'Boolean':
      // 只允许输入'true'/'false'的字符
      const allowedChars = [116, 114, 117, 101, 102, 97, 108, 115]; // t,r,u,e,f,a,l,s
      if (allowedChars.includes(charCode)) {
        return true;
      }
      break;
      
    case 'String':
      // 允许字母、数字、空格和常见符号
      if ((charCode >= 48 && charCode <= 57) || // 数字
          (charCode >= 65 && charCode <= 90) || // 大写字母
          (charCode >= 97 && charCode <= 122) || // 小写字母
          charCode === 32) { // 空格
        return true;
      }
      break;
      
    case 'DateTime':
      // 允许数字、冒号、连字符、空格、T
      if ((charCode >= 48 && charCode <= 57) || // 数字
          charCode === 58 || // 冒号
          charCode === 45 || // 连字符
          charCode === 32 || // 空格
          charCode === 84) { // T
        return true;
      }
      break;
      
    case 'NodeId':
      // 允许数字、字母、冒号、分号、等号、下划线
      if ((charCode >= 48 && charCode <= 57) || // 数字
          (charCode >= 65 && charCode <= 90) || // 大写字母
          (charCode >= 97 && charCode <= 122) || // 小写字母
          charCode === 58 || // 冒号
          charCode === 59 || // 分号
          charCode === 61 || // 等号
          charCode === 95) { // 下划线
        return true;
      }
      break;
      
    default:
      // 其他类型允许所有ASCII字符
      if (charCode >= 32 && charCode <= 126) {
        return true;
      }
  }
  
  // 其他情况阻止输入
  event.preventDefault();
  return false;
};

const handleVariableKeyDown = (event, dataType) => {
  const keyCode = event.keyCode;
  
  // 允许的控制键：退格 (8)、删除 (46)、Tab (9)、Enter (13)、Esc (27)、方向键等
  if (keyCode === 8 || keyCode === 46 || keyCode === 9 || keyCode === 13 || keyCode === 27 || 
      keyCode === 37 || keyCode === 38 || keyCode === 39 || keyCode === 40) {
    return true;
  }
  
  // 其他键的处理交给 @input 事件
  return true;
};

const handleVariablePaste = (event, dataType) => {
  event.preventDefault();
  const pasteData = (event.clipboardData || window.clipboardData).getData('text');
  
  // 检查是否包含汉字或其他非ASCII字符
  if (/[\u4e00-\u9fa5]/.test(pasteData)) {
    return;
  }
  
  // 根据数据类型检查粘贴内容
  let isValid = false;
  switch (dataType) {
    case 'Double':
    case 'Number':
      isValid = /^-?\d*\.?\d*$/.test(pasteData);
      break;
    case 'Integer':
    case 'Int16':
    case 'Int32':
    case 'Int64':
      isValid = /^-?\d*$/.test(pasteData);
      break;
    case 'UInteger':
    case 'UInt16':
    case 'UInt32':
    case 'UInt64':
      isValid = /^\d*$/.test(pasteData);
      break;
    case 'String':
      isValid = typeof pasteData === 'string';
      break;
    case 'Boolean':
      isValid = pasteData === 'true' || pasteData === 'false';
      break;
    case 'DateTime':
      isValid = isValidDateTime(pasteData);
      break;
    case 'NodeId':
      isValid = isValidNodeId(pasteData);
      break;
    default:
      isValid = true;
  }
  
  if (isValid) {
    const target = event.target;
    const start = target.selectionStart;
    const end = target.selectionEnd;
    const currentValue = target.value;
    const newValue = currentValue.substring(0, start) + pasteData + currentValue.substring(end);
    
    target.value = newValue;
    // 触发 input 事件
    const inputEvent = new Event('input', { bubbles: true });
    target.dispatchEvent(inputEvent);
  }
};
// 右键菜单相关状态
const urlDialogVisible = ref(false);
const FolderDialogVisible = ref(false);
const PropertyDialogVisible = ref(false);

const connectFlag = ref(false);
const lastConnectFlag = ref(false); // 添加这个来跟踪连接状态变化
const rootNodeLoaded = ref(false); // 添加这个来跟踪根节点是否已加载
const isEditingConnection = ref(false);
const showBooleanFlag = ref(false);
const showBooleanData= reactive([]);
const showDoubleFlag = ref(false);
const showDoubleData = reactive([]);
const showUIntegerFlag = ref(false);
const showUIntegerData = reactive([]);
const showCarExtrasFlag = ref(false);
const showCarExtrasData = reactive([]);
const showUInt64Flag = ref(false);
const showUInt64Data = reactive([]);
const showByteFlag = ref(false);
const showByteData = reactive({
  value: 0,
  bits: [false, false, false, false, false, false, false, false]
});
const showByteStringFlag = ref(false);
const showByteStringData = reactive([]);
const showExpandedNodeIdFlag = ref(false);
const showExpandedNodeIdData = reactive([]);
const showImagePNGFlag = ref(false);
const showImagePNGData = reactive([]);
const currentEditingItem = ref(null); // 保存当前正在编辑的 item

// Method Call 相关状态
const showMethodCallFlag = ref(false);
const currentMethodNode = ref(null);
const methodInputArgs = reactive([]);
const methodOutputArgs = reactive([]);
const methodResult = ref('');

// Method OptionSet 相关状态
const showMethodOptionSetFlag = ref(false);
const methodOptionSetData = reactive([]);
const currentOptionSetValue = ref(0);
const currentEditingMethodArg = ref(null);

// IdentifierType 选项
const identifierTypeOptions = [
  { value: 0, label: 'Numeric (0)' },
  { value: 1, label: 'String (1)' },
  { value: 2, label: 'GUID (2)' },
  { value: 3, label: 'ByteString (3)' }
];

// NodeClass 映射表
const nodeClassMapping = {
  0: 'Unspecified',
  1: 'Object',
  2: 'Variable',
  4: 'Method',
  8: 'ObjectType',
  16: 'VariableType',
  32: 'ReferenceType',
  64: 'DataType',
  128: 'View'
};

// 将nodeClass数值转换为字母标识
const getNodeClassText = (nodeClassValue) => {
  if (nodeClassValue === null || nodeClassValue === undefined) {
    return 'Unknown';
  }
  
  // 如果是单个值，直接查找
  if (nodeClassMapping[nodeClassValue]) {
    return nodeClassMapping[nodeClassValue];
  }
  
  // 如果是组合值（位运算结果），分解并组合
  const activeClasses = [];
  Object.keys(nodeClassMapping).forEach(key => {
    const numKey = parseInt(key);
    if (nodeClassValue & numKey) {
      activeClasses.push(nodeClassMapping[numKey]);
    }
  });
  
  return activeClasses.length > 0 ? activeClasses.join(' | ') : 'Unknown';
};

// 获取读写状态信息
const getAccessStatus = (userWriteMask) => {
  const statusMap = {
    1: {
      icon: 'Lock',
      text: '只读',
      color: '#f56c6c',
      canEdit: false
    },
    2: {
      icon: 'EditPen',
      text: '只写',
      color: '#e6a23c',
      canEdit: true
    },
    3: {
      icon: 'Edit',
      text: '可读可写',
      color: '#67c23a',
      canEdit: true
    }
  };
  return statusMap[userWriteMask] || {
    icon: 'QuestionFilled',
    text: '未知',
    color: '#909399',
    canEdit: false
  };
};



// 处理复制命令
const handleCopyCommand = async (command) => {
  try {
    const { text, key } = command;
    const textToCopy = typeof text === 'object' ? text.text || text : String(text);
    
    // 使用现代浏览器的 Clipboard API
    if (navigator.clipboard && window.isSecureContext) {
      await navigator.clipboard.writeText(textToCopy);
      ElMessage.success(`已复制 ${key} 的值到剪贴板`);
    } else {
      // 降级方案：使用传统的 document.execCommand
      const textArea = document.createElement('textarea');
      textArea.value = textToCopy;
      textArea.style.position = 'fixed';
      textArea.style.left = '-999999px';
      textArea.style.top = '-999999px';
      document.body.appendChild(textArea);
      textArea.focus();
      textArea.select();
      
      const successful = document.execCommand('copy');
      document.body.removeChild(textArea);
      
      if (successful) {
        ElMessage.success(`已复制 ${key} 的值到剪贴板`);
      } else {
        ElMessage.error('复制失败，请手动复制');
      }
    }
  } catch (error) {
    ElMessage.error('复制失败，请手动复制');
  }
};
const currentNode = ref(null);
const availableUrls = ref([]);
const propertyData= ref(null);
// 表单数据
const urlForm = ref({
  url: '',
  urlName: '',
});

const connectionForm = ref({
  selectedUrl: '',
});

const expandRowKeys = ref([]); // 默认展开 ID 为 1 的行

const handleExpandChange = (row, expandedRows) => {
};
const toggleExpand = (row) => {
      // 使用表格行的 id 字段作为唯一标识符
      let rowKey = row.id;
      
      if (expandRowKeys.value.includes(rowKey)) {
        // 如果已展开，则收起
        expandRowKeys.value = expandRowKeys.value.filter(key => key !== rowKey);
      } else {
        // 如果已收起，则展开
        expandRowKeys.value.push(rowKey);
      }
    };

// 为表格行添加CSS类名
const getRowClassName = ({ row, rowIndex }) => {
  if (expandRowKeys.value.includes(row.id)) {
    return 'expanded-row';
  }
  return '';
};

// 当前页面标题
const currentPageTitle = computed(() => {
  return pageStore.currentPage 
    ? pageStore.currentPage.title 
    : '动态面包屑示例';
});

// Method Call 弹窗标题
const methodCallTitle = computed(() => {
  if (!currentMethodNode.value) return 'Call Method';
  
  const methodName = currentMethodNode.value.displayName?.text || 
                    currentMethodNode.value.browseName?.name || 
                    'Method';
  
  if (methodName.includes('Multiply')) {
    return 'Returns the product of the two given numbers';
  }
  
  if (methodName.includes('EnumTest')) {
    return 'Call Method';
  }
  
  return `Call ${methodName}`;
});
const enumComputedValue = computed({
  get() {
    return item.value.value;
  },
  set(newVal) {
    item.value.value = newVal;
  }
});
// 添加新页面
const addNewPage = () => {
  if (!newPage.value.title || !newPage.value.path) {
    ElMessage.error('请填写页面标题和路径');
    return;
  }
  
  // 确保路径以斜杠开头
  if (!newPage.value.path.startsWith('/')) {
    newPage.value.path = '/' + newPage.value.path;
  }
  
  // 添加页面
  const addedPage = pageStore.addPage(newPage.value);
  
  // 导航到新页面
  pageStore.setCurrentPage(addedPage.id);
  router.push(addedPage.path);
  
  newPage.value = { title: '', path: '', content: '' };
  
  ElMessage.success('页面添加成功');
};
// 添加示例页面
const addDemoPage = () => {
  const demos = [
    {
      title: '产品介绍',
      path: '/panel',
      content: '这里展示我们的产品系列，包括各种创新产品和技术解决方案。'
    },
    {
      title: '系统仪表盘',
      path: '/dashboard',
      content: '系统运行状态和关键指标展示。'
    }
  ];
  
  const demo = demos[Math.floor(Math.random() * demos.length)];
  
  // 直接导航到子路由，而不是通过 pageStore
  router.push(demo.path);
  
  ElMessage.success(`已导航到 ${demo.title}`);
};

// 导航到页面
const goToPage = (page) => {
  pageStore.setCurrentPage(page.id);
  router.push(page.path);
};
const targetNode = ref(null)
const getDetialCard =  (e)=> {
  showCardDetail.value = e?.nodeId?._nodeId?.value
  selectedNodeId.value = e.nodeId
  let passNodeClass = Number(NodeClass.Variable) 
  
  // 设置loading状态
  e._isLoading = true
  
  try{
      getBrowseDatas(passNodeClass).then(async (firstRes) => {
        state.allCardData = []
        let allArr = firstRes?.results || []; // 初始化累计数组
        let continuationPoint = firstRes?.ContinuationPoint;

        // 循环获取后续页数据
        while (continuationPoint) {
          continuationPoints.value = continuationPoint; // 设置继续点
          try {
            const nextRes = await getBrowseNextDatas();

            // 合并数据
            if (nextRes?.results) {
              allArr = allArr.concat(nextRes.results);
            }

            // 更新继续点
            continuationPoint = nextRes?.ContinuationPoint;
          } catch (error) {
            break; // 出错时终止循环
          }
        }
        if(!allArr || allArr.length == 0 ){
          e._hasChildren = []
          e._showCard = false  // 没有数据时隐藏按钮
          e._isLoading = false  // 隐藏loading
          ElMessage.warning('获取数据为空')
          return
        }
        allArr.map((item)=>{
          let tempNodeClass = item.nodeClass
          item.changeNodeClass = nodeClassType[tempNodeClass]
          item.nodeIdNum = item.nodeId?._nodeId.toString()
        })
        // 将数据设置到当前行的 _hasChildren 属性中
        e._hasChildren = allArr
        // 同时保持全局数据用于其他用途
        state.allCardData = allArr
        e._showCard = true  // 有数据时显示子表
        e._isLoading = false  // 隐藏loading
     });
     }
     catch(err){
        e._showCard = false
        e._isLoading = false  // 隐藏loading
        connectFlag.value = false
        ElMessage.warning('获取数据失败')
     }
}

// 双击行事件处理函数
const handleRowDblClick = (item, index) => {
  // 清除其他行的选中状态
  state.mergedNodeData.forEach((row, idx) => {
    if (idx !== index) {
      row._isSelected = false;
    }
  });
  
  // 切换当前行的选中状态
  item._isSelected = !item._isSelected;
  dbSelectRowId.value = item.nodeId
  detailsVariableMessage(dbSelectRowId.value)
};

// 开始编辑值
const startEditValue = (item) => {
  // 检查 UserWriteMask 权限
  const accessStatus = getAccessStatus(item.UserWriteMask);
  if (!accessStatus.canEdit) {
    ElMessage.warning(`该变量为${accessStatus.text}状态，无法编辑`);
    return;
  }
  
  if(item.dataTypes == "Boolean" || item.dataType == "Boolean" ){
     showBooleanFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     // 确保 showBooleanData 是数组格式
     if (Array.isArray(item.value)) {
       showBooleanData.splice(0, showBooleanData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showBooleanData.splice(0, showBooleanData.length, ...item.value.value);
     } else {
       // 如果不是数组，创建一个包含单个值的数组
       showBooleanData.splice(0, showBooleanData.length, item.value);
     }
     return
  }
  
  if((item.dataTypes == "Double" || item.dataType == "Double") ){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     // 确保 showDoubleData 是数组格式
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       // 如果不是数组，创建一个包含单个值的数组
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }
  
  // String 类型使用 Double 弹窗编辑
  if((item.dataTypes == "String" || item.dataType == "String") ){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }
  
  // Int16/Int32/Int64 同 Double 弹窗编辑
  if((item.dataType == "Int16" || item.dataType == "Int32" || item.dataType == "Int64" )){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value.value);
     }
     return
  }
  
  // Integer 类型使用 Double 弹窗编辑
  if((item.dataTypes == "Integer" || item.dataType == "Integer") ){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }

  // UInteger 类型使用 UInteger 弹窗编辑
  if((item.dataTypes == "UInteger" || item.dataType == "UInteger") ){
     showUIntegerFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showUIntegerData.splice(0, showUIntegerData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showUIntegerData.splice(0, showUIntegerData.length, ...item.value.value);
     } else {
       showUIntegerData.splice(0, showUIntegerData.length, item._editValue || item.value);
     }
     return
  }

  // Number 同 Double 弹窗编辑
  if((item.dataTypes == "Number" || item.dataType == "Number" )){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }
  
  // NodeId 类型使用 Double 弹窗编辑
  if((item.dataTypes == "NodeId" || item.dataType == "NodeId") ){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }

  // None 类型使用 Double 弹窗编辑
  if((item.dataTypes == "None" || item.dataType == "None") ){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }

  // DateTime 类型使用 Double 弹窗编辑
  if((item.dataTypes == "DateTime" || item.dataType == "DateTime") ){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) { 
      // const newArr = item.value.map(v => moment(v).format('YYYY-MM-DD HH:mm:ss'))
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
      // 1. 先转日期
      // const newArr = item.value.value.map(v => moment(v).format('YYYY-MM-DD HH:mm:ss'))
      // 2. 正确替换响应式数组
      showDoubleData.splice(0, showDoubleData.length, ...item.value.value)
     } else {
      //  let time = moment(item._editValue ).format('YYYY-MM-DD HH:mm:ss') || moment(item.value ).format('YYYY-MM-DD HH:mm:ss')
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }

  // Enumeration 类型使用 Double 弹窗编辑
  if((item.dataTypes == "Enumeration" || item.dataType == "Enumeration") ){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }

  // Structure 类型使用 Double 弹窗编辑
  if((item.dataTypes == "Structure" || item.dataType == "Structure") ){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }

  // QualifiedName 同 Double 弹窗编辑
  if((item.dataTypes == "QualifiedName" || item.dataType == "QualifiedName" )){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
     }
     return
  }
  
  // NumericRange 同 Double 弹窗编辑
  if((item.dataTypes == "String" && item.dataType == "NumericRange") && Array.isArray(item.value.value)){
     showDoubleFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     if (Array.isArray(item.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value);
     } else if (item.value && Array.isArray(item.value.value)) {
       showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
     } else {
       showDoubleData.splice(0, showDoubleData.length, item.value);
     }
     return
  }
  
  if((item.dataTypes == "ByteString" || item.dataType == "ByteString") && Array.isArray(item.value.value)){
     showByteStringFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     // 确保 showByteStringData 是数组格式，并转换为十六进制显示
     if (Array.isArray(item._editValue)) {
       showByteStringData.splice(0, showByteStringData.length, ...item._editValue);
     } else if (Array.isArray(item.value)) {
       const hexValues = item.value.map(val => byteStringToHex(val));
       showByteStringData.splice(0, showByteStringData.length, ...hexValues);
     } else if (item.value && Array.isArray(item.value.value)) {
       const hexValues = item.value.value.map(val => byteStringToHex(val));
       showByteStringData.splice(0, showByteStringData.length, ...hexValues);
     } else {
       // 如果不是数组，创建一个包含单个值的数组
       const hexValue = byteStringToHex(item.value || item._editValue);
       showByteStringData.splice(0, showByteStringData.length, hexValue);
     }
     return
  }
  
  if(item.dataTypes == "ExpandedNodeId" || item.dataType == "ExpandedNodeId" ){
     showExpandedNodeIdFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     // 确保 showExpandedNodeIdData 是数组格式
     if (Array.isArray(item._editValue)) {
       showExpandedNodeIdData.splice(0, showExpandedNodeIdData.length, ...item._editValue);
     } else if (Array.isArray(item.value)) {
       const expandedNodeIdValues = item.value.map(val => parseExpandedNodeId(val));
       showExpandedNodeIdData.splice(0, showExpandedNodeIdData.length, ...expandedNodeIdValues);
     } else if (item.value && Array.isArray(item.value.value)) {
       const expandedNodeIdValues = item.value.value.map(val => parseExpandedNodeId(val));
       showExpandedNodeIdData.splice(0, showExpandedNodeIdData.length, ...expandedNodeIdValues);
     } else {
       // 如果不是数组，创建一个包含单个值的数组
       const expandedNodeIdValue = parseExpandedNodeId(item.value || item._editValue);
       showExpandedNodeIdData.splice(0, showExpandedNodeIdData.length, expandedNodeIdValue);
     }
     return
  }
  
  if(item.dataTypes == "ImagePNG" || item.dataType == "ImagePNG" ){
     showImagePNGFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     // 确保 showImagePNGData 是数组格式，并转换为十六进制显示
     if (Array.isArray(item._editValue)) {
       // 若 _editValue 已是字符串数组，逐项判断是否为二进制：包含不可见字符则转 hex，否则认为已是 hex
       const hexValues = item._editValue.map(val => (typeof val === 'string' && /[\x00-\x08\x0B\x0C\x0E-\x1F]/.test(val)) ? binaryStringToHex(val) : val);
       showImagePNGData.splice(0, showImagePNGData.length, ...hexValues);
     } else if (Array.isArray(item.value)) {
       const hexValues = item.value.map(val => binaryStringToHex(val));
       showImagePNGData.splice(0, showImagePNGData.length, ...hexValues);
     } else if (item.value && Array.isArray(item.value.value)) {
       const hexValues = item.value.value.map(val => binaryStringToHex(val));
       showImagePNGData.splice(0, showImagePNGData.length, ...hexValues);
     } else {
       // 如果不是数组，创建一个包含单个值的数组
       const hexValue = binaryStringToHex(item._editValue || item.value || '');
       showImagePNGData.splice(0, showImagePNGData.length, hexValue);
     }
     return
  }
  if((item.dataTypes == "Byte" || item.dataType.includes('OptionSet'))&& item.dataTypes != "UInt64"){
     showByteFlag.value = true
     currentEditingItem.value = item; // 保存当前编辑的 item
     // 从 UaVariant 对象中提取字节值
     let byteValue = 0;
     if (item.value && typeof item.value === 'object') {
       if (item.value._value !== undefined) {
         byteValue = item.value._value;
       } else if (item.value.value !== undefined) {
         byteValue = item.value.value;
       }
     } else {
       byteValue = item.value || 0;
     }
     
     // 将字节值转换为位数组
     showByteData.value = byteValue;
     showByteData.bits = byteToBits(byteValue);
     
     return
  }
  if(item.dataTypes == "UInt64" && item.dataType == "OptionSetUInt64"){
    showUInt64Flag.value = true
    currentEditingItem.value = item; // 保存当前编辑的 item
    
     // 从 UInt64 值中提取位标志
     let uint64Value = 0;
     if (item.value && typeof item.value === 'object') {
       if (item.value._value !== undefined) {
         uint64Value = item.value._value;
       } else if (item.value.value !== undefined) {
         uint64Value = item.value.value;
       }
     } else {
       uint64Value = item.value || 0;
     }
     
     // 确保 uint64Value 是有效的数字或字符串
     if (typeof uint64Value === 'string') {
       uint64Value = uint64Value.trim();
       if (uint64Value === '' || uint64Value === 'undefined' || uint64Value === 'null') {
         uint64Value = 0;
       }
     }
     
     // 转换为 BigInt，处理可能的错误
     let bigIntValue;
     try {
       bigIntValue = BigInt(uint64Value);
     } catch (error) {
       bigIntValue = 0n;
     }
     
     // 生成64个位标志的名称和值
     const uint64Data = [];
     for (let i = 0; i < 64; i++) {
       const bitValue = (bigIntValue & (1n << BigInt(i))) !== 0n;
       uint64Data.push({
         name: generateBitName(i),
         value: bitValue,
         bitIndex: i
       });
     }
    
    showUInt64Data.splice(0, showUInt64Data.length, ...uint64Data);
    return
  }
  if((item.dataTypes == "UInteger" || item.dataType == "UInteger" )&& Array.isArray(item.value.value)){
    showUIntegerFlag.value = true
    currentEditingItem.value = item; // 保存当前编辑的 item
    // 确保 showUIntegerData 是数组格式
    if (Array.isArray(item.value)) {
      showUIntegerData.splice(0, showUIntegerData.length, ...item.value);
    } else if (item.value && Array.isArray(item.value.value)) {
      showUIntegerData.splice(0, showUIntegerData.length, ...item.value.value);
    } else {
      // 如果不是数组，创建一个包含单个值的数组
      showUIntegerData.splice(0, showUIntegerData.length, item.value);
    }
    return
  }
  if(item.dataType == "CarExtras" ){
    showCarExtrasFlag.value = true
    currentEditingItem.value = item; // 保存当前编辑的 item
    
    // 解析 CarExtras 数据
    let carExtrasData = [];
    if (carExtrasData.length === 0) {
      carExtrasData = [
        { name: 'airconditioner', value: false, selected: true },
        { name: 'supercharged', value: true, selected: true },
        { name: 'launchcontrol', value: true, selected: true },
        { name: 'navigationsystem', value: false, selected: true }
      ];
    }
    
    showCarExtrasData.splice(0, showCarExtrasData.length, ...carExtrasData);
     return
  }
  // 默认处理：对于未匹配的类型，使用 Double 弹窗编辑
  showDoubleFlag.value = true
  currentEditingItem.value = item; // 保存当前编辑的 item
  if (Array.isArray(item.value)) {
    showDoubleData.splice(0, showDoubleData.length, ...item.value);
  } else if (item.value && Array.isArray(item.value.value)) {
    showDoubleData.splice(0, showDoubleData.length, ...item.value.value);
  } else {
    showDoubleData.splice(0, showDoubleData.length, item._editValue || item.value);
  }
  return

  // 清除其他项的编辑状态
  state.mergedNodeData.forEach((row) => {
    if (row !== item) {
      row._isEditing = false;
    }
  });
  
  // 设置当前项为编辑状态
  item._isEditing = true;
  item._showCard = !item._isSelected;
  
  // 保存原始值，用于取消编辑时恢复
  item._originalValue = item._editValue;
  
};

// 完成编辑值
const finishEditValue = (item) => {
  item._isEditing = false;
  delete item._originalValue;
  
  // 根据数据类型验证和转换值
  validateAndConvertValue(item);
  
};

// 取消编辑值
const cancelEditValue = (item) => {
  item._isEditing = false;
  
  // 恢复原始值
  if (item._originalValue !== undefined) {
    item._editValue = item._originalValue;
    delete item._originalValue;
  }
  
};

// 验证和转换值
const validateAndConvertValue = (item) => {
  const dataType = item.dataType?.toLowerCase() || '';
  
  try {
    switch (item._inputMode) {
      case 'boolean':
        if (typeof item._editValue === 'string') {
          item._editValue = item._editValue.toLowerCase() === 'true';
        }
        break;
      case 'number':
        const numValue = parseFloat(item._editValue);
        if (!isNaN(numValue)) {
          item._editValue = numValue;
        } else {
          ElMessage.warning(`${item.name}: 请输入有效的数字`);
          item._editValue = item._originalValue || 0;
        }
        break;
      case 'datetime':
        if (item._editValue && !(item._editValue instanceof Date)) {
          const dateValue = new Date(item._editValue);
          if (isNaN(dateValue.getTime())) {
            ElMessage.warning(`${item.name}: 请输入有效的日期时间`);
            item._editValue = item._originalValue || new Date();
          } else {
            item._editValue = dateValue;
          }
        }
        break;
      default:
        // 文本类型不需要特殊处理
        break;
    }
  } catch (error) {
    ElMessage.error(`${item.name}: 值验证失败`);
  }
};

// 格式化显示值
const formatDisplayValue = (value, item) => {
  if (value === null || value === undefined) {
    return '';
  }
  
  // 处理枚举类型的显示
  if (item.dataTypes === 'Enumeration') {
    // 首先尝试使用 enumStrings（简单字符串数组）
    if (item.dataTypesObj?.enumValues ) {
      const arr = [...item.dataTypesObj.enumValues]; 

      const enumString = arr.find(val => {
          if(val[0] == value){
            return val[1]?.text
          }
      });
      if (enumString) {
        return  enumString[0] + '('+ enumString[1].text+ ')';
      }
    }
    
    
  }

  // 特殊处理 ImagePNG 类型
  if (item.dataTypes === 'ImagePNG') {
    return formatImagePNGForDisplay(value);
  }
  
  // 特殊处理 DateTime 类型
  if (item.dataType === 'DateTime' || item.dataTypes === 'DateTime') {
    if (value instanceof Date) {
      return value.toLocaleString('zh-CN');
    } else if (value && (typeof value === 'string' || typeof value === 'number')) {
      const dateValue = new Date(value);
      if (!isNaN(dateValue.getTime())) {
        return dateValue.toLocaleString('zh-CN');
      }
    }
    return value ? value.toString() : '';
  }
  
   if(item.dataType == "CarExtras"  && item.dataTypes == "OptionSet"){
     return 'Double click to display value'
   }
   if(item.dataTypes == "UInt64" && item.dataType == "OptionSetUInt64"){
     return 'Double click to display value'
   }
  if( (item.dataTypes.includes('Structure') 
      || item.dataTypes == 'ByteString' 
      || item.dataTypes == "ExpandedNodeId"
      || item.dataTypes == "Image"
    )
      && Array.isArray(item.value.value)
  ){
     
    return 'Double click to display value'
  }

  switch (item._inputMode) {
    case 'boolean':
      return value ? '是' : '否';
    case 'number':
      return typeof value === 'number' ? value.toString() : value;
    case 'datetime':
      if (value instanceof Date) {
        return value.toLocaleString('zh-CN');
      }
      return value;
    default:
      return String(value);
  }
};

const onCtx =  (e)=> {
  e.preventDefault()     
  styleObject.value.left = e.layerX+ 10 + 'px';
  styleObject.value.top = e.layerY+ 10 + 'px';
  // newBtn.value = !newBtn.value;

  targetNode.value = e
}
const detailsMessage = async (nodeIDs, url) => {
  let nodeId = nodeIDs;
  
  // 使用传入的 url 参数，如果没有则尝试从 store 获取
  let cacheUrl = url;
  if (!cacheUrl) {
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    cacheUrl = allData?.url || urlForm.value.url;
  }
  
  if (nodeId) {
    let apiConfig = new Configuration({
      basePath: cacheUrl
  });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);

    let nodeAttributesArr  = await testOpcServer.readNodeAttributes(nodeId,true);
    let variableAttributesArr  = await testOpcServer.readVariableAttributes([nodeId]);
   
    // nodeDetailsData.value.displayName = nodeDetailsData.value.displayName.text
     
    nodeDetailsData.value  = Object.assign({}, nodeAttributesArr, variableAttributesArr);
    let temp = state.allTypesData.filter((idx)=>{
          let typeId= idx.nodeId.toString()
          let nodeId = nodeDetailsData.value[0].dataType.toString()
          return typeId == nodeId
        })
    nodeDetailsData.value.dataType = temp[0]?._browseName
    nodeDetailsData.value.nodeId =  nodeId
    nodeDetailsData.value.UserWriteMask = nodeDetailsData.value[0].userAccessLevel
    nodeDetailsData.value.accessLevel = nodeDetailsData.value[0].userAccessLevel
    nodeDetailsData.value.description = nodeDetailsData.value.description?.text
    // 保留原始nodeClass数值，同时添加转换后的文本
    if (nodeDetailsData.value.nodeClass !== undefined) {
      nodeDetailsData.value.nodeClassText = getNodeClassText(nodeDetailsData.value.nodeClass);
       delete nodeDetailsData.value.nodeClass
    }
    nodeDetailsData.value.nodeClass = nodeDetailsData.value.nodeClassText
    delete nodeDetailsData.value[0]
    delete nodeDetailsData.value.dataType

    delete nodeDetailsData.value.nodeClassText
    delete nodeDetailsData.value.writeMask
    delete nodeDetailsData.value.UserWriteMask
    delete nodeDetailsData.value.accessLevel
    nodeDetailsData.value =  reorderObj(nodeDetailsData.value, ['nodeId','nodeClass','browseName','displayName','description'])

  } else {
  }
}
const detailsVariableMessage = async(nodeIDs) => {
  let nodeId = nodeIDs;
  const accessLevelMap ={
    1:'Read',
    2:'Write',
    3:'Read&Write'
  }
  // 获取正确的URL
  const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
  let url = allData?.url || urlForm.value.url;
  
  if (!url) {
    ElMessage.error('请先配置服务器连接');
    return;
  }
  
  if (nodeId) {
    let apiConfig = new Configuration({
      basePath: url
    });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);

    let nodeAttributesArr  = await testOpcServer.readNodeAttributes(nodeId,true);
    let variableAttributesArr  = await testOpcServer.readVariableAttributes([nodeId]);
   
    // nodeDetailsData.value.displayName = nodeDetailsData.value.displayName.text
     
    nodeDetailsData.value  = Object.assign({}, nodeAttributesArr, variableAttributesArr);
    let temp = state.allTypesData.filter((idx)=>{
          let typeId= idx.nodeId.toString()
          let nodeId = nodeDetailsData.value[0].dataType.toString()
          return typeId == nodeId
        })
    nodeDetailsData.value.dataType = temp[0]?._browseName
    nodeDetailsData.value.nodeId = nodeId
    nodeDetailsData.value.UserWriteMask = nodeDetailsData.value[0].userAccessLevel
    nodeDetailsData.value.accessLevel = accessLevelMap[nodeDetailsData.value[0].userAccessLevel]
    nodeDetailsData.value.valueRank = nodeDetailsData.value[0].valueRank
    nodeDetailsData.value.description = nodeDetailsData.value.description?.text
    // 保留原始nodeClass数值，同时添加转换后的文本
    if (nodeDetailsData.value.nodeClass !== undefined) {
      nodeDetailsData.value.nodeClassText = getNodeClassText(nodeDetailsData.value.nodeClass);
       delete nodeDetailsData.value.nodeClass
    }
    nodeDetailsData.value.nodeClass = nodeDetailsData.value.nodeClassText
    delete nodeDetailsData.value[0]
    
    delete nodeDetailsData.value.nodeClassText
    delete nodeDetailsData.value.writeMask
    delete nodeDetailsData.value.UserWriteMask
    nodeDetailsData.value =  reorderObj(nodeDetailsData.value, ['nodeId','nodeClass','browseName','displayName','description','dataType','accessLevel','valueRank'])

  } else {
  }
}
// 删除页面
const removePage = (id) => {
  pageStore.removePage(id);
  ElMessage.success('页面已删除');
};

// 重置所有
const resetAll = () => {
  pageStore.resetPages();
  router.push('/');
  ElMessage.success('已重置所有动态页面');
};
const breadcrumbList = computed(() => {
  const matchedRoutes = router.currentRoute.value.matched;
  return matchedRoutes.filter(route => route.meta?.breadcrumb);
});
const isLetter = (event) => {
  var charCode = event.charCode;
  if ((charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122) || charCode == 0) {
    return true;
  } else {
    event.preventDefault();
    return false;
  }
};
const isLetters = (event, key) => {
  var charCode = event.charCode;
  if ((charCode >= 45 && charCode <= 57) || charCode == 46 || charCode == 0) {
    return true;
  } else {
    event.preventDefault();
    return false;
  }
};
 
const isNumber = (event) => {
  const key = event.key;
  const el  = event.target;
  const val = el.value;
  const pos = el.selectionStart;

  if (['Backspace','Delete','Tab','Enter','Escape','ArrowLeft','ArrowRight','ArrowUp','ArrowDown'].includes(key)) {
    return true;
  }

  const dt = currentEditingItem.value?.dataTypes || currentEditingItem.value?.dataType || '';

  if (key >= '0' && key <= '9') {
    return true;
  }

  if (['Double', 'Number'].includes(dt)) {
    // 小数点：只允许一个，且不能重复
    if (key === '.') {
      if (!val.includes('.')) {
        return true;
      } else {
        event.preventDefault();
        return false;
      }
    }
    
    // 负号：只允许在开头，且不能重复
    if (key === '-') {
      if (pos === 0 && !val.includes('-')) {
        return true;
      } else {
        event.preventDefault();
        return false;
      }
    }
    
    // 其他字符一律拦截
    event.preventDefault();
    return false;
  }

  if (['Integer', 'Int16', 'Int32', 'Int64'].includes(dt)) {
    // 负号：只允许在开头，且不能重复
    if (key === '-') {
      if (pos === 0 && !val.includes('-')) {
        return true;
      } else {
        event.preventDefault();
        return false;
      }
    }
    
    // 小数点和其他字符一律拦截
    event.preventDefault();
    return false;
  }

  if (dt === 'UInteger') {
    // 只允许数字，其他一律拦截
    event.preventDefault();
    return false;
  }

  event.preventDefault();
  return false;
};
// 处理数字输入框的粘贴事件
const handleNumberPaste = (event) => {
  event.preventDefault();
  const pasteData = (event.clipboardData || window.clipboardData).getData('text');
  
  // 检查是否包含汉字或其他非ASCII字符
  if (/[\u4e00-\u9fa5]/.test(pasteData)) {
    return;
  }
  
  // 检查是否包含其他特殊字符
  if (/[^\d\.\-\s]/.test(pasteData)) {
    return;
  }
  
  // 检查粘贴的内容是否为有效数字
  const dataType = currentEditingItem.value?.dataTypes || currentEditingItem.value?.dataType;
  
  if (dataType === 'Number' || dataType === 'Double') {
    // 允许数字、小数点、负号
    const numberPattern = /^-?\d*\.?\d*$/;
    if (numberPattern.test(pasteData)) {
      const target = event.target;
      const start = target.selectionStart;
      const end = target.selectionEnd;
      const currentValue = target.value;
      const newValue = currentValue.substring(0, start) + pasteData + currentValue.substring(end);
      
      // 验证新值是否为有效数字
      if (isValidNumber(newValue) || newValue === '' || newValue === '-') {
        target.value = newValue;
        // 触发 input 事件
        const inputEvent = new Event('input', { bubbles: true });
        target.dispatchEvent(inputEvent);
      }
    }
  } else if (dataType === 'Integer' || dataType === 'Int16' || dataType === 'Int32' || dataType === 'Int64') {
    // 只允许整数
    const integerPattern = /^-?\d*$/;
    if (integerPattern.test(pasteData)) {
      const target = event.target;
      const start = target.selectionStart;
      const end = target.selectionEnd;
      const currentValue = target.value;
      const newValue = currentValue.substring(0, start) + pasteData + currentValue.substring(end);
      
      // 验证新值是否为有效整数
      if (isValidInteger(newValue) || newValue === '' || newValue === '-') {
        target.value = newValue;
        // 触发 input 事件
        const inputEvent = new Event('input', { bubbles: true });
        target.dispatchEvent(inputEvent);
      }
    }
  } else if (dataType === 'UInteger') {
    // 只允许正整数
    const uintegerPattern = /^\d*$/;
    if (uintegerPattern.test(pasteData)) {
      const target = event.target;
      const start = target.selectionStart;
      const end = target.selectionEnd;
      const currentValue = target.value;
      const newValue = currentValue.substring(0, start) + pasteData + currentValue.substring(end);
      
      // 验证新值是否为有效无符号整数
      if (isValidUInteger(newValue) || newValue === '') {
        target.value = newValue;
        // 触发 input 事件
        const inputEvent = new Event('input', { bubbles: true });
        target.dispatchEvent(inputEvent);
      }
    }
  }
};

// 验证数字格式
const validateNumberInput = (value) => {
  // 使用正则表达式验证数字格式（整数或小数）
  const numberRegex = /^-?\d*\.?\d*$/;
  if (!numberRegex.test(value)) {
    // 如果格式不正确，可以在这里处理
  }
};

// 验证 Byte 数字输入（0-99的整数）
const isByteNumber = (event) => {
  const charCode = event.charCode;
  const currentValue = event.target.value;
  
  // 只允许数字 0-9 (48-57)、退格 (8)、删除 (46)
  if ((charCode >= 48 && charCode <= 57) || charCode === 8 || charCode === 0) {
    // 如果当前值已经有2位数字，且输入的是数字，则阻止输入
    if (charCode >= 48 && charCode <= 57 && currentValue.length >= 2) {
      event.preventDefault();
      return false;
    }
    return true;
  } else {
    event.preventDefault();
    return false;
  }
};

// 验证 Byte 格式（0-99的整数）
const validateByteInput = (index,value) => {
  // 如果输入超过2位数字，截取前两位
  if (value && value.length > 2) {
    showByteData[index] = value.substring(0, 2);
    return;
  }
  
  // 处理输入都为0的情况，确保值为0
  if (value && /^0+$/.test(value)) {
    showByteData[index] = '0';
    return;
  }
  
  // 处理前置位为0的情况，移除前置0
  if (value && /^0+[1-9]/.test(value)) {
    // 移除前置0，保留非零部分
    const cleanValue = value.replace(/^0+/, '');
    showByteData[index] = cleanValue;
    return;
  }
  
  // 使用正则表达式验证 Byte 格式（0-99的整数）
  const byteRegex = /^([0-9]|[1-9][0-9])$/;
  if (value && !byteRegex.test(value)) {
    // 如果格式不正确，清空输入框
    showByteData[index] = '';
  }
};
const  byteStringToBuffer = (str)=> {
  const buf = new ArrayBuffer(str.length);
  const view = new Uint8Array(buf);
  for (let i = 0; i < str.length; i++) {
    view[i] = str.charCodeAt(i) & 0xFF;   // 只留低 8 位
  }
  return buf;
}

// 将 ByteString 转换为十六进制字符串显示
const byteStringToHex = (str) => {
  if (!str || typeof str !== 'string') return str;
  
  let hex = '';
  for (let i = 0; i < str.length; i++) {
    const byte = str.charCodeAt(i) & 0xFF;
    hex += byte.toString(16).padStart(2, '0').toUpperCase();
    if (i < str.length - 1) {
      hex += ' '; // 添加空格分隔
    }
  }
  return hex;
}

// 将十六进制字符串转换回 ByteString
const hexToByteString = (hex) => {
  if (!hex || typeof hex !== 'string') return hex;
  
  // 移除空格和分隔符
  const cleanHex = hex.replace(/[^0-9A-Fa-f]/g, '');
  
  if (cleanHex.length % 2 !== 0) {
    return hex;
  }
  
  let result = '';
  for (let i = 0; i < cleanHex.length; i += 2) {
    const byte = parseInt(cleanHex.substr(i, 2), 16);
    result += String.fromCharCode(byte);
  }
  return result;
}

// 将二进制字符串转换为可预览的 Base64 PNG
const binaryStringToBase64 = (binaryString) => {
  if (!binaryString || typeof binaryString !== 'string') return '';
  try {
    const base64 = btoa(binaryString);
    return `data:image/png;base64,${base64}`;
  } catch (e) {
    return '';
  }
}

// 将 Base64 PNG 或纯 base64 转为二进制字符串
const base64ToBinaryString = (base64Data) => {
  if (!base64Data || typeof base64Data !== 'string') return '';
  try {
    if (base64Data.startsWith('data:image/')) {
      const base64 = base64Data.split(',')[1];
      return atob(base64);
    }
    return atob(base64Data);
  } catch (e) {
    return '';
  }
}

// ImagePNG 展示格式化
const formatImagePNGForDisplay = (value) => {
  if (!value) return '';
  if (Array.isArray(value)) {
    return value.map((item) => {
      if (typeof item === 'string') {
        if (item.includes('\x00') || item.includes('\r') || item.includes('\n')) {
          return `[PNG Image - ${item.length} bytes]`;
        }
        return item;
      }
      return String(item);
    }).join(', ');
  }
  if (typeof value === 'string') {
    if (value.includes('\x00') || value.includes('\r') || value.includes('\n')) {
      return `[PNG Image - ${value.length} bytes]`;
    }
    return value;
  }
  return String(value);
}

// 解析 ExpandedNodeId 数据
const parseExpandedNodeId = (value) => {
  if (!value) {
    return {
      identifierType: 0,
      nsIndex: 0,
      value: '',
      serverIndex: 0,
      namespaceUri: null
    };
  }
  
  // 如果已经是对象格式，直接返回
  if (typeof value === 'object') {
    return {
      identifierType: value.identifierType || value._identifierType || 0,
      nsIndex: value.nsIndex || value._nsIndex || 0,
      value: value.value || value._value || '',
      serverIndex: value.serverIndex || value._serverIndex || 0,
      namespaceUri: value.namespaceUri || value._namespaceUri || null
    };
  }
  
  // 如果是字符串，尝试解析
  if (typeof value === 'string') {
    return {
      identifierType: 0,
      nsIndex: 0,
      value: value,
      serverIndex: 0,
      namespaceUri: null
    };
  }
  
  // 默认返回空对象
  return {
    identifierType: 0,
    nsIndex: 0,
    value: '',
    serverIndex: 0,
    namespaceUri: null
  };
}

// 将 ExpandedNodeId 对象转换为字符串格式
const expandedNodeIdToString = (obj) => {
  if (!obj || typeof obj !== 'object') return '';
  
  const parts = [];
  if (obj.identifierType !== undefined) parts.push(`IdentifierType: ${obj.identifierType}`);
  if (obj.nsIndex !== undefined) parts.push(`NsIndex: ${obj.nsIndex}`);
  if (obj.value) parts.push(`Value: ${obj.value}`);
  if (obj.serverIndex !== undefined) parts.push(`ServerIndex: ${obj.serverIndex}`);
  if (obj.namespaceUri) parts.push(`NamespaceUri: ${obj.namespaceUri}`);
  
  return parts.join(', ');
}
const handleMenuClick = (tab) => {
  activeFolder.value = '1';
  // 根据点击的tab获取对应的数据
  const currentItem = state.nodeDetailsData.find(item => item.key === tab.name);
  if (currentItem) {
  }
};

// 获取当前节点信息的方法
const getCurrentNodeInfo = () => {
  const currentItem = state.nodeDetailsData.find(item => item.key === activeFolder.value);
  if (currentItem) {
    return `${currentItem.value} (${currentItem.type})`;
  }
  return '未选择节点';
};
const isAllLetter = (event) => {
  var charCode = event.charCode;
  if (!this.isNotANumber(this.allValueForm[this.curNodeId][this.valueKey])) {
    //number
    if ((charCode >= 45 && charCode <= 57) || charCode == 46 || charCode == 0) {
      return true;
    } else {
      event.preventDefault();
      return false;
    }
  } else {
    //string
    if (
      (charCode >= 65 && charCode <= 90) ||
      (charCode >= 97 && charCode <= 122) ||
      charCode == 0
    ) {
      return true;
    } else {
      event.preventDefault();
      return false;
    }
  }
};
const isNotANumber = (str) => {
  return /^\s*$/.test(str) || !/^\d+(\.\d+)?$/.test(str);
};
const handleVisibleChange =(node, visible)=> {
      let idx = node.id;
      propertyData.value = node.data.NodeId
      currentDrop.value = visible ? idx : null;
    }
const handleSelectionMethodChange = () => {};
const compositionStart = (e) => {
  // this.composing = true;
};
const compositionEnd = (e) => {
  // this.composing = false;
  // this.handleInput(e);
};
const changeNodeId = (e, valueKey) => {
  // this.curNodeId = e;
  // this.valueKey = valueKey;
};
const getMethodMessageList = () => {};
// 处理右键菜单
const handleContextMenu = (event, node, data) => {
  event.preventDefault();
  event.stopPropagation();
  
  if (addDropdownRef.value) {
    addDropdownRef.value.handleOpen();
  }
  event.preventDefault();
  currentNode.value = { node, data };
  if (data.level === 1) {
    // 第一层节点，显示添加URL对话框
    isEditingConnection.value = false;
    urlForm.value = { url: '', urlName: '' };
    // if(data.data.BrowseName  == "FolderType"){
      // FolderDialogVisible.value = true;
    // }
    if(data.data.BrowseName  == 'Servers'){
      urlDialogVisible.value = true;
      isEditingConnection.value = true;
    }
  } else if (data.level === 2) {
    // 第二层节点，显示创建连接对话框
    // isEditingConnection.value = true;
    // 获取所有可用的URL
    availableUrls.value = state.treeData
      .filter((node) => node.urls && node.urls.length)
      .flatMap((node) => node.urls);
    connectionForm.value = { selectedUrl: '' };
    // let parent this.$refs.eltree.getNode(node.data.NodeId).parent;
  }
};
const handleContextMenuAll= (event, node, data) => {
  event.preventDefault();
  currentNode.value = { node, data };
  selectedNodeId.value = node;
  if (data.level === 1) {
    // 第一层节点，显示添加URL对话框
    isEditingConnection.value = false;
    urlForm.value = { url: '', urlName: '' };
    // if(data.data.BrowseName  == "FolderType"){
      // FolderDialogVisible.value = true;
    // }
    if(data.data.BrowseName  == 'Server'){
      urlDialogVisible.value = true;
      isEditingConnection.value = true;
    }
  } else if (data.level === 2) {
    // 第二层节点，显示创建连接对话框
    isEditingConnection.value = true;
    // 获取所有可用的URL
    availableUrls.value = state.treeData
      .filter((node) => node.urls && node.urls.length)
      .flatMap((node) => node.urls);
    connectionForm.value = { selectedUrl: '' };
    // let parent this.$refs.eltree.getNode(node.data.NodeId).parent;
  }
};
const  generateKey =()=> {
  return Date.now().toString(36) + Math.random().toString(36).substring(2);
}
const handleDialogConfirm = useThrottleFn(() => {
  
  urlRef.value.validate( async(valid) => {
    if (valid) {
      const targetNode =  eltreeTop.value.getNode('i=88'); // 通过节点ID获取
      if (!targetNode) {
        return;
      }
      
      // 2. 生成新节点数据
      const newNode =   {
      ReferenceTypeId: 'i=38',
      IsForward: true,
      NodeId: `i=${generateKey()}`, // 生成唯一的节点ID
      BrowseName: urlForm.value.urlName,
      DisplayName: { Text: urlForm.value.urlName },
      NodeClass: 1,
      TypeDefinition: 'i=61',
      url:urlForm.value.url,
       
    };
    selectedTopNodeId.value = newNode.NodeId
    // 设置 selectedNodeId 为根节点，因为新添加的URL需要从根节点开始浏览
    selectedNodeId.value = new UaNodeId(ObjectIds.RootFolder)
    eltreeTop.value.setCurrentKey(null)
    await nextTick()
    eltreeTop.value.setCurrentKey(newNode.NodeId)
    urlDatas.setDataByKey(newNode.NodeId,urlForm.value)

      // 3. 修改数据源
      if (Array.isArray(targetNode.data.children)) {
        targetNode.data.children.push(newNode);
        urlDialogVisible.value = false;

      } else {
        // Vue 3 中直接赋值即可
        targetNode.data.children = [newNode];
        targetNode.expanded = true; // 展开父节点
        urlDialogVisible.value = false;
      }
      let url = urlForm.value.url
     connectFlag.value = true
     rootNodeLoaded.value = false; // 重置根节点加载标志，确保重新加载
     getAllDataTypes()
   
   
     try{
      getBrowseDatas().then(async (firstRes) => {
        let allArr = firstRes?.results || []; // 初始化累计数组
        let continuationPoint = firstRes?.ContinuationPoint;

        // 循环获取后续页数据
        while (continuationPoint) {
          continuationPoints.value = continuationPoint; // 设置继续点
          try {
            const nextRes = await getBrowseNextDatas();

            // 合并数据
            if (nextRes?.results) {
              allArr = allArr.concat(nextRes.results);
            }

            // 更新继续点
            continuationPoint = nextRes?.ContinuationPoint;
          } catch (error) {
            break; // 出错时终止循环
          }
        }
        if(!allArr || allArr.length == 0 ){
          state.bottomTreeData = []
          
          return
        }
        allArr.map((item)=>{
          let typeDefinitionId = item.typeDefinition?._nodeId?.value
          item.NodeClassType = ObjectTypeIds[typeDefinitionId]
          // item.nodeIdNum = item.nodeId?._nodeId?.value
          item.nodeIdNum = item.nodeId?._nodeId.toString()
        })
        const slim = allArr.map(item => ({
          nodeId      : item.nodeId,            // 关键键
          nodeIdNum   : item.nodeId?._nodeId.toString(),
          label       : item.displayName?._text || item.browseName || 'Node',
          isLeaf      : !item.hasChildren,
          nodeClass   : item.nodeClass,
          NodeClassType: item.nodeClass !== undefined ? (ObjectTypeIds[item.typeDefinition?._nodeId?.value] || nodeClassType[item.nodeClass]) : '',
          hasChildren : item.hasChildren,
          children    : []        ,
          hasChildren: !item.hasChildren               // 懒加载留空
          }));
        state.bottomTreeData = slim
        // return resolve(allArr);
     });

     }
     catch(e){
        connectFlag.value = false
     }
     
    
    } else {
        return false;
      }
    });
    
    
    
    })
   const getAllDataTypes = async() => {
        // 获取正确的URL
        const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
        let url = allData?.url || urlForm.value.url;
        
        if (!url) {
          ElMessage.error('请先配置服务器连接');
          return;
        }
        
        let apiConfig = new Configuration({
            basePath: url
        });
        let clientConfig = new UaClientConfiguration(apiConfig);
        let testOpcServer = new UaWebClient(clientConfig);
        let dataTypeDictionary = new UaDataTypeDictionary();
        await dataTypeDictionary.read(testOpcServer);
        let dataTypes = dataTypeDictionary.getDataTypeIds();  
        let arr = []
        dataTypes.map((item)=>{ 
          let dataType = dataTypeDictionary.getDataType(item);
          arr.push(dataType)
          })
        state.allTypesData = arr
   } 

// 处理 Boolean 值变化
const handleBooleanChange = (index, value) => {
  showBooleanData[index] = value;
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    // 更新 _editValue 用于显示
    currentEditingItem.value._editValue = [...showBooleanData];
  }
};

// 处理 Boolean dialog 确认
const handleBooleanDialogConfirm = () => {
  // Boolean 类型校验：确保至少有一个值被选中
  if (showBooleanData.length === 0) {
    ElMessage.error('请至少选择一个布尔值');
    return;
  }
  
  // 检查是否有无效的布尔值
  const hasInvalidValue = showBooleanData.some(value => typeof value !== 'boolean');
  if (hasInvalidValue) {
    ElMessage.error('布尔值格式不正确');
    return;
  }
  
  // 最终更新当前编辑 item 的值
  if (currentEditingItem.value) {
    try {
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = [...showBooleanData];
      
      // 尝试更新原始 value，使用更安全的方式
      if (Array.isArray(currentEditingItem.value.value)) {
        // 使用 splice 来更新数组，避免直接赋值
        currentEditingItem.value.value.splice(0, currentEditingItem.value.value.length, ...showBooleanData);
      } else if (currentEditingItem.value.value && Array.isArray(currentEditingItem.value.value.value)) {
        currentEditingItem.value.value.value.splice(0, currentEditingItem.value.value.value.length, ...showBooleanData);
      }
      
      ElMessage.success('布尔值更新成功！');
      
    } catch (error) {
      ElMessage.error('布尔值更新失败，请重试');
      return;
    }
  }
  
  showBooleanFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
};

// 处理 Double 值变化
const handleDoubleChange = (index, value) => {
  // 根据数据类型进行实时校验
  const dataType = currentEditingItem.value?.dataTypes || currentEditingItem.value?.dataType;
  
  // 如果输入为空，允许清空
  if (!value || value === '') {
  showDoubleData[index] = value;
    if (currentEditingItem.value) {
      currentEditingItem.value._editValue = [...showDoubleData];
    }
    return;
  }
  
  // 检查是否包含汉字
  if (/[\u4e00-\u9fa5]/.test(value)) {
    // 恢复之前的值
    const previousValue = showDoubleData[index];
    showDoubleData[index] = previousValue;
    return;
  }
  
  // 检查是否包含其他非数字字符（除了允许的符号）
  if (dataType === 'Number' || dataType === 'Double') {
    if (!/^-?\d*\.?\d*$/.test(value)) {
      const previousValue = showDoubleData[index];
      showDoubleData[index] = previousValue;
      return;
    }
  } else if (dataType === 'Integer' || dataType === 'Int16' || dataType === 'Int32' || dataType === 'Int64') {
    if (!/^-?\d*$/.test(value)) {
      const previousValue = showDoubleData[index];
      showDoubleData[index] = previousValue;
      return;
    }
  } else if (dataType === 'UInteger') {
    if (!/^\d*$/.test(value)) {
      const previousValue = showDoubleData[index];
      showDoubleData[index] = previousValue;
      return;
    }
  }
  
  // 根据数据类型进行格式校验
  let isValid = false;
  switch (dataType) {
    case 'Number':
    case 'Double':
      isValid = isValidNumber(value);
      break;
    case 'Integer':
    case 'Int16':
    case 'Int32':
    case 'Int64':
      isValid = isValidInteger(value);
      break;
    case 'UInteger':
      isValid = isValidUInteger(value);
      break;
    case 'String':
      isValid = typeof value === 'string';
      break;
    case 'NodeId':
      isValid = isValidNodeId(value);
      break;
    default:
      isValid = true; // 其他类型暂时允许
  }
  
  // 如果校验通过，更新值
  if (isValid) {
    showDoubleData[index] = value;
  } else {
    // 如果校验不通过，恢复之前的值
    const previousValue = showDoubleData[index];
    showDoubleData[index] = previousValue;
    return;
  }
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    currentEditingItem.value._editValue = [...showDoubleData];
  }
};

// 处理 Double dialog 确认
const handleDoubleDialogConfirm = async () => {
  // 先进行表单验证
  const validationRules = getValidationRules();
  const dataType = currentEditingItem.value?.dataTypes || currentEditingItem.value?.dataType;
  
  // 手动验证每个输入值
  let hasError = false;
  const errorMessages = [];
  
  for (let i = 0; i < showDoubleData.length; i++) {
    const value = showDoubleData[i];
    
    // 根据数据类型进行验证
    switch (dataType) {
      case 'DateTime':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请选择日期时间`);
          hasError = true;
        } else if (!isValidDateTime(value)) {
          errorMessages.push(`Value ${i + 1}: 日期时间格式不正确`);
          hasError = true;
        }
        break;
      case 'String':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入文本`);
          hasError = true;
        } else if (value.length < 1 || value.length > 1000) {
          errorMessages.push(`Value ${i + 1}: 文本长度应在1-1000字符之间`);
          hasError = true;
        }
        break;
      case 'Double':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入小数`);
          hasError = true;
        } else if (!isValidDouble(value)) {
          errorMessages.push(`Value ${i + 1}: 请输入有效的小数`);
          hasError = true;
        }
        break;
      case 'Integer':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入整数`);
          hasError = true;
        } else if (!isValidInteger(value)) {
          errorMessages.push(`Value ${i + 1}: 请输入有效的整数`);
          hasError = true;
        }
        break;
      case 'UInteger':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入正整数`);
          hasError = true;
        } else if (!isValidUInteger(value)) {
          errorMessages.push(`Value ${i + 1}: 请输入有效的正整数`);
          hasError = true;
        }
        break;
      case 'Number':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入数字`);
          hasError = true;
        } else if (!isValidNumber(value)) {
          errorMessages.push(`Value ${i + 1}: 请输入有效的数字`);
          hasError = true;
        }
        break;
      case 'Int16':
      case 'Int32':
      case 'Int64':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入整数`);
          hasError = true;
        } else if (!isValidInteger(value)) {
          errorMessages.push(`Value ${i + 1}: 请输入有效的整数`);
          hasError = true;
        }
        break;
      case 'None':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入值`);
          hasError = true;
        }
        break;
      case 'Enumeration':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入枚举值`);
          hasError = true;
        }
        break;
      case 'Structure':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入结构体值`);
          hasError = true;
        }
        break;
      case 'QualifiedName':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入限定名称`);
          hasError = true;
        }
        break;
      case 'NumericRange':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入数值范围`);
          hasError = true;
        }
        break;
      case 'NodeId':
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入NodeId`);
          hasError = true;
        } else if (!isValidNodeId(value)) {
          errorMessages.push(`Value ${i + 1}: 请输入有效的NodeId格式`);
          hasError = true;
        }
        break;
      default:
        if (!value) {
          errorMessages.push(`Value ${i + 1}: 请输入值`);
          hasError = true;
        }
    }
  }
  
  // 如果有验证错误，显示错误信息
  if (hasError) {
    ElMessage.error(errorMessages.join('; '));
    return;
  }
  
  // 验证通过，更新数据
  if (currentEditingItem.value) {
    try {
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = [...showDoubleData];
      
      // 尝试更新原始 value，使用更安全的方式
      if (Array.isArray(currentEditingItem.value.value)) {
        // 使用 splice 来更新数组，避免直接赋值
        currentEditingItem.value.value.splice(0, currentEditingItem.value.value.length, ...showDoubleData);
      } else if (currentEditingItem.value.value && Array.isArray(currentEditingItem.value.value.value)) {
        currentEditingItem.value.value.value.splice(0, currentEditingItem.value.value.value.length, ...showDoubleData);
      }
      
      ElMessage.success('数据更新成功！');
      showDoubleFlag.value = false;
      
    } catch (error) {
      ElMessage.error('数据更新失败，请重试');
    }
  }
  
  showDoubleFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
  // 这里可以添加保存逻辑，比如调用 API 更新值
};

// 处理 UInteger 值变化
const handleUIntegerChange = (index, value) => {
  showUIntegerData[index] = value;
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    // 更新 _editValue 用于显示
    currentEditingItem.value._editValue = [...showUIntegerData];
  }
};

// 处理 UInteger dialog 确认
const handleUIntegerDialogConfirm = () => {
  // UInteger 类型校验
  let hasError = false;
  const errorMessages = [];
  
  for (let i = 0; i < showUIntegerData.length; i++) {
    const value = showUIntegerData[i];
    
    if (!value) {
      errorMessages.push(`Value ${i + 1}: 请输入无符号整数`);
      hasError = true;
    } else if (!isValidUInteger(value)) {
      errorMessages.push(`Value ${i + 1}: 请输入有效的无符号整数`);
      hasError = true;
    }
  }
  
  if (hasError) {
    ElMessage.error(errorMessages.join('; '));
    return;
  }
  
  // 最终更新当前编辑 item 的值
  if (currentEditingItem.value) {
    try {
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = [...showUIntegerData];
      
      // 尝试更新原始 value，使用更安全的方式
      if (Array.isArray(currentEditingItem.value.value)) {
        // 使用 splice 来更新数组，避免直接赋值
        currentEditingItem.value.value.splice(0, currentEditingItem.value.value.length, ...showUIntegerData);
      } else if (currentEditingItem.value.value && Array.isArray(currentEditingItem.value.value.value)) {
        currentEditingItem.value.value.value.splice(0, currentEditingItem.value.value.value.length, ...showUIntegerData);
      }
      
      ElMessage.success('无符号整数更新成功！');
      
    } catch (error) {
      ElMessage.error('无符号整数更新失败，请重试');
      return;
    }
  }
  
  showUIntegerFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
};

// 处理 CarExtras 选择变化
const handleCarExtrasSelectionChange = (index) => {
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    const result = {};
    showCarExtrasData.forEach(item => {
      if (item.selected) {
        result[item.name] = item.value;
      }
    });
    currentEditingItem.value._editValue = result;
  }
};

// 处理 CarExtras 值变化
const handleCarExtrasValueChange = (index) => {
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    const result = {};
    showCarExtrasData.forEach(item => {
      if (item.selected) {
        result[item.name] = item.value;
      }
    });
    currentEditingItem.value._editValue = result;
  }
};

// 处理 CarExtras dialog 确认
const handleCarExtrasDialogConfirm = () => {
  // CarExtras 类型校验：检查选中的项目是否有有效的值
  const selectedItems = showCarExtrasData.filter(item => item.selected);
  
  if (selectedItems.length === 0) {
    ElMessage.error('请至少选择一个CarExtras项目');
    return;
  }
  
  // 检查选中的项目是否有有效的值
  const hasInvalidValue = selectedItems.some(item => {
    if (typeof item.value === 'number') {
      return isNaN(item.value) || item.value < 0;
    }
    return false;
  });
  
  if (hasInvalidValue) {
    ElMessage.error('CarExtras值格式不正确');
    return;
  }
  
  // 最终更新当前编辑 item 的值
  if (currentEditingItem.value) {
    try {
      const result = {};
      showCarExtrasData.forEach(item => {
        if (item.selected) {
          result[item.name] = item.value;
        }
      });
      
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = result;
      
      // 更新原始 value
      if (currentEditingItem.value.value && typeof currentEditingItem.value.value === 'object') {
        if (currentEditingItem.value.value.value !== undefined) {
          currentEditingItem.value.value.value = result;
        } else {
          currentEditingItem.value.value = result;
        }
      } else {
        currentEditingItem.value.value = result;
      }
      
      ElMessage.success('CarExtras更新成功！');
      
    } catch (error) {
      ElMessage.error('CarExtras更新失败，请重试');
      return;
    }
  }
  
  showCarExtrasFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
};

// 处理 UInt64 值变化
const handleUInt64ValueChange = (index) => {
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    // 将位标志转换回 UInt64 值
    let uint64Value = 0n;
    showUInt64Data.forEach(item => {
      if (item.value) {
        uint64Value |= (1n << BigInt(item.bitIndex));
      }
    });
    
    currentEditingItem.value._editValue = uint64Value.toString();
  }
};

// 处理 UInt64 dialog 确认
const handleUInt64DialogConfirm = () => {
  // UInt64 类型校验：检查位索引是否有效
  const hasInvalidBitIndex = showUInt64Data.some(item => {
    return typeof item.bitIndex !== 'number' || item.bitIndex < 0 || item.bitIndex > 63;
  });
  
  if (hasInvalidBitIndex) {
    ElMessage.error('UInt64位索引无效');
    return;
  }
  
  // 最终更新当前编辑 item 的值
  if (currentEditingItem.value) {
    try {
      // 将位标志转换回 UInt64 值
      let uint64Value = 0n;
      showUInt64Data.forEach(item => {
        if (item.value) {
          uint64Value |= (1n << BigInt(item.bitIndex));
        }
      });
      
      const finalValue = uint64Value.toString();
      
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = finalValue;
      
      // 更新 UaVariant 对象的 _value
      if (currentEditingItem.value.value && typeof currentEditingItem.value.value === 'object') {
        if (currentEditingItem.value.value._value !== undefined) {
          currentEditingItem.value.value._value = finalValue;
        } else if (currentEditingItem.value.value.value !== undefined) {
          currentEditingItem.value.value.value = finalValue;
        }
      } else {
        currentEditingItem.value.value = finalValue;
      }
      
      ElMessage.success('UInt64更新成功！');
      
    } catch (error) {
    }
  }
  
  showUInt64Flag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
};

// 处理 Byte 位变化
const handleByteBitChange = () => {
  // 将位数组转换回字节值
  showByteData.value = bitsToByte(showByteData.bits);
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    currentEditingItem.value._editValue = showByteData.value;
  }
};

// 处理 Byte dialog 确认
const handleByteDialogConfirm = () => {
  // Byte 类型校验：检查字节值是否在有效范围内
  const byteValue = showByteData.value;
  
  if (typeof byteValue !== 'number' || isNaN(byteValue)) {
    ElMessage.error('请输入有效的字节值');
    return;
  }
  
  if (byteValue < 0 || byteValue > 255) {
    ElMessage.error('字节值必须在0-255范围内');
    return;
  }
  
  // 检查位标志是否有效
  const hasInvalidBit = showByteData.bits.some(bit => typeof bit !== 'boolean');
  if (hasInvalidBit) {
    ElMessage.error('位标志格式不正确');
    return;
  }
  
  // 最终更新当前编辑 item 的值
  if (currentEditingItem.value) {
    try {
      const finalByteValue = showByteData.value;
      
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = finalByteValue;
      
      // 更新 UaVariant 对象的 _value
      if (currentEditingItem.value.value && typeof currentEditingItem.value.value === 'object') {
        if (currentEditingItem.value.value._value !== undefined) {
          currentEditingItem.value.value._value = finalByteValue;
        } else if (currentEditingItem.value.value.value !== undefined) {
          currentEditingItem.value.value.value = finalByteValue;
        }
      } else {
        currentEditingItem.value.value = finalByteValue;
      }
      
      ElMessage.success('Byte值更新成功！');
      
    } catch (error) {
      ElMessage.error('Byte值更新失败，请重试');
      return;
    }
  }
  
  showByteFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
};

// 处理 ByteString 值变化
const handleByteStringChange = (index, value) => {
  showByteStringData[index] = value;
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    // 更新 _editValue 用于显示
    currentEditingItem.value._editValue = [...showByteStringData];
  }
};

// 验证十六进制输入
const validateHexInput = (index, value) => {
  // 移除空格和分隔符进行验证
  const cleanValue = value.replace(/[^0-9A-Fa-f]/g, '');
  
  if (value && cleanValue.length % 2 !== 0) {
    return;
  }
  
  // 验证是否为有效的十六进制
  const hexRegex = /^[0-9A-Fa-f\s]*$/;
  if (value && !hexRegex.test(value)) {
    return;
  }
};

// 处理 ByteString dialog 确认
const handleByteStringDialogConfirm = () => {
  // ByteString 类型校验：检查十六进制字符串格式
  let hasError = false;
  const errorMessages = [];
  
  for (let i = 0; i < showByteStringData.length; i++) {
    const hexValue = showByteStringData[i];
    
    if (!hexValue) {
      errorMessages.push(`Value ${i + 1}: 请输入十六进制字符串`);
      hasError = true;
    } else if (!isValidHexString(hexValue)) {
      errorMessages.push(`Value ${i + 1}: 请输入有效的十六进制字符串`);
      hasError = true;
    }
  }
  
  if (hasError) {
    ElMessage.error(errorMessages.join('; '));
    return;
  }
  
  // 最终更新当前编辑 item 的值
  if (currentEditingItem.value) {
    try {
      // 将十六进制字符串转换回 ByteString
      const byteStringValues = showByteStringData.map(hex => hexToByteString(hex));
      
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = [...showByteStringData];
      
      // 尝试更新原始 value，使用更安全的方式
      if (Array.isArray(currentEditingItem.value.value)) {
        // 使用 splice 来更新数组，避免直接赋值
        currentEditingItem.value.value.splice(0, currentEditingItem.value.value.length, ...byteStringValues);
      } else if (currentEditingItem.value.value && Array.isArray(currentEditingItem.value.value.value)) {
        currentEditingItem.value.value.value.splice(0, currentEditingItem.value.value.value.length, ...byteStringValues);
      }
      
      ElMessage.success('ByteString更新成功！');
      
    } catch (error) {
      ElMessage.error('ByteString更新失败，请重试');
      return;
    }
  }
  
  showByteStringFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
};

// 处理 ExpandedNodeId 值变化
const handleExpandedNodeIdChange = (index, field, value) => {
  if (showExpandedNodeIdData[index]) {
    showExpandedNodeIdData[index][field] = value;
  }
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    // 更新 _editValue 用于显示
    currentEditingItem.value._editValue = [...showExpandedNodeIdData];
  }
};

// 处理 ExpandedNodeId dialog 确认（最终实现）
;(window).__handleExpandedNodeIdDialogConfirmImpl = () => {
  
  // 最终更新当前编辑 item 的值
  if (currentEditingItem.value) {
    try {
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = [...showExpandedNodeIdData];
      
      // 尝试更新原始 value，使用更安全的方式
      if (Array.isArray(currentEditingItem.value.value)) {
        // 使用 splice 来更新数组，避免直接赋值
        currentEditingItem.value.value.splice(0, currentEditingItem.value.value.length, ...showExpandedNodeIdData);
      } else if (currentEditingItem.value.value && Array.isArray(currentEditingItem.value.value.value)) {
        currentEditingItem.value.value.value.splice(0, currentEditingItem.value.value.value.length, ...showExpandedNodeIdData);
      }
      
    } catch (error) {
      // 即使更新原始值失败，至少确保 _editValue 已更新
    }
  }
  
  showExpandedNodeIdFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
  // 这里可以添加保存逻辑，比如调用 API 更新值
};

const handleDialogFolder = () => {
     activeFolder.value = currentNode.value.data.data.NodeId
    FolderDialogVisible.value = false
  state.folderArr.push(currentNode.value.data.data)

};
const formatDateToISO8601Extended = () => {
  const now = new Date();
  const isoString = now.toISOString();
  // 分割毫秒部分并补零至 7 位
  const [dateTime, ms] = isoString.split(/[.,]/);
  const paddedMs = (ms || '000').substring(0, 3).padEnd(7, '0');
  return `${dateTime}.${paddedMs}Z`; // 保留 Z 表示 UTC
};

const loadNode = (node, resolve) => { 
  let nodeClassToReturn = 'Object'; 
  let id = node.data.nodeId?._nodeId?._value
  let nodeid2 = node.data.nodeId;
  fixedNodeId.value = id
  ensureFixedNodeExpanded()
  selectedNodeId.value = nodeid2; 
  getBrowseDatas().then(async (firstRes) => {
    let allArr = firstRes?.results || []; // 初始化累计数组
    let continuationPoint = firstRes?.ContinuationPoint;

    // 循环获取后续页数据
    while (continuationPoint) {
      continuationPoints.value = continuationPoint; // 设置继续点
      try {
        const nextRes = await getBrowseNextDatas();

        // 合并数据
        if (nextRes?.results) {
          allArr = allArr.concat(nextRes.results);
        }

        // 更新继续点
        continuationPoint = nextRes?.ContinuationPoint;
      } catch (error) {
        break; // 出错时终止循环
      }
    }
    
     // 为每个节点添加dataTypes信息
     allArr.map((item) => {
       let typeDefinitionId = item.typeDefinition?._nodeId?.value
       if(!typeDefinitionId){
         item.NodeClassType = nodeClassType[item.nodeClass]
       }else{
         item.NodeClassType = ObjectTypeIds[typeDefinitionId]
       }
       
       item.nodeIdNum = item.nodeId?._nodeId.toString()
       
       // 从allTypesData中查找对应的数据类型信息
       let temp = state.allTypesData.filter((idx)=>{
         let typeId = idx.nodeId.toString();
         let dataTypeId = item.nodeIdNum;
         return typeId === dataTypeId;
       });
       
       // 正确赋值dataTypes信息
       if (temp[0]) {
         item.dataTypesObj = temp[0];
         // 优先使用_parentType的_browseName，否则使用browseName
         item.dataTypes = temp[0]?._parentType?._browseName || temp[0]?._browseName || temp[0]?.browseName;
       } else {
         item.dataTypes = 'Unknown';
         item.dataTypesObj = null;
       }
     })
    // 返回所有累计数据
    const slim = allArr.map(item => ({
        nodeId      : item.nodeId,            // 关键键
        nodeIdNum   : item.nodeId?._nodeId.toString(),
        label       : item.displayName?._text || item.browseName || 'Node',
        isLeaf      : true,
        nodeClass   : item.nodeClass,
        NodeClassType: item.nodeClass  ,
        hasChildren : item.hasChildren,
        children    : []     ,
        hasChildren: true,  
        dataTypes: item.dataTypes,
        dataTypesObj: item.dataTypesObj,
        // 懒加载留空
      }));

      resolve(slim);              // 传给 el-tree，秒级完成
  });

  
};
 
  
const opcClientCache = ref(new Map());
 
// 分层缓存实现
const createLayeredCache = () => {
  const caches = [
    new Map(), // 层级 0-1
    new Map(), // 层级 2-3  
    new Map(), // 层级 4+
  ];
  
  const getCacheForLevel = (level) => {
    if (level <= 1) return caches[0];
    if (level <= 3) return caches[1];
    return caches[2];
  };
  
  return {
    get(nodeId, level) {
      return getCacheForLevel(level).get(nodeId);
    },
    
    set(nodeId, data, level) {
      const cache = getCacheForLevel(level);
      
      // 深层级缓存使用更严格的限制
      if (level >= 4 && cache.size > 200) {
        const firstKey = Array.from(cache.keys())[0];
        cache.delete(firstKey);
      }
      
      cache.set(nodeId, data);
    },
    
    // 定期清理深层缓存
    cleanup() {
      caches[2].clear();
    }
  };
};

// 初始化分层缓存
const layeredCache = createLayeredCache();
// 深层级节点专用优化
const loadDeepNode = async (node, resolve) => {
  const nodeId = node.data?.nodeId?._nodeId?._value || node.data?.nodeId;
  const nodeLevel = node.level;
  
   
  
  // ⚡ 优化1: 深层级节点使用更小的页面大小
  const pageSize = getOptimalPageSize(nodeLevel);
  
  // ⚡ 优化2: 深层级节点跳过某些节点类型
  const nodeClassToReturn = getNodeClassFilter(nodeLevel);
  
  try {
    // ⚡ 优化3: 深层级节点使用更激进的缓存
    if (nodeLevel >= 3) {
      const cached = getDeepNodeFromCache(nodeId, nodeLevel);
      if (cached) {
        console.log(`💾 Using deep cache for level ${nodeLevel}`);
        resolve(cached);
        return;
      }
    }
    
    // 执行浏览操作
    const result = await browseWithRetry(targetNodeId, nodeClassToReturn, pageSize, nodeLevel);
    
    // ⚡ 优化4: 深层级节点简化数据处理
    const processedData = processDeepNodeData(result.results, nodeLevel, nodeId);
    
    // ⚡ 优化5: 深层级节点使用压缩缓存
    if (nodeLevel >= 4) {
      cacheDeepNodeData(nodeId, processedData, nodeLevel);
    }
    
    resolve(processedData);
  } catch (error) {
    console.error(`❌ Deep node load error (level ${nodeLevel}):`, error);
    resolve([{
      nodeIdNum: `${nodeId}-fallback`,
      label: `Load Error - Level ${nodeLevel}`,
      BrowseName: `Load Error - Level ${nodeLevel}`,
      displayName: { _text: `Load Error - Level ${nodeLevel}` },
      isLeaf: true,
      hasChildren: false,
      error: true
    }]);
  }
};

// 🚀 智能页面大小配置
const getOptimalPageSize = (level) => {
  // ⚡ 优化1: 基于网络状况和设备性能动态调整
  const isSlowNetwork = navigator.connection?.effectiveType === 'slow-2g' || 
                       navigator.connection?.effectiveType === '2g';
  const isLowEndDevice = navigator.hardwareConcurrency < 4;
  
  // 基础页面大小配置
  const baseSizes = {
    0: 120, // 根节点 - 增加初始加载量
    1: 100, // 第一层
    2: 80,  // 第二层  
    3: 60,  // 第三层
    4: 40,  // 第四层
    5: 30,  // 第五层
    6: 20   // 第六层及以上
  };
  
  let size = baseSizes[level] || 20;
  
  // ⚡ 优化2: 根据网络和设备状况调整
  if (isSlowNetwork) {
    size = Math.floor(size * 0.6); // 慢网络减少60%
  } else if (isLowEndDevice) {
    size = Math.floor(size * 0.8); // 低端设备减少20%
  }
  
  // ⚡ 优化3: 基于缓存命中率动态调整
  const cacheStats = nodeCache.value?.getStats?.();
  if (cacheStats && cacheStats.size > 200) {
    size = Math.floor(size * 1.2); // 缓存充足时增加页面大小
  }
  
  console.log(`📊 Optimized page size for level ${level}: ${size} (network: ${navigator.connection?.effectiveType || 'unknown'})`);
  return size;
};

// 修复节点类型过滤
const getNodeClassFilter = (level) => {
  // 确保所有层级都能加载必要的节点类型
  const baseTypes = NodeClass.Object | NodeClass.ObjectType | NodeClass.View;
  
  // 深层级节点也需要方法和其他类型
  if (level >= 3) {
    return Number(
      baseTypes |
      NodeClass.Method |
      NodeClass.VariableType |
      NodeClass.ReferenceType
    );
  }
  
  // 浅层节点加载所有类型
  return Number(
    baseTypes |
    NodeClass.Method |
    NodeClass.DataType |
    NodeClass.VariableType |
    NodeClass.ReferenceType
  );
};

// 根据层级过滤节点类型
 
// 🚀 高性能请求批处理系统
const createRequestBatcher = () => {
  const batch = new Map();
  const requestQueue = [];
  let batchTimeout = null;
  let isProcessing = false;
  
  return {
    async addRequest(nodeId, nodeLevel, browseFunction) {
      // ⚡ 优化1: 深层级节点使用快速通道
      if (nodeLevel >= 4) {
        return browseFunction();
      }
      
      // ⚡ 优化2: 检查重复请求
      if (batch.has(nodeId)) {
        return batch.get(nodeId);
      }
      
      const promise = new Promise((resolve, reject) => {
        requestQueue.push({
          nodeId,
          nodeLevel,
          browseFunction,
          resolve,
          reject,
          timestamp: Date.now()
        });
        
        // ⚡ 优化3: 智能批处理时机
        if (!batchTimeout) {
          const delay = requestQueue.length > 5 ? 20 : 50; // 请求多时更快处理
          batchTimeout = setTimeout(() => this.executeBatch(), delay);
        }
      });
      
      batch.set(nodeId, promise);
      return promise;
    },
    
    async executeBatch() {
      if (isProcessing || requestQueue.length === 0) {
        return;
      }
      
      isProcessing = true;
      batchTimeout = null;
      
      console.log(`🚀 Starting batch processing: ${requestQueue.length} requests in queue`);
      
      try {
        // ⚡ 优化4: 并发处理批处理请求
        const requests = requestQueue.splice(0, Math.min(10, requestQueue.length));
        console.log(`📦 Processing ${requests.length} requests in this batch`);
        
        const promises = requests.map(req => 
          req.browseFunction()
            .then(result => ({ success: true, result, req }))
            .catch(error => ({ success: false, error, req }))
        );
        
        const results = await Promise.allSettled(promises);
        
        // ⚡ 优化5: 处理结果
        results.forEach((result, index) => {
          const req = requests[index];
          
          // 🚀 添加安全检查
          if (!req || typeof req.resolve !== 'function' || typeof req.reject !== 'function') {
            console.error('Invalid request object:', req);
            return;
          }
          
          try {
            if (result.status === 'fulfilled') {
              const { success, result: data, error } = result.value;
              if (success) {
                req.resolve(data);
              } else {
                req.reject(error);
              }
            } else {
              req.reject(result.reason);
            }
          } catch (error) {
            console.error('Error processing request result:', error);
            req.reject(error);
          }
          
          batch.delete(req.nodeId);
        });
        
        console.log(`🚀 Batch processed ${requests.length} requests`);
        
      } catch (error) {
        console.error('Batch processing error:', error);
        
        // 🚀 确保所有请求都被处理，即使出现错误
        requests.forEach(req => {
          try {
            if (req && typeof req.reject === 'function') {
              req.reject(error);
            }
          } catch (rejectError) {
            console.error('Error rejecting request:', rejectError);
          }
          batch.delete(req.nodeId);
        });
      } finally {
        isProcessing = false;
        
        // ⚡ 优化6: 继续处理剩余请求
        if (requestQueue.length > 0) {
          setTimeout(() => this.executeBatch(), 10);
        }
      }
    },
    
    // 获取批处理统计
    getStats() {
      return {
        queueLength: requestQueue.length,
        batchSize: batch.size,
        isProcessing
      };
    },
    
    // 清理批处理
    clear() {
      requestQueue.forEach(req => req.reject(new Error('Batch cleared')));
      requestQueue.length = 0;
      batch.clear();
      if (batchTimeout) {
        clearTimeout(batchTimeout);
        batchTimeout = null;
      }
    }
  };
};

const requestBatcher = createRequestBatcher();
// 优化深层级节点的数据处理
const processDeepNodeData = (items, level, parentNodeId) => {
  if (level >= 3) {
    // ⚡ 深层级节点使用轻量级数据格式
    return items.map((item, index) => {
      const nodeIdValue = item.nodeId?._nodeId?.value || `deep-${parentNodeId}-${index}`;
      
      // 极简数据格式，确保字段名与模板匹配
      const label = item.displayName?._text || item.browseName || `Node ${index}`;
      return {
        // 只保留必要字段
        nodeIdNum: nodeIdValue,
        label: label, // Element Plus tree 使用的字段
        BrowseName: label, // 模板中使用的字段
        displayName: { _text: label }, // 模板中使用的字段
        isLeaf: level >= 5 ? true : (item.hasChildren || false), // 假设5层以上都是叶子节点
        hasChildren: level >= 5 ? false : (item.hasChildren || false),
        nodeClass: item.nodeClass,
        
        // 延迟加载其他数据
        _raw: level >= 4 ? null : item, // 深层级不保存原始数据
        _loadTime: Date.now()
      };
    });
  }
  
  // 浅层节点使用完整处理，确保字段名与模板匹配
  return items.map((item, index) => {
    const label = item.displayName?._text || item.browseName || item.displayName?.text || `Node ${index}`;
    return {
    ...item,
    nodeIdNum: item.nodeId?._nodeId?.value || `temp-${parentNodeId}-${index}`,
      label: label, // Element Plus tree 使用的字段
      BrowseName: label, // 模板中使用的字段
      displayName: { _text: label }, // 模板中使用的字段
    isLeaf: !item.hasChildren,
    hasChildren: item.hasChildren || false,
    NodeClassType: item.nodeClass !== undefined ? 
      (nodeClassType[item.nodeClass] || `NodeClass_${item.nodeClass}`) : undefined
    };
  });
};
// 🚀 高性能优化的数据处理函数 - 保持原有功能，只优化性能
const processNodeDataOptimized = async (items, level, parentNodeId) => {
  // ⚡ 保持原有功能：完整的数据处理逻辑
  return new Promise((resolve) => {
    if (items.length === 0) {
      resolve([]);
      return;
    }
    
    // 🚀 性能优化：限制超大数据集，但保持功能完整
    const MAX_ITEMS = 2000; // 适当提高限制，保持更多数据
    if (items.length > MAX_ITEMS) {
      console.warn(`⚠️ 数据量过大 (${items.length} 条)，限制为 ${MAX_ITEMS} 条`);
      items = items.slice(0, MAX_ITEMS);
    }
    
    const processedItems = [];
    let currentIndex = 0;
    const CHUNK_SIZE = 20; // 适中的块大小，保持处理效率
    
    // ⚡ 性能优化：使用时间切片，但保持原有处理逻辑
    const processChunk = () => {
      const startTime = performance.now();
      
      const endIndex = Math.min(currentIndex + CHUNK_SIZE, items.length);
      const chunk = items.slice(currentIndex, endIndex);
      
      // 🚀 保持原有的完整数据处理逻辑
      const processedChunk = chunk.map((item, index) => {
        const globalIndex = currentIndex + index;
        
        // 保持原有的节点ID提取逻辑
    const nodeIdValue = item.nodeId?._nodeId?.value || 
                       item.nodeId?._value || 
                       item.nodeId ||
                           `node-${parentNodeId}-${globalIndex}-${level}`;
    
        // 保持原有的标签生成逻辑
    const label = item.displayName?._text || 
                 item.browseName || 
                 item.displayName?.text ||
                 item.name ||
                     `Node_${globalIndex}`;
    
        // 保持原有的智能子节点判断逻辑
    let hasChildren = item.hasChildren;
    if (hasChildren === undefined) {
          // 保持原有的判断逻辑
      if (item.nodeClass === NodeClass.Method || 
          item.nodeClass === NodeClass.DataType ||
          item.nodeClass === NodeClass.VariableType) {
        hasChildren = false;
      } else {
            hasChildren = level < 6; // 保持原有的层级判断
          }
        }
        
        // 🚀 保持原有的完整对象结构
        const processedItem = {
      nodeIdNum: nodeIdValue,
          label: label, // Element Plus tree 使用的字段
          BrowseName: label, // 模板中使用的字段
          displayName: { _text: label }, // 模板中使用的字段
          isLeaf: !hasChildren,
      hasChildren: hasChildren,
          nodeClass: item.nodeClass,
      NodeClassType: item.nodeClass !== undefined ? 
        (nodeClassType[item.nodeClass] || `NodeClass_${item.nodeClass}`) : undefined,
      nodeId: item.nodeId || nodeIdValue,
          // 保持原有的字段兼容性
          browseName: item.browseName || label,
          name: item.name || label,
          // 保持原有的原始数据保留逻辑
          ...(level < 3 ? { _raw: item } : {}) // 保持原有的浅层节点原始数据
        };
        
        // 保持原有的调试信息
        if (globalIndex < 3) {
          console.log(`🔍 Node ${globalIndex} hasChildren:`, {
            original: item.hasChildren,
            processed: hasChildren,
            nodeClass: item.nodeClass,
        level: level,
            label: label
          });
        }
        
        return processedItem;
      });
      
      processedItems.push(...processedChunk);
      currentIndex = endIndex;
      
      const processingTime = performance.now() - startTime;
      
      // 🚀 检查是否完成
      if (currentIndex >= items.length) {
        console.log(`✅ 数据处理完成: ${processedItems.length} 条，总耗时: ${processingTime.toFixed(2)}ms`);
        resolve(processedItems);
        return;
      }
      
      // ⚡ 性能优化：只在必要时让出控制权
      if (processingTime > 8) { // 提高阈值，减少不必要的让出
        setTimeout(processChunk, 0);
      } else {
        processChunk(); // 继续处理下一块
      }
    };
    
    // 开始处理
    processChunk();
  });
};

// 🚀 流式渲染函数 - 保持原有功能，优化性能
const renderDataInChunks = async (data, resolve, nodeId) => {
  // ⚡ 性能优化：适中的块大小，保持渲染效率
  const chunkSize = 15; // 保持原有的15个节点，但优化调度
  let renderedCount = 0;
  const allRendered = [];
  
  const renderNextChunk = () => {
    const chunk = data.slice(renderedCount, renderedCount + chunkSize);
    allRendered.push(...chunk);
    renderedCount += chunkSize;
    
    // 🚀 保持原有功能：立即返回已渲染的数据
    resolve([...allRendered]);
    
    if (renderedCount < data.length) {
      // ⚡ 性能优化：使用更高效的调度策略
      if (window.requestIdleCallback) {
        window.requestIdleCallback(renderNextChunk, { timeout: 50 });
      } else {
        setTimeout(renderNextChunk, 20); // 保持原有的20ms延迟
      }
    } else {
      console.log(`🎉 流式渲染完成: ${allRendered.length} 个节点`);
    }
  };
  
  // 立即渲染第一批
  renderNextChunk();
};
// 重试机制用于处理加载失败的情况
const createRetryHandler = () => {
  const retryCounts = new Map();
  const MAX_RETRIES = 2;
  
  return {
    shouldRetry(nodeId) {
      const count = retryCounts.get(nodeId) || 0;
      return count < MAX_RETRIES;
    },
    
    recordRetry(nodeId) {
      const count = retryCounts.get(nodeId) || 0;
      retryCounts.set(nodeId, count + 1);
    },
    
    clearRetry(nodeId) {
      retryCounts.delete(nodeId);
    },
    
    getRetryCount(nodeId) {
      return retryCounts.get(nodeId) || 0;
    }
  };
};

const retryHandler = createRetryHandler();

// 🚀 高性能智能缓存系统
const setupIntelligentCache = () => {
  const cache = new Map();
  const accessTimes = new Map();
  const accessCounts = new Map();
  const nodeLevels = new Map();
  
  return {
    get(nodeId) {
      if (cache.has(nodeId)) {
        const now = Date.now();
        accessTimes.set(nodeId, now);
        accessCounts.set(nodeId, (accessCounts.get(nodeId) || 0) + 1);
        return cache.get(nodeId);
      }
      return null;
    },
    
    set(nodeId, data, level = 0) {
      cache.set(nodeId, data);
      const now = Date.now();
      accessTimes.set(nodeId, now);
      accessCounts.set(nodeId, 1);
      nodeLevels.set(nodeId, level);
      
      // ⚡ 智能缓存清理策略
      if (cache.size > 300) {
        this.cleanupCache();
      }
    },
    
    cleanupCache() {
      const entries = Array.from(cache.keys()).map(nodeId => ({
        nodeId,
        accessTime: accessTimes.get(nodeId) || 0,
        accessCount: accessCounts.get(nodeId) || 0,
        level: nodeLevels.get(nodeId) || 0
      }));
      
      // ⚡ 智能清理算法：优先保留高频访问和浅层节点
      entries.sort((a, b) => {
        // 计算综合评分：访问频率 + 层级权重 + 时间权重
        const scoreA = a.accessCount * 10 + (6 - a.level) * 5 + (Date.now() - a.accessTime) / 10000;
        const scoreB = b.accessCount * 10 + (6 - b.level) * 5 + (Date.now() - b.accessTime) / 10000;
        return scoreA - scoreB;
      });
      
      // 删除评分最低的50个条目
      const toDelete = entries.slice(0, 50);
      toDelete.forEach(entry => {
        cache.delete(entry.nodeId);
        accessTimes.delete(entry.nodeId);
        accessCounts.delete(entry.nodeId);
        nodeLevels.delete(entry.nodeId);
      });
      
      console.log(`🧹 Intelligent cache cleanup: removed ${toDelete.length} entries`);
    },
    
    clear() {
      cache.clear();
      accessTimes.clear();
      accessCounts.clear();
      nodeLevels.clear();
    },
    
    clearNode(nodeId) {
      cache.delete(nodeId);
      accessTimes.delete(nodeId);
      accessCounts.delete(nodeId);
      nodeLevels.delete(nodeId);
    },
    
    // 获取缓存统计信息
    getStats() {
      return {
        size: cache.size,
        levels: Array.from(nodeLevels.values()).reduce((acc, level) => {
          acc[level] = (acc[level] || 0) + 1;
          return acc;
        }, {})
      };
    }
  };
};

// 🚀 智能预加载系统
const createPreloadManager = () => {
  const preloadQueue = new Set();
  const preloadCache = new Map();
  const userBehaviorPattern = new Map();
  
  return {
    // 记录用户行为模式
    recordUserBehavior(nodeId, action) {
      const pattern = userBehaviorPattern.get(nodeId) || { expand: 0, click: 0, time: Date.now() };
      pattern[action] = (pattern[action] || 0) + 1;
      pattern.time = Date.now();
      userBehaviorPattern.set(nodeId, pattern);
    },
    
    // 预测需要预加载的节点
    predictPreloadNodes(currentNodeId, level) {
      const predictions = [];
      
      // 基于用户行为模式预测
      const behavior = userBehaviorPattern.get(currentNodeId);
      if (behavior && behavior.expand > 2) {
        // 用户经常展开此节点，预加载其子节点
        predictions.push({ nodeId: currentNodeId, priority: 'high', reason: 'frequent_expand' });
      }
      
      // 基于层级预测
      if (level < 3) {
        // 浅层节点更可能被访问
        predictions.push({ nodeId: currentNodeId, priority: 'medium', reason: 'shallow_level' });
      }
      
      return predictions;
    },
    
    // 执行预加载
    async executePreload(predictions) {
      const preloadPromises = predictions.map(async (prediction) => {
        if (preloadQueue.has(prediction.nodeId) || preloadCache.has(prediction.nodeId)) {
          return;
        }
        
        preloadQueue.add(prediction.nodeId);
        
        try {
          // 使用较低的优先级进行预加载
          const data = await this.preloadNodeData(prediction.nodeId);
          preloadCache.set(prediction.nodeId, data);
        } catch (error) {
          console.warn(`Preload failed for ${prediction.nodeId}:`, error);
        } finally {
          preloadQueue.delete(prediction.nodeId);
        }
      });
      
      // 使用 Promise.allSettled 避免单个失败影响整体
      await Promise.allSettled(preloadPromises);
    },
    
    // 预加载节点数据（简化版本）
    async preloadNodeData(nodeId) {
      // 这里可以实现实际的预加载逻辑
      // 暂时返回空数组
      return [];
    },
    
    // 获取预加载的数据
    getPreloadedData(nodeId) {
      return preloadCache.get(nodeId);
    },
    
    // 清理预加载缓存
    clearPreloadCache() {
      preloadCache.clear();
      preloadQueue.clear();
    }
  };
};

// 🚀 初始化智能缓存和预加载系统
const intelligentCache = setupIntelligentCache();
const preloadManager = createPreloadManager();

// 替换原来的 nodeCache 为智能缓存
const nodeCache = ref(intelligentCache);

 

// 🚀 简化的性能监控 - 只监控关键性能问题
const performanceMonitor = {
  longTasks: [],
  
  startMonitoring() {
    // 只在开发环境启动监控
    if (process.env.NODE_ENV === 'development' && 'PerformanceObserver' in window) {
      try {
        const observer = new PerformanceObserver((list) => {
          for (const entry of list.getEntries()) {
            if (entry.duration > 100) { // 只监控超过100ms的严重长任务
              console.warn(`⚠️ 检测到长任务: ${entry.duration.toFixed(2)}ms - ${entry.name}`);
            }
          }
        });
        
        observer.observe({ entryTypes: ['longtask'] });
        console.log('🚀 性能监控已启动 (仅开发环境)');
      } catch (error) {
        console.warn('性能监控启动失败:', error);
      }
    }
  }
};

// 启动性能监控
// performanceMonitor.startMonitoring();

// 修复缓存策略
const setupFixedCache = () => {
  const cache = new Map();
  const accessTimes = new Map();
  
  return {
    get(nodeId) {
      if (cache.has(nodeId)) {
        accessTimes.set(nodeId, Date.now());
        return cache.get(nodeId);
      }
      return null;
    },
    
    set(nodeId, data) {
      // ⚡ 修复: 确保即使深层节点数据也被缓存
      cache.set(nodeId, data);
      accessTimes.set(nodeId, Date.now());
      
      // 简单的缓存清理 - 基于大小而非层级
      if (cache.size > 500) {
        const entries = Array.from(accessTimes.entries());
        entries.sort((a, b) => a[1] - b[1]); // 按访问时间排序
        
        // 删除最旧的100个条目
        for (let i = 0; i < Math.min(100, entries.length); i++) {
          cache.delete(entries[i][0]);
          accessTimes.delete(entries[i][0]);
        }
        
        console.log('🧹 Cache cleaned, removed 100 oldest entries');
      }
    },
    
    clear() {
      cache.clear();
      accessTimes.clear();
    },
    
    // 专门清理某个节点的缓存（用于重试）
    clearNode(nodeId) {
      cache.delete(nodeId);
      accessTimes.delete(nodeId);
    }
  };
};

// 🚀 高性能优化的 loadNode 函数
const loadNodetemp = async (node, resolve) => {
  const nodeId = node.data?.nodeId?._nodeId?._value || node.data?.nodeId;
  const nodeLevel = node.level;
  
  console.log(`🚀 Loading node: ${nodeId} at level ${nodeLevel}`, node.data);
  
  if (!nodeId) {
    console.log('❌ No nodeId found');
    resolve([]);
    return;
  }
  
  // ⚡ 优化1: 快速叶子节点检查 - 同时检查 isLeaf 和 hasChildren
  if (node.data?.isLeaf === true || node.data?.hasChildren === false) {
    console.log(`🍃 Node ${nodeId} is leaf (isLeaf: ${node.data?.isLeaf}, hasChildren: ${node.data?.hasChildren}), skipping load`);
    resolve([]);
    return;
  }
  
  // ⚡ 优化2: 智能缓存检查
    const cachedData = nodeCache.value.get(nodeId);
  if (cachedData) {
    console.log(`💾 Using cached data for node ${nodeId}, level ${nodeLevel}`);
    
    // 🚀 智能预加载：在缓存命中时触发预加载
    if (preloadManager && nodeLevel < 4) {
      const predictions = preloadManager.predictPreloadNodes(nodeId, nodeLevel);
      if (predictions.length > 0) {
        // 异步执行预加载，不阻塞当前响应
        preloadManager.executePreload(predictions).catch(err => 
          console.warn('Preload execution failed:', err)
        );
      }
    }
    
    resolve(cachedData);
    return;
  }
  
  // ⚡ 优化3: 防止重复加载
  if (loadingNodes.value.has(nodeId)) {
    console.log(`⏳ Node ${nodeId} already loading`);
    resolve([]);
    return;
  }
  
  loadingNodes.value.add(nodeId);
  
  try {
    // ⚡ 优化4: 快速连接检查
    if (!connectFlag.value) {
      console.warn('❌ No connection established');
      resolve([]);
      return;
    }
    
    // ⚡ 优化5: 智能节点ID处理
    let targetNodeId = node.data.nodeId;
    if (nodeLevel === 0 && !targetNodeId) {
      targetNodeId = new UaNodeId(ObjectIds.RootFolder);
    }
    
    if (!targetNodeId) {
      console.warn(`❌ No target node ID found for level ${nodeLevel}`, node.data);
      resolve([]);
      return;
    }
    
    // ⚡ 优化6: 动态页面大小和超时策略
    const pageSize = getOptimalPageSize(nodeLevel);
    const timeout = Math.min(5000 + nodeLevel * 1000, 12000); // 动态超时
    
    // ⚡ 优化7: 客户端复用
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    const baseUrl = allData?.url || urlForm.value.url;
    
    if (!baseUrl) {
      console.warn('❌ No valid URL configuration found');
      resolve([]);
      return;
    }
    
    let opcClient = opcClientCache.value.get(baseUrl);
    if (!opcClient) {
      const apiConfig = new Configuration({ basePath: baseUrl });
      const clientConfig = new UaClientConfiguration(apiConfig);
      opcClient = new UaWebClient(clientConfig);
      opcClientCache.value.set(baseUrl, opcClient);
    }
    
    // ⚡ 优化8: 智能节点类型过滤
    const nodeClassToReturn = getNodeClassFilter(nodeLevel);
    
    // ⚡ 优化9: 智能请求处理 - 使用批处理系统
    const browseFunction = () => opcClient.browseChild(targetNodeId, nodeClassToReturn, pageSize);
    
    let result;
    if (nodeLevel < 4) {
      // 浅层节点使用批处理
      result = await requestBatcher.addRequest(nodeId, nodeLevel, browseFunction);
    } else {
      // 深层节点直接执行
      const browsePromise = browseFunction();
    const timeoutPromise = new Promise((_, reject) => {
      setTimeout(() => reject(new Error(`Browse timeout after ${timeout}ms`)), timeout);
    });
      result = await Promise.race([browsePromise, timeoutPromise]);
    }
    
    if (!result?.results) {
      console.log(`❌ No results for node ${nodeId} at level ${nodeLevel}`);
      const emptyNode = [{
        nodeIdNum: `${nodeId}-empty`,
        label: 'No Data',
        BrowseName: 'No Data',
        displayName: { _text: 'No Data' },
        isLeaf: true,
        hasChildren: false,
        isEmpty: true
      }];
      nodeCache.value.set(nodeId, emptyNode);
      resolve(emptyNode);
      return;
    }
    
    const allArr = result.results || [];
    console.log(`📥 Received ${allArr.length} items for node ${nodeId} at level ${nodeLevel}`);
    
    if (allArr.length === 0) {
      console.log(`📭 Empty results for node ${nodeId}`);
      const leafNode = [{ 
        ...node.data,
        nodeIdNum: `${nodeId}-leaf`,
        label: node.data.label || 'Empty Folder',
        BrowseName: node.data.label || 'Empty Folder',
        displayName: { _text: node.data.label || 'Empty Folder' },
        isLeaf: true,
        hasChildren: false,
        isEmpty: true
      }];
      nodeCache.value.set(nodeId, leafNode);
      resolve(leafNode);
      return;
    }
    
    // ⚡ 优化10: 高效数据处理和流式渲染
    const processedData = await processNodeDataOptimized(allArr, nodeLevel, nodeId);
    
    console.log(`✅ Processed ${processedData.length} items for level ${nodeLevel}`);
    
    // ⚡ 优化11: 智能缓存策略
    nodeCache.value.set(nodeId, processedData, nodeLevel);
    
    // 🚀 记录用户行为用于预加载预测
    if (preloadManager) {
      preloadManager.recordUserBehavior(nodeId, 'expand');
    }
    
    // ⚡ 优化12: 保持原有的渲染策略，但优化性能
    if (processedData.length > 50) {
      // 保持原有的50个节点阈值
      console.log(`🚀 使用流式渲染: ${processedData.length} 个节点`);
      await renderDataInChunks(processedData, resolve, nodeId);
    } else {
      // 少量数据直接渲染
      console.log(`⚡ 直接渲染: ${processedData.length} 个节点`);
      resolve(processedData);
    }
    
  } catch (error) {
    console.error(`❌ Load error for node ${nodeId} at level ${nodeLevel}:`, error);
    
    // ⚡ 优化13: 智能错误处理
    const errorNode = [{
      nodeIdNum: `${nodeId}-error-${Date.now()}`,
      label: `Load Error - Click to retry`,
      BrowseName: `Load Error - Click to retry`,
      displayName: { _text: `Load Error - Click to retry` },
      isLeaf: false,
      hasChildren: true,
      error: true,
      originalNodeId: nodeId,
      retry: true
    }];
    
    resolve(errorNode);
  } finally {
    loadingNodes.value.delete(nodeId);
  }
};

// 🚀 为 el-tree-v2 优化的 loadNode 函数
const optimizedLoadNode = async (node, resolve) => {
  const nodeId = node.data?.nodeId?._nodeId?._value || node.data?.nodeId;
  const nodeLevel = node.level;
  
  console.log(`🚀 OptimizedLoadNode: ${nodeId} at level ${nodeLevel}`, node.data);
  
  if (!nodeId) {
    console.log('❌ No nodeId found');
    resolve([]);
    return;
  }
  
  // ⚡ 优化1: 快速叶子节点检查
  if (node.data?.isLeaf === true || node.data?.hasChildren === false) {
    console.log(`🍃 Node ${nodeId} is leaf, skipping load`);
    resolve([]);
    return;
  }
  
  // ⚡ 优化2: 智能缓存检查
  const cachedData = nodeCache.value.get(nodeId);
  if (cachedData) {
    console.log(`💾 Using cached data for node ${nodeId}, level ${nodeLevel}`);
    resolve(cachedData);
    return;
  }
  
  // ⚡ 优化3: 防止重复加载
  if (loadingNodes.value.has(nodeId)) {
    console.log(`⏳ Node ${nodeId} already loading`);
    resolve([]);
    return;
  }
  
  loadingNodes.value.add(nodeId);
  
  try {
    // ⚡ 优化4: 快速连接检查
    if (!connectFlag.value) {
      console.warn('❌ No connection established');
      resolve([]);
      return;
    }
    
    // ⚡ 优化5: 智能节点ID处理
    let targetNodeId = node.data.nodeId;
    if (nodeLevel === 0 && !targetNodeId) {
      targetNodeId = new UaNodeId(ObjectIds.RootFolder);
    }
    
    if (!targetNodeId) {
      console.warn(`❌ No target node ID found for level ${nodeLevel}`, node.data);
      resolve([]);
      return;
    }
    
    // ⚡ 优化6: 动态页面大小和超时策略
    const pageSize = getOptimalPageSize(nodeLevel);
    const timeout = Math.min(5000 + nodeLevel * 1000, 12000);
    
    // ⚡ 优化7: 客户端复用
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    const baseUrl = allData?.url || urlForm.value.url;
    
    if (!baseUrl) {
      console.warn('❌ No valid URL configuration found');
      resolve([]);
      return;
    }
    
    let opcClient = opcClientCache.value.get(baseUrl);
    if (!opcClient) {
      const apiConfig = new Configuration({ basePath: baseUrl });
      const clientConfig = new UaClientConfiguration(apiConfig);
      opcClient = new UaWebClient(clientConfig);
      opcClientCache.value.set(baseUrl, opcClient);
    }
    
    // ⚡ 优化8: 智能节点类型过滤
    const nodeClassToReturn = getNodeClassFilter(nodeLevel);
    
    // ⚡ 优化9: 智能请求处理 - 使用批处理系统
    const browseFunction = () => opcClient.browseChild(targetNodeId, nodeClassToReturn, pageSize);
    
    let result;
    if (nodeLevel < 4) {
      // 浅层节点使用批处理
      result = await requestBatcher.addRequest(nodeId, nodeLevel, browseFunction);
    } else {
      // 深层节点直接执行
      const browsePromise = browseFunction();
      const timeoutPromise = new Promise((_, reject) => {
        setTimeout(() => reject(new Error(`Browse timeout after ${timeout}ms`)), timeout);
      });
      result = await Promise.race([browsePromise, timeoutPromise]);
    }
    
    if (!result?.results) {
      console.log(`❌ No results for node ${nodeId} at level ${nodeLevel}`);
      const emptyNode = [{
        nodeIdNum: `${nodeId}-empty`,
        label: 'No Data',
        BrowseName: 'No Data',
        displayName: { _text: 'No Data' },
        isLeaf: true,
        hasChildren: false,
        isEmpty: true
      }];
      nodeCache.value.set(nodeId, emptyNode);
      resolve(emptyNode);
      return;
    }
    
    const allArr = result.results || [];
    console.log(`📥 Received ${allArr.length} items for node ${nodeId} at level ${nodeLevel}`);
    
    if (allArr.length === 0) {
      console.log(`📭 Empty results for node ${nodeId}`);
      const leafNode = [{ 
        ...node.data,
        nodeIdNum: `${nodeId}-leaf`,
        label: node.data.label || 'Empty Folder',
        BrowseName: node.data.label || 'Empty Folder',
        displayName: { _text: node.data.label || 'Empty Folder' },
        isLeaf: true,
        hasChildren: false,
        isEmpty: true
      }];
      nodeCache.value.set(nodeId, leafNode);
      resolve(leafNode);
      return;
    }
    
    // ⚡ 优化10: 高效数据处理和流式渲染
    const processedData = await processNodeDataOptimized(allArr, nodeLevel, nodeId);
    
    console.log(`✅ Processed ${processedData.length} items for level ${nodeLevel}`);
    
    // ⚡ 优化11: 智能缓存策略
    nodeCache.value.set(nodeId, processedData, nodeLevel);
    
    // 🚀 记录用户行为用于预加载预测
    if (preloadManager) {
      preloadManager.recordUserBehavior(nodeId, 'expand');
    }
    
    // ⚡ 优化12: 保持原有的渲染策略，但优化性能
    if (processedData.length > 50) {
      // 保持原有的50个节点阈值
      console.log(`🚀 使用流式渲染: ${processedData.length} 个节点`);
      await renderDataInChunks(processedData, resolve, nodeId);
    } else {
      // 少量数据直接渲染
      console.log(`⚡ 直接渲染: ${processedData.length} 个节点`);
      resolve(processedData);
    }
    
  } catch (error) {
    console.error(`❌ Load error for node ${nodeId} at level ${nodeLevel}:`, error);
    
    // ⚡ 优化13: 智能错误处理
    const errorNode = [{
      nodeIdNum: `${nodeId}-error-${Date.now()}`,
      label: `Load Error - Click to retry`,
      BrowseName: `Load Error - Click to retry`,
      displayName: { _text: `Load Error - Click to retry` },
      isLeaf: false,
      hasChildren: true,
      error: true,
      originalNodeId: nodeId,
      retry: true
    }];
    
    resolve(errorNode);
  } finally {
    loadingNodes.value.delete(nodeId);
  }
};

// 获取节点深度
const getNodeDepth = (node) => {
  let depth = 0;
  let current = node;
  while (current.parent) {
    depth++;
    current = current.parent;
  }
  return depth;
};

// 智能分块渲染，确保所有数据都能显示但避免卡顿
const renderAllDataInChunks = (dataArray, resolve, nodeId) => {
  const CHUNK_SIZE = 15; // 每次渲染15个节点
  const INITIAL_CHUNK = 10; // 首次渲染10个节点
  
  console.log(`开始分块渲染 ${dataArray.length} 条数据`);
  
  // 先返回第一批数据
  const firstBatch = dataArray.slice(0, INITIAL_CHUNK);
  resolve(firstBatch);
  
  const remaining = dataArray.slice(INITIAL_CHUNK);
  if (remaining.length === 0) {
    loadingNodes.value.delete(nodeId);
    return;
  }
  
  // 创建渲染队列，确保所有数据都能显示
  const renderQueue = [];
  for (let i = 0; i < remaining.length; i += CHUNK_SIZE) {
    renderQueue.push(remaining.slice(i, i + CHUNK_SIZE));
  }
  
  let queueIndex = 0;
  const processNextChunk = () => {
    if (queueIndex >= renderQueue.length) {
      console.log(`分块渲染完成，共渲染 ${dataArray.length} 条数据`);
      loadingNodes.value.delete(nodeId);
      return;
    }
    
    const chunk = renderQueue[queueIndex];
    queueIndex++;
    
    // 使用 requestIdleCallback 在浏览器空闲时渲染
    if (window.requestIdleCallback) {
      window.requestIdleCallback(() => {
        try {
          // 触发 el-tree 更新 - 使用更安全的方式
          const tree = eltree.value;
          if (tree && tree.store) {
            const currentNode = tree.store.getNode(nodeId);
            if (currentNode) {
              // 直接更新节点的子节点数据
              const currentChildren = currentNode.childNodes || [];
              const newChildren = [...currentChildren];
              
              chunk.forEach((item) => {
                newChildren.push({
                  data: item,
                  key: item.nodeIdNum || item.nodeId?._nodeId?.value,
                  level: currentNode.level + 1,
                  parent: currentNode,
                  loaded: true,
                  isLeaf: item.isLeaf
                });
              });
              
              // 更新子节点
              currentNode.childNodes = newChildren;
              // 触发树重新渲染
              tree.store.updateChildren(currentNode, newChildren);
            }
          }
        } catch (e) {
          console.warn('Chunk rendering failed:', e);
        }
        
        // 继续处理下一块
        setTimeout(processNextChunk, 30); // 30ms延迟，确保UI响应
      }, { timeout: 100 });
    } else {
      // 降级到 setTimeout
      setTimeout(() => {
        try {
          const tree = eltree.value;
          if (tree && tree.store) {
            const currentNode = tree.store.getNode(nodeId);
            if (currentNode) {
              const currentChildren = currentNode.childNodes || [];
              const newChildren = [...currentChildren];
              
              chunk.forEach((item) => {
                newChildren.push({
                  data: item,
                  key: item.nodeIdNum || item.nodeId?._nodeId?.value,
                  level: currentNode.level + 1,
                  parent: currentNode,
                  loaded: true,
                  isLeaf: item.isLeaf
                });
              });
              
              currentNode.childNodes = newChildren;
              tree.store.updateChildren(currentNode, newChildren);
            }
          }
        } catch (e) {
          console.warn('Chunk rendering failed:', e);
        }
        
        setTimeout(processNextChunk, 30);
      }, 30);
    }
  };
  
  // 开始处理队列
  setTimeout(processNextChunk, 50); // 50ms后开始
};
// 在 loadNode 方法中添加优化
const loadNodes = (node, resolve) => {
  // ... 原有逻辑
  
  // 确保 selectedNodeId 被正确设置
  if (!selectedNodeId.value) {
    selectedNodeId.value = new UaNodeId(ObjectIds.RootFolder);
  }
  
  getBrowseDatas().then((firstRes) => {
    const rawData = firstRes?.results || [];
    
    // 优化1: 限制初始加载数量
    const initialData = rawData.slice(0, 30); // 只处理前30条
    
    // 优化2: 简化数据处理
    const processedData = initialData.map(item => ({
      ...item,
      NodeClassType: item.typeDefinition?._nodeId?.value 
        ? ObjectTypeIds[item.typeDefinition._nodeId.value] 
        : nodeClassType[item.nodeClass],
      nodeIdNum: item.nodeId?._nodeId?.value,
      isServer: /Server|BaseObjectType/.test(item.NodeClassType),
      isFolder: item.NodeClassType === 'FolderType',
      isView: item.NodeClassType === 'View',
      isLeaf: !item.hasChildren // 添加叶子节点标识
    }));
    
    // 优化3: 冻结数据避免响应式开销
    const frozenData = Object.freeze(processedData);
    
    // 优化4: 分批渲染
    const renderInChunks = (data, chunkSize = 10) => {
      let rendered = 0;
      const renderNext = () => {
        const chunk = data.slice(rendered, rendered + chunkSize);
        rendered += chunkSize;
        
        // 部分解析
        resolve([...resolve() || [], ...chunk]);
        
        if (rendered < data.length) {
          requestAnimationFrame(renderNext);
        } else {
          loadingNodes.value.delete(nodeId);
        }
      };
      renderNext();
    };
    
    // 启动分批渲染
    renderInChunks(frozenData);
    
    // ... 原有分页逻辑可以保留但应用相同优化
  });
};
const loadNode2 = (node, resolve) => {
  let flag = node.data?.isAbstract;
  let isArray = Array.isArray(node.data);
  if (node.level = 1 ) {
    return resolve([
    {
      ReferenceTypeId: 'i=38',
      IsForward: true,
      NodeId: 'i=88',
      browseName: 'Server',
      DisplayName: { Text: 'New Node' },
      NodeClass: 1,
      level:1,
      TypeDefinition: 'i=61',
    },
    ]);
  }
   
   
};
const ensureFixedNodeExpanded = () => {
      if (!expandedKeys.value.includes(fixedNodeId.value)) {
        expandedKeys.value.push(fixedNodeId.value);
      }
    };
const onNodeCollapse = (data,node) => {
      let arr = expandedKeys.value.filter((item)=>{return item != data.nodeIdNum})
      expandedKeys.value = arr
};
    
const getTestDatas = async (e) => {
  const date = formatDateToISO8601Extended();
  const data = {
    RequestHeader: {
      AuthenticationToken: 'string',
      Timestamp: '0001-01-01T00:00:00.0000000+00:00',
      RequestHandle: 0,
      ReturnDiagnostics: 0,
      AuditEntryId: 'string',
      TimeoutHint: 0,
      AdditionalHeader: {
        UaTypeId: 'string',
        UaEncoding: 255,
        UaBody: 'string',
      },
    },
    View: {
      ViewId: 'string',
      Timestamp: '0001-01-01T00:00:00.0000000+00:00',
      ViewVersion: 0,
    },
    RequestedMaxReferencesPerNode: 0,
    NodesToBrowse: [
      {
        NodeId: 'string',
        BrowseDirection: 0,
        ReferenceTypeId: 'string',
        IncludeSubtypes: false,
        NodeClassMask: 0,
        ResultMask: 0,
      },
    ],
  };

  try {
    const result = await getTestData(data);
    return result;
  } catch (error) {
    console.error('Error in getTestDatas:', error);
    return null; // 或者抛出错误，取决于你的需求
  }
};

const getBrowseDatas = async (passNodeClass, pageSize = 20, overrideNodeId = null) => {
  let nodeId = ''
  state.allCardData = []
  
  // 检查连接状态，如果没有连接则直接返回
  if (!connectFlag.value) {
    console.warn('No connection established, skipping getBrowseDatas');
    return { results: [], ContinuationPoint: null };
  }
  
  if(overrideNodeId){
    nodeId = overrideNodeId;
  }else if(!selectedNodeId.value && !rootNodeLoaded.value){
    // 只有在没有选中节点且根节点未加载时才使用根节点
    nodeId = new UaNodeId(ObjectIds.RootFolder);
    rootNodeLoaded.value = true; // 标记根节点已加载
  }else if(selectedNodeId.value){
    // 有选中节点时使用选中的节点
    nodeId = selectedNodeId.value;
  }else{
    // 其他情况默认使用根节点
    nodeId = new UaNodeId(ObjectIds.RootFolder);
  }
  
  let baseUrl = urlForm.value.url
  let allData = urlDatas.getDataByKey(selectedTopNodeId.value)
  
  // 检查是否有有效的URL配置
  if (!allData?.url && !urlForm.value.url) {
    console.warn('No valid URL configuration found, skipping getBrowseDatas');
    return { results: [], ContinuationPoint: null };
  }
  
  let apiConfig2 = new Configuration({
      basePath: allData?.url || urlForm.value.url || "http://localhost:4840"  // 提供默认值
  });
  let clientConfig = new UaClientConfiguration(apiConfig2);
  let testOpcServer = new UaWebClient(clientConfig);

  try {
    // 验证 nodeId 是否有效
    if (!nodeId) {
      console.warn('Invalid nodeId provided to getBrowseDatas');
      return { results: [], ContinuationPoint: null };
    }
    
    // 确保 nodeId 是 UaNodeId 对象
    let validNodeId = nodeId;
    if (nodeId && typeof nodeId === 'object' && nodeId._nodeId) {
      validNodeId = nodeId._nodeId;
    }
    
    let nodeClassToReturn = 0;
    // 使用默认的节点类型过滤，包含所有主要类型
    nodeClassToReturn = Number( 
      NodeClass.Object 
      | NodeClass.ObjectType 
      | NodeClass.Method
      | NodeClass.DataType
      | NodeClass.VariableType
      | NodeClass.ReferenceType
      | NodeClass.View
    );
   
  
    // 保持异步调用，但简化处理
    let result = await testOpcServer.browseChild(validNodeId, nodeClassToReturn, pageSize);
    
    // 检查结果结构
    if (result) {
      if (result.results && result.results.length > 0) {
      }
    }
    
    return result;
  } catch (error) {
    console.error('getBrowseDatas error:', error);
    return { results: [], ContinuationPoint: null };
  }
};

const getRightBrowseDatas = async () => {
  let nodeId = selectedNodeId.value;
  
  // 验证 nodeId 是否有效
  if (!nodeId) {
    console.warn('No valid nodeId found for getRightBrowseDatas');
    return { results: [], ContinuationPoint: null };
  }
  
  // 确保 nodeId 是 UaNodeId 对象
  let validNodeId = nodeId;
  if (nodeId && typeof nodeId === 'object' && nodeId._nodeId) {
    validNodeId = nodeId._nodeId;
  }
  
  // 获取正确的URL
  const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
  let url = allData?.url || urlForm.value.url;
  
  if (!url) {
    console.warn('No valid URL configuration found, skipping getRightBrowseDatas');
    return { results: [], ContinuationPoint: null };
  }
  
  let apiConfig = new Configuration({
    basePath: url
  });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);
 
  try {
    // let nodeId = new UaNodeId("BuildingAutomation",nodeIdNum,UaNodeIdType.STRING);

    let nodeClassToReturn = Number( 
       NodeClass.Object 
      | NodeClass.ObjectType 
      | NodeClass.Method
      | NodeClass.DataType
      | NodeClass.ReferenceType
      | NodeClass.Unspecified
      | NodeClass.View
    );
    // let nodeClassToReturn = [ 
    //    Number(NodeClass.Object),
    //    Number(NodeClass.ObjectType),
    //    Number(NodeClass.Method),
    //    Number(NodeClass.DataType),
    //    Number(NodeClass.ReferenceType),
    //    Number(NodeClass.Unspecified),
    //    Number(NodeClass.View),
    // ];
    let result = await testOpcServer.browseChild(validNodeId, nodeClassToReturn, 20);
    return result;
  } catch (error) {
    console.error('getRightBrowseDatas error:', error);
    ElMessage.error('OPC UA 测试失败: ' + error.message);
    return { results: [], ContinuationPoint: null };
  }
};
const getRightDetailsBrowseDatas = async (url) => {
  let nodeId = selectedNodeId.value?.nodeId;
  
  // 验证 nodeId 是否有效
  if (!nodeId) {
    console.warn('No valid nodeId found for getRightDetailsBrowseDatas');
    return { results: [], ContinuationPoint: null };
  }
  
  // 确保 nodeId 是 UaNodeId 对象
  let validNodeId = nodeId;
  if (nodeId && typeof nodeId === 'object' && nodeId._nodeId) {
    validNodeId = nodeId._nodeId;
  }

  let apiConfig = new Configuration({
    basePath: url
  });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);
 
  try {
    // let nodeId = new UaNodeId("BuildingAutomation",nodeIdNum,UaNodeIdType.STRING);
    let nodeClassToReturn = Number( 
       NodeClass.Variable
    );
    // let nodeClassToReturn = [ 
    //    Number(NodeClass.Object),
    //    Number(NodeClass.ObjectType),
    //    Number(NodeClass.Method),
    //    Number(NodeClass.DataType),
    //    Number(NodeClass.ReferenceType),
    //    Number(NodeClass.Unspecified),
    //    Number(NodeClass.View),
    // ];
    let result = await testOpcServer.browseChild(validNodeId, nodeClassToReturn, 20);
    return result;
  } catch (error) {
    console.error('getRightDetailsBrowseDatas error:', error);
    ElMessage.error('OPC UA 测试失败: ' + error.message);
    return { results: [], ContinuationPoint: null };
  }
};
const getBrowseNextDatas = async () => {
  let nodeClassToReturn = Number( 
       NodeClass.Object 
      | NodeClass.ObjectType 
      | NodeClass.Method
      | NodeClass.DataType
      | NodeClass.ReferenceType
      | NodeClass.Unspecified
      | NodeClass.View
    );
  let data = {
    nodeClassToReturn:nodeClassToReturn,
    ReleaseContinuationPoints: false,
    ContinuationPoints: [continuationPoints.value],
  };
  const result = await getBrowseNextData(data);
  return result;
};

const getOpcuaDatas = async () => {
  let data = {
    NodesToRead: [
      {
        NodeId: 'ns=3;s=Demo.Dynamic.Scalar.Float',
        AttributeId: 2272810827,
        IndexRange: 'sint',
        DataEncoding: 'exercitation aute tempor Ut esse',
      },
    ],
  };
  const result = await getOpcuaData(data);
  return result;
};
const getMessageListBottom = async () => {
  const result = await getOpcuaData(data);
  state.messageListBottom = result;

   
};
const changeSeletedStatus = (e, id) => {
  state.messageListBottom.map((item) => {
    if (item.nodeId === id) {
      item.enabled = e;
    }
  });
};
const runOpcuaTest = async () => {
  // 获取正确的URL
  const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
  let url = allData?.url || urlForm.value.url;
  
  if (!url) {
    ElMessage.error('请先配置服务器连接');
    return;
  }

  let apiConfig = new Configuration({
    basePath: url
  });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);
  try {
    let nodeId = new UaNodeId("BuildingAutomation",4,UaNodeIdType.STRING);
    let nodeClassToReturn = Number(NodeClass.Object | NodeClass.Variable | NodeClass.Method);
    let children = await testOpcServer.browseChild(nodeId, nodeClassToReturn, 3);
  } catch (error) {
    ElMessage.error('OPC UA 测试失败: ' + error.message);
  }
};
onMounted(async () => {
  const hidePopperArrows = () => {
    const arrows = document.querySelectorAll('.el-popper__arrow, span[data-popper-arrow]');
    arrows.forEach(arrow => {
      arrow.style.display = 'none';
      arrow.style.visibility = 'hidden';
      arrow.style.opacity = '0';
      arrow.style.width = '0';
      arrow.style.height = '0';
      arrow.style.overflow = 'hidden';
    });
  };
  
  // 立即执行一次
  hidePopperArrows();
  
  // 使用 MutationObserver 监听 DOM 变化，确保新添加的元素也被隐藏
  const observer = new MutationObserver((mutations) => {
    mutations.forEach((mutation) => {
      if (mutation.type === 'childList') {
        mutation.addedNodes.forEach((node) => {
          if (node.nodeType === Node.ELEMENT_NODE) {
            // 检查新添加的节点
            if (node.classList && node.classList.contains('el-popper__arrow')) {
              hidePopperArrows();
            }
            // 检查新添加节点的子元素
            const arrows = node.querySelectorAll && node.querySelectorAll('.el-popper__arrow, span[data-popper-arrow]');
            if (arrows && arrows.length > 0) {
              hidePopperArrows();
            }
          }
        });
      }
    });
  });
  
  // 开始观察
  observer.observe(document.body, {
    childList: true,
    subtree: true
  });
  
  // 定期检查并隐藏（作为备用方案）
  setInterval(hidePopperArrows, 1000);
});

// Add new reactive data
const selectedNodeId = ref('');
const selectedTopNodeId = ref('');
const continuationPoints = ref('');

const nodeDetails = reactive({});
const nodeDetailsData = reactive({});
const styleObject = ref({
        left: '0px',
        top: '0px',
});
const newBtn = ref(false)

const defaultProps = {
  children: 'children',
  label: 'BrowseName',
};

// Add new methods
const handleRowClick = (row) => {
  // Handle row click logic
};

const handleNodeClick = (data,node) => { 
  // 安全地提取 nodeId，确保是 UaNodeId 对象
  if (data.nodeId && data.nodeId._nodeId) {
    selectedNodeId.value = data.nodeId._nodeId; // 使用 _nodeId 属性
  } else if (data.nodeId) {
    selectedNodeId.value = data.nodeId; // 如果已经是 UaNodeId
  } else {
    selectedNodeId.value = '';
     
  }
  
  selectedTopNodeId.value = data.NodeId
  
  let tempArr = []
  Object.entries(data).forEach(([key, value], index) => {
  tempArr.push({
    id: index, // 添加唯一的 id 字段
    [key]: value,
    value: value,
    key: key,
    // type: type
  })
  });
  state.detailsArr = tempArr
  Object.assign(nodeDetails, data || {});
  if(node.level != 1){
    //  getBrowseDatas()
    try{
      getBrowseDatas().then(async (firstRes) => {
        let allArr = firstRes?.results || []; // 初始化累计数组
        let continuationPoint = firstRes?.ContinuationPoint;

        // 循环获取后续页数据
        while (continuationPoint) {
          continuationPoints.value = continuationPoint; // 设置继续点
          try {
            const nextRes = await getBrowseNextDatas();

            // 合并数据
            if (nextRes?.results) {
              allArr = allArr.concat(nextRes.results);
            }

            // 更新继续点
            continuationPoint = nextRes?.ContinuationPoint;
          } catch (error) {
            break; // 出错时终止循环
          }
        }
        // if(!allArr || allArr.length == 0 ){
        //   state.bottomTreeData = []
          
        //   return
        // }
        allArr.map((item)=>{
          let typeDefinitionId = item.typeDefinition?._nodeId?.value
          item.NodeClassType = ObjectTypeIds[typeDefinitionId]
          item.nodeIdNum = item.nodeId?._nodeId?.value
        })
        const slim = allArr.map(item => ({
          nodeId      : item.nodeId,            // 关键键
          nodeIdNum   : item.nodeId?._nodeId.toString(),
          label       : item.displayName?._text || item.browseName || 'Node',
          isLeaf      : !item.hasChildren,
          nodeClass   : item.nodeClass,
          NodeClassType: item.nodeClass !== undefined ? (ObjectTypeIds[item.typeDefinition?._nodeId?.value] || nodeClassType[item.nodeClass]) : '',
          hasChildren : item.hasChildren,
          children    : []        ,
          hasChildren: !item.hasChildren               // 懒加载留空
          }));
        state.bottomTreeData = slim
        // return resolve(allArr);
     });

     }
     catch(e){
        connectFlag.value = false
     }
  }
};
const handleNodeClick2 = (data) => {
  // 安全地提取 nodeId，确保是 UaNodeId 对象
  if (data.nodeId && data.nodeId._nodeId) {
    selectedNodeId.value = data.nodeId._nodeId; // 使用 _nodeId 属性
  } else if (data.nodeId) {
    selectedNodeId.value = data.nodeId; // 如果已经是 UaNodeId
  } else {
    console.warn('Invalid nodeId structure in handleNodeClick2:', data);
    return;
  }
  
  // 保持 selectedTopNodeId 不变，使用之前设置的值
  // 如果没有设置过 selectedTopNodeId，则使用当前节点的 NodeId
  if (!selectedTopNodeId.value) {
    selectedTopNodeId.value = data.nodeId;
  }
  
  let tempArr = []
  Object.entries(data).forEach(([key, value], index) => {
  tempArr.push({
    id: index, // 添加唯一的 id 字段
    [key]: value,
    value: value,
    key: key,
    // type: type
  })
  });
  state.detailsArr = tempArr
  Object.assign(nodeDetails, data || {});
  

  // 获取正确的URL
  const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
  let url = allData?.url || urlForm.value.url;
  detailsMessage(data.nodeId, url);
   
};

const getReadValues = async() => {
  // 获取正确的URL
  const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
  let url = allData?.url || urlForm.value.url;
  
  if (!url) {
    ElMessage.error('请先配置服务器连接');
    return;
  }
  
  let apiConfig = new Configuration({
      basePath: url
  });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);
 
  try {
    // let nodeId = new UaNodeId("Demo.History.Historian_1",3,UaNodeIdType.STRING);
    let nodeId = new UaNodeId(selectedNodeId.value.browseName,selectedNodeId.value.nodeId);

    let attribute = await testOpcServer.readVariableAttributes(selectedNodeId.value);

    let type = DataTypeIds[attribute.dataType.value]
    selectedNodeId.value.name = selectedNodeId.value.browseName
    selectedNodeId.value.dataTypes = type
 
  } catch (error) {
    ElMessage.error('OPC UA 测试失败: ' + error.message);
  }
}
const dataConfig = ref({
  boolean: 'false',
  uinterger: 10,
  interger: -10,
  double: 1.3,
  doubleValue: 100,
  doubleUnit: '毫秒',
  time: '2023-01-01T07:00',
  string: 'abc',
  stringFormat: 'http://172.18.21.2:4000',
  nodeId: '变量A',
  enumeration: '严重',
  structure: {
    high: 100,
    low: 0,
  },
});

// 属性值配置
const propertyValues = reactive({
  eurange: 'UaTypeId',
  engineeringUnits: 'UaTypeId',
  dataType: 'UaTypeId',
  valueRank: '0',
  arrayDimensions: '[]',
});

const loggerSettings = reactive({
  publishInterval: 500,
  keepAliveCount: 10,
  lifetimeCount: 2400,
  samplingInterval: 250,
  queueSize: 10,
  discardOldest: true,
  outputDir: 'C:/users/crossover',
  maxLines: 100000,
  maxBackupFiles: 10,
  dataChanges: 0,
  keepAlives: 0,
});
// 添加缺失的变量
const isNewProject = ref(false);
const nodeIdFlag = ref(true);
const displayNameFlag = ref(true);

// 辅助函数：从 ObjectIds 中查找对应的值
const findObjectIdByType = (typeValue) => {
  if (typeValue === undefined || typeValue === null) {
    return null;
  }
  
  
  // 方法1: 直接查找
  let result = DataTypeIds[typeValue];
  if (result !== undefined) {
    return result;
  }
  
  // 方法2: 数字转换查找
  const numericType = Number(typeValue);
  if (!isNaN(numericType)) {
    result = DataTypeIds[numericType];
    if (result !== undefined) {
      return result;
    }
  }
  
  // 方法3: 值匹配查找
  for (const [key, value] of Object.entries(DataTypeIds)) {
    if (value === typeValue || value === numericType) {
      return key;
    }
  }
  
  // 方法4: 字符串模糊匹配
  const typeString = String(typeValue).toLowerCase();
  for (const [key, value] of Object.entries(DataTypeIds)) {
    if (key.toLowerCase().includes(typeString) || 
        String(value).toLowerCase().includes(typeString)) {
      return key;
    }
  }
  
  return null;
};

// 添加缺失的方法
const handleModal = async() => {
      // 清空数据，避免重复
  state.mergedNodeData = [];
  
  // 获取正确的URL
  const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
  let url = allData?.url || urlForm.value.url;
  
  if (!url) {
    ElMessage.error('请先配置服务器连接');
    return;
  }

    let apiConfig = new Configuration({
    basePath: url
    });
    let clientConfig = new UaClientConfiguration(apiConfig);
    let testOpcServer = new UaWebClient(clientConfig);
    let tempId = selectedNodeId?.value?.nodeId?._nodeId

    try{
       nodeDetailsData.value = await testOpcServer.readNodeAttributes(tempId,true);
       nodeDetailsData.value.nodeId = selectedNodeId.value?.nodeId?.toString()
       
       // 保留原始nodeClass数值，同时添加转换后的文本
       if (nodeDetailsData.value.nodeClass !== undefined) {
         nodeDetailsData.value.nodeClassText = getNodeClassText(nodeDetailsData.value.nodeClass);
       }
       
      nodeDetailsData.value =  moveKeyToFirst(nodeDetailsData.value, 'nodeId')

      getRightDetailsBrowseDatas().then(async (firstRes) => {
      let allArr = firstRes?.results || []; // 初始化累计数组
      let continuationPoint = firstRes?.ContinuationPoint;
      while (continuationPoint) {
        continuationPoints.value = continuationPoint; // 设置继续点
        try {
          const nextRes = await getBrowseNextDatas();
          // 合并数据
          if (nextRes?.results) {
            allArr = allArr.concat(nextRes.results);
          }
          continuationPoint = nextRes?.ContinuationPoint;
        } catch (error) {
          break; // 出错时终止循环
        }
      }
       state.nodeDetailsArr = allArr
       if(!allArr || allArr.length == 0){
        state.mergedNodeData = [];
        return
      }
       let nodeIds = []
       allArr.map((item)=>{
        let nodeid = item.nodeId 
        nodeIds.push(  nodeid)
       })
       let current = await testOpcServer.readValues(nodeIds);
        
       
       // 合并 allArr 和 current 数组
       let mergedArray = allArr.map(async (item, index) => {
         
         // 安全地获取类型值
         let typeValue = current[index]?.value?.type;
         let dataValue = current[index] || null;
         
         // 如果当前节点没有数据，跳过处理
         if (!dataValue) {
           console.warn('No data available for node:', item.nodeId);
           return null;
         }
         // 使用辅助函数查找对应的 ObjectId
         let translateTypes = findObjectIdByType(typeValue);
         // 将翻译后的类型添加到 item 中
         item.translateTypes = translateTypes;
        
        // 获取正确的URL
        const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
        let url = allData?.url || urlForm.value.url;
        
        if (!url) {
          console.warn('No valid URL configuration found, skipping data type read');
          return;
        }
        
        let apiConfig = new Configuration({
            basePath: url
        });
        let clientConfig = new UaClientConfiguration(apiConfig);
        let testOpcServer = new UaWebClient(clientConfig);
        try{
          let currentID =  await  testOpcServer.readDataTypes([item.nodeId])
         let temp = state.allTypesData.filter((idx)=>{
          let typeId= idx.nodeId.toString()
          let nodeId = currentID.toString()
          return typeId == nodeId
        })

        let obj= {
           ...item,
           value: current[index]?.value || null,
           originalType: typeValue, // 保存原始类型值以便调试
           dataValue:dataValue,
           name:item.BrowseName,
           typeLookupSuccess: translateTypes !== null ,// 标记查找是否成功
           dataType:temp[0]?._browseName,
           dataTypesObj:temp[0],
           dataTypes:temp[0]?._parentType?._browseName || temp[0]?._browseName || temp[0]?.browseName,
           nodeIds:item.nodeId.toString(),
           _editValue: (() => {
             let value = (current[index]?.value && typeof current[index]?.value === 'object')
             ? current[index]?.value?.value
               : current[index]?.value;
             
             // 特殊处理 DateTime 类型
             if (temp[0]?._browseName === 'DateTime' && value) {
               // 尝试将值转换为 Date 对象
               if (typeof value === 'string' || typeof value === 'number') {
                 const dateValue = new Date(value);
                 if (!isNaN(dateValue.getTime())) {
                   return dateValue;
                 }
               }
               // 如果转换失败，返回当前时间
               return new Date();
             }
             
             return value;
           })(),
           _isSelected: false, // 添加选中状态属性
           _isEditing: false // 添加编辑状态属性
         }

        state.mergedNodeData.push(obj);
        }catch(err){
            console.log(err)
        }
        
         
       });

       // 可以将合并后的数组存储到 state 中
      });
      
    }
    catch(err){
        state.mergedNodeData = [];
        // ElMessage.warning('未获取到详细信息');
    }
    
};
const moveKeyToFirst =(obj, keyToMove) => {
  if (!(keyToMove in obj)) return obj;          // 容错：键不存在原样返回
  const ordered = Object.keys(obj)
    .filter(k => k !== keyToMove)
    .reduce((acc, k) => ({ ...acc, [k]: obj[k] }), {});
  return { [keyToMove]: obj[keyToMove], ...ordered };
}
const reorderObj = (obj, orderKeys) =>
  Object.fromEntries([
    ...orderKeys.filter(k => k in obj),          // 1. 按指定顺序提前
    ...Object.keys(obj).filter(k => !orderKeys.includes(k)) // 2. 其余保持原序
  ].map(k => [k, obj[k]]));
const handleBottomTree = async() => {
     let url = urlForm.value.url
    //  connectFlag.value = true
    //  rootNodeLoaded.value = false; // 重置根节点加载标志，确保重新加载
     
     // 确保 selectedNodeId 被正确设置
     if (!selectedNodeId.value) {
       selectedNodeId.value = new UaNodeId(ObjectIds.RootFolder);
     }
     
     try{
      getBrowseDatas().then(async (firstRes) => {
        let allArr = firstRes?.results || []; // 初始化累计数组
        let continuationPoint = firstRes?.ContinuationPoint;

        // 循环获取后续页数据
        while (continuationPoint) {
          continuationPoints.value = continuationPoint; // 设置继续点
          try {
            const nextRes = await getBrowseNextDatas();

            // 合并数据
            if (nextRes?.results) {
              allArr = allArr.concat(nextRes.results);
            }

            // 更新继续点
            continuationPoint = nextRes?.ContinuationPoint;
          } catch (error) {
            break; // 出错时终止循环
          }
        }
        if(!allArr || allArr.length == 0 ){
          state.bottomTreeData = []
          
          return
        }
        allArr.map((item)=>{
          let typeDefinitionId = item.typeDefinition?._nodeId?.value
          item.NodeClassType = ObjectTypeIds[typeDefinitionId]
          item.nodeIdNum = item.nodeId?._nodeId?.value
        })
        state.bottomTreeData = allArr
        // return resolve(allArr);
     });

     }
     catch(e){
        connectFlag.value = false
     }
};
const handleBottomFlag = async() => {
      connectFlag.value = false
      lastConnectFlag.value = false; // 重置连接状态跟踪
      rootNodeLoaded.value = false; // 重置根节点加载标志
      urlForm.value.url = ''
      state.bottomTreeData = []
};
const handleBottomProperty = async(data) => {
  PropertyDialogVisible.value =true
  // getUrlData(newNode.NodeId,urlForm.value)
  let datas = urlDatas.getDataByKey(data.NodeId)
  urlForm.value = datas
};
// 辅助函数：根据节点ID查找父节点（如果需要处理多层嵌套，这个函数需要递归）
const findParentNode = (nodes, nodeId, parent = null) => {
  for (let node of nodes) {
    if (node.id === nodeId) return parent;
    if (node.children) {
      const found = findParentNode(node.children, nodeId, node);
      if (found) return found;
    }
  }
  return null;
};
// 通用的删除节点函数
const deleteNodeFromTree = (nodes, nodeId, idField = 'id') => {
  if (!nodes || !Array.isArray(nodes)) {
    console.warn('deleteNodeFromTree: nodes 不是数组或为空', nodes);
    return false;
  }
  
  for (let i = 0; i < nodes.length; i++) {
    const node = nodes[i];
    if (!node) continue;
    
    
    if (node[idField] === nodeId) {
      nodes.splice(i, 1);
      return true;
    }
    if (node.children && Array.isArray(node.children)) {
      if (deleteNodeFromTree(node.children, nodeId, idField)) {
        return true;
      }
    }
  }
  return false;
};

// 通用的查找父节点函数
const findParentNodeGeneric = (nodes, nodeId, idField = 'id', parent = null) => {
  for (let node of nodes) {
    if (node[idField] === nodeId) return parent;
    if (node.children) {
      const found = findParentNodeGeneric(node.children, nodeId, idField, node);
      if (found) return found;
    }
  }
  return null;
};

const handleBottomDelete = async(data) => {
  try {
   
    // 获取节点ID，支持多种字段名
    const nodeId = data.NodeId || data.nodeId || data.nodeIdNum || data.id;
    
    if (!nodeId) {
     
      return;
    }
    
    let deletedCount = 0;
    
    // 从 treeData 中删除节点（使用 id 字段）
    if (data.nodeId) {
      const deletedFromTreeData = deleteNodeFromTree(state.treeData, data.nodeId, 'id');
      if (deletedFromTreeData) {
        deletedCount++;
      } else {
      }
    }
    
    // 从 templateTreeData 中删除节点（使用 NodeId 字段）
    if (data.NodeId) {
      const deletedFromTemplateData = deleteNodeFromTree(state.templateTreeData, data.NodeId, 'NodeId');
      if (deletedFromTemplateData) {
        state.bottomTreeData = []
        deletedCount++;
      } else {
      }
    }
    
    // 从 bottomTreeData 中删除节点（使用 nodeIdNum 字段）
    if (data.nodeIdNum) {
      const deletedFromBottomData = deleteNodeFromTree(state.bottomTreeData, data.nodeIdNum, 'nodeIdNum');
      if (deletedFromBottomData) {
        deletedCount++;
      } else {
      }
    }
    
    
    if (deletedCount > 0) {
      // 可以添加成功提示
      // ElMessage.success('节点删除成功');
    } else {
      console.warn('⚠️ 未找到要删除的节点，请检查数据结构');
      // 可以添加警告提示
      // ElMessage.warning('未找到要删除的节点');
    }
  } catch (error) {
    console.error('❌ 删除节点时发生错误:', error);
    // 可以添加错误提示
    // ElMessage.error('删除节点失败');
  }
}
const handleChangeFormValue = async() => {
   PropertyDialogVisible.value =false
   const node = eltreeTop.value.getNode(propertyData.value)
  if (node && node.data) {
    node.data.BrowseName = urlForm.value.urlName
    node.data.name = urlForm.value.urlName
    urlRef.value.validate( async(valid) => {
       if(valid){
        // 确保 selectedNodeId 被正确设置
        if (!selectedNodeId.value) {
          selectedNodeId.value = new UaNodeId(ObjectIds.RootFolder);
        }
        getBrowseDatas().then(async (firstRes) => {
        let allArr = firstRes?.results || []; // 初始化累计数组
        let continuationPoint = firstRes?.ContinuationPoint;

        // 循环获取后续页数据
        while (continuationPoint) {
          continuationPoints.value = continuationPoint; // 设置继续点
          try {
            const nextRes = await getBrowseNextDatas();

            // 合并数据
            if (nextRes?.results) {
              allArr = allArr.concat(nextRes.results);
            }

            // 更新继续点
            continuationPoint = nextRes?.ContinuationPoint;
          } catch (error) {
            break; // 出错时终止循环
          }
        }
        if(!allArr || allArr.length == 0 ){
          state.bottomTreeData = []
          
          return
        }
        allArr.map((item)=>{
          let typeDefinitionId = item.typeDefinition?._nodeId?.value
          item.NodeClassType = ObjectTypeIds[typeDefinitionId]
          item.nodeIdNum = item.nodeId?._nodeId?.value
        })
        state.bottomTreeData = allArr
        // return resolve(allArr);
     });
       }
    })
  }
};

const deleteNodes = () => {
  console.log('删除');
};

const renameNode = () => {
  console.log('重命名');
};

const handleMessageModal = () => {
  console.log('成员');
};

const handleNodeDblClick =async (node, data,url) => {

   selectedNodeId.value = data.nodeId
    // 安全地访问 _nodeId 属性
    if (selectedNodeId.value && selectedNodeId.value._nodeId) {
   selectedNodeId.value.nodeId = selectedNodeId.value._nodeId
    } else {
      console.warn('无法获取 nodeId 信息:', data);
      return;
    }
    // 清空数据，避免重复
    state.mergedNodeData = []
    // 开始加载
    state.loadingVariables = true
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let cacheUrl = allData?.url || urlForm.value.url;
   // 检查是否有有效的URL配置
   if ( !cacheUrl ) {
     ElMessage.warning('请先配置服务器连接');
     state.loadingVariables = false
     return;
   }
   
    let apiConfig = new Configuration({
        basePath: cacheUrl
    });
    let clientConfig = new UaClientConfiguration(apiConfig);
    let testOpcServer = new UaWebClient(clientConfig);
    // let tempId = selectedNodeId?.value?.nodeId?._nodeId?.value
    let tempId = selectedNodeId?.value?._nodeId
    try{
      
      detailsMessage(tempId,cacheUrl)
      getRightDetailsBrowseDatas(cacheUrl).then(async (firstRes) => {
      let allArr = firstRes?.results || []; // 初始化累计数组
      let continuationPoint = firstRes?.ContinuationPoint;
      while (continuationPoint) {
        continuationPoints.value = continuationPoint; // 设置继续点
        try {
          const nextRes = await getBrowseNextDatas();
          // 合并数据
          if (nextRes?.results) {
            allArr = allArr.concat(nextRes.results);
          }
          continuationPoint = nextRes?.ContinuationPoint;
        } catch (error) {
          break; // 出错时终止循环
        }
      }
       state.nodeDetailsArr = allArr
       if(!allArr || allArr.length == 0){
        state.mergedNodeData = [];
        state.loadingVariables = false
        // ElMessage.warning('未获取到详细信息');
        // 移除 ElMessage，改为在模板中显示空数据提示
        return
      }
        // 初始化 OPC UA 客户端
        let apiConfig = new Configuration({
            basePath: cacheUrl
        });
        let clientConfig = new UaClientConfiguration(apiConfig);
        let testOpcServer = new UaWebClient(clientConfig);
        
        let readNodeIds = []
       allArr.map((item)=>{
        let nodeid = item.nodeId 
         // item.nodeId 是 UaExpandedNodeId，需要提取其中的 _nodeId
         readNodeIds.push(nodeid._nodeId)
       })
       
       // 添加错误处理，避免读取没有数据的节点
       let current = [];
       try {
          current = await testOpcServer.readValues(readNodeIds);
       } catch (error) {
         console.warn('Error reading values for nodes:', error);
         // 如果读取失败，创建一个与 allArr 长度相同的空数组
         current = new Array(allArr.length).fill(null);
       }
        
        // 获取所有节点的 nodeId，并创建映射关系
        let variableNodeIds = [];
        let nodeIdToIndexMap = new Map();
        
        allArr.forEach((item, index) => {
          if (item.nodeId?._nodeId) {
            // item.nodeId 是 UaExpandedNodeId，直接使用其中的 _nodeId
            variableNodeIds.push(item.nodeId._nodeId);
            nodeIdToIndexMap.set(item.nodeId._nodeId.toString(), index);
          }
        });
        
        // 批量读取所有节点的变量属性
        let allVariableAttributes = [];
        try {
          allVariableAttributes = await testOpcServer.readVariableAttributes(variableNodeIds);
        } catch (error) {
          console.warn('Error reading variable attributes for all nodes:', error);
          // 如果批量读取失败，创建一个空数组
          allVariableAttributes = new Array(variableNodeIds.length).fill(null);
       }
        
       
       // 合并 allArr 和 current 数组
       let mergedArray = allArr.map((item, index) => {
         
         // 安全地获取类型值
         let typeValue = current[index]?.value?.type;
         let dataValue = current[index] || null;
         
         // 如果当前节点没有数据，跳过处理
         if (!dataValue) {
           return null;
         }
         // 使用辅助函数查找对应的 ObjectId
         let translateTypes = findObjectIdByType(typeValue);
         // 将翻译后的类型添加到 item 中
         item.translateTypes = translateTypes;
        
         try{
           // 根据当前节点的 nodeId 查找在 variableNodeIds 中的索引
           let currentNodeId = item.nodeId?._nodeId?.toString();
           if (!currentNodeId) {
             console.warn('No nodeId found for item at index:', index);
            return null;
          }
           
           // 获取当前节点在 variableNodeIds 中的索引
           let variableIndex = variableNodeIds.findIndex(nodeId => 
             nodeId.toString() === currentNodeId
           );
           
           if (variableIndex === -1) {
             console.warn('NodeId not found in variableNodeIds:', currentNodeId);
             return null;
           }
           
           // 根据索引获取对应的变量属性数据
           let currentID = allVariableAttributes[variableIndex];
           
           if (!currentID) {
             console.warn('No variable attributes found for variableIndex:', variableIndex);
             return null;
           }
           
           
           // 根据 datatype ID 去 allTypesData 中查找对应的数据类型
         let temp = state.allTypesData.filter((idx)=>{
             let typeId = idx.nodeId.toString();
             let dataTypeId = currentID.dataType ? currentID.dataType.toString() : '';
             return typeId === dataTypeId;
           });
        let idxs = temp[0].nodeId
        let dataTypeDictionary = new UaDataTypeDictionary();
        let dataTypes = dataTypeDictionary.getDataType(currentID); 
        let obj= {
           ...item,
           value: current[index]?.value || null,
           originalType: typeValue, // 保存原始类型值以便调试
           dataValue:dataValue,
           UserWriteMask:currentID.userAccessLevel,
           name:item.browseName,
           typeLookupSuccess: translateTypes !== null ,// 标记查找是否成功
           dataType:temp[0]?._browseName,
           dataTypesObj:temp[0],
           dataTypes:temp[0]?._parentType?._browseName ?temp[0]?._parentType?._browseName :temp[0]?.browseName  ,
           nodeId:item.nodeId.toString(),
           _editValue: (() => {
             let value = (current[index]?.value && typeof current[index]?.value === 'object')
             ? current[index]?.value?.value
               : current[index]?.value;
             
             // 特殊处理 DateTime 类型
             if (temp[0]?._browseName === 'DateTime' && value) {
               // 尝试将值转换为 Date 对象
               if (typeof value === 'string' || typeof value === 'number') {
                 const dateValue = new Date(value);
                 if (!isNaN(dateValue.getTime())) {
                   return value;
                 }
               }
               // 如果转换失败，返回当前时间
               return value;
             }
             
             return value;
           })(),
           _isSelected: false, // 添加选中状态属性
           _isEditing: false // 添加编辑状态属性
         }
        if(obj.dataTypes === 'Boolean' && !Array.isArray(obj._editValue.value)) {
            obj.enumStrings = [false, true];
        }
        if(obj.dataTypes === 'ByteString'){
          // 处理 ByteString 类型的数据显示
          if (obj.value && Array.isArray(obj.value)) {
            obj._editValue = obj.value.map(item => {
              if (typeof item === 'string') {
                // 将二进制字符串转换为可读的十六进制字符串
                return byteStringToHex(item);
              }
              return item;
            });
          } else if (obj.value) {
            // 单个值的情况
            if (typeof obj.value === 'string') {
              obj._editValue = byteStringToHex(obj.value);
            } else {
              obj._editValue = obj.value;
            }
          }
        }
        
        if(obj.dataTypes === 'ExpandedNodeId' && !Array.isArray(obj._editValue.value)){
          // 处理 ExpandedNodeId 类型的数据显示
          if (obj.value && Array.isArray(obj.value)) {
            obj._editValue = obj.value.map(item => {
              const parsed = parseExpandedNodeId(item);
              return expandedNodeIdToString(parsed);
            });
          } else if (obj.value) {
            // 单个值的情况
            const parsed = parseExpandedNodeId(obj.value);
            obj._editValue = expandedNodeIdToString(parsed);
          }
        }
        if(obj.dataTypes === "ImagePNG" || obj.dataTypes ===  "Image"){
          if (Array.isArray(obj._editValue) && obj._editValue.length > 0) {
            obj._editValue = obj._editValue.map(v => (typeof v === 'string' && /[\x00-\x08\x0B\x0C\x0E-\x1F]/.test(v)) ? binaryStringToHex(v) : v);
          } else if (typeof obj._editValue == 'string' && obj._editValue) {
            obj._editValue = /[\x00-\x08\x0B\x0C\x0E-\x1F]/.test(obj._editValue) ? binaryStringToHex(obj._editValue) : obj._editValue;
          } else if (obj.value && Array.isArray(obj.value)) {
            obj._editValue = obj.value.map(v => binaryStringToHex(v));
          } else if (obj.value) {
            obj._editValue = binaryStringToHex(obj.value);
          } else {
            obj._editValue = '';
          }
           
        }
        if(obj.dataTypes === "ByteString" && !Array.isArray(obj._editValue.value)){
           // 将字符串转换为十六进制格式
           const str = obj._editValue.value;
           const hexString = stringToHex(str);
           obj._editValue = hexString;
        }
        // 返回处理后的对象
        return obj;
        }catch(err){
            console.log(err)
            return null;
        }
       });

      // 处理所有数据并推送到 mergedNodeData
      mergedArray.forEach(obj => {
        if (obj) {
          state.mergedNodeData.push(obj);
        }
      });
      // 数据完全展示后再结束 loading
      state.loadingVariables = false
      if(state.mergedNodeData.length == 0   || state.mergedNodeData.length == null){
        ElMessage.warning('未获取到详细信息');
      }
      });
    }
    catch(err){
        state.mergedNodeData = [];
        state.loadingVariables = false
        ElMessage.warning('未获取到详细信息');
    }
};
const handleVariableClick = async (node, data, url) => {
  selectedNodeId.value = data.nodeId;
  
  // 安全地访问 _nodeId 属性
  if (selectedNodeId.value && selectedNodeId.value._nodeId) {
    selectedNodeId.value.nodeId = selectedNodeId.value._nodeId;
  } else {
    console.warn('无法获取 nodeId 信息:', data);
    return;
  }
  
  // 清空数据，避免重复
  state.mergedNodeData = [];
  // 开始加载
  state.loadingVariables = true;
  
  // 使用传入的 url 参数，如果没有则尝试从 store 获取
  let cacheUrl = url;
  if (!cacheUrl) {
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    cacheUrl = allData?.url || urlForm.value.url;
  }
  
  // 检查是否有有效的URL配置
  if (!cacheUrl) {
    ElMessage.warning('请先配置服务器连接');
    state.loadingVariables = false;
    return;
  }

  let apiConfig = new Configuration({
    basePath: cacheUrl
  });
 let clientConfig = new UaClientConfiguration(apiConfig);
 let testOpcServer = new UaWebClient(clientConfig);
 let tempId = selectedNodeId?.value?._nodeId
 try{
   detailsMessage(tempId,cacheUrl)
   getRightDetailsBrowseDatas(cacheUrl).then(async (firstRes) => {
   let allArr = firstRes?.results || []; // 初始化累计数组
   let continuationPoint = firstRes?.ContinuationPoint;
   while (continuationPoint) {
     continuationPoints.value = continuationPoint; // 设置继续点
     try {
       const nextRes = await getBrowseNextDatas();
       // 合并数据
       if (nextRes?.results) {
         allArr = allArr.concat(nextRes.results);
       }
       continuationPoint = nextRes?.ContinuationPoint;
     } catch (error) {
       console.error('Error fetching next page:', error);
       break; // 出错时终止循环
     }
   }
    state.nodeDetailsArr = allArr
    if(!allArr || allArr.length == 0){
     state.mergedNodeData = [];
     state.loadingVariables = false
      
     return
   }
     // 初始化 OPC UA 客户端
     let apiConfig = new Configuration({
         basePath: cacheUrl
     });
     let clientConfig = new UaClientConfiguration(apiConfig);
     let testOpcServer = new UaWebClient(clientConfig);
     
     let readNodeIds = []
    allArr.map((item)=>{
     let nodeid = item.nodeId 
      // item.nodeId 是 UaExpandedNodeId，需要提取其中的 _nodeId
      readNodeIds.push(nodeid._nodeId)
    })
    
    // 添加错误处理，避免读取没有数据的节点
    let current = [];
    try {
       current = await testOpcServer.readValues(readNodeIds);
    } catch (error) {
      console.warn('Error reading values for nodes:', error);
      // 如果读取失败，创建一个与 allArr 长度相同的空数组
      current = new Array(allArr.length).fill(null);
    }
     
     // 获取所有节点的 nodeId，并创建映射关系
     let variableNodeIds = [];
     let nodeIdToIndexMap = new Map();
     
     allArr.forEach((item, index) => {
       if (item.nodeId?._nodeId) {
         // item.nodeId 是 UaExpandedNodeId，直接使用其中的 _nodeId
         variableNodeIds.push(item.nodeId._nodeId);
         nodeIdToIndexMap.set(item.nodeId._nodeId.toString(), index);
       }
     });
     
     // 批量读取所有节点的变量属性
     let allVariableAttributes = [];
     try {
       allVariableAttributes = await testOpcServer.readVariableAttributes(variableNodeIds);
     } catch (error) {
       console.warn('Error reading variable attributes for all nodes:', error);
       // 如果批量读取失败，创建一个空数组
       allVariableAttributes = new Array(variableNodeIds.length).fill(null);
    }
     
    
    // 合并 allArr 和 current 数组
    let mergedArray = allArr.map((item, index) => {
      
      // 安全地获取类型值
      let typeValue = current[index]?.value?.type;
      let dataValue = current[index] || null;
      
      // 如果当前节点没有数据，跳过处理
      if (!dataValue) {
        return null;
      }
      // 使用辅助函数查找对应的 ObjectId
      let translateTypes = findObjectIdByType(typeValue);
      // 将翻译后的类型添加到 item 中
      item.translateTypes = translateTypes;
     
      try{
        // 根据当前节点的 nodeId 查找在 variableNodeIds 中的索引
        let currentNodeId = item.nodeId?._nodeId?.toString();
        if (!currentNodeId) {
          console.warn('No nodeId found for item at index:', index);
         return null;
       }
        
        // 获取当前节点在 variableNodeIds 中的索引
        let variableIndex = variableNodeIds.findIndex(nodeId => 
          nodeId.toString() === currentNodeId
        );
        
        if (variableIndex === -1) {
          console.warn('NodeId not found in variableNodeIds:', currentNodeId);
          return null;
        }
        
        // 根据索引获取对应的变量属性数据
        let currentID = allVariableAttributes[variableIndex];
        
        if (!currentID) {
          console.warn('No variable attributes found for variableIndex:', variableIndex);
          return null;
        }
      let temp = state.allTypesData.filter((idx)=>{
          let typeId = idx.nodeId.toString();
          let dataTypeId = currentID.dataType ? currentID.dataType.toString() : '';
          return typeId === dataTypeId;
        });
     let idxs = temp[0].nodeId
     let dataTypeDictionary = new UaDataTypeDictionary();
     let dataTypes = dataTypeDictionary.getDataType(currentID); 
     let obj= {
        ...item,
        value: current[index]?.value || null,
        originalType: typeValue, // 保存原始类型值以便调试
        dataValue:dataValue,
        UserWriteMask:currentID.userAccessLevel,
        name:item.browseName,
        typeLookupSuccess: translateTypes !== null ,// 标记查找是否成功
        dataType:temp[0]?._browseName,
        dataTypesObj:temp[0],
        dataTypes:temp[0]?._parentType?._browseName ?temp[0]?._parentType?._browseName :temp[0]?.browseName  ,
        nodeId:item.nodeId.toString(),
        _editValue: (() => {
          let value = (current[index]?.value && typeof current[index]?.value === 'object')
          ? current[index]?.value?.value
            : current[index]?.value;
          
          // 特殊处理 DateTime 类型
          if (temp[0]?._browseName === 'DateTime' && value) {
            // 尝试将值转换为 Date 对象
            if (typeof value === 'string' || typeof value === 'number') {
              const dateValue = new Date(value);
              if (!isNaN(dateValue.getTime())) {
                return value;
              }
            }
            // 如果转换失败，返回当前时间
            return value;
          }
          
          return value;
        })(),
        _isSelected: false, // 添加选中状态属性
        _isEditing: false // 添加编辑状态属性
      }
     if(obj.dataTypes === 'Boolean' && !Array.isArray(obj._editValue.value)) {
         obj.enumStrings = [false, true];
     }
     if(obj.dataTypes === 'ByteString'){
       // 处理 ByteString 类型的数据显示
       if (obj.value && Array.isArray(obj.value)) {
         obj._editValue = obj.value.map(item => {
           if (typeof item === 'string') {
             // 将二进制字符串转换为可读的十六进制字符串
             return byteStringToHex(item);
           }
           return item;
         });
       } else if (obj.value) {
         // 单个值的情况
         if (typeof obj.value === 'string') {
           obj._editValue = byteStringToHex(obj.value);
         } else {
           obj._editValue = obj.value;
         }
       }
     }
     
     if(obj.dataTypes === 'ExpandedNodeId' && !Array.isArray(obj._editValue.value)){
       // 处理 ExpandedNodeId 类型的数据显示
       if (obj.value && Array.isArray(obj.value)) {
         obj._editValue = obj.value.map(item => {
           const parsed = parseExpandedNodeId(item);
           return expandedNodeIdToString(parsed);
         });
       } else if (obj.value) {
         // 单个值的情况
         const parsed = parseExpandedNodeId(obj.value);
         obj._editValue = expandedNodeIdToString(parsed);
       }
     }
     if(obj.dataTypes === "ImagePNG" || obj.dataTypes ===  "Image"){
       if (Array.isArray(obj._editValue) && obj._editValue.length > 0) {
         obj._editValue = obj._editValue.map(v => (typeof v === 'string' && /[\x00-\x08\x0B\x0C\x0E-\x1F]/.test(v)) ? binaryStringToHex(v) : v);
       } else if (typeof obj._editValue == 'string' && obj._editValue) {
         obj._editValue = /[\x00-\x08\x0B\x0C\x0E-\x1F]/.test(obj._editValue) ? binaryStringToHex(obj._editValue) : obj._editValue;
       } else if (obj.value && Array.isArray(obj.value)) {
         obj._editValue = obj.value.map(v => binaryStringToHex(v));
       } else if (obj.value) {
         obj._editValue = binaryStringToHex(obj.value);
       } else {
         obj._editValue = '';
       }
        
     }
     if(obj.dataTypes === "ByteString" && !Array.isArray(obj._editValue.value)){
        // 将字符串转换为十六进制格式
        const str = obj._editValue.value;
        const hexString = stringToHex(str);
        obj._editValue = hexString;
     }
     // 返回处理后的对象
     return obj;
     }catch(err){
         console.log(err)
         return null;
     }
    });

   // 处理所有数据并推送到 mergedNodeData
   mergedArray.forEach(obj => {
     if (obj) {
       state.mergedNodeData.push(obj);
     }
   });
   // 数据完全展示后再结束 loading
   state.loadingVariables = false
   if(state.mergedNodeData.length == 0   || state.mergedNodeData.length == null){
     ElMessage.warning('未获取到详细信息');
   }
   });
 }
 catch(err){
     state.mergedNodeData = [];
     state.loadingVariables = false
     ElMessage.warning('未获取到详细信息');
 }
};
const handleNodeSingleClick =async (node, data) => {

selectedNodeId.value = data.nodeId
 // 安全地访问 _nodeId 属性
 if (selectedNodeId.value && selectedNodeId.value._nodeId) {
selectedNodeId.value.nodeId = selectedNodeId.value._nodeId
 } else {
   console.warn('无法获取 nodeId 信息:', data);
   return;
 }
 // 清空数据，避免重复
 state.mergedNodeData = []
 // 开始加载
 state.loadingVariables = true

// 检查是否有有效的URL配置
if (!urlForm.value.url) {
  ElMessage.warning('请先配置服务器连接');
  state.loadingVariables = false;
  return;
}

 // 获取正确的URL
 const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
 let url = allData?.url || urlForm.value.url;
 
 if (!url) {
   ElMessage.error('请先配置服务器连接');
   state.loadingVariables = false;
   return;
 }

 let apiConfig = new Configuration({
     basePath: url
 });
 let clientConfig = new UaClientConfiguration(apiConfig);
 let testOpcServer = new UaWebClient(clientConfig);
 // let tempId = selectedNodeId?.value?.nodeId?._nodeId?.value
 let tempId = selectedNodeId?.value?._nodeId
 try{
   detailsMessage(tempId, urlForm.value.url);
 } catch(err) {
     state.mergedNodeData = [];
     state.loadingVariables = false;
     ElMessage.warning('未获取到详细信息');
 }
};

 

 
 
const byteStringToNumbers=(list)=> {
  return list.map(s => bufferToFloat64(latin1ToBuffer(s)));
}

// 将字符串转换为十六进制格式
const stringToHex = (str) => {
    

  let result = '';
          for (let i = 0; i < str.length; i++) {
            const charCode = str.charCodeAt(i);
            // 只取低8位，确保是字节值 - 这是问题所在
            const byteValue = charCode & 0xFF;
            result += byteValue.toString(16).padStart(2, '0');
          }
          return result;
}

// 生成位标志名称
const generateBitName = (index) => {
  if (index < 26) {
    // AA, BB, CC, ..., ZZ
    const char = String.fromCharCode(65 + index); // A=65
    return char + char;
  } else if (index < 52) {
    // AAAA, BBBB, CCCC, ..., ZZZZ
    const char = String.fromCharCode(65 + (index - 26));
    return char + char + char + char;
  } else {
    // EEE, FFFF, GGG (剩余的位置)
    const char = String.fromCharCode(65 + (index - 52));
    if (index < 55) {
      return char + char + char;
    } else {
      return char + char + char + char;
    }
  }
}
 

//  Latin-1 乱码 → ArrayBuffer
const  latin1ToBuffer =(str)=> {
  const buf = new ArrayBuffer(str.length);
  const view = new Uint8Array(buf);
  for (let i = 0; i < str.length; i++) view[i] = str.charCodeAt(i) & 0xFF;
  return buf;
}

//  ArrayBuffer → Float64 数组（little-endian）
const bufferToFloat64 =(buf)=> {
  const view = new DataView(buf);
  const n = buf.byteLength / 8;
  const arr = new Array(n);
  for (let i = 0; i < n; i++) arr[i] = view.getFloat64(i * 8, true);
  return arr;
}
const onchangeAll = (e) => {
  console.log('onchangeAll:', e);
};

const onchangeEnum = (e, val, id) => {
  console.log('onchangeEnum:', e, val, id);
};

const onchangeRight = (e, id) => {
  console.log('onchangeRight:', e, id);
};

// 处理枚举值选择变化
const onEnumValueChange = (e, item) => {
  
  // Vue 的 v-model 会自动更新 item.value.value，不需要手动设置
  // 这里可以添加其他需要在值变化时执行的逻辑
};
 const changeDate = (e, item) => {
    // 处理日期变化
    if (e && typeof e === 'object' && e.getTime) {
      // e 是一个 Date 对象
    } else if (e && typeof e === 'string') {
      // e 是一个日期字符串
      const dateValue = new Date(e);
      if (!isNaN(dateValue.getTime())) {
        // console.log('Date string changed:', e, 'parsed to:', dateValue, 'for item:', item);
      }
    }
    // Vue 的 v-model 会自动更新 item.value.value，不需要手动设置
    // 这里可以添加其他需要在值变化时执行的逻辑
  };
// 监听连接状态变化
watch(connectFlag, (newVal, oldVal) => {
  if (newVal && !oldVal) {
    // 连接状态从 false 变为 true 时，重置 firstFlagLoad 和 rootNodeLoaded
    // firstFlagLoad.value = true;
    rootNodeLoaded.value = false;
  }
});

// 处理 ExpandedNodeId dialog 确认
const handleExpandedNodeIdDialogConfirm = () => {
  // ExpandedNodeId 类型校验
  let hasError = false;
  const errorMessages = [];
  
  for (let i = 0; i < showExpandedNodeIdData.length; i++) {
    const item = showExpandedNodeIdData[i];
    
    if (!item.identifierType) {
      errorMessages.push(`Value ${i + 1}: 请选择IdentifierType`);
      hasError = true;
    }
    
    if (!item.value) {
      errorMessages.push(`Value ${i + 1}: 请输入Value`);
      hasError = true;
    } else if (item.identifierType === 'Numeric' && !isValidInteger(item.value)) {
      errorMessages.push(`Value ${i + 1}: Value必须是有效的整数`);
      hasError = true;
    } else if (item.identifierType === 'String' && typeof item.value !== 'string') {
      errorMessages.push(`Value ${i + 1}: Value必须是字符串`);
      hasError = true;
    }
    
    if (item.nsIndex !== undefined && !isValidUInteger(item.nsIndex)) {
      errorMessages.push(`Value ${i + 1}: NsIndex必须是有效的无符号整数`);
      hasError = true;
    }
    
    if (item.serverIndex !== undefined && !isValidUInteger(item.serverIndex)) {
      errorMessages.push(`Value ${i + 1}: ServerIndex必须是有效的无符号整数`);
      hasError = true;
    }
    
    if (item.namespaceUri !== undefined && typeof item.namespaceUri !== 'string') {
      errorMessages.push(`Value ${i + 1}: NamespaceUri必须是字符串`);
      hasError = true;
    }
  }
  
  if (hasError) {
    ElMessage.error(errorMessages.join('; '));
    return;
  }
  
  // 最终更新当前编辑 item 的值
  if (currentEditingItem.value) {
    try {
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = [...showExpandedNodeIdData];
      
      // 尝试更新原始 value，使用更安全的方式
      if (Array.isArray(currentEditingItem.value.value)) {
        // 使用 splice 来更新数组，避免直接赋值
        currentEditingItem.value.value.splice(0, currentEditingItem.value.value.length, ...showExpandedNodeIdData);
      } else if (currentEditingItem.value.value && Array.isArray(currentEditingItem.value.value.value)) {
        currentEditingItem.value.value.value.splice(0, currentEditingItem.value.value.value.length, ...showExpandedNodeIdData);
      }
      
      ElMessage.success('ExpandedNodeId更新成功！');
      
    } catch (error) {
      ElMessage.error('ExpandedNodeId更新失败，请重试');
      return;
    }
  }
  
  showExpandedNodeIdFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
};

// 处理 ImagePNG 变化
const handleImagePNGChange = (index, value) => {
  showImagePNGData[index] = value;
  if (currentEditingItem.value) {
    currentEditingItem.value._editValue = [...showImagePNGData];
  }
};

// 处理 ImagePNG dialog 确认
const handleImagePNGDialogConfirm = () => {
  // ImagePNG 类型校验：检查十六进制PNG格式
  let hasError = false;
  const errorMessages = [];
  
  for (let i = 0; i < showImagePNGData.length; i++) {
    const hexValue = showImagePNGData[i];
    
    if (!hexValue) {
      errorMessages.push(`PNG ${i + 1}: 请输入十六进制PNG数据`);
      hasError = true;
    } else if (!isValidHexString(hexValue)) {
      errorMessages.push(`PNG ${i + 1}: 请输入有效的十六进制字符串`);
      hasError = true;
    } else if (!isValidPNGFormat(hexValue)) {
      errorMessages.push(`PNG ${i + 1}: 请输入有效的PNG格式数据`);
      hasError = true;
    }
  }
  
  if (hasError) {
    ElMessage.error(errorMessages.join('; '));
    return;
  }
  
  if (currentEditingItem.value) {
    try {
      const binaryValues = showImagePNGData.map(val => hexToBinaryStringStrict(val));
      currentEditingItem.value._editValue = [...showImagePNGData];
      if (Array.isArray(currentEditingItem.value.value)) {
        currentEditingItem.value.value.splice(0, currentEditingItem.value.value.length, ...binaryValues);
      } else if (currentEditingItem.value.value && Array.isArray(currentEditingItem.value.value.value)) {
        currentEditingItem.value.value.value.splice(0, currentEditingItem.value.value.value.length, ...binaryValues);
      }
      
      ElMessage.success('ImagePNG更新成功！');
      
    } catch (error) {
      console.warn('Error updating ImagePNG item value:', error);
      ElMessage.error('ImagePNG更新失败，请重试');
      return;
    }
  }
  showImagePNGFlag.value = false;
  currentEditingItem.value = null;
};

// 将二进制字符串转换为十六进制（无空格、大写）
const binaryStringToHex = (str) => {
  if (!str || typeof str !== 'string') return '';
  let hex = '';
  for (let i = 0; i < str.length; i++) {
    const byte = str.charCodeAt(i) & 0xFF;
    hex += byte.toString(16).padStart(2, '0');
  }
  return hex.toUpperCase();
}

// 将十六进制（允许空格和非 hex 字符，自动清理）转回二进制字符串
const hexToBinaryStringStrict = (hex) => {
  if (!hex || typeof hex !== 'string') return '';
  const cleanHex = hex.replace(/[^0-9A-Fa-f]/g, '');
  if (cleanHex.length % 2 !== 0) {
    return '';
  }
  let result = '';
  for (let i = 0; i < cleanHex.length; i += 2) {
    const byte = parseInt(cleanHex.substr(i, 2), 16);
    result += String.fromCharCode(byte);
  }
  return result;
}

// Byte 位操作辅助函数
const byteToBits = (byteValue) => {
  const num = Number(byteValue) || 0;
  const bits = [];
  for (let i = 0; i < 8; i++) {
    bits.push((num & (1 << i)) !== 0);
  }
  return bits;
}

const bitsToByte = (bits) => {
  let result = 0;
  for (let i = 0; i < 8; i++) {
    if (bits[i]) {
      result |= (1 << i);
    }
  }
  return result;
}

const getIntegerMaxLength = (dataType) => {
  switch (dataType) {
    case 'Integer':
      return 10; // 假设最大长度为10位
    case 'UInteger':
      return 10; // 假设最大长度为10位
    default:
      return 10; // 默认最大长度为10位
  }
}

const clampIntegerByType = (dataType, value) => {
  if (value === '' || value === null || value === undefined) return value;
  const n = Number(value);
  if (Number.isNaN(n)) return value;
  switch ((dataType || '').toLowerCase()) {
    case 'int16': return Math.max(-32768, Math.min(32767, Math.trunc(n)));
    case 'uint16': return Math.max(0, Math.min(65535, Math.trunc(n)));
    case 'int32': return Math.max(-2147483648, Math.min(2147483647, Math.trunc(n)));
    case 'uint32': return Math.max(0, Math.min(4294967295, Math.trunc(n)));
    case 'int64': {
      // JS 安全范围内近似截断；如需 BigInt 可进一步增强
      const min = -9223372036854775808;
      const max = 9223372036854775807;
      return Math.max(min, Math.min(max, Math.trunc(n)));
    }
    case 'uint64': {
      const min = 0;
      const max = 18446744073709551615;
      return Math.max(min, Math.min(max, Math.trunc(n)));
    }
    default:
      return Math.trunc(n);
  }
}

const handleIntegerInput = (item, val) => {
  // 只允许可选负号 + 数字
  const raw = String(val ?? '');
  const cleaned = raw.replace(/[^0-9-]/g, '')
                     .replace(/(?!^)-/g, '') // 只保留开头的负号
                     .replace(/^(-?)0+(\d)/, '$1$2'); // 去掉多余前导0
  if (cleaned !== raw) {
    item._editValue = cleaned;
    return;
  }
  // 长度限制由 input maxlength 控制；这里做范围截断
  const clamped = clampIntegerByType(item.dataType, cleaned === '' || cleaned === '-' ? cleaned : Number(cleaned));
  item._editValue = clamped;
}

// 辅助函数：获取数据类型的默认值
const getDefaultValueForDataType = (dataType) => {
  const dataTypeStr = dataType.toString().toLowerCase();
  
  // Boolean类型
  if (dataTypeStr.includes('boolean')) return 'false';
  
  // 整数类型
  if (dataTypeStr.includes('int8') || dataTypeStr.includes('int16') || 
      dataTypeStr.includes('int32') || dataTypeStr.includes('int64') ||
      dataTypeStr.includes('integer')) return 0;
  
  // 无符号整数类型
  if (dataTypeStr.includes('uint8') || dataTypeStr.includes('uint16') || 
      dataTypeStr.includes('uint32') || dataTypeStr.includes('uint64') ||
      dataTypeStr.includes('uinteger')) return 0;
  
  // 浮点类型
  if (dataTypeStr.includes('float') || dataTypeStr.includes('double') || 
      dataTypeStr.includes('number')) return 0.0;
  
  // 字符串类型
  if (dataTypeStr.includes('string')) return '';
  
  // 日期时间类型
  if (dataTypeStr.includes('datetime')) return new Date().toISOString();
  
  // 字节类型
  if (dataTypeStr.includes('byte') && !dataTypeStr.includes('string')) return 0;
  if (dataTypeStr.includes('sbyte')) return 0;
  
  // NodeId类型
  if (dataTypeStr.includes('nodeid')) return 'ns=0;i=0';
  
  // ByteString类型
  if (dataTypeStr.includes('bytestring')) return '';
  
  // OptionSet类型
  if (dataTypeStr.includes('optionset')) return '0';
  
  // 默认返回空字符串
  return '';
};

// 辅助函数：获取数据类型名称
const getDataTypeName = (dataType) => {
  if (!dataType) return 'Unknown';
  
  // 如果是对象格式（接口返回的格式）
  if (typeof dataType === 'object') {
    const value = dataType._value;
    const nsIndex = dataType._nsIndex;
    
    // 根据命名空间和值映射到具体类型
    if (nsIndex === 0) {
      // 标准数据类型
      switch (value) {
        case 1: return 'Boolean';
        case 2: return 'SByte';
        case 3: return 'Byte';
        case 4: return 'Int16';
        case 5: return 'UInt16';
        case 6: return 'Int32';
        case 7: return 'UInt32';
        case 8: return 'Int64';
        case 9: return 'UInt64';
        case 10: return 'Float';
        case 11: return 'Double';
        case 12: return 'String';
        case 13: return 'DateTime';
        case 14: return 'Guid';
        case 15: return 'ByteString';
        case 16: return 'XmlElement';
        case 17: return 'NodeId';
        case 18: return 'ExpandedNodeId';
        case 19: return 'StatusCode';
        case 20: return 'QualifiedName';
        case 21: return 'LocalizedText';
        case 22: return 'ExtensionObject';
        case 23: return 'DataValue';
        case 24: return 'Variant';
        case 25: return 'DiagnosticInfo';
        default: return `DataType_${value}`;
      }
    } else if (nsIndex === 3) {
      // 自定义数据类型（根据值映射）
      switch (value) {
        case 3001: return 'HeaterStatus';
        case 3005: return 'Priority';
        default: 
          // 尝试从 allTypesData 中查找对应的类型名称
          const foundType = state.allTypesData?.find(type => {
            if (type.nodeId && type.nodeId._value === value && type.nodeId._nsIndex === nsIndex) {
              return true;
            }
            return false;
          });
          return foundType?.name || foundType?._browseName || foundType?.displayName?.text || `CustomType_${value}`;
      }
    } else {
      return `Type_${nsIndex}_${value}`;
    }
  }
  
  // 如果是字符串格式
  const dataTypeStr = dataType.toString();
  
  // 提取数据类型名称，去掉命名空间前缀
  const parts = dataTypeStr.split(':');
  if (parts.length > 1) {
    return parts[parts.length - 1];
  }
  
  return dataTypeStr;
};

// 辅助函数：获取描述文本
const getDescriptionText = (description) => {
  if (!description) return '';
  
  // 如果是 UaLocalizedText 对象，提取文本
  if (typeof description === 'object' && description.text) {
    return description.text;
  }
  
  // 如果是字符串，直接返回
  if (typeof description === 'string') {
    return description;
  }
  
  return '';
};

// 辅助函数：安全获取参数属性
const getArgumentProperty = (arg, property) => {
  if (!arg) return '';
  
  // 尝试直接访问属性
  if (arg[property] !== undefined) {
    return arg[property];
  }
  
  // 尝试访问私有属性（以下划线开头）
  const privateProperty = '_' + property;
  if (arg[privateProperty] !== undefined) {
    return arg[privateProperty];
  }
  
  return '';
};

// 辅助函数：获取特定方法的选项
const getOptionsForMethod = (methodName, argName, dataType) => {
  
  if (!dataType || !state.allTypesData || state.allTypesData.length === 0) {
    return [];
  }
  
  // 根据 dataType 在 allTypesData 中查找对应的枚举值
  const dataTypeStr = dataType.toString();
  
  // 先打印 allTypesData 的结构来调试
  
  const foundType = state.allTypesData.find(type => {
    
    // 检查 nodeId 是否匹配
    if (type.nodeId && type.nodeId.toString() === dataTypeStr) {
      return true;
    }
    
    // 检查 _browseName 是否匹配
    if (type._browseName && type._browseName === dataTypeStr) {
      return true;
    }
    
    // 检查 _displayName 是否匹配
    if (type._displayName && type._displayName.text === dataTypeStr) {
      return true;
    }
    
    // 检查是否有其他可能的匹配方式
    if (type.browseName && type.browseName === dataTypeStr) {
      return true;
    }
    
    if (type.displayName && type.displayName.text === dataTypeStr) {
      return true;
    }
    
    return false;
  });
  
  
  
  
  if (foundType && foundType.enumValues) {
    
    // 检查 enumValues 是否为 Map 对象
    if (foundType.enumValues instanceof Map) {
      // 将 Map 转换为数组格式
      const enumArray = Array.from(foundType.enumValues.entries()).map(([value, textObj]) => ({
        value: value,
        label: `${value} (${textObj._text || 'Unknown'})`
      }));
      return enumArray;
    }
    
    // 如果已经是数组格式
    if (Array.isArray(foundType.enumValues)) {
      return foundType.enumValues.map(enumValue => ({
        value: enumValue.value,
        label: `${enumValue.value} (${enumValue.displayName?.text || enumValue.name || 'Unknown'})`
      }));
    }
  }
  
  return [];
};

// Method Call 相关函数
const handleMethodCall = async (methodNode) => {
  currentMethodNode.value = methodNode;
  
  if (methodNode) {
    
    // 获取正确的URL
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;
    
    if (!url) {
      ElMessage.error('请先配置服务器连接');
      return;
    }
    
    let apiConfig = new Configuration({
      basePath: url
    });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);
    var methodArguments = await testOpcServer.readMethodArguments(methodNode.nodeIdNum);
    
    // 根据 methodArguments 填充输入参数
    if (methodArguments.inputArguments && methodArguments.inputArguments.length > 0) {
      methodInputArgs.splice(0, methodInputArgs.length, 
        ...methodArguments.inputArguments.map(arg => {
          let dataTypes = state.allTypesData.find(item => item.nodeId.toString() === arg.dataType.toString())
          return {
            name: getArgumentProperty(arg, 'name'),
            value: getDefaultValueForDataType(getArgumentProperty(arg, 'dataType')),
            // dataType: getDataTypeName(getArgumentProperty(arg, 'dataType')),
            dataTypesObj: dataTypes,
            dataType:dataTypes?._parentType?._browseName || dataTypes?._browseName || dataTypes?.browseName,
            description: getDescriptionText(getArgumentProperty(arg, 'description')),
            options: getOptionsForMethod(methodNode.displayName?._text || methodNode.displayName, getArgumentProperty(arg, 'name'), getArgumentProperty(arg, 'dataType'))
          }
        })
      );
    } else {
      methodInputArgs.splice(0, methodInputArgs.length);
    }
    
    // 根据 methodArguments 填充输出参数
    if (methodArguments.outputArguments && methodArguments.outputArguments.length > 0) {
      methodOutputArgs.splice(0, methodOutputArgs.length,
        ...methodArguments.outputArguments.map(arg => 
         { 
          let dataTypes = state.allTypesData.find(item => item.nodeId.toString() === arg.dataType.toString())
          return {
          name: getArgumentProperty(arg, 'name'),
          // value: '',
          value: getDefaultValueForDataType(getArgumentProperty(arg, 'dataType')) ,
          // dataType: getDataTypeName(getArgumentProperty(arg, 'dataType')),
          dataType:dataTypes?._parentType?._browseName || dataTypes?._browseName || dataTypes?.browseName,
          description: getDescriptionText(getArgumentProperty(arg, 'description')),
          options: getOptionsForMethod(methodNode.displayName?._text || methodNode.displayName, getArgumentProperty(arg, 'name'), getArgumentProperty(arg, 'dataType'))
        }
      }
      ))
      ;
    } else {
      methodOutputArgs.splice(0, methodOutputArgs.length);
    }
  }
  
   try {
     // 所有数据都从接口获取，不再使用硬编码数据
     // 如果接口没有返回数据，则显示空状态
     if (!methodArguments.inputArguments || methodArguments.inputArguments.length === 0) {
       methodInputArgs.splice(0, methodInputArgs.length);
     }
     
     if (!methodArguments.outputArguments || methodArguments.outputArguments.length === 0) {
       methodOutputArgs.splice(0, methodOutputArgs.length);
     }
      
      methodResult.value = '';
    
    showMethodCallFlag.value = true;
    
  } catch (error) {
    console.error('Error getting method arguments:', error);
    ElMessage.error('获取方法参数失败');
  }
};
const handleVariables = async(node,data) => {
  // 确保 selectedTopNodeId 有值
  if (!selectedTopNodeId.value) {
    ElMessage.warning('请先选择顶层节点');
    return;
  }
  
  // 从 urlDatas store 中获取 URL，如果没有则使用 urlForm.value.url
  const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
  let url = allData?.url || urlForm.value.url;
  
  if (!url) {
    ElMessage.warning('请先配置服务器连接');
    return;
  }
  
  handleVariableClick(node, data, url);
}
// 辅助函数：判断是否为数字类型
const isNumericType = (dataType) => {
  const numericTypes = [
    'Double', 'Float', 'Number',
    'Integer', 'Int8', 'Int16', 'Int32', 'Int64',
    'UInteger', 'UInt8', 'UInt16', 'UInt32', 'UInt64',
    'Byte', 'SByte'
  ];
  return numericTypes.includes(dataType);
};

// 辅助函数：判断是否为浮点类型
const isFloatType = (dataType) => {
  const floatTypes = ['Double', 'Float', 'Number'];
  return floatTypes.includes(dataType);
};

// 辅助函数：判断是否为无符号类型
const isUnsignedType = (dataType) => {
  const unsignedTypes = ['UInteger', 'UInt8', 'UInt16', 'UInt32', 'UInt64', 'Byte'];
  return unsignedTypes.includes(dataType);
};

// 辅助函数：根据枚举名称查找对应的数字值
const findEnumValueByName = (enumName, dataType) => {
  // 从allTypesData中查找对应的枚举类型
  const enumType = state.allTypesData.find(item => 
    item._browseName === dataType || 
    item._parentType?._browseName === dataType ||
    item.browseName === dataType
  );
  
  if (enumType && enumType.enumValues) {
    // 检查 enumValues 是否为 Map 对象
    if (enumType.enumValues instanceof Map) {
      for (let [key, value] of enumType.enumValues) {
        if (value.displayName === enumName || value.name === enumName) {
          return value.value;
        }
      }
    } else if (Array.isArray(enumType.enumValues)) {
      // 如果是数组格式
      const enumItem = enumType.enumValues.find(item => 
        item.displayName === enumName || item.name === enumName
      );
      if (enumItem) {
        return enumItem.value;
      }
    } else if (typeof enumType.enumValues === 'object') {
      // 如果是对象格式
      for (let key in enumType.enumValues) {
        const enumItem = enumType.enumValues[key];
        if (enumItem.displayName === enumName || enumItem.name === enumName) {
          return enumItem.value;
        }
      }
    }
  }
  
  // 如果没找到，返回0
  return 0;
};

// 辅助函数：验证NodeId格式
// 数字类型输入校验
const validateNumericInput = (arg, value) => {
  const dataType = arg.dataType;
  
  // 移除所有非数字字符（除了负号和小数点）
  let cleanValue = value.replace(/[^0-9.\-]/g, '');
  
  // 处理多个小数点的情况
  const parts = cleanValue.split('.');
  if (parts.length > 2) {
    cleanValue = parts[0] + '.' + parts.slice(1).join('');
  }
  
  // 处理多个负号的情况
  if (cleanValue.indexOf('-') > 0) {
    cleanValue = cleanValue.replace(/-/g, '');
  }
  
  // 根据数据类型进行范围校验
  if (cleanValue !== '') {
    const numValue = parseFloat(cleanValue);
    
    if (isUnsignedType(dataType) && numValue < 0) {
      cleanValue = '0';
    }
    
    // 检查整数类型的范围
    if (!isFloatType(dataType) && cleanValue.includes('.')) {
      cleanValue = Math.floor(numValue).toString();
    }
  }
  
  arg.value = cleanValue;
};

// 阻止非数字字符输入
const preventNonNumericInput = (arg, event) => {
  const dataType = arg.dataType;
  const char = String.fromCharCode(event.which);
  
  // 允许的字符：数字、小数点、负号、退格、删除等控制键
  const allowedChars = /[0-9.\-]/;
  const controlKeys = [8, 9, 27, 46, 110, 190]; // 退格、Tab、Esc、删除、小数点
  
  if (!allowedChars.test(char) && !controlKeys.includes(event.which)) {
    event.preventDefault();
    ElMessage.warning(`${dataType}类型只能输入数字`);
  }
};

// NodeId输入校验
const validateNodeIdInput = (arg, value) => {
  // 只允许字母、数字、分号、等号、冒号、下划线
  const cleanValue = value.replace(/[^a-zA-Z0-9;=:_]/g, '');
  arg.value = cleanValue;
};

// 阻止无效NodeId字符输入
const preventInvalidNodeIdInput = (event) => {
  const char = String.fromCharCode(event.which);
  const allowedChars = /[a-zA-Z0-9;=:_]/;
  const controlKeys = [8, 9, 27, 46]; // 退格、Tab、Esc、删除
  
  if (!allowedChars.test(char) && !controlKeys.includes(event.which)) {
    event.preventDefault();
    ElMessage.warning('NodeId只能包含字母、数字、分号、等号、冒号、下划线');
  }
};

// ByteString输入校验
const validateByteStringInput = (arg, value) => {
  // 只允许十六进制字符
  const cleanValue = value.replace(/[^0-9A-Fa-f]/g, '').toUpperCase();
  arg.value = cleanValue;
};

// 阻止无效十六进制字符输入
const preventInvalidHexInput = (event) => {
  const char = String.fromCharCode(event.which);
  const allowedChars = /[0-9A-Fa-f]/;
  const controlKeys = [8, 9, 27, 46]; // 退格、Tab、Esc、删除
  
  if (!allowedChars.test(char) && !controlKeys.includes(event.which)) {
    event.preventDefault();
    ElMessage.warning('ByteString只能输入十六进制字符(0-9, A-F)');
  }
};

// 字符串输入校验
const validateStringInput = (arg, value) => {
  const dataType = arg.dataType;
  
  // 检查是否包含汉字
  if (/[\u4e00-\u9fa5]/.test(value)) {
    ElMessage.warning('不支持输入中文字符');
    return;
  }
  
  // 根据数据类型进行格式校验
  let cleanValue = value;
  
  switch (dataType) {
    case 'String':
      // 允许字母、数字、空格、特殊字符
      cleanValue = value.replace(/[^\w\s\-_.,!?@#$%^&*()+=]/g, '');
      break;
    case 'QualifiedName':
      // 限定名称格式
      cleanValue = value.replace(/[^a-zA-Z0-9:_]/g, '');
      break;
    case 'LocalizedText':
      // 本地化文本
      cleanValue = value.replace(/[^\w\s\-_.,!?@#$%^&*()+=]/g, '');
      break;
    default:
      // 默认只允许字母、数字、空格、基本标点
      cleanValue = value.replace(/[^\w\s\-_.,]/g, '');
  }
  
  arg.value = cleanValue;
};

// 阻止无效字符串字符输入
const preventInvalidStringInput = (arg, event) => {
  const dataType = arg.dataType;
  const char = String.fromCharCode(event.which);
  
  // 检查中文字符
  if (/[\u4e00-\u9fa5]/.test(char)) {
    event.preventDefault();
    ElMessage.warning('不支持输入中文字符');
    return;
  }
  
  let allowedChars;
  switch (dataType) {
    case 'String':
      allowedChars = /[\w\s\-_.,!?@#$%^&*()+=]/;
      break;
    case 'QualifiedName':
      allowedChars = /[a-zA-Z0-9:_]/;
      break;
    case 'LocalizedText':
      allowedChars = /[\w\s\-_.,!?@#$%^&*()+=]/;
      break;
    default:
      allowedChars = /[\w\s\-_.,]/;
  }
  
  const controlKeys = [8, 9, 27, 46]; // 退格、Tab、Esc、删除
  
  if (!allowedChars.test(char) && !controlKeys.includes(event.which)) {
    event.preventDefault();
    ElMessage.warning(`${dataType}类型包含无效字符`);
  }
};

// Enumeration输入校验
const validateEnumerationInput = (arg, value) => {
  // 允许数字、字母、下划线、连字符
  const cleanValue = value.replace(/[^0-9a-zA-Z_\-]/g, '');
  arg.value = cleanValue;
};

// 阻止无效Enumeration字符输入
const preventInvalidEnumerationInput = (event) => {
  const char = String.fromCharCode(event.which);
  const allowedChars = /[0-9a-zA-Z_\-]/;
  const controlKeys = [8, 9, 27, 46]; // 退格、Tab、Esc、删除
  
  if (!allowedChars.test(char) && !controlKeys.includes(event.which)) {
    event.preventDefault();
    ElMessage.warning('Enumeration只能输入数字、字母、下划线、连字符');
  }
};

// 获取枚举选项用于输出参数显示
const getEnumerationOptions = (dataType) => {
  // 从allTypesData中查找对应的枚举类型
  const enumType = state.allTypesData.find(item => 
    item._browseName === dataType || 
    item._parentType?._browseName === dataType ||
    item.browseName === dataType
  );
  
  if (enumType && enumType.enumValues) {
    // 检查 enumValues 是否为 Map 对象
    if (enumType.enumValues instanceof Map) {
      return Array.from(enumType.enumValues.values()).map(item => ({
        label: item.displayName || item.name || `Value_${item.value}`,
        value: item.value
      }));
    } else if (Array.isArray(enumType.enumValues)) {
      // 如果是数组格式
      return enumType.enumValues.map(item => ({
        label: item.displayName || item.name || `Value_${item.value}`,
        value: item.value
      }));
    } else if (typeof enumType.enumValues === 'object') {
      // 如果是对象格式
      return Object.values(enumType.enumValues).map(item => ({
        label: item.displayName || item.name || `Value_${item.value}`,
        value: item.value
      }));
    }
  }
  
  // 如果没找到枚举值，返回空数组
  return [];
};

const handleMethodArgClick = (arg, type, index) => {
  if (arg.dataType && arg.dataType.includes('OptionSet')) {
    currentEditingMethodArg.value = { arg, type, index };
    generateOptionSetData(arg.value);
    // showByteFlag.value = true;
  } else if (arg.dataType === 'ByteString') {
    // 处理ByteString类型的特殊编辑
    currentEditingMethodArg.value = { arg, type, index };
    // 可以在这里添加ByteString的特殊处理逻辑
    ElMessage.info('ByteString编辑功能待实现');
  }
};

const generateOptionSetData = (currentValue) => {
  // 生成OptionSet选项数据
  const options = [
    { name: 'AA', selected: false, value: false },
    { name: 'BB', selected: false, value: false },
    { name: 'CC', selected: false, value: false },
    { name: 'DD', selected: false, value: false },
    { name: 'EE', selected: false, value: false },
    { name: 'FF', selected: false, value: false },
    { name: 'GG', selected: false, value: false },
    { name: 'HH', selected: false, value: false }
  ];
  
  // 根据当前值设置选项状态
  const value = parseInt(currentValue) || 0;
  options.forEach((option, index) => {
    const bitValue = (value >> index) & 1;
    option.selected = bitValue === 1;
    option.value = bitValue === 1;
  });
  
  methodOptionSetData.splice(0, methodOptionSetData.length, ...options);
  currentOptionSetValue.value = value;
};

const handleOptionSetSelectionChange = (index) => {
  // 当选择状态改变时，同步更新值状态
  const option = methodOptionSetData[index];
  option.value = option.selected;
  updateOptionSetValue();
};

const handleOptionSetValueChange = (index) => {
  // 当值改变时，同步更新选择状态
  const option = methodOptionSetData[index];
  option.selected = option.value;
  updateOptionSetValue();
};

const updateOptionSetValue = () => {
  let value = 0;
  methodOptionSetData.forEach((option, index) => {
    if (option.selected && option.value) {
      value |= (1 << index);
    }
  });
  currentOptionSetValue.value = value;
};

const handleMethodOptionSetConfirm = () => {
  // MethodOptionSet 校验：检查选项集值
  if (!currentOptionSetValue.value && currentOptionSetValue.value !== 0) {
    ElMessage.error('请选择选项集值');
    return;
  }
  
  // 检查当前编辑的参数是否存在
  if (!currentEditingMethodArg.value) {
    ElMessage.error('当前编辑参数不存在');
    return;
  }
  
  if (currentEditingMethodArg.value) {
    const { arg, type, index } = currentEditingMethodArg.value;
    const newValue = currentOptionSetValue.value.toString();
    
    // 更新当前编辑的参数对象
    arg.value = newValue;
    
    // 更新对应的参数数组中的值
    if (type === 'input') {
      methodInputArgs[index].value = newValue;
    } else if (type === 'output') {
      methodOutputArgs[index].value = newValue;
    }
    
  }
  
  showMethodOptionSetFlag.value = false;
  currentEditingMethodArg.value = null;
};

// 将输入参数转换为UaVariant格式
const convertToUaVariant = (value, dataType) => {
  try {
    switch (dataType) {
      case 'Boolean':
        return UaVariant.boolean(value === 'true' || value === true);
        case 'Enumeration':
          // 处理枚举类型，支持数字值和枚举名称
          if (typeof value === 'string' && isNaN(value)) {
            // 如果是字符串且不是数字，尝试从枚举值中查找对应的数字
            const enumValue = findEnumValueByName(value, dataType);
            return UaVariant.integer(enumValue || 0, UaVariantType.Int32);
          } else {
            // 如果是数字或数字字符串，直接转换
            return UaVariant.integer(parseInt(value) || 0, UaVariantType.Int32);
          }
      case 'SByte':
        return UaVariant.integer(parseInt(value) || 0);
      case 'Byte':
        return UaVariant.integer(parseInt(value) || 0);
      case 'Int8':
        return UaVariant.integer(parseInt(value) || 0);
      case 'Int16':
        return UaVariant.integer(parseInt(value) || 0);
      case 'UInt16':
        return UaVariant.integer(parseInt(value) || 0);
      case 'Int32':
        return UaVariant.integer(parseInt(value) || 0);
      case 'UInt32':
        return UaVariant.integer(parseInt(value) || 0);
      case 'Int64':
        return UaVariant.integer(parseInt(value) || 0);
      case 'UInt64':
        return UaVariant.integer(parseInt(value) || 0);
      case 'Integer':
        return UaVariant.integer(parseInt(value) || 0);
      case 'UInteger':
        return UaVariant.integer(parseInt(value) || 0);
      case 'Float':
        return UaVariant.integer(parseFloat(value) || 0);
      case 'Double':
        return UaVariant.integer(parseFloat(value) || 0);
      case 'Number':
        return UaVariant.integer(parseFloat(value) || 0);
      case 'String':
        return UaVariant.integer(value || '');
      case 'DateTime':
        return UaVariant.integer(new Date(value));
      case 'NodeId':
        return UaVariant.integer(value || 'ns=0;i=0');
      case 'ByteString':
        return UaVariant.integer(value || '');
      case 'OptionSet':
        return UaVariant.integer(parseInt(value) || 0);
      case 'ExpandedNodeId':
        return UaVariant.expandedNodeId(new UaExpandedNodeId(value));
      case 'StatusCode':
        return UaVariant.statusCode(makeUaStatusCode());
      case 'QualifiedName':
        return UaVariant.qualifiedName(value || '');
      case 'LocalizedText':
        return UaVariant.localizedText(new UaLocalizedText(value));
      default:
        // 对于未知类型，尝试作为字符串处理
        return UaVariant.string(value || '');
    }
  } catch (error) {
    console.error(`Error converting value ${value} to ${dataType}:`, error);
    // 如果转换失败，返回字符串类型
    return UaVariant.string(value || '');
  }
};

// 将UaVariant结果转换为可显示的值
const convertFromUaVariant = (uaVariant) => {
  if (!uaVariant) return '';
  
  try {
    const value = uaVariant.value;
    const type = uaVariant.type;
    
    switch (type) {
      case UaVariantType.Boolean:
        return value ? 'true' : 'false';
      case UaVariantType.SByte:
      case UaVariantType.Byte:
      case UaVariantType.Int16:
      case UaVariantType.UInt16:
      case UaVariantType.Int32:
      case UaVariantType.UInt32:
      case UaVariantType.Int64:
      case UaVariantType.UInt64:
        return value.toString();
      case UaVariantType.Float:
      case UaVariantType.Double:
        return value.toString();
      case UaVariantType.String:
        return value || '';
      case UaVariantType.DateTime:
        return value instanceof Date ? value.toISOString() : value.toString();
      case UaVariantType.ByteString:
        return value || '';
      case UaVariantType.NodeId:
        return value ? value.toString() : '';
      case UaVariantType.ExpandedNodeId:
        return value ? value.toString() : '';
      case UaVariantType.StatusCode:
        return value ? value.toString() : '';
      case UaVariantType.QualifiedName:
        return value || '';
      case UaVariantType.LocalizedText:
        return value ? value.toString() : '';
      default:
        return value ? value.toString() : '';
    }
  } catch (error) {
    console.error('Error converting UaVariant to display value:', error);
    return '';
  }
};

const handleMethodCallExecute = async (event,arg) => {
  // MethodCall 校验：检查输入参数
  
  let hasError = false;
  const errorMessages = [];
  
  // 检查必需的输入参数
  for (let i = 0; i < methodInputArgs.length; i++) {
    const arg = methodInputArgs[i];
    
    if (!arg.value && arg.value !== 0) {
      errorMessages.push(`参数 ${arg.name}: 请输入值`);
      hasError = true;
    } else {
       
      // 根据数据类型进行校验
      switch (arg.dataType) {
        case 'Double':
        case 'Float':
        case 'Number':
          if (isNaN(parseFloat(arg.value))) {
            errorMessages.push(`参数 ${arg.name}: 请输入有效的数字`);
            hasError = true;
          }
          break;
        case 'Int32':
        case 'Int16':
        case 'Int64':
        case 'Int8':
        case 'Integer':
        case 'SByte':
          if (isNaN(parseInt(arg.value))) {
            errorMessages.push(`参数 ${arg.name}: 请输入有效的整数`);
            hasError = true;
          }
          break;
        case 'UInt32':
        case 'UInt16':
        case 'UInt64':
        case 'UInt8':
        case 'UInteger':
        case 'Byte':
          const intValue = parseInt(arg.value);
          if (isNaN(intValue) || intValue < 0) {
            errorMessages.push(`参数 ${arg.name}: 请输入有效的无符号整数`);
            hasError = true;
          }
          break;
        case 'Boolean':
          if (arg.value !== 'true' && arg.value !== 'false') {
            errorMessages.push(`参数 ${arg.name}: 请输入 true 或 false`);
            hasError = true;
          }
          break;
        case 'String':
          if (typeof arg.value !== 'string') {
            errorMessages.push(`参数 ${arg.name}: 请输入有效的字符串`);
            hasError = true;
          }
          break;
        case 'DateTime':
          if (arg.value && isNaN(new Date(arg.value).getTime())) {
            errorMessages.push(`参数 ${arg.name}: 请输入有效的日期时间`);
            hasError = true;
          }
          break;
        case 'NodeId':
          if (arg.value && !/^ns=\d+;(i|s|g|b)=.+$/.test(arg.value)) {
            errorMessages.push(`参数 ${arg.name}: NodeId格式不正确`);
            hasError = true;
          }
          break;
        case 'ByteString':
          if (arg.value && !/^[0-9A-Fa-f]*$/.test(arg.value)) {
            errorMessages.push(`参数 ${arg.name}: ByteString应为十六进制字符串`);
            hasError = true;
          }
          break;
        default:
          // 对于其他类型，进行基本验证
          if (arg.value === undefined || arg.value === null) {
            errorMessages.push(`参数 ${arg.name}: 请输入值`);
            hasError = true;
          }
          break;
      }
    }
  }
  if (hasError) {
    ElMessage.error(errorMessages.join('; '));
    return;
  }
  
  try {
    // 获取方法节点和对象节点信息
    const methodNode = currentMethodNode.value;
    if (!methodNode) {
      ElMessage.error('未找到方法节点');
      return;
    }
    
    // 创建OPC UA客户端
    // 获取正确的URL
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;
    
    if (!url) {
      ElMessage.error('请先配置服务器连接');
      return;
    }
    
    let apiConfig = new Configuration({
      basePath: url
    });
    let clientConfig = new UaClientConfiguration(apiConfig);
    let testOpcServer = new UaWebClient(clientConfig);
    
    // 获取对象ID（方法的父节点）
    const objectId = methodNode.parent?.data?.nodeId || methodNode.nodeId;
    const methodId = methodNode.nodeId;
    
    
    // 将输入参数转换为UaVariant数组
    const inputVariants = methodInputArgs.map(arg => {

      const variant = convertToUaVariant(arg.value, arg.dataType);
      return variant;
    });
    
    // 调用methodCall接口
    const outputVariants = await testOpcServer.methodCall(objectId, methodId, inputVariants);
    
    
    // 将返回结果更新到输出参数
    if (outputVariants && outputVariants.length > 0) {
      for (let i = 0; i < Math.min(outputVariants.length, methodOutputArgs.length); i++) {
        const outputArg = methodOutputArgs[i];
        const outputVariant = outputVariants[i];
        
        if (outputArg && outputVariant) {
          let label =outputArg.options.find(item => item.value === outputVariant.value)?.label || '';
          if(outputArg.dataType === 'Enumeration'){
            outputArg.value = label;
          }else{
            outputArg.value = convertFromUaVariant(outputVariant);
          }
        }
      }
    }
    
    // 更新方法调用结果
    methodResult.value = 'Succeeded';
    ElMessage.success('方法调用成功');
    
  } catch (error) {
    console.error('Method call failed:', error);
    methodResult.value = `Method call failed: ${error.message}`;
    ElMessage.error(`方法调用失败: ${error.message}`);
  }
};

// 手动加载子节点的函数
const loadChildrenForNode = async (data, node) => {
  
  try {
    // 获取节点ID
    let id = data.nodeId?._nodeId?._value || data.nodeIdNum;
    
    // 设置状态
    fixedNodeId.value = id;
    selectedNodeId.value = data.nodeId;
    
    // 获取浏览数据
    let allArr = [];
    let continuationPoint = null;
    
    const firstRes = await getBrowseDatas();
    allArr = firstRes?.results || [];
    continuationPoint = firstRes?.ContinuationPoint;
    
    // 循环获取后续页数据
    while (continuationPoint) {
      continuationPoints.value = continuationPoint;
      const nextRes = await getBrowseNextDatas();
      if (nextRes?.results) {
        allArr = allArr.concat(nextRes.results);
      }
      continuationPoint = nextRes?.ContinuationPoint;
    }
    
    
    // 处理数据：格式化子节点
    const childNodes = allArr.map(item => {
      const hasChildren = !!item.hasChildren;
      return {
        nodeId: item.nodeId,
        nodeIdNum: item.nodeId?._nodeId?.toString(),
        label: item.displayName?._text || item.browseName || 'Unnamed Node',
        isLeaf: !hasChildren,
        nodeClass: item.nodeClass,
        NodeClassType: item.NodeClassType,
        hasChildren: hasChildren,
        children: hasChildren ? [] : undefined
      };
    });
    
    
    // 使用更精确的方式更新数据，只更新目标节点的 children
    // 直接更新 data.children，这样不会破坏父子关系
    data.children = childNodes;
    
    // 同时更新 state.bottomTreeData 中对应的节点，保持数据同步
    const updateSpecificNode = (nodes, targetNodeIdNum, newChildren) => {
      for (let i = 0; i < nodes.length; i++) {
        if (nodes[i].nodeIdNum === targetNodeIdNum) {
          nodes[i].children = newChildren;
          return true;
        }
        // 递归查找子节点
        if (nodes[i].children && nodes[i].children.length > 0) {
          if (updateSpecificNode(nodes[i].children, targetNodeIdNum, newChildren)) {
            return true;
          }
        }
      }
      return false;
    };
    
    // 更新树数据中的对应节点
    const updated = updateSpecificNode(state.bottomTreeData, data.nodeIdNum, childNodes);
    
    // 使用 nextTick 确保 DOM 更新完成
    await nextTick();
    
    
  } catch (error) {
    console.error('🚀 Error loading children:', error);
  }
};

// 处理节点展开事件
const handleNodeExpand = (data, node) => {
   
  
  // 使用 data.nodeIdNum 作为节点的唯一标识
  const nodeKey = data.nodeIdNum || data.nodeId;
  
  // 如果节点有子节点但 children 为空，且没有正在加载，则手动触发加载
  if (data.hasChildren && (!data.children || data.children.length === 0) && !loadingNodes.value.has(nodeKey)) {
    loadingNodes.value.add(nodeKey);
    loadChildrenForNode(data, node).finally(() => {
      loadingNodes.value.delete(nodeKey);
    });
  }
};


// 注册组件
const components = {
};
</script>

 
