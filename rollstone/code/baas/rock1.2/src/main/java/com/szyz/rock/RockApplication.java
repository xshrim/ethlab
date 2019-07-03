package com.szyz.rock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(exclude =ErrorMvcAutoConfiguration.class)
@MapperScan("com.szyz.rock.mapper")
@ServletComponentScan
public class RockApplication {

    public static void main(String[] args) {
        SpringApplication.run(RockApplication.class, args);
    }
}
