package com.project.trip.allplace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.allplace.mapper.PlaceMapper;
import com.project.trip.allplace.model.KeywordDTO;
import com.project.trip.allplace.model.KeywordLinkDTO;
import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
// [추가] TourIntroVO import
import com.project.trip.allplace.model.TourIntroVO;
import com.project.trip.allplace.model.TourItemVO;
import com.project.trip.allplace.model.TouristSpotDTO;

@Service
public class AllPlaceServiceImpl implements AllPlaceService {

    @Autowired
    private PlaceMapper placeMapper;

    @Autowired
    private TourApiService tourApiService;

    @Transactional
    @Override
    public PlaceDTO addPlaceOnDemand(String contentId) {
        
        PlaceDTO place = placeMapper.findPlaceByContentId(contentId);

        if (place == null) {
            System.out.println("[OnDemand] DB에 없음. TourAPI에서 상세정보를 가져옵니다...");
            
            // --- [수정] ---
            // 1. (공통정보) API 호출
            TourItemVO item = tourApiService.getPlaceDetail(contentId);

            if (item != null && item.getContentId() != null) {
                
                PlaceDTO newPlace = new PlaceDTO();
                newPlace.setPlaceApiId(item.getContentId());
                newPlace.setName(item.getTitle());
                newPlace.setAddress(item.getAddress());
                newPlace.setPlaceMainImageUrl(item.getFirstImage());

                try { /* ... (좌표 변환) ... */ 
                    newPlace.setLatitude(Double.parseDouble(item.getLatitude()));
                    newPlace.setLongitude(Double.parseDouble(item.getLongitude()));
                } catch (Exception e) { 
                    newPlace.setLatitude(0.0);
                    newPlace.setLongitude(0.0);
                }
                
                String apiContentTypeId = item.getContentTypeId(); // "12"
                long dbPlaceTypeId;
                switch (apiContentTypeId) {
                    case "12": dbPlaceTypeId = 1L; break; // '관광지'
                    default:   dbPlaceTypeId = 1L; // (임시)
                }
                newPlace.setPlaceTypeId(dbPlaceTypeId);

                try { /* ... (Location ID 매핑) ... */ 
                    newPlace.setPlaceLocationId(Long.parseLong(item.getAreaCode()));
                } catch (Exception e) { 
                    newPlace.setPlaceLocationId(1); // '서울'로 임시 설정
                }
                
                // 2. (공통정보) tblPlace에 INSERT
                placeMapper.insertPlace(newPlace);
                
                // 3. (신규) 관광지(12)일 경우, '소개정보' API 추가 호출
                if (apiContentTypeId.equals("12")) {
                    
                    // 3-1. (소개정보) API 호출
                    TourIntroVO introResponse = tourApiService.getPlaceIntro(contentId, apiContentTypeId);
                    
                    TouristSpotDTO spotDetail = new TouristSpotDTO();
                    spotDetail.setPlaceId(newPlace.getPlaceId()); 
                    
                    // 3-2. '공통정보(item)'에서 CLOB 데이터 가져오기
                    spotDetail.setSpotOverinfo(item.getOverview()); 

                    // 3-3. '소개정보(introResponse)'에서 나머지 상세정보 가져오기
                    if (introResponse != null && 
                        introResponse.getResponse().getBody().getItems().getItem() != null &&
                        !introResponse.getResponse().getBody().getItems().getItem().isEmpty()) {
                        
                        // 소개정보 API는 item이 List이므로 0번째를 꺼냄
                        TourIntroVO.Item introItem = introResponse.getResponse().getBody().getItems().getItem().get(0);
                        
                        spotDetail.setAdmissionFee(introItem.getAdmissionFee());
                        spotDetail.setOpeningHours(introItem.getOpeningHours());
                        spotDetail.setContactInfo(introItem.getContactInfo());
                        spotDetail.setParkingInfo(introItem.getParkingInfo());
                        spotDetail.setRestDay(introItem.getRestDay());
                    }
                    
                    // 3-4. (상세정보) tblTouristSpot에 INSERT
                    placeMapper.insertTouristSpot(spotDetail);
                }
                // --- [끝] ---
                
                return newPlace;
            } else {
                System.err.println("[OnDemand] '공통정보' API 호출 실패 (contentId: " + contentId + ")");
                return null; 
            }
        }
        
        System.out.println("[OnDemand] DB에 이미 존재함. 기존 정보를 반환합니다.");
        return place;
    }

    // (기존) 상세정보 조회
    @Override
    public PlaceDTO getPlaceDetail(long placeId) {
        return placeMapper.getPlaceDetailById(placeId);
    }
    
    /**
     * API에서 키워드로 장소 목록 검색
     * (DB 저장 X, 단순 API 호출 전달)
     */
    @Override
    public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId) { // (파라미터 추가)
        return tourApiService.searchByKeyword(keyword, arrange, contentTypeId); // (파라미터 전달)
    }
    
    /**
     * 장소에 해시태그(키워드)를 추가(연결)합니다.
     * (Get or Create 로직)
     */
    @Transactional
    @Override
    public void addHashtagToPlace(long placeId, String keywordName) {
        
        // 1. 키워드 이름(예: "데이트")으로 tblKeyword 조회
        KeywordDTO keyword = placeMapper.findKeywordByName(keywordName);
        long keywordId;

        if (keyword == null) {
            // 2. (없으면) tblKeyword에 "데이트" INSERT
            KeywordDTO newKeyword = new KeywordDTO();
            newKeyword.setKeywordName(keywordName);
            placeMapper.insertKeyword(newKeyword); // <selectKey>로 newKeyword.keywordId가 채워짐
            keywordId = newKeyword.getKeywordId();
        } else {
            // 3. (있으면) 기존 keyword_id 사용
            keywordId = keyword.getKeywordId();
        }

        // 4. tblKeywordLink에 장소(placeId)와 키워드(keywordId)를 연결
        try {
            KeywordLinkDTO link = new KeywordLinkDTO(placeId, keywordId);
            placeMapper.insertKeywordLink(link);
            System.out.println("[Hashtag] '" + keywordName + "' 태그 연결 성공!");
        } catch (DataIntegrityViolationException e) {
            // (PK/UK 중복 오류) 이미 연결된 태그는 무시
            System.out.println("[Hashtag] '" + keywordName + "' 태그는 이미 연결되어 있습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 해시태그 이름으로 장소 목록을 검색합니다. (JOIN)
     */
    @Override
    public List<PlaceDTO> findPlacesByKeyword(String keywordName) {
        return placeMapper.findPlacesByKeywordName(keywordName);
    }
}