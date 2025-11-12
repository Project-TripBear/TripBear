package com.project.trip.allplace.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    // ----------------------- 기존 검색 관련 메서드 -----------------------
    @GetMapping("/search")
    public String searchByKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "contentTypeId", defaultValue = "12") String contentTypeId,
            @RequestParam(value = "arrange", defaultValue = "A") String arrange, 
            Model model) {

        log.info("[Controller] 키워드 검색: " + keyword);

        TourApiResponseVO apiResponse = allPlaceService.searchByKeyword(keyword, arrange, contentTypeId);
        List<PlaceDTO> savedPlaceList = new ArrayList<>();

        if (apiResponse != null &&
            apiResponse.getResponse().getBody().getItems() != null &&
            apiResponse.getResponse().getBody().getItems().getItem() != null) {

            List<TourItemVO> apiItems = apiResponse.getResponse().getBody().getItems().getItem();
            for (TourItemVO item : apiItems) {
                PlaceDTO savedPlace = allPlaceService.addPlaceOnDemand(item);
                if (savedPlace != null) {
                    savedPlaceList.add(savedPlace);
                }
            }
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("placeList", savedPlaceList);
        model.addAttribute("arrange", arrange);

        return "allplace.search_result";
    }

    @GetMapping("/searchByArea")
    public String searchByArea(
            @RequestParam("locationId") long locationId,
            @RequestParam(value = "contentTypeId", defaultValue = "12") String contentTypeId,
            @RequestParam(value = "arrange", defaultValue = "A") String arrange,
            Model model) {

        log.info("[Controller] 지역 검색: locationId=" + locationId);

        TourApiResponseVO apiResponse = allPlaceService.searchByArea(locationId, contentTypeId, arrange);
        List<PlaceDTO> savedPlaceList = new ArrayList<>();

        if (apiResponse != null &&
            apiResponse.getResponse().getBody().getItems() != null &&
            apiResponse.getResponse().getBody().getItems().getItem() != null) {

            List<TourItemVO> apiItems = apiResponse.getResponse().getBody().getItems().getItem();
            for (TourItemVO item : apiItems) {
                PlaceDTO savedPlace = allPlaceService.addPlaceOnDemand(item);
                if (savedPlace != null) {
                    savedPlaceList.add(savedPlace);
                }
            }
        }

        model.addAttribute("keyword", "지역 검색 결과");
        model.addAttribute("placeList", savedPlaceList);
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

        log.info("[Controller] 축제 검색: " + eventStartDate);

        TourApiResponseVO apiResponse = allPlaceService.searchFestival(eventStartDate, arrange);
        List<PlaceDTO> savedPlaceList = new ArrayList<>();

        if (apiResponse != null &&
            apiResponse.getResponse().getBody().getItems() != null &&
            apiResponse.getResponse().getBody().getItems().getItem() != null) {

            List<TourItemVO> apiItems = apiResponse.getResponse().getBody().getItems().getItem();
            for (TourItemVO item : apiItems) {
                PlaceDTO savedPlace = allPlaceService.addPlaceOnDemand(item);
                if (savedPlace != null) {
                    savedPlaceList.add(savedPlace);
                }
            }
        }

        model.addAttribute("keyword", eventStartDate + " 축제 검색 결과");
        model.addAttribute("placeList", savedPlaceList);
        model.addAttribute("arrange", arrange);

        return "allplace.search_result";
    }

    // ----------------------- 상세보기 -----------------------
    @GetMapping("/detail/{placeId}")
    public String placeDetail(@PathVariable("placeId") long placeId, Model model) {
        log.info("[Controller] 상세 조회: " + placeId);

        PlaceDTO place = allPlaceService.getPlaceDetail(placeId);

        if (place == null) {
            model.addAttribute("errorMessage", "장소 정보를 찾을 수 없습니다.");
            return "common/error";
        }

        model.addAttribute("place", place);
        return "allplace.detail";
    }

    // ----------------------- 날씨 JSON -----------------------
    @GetMapping("/weatherok")
    @ResponseBody
    public ResponseEntity<WeatherVO> getWeatherByCoords(
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon) {

        log.info("[Controller] REST 날씨 요청: lat=" + lat + ", lon=" + lon);

        WeatherVO weather = weatherService.getTodayWeather(lat, lon);
        if (weather != null) {
            return new ResponseEntity<>(weather, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ----------------------- ★ 지도용 (수정됨) -----------------------
    @GetMapping("/mapok")
    @ResponseBody
    public ResponseEntity<List<PlaceDTO>> getSpotsForMapOk(
            @RequestParam(value = "areaCode", defaultValue = "1") String areaCode,
            @RequestParam(value = "contentTypeId", defaultValue = "12") String contentTypeId,
            @RequestParam(value = "rows", defaultValue = "200") int rows,       // ★ 추가
            @RequestParam(value = "maxPages", defaultValue = "20") int maxPages // ★ 추가
    ) {
        log.info("[Controller] REST (지도용 /mapok) 요청: areaCode=" + areaCode
                + ", rows=" + rows + ", maxPages=" + maxPages);

        long locationId = Long.parseLong(areaCode);

        // ★ 여러 페이지 모아서 가져오는 신규 메서드 호출
        List<PlaceDTO> all = allPlaceService.searchByAreaAll(locationId, contentTypeId, "A", rows, maxPages);

        // ★ 좌표 없는 데이터 제거 및 중복 제거
        Map<Long, PlaceDTO> dedup = new LinkedHashMap<>();
        for (PlaceDTO p : all) {
            if (p != null && p.getLatitude() != 0.0 && p.getLongitude() != 0.0) {
                dedup.put(p.getPlaceId(), p);
            }
        }
        List<PlaceDTO> list = new ArrayList<>(dedup.values());

        return list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    // ----------------------- Tiles 페이지 -----------------------
    @GetMapping("/map")
    public String showMapPage(Model model) {
        log.info("[Controller] 관광지 지도 페이지 요청");
        return "allplace.map";
    }

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
}
