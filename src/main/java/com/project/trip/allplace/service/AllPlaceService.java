package com.project.trip.allplace.service;

import java.util.List;

import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourItemVO;

public interface AllPlaceService {

    /**
     * Tour API에서 조회한 장소 정보를 데이터베이스에 저장하거나 업데이트합니다.
     * contentId를 기준으로 DB에 장소가 이미 있는지 확인하고,
     * 없으면 새로 추가하고, 있으면 기존 정보를 업데이트합니다.
     *
     * @param item Tour API로부터 받은 장소 정보 값 객체
     * @return 데이터베이스에 저장되거나 조회된 장소 정보 DTO
     */
    public PlaceDTO addPlaceOnDemand(TourItemVO item);
	
    /**
     * 데이터베이스에서 장소 ID를 기준으로 장소의 상세 정보를 조회합니다.
     *
     * @param placeId 조회할 장소의 고유 ID
     * @return 조회된 장소의 상세 정보 DTO
     */
    public PlaceDTO getPlaceDetail(long placeId);
	
    /**
     * Tour API를 사용하여 키워드로 장소를 검색합니다.
     *
     * @param keyword       검색할 키워드
     * @param arrange       정렬 방식 (A=제목순, B=조회순, C=수정일순, D=생성일순)
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId);
	
	
    /**
     * 특정 장소에 해시태그(키워드)를 추가합니다.
     * 키워드가 DB에 없으면 새로 생성한 후 장소와 연결합니다.
     *
     * @param placeId     해시태그를 추가할 장소의 고유 ID
     * @param keywordName 추가할 키워드(해시태그)의 이름
     */
    public void addHashtagToPlace(long placeId, String keywordName);
    
    /**
     * 특정 키워드(해시태그)를 가진 모든 장소의 목록을 조회합니다.
     *
     * @param keywordName 조회할 키워드의 이름
     * @return 해당 키워드를 가진 장소({@link PlaceDTO})의 리스트
     */
    public List<PlaceDTO> findPlacesByKeyword(String keywordName);
    
    /**
     * Tour API를 사용하여 특정 날짜와 지역을 기준으로 축제 정보를 검색합니다.
     *
     * @param eventStartDate 행사 시작일 (yyyyMMdd 형식)
     * @param arrange        정렬 방식 (A=제목순, B=조회순 등)
     * @param locationId     검색할 지역의 ID
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    public TourApiResponseVO searchFestival(String eventStartDate, String arrange, long locationId);
	
    /**
     * Tour API를 사용하여 지역 코드를 기반으로 장소를 검색합니다.
     *
     * @param locationId    검색할 지역의 ID
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    public TourApiResponseVO searchByArea(long locationId, String contentTypeId, String arrange);
    
    /**
     * Tour API를 사용하여 특정 지역의 모든 장소를 검색합니다.
     * 여러 페이지에 걸쳐 데이터를 가져올 수 있습니다.
     *
     * @param locationId    검색할 지역의 ID
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @param rows          한 페이지당 가져올 결과 수
     * @param maxPages      최대 검색할 페이지 수
     * @return 검색된 장소({@link PlaceDTO})의 리스트
     */
    List<PlaceDTO> searchByAreaAll(long locationId, String contentTypeId, String arrange, int rows, int maxPages);

    /**
     * 특정 장소({@link PlaceDTO})를 기준으로 유사한 장소들을 추천합니다.
     *
     * @param base 기준이 되는 장소 정보 DTO
     * @return 추천 장소({@link PlaceDTO})의 리스트
     */
    List<PlaceDTO> getRecommendPlaces(PlaceDTO base);

    /**
     * 특정 장소에 연결된 모든 해시태그(키워드 이름) 목록을 조회합니다.
     *
     * @param placeId 조회할 장소의 고유 ID
     * @return 해시태그 이름 문자열의 리스트
     */
    List<String> getHashtags(long placeId);

}
