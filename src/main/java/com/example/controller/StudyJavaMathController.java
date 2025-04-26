package com.example.controller;

import com.example.domain.vo.StudyJavaQuestionVo;
import com.example.domain.vo.StudyJavaWrongQuestionVo;
import com.example.domain.vo.StudyJavaProgressVo;
import com.example.service.StudyJavaMathService;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/math")
public class StudyJavaMathController extends BaseController {

    @Autowired
    private StudyJavaMathService mathService;

    @GetMapping("/questions")
    public ResponseResult<List<StudyJavaQuestionVo>> getQuestions(@RequestParam int level) {
        return ResponseGenerator.generateSuccessResult(mathService.getQuestions(level));
    }

    @PostMapping("/submit")
    public ResponseResult<Boolean> submitAnswer(@RequestParam String questionId, @RequestParam int answer) {
        return ResponseGenerator.generateSuccessResult(mathService.submitAnswer(questionId, answer));
    }

    @GetMapping("/wrong-questions")
    public ResponseResult<List<StudyJavaWrongQuestionVo>> getWrongQuestions() {
        return ResponseGenerator.generateSuccessResult(mathService.getWrongQuestions());
    }

    @PostMapping("/progress")
    public ResponseResult<Boolean> updateProgress(@RequestBody StudyJavaProgressVo progress) {
        mathService.updateProgress(progress);
        return ResponseGenerator.generateSuccessResult(true);
    }
}
