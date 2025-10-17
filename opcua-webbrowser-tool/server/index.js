const express = require('express');
const cors = require('cors');
const { OPCUAClient, SecurityPolicy, MessageSecurityMode } = require('node-opcua');

const app = express();

// 配置 CORS
const corsOptions = {
  origin: ['http://localhost:5173', 'http://localhost:3000'], // 允许的前端域名
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization'],
  credentials: true, // 允许携带凭证
  maxAge: 86400 // 预检请求缓存时间
};

app.use(cors(corsOptions));
app.use(express.json());

let client = null;
let session = null;

// Connect to OPC UA server
app.post('/api/opcua/connect', async (req, res) => {
  try {
    const { endpointUrl } = req.body;
    
    // Create OPC UA client
    client = OPCUAClient.create({
      securityPolicy: SecurityPolicy.None,
      securityMode: MessageSecurityMode.None,
      connectionStrategy: {
        initialDelay: 1000,
        maxRetry: 3,
        maxDelay: 10 * 1000
      },
      keepSessionAlive: true,
      keepPendingSessionsOnDisconnect: true,
      defaultSecureTokenLifetime: 40000,
      defaultSessionTimeout: 60000
    });

    // Connect to server
    await client.connect(endpointUrl);
    
    // Create session
    session = await client.createSession();
    
    res.json({ success: true, message: 'Connected to OPC UA server' });
  } catch (error) {
    console.error('Connection error:', error);
    res.status(500).json({ success: false, error: error.message });
  }
});

// Read node value
app.post('/api/opcua/read', async (req, res) => {
  try {
    const { nodeId } = req.body;
    
    if (!session) {
      throw new Error('No active session');
    }

    const dataValue = await session.read({ nodeId });
    res.json({ success: true, value: dataValue.toString() });
  } catch (error) {
    console.error('Read error:', error);
    res.status(500).json({ success: false, error: error.message });
  }
});

// Cleanup on server shutdown
process.on('SIGINT', async () => {
  if (session) {
    await session.close();
  }
  if (client) {
    await client.disconnect();
  }
  process.exit(0);
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
}); 