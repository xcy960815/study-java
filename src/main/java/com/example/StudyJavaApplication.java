
package com.example;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.example.mapper") // 扫描mapper
@SpringBootApplication(scanBasePackages = "com.example")
@Slf4j
public class StudyJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudyJavaApplication.class, args);
    }
    // 获取服务端端口
//    public void getServerPort() {
//        ApplicationContext context = SpringApplication.run(StudyJavaApplication.class);
//        ServletWebServerApplicationContext servletWebServerApplicationContext = (ServletWebServerApplicationContext) context;
//        int port = servletWebServerApplicationContext.getWebServer().getPort();
//        log.info("(♥◠‿◠)ﾉﾞ  项目启动成功   ლ(´ڡ`ლ)ﾞ  \n" );
//        log.info("服务器端口号：" + port);
//    }
}
