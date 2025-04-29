# 第一个阶段：构建阶段
#FROM maven:3.9.9 AS build
##
### 设置工作目录
#WORKDIR /study-java
##
### 将本地项目的 pom.xml 和 src 目录复制到容器中
#COPY pom.xml .
#COPY src ./src
#
## 使用 Maven 执行 clean 和 package
## DskipTests 忽略测试
#RUN mvn clean package -DskipTests -Dspring.profiles.active=prod

# 第二个阶段：运行阶段
FROM openjdk:21

# 设置工作目录为 /app
WORKDIR /app

# 将构建阶段的 JAR 文件复制到运行阶段
COPY ./target/study-java-1.0-SNAPSHOT.jar ./study-java.jar
#COPY --from=build /study-java/target/study-java-1.0-SNAPSHOT.jar ./study-java.jar
# 启动 Java 应用
CMD ["java", "-jar", "study-java.jar"]

#导出端口
EXPOSE 8084


# 执行脚本
# DOCKER_BUILDKIT=1 docker build -t xcy960815/study-java:1.0 .
