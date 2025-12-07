package com.studyjava.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyjava.domain.dao.StudyJavaSysOperLogDao;
import com.studyjava.mapper.StudyJavaSysOperLogMapper;
import com.studyjava.service.StudyJavaSysOperLogService;
import org.springframework.stereotype.Service;

/**
 * 操作日志记录 服务实现类
 */
@Service
public class StudyJavaSysOperLogServiceImpl extends ServiceImpl<StudyJavaSysOperLogMapper, StudyJavaSysOperLogDao> implements StudyJavaSysOperLogService
{
    @Override
    public void cleanOperLog()
    {
        baseMapper.delete(null);
    }
}
