

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
                  {{ getDisplayName(data) || data.BrowseName }}
                </span>
              </p>
                <template #dropdown v-if="currentDrop === node.id">
                  <el-dropdown-menu>
                    <el-dropdown-item v-if="isDiscoveryNode(data)" @click="handleDiscoveryFind">
                      Find
                    </el-dropdown-item>
                    <el-dropdown-item v-if="!isDiscoveryNode(data)" @click="handleBottomDelete(data)" >
                      Delete
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <!-- 当node.level == 1时，Discovery 使用右键菜单，其余根节点保留 Add -->
              <p v-else>
                <el-icon v-if="node.level == 1" class="cus-icons">
                  <FolderOpened />
                </el-icon>
                <!-- <el-icon v-else><Link /></el-icon> -->
                <el-icon class="cus-icons-connect" v-else-if="connectFlag"><Connection /></el-icon>
                <el-icon class="cus-icons-connect" v-else="!connectFlag"><SwitchButton /></el-icon>
                <el-dropdown
                  v-if="isDiscoveryNode(data)"
                  trigger="contextmenu"
                  :ref="'dropdown' + (data.nodeIdNum || data.nodeId || data.id)"
                  @visible-change="(visible) => handleVisibleChange(node, visible)"
                >
                  <span class="cus-label">
                    {{ getDisplayName(data) || data.BrowseName }}
                  </span>
                  <template #dropdown v-if="currentDrop === node.id">
                    <el-dropdown-menu>
                      <el-dropdown-item @click="handleDiscoveryFind">
                        Find
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
                <el-dropdown
                  v-else
                  :ref="el => setAddDropdownRef(data.NodeId, el)"
                  @command="(command) => handleAddCommand(command, node, data)"
                  trigger="manual"
                  placement="bottom-start"
                >
                  <span class="cus-label" @contextmenu="(e) => showAddDropdown(e, data.NodeId)">
                  {{ getDisplayName(data) || data.BrowseName }}
                </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="add">Add</el-dropdown-item>
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
      :draggable="false"
      :virtual-scrolling="true"
      :virtual-scroll-item-height="32"
      :virtual-scroll-buffer-size="10"
      style="  overflow-y: auto; overflow-x: hidden;"
      class="optimized-tree">
            <template class="custom-tree-node" #default="{ node, data }">
                      <!-- 加载更多按钮（只在二级节点显示，且 continuationPoint 存在时） -->
                      <div 
                        v-if="data.isLoadMore && node.level != 1 && nodeContinuationPoints.get(data.parentNodeId)" 
                        class="load-more-node"
                        @click.stop="loadNextPageForNode(data.parentNodeId)"
                      >
                        <span class="load-more-text">...</span>
                      </div>
                      <!-- 普通节点 -->
                      <p 
                        v-else
                        class="node-label draggable-node" 
                        @dblclick="() => handleNodeDblClick(node, data)"
                        draggable="true"
                        @dragstart="handleDragStart($event, node, data)"
                        @dragend="handleDragEnd"
                      >
                        <el-dropdown
                          trigger="contextmenu"
                          :ref="'dropdown' + (data.nodeIdNum || data.nodeId || data.id)"
                          @visible-change="(visible) => handleVisibleChange(node, visible)"
                        >
                        <p class="node-content" draggable="false">
                          <i :class="getNodeIcon(data)"></i>
                          <span class="node-label cus-label">
                            {{ data.label}}
                          </span>
                        </p>
                        <!-- <template #dropdown v-if="currentDrop === node.id && Number(data.nodeClass) === 4">
                            <el-dropdown-menu>
                              <el-dropdown-item 
                                :disabled="isNewProject" 
                                @click="handleMethodCall(data, node)">
                                Call
                              </el-dropdown-item>
                            </el-dropdown-menu>
                          </template> -->
                          <template #dropdown v-if="currentDrop === node.id && Number(data.nodeClass) === 4">
                            <el-dropdown-menu>
                              <el-dropdown-item 
                              class="icon-reload-item"
                                @click="handleRebrowse(data, node)"> 
                                <i class="node-icon icon-reload"></i> Rebrowse
                              </el-dropdown-item>
                              <el-dropdown-item 
                                :disabled="isNewProject" 
                                @click="handleMethodCall(data, node)"> 
                                <el-icon><Cellphone /></el-icon> Call
                              </el-dropdown-item>
                            </el-dropdown-menu>
                          </template>
                          <template #dropdown v-else>
                            <el-dropdown-menu>
                              <el-dropdown-item 
                                class="icon-reload-item"
                                @click="handleRebrowse(data, node)"> 
                                <i class="node-icon icon-reload"></i> Rebrowse
                              </el-dropdown-item>
                              <el-dropdown-item 
                                :disabled="isNewProject" 
                                @click="handleVariables(node,data)">
                                <el-icon><Operation /></el-icon> Variables
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
                <div v-else ref="variablesTableBodyRef" class="table-boxs smooth-scroll" @contextmenu="onCtx"  >
                  <div class="panel-header-table">
                    <span class="header-index">Index</span>
                    <span class="header-name">Name</span>
                    <span class="header-type">DataType</span>
                    <span class="header-his">Historizing</span>
                    <span class="header-value">Value</span>
                  </div>
                  <div v-for="(item, index) in sortedMergedNodeData" :key="item.nodeId" label-width="100px">
                    <div 
                      class="property-row bas-car" 
                      :data-node-id="item.nodeId" 
                      :ref="el => setVariablesTableRowRef(el, index)"
                      :class="{
                        'expanded': item._isExpanded,
                        'selected-row': item._isSelected,
                        'child-data-row': item._isChildData
                      }"
                      @dblclick="handleRowDblClick(item, index)"
                    >
                      <div class="property-index property-index-col">
                        <div class="property-name-main">
                          <span class="expand-button" v-if="getVariableType(item) && !item._isChildData">
                              <span v-if="item._isLoading">
                                <el-icon class="is-loading"><Loading /></el-icon>
                              </span>
                              <span v-else-if="!item._isExpanded" @click="getDetialCard(item)">
                              <el-icon><ArrowDown /></el-icon>
                            </span>
                            <span v-else @click="getDetialCard(item)">
                              <el-icon><ArrowUp /></el-icon>
                            </span>
                          </span>
                          <span class="property-name-main-text">{{ item._isChildData ? '' : (() => { const nodeId = getNodeIdString(item); const index = mainDataIndexMap.get(nodeId); return index !== undefined ? index + 1 : ''; })() }}</span>
                        </div>
                      </div>
                      <div class="property-name property-name-col">
                        <div class="property-name-main">
                            <span class="property-name-main-text">{{ getDisplayName(item) || item.name }}</span>
                            <!-- 展开/收起按钮 -->
                              
                           
                        </div>
                      </div>
                      <div class="property-type property-name-col">
                        <div class="property-name-main">{{ item.dataType }}</div>
                      </div>
                      <div class="property-his property-name-col">
                        <div class="property-name-main">{{ item.Historizing ? 'true' : 'false' }}</div>
                      </div>
                      <div class="property-value">
                        <!-- 展示模式：显示当前值 -->
                        <div 
                          v-if="!item._isEditing" 
                          class="value-display"
                        >
                          <!-- 当文本溢出且不可编辑时显示 Tooltip -->
                          <el-tooltip 
                            :disabled="!shouldShowTooltip(item)"
                            :content="formatDisplayValue(item._editValue, item)"
                            placement="top"
                            effect="dark"
                            :show-after="100"
                            popper-class="value-text-tooltip"
                            :teleported="true"
                          >
                            <span 
                              ref="(el) => setValueTextRef(item, el)"
                              class="value-text"
                              :data-item-id="getItemId(item)"
                              @mouseenter="(e) => handleValueTextMouseEnter(item, e)"
                            >{{ formatDisplayValue(item._editValue, item) }}</span>
                          </el-tooltip>
                          <div class="value-actions" v-if="!item._isChildData">
                            <el-icon 
                              v-if="isDataTypeEditable(item)"
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
                            <el-icon 
                              v-else
                              class="access-status-icon" 
                              :style="{ color: getAccessStatus(item.UserWriteMask).color }"
                              :title="getAccessStatus(item.UserWriteMask).text + ' (该数据类型不允许编辑)'"
                            >
                              <span />
                            </el-icon>
                          </div>
                        </div>

                    <!-- Boolean 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Boolean' && item._isEditing"
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                      </div>

                    <!-- DateTime 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'DateTime' && item._isEditing" 
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                    
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                        </div>
                    <!-- Enumeration 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Enumeration' && item._isEditing"
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- Structure 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Structure' && item._isEditing"
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                          </div>
                    <!-- String 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'String' && item._isEditing" 
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                        </div>

                    <!-- UInteger 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'UInteger' && item._isEditing" 
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- Integer 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Integer' && item._isEditing" 
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- Double 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'Double' && item._isEditing"
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- NodeId 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'NodeId' && item._isEditing" 
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                    
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                      </div>
                    <!-- None 类型改为弹窗编辑 -->
                    <div 
                      v-if="item.dataTypes === 'None' && item._isEditing" 
                      class="value-display"
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
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
                      :class="{ 'no-edit': !isDataTypeEditable(item) }"
                      @click="isDataTypeEditable(item) && startEditValue(item)"
                    >
                      <span class="value-text">{{ formatDisplayValue(item._editValue, item) }}</span>
                      <el-icon v-if="isDataTypeEditable(item) && !item._isChildData" class="edit-hint"><Edit /></el-icon>
                      </div>
                    <el-card class="bas-car" v-if="item.specific">
                      <div class="tit-box">
                        <div class="bas-label">{{ getDisplayName(item) || item.name }}</div>
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
                   
                  </div>
                  
                  <!-- 加载更多按钮 -->
                  <div v-if="hasNextVariablesPage && !state.loadingVariables" class="variables-load-more-trigger">
                    <div 
                      class="load-more-trigger"
                      @click="loadNextPageVariables"
                    >
                      ...
                </div>
              </div>
                </div>
              </div>
              <div v-else-if="tab.type === 'References'" class="details-panel">
                <div class="panel-header">
                  <span>节点信息</span>
                </div>
                <div 
                  class="details-content event-view-dropzone"
                  :class="{ 'drag-over': isDragOver }"
                  @dragover.prevent="handleDragOver"
                  @dragleave="handleDragLeave"
                  @drop="handleDrop"
                >
                  <div v-if="state.nodeDetails.length === 0" class="empty-dropzone">
                    <el-icon class="empty-icon"><DocumentAdd /></el-icon>
                    <p>拖拽树节点到这里添加</p>
                  </div>
                  <div v-else class="detail-item single-detail" :key="state.nodeDetails[0].id">
                    <div class="detail-name">
                      <el-icon class="node-icon"><Document /></el-icon>
                      <span>{{ getDisplayName(state.nodeDetails[0]) || state.nodeDetails[0].name || state.nodeDetails[0].label || state.nodeDetails[0].nodeIdNum }}</span>
                    </div>
                    <!-- <div class="detail-children">
                      <div 
                        v-for="(child, childIndex) in state.nodeDetails[0].children" 
                        :key="childIndex" 
                        class="detail-child"
                      >
                        <div class="child-name">{{ getDisplayName(child) || child.name }}</div>
                        <div class="child-value">{{ child.value }}</div>
                      </div>
                    </div> -->
                  </div>
                </div>
                
                <!-- References 引用列表区域 -->
                <div class="references-section">
                  <div class="references-header">
                    <span class="references-title">References</span>
                  </div>
                  <div class="references-toolbar">
                    <div class="toolbar-left">
                      <el-select 
                        v-model="referencesTabHierarchy" 
                        size="small" 
                        style="width: 120px; margin-left: 8px;"
                      >
                        <el-option label="All" value="All" />
                        <el-option label="Hierarchy" value="Hierarchy" />
                        <el-option label="Nonhierarchy" value="Nonhierarchy" />
                      </el-select>
                      <el-select 
                        v-model="referencesTabDirection" 
                        size="small" 
                        style="width: 120px; margin-left: 8px;"
                      >
                        <el-option label="Forward" value="Forward" />
                        <el-option label="Reverse" value="Reverse" />
                        <el-option label="Both" value="Both" />
                      </el-select>
                    </div>
                    <div class="toolbar-right">
                    <el-button 
                      size="small" 
                      type="primary" 
                      plain
                      @click="handleLoadReferencesClick"
                      :loading="loadingReferences"
                    >
                      <el-icon><Search /></el-icon>
                    </el-button>
                    <el-button 
                      size="small" 
                      type="success" 
                      circle
                      @click="showTopologyDialog = true"
                    >
                    <el-icon><TrendCharts /></el-icon>
                    </el-button>
                    </div>
                    
                  </div>
                  
                  <div class="references-table">
                    <div class="table-header">
                      <div class="header-cell header-index">Index</div>
                      <div class="header-cell header-reference">Reference</div>
                      <div class="header-cell header-target">Target DisplayName</div>
                    </div>
                    <div v-if="loadingReferences" class="loading-references">
                      <el-icon class="is-loading"><Loading /></el-icon>
                      <span>加载中...</span>
                    </div>
                    <div v-else-if="!references || references.length === 0" class="empty-references">
                      <el-icon><Document /></el-icon>
                      <span>无引用数据</span>
                    </div>
                    <div v-else ref="tableBodyRef" class="table-body">
                      <div 
                        v-for="(ref, refIndex) in filteredReferencesList" 
                        :key="refIndex" 
                        :ref="el => { if (el) setTableRowRef(el, refIndex) }"
                        class="table-row"
                      >
                        <div class="table-cell cell-index">{{ refIndex + 1 }}</div>
                        <div class="table-cell cell-reference">{{ ref.referenceTypeName || ref.referenceTypeId || 'N/A' }}</div>
                        <div class="table-cell cell-target">{{  getDisplayName(ref) || 'N/A' }}</div>
                      </div>
                    </div>
                    <div v-if="hasNextPage && !loadingReferences" class="table-footer">
                      <div 
                        class="load-more-trigger"
                        @click="loadNextPageReferences"
                      >
                        ...
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 拓扑图弹框 -->
                <el-dialog
                  v-model="showTopologyDialog"
                  title="节点引用拓扑图"
                  width="90vw"
                  :close-on-click-modal="false"
                  :destroy-on-close="false"
                  custom-class="topology-dialog"
                  @opened="initTopologyChart"
                  @closed="destroyTopologyChart"
                >
                  <div class="topology-chart-container">
                    <div ref="topologyChartRef" id="topology-chart-container" class="topology-chart"></div>
                  </div>
                  <template #footer>
                    <span class="dialog-footer">
                      <el-button @click="showTopologyDialog = false">关闭</el-button>
                    </span>
                  </template>
                </el-dialog>
              </div>
              <div v-else-if="tab.type === 'Event History'" class="history-panel">
                <div class="panel-header">
                  <span>历史记录</span>
                </div>
                
                <!-- 拖拽区域 - 树形展示拖拽的节点 -->
                <div class="history-dropzone-container">
                  <div 
                    class="history-dropzone"
                    :class="{ 'drag-over': historyDragOver }"
                    @dragover.prevent="handleHistoryDragOver"
                    @dragleave="handleHistoryDragLeave"
                    @drop="handleHistoryDrop"
                  >
                    <div v-if="!historySelectedNode" class="empty-dropzone">
                      <el-icon class="empty-icon"><DocumentAdd /></el-icon>
                      <p>拖拽树节点到这里添加对象</p>
                    </div>
                    <div v-else class="history-tree-display">
                      <div class="tree-display-header">
                        <el-icon class="node-icon"><Document /></el-icon>
                        <span>{{ getDisplayName(historySelectedNode) || historySelectedNode.label || historySelectedNode.browseName || historySelectedNode.nodeIdNum }}</span>
                        <el-button 
                          type="danger" 
                          size="small" 
                          text 
                          @click="clearHistoryNode"
                          class="remove-node-btn"
                        >
                          <!-- <el-icon><Close /></el-icon> -->
                        </el-button>
                      </div>
                      <!-- <div class="tree-display-content">
                        <el-tree
                          ref="historyDisplayTreeRef"
                          :data="historyTreeData"
                          :props="{
                            children: 'children',
                            label: 'label',
                            hasChildren: 'hasChildren',
                            key: 'nodeIdNum',
                          }"
                          node-key="nodeIdNum"
                          :highlight-current="true"
                          :expand-on-click-node="false"
                          :default-expand-all="false"
                          :check-on-click-node="false"
                          show-checkbox
                          @check="handleHistoryTreeCheck"
                          empty-text="无数据"
                          class="history-display-tree"
                        >
                          <template #default="{ node, data }">
                            <div class="history-tree-node">
                              <i :class="getNodeIcon(data)" class="node-icon"></i>
                              <span class="node-label-text">
                                {{ data.label || getDisplayName(data) || data.browseName || data.nodeIdNum }}
                              </span>
                            </div>
                          </template>
                        </el-tree>
                      </div> -->
                    </div>
                  </div>
                </div>

                <!-- 对象下拉树选择器 -->
                <!-- <div class="history-tree-selector">
                  <el-select
                    v-model="historyTreeSelectValue"
                    placeholder="选择对象"
                    clearable
                    filterable
                    @change="handleHistoryTreeSelectChange"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="option in flattenedTreeOptions"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                    />
                  </el-select>
                </div> -->

                <!-- 时间搜索区域 -->
                <div class="history-time-search">
                  <div class="time-search-row">
                    <label class="time-label">开始时间</label>
                    <el-date-picker
                      v-model="historyStartTime"
                      type="datetime"
                      placeholder="选择开始时间"
                      format="YYYY-MM-DD HH:mm:ss"
                      value-format="YYYY-MM-DD HH:mm:ss"
                      class="time-picker"
                      clearable
                    />
                  </div>
                  <div class="time-search-row">
                    <label class="time-label">结束时间</label>
                    <el-date-picker
                      v-model="historyEndTime"
                      type="datetime"
                      placeholder="选择结束时间"
                      format="YYYY-MM-DD HH:mm:ss"
                      value-format="YYYY-MM-DD HH:mm:ss"
                      class="time-picker"
                      clearable
                    />
                  </div>
                  <div class="time-search-actions">
                    <el-button 
                      type="primary" 
                      @click="handleHistorySearch"
                      :loading="historySearchLoading"
                      :icon="Search"
                    >
                      搜索
                    </el-button>
                  </div>
                </div>

                <!-- Event Types 树 -->
                <div class="history-event-types-section">
                  <div class="section-header">
                    <span class="section-title">Query</span>
                  </div>
                  <el-tabs v-model="historyEventTypeActiveTab" type="card" class="history-event-type-tabs">
                    <el-tab-pane label="Select" name="select">
                      <el-tree
                        ref="historyEventTypesTreeRef"
                        :data="historyEventTypesTreeData"
                        :props="{
                          children: 'children',
                          label: 'label',
                          disabled: 'disabled',
                          disableCheckbox: 'disableCheckbox'
                        }"
                        node-key="nodeId"
                        :expand-on-click-node="false"
                        :default-expand-all="true"
                        show-checkbox
                        check-strictly
                        :default-checked-keys="historyEventTypeDefaultCheckedKeys"
                        @node-dblclick="handleEventTypeNodeDblClick"
                        @check="handleEventTypeTreeCheck"
                        class="history-event-types-tree"
                      >
                        <template #default="{ node, data }">
                          <div class="event-type-tree-node">
                            <span class="node-label-text">
                              {{ data.label }}
                            </span>
                          </div>
                        </template>
                      </el-tree>
                    </el-tab-pane>
                    <el-tab-pane label="Where" name="where">
                      <div class="where-tab-content">
                        <div class="where-header">
                          <el-select v-model="whereMatchMode" size="small" class="where-match-select">
                            <el-option label="满足全部" value="all" />
                            <el-option label="满足任一" value="any" />
                          </el-select>
                          <div class="where-header-actions">
                            <el-button size="small" circle :icon="Plus" @click="addWhereRow" />
                            <el-button size="small" circle :icon="Minus" :disabled="whereRows.length === 0" @click="removeLastWhereRow" />
                          </div>
                        </div>

                        <div class="where-table-wrap">
                          <div class="where-table">
                            <div class="where-table-head">
                              <div class="where-th where-th-name">名称</div>
                              <div class="where-th where-th-op">操作符</div>
                              <div class="where-th where-th-value">值</div>
                            </div>
                            <div v-if="whereRows.length === 0" class="where-empty-tip">
                              暂无条件，请点击右上角“+”添加
                            </div>
                            <div v-else class="where-table-body">
                              <div
                                v-for="(row, idx) in whereRows"
                                :key="row.id"
                                class="where-tr"
                                :class="{ 'is-active': whereActiveRowIndex === idx }"
                                @click="whereActiveRowIndex = idx"
                              >
                                <div class="where-td where-td-name">
                                  <el-select
                                    v-if="!row.displayName"
                                    v-model="row.fieldNodeId"
                                    size="small"
                                    placeholder="选择字段"
                                    filterable
                                    @change="handleWhereRowFieldChange(idx)"
                                  >
                                    <el-option
                                      v-for="field in whereAvailableFields"
                                      :key="field.nodeId"
                                      :label="field.displayName"
                                      :value="field.nodeId"
                                    />
                                  </el-select>
                                  <span v-else class="where-field-name-display">{{ row.displayName }}</span>
                                </div>
                                <div class="where-td where-td-op">
                                  <el-select v-model="row.op" size="small" :disabled="!row.fieldNodeId">
                                    <el-option
                                      v-for="op in getWhereOpsForRow(row)"
                                      :key="op.value"
                                      :label="op.label"
                                      :value="op.value"
                                    />
                                  </el-select>
                                </div>
                                <div class="where-td where-td-value">
                                  <el-checkbox v-if="shouldShowCheckboxForRow(row)" v-model="row.value" />
                                  <el-date-picker
                                    v-else-if="shouldShowDatePickerForRow(row) === 'datetime'"
                                    v-model="row.value"
                                    type="datetime"
                                    size="small"
                                    placeholder="选择日期时间"
                                    :disabled="!row.fieldNodeId"
                                    format="YYYY-MM-DD HH:mm:ss"
                                    value-format="YYYY-MM-DDTHH:mm:ss"
                                  />
                                  <el-date-picker
                                    v-else-if="shouldShowDatePickerForRow(row) === 'date'"
                                    v-model="row.value"
                                    type="date"
                                    size="small"
                                    placeholder="选择日期"
                                    :disabled="!row.fieldNodeId"
                                    format="YYYY-MM-DD"
                                    value-format="YYYY-MM-DD"
                                  />
                                  <el-time-picker
                                    v-else-if="shouldShowDatePickerForRow(row) === 'time'"
                                    v-model="row.value"
                                    size="small"
                                    placeholder="选择时间"
                                    :disabled="!row.fieldNodeId"
                                    format="HH:mm:ss"
                                    value-format="HH:mm:ss"
                                  />
                                  <el-input
                                    v-else
                                    v-model="row.value"
                                    size="small"
                                    :disabled="!row.fieldNodeId"
                                    placeholder="请输入"
                                  />
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div class="where-footer">
                          <el-button size="small" @click="clearWhereRows" :disabled="whereRows.length === 0">清空</el-button>
                          <el-button type="primary" size="small" @click="confirmWhere">确定</el-button>
                        </div>
                      </div>
                    </el-tab-pane>
                  </el-tabs>
                </div>

                <!-- Results 列表 -->
                <div class="history-results-section">
                  <div class="section-header">
                    <span class="section-title">Results</span>
                    <span class="section-count" v-if="historyResults.length > 0">({{ historyResults.length }})</span>
                  </div>
                  <div class="history-results-table-wrapper">
                    <div class="history-results-table">
                      <div class="table-header">
                        <div class="header-cell header-index">Index</div>
                        <!-- <div class="header-cell header-eventid">EventId</div> -->
                        <div class="header-cell header-time">Time</div>
                        <div class="header-cell header-message">Message</div>
                      </div>
                      <div v-if="historySearchLoading" class="loading-results">
                        <el-icon class="is-loading"><Loading /></el-icon>
                        <span>加载中...</span>
                      </div>
                      <div v-else-if="!historyResults || historyResults.length === 0" class="empty-results">
                        <el-icon><Document /></el-icon>
                        <span>暂无结果</span>
                      </div>
                      <div v-else class="table-body">
                        <div 
                          v-for="(result, resultIndex) in historyResults" 
                          :key="resultIndex" 
                          class="table-row"
                          :class="{ 'selected-row': selectedResultIndex === resultIndex }"
                          @click="selectResult(resultIndex)"
                        >
                          <div class="table-cell cell-index">{{ resultIndex + 1 }}</div>
                          <!-- <div class="table-cell cell-eventid" :title="result.EventId || ''">{{ result.EventId || 'N/A' }}</div> -->
                          <div class="table-cell cell-time">{{ result.Time ? formatDateTimeForPicker(result.Time) : 'N/A' }}</div>
                          <div class="table-cell cell-message" :title="result.Message || ''">{{ result.Message || 'N/A' }}</div>
                        </div>
                      </div>
                      <div v-if="hasNextPageHistory && !historySearchLoading" class="table-footer">
                        <div 
                          class="load-more-trigger"
                          @click="loadNextPageHistory"
                        >
                          ...
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Details 列表 -->
                <div class="history-details-section">
                  <div class="section-header">
                    <span class="section-title">Details</span>
                  </div>
                  <div class="history-details-list">
                    <div v-if="!historyDetails || Object.keys(historyDetails).length === 0" class="empty-list">
                      <el-icon><Document /></el-icon>
                      <span>暂无详情，请先选择结果项</span>
                    </div>
                    <div v-else class="details-table-container">
                      <table class="details-table">
                        <thead>
                          <tr>
                            <th class="details-th-name">Name</th>
                            <th class="details-th-value">Value</th>
                          </tr>
                        </thead>
                        <tbody>
                          <template v-for="(detail, key) in historyDetails" :key="key">
                            <tr 
                              class="details-row"
                              :class="{ 'details-row-selected': selectedHistoryDetailKey === key }"
                              @click="selectedHistoryDetailKey = key"
                            >
                              <td class="details-td-name">
                                <span v-if="isHistoryDetailExpandable(detail)" class="expand-icon-wrapper" @click.stop="toggleHistoryDetail(key)">
                                  <el-icon class="expand-icon" :class="{ 'expanded': expandedHistoryDetails.includes(key) }">
                                    <ArrowDown v-if="expandedHistoryDetails.includes(key)" />
                                    <ArrowRight v-else />
                                  </el-icon>
                                </span>
                                <span>{{ key }}</span>
                              </td>
                              <td class="details-td-value">
                                <span v-if="!isHistoryDetailExpandable(detail)">{{ formatHistoryDetailValue(detail) }}</span>
                                <span v-else>{{ detail.type || 'Object' }}</span>
                              </td>
                            </tr>
                            <!-- 展开的嵌套内容 -->
                            <tr 
                              v-if="isHistoryDetailExpandable(detail) && expandedHistoryDetails.includes(key)"
                              class="details-nested-row"
                            >
                              <td colspan="2" class="details-nested-cell">
                                <table class="details-nested-table">
                                  <tr 
                                    v-for="(nestedValue, nestedKey) in detail.children"
                                    :key="nestedKey"
                                    class="details-nested-item"
                                  >
                                    <td class="details-nested-name">{{ nestedKey }}</td>
                                    <td class="details-nested-value">{{ formatHistoryDetailValue(nestedValue) }}</td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </template>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else-if="tab.type === 'History Trend'" class="history-trend-panel">
                <div class="panel-header">
                  <span>{{ tab.value }}</span>
                </div>
                <div class="history-trend-toolbar">
                  <div class="history-trend-node-info">
                    <span class="history-trend-node-name">{{ historyTrendNode?.name || '未选择变量' }}</span>
                    <span class="history-trend-node-meta" v-if="historyTrendNode?.dataType">
                      {{ historyTrendNode.dataType }}
                    </span>
                  </div>
                  <div class="history-trend-controls">
                    <div class="history-trend-control-item">
                      <span class="history-trend-control-label">开始时间</span>
                      <el-date-picker
                        v-model="historyTrendStartTime"
                        type="datetime"
                        placeholder="选择开始时间"
                        format="YYYY-MM-DD HH:mm:ss"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        class="history-trend-time-picker"
                        clearable
                      />
                    </div>
                    <div class="history-trend-control-item">
                      <span class="history-trend-control-label">结束时间</span>
                      <el-date-picker
                        v-model="historyTrendEndTime"
                        type="datetime"
                        placeholder="选择结束时间"
                        format="YYYY-MM-DD HH:mm:ss"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        class="history-trend-time-picker"
                        clearable
                      />
                    </div>
                    <el-button
                      type="primary"
                      :icon="Refresh"
                      :loading="historyTrendLoading"
                      @click="loadHistoryTrendData"
                    >
                      刷新
                    </el-button>
                  </div>
                </div>
                <div class="history-trend-chart-wrapper">
                  <div v-if="historyTrendLoading" class="history-trend-empty">
                    <el-icon class="is-loading"><Loading /></el-icon>
                    <span>正在加载历史趋势...</span>
                  </div>
                  <div v-else-if="historyTrendEmptyText" class="history-trend-empty">
                    <el-icon><TrendCharts /></el-icon>
                    <span>{{ historyTrendEmptyText }}</span>
                  </div>
                  <div
                    v-show="!historyTrendLoading && !historyTrendEmptyText"
                    ref="historyTrendChartRef"
                    class="history-trend-chart"
                  ></div>
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
          
          <!-- AI助手标签页 -->
          <!-- <el-tab-pane label="AI助手" name="ai-assistant" class="adaptive-tab-pane">
            <div v-if="activeFolder === 'ai-assistant'" class="ai-panel-container">
              <AIPanel 
                :current-node="getCurrentNodeInfo()"
                :current-config="getCurrentConfig()"
                :tree-data="state.bottomTreeData"
              />
            </div>
          </el-tab-pane> -->
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
                    v-else-if="key === 'browseName'"
                    @command="handleCopyCommand"
                    trigger="contextmenu"
                    placement="bottom-start">
                    <p class="text-ellipsis">{{ value?.name || value }}</p>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item :command="{ text: value?.name || value, key: key }">
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
        <el-form ref="urlRef" :model="urlForm" :rules="rules" label-width="100px" @keyup.enter="handleDialogConfirm">
          <el-form-item label="名称" required prop="urlName">
            <el-input  v-model="urlForm.urlName"   placeholder="请输入名称" @keyup.enter="handleDialogConfirm" />
          </el-form-item>
          <el-form-item label="URL地址" required  prop="url">
            <el-input v-model="urlForm.url" placeholder="请输入URL地址" @keyup.enter="handleDialogConfirm" />
          </el-form-item>
          
        </el-form>
      </div>

      <!-- Connection Form -->
      <div v-else>
        <el-form ref="urlRef" :model="connectionForm" label-width="100px" @keyup.enter="handleDialogConfirm">
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
          <el-button type="primary"   @click="handleDialogConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="FolderDialogVisible" :title="'Add Document'" width="500px"
      destroy-on-close class="el-dialog-self">
      <div>
        <el-form :model="documentForm" label-width="120px">
          <el-form-item label="Document Type:">
            <el-select v-model="documentForm.documentType" placeholder="请选择文档类型" style="width: 100%">
              <el-option label="Event View" value="Event View" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="Description:">
            <el-card class="box-card" style="margin-top: 0;">
              <div class="text item">
                This document type can be used to subscribe to events and alarms of the UA server. It supports the selection of event fields and can acknowledge and confirm alarms. It also supports reading of historical event data for a given time period.
              </div>
            </el-card>
          </el-form-item>
          
          <el-form-item label="License Type:">
            <div style="display: flex; align-items: center; gap: 8px;">
              <span style="color: #67c23a; font-weight: 500;">Runtime License</span>
            </div>
            <div style="margin-top: 8px; color: #606266; font-size: 14px;">
              There are no functional limitations.
            </div>
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="FolderDialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleAddDocumentConfirm">Add</el-button>
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

    <el-dialog
      v-model="discoveryConfigDialogVisible"
      title="Discovery Config"
      width="520px"
      destroy-on-close
      class="el-dialog-self discovery-config-dialog"
    >
      <div class="discovery-input-section">
        <el-input
          v-model="discoveryAddressInput"
          placeholder="例如: http://localhost:8080"
          @keyup.enter="handleDiscoveryAddressAdd"
        >
          <template #append>
            <el-button :icon="Plus" @click="handleDiscoveryAddressAdd" />
          </template>
        </el-input>
      </div>

      <div class="discovery-list-header">
        <span class="discovery-list-title">地址列表</span>
        <el-tag size="small" type="info" round>{{ discoveryConfigForm.addresses.length }} 条</el-tag>
      </div>

      <div v-if="discoveryConfigForm.addresses.length" class="discovery-address-list">
        <transition-group name="discovery-list">
          <div
            v-for="(item, index) in discoveryConfigForm.addresses"
            :key="item"
            class="discovery-address-item"
          >
            <div class="discovery-address-info">
              <span class="discovery-address-index">{{ index + 1 }}</span>
              <el-icon class="discovery-address-icon"><Link /></el-icon>
              <span class="discovery-address-text" :title="item">{{ item }}</span>
            </div>
            <el-button
              :icon="Delete"
              circle
              size="small"
              type="danger"
              plain
              @click="handleDiscoveryAddressRemove(item)"
            />
          </div>
        </transition-group>
      </div>
      <el-empty v-else description="暂无 Discovery 地址" :image-size="60" />

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="discoveryConfigDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleDiscoveryConfigSave">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
      v-model="discoveryFindDialogVisible"
      title="Discovery"
      width="800px"
      destroy-on-close
      class="el-dialog-self discovery-find-dialog"
    >
      <div class="discovery-input-section" style="margin-bottom: 12px;">
        <el-input
          v-model="discoveryFindUrlInput"
          placeholder="请输入 Discovery 地址，例如: http://localhost:8080"
          @keyup.enter="handleDiscoveryFindSearch"
        >
          <template #append>
            <el-button
              class="discovery-search-btn"
              type="primary"
              :icon="Search"
              @click="handleDiscoveryFindSearch"
            >
              搜索
            </el-button>
          </template>
        </el-input>
      </div>
      <div class="discovery-table-wrap" :class="{ 'is-loading': discoveryLoading }">
        <div v-if="discoveryLoading" class="discovery-table-loading">
          <el-icon class="discovery-table-loading-icon"><Loading /></el-icon>
          <span>搜索中...</span>
        </div>
        <el-table
          :data="discoveryServers"
          border
          height="320"
          highlight-current-row
          :current-row-key="selectedDiscoveryServerId"
          row-key="id"
          @row-click="(row) => selectedDiscoveryServerId = row.id"
          style="cursor: pointer;"
        >
          <el-table-column label="" width="60" align="center">
            <template #default="{ row }">
              <el-radio v-model="selectedDiscoveryServerId" :label="row.id">&nbsp;</el-radio>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="Name" min-width="160" />
          <el-table-column prop="url" label="URL" min-width="220" />
        </el-table>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="discoveryFindDialogVisible = false">取消</el-button>
          <el-button type="primary"   @click="handleDiscoveryServerAdd">添加到 Server</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="showBooleanFlag" title="Edit Boolean Values" width="500px"
      destroy-on-close class="el-dialog-self">
      
      <div class="boolean-checkbox-container">
        <div v-for="(value, index) in showBooleanData" :key="index" class="checkbox-item">
          <el-checkbox 
            v-model="showBooleanData[index]" 
            @change="handleBooleanChange(index, $event)"
          />
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
             
             <!-- DateTime 类型使用日期时间选择器 -->
             <el-date-picker
              v-if="isDateTimeType()"
              v-model="showDoubleData[index]"
              type="datetime"
              placeholder="请选择日期时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
              @change="(value) => handleDoubleChange(index, value)"
            />
            <!-- 枚举类型使用下拉选择器 -->
            <el-select
              v-else-if="isEnumerationType()"
              v-model="showDoubleData[index]"
              placeholder="请选择枚举值"
              style="width: 100%"
              @change="(value) => handleDoubleChange(index, value)"
            >
              <el-option
                v-for="option in getCurrentEnumerationOptions()"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
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
              <span class="name-label">{{ getDisplayName(item) || item.name }}</span>
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
            <div class="uint64-name">{{ getDisplayName(item) || item.name }}</div>
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

    <el-dialog v-model="showByteFlag" title="Edit Byte Values" width="500px"
      destroy-on-close class="el-dialog-self">
      
      <div class="byte-input-container">
        <div v-for="(value, index) in showByteData" :key="index" class="input-item">
          <el-form-item :label="`Value ${index + 1}`">
            <el-input
              v-model="showByteData[index]"
              placeholder="请输入字节值 (0-255)"
              type="number"
              min="0"
              max="255"
              @input="(value) => handleByteChange(index, value)"
              style="width: 100%"
            ></el-input>
          </el-form-item>
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
              <!-- <div class="header-description">Description</div> -->
            </div>
            <div v-for="(arg, index) in methodInputArgs" :key="'input-' + index" class="table-row">
              <div class="row-name">{{ getDisplayName(arg) || arg.name }}</div>
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
              <!-- <div class="row-description">{{ arg.description || '' }}</div> -->
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
              <!-- <div class="header-description">Description</div> -->
            </div>
            <div v-for="(arg, index) in methodOutputArgs" :key="'output-' + index" class="table-row">
              <div class="row-name">{{ getDisplayName(arg) || arg.name }}</div>
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
                <!-- <el-input 
                  v-else
                  v-model="arg.value" 
                  :placeholder="'Enter ' + arg.dataType"
                  readonly>
                </el-input> -->
                <div class="row-value-display">
                  {{ arg.value }}
                </div>
              </div>
              <div class="row-datatype">{{ arg.dataType }}</div>
              <!-- <div class="row-description">{{ arg.description || '' }}</div> -->
            </div>
          </div>
        </div>

        <!-- Result -->
        <div class="method-section">
          <div class="section-header">
            Result : {{ methodResult }}
          </div>
          <!-- <div class="method-result">
            <el-input 
              v-model="methodResult" 
              type="textarea" 
              :rows="4" 
              placeholder=""
              readonly>
            </el-input>
          </div> -->
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
                {{ getDisplayName(option) || option.name }}
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
  <!-- <div class="console-button-container" v-show="!drawer" @click="drawer = true" >
    <el-icon><Setting /></el-icon>
  </div> -->
  <el-drawer
    v-model="drawer"
    title="调试控制台"
    direction="btt"
    size="60%"
    :with-header="true">
    <div style="height: 100%; width: 100%;">
      <ConsolePanel ref="consoleRef" />
    </div>
  </el-drawer>
  <!-- <ProcessDiagram /> -->
</template>

<script setup>
import './index.css';
import { getBrowseData, getBrowseNextData, getOpcuaData } from '@/api/index.js';
import { onMounted, onUnmounted, reactive, ref,computed, watch,nextTick, unref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {ElDrawer, ElMessageBox, ElDialog, ElButton, ElMessage, stepProps } from 'element-plus';
import { Configuration, NodeClass, BrowseDirection } from "opcua-webapi";
import { Document, CircleCheck, ArrowDown, ArrowRight, Edit, Check, Close, DocumentCopy, Loading, Lock, EditPen, QuestionFilled, DocumentAdd, Delete, Plus, Minus, Share, Search, Refresh, Link, TrendCharts } from '@element-plus/icons-vue';
import moment from 'moment';
import {
  UaWebClient,
  UaDataTypeDictionary,
  UaReferenceTypeDictionary,
  UaObjectTypeDictionary,
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
  ReferenceTypeIds,
  parseUaNodeId,
  VariableTypeIds ,
  getGeneratedEventType,
  historyReadEvent,
  parseUaExpandedNodeId,
  UaWriteValue,
  UaDefintionNode,
  UaReferenceType,
  objectType,
  UaQuery,
  UaQueryFilter,
  UaQueryFilterType
}
// from "@/utils/newopc/index.ts"
from 'opcua-webapi-ts'


import { usePageStore } from '@/stores/pageStore';
import { useThrottleFn,withTimeout }  from "@/utils/utils.ts"
import ConsolePanel from './components/ConsolePanel.vue'
import ProcessDiagram from './components/ProcessDiagram2.vue'
import AIPanel from './components/AIPanel.vue'
import eruda from 'eruda';
import * as echarts from 'echarts';
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
const DISCOVERY_NODE_ID = 'discovery-root';
const DISCOVERY_STORAGE_KEY = 'settingPage.discovery.config';
const DISCOVERY_SEARCH_HISTORY_KEY = 'settingPage.discovery.searchHistory';
const fixedNodeId = ref('85');
const dbSelectRowId = ref('');
const drawer = ref(false) 
const consoleRef = ref(null)
const addDialogVisible = ref(false);
const addDropdownRefs = ref(new Map()); // 使用 Map 存储多个 dropdown ref
const selectNodeData = ref(null);
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
import { useDetailCardStore } from './stores/detailCardStore';
const urlDatas = urlData();
const detailCardStore = useDetailCardStore();

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

// 存储每个 item 的文本溢出状态（使用响应式对象）
const textOverflowMap = reactive({});

// 存储元素引用，用于重新检测溢出
const elementRefMap = new WeakMap();

// 使用 WeakMap 存储 item 对象和对应的唯一 ID，避免循环引用问题
const itemIdMap = new WeakMap();
let itemIdCounter = 0;

// 安全地获取 item 的唯一标识符，避免循环引用
const getItemId = (item) => {
  if (!item) return `item-${Date.now()}-${Math.random()}`;
  
  // 如果 WeakMap 中已有，直接返回
  if (itemIdMap.has(item)) {
    return itemIdMap.get(item);
  }
  
  // 尝试使用常见的唯一标识属性
  if (item.nodeIdNum) {
    const id = String(item.nodeIdNum);
    itemIdMap.set(item, id);
    return id;
  }
  
  if (item.NodeId) {
    const id = String(item.NodeId);
    itemIdMap.set(item, id);
    return id;
  }
  
  if (item.id) {
    const id = String(item.id);
    itemIdMap.set(item, id);
    return id;
  }
  
  // 尝试使用简单属性的组合
  const parts = [];
  if (item.browseName) parts.push(`browseName:${item.browseName}`);
  if (item.displayName) parts.push(`displayName:${typeof item.displayName === 'string' ? item.displayName : item.displayName?.text || item.displayName?.Text || ''}`);
  if (item.name) parts.push(`name:${item.name}`);
  if (item.dataType) parts.push(`dataType:${item.dataType}`);
  
  // 如果有一些简单属性，使用它们组合
  if (parts.length > 0) {
    const id = parts.join('|');
    itemIdMap.set(item, id);
    return id;
  }
  
  // 最后的备选方案：使用计数器生成唯一 ID
  const id = `item-${itemIdCounter++}`;
  itemIdMap.set(item, id);
  return id;
};

// 检查元素文本是否溢出（改进的方法，适用于 line-clamp）
const checkTextOverflow = (el, itemId) => {
  if (!el) return false;
  
  try {
    // 检查 scrollHeight 和 clientHeight（适用于 line-clamp）
    const scrollHeight = el.scrollHeight;
    const clientHeight = el.clientHeight;
    
    // 对于 line-clamp，当内容溢出时，scrollHeight 会大于 clientHeight
    // 添加 1px 的容差以避免浮点数精度问题
    const isOverflow = scrollHeight > clientHeight + 1;
    
    // 更新响应式状态
    const oldValue = textOverflowMap[itemId];
    textOverflowMap[itemId] = isOverflow;
    
    // 如果值发生变化，强制触发更新
    if (oldValue !== isOverflow) {
      // 强制触发响应式更新
      nextTick(() => {
        // 通过重新赋值触发响应式
        textOverflowMap[itemId] = isOverflow;
      });
    }
    
    return isOverflow;
  } catch (error) {
    console.warn('检查文本溢出时出错:', error);
    return false;
  }
};

// 设置文本元素的 ref 回调
const setValueTextRef = (item, el) => {
  if (!item) return;
  
  const itemId = getItemId(item);
  
  if (el) {
    // 存储元素引用
    elementRefMap.set(item, el);
    
    // 立即检查一次
    checkTextOverflow(el, itemId);
    
    // 使用多个 nextTick 确保 DOM 完全渲染后再检查
    nextTick(() => {
      checkTextOverflow(el, itemId);
      
      // 延迟检查，确保样式已应用
      setTimeout(() => {
        checkTextOverflow(el, itemId);
        
        // 再次检查，确保响应式更新
        nextTick(() => {
          checkTextOverflow(el, itemId);
        });
      }, 100);
    });
  } else {
    // 元素被移除，清除引用和溢出状态
    elementRefMap.delete(item);
    delete textOverflowMap[itemId];
  }
};

// 处理文本元素鼠标悬停事件，实时检测溢出
const handleValueTextMouseEnter = (item, event) => {
  if (!item || !event) return;
  
  const itemId = getItemId(item);
  const el = event.target;
  
  if (el) {
    // 实时检查溢出状态
    checkTextOverflow(el, itemId);
  }
};

// 检查 item 是否应该显示 Tooltip（文本溢出且不可编辑）
const shouldShowTooltip = (item) => {
  if (!item) return false;
  
  const itemId = getItemId(item);
  let isOverflow = textOverflowMap[itemId] || false;
  const isEditable = isDataTypeEditable(item);
  
  // 总是尝试从存储的元素重新检测，确保状态最新
  if (elementRefMap.has(item)) {
    const el = elementRefMap.get(item);
    if (el) {
      const recheckResult = checkTextOverflow(el, itemId);
      isOverflow = recheckResult || isOverflow;
    }
  }
  
  // 只有同时满足：文本溢出 且 不可编辑 时才显示 Tooltip
  const shouldShow = isOverflow && !isEditable;
  
  return shouldShow;
};

  
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
  allReferenceTypesData:[],
  allObjectTypesData:[],
  allTypesData:[],
  allCardData:[],
  nodeDetailsArr:[],
  detailsArr:[],
  nodeDetailsData:[
    {key:'1',value:'Variables',type:'Variables'},
    {key:'2',value:'References',type:'References'},
    {key:'3',value:'Event History',type:'Event History'}
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
      DisplayName: { Text: 'Servers' },
      NodeClass: 1,
      TypeDefinition: 'i=61',
      children: [],
    },
    {
      ReferenceTypeId: 'i=38',
      IsForward: true,
      NodeId: DISCOVERY_NODE_ID,
      BrowseName: 'Discovery',
      DisplayName: { Text: 'Discovery' },
      NodeClass: 1,
      TypeDefinition: 'i=61',
    },
  ],
  messageListBottom: [],
  mergedNodeData: [], // 存储合并后的数组
  loadingVariables: false, // Variables 界面加载状态
  nodeDetails: [], // Event View 中显示的节点列表
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
const isValidConnectionUrl = (value) => {
  const urlRule = rules.url?.find((item) => item.pattern);
  if (!urlRule?.pattern) return true;
  return urlRule.pattern.test(String(value || '').trim());
};
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

// AI相关方法
const getCurrentNodeInfo = () => {
  // 获取当前选中的节点信息
  const selectedNode = state.mergedNodeData.find(item => item._isSelected);
  if (selectedNode) {
    return {
      nodeId: selectedNode.nodeId,
      browseName: selectedNode.name,
      nodeClass: selectedNode.nodeClass,
      dataType: selectedNode.dataType,
      value: selectedNode._editValue,
      description: selectedNode.description
    };
  }
  
  // 如果没有选中的节点，返回默认信息
  return {
    nodeId: selectedNodeId.value,
    browseName: '未选择节点',
    nodeClass: 'Unknown',
    dataType: 'Unknown',
    value: null,
    description: '请先选择一个节点'
  };
};

const getCurrentConfig = () => {
  // 获取当前配置信息
  return {
    serverUrl: urlDatas.url || 'opc.tcp://localhost:4840',
    securityMode: 'None',
    securityPolicy: 'None',
    sessionTimeout: 60000,
    connectionTimeout: 10000,
    selectedNodeId: selectedNodeId.value,
    treeDataCount: state.bottomTreeData.length,
    mergedDataCount: state.mergedNodeData.length
  };
};

const getNodeIcon = (data) => {
  const nodeClass = data.nodeClass;
  const objectNodeClass = data.objectNodeClass;
  const objectNodeClassDisplayName = data.objectNodeClassDisplayName;
  const nodeIdStr = data.nodeIdNum || data.nodeId?._nodeId?.toString?.() || data.nodeId?.toString?.() || '';
  // console.log(objectNodeClassDisplayName,'objectNodeClassDisplayName')
  const baseClass = 'node-icon';

  // 图标优先使用更稳定的原始类型线索，避免后续逻辑把 displayName 推成更泛的类型名后影响原有图标。
  if (objectNodeClass == 'ns=1;i=14') return `${baseClass} icon-link`;
  if (nodeClass == 1 && objectNodeClassDisplayName == 'ReferenceElementType' && nodeIdStr !== 'ns=1;i=14') {
    return `${baseClass} icon-link`;
  }
  
  // 优先检查 objectNodeClassDisplayName（最高优先级）
  if (nodeClass ==1 &&   objectNodeClassDisplayName == 'DigitalTwinType') return `${baseClass} icon-digitalTwinType`;
  if (nodeClass ==1 &&   objectNodeClassDisplayName == 'DigitalTwinRepositoryType') return `${baseClass} icon-digitalTwinRepository`;
  if (nodeClass ==1 &&   objectNodeClassDisplayName == 'ElementCollectionType') return `${baseClass} icon-elementCollectionType`;
  if (nodeClass ==1 &&   objectNodeClassDisplayName == 'ElementListType') return `${baseClass} icon-elementListType`;
  if (nodeClass ==1 &&   objectNodeClassDisplayName == 'SubmodelType') return `${baseClass} icon-submodelType`;
  if (nodeClass ==1 &&   objectNodeClassDisplayName == 'SubmodelType') return `${baseClass} icon-elementListType`;
  if (nodeClass ==1 &&   objectNodeClassDisplayName == 'EventElementType') return `${baseClass} icon-eventElementType`;
  if (nodeClass ==1 &&   objectNodeClassDisplayName == 'ReferenceElementType') return `${baseClass} icon-referenceElementType`;
  
  // 最后根据 nodeClass 判断（最低优先级）
  if (nodeClass == 1 && objectNodeClass !== 'ns=1;i=14') return `${baseClass} icon-object`;
  if (nodeClass == 2) return `${baseClass} icon-variable`;
  if ([8,16,32,64].includes(nodeClass)) return `${baseClass} icon-type`;
  if (nodeClass == 128) return `${baseClass} icon-group`;
  if (nodeClass == 4) return `${baseClass} icon-play`;
   
  return baseClass;
};
const showAddDropdown = (event, nodeId) => {
  event.preventDefault();
  event.stopPropagation();

  const dropdownRef = addDropdownRefs.value.get(nodeId);
  if (dropdownRef) {
    dropdownRef.handleOpen();
  }
};

// 设置 dropdown ref 的函数
const setAddDropdownRef = (nodeId, el) => {
  if (el) {
    addDropdownRefs.value.set(nodeId, el);
  } else {
    addDropdownRefs.value.delete(nodeId);
  }
};

const handleAddCommand = (command, node, data) => {
  
  if (command === 'add') {
    currentNode.value = { node, data };
    
    if (node.level === 1) {
      // 获取节点数据，在 Element Plus Tree 中，data 就是节点数据对象
      const browseName = data?.BrowseName || node?.data?.BrowseName;
      
      if (browseName === 'Servers') {
        // Servers 节点：显示 URL 添加对话框
        isEditingConnection.value = true;
        urlForm.value = { url: '', urlName: '' };
        urlDialogVisible.value = true;
      } else if (browseName === 'FolderType') {
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

const handleAddConfirm = useThrottleFn(async () => {
  if (!addFormRef.value) return;
  
  try {
    await addFormRef.value.validate();
    
    // 记录操作日志
    if (consoleRef.value) {
      consoleRef.value.addLog('info', '添加新项目', {
        type: addForm.value.type,
        name: addForm.value.name,
        description: addForm.value.description,
        timestamp: Date.now()
      })
    }
    
    ElMessage.success('添加成功！');
    addDialogVisible.value = false;
    
    addForm.value = {
      type: '',
      name: '',
      description: ''
    };
  } catch (error) {
    // 记录错误日志
    if (consoleRef.value) {
      consoleRef.value.addLog('error', '添加项目失败:', error)
    }
  }
}, 1000);

const getDialogTitle = () => {
  if (!currentEditingItem.value) return 'Edit Values';
  
  const item = currentEditingItem.value;
  const dataType = item.dataType || '';
  const dataTypes = item.dataTypes || '';
  
  // 检查是否为枚举类型（包括 Enumeration 及其子类型）
  const specificEnumTypes = ['HeaterStatus', 'Priority', 'ServerState'];
  const isSpecificEnumType = specificEnumTypes.includes(dataType) || specificEnumTypes.includes(dataTypes);
  let isEnumerationSubType = false;
  
  if (isSpecificEnumType && item.dataTypesObj) {
    try {
      const enumerationNodeId = new UaNodeId(DataTypeIds.Enumeration);
      if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
        if (item.dataTypesObj.isSubtypeOf(enumerationNodeId)) {
          isEnumerationSubType = true;
        }
      }
    } catch (error) {
      console.warn('检查 Enumeration 子类型时出错:', error);
      isEnumerationSubType = true;
    }
  }
  
  const isEnumerationType = dataTypes === 'Enumeration' || dataType === 'Enumeration' || isEnumerationSubType || isSpecificEnumType;
  
  switch (dataType) {
    case 'DateTime':
    case 'UtcTime':
    case 'UTC':
    case 'UTCTime':
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
    case 'HeaterStatus':
    case 'Priority':
    case 'ServerState':
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
  
  const dataType = currentEditingItem.value.dataType || currentEditingItem.value.dataTypes;
  // UtcTime 类型也应该使用日期时间选择器
  return dataType === 'DateTime' || dataType === 'UtcTime' || dataType === 'UTC' || dataType === 'UTCTime';
};

const isStringType = () => {
  if (!currentEditingItem.value) return false;
  
  const dataType = currentEditingItem.value.dataTypes || currentEditingItem.value.dataType;
  return dataType === 'String';
};

// 判断是否为枚举类型（包括 Enumeration 及其子类型）
const isEnumerationType = () => {
  if (!currentEditingItem.value) return false;
  
  const item = currentEditingItem.value;
  const dataType = item.dataType || '';
  const dataTypes = item.dataTypes || '';
  
  // 直接是 Enumeration 类型
  if (dataTypes === 'Enumeration' || dataType === 'Enumeration') {
    return true;
  }
  
  // 检查是否为特定的枚举类型（HeaterStatus、Priority、ServerState）
  const specificEnumTypes = ['HeaterStatus', 'Priority', 'ServerState'];
  if (specificEnumTypes.includes(dataType) || specificEnumTypes.includes(dataTypes)) {
    // 使用 isSubtypeOf 检查是否为 Enumeration 的子类型
    if (item.dataTypesObj) {
      try {
        const enumerationNodeId = new UaNodeId(DataTypeIds.Enumeration);
        if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
          if (item.dataTypesObj.isSubtypeOf(enumerationNodeId)) {
            return true;
          }
        }
      } catch (error) {
        console.warn('检查 Enumeration 子类型时出错:', error);
        // 如果检查失败，假设这些特定类型是枚举类型
        return true;
      }
    }
    // 如果 dataTypesObj 不可用，假设这些特定类型是枚举类型
    return true;
  }
  
  return false;
};

// 获取当前编辑项的枚举选项列表（用于弹窗编辑）
const getCurrentEnumerationOptions = () => {
  if (!currentEditingItem.value || !currentEditingItem.value.dataTypesObj?.enumValues) {
    return [];
  }
  
  const enumValues = currentEditingItem.value.dataTypesObj.enumValues;
  const options = [];
  
  // enumValues 是 Map 结构，格式为 [number, UaLocalizedText]
  for (const [value, localizedText] of enumValues) {
    options.push({
      value: value,
      label: `${value} (${localizedText?.text || localizedText || value})`
    });
  }
  
  return options;
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
const discoveryConfigDialogVisible = ref(false);
const discoveryFindDialogVisible = ref(false);
const discoveryLoading = ref(false);
const discoveryServers = ref([]);
const selectedDiscoveryServerId = ref('');
const discoveryFindUrlInput = ref('');
const discoveryAddressInput = ref('');
const discoverySearchHistory = ref([]);
const discoveryConfigForm = ref({
  addresses: [],
});
const documentForm = ref({
  documentType: 'Event View'
});

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
const showByteData = reactive([]); // 改为数组，支持多个 Byte 值
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
const currentMethodTreeNode = ref(null); // 保存 Element Plus tree 的 node 对象
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
  const numericMask = Number(userWriteMask);
  if (Number.isNaN(numericMask)) {
    return {
      icon: 'QuestionFilled',
      text: '未知',
      color: '#909399',
      canEdit: false
    };
  }

  const canRead = (numericMask & 1) === 1;
  const canWrite = (numericMask & 2) === 2;
  const hasHistoryRead = (numericMask & 4) === 4;
  const hasHistoryWrite = (numericMask & 8) === 8;

  if (canRead && canWrite) {
    return {
      icon: 'Edit',
      text: hasHistoryRead || hasHistoryWrite ? '可读可写（含历史访问）' : '可读可写',
      color: '#67c23a',
      canEdit: true
    };
  }

  if (canWrite) {
    return {
      icon: 'EditPen',
      text: hasHistoryRead || hasHistoryWrite ? '只写（含历史访问）' : '只写',
      color: '#e6a23c',
      canEdit: true
    };
  }

  if (canRead) {
    return {
      icon: 'Lock',
      text: hasHistoryRead || hasHistoryWrite ? '只读（含历史访问）' : '只读',
      color: '#f56c6c',
      canEdit: false
    };
  }

  return {
    icon: 'QuestionFilled',
    text: '未知',
    color: '#909399',
    canEdit: false
  };
};

const DEFAULT_VARIABLE_ACCESS_LEVEL = 7;

const formatAccessLevelValue = (accessLevel) => {
  const numericAccessLevel = Number(accessLevel);
  if (Number.isNaN(numericAccessLevel)) {
    return accessLevel ?? '';
  }

  const accessLevelFlags = [
    { bit: 1, label: 'CurrentRead' },
    { bit: 2, label: 'CurrentWrite' },
    { bit: 4, label: 'HistoryRead' },
    { bit: 8, label: 'HistoryWrite' },
    { bit: 16, label: 'SemanticChange' },
    { bit: 32, label: 'StatusWrite' },
    { bit: 64, label: 'TimestampWrite' },
  ];

  const matchedFlags = accessLevelFlags
    .filter(({ bit }) => (numericAccessLevel & bit) === bit)
    .map(({ label }) => label);

  if (matchedFlags.length === 0) {
    return String(numericAccessLevel);
  }
  return `  (${matchedFlags.join(' | ')})`;
  // return `${numericAccessLevel} (${matchedFlags.join(' | ')})`;
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
// 计算属性：重新组织数据，主数据参与排序，子数据不参与排序但保持位置
const sortedMergedNodeData = computed(() => {
  const data = state.mergedNodeData;
  if (!data || data.length === 0) return [];
  
  // 分离主数据和子数据
  const mainData = [];
  const childDataMap = new Map(); // 存储每个父节点的子数据
  
  data.forEach((item, index) => {
    if (item._isChildData) {
      // 子数据：根据父节点ID分组
      const parentNodeId = item._parentNodeId?.toString();
      if (parentNodeId) {
        if (!childDataMap.has(parentNodeId)) {
          childDataMap.set(parentNodeId, []);
        }
        childDataMap.get(parentNodeId).push({ item, originalIndex: index });
      }
    } else {
      // 主数据：保存原始索引以便后续插入子数据
      mainData.push({ item, originalIndex: index });
    }
  });
  
  // 对主数据进行排序（这里可以根据需要添加排序逻辑）
  // 目前保持原顺序，如果需要排序可以在这里添加
  mainData.sort((a, b) => a.originalIndex - b.originalIndex);
  
  // 重新组织数据：主数据 + 其子数据
  const result = [];
  mainData.forEach(({ item }) => {
    result.push(item);
    
    // 添加该主数据的子数据
    const nodeId = item.nodeId?.toString();
    if (nodeId && childDataMap.has(nodeId)) {
      const children = childDataMap.get(nodeId);
      children.forEach(({ item: childItem }) => {
        result.push(childItem);
      });
    }
  });
  
  return result;
});

// 辅助函数：统一获取 nodeId 字符串
const getNodeIdString = (item) => {
  if (!item) return '';
  
  // 如果 nodeId 是字符串，直接返回
  if (typeof item.nodeId === 'string') {
    return item.nodeId;
  }
  
  // 如果 nodeId 是对象，尝试 toString
  if (item.nodeId && typeof item.nodeId.toString === 'function') {
    return item.nodeId.toString();
  }
  
  // 如果有 nodeIds 字段（字符串格式）
  if (item.nodeIds) {
    return item.nodeIds;
  }
  
  // 如果有 nodeIdNum 字段
  if (item.nodeIdNum) {
    return item.nodeIdNum;
  }
  
  return '';
}

// 计算属性：获取非子数据的索引映射（使用 nodeId 作为 key）
const mainDataIndexMap = computed(() => {
  const data = sortedMergedNodeData.value;
  const indexMap = new Map();
  let mainIndex = 0;
  
  data.forEach((item) => {
    if (!item._isChildData) {
      const nodeId = getNodeIdString(item);
      const ReferenceTypeId = item.referenceTypeId?.toString();
      if (nodeId) {
        indexMap.set(nodeId, mainIndex);
        mainIndex++;
      }
    }
  });
  
  return indexMap;
});

const currentPageTitle = computed(() => {
  return pageStore.currentPage 
    ? pageStore.currentPage.title 
    : '动态面包屑示例';
});

// Method Call 弹窗标题
const methodCallTitle = computed(() => {
  if (!currentMethodNode.value) return 'Call Method';
  
  const methodName = getDisplayName(currentMethodNode.value) || 'Method';
  
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
  
  // 检查是否已经展开过，如果展开过则收起（移除之前插入的数据）
  if (e._isExpanded && e._expandedChildrenCount) {
    const currentIndex = state.mergedNodeData.findIndex(item => item === e);
    if (currentIndex !== -1) {
      // 移除之前插入的子数据
      state.mergedNodeData.splice(currentIndex + 1, e._expandedChildrenCount);
      e._isExpanded = false;
      e._expandedChildrenCount = 0;
      e._isLoading = false;
      return;
    }
  }
  
  // 先尝试从 store 中获取数据
  const parentNodeId = e.nodeId?.toString()
  if (parentNodeId) {
    // 检查 store 中是否有该节点的数据（包括空数据）
    if (detailCardStore.hasNodeDetailData(parentNodeId)) {
      const cachedData = detailCardStore.getNodeDetailData(parentNodeId)
      
      // 如果 store 中有数据（包括空数组），直接使用，避免重复请求
      if (cachedData !== null) {
        if (cachedData.length > 0) {
          console.log(`从 store 中恢复节点 ${parentNodeId} 的详情数据，共 ${cachedData.length} 条`)
          
          // 找到当前行在 mergedNodeData 中的索引
          const currentIndex = state.mergedNodeData.findIndex(item => item === e);
          
          if (currentIndex !== -1) {
            // 将缓存的数据插入到当前行之后
            state.mergedNodeData.splice(currentIndex + 1, 0, ...cachedData);
            
            // 记录展开状态和子数据数量
            e._isExpanded = true
            e._expandedChildrenCount = cachedData.length
          }
          
          // 同时保持全局数据用于其他用途
          state.allCardData = cachedData
        } else {
          ElMessage.warning('获取数据为空')
          console.log(`从 store 中恢复节点 ${parentNodeId} 的详情数据为空，跳过接口请求`)
          // 数据为空，不插入数据，但也不调用接口
          state.allCardData = []
        }
        
        e._isLoading = false
        return
      }
    }
  }
  
  // 如果 store 中没有数据，从接口获取
  console.log(`从接口获取节点 ${parentNodeId} 的详情数据`)
  
  try{
      getBrowseDatas(passNodeClass, 10, null, e).then(async (firstRes) => {
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
        // 即使数据为空，也要保存到 store，避免重复请求
        if(!allArr || allArr.length == 0 ){
          // 保存空数组到 store
          if (parentNodeId) {
            detailCardStore.saveNodeDetailData(parentNodeId, [])
          }
          
          e._isLoading = false  // 隐藏loading
          ElMessage.warning('获取数据为空')
          return
        }
        
        // 获取正确的URL
        const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
        let url = allData?.url || urlForm.value.url;
        
        if (!url) {
          // URL 配置失败时也保存空数组到 store，避免重复请求
          if (parentNodeId) {
            detailCardStore.saveNodeDetailData(parentNodeId, [])
          }
          
          e._isLoading = false
          ElMessage.warning('未找到有效的URL配置')
          return
        }
        
        // 使用 processVariablesData 的逻辑处理数据
        // 初始化 OPC UA 客户端
        let apiConfig = new Configuration({
          basePath: url
        });
        let clientConfig = new UaClientConfiguration(apiConfig);
        let testOpcServer = new UaWebClient(clientConfig);
        
        let readNodeIds = [];
        allArr.forEach((item) => {
          if (item.nodeId?._nodeId) {
            readNodeIds.push(item.nodeId._nodeId);
          }
        });
        
        // 批量读取值
        let current = [];
        try {
          current = await testOpcServer.readValues(readNodeIds);
          console.log(current,'-------');
        } catch (error) {
          console.error('读取值失败:', error);
          current = new Array(allArr.length).fill(null);
        }
        
        // 获取所有节点的 nodeId，并创建映射关系
        let variableNodeIds = [];
        allArr.forEach((item) => {
          if (item.nodeId?._nodeId) {
            variableNodeIds.push(item.nodeId._nodeId);
          }
        });
        
        // 批量读取所有节点的变量属性
        let allVariableAttributes = [];
        try {
          allVariableAttributes = await testOpcServer.readVariableAttributes(variableNodeIds);
        } catch (error) {
          console.warn('Error reading variable attributes for all nodes:', error);
          allVariableAttributes = new Array(variableNodeIds.length).fill(null);
        }
        
        // 处理数据，使用与 processVariablesData 相同的逻辑
        let processedArr = allArr.map((item, index) => {
          // 添加基础属性
          let tempNodeClass = item.nodeClass
          item.changeNodeClass = nodeClassType[tempNodeClass]
          item.nodeIdNum = item.nodeId?._nodeId.toString()
          // 添加标记，表示这是展开的子数据
          item._isChildData = true
          item._parentNodeId = e.nodeId
          
          // 安全地获取类型值
          let typeValue = current[index]?.value?.type;
          let dataValue = current[index] || null;
          
          // 如果当前节点没有数据，返回基础对象
          if (!dataValue) {
            return {
              ...item,
              value: null,
              originalType: typeValue,
              dataValue: null,
              name:  getDisplayName(item)|| '',
              nodeIds: item.nodeId.toString(),
              _isSelected: false,
              _isEditing: false
            };
          }
          
          // 使用辅助函数查找对应的 ObjectId
          let translateTypes = findObjectIdByType(typeValue);
          item.translateTypes = translateTypes;
          
          try {
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
            let temp = state.allTypesData.filter((idx) => {
              let typeId = idx.nodeId.toString();
              let dataTypeId = currentID.dataType ? currentID.dataType.toString() : '';
              return typeId === dataTypeId;
            });
            
            let dataTypeDictionary = new UaDataTypeDictionary();
            let dataTypes = dataTypeDictionary.getDataType(currentID);
            
            let obj = {
              ...item,
              value: current[index]?.value || null,
              originalType: typeValue,
              dataValue: dataValue,
              UserWriteMask: currentID.userAccessLevel,
              AccessLevel: currentID.accessLevel ?? currentID.userAccessLevel ?? null,
              Historizing: currentID.historizing,
              name: getDisplayName(item) || item.browseName,
              typeLookupSuccess: translateTypes !== null,
              dataType: temp[0]?._displayName?._text || '',
              dataTypesObj: temp[0],
              dataTypes: temp[0]?._parentType?._browseName ? temp[0]?._parentType?._browseName : (temp[0]?.browseName || 'Unknown'),
              nodeId: item.nodeId.toString(),
              _editValue: (() => {
                let value = (current[index]?.value && typeof current[index]?.value === 'object')
                  ? current[index]?.value?.value
                  : current[index]?.value;
                
                // 特殊处理 DateTime 类型
                if (temp[0]?._browseName === 'DateTime' && value) {
                  if (typeof value === 'string' || typeof value === 'number') {
                    const dateValue = new Date(value);
                    if (!isNaN(dateValue.getTime())) {
                      return value;
                    }
                  }
                  return value;
                }
                
                return value;
              })(),
              _isSelected: false,
              _isEditing: false
            };
            console.log(obj,'-------');
            // 处理 Boolean 类型
            if (obj.dataTypes === 'Boolean' && !Array.isArray(obj._editValue?.value)) {
              obj.enumStrings = [false, true];
            }
            
            // 处理 ByteString 类型
            if (obj.dataTypes === 'ByteString') {
              if (obj.value && Array.isArray(obj.value)) {
                obj._editValue = obj.value.map(item => {
                  if (typeof item === 'string') {
                    return byteStringToHex(item);
                  }
                  return item;
                });
              } else if (obj.value) {
                if (typeof obj.value === 'string') {
                  obj._editValue = byteStringToHex(obj.value);
                } else {
                  obj._editValue = obj.value?.value ?? '';
                }
              }
            }
            
            // 处理 ExpandedNodeId 类型
            if (obj.dataTypes === 'ExpandedNodeId' && !Array.isArray(obj._editValue.value)) {
              if (obj.value && Array.isArray(obj.value)) {
                obj._editValue = obj.value.map(item => {
                  const parsed = parseExpandedNodeId(item);
                  return expandedNodeIdToString(parsed);
                });
              } else if (obj.value) {
                const parsed = parseExpandedNodeId(obj.value);
                obj._editValue = expandedNodeIdToString(parsed);
              }
            }
            
            // 处理 ImagePNG 类型
            if (obj.dataTypes === "ImagePNG" || obj.dataTypes === "Image") {
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
            
            // 处理 ByteString 特殊情况
            if (obj.dataTypes === "ByteString" && !Array.isArray(obj._editValue?.value)) {
              const str = obj._editValue?.value;
              if (str != null) {
                const hexString = stringToHex(str);
                obj._editValue = hexString;
              }
            }
            
            // 处理数组类型
            if (!obj.dataType && Array.isArray(obj._editValue)) {
              obj._editValue = obj._editValue?.map(item => item._dataTypeId).join(',');
            }
            console.log(obj.value,'obj.value?.type,obj.value?.isScalar()',obj.value.type);
            // 处理 Structure 类型 (UaExtensionObject)
            if (obj.dataTypes == "Structure" || obj.value?.type == UaVariantType.ExtensionObject  ) {
              console.log(12345)
              // 检查是否是 UaExtensionObject 类型
              if (obj._editValue && typeof obj._editValue === 'object') {
                // 尝试获取 payload（UaExtensionObject 有 payload getter）
                if (obj._editValue.payload !== undefined) {
                  const payload = obj._editValue.payload;
                  console.log(payload,'-------141414');
                  // 优先显示 DisplayName.Text，如果没有则显示其他关键信息
                  if (payload?.DisplayName?.Text) {
                    console.log(payload,'-------131313');
                    // obj._editValue = payload.DisplayName.Text;
                    obj._editValue =payload ? Object.entries(payload).map(([k, v]) => `${k}：${JSON.stringify(v)}`).join('，') : '';
                  } else if (payload?.DisplayName) {
                    console.log(payload,'-------121212');
                    obj._editValue = typeof payload.DisplayName === 'string' 
                      ? payload.DisplayName 
                      : payload.DisplayName.Text || JSON.stringify(payload);
                  } else {
                    // 如果没有 DisplayName，使用 toJson() 方法或直接序列化 payload
                    try {
                      if (typeof obj._editValue.toJson === 'function') {
                        obj._editValue = JSON.parse(JSON.stringify(obj._editValue)) || '';
                        console.log(obj._editValue,'-------11111');
                        obj._editValue =obj._editValue?._payload ? Object.entries(obj._editValue?._payload).map(([k, v]) => `${k}：${v}`).join('，') : '';
// 得到 "Low：0，High：100"
                      } else {
                        console.log(obj._editValue,'-------22222');
                        obj._editValue = JSON.stringify(payload, null, 2);
                      }
                    } catch (e) {
                      console.log(obj._editValue,'-------101010');
                      obj._editValue = JSON.stringify(payload);
                    }
                  }
                } else if (obj._editValue._payload !== undefined) {
                  // 如果 payload 是私有属性，尝试直接访问 _payload
                  const payload = obj._editValue._payload;
                  if (payload?.DisplayName?.Text) {
                    obj._editValue = payload.DisplayName.Text;
                    console.log(obj._editValue,'-------33333');
                  } else if (payload?.DisplayName) {
                    console.log(obj._editValue,'-------44444');
                    obj._editValue = typeof payload.DisplayName === 'string' 
                      ? payload.DisplayName 
                      : payload.DisplayName.Text || JSON.stringify(payload);
                  } else {
                    try {
                      if (typeof obj._editValue.toJson === 'function') {
                        console.log(obj._editValue,'-------55555');
                        obj._editValue = JSON.stringify(obj._editValue.toJson(), null, 2);
                      } else {
                        console.log(obj._editValue,'-------66666');
                        obj._editValue = JSON.stringify(payload, null, 2);
                      }
                    } catch (e) { 
                      console.log(obj._editValue,'-------99999');
                      obj._editValue = JSON.stringify(payload);
                    }
                  }
                } else {
                  // 如果不是 UaExtensionObject，尝试序列化整个对象
                  try {
                    // 辅助函数：格式化值，确保对象能正确显示
                    const formatValue = (val) => {
                      if (val === null) return 'null';
                      if (val === undefined) return 'undefined';
                      if (typeof val === 'string') return val;
                      if (typeof val === 'number' || typeof val === 'boolean') return String(val);
                      if (Array.isArray(val)) {
                        return `[${val.map(formatValue).join(', ')}]`;
                      }
                      if (typeof val === 'object') {
                        try {
                          // 尝试使用 JSON.stringify，如果失败则手动格式化
                          return JSON.stringify(val, null, 2);
                        } catch (e) {
                          // 如果有循环引用等问题，手动构建字符串
                          const entries = Object.entries(val).map(([k, v]) => `${k}: ${formatValue(v)}`);
                          return `{${entries.join(', ')}}`;
                        }
                      }
                      return String(val);
                    };
                    
                    const bodyObj = obj._editValue?.body;
                    if (bodyObj && typeof bodyObj === 'object') {
                      if (Array.isArray(bodyObj)) {
                        // 如果是数组，遍历数组中的每个元素
                        const displayName = bodyObj.map((item, index) => {
                          if (item && typeof item === 'object' && !Array.isArray(item)) {
                            const entries = Object.entries(item).map(([k, v]) => `${k}：${formatValue(v)}`);
                            return `[${index}]：${entries.join('，')}`;
                          } else {
                            return `[${index}]：${formatValue(item)}`;
                          }
                        }).join('；');
                        obj._editValue = displayName;
                      } else {
                        // 如果不是数组，按原来的方式处理
                        const displayName = Object.entries(bodyObj).map(([k, v]) => `${k}：${formatValue(v)}`).join('，');
                        obj._editValue = displayName;
                      }
                    } else {
                      obj._editValue = bodyObj ? formatValue(bodyObj) : String(obj._editValue);
                    }
                  } catch (e) {
                    console.log(obj._editValue,'-------88888');
                    obj._editValue = String(obj._editValue);
                  }
                }
              }
            }
            
            return obj;
          } catch (err) {
            console.log(err);
            return null;
          }
        }).filter(obj => obj !== null); // 过滤掉 null 值
        
        // 找到当前行在 mergedNodeData 中的索引
        const currentIndex = state.mergedNodeData.findIndex(item => item === e);
        
        if (currentIndex !== -1) {
          // 将处理后的数据插入到当前行之后
          state.mergedNodeData.splice(currentIndex + 1, 0, ...processedArr);
          
          // 记录展开状态和子数据数量
          e._isExpanded = true
          e._expandedChildrenCount = processedArr.length
        }
        
        // 同时保持全局数据用于其他用途
        state.allCardData = processedArr
        
        // 保存数据到 Pinia store，避免下次重复调用接口
        if (parentNodeId) {
          detailCardStore.saveNodeDetailData(parentNodeId, processedArr)
        }
        
        e._isLoading = false  // 隐藏loading
     });
     }
     catch(err){
        // 获取失败时也保存空数组到 store，避免重复请求
        if (parentNodeId) {
          detailCardStore.saveNodeDetailData(parentNodeId, [])
        }
        
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
  detailsVariableMessage(dbSelectRowId.value,item)
};

// 辅助函数：获取显示名称（优先使用 displayName）
const getDisplayName = (item) => {
  if (!item) return '';
  
  // 优先使用 displayName
  if (item.displayName) {
    if (typeof item.displayName === 'string') {
      return item.displayName;
    }
    if (item.displayName._text) {
      return item.displayName._text;
    }
    if (item.displayName.text) {
      return item.displayName.text;
    }
    if (item.displayName.name) {
      return item.displayName.name;
    }
  }
  
  // 备选：使用 targetDisplayName（用于引用关系）
  if (item.targetDisplayName) {
    return item.targetDisplayName;
  }
  
  // 最后备选：使用 browseName 或 name
  return item.browseName || item.BrowseName || item.name || '';
};
const getVariableType = (item) => {
  if (!item) return '';

  let typeDefinition = item._typeDefinition?._nodeId?.value
  if( VariableTypeIds.BaseVariableType == typeDefinition
   || VariableTypeIds.BaseDataVariableType == typeDefinition
   || VariableTypeIds.PropertyType == typeDefinition
  ){
    return false;
  }
  return true;
};
// 辅助函数：提取值
const extractValue = (item) => {
  // 优先使用 _editValue（已经在数据加载时处理过）
  if (item._editValue !== undefined && item._editValue !== null) {
    return item._editValue;
  }
  
  // 处理数组类型
     if (Array.isArray(item.value)) {
    return item.value;
  }
  
  // 处理嵌套的 value.value 结构
  if (item.value && typeof item.value === 'object' && Array.isArray(item.value.value)) {
    return item.value.value;
  }
  
  // 处理单个值的嵌套结构（如 Int16/Int32/Int64）
  if (item.value && typeof item.value === 'object' && item.value.value !== undefined) {
    const dataType = item.dataType || '';
    // 对于 Int16/Int32/Int64 类型，确保提取正确的值
    if (dataType === 'Int16' || dataType === 'Int32' || dataType === 'Int64') {
      return item.value.value;
    }
    return item.value.value;
  }
  
  // 默认返回 value 或 _editValue
  return item.value !== undefined ? item.value : item._editValue;
};

// 辅助函数：设置值到数组
const setValueToArray = (targetArray, value) => {
  const arrValue = Array.isArray(value) ? value : [value];
  targetArray.splice(0, targetArray.length, ...arrValue);
};

// 辅助函数：格式化日期时间为 el-date-picker 需要的格式
const formatDateTimeForPicker = (date) => {
  if (!date) return '';
  const d = date instanceof Date ? date : new Date(date);
  if (isNaN(d.getTime())) return '';
  
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  const seconds = String(d.getSeconds()).padStart(2, '0');
  
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

// 判断数据类型是否允许编辑
const isDataTypeEditable = (item) => {
  if (!item) return false;
  
  // 检查 UserWriteMask 权限
  const accessStatus = getAccessStatus(item.UserWriteMask);
  if (!accessStatus.canEdit) {
    return false;
  }
  
  const dataType = item.dataType || '';
  const dataTypes = item.dataTypes || '';
  
  // 不允许编辑的数据类型列表
  const nonEditableTypes = [
    'ByteString',
    'NodeId',
    'ExpandedNodeId',
    'Structure',
    'Guid',
    'Xml',
    'XmlElement',
    'LocalizedText',
    'QualifiedName',
    'StatusCode',
    'CarExtras',
    'OptionSetUInt64',
    'OptionSetUInt32',
    'OptionSetUInt16',
    'OptionSetByte',
    'OptionSetSByte',
    'OptionSetBase',
    'AccessRights',
    'StructureWithOptionalFields',
    'AnalogMeasurementValue'
  ];
  
  // 检查 dataType 或 dataTypes 是否在不允许编辑的类型列表中
  if (nonEditableTypes.includes(dataType) || nonEditableTypes.includes(dataTypes)) {
    return false;
  }
  
  return true;
};

// 开始编辑值
const startEditValue = (item) => {
  // 检查数据类型是否允许编辑
  if (!isDataTypeEditable(item)) {
    return;
  }
  
     currentEditingItem.value = item; // 保存当前编辑的 item
  const extractedValue = extractValue(item);
  const dataType = item.dataType || '';
  const dataTypes = item.dataTypes || '';
  
  // 1. 先检查具体类型（最具体的类型优先）
  
  // Boolean 类型
  if (dataType === "Boolean" || dataTypes === "Boolean") {
    showBooleanFlag.value = true;
    setValueToArray(showBooleanData, extractedValue);
    return;
  }
  
  // Byte 类型（OptionSet）- 必须在 UInteger 之前检查
  if ((dataType === "Byte" || (dataType && typeof dataType.includes === 'function' && dataType.includes('OptionSet'))) && dataTypes !== "UInt64") {
    showByteFlag.value = true;
    
    // 提取 Byte 值，支持数组和单个值
    let byteValues = [];
     if (Array.isArray(item.value)) {
      // 数组类型
      byteValues = item.value.map(val => {
        if (typeof val === 'object' && val !== null) {
          return val._value !== undefined ? val._value : (val.value !== undefined ? val.value : 0);
        }
        return val || 0;
      });
     } else if (item.value && Array.isArray(item.value.value)) {
      // value.value 是数组
      byteValues = item.value.value.map(val => {
        if (typeof val === 'object' && val !== null) {
          return val._value !== undefined ? val._value : (val.value !== undefined ? val.value : 0);
        }
        return val || 0;
      });
     } else {
      // 单个值
     let byteValue = 0;
     if (item.value && typeof item.value === 'object') {
        byteValue = item.value._value !== undefined ? item.value._value : 
                   (item.value.value !== undefined ? item.value.value : 0);
     } else {
        byteValue = item.value || item._editValue || 0;
      }
      byteValues = [byteValue];
    }
    
    // 设置到 showByteData 数组
    setValueToArray(showByteData, byteValues);
    return;
  }
  
  // UInt64 OptionSet 特殊处理
  if (dataTypes === "UInt64" && dataType === "OptionSetUInt64") {
    showUInt64Flag.value = true;
     let uint64Value = 0;
     if (item.value && typeof item.value === 'object') {
      uint64Value = item.value._value !== undefined ? item.value._value : 
                   (item.value.value !== undefined ? item.value.value : 0);
     } else {
       uint64Value = item.value || 0;
     }
     
     if (typeof uint64Value === 'string') {
       uint64Value = uint64Value.trim();
       if (uint64Value === '' || uint64Value === 'undefined' || uint64Value === 'null') {
         uint64Value = 0;
       }
     }
     
     let bigIntValue;
     try {
       bigIntValue = BigInt(uint64Value);
     } catch (error) {
       bigIntValue = 0n;
     }
     
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
    return;
  }
  
  // 注意：ByteString 和 ExpandedNodeId 类型不允许编辑，已在函数开始处检查并返回
  
  // ImagePNG 类型
  if (dataTypes === "ImagePNG" || dataType === "ImagePNG") {
    showImagePNGFlag.value = true;
    let hexValues = [];
     if (Array.isArray(item._editValue)) {
      hexValues = item._editValue.map(val => (typeof val === 'string' && /[\x00-\x08\x0B\x0C\x0E-\x1F]/.test(val)) ? binaryStringToHex(val) : val);
     } else if (Array.isArray(item.value)) {
      hexValues = item.value.map(val => binaryStringToHex(val));
    } else if (item.value && Array.isArray(item.value.value)) {
      hexValues = item.value.value.map(val => binaryStringToHex(val));
    } else {
      hexValues = [binaryStringToHex(item._editValue || item.value || '')];
    }
    showImagePNGData.splice(0, showImagePNGData.length, ...hexValues);
    return;
  }
  
  // CarExtras 类型
  if (dataType === "CarExtras") {
    showCarExtrasFlag.value = true;
    const carExtrasData = [
        { name: 'airconditioner', value: false, selected: true },
        { name: 'supercharged', value: true, selected: true },
        { name: 'launchcontrol', value: true, selected: true },
        { name: 'navigationsystem', value: false, selected: true }
      ];
    showCarExtrasData.splice(0, showCarExtrasData.length, ...carExtrasData);
    return;
  }
  
  // 2. 使用 issubtypeof 检查子类型关系（检查父类型）
  
  // UInteger 类型及其子类型（但排除已处理的 Byte）
  // 使用 isSubtypeOf 检查是否是 UInteger 的子类型，但确保不是 Byte
  let isUIntegerSubType = false;
  if (item.dataTypesObj && dataType !== "Byte" && dataTypes !== "Byte") {
    try {
      // 使用实例方法 isSubtypeOf 检查是否是 UInteger 的子类型
      const baseTypeNodeId = new UaNodeId(DataTypeIds.UInteger);
      if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
        if (item.dataTypesObj.isSubtypeOf(baseTypeNodeId)) {
          isUIntegerSubType = true;
        }
      }
    } catch (e) {
      console.warn('isSubtypeOf check failed:', e);
    }
  }
  
  // 回退：直接字符串匹配 UInteger（但排除 Byte 和已处理的具体类型）
  if (!isUIntegerSubType && (dataTypes === "UInteger" || dataType === "UInteger") && 
      dataType !== "Byte" && dataTypes !== "Byte" &&
      dataType !== "UInt16" && dataType !== "UInt32" && dataType !== "UInt64") {
    // 检查是否是数组类型
    if (Array.isArray(item.value?.value)) {
      showUIntegerFlag.value = true;
      setValueToArray(showUIntegerData, extractedValue);
      return;
    }
  }
  
  // 如果通过 issubtypeof 确认是 UInteger 子类型
  if (isUIntegerSubType && Array.isArray(item.value?.value)) {
    showUIntegerFlag.value = true;
    setValueToArray(showUIntegerData, extractedValue);
    return;
  }

  // 3. 检查其他具体类型（使用 issubtypeof 或直接匹配）
  
  // Double 类型
  if (dataTypes === "Double" || dataType === "Double") {
    showDoubleFlag.value = true;
    setValueToArray(showDoubleData, extractedValue);
    return;
  }
  
  // String 类型使用 Double 弹窗编辑
  if (dataTypes === "String" || dataType === "String") {
    showDoubleFlag.value = true;
    setValueToArray(showDoubleData, extractedValue);
    return;
  }
  
  // Int16/Int32/Int64 同 Double 弹窗编辑
  if (dataType === "Int16" || dataType === "Int32" || dataType === "Int64") {
    showDoubleFlag.value = true;
    // 特殊处理：Int16/Int32/Int64 的值可能在 value.value 中
    const intValue = Array.isArray(item.value) ? item.value : 
                    (item.value?.value !== undefined ? (Array.isArray(item.value.value) ? item.value.value : [item.value.value]) : extractedValue);
    setValueToArray(showDoubleData, intValue);
    return;
  }
  
  // Integer 类型使用 Double 弹窗编辑
  if (dataTypes === "Integer" || dataType === "Integer") {
    showDoubleFlag.value = true;
    setValueToArray(showDoubleData, extractedValue);
    return;
  }
  
  // Number 同 Double 弹窗编辑
  if (dataTypes === "Number" || dataType === "Number") {
    showDoubleFlag.value = true;
    setValueToArray(showDoubleData, extractedValue);
    return;
  }
  
  // 注意：NodeId 类型不允许编辑，已在函数开始处检查并返回

  // None 类型使用 Double 弹窗编辑
  if (dataTypes === "None" || dataType === "None") {
    showDoubleFlag.value = true;
    setValueToArray(showDoubleData, extractedValue);
    return;
  }

  // DateTime 和 UtcTime 类型使用 Double 弹窗编辑
  if (dataTypes === "DateTime" || dataType === "DateTime" || 
      dataTypes === "UtcTime" || dataType === "UtcTime" ||
      dataTypes === "UTC" || dataType === "UTC" ||
      dataTypes === "UTCTime" || dataType === "UTCTime") {
    showDoubleFlag.value = true;
    
    // 处理 DateTime 值，转换为日期时间字符串格式
    let dateTimeValues = [];
    if (Array.isArray(extractedValue)) {
      dateTimeValues = extractedValue.map(val => {
        if (val instanceof Date) {
          // 如果已经是 Date 对象，格式化为字符串
          return formatDateTimeForPicker(val);
        } else if (typeof val === 'string' || typeof val === 'number') {
          // 如果是字符串或数字，转换为 Date 对象再格式化
          const dateValue = new Date(val);
          if (!isNaN(dateValue.getTime())) {
            return formatDateTimeForPicker(dateValue);
          }
          return val;
        }
        return val;
      });
  } else {
      // 单个值
      if (extractedValue instanceof Date) {
        dateTimeValues = [formatDateTimeForPicker(extractedValue)];
      } else if (extractedValue && (typeof extractedValue === 'string' || typeof extractedValue === 'number')) {
        const dateValue = new Date(extractedValue);
        if (!isNaN(dateValue.getTime())) {
          dateTimeValues = [formatDateTimeForPicker(dateValue)];
        } else {
          dateTimeValues = [extractedValue];
        }
      } else {
        dateTimeValues = [extractedValue || formatDateTimeForPicker(new Date())];
      }
    }
    
    setValueToArray(showDoubleData, dateTimeValues);
    return;
  }
  // 检查是否为枚举类型（包括 Enumeration 及其子类型，如 HeaterStatus、Priority、ServerState）
  let isEnumerationSubType = false;
  const specificEnumTypes = ['HeaterStatus', 'Priority', 'ServerState'];
  const isSpecificEnumType = specificEnumTypes.includes(dataType) || specificEnumTypes.includes(dataTypes);
  
  console.log('isSpecificEnumType123456', isSpecificEnumType);
  // 如果是特定的枚举类型，使用 isSubtypeOf 检查是否为 Enumeration 的子类型
  if (isSpecificEnumType && item.dataTypesObj) {
    try {
      const enumerationNodeId = new UaNodeId(DataTypeIds.Enumeration);
      if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
        if (item.dataTypesObj.isSubtypeOf(enumerationNodeId)) {
          isEnumerationSubType = true;
          showDoubleFlag.value = true;
          setValueToArray(showDoubleData, extractedValue);
          return;
        }
      }
    } catch (error) {
      console.warn('检查 Enumeration 子类型时出错:', error);
      // 如果检查失败，假设这些特定类型是枚举类型
      isEnumerationSubType = true;
    }
  }
  
  // Enumeration 类型或其子类型使用 Double 弹窗编辑
  // if (dataTypes === "Enumeration" || dataType === "Enumeration" || isEnumerationSubType) {
  //   showDoubleFlag.value = true;
  //   setValueToArray(showDoubleData, extractedValue);
  //   return;
  // }

  // 注意：Structure 和 QualifiedName 类型不允许编辑，已在函数开始处检查并返回
  
  // NumericRange 同 Double 弹窗编辑
  if (dataTypes === "String" && dataType === "NumericRange" && Array.isArray(item.value?.value)) {
    showDoubleFlag.value = true;
    setValueToArray(showDoubleData, item.value?.value || extractedValue);
    return;
  }
  
  // 4. 默认处理：对于未匹配的类型，使用 Double 弹窗编辑
  showDoubleFlag.value = true;
  setValueToArray(showDoubleData, extractedValue);
  return;

  // 清除其他项的编辑状态
  state.mergedNodeData.forEach((row) => {
    if (row !== item) {
      row._isEditing = false;
    }
  });
  
  // 设置当前项为编辑状态
  item._isEditing = true;
  
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

  // 使用 isSubtypeOf 判断数据类型
  if (item.dataTypesObj && typeof item.dataTypesObj.isSubtypeOf === 'function') {
    try {
      // 判断是否是 Enumeration 类型的子类型
      const enumerationNodeId = new UaNodeId(DataTypeIds.Enumeration);
      if (item.dataTypesObj.isSubtypeOf(enumerationNodeId)) {
        // 如果 value 已经包含括号（在 processVariablesData 中已转换），直接返回
        if (typeof value === 'string' && value.includes('（')) {
          return value;
        }
        // 否则使用 UaLocalizedText 获取枚举值文本，格式为 "数字（文本）"
        if (item.dataTypesObj._enumValues && item.dataTypesObj._enumValues.size > 0) {
          const numValue = Number(value);
          const localizedText = item.dataTypesObj._enumValues.get(numValue);
          if (localizedText) {
            const text = typeof localizedText.text === 'function' ? localizedText.text() : (localizedText._text || String(localizedText));
            return `${numValue}（${text}）`;
          }
        }
        // 如果没有找到枚举值，返回原始值
        return value !== null && value !== undefined ? String(value) : '';
      }
    } catch (error) {
      console.warn('isSubtypeOf check failed:', error);
    }
  }

  // 特殊处理 ImagePNG 类型
  if (item.dataTypes === 'ImagePNG') {
    return formatImagePNGForDisplay(value);
  }
  
  // 特殊处理 Structure 类型 (UaExtensionObject)
  if (item.dataTypes === 'Structure' || item.dataType === 'Structure') {
    // 如果 value 已经是字符串（已经在 getDetialCard 中处理过），直接返回
    if (typeof value === 'string') {
      return value;
    }
    // 如果是对象，尝试提取 payload 数据
    if (value && typeof value === 'object') {
      try {
        // 尝试获取 payload（UaExtensionObject 有 payload getter）
        if (value.payload !== undefined) {
          const payload = value.payload;
          // 优先显示 DisplayName.Text
          if (payload?.DisplayName?.Text) {
            return payload.DisplayName.Text;
          } else if (payload?.DisplayName) {
            return typeof payload.DisplayName === 'string' 
              ? payload.DisplayName 
              : payload.DisplayName.Text || JSON.stringify(payload, null, 2);
          } else {
            // 使用 toJson() 方法或直接序列化 payload
            if (typeof value.toJson === 'function') {
              return JSON.stringify(value.toJson(), null, 2);
            } else {
              return JSON.stringify(payload, null, 2);
            }
          }
        } else if (value._payload !== undefined) {
          // 如果 payload 是私有属性，尝试直接访问 _payload
          const payload = value._payload;
          if (payload?.DisplayName?.Text) {
            return payload.DisplayName.Text;
          } else if (payload?.DisplayName) {
            return typeof payload.DisplayName === 'string' 
              ? payload.DisplayName 
              : payload.DisplayName.Text || JSON.stringify(payload, null, 2);
          } else {
            if (typeof value.toJson === 'function') {
              return JSON.stringify(value.toJson(), null, 2);
            } else {
              return JSON.stringify(payload, null, 2);
            }
          }
        } else {
          // 如果不是 UaExtensionObject，尝试序列化整个对象
          return JSON.stringify(value, null, 2);
        }
      } catch (e) {
        return String(value);
      }
    }
    return value ? String(value) : '';
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
  
  // 特殊处理 Int16/Int32/Int64 类型，确保正确显示数值
  if (item.dataType === 'Int16' || item.dataType === 'Int32' || item.dataType === 'Int64') {
    // 如果 value 是对象，尝试提取 value.value
    if (value && typeof value === 'object' && value.value !== undefined) {
      value = value.value;
    }
    // 处理数组
    if (Array.isArray(value)) {
      return value.map(v => {
        if (v && typeof v === 'object' && v.value !== undefined) {
          return String(v.value);
        }
        return String(v);
      }).join(', ');
    }
    // 单个值
    if (typeof value === 'number') {
      return String(value);
    }
    if (typeof value === 'string' && !isNaN(parseFloat(value))) {
      return String(parseInt(value));
    }
    return value ? String(value) : '';
  }
  
   if(item.dataType == "CarExtras"  && item.dataTypes == "OptionSet"){
     return 'Click to display value'
   }
   if(item.dataTypes == "UInt64" && item.dataType == "OptionSetUInt64"){
     return 'Click to display value'
   }
  if( ((item.dataTypes && typeof item.dataTypes.includes === 'function' && item.dataTypes.includes('Structure')) 
      || item.dataTypes == 'ByteString' 
      || item.dataTypes == "ExpandedNodeId"
      || item.dataTypes == "Image"
    )
      && item.value && Array.isArray(item.value.value)
  ){
     
    return 'Click to display value'
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
// 防抖定时器和请求状态跟踪
let detailsMessageTimer = null;
const detailsMessageLoading = ref(false);
const lastDetailsNodeId = ref(null);

const detailsMessage = async (nodeIDs, url) => {
  // 参数验证：检查 nodeId 是否有效
  if (!nodeIDs) {
    console.warn('detailsMessage: nodeId is required');
    return;
  }

  let nodeId = nodeIDs;
  
  // 检查是否正在加载，避免重复请求
  if (detailsMessageLoading.value) {
    console.log('detailsMessage: 正在加载中，忽略重复请求');
    return;
  }

  // 检查是否是同一个节点，避免重复请求相同节点
  const currentNodeIdStr = nodeId?.toString?.() || nodeId?._nodeId?.toString?.() || '';
  if (lastDetailsNodeId.value === currentNodeIdStr) {
    console.log('detailsMessage: 相同节点，忽略重复请求');
    return;
  }

  // 清除之前的防抖定时器
  if (detailsMessageTimer) {
    clearTimeout(detailsMessageTimer);
    detailsMessageTimer = null;
  }

  // 设置防抖延迟（200ms），避免快速连续点击
  detailsMessageTimer = setTimeout(async () => {
    try {
      // 标记为正在加载
      detailsMessageLoading.value = true;
      lastDetailsNodeId.value = currentNodeIdStr;

      // 使用传入的 url 参数，如果没有则尝试从 store 获取
      let cacheUrl = url;
      if (!cacheUrl) {
        const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
        cacheUrl = allData?.url || urlForm.value.url;
      }

      // 验证 URL 是否有效
      if (!cacheUrl) {
        console.warn('detailsMessage: 无效的 URL 配置');
        detailsMessageLoading.value = false;
        return;
      }

      // 验证 nodeId 是否有效
      if (!nodeId || (typeof nodeId === 'object' && !nodeId._nodeId && !nodeId.toString)) {
        console.warn('detailsMessage: 无效的 nodeId', nodeId);
        detailsMessageLoading.value = false;
        return;
      }

      let apiConfig = new Configuration({
        basePath: cacheUrl
      });
      let clientConfig = new UaClientConfiguration(apiConfig);
      let testOpcServer = new UaWebClient(clientConfig);

      // 并行执行两个请求，提高性能
      const [nodeAttributesArr, variableAttributesArr] = await Promise.all([
        testOpcServer.readNodeAttributes(nodeId, true).catch((error) => {
          console.error('读取节点属性失败:', error);
          return null;
        }),
        testOpcServer.readVariableAttributes([nodeId]).catch((error) => {
          console.error('读取变量属性失败:', error);
          return null;
        })
      ]);

      // 检查请求结果是否有效
      if (!nodeAttributesArr || !variableAttributesArr) {
        console.warn('detailsMessage: 请求结果无效');
        detailsMessageLoading.value = false;
        return;
      }

      // nodeDetailsData.value.displayName = nodeDetailsData.value.displayName.text
      nodeDetailsData.value = Object.assign({}, nodeAttributesArr, variableAttributesArr);
      console.log(nodeDetailsData.value,'nodeDetailsData.value')
      let temp = state.allTypesData.filter((idx) => {
        let typeId = idx.nodeId.toString();
        let dataTypeId = nodeDetailsData.value[0]?.dataType?.toString();
        return typeId == dataTypeId;
      });
      
      nodeDetailsData.value.dataType = temp[0]?._displayName?._text;
      nodeDetailsData.value.nodeId = nodeId;
      nodeDetailsData.value.UserWriteMask = nodeDetailsData.value[0]?.userAccessLevel;
      nodeDetailsData.value.accessLevel = nodeDetailsData.value[0]?.userAccessLevel;
      nodeDetailsData.value.description = nodeDetailsData.value.description?.text;
      
      // 保留原始nodeClass数值，同时添加转换后的文本
      if (nodeDetailsData.value.nodeClass !== undefined) {
        nodeDetailsData.value.nodeClassText = getNodeClassText(nodeDetailsData.value.nodeClass);
        delete nodeDetailsData.value.nodeClass;
      }
      nodeDetailsData.value.nodeClass = nodeDetailsData.value.nodeClassText;
      delete nodeDetailsData.value[0];
      delete nodeDetailsData.value.dataType;

      delete nodeDetailsData.value.nodeClassText;
      delete nodeDetailsData.value.writeMask;
      delete nodeDetailsData.value.UserWriteMask;
      delete nodeDetailsData.value.accessLevel;
      nodeDetailsData.value = reorderObj(nodeDetailsData.value, ['nodeId', 'nodeClass', 'browseName', 'displayName', 'description']);

    } catch (error) {
      console.error('detailsMessage 执行错误:', error);
    } finally {
      // 重置加载状态
      detailsMessageLoading.value = false;
    }
  }, 200); // 200ms 防抖延迟
}
const singleDetailsMessage = async (nodeIDs, url) => {
  console.log(selectNodeData.value,'selectNodeData.value')
  // 参数验证：检查 nodeId 是否有效
  if (!nodeIDs) {
    console.warn('detailsMessage: nodeId is required');
    return;
  }

  let nodeId = nodeIDs;
  
  // 检查是否正在加载，避免重复请求
  if (detailsMessageLoading.value) {
    console.log('detailsMessage: 正在加载中，忽略重复请求');
    return;
  }

  // 检查是否是同一个节点，避免重复请求相同节点
  const currentNodeIdStr = nodeId?.toString?.() || nodeId?._nodeId?.toString?.() || '';
  if (lastDetailsNodeId.value === currentNodeIdStr) {
    console.log('detailsMessage: 相同节点，忽略重复请求');
    return;
  }

  // 清除之前的防抖定时器
  if (detailsMessageTimer) {
    clearTimeout(detailsMessageTimer);
    detailsMessageTimer = null;
  }

  // 设置防抖延迟（200ms），避免快速连续点击
  detailsMessageTimer = setTimeout(async () => {
    try {
      // 标记为正在加载
      detailsMessageLoading.value = true;
      lastDetailsNodeId.value = currentNodeIdStr;

      // 使用传入的 url 参数，如果没有则尝试从 store 获取
      let cacheUrl = url;
      if (!cacheUrl) {
        const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
        cacheUrl = allData?.url || urlForm.value.url;
      }

      // 验证 URL 是否有效
      if (!cacheUrl) {
        console.warn('detailsMessage: 无效的 URL 配置');
        detailsMessageLoading.value = false;
        return;
      }

      // 验证 nodeId 是否有效
      if (!nodeId || (typeof nodeId === 'object' && !nodeId._nodeId && !nodeId.toString)) {
        console.warn('detailsMessage: 无效的 nodeId', nodeId);
        detailsMessageLoading.value = false;
        return;
      }

      let apiConfig = new Configuration({
        basePath: cacheUrl
      });
      let clientConfig = new UaClientConfiguration(apiConfig);
      let testOpcServer = new UaWebClient(clientConfig);

      // 并行执行两个请求，提高性能
      const [ variableAttributesArr] = await Promise.all([
        testOpcServer.readNodeAttributes([nodeId],true).catch((error) => {
          console.error('读取变量属性失败:', error);
          return null;
        })
      ]);

      // 检查请求结果是否有效
      if (  !variableAttributesArr) {
        console.warn('detailsMessage: 请求结果无效');
        detailsMessageLoading.value = false;
        return;
      }

      // nodeDetailsData.value.displayName = nodeDetailsData.value.displayName.text
      nodeDetailsData.value = Object.assign({}, variableAttributesArr);
      console.log(nodeDetailsData.value,'nodeDetailsData.value')
      let temp = state.allTypesData.filter((idx) => {
        let typeId = idx.nodeId.toString();
        let dataTypeId = nodeDetailsData.value[0]?.dataType?.toString();
        return typeId == dataTypeId;
      });
      // console.log(state.allTypesData,'temp')
      nodeDetailsData.value.dataType = temp[0]?._displayName?._text;
      nodeDetailsData.value.nodeId = nodeId;
      nodeDetailsData.value.UserWriteMask = nodeDetailsData.value[0]?.userAccessLevel;
      nodeDetailsData.value.accessLevel = nodeDetailsData.value[0]?.userAccessLevel;
      nodeDetailsData.value.description = nodeDetailsData.value.description?.text;
      
      // 保留原始nodeClass数值，同时添加转换后的文本
      if (nodeDetailsData.value.nodeClass !== undefined) {
        nodeDetailsData.value.nodeClassText = getNodeClassText(nodeDetailsData.value.nodeClass);
        delete nodeDetailsData.value.nodeClass;
      }
      nodeDetailsData.value.nodeClass = nodeDetailsData.value.nodeClassText.toString();
      
      if( nodeDetailsData.value.nodeClass  ==  "Object"){
        let typeDefinitionId = selectNodeData.value?.typeDefinition?._nodeId?.toString();
      let definition = '';
      state.allObjectTypesData.forEach(item => {
        if(item.nodeId.toString() == typeDefinitionId){
          definition = item?.displayName?.text;
        }
      });
      nodeDetailsData.value.definition =  definition;
        // nodeDetailsData.value.nodeClass = definition ;
      }
      // nodeDetailsData.value.definition = ObjectTypeIds[typeDefinitionId];
      delete nodeDetailsData.value[0];
      delete nodeDetailsData.value.dataType;

      delete nodeDetailsData.value.nodeClassText;
      delete nodeDetailsData.value.writeMask;
      delete nodeDetailsData.value.UserWriteMask;
      delete nodeDetailsData.value.accessLevel;
      nodeDetailsData.value = reorderObj(nodeDetailsData.value, ['nodeId', 'nodeClass', 'definition', 'browseName', 'displayName', 'description']);

    } catch (error) {
      console.error('detailsMessage 执行错误:', error);
    } finally {
      // 重置加载状态
      detailsMessageLoading.value = false;
    }
  }, 200); // 200ms 防抖延迟
}
const detailsVariableMessage = async(nodeIDs,item) => {
  let nodeId = nodeIDs;
  console.log(item,'selectNodeData.value')
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
    // console.log(nodeDetailsData.value,'nodeDetailsData.value')
    let temp = state.allTypesData.filter((idx)=>{
          let typeId= idx.nodeId.toString()
          let nodeId = nodeDetailsData.value[0].dataType.toString()
          return typeId == nodeId
        })
    nodeDetailsData.value.dataType = temp[0]?._displayName?._text
    nodeDetailsData.value.nodeId = nodeId
    nodeDetailsData.value.UserWriteMask = nodeDetailsData.value[0].userAccessLevel
    const resolvedAccessLevel =
      item?.AccessLevel ??
      nodeDetailsData.value[0]?.accessLevel ??
      nodeDetailsData.value[0]?.userAccessLevel ??
      DEFAULT_VARIABLE_ACCESS_LEVEL;
    nodeDetailsData.value.accessLevel = formatAccessLevelValue(resolvedAccessLevel)

    nodeDetailsData.value.historizing = item?.Historizing ?? nodeDetailsData.value[0].historizing
    nodeDetailsData.value.valueRank = nodeDetailsData.value[0].valueRank
    nodeDetailsData.value.description = nodeDetailsData.value.description?.text
    // 保留原始nodeClass数值，同时添加转换后的文本
    if (nodeDetailsData.value.nodeClass !== undefined) {
      nodeDetailsData.value.nodeClassText = getNodeClassText(nodeDetailsData.value.nodeClass);
       delete nodeDetailsData.value.nodeClass
    }
   
    // nodeDetailsData.value.nodeClass = nodeDetailsData.value.nodeClassText
    nodeDetailsData.value.nodeClass = nodeDetailsData.value.nodeClassText.toString();
      if( nodeDetailsData.value.nodeClass  ==  "Object"){
      let typeDefinitionId = item?.typeDefinition?._nodeId?.value;
      let definition = '';
      state.allObjectTypesData.forEach(item => {
        if(item.nodeId.toString() == typeDefinitionId){
          definition = item?.displayName?.text;
        }
      });
      nodeDetailsData.value.definition =  definition;
        // nodeDetailsData.value.nodeClass = definition;
      }
    delete nodeDetailsData.value[0]
    delete nodeDetailsData.value.nodeClassText
    delete nodeDetailsData.value.writeMask
    delete nodeDetailsData.value.UserWriteMask
    nodeDetailsData.value =  reorderObj(nodeDetailsData.value, ['nodeId','nodeClass','definition','browseName','displayName','description','dataType','accessLevel','historizing','valueRank'])

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
  if (!str || typeof str !== 'string' || str == null || str == undefined) return str;
  
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
const isDiscoveryNode = (data) => {
  if (!data) return false;
  return data.NodeId === DISCOVERY_NODE_ID || data.BrowseName === 'Discovery';
};
const ensureDiscoveryNodeExists = () => {
  const hasDiscovery = state.templateTreeData.some((item) => isDiscoveryNode(item));
  if (!hasDiscovery) {
    state.templateTreeData.push({
      ReferenceTypeId: 'i=38',
      IsForward: true,
      NodeId: DISCOVERY_NODE_ID,
      BrowseName: 'Discovery',
      DisplayName: { Text: 'Discovery' },
      NodeClass: 1,
      TypeDefinition: 'i=61',
    });
  }
};
const normalizeLocalizedText = (text) => {
  if (!text) return '';
  if (typeof text === 'string') return text;
  return text.Text || text.text || text._text || '';
};
const readDiscoveryUrls = (server) => {
  return server?.DiscoveryUrls || server?.discoveryUrls || server?.discoveryURLs || [];
};
const mapDiscoveryServer = (server, index) => {
  const urls = readDiscoveryUrls(server);
  const primaryUrl = urls[0] || '';
  const appName = normalizeLocalizedText(server?.ApplicationName || server?.applicationName);
  const uri = server?.ApplicationUri || server?.applicationUri || '';
  return {
    id: `${index}-${uri}-${primaryUrl}`,
    name: appName || uri || `Server ${index + 1}`,
    url: primaryUrl,
    urls,
    applicationUri: uri,
    raw: server,
  };
};
const normalizeDiscoverySearchAddress = (value) => String(value || '').trim();
const clearDiscoverySearchResults = () => {
  discoveryServers.value = [];
  selectedDiscoveryServerId.value = '';
};
const loadDiscoverySearchHistory = () => {
  try {
    const cache = localStorage.getItem(DISCOVERY_SEARCH_HISTORY_KEY);
    if (!cache) return;
    const parsed = JSON.parse(cache);
    if (Array.isArray(parsed)) {
      discoverySearchHistory.value = parsed
        .map((item) => normalizeDiscoverySearchAddress(item))
        .filter(Boolean);
    }
  } catch (error) {
    console.warn('读取 Discovery 搜索历史失败:', error);
  }
};
const saveDiscoverySearchHistory = () => {
  try {
    localStorage.setItem(DISCOVERY_SEARCH_HISTORY_KEY, JSON.stringify(discoverySearchHistory.value));
  } catch (error) {
    console.warn('保存 Discovery 搜索历史失败:', error);
  }
};
const recordDiscoverySearchAddress = (address) => {
  const normalizedAddress = normalizeDiscoverySearchAddress(address);
  if (!normalizedAddress) return;
  discoverySearchHistory.value = [
    normalizedAddress,
    ...discoverySearchHistory.value.filter((item) => item !== normalizedAddress),
  ].slice(0, 20);
  saveDiscoverySearchHistory();
};
const isValidDiscoveryAddress = (value) => isValidConnectionUrl(value);
const loadDiscoveryConfig = () => {
  try {
    const cache = localStorage.getItem(DISCOVERY_STORAGE_KEY);
    if (!cache) return;
    const parsed = JSON.parse(cache);
    if (Array.isArray(parsed?.addresses)) {
      discoveryConfigForm.value.addresses = parsed.addresses.filter(Boolean);
    } else if (parsed?.address) {
      discoveryConfigForm.value.addresses = [parsed.address].filter(Boolean);
    }
  } catch (error) {
    console.warn('读取 Discovery 配置失败:', error);
  }
};
const handleDiscoveryConfig = () => {
  discoveryConfigDialogVisible.value = true;
};
const handleDiscoveryAddressAdd = () => {
  const address = discoveryAddressInput.value.trim();
  if (!address) {
    ElMessage.error('请填写 Discovery 地址');
    return;
  }
  if (!isValidDiscoveryAddress(address)) {
    ElMessage.error('Discovery 地址格式不正确');
    return;
  }
  if (discoveryConfigForm.value.addresses.includes(address)) {
    ElMessage.warning('该地址已存在');
    return;
  }
  discoveryConfigForm.value.addresses.push(address);
  discoveryAddressInput.value = '';
};
const handleDiscoveryAddressRemove = (address) => {
  discoveryConfigForm.value.addresses = discoveryConfigForm.value.addresses.filter((item) => item !== address);
};
const handleDiscoveryConfigSave = () => {
  if (!discoveryConfigForm.value.addresses.length) {
    ElMessage.error('请至少配置一个 Discovery 地址');
    return;
  }
  try {
    localStorage.setItem(DISCOVERY_STORAGE_KEY, JSON.stringify(discoveryConfigForm.value));
    discoveryConfigDialogVisible.value = false;
    ElMessage.success('Discovery 配置已保存');
  } catch (error) {
    ElMessage.error('保存 Discovery 配置失败');
  }
};
const handleDiscoveryFind = () => {
  discoveryFindDialogVisible.value = true;
  discoveryFindUrlInput.value = discoveryFindUrlInput.value || discoverySearchHistory.value[0] || '';
  clearDiscoverySearchResults();
};
const handleDiscoveryFindSearch = async () => {
  const address = normalizeDiscoverySearchAddress(discoveryFindUrlInput.value);
  if (!address) {
    ElMessage.error('请输入 Discovery 地址');
    return;
  }
  if (!isValidConnectionUrl(address)) {
    ElMessage.error('地址格式不正确，请输入有效的 URL');
    return;
  }
  discoveryLoading.value = true;
  selectedDiscoveryServerId.value = '';
  try {
    const controller = new AbortController();
    const fetchApi = (input, init) => fetch(input, { ...init, signal: controller.signal });
    const apiConfig = new Configuration({
      basePath: address,
      fetchApi,
    });
    const clientConfig = new UaClientConfiguration(apiConfig);
    const testOpcServer = new UaWebClient(clientConfig);
    const servers = await withTimeout(
      testOpcServer.findServer(address, []),
      10000,
      'Discovery Find 超时',
      controller
    );
    const mappedServers = (servers || []).map((item, index) => ({
      ...mapDiscoveryServer(item, index),
      discoveryAddress: address,
    })).filter((item) => item.url);
    const dedupedServers = mappedServers.filter((item, index, arr) => {
      return arr.findIndex((current) => current.url === item.url) === index;
    });
    recordDiscoverySearchAddress(address);
    discoveryServers.value = dedupedServers;
    selectedDiscoveryServerId.value = dedupedServers[0]?.id || '';
    if (dedupedServers.length === 0) {
      ElMessage.warning('未查询到可用服务器');
      return;
    }
  } catch (error) {
    console.error('Discovery Find 失败:', error);
    ElMessage.error('Discovery Find 失败，请检查地址是否可用');
    clearDiscoverySearchResults();
  } finally {
    discoveryLoading.value = false;
  }
};
const loadConnectedTopServerData = async () => {
  try {
    const firstRes = await getBrowseDatas();
    let allArr = firstRes?.results || [];
    let continuationPoint = firstRes?.ContinuationPoint;

    while (continuationPoint) {
      continuationPoints.value = continuationPoint;
      try {
        const nextRes = await getBrowseNextDatas();
        if (nextRes?.results) {
          allArr = allArr.concat(nextRes.results);
        }
        continuationPoint = nextRes?.ContinuationPoint;
      } catch (error) {
        console.error('获取下一页数据失败:', error);
        break;
      }
    }

    if (!allArr || allArr.length === 0) {
      state.bottomTreeData = [];
      return;
    }

    if (consoleRef.value) {
      const rootNodeId = 'i=84';
      consoleRef.value.addLog('info', `Browse on node '${rootNodeId}' succeeded.`);
    }

    state.bottomTreeData = processNodeDataItems(allArr);
  } catch (error) {
    console.error('获取浏览数据失败:', error);
    state.bottomTreeData = [];
    if (consoleRef.value) {
      consoleRef.value.addLog('error', `Browse failed: ${error.message || 'Unknown error'}`);
    }
  }
};
const addServerNodeAndConnect = async (serverConfig) => {
  const targetNode = eltreeTop.value?.getNode('i=88');
  if (!targetNode) {
    ElMessage.error('未找到 Servers 根节点');
    return null;
  }

  const newNode = {
    ReferenceTypeId: 'i=38',
    IsForward: true,
    NodeId: `i=${generateKey()}`,
    BrowseName: serverConfig.urlName,
    DisplayName: { Text: serverConfig.urlName },
    NodeClass: 1,
    TypeDefinition: 'i=61',
    url: serverConfig.url,
  };

  if (!Array.isArray(targetNode.data.children)) {
    targetNode.data.children = [];
  }
  targetNode.data.children.push(newNode);
  targetNode.expanded = true;
  if (typeof targetNode.expand === 'function') {
    targetNode.expand();
  }

  selectedTopNodeId.value = newNode.NodeId;
  selectedNodeId.value = new UaNodeId(ObjectIds.RootFolder);
  urlDatas.setDataByKey(newNode.NodeId, serverConfig);

  await nextTick();
  eltreeTop.value?.setCurrentKey(null);
  await nextTick();
  eltreeTop.value?.setCurrentKey(newNode.NodeId);

  connectFlag.value = true;
  rootNodeLoaded.value = false;

  if (consoleRef.value) {
    const endpointUrl = serverConfig.url;
    consoleRef.value.addLog('info', `Endpoint: '${endpointUrl}'`);
    consoleRef.value.addLog('info', `Security policy: 'http://opcfoundation.org/UA/SecurityPolicy#None'`);
    if (serverConfig.applicationUri) {
      consoleRef.value.addLog('info', `ApplicationUri: '${serverConfig.applicationUri}'`);
    }
    consoleRef.value.addLog('info', `Used UserTokenType: Anonymous`);
    consoleRef.value.addLog('info', `Registered for Model ChangeEvents`);
    consoleRef.value.addLog('info', `Connection status of server '@' changed to 'Connected'`);
    consoleRef.value.addLog('info', `Revised values: SessionTimeout=1200000, SecureChannelLifetime=3600000`);
  }

  try {
    await ensureConnectionDictionariesLoaded(newNode.NodeId);
  } catch (error) {
    console.error('获取连接类型字典失败:', error);
  }

  await loadConnectedTopServerData();
  return newNode;
};
const handleDiscoveryServerAdd = async () => {
  const server = discoveryServers.value.find((item) => item.id === selectedDiscoveryServerId.value);
  if (!server) {
    ElMessage.error('请先选择一个服务器');
    return;
  }

  const newNode = await addServerNodeAndConnect({
    url: server.url,
    urlName: server.name,
    applicationUri: server.applicationUri || server.raw?.ApplicationUri || server.raw?.applicationUri || '',
  });

  if (!newNode) return;

  discoveryFindDialogVisible.value = false;
  ElMessage.success('服务器已添加并连接');
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
      urlRef.value.validate(async (valid) => {
      if (!valid) return false;

      // 立即关闭对话框，提升UI响应速度
      urlDialogVisible.value = false;

      try {
        const newNode = await addServerNodeAndConnect({
          ...urlForm.value,
          applicationUri: urlDatas.getDataByKey(selectedTopNodeId.value)?.applicationUri || '',
        });

        // 如果添加失败，可以选择重新打开对话框或提示错误
        if (!newNode) {
          // 可选：根据业务需求决定是否重新打开对话框
          // urlDialogVisible.value = true;
          console.warn('添加节点失败');
        }
      } catch (error) {
        // 异常处理：可以提示用户或重新打开对话框
        console.error('添加节点出错:', error);
        // urlDialogVisible.value = true; // 如果需要让用户重试
      }
    });
  // urlRef.value.validate( async(valid) => {
  //   if (valid) {
  //     const newNode = await addServerNodeAndConnect({
  //       ...urlForm.value,
  //       applicationUri: urlDatas.getDataByKey(selectedTopNodeId.value)?.applicationUri || '',
  //     });

  //     if (!newNode) {
  //       return;
  //     }

  //     urlDialogVisible.value = false;
     
    
  //   } else {
  //       return false;
  //     }
  // });
    
    
    
    })
   const getAllDataTypes = async(connectionKey = selectedTopNodeId.value) => {
        try {
          // 获取正确的URL
          const allData = urlDatas.getDataByKey(connectionKey);
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
          let dataTypes = dataTypeDictionary.getDataTypes();  
          let arr = []
          dataTypes.map((item)=>{ 
            let dataType = dataTypeDictionary.getDataType(item.nodeId);
            arr.push(dataType)
            })
          state.allTypesData = arr
          connectionDataTypesMap.value.set(stringifyNodeId(connectionKey), arr);
          // console.log(state.allTypesData,'state.allTypesData')
        } catch (error) {
          console.error('getAllDataTypes 错误:', error);
          // 不抛出错误，避免未捕获的Promise rejection
        }
   } 
   const getAllReferences = async(connectionKey = selectedTopNodeId.value) => {
    try {
      const allData = urlDatas.getDataByKey(connectionKey);
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
      let referenceTypeDictionary = new UaReferenceTypeDictionary( );
      await referenceTypeDictionary.read(testOpcServer);
      let referencesTypes = referenceTypeDictionary.getReferenceTypes();  
      let arr = []
      referencesTypes.map((item)=>{ 
            let referencesType = referenceTypeDictionary.getReferenceType(item.nodeId);
            arr.push(referencesType)
            })
      state.allReferenceTypesData = arr
      connectionReferenceTypesMap.value.set(stringifyNodeId(connectionKey), arr);
      // return references;
      // console.log(state.allReferenceTypesData,'state.allReferenceTypesData')
    } catch (error) {
      console.error('getAllReferences 错误:', error);
      // 不抛出错误，避免未捕获的Promise rejection
    }
   }
   const getAllObjectTypes = async(connectionKey = selectedTopNodeId.value) => {
    try {
      const allData = urlDatas.getDataByKey(connectionKey);
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
      let UaObjectTypeDictionarys = new UaObjectTypeDictionary( );
      await UaObjectTypeDictionarys.read(testOpcServer);
      let objectTypes = UaObjectTypeDictionarys.getObjectTypes();  
      // console.log(objectTypes,'dfsaf')
      let arr = []
      objectTypes.map((item)=>{ 
            let objectType = UaObjectTypeDictionarys.getObjectType(item.nodeId);
            // objectType.nodeids = item.nodeId.toString()
            // console.log(item,'item----',objectType)
            objectType.nodeids = item.nodeId.toString()
            arr.push(objectType)
            })
      state.allObjectTypesData = arr
      connectionObjectTypesMap.value.set(stringifyNodeId(connectionKey), arr);
      // console.log(state.allObjectTypesData)
      // return references;
    } catch (error) {
      console.error('getAllObjectTypes 错误:', error);
      // 不抛出错误，避免未捕获的Promise rejection
    }
   }

const ensureConnectionDictionariesLoaded = async (connectionKey = selectedTopNodeId.value) => {
  const normalizedConnectionKey = stringifyNodeId(connectionKey);
  if (!normalizedConnectionKey) return;

  const cachedDataTypes = connectionDataTypesMap.value.get(normalizedConnectionKey);
  const cachedReferenceTypes = connectionReferenceTypesMap.value.get(normalizedConnectionKey);
  const cachedObjectTypes = connectionObjectTypesMap.value.get(normalizedConnectionKey);

  if (cachedDataTypes && cachedReferenceTypes && cachedObjectTypes) {
    state.allTypesData = cachedDataTypes;
    state.allReferenceTypesData = cachedReferenceTypes;
    state.allObjectTypesData = cachedObjectTypes;
    return;
  }

  await Promise.all([
    getAllDataTypes(connectionKey),
    getAllReferences(connectionKey),
    getAllObjectTypes(connectionKey),
  ]);
};
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
const handleBooleanDialogConfirm = useThrottleFn(async () => {
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
  
  // 最终更新当前编辑 item 的值并写入 OPC UA
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
      
      // 调用 writeValues 写入 OPC UA
      const isArray = showBooleanData.length > 1;
      let uaVariant;
      if (isArray) {
        uaVariant = UaVariant.booleans(showBooleanData);
      } else {
        uaVariant = UaVariant.boolean(showBooleanData[0]);
      }
      
      await writeValueToOpcUa(uaVariant, 'Boolean', '布尔值写入成功！');
      
    } catch (error) {
      console.error('布尔值更新失败:', error);
      ElMessage.error('布尔值更新失败: ' + (error.message || '未知错误'));
      return;
    }
  }
  
  showBooleanFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
}, 1000);

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
const handleDoubleDialogConfirm = useThrottleFn(async () => {
  // 先进行表单验证
  const validationRules = getValidationRules();
  // 优先使用 dataType，如果不存在再使用 dataTypes（因为 dataType 更具体）
  const dataType = currentEditingItem.value?.dataType || currentEditingItem.value?.dataTypes;
  
  // 手动验证每个输入值
  let hasError = false;
  const errorMessages = [];
  
  // 使用 isSubtypeOf 判断实际数据类型
  let actualDataType = dataType;
  
  // UtcTime 类型应该作为 DateTime 处理
  if (dataType === 'UtcTime' || dataType === 'UTC' || dataType === 'UTCTime') {
    actualDataType = 'DateTime';
  } else if (currentEditingItem.value?.dataTypesObj) {
    try {
      const item = currentEditingItem.value;
      // 检查是否为 DateTime 的子类型（包括 UtcTime）
      const dateTimeNodeId = new UaNodeId(DataTypeIds.DateTime);
      if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
        if (item.dataTypesObj.isSubtypeOf(dateTimeNodeId)) {
          actualDataType = 'DateTime';
        }
      }
      
      // 检查是否为 Enumeration 的子类型
      const enumerationNodeId = new UaNodeId(DataTypeIds.Enumeration);
      if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
        if (item.dataTypesObj.isSubtypeOf(enumerationNodeId)) {
          actualDataType = 'Enumeration';
        }
      }
    } catch (error) {
      console.warn('使用 isSubtypeOf 判断数据类型时出错:', error);
    }
  }
  
  for (let i = 0; i < showDoubleData.length; i++) {
    const value = showDoubleData[i];
    
    // 根据实际数据类型进行验证
    switch (actualDataType) {
      case 'DateTime':
      case 'UtcTime':
      case 'UTC':
      case 'UTCTime':
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
      case 'HeaterStatus':
      case 'Priority':
      case 'ServerState':
        if (value === null || value === undefined || value === '') {
          errorMessages.push(`Value ${i + 1}: 请选择枚举值`);
          hasError = true;
        } else if (isNaN(parseInt(value))) {
          errorMessages.push(`Value ${i + 1}: 枚举值必须是数字`);
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
  
  // 验证通过，更新数据并写入 OPC UA
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
      
      // 调用 writeValues 写入 OPC UA
      try {
        // 使用 isSubtypeOf 判断数据类型并转换为 UaVariant
        let uaVariant;
        const isArray = showDoubleData.length > 1;
        const item = currentEditingItem.value;
        
        // 辅助函数：使用 isSubtypeOf 判断数据类型
        const getActualDataType = () => {
          // UtcTime 类型应该作为 DateTime 处理
          if (dataType === 'UtcTime' || dataType === 'UTC' || dataType === 'UTCTime') {
            return 'DateTime';
          }
          
          if (!item.dataTypesObj) {
            return dataType;
          }
          
          try {
            // 检查是否为 DateTime 的子类型（包括 UtcTime）
            const dateTimeNodeId = new UaNodeId(DataTypeIds.DateTime);
            if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
              if (item.dataTypesObj.isSubtypeOf(dateTimeNodeId)) {
                return 'DateTime';
              }
            }
            
            // 检查是否为 Enumeration 的子类型
            const enumerationNodeId = new UaNodeId(DataTypeIds.Enumeration);
            if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
              if (item.dataTypesObj.isSubtypeOf(enumerationNodeId)) {
                return 'Enumeration';
              }
            }
            
            // 检查是否为 UInteger 的子类型
            const uintegerNodeId = new UaNodeId(DataTypeIds.UInteger);
            if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
              if (item.dataTypesObj.isSubtypeOf(uintegerNodeId)) {
                // 检查具体的 UInteger 子类型
                if (dataType === 'Byte') {
                  return 'Byte';
                }
                return 'UInteger';
              }
            }
            
            // 检查是否为 Integer 的子类型
            const integerNodeId = new UaNodeId(DataTypeIds.Integer);
            if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
              if (item.dataTypesObj.isSubtypeOf(integerNodeId)) {
                // 检查具体的 Integer 子类型
                if (dataType === 'Int16' || dataType === 'Int32' || dataType === 'Int64') {
                  return dataType;
                }
                return 'Integer';
              }
            }
            
            // 检查是否为 Number 的子类型
            const numberNodeId = new UaNodeId(DataTypeIds.Number);
            if (item.dataTypesObj.isSubtypeOf && typeof item.dataTypesObj.isSubtypeOf === 'function') {
              if (item.dataTypesObj.isSubtypeOf(numberNodeId)) {
                if (dataType === 'Double' || dataType === 'Float') {
                  return dataType;
                }
                return 'Number';
              }
            }
          } catch (error) {
            console.warn('使用 isSubtypeOf 判断数据类型时出错:', error);
          }
          
          return dataType;
        };
        
        const actualDataType = getActualDataType();
        
        if (isArray) {
          // 数组类型
          switch (actualDataType) {
            case 'Double':
            case 'Number':
              uaVariant = UaVariant.doubles(showDoubleData.map(v => parseFloat(v) || 0));
              break;
            case 'Integer':
            case 'Int16':
            case 'Int32':
            case 'Int64':
              uaVariant = UaVariant.integers(showDoubleData.map(v => parseInt(v) || 0), 
                actualDataType === 'Int16' ? UaVariantType.Int16 :
                actualDataType === 'Int32' ? UaVariantType.Int32 :
                actualDataType === 'Int64' ? UaVariantType.Int64 :
                UaVariantType.Int32);
              break;
            case 'UInteger':
              uaVariant = UaVariant.integers(showDoubleData.map(v => parseInt(v) || 0), UaVariantType.UInt32);
              break;
            case 'Byte':
              uaVariant = UaVariant.byteString(showDoubleData.map(v => parseInt(v) || 0), UaVariantType.Byte);
              break;
            case 'String':
              uaVariant = UaVariant.strings(showDoubleData.map(v => String(v || '')));
              break;
            case 'DateTime':
              // 将日期时间字符串转换为 Date 对象
              uaVariant = UaVariant.dateTimes(showDoubleData.map(v => {
                if (!v) return new Date();
                if (v instanceof Date) return v;
                // 处理字符串格式的日期时间 (YYYY-MM-DD HH:mm:ss)
                const dateValue = new Date(v);
                return isNaN(dateValue.getTime()) ? new Date() : dateValue;
              }));
              break;
            case 'Enumeration':
              // 枚举类型转换为整数数组
              uaVariant = UaVariant.integers(showDoubleData.map(v => parseInt(v) || 0), UaVariantType.Int32);
              break;
            default:
              // 使用 convertToUaVariant 处理单个值，然后转换为数组
              const firstVariant = convertToUaVariant(showDoubleData[0], actualDataType);
              // 对于数组，需要根据第一个值的类型创建数组变体
              if (firstVariant.type === UaVariantType.Double) {
                uaVariant = UaVariant.doubles(showDoubleData.map(v => parseFloat(v) || 0));
              } else if (firstVariant.type === UaVariantType.String) {
                uaVariant = UaVariant.strings(showDoubleData.map(v => String(v || '')));
              } else if (firstVariant.type === UaVariantType.Int32 || firstVariant.type === UaVariantType.Int16 || firstVariant.type === UaVariantType.Int64) {
                uaVariant = UaVariant.integers(showDoubleData.map(v => parseInt(v) || 0), firstVariant.type);
              } else {
                // 默认使用 doubles
                uaVariant = UaVariant.doubles(showDoubleData.map(v => parseFloat(v) || 0));
              }
          }
        } else {
          // 标量类型，使用实际数据类型进行转换
          uaVariant = convertToUaVariant(showDoubleData[0], actualDataType);
        }

        const writeSuccess = await writeValueToOpcUa(uaVariant, actualDataType, '数据写入成功！');
        if (!writeSuccess) {
          return;
        }
      } catch (writeError) {
        console.error('写入 OPC UA 失败:', writeError);
        ElMessage.error('写入 OPC UA 失败: ' + (writeError.message || '未知错误'));
        return;
  }
  
  showDoubleFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
      
    } catch (error) {
      console.error('数据更新失败:', error);
      ElMessage.error('数据更新失败: ' + (error.message || '未知错误'));
    }
  } else {
    showDoubleFlag.value = false;
  }
}, 1000);

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
const handleUIntegerDialogConfirm = useThrottleFn(async () => {
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
  
  // 最终更新当前编辑 item 的值并写入 OPC UA
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
      
      // 调用 writeValues 写入 OPC UA
      const isArray = showUIntegerData.length > 1;
      let uaVariant;
      if (isArray) {
        uaVariant = UaVariant.integers(showUIntegerData.map(v => parseInt(v) || 0), UaVariantType.UInt32);
      } else {
        uaVariant = UaVariant.integer(parseInt(showUIntegerData[0]) || 0, UaVariantType.UInt32);
      }
      
      await writeValueToOpcUa(uaVariant, 'UInteger', '无符号整数写入成功！');
      
    } catch (error) {
      console.error('无符号整数更新失败:', error);
      ElMessage.error('无符号整数更新失败: ' + (error.message || '未知错误'));
      return;
    }
  }
  
  showUIntegerFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
}, 1000);

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
const handleCarExtrasDialogConfirm = useThrottleFn(async () => {
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
      
      // CarExtras 通常是 ExtensionObject，这里使用字符串表示
      // 如果需要写入，可能需要根据实际的数据类型进行转换
      const dataType = currentEditingItem.value?.dataTypes || currentEditingItem.value?.dataType;
      // 尝试将对象转换为字符串进行写入（如果服务器支持）
      try {
        const jsonString = JSON.stringify(result);
        const uaVariant = UaVariant.string(jsonString);
        await writeValueToOpcUa(uaVariant, dataType || 'String', 'CarExtras写入成功！');
      } catch (writeError) {
        // 如果写入失败，仍然更新本地显示
      ElMessage.success('CarExtras更新成功！');
      }
      
    } catch (error) {
      console.error('CarExtras更新失败:', error);
      ElMessage.error('CarExtras更新失败: ' + (error.message || '未知错误'));
      return;
    }
  }
  
  showCarExtrasFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
}, 1000);

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
const handleUInt64DialogConfirm = useThrottleFn(async () => {
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
      
      // 调用 writeValues 写入 OPC UA
      // UInt64 值需要转换为数字
      const numericValue = Number(uint64Value);
      const uaVariant = UaVariant.integer(numericValue, UaVariantType.UInt64);
      await writeValueToOpcUa(uaVariant, 'UInt64', 'UInt64写入成功！');
      
    } catch (error) {
      console.error('UInt64更新失败:', error);
      ElMessage.error('UInt64更新失败: ' + (error.message || '未知错误'));
      return;
    }
  }
  
  showUInt64Flag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
}, 1000);

// 处理 Byte 值变化
const handleByteChange = (index, value) => {
  // 验证输入值是否为有效的字节值 (0-255)
  const numValue = parseInt(value);
  if (isNaN(numValue)) {
    showByteData[index] = '';
    return;
  }
  
  if (numValue < 0) {
    showByteData[index] = '0';
    return;
  }
  
  if (numValue > 255) {
    showByteData[index] = '255';
    return;
  }
  
  showByteData[index] = String(numValue);
  
  // 更新当前编辑 item 的 _editValue
  if (currentEditingItem.value) {
    const byteValues = showByteData.map(v => parseInt(v) || 0);
    currentEditingItem.value._editValue = byteValues.length === 1 ? byteValues[0] : byteValues;
  }
};

// 处理 Byte dialog 确认
const handleByteDialogConfirm = useThrottleFn(async () => {
  // Byte 类型校验：检查每个字节值是否在有效范围内
  let hasError = false;
  const errorMessages = [];
  
  for (let i = 0; i < showByteData.length; i++) {
    const value = showByteData[i];
    const numValue = parseInt(value);
    
    if (!value || value === '') {
      errorMessages.push(`Value ${i + 1}: 请输入字节值`);
      hasError = true;
    } else if (isNaN(numValue)) {
      errorMessages.push(`Value ${i + 1}: 请输入有效的数字`);
      hasError = true;
    } else if (numValue < 0 || numValue > 255) {
      errorMessages.push(`Value ${i + 1}: 字节值必须在0-255范围内`);
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
      // 转换为数字数组
      const byteValues = showByteData.map(v => parseInt(v) || 0);
      const isArray = byteValues.length > 1;
      const finalByteValue = isArray ? byteValues : byteValues[0];
      
      // 更新 _editValue 用于显示
      currentEditingItem.value._editValue = finalByteValue;
      
      // 更新原始 value
      if (Array.isArray(currentEditingItem.value.value)) {
        currentEditingItem.value.value.splice(0, currentEditingItem.value.value.length, ...byteValues);
      } else if (currentEditingItem.value.value && Array.isArray(currentEditingItem.value.value.value)) {
        currentEditingItem.value.value.value.splice(0, currentEditingItem.value.value.value.length, ...byteValues);
      } else {
      if (currentEditingItem.value.value && typeof currentEditingItem.value.value === 'object') {
        if (currentEditingItem.value.value._value !== undefined) {
          currentEditingItem.value.value._value = finalByteValue;
        } else if (currentEditingItem.value.value.value !== undefined) {
          currentEditingItem.value.value.value = finalByteValue;
        }
      } else {
        currentEditingItem.value.value = finalByteValue;
        }
      }
      
      // 调用 writeValues 写入 OPC UA
      let uaVariant;
      if (isArray) {
        uaVariant = UaVariant.integers(byteValues, UaVariantType.Byte);
      } else {
        uaVariant = UaVariant.integer(byteValues[0], UaVariantType.Byte);
      }
      
      await writeValueToOpcUa(uaVariant, 'Byte', 'Byte值写入成功！');
      
    } catch (error) {
      console.error('Byte值更新失败:', error);
      ElMessage.error('Byte值更新失败: ' + (error.message || '未知错误'));
      return;
    }
  }
  
  showByteFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
}, 1000);

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
const handleByteStringDialogConfirm = useThrottleFn(async () => {
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
      
      // 调用 writeValues 写入 OPC UA
      const isArray = byteStringValues.length > 1;
      let uaVariant;
      if (isArray) {
        uaVariant = UaVariant.byteStrings(byteStringValues);
      } else {
        uaVariant = UaVariant.byteString(byteStringValues[0]);
      }
      
      await writeValueToOpcUa(uaVariant, 'ByteString', 'ByteString写入成功！');
      
    } catch (error) {
      console.error('ByteString更新失败:', error);
      ElMessage.error('ByteString更新失败: ' + (error.message || '未知错误'));
      return;
    }
  }
  
  showByteStringFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
}, 1000);

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

const handleDialogFolder = useThrottleFn(() => {
     activeFolder.value = currentNode.value.data.data.NodeId
    FolderDialogVisible.value = false
  state.folderArr.push(currentNode.value.data.data)
}, 1000);

// 确认添加 Document
const handleAddDocumentConfirm = () => {
  // 检查是否已存在 History tab
  const hasHistoryTab = state.nodeDetailsData.some(tab => tab.type === 'Event History');
  
  if (!hasHistoryTab) {
    // 添加 History tab
    state.nodeDetailsData.push({
      key: '3',
      value: 'History',
      type: 'Event History'
    });
    // 切换到 History 选项卡
    nextTick(() => {
      activeFolder.value = '3';
    });
    ElMessage.success('Document added successfully. History tab is now available.');
  } else {
    // 如果已存在，也切换到 History 选项卡
    activeFolder.value = '3';
    ElMessage.success('Document added successfully.');
  }
  
  FolderDialogVisible.value = false;
  documentForm.value = {
    documentType: 'Event View'
  };
};

const formatDateToISO8601Extended = () => {
  const now = new Date();
  const isoString = now.toISOString();
  // 分割毫秒部分并补零至 7 位
  const [dateTime, ms] = isoString.split(/[.,]/);
  const paddedMs = (ms || '000').substring(0, 3).padEnd(7, '0');
  return `${dateTime}.${paddedMs}Z`; // 保留 Z 表示 UTC
};

// 辅助函数：处理节点数据，添加 dataTypes 和 objectType 信息
const processNodeDataItems = (items) => {
  const getDictionaryTypeDisplayName = (dictionaryItem) => {
    if (!dictionaryItem) return null;
    return (
      dictionaryItem?._displayName?._text ||
      dictionaryItem?.displayName?.text ||
      dictionaryItem?.displayName?.Text ||
      dictionaryItem?._browseName ||
      dictionaryItem?.browseName ||
      null
    );
  };
  const currentTypesData = getCurrentDataTypesData();
  const currentObjectTypesData = getCurrentObjectTypesData();
  const currentReferenceTypesData = getCurrentReferenceTypesData();

  // 为每个节点添加dataTypes信息
  items.forEach((item) => {
    let typeDefinitionId = item.typeDefinition?._nodeId?.value;
    if (!typeDefinitionId) {
      item.NodeClassType = nodeClassType[item.nodeClass];
    } else {
      item.NodeClassType = ObjectTypeIds[typeDefinitionId];
    }
    
    item.nodeIdNum = item.nodeId?._nodeId.toString();
    item.typeDefinitionId = item.typeDefinition?._nodeId?.toString()
    // 从allTypesData中查找对应的数据类型信息
    let temp = currentTypesData.filter((idx) => {
      let typeId = idx.nodeId.toString();
      let dataTypeId = item.nodeIdNum;
      return typeId === dataTypeId;
    });
    
    // 正确赋值dataTypes信息
    if (temp[0]) {
      item.dataTypesObj = temp[0];
      item.dataTypes = temp[0]?._parentType?._browseName || temp[0]?._browseName || temp[0]?.browseName;
    } else {
      item.dataTypes = 'Unknown';
      item.dataTypesObj = null;
    }
  });
  
  // 转换为树节点格式
  const slim = items.map(item => {
    const hasChildren = item.hasChildren === undefined ? false : !!item.hasChildren;
    const label = getDisplayName(item) || 'Node';

    return {
      nodeId: item.nodeId,
      nodeIdNum: item.nodeId?._nodeId.toString(),
      typeDefinitionId: item.typeDefinitionId,
      label,
      browseName: item.browseName || item.BrowseName || label,
      BrowseName: item.browseName || item.BrowseName || label,
      displayName: item.displayName || item.DisplayName || { _text: label },
      isLeaf: !hasChildren,
      nodeClass: item.nodeClass,
      NodeClassType: item.nodeClass,
      hasChildren,
      children: [],
      dataTypes: item.dataTypes,
      dataTypesObj: item.dataTypesObj,
      typeDefinition: item.typeDefinition,
      objectType: null,
      objectNodeClass: null,
      objectNodeClassDisplayName: null,
    };
  });
  
  // 为每个节点添加objectType信息和objectNodeClass字段
  slim.forEach((item) => {
    let typeDefinitionId = item.typeDefinition?._nodeId?.toString();
    let objectNodeId = item.nodeId?.toString();
    item.objectType = null;
    item.objectNodeClass = null;
    item.objectNodeClassDisplayName = null;
    // if (!typeDefinitionId) {
    //   return;
    // }

    // 从allObjectTypesData中查找对应的ObjectType信息
    let matchedObjectType = currentObjectTypesData.find((objType) => {
      let objectTypeId = objType.nodeId?.toString();
      return objectTypeId === typeDefinitionId;
    });
    let matchedObjectNodeId = currentObjectTypesData.find((objType) => {
      let objectTypeId = objType.nodeId?.toString();
      return objectTypeId === objectNodeId;
    });
    // 如果直接匹配未找到，尝试在_childTypes中查找
    if (!matchedObjectType) {
      matchedObjectType = currentObjectTypesData.find((objType) => {
        if (!objType._childTypes || !Array.isArray(objType._childTypes)) {
          return false;
        }
        return objType._childTypes.some((childType) => {
          let childTypeId = childType?.nodeId?.toString();
          return childTypeId === typeDefinitionId;
        });
      });
    }

    item.objectType = matchedObjectType || null;
    // console.log(item.objectType,'item.objectType----',item)
    if (item.objectType) {
      item.objectNodeClass = item.objectType._parentType?.nodeids ||
                            item.objectType.nodeids ||
                            null;
      item.objectNodeClassDisplayName = item?.objectType?._parent?._displayName?._text || null;
      // console.log(item,item.objectNodeClassDisplayName,'item.objectNodeClassDisplayName----')
    }
    if (matchedObjectNodeId && !item.objectNodeClassDisplayName) {
      item.objectNodeClassDisplayName = matchedObjectNodeId.displayName?.text || matchedObjectNodeId.displayName?.Text || null;
    }

    // 使用 isSubtypeOf 判断类型，获取正确的图标类型
    if (item.objectType && typeof item.objectType.isSubtypeOf === 'function') {
      try {
        // 定义需要检查的类型名称列表
        const typeDisplayNames = [
          'DigitalTwinType',
          'DigitalTwinRepositoryType',
          'ElementCollectionType',
          'ElementListType',
          'SubmodelType',
          'EventElementType',
          'ReferenceElementType'
        ];

        // 从 allObjectTypesData 中查找对应的类型对象，获取实际的 nodeId
        for (const typeName of typeDisplayNames) {
          const baseType = currentObjectTypesData.find((objType) => {
            const displayName = objType._displayName?._text || objType.displayName?.text || objType._browseName;
            return displayName === typeName;
          });
          if (baseType && baseType.nodeId) {
            if (item.objectType.isSubtypeOf(baseType.nodeId)) {
              item.objectNodeClassDisplayName = typeName;
              break;
            }
          }
        }
      } catch (e) {
        console.warn('isSubtypeOf check failed for objectType:', e);
      }
    }

    if (!item.objectNodeClassDisplayName && item.typeDefinitionId) {
      const matchedTypeDefinitionNode = currentObjectTypesData.find((objType) => {
        return objType.nodeId?.toString?.() === item.typeDefinitionId;
      });
      const matchedReferenceTypeDefinitionNode = currentReferenceTypesData.find((refType) => {
        return refType.nodeId?.toString?.() === item.typeDefinitionId;
      });
      const matchedTypeDefinitionName =
        getDictionaryTypeDisplayName(matchedTypeDefinitionNode) ||
        getDictionaryTypeDisplayName(matchedReferenceTypeDefinitionNode);

      if (matchedTypeDefinitionName) {
        item.objectNodeClassDisplayName = matchedTypeDefinitionName;
      }
    }

    registerBrowseNodeMeta(item);
  });
  console.log(slim,'slim----')
  return slim;
};

const browseNodeMetaMap = ref(new Map());
const treeSelectedNodeMeta = ref(null);
const connectionReferenceTypesMap = ref(new Map());
const connectionObjectTypesMap = ref(new Map());
const connectionDataTypesMap = ref(new Map());

const stringifyNodeId = (nodeIdLike) => {
  if (!nodeIdLike) return '';
  if (typeof nodeIdLike === 'string') return nodeIdLike;
  if (nodeIdLike._nodeId && typeof nodeIdLike._nodeId.toString === 'function') {
    return nodeIdLike._nodeId.toString();
  }
  if (typeof nodeIdLike.toString === 'function') {
    return nodeIdLike.toString();
  }
  return String(nodeIdLike);
};

const getCurrentConnectionKey = () => stringifyNodeId(selectedTopNodeId.value);

const getCurrentReferenceTypesData = () => {
  const connectionKey = getCurrentConnectionKey();
  return connectionReferenceTypesMap.value.get(connectionKey) || state.allReferenceTypesData || [];
};

const getCurrentObjectTypesData = () => {
  const connectionKey = getCurrentConnectionKey();
  return connectionObjectTypesMap.value.get(connectionKey) || state.allObjectTypesData || [];
};

const getCurrentDataTypesData = () => {
  const connectionKey = getCurrentConnectionKey();
  return connectionDataTypesMap.value.get(connectionKey) || state.allTypesData || [];
};

const matchesNodeIdLike = (targetNodeIdLike, candidate) => {
  const targetNodeId = stringifyNodeId(targetNodeIdLike);
  if (!targetNodeId || !candidate) return false;

  const candidateNodeIds = [
    candidate.nodeId,
    candidate.nodeIdNum,
    candidate.NodeId,
  ];

  return candidateNodeIds.some((item) => stringifyNodeId(item) === targetNodeId);
};

const registerBrowseNodeMeta = (nodeMeta) => {
  if (!nodeMeta) return;

  const candidateNodeIds = [
    nodeMeta.nodeId,
    nodeMeta.nodeIdNum,
    nodeMeta.NodeId,
    nodeMeta?.data?.nodeId,
    nodeMeta?.data?.nodeIdNum,
    nodeMeta?.data?.NodeId,
  ];

  const nodeIdKey = candidateNodeIds.map((item) => stringifyNodeId(item)).find(Boolean);
  if (!nodeIdKey) return;

  const existingMeta = browseNodeMetaMap.value.get(nodeIdKey);
  if (!existingMeta) {
    browseNodeMetaMap.value.set(nodeIdKey, nodeMeta);
    return;
  }

  const mergedMeta = {
    ...existingMeta,
    ...nodeMeta,
    data: {
      ...(existingMeta?.data || {}),
      ...(nodeMeta?.data || {}),
    },
  };

  const existingDisplayName =
    existingMeta?.objectNodeClassDisplayName ||
    existingMeta?.data?.objectNodeClassDisplayName;
  const incomingDisplayName =
    nodeMeta?.objectNodeClassDisplayName ||
    nodeMeta?.data?.objectNodeClassDisplayName;

  if (existingDisplayName && !incomingDisplayName) {
    mergedMeta.objectNodeClassDisplayName = existingMeta.objectNodeClassDisplayName;
    if (existingMeta?.data?.objectNodeClassDisplayName) {
      mergedMeta.data.objectNodeClassDisplayName = existingMeta.data.objectNodeClassDisplayName;
    }
  }

  const existingTypeDefinitionId =
    existingMeta?.typeDefinitionId ||
    existingMeta?.data?.typeDefinitionId;
  const incomingTypeDefinitionId =
    nodeMeta?.typeDefinitionId ||
    nodeMeta?.data?.typeDefinitionId;

  if (existingTypeDefinitionId && !incomingTypeDefinitionId) {
    mergedMeta.typeDefinitionId = existingMeta.typeDefinitionId;
    if (existingMeta?.data?.typeDefinitionId) {
      mergedMeta.data.typeDefinitionId = existingMeta.data.typeDefinitionId;
    }
  }

  browseNodeMetaMap.value.set(nodeIdKey, mergedMeta);
};

const setTreeSelectedNodeMeta = (nodeMeta) => {
  if (!nodeMeta) return;
  treeSelectedNodeMeta.value = nodeMeta;
  registerBrowseNodeMeta(nodeMeta);
};

const resolveBrowseNodeMeta = (nodeIdLike, preferredMeta = null) => {
  if (preferredMeta) {
    registerBrowseNodeMeta(preferredMeta);
    return preferredMeta;
  }

  const nodeIdKey = stringifyNodeId(nodeIdLike);
  if (nodeIdKey && browseNodeMetaMap.value.has(nodeIdKey)) {
    return browseNodeMetaMap.value.get(nodeIdKey);
  }

  const candidates = [
    treeSelectedNodeMeta.value,
    selectNodeData.value,
    targetNode.value,
    nodeDetails,
    selectedNodeId.value,
  ];

  const matchedCandidate = candidates.find((candidate) => matchesNodeIdLike(nodeIdLike, candidate)) || preferredMeta;
  if (matchedCandidate) {
    registerBrowseNodeMeta(matchedCandidate);
  }
  return matchedCandidate;
};

const isReferenceElementTypeNode = (nodeMeta) => {
  if (!nodeMeta) return false;

  const displayName =
    nodeMeta.objectNodeClassDisplayName ||
    nodeMeta?.data?.objectNodeClassDisplayName ||
    '';

  return displayName === 'ReferenceElementType';
};

const getResolvedTypeDefinitionId = (nodeMeta) => {
  if (!nodeMeta) return '';
  return (
    nodeMeta.typeDefinitionId ||
    nodeMeta?.data?.typeDefinitionId ||
    nodeMeta?.typeDefinition?._nodeId?.toString?.() ||
    nodeMeta?.data?.typeDefinition?._nodeId?.toString?.() ||
    ''
  );
};

const shouldUseObjectOnlyNodeClassMask = (nodeMeta) => {
  if (!nodeMeta) return false;

  const typeDefinitionId = getResolvedTypeDefinitionId(nodeMeta);
  const objectNodeClassDisplayName =
    nodeMeta.objectNodeClassDisplayName ||
    nodeMeta?.data?.objectNodeClassDisplayName ||
    '';

  return (
    Boolean(typeDefinitionId) &&
    /Reference/i.test(typeDefinitionId) &&
    objectNodeClassDisplayName === 'ReferenceElementType'
  );
};

const normalizeBrowseNodeClassMask = (nodeIdLike, defaultMask, nodeMeta = null) => {
  const resolvedNodeMeta = resolveBrowseNodeMeta(nodeIdLike, nodeMeta);
  if (shouldUseObjectOnlyNodeClassMask(resolvedNodeMeta)) {
    console.warn('[BrowseMask] force NodeClassMask=Object because typeDefinitionId contains Reference and node is ReferenceElementType', {
      nodeId: stringifyNodeId(nodeIdLike),
      typeDefinitionId: getResolvedTypeDefinitionId(resolvedNodeMeta),
      objectNodeClassDisplayName:
        resolvedNodeMeta?.objectNodeClassDisplayName ||
        resolvedNodeMeta?.data?.objectNodeClassDisplayName,
    });
    return Number(NodeClass.Object);
  }
  return Number(
    NodeClass.Object
    | NodeClass.Method
    | NodeClass.ObjectType
    | NodeClass.VariableType
    | NodeClass.DataType
    | NodeClass.ReferenceType
  );
};

const normalizeBrowseReferenceTypeId = (nodeIdLike, defaultReferenceTypeId) => {
  return defaultReferenceTypeId;
};

const loadNode =  (node, resolve) => { 
  let nodeClassToReturn = 'Object'; 
  let id = node.data.nodeId?._nodeId?._value;
  let nodeid2 = node.data.nodeId;
  const nodeLevel = node.level || 0;
  
  // 确保 nodeIdNum 正确获取
  let nodeIdNum = node.data.nodeIdNum;
  if (!nodeIdNum && node.data.nodeId) {
    if (node.data.nodeId._nodeId) {
      nodeIdNum = node.data.nodeId._nodeId.toString();
    } else if (typeof node.data.nodeId.toString === 'function') {
      nodeIdNum = node.data.nodeId.toString();
    } else {
      nodeIdNum = String(node.data.nodeId);
    }
  }
  
  fixedNodeId.value = id;
  ensureFixedNodeExpanded();
  selectedNodeId.value = nodeid2;
  const currentNodeIdStr = nodeIdNum || node.data?.nodeId?._nodeId?.toString?.() || node.data?.nodeId?.toString?.() || '';
  const typeDefinitionIdStr =
    node.data?.typeDefinitionId ||
    node.data?.typeDefinition?._nodeId?.toString?.() ||
    node.data?.typeDefinition?.toString?.() ||
    '';
  const isReferenceElementTypeDefinitionNode = currentNodeIdStr === 'ns=1;i=14';
  const isReferenceElementTypeInstanceNode =
    currentNodeIdStr !== 'ns=1;i=14' &&
    node.data.objectNodeClassDisplayName === 'ReferenceElementType';
  const isReferenceOnlyObjectNode =
    (node.data?.BrowseName === 'Reference' ||
      node.data?.browseName === 'Reference' ||
      node.data?.label === 'Reference') &&
    Number(node.data?.nodeClass) === NodeClass.Object &&
    /ReferenceElementTestType/i.test(typeDefinitionIdStr);
  const isEmployeesDebugNode =
    currentNodeIdStr === 'ns=2;b=eyJvaSI6eyJpIjoiMiIsImlkIjoibnM9MjtzPURlcGFydG1lbnRUeXBlLUVtcGxveWVlcyJ9fQ==' ||
    node.data?.label === 'Employees';
  const isLeafDebugNode =
    currentNodeIdStr === 'i=2253' ||
    currentNodeIdStr === 'ns=2;b=eyJvaSI6eyJpIjoiMSIsImlkIjoibnM9MjtzPUVtcGxveWVlRGlnaXRhbFR3aW5UeXBlLVBlcnNvbmFsRGF0YSJ9fQ==' ||
    node.data?.label === 'Server' ||
    node.data?.label === 'PersonalData' ||
    node.data?.browseName === 'Server' ||
    node.data?.browseName === 'PersonalData';
  const isKnownLeafNode =
    currentNodeIdStr === 'i=2253' ||
    currentNodeIdStr === 'ns=2;b=eyJvaSI6eyJpIjoiMSIsImlkIjoibnM9MjtzPUVtcGxveWVlRGlnaXRhbFR3aW5UeXBlLVBlcnNvbmFsRGF0YSJ9fQ==' ||
    (
      (node.data?.label === 'Server' || node.data?.browseName === 'Server') &&
      node.data?.typeDefinitionId === 'i=2004'
    ) ||
    (
      (node.data?.label === 'PersonalData' || node.data?.browseName === 'PersonalData') &&
      node.data?.typeDefinitionId === 'ns=2;s=PersonalDataSubmodelType'
    );
  const fallbackNodeClassMask = normalizeBrowseNodeClassMask(currentNodeIdStr, Number(
    NodeClass.Object
    | NodeClass.Variable
    | NodeClass.ObjectType
    | NodeClass.VariableType
  ), node.data);

  const fallbackToDefaultBrowse = async () => {
    if (isEmployeesDebugNode) {
      console.warn('[Employees Debug] fallbackToDefaultBrowse:start', {
        currentNodeIdStr,
        label: node.data?.label,
        objectNodeClass: node.data?.objectNodeClass,
        objectNodeClassDisplayName: node.data?.objectNodeClassDisplayName,
      });
    }

    const firstRes = await getBrowseDatas(fallbackNodeClassMask, 10, nodeid2);
    if (isEmployeesDebugNode) {
      console.warn('[Employees Debug] fallbackToDefaultBrowse:firstRes', {
        resultsLength: firstRes?.results?.length || 0,
        continuationPoint: firstRes?.continuationPoint || null,
      });
    }

    const allArr = firstRes?.results || [];
    const slim = processNodeDataItems(allArr);

    if (isEmployeesDebugNode) {
      console.warn('[Employees Debug] fallbackToDefaultBrowse:processed', {
        allArrLength: allArr.length,
        slimLength: slim.length,
      });
    }

    resolve(slim);
  };

  if (isEmployeesDebugNode) {
    console.warn('[Employees Debug] loadNode triggered', {
      currentNodeIdStr,
      browseName: node.data?.browseName,
      label: node.data?.label,
      objectNodeClass: node.data?.objectNodeClass,
      objectNodeClassDisplayName: node.data?.objectNodeClassDisplayName,
      nodeLevel,
      nodeData: node.data,
    });
  }
  if (isLeafDebugNode) {
    console.warn('[Leaf Debug] loadNode triggered', {
      currentNodeIdStr,
      label: node.data?.label,
      browseName: node.data?.browseName,
      isLeaf: node.data?.isLeaf,
      hasChildren: node.data?.hasChildren,
      typeDefinitionId: node.data?.typeDefinitionId,
      objectNodeClass: node.data?.objectNodeClass,
      objectNodeClassDisplayName: node.data?.objectNodeClassDisplayName,
      nodeData: node.data,
    });
  }

  if (isKnownLeafNode) {
    if (isLeafDebugNode) {
      console.warn('[Leaf Debug] skip loadNode for leaf-like node', {
        currentNodeIdStr,
        label: node.data?.label,
        isLeaf: node.data?.isLeaf,
        hasChildren: node.data?.hasChildren,
        typeDefinitionId: node.data?.typeDefinitionId,
        isKnownLeafNode,
      });
    }
    resolve([]);
    return;
  }
  // let idx =  node.data.isSubtypeOf(node.data.nodeId)
  // 当 objectNodeClassDisplayName 为 'ReferenceElementType' 时，调用 browseReference 获取引用
  if (isReferenceElementTypeDefinitionNode) {
    // 获取URL配置
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;

    if (!url) {
      console.warn('No URL configuration found for ReferenceElementType');
      resolve([]);
      return;
    }

    // 创建客户端配置
    const apiConfig = new Configuration({
      basePath: url
    });
    const clientConfig = new UaClientConfiguration(apiConfig);
    const testOpcServer = new UaWebClient(clientConfig);

    let browseHierarchy = ReferenceTypeIds?.HierarchicalReferences ? `i=${ReferenceTypeIds.HierarchicalReferences}` : 'i=33';
    const browseDirection = BrowseDirection?.Forward ?? 0;

    // ReferenceElementType 下通常返回 HasSubtype 指向的类型节点，需包含 ObjectType / VariableType
    let nodeClassMask = Number(
      NodeClass.Object
      | NodeClass.Variable
      | NodeClass.ObjectType
      | NodeClass.VariableType
    );

    // 解析节点ID
    let targetNodeId = nodeid2;
    if (!targetNodeId) {
      if (nodeIdNum) {
        targetNodeId = UaNodeId.parse(nodeIdNum);
      } else {
        targetNodeId = node.data.nodeId;
      }
    }
    browseHierarchy = normalizeBrowseReferenceTypeId(targetNodeId, browseHierarchy);
    nodeClassMask = normalizeBrowseNodeClassMask(targetNodeId, nodeClassMask, node.data);

    // 调用 browseReference 方法获取引用
    testOpcServer.browseReference(
      targetNodeId,
      nodeClassMask,
      browseDirection,
      browseHierarchy,
      100
    ).then((browseResult) => {
      // 处理返回结果，转换为树节点格式
      let referenceResults = [];
      if (browseResult && browseResult.results && browseResult.results.length > 0) {
        referenceResults = browseResult.results.map(ref => {
          // 解析节点ID
          let refNodeId = ref.nodeId || ref.NodeId;
          if (typeof refNodeId === 'string') {
            refNodeId = UaNodeId.parse(refNodeId);
          }

          return {
            nodeId: refNodeId,
            browseName: ref.browseName || ref.BrowseName || '',
            displayName: ref.displayName || ref.DisplayName || { text: '', Text: '' },
            nodeClass: ref.nodeClass || ref.NodeClass || 0,
            typeDefinition: ref.typeDefinition || ref.TypeDefinition || null,
            hasChildren: ref.hasChildren !== undefined ? ref.hasChildren : true,
          };
        });
      }

      const slim = processNodeDataItems(referenceResults);
      if (slim.length === 0) {
        fallbackToDefaultBrowse().catch((fallbackError) => {
          console.error('Fallback browse failed for ReferenceElementType definition:', fallbackError);
          ElMessage.warning('加载引用失败: ' + (fallbackError.message || '未知错误'));
          resolve([]);
        });
        return;
      }
      resolve(slim);
    }).catch((error) => {
      console.error('Error loading ReferenceElementType references:', error);
      fallbackToDefaultBrowse().catch((fallbackError) => {
        console.error('Fallback browse failed after ReferenceElementType definition error:', fallbackError);
        ElMessage.warning('加载引用失败: ' + ((fallbackError && fallbackError.message) || error.message || '未知错误'));
        resolve([]);
      });
    });

    return; // 提前返回，避免执行后续的 getBrowseDatas
  }

  // 当 objectNodeClass 为 'ns=1;i=14' 时，调用 browseReference 获取 Nonhierarchy 引用
  // console.log(node,'node.data.objectNodeClass----')
  if (isReferenceElementTypeInstanceNode) {
    if (isEmployeesDebugNode) {
      console.warn('[Employees Debug] instance branch entered', {
        currentNodeIdStr,
        objectNodeClass: node.data?.objectNodeClass,
        objectNodeClassDisplayName: node.data?.objectNodeClassDisplayName,
      });
    }
    // 获取URL配置
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;
    
    if (!url) {
      console.warn('No URL configuration found for browseReference');
      resolve([]);
      return;
    }
    
    // 创建客户端配置
    const apiConfig = new Configuration({
      basePath: url
    });
    const clientConfig = new UaClientConfiguration(apiConfig);
    const testOpcServer = new UaWebClient(clientConfig);
    
    // 设置 Nonhierarchy 参数
    let browseHierarchy = ReferenceTypeIds?.NonHierarchicalReferences ? `i=${ReferenceTypeIds.NonHierarchicalReferences}` : 'i=32';
    const browseDirection = BrowseDirection?.Forward ?? 0;
    
    // 特定 Reference 测试节点只需要返回 Object，其他节点保持原有过滤逻辑。
    let nodeClassMask = isReferenceOnlyObjectNode
      ? Number(NodeClass.Object)
      : Number(
        NodeClass.Object
        | NodeClass.Variable
        | NodeClass.ObjectType
        | NodeClass.VariableType
      );
    
    // 解析节点ID
    let targetNodeId = nodeid2;
    if (!targetNodeId) {
      if (nodeIdNum) {
        targetNodeId = UaNodeId.parse(nodeIdNum);
      } else {
        targetNodeId = node.data.nodeId;
      }
    }
    browseHierarchy = normalizeBrowseReferenceTypeId(targetNodeId, browseHierarchy);
    nodeClassMask = normalizeBrowseNodeClassMask(targetNodeId, nodeClassMask, node.data);
    
    // 调用 browseReference 方法，使用 .then() 处理 Promise
    testOpcServer.browseReference(
      targetNodeId, 
      nodeClassMask,
      browseDirection,
      browseHierarchy, 
      10
    ).then((browseResult) => {
      // 处理返回结果，转换为树节点格式
      let referenceResults = [];
      if (browseResult && browseResult.results && browseResult.results.length > 0) {
        referenceResults = browseResult.results.map(ref => {
          // 解析节点ID
          let refNodeId = ref.nodeId || ref.NodeId;
          if (typeof refNodeId === 'string') {
            refNodeId = UaNodeId.parse(refNodeId);
          }
          
          return {
            nodeId: refNodeId,
            browseName: ref.browseName || ref.BrowseName || '',
            displayName: ref.displayName || ref.DisplayName || { text: '', Text: '' },
            nodeClass: ref.nodeClass || ref.NodeClass || 0,
            typeDefinition: ref.typeDefinition || ref.TypeDefinition || null,
            hasChildren: false, // 非层次引用默认无子节点
          };
        });
      }
      
      if (isEmployeesDebugNode) {
        console.warn('[Employees Debug] instance browseReference result', {
          browseResultResultsLength: browseResult?.results?.length || 0,
          referenceResultsLength: referenceResults.length,
        });
      }

      const slim = processNodeDataItems(referenceResults);
      if (slim.length === 0) {
        if (isEmployeesDebugNode) {
          console.warn('[Employees Debug] instance browseReference empty, fallback to default browse');
        }
        fallbackToDefaultBrowse().catch((fallbackError) => {
          console.error('Fallback browse failed for ReferenceElementType instance:', fallbackError);
          ElMessage.warning('加载非层次引用失败: ' + (fallbackError.message || '未知错误'));
          resolve([]);
        });
        return;
      }
      resolve(slim);
    }).catch((error) => {
      console.error('Error loading Nonhierarchy references:', error);
      fallbackToDefaultBrowse().catch((fallbackError) => {
        console.error('Fallback browse failed after Nonhierarchy reference error:', fallbackError);
        ElMessage.warning('加载非层次引用失败: ' + ((fallbackError && fallbackError.message) || error.message || '未知错误'));
        resolve([]);
      });
    });
    
    return; // 提前返回，避免执行后续的 getBrowseDatas
  }
  
  // 当不满足条件时，调用 getBrowseDatas
  getBrowseDatas(normalizeBrowseNodeClassMask(nodeid2 || currentNodeIdStr, undefined, node.data), 10, null, node.data).then(async (firstRes) => {
    let allArr = firstRes?.results || []; // 初始化累计数组
    let continuationPoint = firstRes?.continuationPoint;
    
    // 如果是非一级节点（level != 1），只加载第一页，保存 continuationPoint
    if (nodeLevel !== 1) {
      // 保存该节点的 continuationPoint
      if (continuationPoint && nodeIdNum) {
        nodeContinuationPoints.value.set(nodeIdNum, continuationPoint);
      } else if (nodeIdNum) {
        nodeContinuationPoints.value.delete(nodeIdNum);
      }
    } else {
      // 一级节点，循环获取后续页数据（保持原有逻辑）
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
    }
    
    // 使用公共函数处理数据
    const slim = processNodeDataItems(allArr);
      // 如果是非一级节点且有 continuationPoint，添加一个特殊的加载更多节点
      if (nodeLevel !== 1) {
        
        // 先移除可能存在的旧加载更多节点（防止重复）
        const existingLoadMoreIndex = slim.findIndex(
          item => item.isLoadMore && item.parentNodeId === nodeIdNum
        );
        if (existingLoadMoreIndex !== -1) {
          slim.splice(existingLoadMoreIndex, 1);
        }
        
        // 只有当 continuationPoint 存在时才添加加载更多节点
        if (continuationPoint && nodeIdNum) {
          const loadMoreNodeId = `__load_more_${nodeIdNum}`;
          slim.push({
            nodeId: null,
            nodeIdNum: loadMoreNodeId,
            label: '...',
            isLeaf: true,
            nodeClass: -1, // 特殊标记
            NodeClassType: -1,
            hasChildren: false,
            children: [],
            isLoadMore: true, // 标记为加载更多按钮
            parentNodeId: nodeIdNum,
            className: 'load-more-tree-node', // 添加类名用于 CSS 选择器
          });
          
          // 在节点渲染后隐藏展开图标
          nextTick(() => {
            // 使用多种方法尝试查找并隐藏展开图标
            const loadMoreTreeNode = document.querySelector(`.optimized-tree .el-tree-node[data-key="${loadMoreNodeId}"]`);
            if (loadMoreTreeNode) {
              const expandIcon = loadMoreTreeNode.querySelector('.el-tree-node__expand-icon');
              if (expandIcon) {
                expandIcon.style.display = 'none';
                expandIcon.style.opacity = '0';
                expandIcon.style.visibility = 'hidden';
                expandIcon.style.width = '0';
                expandIcon.style.height = '0';
                expandIcon.style.minWidth = '0';
                expandIcon.style.minHeight = '0';
                expandIcon.style.margin = '0';
                expandIcon.style.padding = '0';
              }
            } else {
              // 如果第一次查找失败，延迟再试一次（等待 DOM 完全渲染）
              setTimeout(() => {
                const retryNode = document.querySelector(`.optimized-tree .el-tree-node[data-key="${loadMoreNodeId}"]`);
                if (retryNode) {
                  const retryExpandIcon = retryNode.querySelector('.el-tree-node__expand-icon');
                  if (retryExpandIcon) {
                    retryExpandIcon.style.display = 'none';
                    retryExpandIcon.style.opacity = '0';
                    retryExpandIcon.style.visibility = 'hidden';
                    retryExpandIcon.style.width = '0';
                    retryExpandIcon.style.height = '0';
                    retryExpandIcon.style.minWidth = '0';
                    retryExpandIcon.style.minHeight = '0';
                    retryExpandIcon.style.margin = '0';
                    retryExpandIcon.style.padding = '0';
                  }
                }
              }, 100);
            }
          });
        } else {
          // 确保没有 continuationPoint 时，移除 Map 中的记录
          if (nodeIdNum) {
            nodeContinuationPoints.value.delete(nodeIdNum);
          }
        }
      }

      resolve(slim);              // 传给 el-tree，秒级完成
  });

  
};

// 加载二级节点的下一页数据
const loadNextPageForNode = async (parentNodeId) => {
  const continuationPoint = nodeContinuationPoints.value.get(parentNodeId);
  
  if (!continuationPoint) {
    ElMessage.info('没有更多数据了');
    return;
  }

  try {
    // 找到父节点，设置正确的上下文
    const tree = eltree.value;
    if (!tree) return;
    
    const parentNode = tree.getNode(parentNodeId);
    if (!parentNode || !parentNode.data) {
      ElMessage.error('找不到父节点');
      return;
    }
    
    // 判断节点的 objectNodeClass，如果是 'ns=1;i=14'，则不支持加载更多
    if (parentNode.data.objectNodeClass == 'ns=1;i=14') {
      ElMessage.info('非层次引用不支持分页加载');
      return;
    }
    
    // 获取正确的URL
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;
    
    if (!url) {
      ElMessage.error('请先配置服务器连接');
      return;
    }
    
    // 创建 OPC 服务器实例
    const apiConfig = new Configuration({
      basePath: url
    });
    const clientConfig = new UaClientConfiguration(apiConfig);
    const testOpcServer = new UaWebClient(clientConfig);
    
    // 使用 browseNextByCP 加载下一页
    const nextResult = await testOpcServer.browseNextByCP(continuationPoint, false);
    
    if (!nextResult || !nextResult.results || nextResult.results.length === 0) {
      // 没有更多数据，移除 continuationPoint
      nodeContinuationPoints.value.delete(parentNodeId);
      // 移除加载更多按钮
      removeLoadMoreNode(parentNodeId);
      return;
    }

    // 处理新数据
    const newResults = nextResult.results || [];
    
    // 使用公共函数处理数据
    const regularNodes = processNodeDataItems(newResults);

    // 更新 continuationPoint（使用小写字段名）
    const newContinuationPoint = nextResult?.continuationPoint;
    let loadMoreNode = null;
    
    if (newContinuationPoint) {
      nodeContinuationPoints.value.set(parentNodeId, newContinuationPoint);
      // 创建加载更多节点（但不立即添加到数组）
      loadMoreNode = {
        nodeId: null,
        nodeIdNum: `__load_more_${parentNodeId}`,
        label: '...',
        isLeaf: true,
        nodeClass: -1,
        NodeClassType: -1,
        hasChildren: false,
        children: [],
        isLoadMore: true,
        parentNodeId: parentNodeId,
        className: 'load-more-tree-node' // 添加类名用于 CSS 选择器
      };
    } else {
      // 没有更多数据，移除 continuationPoint 和加载更多节点
      nodeContinuationPoints.value.delete(parentNodeId);
      // 显式移除加载更多节点
      removeLoadMoreNode(parentNodeId);
    }
    
    // 确保加载更多节点在最后
    const newNodes = loadMoreNode ? [...regularNodes, loadMoreNode] : regularNodes;

    // 找到父节点并添加新节点（addNodesToParent 内部会处理移除旧的加载更多节点）
    addNodesToParent(parentNodeId, newNodes);
    
  } catch (error) {
    console.error('加载下一页失败:', error);
    ElMessage.error('加载下一页失败: ' + (error.message || '未知错误'));
  }
};

// 移除加载更多节点（移除所有匹配的加载更多节点）
const removeLoadMoreNode = (parentNodeId) => {
  const tree = eltree.value;
  if (!tree) return;
  
  // 找到父节点
  const parentNode = tree.getNode(parentNodeId);
  if (!parentNode) return;
  
  let removedCount = 0;
  
  // 从 childNodes 中移除所有加载更多节点
  if (parentNode.childNodes) {
    const initialLength = parentNode.childNodes.length;
    parentNode.childNodes = parentNode.childNodes.filter(
      child => !(child.data?.isLoadMore && child.data?.parentNodeId === parentNodeId)
    );
    removedCount = initialLength - parentNode.childNodes.length;
  }
  
  // 从 data.children 中移除所有加载更多节点
  if (parentNode.data && parentNode.data.children) {
    const initialDataLength = parentNode.data.children.length;
    parentNode.data.children = parentNode.data.children.filter(
      child => !(child.isLoadMore && child.parentNodeId === parentNodeId)
    );
    const removedDataCount = initialDataLength - parentNode.data.children.length;
    if (removedDataCount > 0) {
      console.log(`从 data.children 中移除了 ${removedDataCount} 个加载更多节点`);
    }
  }
  
  if (removedCount > 0) {
    console.log(`从 childNodes 中移除了 ${removedCount} 个加载更多节点`);
  }
};

// 添加新节点到父节点
const addNodesToParent = (parentNodeId, newNodes) => {
  const tree = eltree.value;
  if (!tree) {
    console.error('树实例不存在');
    return;
  }
  
  // 找到父节点
  const parentNode = tree.getNode(parentNodeId);
  if (!parentNode) {
    console.error(`找不到父节点: ${parentNodeId}`);
    return;
  }
  
  // 分离普通节点和加载更多节点
  const regularNodes = newNodes.filter(n => !n.isLoadMore);
  const loadMoreNode = newNodes.find(n => n.isLoadMore);
  
  
  // 确保 parentNode.data.children 存在
  if (!parentNode.data.children) {
    parentNode.data.children = [];
  }
  
  // 获取现有的普通节点（从 childNodes 中获取，这是 Element Plus 树组件的实际子节点存储位置）
  let existingRegularNodes = [];
  if (parentNode.childNodes && parentNode.childNodes.length > 0) {
    // 从 childNodes 中提取 data，排除加载更多节点
    existingRegularNodes = parentNode.childNodes
      .map(childNode => childNode.data)
      .filter(childData => childData && !childData.isLoadMore);
  } else if (parentNode.data && parentNode.data.children && parentNode.data.children.length > 0) {
    // 如果 childNodes 不存在，则从 data.children 中获取（备用方案）
    existingRegularNodes = parentNode.data.children.filter(
      child => child && !child.isLoadMore
    );
  } else {
    console.log('未找到现有节点，childNodes 和 data.children 都为空');
  }
  console.log(existingRegularNodes,'existingRegularNodes')
  // 构建最终的子节点数组：现有普通节点 + 新普通节点 + 加载更多节点（如果有）
  const finalChildren = [...existingRegularNodes, ...regularNodes];
  if (loadMoreNode) {
    finalChildren.push(loadMoreNode);
  }
  // 直接替换整个 children 数组，确保顺序正确
  parentNode.data.children = finalChildren;
  
  
  // 强制更新树视图
  nextTick(() => {
    // 确保父节点是展开的
    if (parentNode && !parentNode.expanded) {
      parentNode.expanded = true;
    }
    
    // 重新获取父节点，确保获取最新的引用
    const updatedParentNode = tree.getNode(parentNodeId);
    if (!updatedParentNode) return;
    
    // 再次确保顺序正确：移除所有加载更多节点，然后重新添加
    if (updatedParentNode.data && updatedParentNode.data.children) {
      const currentChildren = [...updatedParentNode.data.children];
      const regularChildren = currentChildren.filter(
        child => !child.isLoadMore
      );
      
      // 重新构建数组：先普通节点，最后是加载更多节点
      let newChildrenArray;
      if (loadMoreNode) {
        newChildrenArray = [...regularChildren, loadMoreNode];
      } else {
        newChildrenArray = regularChildren;
      }
      
      // 直接替换整个数组
      updatedParentNode.data.children = newChildrenArray;
      
      // 强制触发 Vue 响应式更新 - 创建新数组引用
      updatedParentNode.data.children = [...newChildrenArray];
      
    }
    
    // 更新 childNodes（Element Plus tree 内部结构）
    nextTick(() => {
      const finalParentNode = tree.getNode(parentNodeId);
      if (!finalParentNode || !finalParentNode.childNodes) return;
      
      // 获取当前的 childNodes
      const currentChildNodes = Array.from(finalParentNode.childNodes);
      
      // 分离普通节点和加载更多节点
      const regularChildNodes = currentChildNodes.filter(
        cn => !(cn.data?.isLoadMore && cn.data?.parentNodeId === parentNodeId)
      );
      
      // 查找加载更多节点
      const loadMoreChildNodes = currentChildNodes.filter(
        cn => cn.data?.isLoadMore && cn.data?.parentNodeId === parentNodeId
      );
      
      // 重新构建 childNodes 数组：先普通节点，最后是加载更多节点（只保留一个）
      let newChildNodesArray;
      if (loadMoreNode && loadMoreChildNodes.length > 0) {
        // 如果有加载更多节点，使用第一个（应该只有一个）
        newChildNodesArray = [...regularChildNodes, loadMoreChildNodes[0]];
      } else if (loadMoreNode && loadMoreChildNodes.length === 0) {
        // 如果 data.children 中有加载更多节点，但 childNodes 中还没有，等待 tree 自动创建
        newChildNodesArray = regularChildNodes;
      } else {
        // 没有加载更多节点
        newChildNodesArray = regularChildNodes;
      }
      
      // 更新 childNodes
      finalParentNode.childNodes.splice(0, finalParentNode.childNodes.length, ...newChildNodesArray);
      
      // 隐藏加载更多节点的展开图标
      if (loadMoreNode) {
        nextTick(() => {
          const loadMoreNodeId = loadMoreNode.nodeIdNum;
          const loadMoreTreeNode = document.querySelector(`.optimized-tree .el-tree-node[data-key="${loadMoreNodeId}"]`);
          if (loadMoreTreeNode) {
            const expandIcon = loadMoreTreeNode.querySelector('.el-tree-node__expand-icon');
            if (expandIcon) {
              expandIcon.style.display = 'none';
              expandIcon.style.opacity = '0';
              expandIcon.style.visibility = 'hidden';
              expandIcon.style.width = '0';
              expandIcon.style.height = '0';
              console.log(`✓ 已隐藏加载更多节点的展开图标`);
            }
          }
        });
      }
    });
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
  const nodeClassToReturn = getNodeClassFilter(nodeLevel, node.data);
  
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
const getNodeClassFilter = (level, nodeData = null) => {
  if (isReferenceElementTypeNode(nodeData)) {
    return Number(NodeClass.Object);
  }

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
      const label = getDisplayName(item) || `Node ${index}`;
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
    const label = getDisplayName(item) || `Node ${index}`;
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
    const label = getDisplayName(item) || `Node_${globalIndex}`;
    
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
    const nodeClassToReturn = getNodeClassFilter(nodeLevel, node.data);
    
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
    const nodeClassToReturn = getNodeClassFilter(nodeLevel, node.data);
    
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

const getBrowseDatas = async (passNodeClass, pageSize = 10, overrideNodeId = null, browseNodeMeta = null) => {
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
    const validNodeIdStr =
      validNodeId && typeof validNodeId.toString === 'function'
        ? validNodeId.toString()
        : String(validNodeId || '');
    
    let nodeClassToReturn = 0;
    // 默认浏览保持与原始请求一致，使用 253：
    // Object | Method | ObjectType | VariableType | ReferenceType | DataType | View
    // Variable 只在特殊引用节点分支里单独放开，避免普通浏览返回过多节点。
    nodeClassToReturn = Number( 
      NodeClass.Object 
      | NodeClass.Method
      | NodeClass.ObjectType 
      | NodeClass.VariableType
      | NodeClass.ReferenceType
      | NodeClass.DataType
      | NodeClass.View
   );
   if(passNodeClass){
    nodeClassToReturn = passNodeClass
   }
    nodeClassToReturn = normalizeBrowseNodeClassMask(validNodeIdStr, nodeClassToReturn, browseNodeMeta);
    const controller = new AbortController();
    try{
      const fetchApi = (input ,init ) => fetch(input, { ...init, signal: controller.signal });
      const currentUrl = allData?.url || urlForm.value.url || "http://localhost:4840";
      let apiConfig2 = new Configuration({
          basePath: currentUrl,
          fetchApi
        } );
      let clientConfig = new UaClientConfiguration(apiConfig2);
      let testOpcServer = new UaWebClient(clientConfig);

      const result = await withTimeout(
        testOpcServer.browseChild(validNodeId, nodeClassToReturn, pageSize),
        10000,
        '请求超时',
        controller
      );

      return result;
    }catch(err){
      // 超时中断
      if (err.name === 'AbortError' || err.message?.includes('超时')) {
        ElMessage.error(`连接超时，请检查服务器 ${allData?.url || urlForm.value.url} 是否可用`);
      } else if (err instanceof TypeError && err.message?.includes('fetch')) {
        ElMessage.error('网络错误，无法连接到服务器');
      } else if (err.status) {
        ElMessage.error(`服务器响应错误 (${err.status})`);
      } else {
        ElMessage.error('连接失败: ' + (err.message || '未知错误'));
      }
      console.error('[OPC UA] getBrowseDatas error:', err);
      return { results: [], ContinuationPoint: null };
    }
    
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
  
  const controller = new AbortController();
  const fetchApi = (input, init) => fetch(input, { ...init, signal: controller.signal });
  let apiConfig = new Configuration({
    basePath: url,
    fetchApi
  });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);

  try {
    let nodeClassToReturn = Number(
       NodeClass.Object
      | NodeClass.ObjectType
      | NodeClass.Method
      | NodeClass.DataType
      | NodeClass.ReferenceType
      | NodeClass.Unspecified
      | NodeClass.View
    );
    nodeClassToReturn = normalizeBrowseNodeClassMask(
      validNodeId,
      nodeClassToReturn,
      selectNodeData.value || nodeDetails
    );
    const result = await withTimeout(
      testOpcServer.browseChild(validNodeId, nodeClassToReturn, 20),
      10000,
      '请求超时',
      controller
    );
    return result;
  } catch (err) {
    if (err.name === 'AbortError' || err.message?.includes('超时')) {
      ElMessage.error(`连接超时，请检查服务器 ${url} 是否可用`);
    } else if (err instanceof TypeError && err.message?.includes('fetch')) {
      ElMessage.error('网络错误，无法连接到服务器');
    } else if (err.status) {
      ElMessage.error(`服务器响应错误 (${err.status})`);
    } else {
      ElMessage.error('连接失败: ' + (err.message || '未知错误'));
    }
    console.error('[OPC UA] getRightBrowseDatas error:', err);
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

  const controller = new AbortController();
  const fetchApi = (input, init) => fetch(input, { ...init, signal: controller.signal });
  let apiConfig = new Configuration({
    basePath: url,
    fetchApi
  });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);

  try {
    // Variables 面板的双击加载只允许返回 Variable，避免复用左侧树的浏览掩码规则。
    const nodeClassToReturn = Number(NodeClass.Variable);
    const result = await withTimeout(
      testOpcServer.browseChild(validNodeId, nodeClassToReturn, 10),
      10000,
      '请求超时',
      controller
    );
    return result;
  } catch (err) {
    if (err.name === 'AbortError' || err.message?.includes('超时')) {
      ElMessage.error(`连接超时，请检查服务器 ${url} 是否可用`);
    } else if (err instanceof TypeError && err.message?.includes('fetch')) {
      ElMessage.error('网络错误，无法连接到服务器');
    } else if (err.status) {
      ElMessage.error(`服务器响应错误 (${err.status})`);
    } else {
      ElMessage.error('连接失败: ' + (err.message || '未知错误'));
    }
    console.error('[OPC UA] getRightDetailsBrowseDatas error:', err);
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
  nodeClassToReturn = normalizeBrowseNodeClassMask(
    selectedNodeId.value?.nodeId || selectedNodeId.value,
    nodeClassToReturn,
    treeSelectedNodeMeta.value || selectNodeData.value || selectedNodeId.value
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
//   eruda.init();

// // 获取 Eruda 实例
// const erudaInstance = eruda.get();

// // 显示/隐藏控制台
// eruda.show();
  loadDiscoveryConfig();
  loadDiscoverySearchHistory();
  ensureDiscoveryNodeExists();
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
  
  // 监听树节点变化，自动隐藏加载更多节点的展开图标
  const hideLoadMoreExpandIcons = () => {
    const loadMoreNodes = document.querySelectorAll('.optimized-tree .el-tree-node[data-key^="__load_more_"]');
    loadMoreNodes.forEach(node => {
      const expandIcon = node.querySelector('.el-tree-node__expand-icon');
      if (expandIcon && expandIcon.style.display !== 'none') {
        expandIcon.style.display = 'none';
        expandIcon.style.opacity = '0';
        expandIcon.style.visibility = 'hidden';
        expandIcon.style.width = '0';
        expandIcon.style.height = '0';
        expandIcon.style.minWidth = '0';
        expandIcon.style.minHeight = '0';
        expandIcon.style.margin = '0';
        expandIcon.style.padding = '0';
      }
    });
  };
  
  // 使用 MutationObserver 监听树节点的添加
  const treeObserver = new MutationObserver(() => {
    hideLoadMoreExpandIcons();
  });
  
  // 观察树容器
  nextTick(() => {
    const treeContainer = document.querySelector('.optimized-tree');
    if (treeContainer) {
      treeObserver.observe(treeContainer, {
        childList: true,
        subtree: true
      });
      // 立即执行一次，处理已存在的节点
      hideLoadMoreExpandIcons();
      // 延迟再执行一次，确保所有节点都已渲染
      setTimeout(hideLoadMoreExpandIcons, 200);
    }
  });
  
  // 定期检查并隐藏（作为备用方案）
  setInterval(hidePopperArrows, 1000);
});

watch(discoveryFindUrlInput, (newValue) => {
  if (discoveryLoading.value) return;
  if (!normalizeDiscoverySearchAddress(newValue)) {
    clearDiscoverySearchResults();
    return;
  }
  clearDiscoverySearchResults();
});

// Add new reactive data
const selectedNodeId = ref('');
const selectedTopNodeId = ref('');
const continuationPoints = ref('');
// 存储每个节点的 ContinuationPoint（key: nodeIdNum, value: continuationPoint）
const nodeContinuationPoints = ref(new Map());

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
  setTreeSelectedNodeMeta(data);
  const isLeafDebugNode =
    data?.label === 'Server' ||
    data?.label === 'PersonalData' ||
    data?.browseName === 'Server' ||
    data?.browseName === 'PersonalData';

  if (isLeafDebugNode) {
    console.warn('[Leaf Debug] handleNodeClick input', {
      label: data?.label,
      browseName: data?.browseName,
      isLeaf: data?.isLeaf,
      hasChildren: data?.hasChildren,
      nodeIdNum: data?.nodeIdNum,
      typeDefinitionId: data?.typeDefinitionId,
    });
  }
  if (isDiscoveryNode(data)) {
    selectedNodeId.value = '';
    state.bottomTreeData = [];
    return;
  }
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
      getBrowseDatas(undefined, 10, null, data).then(async (firstRes) => {
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
        const slim = processNodeDataItems(allArr);
        if (isLeafDebugNode) {
          console.warn('[Leaf Debug] handleNodeClick mapped children', slim.map((item) => ({
            label: item.label,
            nodeIdNum: item.nodeIdNum,
            isLeaf: item.isLeaf,
            hasChildren: item.hasChildren,
            nodeClass: item.nodeClass,
            typeDefinitionId: item.typeDefinitionId,
            objectNodeClassDisplayName: item.objectNodeClassDisplayName,
          })));
        }
        state.bottomTreeData = slim
        // return resolve(allArr);
     });

     }
     catch(e){
        connectFlag.value = false
     }
  }
};
// 单击处理定时器，用于区分单击和双击
let handleNodeClick2Timer = null;
const isDoubleClicking = ref(false);

const handleNodeClick2 = (data) => {
  setTreeSelectedNodeMeta(data);
  // 如果正在双击处理中，忽略单击
  if (isDoubleClicking.value) {
    console.log('正在双击处理中，忽略单击事件');
    return;
  }
  selectNodeData.value = data;
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

  // 清除之前的定时器
  if (handleNodeClick2Timer) {
    clearTimeout(handleNodeClick2Timer);
    handleNodeClick2Timer = null;
  }

  // 延迟执行 detailsMessage，等待判断是否为双击
  // 如果 300ms 内发生双击，则取消执行
  handleNodeClick2Timer = setTimeout(() => {
    // 如果此时不是双击状态，则执行单击处理
    if (!isDoubleClicking.value) {
      // 获取正确的URL
      const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
      let url = allData?.url || urlForm.value.url;
      singleDetailsMessage(data.nodeId, url);
    }
    handleNodeClick2Timer = null;
  }, 300); // 300ms 延迟，与双击防抖时间一致
};

// 拖拽相关状态
const isDragOver = ref(false);
const draggedNodeData = ref(null);

// 拖拽开始
const handleDragStart = (event, node, data) => {
  // 阻止事件冒泡，避免被 Element Plus Tree 拦截
  event.stopPropagation();
  
  // 只提取需要的数据，避免循环引用问题
  // 不要直接包含 node 对象，因为它有循环引用（parent -> node -> parent）
  console.log(node,'node',data)
  const cleanData = {
    // 提取节点的关键信息
    nodeIdNum: data.nodeIdNum,
    nodeId: data.nodeId,
    label: data.label,
    name: data.name,
    nodeClass: data.nodeClass,
    browseName: data.browseName,
    displayName: data.displayName,
    typeDefinition: data.typeDefinition, // 添加 TypeDefinition
    // 提取 node 的关键属性（不包含循环引用）
    nodeIdFromNode: node?.data?.nodeIdNum || node?.data?.nodeId,
    typeDefinitionId: node?.data?.typeDefinitionId,
    level: node?.level,
    // 生成唯一ID
    id: `node-${Date.now()}-${Math.random()}`
  };
  
  draggedNodeData.value = cleanData;
  
  // 设置拖拽数据 - 使用清理后的数据，避免循环引用
  event.dataTransfer.effectAllowed = 'move';
  try {
    event.dataTransfer.setData('application/json', JSON.stringify(cleanData));
  } catch (error) {
    console.error('拖拽数据序列化失败:', error);
    // 如果序列化失败，使用文本格式作为备选
    event.dataTransfer.setData('text/plain', cleanData.nodeIdNum || cleanData.nodeId || '');
  }
  
  // 添加拖拽样式
  if (event.target && event.target.style) {
    event.target.style.opacity = '0.5';
  }
};

// 拖拽结束
const handleDragEnd = (event) => {
  // 恢复样式
  if (event.target && event.target.style) {
    event.target.style.opacity = '1';
  }
  // 如果事件目标不是拖拽元素本身，尝试找到父元素
  if (event.target && event.target.closest) {
    const dragElement = event.target.closest('.draggable-node');
    if (dragElement && dragElement.style) {
      dragElement.style.opacity = '1';
    }
  }
  isDragOver.value = false;
};

// 拖拽悬停
const handleDragOver = (event) => {
  event.preventDefault();
  event.dataTransfer.dropEffect = 'move';
  isDragOver.value = true;
};

// 拖拽离开
const handleDragLeave = (event) => {
  // 检查是否真的离开了 drop zone（避免子元素触发）
  const rect = event.currentTarget.getBoundingClientRect();
  const x = event.clientX;
  const y = event.clientY;
  
  if (x < rect.left || x > rect.right || y < rect.top || y > rect.bottom) {
    isDragOver.value = false;
  }
};

// 放置节点
const handleDrop = (event) => {
  event.preventDefault();
  isDragOver.value = false;
  
  if (!draggedNodeData.value) {
    // 尝试从 dataTransfer 获取数据
    try {
      const data = event.dataTransfer.getData('application/json');
      if (data) {
        draggedNodeData.value = JSON.parse(data);
      }
    } catch (e) {
      console.error('Failed to parse drag data:', e);
      return;
    }
  }
  
  if (!draggedNodeData.value) {
    return;
  }
  
  // 生成节点信息
  const nodeInfo = {
    id: draggedNodeData.value.id || `node-${Date.now()}-${Math.random()}`,
    name: draggedNodeData.value.label || draggedNodeData.value.browseName || draggedNodeData.value.displayName || draggedNodeData.value.nodeIdNum || '未知节点',
    label: draggedNodeData.value.label,
    nodeIdNum: draggedNodeData.value.nodeIdNum,
    nodeId: draggedNodeData.value.nodeId,
    nodeClass: draggedNodeData.value.nodeClass,
    browseName: draggedNodeData.value.browseName || draggedNodeData.value.label,
    displayName: draggedNodeData.value.displayName,
    // children: [
    //   { name: '节点ID', value: draggedNodeData.value.nodeIdNum || draggedNodeData.value.nodeId || 'N/A' },
    //   { name: '节点类', value: getNodeClassText(draggedNodeData.value.nodeClass) || 'N/A' },
    //   { name: '浏览名称', value: draggedNodeData.value.label || draggedNodeData.value.browseName || 'N/A' },
    //   { name: '添加时间', value: new Date().toLocaleString('zh-CN') }
    // ]
  };
  
  // 仅保留一个节点，新的节点会替换旧节点
  state.nodeDetails.splice(0, state.nodeDetails.length, nodeInfo);
  ElMessage.success(`已更新节点: ${nodeInfo.name}`);
  
  draggedNodeData.value = null;
};

// References 引用列表相关状态
const referenceFilter = ref('all');
// References tab 专用状态，避免影响左侧树的请求参数
const referencesTabDirection = ref('Forward');
const referencesTabHierarchy = ref('All');
const loadingReferences = ref(false);
const references = ref([]);
const selectedReferenceNode = ref(null);
// 分页相关状态
const forwardContinuationPoint = ref(null);
const reverseContinuationPoint = ref(null);
const currentOpcServer = ref(null); // 保存当前的 OPC 服务器实例

// 拓扑图相关状态
const showTopologyDialog = ref(false);
const topologyChartRef = ref(null);
let topologyChart = null;
let isInitializingChart = false;

// History 历史记录相关状态
const historySelectedNode = ref(null);
const historyTreeData = ref([]); // 用于树形展示的数据
const historyTreeSelectValue = ref(null);
const historyDragOver = ref(false);
const historyEventTypesTreeRef = ref(null);
const historyEventTypesTreeData = ref([]);
const historyEventTypeCheckedNodes = ref([]);
const historyEventTypeDefaultCheckedKeys = ref([]); // 默认勾选的节点 nodeId 数组
const historyEventTypeActiveTab = ref('select'); // 当前选中的 tab：'select' 或 'where'
const whereMatchMode = ref('all'); // all=满足全部(AND), any=满足任一(OR)
const whereRows = ref([]); // Where 条件行
const whereActiveRowIndex = ref(-1);
const historyContinuationPoint = ref(null); // 历史事件查询的 continuationPoint
const currentHistoryOpcServer = ref(null); // 保存当前历史查询的 OPC 服务器实例
const childNodeIds = ref(null); // 存储 getGeneratedEventType 返回的子节点
// 初始化时间为当前日期（开始时间为今天00:00:00，结束时间为今天23:59:59）
const getDefaultStartTime = () => {
  return moment().format('YYYY-MM-DD HH:mm:ss');
};

const getDefaultEndTime = () => {
  return moment().endOf('day').format('YYYY-MM-DD HH:mm:ss');
};

const historyStartTime = ref(getDefaultStartTime());
const historyEndTime = ref(getDefaultEndTime());
const historySearchLoading = ref(false);
const historyResults = ref([]);
const historyDetails = ref({});
const selectedResultIndex = ref(-1);
const selectedHistoryDetailKey = ref(null);
const expandedHistoryDetails = ref([]);
const historyCheckedNodes = ref([]); // 选中的节点列表
const historyDisplayTreeRef = ref(null); // 树组件引用
const HISTORY_TREND_TAB_KEY = 'history-trend';
const historyTrendChartRef = ref(null);
const historyTrendNode = ref(null);
const historyTrendStartTime = ref(getDefaultStartTime());
const historyTrendEndTime = ref(getDefaultEndTime());
const historyTrendLoading = ref(false);
const historyTrendPoints = ref([]);
const historyTrendEmptyText = ref('点击变量右侧图标查看历史趋势');
let historyTrendChart = null;

// 扁平化树数据用于下拉选择
const flattenedTreeOptions = computed(() => {
  const flatten = (nodes, level = 0) => {
    const result = [];
    nodes.forEach(node => {
      const label = getDisplayName(node) || node.label || node.browseName || node.nodeIdNum || '未知节点';
      const prefix = '  '.repeat(level);
      result.push({
        label: prefix + label,
        value: node.nodeIdNum,
        node: node
      });
      if (node.children && node.children.length > 0) {
        result.push(...flatten(node.children, level + 1));
      }
    });
    return result;
  };
  return flatten(state.bottomTreeData);
});

// 监听节点切换，清空详情卡片数据
watch(selectedTopNodeId, (newNodeId, oldNodeId) => {
  if (newNodeId && newNodeId !== oldNodeId) {
    // 切换节点时清空 store 数据
    detailCardStore.switchNode(newNodeId)
    browseNodeMetaMap.value = new Map();
    treeSelectedNodeMeta.value = null;

    const connectionKey = stringifyNodeId(newNodeId);
    const cachedDataTypes = connectionDataTypesMap.value.get(connectionKey);
    const cachedReferenceTypes = connectionReferenceTypesMap.value.get(connectionKey);
    const cachedObjectTypes = connectionObjectTypesMap.value.get(connectionKey);

    if (cachedDataTypes) {
      state.allTypesData = cachedDataTypes;
    }
    if (cachedReferenceTypes) {
      state.allReferenceTypesData = cachedReferenceTypes;
    }
    if (cachedObjectTypes) {
      state.allObjectTypesData = cachedObjectTypes;
    }

    if (connectFlag.value) {
      ensureConnectionDictionariesLoaded(newNodeId).catch((error) => {
        console.error('切换连接后加载类型字典失败:', error);
      });
    }
  }
}, { immediate: false })

// 监听 dialog 状态变化，作为备用初始化方案
watch(showTopologyDialog, (newVal) => {
  if (newVal && !topologyChart && !isInitializingChart) {
    // 如果 @opened 事件没有触发，watch 会作为备用方案
    // 延迟更长时间，确保 @opened 事件优先
    setTimeout(() => {
      if (showTopologyDialog.value && !topologyChart && !isInitializingChart) {
        console.log('通过 watch 触发拓扑图初始化');
        initTopologyChart();
      }
    }, 1000);
  }
});

// 监听 History tab 的显示，自动加载 BaseEventType 详细信息
watch(activeFolder, (newVal) => {
  // 检查当前激活的 tab 是否是 History tab
  const historyTab = state.nodeDetailsData.find(tab => tab.key === newVal && tab.type === 'Event History');
  if (historyTab) {
    // 延迟加载，确保树组件已渲染
    nextTick(() => {
      // 检查 EmployeeCheckInEventType 节点是否已有 BaseEventType 子节点
      const employeeNode = findNodeInHistoryEventTree(historyEventTypesTreeData.value, 'EmployeeCheckInEventType');
      if (employeeNode) {
        const hasBaseEventType = employeeNode.children && employeeNode.children.some(
          child => child.nodeId && child.nodeId.toString().includes('2041')
        );
        // 如果还没有加载过，则自动加载
        if (!hasBaseEventType) {
          loadBaseEventTypeDetailsForHistory();
        }
      }
    });
  }
}, { immediate: true });

watch(activeFolder, (newVal) => {
  if (newVal === HISTORY_TREND_TAB_KEY && historyTrendPoints.value.length > 0) {
    renderHistoryTrendChart();
  }
});

// Where 可用字段列表（EmployeeCheckInEventType 下的字段，排除 BaseEventType 里的字段）
const whereAvailableFields = computed(() => {
  const fields = [];

  console.log('whereAvailableFields - historyEventTypesTreeData:', historyEventTypesTreeData.value);

  // 内部查找函数
  const findNode = (tree, targetNodeId) => {
    if (!tree || !Array.isArray(tree)) return null;
    for (const node of tree) {
      if (node.nodeId === targetNodeId || node.browseName === targetNodeId) {
        return node;
      }
      if (node.children && node.children.length > 0) {
        const found = findNode(node.children, targetNodeId);
        if (found) return found;
      }
    }
    return null;
  };

  // 从 historyEventTypesTreeData 中获取 EmployeeCheckInEventType 节点
  let employeeNode = findNode(historyEventTypesTreeData.value, 'EmployeeCheckInEventType');

  // 如果没找到，尝试在顶层数组中查找
  if (!employeeNode) {
    employeeNode = historyEventTypesTreeData.value.find(node => {
      const browseName = node.browseName || '';
      const displayName = node.displayName || '';
      const label = node.label || '';
      return browseName === 'EmployeeCheckInEventType' ||
             displayName === 'EmployeeCheckInEventType' ||
             label === 'EmployeeCheckInEventType' ||
             label.includes('EmployeeCheckInEventType');
    });
  }

  console.log('whereAvailableFields - employeeNode:', employeeNode);

  if (!employeeNode || !employeeNode.children) {
    return fields;
  }

  // 收集 BaseEventType 的字段名称（用于排除）
  const baseEventTypeFieldNames = new Set();

  // 在顶层数组中查找 BaseEventType 节点
  let baseEventTypeNode = historyEventTypesTreeData.value.find(node => {
    const browseName = node.browseName || '';
    const displayName = node.displayName || '';
    const label = node.label || '';
    return browseName === 'BaseEventType' ||
           displayName === 'BaseEventType' ||
           label === 'BaseEventType' ||
           label.includes('BaseEventType') ||
           (node.nodeId && node.nodeId.toString().includes('2041'));
  });

  // 如果顶层没找到，在 EmployeeCheckInEventType 的 children 中查找
  if (!baseEventTypeNode) {
    baseEventTypeNode = employeeNode.children.find(child => {
      const browseName = child.browseName || '';
      const displayName = child.displayName || '';
      const label = child.label || '';
      return browseName === 'BaseEventType' ||
             displayName === 'BaseEventType' ||
             label === 'BaseEventType' ||
             label.includes('BaseEventType') ||
             (child.nodeId && child.nodeId.toString().includes('2041'));
    });
  }

  console.log('whereAvailableFields - baseEventTypeNode:', baseEventTypeNode);

  if (baseEventTypeNode && baseEventTypeNode.children) {
    baseEventTypeNode.children.forEach(child => {
      const browseName = child.browseName || child.displayName || '';
      if (browseName) {
        baseEventTypeFieldNames.add(browseName.toLowerCase());
      }
    });
  }

  console.log('whereAvailableFields - baseEventTypeFieldNames:', Array.from(baseEventTypeFieldNames));

  // 收集 EmployeeCheckInEventType 的直接字段（排除 BaseEventType 及其子节点）
  employeeNode.children.forEach(child => {
    const browseName = child.browseName || '';
    const displayName = child.displayName || '';
    const label = child.label || '';

    console.log('whereAvailableFields - 检查字段:', { browseName, displayName, label, dataType: child.dataType, dataTypes: child.dataTypes });

    // 跳过 BaseEventType 节点
    if (browseName === 'BaseEventType' ||
        displayName === 'BaseEventType' ||
        label === 'BaseEventType' ||
        label.includes('BaseEventType') ||
        (child.nodeId && child.nodeId.toString().includes('2041'))) {
      return;
    }

    // 跳过已在 BaseEventType 中存在的字段
    if (baseEventTypeFieldNames.has(browseName.toLowerCase())) {
      return;
    }

    // 获取字段的显示名称（从 label 中提取，去掉值部分）
    let fieldDisplayName = displayName || browseName;
    if (!fieldDisplayName && label) {
      // label 格式可能是 "CheckIn: true"，需要提取字段名
      fieldDisplayName = label.split(':')[0].trim();
    }

    // 获取数据类型
    const dataType = child.dataType || child.dataTypes || '';

    // 只要有 nodeId 就添加到列表
    if (child.nodeId && fieldDisplayName) {
      fields.push({
        nodeId: child.nodeId,
        browseName: browseName || fieldDisplayName,
        displayName: fieldDisplayName,
        dataType: dataType,
        dataTypes: child.dataTypes || ''
      });
    }
  });

  console.log('whereAvailableFields - 最终字段列表:', fields);

  return fields;
});

// 筛选后的引用列表
const filteredReferencesList = computed(() => {
  if (!references.value || references.value.length === 0) {
    return [];
  }
  
  let filtered = references.value;
  
  // 根据referenceFilter筛选（目前只有'all'，后续可以扩展）
  if (referenceFilter.value !== 'all') {
    // 可以添加其他筛选逻辑
  }
  
  return filtered;
});

// 处理加载引用按钮点击事件（避免直接绑定异步函数）
const handleLoadReferencesClick = (event) => {
  // 阻止事件冒泡和默认行为，避免触发 Element Plus 内部错误
  if (event) {
    event.stopPropagation();
    event.preventDefault();
  }
  
  // 必须先从 Event View 中获取节点信息，只有拖拽到 Event View 的节点才能搜索
  if (!state.nodeDetails || state.nodeDetails.length === 0) {
    ElMessage.warning('请先拖拽节点到Event View（节点信息区域）');
    return;
  }
  
  // 获取 Event View 中的第一个节点（仅保留一个）
  const targetNode = state.nodeDetails[0];
  
  // 验证节点信息是否有效
  if (!targetNode) {
    ElMessage.warning('Event View 中的节点信息无效，请重新拖拽节点');
    return;
  }
  
  // 验证节点是否包含必要的标识信息
  const hasNodeId = targetNode.nodeId || targetNode.nodeIdNum;
  if (!hasNodeId) {
    ElMessage.warning('节点信息不完整，无法加载引用数据，请重新拖拽节点到Event View');
    return;
  }
  
  // 使用 requestAnimationFrame 和 setTimeout 双重延迟，确保完全脱离事件处理上下文
  requestAnimationFrame(() => {
    setTimeout(() => {
      // 在独立的执行上下文中调用异步函数
      Promise.resolve().then(() => {
        return loadReferences();
      }).catch(error => {
        console.error('加载引用失败:', error);
        setTimeout(() => {
          loadingReferences.value = false;
        }, 0);
      });
    }, 0);
  });
};

// 表格引用
const tableBodyRef = ref(null);
const tableRowRefs = ref([]);
const previousReferencesCount = ref(0);

// Variables 分页相关变量
const variablesContinuationPoint = ref(null);
const currentVariablesOpcServer = ref(null); // 保存当前的 OPC 服务器实例（用于 Variables）
const variablesTableBodyRef = ref(null);
const variablesTableRowRefs = ref([]);
const previousVariablesCount = ref(0);
const variablesReadError = ref(false); // 标记是否发生读取错误

// 设置表格行引用
const setTableRowRef = (el, index) => {
  if (el) {
    tableRowRefs.value[index] = el;
  }
};

// 设置 Variables 表格行引用
const setVariablesTableRowRef = (el, index) => {
  if (el) {
    variablesTableRowRefs.value[index] = el;
  }
};

const getHistoryTrendTabLabel = (item) => {
  const name = getDisplayName(item) || item?.name || item?.browseName || 'Variable';
  return `Event History - ${name}`;
};

const ensureHistoryTrendTab = (item) => {
  const label = getHistoryTrendTabLabel(item);
  const existingTab = state.nodeDetailsData.find(tab => tab.key === HISTORY_TREND_TAB_KEY);

  if (existingTab) {
    existingTab.value = label;
    return existingTab;
  }

  const newTab = {
    key: HISTORY_TREND_TAB_KEY,
    value: label,
    type: 'History Trend'
  };
  state.nodeDetailsData.push(newTab);
  return newTab;
};

const resolveHistoryTrendNodeId = (nodeIdValue) => {
  if (!nodeIdValue) return null;

  if (nodeIdValue instanceof UaNodeId) {
    return nodeIdValue;
  }

  if (typeof nodeIdValue === 'string') {
    return parseUaNodeId(nodeIdValue);
  }

  if (nodeIdValue._nodeId) {
    return nodeIdValue._nodeId;
  }

  if (nodeIdValue.nodeId instanceof UaNodeId) {
    return nodeIdValue.nodeId;
  }

  if (typeof nodeIdValue.toString === 'function') {
    return parseUaNodeId(nodeIdValue.toString());
  }

  return null;
};

const normalizeHistoryTrendValue = (variant) => {
  if (!variant) return null;

  try {
    if (typeof variant.toNumber === 'function') {
      const numericValue = variant.toNumber();
      if (numericValue !== null && !Number.isNaN(Number(numericValue))) {
        return Number(numericValue);
      }
    }

    if (typeof variant.toBoolean === 'function') {
      const booleanValue = variant.toBoolean();
      if (typeof booleanValue === 'boolean') {
        return booleanValue ? 1 : 0;
      }
    }
  } catch (error) {
    console.warn('解析历史趋势值失败:', error);
  }

  const rawValue = variant?.value?.value ?? variant?.value;

  if (typeof rawValue === 'number' && !Number.isNaN(rawValue)) {
    return rawValue;
  }

  if (typeof rawValue === 'boolean') {
    return rawValue ? 1 : 0;
  }

  if (typeof rawValue === 'string') {
    const parsedValue = Number(rawValue);
    return Number.isNaN(parsedValue) ? null : parsedValue;
  }

  return null;
};

const generateMockHistoryTrendPoints = (startTime, endTime, itemName = 'Variable') => {
  const start = startTime instanceof Date ? startTime.getTime() : new Date(startTime).getTime();
  const end = endTime instanceof Date ? endTime.getTime() : new Date(endTime).getTime();
  const safeStart = Number.isNaN(start) ? Date.now() - 24 * 60 * 60 * 1000 : start;
  const safeEnd = Number.isNaN(end) ? Date.now() : end;
  const pointCount = 24;
  const duration = Math.max(safeEnd - safeStart, 60 * 60 * 1000);
  const step = duration / Math.max(pointCount - 1, 1);
  const seed = String(itemName)
    .split('')
    .reduce((total, char) => total + char.charCodeAt(0), 0);

  return Array.from({ length: pointCount }, (_, index) => {
    const time = safeStart + step * index;
    const wave = Math.sin(index / 3 + seed / 50) * 8;
    const trend = index * 1.6;
    const noise = ((seed + index * 17) % 9) - 4;

    return {
      time,
      value: Number((45 + wave + trend + noise).toFixed(2))
    };
  });
};

const renderHistoryTrendChart = () => {
  nextTick(() => {
    const chartElement = historyTrendChartRef.value;
    if (!chartElement) return;

    if (historyTrendChart && historyTrendChart.getDom() !== chartElement) {
      historyTrendChart.dispose();
      historyTrendChart = null;
    }

    if (!historyTrendChart) {
      historyTrendChart = echarts.init(chartElement);
    }

    const seriesData = historyTrendPoints.value.map(point => [point.time, point.value]);
    const chartTitle = historyTrendNode.value?.name || '历史趋势';

    historyTrendChart.setOption({
      title: {
        text: chartTitle,
        left: 'center',
        top: 8,
        textStyle: {
          fontSize: 14,
          fontWeight: 600
        }
      },
      tooltip: {
        trigger: 'axis',
        formatter: (params) => {
          const firstPoint = Array.isArray(params) ? params[0] : params;
          if (!firstPoint) return '';
          const dataPoint = Array.isArray(firstPoint.value) ? firstPoint.value : [];
          const timeText = formatDateTimeForPicker(dataPoint[0]);
          return `${timeText}<br/>Value: ${dataPoint[1] ?? '-'}`;
        }
      },
      grid: {
        left: 48,
        right: 24,
        top: 56,
        bottom: 56
      },
      xAxis: {
        type: 'time',
        axisLabel: {
          formatter: (value) => moment(value).format('MM-DD HH:mm')
        }
      },
      yAxis: {
        type: 'value',
        scale: true
      },
      dataZoom: [
        {
          type: 'inside'
        },
        {
          type: 'slider',
          height: 18,
          bottom: 16
        }
      ],
      series: [
        {
          type: 'line',
          smooth: true,
          showSymbol: false,
          sampling: 'lttb',
          lineStyle: {
            width: 2,
            color: '#409eff'
          },
          areaStyle: {
            color: 'rgba(64, 158, 255, 0.16)'
          },
          data: seriesData
        }
      ]
    });

    historyTrendChart.resize();
  });
};

const loadHistoryTrendData = async () => {
  if (!historyTrendNode.value?.nodeId) {
    historyTrendEmptyText.value = '未选择可查询历史趋势的变量';
    return;
  }

  if (!historyTrendStartTime.value || !historyTrendEndTime.value) {
    ElMessage.warning('请选择完整的开始和结束时间');
    return;
  }

  if (new Date(historyTrendStartTime.value) > new Date(historyTrendEndTime.value)) {
    ElMessage.warning('开始时间不能晚于结束时间');
    return;
  }

  const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
  const url = allData?.url || urlForm.value.url;

  if (!url) {
    ElMessage.warning('请先配置服务器连接');
    return;
  }

  const nodeId = resolveHistoryTrendNodeId(historyTrendNode.value.nodeId);
  if (!nodeId) {
    ElMessage.error('无法解析变量节点ID');
    return;
  }

  historyTrendLoading.value = true;
  historyTrendEmptyText.value = '';
  historyTrendPoints.value = [];

  const startTime = new Date(historyTrendStartTime.value);
  const endTime = new Date(historyTrendEndTime.value);

  try {
    const apiConfig = new Configuration({
      basePath: url
    });
    const clientConfig = new UaClientConfiguration(apiConfig);
    const testOpcServer = new UaWebClient(clientConfig);

    let historyResult = await testOpcServer.historyReadRawData(
      nodeId,
      startTime,
      endTime,
      500,
      null,
      false,
      false
    );

    let historyValues = historyResult?.historyData || [];
    let continuationPoint = historyResult?.continuationPoint || null;
    let pageCount = 0;

    while (continuationPoint && pageCount < 9) {
      pageCount += 1;
      const nextPageResult = await testOpcServer.historyReadRawData(
        nodeId,
        startTime,
        endTime,
        500,
        continuationPoint,
        false,
        false
      );
      historyValues = historyValues.concat(nextPageResult?.historyData || []);
      continuationPoint = nextPageResult?.continuationPoint || null;
    }

    const points = historyValues
      .map(item => {
        const timestamp = item?.sourceTimestamp || item?.serverTimestamp;
        const value = normalizeHistoryTrendValue(item?.value);

        if (!timestamp || value === null) {
          return null;
        }

        return {
          time: timestamp instanceof Date ? timestamp.getTime() : new Date(timestamp).getTime(),
          value
        };
      })
      .filter(item => item && !Number.isNaN(item.time))
      .sort((left, right) => left.time - right.time);

    historyTrendPoints.value = points;

    if (points.length === 0) {
      historyTrendEmptyText.value = '当前时间范围内没有可展示的历史数据';
      if (historyTrendChart) {
        historyTrendChart.clear();
      }
      return;
    }

    historyTrendEmptyText.value = '';
  } catch (error) {
    console.error('加载历史趋势失败:', error);
    historyTrendPoints.value = generateMockHistoryTrendPoints(
      startTime,
      endTime,
      historyTrendNode.value?.name
    );
    historyTrendEmptyText.value = '';
    ElMessage.warning(`历史接口不可读，已使用 mock 数据进行展示`);
  } finally {
    historyTrendLoading.value = false;
    if (historyTrendPoints.value.length > 0 && !historyTrendEmptyText.value) {
      nextTick(() => {
        renderHistoryTrendChart();
      });
    }
  }
};

const openHistoryTrendTab = async (item) => {
  historyTrendNode.value = {
    name: getDisplayName(item) || item?.name || item?.browseName || item?.nodeId,
    nodeId: item?.nodeId,
    dataType: item?.dataType || item?.dataTypes || ''
  };
  historyTrendStartTime.value = historyStartTime.value || getDefaultStartTime();
  historyTrendEndTime.value = historyEndTime.value || getDefaultEndTime();

  ensureHistoryTrendTab(item);
  activeFolder.value = HISTORY_TREND_TAB_KEY;

  await nextTick();
  await loadHistoryTrendData();
};

// 滚动到新加载的数据行
const scrollToNewData = () => {
  nextTick(() => {
    // 确保 tableBodyRef 是 DOM 元素
    const tableBody = tableBodyRef.value;
    if (!tableBody || typeof tableBody.getBoundingClientRect !== 'function') {
      // 如果 ref 没有正确绑定，尝试通过选择器查找
      const tableBodyElement = document.querySelector('.references-table .table-body');
      if (!tableBodyElement) {
        console.warn('无法找到表格容器');
        return;
      }
      
      // 如果是首次加载，滚动到顶部
      if (previousReferencesCount.value === 0) {
        tableBodyElement.scrollTop = 0;
        return;
      }
      
      // 查找新加载的第一行数据
      const newDataStartIndex = previousReferencesCount.value;
      const allRows = tableBodyElement.querySelectorAll('.table-row');
      const targetRow = allRows[newDataStartIndex];
      
      if (targetRow) {
        // 计算目标行相对于滚动容器的位置
        const containerRect = tableBodyElement.getBoundingClientRect();
        const rowRect = targetRow.getBoundingClientRect();
        const scrollTop = tableBodyElement.scrollTop;
        const relativeTop = rowRect.top - containerRect.top + scrollTop;
        
        // 滚动到目标位置，留一些顶部间距
        tableBodyElement.scrollTo({
          top: Math.max(0, relativeTop - 10),
          behavior: 'smooth'
        });
        
      }
      return;
    }
    
    // 如果首次加载，滚动到顶部
    if (previousReferencesCount.value === 0) {
      tableBody.scrollTop = 0;
      return;
    }
    
    // 查找新加载的第一行数据
    const newDataStartIndex = previousReferencesCount.value;
    const targetRow = tableRowRefs.value[newDataStartIndex];
    
    // 如果通过 ref 找不到，尝试通过 DOM 查询
    if (!targetRow) {
      const allRows = tableBody.querySelectorAll('.table-row');
      const domTargetRow = allRows[newDataStartIndex];
      if (domTargetRow) {
        const containerRect = tableBody.getBoundingClientRect();
        const rowRect = domTargetRow.getBoundingClientRect();
        const scrollTop = tableBody.scrollTop;
        const relativeTop = rowRect.top - containerRect.top + scrollTop;
        
        tableBody.scrollTo({
          top: Math.max(0, relativeTop - 10),
          behavior: 'smooth'
        });
        console.log(`滚动到新数据行（通过DOM查询），索引: ${newDataStartIndex}`);
      }
      return;
    }
    
    // 计算目标行相对于滚动容器的位置
    const containerRect = tableBody.getBoundingClientRect();
    const rowRect = targetRow.getBoundingClientRect();
    const scrollTop = tableBody.scrollTop;
    const relativeTop = rowRect.top - containerRect.top + scrollTop;
    
    // 滚动到目标位置，留一些顶部间距
    tableBody.scrollTo({
      top: Math.max(0, relativeTop - 10),
      behavior: 'smooth'
    });
    
    console.log(`滚动到新数据行，索引: ${newDataStartIndex}`);
  });
};

// 加载引用数据
const loadReferences = async () => {
  // 记录加载前的数据数量（用于滚动定位）
  previousReferencesCount.value = references.value.length;
  
  // 必须从 Event View 中获取节点，只有拖拽到 Event View 的节点才能搜索
  // 不再允许从树中选中节点进行搜索，必须通过拖拽到 Event View 的方式
  if (!state.nodeDetails || state.nodeDetails.length === 0) {
    setTimeout(() => {
      loadingReferences.value = false;
    }, 0);
    ElMessage.warning('请先拖拽节点到Event View（节点信息区域）');
    return;
  }
  
  // 获取 Event View 中的第一个节点（仅保留一个）
  const targetNode = state.nodeDetails[0];
  
  // 验证节点信息是否有效
  if (!targetNode || (!targetNode.nodeIdNum && !targetNode.nodeId)) {
    setTimeout(() => {
      loadingReferences.value = false;
    }, 0);
    ElMessage.warning('Event View 中的节点信息无效，请重新拖拽节点');
    return;
  }
  
  // 立即读取所有响应式值，避免在异步操作中访问
  const currentDirection = String(referencesTabDirection.value || 'Forward');
  const currentHierarchy = String(referencesTabHierarchy.value || 'All');
  const isConnected = Boolean(connectFlag.value);
  const topNodeId = selectedTopNodeId.value;
  const urlFormData = urlForm.value;

  if (!isConnected) {
    ElMessage.warning('请先连接到OPC UA服务器');
    return;
  }

  // 使用 setTimeout 延迟更新响应式值，避免与 Element Plus 冲突
  setTimeout(() => {
    loadingReferences.value = true;
    selectedReferenceNode.value = targetNode;
  }, 0);

  try {
    // 获取正确的URL（使用提前读取的值）
    const allData = urlDatas.getDataByKey(topNodeId);
    let url = allData?.url || urlFormData?.url;

    if (!url) {
      setTimeout(() => {
        loadingReferences.value = false;
      }, 0);
      ElMessage.error('请先配置服务器连接');
      return;
    }

    // 创建客户端配置（使用 Configuration 对象）
    const apiConfig = new Configuration({
      basePath: url
    });
    const clientConfig = new UaClientConfiguration(apiConfig);

    // 创建客户端
    const testOpcServer = new UaWebClient(clientConfig);

    // 解析节点ID
    let nodeId;
    if (targetNode.nodeId) {
      nodeId = targetNode.nodeId;
    } else if (targetNode.nodeIdNum) {
      nodeId = UaNodeId.parse(targetNode.nodeIdNum);
    } else {
      throw new Error('无法解析节点ID');
    }

    // 确定浏览方向（使用提前读取的值）
    let browseDirection;
    if (currentDirection === 'Forward') {
      browseDirection = BrowseDirection?.Forward ?? 0;
    } else if (currentDirection === 'Reverse') {
      browseDirection = BrowseDirection?.Reverse ?? 1;
    } else {
      browseDirection =  2;
    }
    let browseHierarchy;
    if(currentHierarchy === 'All'){
      browseHierarchy = ReferenceTypeIds?.References ? `i=${ReferenceTypeIds.References}` : 'i=31';
    } else if(currentHierarchy === 'Hierarchy'){
      browseHierarchy = ReferenceTypeIds?.HierarchicalReferences ? `i=${ReferenceTypeIds.HierarchicalReferences}` : 'i=33';
    } else if(currentHierarchy === 'NonHierarchy'){
      browseHierarchy = ReferenceTypeIds?.NonHierarchicalReferences ? `i=${ReferenceTypeIds.NonHierarchicalReferences}` : 'i=32';
    }
    browseHierarchy = normalizeBrowseReferenceTypeId(nodeId, browseHierarchy);

    let nodeClassToReturn = Number( 
       NodeClass.Object 
      | NodeClass.ObjectType 
      | NodeClass.Method
      | NodeClass.DataType
      | NodeClass.ReferenceType
      | NodeClass.View
      | NodeClass.Variable
    );
    nodeClassToReturn = normalizeBrowseNodeClassMask(nodeId, nodeClassToReturn, targetNode);
    // 构建浏览请求
    const nodesToBrowse = [{
      NodeId: nodeId,
      BrowseDirection: browseDirection,
      ReferenceTypeId: browseHierarchy || 'i=35', // 所有引用类型
      IncludeSubtypes: true,
      NodeClassMask: nodeClassToReturn || 0, // 使用节点类或默认值
      ResultMask: 63 // 返回所有信息
    }];
    
    // 保存 OPC 服务器实例，用于后续分页加载
    currentOpcServer.value = testOpcServer;
   
    // 重置分页状态
    forwardContinuationPoint.value = null;
    reverseContinuationPoint.value = null;
    
    // 执行浏览
    const results = await testOpcServer.browseReference(nodeId, nodeClassToReturn,browseDirection,browseHierarchy, 10);
    console.log(results,'results----')
    if (!results || !results.results || results.results.length === 0) {
      setTimeout(() => {
        references.value = [];
        loadingReferences.value = false;
        forwardContinuationPoint.value = null;
        reverseContinuationPoint.value = null;
      }, 0);
      return;
    }

    const browseResult = results;
    // 根据方向保存 continuationPoint
    if (currentDirection === 'Forward') {
      forwardContinuationPoint.value = browseResult.continuationPoint || null;
    } else if (currentDirection === 'Reverse') {
      reverseContinuationPoint.value = browseResult.continuationPoint || null;
    } else {
      // Both 模式，只请求一次，browseDirection 为 2，保存 continuationPoint
      forwardContinuationPoint.value = browseResult.continuationPoint || null;
      // Both 模式下不使用 reverseContinuationPoint，因为只请求一次
      reverseContinuationPoint.value = null;
    }

    // 处理引用数据
    let refs = [];
    if (browseResult.results && browseResult.results.length > 0) {
      // 辅助函数：将 referenceTypeId 转换为名称，优先从 allReferenceTypesData 中查找
      const getReferenceTypeName = (referenceTypeId) => {
        if (!referenceTypeId) return '';
        
        // 将 referenceTypeId 转换为字符串格式，用于比较
        let referenceTypeIdStr = '';
        
        if (typeof referenceTypeId === 'string') {
          referenceTypeIdStr = referenceTypeId;
        } else if (typeof referenceTypeId === 'number') {
          referenceTypeIdStr = `i=${referenceTypeId}`;
        } else if (referenceTypeId && typeof referenceTypeId.toString === 'function') {
          // 处理 UaNodeId 对象
          referenceTypeIdStr = referenceTypeId.toString();
          } else {
          referenceTypeIdStr = String(referenceTypeId);
        }
        
        // 规范化 ID 格式（统一为 "ns=0;i=xxx" 或 "i=xxx" 格式）
        const normalizeNodeId = (nodeIdStr) => {
          if (!nodeIdStr) return '';
          // 如果已经是完整格式 "ns=X;i=Y"，直接返回
          if (/ns=\d+;i=\d+/.test(nodeIdStr)) {
            return nodeIdStr;
          }
          // 如果是 "i=XXX" 格式，添加默认命名空间
          if (/^i=\d+/.test(nodeIdStr)) {
            return `ns=0;${nodeIdStr}`;
          }
          // 如果是纯数字，转换为标准格式
          if (/^\d+$/.test(nodeIdStr)) {
            return `ns=0;i=${nodeIdStr}`;
          }
          return nodeIdStr;
        };
        
        const normalizedId = normalizeNodeId(referenceTypeIdStr);
        
        // 在 allReferenceTypesData 中查找匹配的 reference type
        if (state.allReferenceTypesData && state.allReferenceTypesData.length > 0) {
          const matchedReferenceType = state.allReferenceTypesData.find(refType => {
            if (!refType || !refType.nodeId) return false;
            
            // 获取 reference type 的 nodeId
            let refTypeNodeIdStr = '';
            if (typeof refType.nodeId === 'string') {
              refTypeNodeIdStr = refType.nodeId;
            } else if (refType.nodeId && typeof refType.nodeId.toString === 'function') {
              refTypeNodeIdStr = refType.nodeId.toString();
            }
            
            // 规范化并比较
            const normalizedRefTypeId = normalizeNodeId(refTypeNodeIdStr);
            
            // 直接字符串比较或提取数字部分比较
            if (normalizedRefTypeId === normalizedId) {
              return true;
            }
            
            // 如果都是 "ns=0;i=XXX" 格式，提取数字部分比较
            const normalizedMatch = normalizedId.match(/ns=(\d+);i=(\d+)/);
            const refTypeMatch = normalizedRefTypeId.match(/ns=(\d+);i=(\d+)/);
            if (normalizedMatch && refTypeMatch) {
              return normalizedMatch[1] === refTypeMatch[1] && normalizedMatch[2] === refTypeMatch[2];
            }
            
            return false;
          });
          
          if (matchedReferenceType) {
            // 优先返回 browseName，如果没有则返回 displayName
            if (matchedReferenceType.browseName) {
              return matchedReferenceType.browseName;
            }
            if (matchedReferenceType.displayName) {
              // displayName 可能是 UaLocalizedText 对象，需要提取 text
              if (typeof matchedReferenceType.displayName === 'string') {
                return matchedReferenceType.displayName;
              } else if (matchedReferenceType.displayName && matchedReferenceType.displayName.text) {
                return matchedReferenceType.displayName.text;
              } else if (matchedReferenceType.displayName && matchedReferenceType.displayName.Text) {
                return matchedReferenceType.displayName.Text;
              }
            }
          }
        }
        
        // 如果 allReferenceTypesData 中没有找到，回退到使用 ReferenceTypeIds 枚举
        let numericId = null;
        const match = normalizedId.match(/i=(\d+)/);
        if (match) {
          numericId = parseInt(match[1], 10);
        } else if (/^\d+$/.test(referenceTypeIdStr)) {
          numericId = parseInt(referenceTypeIdStr, 10);
        }
        
        if (numericId !== null && ReferenceTypeIds) {
          const typeName = Object.keys(ReferenceTypeIds).find(
            key => ReferenceTypeIds[key] === numericId
          );
          if (typeName) {
            return typeName;
          }
        }
        
        // 如果都找不到，返回原始值
        return referenceTypeIdStr;
      };
      
      refs = browseResult.results.map(ref => {
        const referenceTypeId = ref.referenceTypeId || ref.ReferenceTypeId;
        return {
          referenceTypeId: referenceTypeId,
          referenceTypeName: getReferenceTypeName(referenceTypeId), // 使用字典转换名称
          targetNodeId: ref.nodeId ? (typeof ref.nodeId === 'string' ? ref.nodeId : ref.nodeId.toString()) : (ref.NodeId ? (typeof ref.NodeId === 'string' ? ref.NodeId : ref.NodeId.toString()) : ''),
          targetDisplayName: ref.displayName?.text ||   '',
          browseName: ref.browseName || '',
          nodeClass: ref.nodeClass || ref.NodeClass,
          isForward: ref.isForward !== undefined ? ref.isForward : (ref.IsForward !== undefined ? ref.IsForward : true)
        };
      });
    }

    // 使用 setTimeout 延迟更新响应式值，避免与 Element Plus 冲突
    setTimeout(() => {
      references.value = refs;
      loadingReferences.value = false;
      ElMessage.success(`已加载 ${refs.length} 个引用`);
      
      // 滚动到新加载的数据行（如果是首次加载，会滚动到顶部）
      scrollToNewData();
    }, 0);
  } catch (error) {
    console.error('加载引用失败:', error);
    setTimeout(() => {
      references.value = [];
      loadingReferences.value = false;
    }, 0);
    ElMessage.error('加载引用失败: ' + (error.message || '未知错误'));
  }
};

// 加载下一页引用数据
const loadNextPageReferences = async () => {
  if (!currentOpcServer.value) {
    ElMessage.warning('OPC 服务器连接不可用');
    return;
  }

  const currentDirection = String(referencesTabDirection.value || 'Forward');
  const currentHierarchy = String(referencesTabHierarchy.value || 'All');
  
  // 确定要使用的 continuationPoint
  let continuationPoint = null;
  if (currentDirection === 'Forward') {
    continuationPoint = forwardContinuationPoint.value;
  } else if (currentDirection === 'Reverse') {
    continuationPoint = reverseContinuationPoint.value;
  } else if (currentDirection === 'Both') {
    // Both 模式下，只使用 forwardContinuationPoint（因为只请求一次）
    continuationPoint = forwardContinuationPoint.value;
  }

  if (!continuationPoint) {
    ElMessage.info('没有更多数据了');
    return;
  }

  // 根据 hierarchy 设置（虽然 browseNext 使用 continuationPoint，但保持代码一致性）
  let browseHierarchy;
  if(currentHierarchy === 'All'){
    browseHierarchy = ReferenceTypeIds?.References ? `i=${ReferenceTypeIds.References}` : 'i=31';
  } else if(currentHierarchy === 'Hierarchy'){
    browseHierarchy = ReferenceTypeIds?.HierarchicalReferences ? `i=${ReferenceTypeIds.HierarchicalReferences}` : 'i=33';
  } else if(currentHierarchy === 'NonHierarchy'){
    browseHierarchy = ReferenceTypeIds?.NonHierarchicalReferences ? `i=${ReferenceTypeIds.NonHierarchicalReferences}` : 'i=32';
  }
  browseHierarchy = normalizeBrowseReferenceTypeId(
    selectedReferenceNode.value?.nodeId || selectedReferenceNode.value?.nodeIdNum,
    browseHierarchy
  );

  setTimeout(() => {
    loadingReferences.value = true;
  }, 0);

  try {
    // 调用 browseNext 加载下一页
    // 注意：browseNextByCP 的第二个参数是 releaseContinuationPoint，设为 false 表示保留 continuationPoint
    const nextResult = await currentOpcServer.value.browseNextByCP(continuationPoint, false);
    
    // 定义每次请求的预期条数（与 loadReferences 中的 maxResults 保持一致）
    const expectedPageSize = 10;
    
    if (!nextResult || !nextResult.results || nextResult.results.length === 0) {
      // 没有更多数据，清除 continuationPoint 以隐藏"..."节点
      if (currentDirection === 'Forward') {
        forwardContinuationPoint.value = null;
      } else if (currentDirection === 'Reverse') {
        reverseContinuationPoint.value = null;
      } else if (currentDirection === 'Both') {
        forwardContinuationPoint.value = null;
      }
      
      setTimeout(() => {
        loadingReferences.value = false;
      }, 0);
      ElMessage.info('没有更多数据了');
      return;
    }
    
    // 获取返回的数据条数
    const returnedCount = nextResult.results ? nextResult.results.length : 0;
    
    // 获取新的 continuationPoint（如果不存在则为 null）
    const newContinuationPoint = nextResult.continuationPoint || null;
    
    // 判断是否应该隐藏"..."节点：
    // 1. 如果返回的数据条数小于预期条数，说明没有更多数据了
    // 2. 如果没有 continuationPoint，也说明没有更多数据了
    const shouldHideLoadMore = returnedCount < expectedPageSize || !newContinuationPoint;
    
    // 更新 continuationPoint
    // 如果应该隐藏"..."节点，将 continuationPoint 设置为 null
    const finalContinuationPoint = shouldHideLoadMore ? null : newContinuationPoint;
    
    if (currentDirection === 'Forward') {
      forwardContinuationPoint.value = finalContinuationPoint;
    } else if (currentDirection === 'Reverse') {
      reverseContinuationPoint.value = finalContinuationPoint;
    } else if (currentDirection === 'Both') {
      // Both 模式下，只更新 forwardContinuationPoint（因为只请求一次）
      forwardContinuationPoint.value = finalContinuationPoint;
    }

    // 处理新的引用数据
    let newRefs = [];
    if (nextResult.results && nextResult.results.length > 0) {
      // 辅助函数：将 referenceTypeId 转换为名称，优先从 allReferenceTypesData 中查找
      const getReferenceTypeName = (referenceTypeId) => {
        if (!referenceTypeId) return '';
        
        // 将 referenceTypeId 转换为字符串格式，用于比较
        let referenceTypeIdStr = '';
        
        if (typeof referenceTypeId === 'string') {
          referenceTypeIdStr = referenceTypeId;
        } else if (typeof referenceTypeId === 'number') {
          referenceTypeIdStr = `i=${referenceTypeId}`;
        } else if (referenceTypeId && typeof referenceTypeId.toString === 'function') {
          // 处理 UaNodeId 对象
          referenceTypeIdStr = referenceTypeId.toString();
          } else {
          referenceTypeIdStr = String(referenceTypeId);
        }
        
        // 规范化 ID 格式（统一为 "ns=0;i=xxx" 或 "i=xxx" 格式）
        const normalizeNodeId = (nodeIdStr) => {
          if (!nodeIdStr) return '';
          // 如果已经是完整格式 "ns=X;i=Y"，直接返回
          if (/ns=\d+;i=\d+/.test(nodeIdStr)) {
            return nodeIdStr;
          }
          // 如果是 "i=XXX" 格式，添加默认命名空间
          if (/^i=\d+/.test(nodeIdStr)) {
            return `ns=0;${nodeIdStr}`;
          }
          // 如果是纯数字，转换为标准格式
          if (/^\d+$/.test(nodeIdStr)) {
            return `ns=0;i=${nodeIdStr}`;
          }
          return nodeIdStr;
        };
        
        const normalizedId = normalizeNodeId(referenceTypeIdStr);
        
        // 在 allReferenceTypesData 中查找匹配的 reference type
        if (state.allReferenceTypesData && state.allReferenceTypesData.length > 0) {
          const matchedReferenceType = state.allReferenceTypesData.find(refType => {
            if (!refType || !refType.nodeId) return false;
            
            // 获取 reference type 的 nodeId
            let refTypeNodeIdStr = '';
            if (typeof refType.nodeId === 'string') {
              refTypeNodeIdStr = refType.nodeId;
            } else if (refType.nodeId && typeof refType.nodeId.toString === 'function') {
              refTypeNodeIdStr = refType.nodeId.toString();
            }
            
            // 规范化并比较
            const normalizedRefTypeId = normalizeNodeId(refTypeNodeIdStr);
            
            // 直接字符串比较或提取数字部分比较
            if (normalizedRefTypeId === normalizedId) {
              return true;
            }
            
            // 如果都是 "ns=0;i=XXX" 格式，提取数字部分比较
            const normalizedMatch = normalizedId.match(/ns=(\d+);i=(\d+)/);
            const refTypeMatch = normalizedRefTypeId.match(/ns=(\d+);i=(\d+)/);
            if (normalizedMatch && refTypeMatch) {
              return normalizedMatch[1] === refTypeMatch[1] && normalizedMatch[2] === refTypeMatch[2];
            }
            
            return false;
          });
          
          if (matchedReferenceType) {
            // 优先返回 browseName，如果没有则返回 displayName
            if (matchedReferenceType.browseName) {
              return matchedReferenceType.browseName;
            }
            if (matchedReferenceType.displayName) {
              // displayName 可能是 UaLocalizedText 对象，需要提取 text
              if (typeof matchedReferenceType.displayName === 'string') {
                return matchedReferenceType.displayName;
              } else if (matchedReferenceType.displayName && matchedReferenceType.displayName.text) {
                return matchedReferenceType.displayName.text;
              } else if (matchedReferenceType.displayName && matchedReferenceType.displayName.Text) {
                return matchedReferenceType.displayName.Text;
              }
            }
          }
        }
        
        // 如果 allReferenceTypesData 中没有找到，回退到使用 ReferenceTypeIds 枚举
        let numericId = null;
        const match = normalizedId.match(/i=(\d+)/);
        if (match) {
          numericId = parseInt(match[1], 10);
        } else if (/^\d+$/.test(referenceTypeIdStr)) {
          numericId = parseInt(referenceTypeIdStr, 10);
        }
        
        if (numericId !== null && ReferenceTypeIds) {
          const typeName = Object.keys(ReferenceTypeIds).find(
            key => ReferenceTypeIds[key] === numericId
          );
          if (typeName) {
            return typeName;
          }
        }
        
        // 如果都找不到，返回原始值
        return referenceTypeIdStr;
      };
      
      newRefs = nextResult.results.map(ref => {
        const referenceTypeId = ref.referenceTypeId || ref.ReferenceTypeId;
        return {
          referenceTypeId: referenceTypeId,
          referenceTypeName: getReferenceTypeName(referenceTypeId), // 使用字典转换名称
          targetNodeId: ref.nodeId ? (typeof ref.nodeId === 'string' ? ref.nodeId : ref.nodeId.toString()) : (ref.NodeId ? (typeof ref.NodeId === 'string' ? ref.NodeId : ref.NodeId.toString()) : ''),
          targetDisplayName: ref.displayName?.text   || '',
          browseName: ref.browseName || '',
          nodeClass: ref.nodeClass || ref.NodeClass,
          isForward: ref.isForward !== undefined ? ref.isForward : (currentDirection !== 'Reverse')
        };
      });
    }

    // 记录加载前的数据数量
    previousReferencesCount.value = references.value.length;
    
    // 追加到现有引用列表
    setTimeout(() => {
      references.value = [...references.value, ...newRefs];
      loadingReferences.value = false;
      
      // 判断是否还有更多数据，决定提示信息
      // 如果返回的数据条数小于预期条数，或者没有 continuationPoint，说明已全部加载
      const hasMore = !shouldHideLoadMore;
      if (hasMore) {
      ElMessage.success(`已加载 ${newRefs.length} 个引用，共 ${references.value.length} 个`);
      } else {
        ElMessage.success(`已加载 ${newRefs.length} 个引用，共 ${references.value.length} 个（已全部加载）`);
      }
      
      // 滚动到新加载的数据行
      scrollToNewData();
    }, 0);
  } catch (error) {
    console.error('加载下一页引用失败:', error);
    setTimeout(() => {
      loadingReferences.value = false;
    }, 0);
    ElMessage.error('加载下一页引用失败: ' + (error.message || '未知错误'));
  }
};

// 计算是否有下一页数据
const hasNextPage = computed(() => {
  const currentDirection = String(referencesTabDirection.value || 'Forward');
  if (currentDirection === 'Forward') {
    return !!forwardContinuationPoint.value;
  } else if (currentDirection === 'Reverse') {
    return !!reverseContinuationPoint.value;
  } else if (currentDirection === 'Both') {
    // Both 模式下，只检查 forwardContinuationPoint（因为只请求一次）
    return !!forwardContinuationPoint.value;
  }
  return false;
});

// 计算是否有更多 Variables 数据
const hasNextVariablesPage = computed(() => {
  // 如果发生读取错误，不显示 "..." 按钮
  if (variablesReadError.value) {
    return false;
  }
  return !!variablesContinuationPoint.value;
});

// 初始化拓扑图 - 完全重写的简化版本
const initTopologyChart = () => {
  // 防止重复初始化
  if (isInitializingChart || topologyChart) {
    return;
  }
  
  if (!showTopologyDialog.value) {
    return;
  }
  
  isInitializingChart = true;
  
  // 使用简单的延迟，等待 dialog 完全渲染
  let retryCount = 0;
  const maxRetries = 15;
  
  const doInit = () => {
    retryCount++;
    
    if (retryCount > maxRetries) {
      isInitializingChart = false;
      ElMessage.error('拓扑图初始化超时，请关闭后重新打开');
      return;
    }
    
    // 直接通过 ID 查找元素（最可靠的方式）
    let element = document.getElementById('topology-chart-container');
    
    // 如果通过 ID 找不到，尝试通过 ref
    if (!element && topologyChartRef.value) {
      element = topologyChartRef.value;
      // 如果是 Vue 组件实例，获取 $el
      if (element && element.$el) {
        element = element.$el;
      }
    }
    
    // 如果还是找不到，尝试通过 class 查找
    if (!element) {
      const dialogs = Array.from(document.querySelectorAll('.el-dialog')).filter(d => {
        const style = window.getComputedStyle(d);
        return style.display !== 'none' && d.offsetWidth > 0;
      });
      
      if (dialogs.length > 0) {
        const dialogBody = dialogs[0].querySelector('.el-dialog__body');
        if (dialogBody) {
          element = dialogBody.querySelector('#topology-chart-container') || 
                   dialogBody.querySelector('.topology-chart');
        }
      }
    }
    
    if (!element) {
      setTimeout(() => {
        if (showTopologyDialog.value && isInitializingChart) {
          doInit();
        } else {
          isInitializingChart = false;
        }
      }, 200);
      return;
    }
    
    // 确保是有效的 DOM 元素
    if (!(element instanceof HTMLElement)) {
      setTimeout(() => {
        if (showTopologyDialog.value && isInitializingChart) {
          doInit();
        } else {
          isInitializingChart = false;
        }
      }, 200);
      return;
    }
    
    // 确保元素可见且有尺寸
    if (element.offsetWidth === 0 || element.offsetHeight === 0) {
      setTimeout(() => {
        if (showTopologyDialog.value && isInitializingChart) {
          doInit();
        } else {
          isInitializingChart = false;
        }
      }, 200);
      return;
    }
    
    // 销毁之前的图表实例
    if (topologyChart) {
      try {
        topologyChart.dispose();
      } catch (e) {
        console.warn('销毁旧图表实例失败:', e);
      }
      topologyChart = null;
    }
    
    try {
      // 创建新的图表实例
      topologyChart = echarts.init(element);
      
      // 检查引用数据，如果已加载0个引用，清空拓扑图数据
      if (!references.value || references.value.length === 0) {
        topologyChart.setOption({
          title: {
            text: '',
            left: 'center',
            top: 10,
            textStyle: {
              fontSize: 16,
              fontWeight: 'bold'
            }
          },
          series: [{
            type: 'graph',
            layout: 'force',
            roam: true,
            draggable: true,
            data: [],
            links: []
          }]
        });
        // 延迟调用resize，确保弹窗完全渲染后再调整大小
        setTimeout(() => {
          if (topologyChart) {
            topologyChart.resize();
          }
        }, 100);
        isInitializingChart = false;
        return;
      }
      
      // 生成拓扑图数据
      const topologyData = generateTopologyData();
      
      
      
      // 配置图表选项 - 根据图片样式调整
      const option = {
        title: {
          text: '',
          left: 'center',
          top: 10,
          textStyle: {
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: (params) => {
            if (params.dataType === 'edge' || params.dataType === 'link') {
              return `${params.data.source} → ${params.data.target}<br/>引用类型: ${params.data.referenceType || 'N/A'}`;
            } else {
              return `节点名称: ${params.data.name}<br/>节点ID: ${params.data.nodeId || 'N/A'}`;
            }
          }
        },
        series: [
          {
            type: 'graph',
            layout: 'force',
            roam: true,
            draggable: true,
            zoom: 1,
            // 确保连接线显示
            edgeSymbol: ['none', 'arrow'], // 起点无箭头，终点有箭头
            edgeSymbolSize: [8, 10], // 箭头大小
            // 重要：确保连接线使用正确的引用方式
            // 如果节点有 id，使用 id；否则使用 name；否则使用数组索引
            coordinateSystem: null, // 使用 graph 自己的坐标系
            // 节点样式 - 矩形蓝色
            symbol: 'rect',
            symbolSize: [140, 40], // 矩形宽度和高度（固定尺寸）
            itemStyle: {
              color: '#409EFF', // 蓝色
              borderColor: '#66B1FF',
              borderWidth: 1,
              borderRadius: 4
            },
            // 节点标签 - 显示在节点内部，图标和名称在同一行
            label: {
              show: true,
              position: 'inside',
              formatter: (params) => {
                // 显示图标和名称，图标在左侧
                const icon = params.data.icon || '+';
                let nodeName = params.data.name || '';
                // 截断过长的节点名称（节点宽度140px，图标约20px，可用约120px，12px字体约可显示10个中文字符）
                const maxLength = 40;
                if (nodeName.length > maxLength) {
                  nodeName = nodeName.substring(0, maxLength) + '...';
                }
                // return `{icon|${icon}} {name|${nodeName}}`;
                return `{name|${nodeName}}`;
              },
              rich: {
                // icon: {
                //   fontSize: 14,
                //   fontWeight: 'bold',
                //   color: '#fff',
                //   align: 'left',
                //   padding: [0, 4, 0, 2],
                //   width: 20
                // },
                name: {
                  fontSize: 12,
                  color: '#fff',
                  fontWeight: 'normal',
                  align: 'left',
                  padding: [0, 2, 0, 0],
                  // width: 110,
                  overflow: 'truncate',
                  ellipsis: '...'
                }
              }
            },
            // 边标签 - 显示关系类型
            edgeLabel: {
              show: true,
              position: 'middle',
              formatter: (params) => {
                return params.data.referenceType || '';
              },
              fontSize: 11,
              color: '#333',
              backgroundColor: '#fff',
              borderColor: '#409EFF',
              borderWidth: 1,
              borderRadius: 3,
              padding: [2, 4]
            },
            // 边的样式 - 带箭头，使用蓝色使其更明显
            lineStyle: {
              color: '#409EFF', // 使用蓝色，与节点颜色一致
              width: 3, // 增加宽度使其更明显
              curveness: 0,
              type: 'solid',
              opacity: 1 // 确保不透明
            },
            // 箭头样式
            emphasis: {
              focus: 'adjacency',
              lineStyle: {
                width: 3,
                color: '#409EFF'
              },
              itemStyle: {
                borderColor: '#66B1FF',
                borderWidth: 2
              }
            },
            // 力导向布局参数 - 优化为中心节点布局
            force: {
              repulsion: 1200,
              gravity: 0.1,
              edgeLength: 220,
              layoutAnimation: true,
              friction: 0.8,
              initLayout: null // 使用自定义初始布局（通过 x, y 坐标）
            },
            // 节点数据 - 设置中心节点位置和周围节点布局
            data: (() => {
              const centerNode = topologyData.nodes.find(n => n.category === 0);
              const otherNodes = topologyData.nodes.filter(n => n.category !== 0);
              const centerX = 0; // 画布中心 X
              const centerY = 0; // 画布中心 Y
              const radius = 250; // 周围节点到中心的距离
              
              const processedNodes = [];
              
              // 处理中心节点
              if (centerNode) {
                const nameLength = centerNode.name ? centerNode.name.length : 10;
                const width = Math.max(140, Math.min(nameLength * 7 + 50, 280));
                
                // 确保 id 字段存在且是字符串
                const centerId = String(centerNode.id || centerNode.nodeId || 'center');
                
                processedNodes.push({
                  ...centerNode,
                  id: centerId, // 使用 nodeId 作为 id，确保链接能匹配
                  nodeId: centerId, // 确保 nodeId 也存在
                  name: centerNode.name,
                  icon: centerNode.icon || '-',
                  symbolSize: [width, 45], // 中心节点较大，更突出
                  x: centerX, // 固定中心节点在中心
                  y: centerY,
                  fixed: true // 固定中心节点位置（ECharts 支持）
                });
              }
              
              // 处理周围节点，圆形分布
              otherNodes.forEach((node, index) => {
                const nameLength = node.name ? node.name.length : 10;
                const width = Math.max(120, Math.min(nameLength * 7 + 50, 280));
                
                // 计算圆形分布的坐标
                const angle = (index * 2 * Math.PI) / otherNodes.length;
                const x = centerX + radius * Math.cos(angle);
                const y = centerY + radius * Math.sin(angle);
                
                // 确保 id 字段存在且是字符串
                const nodeId = String(node.id || node.nodeId || `node_${index}`);
                
                processedNodes.push({
                  ...node,
                  id: nodeId, // 使用 nodeId 作为 id，确保链接能匹配
                  nodeId: nodeId, // 确保 nodeId 也存在
                  name: node.name,
                  icon: node.icon || '+',
                  symbolSize: [width, 40],
                  x: x, // 设置初始位置
                  y: y,
                  fixed: false // 周围节点可以移动
                });
              });
              
              return processedNodes;
            })(),
            // 连接数据 - 确保所有连接都从中心节点指向周围节点
            links: (() => {
              // 找到中心节点的 nodeId
              const centerNode = topologyData.nodes.find(n => n.category === 0);
              if (!centerNode) {
                console.warn('未找到中心节点，无法生成连接');
                return [];
              }
              
              return topologyData.links.map(link => {
                // 确保 source 和 target 都是字符串，使用节点的 id 或 nodeId
                const sourceId = String(centerNode.id || centerNode.nodeId || link.source);
                const targetId = String(link.target || '');
                
                return {
                  source: sourceId, // 使用中心节点的 id
                  target: targetId, // 使用目标节点的 id
                  value: link.referenceType || link.value || '',
                  referenceType: link.referenceType || '',
                  // 连接线样式 - 使用蓝色使其更明显
                  lineStyle: {
                    color: '#409EFF', // 使用蓝色，与节点颜色一致，更明显
                    width: 3, // 增加宽度
                    curveness: 0,
                    type: 'solid',
                    opacity: 1
                  },
                  // 边标签配置
                  label: {
                    show: true,
                    formatter: link.referenceType || '',
                    fontSize: 11,
                    color: '#333',
                    backgroundColor: '#fff',
                    borderColor: '#409EFF',
                    borderWidth: 1,
                    borderRadius: 3,
                    padding: [2, 4]
                  }
                };
              }).filter(link => {
                // 确保链接的 target 节点存在
                const targetExists = topologyData.nodes.some(n => n.nodeId === link.target);
                if (!targetExists) {
                  console.warn(`链接目标节点不存在: ${link.target}`);
                }
                return targetExists;
              });
            })(),
            categories: [
              { 
                name: '中心节点',
                itemStyle: {
                  color: '#409EFF'
                }
              },
              { 
                name: '目标节点',
                itemStyle: {
                  color: '#409EFF'
                }
              }
            ]
          }
        ]
      };
      
      // 设置图表选项
      topologyChart.setOption(option);
      
      // 调试：输出处理后的节点和链接数据
      const processedNodes = option.series[0].data;
      const processedLinks = option.series[0].links;
      
      
      // 验证链接是否都能找到对应的节点
      processedLinks.forEach(link => {
        const sourceNode = processedNodes.find(n => (n.id || n.nodeId) === link.source);
        const targetNode = processedNodes.find(n => (n.id || n.nodeId) === link.target);
        if (!sourceNode) {
          console.error(`链接源节点未找到: ${link.source}`);
        }
        if (!targetNode) {
          console.error(`链接目标节点未找到: ${link.target}`);
        }
      });
      
      // 响应式调整
      const resizeHandler = () => {
        if (topologyChart) {
          topologyChart.resize();
        }
      };
      window.addEventListener('resize', resizeHandler);
      
      // 保存 resize handler，以便在销毁时移除
      if (topologyChart) {
        topologyChart._resizeHandler = resizeHandler;
      }
      
      // 延迟调用resize，确保弹窗完全渲染后再调整大小
      setTimeout(() => {
        if (topologyChart) {
          topologyChart.resize();
        }
      }, 100);
      
      isInitializingChart = false;
    } catch (error) {
      ElMessage.error('初始化拓扑图失败: ' + (error.message || '未知错误'));
      isInitializingChart = false;
      topologyChart = null;
    }
  };
  
  // 等待 dialog 动画完成后再初始化
  setTimeout(() => {
    if (showTopologyDialog.value) {
      doInit();
    } else {
      isInitializingChart = false;
    }
  }, 600);
};



// 生成拓扑图数据
const generateTopologyData = () => {
  const nodes = [];
  const links = [];
  const nodeMap = new Map();

  // 检查是否有真实数据
  const hasRealData = references.value && references.value.length > 0;
  
  // 如果没有真实数据，生成假数据用于演示（根据图片样式）
  if (!hasRealData) {
    // 创建中心节点 - Objects
    const centerNode = {
      name: 'Objects',
      nodeId: 'i=85',
      category: 0,
      icon: '-', // 中心节点使用减号
      itemStyle: {
        color: '#409EFF'
      }
    };
    nodes.push(centerNode);
    nodeMap.set(centerNode.nodeId, centerNode);

    // 创建假的目标节点和引用（根据图片）
    const mockReferences = [
      { name: 'Locations', nodeId: 'i=1001', referenceType: 'Organizes', icon: '+' },
      { name: 'Root', nodeId: 'i=1002', referenceType: 'Organizes', icon: '+' },
      { name: 'Server', nodeId: 'i=1003', referenceType: 'Organizes', icon: '+' },
      { name: 'DeviceSet', nodeId: 'i=1004', referenceType: 'Organizes', icon: '-' },
      { name: 'Aliases', nodeId: 'i=1005', referenceType: 'Organizes', icon: '+' },
      { name: 'AutomationMLInstanceHierarchies', nodeId: 'i=1006', referenceType: 'Organizes', icon: '+' },
      { name: 'FolderType', nodeId: 'i=2004', referenceType: 'HasTypeDefinition', icon: '+' }
    ];

    mockReferences.forEach((mockRef) => {
      if (!nodeMap.has(mockRef.nodeId)) {
        const targetNode = {
          name: mockRef.name,
          nodeId: mockRef.nodeId,
          category: 1,
          icon: mockRef.icon || '+',
          itemStyle: {
            color: '#409EFF' // 所有节点统一为蓝色
          }
        };
        nodes.push(targetNode);
        nodeMap.set(mockRef.nodeId, targetNode);
      }

      // 创建链接
      const link = {
        source: centerNode.nodeId,
        target: mockRef.nodeId,
        referenceType: mockRef.referenceType,
        value: mockRef.referenceType
      };
      links.push(link);
    });

    return { nodes, links };
  }

  // 处理真实数据
  // 获取中心节点（当前选中的节点）
  let centerNode = null;
  if (selectedReferenceNode.value) {
    const centerNodeId = selectedReferenceNode.value.nodeIdNum || selectedReferenceNode.value.nodeId || 'center';
    centerNode = {
      name: selectedReferenceNode.value.name || selectedReferenceNode.value.label || selectedReferenceNode.value.nodeIdNum || '中心节点',
      nodeId: String(centerNodeId),
      id: String(centerNodeId), // 确保有 id 字段，ECharts 需要
      category: 0,
      icon: '-', // 中心节点使用减号
      itemStyle: {
        color: '#409EFF'
      }
    };
    nodes.push(centerNode);
    nodeMap.set(String(centerNodeId), centerNode);
  } else if (references.value.length > 0) {
    // 如果没有选中的节点，使用第一个引用的源节点
    centerNode = {
      name: '中心节点',
      nodeId: 'center',
      id: 'center', // 确保有 id 字段，ECharts 需要
      category: 0,
      icon: '-',
      itemStyle: {
        color: '#409EFF'
      }
    };
    nodes.push(centerNode);
    nodeMap.set('center', centerNode);
  }

  if (!centerNode) {
    return { nodes: [], links: [] };
  }

  // 处理引用数据
  references.value.forEach((ref, index) => {
    // 确保 targetNodeId 是字符串类型
    let targetNodeId = ref.targetNodeId;
    if (!targetNodeId) {
      targetNodeId = `target_${index}`;
    } else if (typeof targetNodeId !== 'string') {
      // 如果是对象，尝试转换为字符串
      targetNodeId = targetNodeId.toString();
    }
    
    const targetName = ref.targetDisplayName || ref.browseName || `节点${index + 1}`;
    
    // 如果目标节点不存在，创建它
    if (!nodeMap.has(targetNodeId)) {
      const targetNode = {
        name: targetName,
        nodeId: targetNodeId,
        id: targetNodeId, // 确保有 id 字段，ECharts 需要
        category: 1,
        icon: '+', // 目标节点使用加号
        itemStyle: {
          color: '#409EFF' // 统一为蓝色
        }
      };
      nodes.push(targetNode);
      nodeMap.set(targetNodeId, targetNode);
    }

    // 创建链接 - 确保 source 和 target 都是字符串
    const link = {
      source: String(centerNode.nodeId || centerNode.id),
      target: String(targetNodeId),
      referenceType: ref.referenceTypeName || ref.referenceTypeId || 'Reference',
      value: ref.referenceTypeId || ''
    };
    links.push(link);
  });

  return { nodes, links };
};

// 销毁拓扑图
const destroyTopologyChart = () => {
  if (topologyChart) {
    // 移除 resize 事件监听
    if (topologyChart._resizeHandler) {
      window.removeEventListener('resize', topologyChart._resizeHandler);
    }
    try {
      topologyChart.dispose();
    } catch (error) {
      console.error('销毁拓扑图失败:', error);
    }
    topologyChart = null;
  }
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
           name:item._displayName?._text,
           typeLookupSuccess: translateTypes !== null ,// 标记查找是否成功
           dataType:temp[0]?._displayName?._text,
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
          item.typeDefinitionId = item.typeDefinition?._nodeId?.toString()
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
const handleChangeFormValue = useThrottleFn(async() => {
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
          item.typeDefinitionId = item.typeDefinition?._nodeId?.toString()
        })
        state.bottomTreeData = allArr
        // return resolve(allArr);
     });
       }
    })
  }
}, 1000);

const deleteNodes = () => {
  console.log('删除');
};

const renameNode = () => {
  console.log('重命名');
};

const handleMessageModal = () => {
  console.log('成员');
};

// 防抖定时器和当前处理节点跟踪
let handleNodeDblClickTimer = null;
const currentProcessingNodeId = ref(null);

// 处理变量数据的独立函数
const processVariablesData = async (allArr, cacheUrl, continuationPoint) => {
  // 保存 continuationPoint，用于后续分页加载
  variablesContinuationPoint.value = continuationPoint || null;
  console.log(allArr,'allArr')
  // 记录加载前的数据数量（用于滚动定位）
  previousVariablesCount.value = 0;
  
  state.nodeDetailsArr = allArr;
  if(!allArr || allArr.length == 0){
    state.mergedNodeData = [];
    state.loadingVariables = false;
    currentProcessingNodeId.value = null;
    return;
  }
  
  // 初始化 OPC UA 客户端
  let apiConfig = new Configuration({
    basePath: cacheUrl
  });
  let clientConfig = new UaClientConfiguration(apiConfig);
  let testOpcServer = new UaWebClient(clientConfig);
  
  let readNodeIds = [];
  allArr.map((item)=>{
    let nodeid = item.nodeId;
    // item.nodeId 是 UaExpandedNodeId，需要提取其中的 _nodeId
    readNodeIds.push(nodeid._nodeId);
  });
  
  // 添加错误处理，避免读取没有数据的节点
  let current = [];
  try {
    current = await testOpcServer.readValues(readNodeIds);
  } catch (error) {
    // 如果读取失败，创建一个与 allArr 长度相同的空数组
    current = new Array(allArr.length).fill(null);
    // 标记发生读取错误，不显示 "..." 按钮
    variablesReadError.value = true;
    // 清空 continuationPoint，避免显示加载更多按钮
    variablesContinuationPoint.value = null;
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
      // let idxs = temp[0].nodeId
      let dataTypeDictionary = new UaDataTypeDictionary();
      let dataTypes = dataTypeDictionary.getDataType(currentID); 
      let obj= {
        ...item,
        value: current[index]?.value || null,
        originalType: typeValue, // 保存原始类型值以便调试
        dataValue:dataValue,
        UserWriteMask:currentID.userAccessLevel,
        AccessLevel: currentID.accessLevel ?? currentID.userAccessLevel ?? null,
        Historizing: currentID.historizing,
        name: getDisplayName(item) || item.browseName,
        typeLookupSuccess: translateTypes !== null ,// 标记查找是否成功
        dataType:temp[0]?._displayName?._text || '',
        dataTypesObj:temp[0],
        dataTypes:temp[0]?._parentType?._browseName ?temp[0]?._parentType?._browseName : (temp[0]?.browseName || 'Unknown'),
        nodeId:item.nodeId.toString(),
        _editValue: (() => {
          let value = (current[index]?.value && typeof current[index]?.value === 'object')
          ? current[index]?.value?.value
            : current[index]?.value;
          // if(Array.isArray(value)){
          //    value = value.map(item => item._dataTypeId).join(',');
          // }
          
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
      };

      // 使用 isSubtypeOf 判断数据类型并处理 value 展示
      if (obj.dataTypesObj && typeof obj.dataTypesObj.isSubtypeOf === 'function') {
        try {
          // 判断是否是 Enumeration 类型的子类型
          const enumerationNodeId = new UaNodeId(DataTypeIds.Enumeration);
          if (obj.dataTypesObj.isSubtypeOf(enumerationNodeId)) {
            // Enumeration 类型：使用 UaLocalizedText.text() 获取枚举值文本
            if (obj.dataTypesObj._enumValues && obj.dataTypesObj._enumValues.size > 0) {
              // 获取原始数字值
              const numValue = Number(obj._editValue);
              const localizedText = obj.dataTypesObj._enumValues.get(numValue);
              if (localizedText) {
                // 使用 UaLocalizedText 获取文本，格式为 "数字（文本）"
                const text = typeof localizedText.text === 'function' ? localizedText.text() : (localizedText._text || String(localizedText));
                obj._editValue = `${numValue}（${text}）`;
              }
            }
          }
        } catch (e) {
          console.warn('isSubtypeOf check for Enumeration failed:', e);
        }
      }

      if(obj.dataTypes === 'Boolean' && !Array.isArray(obj._editValue?.value)) {
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
            obj._editValue = obj.value?.value ?? '';
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
        const str = obj._editValue?.value;
        // 只有当 str 不为 null/undefined 时才转换
        if (str != null) {
          const hexString = stringToHex(str);
          obj._editValue = hexString;
        }
      }
      if(!obj.dataType  && Array.isArray(obj._editValue)){
        console.log(232,obj._editValue,obj.value?.type)
        if(obj.value?.type == UaVariantType.ExtensionObject){
          // 辅助函数：格式化值，确保对象能正确显示
          const formatValue = (val) => {
            if (val === null) return 'null';
            if (val === undefined) return 'undefined';
            if (typeof val === 'string') return val;
            if (typeof val === 'number' || typeof val === 'boolean') return String(val);
            if (Array.isArray(val)) {
              return `[${val.map(formatValue).join(', ')}]`;
            }
            if (typeof val === 'object') {
              try {
                // 尝试使用 JSON.stringify，如果失败则手动格式化
                return JSON.stringify(val, null, 2);
              } catch (e) {
                // 如果有循环引用等问题，手动构建字符串
                const entries = Object.entries(val).map(([k, v]) => `${k}: ${formatValue(v)}`);
                return `{${entries.join(', ')}}`;
              }
            }
            return String(val);
          };

          // 此处 obj._editValue 本身就是一个数组（Array of ExtensionObject）
          const extArray = obj._editValue;
          const displayName = extArray.map((item, index) => {
            // 每个元素可能是 UaExtensionObject，优先取 body，没有就用元素本身
            const body = (item && typeof item === 'object' && 'body' in item) ? item.body : item;

            if (body && typeof body === 'object' && !Array.isArray(body)) {
              const entries = Object.entries(body).map(([k, v]) => `${k}：${formatValue(v)}`);
              return `[${index}]：${entries.join('，')}`;
            } else {
              return `[${index}]：${formatValue(body)}`;
            }
          }).join('；');

          obj._editValue = displayName;
        }else{
          // 非 ExtensionObject 数组，保持原有逻辑
          obj._editValue = obj._editValue?.map(item => item._dataTypeId).join(',');

        }
      } 
      // if(obj.dataType == "Structure"){
      //   obj._editValue = JSON.stringify(obj._editValue);
      // }
      if (obj.dataTypes == "Structure" || obj.value?.type == UaVariantType.ExtensionObject && obj.value?.isScalar()) {
              // 检查是否是 UaExtensionObject 类型
              if (obj._editValue && typeof obj._editValue === 'object') {
                // 尝试获取 payload（UaExtensionObject 有 payload getter）
                if (obj._editValue.payload !== undefined) {
                  const payload = obj._editValue.payload;
                  // 优先显示 DisplayName.Text，如果没有则显示其他关键信息
                  if (payload?.DisplayName?.Text) {
                    // obj._editValue = payload.DisplayName.Text;
                    obj._editValue =payload ? Object.entries(payload).map(([k, v]) => `${k}：${JSON.stringify(v)}`).join('，') : '';
                  } else if (payload?.DisplayName) {
                    obj._editValue = typeof payload.DisplayName === 'string' 
                      ? payload.DisplayName 
                      : payload.DisplayName.Text || JSON.stringify(payload);
                  } else {
                    // 如果没有 DisplayName，使用 toJson() 方法或直接序列化 payload
                    try {
                      if (typeof obj._editValue.toJson === 'function') {
                        obj._editValue = JSON.parse(JSON.stringify(obj._editValue)) || '';
                        obj._editValue =obj._editValue?._payload ? Object.entries(obj._editValue?._payload).map(([k, v]) => `${k}：${v}`).join('，') : '';
// 得到 "Low：0，High：100"
                      } else {
                        obj._editValue = JSON.stringify(payload, null, 2);
                      }
                    } catch (e) {
                      obj._editValue = JSON.stringify(payload);
                    }
                  }
                } else if (obj._editValue._payload !== undefined) {
                  // 如果 payload 是私有属性，尝试直接访问 _payload
                  const payload = obj._editValue._payload;
                  if (payload?.DisplayName?.Text) {
                    obj._editValue = payload.DisplayName.Text;
                  } else if (payload?.DisplayName) {
                    obj._editValue = typeof payload.DisplayName === 'string' 
                      ? payload.DisplayName 
                      : payload.DisplayName.Text || JSON.stringify(payload);
                  } else {
                    try {
                      if (typeof obj._editValue.toJson === 'function') {
                        obj._editValue = JSON.stringify(obj._editValue.toJson(), null, 2);
                      } else {
                        obj._editValue = JSON.stringify(payload, null, 2);
                      }
                    } catch (e) { 
                      obj._editValue = JSON.stringify(payload);
                    }
                  }
                } else {
                  // 如果不是 UaExtensionObject，尝试序列化整个对象
                  try {
                    // 辅助函数：格式化值，确保对象能正确显示
                    const formatValue = (val) => {
                      if (val === null) return 'null';
                      if (val === undefined) return 'undefined';
                      if (typeof val === 'string') return val;
                      if (typeof val === 'number' || typeof val === 'boolean') return String(val);
                      if (Array.isArray(val)) {
                        return `[${val.map(formatValue).join(', ')}]`;
                      }
                      if (typeof val === 'object') {
                        try {
                          // 尝试使用 JSON.stringify，如果失败则手动格式化
                          return JSON.stringify(val, null, 2);
                        } catch (e) {
                          // 如果有循环引用等问题，手动构建字符串
                          const entries = Object.entries(val).map(([k, v]) => `${k}: ${formatValue(v)}`);
                          return `{${entries.join(', ')}}`;
                        }
                      }
                      return String(val);
                    };
                    
                    const bodyObj = obj._editValue?.body;
                    if (bodyObj && typeof bodyObj === 'object') {
                      if (Array.isArray(bodyObj)) {
                        // 如果是数组，遍历数组中的每个元素
                        const displayName = bodyObj.map((item, index) => {
                          if (item && typeof item === 'object' && !Array.isArray(item)) {
                            const entries = Object.entries(item).map(([k, v]) => `${k}：${formatValue(v)}`);
                            return `[${index}]：${entries.join('，')}`;
                          } else {
                            return `[${index}]：${formatValue(item)}`;
                          }
                        }).join('；');
                        obj._editValue = displayName;
                      } else {
                        // 如果不是数组，按原来的方式处理
                        const displayName = Object.entries(bodyObj).map(([k, v]) => `${k}：${formatValue(v)}`).join('，');
                        obj._editValue = displayName;
                      }
                    } else {
                      obj._editValue = bodyObj ? formatValue(bodyObj) : String(obj._editValue);
                    }
                    // obj._editValue = JSON.stringify(obj._editValue, null, 2);
                  } catch (e) {
                    obj._editValue = String(obj._editValue);
                  }
                }
              }
            }
      if(obj.dataType == 'Argument'){
        let arr = obj.value?.value;
        let result = arr.map(item => Object.entries(item._payload)
                     .map(([k, v]) => `${k}:${ JSON.stringify(v)}`)
                     .join(',')).join(',')
        obj._editValue = result;
      }

      // 返回处理后的对象
      console.log(obj,'---------')
      return obj;
    }catch(err){
      console.log(err);
      return null;
    }
  });

  // 处理所有数据并推送到 mergedNodeData
  mergedArray.forEach(obj => {
    if (obj) {
      state.mergedNodeData.push(obj);
    }
  });
  console.log(mergedArray,'mergedArray')
  // 数据完全展示后再结束 loading
  state.loadingVariables = false;
  currentProcessingNodeId.value = null;
  if(state.mergedNodeData.length == 0   || state.mergedNodeData.length == null){
    ElMessage.warning('未获取到详细信息');
  }
  
  // 滚动到新加载的数据行（如果是首次加载，会滚动到顶部）
  scrollToNewVariablesData();
};

const handleNodeDblClick =async (node, data,url) => {
    // 标记正在双击处理，阻止单击事件触发 detailsMessage
    isDoubleClicking.value = true;
    
    // 清除单击处理定时器，防止单击事件触发
    if (handleNodeClick2Timer) {
        clearTimeout(handleNodeClick2Timer);
        handleNodeClick2Timer = null;
    }

    // 防止重复调用：如果正在加载，直接返回
    if (state.loadingVariables) {
        console.log('正在加载中，忽略重复调用');
        isDoubleClicking.value = false;
        return;
    }

    // 获取当前节点的唯一标识
    const currentNodeId = data.nodeId?._nodeId?.toString() || data.nodeId?.toString();
    
    // 如果正在处理同一个节点，直接返回
    if (currentProcessingNodeId.value === currentNodeId) {
        console.log('正在处理该节点，忽略重复调用');
        isDoubleClicking.value = false;
        return;
    }

    // 清除之前的防抖定时器
    if (handleNodeDblClickTimer) {
        clearTimeout(handleNodeDblClickTimer);
        handleNodeDblClickTimer = null;
    }

    // 设置防抖延迟（300ms）
    handleNodeDblClickTimer = setTimeout(async () => {
        try {
            // 标记当前正在处理的节点
            currentProcessingNodeId.value = currentNodeId;

            selectedNodeId.value = data.nodeId
            // 安全地访问 _nodeId 属性
            if (selectedNodeId.value && selectedNodeId.value._nodeId) {
                selectedNodeId.value.nodeId = selectedNodeId.value._nodeId
            } else {
                console.warn('无法获取 nodeId 信息:', data);
                currentProcessingNodeId.value = null;
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
     currentProcessingNodeId.value = null;
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
      
      // detailsMessage(tempId,cacheUrl)
      
      // 保存 OPC 服务器实例，用于后续分页加载
      currentVariablesOpcServer.value = testOpcServer;
      
      // 重置分页状态
      variablesContinuationPoint.value = null;
      previousVariablesCount.value = 0;
      variablesReadError.value = false; // 重置错误标志
      console.log(cacheUrl,'cacheUrl')
      getRightDetailsBrowseDatas(cacheUrl).then(async (firstRes) => {
        let allArr = firstRes?.results || []; // 只加载第一页（10条）
        let continuationPoint = firstRes?.continuationPoint;
        // 调用独立的处理函数
        await processVariablesData(allArr, cacheUrl, continuationPoint);
      }).catch((error) => {
          // Promise 错误处理
          console.error('getRightDetailsBrowseDatas error:', error);
          state.mergedNodeData = [];
          state.loadingVariables = false;
          currentProcessingNodeId.value = null;
          ElMessage.warning('加载数据失败，请重试');
      });
    }
    catch(err){
        state.mergedNodeData = [];
        state.loadingVariables = false
        currentProcessingNodeId.value = null;
        ElMessage.warning('未获取到详细信息');
    }
    } catch (error) {
        // 外层 try-catch 的错误处理
        state.loadingVariables = false;
        currentProcessingNodeId.value = null;
        console.error('handleNodeDblClick error:', error);
    } finally {
        // 重置双击状态，允许下次单击
        setTimeout(() => {
            isDoubleClicking.value = false;
        }, 350); // 稍微延迟，确保单击处理已完成
    }
    }, 300); // 300ms 防抖延迟
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
   
   // 保存 OPC 服务器实例，用于后续分页加载
   currentVariablesOpcServer.value = testOpcServer;
   
   // 重置分页状态
   variablesContinuationPoint.value = null;
   previousVariablesCount.value = 0;
   variablesReadError.value = false; // 重置错误标志
   
   getRightDetailsBrowseDatas(cacheUrl).then(async (firstRes) => {
     let allArr = firstRes?.results || []; // 只加载第一页（10条）
     let continuationPoint = firstRes?.continuationPoint;
     // 调用独立的处理函数
     await processVariablesData(allArr, cacheUrl, continuationPoint);
   }).catch((error) => {
     console.error('getRightDetailsBrowseDatas error:', error);
     state.mergedNodeData = [];
     state.loadingVariables = false;
     ElMessage.warning('加载数据失败，请重试');
   });
 }
 catch(err){
     state.mergedNodeData = [];
     state.loadingVariables = false
     ElMessage.warning('未获取到详细信息');
 }
};

// 加载下一页 Variables 数据
const loadNextPageVariables = async () => {
  if (!currentVariablesOpcServer.value) {
    ElMessage.warning('OPC 服务器连接不可用');
    return;
  }

  if (!variablesContinuationPoint.value) {
    ElMessage.info('没有更多数据了');
    return;
  }

  state.loadingVariables = true;

  try {
    // 调用 browseNextByCP 加载下一页
    const nextResult = await currentVariablesOpcServer.value.browseNextByCP(variablesContinuationPoint.value, false);
    
    if (!nextResult || !nextResult.results || nextResult.results.length === 0) {
      state.loadingVariables = false;
      variablesContinuationPoint.value = null;
      return;
    }
    
    // 更新 continuationPoint
    variablesContinuationPoint.value = nextResult.continuationPoint || null;
    
    // 记录加载前的数据数量
    previousVariablesCount.value = state.mergedNodeData.length;
    
    // 处理新的变量数据
    let newArr = nextResult.results || [];
    state.nodeDetailsArr = [...state.nodeDetailsArr, ...newArr];
    
    if (newArr.length === 0) {
      state.loadingVariables = false;
      return;
    }
    
    // 初始化 OPC UA 客户端（使用已保存的实例）
    let testOpcServer = currentVariablesOpcServer.value;
    
    let readNodeIds = [];
    newArr.map((item) => {
      let nodeid = item.nodeId;
      readNodeIds.push(nodeid._nodeId);
    });
    
    // 添加错误处理，避免读取没有数据的节点
    let current = [];
    try {
      current = await testOpcServer.readValues(readNodeIds);
    } catch (error) {
      console.warn('Error reading values for nodes:', error);
      current = new Array(newArr.length).fill(null);
      // 标记发生读取错误，不显示 "..." 按钮
      variablesReadError.value = true;
      // 清空 continuationPoint，避免显示加载更多按钮
      variablesContinuationPoint.value = null;
    }
    
    // 获取所有节点的 nodeId，并创建映射关系
    let variableNodeIds = [];
    let nodeIdToIndexMap = new Map();
    
    newArr.forEach((item, index) => {
      if (item.nodeId?._nodeId) {
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
      allVariableAttributes = new Array(variableNodeIds.length).fill(null);
    }
    
    // 合并 newArr 和 current 数组
    let mergedArray = newArr.map((item, index) => {
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
     
      try {
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
        let temp = state.allTypesData.filter((idx) => {
          let typeId = idx.nodeId.toString();
          let dataTypeId = currentID.dataType ? currentID.dataType.toString() : '';
          return typeId === dataTypeId;
        });
        let idxs = temp[0].nodeId;
        let dataTypeDictionary = new UaDataTypeDictionary();
        let dataTypes = dataTypeDictionary.getDataType(currentID); 
        let obj = {
          ...item,
          value: current[index]?.value || null,
          originalType: typeValue,
          dataValue: dataValue,
          UserWriteMask: currentID.userAccessLevel,
          AccessLevel: currentID.accessLevel ?? currentID.userAccessLevel ?? null,
          Historizing: currentID.historizing,
          name: item._displayName?._text,
          typeLookupSuccess: translateTypes !== null,
          dataType: temp[0]?._browseName || '',
          dataTypesObj: temp[0],
          dataTypes: temp[0]?._parentType?._browseName ? temp[0]?._parentType?._browseName : (temp[0]?.browseName || 'Unknown'),
          nodeId: item.nodeId.toString(),
          _editValue: (() => {
            let value = (current[index]?.value && typeof current[index]?.value === 'object')
              ? current[index]?.value?.value
              : current[index]?.value;
            
            // 特殊处理 DateTime 类型
            if (temp[0]?._browseName === 'DateTime' && value) {
              if (typeof value === 'string' || typeof value === 'number') {
                const dateValue = new Date(value);
                if (!isNaN(dateValue.getTime())) {
                  return value;
                }
              }
              return value;
            }
            
            // 特殊处理 Int16/Int32/Int64 类型，确保正确提取数值
            const dataTypeName = temp[0]?._browseName || '';
            if (dataTypeName === 'Int16' || dataTypeName === 'Int32' || dataTypeName === 'Int64') {
              // 如果 value 是对象，尝试提取 value.value
              if (value && typeof value === 'object' && value.value !== undefined) {
                value = value.value;
              }
              // 确保返回的是数字或数字数组
              if (Array.isArray(value)) {
                return value.map(v => {
                  if (v && typeof v === 'object' && v.value !== undefined) {
                    return v.value;
                  }
                  return typeof v === 'number' ? v : (typeof v === 'string' ? parseFloat(v) : v);
                });
              }
              // 单个值的情况
              if (value && typeof value === 'object' && value.value !== undefined) {
                return value.value;
              }
              // 如果是字符串，尝试转换为数字
              if (typeof value === 'string' && !isNaN(parseFloat(value))) {
                return parseFloat(value);
              }
              return typeof value === 'number' ? value : value;
            }
            
            return value;
          })(),
          _isSelected: false,
          _isEditing: false
        };
        if (obj.dataTypes === 'Boolean' && !Array.isArray(obj._editValue.value)) {
          obj.enumStrings = [false, true];
        }
        if (obj.dataTypes === 'ByteString') {
          if (obj.value && Array.isArray(obj.value)) {
            obj._editValue = obj.value.map(item => {
              if (typeof item === 'string') {
                return byteStringToHex(item);
              }
              return item;
            });
          } else if (obj.value) {
            if (typeof obj.value === 'string') {
              obj._editValue = byteStringToHex(obj.value);
            } else {
              obj._editValue = obj.value?.value ?? obj.value ?? '';
            }
          }
        }
        
        if (obj.dataTypes === 'ExpandedNodeId' && !Array.isArray(obj._editValue.value)) {
          if (obj.value && Array.isArray(obj.value)) {
            obj._editValue = obj.value.map(item => {
              const parsed = parseExpandedNodeId(item);
              return expandedNodeIdToString(parsed);
            });
          } else if (obj.value) {
            const parsed = parseExpandedNodeId(obj.value);
            obj._editValue = expandedNodeIdToString(parsed);
          }
        }
        if (obj.dataTypes === "ImagePNG" || obj.dataTypes === "Image") {
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
        if (obj.dataTypes === "ByteString" && !Array.isArray(obj._editValue.value)) {
          const str = obj._editValue.value;
          const hexString = stringToHex(str);
          obj._editValue = hexString;
        }
        // if(obj.dataType == "Structure"){
        //   obj._editValue = JSON.stringify(obj._editValue);
        // }
        if (obj.dataTypes == "Structure" || obj.value?.type == UaVariantType.ExtensionObject && obj.value?.isScalar()) {
              // 检查是否是 UaExtensionObject 类型
              if (obj._editValue && typeof obj._editValue === 'object') {
                // 尝试获取 payload（UaExtensionObject 有 payload getter）
                if (obj._editValue.payload !== undefined) {
                  const payload = obj._editValue.payload;
                  // 优先显示 DisplayName.Text，如果没有则显示其他关键信息
                  if (payload?.DisplayName?.Text) {
                    obj._editValue =payload ? Object.entries(payload).map(([k, v]) => `${k}：${JSON.stringify(v)}`).join('，') : '';
                  } else if (payload?.DisplayName) {
                    obj._editValue = typeof payload.DisplayName === 'string' 
                      ? payload.DisplayName 
                      : payload.DisplayName.Text || JSON.stringify(payload);
                  } else {
                    // 如果没有 DisplayName，使用 toJson() 方法或直接序列化 payload
                    try {
                      if (typeof obj._editValue.toJson === 'function') {
                        obj._editValue = JSON.parse(JSON.stringify(obj._editValue)) || '';
                        obj._editValue =obj._editValue?._payload ? Object.entries(obj._editValue?._payload).map(([k, v]) => `${k}：${v}`).join('，') : '';
// 得到 "Low：0，High：100"
                      } else {
                        obj._editValue = JSON.stringify(payload, null, 2);
                      }
                    } catch (e) {
                      obj._editValue = JSON.stringify(payload);
                    }
                  }
                } else if (obj._editValue._payload !== undefined) {
                  // 如果 payload 是私有属性，尝试直接访问 _payload
                  const payload = obj._editValue._payload;
                  if (payload?.DisplayName?.Text) {
                    obj._editValue = payload.DisplayName.Text;
                  } else if (payload?.DisplayName) {
                    obj._editValue = typeof payload.DisplayName === 'string' 
                      ? payload.DisplayName 
                      : payload.DisplayName.Text || JSON.stringify(payload);
                  } else {
                    try {
                      if (typeof obj._editValue.toJson === 'function') {
                        obj._editValue = JSON.stringify(obj._editValue.toJson(), null, 2);
                      } else {
                        obj._editValue = JSON.stringify(payload, null, 2);
                      }
                    } catch (e) { 
                      obj._editValue = JSON.stringify(payload);
                    }
                  }
                } else {
                  // 如果不是 UaExtensionObject，尝试序列化整个对象
                  try {
                     let arr = obj.value?.value;
                     let result = arr.map(item => Object.entries(item._payload)
                     .map(([k, v]) => `${k}:${ JSON.stringify(v)}`)
                     .join(',')).join(',')
                    obj._editValue = result;
                  } catch (e) {
                    obj._editValue = String(obj._editValue);
                  }
                }
              }
        }
        return obj;
      } catch (err) {
        console.log(err);
        return null;
      }
    });
    
    // 追加到现有变量列表
    mergedArray.forEach(obj => {
      if (obj) {
        state.mergedNodeData.push(obj);
      }
    });
    
    state.loadingVariables = false;
    ElMessage.success(`已加载 ${mergedArray.filter(obj => obj !== null).length} 个变量，共 ${state.mergedNodeData.length} 个`);
    
    // 滚动到新加载的数据行
    scrollToNewVariablesData();
  } catch (error) {
    console.error('加载下一页变量失败:', error);
    state.loadingVariables = false;
    ElMessage.error('加载下一页变量失败: ' + (error.message || '未知错误'));
  }
};

// 滚动到新加载的 Variables 数据行
const scrollToNewVariablesData = () => {
  nextTick(() => {
    // 确保 variablesTableBodyRef 是 DOM 元素
    const tableBody = variablesTableBodyRef.value;
    
    // 如果 ref 未绑定或不是 DOM 元素，尝试通过 DOM 查询
    if (!tableBody || typeof tableBody.getBoundingClientRect !== 'function') {
      const domTableBody = document.querySelector('.table-boxs.smooth-scroll');
      if (!domTableBody || typeof domTableBody.getBoundingClientRect !== 'function') {
        console.warn('无法找到 Variables 表格容器');
        return;
      }
      
      // 如果首次加载，滚动到顶部
      if (previousVariablesCount.value === 0) {
        domTableBody.scrollTop = 0;
        return;
      }
      
      // 查找新加载的第一行数据
      const newDataStartIndex = previousVariablesCount.value;
      const allRows = domTableBody.querySelectorAll('.property-row');
      const domTargetRow = allRows[newDataStartIndex];
      if (domTargetRow) {
        const containerRect = domTableBody.getBoundingClientRect();
        const rowRect = domTargetRow.getBoundingClientRect();
        const scrollTop = domTableBody.scrollTop;
        const relativeTop = rowRect.top - containerRect.top + scrollTop;
        
        domTableBody.scrollTo({
          top: Math.max(0, relativeTop - 10),
          behavior: 'smooth'
        });
        console.log(`滚动到新变量数据行（通过DOM查询），索引: ${newDataStartIndex}`);
      }
      return;
    }
    
    // 如果首次加载，滚动到顶部
    if (previousVariablesCount.value === 0) {
      tableBody.scrollTop = 0;
      return;
    }
    
    // 查找新加载的第一行数据
    const newDataStartIndex = previousVariablesCount.value;
    const targetRow = variablesTableRowRefs.value[newDataStartIndex];
    
    // 如果通过 ref 找不到，尝试通过 DOM 查询
    if (!targetRow) {
      const allRows = tableBody.querySelectorAll('.property-row');
      const domTargetRow = allRows[newDataStartIndex];
      if (domTargetRow) {
        const containerRect = tableBody.getBoundingClientRect();
        const rowRect = domTargetRow.getBoundingClientRect();
        const scrollTop = tableBody.scrollTop;
        const relativeTop = rowRect.top - containerRect.top + scrollTop;
        
        tableBody.scrollTo({
          top: Math.max(0, relativeTop - 10),
          behavior: 'smooth'
        });
        console.log(`滚动到新变量数据行（通过DOM查询），索引: ${newDataStartIndex}`);
      }
      return;
    }
    
    // 计算目标行相对于滚动容器的位置
    const containerRect = tableBody.getBoundingClientRect();
    const rowRect = targetRow.getBoundingClientRect();
    const scrollTop = tableBody.scrollTop;
    const relativeTop = rowRect.top - containerRect.top + scrollTop;
    
    // 滚动到目标位置，留一些顶部间距
    tableBody.scrollTo({
      top: Math.max(0, relativeTop - 10),
      behavior: 'smooth'
    });
    
    console.log(`滚动到新变量数据行，索引: ${newDataStartIndex}`);
  });
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
    // 添加空值检查，防止传入 null 或 undefined
    if (str === null || str === undefined) {
        return '';
    }
    
    // 确保 str 是字符串类型
    if (typeof str !== 'string') {
        str = String(str);
    }

    let result = '';
    for (let i = 0; i < str.length; i++) {
        const charCode = str.charCodeAt(i);
        // 只取低8位，确保是字节值
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
const handleExpandedNodeIdDialogConfirm = useThrottleFn(async () => {
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
      
      // 调用 writeValues 写入 OPC UA
      // 将 showExpandedNodeIdData 转换为 UaExpandedNodeId 数组
      const expandedNodeIds = showExpandedNodeIdData.map(item => {
        const identifierType = item.identifierType === 'Numeric' ? UaNodeIdType.NUMERIC : 
                              item.identifierType === 'String' ? UaNodeIdType.STRING :
                              item.identifierType === 'Guid' ? UaNodeIdType.GUID :
                              item.identifierType === 'ByteString' ? UaNodeIdType.BYTESTRING :
                              UaNodeIdType.NUMERIC;
        const value = item.identifierType === 'Numeric' ? parseInt(item.value) : item.value;
        const nsIndex = item.nsIndex !== undefined ? parseInt(item.nsIndex) : 0;
        const serverIndex = item.serverIndex !== undefined ? parseInt(item.serverIndex) : 0;
        const namespaceUri = item.namespaceUri || null;
        
        return new UaExpandedNodeId(value, nsIndex, identifierType, namespaceUri, serverIndex);
      });
      
      const isArray = expandedNodeIds.length > 1;
      let uaVariant;
      if (isArray) {
        uaVariant = UaVariant.expandedNodeIds(expandedNodeIds);
      } else {
        uaVariant = UaVariant.expandedNodeId(expandedNodeIds[0]);
      }
      
      await writeValueToOpcUa(uaVariant, 'ExpandedNodeId', 'ExpandedNodeId写入成功！');
      
    } catch (error) {
      console.error('ExpandedNodeId更新失败:', error);
      ElMessage.error('ExpandedNodeId更新失败: ' + (error.message || '未知错误'));
      return;
    }
  }
  
  showExpandedNodeIdFlag.value = false;
  currentEditingItem.value = null; // 清除当前编辑的 item
}, 1000);

// 处理 ImagePNG 变化
const handleImagePNGChange = (index, value) => {
  showImagePNGData[index] = value;
  if (currentEditingItem.value) {
    currentEditingItem.value._editValue = [...showImagePNGData];
  }
};

// 处理 ImagePNG dialog 确认
const handleImagePNGDialogConfirm = useThrottleFn(async () => {
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
      
      // 调用 writeValues 写入 OPC UA
      // ImagePNG 是 ByteString 类型
      const isArray = binaryValues.length > 1;
      let uaVariant;
      if (isArray) {
        uaVariant = UaVariant.byteStrings(binaryValues);
      } else {
        uaVariant = UaVariant.byteString(binaryValues[0]);
      }
      
      await writeValueToOpcUa(uaVariant, 'ImagePNG', 'ImagePNG写入成功！');
      
    } catch (error) {
      console.error('ImagePNG更新失败:', error);
      ElMessage.error('ImagePNG更新失败: ' + (error.message || '未知错误'));
      return;
    }
  }
  showImagePNGFlag.value = false;
  currentEditingItem.value = null;
}, 1000);

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
        label: `${enumValue.value} (${getDisplayName(enumValue) || 'Unknown'})`
      }));
    }
  }
  
  return [];
};

// Method Call 相关函数
const handleMethodCall = async (methodNode, treeNode = null) => {
  currentMethodNode.value = methodNode;
  currentMethodTreeNode.value = treeNode; // 保存 tree node 对象
  
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
            dataType:  dataTypes?._displayName?._text  || '',
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
          dataType: dataTypes?._displayName?._text || '',
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
    console.log(methodOutputArgs,'methodOutputArgs')
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

// 处理重新浏览节点（Rebrowse）
const handleRebrowse = async (data, node) => {
  try {
    // 检查连接状态
    if (!connectFlag.value) {
      ElMessage.warning('请先连接到 OPC UA 服务器');
      return;
    }
    
    // 获取节点的 nodeIdNum，用于标识节点
    let nodeIdNum = data.nodeIdNum;
    if (!nodeIdNum && data.nodeId) {
      if (data.nodeId._nodeId) {
        nodeIdNum = data.nodeId._nodeId.toString();
      } else if (typeof data.nodeId.toString === 'function') {
        nodeIdNum = data.nodeId.toString();
      } else {
        nodeIdNum = String(data.nodeId);
      }
    }
    
    if (!nodeIdNum) {
      ElMessage.error('无法获取节点ID');
      return;
    }
    
    // 清除该节点的缓存
    if (nodeCache.value && typeof nodeCache.value.clearNode === 'function') {
      nodeCache.value.clearNode(nodeIdNum);
    }
    
    // 清除分层缓存中的该节点数据（如果存在分层缓存的清除方法）
    const nodeLevel = node.level || 0;
    // 注意：layeredCache 是内部实现，可能没有直接的清除方法，但我们可以尝试清除相关缓存
    
    // 清除该节点的 continuationPoint
    if (nodeContinuationPoints.value && nodeContinuationPoints.value.has(nodeIdNum)) {
      nodeContinuationPoints.value.delete(nodeIdNum);
    }
    
    // 清除 detailCardStore 中该节点的缓存
    if (detailCardStore && typeof detailCardStore.clearNodeDetailData === 'function') {
      detailCardStore.clearNodeDetailData(nodeIdNum);
    }
    
    // 获取树组件实例
    const tree = eltree.value;
    if (!tree) {
      ElMessage.error('无法获取树组件实例');
      return;
    }
    
    // 获取树节点对象
    const treeNode = tree.getNode(nodeIdNum);
    if (!treeNode) {
      ElMessage.error('无法找到树节点');
      return;
    }
    
    // 标记节点需要重新加载
    const wasExpanded = treeNode.expanded;
    
    // 重置节点状态，强制重新加载
    if (treeNode.data) {
      treeNode.data.children = [];
    }
    if (treeNode.childNodes && Array.isArray(treeNode.childNodes)) {
      treeNode.childNodes = [];
    }
    // 标记节点未加载，这样再次展开时会触发 loadNode
    treeNode.loaded = false;
    
    // 如果节点原本是展开的，需要先收起再展开以触发重新加载
    if (wasExpanded) {
      // 收起节点
      treeNode.collapse();
      
      // 等待 DOM 更新
      await nextTick();
      
      // 延迟展开，确保状态完全重置
      setTimeout(() => {
        // 展开节点以触发重新加载
        treeNode.expand();
        ElMessage.success('节点重新加载');
      }, 200);
    } else {
      // 节点未展开，直接标记为需要重新加载即可
      ElMessage.success('节点已重新加载，展开时将加载最新数据');
    }
    
  } catch (error) {
    console.error('重新浏览节点时出错:', error);
    ElMessage.error('重新浏览节点失败: ' + (error.message || '未知错误'));
  }
};
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
        label: getDisplayName(item) || `Value_${item.value}`,
        value: item.value
      }));
    } else if (Array.isArray(enumType.enumValues)) {
      // 如果是数组格式
      return enumType.enumValues.map(item => ({
        label: getDisplayName(item) || `Value_${item.value}`,
        value: item.value
      }));
    } else if (typeof enumType.enumValues === 'object') {
      // 如果是对象格式
      return Object.values(enumType.enumValues).map(item => ({
        label: getDisplayName(item) || `Value_${item.value}`,
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

const handleMethodOptionSetConfirm = useThrottleFn(() => {
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
}, 1000);

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
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.SByte);
      case 'Byte':
        console.log(UaVariant.integer(parseInt(value), UaVariantType.Byte),'-----5566')
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.Byte);
      case 'Int8':
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.SByte);
      case 'Int16':
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.Int16);
      case 'UInt16':
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.UInt16);
      case 'Int32':
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.Int32);
      case 'UInt32':
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.UInt32);
      case 'Int64':
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.Int64);
      case 'UInt64':
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.UInt64);
      case 'Integer':
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.Int32);
      case 'UInteger':
        // UInteger 应该使用 UInt32 类型（UaType = 7），而不是 Int32（UaType = 6）
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.UInt32);
      case 'Float':
        return UaVariant.float(parseFloat(value) || 0);
      case 'Double':
        return UaVariant.double(parseFloat(value) || 0);
      case 'Number':
        return UaVariant.double(parseFloat(value) || 0);
      case 'String':
        return UaVariant.string(value || '');
      case 'DateTime':
        return UaVariant.dateTime(value ? new Date(value) : new Date());
      case 'NodeId':
        // 如果 value 是字符串，需要解析为 UaNodeId
        if (typeof value === 'string') {
          try {
            // 使用 parseUaNodeId 解析字符串格式的 NodeId（如 "i=12873" 或 "ns=0;i=12873"）
            return UaVariant.nodeId(parseUaNodeId(value));
          } catch (e) {
            // 如果解析失败，尝试作为数字创建
            const numValue = parseInt(value, 10);
            if (!isNaN(numValue)) {
              return UaVariant.nodeId(new UaNodeId(numValue, 0));
            }
            // 最后尝试作为字符串创建
            return UaVariant.nodeId(new UaNodeId(value, 0, UaNodeIdType.STRING));
          }
        }
        // 如果 value 已经是 UaNodeId 对象
        if (value && typeof value.toString === 'function') {
          return UaVariant.nodeId(value);
        }
        return UaVariant.nodeId(new UaNodeId(0, 0));
      case 'ByteString':
        return UaVariant.byteString(value || '');
      case 'OptionSet':
        // OptionSet 通常是无符号整数
        return UaVariant.integer(parseInt(value) || 0, UaVariantType.UInt32);
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

const resolveItemNodeId = (item) => {
  if (!item) {
    throw new Error('当前编辑项不存在');
  }

  if (item.nodeId) {
    if (typeof item.nodeId === 'string') {
      return parseUaNodeId(item.nodeId);
    }
    if (item.nodeId._nodeId) {
      return item.nodeId._nodeId;
    }
    return item.nodeId;
  }

  if (item.nodeIdNum) {
    return parseUaNodeId(item.nodeIdNum);
  }

  throw new Error('无法获取 nodeId');
};

const syncVariableItemFromServer = (item, freshDataValue) => {
  if (!item || !freshDataValue) {
    return;
  }

  item.value = freshDataValue.value || null;
  item.dataValue = freshDataValue;

  let nextEditValue = item.value && typeof item.value === 'object'
    ? item.value.value
    : item.value;

  if (item.dataTypes === 'ByteString') {
    if (Array.isArray(item.value)) {
      nextEditValue = item.value.map(entry => typeof entry === 'string' ? byteStringToHex(entry) : entry);
    } else if (typeof item.value === 'string') {
      nextEditValue = byteStringToHex(item.value);
    } else {
      nextEditValue = item.value?.value || '';
    }
  } else if (item.dataTypes === 'ImagePNG' || item.dataTypes === 'Image') {
    if (Array.isArray(nextEditValue)) {
      nextEditValue = nextEditValue.map(entry =>
        (typeof entry === 'string' && /[\x00-\x08\x0B\x0C\x0E-\x1F]/.test(entry)) ? binaryStringToHex(entry) : entry
      );
    } else if (typeof nextEditValue === 'string' && nextEditValue) {
      nextEditValue = /[\x00-\x08\x0B\x0C\x0E-\x1F]/.test(nextEditValue) ? binaryStringToHex(nextEditValue) : nextEditValue;
    }
  } else if (item.dataTypesObj && typeof item.dataTypesObj.isSubtypeOf === 'function') {
    try {
      const enumerationNodeId = new UaNodeId(DataTypeIds.Enumeration);
      if (item.dataTypesObj.isSubtypeOf(enumerationNodeId) && item.dataTypesObj._enumValues?.size > 0) {
        const enumNumericValue = Number(nextEditValue);
        const localizedText = item.dataTypesObj._enumValues.get(enumNumericValue);
        if (localizedText) {
          const text = typeof localizedText.text === 'function'
            ? localizedText.text()
            : (localizedText._text || String(localizedText));
          nextEditValue = `${enumNumericValue}（${text}）`;
        }
      }
    } catch (error) {
      console.warn('回读枚举值格式化失败:', error);
    }
  }

  item._editValue = nextEditValue;
};

// 通用的写入 OPC UA 值的辅助函数
const writeValueToOpcUa = async (uaVariant, dataType, successMessage = '数据写入成功！') => {
  if (!currentEditingItem.value) {
    throw new Error('当前没有正在编辑的项目');
  }
  
  try {
    // 获取正确的URL
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;
    
    if (!url) {
      ElMessage.error('请先配置服务器连接');
      return false;
    }
    
    // 创建 OPC UA 客户端
    let apiConfig = new Configuration({
      basePath: url
    });
    let clientConfig = new UaClientConfiguration(apiConfig);
    let testOpcServer = new UaWebClient(clientConfig);
    const editingItem = currentEditingItem.value;
    const nodeId = resolveItemNodeId(editingItem);

    await testOpcServer.writeValue(nodeId, uaVariant);

    const [freshDataValue] = await testOpcServer.readValues([nodeId]);
    if (freshDataValue) {
      syncVariableItemFromServer(editingItem, freshDataValue);
    }

    ElMessage.success(successMessage);
    return true;
  } catch (error) {
    console.error('写入 OPC UA 失败:', error);
    ElMessage.error('写入 OPC UA 失败: ' + (error.message || '未知错误'));
    return false;
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
    let objectIdRaw = null;
    let methodIdRaw = null;
    
    // 辅助函数：从节点数据中提取 nodeId
    const extractNodeId = (data) => {
      // 优先使用 nodeId（可能是 UaNodeId 对象或 UaExpandedNodeId）
      if (data?.nodeId) {
        // 如果是 UaExpandedNodeId，提取 _nodeId
        if (data.nodeId._nodeId) {
          return data.nodeId._nodeId;
        }
        // 如果已经是 UaNodeId 对象
        if (typeof data.nodeId.toString === 'function') {
          return data.nodeId;
        }
      }
      // 其次使用 nodeIdNum（字符串格式，如 "i=87"）
      if (data?.nodeIdNum) {
        return data.nodeIdNum;
      }
      return null;
    };
    
    // 方法1: 通过保存的 treeNode 获取父节点
    if (currentMethodTreeNode.value && currentMethodTreeNode.value.parent) {
      const parentNode = currentMethodTreeNode.value.parent;
      objectIdRaw = extractNodeId(parentNode.data);
      methodIdRaw = extractNodeId(methodNode);
      console.log('方法1 - 通过 treeNode.parent 获取父节点:', {
        objectIdRaw,
        methodIdRaw,
        parentData: parentNode.data,
        methodData: methodNode
      });
    } else {
      // 方法2: 通过 Element Plus tree 的 getNode 方法获取节点
      const tree = eltree.value;
      if (tree) {
        // 尝试使用 nodeIdNum 获取节点
        const nodeKey = methodNode.nodeIdNum || methodNode.nodeId?._nodeId?.toString() || methodNode.nodeId?.toString();
        if (nodeKey) {
          try {
            const treeNode = tree.getNode(nodeKey);
            if (treeNode && treeNode.parent) {
              const parentNode = treeNode.parent;
              objectIdRaw = extractNodeId(parentNode.data);
              methodIdRaw = extractNodeId(methodNode);
              console.log('方法2 - 通过 tree.getNode 获取父节点:', {
                objectIdRaw,
                methodIdRaw,
                nodeKey,
                parentData: parentNode.data,
                methodData: methodNode
              });
            } else {
              console.warn('treeNode 或 parent 不存在:', { treeNode, nodeKey });
            }
          } catch (error) {
            console.warn('无法通过 getNode 获取父节点:', error, { nodeKey });
          }
        }
      }
    }
    
    // 方法3: 如果以上方法都失败，尝试从 methodNode 中查找父节点信息
    if (!objectIdRaw) {
      // 检查 methodNode 是否有 parent 信息
      if (methodNode.parent?.nodeId) {
        objectIdRaw = extractNodeId(methodNode.parent);
        methodIdRaw = extractNodeId(methodNode);
        console.log('方法3 - 通过 methodNode.parent 获取父节点:', { objectIdRaw, methodIdRaw });
      } else {
        // 最后回退：使用当前节点作为 objectId（某些情况下方法可能没有父对象）
        objectIdRaw = extractNodeId(methodNode);
        methodIdRaw = extractNodeId(methodNode);
        console.warn('方法4 - 无法获取父节点，使用当前节点作为 objectId:', { objectIdRaw, methodIdRaw });
      }
    }
    
    // 确保 objectId 和 methodId 都是有效的
    if (!objectIdRaw || !methodIdRaw) {
      console.error('无法获取方法节点或父节点信息:', {
        objectIdRaw,
        methodIdRaw,
        methodNode,
        currentMethodTreeNode: currentMethodTreeNode.value
      });
      ElMessage.error('无法获取方法节点或父节点信息');
      return;
    }
    
    // 将 objectId 和 methodId 转换为 UaNodeId 对象
    let objectId, methodId;
    try {
      // 辅助函数：将值转换为 UaNodeId
      const convertToUaNodeId = (value) => {
        if (!value) return null;
        
        // 如果已经是 UaNodeId 对象，直接使用
        if (value && typeof value.toString === 'function' && (value._nodeId !== undefined || value._value !== undefined)) {
          return value;
        }
        
        // 如果是字符串格式（如 "i=87" 或 "ns=0;i=87"），解析为 UaNodeId
        if (typeof value === 'string') {
          return parseUaNodeId(value);
        }
        
        // 如果是数字，创建 UaNodeId
        if (typeof value === 'number') {
          return new UaNodeId(value, 0);
        }
        
        // 如果是对象，尝试提取 nodeId
        if (typeof value === 'object') {
          if (value._nodeId) {
            return value._nodeId;
          }
          if (value.nodeId) {
            return convertToUaNodeId(value.nodeId);
          }
        }
        
        throw new Error(`无法转换 NodeId: ${JSON.stringify(value)}`);
      };
      
      objectId = convertToUaNodeId(objectIdRaw);
      methodId = convertToUaNodeId(methodIdRaw);
      
      if (!objectId || !methodId) {
        throw new Error('转换后的 NodeId 为空');
      }
      
      console.log('最终转换结果 - objectId:', objectId.toString(), 'methodId:', methodId.toString());
    } catch (error) {
      console.error('转换 NodeId 失败:', error, {
        objectIdRaw,
        methodIdRaw,
        objectIdRawType: typeof objectIdRaw,
        methodIdRawType: typeof methodIdRaw
      });
      ElMessage.error(`节点ID格式错误: ${error.message}`);
      return;
    }
    
    // 将输入参数转换为UaVariant数组
    const inputVariants = methodInputArgs.map(arg => {
      const variant = convertToUaVariant(arg.value, arg.dataType);
      return variant;
    });
    
    // 调用methodCall接口
    const outputVariants = await testOpcServer.methodCall( objectId, methodId, inputVariants);
    
    
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
      console.log(outputVariants,'outputVariants')
    }
    
    // 更新方法调用结果
    methodResult.value = 'OK';
    ElMessage.success('方法调用成功');
    
  } catch (error) {

    console.error('Method call failed:---', error);
    methodResult.value = `Failed`;
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
        label: getDisplayName(item) || 'Unnamed Node',
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


 

// 使用控制台组件的 addLog 方法
const testAddLog = () => {
  if (consoleRef.value) {
    // 添加信息日志
    consoleRef.value.addLog('info', '这是一条通过 addLog 添加的信息日志', {
      timestamp: Date.now(),
      source: '父组件',
      data: { test: true, value: 123 }
    })
    
    // 添加警告日志
    consoleRef.value.addLog('warn', '这是一条警告信息', '警告详情')
    
    // 添加调试日志
    consoleRef.value.addLog('debug', '调试信息', {
      user: 'admin',
      action: 'test_addLog',
      metadata: { version: '1.0.0' }
    })
  } else {
    console.warn('控制台组件引用不存在')
  }
}

// 添加错误日志示例
const addErrorLog = () => {
  if (consoleRef.value) {
    try {
      // 模拟一个错误
      throw new Error('这是一个模拟的错误')
    } catch (error) {
      consoleRef.value.addLog('error', '捕获到错误:', error, {
        stack: error.stack,
        timestamp: Date.now(),
        context: '父组件错误处理'
      })
    }
  }
}

// 在现有方法中使用 addLog 的示例
const logOperation = (operation, data) => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', `执行操作: ${operation}`, data)
  }
}

// 在异步操作中使用 addLog
const asyncOperation = async () => {
  if (consoleRef.value) {
    consoleRef.value.addLog('info', '开始异步操作')
    
    try {
      // 模拟异步操作
      await new Promise(resolve => setTimeout(resolve, 1000))
      consoleRef.value.addLog('info', '异步操作完成')
    } catch (error) {
      consoleRef.value.addLog('error', '异步操作失败:', error)
    }
  }
}

// History 历史记录相关方法
// 验证节点是否可以拖拽到历史记录区域
const validateHistoryNode = (draggedData) => {
  if (!draggedData) {
    return false;
  }
  
  // 检查是否有 typeDefinition
  const typeDefinition = draggedData.typeDefinition || draggedData.typeDefinitionId;
  if (!typeDefinition) {
    return false;
  }
  
  return true;
};

// 处理 History 拖拽悬停
const handleHistoryDragOver = (event) => {
  // 由于浏览器安全限制，dragover 中无法读取 dataTransfer 数据
  // 所以我们允许拖拽，但在 drop 时会进行完整验证
  event.preventDefault();
  event.dataTransfer.dropEffect = 'move';
  historyDragOver.value = true;
};

// 处理 History 拖拽离开
const handleHistoryDragLeave = (event) => {
  const rect = event.currentTarget.getBoundingClientRect();
  const x = event.clientX;
  const y = event.clientY;
  
  if (x < rect.left || x > rect.right || y < rect.top || y > rect.bottom) {
    historyDragOver.value = false;
  }
};

// 清空所有历史记录相关状态的辅助函数
const clearAllHistoryStates = () => {
  historySelectedNode.value = null;
  historyTreeData.value = [];
  historyTreeSelectValue.value = null;
  historyCheckedNodes.value = [];
  historyEventTypesTreeData.value = [];
  historyEventTypeCheckedNodes.value = [];
  historyEventTypeDefaultCheckedKeys.value = [];
  historyResults.value = [];
  historyDetails.value = {};
  selectedResultIndex.value = -1;
  selectedHistoryDetailKey.value = null;
  expandedHistoryDetails.value = [];
  historyContinuationPoint.value = null;
  currentHistoryOpcServer.value = null;
  childNodeIds.value = null;
};

// 处理 History 拖拽放置
const handleHistoryDrop = async (event) => {
  // 先阻止默认行为和事件传播
  event.preventDefault();
  event.stopPropagation();
  historyDragOver.value = false;
  
  let draggedData = null;
  try {
    const data = event.dataTransfer.getData('application/json');
    if (data) {
      draggedData = JSON.parse(data);
    }
  } catch (e) {
    console.error('Failed to parse drag data:', e);
    clearAllHistoryStates();
    return;
  }
  
  if (!draggedData) {
    clearAllHistoryStates();
    return;
  }
  
  // 从树数据中查找完整的节点信息
  const findNodeInTree = (nodes, nodeIdNum) => {
    for (const node of nodes) {
      if (node.nodeIdNum === nodeIdNum) {
        return node;
      }
      if (node.children && node.children.length > 0) {
        const found = findNodeInTree(node.children, nodeIdNum);
        if (found) return found;
      }
    }
    return null;
  };
  
  const fullNode = findNodeInTree(state.bottomTreeData, draggedData.nodeIdNum) || draggedData;
  console.log(fullNode,'fullNode',draggedData)
  
  // 优先使用 fullNode 中的 typeDefinition（因为它是从树中查找的完整节点）
  const nodeWithTypeDefinition = {
    ...draggedData,
    typeDefinition: fullNode?.typeDefinitionId || fullNode?.typeDefinition
  };
  
  // 先进行基本验证：检查是否有 typeDefinition
  if (!nodeWithTypeDefinition.typeDefinition) {
    ElMessage.warning('该节点没有类型定义，无法进行历史记录查询');
    clearAllHistoryStates();
    return;
  }
  
  // 先验证并加载事件类型树，只有在成功后才设置状态
  try {
    // 获取正确的URL
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;
    
    if (!url) {
      ElMessage.warning('未配置服务器连接，无法加载事件类型树');
      clearAllHistoryStates();
      return;
    }
    
    // 创建 OPC UA 客户端
    let apiConfig = new Configuration({
      basePath: url
    });
    let clientConfig = new UaClientConfiguration(apiConfig);
    let testOpcServer = new UaWebClient(clientConfig);
    
    // 将 TypeDefinition 转换为 UaNodeId 对象（用于调用 getGeneratedEventType）
    let typeDefinitionNodeId = nodeWithTypeDefinition.typeDefinition;
    
    // 调用 getGeneratedEventType 方法获取子节点
    childNodeIds.value = await testOpcServer.getGeneratedEventType(typeDefinitionNodeId);
    console.log('getGeneratedEventType 返回的子节点:', childNodeIds.value);
    
    // 检查返回的子节点是否为空
    if (!childNodeIds.value || (typeof childNodeIds.value === 'object' && Object.keys(childNodeIds.value).length === 0) || 
        (Array.isArray(childNodeIds.value) && childNodeIds.value.length === 0)) {
      console.warn('getGeneratedEventType 返回的子节点为空，不允许拖拽');
      ElMessage.warning('该节点没有事件类型子节点，无法进行历史记录查询');
      childNodeIds.value = null;
      clearAllHistoryStates();
      return;
    }
    
    // 先尝试加载事件类型树，验证节点是否有效
    await loadHistoryEventTypesTree(nodeWithTypeDefinition);
    
    // 只有在加载成功后，才设置所有状态
    historySelectedNode.value = {
      id: draggedData.id || `node-${Date.now()}-${Math.random()}`,
      name: draggedData.label || draggedData.browseName || draggedData.displayName || draggedData.nodeIdNum || '未知节点',
      label: draggedData.label,
      nodeIdNum: draggedData.nodeIdNum,
      nodeId: draggedData.nodeId,
      nodeClass: draggedData.nodeClass,
      browseName: draggedData.browseName || draggedData.label,
      displayName: draggedData.displayName,
      typeDefinition: fullNode?.typeDefinition || draggedData.typeDefinition, // 确保 TypeDefinition 被传递
      ...fullNode
    };
    
    // 构建树数据，包含子节点
    const buildTreeData = (node) => {
      const treeNode = {
        label: node.label || getDisplayName(node) || node.browseName || node.nodeIdNum || '未知节点',
        nodeIdNum: node.nodeIdNum,
        nodeId: node.nodeId,
        nodeClass: node.nodeClass,
        browseName: node.browseName,
        displayName: node.displayName,
        hasChildren: node.hasChildren,
        children: node.children ? node.children.map(child => buildTreeData(child)) : []
      };
      return treeNode;
    };
    
    historyTreeData.value = [buildTreeData(fullNode)];
    historyTreeSelectValue.value = draggedData.nodeIdNum;
    historyCheckedNodes.value = [];
    
    ElMessage.success(`已添加对象: ${historySelectedNode.value.name}`);
  } catch (error) {
    // 如果加载失败（例如子节点为空），确保清空所有状态
    console.error('加载事件类型树失败，阻止拖拽操作:', error);
    clearAllHistoryStates();
    // 错误消息已在 loadHistoryEventTypesTree 中显示，这里不再重复显示
  }
};

// 加载历史事件类型树数据
const loadHistoryEventTypesTree = async (draggedNode) => {
  try {
    // 1. 从拖拽节点获取 TypeDefinition
    const typeDefinition = draggedNode.typeDefinition;
    if (!typeDefinition) {
      console.warn('拖拽节点没有 TypeDefinition，跳过加载事件类型树');
      ElMessage.warning('该节点没有类型定义，无法进行历史记录查询');
      historyEventTypesTreeData.value = [];
      throw new Error('节点没有 TypeDefinition');
    }
    
    // 2. 将 TypeDefinition 转换为 UaNodeId 对象（用于调用 getGeneratedEventType）
    let typeDefinitionNodeId = typeDefinition
    
    // 获取正确的URL
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;
    
    if (!url) {
      console.warn('未配置服务器连接，跳过加载事件类型树');
      ElMessage.warning('未配置服务器连接，无法加载事件类型树');
      historyEventTypesTreeData.value = [];
      throw new Error('未配置服务器连接');
    }
    
    // 创建 OPC UA 客户端
    let apiConfig = new Configuration({
      basePath: url
    });
    let clientConfig = new UaClientConfiguration(apiConfig);
    let testOpcServer = new UaWebClient(clientConfig);
    
    // 3. 使用全局的 childNodeIds（已在 handleHistoryDrop 中获取并验证）
    // childNodeIds 已经在拖拽方法中获取并验证过了
    
    //4. 在 allObjectTypesData 中找到 TypeDefinition 对应的节点（使用 typeDefinitionNodeId）
    // const typeDefinitionNodeIdStr = typeDefinitionNodeId.toString();
    let currentNode = state.allObjectTypesData.find(item => {
      const itemNodeId = item.nodeids
      return itemNodeId === childNodeIds.value?.toString();
    });
    console.log(currentNode,'currentNode')
    
    if (!currentNode) {
      console.warn('在 allObjectTypesData 中未找到 TypeDefinition 对应的节点');
      ElMessage.warning('无法找到对应的节点类型，请检查节点配置');
      historyEventTypesTreeData.value = [];
      throw new Error('未找到对应的节点类型');
    }
    
    //5. 向上查找父节点直到 BaseEventType
    const nodePath = []; // 存储从当前节点到 BaseEventType 的路径
    const visited = new Set(); // 防止循环引用
    
    while (currentNode) {
      const currentNodeId = currentNode.nodeId?.toString ? currentNode.nodeId.toString() : String(currentNode.nodeId);
      
      // 防止循环引用
      if (visited.has(currentNodeId)) {
        console.warn('检测到循环引用，停止向上查找');
        break;
      }
      visited.add(currentNodeId);
      
      // 添加到路径
      nodePath.push(currentNode);
      
      // 检查是否是 BaseEventType
      const browseName = currentNode._browseName?._name || currentNode.browseName?._name || currentNode.browseName || '';
      const displayName = currentNode._displayName?._text || currentNode.displayName?._text || currentNode.displayName || '';
      if (browseName === 'BaseEventType' || displayName === 'BaseEventType') {
        console.log('已找到 BaseEventType，停止向上查找');
        break;
      }
      
      // 查找父节点（通过 _parentType）
      const parentTypeNodeId = currentNode._parent?.nodeids || currentNode._parent?.nodeId;
      if (!parentTypeNodeId) {
        console.warn('当前节点没有父节点，停止向上查找');
        break;
      }
      
      const parentNodeIdStr = parentTypeNodeId.toString ? parentTypeNodeId.toString() : String(parentTypeNodeId);
      currentNode = state.allObjectTypesData.find(item => {
        const itemNodeId = item.nodeId?.toString ? item.nodeId.toString() : String(item.nodeId);
        return itemNodeId === parentNodeIdStr;
      });
      
      if (!currentNode) {
        console.warn('在 allObjectTypesData 中未找到父节点');
        break;
      }
    }
    
    console.log('向上查找的节点路径:', nodePath.map(n => n._browseName?._name || n.browseName?._name || 'Unknown'));
    
    // 6. 加载所有路径节点的详细信息，将服务器获取的子节点作为当前节点的children
    const flatNodes = [];
    
    for (const node of nodePath) {
      const nodeId = node.nodeId;
      let nodeIdObj = null;
      
      try {
        if (nodeId instanceof UaNodeId) {
          nodeIdObj = nodeId;
        } else if (typeof nodeId.toString === 'function') {
          nodeIdObj = parseUaNodeId(nodeId.toString());
        } else {
          nodeIdObj = parseUaNodeId(String(nodeId));
        }
        
        // 加载节点的详细信息（EventId, Time, Message 等，如CheckIn等子节点）
        const nodeDetails = await loadNodeDetailsForEventType(testOpcServer, nodeIdObj, url);
        
        const nodeLabel = node._displayName?._text || node.displayName?._text || 
                         node._browseName?._name || node.browseName?._name || 
                         nodeIdObj.toString();
        
        // 将服务器获取的子节点作为当前节点的children，并放在前面（优先显示）
        const children = [];
        if (nodeDetails && Array.isArray(nodeDetails) && nodeDetails.length > 0) {
          // 将服务器获取的数据放在children数组的前面
          children.push(...nodeDetails);
        }
        
        // 获取节点的 browseName 和 displayName，用于后续识别
        const browseName = node._browseName?._name || node.browseName?._name || node.browseName || '';
        const displayName = node._displayName?._text || node.displayName?._text || node.displayName || '';
        
        // 添加节点本身，子节点作为children（而不是扁平化）
        // 根节点禁用勾选框
        flatNodes.push({
          nodeId: nodeIdObj.toString(),
          label: nodeLabel,
          browseName: browseName,
          displayName: displayName,
          children: children,
          disableCheckbox: true  // 根节点不显示勾选框
        });
      } catch (error) {
        console.error(`加载节点 ${node._browseName?._name || 'Unknown'} 详细信息失败:`, error);
        // 即使失败也添加节点
        const nodeLabel = node._displayName?._text || node.displayName?._text || 
                         node._browseName?._name || node.browseName?._name || 
                         'Unknown';
        const browseName = node._browseName?._name || node.browseName?._name || node.browseName || '';
        const displayName = node._displayName?._text || node.displayName?._text || node.displayName || '';
        
        flatNodes.push({
          nodeId: node.nodeId?.toString ? node.nodeId.toString() : String(node.nodeId),
          label: nodeLabel,
          browseName: browseName,
          displayName: displayName,
          children: [],
          disableCheckbox: true  // 根节点不显示勾选框
        });
      }
    }
    
    // 7. 递归查找所有 Time 和 Message 节点的 nodeId，用于默认勾选
    const findTimeAndMessageNodes = (nodes) => {
      const result = [];
      if (!nodes || !Array.isArray(nodes)) return result;
      
      nodes.forEach(node => {
        if (node && node.nodeId) {
          const browseName = (node.browseName || '').toLowerCase();
          const displayName = (node.displayName || '').toLowerCase();
          const label = (node.label || '').toLowerCase();
          
          // 检查是否是 Time 或 Message 节点
          if (browseName === 'time' || displayName === 'time' || 
              browseName === 'message' || displayName === 'message' ||
              (label.includes('time') && !label.includes('receive') && !label.includes('local')) ||
              label.includes('message')) {
            result.push(node.nodeId);
          }
          
          // 递归查找子节点
          if (node.children && Array.isArray(node.children)) {
            result.push(...findTimeAndMessageNodes(node.children));
          }
        }
      });
      
      return result;
    };
    
    // 从所有已加载的节点中查找 Time 和 Message
    const timeAndMessageNodeIds = findTimeAndMessageNodes(flatNodes);
    historyEventTypeDefaultCheckedKeys.value = timeAndMessageNodeIds;
    
    // 8. 重新排序 flatNodes，将 BaseEventType 放到 EmployeeCheckInEventType 的上面
    const baseEventTypeIndex = flatNodes.findIndex(node => {
      const browseName = node.browseName || '';
      const displayName = node.displayName || '';
      const label = node.label || '';
      return browseName === 'BaseEventType' || 
             displayName === 'BaseEventType' ||
             label === 'BaseEventType' ||
             label.includes('BaseEventType');
    });
    
    if (baseEventTypeIndex > 0) {
      // 如果找到 BaseEventType 且不在第一位，将其移到数组前面
      const baseEventTypeNode = flatNodes.splice(baseEventTypeIndex, 1)[0];
      flatNodes.unshift(baseEventTypeNode);
      console.log('已将 BaseEventType 移到数组前面');
    }
    
    // 9. 直接使用 flatNodes 数组
    historyEventTypesTreeData.value = flatNodes;
    
    console.log('已加载事件类型树（平铺）:', historyEventTypesTreeData.value);
  } catch (error) {
    console.error('加载事件类型树失败:', error);
    // 如果加载失败，设置为空数组（去掉根节点层级）
    historyEventTypesTreeData.value = [];
  }
};

// 使用 processVariablesData 逻辑加载节点详细信息（用于事件类型树）
const loadNodeDetailsForEventType = async (testOpcServer, nodeId, url) => {
  try {
    // 浏览节点获取子节点（变量、属性等）
    const nodeClassToReturn = Number(NodeClass.Object | NodeClass.Variable | NodeClass.Method);
    const browseResult = await testOpcServer.browseChild(nodeId, nodeClassToReturn, 100);

    if (!browseResult || !browseResult.results || browseResult.results.length === 0) {
      return [];
    }

    // 使用 processVariablesData 的逻辑处理数据
    const allArr = browseResult.results;
    
    // 读取变量值
    let readNodeIds = [];
    allArr.forEach((item) => {
      if (item.nodeId?._nodeId) {
        readNodeIds.push(item.nodeId._nodeId);
      }
    });

    let current = [];
    try {
      current = await testOpcServer.readValues(readNodeIds);
    } catch (error) {
      console.warn('读取变量值失败:', error);
      current = new Array(allArr.length).fill(null);
    }

    // 获取所有节点的 nodeId，并创建映射关系
    let variableNodeIds = [];
    
    allArr.forEach((item, index) => {
      if (item.nodeId?._nodeId) {
        variableNodeIds.push(item.nodeId._nodeId);
      }
    });

    // 批量读取所有节点的变量属性
    let allVariableAttributes = [];
    try {
      allVariableAttributes = await testOpcServer.readVariableAttributes(variableNodeIds);
    } catch (error) {
      console.warn('读取变量属性失败:', error);
      allVariableAttributes = new Array(variableNodeIds.length).fill(null);
    }

    // 使用 processVariablesData 的逻辑处理数据，转换为树节点格式
    const processedNodes = allArr.map((item, index) => {
      let typeValue = current[index]?.value?.type;
      let dataValue = current[index] || null;
      
      if (!dataValue) {
        return null;
      }

      let translateTypes = findObjectIdByType(typeValue);
      item.translateTypes = translateTypes;
      
      try {
        let currentNodeId = item.nodeId?._nodeId?.toString();
        if (!currentNodeId) {
          return null;
        }
        
        let variableIndex = variableNodeIds.findIndex(nodeId => 
          nodeId.toString() === currentNodeId
        );
        
        if (variableIndex === -1) {
          return null;
        }
        
        let currentID = allVariableAttributes[variableIndex];
        if (!currentID) {
          return null;
        }
        
        // 根据 datatype ID 去 allTypesData 中查找对应的数据类型
        let temp = state.allTypesData.filter((idx) => {
          let typeId = idx.nodeId.toString();
          let dataTypeId = currentID.dataType ? currentID.dataType.toString() : '';
          return typeId === dataTypeId;
        });
        
        let dataTypeDictionary = new UaDataTypeDictionary();
        let dataTypes = dataTypeDictionary.getDataType(currentID);
        
        // 获取显示值，优先使用 toString()
        let editValue = (current[index]?.value && typeof current[index]?.value === 'object')
          ? current[index]?.value?.value
          : current[index]?.value;
        
        // 格式化值显示
        let displayValue = editValue;
        if (editValue !== null && editValue !== undefined && typeof editValue.toString === 'function') {
          try {
            displayValue = editValue.toString();
          } catch (e) {
            // 如果 toString 失败，继续按类型处理
            if (temp[0]?._browseName === 'DateTime' && editValue) {
              if (typeof editValue === 'string' || typeof editValue === 'number') {
                const dateValue = new Date(editValue);
                if (!isNaN(dateValue.getTime())) {
                  displayValue = dateValue.toLocaleString();
                }
              }
            } else {
              displayValue = String(editValue);
            }
          }
        } else if (temp[0]?._browseName === 'DateTime' && editValue) {
          if (typeof editValue === 'string' || typeof editValue === 'number') {
            const dateValue = new Date(editValue);
            if (!isNaN(dateValue.getTime())) {
              displayValue = dateValue.toLocaleString();
            }
          }
        } else {
          displayValue = editValue !== null && editValue !== undefined ? String(editValue) : null;
        }
        
        const displayName = getDisplayName(item) || item.browseName?._name || item.browseName || 'Unknown';
        const browseName = item.browseName?._name || item.browseName || '';
        
        // 判断是否是 Time 或 Message 节点
        const isTimeOrMessage = browseName.toLowerCase() === 'time' || 
                                displayName.toLowerCase() === 'time' ||
                                browseName.toLowerCase() === 'message' || 
                                displayName.toLowerCase() === 'message';
        
        // 构建树节点
        return {
          nodeId: item.nodeId?.toString() || `node-${Date.now()}-${Math.random()}`,
          label: `${displayName}${displayValue !== null && displayValue !== undefined ? `: ${displayValue}` : ''}`,
          browseName: browseName,
          displayName: displayName,
          nodeClass: item.nodeClass,
          value: displayValue,
          dataType: temp[0]?._displayName?._text || '',
          dataTypes: temp[0]?._parentType?._browseName ? temp[0]?._parentType?._browseName : (temp[0]?.browseName || 'Unknown'),
          hasChildren: item.hasChildren || false,
          children: [],
          disabled: isTimeOrMessage  // Time 和 Message 节点禁用勾选（不能取消）
        };
      } catch (error) {
        console.warn('处理节点失败:', error);
        return null;
      }
    }).filter(node => node !== null);

    // 按优先级排序属性节点（优先显示 EventId, Time, Message）
    const priorityAttributes = ['EventId', 'EventType', 'Time', 'ReceiveTime', 'LocalTime', 'Message', 'Severity', 'SourceName', 'SourceNode'];
    const eventAttributeChildren = [];
    const otherChildren = [];
    
    // 先添加优先级属性
    for (const attrName of priorityAttributes) {
      const found = processedNodes.find(node => {
        const browseName = (node.browseName || '').toLowerCase();
        const label = (node.displayName || '').toLowerCase();
        return browseName === attrName.toLowerCase() || label === attrName.toLowerCase() ||
               browseName.includes(attrName.toLowerCase()) || label.includes(attrName.toLowerCase());
      });
      if (found) {
        eventAttributeChildren.push(found);
      }
    }
    
    // 添加其他变量节点
    for (const node of processedNodes) {
      if (!eventAttributeChildren.find(n => n.nodeId === node.nodeId)) {
        if (node.nodeClass === NodeClass.Variable) {
          eventAttributeChildren.push(node);
        } else {
          otherChildren.push(node);
        }
      }
    }

    return [...eventAttributeChildren, ...otherChildren];
  } catch (error) {
    console.error('加载节点详细信息失败:', error);
    return [];
  }
};

// 处理树选择器变化
const handleHistoryTreeSelectChange = (value) => {
  if (!value) {
    historySelectedNode.value = null;
    return;
  }
  
  // 从树数据中查找节点
  const findNodeInTree = (nodes, nodeIdNum) => {
    for (const node of nodes) {
      if (node.nodeIdNum === nodeIdNum) {
        return node;
      }
      if (node.children && node.children.length > 0) {
        const found = findNodeInTree(node.children, nodeIdNum);
        if (found) return found;
      }
    }
    return null;
  };
  
  const node = findNodeInTree(state.bottomTreeData, value);
  if (node) {
    historySelectedNode.value = {
      id: `node-${Date.now()}-${Math.random()}`,
      name: node.label || node.browseName || node.displayName || node.nodeIdNum || '未知节点',
      label: node.label,
      nodeIdNum: node.nodeIdNum,
      nodeId: node.nodeId,
      nodeClass: node.nodeClass,
      browseName: node.browseName || node.label,
      displayName: node.displayName,
      ...node
    };
    
    // 构建树数据
    const buildTreeData = (node) => {
      const treeNode = {
        label: node.label || getDisplayName(node) || node.browseName || node.nodeIdNum || '未知节点',
        nodeIdNum: node.nodeIdNum,
        nodeId: node.nodeId,
        nodeClass: node.nodeClass,
        browseName: node.browseName,
        displayName: node.displayName,
        hasChildren: node.hasChildren,
        children: node.children ? node.children.map(child => buildTreeData(child)) : []
      };
      return treeNode;
    };
    
    historyTreeData.value = [buildTreeData(node)];
    historyCheckedNodes.value = [];
  }
};

// 清空选中的节点
const clearHistoryNode = () => {
  historySelectedNode.value = null;
  historyTreeData.value = [];
  historyTreeSelectValue.value = null;
  historyCheckedNodes.value = [];
  historyResults.value = [];
  historyDetails.value = {};
  selectedResultIndex.value = -1;
  historyEventTypesTreeData.value = [];
  historyEventTypeCheckedNodes.value = [];
  historyEventTypeDefaultCheckedKeys.value = [];
  // 清空 Where 条件
  whereMatchMode.value = 'all';
  whereRows.value = [];
  whereActiveRowIndex.value = -1;
};

const normalizeDataType = (t) => String(t || '').toLowerCase();

// 判断是否应该显示 checkbox（根据字段的 browseName 和 dataType）
const shouldShowCheckboxForRow = (row) => {
  if (!row) return false;

  const browseName = String(row.browseName || row.fieldName || '').toLowerCase();
  const dataType = normalizeDataType(row.dataType);

  // 根据 browseName 判断（如 checkin）
  if (browseName === 'checkin' || browseName.includes('checkin')) {
    return true;
  }

  // 根据 dataType 判断 - Boolean 类型
  if (dataType.includes('boolean') || dataType === 'bool') {
    return true;
  }

  return false;
};

// 判断是否应该显示日期时间选择器
const shouldShowDatePickerForRow = (row) => {
  if (!row) return false;

  const dataType = normalizeDataType(row.dataType);

  // DateTime 类型使用日期时间选择器
  if (dataType.includes('datetime') || dataType.includes('utctime')) {
    return 'datetime';
  }

  // 纯日期类型
  if (dataType === 'date') {
    return 'date';
  }

  // 纯时间类型
  if (dataType === 'time') {
    return 'time';
  }

  return false;
};

// 判断是否是枚举类型
const isEnumTypeForRow = (row) => {
  if (!row) return false;

  const dataType = normalizeDataType(row.dataType);

  // 枚举类型判断
  return dataType.includes('enum') || dataType.includes('enumeration');
};

const getNodePathTextForWhere = () => {
  // 尽量复用已拖拽对象的名称作为“路径”展示（如果后续有真实路径字段可替换）
  return historySelectedNode.value?.name || historySelectedNode.value?.browseName || '';
};

const addWhereRow = () => {
  whereRows.value.push({
    id: `where-${Date.now()}-${Math.random()}`,
    fieldNodeId: null,
    browseName: '',
    fieldName: '',
    displayName: '',
    dataType: '',
    op: 'eq',
    value: '',
    path: getNodePathTextForWhere()
  });
  whereActiveRowIndex.value = whereRows.value.length - 1;
};

const removeLastWhereRow = () => {
  if (whereRows.value.length === 0) return;
  whereRows.value.pop();
  whereActiveRowIndex.value = Math.min(whereActiveRowIndex.value, whereRows.value.length - 1);
};

const clearWhereRows = () => {
  whereRows.value = [];
  whereActiveRowIndex.value = -1;
};

const handleWhereRowFieldChange = (rowIndex) => {
  const row = whereRows.value[rowIndex];
  if (!row) return;
  const field = whereAvailableFields.value.find(f => f.nodeId === row.fieldNodeId);
  if (!field) return;

  // 正确设置字段信息
  row.browseName = field.browseName || '';
  row.fieldName = field.browseName || field.displayName;
  row.displayName = field.displayName || field.browseName;
  row.dataType = field.dataType || field.dataTypes || '';
  row.path = getNodePathTextForWhere();

  // 根据类型和字段名给一个默认操作符和默认值
  const dt = normalizeDataType(row.dataType);
  const browseName = String(row.browseName || row.fieldName || '').toLowerCase();
  
  // 判断是否是 Boolean 类型（根据 browseName 或 dataType）
  const isBoolean = browseName === 'checkin' || browseName.includes('checkin') || dt.includes('boolean');
  
  if (isBoolean) {
    row.op = 'eq';
    row.value = false;
  } else {
    row.op = 'eq';
    row.value = '';
  }
};

const getWhereOpsForRow = (row) => {
  if (!row) return [];

  const dt = normalizeDataType(row.dataType);
  const browseName = String(row.browseName || row.fieldName || '').toLowerCase();

  // 根据 browseName 判断（如 checkin 是 Boolean）
  if (browseName === 'checkin' || browseName.includes('checkin')) {
    return [
      { label: '等于', value: 'eq' },
      { label: '不等于', value: 'neq' }
    ];
  }

  // 根据 dataType 判断
  // 布尔类型：等于、不等于
  if (dt.includes('boolean') || dt === 'bool') {
    return [
      { label: '等于', value: 'eq' },
      { label: '不等于', value: 'neq' }
    ];
  }

  // 字符串类型：等于、不等于、包含、不包含
  if (dt.includes('string') || dt.includes('text') || dt.includes('localizedtext') || dt.includes('qualifiedname')) {
    return [
      { label: '大于', value: 'eq' },
      { label: '小于', value: 'neq' },
      { label: '等于', value: 'like' }, 
    ];
  }

  // 数值类型（byte, sbyte, int16, uint16, int32, uint32, int64, uint64, float, double 等）
  if (
    dt.includes('double') ||
    dt.includes('float') ||
    dt.includes('int') ||
    dt.includes('uint') ||
    dt.includes('number') ||
    dt.includes('numeric') ||
    dt === 'byte' ||
    dt === 'sbyte' ||
    dt.includes('int16') ||
    dt.includes('int32') ||
    dt.includes('int64') ||
    dt.includes('uint16') ||
    dt.includes('uint32') ||
    dt.includes('uint64')
  ) {
    return [
      { label: '等于', value: 'eq' },
      { label: '不等于', value: 'neq' },
      { label: '大于', value: 'gt' },
      { label: '小于', value: 'lt' },
      { label: '大于等于', value: 'gte' },
      { label: '小于等于', value: 'lte' }
    ];
  }

  // 时间类型：等于、不等于、大于、小于、大于等于、小于等于
  if (dt.includes('datetime') || dt.includes('time') || dt.includes('date') || dt.includes('utctime')) {
    return [
      { label: '等于', value: 'eq' },
      { label: '不等于', value: 'neq' },
      { label: '大于', value: 'gt' },
      { label: '小于', value: 'lt' },
      { label: '大于等于', value: 'gte' },
      { label: '小于等于', value: 'lte' }
    ];
  }

  // 枚举类型：等于、不等于
  if (dt.includes('enum') || dt.includes('enumeration')) {
    return [
      { label: '等于', value: 'eq' },
      { label: '不等于', value: 'neq' }
    ];
  }

  // Guid 类型：等于、不等于
  if (dt.includes('guid')) {
    return [
      { label: '等于', value: 'eq' },
      { label: '不等于', value: 'neq' }
    ];
  }

  // NodeId 类型：等于、不等于
  if (dt.includes('nodeid')) {
    return [
      { label: '等于', value: 'eq' },
      { label: '不等于', value: 'neq' }
    ];
  }

  // ByteString 类型：等于、不等于
  if (dt.includes('bytestring')) {
    return [
      { label: '等于', value: 'eq' },
      { label: '不等于', value: 'neq' }
    ];
  }

  // StatusCode 类型：等于、不等于
  if (dt.includes('statuscode')) {
    return [
      { label: '等于', value: 'eq' },
      { label: '不等于', value: 'neq' }
    ];
  }

  // 默认：等于、不等于
  return [
    { label: '等于', value: 'eq' },
    { label: '不等于', value: 'neq' }
  ];
};

const confirmWhere = () => {
  ElMessage.success('Where 条件已更新');
};

// 构建 Where 查询参数
const buildWhereQuery = () => {
  if (!whereRows.value || whereRows.value.length === 0) {
    return null;
  }

  try {
    const toUaVariant = (row, rawValue) => {
      const browseName = String(row.browseName || row.fieldName || '').toLowerCase();
      const dt = normalizeDataType(row.dataType);
      
      // 根据 browseName 判断（如 checkin 是 Boolean）
      if (browseName === 'checkin' || browseName.includes('checkin')) {
        return UaVariant.boolean(Boolean(rawValue));
      }
      
      // 根据 dataType 判断
      if (dt.includes('boolean')) return UaVariant.boolean(Boolean(rawValue));
      if (dt.includes('datetime') || dt.includes('time')) {
        const d = rawValue instanceof Date ? rawValue : new Date(rawValue);
        return UaVariant.dateTime(d);
      }
      if (dt.includes('int32')) return UaVariant.integer(parseInt(rawValue) || 0, UaVariantType.Int32);
      if (dt.includes('uint32')) return UaVariant.integer(parseInt(rawValue) || 0, UaVariantType.UInt32);
      if (dt.includes('int64')) return UaVariant.integer(parseInt(rawValue) || 0, UaVariantType.Int64);
      if (dt.includes('uint64')) return UaVariant.integer(parseInt(rawValue) || 0, UaVariantType.UInt64);
      if (dt.includes('double')) return UaVariant.double(parseFloat(rawValue) || 0);
      if (dt.includes('float')) return UaVariant.float(parseFloat(rawValue) || 0);
      if (dt.includes('nodeid')) {
        const nodeId = typeof rawValue === 'string' ? parseUaNodeId(rawValue) : rawValue;
        return UaVariant.nodeId(nodeId);
      }
      return UaVariant.string(String(rawValue ?? ''));
    };

    const filters = whereRows.value
      .filter(r => r && r.fieldNodeId && r.fieldName)
      .map(r => {
        const operatorMap = {
          eq: { op: UaQueryFilterType.Equals, isNot: false },
          neq: { op: UaQueryFilterType.Equals, isNot: true },
          gt: { op: UaQueryFilterType.GreaterThan, isNot: false },
          lt: { op: UaQueryFilterType.LessThan, isNot: false },
          gte: { op: UaQueryFilterType.GreaterThanOrEqual, isNot: false },
          lte: { op: UaQueryFilterType.LessThanOrEqual, isNot: false },
          like: { op: UaQueryFilterType.Like, isNot: false },
          nlike: { op: UaQueryFilterType.Like, isNot: true }
        };
        const m = operatorMap[r.op] || operatorMap.eq;
        const v = toUaVariant(r, r.value);
        return new UaQueryFilter(r.fieldName, m.op, v, m.isNot);
      });

    if (filters.length === 0) return null;

    // UaQuery(filters, orAll?)：orAll=true 表示任一满足（OR）
    const query = new UaQuery(filters, whereMatchMode.value === 'any');
    console.log('buildWhereQuery - 构建的 UaQuery:', query);
    return query;
  } catch (error) {
    console.error('buildWhereQuery - 构建 where 查询失败:', error);
    ElMessage.error('构建查询条件失败: ' + error.message);
    return null;
  }
};

// 处理树节点复选框变化
const handleHistoryTreeCheck = (data, checkedInfo) => {
  const { checkedNodes, checkedKeys, halfCheckedKeys } = checkedInfo;
  historyCheckedNodes.value = checkedNodes || [];
};

// 根据选中的树节点构建 select 数组
const buildHistorySelectArray = () => {
  // 默认值
  const defaultSelect = [  'Time', 'Message'];
  
  // 如果没有选中任何节点，返回默认值
  if (!historyEventTypeCheckedNodes.value || historyEventTypeCheckedNodes.value.length === 0) {
    return defaultSelect;
  }
  
  // 从选中的节点中提取 browseName 或 displayName
  const selectArray = new Set(defaultSelect);
  
  historyEventTypeCheckedNodes.value.forEach(node => {
    if (node && node.browseName) {
      selectArray.add(node.browseName);
    } else if (node && node.displayName) {
      const displayName = typeof node.displayName === 'string' 
        ? node.displayName 
        : (node.displayName._text || node.displayName.text || '');
      if (displayName) {
        selectArray.add(displayName);
      }
    } else if (node && node.label) {
      selectArray.add(node.label);
    }
  });
  
  return Array.from(selectArray);
};

// 处理历史搜索
const handleHistorySearch = async () => {
  if (!historySelectedNode.value) {
    ElMessage.warning('请先选择对象');
    return;
  }
  
  if (!historyStartTime.value || !historyEndTime.value) {
    ElMessage.warning('请选择开始时间和结束时间');
    return;
  }
  
  if (new Date(historyStartTime.value) > new Date(historyEndTime.value)) {
    ElMessage.warning('开始时间不能大于结束时间');
    return;
  }
  
  historySearchLoading.value = true;
  try {
    // 获取正确的URL
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;
    
    if (!url) {
      ElMessage.warning('请先配置服务器连接');
      historySearchLoading.value = false;
      return;
    }
    
    // 创建 OPC UA 客户端
    let apiConfig = new Configuration({
      basePath: url
    });
    let clientConfig = new UaClientConfiguration(apiConfig);
    let testOpcServer = new UaWebClient(clientConfig);
    
    // 保存 OPC 服务器实例，用于后续分页加载
    currentHistoryOpcServer.value = testOpcServer;
    
    // 重置分页状态（首次搜索时）
    historyContinuationPoint.value = null;
    
    // 获取 nodeId - 参考其他地方的提取逻辑
    const extractNodeId = (data) => {
      // 优先使用 nodeIdNum（字符串格式，如 "i=87" 或 "ns=2;i=87"）
      if (data?.nodeIdNum) {
        return data.nodeIdNum;
      }
      // 其次使用 nodeId（可能是 UaNodeId 对象或 UaExpandedNodeId）
      if (data?.nodeId) {
        // 如果是 UaExpandedNodeId，提取 _nodeId
        if (data.nodeId._nodeId) {
          const nodeId = data.nodeId._nodeId;
          if (typeof nodeId.toString === 'function') {
            return nodeId.toString();
          }
          return String(nodeId);
        }
        // 如果已经是 UaNodeId 对象
        if (typeof data.nodeId.toString === 'function') {
          return data.nodeId.toString();
        }
        // 如果是字符串
        if (typeof data.nodeId === 'string') {
          return data.nodeId;
        }
      }
      return null;
    };
    
    const nodeIdRaw = extractNodeId(historySelectedNode.value);
    
    if (!nodeIdRaw) {
      ElMessage.warning('无法获取节点ID，请检查节点数据');
      console.error('无法获取节点ID，节点数据:', historySelectedNode.value);
      historySearchLoading.value = false;
      return;
    }
    
    // 将 nodeId 转换为 UaNodeId 对象
    let nodeIdObj = null;
    try {
      // 如果已经是 UaNodeId 对象，直接使用
      if (nodeIdRaw instanceof UaNodeId) {
        nodeIdObj = nodeIdRaw;
      } else if (typeof nodeIdRaw === 'string') {
        // 如果是字符串，解析为 UaNodeId
        nodeIdObj = parseUaNodeId(nodeIdRaw);
      } else if (nodeIdRaw && typeof nodeIdRaw.toString === 'function') {
        // 如果是对象且有 toString 方法，先转为字符串再解析
        nodeIdObj = parseUaNodeId(nodeIdRaw.toString());
      } else {
        throw new Error('无法解析 nodeId');
      }
    } catch (error) {
      console.error('转换 nodeId 失败:', error, nodeIdRaw);
      ElMessage.warning('节点ID格式错误: ' + error.message);
      historySearchLoading.value = false;
      return;
    }
    
    // 将时间字符串转换为 Date 对象
    let startTimeDate = null;
    let endTimeDate = null;
    try {
      startTimeDate = new Date(historyStartTime.value);
      endTimeDate = new Date(historyEndTime.value);
      
      if (isNaN(startTimeDate.getTime())) {
        throw new Error('开始时间格式错误');
      }
      if (isNaN(endTimeDate.getTime())) {
        throw new Error('结束时间格式错误');
      }
    } catch (error) {
      console.error('转换时间失败:', error);
      ElMessage.warning('时间格式错误: ' + error.message);
      historySearchLoading.value = false;
      return;
    }
    
    // 构建 select 数组
    const select = buildHistorySelectArray();
    console.log('调用 historyReadEvent 接口，参数:', {
      nodeId: nodeIdObj.toString(),
      startTime: startTimeDate,
      endTime: endTimeDate,
      select: select
    });
    
    // 辅助函数：根据类型格式化值（当 toString() 不可用时使用）
    const formatVariantValueByType = (value, type) => {
      if (type === 13) {
        // DateTime 类型
        if (value instanceof Date) {
          return value.toLocaleString();
        } else if (typeof value === 'string') {
          return new Date(value).toLocaleString();
        } else if (typeof value === 'number') {
          return new Date(value).toLocaleString();
        }
        return String(value);
      } else if (type === 21) {
        // LocalizedText 类型 (UaLocalizedText)
        if (value && typeof value === 'object') {
          const text = value._text || value.text;
          if (text) return text;
          return String(value);
        }
        return String(value);
      } else if (type === 17) {
        // NodeId 类型 (UaNodeId)
        if (value && typeof value === 'object') {
          return value.toString ? value.toString() : String(value);
        }
        return String(value);
      } else if (type === 0) {
        // Null 类型
        return null;
      } else {
        // 其他类型，直接转换为字符串
        return String(value);
      }
    };
    
    // 调用 historyReadEvent 接口
    // 根据 UaWebClient.d.ts，方法签名为：
    // historyReadEvent(nodeId: UaNodeId, startTime: Date, endTime: Date, select: Array<string>, ...)
    let response;
    try {
      if (!testOpcServer.historyReadEvent) {
        throw new Error('historyReadEvent 方法不存在');
      }
      
      // 保存 OPC 服务器实例，用于后续分页加载
      currentHistoryOpcServer.value = testOpcServer;
      
      // 重置分页状态（首次搜索时）
      historyContinuationPoint.value = null;

      // 构建 where 查询条件
      const whereQuery = buildWhereQuery();
      console.log('Where 查询条件:', whereQuery);

      // 按照方法签名传递参数（首次搜索，continuationPoint 为 null）
      const historyResult = await testOpcServer.historyReadEvent(
        nodeIdObj,           // nodeId: UaNodeId
        startTimeDate,       // startTime: Date
        endTimeDate,         // endTime: Date
        select,              // select: Array<string>
        whereQuery,          // where: UaQuery | null (可选)
        15,                  // numValuesPerNode: number | null (可选)
        null,                // continuationPoint: string | null (可选，首次搜索为 null)
        false                // releaseContinuationPoints: boolean | null (可选，设为 false 保留 continuationPoint)
      );
      
      // 将 UaHistoryEventResult 转换为响应格式
      // 根据实际返回结构：UaHistoryEventResult { _historyEvents: Array, _continuationPoint: null }
      console.log('historyReadEvent 返回结果:', historyResult);
      
      // 提取 continuationPoint
      const continuationPoint = historyResult._continuationPoint || historyResult.continuationPoint || null;
      
      // 提取历史事件数组
      const historyEvents = historyResult._historyEvents || historyResult.historyEvents || [];
      
      // 判断是否还有更多数据
      // 如果返回的数据条数小于请求的条数，或者没有 continuationPoint，说明没有更多数据了
      const expectedPageSize = 15;
      const returnedCount = historyEvents.length;
      const shouldHideLoadMore = returnedCount < expectedPageSize || !continuationPoint;
      
      // 更新 continuationPoint（如果应该隐藏"..."节点，将 continuationPoint 设置为 null）
      historyContinuationPoint.value = shouldHideLoadMore ? null : continuationPoint;
      
      // 处理每个历史事件
      // 每个事件是 UaHistoryEventFieldList { _eventFields: Array }
      // 每个字段是 _UaVariant { _value, _type, _arrayType }
      const eventsData = historyEvents.map((event, eventIndex) => {
        // 获取事件字段数组
        const eventFields = event._eventFields || event.eventFields || [];
        
        // 根据 select 数组的顺序，将字段值映射到对应的字段名
        // select 数组如: ["EventId", "Time", "Message"]
        const eventData = {};
        
        select.forEach((fieldName, index) => {
          if (eventFields[index]) {
            const variant = eventFields[index];
            // 获取原始值
            const rawValue = variant._value !== undefined ? variant._value : variant.value;
            const type = variant._type !== undefined ? variant._type : variant.type;
            const arrayType = variant._arrayType !== undefined ? variant._arrayType : variant.arrayType;
            
            // 格式化值：对于所有类型，都尝试调用 toString() 方法
            let formattedValue = null;
            
            if (rawValue === null || rawValue === undefined) {
              formattedValue = null;
            } else {
              // 优先尝试调用 toString() 方法
              if (rawValue && typeof rawValue.toString === 'function') {
                try {
                  formattedValue = rawValue.toString();
                } catch (e) {
                  console.warn(`调用 toString() 失败 (${fieldName}):`, e);
                  formattedValue = String(rawValue);
                }
              } else {
                // 如果没有 toString 方法，根据类型处理
                if (type === 13) {
                  // DateTime 类型
                  if (rawValue instanceof Date) {
                    formattedValue = rawValue.toLocaleString();
                  } else if (typeof rawValue === 'string') {
                    formattedValue = new Date(rawValue).toLocaleString();
                  } else if (typeof rawValue === 'number') {
                    formattedValue = new Date(rawValue).toLocaleString();
                  } else {
                    formattedValue = String(rawValue);
                  }
                } else if (type === 21) {
                  // LocalizedText 类型 (UaLocalizedText)
                  if (rawValue && typeof rawValue === 'object') {
                    // UaLocalizedText 可能有 _text 或 text 属性
                    formattedValue = rawValue._text || rawValue.text || String(rawValue);
                  } else {
                    formattedValue = String(rawValue);
                  }
                } else if (type === 17) {
                  // NodeId 类型 (UaNodeId)
                  if (rawValue && typeof rawValue === 'object') {
                    // UaNodeId 可能有 toString 方法
                    formattedValue = rawValue.toString ? rawValue.toString() : String(rawValue);
                  } else {
                    formattedValue = String(rawValue);
                  }
                } else {
                  // 其他类型，直接转换为字符串
                  formattedValue = String(rawValue);
                }
              }
            }
            
            // 使用字段名作为 key（首字母大写，如 EventId, Time, Message）
            const key = fieldName.charAt(0).toUpperCase() + fieldName.slice(1);
            eventData[key] = formattedValue;
            
            // 同时保存原始值用于 Details 显示
            if (!eventData._rawFields) {
              eventData._rawFields = {};
            }
            eventData._rawFields[fieldName] = {
              value: rawValue,
              type: type,
              arrayType: arrayType,
              variant: variant
            };
          }
        });
        
        // 保存完整的事件数据用于 Details
        eventData._fullData = {
          eventFields: eventFields,
          select: select,
          rawEvent: event
        };
        
        return eventData;
      });
      
      console.log('解析后的事件数据:', eventsData);
      
      response = {
        data: eventsData
      };
    } catch (apiError) {
      // 如果接口调用失败，使用模拟数据
      console.error('调用 historyReadEvent 接口失败:', apiError);
      console.warn('使用模拟数据');
      response = {
        data: [ ]
      };
    }
    
    // 处理返回的数据
    const resultData = response.data || response || [];
    console.log(resultData,'resultData');
    // 转换为 Results 格式（只包含 EventId, Time, Message 用于显示）
    // 数据已经在上面处理过了，直接使用
    const newResults = resultData.map(item => {
      let text = '';
      
      // 处理 Message 字段
      try {
        if (item._rawFields && item._rawFields.Message && item._rawFields.Message.variant) {
          const message = item._rawFields.Message.variant;
          
          if (message.type === UaVariantType.LocalizedText) {
            // 如果是 LocalizedText 类型，提取文本
            const localizedText = message.toLocalizedText();
            text = localizedText ? localizedText.text : '';
          } else {
            // 其他类型，转换为字符串
            text = message.toString() || '';
          }
        } else if (item.Message) {
          // 如果 Message 已经是处理过的文本，直接使用
          text = typeof item.Message === 'string' ? item.Message : String(item.Message || '');
        }
      } catch (error) {
        console.warn('处理 Message 字段失败:', error, item);
        text = item.Message ? String(item.Message) : '';
      }
      console.log(item,'text')
      return {
        EventId:item.EventId? new UaNodeId(item.EventId):'',
        Time: item.Time || item.time || '',
        Message: text,
        // 保存完整数据用于 Details
        _fullData: item._fullData || item
      };
    });
    
    // 首次搜索，重置结果列表
    historyResults.value = newResults;
    
    // 清空之前的详情
    historyDetails.value = {};
    selectedResultIndex.value = -1;
    
    const hasMore = historyContinuationPoint.value !== null;
    if (hasMore) {
      ElMessage.success(`已加载 ${newResults.length} 条记录，共 ${historyResults.value.length} 条`);
    } else {
      ElMessage.success(`搜索完成，找到 ${historyResults.value.length} 条记录（已全部加载）`);
    }
  } catch (error) {
    console.error('History search failed:', error);
    ElMessage.error(`搜索失败: ${error.message}`);
  } finally {
    historySearchLoading.value = false;
  }
};

// 处理重置
const handleHistoryReset = () => {
  historyStartTime.value = getDefaultStartTime();
  historyEndTime.value = getDefaultEndTime();
  historyResults.value = [];
  historyDetails.value = {};
  selectedResultIndex.value = -1;
  historyContinuationPoint.value = null;
  currentHistoryOpcServer.value = null;
};

// 处理表格行变化
const handleTableRowChange = (currentRow) => {
  if (currentRow) {
    const index = historyResults.value.findIndex(row => row === currentRow);
    selectedResultIndex.value = index;
    selectResult(currentRow);
  }
};

// 获取历史记录表格行类名
const getHistoryRowClassName = ({ row, rowIndex }) => {
  return rowIndex === selectedResultIndex.value ? 'row-selected' : '';
};

// 格式化 UaVariant 值为可显示格式
const formatUaVariantValue = (variant) => {
  if (!variant) return null;
  
  const value = variant._value !== undefined ? variant._value : variant.value;
  const type = variant._type !== undefined ? variant._type : variant.type;
  
  if (value === null || value === undefined) {
    return null;
  }
  
  // 根据类型格式化值
  if (type === 13) {
    // DateTime 类型
    if (value instanceof Date) {
      return value.toLocaleString();
    } else if (typeof value === 'string') {
      return new Date(value).toLocaleString();
    }
    return String(value);
  } else if (type === 21) {
    // LocalizedText 类型 (UaLocalizedText)
    if (value && typeof value === 'object') {
      const text = value._text || value.text || value.toString();
      return text || String(value);
    }
    return String(value);
  } else if (type === 17) {
    // NodeId 类型 (UaNodeId)
    if (value && typeof value === 'object') {
      // 如果是 UaNodeId 对象，调用 toString() 获取 "ns=1;i=1" 格式
      if (value instanceof UaNodeId) {
        return value.toString();
      }
      // 如果对象有 toString 方法，调用它
      if (typeof value.toString === 'function') {
        return value.toString();
      }
      return String(value);
    }
    // 如果 value 是字符串，可能是已经格式化的 NodeId，直接返回
    // 如果是数字，需要转换为 UaNodeId 对象（但需要 namespace，这里无法确定）
    return String(value);
  } else if (type === 0) {
    // Null 类型
    return null;
  } else {
    // 其他类型，直接转换为字符串
    return String(value);
  }
};

// 选择结果项
const selectResult = (index) => {
  if (index >= 0 && index < historyResults.value.length) {
    const result = historyResults.value[index];
    selectedResultIndex.value = index;
    // 使用完整数据展示详情
    const fullData = result._fullData || result;
    console.log(fullData,'fullData');
    // 从 _fullData 中提取事件字段
    const details = {};
    
    if (fullData && fullData.eventFields && Array.isArray(fullData.eventFields)) {
      // 根据 select 数组，将字段映射到对应的字段名
      const select = fullData.select || buildHistorySelectArray();
      
      select.forEach((fieldName, index) => {
        if (fullData.eventFields[index]) {
          const variant = fullData.eventFields[index];
          let formattedValue = formatUaVariantValue(variant);
          
          // 使用字段名作为 key（首字母大写）
          const key = fieldName.charAt(0).toUpperCase() + fieldName.slice(1);
          
          // 特殊处理 EventId：如果 formattedValue 是 "1" 或看起来不像完整的 NodeId 格式，使用 result.EventId
          if (key === 'EventId' && result.EventId) {
            if (result.EventId instanceof UaNodeId) {
              formattedValue = result.EventId.toString();
            } else if (typeof result.EventId === 'object' && typeof result.EventId.toString === 'function') {
              formattedValue = result.EventId.toString();
            } else if (typeof formattedValue === 'string' && !formattedValue.includes('ns=') && !formattedValue.includes('i=') && !formattedValue.includes('s=')) {
              // 如果 formattedValue 看起来不像完整的 NodeId 格式，尝试使用 result.EventId
              formattedValue = result.EventId.toString ? result.EventId.toString() : String(result.EventId);
            }
          }
          
          // 如果值是对象（如 UaLocalizedText、UaNodeId），可能需要展开显示
          if (formattedValue !== null && typeof formattedValue === 'object' && !Array.isArray(formattedValue)) {
            details[key] = {
              type: 'Object',
              children: formattedValue
            };
          } else {
            details[key] = formattedValue;
          }
        }
      });
    } else if (fullData && fullData._rawFields) {
      // 如果已经有处理过的原始字段，直接使用
      Object.keys(fullData._rawFields).forEach(fieldName => {
        const fieldData = fullData._rawFields[fieldName];
        const key = fieldName.charAt(0).toUpperCase() + fieldName.slice(1);
        let formattedValue = formatUaVariantValue(fieldData.variant);
        
        // 特殊处理 EventId：如果 formattedValue 是 "1" 或看起来不像完整的 NodeId 格式，使用 result.EventId
        if (key === 'EventId' && result.EventId) {
          if (result.EventId instanceof UaNodeId) {
            formattedValue = result.EventId.toString();
          } else if (typeof result.EventId === 'object' && typeof result.EventId.toString === 'function') {
            formattedValue = result.EventId.toString();
          } else if (typeof formattedValue === 'string' && !formattedValue.includes('ns=') && !formattedValue.includes('i=') && !formattedValue.includes('s=')) {
            // 如果 formattedValue 看起来不像完整的 NodeId 格式，尝试使用 result.EventId
            formattedValue = result.EventId.toString ? result.EventId.toString() : String(result.EventId);
          }
        }
        
        if (formattedValue !== null && typeof formattedValue === 'object' && !Array.isArray(formattedValue)) {
          details[key] = {
            type: 'Object',
            children: formattedValue
          };
        } else {
          details[key] = formattedValue;
        }
      });
    } else {
      // 降级处理：直接使用 result 中的数据
      // 获取当前选中的字段列表，只显示用户选择的字段
      const select = buildHistorySelectArray();
      const selectKeys = new Set(select.map(s => s.charAt(0).toUpperCase() + s.slice(1)));

      Object.keys(result).forEach(key => {
        if (key !== '_fullData' && key !== '_rawFields' && selectKeys.has(key)) {
          let value = result[key];
          if (value !== null && value !== undefined) {
            // 特殊处理 EventId：确保显示为 "ns=1;i=1" 格式
            if (key === 'EventId') {
              if (value instanceof UaNodeId) {
                value = value.toString();
              } else if (typeof value === 'object' && typeof value.toString === 'function') {
                value = value.toString();
              }
            }
            details[key] = value;
          }
        }
      });
    }
    
    historyDetails.value = details;
    selectedHistoryDetailKey.value = null;
    expandedHistoryDetails.value = [];
  }
};

// 计算是否有下一页历史数据
const hasNextPageHistory = computed(() => {
  return !!historyContinuationPoint.value;
});

// 加载下一页历史数据
const loadNextPageHistory = async () => {
  if (!currentHistoryOpcServer.value) {
    ElMessage.warning('OPC 服务器连接不可用');
    return;
  }

  if (!historyContinuationPoint.value) {
    ElMessage.info('没有更多数据了');
    return;
  }

  if (!historySelectedNode.value) {
    ElMessage.warning('请先选择对象');
    return;
  }

  if (!historyStartTime.value || !historyEndTime.value) {
    ElMessage.warning('请选择开始时间和结束时间');
    return;
  }

  historySearchLoading.value = true;

  try {
    // 获取 nodeId
    const extractNodeId = (data) => {
      if (data?.nodeIdNum) {
        return data.nodeIdNum;
      }
      if (data?.nodeId) {
        if (data.nodeId._nodeId) {
          const nodeId = data.nodeId._nodeId;
          if (typeof nodeId.toString === 'function') {
            return nodeId.toString();
          }
          return String(nodeId);
        }
        if (typeof data.nodeId.toString === 'function') {
          return data.nodeId.toString();
        }
        if (typeof data.nodeId === 'string') {
          return data.nodeId;
        }
      }
      return null;
    };

    const nodeIdRaw = extractNodeId(historySelectedNode.value);
    if (!nodeIdRaw) {
      ElMessage.warning('无法获取节点ID');
      historySearchLoading.value = false;
      return;
    }

    // 将 nodeId 转换为 UaNodeId 对象
    let nodeIdObj = null;
    try {
      if (nodeIdRaw instanceof UaNodeId) {
        nodeIdObj = nodeIdRaw;
      } else if (typeof nodeIdRaw === 'string') {
        nodeIdObj = parseUaNodeId(nodeIdRaw);
      } else if (nodeIdRaw && typeof nodeIdRaw.toString === 'function') {
        nodeIdObj = parseUaNodeId(nodeIdRaw.toString());
      } else {
        throw new Error('无法解析 nodeId');
      }
    } catch (error) {
      console.error('转换 nodeId 失败:', error);
      ElMessage.warning('节点ID格式错误');
      historySearchLoading.value = false;
      return;
    }

    // 将时间字符串转换为 Date 对象
    let startTimeDate = null;
    let endTimeDate = null;
    try {
      startTimeDate = new Date(historyStartTime.value);
      endTimeDate = new Date(historyEndTime.value);
      
      if (isNaN(startTimeDate.getTime()) || isNaN(endTimeDate.getTime())) {
        throw new Error('时间格式错误');
      }
    } catch (error) {
      console.error('转换时间失败:', error);
      ElMessage.warning('时间格式错误');
      historySearchLoading.value = false;
      return;
    }

    // 构建 select 数组
    const select = buildHistorySelectArray();

    // 构建 where 查询条件
    const whereQuery = buildWhereQuery();

    // 调用 historyReadEvent 接口，使用 continuationPoint
    const historyResult = await currentHistoryOpcServer.value.historyReadEvent(
      nodeIdObj,
      startTimeDate,
      endTimeDate,
      select,
      whereQuery,          // where: UaQuery | null
      15,                  // numValuesPerNode: number
      historyContinuationPoint.value, // continuationPoint: string
      false                // releaseContinuationPoints: boolean (设为 false 保留 continuationPoint)
    );

    console.log('loadNextPageHistory 返回结果:', historyResult);

    // 提取 continuationPoint
    const continuationPoint = historyResult._continuationPoint || historyResult.continuationPoint || null;

    // 提取历史事件数组
    const historyEvents = historyResult._historyEvents || historyResult.historyEvents || [];
    const expectedPageSize = 15;
    const returnedCount = historyEvents.length;
    const shouldHideLoadMore = returnedCount < expectedPageSize || !continuationPoint;

    // 更新 continuationPoint
    historyContinuationPoint.value = shouldHideLoadMore ? null : continuationPoint;

    // 格式化值的辅助函数
    const formatVariantValueByType = (value, type) => {
      if (type === 13) {
        if (value instanceof Date) {
          return value.toLocaleString();
        } else if (typeof value === 'string') {
          return new Date(value).toLocaleString();
        } else if (typeof value === 'number') {
          return new Date(value).toLocaleString();
        }
        return String(value);
      } else if (type === 21) {
        if (value && typeof value === 'object') {
          const text = value._text || value.text;
          if (text) return text;
          return String(value);
        }
        return String(value);
      } else if (type === 17) {
        if (value && typeof value === 'object') {
          return value.toString ? value.toString() : String(value);
        }
        return String(value);
      } else if (type === 0) {
        return null;
      } else {
        return String(value);
      }
    };

    // 处理每个历史事件
    const eventsData = historyEvents.map((event) => {
      const eventFields = event._eventFields || event.eventFields || [];
      const eventData = {};

      select.forEach((fieldName, index) => {
        if (eventFields[index]) {
          const variant = eventFields[index];
          const rawValue = variant._value !== undefined ? variant._value : variant.value;
          const type = variant._type !== undefined ? variant._type : variant.type;

          let formattedValue = null;
          if (rawValue === null || rawValue === undefined) {
            formattedValue = null;
          } else {
            if (rawValue && typeof rawValue.toString === 'function') {
              try {
                formattedValue = rawValue.toString();
              } catch (e) {
                formattedValue = formatVariantValueByType(rawValue, type);
              }
            } else {
              formattedValue = formatVariantValueByType(rawValue, type);
            }
          }

          const key = fieldName.charAt(0).toUpperCase() + fieldName.slice(1);
          eventData[key] = formattedValue;

          if (!eventData._rawFields) {
            eventData._rawFields = {};
          }
          eventData._rawFields[fieldName] = {
            value: rawValue,
            type: type,
            variant: variant
          };
        }
      });

      eventData._fullData = {
        eventFields: eventFields,
        select: select,
        rawEvent: event
      };

      return eventData;
    });

    // 转换为 Results 格式
    const newResults = eventsData.map(item => {
      console.log(item,'item');
      return {
      EventId:   new UaNodeId(item.EventId)  ,
        Time: item.Time || item.time || '',
        Message: new UaLocalizedText(item.Message) ,
      _fullData: item._fullData || item
    }
  });

    // 追加到现有结果列表
    historyResults.value = [...historyResults.value, ...newResults];

    const hasMore = historyContinuationPoint.value !== null;
    if (hasMore) {
      ElMessage.success(`已加载 ${newResults.length} 条记录，共 ${historyResults.value.length} 条`);
    } else {
      ElMessage.success(`已加载 ${newResults.length} 条记录，共 ${historyResults.value.length} 条（已全部加载）`);
    }
  } catch (error) {
    console.error('加载下一页历史数据失败:', error);
    ElMessage.error('加载下一页历史数据失败: ' + (error.message || '未知错误'));
  } finally {
    historySearchLoading.value = false;
  }
};

// 判断历史记录详情项是否可展开
const isHistoryDetailExpandable = (detail) => {
  return detail && typeof detail === 'object' && detail.children && !Array.isArray(detail);
};

// 切换历史记录详情展开状态
const toggleHistoryDetail = (key) => {
  const index = expandedHistoryDetails.value.indexOf(key);
  if (index > -1) {
    expandedHistoryDetails.value.splice(index, 1);
  } else {
    expandedHistoryDetails.value.push(key);
  }
};

// 格式化历史记录详情值
const formatHistoryDetailValue = (value) => {
  if (value === null || value === undefined) {
    return 'N/A';
  }
  
  // 特殊处理 UaNodeId 对象，确保显示为 "ns=1;i=1" 格式
  if (value instanceof UaNodeId) {
    return value.toString();
  }
  
  // 如果对象有 toString 方法且看起来像 UaNodeId（有 nsIndex 属性）
  if (typeof value === 'object' && value !== null && typeof value.toString === 'function') {
    // 检查是否是 UaNodeId 对象（通过检查是否有 nsIndex 属性）
    if ('nsIndex' in value || ('_nsIndex' in value)) {
      return value.toString();
    }
  }
  
  // 处理日期时间格式：将 2026/1/6 01:32:00 转换为 2026-01-06 01:32:00
  if (typeof value === 'string' || value instanceof Date) {
    let dateStr = typeof value === 'string' ? value : value.toString();
    
    // 匹配格式：YYYY/M/D HH:mm:ss 或 YYYY/M/DD HH:mm:ss 等变体
    const dateTimePattern = /^(\d{4})\/(\d{1,2})\/(\d{1,2})\s+(\d{1,2}):(\d{1,2}):(\d{1,2})/;
    const match = dateStr.match(dateTimePattern);
    
    if (match) {
      const year = match[1];
      const month = String(match[2]).padStart(2, '0');
      const day = String(match[3]).padStart(2, '0');
      const hours = String(match[4]).padStart(2, '0');
      const minutes = String(match[5]).padStart(2, '0');
      const seconds = String(match[6]).padStart(2, '0');
      
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }
    
    // 如果是 Date 对象，使用 formatDateTimeForPicker 格式化
    if (value instanceof Date) {
      return formatDateTimeForPicker(value);
    }
  }
  
  if (typeof value === 'object' && !isHistoryDetailExpandable(value)) {
    return JSON.stringify(value);
  }
  return String(value);
};

// 使用 processVariablesData 逻辑加载 BaseEventType 详细信息
const loadBaseEventTypeDetailsForHistory = async () => {
  try {
    // 获取正确的URL
    const allData = urlDatas.getDataByKey(selectedTopNodeId.value);
    let url = allData?.url || urlForm.value.url;
    
    if (!url) {
      console.warn('History: 未配置服务器连接，跳过加载 BaseEventType 详细信息');
      return;
    }

    // 创建 OPC UA 客户端
    let apiConfig = new Configuration({
      basePath: url
    });
    let clientConfig = new UaClientConfiguration(apiConfig);
    let testOpcServer = new UaWebClient(clientConfig);

    // 创建 nodeId i=2041
    const baseEventTypeNodeId = new UaNodeId(2041, 0);
    
    // 浏览节点获取子节点（变量、属性等）
    const nodeClassToReturn = Number(NodeClass.Object | NodeClass.Variable | NodeClass.Method);
    const browseResult = await testOpcServer.browseChild(baseEventTypeNodeId, nodeClassToReturn, 100);

    if (!browseResult || !browseResult.results || browseResult.results.length === 0) {
      console.warn('History: BaseEventType 没有子节点');
      return;
    }

    // 使用 processVariablesData 的逻辑处理数据
    const allArr = browseResult.results;
    
    // 读取节点属性
    const nodeAttributes = await testOpcServer.readNodeAttributes(baseEventTypeNodeId, true);
    
    // 读取变量值
    let readNodeIds = [];
    allArr.forEach((item) => {
      if (item.nodeId?._nodeId) {
        readNodeIds.push(item.nodeId._nodeId);
      }
    });

    let current = [];
    try {
      current = await testOpcServer.readValues(readNodeIds);
    } catch (error) {
      console.warn('History: 读取变量值失败:', error);
      current = new Array(allArr.length).fill(null);
    }

    // 获取所有节点的 nodeId，并创建映射关系
    let variableNodeIds = [];
    let nodeIdToIndexMap = new Map();
    
    allArr.forEach((item, index) => {
      if (item.nodeId?._nodeId) {
        variableNodeIds.push(item.nodeId._nodeId);
        nodeIdToIndexMap.set(item.nodeId._nodeId.toString(), index);
      }
    });

    // 批量读取所有节点的变量属性
    let allVariableAttributes = [];
    try {
      allVariableAttributes = await testOpcServer.readVariableAttributes(variableNodeIds);
    } catch (error) {
      console.warn('History: 读取变量属性失败:', error);
      allVariableAttributes = new Array(variableNodeIds.length).fill(null);
    }

    // 使用 processVariablesData 的逻辑处理数据，转换为树节点格式
    const processedNodes = allArr.map((item, index) => {
      let typeValue = current[index]?.value?.type;
      let dataValue = current[index] || null;
      
      if (!dataValue) {
        return null;
      }

      let translateTypes = findObjectIdByType(typeValue);
      item.translateTypes = translateTypes;
      
      try {
        let currentNodeId = item.nodeId?._nodeId?.toString();
        if (!currentNodeId) {
          return null;
        }
        
        let variableIndex = variableNodeIds.findIndex(nodeId => 
          nodeId.toString() === currentNodeId
        );
        
        if (variableIndex === -1) {
          return null;
        }
        
        let currentID = allVariableAttributes[variableIndex];
        if (!currentID) {
          return null;
        }
        
        // 根据 datatype ID 去 allTypesData 中查找对应的数据类型
        let temp = state.allTypesData.filter((idx) => {
          let typeId = idx.nodeId.toString();
          let dataTypeId = currentID.dataType ? currentID.dataType.toString() : '';
          return typeId === dataTypeId;
        });
        
        let dataTypeDictionary = new UaDataTypeDictionary();
        let dataTypes = dataTypeDictionary.getDataType(currentID);
        
        // 获取显示值
        let editValue = (current[index]?.value && typeof current[index]?.value === 'object')
          ? current[index]?.value?.value
          : current[index]?.value;
        
        // 格式化值显示
        let displayValue = editValue;
        if (temp[0]?._browseName === 'DateTime' && editValue) {
          if (typeof editValue === 'string' || typeof editValue === 'number') {
            const dateValue = new Date(editValue);
            if (!isNaN(dateValue.getTime())) {
              displayValue = dateValue.toLocaleString();
            }
          }
        }
        
        const displayName = getDisplayName(item) || item.browseName?._name || item.browseName || 'Unknown';
        const browseName = item.browseName?._name || item.browseName || '';
        
        // 构建树节点
        return {
          nodeId: item.nodeId?.toString() || `node-${Date.now()}-${Math.random()}`,
          label: `${displayName}${displayValue !== null && displayValue !== undefined ? `: ${displayValue}` : ''}`,
          browseName: browseName,
          displayName: displayName,
          nodeClass: item.nodeClass,
          value: displayValue,
          dataType: temp[0]?._displayName?._text || '',
          dataTypes: temp[0]?._parentType?._browseName ? temp[0]?._parentType?._browseName : (temp[0]?.browseName || 'Unknown'),
          hasChildren: item.hasChildren || false,
          children: []
        };
      } catch (error) {
        console.warn('History: 处理节点失败:', error);
        return null;
      }
    }).filter(node => node !== null);

    // 按优先级排序属性节点
    const priorityAttributes = ['EventId', 'EventType', 'Time', 'ReceiveTime', 'LocalTime', 'Message', 'Severity', 'SourceName', 'SourceNode'];
    const eventAttributeChildren = [];
    const otherChildren = [];
    
    // 先添加优先级属性
    for (const attrName of priorityAttributes) {
      const found = processedNodes.find(node => {
        const browseName = (node.browseName || '').toLowerCase();
        const label = (node.displayName || '').toLowerCase();
        return browseName === attrName.toLowerCase() || label === attrName.toLowerCase() ||
               browseName.includes(attrName.toLowerCase()) || label.includes(attrName.toLowerCase());
      });
      if (found) {
        eventAttributeChildren.push(found);
      }
    }
    
    // 添加其他变量节点
    for (const node of processedNodes) {
      if (!eventAttributeChildren.find(n => n.nodeId === node.nodeId)) {
        if (node.nodeClass === NodeClass.Variable) {
          eventAttributeChildren.push(node);
        } else {
          otherChildren.push(node);
        }
      }
    }

    // 创建 BaseEventType 树节点
    const baseEventTypeNode = {
      nodeId: baseEventTypeNodeId.toString(),
      label: nodeAttributes.displayName?._text || nodeAttributes.displayName?.text || 'BaseEventType',
      browseName: nodeAttributes.browseName?._name || nodeAttributes.browseName,
      displayName: nodeAttributes.displayName?._text || nodeAttributes.displayName?.text,
      nodeClass: nodeAttributes.nodeClass,
      description: nodeAttributes.description?._text || nodeAttributes.description?.text || '',
      hasChildren: eventAttributeChildren.length > 0 || otherChildren.length > 0,
      children: [...eventAttributeChildren, ...otherChildren]
    };

    // 将 BaseEventType 节点添加到 EmployeeCheckInEventType 的 children 中
    const employeeNode = findNodeInHistoryEventTree(historyEventTypesTreeData.value, 'EmployeeCheckInEventType');
    if (employeeNode) {
      if (!employeeNode.children) {
        employeeNode.children = [];
      }
      // 检查是否已存在，避免重复添加
      const existingIndex = employeeNode.children.findIndex(child => child.nodeId === baseEventTypeNodeId.toString());
      if (existingIndex > -1) {
        employeeNode.children[existingIndex] = baseEventTypeNode;
      } else {
        employeeNode.children.push(baseEventTypeNode);
      }
      // 强制更新树
      historyEventTypesTreeData.value = [...historyEventTypesTreeData.value];
      console.log('History: 已加载 BaseEventType 详细信息');
    }
  } catch (error) {
    console.error('History: 加载 BaseEventType 详细信息失败:', error);
  }
};

// 处理事件类型树节点双击
const handleEventTypeNodeDblClick = async (data, node) => {
  // 如果是 EmployeeCheckInEventType 节点，重新加载 BaseEventType (i=2041) 的信息
  if (data.nodeId === 'EmployeeCheckInEventType') {
    await loadBaseEventTypeDetailsForHistory();
    ElMessage.success('已重新加载 BaseEventType 节点信息');
  }
};

// 在历史事件类型树中查找节点
const findNodeInHistoryEventTree = (tree, nodeId) => {
  for (const node of tree) {
    if (node.nodeId === nodeId) {
      return node;
    }
    if (node.children && node.children.length > 0) {
      const found = findNodeInHistoryEventTree(node.children, nodeId);
      if (found) return found;
    }
  }
  return null;
};

// 处理事件类型树复选框变化
const handleEventTypeTreeCheck = (data, checkedInfo) => {
  const { checkedNodes, checkedKeys } = checkedInfo;
  historyEventTypeCheckedNodes.value = checkedNodes || [];
};

onUnmounted(() => {
  if (historyTrendChart) {
    historyTrendChart.dispose();
    historyTrendChart = null;
  }
});

// 注册组件
const components = {
};
</script>

<style scoped>
.ai-panel-container {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.adaptive-tab-pane {
  height: 100%;
  overflow: hidden;
}

/* Where 条件配置样式 */
.where-tab-content {
  padding: 8px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.where-field-selector {
  width: 100%;
}

.where-conditions-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 200px;
  overflow-y: auto;
}

.where-condition-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
  background-color: var(--el-fill-color-light);
  border-radius: 4px;
}

.condition-label {
  min-width: 80px;
  font-size: 13px;
  color: var(--el-text-color-primary);
}

.condition-value {
  flex: 1;
}

.where-empty-tip {
  text-align: center;
  color: var(--el-text-color-secondary);
  font-size: 12px;
  padding: 16px;
}

.where-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-start;
}

/* History Details Section 滚动样式 */
.history-details-section {
  display: flex;
  flex-direction: column;
  /* flex: 1; */
  /* min-height: 0;
  overflow: hidden; */
  /* overflow-y: auto; */
}

.history-details-list {
  max-height: 200px;
  overflow-y: auto;
}

.details-table-container {
  padding-bottom: 12px;
}

.details-table {
  margin-bottom: 8px;
  box-shadow: 0 5px 8px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

/* el-tabs__content 下树节点默认高度 180px，超出滚动 */
.history-event-type-tabs :deep(.el-tabs__content) {
  height: 200px;
  overflow-y: auto;
}

.history-event-types-tree {
  max-height: 100%;
  overflow-y: auto;
}

/* Discovery Config Dialog */
.discovery-input-section {
  margin-bottom: 16px;
}

.discovery-list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.discovery-list-title {
  font-size: 13px;
  font-weight: 500;
  color: var(--el-text-color-primary);
}

.discovery-address-list {
  max-height: 240px;
  overflow-y: auto;
  padding-right: 4px;
}

.discovery-address-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  margin-bottom: 6px;
  background-color: var(--el-fill-color-light);
  border-radius: 6px;
  border: 1px solid var(--el-border-color-lighter);
  transition: all 0.2s ease;
}

.discovery-address-item:hover {
  background-color: var(--el-fill-color);
  border-color: var(--el-border-color);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.discovery-address-info {
  display: flex;
  align-items: center;
  gap: 8px;
  overflow: hidden;
  flex: 1;
  min-width: 0;
}

.discovery-address-index {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--el-color-primary-light-8);
  color: var(--el-color-primary);
  border-radius: 50%;
  font-size: 11px;
  font-weight: 600;
}

.discovery-address-icon {
  flex-shrink: 0;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.discovery-address-text {
  font-size: 13px;
  color: var(--el-text-color-regular);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.discovery-list-enter-active,
.discovery-list-leave-active {
  transition: all 0.3s ease;
}

.discovery-list-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}

.discovery-list-leave-to {
  opacity: 0;
  transform: translateX(20px);
}

.discovery-table-wrap {
  position: relative;
  min-height: 320px;
}

.discovery-table-loading {
  position: absolute;
  left: 1px;
  right: 1px;
  top: 41px;
  bottom: 1px;
  z-index: 20;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.78);
  color: var(--el-color-primary);
  font-size: 14px;
}

.discovery-table-loading-icon {
  font-size: 18px;
  animation: none !important;
  transform: none !important;
}

.discovery-table-wrap.is-loading :deep(.el-table__empty-block) {
  display: none;
}

:deep(.discovery-find-dialog .discovery-search-btn.el-button:hover) {
  border-color: #409eff;
  background-color: #409eff;
  color: #ffffff;
  opacity: 1;
  box-shadow: 0 6px 14px rgba(64, 158, 255, 0.22);
}

:deep(.discovery-find-dialog .el-button .el-icon.is-loading),
:deep(.discovery-find-dialog .el-button .el-icon[class*="is-loading"]),
:deep(.discovery-find-dialog .el-button.is-loading .el-icon),
:deep(.discovery-find-dialog .el-button.is-loading .el-icon svg) {
  animation: none !important;
  transform: none !important;
}
</style>
