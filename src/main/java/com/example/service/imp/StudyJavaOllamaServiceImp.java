package com.example.service.imp;

import lombok.extern.slf4j.Slf4j;
import com.example.service.StudyJavaOllamaService;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class StudyJavaOllamaServiceImp implements StudyJavaOllamaService {


    public static void main(String[] args){
        StudyJavaOllamaService studyJavaOllamaService = new StudyJavaOllamaServiceImp();
        studyJavaOllamaService.conversation();
    }


    @Override
    public String conversation(){
        return null;
    }

}
