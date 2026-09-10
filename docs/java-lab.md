# Java/JVM 能力实验室

该模块提供 JVM 内部诊断与受控 JFR 录制，不重复现有 OSHI 宿主机 CPU、物理内存、磁盘和操作系统监控。所有路径都受现有 Bearer Token 拦截器和 `@PreAuthorize` 控制；以下路径还需加上当前 profile 的 context path（例如 `/dev-api`）。

## 配置

```yaml
java-lab:
  enabled: true
  dangerous-demo-enabled: false
  jfr:
    enabled: true
    directory: jfr-recordings
    max-duration-seconds: 600
    max-size-mb: 500
```

- `java-lab.enabled` 控制 JVM 实验室。
- `java-lab.jfr.enabled` 控制 JFR 管理能力。
- `directory` 由服务端配置，必须解析到项目运行目录内；客户端不能指定目录或文件名。
- 单次录制时长限制为 1～600 秒，并且不能超过 `max-duration-seconds`；文件最大值不能超过 500 MB。
- `dangerous-demo-enabled` 默认且应保持为 `false`。当前模块不提供危险演示接口。

运行文件位于 `jfr-recordings/`，该目录已加入 `.gitignore`。

## 接口与权限

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| GET | `/lab/jvm/summary` | `monitor:jvm:query` | JVM、PID、时间、堆/非堆、线程、类加载、GC 汇总 |
| GET | `/lab/jvm/memory-pools` | `monitor:jvm:query` | 当前 JVM 暴露的全部内存池 |
| GET | `/lab/jvm/gc` | `monitor:jvm:query` | 全部 GC 及其管理的内存池 |
| GET | `/lab/jvm/threads/summary` | `monitor:jvm:query` | 线程数量、状态分布和 CPU 时间能力 |
| GET | `/lab/jvm/threads/dump?maxDepth=100` | `monitor:jvm:thread` | Thread Dump；深度 1～200 |
| GET | `/lab/jvm/threads/deadlocks?maxDepth=100` | `monitor:jvm:thread` | JVM 检出的死锁；没有时返回 `[]` |
| POST | `/lab/jfr/recordings` | `monitor:jfr:manage` | 启动唯一录制任务 |
| GET | `/lab/jfr/recordings/current` | `monitor:jfr:manage` | 当前任务；没有时返回 HTTP 204 |
| POST | `/lab/jfr/recordings/{id}/stop` | `monitor:jfr:manage` | 停止任务；重复停止返回相同停止状态 |
| GET | `/lab/jfr/recordings/{id}/download` | `monitor:jfr:manage` | 下载已停止任务的 `.jfr` 文件 |
| DELETE | `/lab/jfr/recordings/{id}` | `monitor:jfr:manage` | 删除已停止任务及登记信息 |

启动请求示例：

```json
{
  "configuration": "profile",
  "durationSeconds": 60
}
```

`configuration` 只允许 `default` 或 `profile`。`default` 开销较低，适合较长观察；`profile` 采集更细，适合短时定位问题。同一进程同时只能录制一个任务。

## 使用的 JDK API

- `ManagementFactory` 获取平台 MXBean。
- `RuntimeMXBean` 提供 JVM 实现、PID、启动时间和运行时长。
- `MemoryMXBean` 和动态枚举的 `MemoryPoolMXBean` 提供堆、非堆及各内存池信息。模块不假定 G1、ZGC、Shenandoah 或任何内存池名称；`max=-1` 表示未定义，使用率返回 `null`。
- `GarbageCollectorMXBean` 提供收集次数、耗时及关联内存池。
- `ThreadMXBean` 提供状态统计、锁信息、Thread Dump、CPU 时间能力和死锁检测。死锁检测优先 `findDeadlockedThreads`，JVM 不支持时回退 `findMonitorDeadlockedThreads`。
- `ClassLoadingMXBean` 提供当前、累计加载及卸载类数量。
- `jdk.jfr.Recording` 使用 JDK 内置 `default`/`profile` 配置录制并限制时长和文件大小。

接口不读取或返回环境变量、请求头、数据库/Redis 配置、JWT 密钥、API Key 或任意系统属性集合。

## 阅读 Thread Dump

先看 `state`：持续 `RUNNABLE` 可能是 CPU 热点；大量 `BLOCKED` 通常表示 monitor 竞争；`WAITING`/`TIMED_WAITING` 常见于线程池、队列和定时等待，不一定异常。再对照 `lockName`、`lockOwnerId`、`lockOwnerName` 找锁的持有关系，结合 `blockedCount`/`waitedCount` 判断长期竞争。最后从裁剪后的 `stackTrace` 顶部向下定位业务调用。死锁接口只报告 JVM 已确认的环，不应替代对完整 Dump 和业务时序的分析。

## 使用 JFR 与 JDK Mission Control

1. 以 `profile` 启动 30～120 秒录制并复现问题。
2. 停止后下载 `.jfr`；自动到期的任务也会生成文件。
3. 安装并启动 JDK Mission Control（JMC），选择 **File → Open File**，打开下载的 `.jfr`。
4. 依次查看 Automated Analysis Results、Threads、Method Profiling、Memory 和 Locks。短录制可能没有足够样本，应在可控负载下适当延长。

## 安全边界与风险

JFR 和 Thread Dump 会暴露类名、方法名、线程名、锁关系及部分运行行为，因此权限应只授予可信运维人员，下载文件也应按敏感诊断资料保存。服务仅接受 UUID `recordingId`，下载对象必须存在于进程内登记表，实际路径必须等于服务端生成的 `<recordingId>.jfr` 且位于受控目录。录制中禁止下载和删除，应用关闭时会停止并关闭 `Recording`。

本模块明确不实现任意命令执行、任意文件读取、Heap Dump、远程类加载、杀进程、`System.exit`、`Runtime.exec` 或 `ProcessBuilder`。生产环境建议按需开启 JFR，诊断完成后关闭能力或撤销权限，并监控磁盘空间和下载审计。

## 手工验证

以具有相应权限的用户取得 token 后，将 `$BASE` 设置为环境 API base：

```bash
curl -H "Authorization: Bearer $TOKEN" "$BASE/lab/jvm/summary"
curl -H "Authorization: Bearer $TOKEN" "$BASE/lab/jvm/memory-pools"
curl -H "Authorization: Bearer $TOKEN" "$BASE/lab/jvm/threads/dump?maxDepth=20"
curl -X POST -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"configuration":"default","durationSeconds":30}' "$BASE/lab/jfr/recordings"
```

记录返回的 `recordingId`，调用 stop，再调用 download。另应验证非法 `maxDepth=0`、`recordingId=../../etc/passwd`、并发启动和录制中删除均返回统一业务错误，普通无权限用户返回 HTTP 403。
