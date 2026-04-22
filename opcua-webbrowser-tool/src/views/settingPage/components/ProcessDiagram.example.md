# ProcessDiagram 组件使用说明

## 功能说明

ProcessDiagram 组件现在支持根据动态数据展示节点位置和连线。

## Props

### nodeDataList (Array)
节点数据数组，每个节点对象应包含：
- `nodeId` / `id` / `key`: 节点唯一标识
- `name` / `label` / `text`: 节点显示名称
- `type`: 节点类型（可选，如 'source', 'sink', 'valve', 'tank', 'pump', 'motor', 'instrument'）
- `display`: 包含位置和大小信息
  - `posX`: X坐标
  - `posY`: Y坐标
  - `sizeX`: 宽度
  - `sizeY`: 高度
- `value`: 节点数值（可选）
- `status`: 节点状态（可选，'normal', 'warning', 'error'）

### linkDataList (Array)
连线数据数组，每个连线对象应包含：
- `fromState` / `from` / `source`: 起始节点ID
- `toState` / `to` / `target`: 目标节点ID

### nodeTypeMap (Object, 可选)
节点类型映射表，用于根据节点ID确定节点类型。

## 使用示例

### 基础用法

```vue
<template>
  <ProcessDiagram
    :node-data-list="nodeList"
    :link-data-list="linkList"
  />
</template>

<script setup>
import { ref } from 'vue'
import ProcessDiagram from './components/ProcessDiagram.vue'

const nodeList = ref([
  {
    nodeId: 'ns=6;i=7045',
    name: '故障',
    type: 'default',
    display: {
      posX: 100,
      posY: 200,
      sizeX: 60,
      sizeY: 40
    },
    value: '75%',
    status: 'normal'
  },
  {
    nodeId: 'ns=6;i=7046',
    name: '运行',
    type: 'default',
    display: {
      posX: 300,
      posY: 200,
      sizeX: 60,
      sizeY: 40
    }
  }
])

const linkList = ref([
  {
    fromState: 'ns=6;i=7045',
    toState: 'ns=6;i=7046'
  }
])
</script>
```

### 与 modlePage 数据格式兼容

```vue
<template>
  <ProcessDiagram
    :node-data-list="processedNodeData"
    :link-data-list="processedLinkData"
  />
</template>

<script setup>
import { computed } from 'vue'
import ProcessDiagram from './components/ProcessDiagram.vue'

// 假设从API获取的数据格式
const nodeData = [
  {
    nodeId: 'ns=6;i=7045',
    name: '故障',
    display: {
      posX: 100,
      posY: 200,
      sizeX: 20,
      sizeY: 20
    },
    stateNumber: 1
  }
]

const linkData = [
  {
    fromState: 'ns=6;i=7045',
    toState: 'ns=6;i=7046',
    transitionNumber: 1
  }
]

// 处理后的数据可以直接使用
const processedNodeData = computed(() => nodeData)
const processedLinkData = computed(() => 
  linkData.map(item => ({
    from: item.fromState,
    to: item.toState
  }))
)
</script>
```

## 数据格式说明

### 节点数据格式（nodeDataList）

```javascript
[
  {
    nodeId: 'ns=6;i=7045',        // 节点ID（必填）
    name: '节点名称',               // 显示名称
    type: 'default',               // 节点类型（可选）
    display: {                     // 位置信息（必填）
      posX: 100,                   // X坐标
      posY: 200,                   // Y坐标
      sizeX: 60,                   // 宽度
      sizeY: 40                    // 高度
    },
    value: '75%',                  // 节点数值（可选）
    status: 'normal'               // 状态：normal/warning/error（可选）
  }
]
```

### 连线数据格式（linkDataList）

```javascript
[
  {
    fromState: 'ns=6;i=7045',     // 起始节点ID（支持 fromState/from/source）
    toState: 'ns=6;i=7046'        // 目标节点ID（支持 toState/to/target）
  }
]
```

## 特性

1. **动态数据支持**: 组件会根据传入的 nodeDataList 和 linkDataList 动态绘制节点和连线
2. **位置自动映射**: 节点的位置和大小直接从 display 对象中读取
3. **连线自动连接**: 连线会根据 fromState/toState 自动连接对应的节点
4. **类型识别**: 支持多种节点类型，未知类型会使用默认矩形绘制
5. **数据监听**: 当数据变化时，图表会自动重新绘制
6. **向后兼容**: 如果不传入数据，会使用默认的示例数据

## 节点类型

支持的节点类型：
- `source`: 源节点（矩形+箭头）
- `sink`: 汇节点（矩形+箭头）
- `valve`: 阀门（X符号）
- `tank`: 储罐（椭圆形）
- `pump`: 泵（圆形+三角形）
- `motor`: 电机（方形+M）
- `instrument`: 仪表（椭圆形）
- `default`: 默认类型（矩形）

## 注意事项

1. 节点ID必须唯一
2. 连线中的 fromState/toState 必须对应 nodeDataList 中存在的节点ID
3. 如果节点没有定义 type，会使用默认矩形绘制
4. 坐标系统以左上角为原点(0,0)，向右为X正方向，向下为Y正方向

