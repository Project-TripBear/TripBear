package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservationDTO {
    private Long reservationId;
    private Long userId;
    private Long userRouteId;
    private Integer statusId;
    private Integer reservationPrice;
    private String reservationStartDate;
    private String reservationEndDate;
    private String reservationRegdate;
}


