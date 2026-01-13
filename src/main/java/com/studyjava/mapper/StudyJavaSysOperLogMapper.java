package com.studyjava.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyjava.domain.dao.StudyJavaSysOperLogDao;

/** 操作日志记录 Mapper 接口 */
@Mapper
public interface StudyJavaSysOperLogMapper extends BaseMapper<StudyJavaSysOperLogDao> {}
