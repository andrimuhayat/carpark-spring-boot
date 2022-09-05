package com.andri.carpark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarParkInformationDto {
	public String short_term_parking;
	public String car_park_type;
	public String y_coord;
	public String x_coord;
	public String free_parking;
	public String gantry_height;
	public String car_park_basement;
	public String night_parking;
	public String address;
	public String car_park_decks;
	public int _id;
	public String car_park_no;
	public String type_of_parking_system;
}
