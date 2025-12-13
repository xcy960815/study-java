# Study Java 项目 Makefile
# 提供统一的命令入口

.PHONY: help dev pre prod build run clean docker-up docker-down docker-logs test

# 默认目标：显示帮助
help:
	@echo "📋 Study Java 可用命令："
	@echo ""
	@echo "  make dev          - 使用 Maven 启动开发环境"
	@echo "  make pre          - 使用 Maven 启动预发布环境"
	@echo "  make prod         - 使用 Maven 启动生产环境"
	@echo "  make build        - 编译打包项目"
	@echo "  make run          - 运行已编译的 JAR 包"
	@echo "  make clean        - 清理编译产物"
	@echo "  make test         - 运行测试"
	@echo ""
	@echo "  make docker-up    - 启动 Docker Compose 服务"
	@echo "  make docker-down  - 停止 Docker Compose 服务"
	@echo "  make docker-logs  - 查看 Docker 日志"
	@echo ""

# 开发模式：使用 Maven 直接运行（加载 .env.local）
dev:
	@echo "🚀 启动开发环境..."
	@if [ -f .env ]; then \
		echo "📝 加载 .env 环境变量..."; \
		export $$(cat .env | grep -v '^#' | xargs) && \
		mvn spring-boot:run -Dspring-boot.run.profiles=dev; \
	else \
		echo "⚠️  .env 文件不存在，使用默认配置"; \
		mvn spring-boot:run -Dspring-boot.run.profiles=dev; \
	fi

# 预发布模式：使用 Maven 直接运行（加载 .env）
pre:
	@echo "🚀 启动预发布环境..."
	@if [ -f .env ]; then \
		echo "📝 加载 .env 环境变量..."; \
		export $$(cat .env | grep -v '^#' | xargs) && \
		mvn spring-boot:run -Dspring-boot.run.profiles=pre; \
	else \
		echo "⚠️  .env 文件不存在，使用默认配置"; \
		mvn spring-boot:run -Dspring-boot.run.profiles=pre; \
	fi

# 生产模式：使用 Maven 直接运行（加载 .env）
prod:
	@echo "🚀 启动生产环境..."
	@if [ -f .env ]; then \
		echo "📝 加载 .env 环境变量..."; \
		export $$(cat .env | grep -v '^#' | xargs) && \
		mvn spring-boot:run -Dspring-boot.run.profiles=prod; \
	else \
		echo "⚠️  .env 文件不存在，使用默认配置"; \
		mvn spring-boot:run -Dspring-boot.run.profiles=prod; \
	fi

# 编译打包
build:
	@echo "🔨 编译打包项目..."
	mvn clean package -DskipTests

# 运行 JAR 包（加载 .env.local）
run:
	@echo "🚀 运行 JAR 包..."
	@mkdir -p ./logs
	@if [ -f .env ]; then \
		echo "📝 加载 .env 环境变量..."; \
		export $$(cat .env | grep -v '^#' | xargs) && \
		java -Dlog.path=./logs -jar target/study-java-1.0-SNAPSHOT.jar --spring.profiles.active=dev; \
	else \
		echo "⚠️  .env 文件不存在，使用默认配置"; \
		java -Dlog.path=./logs -jar target/study-java-1.0-SNAPSHOT.jar --spring.profiles.active=dev; \
	fi

# 清理
clean:
	@echo "🧹 清理编译产物..."
	mvn clean
	rm -rf logs/*

# 运行测试
test:
	@echo "🧪 运行测试..."
	mvn test

# Docker 启动（使用 .env.local）
docker-up:
	@echo "🐳 启动 Docker Compose 服务..."
	docker-compose -p study-java -f study-java-compose.yml --env-file .env up -d

# Docker 停止
docker-down:
	@echo "🐳 停止 Docker Compose 服务..."
	docker-compose -p study-java -f study-java-compose.yml down

# Docker 日志
docker-logs:
	@echo "📋 查看 Docker 日志..."
	docker-compose -p study-java -f study-java-compose.yml logs -f
