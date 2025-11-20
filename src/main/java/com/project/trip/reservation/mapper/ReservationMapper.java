package com.project.trip.reservation.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.trip.reservation.model.AccomReservationDTO;
import com.project.trip.reservation.model.AccomRoomCardDTO;
import com.project.trip.reservation.model.CarReservationDTO;
import com.project.trip.reservation.model.RentalCarCardDTO;
import com.project.trip.reservation.model.ReservationDTO;

/**
 * 예약과 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 통합 예약, 숙소 예약, 렌터카 예약 정보의 삽입 및 조회,
 * 숙소 및 렌터카 목록 조회, 가격 정보 조회 등 예약 관리에 필요한
 * 다양한 데이터베이스 작업을 정의합니다.
 * </p>
 */
@Mapper
public interface ReservationMapper {
	
	 // 1) 통합예약 INSERT (selectKey로 reservationId 세팅)
    int insertReservation(ReservationDTO dto);

    // 2) 객실예약 INSERT (필수)
    int insertAccomReservation(AccomReservationDTO dto);

    // 3) 차량예약 INSERT (선택)
    int insertCarReservation(CarReservationDTO dto);
	
	List<AccomRoomCardDTO> selectRoomsByRegion(String region);

	List<RentalCarCardDTO> findCarsByRegion(Map<String, Object> params);

	Integer getRoomPricePerNight(Long roomId);

	Integer getCarPricePerDay(Long carId);
	
	AccomRoomCardDTO getRoomInfo(Long roomId);

    RentalCarCardDTO getCarInfo(Long carId);

	List<?> getCarTypes();

	List<?> getFuelTypes();

	List<?> getSeats();

    ReservationDTO findReservationById(Long reservationId);

    AccomReservationDTO findAccomByReservationId(Long reservationId);

    CarReservationDTO findCarByReservationId(Long reservationId);
    
}
