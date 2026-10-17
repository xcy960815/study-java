package com.studyjava.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyjava.domain.dao.StudyJavaServerMetricDao;

/** 服务器监控历史采样 Mapper */
public interface StudyJavaServerMetricMapper extends BaseMapper<StudyJavaServerMetricDao> {

  /**
   * 批量插入采样记录（唯一键冲突时忽略，保证重复采样幂等）。
   *
   * @param records 待写入的采样记录
   * @return 实际写入条数
   */
  int insertIgnoreBatch(@Param("records") List<StudyJavaServerMetricDao> records);

  /**
   * 查询区间内的指标采样序列（按时间升序）。
   *
   * @param metricKey 指标键
   * @param start 起始时间（含）
   * @param end 结束时间（含）
   * @return 采样记录列表
   */
  List<StudyJavaServerMetricDao> selectHistory(
      @Param("metricKey") String metricKey,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  /**
   * 物理删除 cutoff 之前的采样记录（保留策略）。
   *
   * @param cutoff 截止时间（不含）
   * @return 删除条数
   */
  int deleteSampledBefore(@Param("cutoff") LocalDateTime cutoff);
}
