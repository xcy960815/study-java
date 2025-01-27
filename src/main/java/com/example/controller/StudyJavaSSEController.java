package com.example.controller;




import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/sse")
public class StudyJavaSSEController {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @GetMapping(value = "/getSystemTime")
    public SseEmitter streamSseEvents() {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        executor.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                    sseEmitter.send(System.currentTimeMillis());
                }
                sseEmitter.complete();
            } catch (IOException | InterruptedException e) {
                sseEmitter.completeWithError(e);
            }
        });
        return sseEmitter;
    }
}
