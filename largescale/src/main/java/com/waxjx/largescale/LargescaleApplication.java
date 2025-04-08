package com.waxjx.largescale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
// 开启事务管理
@EnableTransactionManagement
public class LargescaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LargescaleApplication.class, args);
    }

}
