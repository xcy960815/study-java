# cspell:ignore Dskip temurin Dlog
# 第一阶段：构建阶段
FROM maven:3.9.9 AS build

# 设置工作目录
WORKDIR /study-java

# 将本地项目的 pom.xml 和 src 目录复制到容器中
COPY pom.xml .
COPY src ./src

# 使用 Maven 执行 clean 和 package，并指定使用 prod profile
RUN mvn clean package -DskipTests -P prod

# 第二阶段：运行阶段
FROM eclipse-temurin:21-jre

# 设置工作目录为 /study-java
WORKDIR /study-java

# 将构建阶段的 JAR 文件复制到运行阶段
COPY --from=build /study-java/target/study-java-1.0-SNAPSHOT.jar ./study-java.jar

# 设置环境变量和启动命令
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8084
CMD ["java", "-Dlog.path=/study-java/logs", "-jar", "study-java.jar", "--spring.profiles.active=prod"]

# 执行脚本
# docker build -t xcy960815/study-java:1.x .


