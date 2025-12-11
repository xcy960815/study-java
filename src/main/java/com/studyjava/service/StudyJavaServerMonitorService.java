package com.studyjava.service;

import com.studyjava.domain.vo.StudyJavaServerInfoVo;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

/**
 * 系统监控服务
 *
 * <p>优化说明：使用后台线程定时更新服务器信息缓存，避免每次请求都阻塞1秒计算CPU使用率
 */
@Slf4j
@Service
public class StudyJavaServerMonitorService {

  private static final DecimalFormat DF = new DecimalFormat("0.00");
  private static final int UPDATE_INTERVAL_SECONDS = 3; // 更新间隔（秒）

  private final SystemInfo systemInfo = new SystemInfo();
  private final HardwareAbstractionLayer hardware = systemInfo.getHardware();
  private final OperatingSystem os = systemInfo.getOperatingSystem();

  /** 缓存的服务器信息 */
  private volatile StudyJavaServerInfoVo cachedServerInfo;

  /** 后台更新线程池 */
  private ScheduledExecutorService updateExecutor;

  /** 监控是否正在运行 */
  private volatile boolean isMonitoring = false;

  /**
   * 获取服务器信息（立即返回缓存数据）
   *
   * @return 服务器信息
   */
  public StudyJavaServerInfoVo getServerInfo() {
    // 如果缓存为空，说明监控未启动，执行一次同步计算
    if (cachedServerInfo == null) {

      return calculateServerInfo();
    }
    return cachedServerInfo;
  }

