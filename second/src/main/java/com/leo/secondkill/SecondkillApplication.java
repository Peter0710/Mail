package com.leo.secondkill;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liu
 */
@EnableRabbit
@EnableScheduling
@EnableFeignClients
@EnableSwagger2
@EnableDiscoveryClient
@SpringBootApplication
public class SecondkillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondkillApplication.class, args);
    }

}
