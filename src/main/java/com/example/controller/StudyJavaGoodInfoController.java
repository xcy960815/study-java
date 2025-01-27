package com.example.controller;


import com.example.domain.dto.StudyJavaGoodsInfoDto;
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

    @GetMapping("/getGoodsCategoryDetail")
    @ResponseBody
    public ResponseResult<StudyJavaGoodsInfoDto> getGoodsCategoryDetail(@RequestParam StudyJavaGoodsInfoVo studyJavaGoodsInfoVo){
      return  ResponseGenerator.generateSuccessResult(studyJavaGoodsInfoService.getStudyJavaGoodsInfoDetail(studyJavaGoodsInfoVo)) ;
    }
}
