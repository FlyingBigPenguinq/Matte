package com.syl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Liu XiangLiang
 * @description: ${todo}
 * @date ${DATE} ${TIME}
 */
@EnableAsync
@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }
}