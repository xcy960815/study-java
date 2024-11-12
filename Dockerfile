# 使用 Maven 3.9.9 和 Azul JDK 21 的官方镜像作为构建环境
#FROM azul/zulu-openjdk-alpine:21.0.5 AS build
#
## 设置工作目录为 study-java
#WORKDIR /study-java
#
## 将项目的 pom.xml 和源代码复制到容器中
#COPY pom.xml .
#COPY src ./src
#
#FROM maven:3.9.9
## 使用 Maven 构建项目
#RUN mvn clean package -DskipTests
#
## 使用 Azul JDK 21 镜像作为运行环境
#FROM azul/zulu-openjdk-alpine:21.0.5
#
## 设置工作目录为 study-java
#WORKDIR /study-java
#
## 从构建阶段复制构建好的 JAR 文件到当前容器
#COPY --from=build /study-java/target/study-java-1.0-SNAPSHOT.jar ./study-java.jar
#
## 启动 Java 应用
#CMD ["java", "-jar", "study-java.jar"]
#
## 暴露容器端口 80
#EXPOSE 80




# 使用 Maven 3.9.9 和 Azul JDK 21 的官方镜像作为构建环境
FROM maven:3.9.9-eclipse-temurin-21 AS build

# 设置工作目录为 study-java
WORKDIR /study-java

# 将项目的 pom.xml 和源代码复制到容器中
COPY pom.xml .
COPY src ./src

# 使用 Maven 构建项目
RUN mvn clean package -DskipTests

# 使用 Azul JDK 21 镜像作为运行环境
FROM azul/zulu-openjdk-alpine:21.0.5

# 设置工作目录为 study-java
WORKDIR /study-java

# 从构建阶段复制构建好的 JAR 文件到当前容器
COPY --from=build /study-java/target/study-java-1.0-SNAPSHOT.jar ./study-java.jar

# 启动 Java 应用
CMD ["java", "-jar", "study-java.jar"]

# 暴露容器端口 80
EXPOSE 80
