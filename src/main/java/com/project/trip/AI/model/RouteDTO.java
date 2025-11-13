package com.project.trip.AI.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class RouteDTO {
	
	private long aiRouteId;
	private long userId;
	private long conversationId;
	
	private String aiRouteTitle;
	private int aiRouteDays;
	private Date aiRouteCreated;
	private String aiRouteRegion;
	private String aiRouteStartDate;
	private String aiRouteEndDate;
	private String weatherConsideration;
	
	private List<RouteStopDTO> stops;
}
