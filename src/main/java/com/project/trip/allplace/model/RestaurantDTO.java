package com.project.trip.allplace.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 음식점의 상세 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblRestaurant` 테이블과 매핑됩니다.
 */
@Data
@NoArgsConstructor
public class RestaurantDTO {

    /**
     * 이 상세 정보가 속한 장소의 고유 식별자 (FK, tblPlace 참조)
     */
    private long placeId;

    /**
     * 음식점의 카테고리 (예: 한식, 중식, 일식)
     */
    private String restaurantCategory;

    /**
     * 음식점의 전화번호
     */
    private String restaurantCall;

    /**
     * 대표 메뉴 및 가격 정보
     */
    private String restaurantPrice;

    /**
     * 음식점 평점 (현재 API에서 제공되지 않아 사용되지 않음)
     */
    private double restaurantRating;

    /**
     * 방문 횟수 또는 리뷰 수 (현재 API에서 제공되지 않아 사용되지 않음)
     */
    private long restaurantCount;

    /**
     * 영업 시간 정보
     */
    private String restaurantOpenTime;

    /**
     * 영업 종료 시간 (현재 API에서 제공되지 않아 사용되지 않음)
     */
    private String restaurantCloseTime;
}