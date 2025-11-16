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

    @GetMapping("/searchFestival")
    public String searchFestival(
            @RequestParam(value = "eventStartDate", required = false) String eventStartDate,
            @RequestParam(value = "arrange", defaultValue = "A") String arrange,
            Model model) {

        if (eventStartDate == null || eventStartDate.isEmpty()) {
            eventStartDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        log.info("[Controller] 축제 검색 (API Only): " + eventStartDate); 
        TourApiResponseVO apiResponse = allPlaceService.searchFestival(eventStartDate, arrange);

        List<PlaceDTO> placeList = convertApiItemsToDtoList(apiResponse);

        model.addAttribute("keyword", eventStartDate + " 축제 검색 결과");
        model.addAttribute("placeList", placeList);
        model.addAttribute("arrange", arrange);

        return "allplace.search_result";
    }
    
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

    private String clean(String s) {
        if (s == null) return null;
        if (s.trim().equals("") || s.trim().equals("false")) return null;
        return s;
    }

    private double safeDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (Exception e) { return 0; }
    }

    // (calcDistance 헬퍼 삭제 - REST 컨트롤러로 이동)
    
    
    // --- 단순 페이지 이동 ---
    @GetMapping("/map")
    public String showMapPage(Model model) {
        log.info("[Controller] 관광지 지도 페이지 요청");
        return "allplace.map";
    }
    
    @GetMapping("/trend")
    public String showTrendPage(Model model) {
        log.info("[Controller] 여행 트렌드 페이지 요청");
        // (참고: trend.jsp가 데이터를 표시하려면 여기서 Service를 호출하고
        // model.addAttribute("trendList", ...)를 추가해야 합니다)
        return "allplace.trend";
    }

    @GetMapping("/news")
    public String showNewsPage(Model model) {
        log.info("[Controller] 여행지 뉴스 페이지 요청");
        return "allplace.news";
    }

    @GetMapping("/weather")
    public String showWeatherPage(Model model) {
        log.info("[Controller] 날씨/공기질 페이지 요청");
        return "allplace.weatherPage";
    }
    
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
                
                placeList.add(dto);
            }
        } else {
            log.info("[Controller] API 검색 결과 없음");
        }
        return placeList;
    }
}