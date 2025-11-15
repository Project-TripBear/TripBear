package com.project.trip.allplace.service;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trip.allplace.model.TourApiResponseVO;
// (필요한 VO Import 추가)
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
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String serviceKey = "4ad9f6404c1b5c50ee33409214a285bd720eab16c791577e2e460245c5f3b7b4";

    private final String DETAIL_COMMON_URL = "https://apis.data.go.kr/B551011/KorService2/detailCommon2";
    private final String SEARCH_KEYWORD_URL = "https://apis.data.go.kr/B551011/KorService2/searchKeyword2";
    private final String SEARCH_FESTIVAL_URL = "https://apis.data.go.kr/B551011/KorService2/searchFestival2";
    private final String AREA_BASED_URL = "https://apis.data.go.kr/B551011/KorService2/areaBasedList2";
    private final String LOCATION_BASED_URL = "https://apis.data.go.kr/B551011/KorService2/locationBasedList2";

    private final String DETAIL_INTRO_URL = "https://apis.data.go.kr/B551011/KorService2/detailIntro2";
  
    private final String DETAIL_EVENT_URL = "https://apis.data.go.kr/B551011/KorService2/detailEvent2";
   
    
    @Override
    public TourItemVO getPlaceDetail(String contentId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(DETAIL_COMMON_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                .queryParam("defaultYN", "Y")
                .queryParam("mapinfoYN", "Y")
                .queryParam("firstImageYN", "Y")
                .queryParam("overviewYN", "Y")
                .queryParam("_type", "json")
                .build(true).toUri();

        try {
            TourApiResponseVO r = restTemplate.getForObject(uri, TourApiResponseVO.class);
            if (r != null &&
                    r.getResponse().getBody().getItems() != null &&
                    r.getResponse().getBody().getItems().getItem() != null &&
                    !r.getResponse().getBody().getItems().getItem().isEmpty()) {
                return r.getResponse().getBody().getItems().getItem().get(0);
            }
        } catch (Exception e) {
            log.error("Detail API error", e);
        }
        return null;
    }

    @Override
    public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SEARCH_KEYWORD_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("keyword", keyword)
                .queryParam("arrange", arrange)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("_type", "json")
                .build(true).toUri();

        return restTemplate.getForObject(uri, TourApiResponseVO.class);
    }

    @Override
    public TourApiResponseVO searchFestival(String eventStartDate, String arrange) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SEARCH_FESTIVAL_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("eventStartDate", eventStartDate)
                .queryParam("arrange", arrange)
                .queryParam("_type", "json")
                .build(true).toUri();

        return restTemplate.getForObject(uri, TourApiResponseVO.class);
    }

    // --- [신규 추가] (파라미터 3개 버전) ---
    // AllPlaceServiceImpl의 282라인에서 호출
    @Override
    public TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange) {
        // 파라미터 5개짜리 메서드를 기본값(1, 100)으로 호출
        return searchByArea(areaCode, contentTypeId, arrange, 1, 100);
    }
    // --- [여기까지] ---

    // (기존) 파라미터 5개 버전
    @Override
    public TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange, int pageNo, int rows) {

        URI uri = UriComponentsBuilder.fromHttpUrl(AREA_BASED_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("arrange", arrange)
                .queryParam("areaCode", areaCode)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", rows)
                .queryParam("_type", "json")
                .build(true).toUri();

        return restTemplate.getForObject(uri, TourApiResponseVO.class);
    }

    @Override
    public List<TourItemVO> searchByAreaAllRaw(String areaCode, String contentTypeId, String arrange, int rows,
            int maxPages) {

        List<TourItemVO> out = new ArrayList<>();
        int page = 1;

        while (page <= maxPages) {

            TourApiResponseVO res = searchByArea(areaCode, contentTypeId, arrange, page, rows);

            if (res == null ||
                    res.getResponse() == null ||
                    res.getResponse().getBody() == null ||
                    res.getResponse().getBody().getItems() == null ||
                    res.getResponse().getBody().getItems().getItem() == null) {
                break;
            }

            List<TourItemVO> list = res.getResponse().getBody().getItems().getItem();
            if (list.isEmpty()) break;

            out.addAll(list);

            // 마지막 페이지 판단
            if (list.size() < rows) break;

            page++;
        }

        return out;
    }
    
    @Override
    public TourApiResponseVO searchByLocation(String lat, String lng, String radius, String contentTypeId) {

        // 1) "12,39" → ["12", "39"]
        String[] types = contentTypeId.split(",");

        // 병합할 item 리스트
        List<TourItemVO> mergedItems = new java.util.ArrayList<>();

        Integer totalCount = 0;  // 합산용
        Integer pageNo = 1;
        Integer numOfRows = 1000;  // 요청값 고정

        for (String type : types) {

            URI uri = UriComponentsBuilder
                    .fromHttpUrl(LOCATION_BASED_URL)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("MobileApp", "TripBear")
                    .queryParam("MobileOS", "ETC")
                    .queryParam("_type", "json")
                    .queryParam("mapY", lat)
                    .queryParam("mapX", lng)
                    .queryParam("radius", radius)
                    .queryParam("contentTypeId", type.trim()) // ← ★ 단일 값만 넣기
                    .queryParam("arrange", "A")
                    .queryParam("numOfRows", numOfRows)
                    .queryParam("pageNo", pageNo)
                    .build(true)
                    .toUri();

            try {
                TourApiResponseVO resp = restTemplate.getForObject(uri, TourApiResponseVO.class);

                if (resp != null &&
                    resp.getResponse() != null &&
                    resp.getResponse().getBody() != null &&
                    resp.getResponse().getBody().getItems() != null &&
                    resp.getResponse().getBody().getItems().getItem() != null) {

                    mergedItems.addAll(resp.getResponse().getBody().getItems().getItem());

                    // totalCount는 그냥 합산 또는 최대값을 사용 (여기선 합산)
                    if (resp.getResponse().getBody().getTotalCount() != null)
                        totalCount += resp.getResponse().getBody().getTotalCount();
                }

            } catch (Exception e) {
            	log.error("에러 발생: " + e.getMessage());
            }
        }

        // ▶▶ 이제 mergedItems(list)를 이용해 TourApiResponseVO 객체를 재구성해야 한다.

        TourApiResponseVO.Items itemsWrapper = new TourApiResponseVO.Items();
        itemsWrapper.setItem(mergedItems);

        TourApiResponseVO.Body body = new TourApiResponseVO.Body();
        body.setItems(itemsWrapper);
        body.setTotalCount(totalCount);
        body.setPageNo(pageNo);
        body.setNumOfRows(numOfRows);

        TourApiResponseVO.Response response = new TourApiResponseVO.Response();
        response.setBody(body);

        TourApiResponseVO finalVO = new TourApiResponseVO();
        finalVO.setResponse(response);

        return finalVO;
    }
    
   

    
    // --- [신규 추가] ---
    // AllPlaceServiceImpl에서 호출하는 3개의 Intro API 구현
    
    private URI buildIntroUri(String baseUrl, String contentId, String contentTypeId) {
        return UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("_type", "json")
                .queryParam("contentId", contentId)
                .queryParam("contentTypeId", contentTypeId)
                .build(true).toUri();
    }

    @Override
    public TourIntroVO getPlaceIntro(String contentId, String contentTypeId) {
        // (contentTypeId=12, 관광지)
        URI uri = buildIntroUri(DETAIL_INTRO_URL, contentId, contentTypeId);
        try {
            return restTemplate.getForObject(uri, TourIntroVO.class);
        } catch (Exception e) {
            log.error("[TourApi] getPlaceIntro 호출 오류: " + e.getMessage());
            return null;
        }
    }

    @Override
    public TourIntroEventVO getEventIntro(String contentId, String contentTypeId) {
        // (contentTypeId=15, 행사)
        URI uri = buildIntroUri(DETAIL_EVENT_URL, contentId, contentTypeId);
         try {
            return restTemplate.getForObject(uri, TourIntroEventVO.class);
        } catch (Exception e) {
            log.error("[TourApi] getEventIntro 호출 오류: " + e.getMessage());
            return null;
        }
    }

    @Override
    public TourIntroRestaurantVO getRestaurantIntro(String contentId, String contentTypeId) {
        // (contentTypeId=39, 음식점)
        URI uri = buildIntroUri(DETAIL_INTRO_URL, contentId, contentTypeId);
         try {
            return restTemplate.getForObject(uri, TourIntroRestaurantVO.class);
        } catch (Exception e) {
            log.error("[TourApi] getRestaurantIntro 호출 오류: " + e.getMessage());
            return null;
        }
    }
}