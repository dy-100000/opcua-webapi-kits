// src/utils/socket/useWebSocket.js
import { ref, onUnmounted } from 'vue';
import { WebSocketService } from './WebSocketService';

export function useWebSocket(url, options = {}) {
  const wsService = new WebSocketService(url, options);
  
  const isConnected = ref(false);
  const messages = ref([]);
  const stats = ref({
    sent: 0,
    received: 0,
    reconnects: 0,
    heartbeats: 0
  });
  
  // 事件处理
  wsService.on('open', () => {
    isConnected.value = true;
  });
  
  wsService.on('close', () => {
    isConnected.value = false;
  });
  
  wsService.on('error', (error) => {
    console.error('WebSocket error:', error);
  });
  
  wsService.on('message', (data) => {
    if (data === options.heartbeatMessage || data === '{"type":"heartbeat"}') {
      stats.value.heartbeats++;
    } else {
      stats.value.received++;
      messages.value.push({
        content: data,
        type: 'incoming',
        timestamp: new Date().toLocaleTimeString()
      });
    }
  });
  
  wsService.on('heartbeat', () => {
    stats.value.heartbeats++;
  });
  
  // 组件卸载时断开连接
  onUnmounted(() => {
    wsService.disconnect();
  });
  
  // 返回方法和状态
  return {
    isConnected,
    messages,
    stats,
    connect: () => wsService.connect(),
    disconnect: () => wsService.disconnect(),
    send: (message) => {
      const success = wsService.send(message);
      if (success) {
        stats.value.sent++;
        messages.value.push({
          content: message,
          type: 'outgoing',
          timestamp: new Date().toLocaleTimeString()
        });
      }
      return success;
    }
  };
}

export default useWebSocket;