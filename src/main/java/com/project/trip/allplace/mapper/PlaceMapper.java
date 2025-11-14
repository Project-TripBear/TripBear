package com.project.trip.allplace.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.project.trip.allplace.model.EventDTO;
import com.project.trip.allplace.model.KeywordDTO;
import com.project.trip.allplace.model.KeywordLinkDTO;
import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.RestaurantDTO;
import com.project.trip.allplace.model.TouristSpotDTO;

public interface PlaceMapper {

    // ====== PLACE 기본 기능 ======
    PlaceDTO findPlaceByContentId(String apiContentId);

    void insertPlace(PlaceDTO place);

    PlaceDTO getPlaceDetailById(long placeId);


    // ====== 관광지 / 축제 / 음식점 상세 저장 ======
    int insertTouristSpot(TouristSpotDTO dto);

    int insertEvent(EventDTO dto);

    int insertRestaurant(RestaurantDTO dto);


    // ====== HASHTAG 기능 ======
    // 키워드 조회
    KeywordDTO findKeywordByName(String keywordName);

    // 키워드 생성
    int insertKeyword(KeywordDTO dto);

    // 장소-키워드 연결
    int insertKeywordLink(KeywordLinkDTO link);

    // 장소에 연결된 해시태그 목록 조회
    List<String> findHashtagsByPlaceId(@Param("placeId") long placeId);

    // 특정 키워드를 가진 장소 목록 조회
    List<PlaceDTO> findPlacesByKeywordName(String keywordName);


    // ====== 추천 장소 기능 ======
    List<PlaceDTO> findRecommendPlaces(
            @Param("locationId") long locationId,
            @Param("typeId") long typeId,
            @Param("placeId") long placeId
    );
}
