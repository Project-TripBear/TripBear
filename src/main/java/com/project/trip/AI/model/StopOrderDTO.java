package com.project.trip.AI.model;

/**
 * AI 추천 경로의 특정 경유지(정류장)의 ID와 순서 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 주로 경유지 순서를 재정렬하는 요청에서 각 경유지의 새로운 순서를 지정할 때 활용됩니다.
 * </p>
 */
public class StopOrderDTO {

    /**
     * 경유지의 고유 식별자
     */
    private Long stopId;
    /**
     * 경유지의 새로운 순서
     */
    private int order;

    /**
     * 경유지의 고유 식별자를 반환합니다.
     * @return 경유지 ID
     */
    public Long getStopId() {
        return stopId;
    }
    /**
     * 경유지의 고유 식별자를 설정합니다.
     * @param stopId 설정할 경유지 ID
     */
    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    /**
     * 경유지의 순서를 반환합니다.
     * @return 순서
     */
    public int getOrder() {
        return order;
    }
    /**
     * 경유지의 순서를 설정합니다.
     * @param order 설정할 순서
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
