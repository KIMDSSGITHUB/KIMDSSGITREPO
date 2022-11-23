package com.dss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class Dss4MsMovieV1Application {

	public static void main(String[] args) {
		SpringApplication.run(Dss4MsMovieV1Application.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate getTemplate() {
		return new RestTemplate();
	}
}
