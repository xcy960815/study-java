package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dto.StudyJavaDataDictionaryDto;
import com.example.domain.vo.StudyJavaDataDictionaryVo;
import com.example.service.StudyJavaDataDictionaryService;
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
public class StudyJavaDataDictionaryController extends BaseController {

    @Resource
    private StudyJavaDataDictionaryService studyJavaDataDictionaryService;

    @GetMapping("/list")
    public ResponseListResult<StudyJavaDataDictionaryVo> list(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute StudyJavaDataDictionaryDto studyJavaDataDictionaryDto
    ) {
        IPage<StudyJavaDataDictionaryVo> dataDictionaryPage = studyJavaDataDictionaryService.dataDictionaryList(startPage(pageNum, pageSize), studyJavaDataDictionaryDto);
        return ResponseGenerator.generateListResult(dataDictionaryPage.getRecords(),dataDictionaryPage.getTotal());
    }

    @PostMapping("/add")
    public ResponseResult<Boolean> add(@Valid @RequestBody StudyJavaDataDictionaryDto studyJavaDataDictionaryDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaDataDictionaryService.addDataDictionary(studyJavaDataDictionaryDto));
    }

    @PutMapping("/update")
    public ResponseResult<Boolean> update(@Valid @RequestBody StudyJavaDataDictionaryDto studyJavaDataDictionaryDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaDataDictionaryService.updateDataDictionary(studyJavaDataDictionaryDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseGenerator.generateSuccessResult(studyJavaDataDictionaryService.deleteDataDictionary(id)) ;
    }

    @GetMapping("/detail/{id}")
    public ResponseResult<StudyJavaDataDictionaryVo> detail(@PathVariable("id") Long id) {
       return ResponseGenerator.generateSuccessResult(studyJavaDataDictionaryService.dataDictionaryDetail(id)) ;
    }
}
