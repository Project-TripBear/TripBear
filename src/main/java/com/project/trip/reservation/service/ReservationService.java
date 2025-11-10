package com.project.trip.reservation.service;

import java.util.List;

import com.project.trip.reservation.model.AccomReservationDTO;
import com.project.trip.reservation.model.AccomRoomCardDTO;
import com.project.trip.reservation.model.CarReservationDTO;
import com.project.trip.reservation.model.IntegratedReservation;
import com.project.trip.reservation.model.ReservationDTO;

public interface ReservationService {

    Long createIntegratedReservation(
            ReservationDTO reservationDTO,
            AccomReservationDTO accomReservationDTO,
            CarReservationDTO carReservationDTO
    ) throws Exception;
    
    IntegratedReservation getIntegratedReservation(Long reservationId) throws Exception;
    
    List<AccomRoomCardDTO> findRoomsByRegion(String region) throws Exception;

}
