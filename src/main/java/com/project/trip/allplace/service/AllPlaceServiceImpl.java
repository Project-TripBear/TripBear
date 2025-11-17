package com.project.trip.allplace.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.allplace.mapper.PlaceMapper;
// (모든 DTO import)
import com.project.trip.allplace.model.EventDTO;
import com.project.trip.allplace.model.KeywordDTO;
import com.project.trip.allplace.model.KeywordLinkDTO;
import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.RestaurantDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourIntroEventVO;
import com.project.trip.allplace.model.TourIntroRestaurantVO;
import com.project.trip.allplace.model.TourIntroVO;
import com.project.trip.allplace.model.TourItemVO;
import com.project.trip.allplace.model.TouristSpotDTO;

@Service
public class AllPlaceServiceImpl implements AllPlaceService {

    @Autowired
    private PlaceMapper placeMapper;

    @Autowired
    private TourApiService tourApiService;

    /**
     * Tour API에서 조회한 장소 정보를 데이터베이스에 저장하거나 업데이트합니다.
     * <p>
     * contentId를 기준으로 DB에 장소가 이미 있는지 확인하고,
     * 없으면 {@link PlaceDTO} 및 해당 타입(관광지/축제/음식점)의 상세 정보를 새로 추가합니다.
     * 이미 존재하면 기존 정보를 반환합니다.
     * </p>
     * @param item Tour API로부터 받은 장소 정보 값 객체
     * @return 데이터베이스에 저장되거나 조회된 장소 정보 DTO
     */
    @Transactional
    @Override
    public PlaceDTO addPlaceOnDemand(TourItemVO item) { 
        
        if (item == null || item.getContentId() == null) {
            System.err.println("[OnDemand] 'item' 파라미터가 null입니다.");
            return null;
        }
        
        String contentId = item.getContentId();
        PlaceDTO place = placeMapper.findPlaceByContentId(contentId);

        if (place == null) {
            System.out.println("[OnDemand] DB에 없음. API Item을 DB에 저장합니다...");
            
            PlaceDTO newPlace = new PlaceDTO();
            newPlace.setPlaceApiId(item.getContentId());
            newPlace.setName(item.getTitle());
            newPlace.setAddress(item.getAddress());
            newPlace.setPlaceMainImageUrl(item.getFirstImage());

            try { 
                newPlace.setLatitude(Double.parseDouble(item.getLatitude()));
                newPlace.setLongitude(Double.parseDouble(item.getLongitude()));
            } catch (Exception e) { 
                newPlace.setLatitude(0.0);
                newPlace.setLongitude(0.0);
            }
            
            String apiContentTypeId = item.getContentTypeId(); // "12", "15", "39"
            long dbPlaceTypeId;
            
            switch (apiContentTypeId) {
                case "12": dbPlaceTypeId = 1L; break; // '관광지'
                case "15": dbPlaceTypeId = 2L; break; // '축제/행사'
                case "39": dbPlaceTypeId = 3L; break; // '음식점'
                default:   dbPlaceTypeId = 1L; 
            }
            
            newPlace.setPlaceTypeId(dbPlaceTypeId);

            // API areacode를 DB location_id로 변환
            long locationId = mapAreaCodeToLocationId(item.getAreaCode());
            newPlace.setPlaceLocationId(locationId);
            
            // 2. (공통정보) tblPlace에 INSERT
            placeMapper.insertPlace(newPlace);
            
            
            // 3. contentTypeId에 따라 분기
            if (apiContentTypeId.equals("12")) {

                TourIntroVO introResponse = tourApiService.getPlaceIntro(contentId, apiContentTypeId);

                TouristSpotDTO spotDetail = new TouristSpotDTO();
                spotDetail.setPlaceId(newPlace.getPlaceId());
                spotDetail.setSpotOverinfo(item.getOverview());

                // ★★★ NULL 안전 검사 추가 ★★★
                if (introResponse != null &&
                    introResponse.getResponse() != null &&
                    introResponse.getResponse().getBody() != null &&
                    introResponse.getResponse().getBody().getItems() != null &&
                    introResponse.getResponse().getBody().getItems().getItem() != null &&
                    !introResponse.getResponse().getBody().getItems().getItem().isEmpty()) {

                    TourIntroVO.Item introItem =
                            introResponse.getResponse().getBody().getItems().getItem().get(0);

                    spotDetail.setAdmissionFee(introItem.getAdmissionFee());
                    spotDetail.setOpeningHours(introItem.getOpeningHours());
                    spotDetail.setContactInfo(introItem.getContactInfo());
                    spotDetail.setParkingInfo(introItem.getParkingInfo());
                    spotDetail.setRestDay(introItem.getRestDay());
                }

                placeMapper.insertTouristSpot(spotDetail);
                System.out.println("[OnDemand] tblTouristSpot INSERT 성공! (placeId: " + newPlace.getPlaceId() + ")");

            } else if (apiContentTypeId.equals("15")) {

                TourIntroEventVO eventResponse = tourApiService.getEventIntro(contentId, apiContentTypeId);

                EventDTO eventDetail = new EventDTO();
                eventDetail.setPlaceId(newPlace.getPlaceId());
                eventDetail.setEventName(newPlace.getName());
                eventDetail.setEventThemeId(1L); // 기본값

                // ★★★ NULL 안전 검사 추가 ★★★
                if (eventResponse != null &&
                    eventResponse.getResponse() != null &&
                    eventResponse.getResponse().getBody() != null &&
                    eventResponse.getResponse().getBody().getItems() != null &&
                    eventResponse.getResponse().getBody().getItems().getItem() != null &&
                    !eventResponse.getResponse().getBody().getItems().getItem().isEmpty()) {

                    TourIntroEventVO.Item introItem =
                            eventResponse.getResponse().getBody().getItems().getItem().get(0);

                    eventDetail.setEventStart(clean(introItem.getEventStart()));
                    eventDetail.setEventEnd(clean(introItem.getEventEnd()));
                    eventDetail.setEventInfo(clean(introItem.getEventInfo())); // 다른 값들도 해주면 더 안전합니다.
                    eventDetail.setEventLink(clean(introItem.getEventLink()));
                }

                placeMapper.insertEvent(eventDetail);
                System.out.println("[OnDemand] tblEvent INSERT 성공! (placeId: " + newPlace.getPlaceId() + ")");

            } else if (apiContentTypeId.equals("39")) {

                TourIntroRestaurantVO introResponse = tourApiService.getRestaurantIntro(contentId, apiContentTypeId);

                RestaurantDTO restDetail = new RestaurantDTO();
                restDetail.setPlaceId(newPlace.getPlaceId());

                // ★★★ NULL 안전 검사 추가 ★★★
                if (introResponse != null &&
                    introResponse.getResponse() != null &&
                    introResponse.getResponse().getBody() != null &&
                    introResponse.getResponse().getBody().getItems() != null &&
                    introResponse.getResponse().getBody().getItems().getItem() != null &&
                    !introResponse.getResponse().getBody().getItems().getItem().isEmpty()) {

                    TourIntroRestaurantVO.Item introItem =
                            introResponse.getResponse().getBody().getItems().getItem().get(0);

                    restDetail.setRestaurantCategory(introItem.getTreatMenu());
                    restDetail.setRestaurantCall(introItem.getContactInfo());
                    restDetail.setRestaurantPrice(introItem.getFirstMenu());
                    restDetail.setRestaurantOpenTime(introItem.getOpeningHours());
                    // closeTime, rating 등은 API에 없으니 그대로 기본값
                }

                placeMapper.insertRestaurant(restDetail);
                System.out.println("[OnDemand] tblRestaurant INSERT 성공! (placeId: " + newPlace.getPlaceId() + ")");
            }

            
            return newPlace;
        }
        
        System.out.println("[OnDemand] DB에 이미 존재함. 기존 정보를 반환합니다.");
        return place;
    }

    
    /**
     * 데이터베이스에서 장소 ID를 기준으로 장소의 상세 정보를 조회합니다.
     *
     * @param placeId 조회할 장소의 고유 ID
     * @return 조회된 장소의 상세 정보 DTO
     */
    @Override
    public PlaceDTO getPlaceDetail(long placeId) {
        return placeMapper.getPlaceDetailById(placeId);
    }
    
