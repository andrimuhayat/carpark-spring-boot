package com.andri.carpark.repository;

import com.andri.carpark.model.CarParks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface CarParkRepository extends JpaRepository<CarParks, Integer>{

	@Query(nativeQuery = true,value = "SELECT * FROM car_parks where car_park = :car_park LIMIT 1")
	Optional<CarParks> findCarParksByCarPark(@Param("car_park") String carPark);

	@Query(nativeQuery = true,
			value = "SELECT *, 111.111 * DEGREES(ACOS(LEAST(COS(RADIANS(cp.y_coord)) * COS(RADIANS(:latitude)) * COS(RADIANS(cp.x_coord - :longitude)) + SIN(RADIANS(cp.y_coord)) * SIN(RADIANS(:latitude)),1.0))) AS distance_in_km FROM car_parks cp where cp.total_lots > cp.available_lots AND cp.available_lots > 0 and (cp.y_coord is not null  and y_coord is not null) order by 111.111 * DEGREES(ACOS(LEAST(COS(RADIANS(cp.y_coord)) * COS(RADIANS(:latitude)) * COS(RADIANS(cp.x_coord - :longitude)) + SIN(RADIANS(cp.y_coord)) * SIN(RADIANS(:latitude)),1.0)))")
	Page<Map<String, Object>> getCarParks(Pageable pageable, @Param("latitude") double latitude, @Param("longitude")  double longitude );

//	List<CarParks> getCarParkAvailabilities(String carPark);

}
