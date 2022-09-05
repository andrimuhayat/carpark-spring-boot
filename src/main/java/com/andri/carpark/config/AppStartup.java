package com.andri.carpark.config;

import com.andri.carpark.service.CarParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AppStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private CarParkService carParkService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		System.out.println("On startup");
		carParkService.fetchCarParkInformation();
		/* task execution */
	}
}

