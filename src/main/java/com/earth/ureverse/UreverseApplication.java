package com.earth.ureverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class UreverseApplication {

	public static void main(String[] args) {
		SpringApplication.run(UreverseApplication.class, args);
	}

}
