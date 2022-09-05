package com.andri.carpark.controller;


import com.andri.carpark.dto.CarParkDto;
import com.andri.carpark.dto.PagedResponse;
import com.andri.carpark.service.CarParkService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/carparks")
public class CarParkController {

	private final CarParkService CarParkService;

	public CarParkController(CarParkService CarParkService) {
		this.CarParkService = CarParkService;
	}

	@GetMapping("/nearest")
	public ResponseEntity<?> getParksList(@RequestParam(value = "latitude") double latitude,
	                                      @RequestParam(value = "longitude") double longitude,
	                                      @RequestParam(value = "page", defaultValue = "1") int page,
	                                      @RequestParam(value = "per_page", defaultValue = "10") int perPage) {


		if (latitude == 0 || longitude == 0) {
			return ResponseEntity.status(400).body("Latitude and longitude is required");
		}

//		page = (page > 0) ? (page - 1) * perPage : 0;
//		System.out.println("page : " + perPage);
		Pageable pageable = PageRequest.of(page, perPage);
		PagedResponse<CarParkDto> carParkResult = CarParkService.getCarParks(pageable, latitude, longitude);
		return ResponseEntity.ok(carParkResult);

	}
}
