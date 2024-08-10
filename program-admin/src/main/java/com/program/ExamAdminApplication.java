package com.program;

import com.program.utils.EmptyUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableSwagger2
//@ComponentScan(basePackages = {"com.program"})
@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
public class ExamAdminApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ExamAdminApplication.class, args);
        ConfigurableEnvironment env = run.getEnvironment();
        // 获取端口号
        String port = env.getProperty("server.port");
        // 获取上下文路径
        String path = env.getProperty("server.servlet.context-path");
        if (EmptyUtil.isEmpty(path)) {
            path = "";
        } else if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        // 获取ip地址
        String ipAddress = "";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        if (EmptyUtil.isEmpty(ipAddress)) {
            ipAddress = "127.0.0.1";
        }
        System.out.println("========编程学习系统★启动完成=========================================== \n" +
                "　　　　　　　　　▍ ★∴ IpAddress: http://"+ipAddress+":" + port + path + "/\n" +
                "　　　．．．．▍▍．..．█▍ ☆ ★∵ ..../ \n" +
                "　　 　◥█▅▅██▅▅██▅▅▅▅▅▅▅▅▅███◤ \n" +
                "　　 　．◥███████████████◤ \n" +
                "　 ～～～～◥█████████████◤～～～～ ");
    }
}