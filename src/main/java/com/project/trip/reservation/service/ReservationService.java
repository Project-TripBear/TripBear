package com.project.trip.reservation.service;

import com.project.trip.reservation.model.AccomReservationDTO;
import com.project.trip.reservation.model.CarReservationDTO;
import com.project.trip.reservation.model.IntegratedReservationResponse;
import com.project.trip.reservation.model.ReservationDTO;

public interface ReservationService {

    Long createIntegratedReservation(
            ReservationDTO reservationDTO,
            AccomReservationDTO accomReservationDTO,
            CarReservationDTO carReservationDTO
    ) throws Exception;
    
    IntegratedReservationResponse getIntegratedReservation(Long reservationId) throws Exception;

}
