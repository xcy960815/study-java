
package com.example;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;



@MapperScan("com.example.mapper") // 扫描mapper
@SpringBootApplication(scanBasePackages = "com.example")
public class StudyJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudyJavaApplication.class, args);
    }

//    获取服务端端口
    public void getServerPort() {
        ApplicationContext context = SpringApplication.run(StudyJavaApplication.class, null);
        ServletWebServerApplicationContext servletWebServerApplicationContext = (ServletWebServerApplicationContext) context;
        int port = servletWebServerApplicationContext.getWebServer().getPort();
        System.out.println("服务器端口号：" + port);
    }
}
