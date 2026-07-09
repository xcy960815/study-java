# AI Agent Guide

## Project Snapshot

- `study-java` 是 Java 21 / Spring Boot 3.1.4 后端 API 项目。
- 主包名是 `com.studyjava`；应用入口是 `src/main/java/com/studyjava/StudyJavaApplication.java`。
- 主要能力：登录/注册/验证码、JWT + Redis 会话校验、权限字符串校验、用户/角色/菜单/商品/订单/数据字典、文件上传、操作日志、服务器监控、DeepSeek 与 Ollama 集成。
- 持久层使用 MyBatis Plus + XML mapper；mapper XML 位于 `src/main/resources/mapper/`，数据库初始化 SQL 位于 `src/main/resources/sql/study_java.sql`。
- Maven profile 会过滤运行配置：`dev` -> `8082` + `/dev-api`，`pre` -> `8083` + `/pre-api`，`prod` -> `8084` + `/prod-api`。

## Start Here

1. 先读本文件，再按任务打开 `.agents/` 下的主题文档。
2. 安装、运行、构建、格式化命令见 `.agents/development.md`。
3. 项目用途、目录和运行形态见 `.agents/project-overview.md`。
4. 请求流、认证、AI、上传和扩展点见 `.agents/architecture.md`。
5. 当前测试现状和新增测试方式见 `.agents/testing.md`。

## High-Value Paths

- 应用入口：`src/main/java/com/studyjava/StudyJavaApplication.java`
- HTTP 控制器：`src/main/java/com/studyjava/controller/`
- Service 接口和实现：`src/main/java/com/studyjava/service/`、`src/main/java/com/studyjava/service/impl/`
- 数据模型：`src/main/java/com/studyjava/domain/{dao,dto,vo,enums}/`
- MyBatis mapper 接口/XML：`src/main/java/com/studyjava/mapper/`、`src/main/resources/mapper/`
- 认证、Redis、JWT、日志切面：`src/main/java/com/studyjava/component/`
- Spring 配置：`src/main/java/com/studyjava/config/`
- 环境配置：`src/main/resources/application*.yml`、`.env.example`、`docker-compose.yml`

## Default Workflow For AI Agents

1. 修改前先读相关 controller、service 接口、service 实现、DTO/VO/DAO、mapper 接口和 mapper XML。
2. 小范围修改，沿用现有命名，例如 `StudyJavaXxxController`、`StudyJavaXxxService`、`StudyJavaXxxMapper`。
3. 行为变更要补回归测试；如果测试依赖 MySQL/Redis/外部 AI，优先 mock 或明确记录无法覆盖的原因。
4. 先跑最窄的有效验证；涉及公共流程、认证或 mapper 时再扩大验证范围。
5. 不要把真实密钥写入 Git 跟踪文件；使用环境变量或被忽略的 `.env`。

## Common Commands

```bash
mvn test
mvn spotless:check
mvn spotless:apply
mvn spring-boot:run -P dev
mvn clean package -P prod
mvn clean package -DskipTests -P prod
docker build -t study-java:latest .
docker compose --env-file .env up -d
```

注意：

- 当前仓库没有 Maven wrapper，请使用系统 `mvn`。
- `docker-compose.yml` 引用了 `.env.example` 中不完整的镜像/环境变量名；运行前先校验真实 `.env`。
- 部分旧文档提到 `run-local.sh`、`study-java-compose.yml` 和 Swagger 地址，但这些文件/依赖当前不存在。

## Important Constraints

- 生成物或本地状态不要手动编辑/提交：`target/`、`logs/`、`uploadFiles/`、`uploadLargeFiles/`、`.idea/`、`.vscode/`、真实 `.env`、系统/编辑器临时文件。
- `src/main/resources/dump.rdb` 是二进制 Redis dump，不要手动编辑。
- MyBatis XML 的 namespace 必须和 mapper 接口保持一致。
- 大多数接口受 `AuthInterceptorComponent` 保护；公开路径在 `AuthInterceptorConfig` 中维护。
- `@Log` 会通过 AOP 写操作日志；只在需要审计的接口上使用。
- DeepSeek、Redis、MySQL 的部署配置应来自环境变量。
