package com.project.trip.allplace.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourIntroVO;
import com.project.trip.allplace.model.TourItemVO;

@Service
public class TourApiServiceImpl implements TourApiService {

    @Autowired
    private RestTemplate restTemplate;

    // (재발급 받은 새 키를 사용해주세요)
    private final String serviceKey = "4ad9f6404c1b5c50ee33409214a285bd720eab16c791577e2e460245c5f3b7b4"; 
    
    // --- 3개의 API 엔드포인트 ---
    private final String DETAIL_COMMON_URL = "https://apis.data.go.kr/B551011/KorService2/detailCommon2";
    private final String DETAIL_INTRO_URL = "https://apis.data.go.kr/B551011/KorService2/detailIntro2";
    private final String SEARCH_KEYWORD_URL = "https://apis.data.go.kr/B551011/KorService2/searchKeyword2";

    /**
     * 1. (공통정보) API 호출
     * - YN 파라미터가 제거된 최종본
     */
    @Override
    public TourItemVO getPlaceDetail(String contentId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(DETAIL_COMMON_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
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
     * 2. (소개정보) API 호출
     * - contentId와 contentTypeId 사용
     */
    @Override
    public TourIntroVO getPlaceIntro(String contentId, String contentTypeId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(DETAIL_INTRO_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                .queryParam("contentTypeId", contentTypeId) // (필수 파라미터)
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
     * 3. (키워드검색) API 호출
     * - arrange 파라미터 추가
     * - 한글 키워드 인코딩 오류 해결
     */
    @Override
    public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(SEARCH_KEYWORD_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("keyword", keyword) 
                .queryParam("arrange", arrange) // ("A" 대신 파라미터 사용)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("_type", "json")
                
                // (한글 인코딩 오류 해결)
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
}