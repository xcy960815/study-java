#!/bin/bash

# Git Hooks 设置脚本
# 用途：配置 Git 使用自定义的 .githooks 目录
# 作者：Antigravity AI
# 日期：2025-12-08

set -e

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}🔧 开始配置 Git Hooks...${NC}"

# 1. 配置 Git 使用 .githooks 目录
git config core.hooksPath .githooks
echo -e "${GREEN}✅ Git hooks 路径已设置为: .githooks${NC}"

# 2. 给所有 hook 脚本添加执行权限
chmod +x .githooks/*
echo -e "${GREEN}✅ 已为所有 hook 脚本添加执行权限${NC}"

# 3. 显示当前配置
HOOKS_PATH=$(git config --get core.hooksPath)
echo -e "${YELLOW}📍 当前 Git hooks 路径: ${HOOKS_PATH}${NC}"

# 4. 列出可用的 hooks
echo -e "${YELLOW}📋 可用的 Git Hooks:${NC}"
ls -la .githooks/

echo -e "${GREEN}🎉 Git Hooks 配置完成！${NC}"
echo -e "${YELLOW}💡 提示：在提交代码时，pre-commit hook 会自动清理 API Key 等敏感信息${NC}"
