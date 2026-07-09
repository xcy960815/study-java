# Testing

## 当前状态

- `pom.xml` 已包含测试依赖：`spring-boot-starter-test`、JUnit 4、JUnit Jupiter。
- 当前 `src/test/java/com/studyjava/config/MyBatisPlusTest.java` 完全被注释掉，而且仍使用过期的 `com.example` 包名。
- 因此当前没有真正可执行的项目测试套件。

## 运行测试

```bash
mvn test
```

在没有活跃测试的现状下，这主要验证 Maven test 生命周期以及 main/test 源码编译。

## 格式验证

```bash
mvn spotless:check
```

Java 代码修改收尾前应运行该命令。如果只是格式问题，可以执行：

```bash
mvn spotless:apply
```

## 如何添加回归测试

优先写不依赖真实 MySQL、Redis、DeepSeek 或 Ollama 的小测试；只有明确要验证集成行为时才连接外部服务。

推荐起点：

- 工具类单元测试：例如 `PasswordUtils`、`AuthRedisKeys`、上传文件名清理逻辑、AI 请求体构建逻辑。
- Service 测试：mock mapper、Redis、JWT 等依赖，覆盖认证和业务规则。
- Controller/MVC slice 测试：mock service，覆盖路由、参数校验、错误响应。
- MyBatis 集成测试：仅在验证 XML SQL 或数据库行为时使用，并说明所需 schema/data。

## 建议测试目录

按主包镜像放置：

- `src/test/java/com/studyjava/utils/`
- `src/test/java/com/studyjava/service/`
- `src/test/java/com/studyjava/controller/`
- `src/test/java/com/studyjava/mapper/`

## 外部依赖注意点

- 认证依赖 Redis 中保存的 token。
- 登录/注册依赖验证码 Redis key 和密码哈希。
- MyBatis 测试需要 `src/main/resources/sql/study_java.sql` 或专门测试 fixture。
- DeepSeek 测试应 mock HTTP 调用或只测试请求构建；不要在测试中使用真实 API key。
- Ollama 测试不应默认假设本机有模型服务，除非明确标记为 integration/manual。

## AI 默认测试流程

1. 先读生产代码路径；涉及 SQL 时必须读 mapper XML。
2. 添加最小回归测试，让它能在没有修复时失败。
3. 默认 mock Redis/MySQL/外部 AI；除非问题就在集成 wiring。
4. 运行 `mvn test`。
5. Java 代码变更后运行 `mvn spotless:check`。
6. 如果无法添加或运行测试，记录具体阻塞点和已做的手工验证。
