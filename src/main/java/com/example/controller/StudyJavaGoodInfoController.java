package com.example.controller;


import com.example.domain.vo.StudyJavaGoodsInfoVo;
import com.example.service.StudyJavaGoodsInfoService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goodsCategory")
public class StudyJavaGoodInfoController {
    @Resource
    private StudyJavaGoodsInfoService studyJavaGoodsInfoService;

    @GetMapping("getGoodsCategoryDetail")
    @ResponseBody
    public ResponseResult getGoodsCategoryDetail(@RequestParam StudyJavaGoodsInfoVo studyJavaGoodsInfoVo){
      return  ResponseGenerator.generatSuccessResult(studyJavaGoodsInfoService.getStudyJavaGoodsInfoDetail(studyJavaGoodsInfoVo)) ;
    }
}
