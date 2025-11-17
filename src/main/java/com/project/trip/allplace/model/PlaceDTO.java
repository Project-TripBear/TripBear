package com.project.trip.allplace.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 관광지, 축제, 음식점 등 모든 장소의 공통 정보를 나타내는 핵심 데이터 전송 객체(DTO)입니다.
 * `tblPlace` 테이블과 매핑됩니다.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PlaceDTO {

    /**
     * 장소의 고유 식별자 (PK)
     */
    private long placeId;

    /**
     * Tour API에서 사용하는 콘텐츠 ID
     */
    private String placeApiId;

    /**
     * 장소 타입의 고유 식별자 (FK, tblPlaceType 참조. 예: 1-관광지, 2-축제, 3-음식점)
     */
    private long placeTypeId;

    /**
     * 장소가 속한 지역의 고유 식별자 (FK, tblLocation 참조)
     */
    private long placeLocationId;

    /**
     * 장소의 이름
     */
    private String name;

    /**
     * 장소의 주소
     */
    private String address;

    /**
     * 장소의 위도
     */
    private double latitude;

    /**
     * 장소의 경도
     */
    private double longitude;

    /**
     * 장소의 대표 이미지 URL
     */
    private String placeMainImageUrl;

    /**
     * 현재 위치로부터의 거리 (계산된 값, DB 컬럼 아님)
     */
    private double distance;

    /**
     * 장소에 대한 개요 설명 (API 제공)
     */
    private String overview;

    /**
     * 장소에 연결된 해시태그 목록 (DB 조회 후 채워짐, DB 컬럼 아님)
     */
    private List<String> hashtags;

    /**
     * 관광지 상세 정보 (장소 타입이 관광지일 경우)
     */
    private TouristSpotDTO touristSpotDetail;

    /**
     * 행사/축제 상세 정보 (장소 타입이 축제일 경우)
     */
    private EventDTO eventDetail;

    /**
     * 음식점 상세 정보 (장소 타입이 음식점일 경우)
     */
    private RestaurantDTO restaurantDetail;

    /**
     * Tour API의 콘텐츠 타입 ID (예: "12"-관광지, "15"-축제, "39"-음식점)
     */
    private String contentTypeId;
}