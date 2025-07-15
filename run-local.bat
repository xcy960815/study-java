@echo off

REM 创建日志目录（如果不存在）
if not exist .\logs mkdir .\logs

REM 使用系统属性设置日志路径为相对路径./logs
java -Dlog.path=./logs -jar target\study-java-1.0-SNAPSHOT.jar --spring.profiles.active=dev 