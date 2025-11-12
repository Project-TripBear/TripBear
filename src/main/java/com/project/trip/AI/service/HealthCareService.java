package com.project.trip.AI.service;

import java.time.LocalDate;

import com.project.trip.AI.model.RouteStopDTO;

public interface HealthCareService {
	
	void saveHealthCareLog(long userId, double userWeight, RouteStopDTO stop, LocalDate tripStartDate);

}
