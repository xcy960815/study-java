package com.studyjava.controller;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dto.StudyJavaSysDataDictDto;
import com.studyjava.domain.vo.StudyJavaSysDataDictVo;
import com.studyjava.service.StudyJavaSysDataDictService;
import com.studyjava.utils.PageResult;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/dataDict")
@Slf4j
public class StudyJavaSysDataDictController extends BaseController {

  @Resource private StudyJavaSysDataDictService studyJavaSysDataDictService;

  @GetMapping("/getDataDictList")
  public PageResult<StudyJavaSysDataDictVo> getDataDictList(
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
      @ModelAttribute StudyJavaSysDataDictDto studyJavaSysDataDictDto) {
    IPage<StudyJavaSysDataDictVo> dataDictionaryPage =
        studyJavaSysDataDictService.getDataDictList(
            startPage(pageNum, pageSize), studyJavaSysDataDictDto);
    return PageResult.of(dataDictionaryPage.getRecords(), dataDictionaryPage.getTotal());
  }

  @PostMapping("/insertDataDict")
  public Boolean add(@Valid @RequestBody StudyJavaSysDataDictDto studyJavaSysDataDictDto) {
    return studyJavaSysDataDictService.insertDataDict(studyJavaSysDataDictDto);
  }

  @PutMapping("/updateDataDict")
  public Boolean update(@Valid @RequestBody StudyJavaSysDataDictDto studyJavaSysDataDictDto) {
    return studyJavaSysDataDictService.updateDataDict(studyJavaSysDataDictDto);
  }

  @DeleteMapping("/deleteDataDict/{id}")
  public Boolean delete(@PathVariable("id") Long id) {
    return studyJavaSysDataDictService.deleteDataDict(id);
  }

  @GetMapping("/getDataDictDetail/{id}")
  public StudyJavaSysDataDictVo detail(@PathVariable("id") Long id) {
    return studyJavaSysDataDictService.getDataDictDetail(id);
  }
}
