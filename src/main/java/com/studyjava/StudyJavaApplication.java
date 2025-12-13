
package com.studyjava;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@MapperScan("com.studyjava.mapper") // 扫描mapper
@SpringBootApplication(scanBasePackages = "com.studyjava")
@EnableScheduling
@Slf4j
public class StudyJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudyJavaApplication.class, args);
    }
}
