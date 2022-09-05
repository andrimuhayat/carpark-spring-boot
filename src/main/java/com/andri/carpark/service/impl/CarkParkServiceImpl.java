package com.andri.carpark.service.impl;

import com.andri.carpark.dto.*;
import com.andri.carpark.model.CarParks;
import com.andri.carpark.repository.CarParkRepository;
import com.andri.carpark.service.CarParkService;
import com.andri.carpark.util.UnirestHttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarkParkServiceImpl implements CarParkService {

	private final UnirestHttpClient unirestHttpClient;
	private final Environment environment;
	private final ObjectMapper objectMapper;
	private final RedisTemplate<String, Object> redisTemplate;
	private final CarParkRepository carParkRepository;
	private static final DecimalFormat df = new DecimalFormat("0.00");

	public CarkParkServiceImpl(UnirestHttpClient unirestHttpClient, Environment environment, ObjectMapper objectMapper, RedisTemplate<String, Object> redisTemplate, CarParkRepository carParkRepository) {
		this.unirestHttpClient = unirestHttpClient;
		this.environment = environment;
		this.objectMapper = objectMapper;
		this.redisTemplate = redisTemplate;
		this.carParkRepository = carParkRepository;
	}

	@Override
	public PagedResponse<CarParkDto> getCarParks(Pageable pageable, double latitude, double longitude) {
		String keyRedis = "car_parks_page=" + pageable.getPageSize() + "&per_page=" + pageable.getPageNumber();

		PagedResponse<CarParkDto> carParkResponse;
		Object carParkObj = redisTemplate.opsForHash().get(keyRedis, keyRedis);
		carParkResponse = objectMapper.convertValue(carParkObj, new TypeReference<>() {
		});

		if (carParkObj == null) {
			Page<Map<String, Object>> carParksPage = carParkRepository.getCarParks(pageable, latitude, longitude);
			List<CarParkDto> carParkList = carParksPage.stream().map(carPark -> {
				CarParkDto carParkDto = new CarParkDto();
				carParkDto.setAddress((String) carPark.get("address"));
				carParkDto.setTotal_lots((Integer) carPark.get("total_lots"));
				carParkDto.setAvailable_lots((Integer) carPark.get("available_lots"));
				carParkDto.setLatitude((Double) carPark.get("y_coord"));
				carParkDto.setLongitude((Double) carPark.get("x_coord"));
				carParkDto.setDistance_in_km(Double.parseDouble(df.format(carPark.get("distance_in_km"))));
				return carParkDto;
			}).collect(Collectors.toList());

			carParkResponse = new PagedResponse<>(carParkList, carParksPage.getNumber(),
					carParksPage.getSize(), carParksPage.getTotalElements(), carParksPage.getTotalPages(), carParksPage.isLast());

			redisTemplate.opsForHash().put(keyRedis, keyRedis, carParkResponse);
			redisTemplate.expire(keyRedis, 60, TimeUnit.SECONDS);
		}
		return carParkResponse;
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

	@Override
	@Scheduled(cron = "0 */2 * ? * *")
	public void fetchCarParkAvailibilty() {
		log.info("Run fetching carParkAvailability");
		String url = environment.getProperty("carpark.availability");
		HttpResponse<JsonNode> jsonResponse = unirestHttpClient.get(url, null, null);
		String responseJSONString = jsonResponse.getBody().toString();
		JSONObject responeJson = new JSONObject(responseJSONString);
		JSONArray jsonArrayItems = responeJson.getJSONArray("items");

		jsonArrayItems.forEach(c -> {
			JSONObject items = new JSONObject(c.toString());
			try {
				List<CarParkInfoHeaderDto> carParkAvailabilityDtos = objectMapper.readValue(items.getJSONArray("carpark_data").toString(), new TypeReference<>() {
				});
				CompletableFuture<List<CarParks>> carParkAvailabilityList = InserOrUpdateCarParkAvailability(carParkAvailabilityDtos);
				this.carParkRepository.saveAll(carParkAvailabilityList.get());
				log.info("Success Insert or update carParkAvailability");
			} catch (JsonProcessingException | ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	@Async
	public CompletableFuture<List<CarParks>> InserOrUpdateCarParkAvailability(List<CarParkInfoHeaderDto> carParkInfoHeaderDtos) {
		List<CarParks> carParkList = new ArrayList<>();
		carParkInfoHeaderDtos.forEach(carParkInfoHeaderDto -> {
			String carParkNumber = carParkInfoHeaderDto.carpark_number;
			int totalLoats = 0;
			int lotsAvailable = 0;
			Optional<CarParks> carParkOpt = this.carParkRepository.findCarParksByCarPark(carParkNumber);

			for (CarParkInfoDto c : carParkInfoHeaderDto.carpark_info) {
				totalLoats += Integer.parseInt(c.total_lots);
				lotsAvailable += Integer.parseInt(c.lots_available);
			}

			CarParks carPark;
			if (carParkOpt.isPresent()) {
				carPark = carParkOpt.get();
				carPark.setTotalLots(totalLoats);
				carPark.setAvailableLots(lotsAvailable);
				carPark.setUpdatedAt(Instant.now());

			} else {
				carPark = new CarParks();
				carPark.setTotalLots(totalLoats);
				carPark.setAvailableLots(lotsAvailable);
				carPark.setCarPark(carParkNumber);
			}
			carParkList.add(carPark);
		});
		return CompletableFuture.completedFuture(carParkList);
	}

	@Async
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
