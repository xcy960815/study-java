
package com.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StudyJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudyJavaApplication.class, args);
    }

    public void getServerPort() {
        ApplicationContext context = SpringApplication.run(StudyJavaApplication.class, null);
        ServletWebServerApplicationContext servletWebServerApplicationContext = (ServletWebServerApplicationContext) context;
        int port = servletWebServerApplicationContext.getWebServer().getPort();
        System.out.println("服务器端口号：" + port);
    }
}
