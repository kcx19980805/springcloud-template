package com.kcx.api.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//feign
@EnableFeignClients
//sentinel
@EnableDiscoveryClient
public class ApiUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiUserApplication.class, args);
    }

}
