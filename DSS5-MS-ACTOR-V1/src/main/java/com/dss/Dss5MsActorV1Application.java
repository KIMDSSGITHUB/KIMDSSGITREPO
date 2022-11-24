package com.dss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Dss5MsActorV1Application {

	public static void main(String[] args) {
		SpringApplication.run(Dss5MsActorV1Application.class, args);
	}

}
