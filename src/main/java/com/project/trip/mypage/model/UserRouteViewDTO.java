package com.project.trip.mypage.model;

import lombok.Data;

@Data
public class UserRouteViewDTO {


    private String seq;                 // user_route_id
    private String useq;                // user_id
    private String userroutetitle;      // user_route_title
    private String userroutedays;       // user_route_days
    private String userroutestartdate;  // user_route_startdate
    private String userrouteenddate;    // user_route_enddate
}
