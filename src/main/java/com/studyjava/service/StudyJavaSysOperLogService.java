package com.studyjava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.studyjava.domain.dao.StudyJavaSysOperLogDao;

/** 操作日志记录 服务类 */
public interface StudyJavaSysOperLogService extends IService<StudyJavaSysOperLogDao> {
  /** 清空操作日志 */
  public void cleanOperLog();
}
