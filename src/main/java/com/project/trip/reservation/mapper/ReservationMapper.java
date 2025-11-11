package com.project.trip.reservation.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.trip.reservation.model.AccomReservationDTO;
import com.project.trip.reservation.model.AccomRoomCardDTO;
import com.project.trip.reservation.model.CarReservationDTO;
import com.project.trip.reservation.model.RentalCarCardDTO;
import com.project.trip.reservation.model.ReservationDTO;

@Mapper
public interface ReservationMapper {
	
	 // 1) 통합예약 INSERT (selectKey로 reservationId 세팅)
    int insertReservation(ReservationDTO dto);

    // 2) 객실예약 INSERT (필수)
    int insertAccomReservation(AccomReservationDTO dto);

    // 3) 차량예약 INSERT (선택)
    int insertCarReservation(CarReservationDTO dto);

	CarReservationDTO findCarByReservationId(Long reservationId);

	ReservationDTO findReservationById(Long reservationId);

	AccomReservationDTO findAccomByReservationId(Long reservationId);
	
	List<AccomRoomCardDTO> selectRoomsByRegion(String region);

	List<RentalCarCardDTO> findCarsByRegion(Map<String, Object> params);

	Integer getRoomPricePerNight(Long roomId);

	Integer getCarPricePerDay(Long carId);
	
	AccomRoomCardDTO getRoomInfo(Long roomId);

    RentalCarCardDTO getCarInfo(Long carId);

	List<?> getCarTypes();

	List<?> getFuelTypes();

	List<?> getSeats();
    
}
