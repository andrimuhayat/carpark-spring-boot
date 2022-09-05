package com.andri.carpark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarParkInfoDto {
	public String total_lots;
	public String lot_type;
	public String lots_available;
}
