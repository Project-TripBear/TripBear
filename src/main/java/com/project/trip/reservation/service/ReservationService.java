package com.project.trip.reservation.service;

import java.util.List;
import java.util.Map;

import com.project.trip.reservation.model.AccomReservationDTO;
import com.project.trip.reservation.model.AccomRoomCardDTO;
import com.project.trip.reservation.model.CarReservationDTO;
import com.project.trip.reservation.model.IntegratedReservation;
import com.project.trip.reservation.model.RentalCarCardDTO;
import com.project.trip.reservation.model.ReservationDTO;

public interface ReservationService {

    Long createIntegratedReservation(
            ReservationDTO reservationDTO,
            AccomReservationDTO accomReservationDTO,
            CarReservationDTO carReservationDTO
    ) throws Exception;
    
    IntegratedReservation getIntegratedReservation(Long reservationId) throws Exception;
    
    List<AccomRoomCardDTO> findRoomsByRegion(String region) throws Exception;

    List<RentalCarCardDTO> findCarsByRegion(String region, String carType, String fuelType, Integer seats, Integer maxPrice);


	long calculateTotalPrice(Long roomId, Long carId, String checkin, String checkout) throws Exception;
	IntegratedReservation getIntegratedReservationPreview(String region, String checkin, String checkout, String people, String roomId, String carId) throws Exception;

	Map<String, List<?>> getCarFilterOptions();
	
	long calculateTotalPrice(Long roomId, Long carId, String checkin, String checkout,
            String rentalStart, String rentalEnd) throws Exception;

	Object getCarInfo(long carId);
	
	Object getRoomInfo(long roomId);

}
