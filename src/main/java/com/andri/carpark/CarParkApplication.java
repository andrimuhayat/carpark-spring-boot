package com.andri.carpark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarParkApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarParkApplication.class, args);
	}

}
