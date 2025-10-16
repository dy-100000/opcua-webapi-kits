本示例代码用于演示UA WebAPI针对MySQL的语义转换功能

1.准备
- 安装并启动MySQL数据，并使用MySQL Workbench对数据库进行配置
- 创建用户（用户名：Employee, 密码： Employee）
- 创建名为employee的数据库Schema，并将其设置为默认schema(右键->Set schema as default)
- 进入本项目(uawebservicedemo)的data目录，找到schema+data.sql，并在MySQL Workbench中执行文件中的SQL语句
- 使用IDEA打开本项目(uawebgatewaydemo)，并用Maven的“同步项目”加载依赖包
- 打开src/main/resources/application.properties，并确认数据库连接配置（如url,username,password）
- 运行main程序启动测试服务

2.测试
- 浏览器打开opcua-webbrowser-tool，网址(https://opcua-webbrowser-tool.opcfoundation.cn/)
- 在Servers下点击add添加连接，并指定名称和URL(http://locahost:4840)

3.数据结构说明
本项目在MySQL数据库中创建了一组简单的员工管理数据集，其表格定义如下：

- Department: 部门信息表，包括部门名称，描述及部门间组织关系
- Project:研发项目表，包括项目的名称，描述，开始/结束时间，预算等
- Employee：员工表，包括员工的基本信息，如姓名，描述
- EmployeeInfo: 员工详细信息表，包括员工的性别，生日，联系方式等
- Skill：员工技能表，包括技能的描述，以及技能的分类（前端开发/后端开发/测试）、分级（初级/中级/高级）

- DepartmentEmployee: 部门与员工关系表，并于表示部门管理的员工
- DepartmentProject: 部门与项目关系表，用于表示部门所设立的研发项目
- EmployeeSkill: 员工与技能关系表，用于表示员工所具备的技能

4.OPC UA模型结构
经过OPC UA WebService的语义转换后，MySQL的数据将以OPC UA的层级方式进行展示

- Objects                           // 根目录
  
  - Skills                          // 展示所有技能，通过查询Skill表获取
    - 高级前端开发                   // 对应Skill表中每行Skill的信息

  - Departments                     // 展示一级部门
    - 研发中心
      - 部门                        // 展示下一级部门，通过查询Department表的父子关系获取信息
        - 开发部
          - 项目                    // 展示部门的项目，通过查询DepartmentProject表获取信息
            - DigitalTwin
          - 员工                    // 展示部门的员工
            - 张三
              - 基本信息            // 员工基本信息，通过查询EmployeeInfo获取信息
              - 技能                // 员工技能信息，通过查询EmployeeSkill  