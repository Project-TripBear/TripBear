package com.project.trip.AI.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RouteDTO {
	
	private long aiRouteId;
	private long userId;
	private long conversationId;
	
	private String AiRouteTitle;
	private int AiRouteDays;
	private Date AiRouteCreated;
	private String AiRouteRegion;
	private String AiRouteStartDate;
	private String AiRouteEndDate;
	private String weather_consideration;
	
	private List<RouteStopDTO> stops;
}
