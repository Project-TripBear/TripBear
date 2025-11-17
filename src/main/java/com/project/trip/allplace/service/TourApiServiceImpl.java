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
   
    
    /**
     * Tour API의 '상세 정보 조회(detailCommon2)'를 사용하여 특정 장소의 상세 정보를 조회합니다.
     *
     * @param contentId 조회할 콘텐츠 ID
     * @return 조회된 장소의 상세 정보 {@link TourItemVO}, 조회 실패 시 null
     */
    @Override
    public TourItemVO getPlaceDetail(String contentId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(DETAIL_COMMON_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                .queryParam("_type", "json")
                .build(true).toUri();
        log.info("[DETAIL] 요청 URL = " + uri.toString());
        try {
            TourApiResponseVO r = restTemplate.getForObject(uri, TourApiResponseVO.class);
            if (r != null &&
                r.getResponse() != null &&
                r.getResponse().getBody() != null &&
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
    

    /**
     * Tour API의 '키워드 검색(searchKeyword2)'을 사용하여 키워드로 장소를 검색합니다.
     *
     * @param keyword       검색할 키워드
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @return Tour API의 원시 응답을 담은 {@link TourApiResponseVO} 객체
     */
    @Override
    public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SEARCH_KEYWORD_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("keyword", keyword)
                .queryParam("arrange", arrange)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("numOfRows", 100) // <-- 이 줄 추가
                .queryParam("pageNo", 1)       // <-- 이 줄 추가
                .queryParam("_type", "json")
                .build(false).toUri();

        return restTemplate.getForObject(uri, TourApiResponseVO.class);
    }

    /**
     * Tour API의 '행사 정보 조회(searchFestival2)'를 사용하여 특정 날짜와 지역을 기준으로 축제 정보를 검색합니다.
     *
     * @param eventStartDate 행사 시작일 (yyyyMMdd 형식)
     * @param arrange        정렬 방식 (A=제목순, B=조회순 등)
     * @param areaCode       검색할 지역 코드 (null 또는 빈 문자열이면 전체 지역)
     * @return Tour API의 원시 응답을 담은 {@link TourApiResponseVO} 객체
     */
    @Override
    public TourApiResponseVO searchFestival(String eventStartDate, String arrange, String areaCode) {
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SEARCH_FESTIVAL_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("eventStartDate", eventStartDate)
                .queryParam("arrange", arrange)
                .queryParam("numOfRows", 100) // 100개 가져오기
                .queryParam("pageNo", 1)
                .queryParam("_type", "json");

        // [핵심] areaCode가 null이 아니거나 비어있지 않으면 파라미터로 추가
        if (areaCode != null && !areaCode.isEmpty()) {
            builder.queryParam("areaCode", areaCode);
        }

        URI uri = builder.build(true).toUri();
        return restTemplate.getForObject(uri, TourApiResponseVO.class);
    }

    /**
     * Tour API의 '지역 기반 정보 조회(areaBasedList2)'를 사용하여 지역 코드를 기반으로 장소를 검색합니다.
     * 이 버전은 페이지네이션 정보 없이 기본값(pageNo=1, numOfRows=100)으로 호출합니다.
     *
     * @param areaCode      검색할 지역 코드
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @return Tour API의 원시 응답을 담은 {@link TourApiResponseVO} 객체
     */
    @Override
    public TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange) {
        // 파라미터 5개짜리 메서드를 기본값(1, 100)으로 호출
        return searchByArea(areaCode, contentTypeId, arrange, 1, 100);
    }
    // --- [여기까지] ---

    /**
     * Tour API의 '지역 기반 정보 조회(areaBasedList2)'를 사용하여 지역 코드를 기반으로 장소를 검색합니다.
     * 페이지네이션 정보를 포함하여 결과를 반환합니다.
     *
     * @param areaCode      검색할 지역 코드 (null 또는 빈 문자열이면 전체 지역)
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @param pageNo        요청할 페이지 번호
     * @param rows          한 페이지당 가져올 결과 수
     * @return Tour API의 원시 응답을 담은 {@link TourApiResponseVO} 객체
     */
    @Override
    public TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange, int pageNo, int rows) {

        // 1. 'UriComponentsBuilder' 타입의 'builder' 변수로 선언합니다.
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(AREA_BASED_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")
                .queryParam("MobileOS", "ETC")
                .queryParam("arrange", arrange)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", rows)
                .queryParam("_type", "json");
        
        // 2. areaCode가 null이나 빈 값이 아닐 때만 builder에 파라미터를 추가합니다.
        if (areaCode != null && !areaCode.isEmpty()) {
            builder.queryParam("areaCode", areaCode);
        }

        // 3. 최종 URI 빌드
        URI uri = builder.build(true).toUri(); // 'uri' 변수는 여기서 한 번만 선언합니다.

        return restTemplate.getForObject(uri, TourApiResponseVO.class);
    }

    /**
     * Tour API를 사용하여 특정 지역의 모든 장소 정보를 원시({@link TourItemVO}) 형태로 검색합니다.
     * <p>
     * 지정된 `maxPages`까지 여러 페이지에 걸쳐 데이터를 가져오며,
     * 각 페이지의 결과를 합산하여 반환합니다.
     * </p>
     * @param areaCode      검색할 지역 코드
     * @param contentTypeId 검색할 콘텐츠 타입 ID
     * @param arrange       정렬 방식 (A=제목순, B=조회순 등)
     * @param rows          한 페이지당 가져올 결과 수
     * @param maxPages      최대 검색할 페이지 수
     * @return 검색된 장소 아이템({@link TourItemVO})의 리스트
     */
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
    
    /**
     * Tour API의 '위치 기반 정보 조회(locationBasedList2)'를 사용하여
     * 특정 위치(위도, 경도)와 반경 내의 장소를 검색합니다.
     * <p>
     * 여러 콘텐츠 타입에 대해 검색을 수행하고 결과를 병합하여 반환합니다.
     * </p>
     * @param lat           중심 위도
     * @param lng           중심 경도
     * @param radius        검색 반경 (미터 단위)
     * @param contentTypeId 검색할 콘텐츠 타입 ID (콤마로 구분된 여러 타입 가능)
     * @return Tour API의 원시 응답을 담은 {@link TourApiResponseVO} 객체
     */
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
    
    /**
     * Tour API의 소개 정보 조회 URI를 생성하는 헬퍼 함수입니다.
     *
     * @param baseUrl       기본 URL (예: DETAIL_INTRO_URL)
     * @param contentId     조회할 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID
     * @return 생성된 {@link URI} 객체
     */
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


    /**
     * Tour API의 '소개 정보 조회(detailIntro2)'를 사용하여 특정 장소의 소개 정보를 조회합니다.
     * (주로 관광지(contentTypeId=12)의 상세 소개 정보)
     *
     * @param contentId     조회할 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID
     * @return 조회된 장소의 소개 정보 {@link TourIntroVO}, 조회 실패 시 null
     */
    @Override
    public TourIntroVO getPlaceIntro(String contentId, String contentTypeId) {
        // (contentTypeId=12, 관광지)
        URI uri = buildIntroUri(DETAIL_INTRO_URL, contentId, contentTypeId);
        
        // 🚨 이 로그를 추가해야 디버깅이 가능합니다!
        log.info("[INTRO] 요청 URL = " + uri.toString()); 
        
        try {
            return restTemplate.getForObject(uri, TourIntroVO.class);
        } catch (Exception e) {
            log.error("[TourApi] getPlaceIntro 호출 오류: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tour API의 '소개 정보 조회(detailIntro2)'를 사용하여 특정 축제/행사의 소개 정보를 조회합니다.
     * (주로 축제/행사(contentTypeId=15)의 상세 소개 정보)
     *
     * @param contentId     조회할 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID
     * @return 조회된 축제/행사의 소개 정보 {@link TourIntroEventVO}, 조회 실패 시 null
     */
    /**
     * Tour API의 '소개 정보 조회(detailIntro2)'를 사용하여 특정 축제/행사의 소개 정보를 조회합니다.
     * (주로 축제/행사(contentTypeId=15)의 상세 소개 정보)
     *
     * @param contentId     조회할 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID
     * @return 조회된 축제/행사의 소개 정보 {@link TourIntroEventVO}, 조회 실패 시 null
     */
    @Override
    public TourIntroEventVO getEventIntro(String contentId, String contentTypeId) {
        // (contentTypeId=15, 행사)
        
        // [★핵심★]
        // 이 부분이 DETAIL_INTRO_URL이 아닌 "DETAIL_EVENT_URL"을 사용해야 합니다.
        URI uri = buildIntroUri(DETAIL_INTRO_URL, contentId, contentTypeId);
        
         try {
            return restTemplate.getForObject(uri, TourIntroEventVO.class);
        } catch (Exception e) {
            log.error("[TourApi] getEventIntro 호출 오류: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tour API의 '소개 정보 조회(detailIntro2)'를 사용하여 특정 음식점의 소개 정보를 조회합니다.
     * (주로 음식점(contentTypeId=39)의 상세 소개 정보)
     *
     * @param contentId     조회할 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID
     * @return 조회된 음식점의 소개 정보 {@link TourIntroRestaurantVO}, 조회 실패 시 null
     */
    /**
     * Tour API의 '소개 정보 조회(detailIntro2)'를 사용하여 특정 음식점의 소개 정보를 조회합니다.
     * (주로 음식점(contentTypeId=39)의 상세 소개 정보)
     *
     * @param contentId     조회할 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID
     * @return 조회된 음식점의 소개 정보 {@link TourIntroRestaurantVO}, 조회 실패 시 null
     */
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