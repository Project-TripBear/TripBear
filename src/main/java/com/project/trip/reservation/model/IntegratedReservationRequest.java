package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegratedReservationRequest {

    private ReservationDTO reservation;
    private AccomReservationDTO accomReservation;
    private CarReservationDTO carReservation; // null 가능
}
