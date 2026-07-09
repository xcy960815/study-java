# Architecture

## 请求流

典型业务请求路径：

1. `controller/` 下的 Controller 接收 HTTP 请求，并对 DTO/VO 做参数校验。
2. 除公开路径外，`AuthInterceptorComponent` 校验 `Authorization: Bearer ...`。
3. Controller 调用 `service/` 中的接口，由 `service/impl/` 的实现处理业务。
4. Service 通过 `mapper/` 接口调用 `src/main/resources/mapper/*.xml` 中的 SQL。
5. `domain/dao`、`domain/dto`、`domain/vo` 分别承载持久化对象、请求/外部响应对象、接口展示对象。
6. 异常由 `GlobalExceptionHandler` 转为统一错误响应。

## 认证与授权

- `StudyJavaLoginController` 提供 `/login`、`/register`、`/captcha`、`/logout`、`/refreshToken`。
- 验证码由 Kaptcha 生成，使用 `AuthRedisKeys` 生成 Redis key。
- `JwtTokenComponent` 生成 access token 和 refresh token；签名密钥通过 Redis 初始化/保存。
- 登录成功后，access token 和 refresh token 都会写入 Redis。拦截器要求请求 token 与 Redis 中的 access token 一致。
- `AuthInterceptorConfig` 当前公开路径：
  - `/login`
  - `/refreshToken`
  - `/captcha`
  - `/test/**`
  - `/ollama/**`
  - `/deepseek/**`
  - `/v1/chat/completions`
  - `/health`
- `@PreAuthorize` 可放在 Controller 类或方法上。拦截器会读取当前用户权限列表；`*:*:*` 代表超级权限。

## 核心模块

- 用户/认证：`StudyJavaLogin*`、`StudyJavaSysUser*`、`JwtTokenComponent`、`AuthInterceptorComponent`。
- 权限/菜单：`StudyJavaSysRole*`、`StudyJavaSysMenu*`、`@PreAuthorize`。
- 商品/订单：`StudyJavaGoods*`、`StudyJavaOrder*`。
- 数据字典：`StudyJavaSysDataDict*`。
- 操作日志：`@Log`、`StudyJavaLogAspectComponent`、`StudyJavaSysOperLog*`。
- 文件上传：`StudyJavaUploadFileController`、`StudyJavaUploadFileServiceImpl`。
- 监控：`controller/monitor/`、`StudyJavaServerMonitorService`、`DailyReportTask`。
- AI：`StudyJavaAiService`、`StudyJavaDeepSeekServiceImpl`、`StudyJavaOllamaServiceImpl`。

## AI 数据流

- `StudyJavaAiService` 提供通用 HTTP 响应处理和 SSE 行读取逻辑。
- DeepSeek 通过 `DeepSeekConfig` 读取 `deepseek.api.*`，使用 `java.net.http.HttpClient` 和 `aiTaskExecutor`。
- Ollama 相关请求固定访问 `http://localhost:11434`，支持 models/tags/version/ps/show/delete/pull/completions。
- 流式 completions 使用 `SseEmitter`；异步任务运行在线程池 bean `aiTaskExecutor` 上。

## 文件上传流

- 普通上传写入 `uploadFiles/`。
- 分片大文件上传写入 `uploadLargeFiles/`。
- `StudyJavaUploadFileServiceImpl` 会校验文件大小、分片索引，并清理文件名。
- 上传目录是运行期本地状态，已在 `.gitignore` 中忽略。

## 公共 API 模式

- Controller 通常直接返回 VO、`Boolean`、`Map<String,Object>` 或列表。
- `BaseController` 提供 MyBatis Plus 分页辅助：`startPage` 和 `getPageData`。
- 参数校验使用 `jakarta.validation`，错误由 `GlobalExceptionHandler` 处理。
- 业务错误优先抛 `StudyJavaException`；AI 下游错误按场景抛 `StudyJavaAiException`。

## 新增业务资源默认路径

1. 在 `domain/` 下新增或更新 DAO、DTO、VO。
2. 在 `mapper/` 下新增 mapper 接口。
3. 在 `src/main/resources/mapper/` 下新增 mapper XML，namespace 必须对应 mapper 接口。
4. 新增 service 接口和实现。
5. 新增 controller 路由和参数校验。
6. 需要审计的接口加 `@Log`。
7. 需要权限控制的接口加 `@PreAuthorize`。
8. schema 或 seed 数据变化时同步 `src/main/resources/sql/study_java.sql`。
9. 添加或更新测试。

## 扩展点

- 新受保护 API：controller + service + mapper，必要时加 `@PreAuthorize`。
- 新操作日志：使用 `@Log(title = "...", businessType = BusinessType.X)`。
- 新 AI provider：复用/继承 `StudyJavaAiService`，补配置、service、DTO/VO 和 controller。
- 新定时任务：直接使用 Spring Scheduling；启动类已启用 `@EnableScheduling`。
- 新分页查询：使用 `MybatisPlusConfig` 和 `BaseController` 的分页辅助。

## 约束

- 不要把密钥写入 `application*.yml`；沿用现有环境变量占位方式。
- 不要随意扩大 `AuthInterceptorConfig` 的公开路径。
- Redis key 命名保持走 `AuthRedisKeys`。
- mapper Java 签名和 XML SQL 必须同步修改。
- 不要手动编辑上传目录、日志目录、`target/` 或二进制 `dump.rdb`。
