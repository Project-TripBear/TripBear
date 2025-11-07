package com.project.trip.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegratedReservationResponse {

    private ReservationDTO reservation;
    private AccomReservationDTO accomReservation;
    private CarReservationDTO carReservation; // null 가능
}
