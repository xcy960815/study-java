# 本地开发指南 - 环境变量配置

## ✅ 您的配置已经可以直接使用了！

您的 `.env.local` 文件已经包含了 DeepSeek API Key，可以直接用于本地开发。

## 🚀 本地启动方式

### 方式1：使用 Docker Compose（推荐用于测试完整环境）

```bash
# 使用 .env.local 文件启动
docker-compose -p study-java -f study-java-compose.yml --env-file .env.local up -d

# 查看日志
docker logs study-java-container -f

# 验证环境变量
docker exec study-java-container env | grep DEEPSEEK
```

### 方式2：使用默认 .env 文件

```bash
# 会自动读取同目录下的 .env 文件
docker-compose -p study-java -f study-java-compose.yml up -d
```

### 方式3：直接用 IDE 启动（开发调试）

#### IntelliJ IDEA 配置步骤：

1. **打开 Run/Debug Configurations**
   - 菜单：Run → Edit Configurations

2. **添加环境变量**
   - 找到 "Environment variables" 字段
   - 点击右侧的文件夹图标
   - 添加以下变量：
   ```
   DEEPSEEK_API_KEY=YOUR_API_KEY
   DEEPSEEK_BASE_URL=https://api.deepseek.com
   SPRING_DATA_REDIS_HOST=localhost
   SPRING_DATA_REDIS_PORT=6381
   SPRING_DATA_REDIS_PASSWORD=123456
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3308/study_java?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=123456
   ```

3. **或者使用 .env 文件插件**
   - 安装 "EnvFile" 插件
   - 在 Run Configuration 中勾选 "Enable EnvFile"
   - 选择 `.env.local` 文件

## 📝 完善您的 .env.local（可选）

如果您想让 `.env.local` 包含完整配置，可以添加以下内容：

```bash
# ==================== 项目版本 ====================
IMAGE_VERSION=latest
WEB_IMAGE_VERSION=latest

# ==================== 数据库配置 ====================
MYSQL_ROOT_PASSWORD=123456

# ==================== Redis配置 ====================
REDIS_PASSWORD=123456

# ==================== 系统配置 ====================
TIMEZONE=Asia/Shanghai

# ==================== DeepSeek API配置 ====================
DEEPSEEK_API_KEY=YOUR_API_KEY
DEEPSEEK_BASE_URL=https://api.deepseek.com
```

## 🔍 验证配置是否生效

### 1. 启动应用后检查日志

```bash
# Docker 方式
docker logs study-java-container -f | grep -i deepseek

# IDE 方式
# 查看控制台输出
```

### 2. 检查环境变量

```bash
# 进入容器查看
docker exec -it study-java-container bash
echo $DEEPSEEK_API_KEY   # (placeholder)
echo $DEEPSEEK_BASE_URL
```

### 3. 测试 API 调用

如果您的代码中使用了 DeepSeek API，可以通过以下方式验证：

```bash
# 调用测试接口
curl http://localhost:8084/api/test/deepseek

# 或者查看 actuator 端点（如果启用）
curl http://localhost:8084/actuator/env | grep DEEPSEEK
```

## 🔄 不同环境的配置优先级

Spring Boot 的配置加载顺序（优先级从高到低）：

1. **命令行参数** `-Ddeepseek.api.key=xxx`
2. **环境变量** `DEEPSEEK_API_KEY=YOUR_API_KEY`
3. **application-{profile}.yml** 配置文件
4. **application.yml** 默认配置

所以环境变量会覆盖配置文件中的值！

## 💡 最佳实践建议

### ✅ 推荐做法

1. **本地开发环境**
   - 使用 `.env.local` 配置本地专用的 API Key
   - 可以使用 DeepSeek 提供的免费测试 Key
   - 不要将此文件提交到 Git（已自动忽略）

2. **Docker 测试**
   - 使用 `--env-file .env.local` 指定环境文件
   - 可以模拟生产环境的配置

3. **IDE 调试**
   - 在 Run Configuration 中配置环境变量
   - 更方便断点调试

### ❌ 避免做法

- ❌ 不要将 API Key 硬编码在 `application.yml` 中
- ❌ 不要将包含真实 Key 的文件提交到 Git
- ❌ 不要在多个地方重复配置（容易不一致）

## 🆚 本地 vs 生产环境

| 项目 | 本地开发 | 生产环境 |
|------|---------|---------|
| **API Key** | 测试 Key | 正式 Key |
| **配置方式** | `.env.local` 或 IDE | dPanel 界面 |
| **数据库** | 本地 MySQL | 生产 MySQL |
| **Redis** | 本地 Redis | 生产 Redis |
| **日志级别** | DEBUG | INFO/WARN |
| **是否提交** | ❌ | ❌ |

## 🎓 快速开始示例

### 场景1：首次克隆项目

```bash
# 1. 克隆仓库
git clone https://github.com/your-repo/study-java.git
cd study-java

# 2. 复制环境变量模板
cp .env.example .env.local

# 3. 编辑 .env.local，填入您的 DeepSeek API Key
vim .env.local

# 4. 启动服务
docker-compose -p study-java -f study-java-compose.yml --env-file .env.local up -d

# 5. 查看日志确认启动成功
docker logs study-java-container -f
```

### 场景2：已有项目，添加新的环境变量

```bash
# 1. 更新 .env.local（您的情况）
# 文件已存在，直接添加新变量即可

# 2. 重启服务使配置生效
docker-compose -p study-java -f study-java-compose.yml down
docker-compose -p study-java -f study-java-compose.yml --env-file .env.local up -d
```

## 🐛 常见问题排查

### Q1: 环境变量没有生效？

**检查步骤：**
```bash
# 1. 确认文件存在
ls -la .env.local

# 2. 确认变量格式正确（无空格）
cat .env.local | grep DEEPSEEK

# 3. 确认使用了正确的 env 文件
docker-compose -p study-java -f study-java-compose.yml --env-file .env.local config

# 4. 查看容器内的环境变量
docker exec study-java-container env | grep DEEPSEEK
```

### Q2: Docker 启动失败？

**检查步骤：**
```bash
# 1. 查看详细日志
docker-compose -p study-java -f study-java-compose.yml --env-file .env.local logs

# 2. 检查是否有语法错误
docker-compose -p study-java -f study-java-compose.yml config

# 3. 确认 Redis 和 MySQL 是否正常
docker ps -a | grep study-java
```

### Q3: IDE 启动时找不到配置？

**解决方案：**
- 确认 Run Configuration 中配置了环境变量
- 或者创建 `application-local.yml` 并添加配置
- 设置 Active Profile 为 `local`

## 📚 相关文档

- [Spring Boot 外部化配置文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Docker Compose 环境变量](https://docs.docker.com/compose/environment-variables/)
- [.env 文件最佳实践](https://12factor.net/config)

---

## ✅ 总结

**您现在的配置已经可以直接使用了！**

- ✅ `.env.local` 已包含 DeepSeek API Key
- ✅ 文件已被 .gitignore 保护，不会泄露
- ✅ 可以通过 Docker Compose 或 IDE 直接启动
- ✅ 本地开发和生产环境完全隔离

**直接运行即可：**
```bash
docker-compose -p study-java -f study-java-compose.yml --env-file .env.local up -d
```
