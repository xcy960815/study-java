# 环境变量注入机制详解

## 🎯 核心问题：.env 文件如何注入到 Spring Boot？

### ⚠️ **重要事实**

**Spring Boot 默认 NOT 自动读取 `.env` 或 `.env.local` 文件！**

这些文件是给 **Docker Compose** 使用的，不是 Spring Boot 的原生功能。

---

## 📊 不同场景的注入机制

### 场景1：Docker Compose 启动（✅ 自动注入）

**流程图：**
```
.env.local 文件
  ↓ Docker Compose 读取
docker-compose.yml (environment 配置)
  ↓ 注入到容器
容器内的环境变量 (DEEPSEEK_API_KEY=YOUR_API_KEY)
  ↓ Spring Boot 读取
application.yml (${DEEPSEEK_API_KEY})
  ↓
Spring Bean 中使用
```

**示例：**

```yaml
# docker-compose.yml
services:
  springboot:
    environment:
      DEEPSEEK_API_KEY: ${DEEPSEEK_API_KEY}  # 从 .env.local 读取
```

```yaml
# application.yml
deepseek:
  api:
    key: ${DEEPSEEK_API_KEY}  # 从环境变量读取
```

```bash
# .env.local
DEEPSEEK_API_KEY=YOUR_API_KEY
```

**启动命令：**
```bash
docker-compose -f study-java-compose.yml --env-file .env.local up -d
```

---

### 场景2：Makefile 启动（✅ 我们已配置）

**流程图：**
```
.env.local 文件
  ↓ Makefile export 命令加载
Shell 环境变量
  ↓ Maven/Java 继承
Spring Boot 进程环境变量
  ↓ Spring Boot 读取
application.yml (${DEEPSEEK_API_KEY})
  ↓
Spring Bean 中使用
```

**工作原理：**

```makefile
# Makefile
dev:
	@export $$(cat .env.local | grep -v '^#' | xargs) && \
	mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**关键点：**
- `cat .env.local` - 读取文件内容
- `grep -v '^#'` - 过滤掉注释行
- `xargs` - 转换为 `KEY=VALUE` 格式
- `export $$()` - 导出环境变量
- `&&` - 在同一 shell 中执行后续命令

**启动命令：**
```bash
make dev
```

---

### 场景3：IDE 启动（需手动配置）

#### IntelliJ IDEA 配置方法：

**方法 A：手动配置环境变量**

1. **打开 Run/Debug Configurations**
   - Run → Edit Configurations

2. **添加环境变量**
   - 找到 "Environment variables" 字段
   - 点击右边的文件夹图标
   - 添加变量：
   ```
   DEEPSEEK_API_KEY=YOUR_API_KEY
   DEEPSEEK_BASE_URL=https://api.deepseek.com
   ```

3. **运行**
   - 点击绿色运行按钮

**方法 B：使用 EnvFile 插件**

1. **安装插件**
   - Settings → Plugins → 搜索 "EnvFile"
   - 安装并重启 IDE

2. **配置 Run Configuration**
   - Run → Edit Configurations
   - 勾选 "Enable EnvFile"
   - 添加 `.env.local` 文件

3. **运行**
   - 插件会自动加载 .env.local 中的变量

**流程图：**
```
.env.local 文件
  ↓ 手动配置 或 EnvFile 插件
IDE Run Configuration
  ↓ IDE 启动时注入
Spring Boot 进程环境变量
  ↓ Spring Boot 读取
application.yml (${DEEPSEEK_API_KEY})
```

---

### 场景4：直接命令行启动

**方法 A：export 后启动**
```bash
# 加载环境变量
export DEEPSEEK_API_KEY=YOUR_API_KEY
export DEEPSEEK_BASE_URL=https://api.deepseek.com

# 启动应用
mvn spring-boot:run
```

**方法 B：一行命令**
```bash
DEEPSEEK_API_KEY=YOUR_API_KEY DEEPSEEK_BASE_URL=https://api.deepseek.com mvn spring-boot:run
```

**方法 C：使用 -D 参数**
```bash
mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-Ddeepseek.api.key=sk-xxxxx"
```

---

## 🔧 Spring Boot 环境变量读取语法

### 基本语法：

```yaml
# application.yml
deepseek:
  api:
    key: ${DEEPSEEK_API_KEY}  # 从环境变量读取
