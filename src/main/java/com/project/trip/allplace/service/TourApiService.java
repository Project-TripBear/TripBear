package com.project.trip.allplace.service;

import java.util.List;

import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourIntroEventVO;
import com.project.trip.allplace.model.TourIntroRestaurantVO;
import com.project.trip.allplace.model.TourIntroVO;
import com.project.trip.allplace.model.TourItemVO;

/**
 * 한국관광공사 Tour API와 연동하여 장소 정보를 조회하는 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 * <p>
 * 키워드, 지역, 위치 기반 검색 및 장소의 상세 정보, 소개 정보 등을 조회하는 기능을 제공합니다.
 * </p>
 */
public interface TourApiService {

    /**
     * Tour API를 사용하여 키워드로 장소를 검색합니다.
     *
     * @param keyword       검색할 키워드
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId);
    /**
     * Tour API를 사용하여 특정 날짜와 지역을 기준으로 축제 정보를 검색합니다.
     *
     * @param eventStartDate 행사 시작일 (yyyyMMdd 형식)
     * @param arrange        정렬 방식 (A=제목순, B=조회순 등)
     * @param areaCode       검색할 지역 코드
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    TourApiResponseVO searchFestival(String eventStartDate, String arrange, String areaCode);
    /**
     * Tour API를 사용하여 지역 코드를 기반으로 장소를 검색하고, 페이지네이션 정보를 포함하여 결과를 반환합니다.
     *
     * @param areaCode      검색할 지역 코드
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @param pageNo        요청할 페이지 번호
     * @param rows          한 페이지당 가져올 결과 수
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange, int pageNo, int rows);
    /**
     * Tour API를 사용하여 특정 지역의 모든 장소 정보를 원시({@link TourItemVO}) 형태로 검색합니다.
     * 여러 페이지에 걸쳐 데이터를 가져올 수 있습니다.
     *
     * @param areaCode      검색할 지역 코드
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @param rows          한 페이지당 가져올 결과 수
     * @param maxPages      최대 검색할 페이지 수
     * @return 검색된 장소 아이템({@link TourItemVO})의 리스트
     */
    List<TourItemVO> searchByAreaAllRaw(String areaCode, String contentTypeId, String arrange, int rows, int maxPages);
    /**
     * Tour API의 '상세 정보 조회'를 사용하여 특정 장소의 상세 정보를 조회합니다.
     *
     * @param contentId 조회할 콘텐츠 ID
     * @return 조회된 장소의 상세 정보 {@link TourItemVO}
     */
    TourItemVO getPlaceDetail(String contentId);

    /**
     * Tour API의 '소개 정보 조회'를 사용하여 특정 장소의 소개 정보를 조회합니다.
     * (주로 관광지(contentTypeId=12)의 상세 소개 정보)
     *
     * @param contentId     조회할 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID
     * @return 조회된 장소의 소개 정보 {@link TourIntroVO}
     */
    TourIntroVO getPlaceIntro(String contentId, String contentTypeId);
    /**
     * Tour API의 '소개 정보 조회'를 사용하여 특정 축제/행사의 소개 정보를 조회합니다.
     * (주로 축제/행사(contentTypeId=15)의 상세 소개 정보)
     *
     * @param contentId     조회할 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID
     * @return 조회된 축제/행사의 소개 정보 {@link TourIntroEventVO}
     */
    TourIntroEventVO getEventIntro(String contentId, String contentTypeId);
    /**
     * Tour API의 '소개 정보 조회'를 사용하여 특정 음식점의 소개 정보를 조회합니다.
     * (주로 음식점(contentTypeId=39)의 상세 소개 정보)
     *
     * @param contentId     조회할 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID
     * @return 조회된 음식점의 소개 정보 {@link TourIntroRestaurantVO}
     */
    TourIntroRestaurantVO getRestaurantIntro(String contentId, String contentTypeId);
    
    /**
     * Tour API를 사용하여 지역 코드를 기반으로 장소를 검색합니다.
     * 이 버전은 페이지네이션 정보 없이 모든 결과를 가져오는 데 사용될 수 있습니다.
     *
     * @param areaCode      검색할 지역 코드
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange);
    
    /**
     * Tour API를 사용하여 특정 위치(위도, 경도)와 반경 내의 장소를 검색합니다.
     *
     * @param lat           중심 위도
     * @param lng           중심 경도
     * @param radius        검색 반경 (미터 단위)
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    public TourApiResponseVO searchByLocation(String lat, String lng, String radius, String contentTypeId);
}