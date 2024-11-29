package com.example.peep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ConfigurationProperties
@SpringBootApplication
@EnableJpaAuditing
public class PeepApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeepApplication.class, args);
	}

}
