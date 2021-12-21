package com.iocl.integratedunblockingboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.iocl.integratedunblockingboot.service.UnblockService;

@SpringBootApplication
public class IntegratedunblockingbootApplication implements CommandLineRunner{

	@Autowired
	UnblockService unblockService;
	
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(IntegratedunblockingbootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		unblockService.unblockCustomers();

	}

}
