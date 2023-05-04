package com.kcx.service.middleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//feign
@EnableFeignClients
//sentinel
@EnableDiscoveryClient
public class ServiceMiddlewareApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMiddlewareApplication.class, args);
    }

}
