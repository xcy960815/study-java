package com.studyjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyjava.domain.dao.StudyJavaSysOperLogDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志记录 Mapper 接口
 */
@Mapper
public interface StudyJavaSysOperLogMapper extends BaseMapper<StudyJavaSysOperLogDao>
{
}
