package com.lynn.emergence;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lynn.emergence.mapper")
public class EmergenceEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmergenceEventApplication.class, args);
    }

}

