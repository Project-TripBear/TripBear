package com.project.trip.AI.model;

import java.util.List;

import javax.swing.text.AbstractDocument.Content;

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
