# Git Hooks 说明文档

## 📖 简介

本目录包含项目的自定义 Git Hooks，用于在代码提交、推送等 Git 操作前自动执行一些检查和清理工作。

## 🚀 快速开始

### 首次设置

在项目根目录执行以下命令：

```bash
bash setup-hooks.sh
```

该脚本会：
1. 配置 Git 使用 `.githooks` 目录
2. 为所有 hook 脚本添加执行权限
3. 显示配置信息

### 手动设置

如果不使用 `setup-hooks.sh` 脚本，也可以手动配置：

```bash
# 1. 配置 Git hooks 路径
git config core.hooksPath .githooks

# 2. 添加执行权限
chmod +x .githooks/pre-commit
```

## 📋 可用的 Hooks

### pre-commit

**用途**：在执行 `git commit` 前自动运行

**功能**：
- 检测 `src/main/resources/application.yml` 文件中的敏感信息
- 自动将真实的 API Key（如 `sk-xxxxxx`）替换为占位符 `your-api-key-here`
- 防止敏感信息被提交到 Git 仓库

**工作流程**：
1. 检查 `application.yml` 是否在暂存区（是否被修改）
2. 如果文件被修改，扫描其中的 API Key
3. 如果检测到真实的 API Key，自动替换为占位符
4. 更新暂存区并继续提交

**示例**：

提交前：
```yaml
deepseek:
  api-key: sk-9ac5792605654c8bb4f9ab06c1db0fc0
```

提交后：
```yaml
deepsesk:
  api-key: your-api-key-here
```

## ⚙️ 配置

### 修改占位符

如果想使用不同的占位符，编辑 `.githooks/pre-commit` 文件：

```bash
# 修改这一行
PLACEHOLDER="your-api-key-here"
```

### 添加更多敏感信息检测

在 `pre-commit` 脚本中添加更多的正则表达式匹配规则：

```bash
# 例如：检测 MySQL 密码
sed -i '' -E "s/(password:\s*)[^\s]+/\1your-password-here/g" "$TEMP_FILE"
```

## 🔍 验证

### 测试 Hook 是否生效

1. 修改 `application.yml`，添加真实的 API Key
2. 执行 `git add` 和 `git commit`
3. 检查提交的内容是否已将 API Key 替换为占位符

```bash
git show HEAD:src/main/resources/application.yml
```

### 查看当前 Hooks 配置

```bash
git config --get core.hooksPath
```

## 📝 注意事项

1. **本地配置**：Hook 只会清理提交到仓库的内容，不会影响本地文件。建议在本地使用环境变量或配置文件管理真实的 API Key。

2. **团队协作**：团队成员需要各自运行 `setup-hooks.sh` 来启用 hooks，因为 `.git/hooks` 目录不会被 Git 追踪。

3. **跳过 Hook**：如果需要临时跳过 hook 检查，可以使用：
   ```bash
   git commit --no-verify
   ```

4. **环境变量**：建议使用环境变量来管理敏感信息：
   ```yaml
   deepseek:
     api-key: ${DEEPSEEK_API_KEY:your-api-key-here}
   ```

## 🛠️ 故障排查

### Hook 没有执行

1. 检查 hooks 路径配置：
   ```bash
   git config --get core.hooksPath
   ```

2. 检查脚本执行权限：
   ```bash
   ls -la .githooks/
   ```

3. 重新运行设置脚本：
   ```bash
   bash setup-hooks.sh
   ```

### 脚本执行出错

查看错误信息并检查：
- 文件路径是否正确
- 正则表达式是否匹配
- 临时文件权限

## 📚 更多资源

- [Git Hooks 官方文档](https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks)
- [Husky - Git Hooks 管理工具](https://typicode.github.io/husky/)
