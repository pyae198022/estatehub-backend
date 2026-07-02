package com.estatehub.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class EstatehubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstatehubBackendApplication.class, args);
	}

}
