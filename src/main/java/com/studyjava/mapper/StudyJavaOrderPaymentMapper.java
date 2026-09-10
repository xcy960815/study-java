package com.studyjava.mapper;

import org.apache.ibatis.annotations.Param;

import com.studyjava.domain.dao.StudyJavaOrderPaymentDao;

public interface StudyJavaOrderPaymentMapper {

  int insertIfAbsent(@Param("payment") StudyJavaOrderPaymentDao payment);

  StudyJavaOrderPaymentDao getByRequestIdForUpdate(@Param("requestId") String requestId);

  int markSuccess(
      @Param("requestId") String requestId, @Param("transactionNo") String transactionNo);

  /** 将流水标记为失败（回滚 PROCESSING 状态），允许同一 requestId 重试 */
  int markFailed(@Param("requestId") String requestId);

  /** 将失败流水重置为处理中（同 requestId 重试时调用） */
  int markProcessing(@Param("requestId") String requestId);
}
