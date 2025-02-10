package com.example.service.imp;

import lombok.extern.slf4j.Slf4j;
import com.example.service.StudyJavaOllamaService;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
//@Slf4j
public class StudyJavaOllamaServiceImp implements StudyJavaOllamaService {

    @Autowired
    @Qualifier("ollamaChatClient")
    private OllamaChatClient ollamaChatClient;


    public static void main(String[] args){
        StudyJavaOllamaService studyJavaOllamaService = new StudyJavaOllamaServiceImp();
        studyJavaOllamaService.conversation();
    }


    @Override
    public String conversation(){
      String answer = ollamaChatClient.call("你好");
//      log.info(answer);
        return null;
    }

}
