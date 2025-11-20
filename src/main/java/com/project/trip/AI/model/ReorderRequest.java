package com.project.trip.AI.model;

import java.util.List;

/**
 * AI 추천 경로에서 특정 날짜의 경유지 순서를 재정렬하기 위한 요청 데이터를 표현하는 클래스입니다.
 * <p>
 * 재정렬할 날짜와 해당 날짜의 경유지 목록을 포함합니다.
 * </p>
 */
public class ReorderRequest {

    /**
     * 재정렬할 날짜 (예: 1일차, 2일차)
     */
    private int day;
    /**
     * 해당 날짜의 재정렬된 경유지 목록
     */
    private List<StopOrderDTO> stops;

    /**
     * 재정렬할 날짜를 반환합니다.
     * @return 날짜
     */
    public int getDay() {
        return day;
    }
    /**
     * 재정렬할 날짜를 설정합니다.
     * @param day 설정할 날짜
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * 해당 날짜의 재정렬된 경유지 목록을 반환합니다.
     * @return 경유지 목록
     */
    public List<StopOrderDTO> getStops() {
        return stops;
    }
    /**
     * 해당 날짜의 재정렬된 경유지 목록을 설정합니다.
     * @param stops 설정할 경유지 목록
     */
    public void setStops(List<StopOrderDTO> stops) {
        this.stops = stops;
    }
}
