package com.andri.carpark.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CarPark")
@Table(name = "car_parks")
public class CarParks extends PrimaryAudit {

	@Column(name = "car_park", columnDefinition = "TEXT")
	private String carPark;

	@Column(name = "address", columnDefinition = "TEXT")
	private String address;

	@Column(name = "x_coord", columnDefinition = "TEXT")
	private Double xcoord;

	@Column(name = "y_coord", columnDefinition = "TEXT")
	private Double ycoord;

	@Column(name = "car_park_type", columnDefinition = "TEXT")
	private String carParkType;

	@Column(name = "type_of_parking_system", columnDefinition = "TEXT")
	private String typeOfParkingSystem;

	@Column(name = "short_term_parking", columnDefinition = "TEXT")
	private String shortTermParking;

	@Column(name = "free_parking", columnDefinition = "TEXT")
	private String freeParking;

	@Column(name = "night_parking", columnDefinition = "TEXT")
	private String nightParking;

	@Column(name = "car_park_decks")
	private int carParkDecks;

	@Column(name = "gantry_height", columnDefinition = "TEXT")
	private String gantryHeight;

	@Column(name = "car_park_basement", columnDefinition = "TEXT")
	private String carParkBasement;

	@Column(name = "total_lots")
	private Integer totalLots;

	@Column(name = "available_lots")
	private Integer availableLots;

}
