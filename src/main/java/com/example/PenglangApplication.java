package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.example.dao")
public class PenglangApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PenglangApplication.class, args);
    }
}
