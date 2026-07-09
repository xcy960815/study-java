# Development

## 环境要求

- JDK 21：`pom.xml` 的 compiler source/target 均为 `21`。
- Maven 3.6+：当前仓库没有 Maven wrapper。
- MySQL 和 Redis：完整启动与集成行为需要这两个服务。
- Docker：仅在构建镜像或 Compose 部署时需要。

## 安装依赖

没有独立安装脚本。Maven 会在测试/构建时解析依赖：

```bash
mvn test
```

## 本地启动

使用 Maven `dev` profile 启动：

```bash
mvn spring-boot:run -P dev
```

预期本地 API base：

```text
http://localhost:8082/dev-api
```

健康检查：

```bash
curl http://localhost:8082/dev-api/health
```

`application-dev.yml` 当前把 MySQL/Redis 指向 `192.168.100.1`，端口分别是 `3308` 和 `6381`。如果本机服务地址不同，启动前需要覆盖配置。

## 构建

生产 profile 打包：

```bash
mvn clean package -P prod
```

只为容器产物打包、跳过测试：

```bash
mvn clean package -DskipTests -P prod
```

构建产物在 `target/` 下。

## 类型检查 / 编译检查

Java 编译随 Maven 生命周期执行：

```bash
mvn test
```

如果只想编译、不跑测试：

```bash
mvn -DskipTests compile
```

## Lint / 格式化

`pom.xml` 配置了 Spotless，使用 Google Java Format `1.19.2` 和固定 import 顺序。

检查格式：

```bash
mvn spotless:check
```

自动格式化：

```bash
mvn spotless:apply
```

## 测试

```bash
mvn test
```

注意：当前 `src/test` 里只有一个完全注释掉的测试类，不能把它视为已有测试套件。新增或修改行为前请先读 `.agents/testing.md`。

## Docker

构建后端镜像：

```bash
docker build -t study-java:latest .
```

准备真实且被忽略的 `.env` 后运行 Compose：

```bash
docker compose --env-file .env up -d
```

重要：当前 `docker-compose.yml` 需要 `APP_IMAGE_NAME`、`VUE_WEB_IMAGE_NAME`、`REACT_WEB_IMAGE_NAME` 等变量；`.env.example` 没有定义全部变量。启动前先运行：

```bash
docker compose --env-file .env config
```

## 文档 / 本地预览

当前 Markdown 文档是普通文件，没有 docs 构建或预览命令。

## 常用环境变量

部署和容器运行常用变量：

- `SPRING_PROFILES_ACTIVE`
- `SPRING_DATA_REDIS_HOST`
- `SPRING_DATA_REDIS_PORT`
- `SPRING_DATA_REDIS_PASSWORD`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `DEEPSEEK_API_KEY`
- `DEEPSEEK_BASE_URL`

不要提交真实密钥。`.env` 已被忽略；`.env.example` 只放模板值。
