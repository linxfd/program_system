package com.program;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
//@ComponentScan(basePackages = {"com.program"})
@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
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