package com.project.trip.allplace.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tblRestaurant (음식점) 테이블과 매핑되는 DTO
 * (ERD 이미지 기준으로 수정됨)
 */
@Data
@NoArgsConstructor
public class RestaurantDTO {

    // 1. FK (tblPlace 참조)
    private long placeId; 
    
    // 2. ERD 기준 컬럼
    private String restaurantCategory; // restaurant_category (취급 메뉴)
    private String restaurantCall;     // restaurant_call (전화번호)
    private String restaurantPrice;    // restaurant_price (대표 메뉴)
    private double restaurantRating;   // restaurant_rating (API 정보 없음 -> 0.0)
    private long restaurantCount;      // restaurant_count (API 정보 없음 -> 0)
    private String restaurantOpenTime; // restaurant_open_time (영업 시간)
    private String restaurantCloseTime;// restaurant_close_time (API 정보 없음 -> null)
}