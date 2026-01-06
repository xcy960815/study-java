# 第一阶段：构建阶段
FROM maven:3.9.9 AS build
WORKDIR /study-java

# 复制 Maven 配置和源码
COPY pom.xml .
COPY src ./src

# 打包
RUN mvn clean package -DskipTests -P prod

# 关键步骤：使用 layertools 提取分层文件
# 这会将 JAR 包拆解为 dependencies, spring-boot-loader, snapshot-dependencies, application 四个目录
RUN java -Djarmode=layertools -jar target/study-java-1.0-SNAPSHOT.jar extract

# 第二阶段：运行阶段
FROM eclipse-temurin:21-jre

# 优化：设置时区为上海
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 设置工作目录
WORKDIR /study-java

# 优化：创建非 root 用户运行应用
RUN useradd -m -s /bin/bash appuser

# 关键步骤：复制分层文件 (利用 Docker 缓存)
# 依赖层 (变动最少，最先复制)
COPY --from=build /study-java/dependencies/ ./
# Loader 层
COPY --from=build /study-java/spring-boot-loader/ ./
# 快照依赖层
COPY --from=build /study-java/snapshot-dependencies/ ./
# 应用代码层 (变动最频繁，最后复制)
COPY --from=build /study-java/application/ ./

# 优化：创建日志目录并授权
RUN mkdir -p /study-java/logs && chown -R appuser:appuser /study-java

# 创建启动脚本
RUN echo '#!/bin/bash\n\
# 确保日志目录存在且可写\n\
mkdir -p /study-java/logs\n\
if [ ! -w /study-java/logs ]; then\n\
    echo "Warning: /study-java/logs is not writable. Please check volume permissions."\n\
fi\n\
# 启动应用 (使用 JarLauncher)\n\
# 注意：Spring Boot 3.1 使用 org.springframework.boot.loader.JarLauncher\n\
exec java -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError -Dlog.path=/study-java/logs org.springframework.boot.loader.JarLauncher\n\
' > /study-java/entrypoint.sh && \
    chmod +x /study-java/entrypoint.sh && \
    chown appuser:appuser /study-java/entrypoint.sh

# 切换到非 root 用户
USER appuser

# 设置环境变量
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8084

ENTRYPOINT ["/study-java/entrypoint.sh"]
