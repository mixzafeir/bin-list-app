package com.interview.etravli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableFeignClients
public class EtravliApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtravliApplication.class, args);
	}

}
