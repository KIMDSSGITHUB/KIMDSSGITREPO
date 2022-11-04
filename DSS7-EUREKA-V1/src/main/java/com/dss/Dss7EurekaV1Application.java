package com.dss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Dss7EurekaV1Application {

	public static void main(String[] args) {
		SpringApplication.run(Dss7EurekaV1Application.class, args);
	}

}
