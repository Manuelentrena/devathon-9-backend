package com.devathon.griffindor_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = "com.devathon.griffindor_backend")
public class GriffindorBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GriffindorBackendApplication.class, args);
	}

}
