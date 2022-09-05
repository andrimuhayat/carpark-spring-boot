package com.andri.carpark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarParkInfoHeaderDto {
	public ArrayList<CarParkInfoDto> carpark_info;
	public String carpark_number;
	public Date update_datetime;
}
