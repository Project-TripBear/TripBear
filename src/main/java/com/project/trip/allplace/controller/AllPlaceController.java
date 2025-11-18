package com.project.trip.allplace.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// (ResponseEntity, HttpStatus, ResponseBody, KrWeatherService 등 REST 관련 import 제거)

import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourItemVO;
import com.project.trip.allplace.service.AllPlaceService;
import com.project.trip.allplace.service.TourApiService;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/allplace")
public class AllPlaceController {

    @Autowired
    private AllPlaceService allPlaceService;

    // @Autowired
    // private KrWeatherService weatherService; // (REST 컨트롤러로 이동)

    @Autowired
    private TourApiService tourApiService;

    
    /**
     * 키워드를 사용하여 장소를 검색하고 결과를 뷰에 전달합니다.
     *
     * @param keyword       검색할 키워드
     * @param contentTypeId 콘텐츠 타입 ID (기본값: 12 - 관광지)
     * @param arrange       정렬 방식 (기본값: A - 제목순)
     * @param model         뷰에 데이터를 전달하기 위한 Model 객체
     * @return "allplace.search_result" 검색 결과 뷰 이름
     */
    @GetMapping("/search")
    public String searchByKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "contentTypeId", defaultValue = "12") String contentTypeId,
            @RequestParam(value = "arrange", defaultValue = "A") String arrange,
            Model model) {

        log.info("[Controller] 키워드 검색 (API Only): " + keyword);
        TourApiResponseVO apiResponse = allPlaceService.searchByKeyword(keyword, arrange, contentTypeId);
        
        List<PlaceDTO> placeList = convertApiItemsToDtoList(apiResponse);

        model.addAttribute("keyword", keyword);
        model.addAttribute("placeList", placeList);
        model.addAttribute("arrange", arrange);

        return "allplace.search_result";
    }

    /**
     * 지역 코드를 기반으로 장소를 검색하고 결과를 뷰에 전달합니다.
     *
     * @param locationId    검색할 지역의 ID
     * @param contentTypeId 콘텐츠 타입 ID (기본값: 12 - 관광지)
     * @param arrange       정렬 방식 (기본값: A - 제목순)
     * @param model         뷰에 데이터를 전달하기 위한 Model 객체
     * @return "allplace.search_result" 검색 결과 뷰 이름
     */
    @GetMapping("/searchByArea")
    public String searchByArea(
            @RequestParam("locationId") long locationId,
            @RequestParam(value = "contentTypeId", defaultValue = "12") String contentTypeId,
            @RequestParam(value = "arrange", defaultValue = "A") String arrange,
            Model model) {

        log.info("[Controller] 지역 검색 (API Only): locationId=" + locationId);
        TourApiResponseVO apiResponse = allPlaceService.searchByArea(locationId, contentTypeId, arrange);
        
        List<PlaceDTO> placeList = convertApiItemsToDtoList(apiResponse);

        model.addAttribute("keyword", "지역 검색 결과");
        model.addAttribute("placeList", placeList);
        model.addAttribute("arrange", arrange);

        return "allplace.search_result";
    }

    /**
     * 날짜와 지역을 기준으로 축제 정보를 검색하고 결과를 뷰에 전달합니다.
     *
     * @param eventStartDate 행사 시작일 (yyyyMMdd 형식, 기본값: 오늘)
     * @param arrange        정렬 방식 (기본값: A - 제목순)
     * @param locationId     검색할 지역의 ID (기본값: 0 - 전체)
     * @param model          뷰에 데이터를 전달하기 위한 Model 객체
     * @return "allplace.search_result" 검색 결과 뷰 이름
     */
    @GetMapping("/searchFestival")
    public String searchFestival(
            @RequestParam(value = "eventStartDate", required = false) String eventStartDate,
            @RequestParam(value = "arrange", defaultValue = "A") String arrange,
            @RequestParam(value = "locationId", defaultValue = "0") long locationId, 
            Model model) {

        if (eventStartDate == null || eventStartDate.isEmpty()) {
            eventStartDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        log.info("[Controller] 축제 검색 (API Only): " + eventStartDate + ", LocationID: " + locationId); 
        
        // 2. [수정] 3번째 인자로 locationId를 전달합니다.
        TourApiResponseVO apiResponse = allPlaceService.searchFestival(eventStartDate, arrange, locationId);

        List<PlaceDTO> placeList = convertApiItemsToDtoList(apiResponse);

        model.addAttribute("keyword", eventStartDate + " 축제 검색 결과");
        model.addAttribute("placeList", placeList);
        model.addAttribute("arrange", arrange);

        return "allplace.search_result";
    }
    
    /**
     * Tour API로부터 contentId와 contentTypeId를 이용해 장소 상세 정보를 조회하고,
     * 해당 정보를 DB에 저장(또는 업데이트)한 후, 내부 상세 페이지로 리다이렉트합니다.
     *
     * @param contentId     API에서 사용하는 콘텐츠 ID
     * @param contentTypeId API에서 사용하는 콘텐츠 타입 ID
     * @param model         오류 발생 시 뷰에 메시지를 전달하기 위한 Model 객체
     * @return 성공 시 "redirect:/allplace/detail/{placeId}", 실패 시 "common/error"
     */
    @GetMapping("/view/{contentId}")
    public String viewAndSave(
            @PathVariable("contentId") String contentId,
            @RequestParam("contentTypeId") String contentTypeId,  // ★ 추가
            Model model) {

        log.info("[Controller] 상세 요청: contentId = " + contentId + ", type = " + contentTypeId);

        // 1) 상세 기본정보
        TourItemVO item = tourApiService.getPlaceDetail(contentId);

        if (item == null) {
            log.warn("[Controller] API 정보를 찾을 수 없습니다.");
            model.addAttribute("errorMessage", "API에서 장소 정보를 찾을 수 없습니다.");
            return "common/error";
        }

        // 2) contentTypeId 직접 세팅
        item.setContentTypeId(contentTypeId); // ★★★ 결정적 부분

        // 3) DB 저장
        PlaceDTO place = allPlaceService.addPlaceOnDemand(item);

        if (place == null) {
            model.addAttribute("errorMessage", "장소 정보 처리 중 오류 발생");
            return "common/error";
        }

        return "redirect:/allplace/detail/" + place.getPlaceId();
    }


    /**
     * 데이터베이스에 저장된 장소의 상세 정보를 조회합니다.
     * 주변 추천 장소 목록과 해시태그 정보를 함께 조회하여 뷰에 전달합니다.
     *
     * @param placeId DB에 저장된 장소의 고유 ID
     * @param model   뷰에 장소 상세 정보, 추천 목록, 해시태그 등을 전달하기 위한 Model 객체
     * @return "allplace.detail" 상세 페이지 뷰 이름, 정보가 없을 경우 "common/error"
     */
    @GetMapping("/detail/{placeId}")
    public String placeDetail(@PathVariable("placeId") long placeId, Model model) {
        log.info("[Controller] 상세 조회: " + placeId);
        PlaceDTO place = allPlaceService.getPlaceDetail(placeId);

        if (place == null) {
            log.warn("[Controller] " + placeId + "번 장소 정보가 DB에 없습니다.");
            model.addAttribute("errorMessage", "장소 정보를 찾을 수 없습니다.");
            return "common/error";
        }
        
        // ⭐ [수정] 주변 추천 장소: API 직접 호출
        List<PlaceDTO> recommendList = new ArrayList<>();
        try {
            String lat = String.valueOf(place.getLatitude());
            String lon = String.valueOf(place.getLongitude());
            String radius = "5000"; // 5km
            String contentTypeIds = "12,15,39"; // 관광,축제,음식
            
            TourApiResponseVO apiResponse = tourApiService.searchByLocation(lat, lon, radius, contentTypeIds);

            // [중요] API Item -> PlaceDTO 변환 (contentTypeId 포함!)
            if (apiResponse != null &&
                apiResponse.getResponse() != null &&
                apiResponse.getResponse().getBody() != null &&
                apiResponse.getResponse().getBody().getItems() != null &&
                apiResponse.getResponse().getBody().getItems().getItem() != null) {

                List<TourItemVO> items = apiResponse.getResponse().getBody().getItems().getItem();
                final String currentApiId = place.getPlaceApiId();

                for (TourItemVO item : items) {
                    if (currentApiId != null && currentApiId.equals(item.getContentId())) {
                        continue;
                    }
                    double dLat = safeDouble(item.getLatitude());
                    double dLon = safeDouble(item.getLongitude());
                    if (dLat == 0 || dLon == 0) continue;

                    PlaceDTO dto = new PlaceDTO();
                    dto.setPlaceApiId(item.getContentId());
                    dto.setName(clean(item.getTitle()));
                    dto.setAddress(clean(item.getAddress()));
                    dto.setPlaceMainImageUrl(clean(item.getFirstImage()));
                    dto.setContentTypeId(item.getContentTypeId());
                    recommendList.add(dto);
                    
                    if (recommendList.size() >= 6) {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            log.error("[Controller] 주변 추천 API 호출 오류: " + e.getMessage());
        }
        
        model.addAttribute("recommendList", recommendList);
        
        List<String> hashtags = allPlaceService.getHashtags(placeId);
        place.setHashtags(hashtags);

        model.addAttribute("place", place);
        return "allplace.detail";
    }

    /* --- (RESTful 메서드 3개 삭제: weatherok, mapok, searchLocation) --- */


    /* --- 헬퍼 함수들 --- */

    /**
     * 문자열에서 불필요한 공백을 제거하고, "false" 문자열을 null로 처리합니다.
     *
     * @param s 처리할 문자열
     * @return 처리된 문자열 또는 null
     */
    private String clean(String s) {
        if (s == null) return null;
        if (s.trim().equals("") || s.trim().equals("false")) return null;
        return s;
    }

    /**
     * 문자열을 double 타입으로 안전하게 변환합니다.
     * 변환 중 오류 발생 시 0.0을 반환합니다.
     *
     * @param s 변환할 문자열
     * @return 변환된 double 값 또는 0.0
     */
    private double safeDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (Exception e) { return 0; }
    }

    // (calcDistance 헬퍼 삭제 - REST 컨트롤러로 이동)
    
    
    // --- 단순 페이지 이동 ---
    /**
     * 관광지 지도 페이지를 표시합니다.
     *
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체 (현재 사용 안 함)
     * @return "allplace.map" 지도 페이지 뷰 이름
     */
    @GetMapping("/map")
    public String showMapPage(Model model) {
        log.info("[Controller] 관광지 지도 페이지 요청");
        return "allplace.map";
    }
   
    /**
     * 특정 지역의 인기 여행 트렌드(관광지)를 조회하여 여행 트렌드 페이지를 표시합니다.
     *
     * @param locationId 조회할 지역의 ID (기본값: 0 - 전체)
     * @param model      뷰에 트렌드 목록과 현재 지역 ID를 전달하기 위한 Model 객체
     * @return "allplace.trend" 트렌드 페이지 뷰 이름
     */
    @GetMapping("/trend")
    public String showTrendPage(
            // 1. 기본값을 '0' (#전체)으로 변경
            @RequestParam(value="locationId", defaultValue="0") long locationId, 
            Model model) {
        
        log.info("[Controller] 여행 트렌드 페이지 요청 (LocationID: " + locationId + ")");
        
        String contentTypeId = "12";   // (12: 관광지)
        String arrange = "A";          // (A: 인기순 정렬)
        
        TourApiResponseVO apiResponse = allPlaceService.searchByArea(locationId, contentTypeId, arrange);
        
        // ⭐ --- [진단 코드 추가 시작] ---
        if (apiResponse != null &&
            apiResponse.getResponse() != null &&
            apiResponse.getResponse().getBody() != null &&
            apiResponse.getResponse().getBody().getItems() != null &&
            !apiResponse.getResponse().getBody().getItems().getItem().isEmpty()) {
            
            TourItemVO firstItem = apiResponse.getResponse().getBody().getItems().getItem().get(0);
            log.info("[진단] API가 준 첫번째 아이템 이름: " + firstItem.getTitle());
            log.info("[진단] API가 준 첫번째 아이템 overview: " + firstItem.getOverview());
        
        } else {
            log.info("[진단] API가 데이터를 반환하지 않았습니다. (trendList가 비어있음)");
        }
        // ⭐ --- [진단 코드 추가 끝] ---
        
        List<PlaceDTO> trendList = convertApiItemsToDtoList(apiResponse);
        
        if (trendList.size() > 100) {
            trendList = trendList.subList(0, 100);
        }
        
        model.addAttribute("currentLocationId", locationId); 
        model.addAttribute("trendList", trendList); 
        
        return "allplace.trend";
    }

    /**
     * 여행지 뉴스 페이지를 표시합니다.
     *
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체 (현재 사용 안 함)
     * @return "allplace.news" 뉴스 페이지 뷰 이름
     */
    @GetMapping("/news")
    public String showNewsPage(Model model) {
        log.info("[Controller] 여행지 뉴스 페이지 요청");
        return "allplace.news";
    }

    /**
     * 날씨 및 공기질 정보 페이지를 표시합니다.
     *
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체 (현재 사용 안 함)
     * @return "allplace.weatherPage" 날씨 정보 페이지 뷰 이름
     */
    @GetMapping("/weather")
    public String showWeatherPage(Model model) {
        log.info("[Controller] 날씨/공기질 페이지 요청");
        return "allplace.weatherPage";
    }
    
    /**
     * 특정 지역과 날짜의 축제/행사 정보를 조회하여 축제/행사 페이지를 표시합니다.
     *
     * @param locationId     조회할 지역의 ID (기본값: 0 - 전체)
     * @param eventStartDate 행사 시작일 (yyyyMMdd 형식, 기본값: 오늘)
     * @param model          뷰에 축제 목록, 현재 지역 ID, 선택된 날짜 등을 전달하기 위한 Model 객체
     * @return "allplace.festival" 축제/행사 페이지 뷰 이름
     */
    @GetMapping("/festival")
    public String showFestivalPage(
            @RequestParam(value="locationId", defaultValue="0") long locationId,
            @RequestParam(value="eventStartDate", required=false) String eventStartDate,
            Model model) {
        
        // 1. "오늘 날짜" (yyyyMMdd)를 변수로 저장
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // 2. 날짜가 없으면 "오늘 날짜"로 기본값 설정
        if (eventStartDate == null || eventStartDate.isEmpty()) {
            eventStartDate = todayDate;
        }
        
        log.info("[Controller] 축제/행사 페이지 요청 (LocationID: " + locationId + ", Date: " + eventStartDate + ")");
        
        String arrange = "A";
        
        TourApiResponseVO apiResponse = allPlaceService.searchFestival(eventStartDate, arrange, locationId);
        
        List<PlaceDTO> trendList = convertApiItemsToDtoList(apiResponse); 
        
        if (trendList.size() > 100) {
            trendList = trendList.subList(0, 100);
        }
        
        model.addAttribute("currentLocationId", locationId);
        model.addAttribute("eventStartDate", eventStartDate); // 3. 선택된 날짜 (혹은 오늘)
        model.addAttribute("todayDate", todayDate); // 4. [추가] "오늘" 날짜
        model.addAttribute("trendList", trendList);
        
        return "allplace.festival";
    }
    
    
    /**
     * Tour API 응답 객체에 포함된 장소 목록(TourItemVO)을
     * 화면 표시에 적합한 PlaceDTO 객체 목록으로 변환합니다.
     * 위경도 정보가 없거나 유효하지 않은 아이템은 목록에서 제외됩니다.
     *
     * @param apiResponse Tour API로부터 받은 전체 응답 객체
     * @return 변환된 PlaceDTO 객체의 리스트
     */
    private List<PlaceDTO> convertApiItemsToDtoList(TourApiResponseVO apiResponse) {
        List<PlaceDTO> placeList = new ArrayList<>();

        if (apiResponse != null &&
            apiResponse.getResponse() != null &&
            apiResponse.getResponse().getBody() != null &&
            apiResponse.getResponse().getBody().getItems() != null &&
            apiResponse.getResponse().getBody().getItems().getItem() != null) {

            List<TourItemVO> apiItems = apiResponse.getResponse().getBody().getItems().getItem();
            log.info("[Controller] API 검색 결과 " + apiItems.size() + "건 (DB 저장 안 함)");

            for (TourItemVO item : apiItems) {
                if (item.getLatitude() == null || item.getLongitude() == null) continue;
                double lat = safeDouble(item.getLatitude());
                double lon = safeDouble(item.getLongitude());
                if (lat == 0 || lon == 0) continue;

                PlaceDTO dto = new PlaceDTO();
                dto.setPlaceApiId(item.getContentId()); 
                dto.setName(item.getTitle());
                dto.setAddress(item.getAddress());
                dto.setLatitude(lat);
                dto.setLongitude(lon);
                dto.setPlaceMainImageUrl(item.getFirstImage());
                dto.setContentTypeId(item.getContentTypeId());
                dto.setOverview(item.getOverview());
                
                placeList.add(dto);
            }
        } else {
            log.info("[Controller] API 검색 결과 없음");
        }
        return placeList;
    }
}