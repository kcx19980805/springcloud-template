package com.kcx.task.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//feign
@EnableFeignClients
//sentinel
@EnableDiscoveryClient
public class TaskConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskConsumerApplication.class, args);
    }

}
