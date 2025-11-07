package com.project.trip.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.reservation.mapper.ReservationMapper;
import com.project.trip.reservation.model.AccomReservationDTO;
import com.project.trip.reservation.model.CarReservationDTO;
import com.project.trip.reservation.model.IntegratedReservationResponse;
import com.project.trip.reservation.model.ReservationDTO;

import lombok.RequiredArgsConstructor;

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



}
