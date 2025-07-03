package com.houtu.mp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.houtu.mp.module.sys.dao")
public class MPApplication {

    public static void main(String[] args) {
        SpringApplication.run(MPApplication.class, args);
    }

}
