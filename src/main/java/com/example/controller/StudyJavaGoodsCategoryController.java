package com.example.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dto.StudyJavaGoodsCategoryDto;
import com.example.domain.vo.StudyJavaGoodsCategoryVo;
import com.example.service.StudyJavaGoodsCategoryService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/goodsCategory") // 前缀
public class StudyJavaGoodsCategoryController extends BaseController {

    @Resource
    private StudyJavaGoodsCategoryService studyJavaGoodsCategoryService;

    @GetMapping("/getGoodsCategoryList")
    public ResponseResult<Map<String,Object>> getGoodsCategoryList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute("studyJavaUser") StudyJavaGoodsCategoryVo studyJavaGoodsCategoryVo
    ) {
        IPage<StudyJavaGoodsCategoryDto> goodsCategory =  studyJavaGoodsCategoryService.getGoodsCategoryList(startPage(pageNum, pageSize),studyJavaGoodsCategoryVo);
        return ResponseGenerator.generateSuccessResult(getPageData(goodsCategory));
    }
}
