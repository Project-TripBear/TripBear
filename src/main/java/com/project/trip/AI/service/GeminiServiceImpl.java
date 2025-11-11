package com.project.trip.AI.service;

import org.springframework.stereotype.Service;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;

@Service
public class GeminiServiceImpl implements GeminiService{

	private String GEMINI_API_KEY = "AIzaSyC9h80v1xE78JuHpoCbTbJwFKJKY9S1Qys";
	private String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY;

	@Override
	public RouteDTO generateRoute(AiRouteRequestDTO preferences) {

		String prompt = createPrompt(preferences);
		
		System.out.println("---Gemini API 요청 프롬프트----");
		System.out.println(prompt);
		System.out.println("-------------------------------");
		
		
	}

	
	

}
