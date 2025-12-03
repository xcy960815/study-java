# Study Java 后端项目

## 项目介绍

Study Java 是一个基于 Spring Boot 开发的综合性后端项目，旨在提供完整的用户管理、权限控制、商品管理、订单处理以及AI服务集成等功能。该项目采用了现代化的Java开发技术栈，实现了一套完整的后台管理系统API。

## 功能特性

- **用户管理系统**：用户注册、登录、信息管理、角色分配
- **权限控制**：基于RBAC的权限管理，菜单权限、数据权限
- **商品管理**：商品信息的增删改查、分类管理
- **订单系统**：订单创建、状态管理、支付流程
- **数据字典**：系统参数配置、枚举值管理
- **文件上传**：支持普通文件和大文件分片上传
- **AI服务集成**：
  - DeepSeek API集成：AI模型调用、文本生成
  - Ollama 本地模型集成：支持本地AI模型部署和调用
- **Redis缓存**：提高系统响应速度，减轻数据库压力
- **JWT认证**：安全的用户身份验证机制
- **验证码功能**：提高系统安全性

## 技术栈

- **核心框架**：Spring Boot
- **ORM框架**：MyBatis Plus
- **数据库**：MySQL
- **缓存**：Redis
- **认证授权**：JWT Token + 自定义拦截器
- **API文档**：Swagger/OpenAPI
- **日志框架**：Log4j2
- **其他工具**：Jackson (JSON处理)、Kaptcha (验证码生成)

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Docker (可选，用于容器化部署)

## 安装部署

### 本地开发环境

1. 克隆项目到本地
   ```bash
   git clone https://github.com/yourusername/study-java.git
   cd study-java
   ```

2. 配置数据库
   - 创建MySQL数据库
   - 导入项目根目录下的`study_java.sql`文件

3. 修改配置文件
   - 根据环境修改`src/main/resources/application-dev.yml`中的数据库连接信息和Redis配置

4. 编译运行
   ```bash
   # Linux/macOS
   ./run-local.sh
   
   # Windows
   run-local.bat
   ```

### Docker部署

1. 构建Docker镜像
   ```bash
   docker build -t study-java:latest .
   ```

2. 使用Docker Compose启动服务
   ```bash
   docker-compose -f study-java-compose.yml up -d
   ```

## API文档

启动项目后，可通过以下地址访问API文档：
```
http://localhost:8082/dev-api/swagger-ui/index.html
```

## 目录结构

```
study-java/
├── src/main/java/com/studyjava/
│   ├── component/         # 组件类
│   ├── config/            # 配置类
│   ├── controller/        # 控制器
│   ├── domain/            # 领域模型
│   │   ├── dao/           # 数据访问对象
│   │   ├── dto/           # 数据传输对象
│   │   ├── enums/         # 枚举类
│   │   └── vo/            # 视图对象
│   ├── exception/         # 异常处理
│   ├── mapper/            # MyBatis映射接口
│   ├── service/           # 服务接口及实现
│   │   └── impl/          # 服务实现类
│   ├── typehandler/       # 类型处理器
│   └── utils/             # 工具类
├── src/main/resources/
│   ├── mapper/            # MyBatis XML映射文件
│   ├── application*.yml   # 应用配置文件
│   └── log4j2.xml         # 日志配置
└── src/test/             # 测试代码
```

## 开发指南

### 添加新功能

1. 创建实体类（DAO/DTO/VO）
2. 创建Mapper接口和XML映射文件
3. 实现Service接口和实现类
4. 创建Controller类并定义API接口
5. 配置权限和路由

### 注意事项

- 确保XML映射文件中的namespace与Mapper接口的包路径一致
- 使用统一的响应格式（ResponseResult/ResponseListResult）
- 遵循RESTful API设计规范
- 添加适当的异常处理

## 许可证

[MIT License](LICENSE)

## 联系方式

如有问题或建议，请联系：example@example.com