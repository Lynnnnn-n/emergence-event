package com.lynn.emergence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EmergenceEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmergenceEventApplication.class, args);
    }

}
