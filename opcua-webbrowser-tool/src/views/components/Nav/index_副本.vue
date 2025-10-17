methods: {
  mapPortToCoordinates(port) {
    // 处理左右两侧的端口 (1-6)
    if (port <= 6) {
      const group = port <= 3 ? 'group1' : 'group2';
      const basePort = group === 'group1' ? port : port - 3;
      let y = (basePort - 1) * 50;
      const x = group === 'group1' ? 0 : 100;
      return { x, y };
    }
    
    // 处理顶部和底部的端口 (7-10)
    else {
      const isTop = port <= 8;  // 7,8 是顶部，9,10 是底部
      const isRight = port % 2 === 0;  // 8,10 是右侧，7,9 是左侧
      
      return {
        x: isRight ? 66.3 : 33.3,
        y: isTop ? 0 : 100
      };
    }
  },

  calculatePortFromPosition(position) {
    if (!position) return null;
    const { posX, posY } = position;

    // 解析坐标值
    const parseValue = (v) => {
      if (typeof v === 'string') return parseFloat(v.replace('%', ''));
      return v;
    };

    // 校验并解析坐标
    const xVal = parseValue(posX);
    const yVal = parseValue(posY);
    if (isNaN(xVal) || isNaN(yVal)) return null;

    // 处理顶部和底部的端口
    if (yVal === 0 || yVal === 100) {
      // 顶部端口
      if (yVal === 0) {
        return Math.abs(xVal - 33.3) < Math.abs(xVal - 66.3) ? 7 : 8;
      }
      // 底部端口
      else {
        return Math.abs(xVal - 33.3) < Math.abs(xVal - 66.3) ? 9 : 10;
      }
    }
    
    // 处理左右两侧的端口
    else {
      // 确定端口组别
      let groupBase;
      if (xVal === 0) {
        groupBase = 1; // 1-3 组
      } else if (xVal === 100) {
        groupBase = 4; // 4-6 组
      } else {
        // 如果不是标准位置，找最近的端口
        if (xVal < 50) {
          groupBase = 1;
        } else {
          groupBase = 4;
        }
      }

      // 计算y偏移量（将0-100映射到0-2）
      const yOffset = Math.round(yVal / 50);
      const clampedOffset = Math.min(Math.max(yOffset, 0), 2); // 限制在0-2范围内

      return groupBase + clampedOffset;
    }
  },

  // 添加一个辅助方法来判断端口位置类型
  getPortType(port) {
    if (port <= 3) return 'left';
    if (port <= 6) return 'right';
    if (port <= 8) return 'top';
    return 'bottom';
  },

  // 在创建连线时使用的方法
  getPortDirections(port) {
    const portType = this.getPortType(port);
    switch (portType) {
      case 'left': return ['left'];
      case 'right': return ['right'];
      case 'top': return ['top'];
      case 'bottom': return ['bottom'];
      default: return ['left', 'right', 'top', 'bottom'];
    }
  }
}