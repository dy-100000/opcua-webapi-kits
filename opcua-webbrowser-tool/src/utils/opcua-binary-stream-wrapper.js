// src/utils/opcua-binary-stream-wrapper.js
import { Buffer } from 'buffer';

// 1. 创建必要的全局变量
if (typeof global === 'undefined') {
  window.global = window;
}

if (typeof process === 'undefined') {
  window.process = { env: {} };
}

// 2. 手动实现 CommonJS 环境
const exports = {};
const module = { exports };

// 3. 复制原始模块的核心功能
class BinaryStream {
  constructor(buffer) {
    this.buffer = buffer || Buffer.alloc(0);
    this.length = 0;
  }
  
  writeByte(value) {
    this.buffer = Buffer.concat([this.buffer, Buffer.from([value])]);
    this.length++;
  }
  
  writeInt32(value) {
    const buf = Buffer.alloc(4);
    buf.writeInt32LE(value, 0);
    this.buffer = Buffer.concat([this.buffer, buf]);
    this.length += 4;
  }
  
  writeDouble(value) {
    const buf = Buffer.alloc(8);
    buf.writeDoubleLE(value, 0);
    this.buffer = Buffer.concat([this.buffer, buf]);
    this.length += 8;
  }
  
  writeString(str) {
    const buf = Buffer.from(str, 'utf8');
    this.writeInt32(buf.length);
    this.buffer = Buffer.concat([this.buffer, buf]);
    this.length += buf.length;
  }
  
  // 添加其他必要的方法...
}

class BinaryStreamSizeCalculator {
  constructor() {
    this.length = 0;
  }
  
  writeByte() {
    this.length += 1;
  }
  
  writeInt32() {
    this.length += 4;
  }
  
  writeDouble() {
    this.length += 8;
  }
  
  writeString(str) {
    const buf = Buffer.from(str, 'utf8');
    this.length += 4 + buf.length;
  }
  
  // 添加其他必要的方法...
}

// 4. 导出模块内容
exports.BinaryStream = BinaryStream;
exports.BinaryStreamSizeCalculator = BinaryStreamSizeCalculator;
module.exports = exports;

// 5. 导出给外部使用
export const BinaryStream = exports.BinaryStream;
export const BinaryStreamSizeCalculator = exports.BinaryStreamSizeCalculator;
export default exports;