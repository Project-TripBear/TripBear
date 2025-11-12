package com.project.trip.allplace.service;

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
     * (핵심 기능) API Item을 DB에 저장 (관광지/축제/음식점 분기)
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

                if (introResponse != null && 
                    introResponse.getResponse().getBody().getItems().getItem() != null &&
                    !introResponse.getResponse().getBody().getItems().getItem().isEmpty()) {
                    
                    TourIntroVO.Item introItem = introResponse.getResponse().getBody().getItems().getItem().get(0);
                    
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
                eventDetail.setEventThemeId(1L); // (1=기타)

                if (eventResponse != null && 
                    eventResponse.getResponse().getBody().getItems().getItem() != null &&
                    !eventResponse.getResponse().getBody().getItems().getItem().isEmpty()) {
                    
                    TourIntroEventVO.Item introItem = eventResponse.getResponse().getBody().getItems().getItem().get(0);
                    
                    eventDetail.setEventStart(introItem.getEventStart());
                    eventDetail.setEventEnd(introItem.getEventEnd());
                    eventDetail.setEventInfo(introItem.getEventInfo());
                    eventDetail.setEventLink(introItem.getEventLink());
                }
                
                placeMapper.insertEvent(eventDetail);
                System.out.println("[OnDemand] tblEvent INSERT 성공! (placeId: " + newPlace.getPlaceId() + ")");
            
            } else if (apiContentTypeId.equals("39")) {
                
                TourIntroRestaurantVO introResponse = tourApiService.getRestaurantIntro(contentId, apiContentTypeId);
                
                RestaurantDTO restDetail = new RestaurantDTO();
                restDetail.setPlaceId(newPlace.getPlaceId());
                
                // (ERD 기반 DTO 필드 매핑)
                if (introResponse != null && 
                    introResponse.getResponse().getBody().getItems().getItem() != null &&
                    !introResponse.getResponse().getBody().getItems().getItem().isEmpty()) {
                    
                    TourIntroRestaurantVO.Item introItem = introResponse.getResponse().getBody().getItems().getItem().get(0);
                    
                    // API(VO) -> DTO(ERD)
                    restDetail.setRestaurantCategory(introItem.getTreatMenu()); 
                    restDetail.setRestaurantCall(introItem.getContactInfo());
                    restDetail.setRestaurantPrice(introItem.getFirstMenu());
                    restDetail.setRestaurantOpenTime(introItem.getOpeningHours());
                    // (rating, count, close_time은 API에 없으므로 DTO 기본값(null, 0.0) 사용)
                }
                
                placeMapper.insertRestaurant(restDetail);
                System.out.println("[OnDemand] tblRestaurant INSERT 성공! (placeId: " + newPlace.getPlaceId() + ")");
            }
            
            return newPlace;
        }
        
        System.out.println("[OnDemand] DB에 이미 존재함. 기존 정보를 반환합니다.");
        return place;
    }

    
    @Override
    public PlaceDTO getPlaceDetail(long placeId) {
        return placeMapper.getPlaceDetailById(placeId);
    }
    
    @Override
    public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId) { 
        return tourApiService.searchByKeyword(keyword, arrange, contentTypeId); 
    }
    
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
    
    
    
    
    

    @Override
    public List<PlaceDTO> findPlacesByKeyword(String keywordName) {
        return placeMapper.findPlacesByKeywordName(keywordName);
    }
    
    // (arrange 파라미터가 있는 버전)
    @Override
    public TourApiResponseVO searchFestival(String eventStartDate, String arrange) {
        return tourApiService.searchFestival(eventStartDate, arrange);
    }
    
    
    /**
     * DB의 locationId를 기반으로 지역 검색을 수행합니다.
     */
    // (arrange 파라미터가 있는 버전)
    @Override
    public TourApiResponseVO searchByArea(long locationId, String contentTypeId, String arrange) {
        // 1. DB의 locationId(1L) -> API의 areaCode("1")로 변환
        String apiAreaCode = mapLocationIdToAreaCode(locationId);
        
        // 2. TourApiService 호출
        return tourApiService.searchByArea(apiAreaCode, contentTypeId, arrange);
    }
    

    
    /**
     * (헬퍼1) TourAPI의 areacode(String)를 DB의 place_location_id(long)로 변환합니다.
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
    
    
    // --- [이 메서드가 오류의 원인이었습니다] ---
    /**
     * (헬퍼2) DB의 place_location_id(long)를 TourAPI의 areacode(String)로 변환합니다.
     */
    private String mapLocationIdToAreaCode(long locationId) {
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
    // --- [여기까지] ---
}