package com.studyjava.mapper;

import org.apache.ibatis.annotations.Param;

import com.studyjava.domain.dao.StudyJavaOrderPaymentDao;

public interface StudyJavaOrderPaymentMapper {

  int insertIfAbsent(@Param("payment") StudyJavaOrderPaymentDao payment);

  StudyJavaOrderPaymentDao getByRequestIdForUpdate(@Param("requestId") String requestId);

  int markSuccess(
      @Param("requestId") String requestId, @Param("transactionNo") String transactionNo);
}
