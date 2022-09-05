package com.andri.carpark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarParkDto {
	public double latitude;
	public double longitude;
	private int available_lots;
	private int total_lots;
	private String address;
	private double distance_in_km;
}
