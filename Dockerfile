
# 使用官方Java运行环境作为基础镜像
FROM azul/zulu-openjdk-alpine:21.0.5

# 指定维护者信息
LABEL maintainer="xcy960815"

# 将工作目录设置为/app
WORKDIR /app

COPY target/study-java-1.0-SNAPSHOT.jar app.jar
# 暴露80端口
EXPOSE 8084

# 运行jar文件
ENTRYPOINT ["java","-jar","app.jar"]

