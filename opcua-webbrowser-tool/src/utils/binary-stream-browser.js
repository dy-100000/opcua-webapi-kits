// src/utils/binary-stream-browser.js
import { Buffer } from 'buffer';

export class BinaryStream {
  constructor(buffer) {
    this.buffer = buffer || Buffer.alloc(0);
    this.position = 0;
  }
  
  writeByte(value) {
    const newBuffer = Buffer.alloc(this.buffer.length + 1);
    this.buffer.copy(newBuffer);
    newBuffer.writeUInt8(value, this.buffer.length);
    this.buffer = newBuffer;
    this.position++;
  }
  
  writeInt32(value) {
    const newBuffer = Buffer.alloc(this.buffer.length + 4);
    this.buffer.copy(newBuffer);
    newBuffer.writeInt32LE(value, this.buffer.length);
    this.buffer = newBuffer;
    this.position += 4;
  }
  
  writeDouble(value) {
    const newBuffer = Buffer.alloc(this.buffer.length + 8);
    this.buffer.copy(newBuffer);
    newBuffer.writeDoubleLE(value, this.buffer.length);
    this.buffer = newBuffer;
    this.position += 8;
  }
  
  writeString(str) {
    const strBuffer = Buffer.from(str, 'utf8');
    this.writeInt32(strBuffer.length);
    
    const newBuffer = Buffer.alloc(this.buffer.length + strBuffer.length);
    this.buffer.copy(newBuffer);
    strBuffer.copy(newBuffer, this.buffer.length);
    this.buffer = newBuffer;
    this.position += strBuffer.length;
  }
  
  // 添加其他必要的方法...
  
  getBuffer() {
    return this.buffer;
  }
}

export class BinaryStreamSizeCalculator {
  constructor() {
    this.size = 0;
  }
  
  writeByte() {
    this.size += 1;
  }
  
  writeInt32() {
    this.size += 4;
  }
  
  writeDouble() {
    this.size += 8;
  }
  
  writeString(str) {
    const strBuffer = Buffer.from(str, 'utf8');
    this.size += 4 + strBuffer.length;
  }
  
  getSize() {
    return this.size;
  }
}