package com.project.trip.reservation.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.reservation.mapper.ReservationMapper;
import com.project.trip.reservation.model.AccomReservationDTO;
import com.project.trip.reservation.model.AccomRoomCardDTO;
import com.project.trip.reservation.model.CarReservationDTO;
import com.project.trip.reservation.model.IntegratedReservation;
import com.project.trip.reservation.model.RentalCarCardDTO;
import com.project.trip.reservation.model.ReservationDTO;

import lombok.RequiredArgsConstructor;

/**
 * {@link ReservationService} 인터페이스의 구현 클래스입니다.
 * <p>
 * 예약과 관련된 비즈니스 로직을 처리합니다.
 * 통합 예약 생성 및 조회, 숙소 및 렌터카 목록 조회, 가격 계산, 필터링 옵션 제공 등
 * 예약 시스템 운영에 필요한 다양한 기능을 제공합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
	
	private final ReservationMapper reservationMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createIntegratedReservation(
			ReservationDTO reservationDTO,
            AccomReservationDTO accomReservationDTO,
            CarReservationDTO carReservationDTO
	) throws Exception {
		
		//1. 통합예약 insert
		reservationMapper.insertReservation(reservationDTO);
		
		Long reservationId = reservationDTO.getReservationId();
		
		//2. 객실예약 insert
		accomReservationDTO.setReservationId(reservationId);
		reservationMapper.insertAccomReservation(accomReservationDTO);
		
		//3. 차량예약 insert (선택)
		if(carReservationDTO != null) {
			carReservationDTO.setReservationId(reservationId);
			reservationMapper.insertCarReservation(carReservationDTO);
		}
		
		return reservationId;
			
	}
	
	@Override
	public IntegratedReservation getIntegratedReservation(Long reservationId) throws Exception {

	    ReservationDTO reservation = reservationMapper.findReservationById(reservationId);
	    AccomReservationDTO accom = reservationMapper.findAccomByReservationId(reservationId);
	    CarReservationDTO car = reservationMapper.findCarByReservationId(reservationId);
	    
	    return new IntegratedReservation(reservation, accom, car);
	}
	
	@Override
    public List<AccomRoomCardDTO> findRoomsByRegion(String region) throws Exception {
        return reservationMapper.selectRoomsByRegion(region);
    }
	
	@Override
	public List<RentalCarCardDTO> findCarsByRegion(String region, String carType, String fuelType, Integer seats, Integer maxPrice) {

	    Map<String, Object> params = new HashMap<>();
	    params.put("region", region);
	    params.put("carType", carType);
	    params.put("fuelType", fuelType);
	    params.put("seats", seats);
	    params.put("maxPrice", maxPrice);

	    List<RentalCarCardDTO> cars = reservationMapper.findCarsByRegion(params);
	    return cars;
	}

	
	@Override
	public long calculateTotalPrice(Long roomId, Long carId, String checkin, String checkout) throws Exception {

	    long total = 0;

	    // 숙박일수 계산
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date in = sdf.parse(checkin);
	    Date out = sdf.parse(checkout);
	    long nights = (out.getTime() - in.getTime()) / (1000 * 60 * 60 * 24);

	    if (roomId != null) {
	        Integer roomPrice = reservationMapper.getRoomPricePerNight(roomId);
	        total += roomPrice * nights;
	    }

	    if (carId != null) {
	        Integer carPrice = reservationMapper.getCarPricePerDay(carId);
	        total += carPrice * nights; // 차량 기간은 동일 가정
	    }

	    return total;
	}
	
	@Override
	public IntegratedReservation getIntegratedReservationPreview(
	        String region, String checkin, String checkout, 
	        String roomId, String carId) throws Exception {
	    
	    IntegratedReservation data = new IntegratedReservation();
	    
	    data.setRegion(region);
	    data.setCheckin(checkin);
	    data.setCheckout(checkout);
	    
	    // 숙소 정보
	    if (roomId != null && !roomId.isBlank()) {
	        data.setRoom(reservationMapper.getRoomInfo(Long.parseLong(roomId)));
	    }
	    
	    // 차량 정보 (선택했을 때만)
	    if (carId != null && !carId.isBlank()) {
	        data.setCar(reservationMapper.getCarInfo(Long.parseLong(carId)));
	    }
	    
	    return data;
	}
	
	@Override
	public Map<String, List<?>> getCarFilterOptions() {
	    Map<String, List<?>> filters = new HashMap<>();
	    filters.put("carTypes", reservationMapper.getCarTypes());
	    filters.put("fuelTypes", reservationMapper.getFuelTypes());
	    filters.put("seats", reservationMapper.getSeats());
	    return filters;
	}
	
	@Override
	public Object getRoomInfo(long roomId) {
	    return reservationMapper.getRoomInfo(roomId);
	}

	@Override
	public Object getCarInfo(long carId) {
	    return reservationMapper.getCarInfo(carId);
	}

	@Override
	public long calculateTotalPrice(Long roomId, Long carId,
	                                String checkin, String checkout,
	                                String rentalStart, String rentalEnd) throws Exception {

	    long total = 0;

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	    Date in = sdf.parse(checkin);
	    Date out = sdf.parse(checkout);
	    long nights = (out.getTime() - in.getTime()) / (1000 * 60 * 60 * 24);
	    if (nights <= 0) nights = 1; // 최소 1박 처리

	    if (roomId != null) {
	        Integer roomPrice = reservationMapper.getRoomPricePerNight(roomId);
	        total += roomPrice * nights;
	    }

	    // 차량 기간은 별도로 계산 (rentalStart ~ rentalEnd)
	    if (carId != null) {
	        Date rentStart = sdf.parse(rentalStart != null ? rentalStart : checkin);
	        Date rentEnd   = sdf.parse(rentalEnd   != null ? rentalEnd   : checkout);
	        long rentDays = (rentEnd.getTime() - rentStart.getTime()) / (1000 * 60 * 60 * 24);
	        if (rentDays <= 0) rentDays = 1;

	        Integer carPrice = reservationMapper.getCarPricePerDay(carId);
	        total += carPrice * rentDays;
	    }

	    return total;
	}

	
	



}