package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dto.StudyJavaSysDataDictDto;
import com.example.domain.vo.StudyJavaSysDataDictVo;
import com.example.service.StudyJavaSysDataDictService;
import com.example.utils.ResponseListResult;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dataDictionary")
@Slf4j
public class StudyJavaSysDataDictController extends BaseController {

    @Resource
    private StudyJavaSysDataDictService studyJavaSysDataDictService;

    @GetMapping("/list")
    public ResponseListResult<StudyJavaSysDataDictVo> list(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute StudyJavaSysDataDictDto studyJavaSysDataDictDto
    ) {
        IPage<StudyJavaSysDataDictVo> dataDictionaryPage = studyJavaSysDataDictService.dataDictionaryList(startPage(pageNum, pageSize), studyJavaSysDataDictDto);
        return ResponseGenerator.generateListResult(dataDictionaryPage.getRecords(),dataDictionaryPage.getTotal());
    }

    @PostMapping("/add")
    public ResponseResult<Boolean> add(@Valid @RequestBody StudyJavaSysDataDictDto studyJavaSysDataDictDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysDataDictService.addDataDictionary(studyJavaSysDataDictDto));
    }

    @PutMapping("/update")
    public ResponseResult<Boolean> update(@Valid @RequestBody StudyJavaSysDataDictDto studyJavaSysDataDictDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysDataDictService.updateDataDictionary(studyJavaSysDataDictDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseGenerator.generateSuccessResult(studyJavaSysDataDictService.deleteDataDictionary(id)) ;
    }

    @GetMapping("/detail/{id}")
    public ResponseResult<StudyJavaSysDataDictVo> detail(@PathVariable("id") Long id) {
       return ResponseGenerator.generateSuccessResult(studyJavaSysDataDictService.dataDictionaryDetail(id)) ;
    }
}
