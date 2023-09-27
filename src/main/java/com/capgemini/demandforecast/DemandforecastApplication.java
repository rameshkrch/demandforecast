package com.capgemini.demandforecast;

import com.capgemini.demandforecast.messaging.EmailTransformer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemandforecastApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemandforecastApplication.class, args);
	}

	@Bean
	public EmailTransformer emailTransformer() {
		return new EmailTransformer();
	}

}
