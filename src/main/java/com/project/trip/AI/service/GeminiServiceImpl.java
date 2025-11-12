package com.project.trip.AI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;

@Service
public class GeminiServiceImpl implements GeminiService{

	@Override
	public RouteDTO generateRoute(AiRouteRequestDTO preferences) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
