package com.project.trip.allplace.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

// (모든 DTO import)
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourIntroEventVO;
import com.project.trip.allplace.model.TourIntroRestaurantVO;
import com.project.trip.allplace.model.TourIntroVO;
import com.project.trip.allplace.model.TourItemVO;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class TourApiServiceImpl implements TourApiService {

    @Autowired
    private RestTemplate restTemplate;

    // (재발급 받은 새 키를 사용해주세요. 이 키는 외부에 노출되면 안 됩니다!)
    private final String serviceKey = "4ad9f6404c1b5c50ee33409214a285bd720eab16c791577e2e460245c5f3b7b4"; 
    
  

    //Api 호출
    private final String DETAIL_COMMON_URL = "https://apis.data.go.kr/B551011/KorService2/detailCommon2";
    private final String DETAIL_INTRO_URL = "https://apis.data.go.kr/B551011/KorService2/detailIntro2";
    private final String SEARCH_KEYWORD_URL = "https://apis.data.go.kr/B551011/KorService2/searchKeyword2";
    private final String SEARCH_FESTIVAL_URL = "https://apis.data.go.kr/B551011/KorService2/searchFestival2";
    private final String AREA_BASED_URL = "https://apis.data.go.kr/B551011/KorService2/areaBasedList2";

    /**
     * 1. (공통정보) API 호출
     */
    @Override
    public TourItemVO getPlaceDetail(String contentId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(DETAIL_COMMON_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                // (KorService1 기준 YN 파라미터들 추가)
                .queryParam("defaultYN", "Y")
                //.queryParam("addrinfoYN", "Y")
                .queryParam("mapinfoYN", "Y")
                .queryParam("firstImageYN", "Y")
                .queryParam("overviewYN", "Y") // (overview를 받기 위해 추가)
                .queryParam("_type", "json")
                .build(true) // (serviceKey는 인코딩되면 안 됨)
                .toUri();
        
        try {
            TourApiResponseVO response = restTemplate.getForObject(uri, TourApiResponseVO.class);

            if (response != null && 
                response.getResponse() != null &&
                response.getResponse().getBody() != null &&
                response.getResponse().getBody().getItems() != null &&
                response.getResponse().getBody().getItems().getItem() != null &&
                !response.getResponse().getBody().getItems().getItem().isEmpty()) {
                
                return response.getResponse().getBody().getItems().getItem().get(0); 
            }
            return null; 

        } catch (Exception e) {
            System.err.println("[TourApiServiceImpl] '공통정보(Common)' API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 2. (소개정보 - 관광지) API 호출
     */
    @Override
    public TourIntroVO getPlaceIntro(String contentId, String contentTypeId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(DETAIL_INTRO_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                .queryParam("contentTypeId", contentTypeId) // (필수 파라미터 "12")
                .queryParam("_type", "json")
                .build(true) // (serviceKey는 인코딩되면 안 됨)
                .toUri();
        
        try {
            TourIntroVO response = restTemplate.getForObject(uri, TourIntroVO.class);

            if (response != null && 
                response.getResponse() != null &&
                response.getResponse().getBody() != null &&
                response.getResponse().getBody().getItems() != null &&
                response.getResponse().getBody().getItems().getItem() != null &&
                !response.getResponse().getBody().getItems().getItem().isEmpty()) {
                
                return response; 
            }
            return null;

        } catch (Exception e) {
            System.err.println("[TourApiServiceImpl] '소개정보(Intro)' API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 2-1. (소개정보 - 축제/행사) API 호출
     */
    @Override
    public TourIntroEventVO getEventIntro(String contentId, String contentTypeId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(DETAIL_INTRO_URL) // (관광지 소개정보와 URL 동일)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                .queryParam("contentTypeId", contentTypeId) // (필수 파라미터 "15")
                .queryParam("_type", "json")
                .build(true) 
                .toUri();
        
        try {
            // [핵심] 반환 DTO를 TourIntroEventVO.class로 변경
            TourIntroEventVO response = restTemplate.getForObject(uri, TourIntroEventVO.class);

            if (response != null && 
                response.getResponse() != null &&
                response.getResponse().getBody() != null &&
                response.getResponse().getBody().getItems() != null &&
                response.getResponse().getBody().getItems().getItem() != null &&
                !response.getResponse().getBody().getItems().getItem().isEmpty()) {
                
                return response; 
            }
            return null;

        } catch (Exception e) {
            System.err.println("[TourApiServiceImpl] '소개정보(Event)' API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public TourIntroRestaurantVO getRestaurantIntro(String contentId, String contentTypeId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(DETAIL_INTRO_URL) // (URL은 동일)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                .queryParam("contentTypeId", contentTypeId) // (39 전달)
                .queryParam("_type", "json")
                .build(true) 
                .toUri();
        
        try {
            // [핵심] 반환 VO 클래스 변경
            TourIntroRestaurantVO response = restTemplate.getForObject(uri, TourIntroRestaurantVO.class);
            
            if (response != null && 
                response.getResponse().getBody().getItems().getItem() != null &&
                !response.getResponse().getBody().getItems().getItem().isEmpty()) {
                return response; 
            }
            return null;
        } catch (Exception e) {
            System.err.println("[TourApiServiceImpl] '소개정보(Restaurant 39)' API 호출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }

    /**
     * 3. (키워드검색) API 호출
     */
    @Override
    public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(SEARCH_KEYWORD_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("keyword", keyword) 
                .queryParam("arrange", arrange) 
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("_type", "json")
                .build()     
                .encode()    
                .toUri();
        
        try {
            TourApiResponseVO response = restTemplate.getForObject(uri, TourApiResponseVO.class);

            if (response != null && 
                response.getResponse() != null &&
                response.getResponse().getBody() != null &&
                response.getResponse().getBody().getItems() != null) {
                
                return response;
            }
            return null;

        } catch (Exception e) {
            System.err.println("[TourApiServiceImpl] '키워드 검색(Search)' API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 4. (축제검색) API 호출
     */
    @Override
    public TourApiResponseVO searchFestival(String eventStartDate, String arrange) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(SEARCH_FESTIVAL_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("eventStartDate", eventStartDate)
                .queryParam("arrange", arrange) // ("A" -> arrange)
                .queryParam("_type", "json")
                .build()     
                .encode()    
                .toUri();
        try {
            TourApiResponseVO response = restTemplate.getForObject(uri, TourApiResponseVO.class);
            if (response != null && 
                response.getResponse() != null && 
                response.getResponse().getBody() != null) {
                return response;
            }
            return null;
        } catch (Exception e) {
            log.error("[TourApiServiceImpl] '축제 검색(SearchFestival)' API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    
    @Override
    public TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(AREA_BASED_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("arrange", arrange) // ("A" -> arrange)
                .queryParam("areaCode", areaCode) 
                .queryParam("contentTypeId", contentTypeId) 
                .queryParam("_type", "json")
                .build()     
                .encode()    
                .toUri();
        try {
            TourApiResponseVO response = restTemplate.getForObject(uri, TourApiResponseVO.class);
            if (response != null && 
                response.getResponse() != null && 
                response.getResponse().getBody() != null) {
                return response;
            }
            return null;
            
        } catch (Exception e) {
            log.error("[TourApiServiceImpl] '지역기반 검색(areaBasedList)' API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    
    
}