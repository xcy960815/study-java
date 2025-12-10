#!/bin/bash

# 修复日志目录权限脚本
# 用于解决 Docker 容器中应用无法写入日志文件的问题

echo "正在设置 logs 目录权限..."

# 获取当前目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOGS_DIR="${SCRIPT_DIR}/logs"

# 创建 logs 目录（如果不存在）
mkdir -p "${LOGS_DIR}"

# 设置权限（appuser 的默认 UID/GID 通常是 1000:1000）
# 如果您的系统不同，请根据实际情况调整
chown -R 1000:1000 "${LOGS_DIR}" 2>/dev/null || sudo chown -R 1000:1000 "${LOGS_DIR}"
chmod -R 755 "${LOGS_DIR}"

echo "权限设置完成！"
echo "logs 目录: ${LOGS_DIR}"
ls -la "${LOGS_DIR}" | head -5

