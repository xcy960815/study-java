# 第一个阶段：构建阶段
FROM maven AS build

# 设置工作目录
WORKDIR /study-java

# 将本地项目的 pom.xml 和 src 目录复制到容器中
COPY pom.xml .
COPY src ./src

# 使用 Maven 执行 clean 和 package
RUN mvn clean package -DskipTests

# 第二个阶段：运行阶段
FROM azul/zulu-openjdk-alpine:21.0.5

# 设置工作目录为 /app
WORKDIR /app

# 将构建阶段的 JAR 文件复制到运行阶段
COPY --from=build /study-java/target/study-java-1.0-SNAPSHOT.jar ./study-java.jar

# 启动 Java 应用
CMD ["java", "-jar", "study-java.jar"]

# 暴露容器端口 8084
EXPOSE 8084
