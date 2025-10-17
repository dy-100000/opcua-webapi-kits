// src/utils/socket/index.js
export { WebSocketService } from './WebSocketService';
export { useWebSocket } from './useWebSocket';

// 默认导出
import { WebSocketService } from './WebSocketService';
import { useWebSocket } from './useWebSocket';

export default {
  WebSocketService,
  useWebSocket
};


//使用demo

{/* <template>
  <div>
    <p>Connection Status: {{ isConnected ? 'Connected' : 'Disconnected' }}</p>
    <button @click="connect" :disabled="isConnected">Connect</button>
    <button @click="disconnect" :disabled="!isConnected">Disconnect</button>
    
    <input v-model="messageInput" @keyup.enter="sendMessage">
    <button @click="sendMessage" :disabled="!isConnected">Send</button>
    
    <div v-for="(msg, index) in messages" :key="index">
      {{ msg.timestamp }} - {{ msg.type }}: {{ msg.content }}
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useWebSocket } from '@/utils/socket';

const messageInput = ref('');

const { 
  isConnected, 
  messages, 
  stats, 
  connect, 
  disconnect, 
  send 
} = useWebSocket('wss://your-websocket-server.com', {
  reconnectAttempts: 5,
  reconnectDelay: 3000,
  heartbeatInterval: 10000
});

const sendMessage = () => {
  if (messageInput.value.trim()) {
    send(messageInput.value);
    messageInput.value = '';
  }
};
</script> 


//第二种

import { WebSocketService } from '@/utils/socket';

const wsService = new WebSocketService('wss://your-websocket-server.com', {
  reconnectAttempts: 5,
  reconnectDelay: 3000
});

wsService.on('open', () => {
  console.log('Connected to WebSocket server');
});

wsService.on('message', (data) => {
  console.log('Received message:', data);
});

wsService.connect();

// 发送消息
wsService.send('Hello WebSocket!');

// 断开连接
// wsService.disconnect();

*/




}