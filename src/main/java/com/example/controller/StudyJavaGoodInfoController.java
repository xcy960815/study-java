package com.example.controller;


import com.example.domain.dto.StudyJavaGoodsInfoDto;
import com.example.domain.vo.StudyJavaGoodsInfoVo;
import com.example.service.StudyJavaGoodsInfoService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goodsCategory")
public class StudyJavaGoodInfoController {

    @Resource
    private StudyJavaGoodsInfoService studyJavaGoodsInfoService;

    @GetMapping("/getGoodsCategoryDetail")
    public ResponseResult<StudyJavaGoodsInfoVo> getGoodsCategoryDetail(@Valid StudyJavaGoodsInfoDto studyJavaGoodsInfoDto){
      return ResponseGenerator.generateSuccessResult(studyJavaGoodsInfoService.getStudyJavaGoodsInfoDetail(studyJavaGoodsInfoDto));
    }
}
