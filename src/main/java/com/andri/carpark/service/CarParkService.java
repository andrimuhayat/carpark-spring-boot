package com.andri.carpark.service;

import com.andri.carpark.dto.CarParkDto;
import com.andri.carpark.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface CarParkService {
	void fetchCarParkInformation();
	void fetchCarParkAvailibilty();

	PagedResponse<CarParkDto> getCarParks(Pageable pageable, double latitude, double longitude);

}
