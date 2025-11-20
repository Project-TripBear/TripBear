package com.project.trip.allplace.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.project.trip.allplace.model.EventDTO;
import com.project.trip.allplace.model.KeywordDTO;
import com.project.trip.allplace.model.KeywordLinkDTO;
import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.RestaurantDTO;
import com.project.trip.allplace.model.TouristSpotDTO;

/**
 * 장소(Place) 정보와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 장소의 조회, 삽입, 상세 정보(관광지, 축제, 음식점) 관리, 해시태그(키워드) 관리,
 * 그리고 추천 장소 조회 등 다양한 데이터베이스 작업을 정의합니다.
 * </p>
 */
public interface PlaceMapper {

    // ====== PLACE 기본 기능 ======
    /**
     * Tour API의 콘텐츠 ID를 사용하여 데이터베이스에서 장소 정보를 조회합니다.
     *
     * @param apiContentId 조회할 API 콘텐츠 ID
     * @return 조회된 장소 정보를 담은 {@link PlaceDTO}, 결과가 없으면 null
     */
    PlaceDTO findPlaceByContentId(String apiContentId);

    /**
     * 새로운 장소 정보를 데이터베이스에 삽입합니다.
     * `useGeneratedKeys` 옵션을 통해 삽입 후 생성된 placeId가 파라미터의 {@link PlaceDTO} 객체에 채워집니다.
     *
     * @param place 삽입할 장소 정보를 담은 {@link PlaceDTO} 객체
     */
    void insertPlace(PlaceDTO place);

    /**
     * 데이터베이스의 장소 ID(PK)를 사용하여 장소의 상세 정보를 조회합니다.
     *
     * @param placeId 조회할 장소의 고유 ID
     * @return 조회된 장소의 상세 정보를 담은 {@link PlaceDTO}, 결과가 없으면 null
     */
    PlaceDTO getPlaceDetailById(long placeId);


    // ====== 관광지 / 축제 / 음식점 상세 저장 ======
    /**
     * 관광지의 상세 정보(주차, 유모차 대여 여부 등)를 별도 테이블에 삽입합니다.
     *
     * @param dto 삽입할 관광지 상세 정보를 담은 {@link TouristSpotDTO}
     * @return 삽입된 행의 수
     */
    int insertTouristSpot(TouristSpotDTO dto);

    /**
     * 축제/행사의 상세 정보(시작일, 종료일, 장소 등)를 별도 테이블에 삽입합니다.
     *
     * @param dto 삽입할 행사 상세 정보를 담은 {@link EventDTO}
     * @return 삽입된 행의 수
     */
    int insertEvent(EventDTO dto);

    /**
     * 음식점의 상세 정보(대표 메뉴, 영업 시간 등)를 별도 테이블에 삽입합니다.
     *
     * @param dto 삽입할 음식점 상세 정보를 담은 {@link RestaurantDTO}
     * @return 삽입된 행의 수
     */
    int insertRestaurant(RestaurantDTO dto);


    // ====== HASHTAG 기능 ======
    // 키워드 조회
    /**
     * 키워드(해시태그) 이름을 사용하여 키워드 정보를 데이터베이스에서 조회합니다.
     *
     * @param keywordName 조회할 키워드의 이름
     * @return 조회된 키워드 정보를 담은 {@link KeywordDTO}, 결과가 없으면 null
     */
    KeywordDTO findKeywordByName(String keywordName);

    // 키워드 생성
    /**
     * 새로운 키워드(해시태그)를 데이터베이스에 삽입합니다.
     * `useGeneratedKeys` 옵션을 통해 삽입 후 생성된 keywordId가 파라미터의 {@link KeywordDTO} 객체에 채워집니다.
     *
     * @param dto 삽입할 키워드 정보를 담은 {@link KeywordDTO}
     * @return 삽입된 행의 수
     */
    int insertKeyword(KeywordDTO dto);

    // 장소-키워드 연결
    /**
     * 장소와 키워드(해시태그) 간의 관계를 연결 테이블에 삽입합니다.
     *
     * @param link 연결할 장소 ID와 키워드 ID를 담은 {@link KeywordLinkDTO}
     * @return 삽입된 행의 수
     */
    int insertKeywordLink(KeywordLinkDTO link);

    // 장소에 연결된 해시태그 목록 조회
    /**
     * 특정 장소에 연결된 모든 해시태그(키워드 이름) 목록을 조회합니다.
     *
     * @param placeId 조회할 장소의 고유 ID
     * @return 해시태그 이름 문자열의 리스트
     */
    List<String> findHashtagsByPlaceId(@Param("placeId") long placeId);

    // 특정 키워드를 가진 장소 목록 조회
    /**
     * 특정 키워드(해시태그)를 가진 모든 장소의 목록을 조회합니다.
     *
     * @param keywordName 조회할 키워드의 이름
     * @return 해당 키워드를 가진 장소({@link PlaceDTO})의 리스트
     */
    List<PlaceDTO> findPlacesByKeywordName(String keywordName);


    // ====== 추천 장소 기능 ======
    /**
     * 특정 장소와 동일한 지역 및 타입에 속하는 다른 장소들을 추천 목록으로 조회합니다.
     *
     * @param locationId 추천을 조회할 지역의 ID
     * @param typeId     추천을 조회할 장소 타입의 ID
     * @param placeId    현재 장소의 ID (추천 목록에서 제외하기 위함)
     * @return 추천 장소({@link PlaceDTO})의 리스트
     */
    List<PlaceDTO> findRecommendPlaces(
            @Param("locationId") long locationId,
            @Param("typeId") long typeId,
            @Param("placeId") long placeId
    );
}
