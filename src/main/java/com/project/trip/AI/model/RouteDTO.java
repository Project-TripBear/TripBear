package com.project.trip.AI.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * AI가 생성한 여행 경로의 전체 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 경로 ID, 사용자 ID, 대화 ID, 경로 제목, 총 일수, 생성일, 지역, 시작일, 종료일, 날씨 고려 여부 등
 * 경로의 기본 정보와 함께 경로의 경유지 목록, 모빌리티 정보 등을 포함합니다.
 * </p>
 */
@Data
public class RouteDTO {
	
	/**
	 * AI 여행 경로의 고유 식별자
	 */
	private long aiRouteId;
	/**
	 * 경로를 생성한 사용자의 고유 식별자
	 */
	private long userId;
	/**
	 * 경로 생성에 사용된 대화의 고유 식별자
	 */
	private long conversationId;
	
	/**
	 * AI 여행 경로의 제목
	 */
	private String aiRouteTitle;
	/**
	 * AI 여행 경로의 총 일수
	 */
	private int aiRouteDays;
	/**
	 * AI 여행 경로 생성일
	 */
	private Date aiRouteCreated;
	/**
	 * AI 여행 경로의 지역
	 */
	private String aiRouteRegion;
	/**
	 * AI 여행 경로의 시작일
	 */
	private String aiRouteStartDate;
	/**
	 * AI 여행 경로의 종료일
	 */
	private String aiRouteEndDate;
	/**
	 * 날씨 고려 여부
	 */
	private String weatherConsideration;

	/**
	 * AI 여행 경로의 경유지 목록
	 */
	private List<RouteStopDTO> stops;
	
	/**
	 * 모빌리티(교통수단) 정보 목록 (JSON 형태로 클라이언트에 전달될 수 있음)
	 */
	private List<Object> mobilityRoutes;

}
