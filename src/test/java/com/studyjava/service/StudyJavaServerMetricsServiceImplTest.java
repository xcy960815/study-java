package com.studyjava.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.studyjava.domain.dao.StudyJavaServerMetricDao;
import com.studyjava.domain.vo.StudyJavaServerInfoVo;
import com.studyjava.domain.vo.StudyJavaServerMetricsHistoryVo;
import com.studyjava.mapper.StudyJavaServerMetricMapper;
import com.studyjava.service.impl.StudyJavaServerMetricsServiceImpl;

/** {@link StudyJavaServerMetricsServiceImpl} 单元测试 */
@ExtendWith(MockitoExtension.class)
class StudyJavaServerMetricsServiceImplTest {

  @Mock private StudyJavaServerMonitorService serverMonitorService;

  @Mock private StudyJavaServerMetricMapper serverMetricMapper;

  @InjectMocks private StudyJavaServerMetricsServiceImpl service;

  /** 构造四项指标齐备的服务器信息快照 stub */
  private void givenServerInfoSnapshot() {
    StudyJavaServerInfoVo info =
        StudyJavaServerInfoVo.builder()
            .cpu(
                StudyJavaServerInfoVo.CpuInfo.builder()
                    .cpuNum(8)
                    .used(37.5)
                    .cpuModel("Apple M1")
                    .build())
            .memory(
                StudyJavaServerInfoVo.MemoryInfo.builder()
                    .total(16.0)
                    .used(8.0)
                    .free(8.0)
                    .usage(50.0)
                    .build())
            .jvm(
                StudyJavaServerInfoVo.JvmInfo.builder()
                    .total(512.0)
                    .max(1024.0)
                    .used(256.0)
                    .free(256.0)
                    .usage(50.0)
                    .version("21")
                    .build())
            .disk(
                StudyJavaServerInfoVo.DiskInfo.builder()
                    .total(500.0)
                    .used(100.0)
                    .free(400.0)
                    .usage(20.0)
                    .build())
            .sys(
                StudyJavaServerInfoVo.SysInfo.builder()
                    .os("macOS 15")
                    .arch("64位")
                    .hostname("local")
                    .ip("127.0.0.1")
                    .build())
            .build();
    when(serverMonitorService.getServerInfo()).thenReturn(info);
  }

  /** 采样一次应把四个百分比指标按统一采样时间批量写入 */
  @Test
  void sampleOnceInsertsAllMetricsWithSharedTimestamp() {
    givenServerInfoSnapshot();

    service.sampleOnce();

    verify(serverMetricMapper)
        .insertIgnoreBatch(
            argThat(
                records -> {
                  if (records.size() != 4) {
                    return false;
                  }
                  LocalDateTime sampledAt = records.get(0).getSampledAt();
                  List<String> keys =
                      records.stream().map(StudyJavaServerMetricDao::getMetricKey).toList();
                  List<BigDecimal> values =
                      records.stream().map(StudyJavaServerMetricDao::getMetricValue).toList();
                  return keys.equals(
                          List.of("cpu_usage", "memory_usage", "jvm_usage", "disk_usage"))
                      && values.equals(
                          List.of(
                              new BigDecimal("37.50"),
                              new BigDecimal("50.00"),
                              new BigDecimal("50.00"),
                              new BigDecimal("20.00")))
                      && records.stream()
                          .allMatch(
                              record ->
                                  sampledAt != null && sampledAt.equals(record.getSampledAt()));
                }));
  }

  /** 快照不可用时应跳过本轮采样，不触发写入 */
  @Test
  void sampleOnceSkipsWhenServerInfoUnavailable() {
    when(serverMonitorService.getServerInfo()).thenReturn(null);

    service.sampleOnce();

    verify(serverMetricMapper, never()).insertIgnoreBatch(argThat(records -> records != null));
  }

  /** getHistory 应返回带 label/unit 的升序趋势点时间戳（秒） */
  @Test
  void getHistoryReturnsPointsWithLabelsAndTimestamps() {
    LocalDateTime sampledAt = LocalDateTime.of(2026, 10, 17, 12, 0, 0);
    when(serverMetricMapper.selectHistory(
            org.mockito.ArgumentMatchers.eq("cpu_usage"),
            org.mockito.ArgumentMatchers.any(LocalDateTime.class),
            org.mockito.ArgumentMatchers.any(LocalDateTime.class)))
        .thenReturn(
            List.of(
                StudyJavaServerMetricDao.builder()
                    .id(1L)
                    .metricKey("cpu_usage")
                    .metricValue(new BigDecimal("37.50"))
                    .sampledAt(sampledAt)
                    .build()));

    StudyJavaServerMetricsHistoryVo history = service.getHistory("cpu_usage", 0, 2_000_000_000L);

    assertEquals("cpu_usage", history.getMetricKey());
    assertEquals("CPU 使用率", history.getLabel());
    assertEquals("%", history.getUnit());
    assertEquals(1, history.getPoints().size());
    assertEquals(
        sampledAt.atZone(java.time.ZoneId.systemDefault()).toEpochSecond(),
        history.getPoints().get(0).getTimestamp());
    assertEquals(37.5, history.getPoints().get(0).getValue());
  }

  /** 未知指标键应抛出 IllegalArgumentException */
  @Test
  void getHistoryRejectsUnknownMetricKey() {
    assertThrows(
        IllegalArgumentException.class, () -> service.getHistory("unknown_metric", 0, 100));
  }

  /** purgeBefore 应透传清理截止时间 */
  @Test
  void purgeBeforeDelegatesToMapper() {
    LocalDateTime cutoff = LocalDateTime.now().minusDays(35);
    when(serverMetricMapper.deleteSampledBefore(cutoff)).thenReturn(12);

    assertEquals(12, service.purgeBefore(cutoff));
    verify(serverMetricMapper).deleteSampledBefore(cutoff);
  }
}
