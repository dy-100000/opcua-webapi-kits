// src/utils/socket/WebSocketService.js
export class WebSocketService {
  constructor(url, options = {}) {
    this.url = url;
    this.options = {
      reconnectAttempts: 10,
      reconnectDelay: 3000,
      heartbeatInterval: 15000,
      heartbeatMessage: JSON.stringify({ type: 'heartbeat' }),
      ...options
    };
    
    this.ws = null;
    this.reconnectCount = 0;
    this.heartbeatTimer = null;
    this.messageQueue = [];
    this.isConnected = false;
    
    // 事件处理器
    this.eventHandlers = {
      open: [],
      close: [],
      error: [],
      message: [],
      heartbeat: []
    };
  }
  
  connect() {
    try {
      this.ws = new WebSocket(this.url);
      this.ws.onopen = this.handleOpen.bind(this);
      this.ws.onclose = this.handleClose.bind(this);
      this.ws.onerror = this.handleError.bind(this);
      this.ws.onmessage = this.handleMessage.bind(this);
    } catch (error) {
      this.handleError(error);
    }
  }
  
  disconnect() {
    this.stopHeartbeat();
    this.reconnectCount = this.options.reconnectAttempts;
    
    if (this.ws) {
      this.ws.close();
      this.ws = null;
    }
    
    this.isConnected = false;
  }
  
  send(message) {
    if (this.isConnected && this.ws.readyState === WebSocket.OPEN) {
      this.ws.send(typeof message === 'string' ? message : JSON.stringify(message));
      return true;
    } else {
      this.messageQueue.push(message);
      return false;
    }
  }
  
  handleOpen(event) {
    this.isConnected = true;
    this.reconnectCount = 0;
    this.startHeartbeat();
    this.flushMessageQueue();
    this.emit('open', event);
  }
  
  handleClose(event) {
    this.isConnected = false;
    this.stopHeartbeat();
    this.emit('close', event);
    
    // 尝试重新连接
    if (this.reconnectCount < this.options.reconnectAttempts) {
      setTimeout(() => {
        this.reconnectCount++;
        this.connect();
      }, this.options.reconnectDelay);
    }
  }
  
  handleError(error) {
    this.emit('error', error);
  }
  
  handleMessage(event) {
    this.emit('message', event.data);
  }
  
  startHeartbeat() {
    if (this.options.heartbeatInterval > 0) {
      this.heartbeatTimer = setInterval(() => {
        if (this.isConnected) {
          this.send(this.options.heartbeatMessage);
          this.emit('heartbeat');
        }
      }, this.options.heartbeatInterval);
    }
  }
  
  stopHeartbeat() {
    if (this.heartbeatTimer) {
      clearInterval(this.heartbeatTimer);
      this.heartbeatTimer = null;
    }
  }
  
  flushMessageQueue() {
    while (this.messageQueue.length > 0) {
      const message = this.messageQueue.shift();
      this.send(message);
    }
  }
  
  on(event, handler) {
    if (this.eventHandlers[event]) {
      this.eventHandlers[event].push(handler);
    }
  }
  
  off(event, handler) {
    if (this.eventHandlers[event]) {
      this.eventHandlers[event] = this.eventHandlers[event].filter(h => h !== handler);
    }
  }
  
  emit(event, data) {
    if (this.eventHandlers[event]) {
      this.eventHandlers[event].forEach(handler => {
        try {
          handler(data);
        } catch (error) {
          console.error(`Error in event handler for ${event}:`, error);
        }
      });
    }
  }
}

export default WebSocketService;