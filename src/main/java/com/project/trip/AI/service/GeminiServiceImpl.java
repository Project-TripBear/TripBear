package com.project.trip.AI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;

@Service
public class GeminiServiceImpl implements GeminiService{
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Gson gson = new Gson();

	private String GEMINI_API_KEY = "key AIzaSyBbLQsNwivCj7-hXYpPT9vpCWm9Z1w1dg4";
	String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
	
	@Override
	public RouteDTO generateRoute(AiRouteRequestDTO preferences) {

		String prompt = createPrompt(preferences);
		
		System.out.println("---Gemini API 요청 프롬프트----");
		System.out.println(prompt);
		System.out.println("-------------------------------");
		return null;
		
		
	}

	private String createPrompt(AiRouteRequestDTO preferences) {
		
		return null;
	}

	
	

}
