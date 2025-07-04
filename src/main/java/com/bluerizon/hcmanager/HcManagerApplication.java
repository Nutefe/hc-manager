package com.bluerizon.hcmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableAutoConfiguration
@EnableScheduling
public class HcManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(HcManagerApplication.class, args);
	}

}