    /**
     * Tour API를 사용하여 키워드로 장소를 검색합니다.
     *
     * @param keyword       검색할 키워드
     * @param arrange       정렬 방식 (A=제목순, B=조회순, C=수정일순, D=생성일순)
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    @Override
    public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId) { 
        return tourApiService.searchByKeyword(keyword, arrange, contentTypeId); 
    }
    
    /**
     * 특정 장소에 연결된 모든 해시태그(키워드 이름) 목록을 조회합니다.
     *
     * @param placeId 조회할 장소의 고유 ID
     * @return 해시태그 이름 문자열의 리스트
     */
    @Override
    public List<String> getHashtags(long placeId) {
        return placeMapper.findHashtagsByPlaceId(placeId);
    }

    
    /**
     * 특정 장소에 해시태그(키워드)를 추가합니다.
     * <p>
     * 키워드가 데이터베이스에 존재하지 않으면 새로 생성한 후 장소와 연결합니다.
     * 이미 연결되어 있는 경우 {@link DataIntegrityViolationException}이 발생할 수 있으나,
     * 이는 로그로 처리하고 무시합니다.
     * </p>
     * @param placeId     해시태그를 추가할 장소의 고유 ID
     * @param keywordName 추가할 키워드(해시태그)의 이름
     */
    @Transactional
    @Override
    public void addHashtagToPlace(long placeId, String keywordName) {
        
        KeywordDTO keyword = placeMapper.findKeywordByName(keywordName);
        long keywordId;

        if (keyword == null) {
            KeywordDTO newKeyword = new KeywordDTO();
            newKeyword.setKeywordName(keywordName);
            placeMapper.insertKeyword(newKeyword); 
            keywordId = newKeyword.getKeywordId();
        } else {
            keywordId = keyword.getKeywordId();
        }

        try {
            KeywordLinkDTO link = new KeywordLinkDTO(placeId, keywordId);
            placeMapper.insertKeywordLink(link);
            System.out.println("[Hashtag] '" + keywordName + "' 태그 연결 성공!");
        } catch (DataIntegrityViolationException e) {
            System.out.println("[Hashtag] '" + keywordName + "' 태그는 이미 연결되어 있습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Tour API를 사용하여 특정 지역의 모든 장소를 검색합니다.
     * <p>
     * 지정된 `maxPages`까지 여러 페이지에 걸쳐 데이터를 가져오며,
     * 각 장소의 위경도 유효성을 검사하고 {@link PlaceDTO}로 변환하여 반환합니다.
     * </p>
     * @param locationId    검색할 지역의 ID
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @param rows          한 페이지당 가져올 결과 수
     * @param maxPages      최대 검색할 페이지 수
     * @return 검색된 장소({@link PlaceDTO})의 리스트
     */
    @Override
    public List<PlaceDTO> searchByAreaAll(long locationId, String contentTypeId,
                                          String arrange, int rows, int maxPages) {

        List<PlaceDTO> out = new ArrayList<>();
        int pageNo = 1;

        while (pageNo <= maxPages) {

            TourApiResponseVO res = tourApiService
                    .searchByArea(String.valueOf(locationId), contentTypeId, arrange, pageNo, rows);

            if (res == null || res.getResponse() == null || res.getResponse().getBody() == null)
                break;

            TourApiResponseVO.Body body = res.getResponse().getBody();
            List<TourItemVO> items =
                    (body.getItems() != null && body.getItems().getItem() != null)
                            ? body.getItems().getItem()
                            : java.util.Collections.emptyList();

            if (items.isEmpty()) break;

            for (TourItemVO item : items) {

                // 지도에 찍을 좌표 체크 
                double lat = parseDoubleSafe(item.getLatitude());
                double lon = parseDoubleSafe(item.getLongitude());

                if (lat == 0 || lon == 0) continue;

                // API → DTO 직접 매핑 (DB 저장 X)
                PlaceDTO dto = new PlaceDTO();
                dto.setPlaceApiId(item.getContentId());
                dto.setName(item.getTitle());
                dto.setAddress(item.getAddress());
                dto.setLatitude(lat);
                dto.setLongitude(lon);
                dto.setPlaceMainImageUrl(item.getFirstImage());

                out.add(dto);
            }

            // 페이지 종료 조건
            if (items.size() < rows) break;

            pageNo++;
        }

        return out;
    }

    /**
     * 문자열 값을 안전하게 double 타입으로 변환하는 헬퍼 함수입니다.
     * 변환 중 {@link NumberFormatException} 등 오류가 발생하면 0.0을 반환합니다.
     *
     * @param val double로 변환할 문자열
     * @return 변환된 double 값, 또는 변환 실패 시 0.0
     */
    private double parseDoubleSafe(String val) {
        try { return Double.parseDouble(val); }
        catch (Exception e) { return 0.0; }
    }

    /**
     * 특정 장소({@link PlaceDTO})를 기준으로 유사한 장소들을 추천합니다.
     * <p>
     * 데이터베이스에서 추천 장소를 조회한 후, 최대 6개의 장소만 반환합니다.
     * </p>
     * @param base 기준이 되는 장소 정보 DTO
     * @return 추천 장소({@link PlaceDTO})의 리스트
     */
    @Override
    public List<PlaceDTO> getRecommendPlaces(PlaceDTO base) {

        List<PlaceDTO> list = placeMapper.findRecommendPlaces(
            base.getPlaceLocationId(),
            base.getPlaceTypeId(),
            base.getPlaceId()
        );

        // 최대 6개만
        if (list.size() > 6) {
            return list.subList(0, 6);
        }
        return list;
    }

    
    

    /**
     * 특정 키워드(해시태그)를 가진 모든 장소의 목록을 조회합니다.
     *
     * @param keywordName 조회할 키워드의 이름
     * @return 해당 키워드를 가진 장소({@link PlaceDTO})의 리스트
     */
    @Override
    public List<PlaceDTO> findPlacesByKeyword(String keywordName) {
        return placeMapper.findPlacesByKeywordName(keywordName);
    }
    
    /**
     * Tour API를 사용하여 특정 날짜와 지역을 기준으로 축제 정보를 검색합니다.
     * <p>
     * 데이터베이스의 `locationId`를 Tour API의 `areaCode`로 변환하여 API를 호출합니다.
     * </p>
     * @param eventStartDate 행사 시작일 (yyyyMMdd 형식)
     * @param arrange        정렬 방식 (A=제목순, B=조회순 등)
     * @param locationId     검색할 지역의 ID (0L이면 전체 지역 검색)
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    @Override
    public TourApiResponseVO searchFestival(String eventStartDate, String arrange, long locationId) {
        // [핵심] locationId를 areaCode로 변환 (0L이면 null 반환)
        String apiAreaCode = mapLocationIdToAreaCode(locationId);
        
        return tourApiService.searchFestival(eventStartDate, arrange, apiAreaCode);
    }
    
    
    /**
     * Tour API를 사용하여 지역 코드를 기반으로 장소를 검색합니다.
     * <p>
     * 데이터베이스의 `locationId`를 Tour API의 `areaCode`로 변환하여 API를 호출합니다.
     * </p>
     * @param locationId    검색할 지역의 ID
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @return Tour API의 원시 응답을 담은 VO 객체
     */
    @Override
    public TourApiResponseVO searchByArea(long locationId, String contentTypeId, String arrange) {
        // 1. DB의 locationId(1L) -> API의 areaCode("1")로 변환
        String apiAreaCode = mapLocationIdToAreaCode(locationId);
        
        // 2. TourApiService 호출
        return tourApiService.searchByArea(apiAreaCode, contentTypeId, arrange);
    }
    
    /**
     * 문자열을 정리하는 헬퍼 함수입니다.
     * <p>
     * 입력 문자열이 null이거나 비어있거나 "false" 문자열인 경우 null을 반환합니다.
     * 그렇지 않으면 앞뒤 공백을 제거한 문자열을 반환합니다.
     * </p>
     * @param s 정리할 문자열
     * @return 정리된 문자열 또는 null
     */
    private String clean(String s) {
        if (s == null || s.trim().isEmpty() || s.trim().equals("false")) {
            return null;
        }
        return s.trim(); // 혹시 모를 공백 제거
    }
    

    
    /**
     * Tour API의 `areaCode`(문자열)를 데이터베이스의 `place_location_id`(long)로 변환하는 헬퍼 함수입니다.
     * <p>
     * 매핑되지 않는 `areaCode`의 경우 기본값으로 서울(1L)을 반환합니다.
     * </p>
     * @param areaCode Tour API의 지역 코드
     * @return 매핑된 데이터베이스 지역 ID
     */
    private long mapAreaCodeToLocationId(String areaCode) {
        if (areaCode == null) return 1L; // 기본값: 서울

        switch (areaCode) {
            case "1":  return 1L;   // 서울
            case "2":  return 7L;   // 인천
            case "3":  return 12L;  // 대전
            case "4":  return 5L;   // 대구
            case "5":  return 14L;  // 광주
            case "6":  return 2L;   // 부산
            case "7":  return 9L;   // 울산
            case "8":  return 15L;  // 세종

            case "31": return 16L;  // 경기
            case "32": return 17L;  // 강원
            case "33": return 18L;  // 충북
            case "34": return 19L;  // 충남
            case "35": return 20L;  // 경북
            case "36": return 21L;  // 경남
            case "37": return 22L;  // 전북
            case "38": return 23L;  // 전남
            case "39": return 3L;   // 제주

            default:   return 1L;   // 기본: 서울
        }
    }
    
    
    /**
     * 데이터베이스의 `place_location_id`(long)를 Tour API의 `areaCode`(문자열)로 변환하는 헬퍼 함수입니다.
     * <p>
     * `locationId`가 0인 경우 null을 반환하며, 매핑되지 않는 `locationId`의 경우 기본값으로 서울("1")을 반환합니다.
     * </p>
     * @param locationId 데이터베이스 지역 ID
     * @return 매핑된 Tour API 지역 코드
     */
    private String mapLocationIdToAreaCode(long locationId) {
    	if (locationId == 0) {
            return null;
        }
    	
        switch ((int) locationId) {
            case 1:  return "1";   // 서울
            case 2:  return "6";   // 부산
            case 3:  return "39";  // 제주
            case 4:  return "32";  // 강릉 (강원)
            case 5:  return "4";   // 대구
            case 6:  return "37";  // 전주 (전북)
            case 7:  return "2";   // 인천
            case 8:  return "38";  // 여수 (전남)
            case 9:  return "7";   // 울산
            case 10: return "31";  // 수원 (경기)
            case 11: return "35";  // 경주 (경북)
            case 12: return "3";   // 대전
            case 13: return "32";  // 춘천 (강원)
            case 14: return "5";   // 광주
            case 15: return "8";   // 세종
            case 16: return "31";  // 경기
            case 17: return "32";  // 강원
            case 18: return "33";  // 충북
            case 19: return "34";  // 충남
            case 20: return "35";  // 경북
            case 21: return "36";  // 경남
            case 22: return "37";  // 전북
            case 23: return "38";  // 전남
            default: return "1";   // 기본값: 서울
        }
    }
}