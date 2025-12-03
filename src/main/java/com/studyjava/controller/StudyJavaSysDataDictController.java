package com.studyjava.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dto.StudyJavaSysDataDictDto;
import com.studyjava.domain.vo.StudyJavaSysDataDictVo;
import com.studyjava.service.StudyJavaSysDataDictService;
import com.studyjava.utils.ResponseListResult;
import com.studyjava.utils.ResponseResult;
import com.studyjava.utils.ResponseGenerator;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dataDict")
@Slf4j
public class StudyJavaSysDataDictController extends BaseController {

    @Resource
    private StudyJavaSysDataDictService studyJavaSysDataDictService;

    @GetMapping("/getDataDictList")
    public ResponseListResult<StudyJavaSysDataDictVo> getDataDictList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute StudyJavaSysDataDictDto studyJavaSysDataDictDto
    ) {
        IPage<StudyJavaSysDataDictVo> dataDictionaryPage = studyJavaSysDataDictService.getDataDictList(startPage(pageNum, pageSize), studyJavaSysDataDictDto);
        return ResponseGenerator.generateListResult(dataDictionaryPage.getRecords(),dataDictionaryPage.getTotal());
    }

    @PostMapping("/insertDataDict")
    public ResponseResult<Boolean> add(@Valid @RequestBody StudyJavaSysDataDictDto studyJavaSysDataDictDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysDataDictService.insertDataDict(studyJavaSysDataDictDto));
    }

    @PutMapping("/updateDataDict")
    public ResponseResult<Boolean> update(@Valid @RequestBody StudyJavaSysDataDictDto studyJavaSysDataDictDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysDataDictService.updateDataDict(studyJavaSysDataDictDto));
    }

    @DeleteMapping("/deleteDataDict/{id}")
    public ResponseResult<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysDataDictService.deleteDataDict(id)) ;
    }

    @GetMapping("/getDataDictDetail/{id}")
    public ResponseResult<StudyJavaSysDataDictVo> detail(@PathVariable("id") Long id) {
       return ResponseGenerator.generateSuccessResult(studyJavaSysDataDictService.getDataDictDetail(id)) ;
    }
}
