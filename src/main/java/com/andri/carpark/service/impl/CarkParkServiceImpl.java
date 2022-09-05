package com.andri.carpark.service.impl;

import com.andri.carpark.dto.CarParkInformationDto;
import com.andri.carpark.model.CarParks;
import com.andri.carpark.repository.CarParkRepository;
import com.andri.carpark.service.CarParkService;
import com.andri.carpark.util.UnirestHttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CarkParkServiceImpl implements CarParkService {

	private final UnirestHttpClient unirestHttpClient;
	private final Environment environment;
	private final ObjectMapper objectMapper;
	private final CarParkRepository carParkRepository;
	private static final DecimalFormat df = new DecimalFormat("0.00");

	public CarkParkServiceImpl(UnirestHttpClient unirestHttpClient, Environment environment, ObjectMapper objectMapper, CarParkRepository carParkRepository) {
		this.unirestHttpClient = unirestHttpClient;
		this.environment = environment;
		this.objectMapper = objectMapper;
		this.carParkRepository = carParkRepository;
	}


	@Override
	public void fetchCarParkInformation() {
		log.info("Run fetching carParkInfo");
		try {
			String url = environment.getProperty("car.information.url");
			Map<String, Object> queryString = new HashMap<>();
			queryString.put("resource_id", "139a3035-e624-4f56-b63f-89ae28d4ae4c");
			queryString.put("limit", "10000");

			HttpResponse<JsonNode> jsonResponse = unirestHttpClient.get(url, null, queryString);
			String responseJSONString = jsonResponse.getBody().toString();
			JSONObject responeJson = new JSONObject(responseJSONString);
			responeJson = responeJson.getJSONObject("result");

			String recordDataString = responeJson.getJSONArray("records").toString();

			List<CarParkInformationDto> carParkInformationDtos = objectMapper.readValue(recordDataString, new TypeReference<>() {
			});

			carParkInformationDtos.forEach(this::InserOrUpdateCarParkInformation);

			log.info("Success Insert or update carParkInformation");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void InserOrUpdateCarParkInformation(CarParkInformationDto carParkInformationDto) {
		Map<String, Double> latLngData = SVY21ToLatLng(carParkInformationDto.x_coord, carParkInformationDto.y_coord);
		CarParks carPark;
		Optional<CarParks> carParkOpt = this.carParkRepository.findCarParksByCarPark(carParkInformationDto.car_park_no);
		if (carParkOpt.isPresent()) {
			carPark = carParkOpt.get();
			assert latLngData != null;
			carPark.setXcoord(latLngData.get("longitude"));
			carPark.setYcoord(latLngData.get("latitude"));
			carPark.setUpdatedAt(Instant.now());
		} else {
			carPark = new CarParks();
			carPark.setCarPark(carParkInformationDto.car_park_no);
			carPark.setXcoord(latLngData.get("longitude"));
			carPark.setYcoord(latLngData.get("latitude"));
			carPark.setAddress(carParkInformationDto.address);
			carPark.setCarParkDecks(Integer.parseInt(carParkInformationDto.car_park_decks));
			carPark.setCarParkType(carParkInformationDto.car_park_type);
			carPark.setTypeOfParkingSystem(carParkInformationDto.type_of_parking_system);
			carPark.setCarParkBasement(carParkInformationDto.car_park_basement);
			carPark.setFreeParking(carParkInformationDto.free_parking);
			carPark.setGantryHeight(carParkInformationDto.gantry_height);
			carPark.setShortTermParking(carParkInformationDto.short_term_parking);
			carPark.setNightParking(carParkInformationDto.night_parking);
		}
		this.carParkRepository.save(carPark);
	}


	private Map<String, Double> SVY21ToLatLng(String x_coord, String y_coord) {
		try {
			Map<String, Object> queryString = new HashMap<>();

			String urlConvert = environment.getProperty("coordinate.convert.url");
			queryString.put("Y", y_coord);
			queryString.put("X", x_coord);
			HttpResponse<JsonNode> jsonResponse = unirestHttpClient.get(urlConvert, null, queryString);

			String responseJSONString = jsonResponse.getBody().toString();
			Map<String, Double> responsedata = objectMapper.readValue(responseJSONString, Map.class);

			log.info("Response convert SVY21 = {}", responsedata);
			return responsedata;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
