package com.program;

import cn.hutool.core.text.csv.CsvUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class ExamAdminApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ExamAdminApplication.class, args);
        System.out.println("=======编程学习系统★启动完成=========================================== \n" +
                "　　　　　　　　　▍ ★∴ \n" +
                "　　　．．．．▍▍．..．█▍ ☆ ★∵ ..../ \n" +
                "　　 　◥█▅▅██▅▅██▅▅▅▅▅▅▅▅▅███◤ \n" +
                "　　 　．◥███████████████◤ \n" +
                "　 ～～～～◥█████████████◤～～～～ ");
    }
}