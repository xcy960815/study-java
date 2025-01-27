package com.example.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dto.StudyJavaGoodsCategoryDto;
import com.example.domain.vo.StudyJavaGoodsCategoryVo;
import com.example.service.StudyJavaGoodsCategoryService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/goodsCategory") // 前缀
public class StudyJavaGoodsCategoryController {

    @Resource
    private StudyJavaGoodsCategoryService studyJavaGoodsCategoryService;

    @GetMapping("/getGoodsCategoryList")
    public ResponseResult<Map<String,Object>> getGoodsCategoryList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute("studyJavaUser") StudyJavaGoodsCategoryVo studyJavaGoodsCategoryVo
    ) {
        Page<StudyJavaGoodsCategoryVo> page = new Page<>(pageNum, pageSize);
        IPage<StudyJavaGoodsCategoryDto> iPageResult =  studyJavaGoodsCategoryService.getGoodsCategoryList(page,studyJavaGoodsCategoryVo);
        // 返回分页数据和总条数
        Map<String,Object> iPageResultMap = new HashMap<>();
        iPageResultMap.put("data",iPageResult.getRecords());
        iPageResultMap.put("total",iPageResult.getTotal());
        return ResponseGenerator.generateSuccessResult(iPageResultMap);
    }
}
