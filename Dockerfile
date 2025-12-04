# cspell:ignore Dskip temurin Dlog
# 第一阶段：构建阶段
FROM maven:3.9.9 AS build

# 设置工作目录
WORKDIR /study-java

# 复制 Maven 配置和源码，直接进行打包
# 说明：
#  - 原来这里用了 `mvn dependency:go-offline -B` 来提前下载所有依赖，但在 CI 多架构构建时，
#    会尝试解析某些与当前平台不匹配的 Netty native 依赖（例如 osx-aarch_64），导致构建失败。
#  - 实际上 `mvn clean package` 本身就会按需下载依赖，这里直接打包即可，影响不大。
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -P prod

# 第二阶段：运行阶段
FROM eclipse-temurin:21-jre

# 优化：设置时区为上海
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 设置工作目录
WORKDIR /study-java

# 优化：创建非 root 用户运行应用
RUN useradd -m -s /bin/bash appuser

# 将构建阶段的 JAR 文件复制到运行阶段
COPY --from=build /study-java/target/study-java-1.0-SNAPSHOT.jar ./study-java.jar

# 优化：创建日志目录并授权
RUN mkdir -p /study-java/logs && chown -R appuser:appuser /study-java

# 切换到非 root 用户
USER appuser

# 设置环境变量
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8084

# 优化：添加 JVM 容器感知参数
CMD ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+ExitOnOutOfMemoryError", "-Dlog.path=/study-java/logs", "-jar", "study-java.jar"]

# 执行脚本
# docker build -t xcy960815/study-java:1.x .


