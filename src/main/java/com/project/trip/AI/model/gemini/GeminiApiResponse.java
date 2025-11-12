package com.project.trip.AI.model.gemini;

import java.util.List;

import lombok.Data;

@Data
public class GeminiApiResponse {

	private List<Candidate> candidate;
	
	@Data
	public static class Candidate {
		private Content content;
	}
	
	@Data
	public static class Content {
		private List<Part> parts;
	}
	
	@Data
	public static class Part {
		private String text;
	}
	
	
	
	
}
