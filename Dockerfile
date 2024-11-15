# 使用 Azul JDK 21 镜像作为运行环境
FROM azul/zulu-openjdk-alpine:21.0.5


# 指定维护者信息
LABEL maintainer="xcy960815"


# 设置工作目录为 study-java
WORKDIR /study-java

# 将本地构建好的 JAR 文件复制到容器中
COPY ./target/study-java-1.0-SNAPSHOT.jar ./study-java.jar

# 启动 Java 应用
CMD ["java", "-jar", "study-java.jar"]

# 暴露容器端口 80
EXPOSE 8084