  /**
   * 启动监控（开始后台定时更新）
   *
   * @return 是否成功启动
   */
  public synchronized boolean startMonitoring() {
    if (isMonitoring) {
      log.warn("监控已在运行中");
      return false;
    }



    // 立即执行一次计算
    cachedServerInfo = calculateServerInfo();

    // 创建定时任务
    updateExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
      Thread thread = new Thread(r, "server-monitor-updater");
      thread.setDaemon(true);
      return thread;
    });

    updateExecutor.scheduleAtFixedRate(
        this::updateServerInfoCache,
        UPDATE_INTERVAL_SECONDS,
        UPDATE_INTERVAL_SECONDS,
        TimeUnit.SECONDS);

    isMonitoring = true;
    return true;
  }

  /**
   * 停止监控（停止后台更新）
   *
   * @return 是否成功停止
   */
  public synchronized boolean stopMonitoring() {
    if (!isMonitoring) {
      log.warn("监控未在运行");
      return false;
    }



    if (updateExecutor != null && !updateExecutor.isShutdown()) {
      updateExecutor.shutdown();
      try {
        if (!updateExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
          updateExecutor.shutdownNow();
        }
      } catch (InterruptedException e) {
        updateExecutor.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }

    isMonitoring = false;
    return true;
  }

  /**
   * 检查监控是否正在运行
   *
   * @return 是否正在运行
   */
  public boolean isMonitoring() {
    return isMonitoring;
  }

  /** 后台更新缓存任务 */
  private void updateServerInfoCache() {
    try {

      cachedServerInfo = calculateServerInfo();
    } catch (Exception e) {
      log.error("更新服务器信息缓存失败", e);
    }
  }

  /**
   * 计算服务器信息（会阻塞约1秒）
   *
   * @return 服务器信息
   */
  private StudyJavaServerInfoVo calculateServerInfo() {
    try {
      return StudyJavaServerInfoVo.builder()
          .cpu(getCpuInfo())
          .memory(getMemoryInfo())
          .jvm(getJvmInfo())
          .disk(getDiskInfo())
          .sys(getSysInfo())
          .build();
    } catch (Exception e) {
      log.error("计算服务器信息失败", e);
      return null;
    }
  }

  /**
   * 获取 CPU 信息
   *
   * @return CPU 信息
   */
  private StudyJavaServerInfoVo.CpuInfo getCpuInfo() {
    CentralProcessor processor = hardware.getProcessor();
    long[] prevTicks = processor.getSystemCpuLoadTicks();
    // 等待 1 秒以计算 CPU 使用率（此操作会阻塞）
    Util.sleep(1000);
    long[] ticks = processor.getSystemCpuLoadTicks();
    long nice =
        ticks[CentralProcessor.TickType.NICE.getIndex()]
            - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
    long irq =
        ticks[CentralProcessor.TickType.IRQ.getIndex()]
            - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
    long softirq =
        ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
            - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
    long steal =
        ticks[CentralProcessor.TickType.STEAL.getIndex()]
            - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
    long cSys =
        ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
            - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
    long user =
        ticks[CentralProcessor.TickType.USER.getIndex()]
            - prevTicks[CentralProcessor.TickType.USER.getIndex()];
    long iowait =
        ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
            - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
    long idle =
        ticks[CentralProcessor.TickType.IDLE.getIndex()]
            - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
    long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

    double used = 0;
    if (totalCpu > 0) {
      used = 100d * (totalCpu - idle) / totalCpu;
    }

    return StudyJavaServerInfoVo.CpuInfo.builder()
        .cpuNum(processor.getLogicalProcessorCount())
        .used(Double.parseDouble(DF.format(used)))
        .free(Double.parseDouble(DF.format(100 - used)))
        .cpuModel(processor.getProcessorIdentifier().getName())
        .build();
  }

  /**
   * 获取内存信息
   *
   * @return 内存信息
   */
  private StudyJavaServerInfoVo.MemoryInfo getMemoryInfo() {
    GlobalMemory memory = hardware.getMemory();
    long total = memory.getTotal();
    long available = memory.getAvailable();
    long used = total - available;

    return StudyJavaServerInfoVo.MemoryInfo.builder()
        .total(Double.parseDouble(DF.format(convertBytesToGB(total))))
        .used(Double.parseDouble(DF.format(convertBytesToGB(used))))
        .free(Double.parseDouble(DF.format(convertBytesToGB(available))))
        .usage(Double.parseDouble(DF.format(100d * used / total)))
        .build();
  }

  /**
   * 获取 JVM 信息
   *
   * @return JVM 信息
   */
  private StudyJavaServerInfoVo.JvmInfo getJvmInfo() {
    Runtime runtime = Runtime.getRuntime();
    long total = runtime.totalMemory();
    long max = runtime.maxMemory();
    long free = runtime.freeMemory();
    long used = total - free;

    return StudyJavaServerInfoVo.JvmInfo.builder()
        .total(Double.parseDouble(DF.format(convertBytesToMB(total))))
        .max(Double.parseDouble(DF.format(convertBytesToMB(max))))
        .used(Double.parseDouble(DF.format(convertBytesToMB(used))))
        .free(Double.parseDouble(DF.format(convertBytesToMB(free))))
        .usage(Double.parseDouble(DF.format(100d * used / total)))
        .version(System.getProperty("java.version"))
        .startTime(getServerStartTime())
        .runTime(getServerRunTime())
        .build();
  }

  /**
   * 获取磁盘信息
   *
   * @return 磁盘信息
   */
  private StudyJavaServerInfoVo.DiskInfo getDiskInfo() {
    FileSystem fileSystem = os.getFileSystem();
    OSFileStore[] fsArray = fileSystem.getFileStores().toArray(new OSFileStore[0]);

    long total = 0;
    long free = 0;



    for (OSFileStore fs : fsArray) {
      String fsName = fs.getName();
      String fsType = fs.getType();
      long fsTotal = fs.getTotalSpace();
      long fsFree = fs.getUsableSpace();

      // 过滤掉虚拟文件系统和网络驱动器
      if (!shouldIncludeFileStore(fs)) {
        continue;
      }

      // 只统计有效的文件系统（容量大于 1GB）
      if (fsTotal <= 1024L * 1024L * 1024L) {
        continue;
      }

      total += fsTotal;
      free += fsFree;

    }

    long used = total - free;



    return StudyJavaServerInfoVo.DiskInfo.builder()
        .total(Double.parseDouble(DF.format(convertBytesToGB(total))))
        .used(Double.parseDouble(DF.format(convertBytesToGB(used))))
        .free(Double.parseDouble(DF.format(convertBytesToGB(free))))
        .usage(total > 0 ? Double.parseDouble(DF.format(100d * used / total)) : 0)
        .build();
  }

  /**
   * 判断是否应该统计该文件系统
   *
   * @param fs 文件系统
   * @return 是否统计
   */
  private boolean shouldIncludeFileStore(OSFileStore fs) {
    String type = fs.getType().toLowerCase();
    String name = fs.getName().toLowerCase();
    String mount = fs.getMount().toLowerCase();

    // macOS APFS: 排除系统辅助卷（它们共享同一个容器，会重复统计）
    if (type.contains("apfs")) {
      // 排除系统卷
      if (name.contains("preboot")
          || name.contains("vm")
          || name.contains("update")
          || name.contains("xart")
          || name.contains("iscpreboot")
          || name.contains("hardware")
          || name.contains("recovery")) {
        return false;
      }

      // 排除模拟器卷（iOS/tvOS/watchOS 模拟器）
      if (name.contains("simulator") || name.contains("ios") || name.contains("tvos")) {
        return false;
      }

      // macOS Catalina+ 系统卷和数据卷分离，只统计数据卷
      // 数据卷通常名称包含 "data" 或挂载在 /System/Volumes/Data
      if (mount.contains("/system/volumes/data")) {
        return true; // 明确是数据卷，统计
      }

      // 如果挂载在根目录，但名称不包含 "data"，可能是系统卷，跳过
      if (mount.equals("/") && !name.contains("data")) {
        return false;
      }

      // 如果不在根目录也不在数据卷挂载点，跳过
      if (!mount.equals("/") && !mount.contains("/system/volumes/data")) {
        return false;
      }
    }

    // macOS: 排除其他虚拟文件系统
    if (type.contains("devfs")
        || type.contains("autofs")
        || type.contains("mtmfs")
        || name.contains("com.apple.timemachine")
        || name.contains("/dev")
        || name.contains("/cores")) {
      return false;
    }

    // Windows: 排除网络驱动器和虚拟驱动器
    if (type.contains("network") || type.contains("cifs") || type.contains("smb")) {
      return false;
    }

    // Linux: 排除虚拟文件系统
    if (type.contains("tmpfs")
        || type.contains("devtmpfs")
        || type.contains("squashfs")
        || type.contains("overlay")
        || type.contains("aufs")) {
      return false;
    }

    return true;
  }

  /**
   * 获取系统信息
   *
   * @return 系统信息
   */
  private StudyJavaServerInfoVo.SysInfo getSysInfo() {
    String hostname = "unknown";
    String ip = "unknown";

    try {
      InetAddress addr = InetAddress.getLocalHost();
      hostname = addr.getHostName();
      ip = addr.getHostAddress();
    } catch (UnknownHostException e) {
      log.error("获取主机信息失败", e);
    }

    return StudyJavaServerInfoVo.SysInfo.builder()
        .os(os.getFamily() + " " + os.getVersionInfo().getVersion())
        .arch(os.getBitness() + "位")
        .hostname(hostname)
        .ip(ip)
        .build();
  }

  /**
   * 字节转 GB
   *
   * @param bytes 字节数
   * @return GB
   */
  private double convertBytesToGB(long bytes) {
    return bytes / 1024.0 / 1024.0 / 1024.0;
  }

  /**
   * 字节转 MB
   *
   * @param bytes 字节数
   * @return MB
   */
  private double convertBytesToMB(long bytes) {
    return bytes / 1024.0 / 1024.0;
  }

  /**
   * 获取服务器启动时间
   *
   * @return 启动时间
   */
  private String getServerStartTime() {
    long startTime = System.currentTimeMillis() - getServerRunTimeMillis();
    return LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(startTime), java.time.ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  /**
   * 获取服务器运行时长
   *
   * @return 运行时长
   */
  private String getServerRunTime() {
    long runTimeMillis = getServerRunTimeMillis();
    long days = TimeUnit.MILLISECONDS.toDays(runTimeMillis);
    long hours = TimeUnit.MILLISECONDS.toHours(runTimeMillis) % 24;
    long minutes = TimeUnit.MILLISECONDS.toMinutes(runTimeMillis) % 60;
    long seconds = TimeUnit.MILLISECONDS.toSeconds(runTimeMillis) % 60;

    return String.format("%d天%d小时%d分钟%d秒", days, hours, minutes, seconds);
  }

  /**
   * 获取服务器运行时长（毫秒）
   *
   * @return 运行时长（毫秒）
   */
  private long getServerRunTimeMillis() {
    return ManagementFactory.getRuntimeMXBean().getUptime();
  }

  /** 应用关闭时清理资源 */
  @PreDestroy
  public void destroy() {

    stopMonitoring();
  }
}
