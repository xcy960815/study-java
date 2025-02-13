package com.example.controller;

import com.example.domain.dto.ollama.*;
import com.example.domain.vo.ollama.StudyJavaOllamaDeleteVo;
import com.example.domain.vo.ollama.StudyJavaOllamaGrenerateVo;
import com.example.service.StudyJavaOllamaService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/ollama")
public class StudyJavaOllamaController {

    @Resource
    private StudyJavaOllamaService studyJavaOllamaService;

    /**
     * 生成模型
     */
    @PostMapping("/generate")
    public ResponseResult<StudyJavaOllamaGenerateDto> generate(@Valid @RequestBody StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo) {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.generate(studyJavaOllamaGrenerateVo));
    }
//    @PostMapping("/generateStream")
//    public ResponseResult<InputStream> generateStream() {
//        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.generateStream());
//    }
    /**
     * 获取标签
     */
    @GetMapping("/tags")
    public ResponseResult<StudyJavaOllamaTagsDto> tags() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.tags());
    }

    /**
     * 获取所有的模型
     */
    @GetMapping("/models")
    public ResponseResult<StudyJavaOllamaModelsDto> models() {
       return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.models());
    }

    /**
     * 获取ollama版本
     * @return ResponseResult
     */
    @GetMapping("/version")
    public ResponseResult<StudyJavaOllamaVersionDto> version() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.version());
    }

    /**
     * 删除模型
     * @return ResponseResult
     */
    @DeleteMapping("/delete")
    public ResponseResult<Boolean> delete(@RequestBody StudyJavaOllamaDeleteVo studyJavaOllamaDeleteVo) {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.delete(studyJavaOllamaDeleteVo));
    }

    /**
    * 获取ps
//        * @return ResponseResult
    */
    @GetMapping("/ps")
    public ResponseResult<StudyJavaOllamaPsDto> ps() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.ps());
    }

    @PostMapping("/pull")
    public void pull() {
        studyJavaOllamaService.pull();
//        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.ps());
    }

    @PostMapping("/show")
    public ResponseResult<StudyJavaOllamaShowDto> show() {
       return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.show());
    }
}
