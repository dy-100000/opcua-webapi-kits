本项目提供了围绕 OPC UA WebAPI 的成套开发工具，可用于构建基于 UA WebAPI 的服务端与客户端程序，同时还包含一款可视化工具，用于查看和操作 UA WebService 地址空间。目前，与该工具套件配套的教学视频已发布至 B 站，供开发者参考学习。

以下是本套件的具体构成：

1.opcua-webservice-spring：基于 Java Spring 框架的 OPC UA Web 服务开发工具包。支持将异构数据源（如 SQL 数据库、OPC UA 数据、时序数据）及自定义代码逻辑，转化为标准化的 OPC UA 信息模型，最终可通过 UA WebClient 程序实现访问。

2.opcua-webclient-ts：TypeScript 语言开发的 UA WebClient 模块。提供 Browse（浏览）、Read（读取）、Write（写入）、MethodCall（方法调用）等基础 UA Web 服务调用能力，可同时运行于浏览器和 Node 环境。

3.opcua-webbrowser-tool：OPC UA WebService 地址空间可视化工具，功能类似 UaExpert。开发者可直接通过浏览器访问 OPC 基金会官网获取（官网网址：https://opcua-webbrowser-tool.opcfoundation.cn/）。

4.opcua-webservice-node：功能与用法和 opcua-webservice-spring 一致，目前处于待开发阶段，后续将逐步完善。

B站教学网址：

1.https://www.bilibili.com/video/BV1h1syzoEFU/

2.https://www.bilibili.com/video/BV1g1syzoEoM/