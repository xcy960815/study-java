# Project Overview

## 项目用途

`study-java` 是一个 Spring Boot 后端项目，面向后台管理类 API。当前源码覆盖用户管理、角色/菜单/权限、商品、订单、数据字典、文件上传、操作日志、服务器监控和 AI 服务集成。

AI 集成包括：

- DeepSeek：读取 `deepseek.api.key` 和 `deepseek.api.base-url`。
- 本地 Ollama：代码中固定访问 `http://localhost:11434`。

## 运行形态

- Java 版本：`pom.xml` 指定 Java `21`。
- 框架：Spring Boot `3.1.4`。
- 构建工具：Maven；当前没有 `mvnw`。
- 主包：`com.studyjava`。
- 启动类：`StudyJavaApplication`，包含 `@MapperScan("com.studyjava.mapper")` 和 `@EnableScheduling`。
- Maven profiles：
  - `dev`：默认启用，端口 `8082`，上下文路径 `/dev-api`。
  - `pre`：端口 `8083`，上下文路径 `/pre-api`。
  - `prod`：端口 `8084`，上下文路径 `/prod-api`。

## 主要目录

- `src/main/java/com/studyjava/controller/`：REST 控制器。
- `src/main/java/com/studyjava/controller/monitor/`：服务器、报表、操作日志监控 API。
- `src/main/java/com/studyjava/service/`：service 接口和 AI 基类。
- `src/main/java/com/studyjava/service/impl/`：service 实现。
- `src/main/java/com/studyjava/domain/dao/`：MyBatis 持久化/数据对象。
- `src/main/java/com/studyjava/domain/dto/`：请求 DTO 和外部接口响应 DTO。
- `src/main/java/com/studyjava/domain/vo/`：接口响应对象，以及部分 AI/文件接口使用的请求对象。
- `src/main/java/com/studyjava/mapper/`：MyBatis mapper 接口。
- `src/main/resources/mapper/`：MyBatis XML SQL。
- `src/main/java/com/studyjava/component/`：JWT、Redis 封装、认证拦截器、操作日志切面等横切组件。
- `src/main/java/com/studyjava/config/`：Spring、Redis、MyBatis Plus、WebSocket、Kaptcha、上传、Jackson、线程池和拦截器配置。
- `src/main/resources/sql/study_java.sql`：数据库 schema/seed SQL。
- `docs/`：部署、本地开发和环境变量说明。
- `scripts/`：iStoreOS Docker 网络辅助脚本。

## 外部依赖

- MySQL：保存业务数据。
- Redis：保存验证码、JWT 签名密钥、access token、refresh token 和缓存/会话类状态。
- DeepSeek API：需要 `DEEPSEEK_API_KEY`；base URL 默认 `https://api.deepseek.com`。
- Ollama：调用 Ollama 相关接口时，本机 `11434` 端口需要有服务。

## 生成物和本地状态

不要手动编辑或提交：

- `target/`
- `logs/`
- `uploadFiles/`
- `uploadLargeFiles/`
- `.idea/`、`.vscode/` 和 IDE 工程文件
- 真实 `.env` 文件
- `.DS_Store`

已跟踪但需谨慎处理：

- `src/main/resources/dump.rdb` 是二进制 Redis 数据，不要手动编辑。
- `src/main/resources/sql/study_java.sql` 是源码级数据库 SQL，改 schema 或 seed 数据时要同步维护。

## 文档现状提示

`readme.md` 和部分 `docs/` 文档存在过期引用，例如 `run-local.sh`、`study-java-compose.yml`、Swagger/OpenAPI 访问方式。未来 agent 应优先相信当前实际存在的 `pom.xml`、`docker-compose.yml` 和源码结构。
