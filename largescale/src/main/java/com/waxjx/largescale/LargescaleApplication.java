package com.waxjx.largescale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// 开启springboot 并且 关闭自动配置的数据源
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// 开启事务管理
@EnableTransactionManagement
public class LargescaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LargescaleApplication.class, args);
    }

}
