// src/utils/opcua-buffer-utils.js
import { Buffer } from 'buffer';

export function createFastUninitializedBuffer(size) {
  return Buffer.allocUnsafe(size);
}

export function bufferFrom(value, encoding) {
  return Buffer.from(value, encoding);
}

// 使用命名导出而不是 default 导出
export const BufferUtils = {
  createFastUninitializedBuffer,
  bufferFrom,
  bufferAlloc: (size, fill, encoding) => Buffer.alloc(size, fill, encoding),
  bufferConcat: (list, totalLength) => Buffer.concat(list, totalLength),
  bufferToArray: (buf) => Array.from(buf)
};