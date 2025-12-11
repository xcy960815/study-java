package com.studyjava.controller.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.annotation.Log;
import com.studyjava.controller.BaseController;
import com.studyjava.domain.dao.StudyJavaSysOperLogDao;
import com.studyjava.domain.enums.BusinessType;
import com.studyjava.service.StudyJavaSysOperLogService;
import com.studyjava.utils.PageResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 操作日志记录
 */
@Slf4j
@RestController
@RequestMapping("/monitor/operlog")
public class StudyJavaSysOperLogController extends BaseController
{
    @Resource
    private StudyJavaSysOperLogService studyJavaSysOperLogService;

    @Log(title = "操作日志", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public PageResult<StudyJavaSysOperLogDao> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     StudyJavaSysOperLogDao operLog)
    {

        Page<StudyJavaSysOperLogDao> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<StudyJavaSysOperLogDao> queryWrapper = new LambdaQueryWrapper<>();
        if (operLog.getTitle() != null && !operLog.getTitle().isEmpty()) {
            queryWrapper.like(StudyJavaSysOperLogDao::getTitle, operLog.getTitle());
        }
        if (operLog.getOperName() != null && !operLog.getOperName().isEmpty()) {
            queryWrapper.like(StudyJavaSysOperLogDao::getOperName, operLog.getOperName());
        }
        if (operLog.getBusinessType() != null) {
            queryWrapper.eq(StudyJavaSysOperLogDao::getBusinessType, operLog.getBusinessType());
        }
        if (operLog.getStatus() != null) {
            queryWrapper.eq(StudyJavaSysOperLogDao::getStatus, operLog.getStatus());
        }
        queryWrapper.orderByDesc(StudyJavaSysOperLogDao::getOperTime);
        
        studyJavaSysOperLogService.page(page, queryWrapper);

        return PageResult.of(page.getRecords(), page.getTotal());
    }

    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{operIds}")
    public Boolean remove(@PathVariable Long[] operIds)
    {
        return studyJavaSysOperLogService.removeBatchByIds(Arrays.asList(operIds));
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public Boolean clean()
    {
        studyJavaSysOperLogService.cleanOperLog();
        return true;
    }
}
