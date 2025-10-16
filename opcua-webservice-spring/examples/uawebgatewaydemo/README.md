本示例代码用于演示UA WebAPI集成OPC UA Server的功能

1.准备
- 启动一个OPC UA Server，并记住它的url
- 使用IDEA打开本项目(uawebgatewaydemo)
- 使用Maven的“同步项目”加载依赖包
- 在src/main/java/org.opcfoundation.uawebgatewaydemo找到UaWebGatewayService.java
- 在代码第41行，设置endpointUrl为您启动的OPC UA Server的URL（如opc.tcp://127.0.0.1:48010）
- 运行main程序启动测试服务

2.测试
- 浏览器打开opcua-webbrowser-tool，网址(https://opcua-webbrowser-tool.opcfoundation.cn/)
- 在Servers下点击add添加连接，并指定名称和URL(http://locahost:4841/Server-1)