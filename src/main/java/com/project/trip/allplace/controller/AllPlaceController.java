package com.project.trip.allplace.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourItemVO;
import com.project.trip.allplace.model.WeatherVO;
import com.project.trip.allplace.service.AllPlaceService;
import com.project.trip.allplace.service.TourApiService;
import com.project.trip.allplace.service.WeatherService;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/allplace")
public class AllPlaceController {

    @Autowired
    private AllPlaceService allPlaceService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private TourApiService tourApiService;

    // --- (이하 검색/상세보기 메서드는 이전과 동일) ---
    
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
            Model model) {
        
        log.info("[Controller] /view/ 상세+저장 요청: " + contentId);

        TourItemVO item = tourApiService.getPlaceDetail(contentId);
        if (item == null) {
            log.warn("[Controller] " + contentId + " API 정보를 찾을 수 없습니다.");
            model.addAttribute("errorMessage", "API에서 장소 정보를 찾을 수 없습니다.");
            return "common/error";
        }

        PlaceDTO place = allPlaceService.addPlaceOnDemand(item);
        
        if (place == null) {
             log.warn("[Controller] " + contentId + " DB 저장/조회 실패.");
            model.addAttribute("errorMessage", "장소 정보를 처리하는 중 오류가 발생했습니다.");
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

        model.addAttribute("place", place);
        return "allplace.detail";
    }

    @GetMapping("/weatherok")
    @ResponseBody
    public ResponseEntity<WeatherVO> getWeatherByCoords(
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon) {

        log.info("[Controller] REST 날씨 요청: lat=" + lat + ", lon=" + lon);
        WeatherVO weather = weatherService.getTodayWeather(lat, lon);

        return (weather != null)
                ? new ResponseEntity<>(weather, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/mapok")
    @ResponseBody
    public ResponseEntity<List<PlaceDTO>> getSpotsForMapOk(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam(value = "radius", defaultValue = "3000") double radius,
            @RequestParam(value = "contentTypeId", defaultValue = "12,39") String contentTypeId) {

        log.info("[Controller] /mapok 요청 lat=" + lat + ", lng=" + lng);

        TourApiResponseVO api = tourApiService.searchByLocation(
                String.valueOf(lat),
                String.valueOf(lng),
                String.valueOf(radius),
                contentTypeId
        );

        if (api == null ||
            api.getResponse() == null ||
            api.getResponse().getBody() == null ||
            api.getResponse().getBody().getItems() == null ||
            api.getResponse().getBody().getItems().getItem() == null) {

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<TourItemVO> items = api.getResponse().getBody().getItems().getItem();
        if (items.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<PlaceDTO> out = new ArrayList<>();

        for (TourItemVO item : items) {

            double dLat = safeDouble(item.getLatitude());
            double dLon = safeDouble(item.getLongitude());
            if (dLat == 0 || dLon == 0) continue;

            PlaceDTO dto = new PlaceDTO();
            dto.setPlaceApiId(item.getContentId());
            dto.setName(clean(item.getTitle()));
            dto.setAddress(clean(item.getAddress()));
            dto.setLatitude(dLat);
            dto.setLongitude(dLon);

            // =============================
            // ⭐ 이미지: firstImage 만 사용
            // =============================
            String img = clean(item.getFirstImage());
            if (img == null) img = clean(item.getFirstImage());
            if (img == null) img = null; // 프론트에서 noimage 처리
            dto.setPlaceMainImageUrl(img);

            // 타입 매핑
            switch (item.getContentTypeId()) {
                case "12": dto.setPlaceTypeId(1L); break;
                case "15": dto.setPlaceTypeId(2L); break;
                case "39": dto.setPlaceTypeId(3L); break;
                default: dto.setPlaceTypeId(1L);
            }

            // 거리 km
            double distance = calcDistance(lat, lng, dLat, dLon);
            dto.setDistance(distance);

            out.add(dto);
        }

        // 가까운 순 → 30개
        out.sort(Comparator.comparingDouble(PlaceDTO::getDistance));
        if (out.size() > 30)
            out = out.subList(0, 30);

        return new ResponseEntity<>(out, HttpStatus.OK);
    }


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

    private double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) *
            Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }



    
    // --- [누락된 메서드 추가] ---
    @GetMapping("/map")
    public String showMapPage(Model model) {
        log.info("[Controller] 관광지 지도 페이지 요청");
        // Tiles definition 이름 (allplace.map)
        return "allplace.map";
    }
    // --- [추가 끝] ---
    
    
    @GetMapping("/trend")
    public String showTrendPage(Model model) {
        log.info("[Controller] 여행 트렌드 페이지 요청");
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
                // (좌표값 없으면 리스트에 추가 안 함)
                if (item.getLatitude() == null || item.getLongitude() == null) continue;
                double lat = safeDouble(item.getLatitude());
                double lon = safeDouble(item.getLongitude());
                if (lat == 0 || lon == 0) continue;

                PlaceDTO dto = new PlaceDTO();
                // (중요) DB PK인 placeId가 아닌, API ID인 placeApiId를 저장
                dto.setPlaceApiId(item.getContentId()); 
                dto.setName(item.getTitle());
                dto.setAddress(item.getAddress());
                dto.setLatitude(lat);
                dto.setLongitude(lon);
                dto.setPlaceMainImageUrl(item.getFirstImage());
                
                // (이 DTO는 DB에 저장되지 않았으므로 placeId는 0입니다)
                placeList.add(dto);
            }
        } else {
            log.info("[Controller] API 검색 결과 없음");
        }
        return placeList;
    }

}