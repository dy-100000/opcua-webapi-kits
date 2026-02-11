opcua-webservice-spring是一套基于Java Spring框架的OPC UA Web服务开发套件，可用于将异构数据源（如SQL数据库，OPC UA，时序数据）及代码逻辑转换为OPC UA信息模型，并可通过UA WebClient端程序进行访问。

准备

1.准备好Maven和IDEA开发环境，并使用IDEA打开本项目(core)

2.使用Maven的同步项目加载依赖包

3.使用Maven的compile指令编译项目

4.使用Maven的install指令将Jar包发布到本地库


测试

1.使用IDEA打开examples/uawebservicetest项目进行测试

2.使用Maven的“同步项目”加载依赖包

3.在src/main/java/org.opcfoundation.webservicetest找到TestUaServerApplication.java

4.运行main程序启动测试服务

5.浏览器打开opcua-webbrowser-tool，网址(https://opcua-webbrowser-tool.opcfoundation.cn/)

6.在Servers下点击add添加连接，并指定服务地址(如UaWebService部署在本机，URL地址为http://locahost:4840)
