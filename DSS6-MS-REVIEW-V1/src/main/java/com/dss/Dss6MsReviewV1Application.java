package com.dss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Dss6MsReviewV1Application {

	public static void main(String[] args) {
		SpringApplication.run(Dss6MsReviewV1Application.class, args);
	}

}