```

### 带默认值：

```yaml
# application.yml
deepseek:
  api:
    key: ${DEEPSEEK_API_KEY:your-api-key-here}  # 如果环境变量不存在，使用默认值
    base-url: ${DEEPSEEK_BASE_URL:https://api.deepseek.com}
```

### 在 Java 代码中使用：

```java
@Value("${deepseek.api.key}")
private String apiKey;

@Value("${deepseek.api.base-url}")
private String baseUrl;
```

或者使用 ConfigurationProperties：

```java
@Configuration
@ConfigurationProperties(prefix = "deepseek.api")
public class DeepSeekConfig {
    private String key;
    private String baseUrl;
    
    // getters and setters
}
```

---

## 📋 环境变量优先级

Spring Boot 配置的加载优先级（从高到低）：

1. **命令行参数** 
   ```bash
   java -jar app.jar --deepseek.api.key=xxx
   ```

2. **系统环境变量** 
   ```bash
   export DEEPSEEK_API_KEY=YOUR_API_KEY
   ```

3. **application-{profile}.yml** 
   ```yaml
   # application-prod.yml
   deepseek.api.key: xxx
   ```

4. **application.yml** 
   ```yaml
   # application.yml
   deepseek.api.key: xxx
   ```

**结论：** 环境变量会覆盖配置文件中的值！

---

## 🎓 实战示例

### 完整流程：本地开发

#### 步骤1：配置 .env.local
```bash
# .env.local
DEEPSEEK_API_KEY=YOUR_API_KEY
DEEPSEEK_BASE_URL=https://api.deepseek.com
```

#### 步骤2：配置 application.yml
```yaml
# application.yml
deepseek:
  api:
    key: ${DEEPSEEK_API_KEY:your-api-key-here}
    base-url: ${DEEPSEEK_BASE_URL:https://api.deepseek.com}
```

#### 步骤3：启动应用
```bash
# 方式1：使用 Makefile（推荐）
make dev

# 方式2：手动 export
export $(cat .env.local | grep -v '^#' | xargs)
mvn spring-boot:run

# 方式3：IDE 配置环境变量后运行
```

#### 步骤4：验证配置
```java
@RestController
public class TestController {
    
    @Value("${deepseek.api.key}")
    private String apiKey;
    
    @GetMapping("/test/config")
    public String testConfig() {
        return "API Key: " + apiKey;
    }
}
```

访问 `http://localhost:8084/test/config` 查看是否正确读取。

---

## ⚠️ 常见问题

### Q1: make dev 时环境变量没有生效？

**检查步骤：**

```bash
# 1. 确认 .env.local 存在
ls -la .env.local

# 2. 查看文件内容
cat .env.local

# 3. 测试环境变量加载
export $(cat .env.local | grep -v '^#' | xargs)
echo $DEEPSEEK_API_KEY   # (placeholder)

# 4. 手动启动测试
DEEPSEEK_API_KEY=YOUR_API_KEY mvn spring-boot:run
```

### Q2: IDE 启动时找不到环境变量？

**解决方案：**
- 确认 Run Configuration 中配置了环境变量
- 或者安装 EnvFile 插件并配置
- 或者在启动前 export 环境变量

### Q3: Docker 容器中环境变量没生效？

**检查步骤：**

```bash
# 1. 进入容器
docker exec -it study-java-container bash

# 2. 查看环境变量
env | grep DEEPSEEK

# 3. 确认 docker-compose.yml 配置正确
docker-compose -f study-java-compose.yml config

# 4. 确认使用了正确的 env 文件
docker-compose --env-file .env.local config
```

### Q4: 为什么 Spring Boot 不自动读取 .env 文件？

**原因：**
- `.env` 文件是 Docker Compose 和 Node.js (dotenv) 的约定
- Spring Boot 的原生方式是使用 `application.yml` 和环境变量
- 如果需要支持，可以使用第三方库如 `spring-dotenv`

**如果想让 Spring Boot 自动读取 .env（可选）：**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>me.paulschwarz</groupId>
    <artifactId>spring-dotenv</artifactId>
    <version>4.0.0</version>
</dependency>
```

但**我们不推荐**这样做，因为：
- 增加了额外的依赖
- 我们已经有更好的方案（Makefile + 环境变量）

---

## ✅ 推荐方案总结

### 本地开发：

```bash
# 配置 .env.local
make dev  # Makefile 自动加载环境变量
```

### Docker 测试：

```bash
# Docker Compose 自动读取 .env.local
make docker-up
```

### 生产部署：

```
dPanel 界面配置环境变量（不使用 .env 文件）
```

---

## 🔗 相关资源

- [Spring Boot 外部化配置](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Docker Compose 环境变量](https://docs.docker.com/compose/environment-variables/)
- [12 Factor App - Config](https://12factor.net/config)
