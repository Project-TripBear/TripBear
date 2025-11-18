package com.project.trip.AI.model.gemini;

import java.util.List;

import lombok.Data;

/**
 * Google Gemini API의 응답 구조를 매핑하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * Gemini API로부터 받은 JSON 응답을 Java 객체로 변환하여 쉽게 접근할 수 있도록 합니다.
 * 주로 생성된 텍스트 콘텐츠를 추출하는 데 사용됩니다.
 * </p>
 */
@Data
public class GeminiApiResponse {

	/**
	 * Gemini 모델이 생성한 응답 후보 목록입니다.
	 */
	private List<Candidate> candidate;
	
	/**
	 * 응답 후보의 내용을 담는 내부 클래스입니다.
	 */
	@Data
	public static class Candidate {
		/**
		 * 생성된 텍스트 콘텐츠를 포함합니다.
		 */
		private Content content;
	}
	
	/**
	 * 콘텐츠의 여러 부분을 담는 내부 클래스입니다.
	 */
	@Data
	public static class Content {
		/**
		 * 콘텐츠의 각 부분을 나타내는 {@link Part} 객체 목록입니다.
		 */
		private List<Part> parts;
	}
	
	/**
	 * 콘텐츠의 단일 텍스트 부분을 담는 내부 클래스입니다.
	 */
	@Data
	public static class Part {
		/**
		 * 생성된 텍스트 내용입니다.
		 */
		private String text;
	}
	
	
	
	
}
