package com.project.trip.allplace.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody; // [import 추가]

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

    // ... (searchByKeyword, searchByArea, searchFestivalByDate, placeDetail 메서드는 기존과 동일) ...
    
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
            log.info("[Controller] API 검색 결과 " + apiItems.size() + "건");
            
            for (TourItemVO item : apiItems) {
                PlaceDTO savedPlace = allPlaceService.addPlaceOnDemand(item);
                if (savedPlace != null) {
                    savedPlaceList.add(savedPlace);
                }
            }
        } else {
            log.info("[Controller] API 검색 결과 없음");
        }
        
        model.addAttribute("keyword", keyword); 
        model.addAttribute("placeList", savedPlaceList); 
        model.addAttribute("arrange", arrange); 
        
        return "allplace/search_result"; 
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
            log.info("[Controller] API 검색 결과 " + apiItems.size() + "건");
            
            for (TourItemVO item : apiItems) {
                PlaceDTO savedPlace = allPlaceService.addPlaceOnDemand(item);
                if (savedPlace != null) {
                    savedPlaceList.add(savedPlace);
                }
            }
        } else {
            log.info("[Controller] API 검색 결과 없음");
        }
        
        model.addAttribute("keyword", "지역 검색 결과"); 
        model.addAttribute("placeList", savedPlaceList); 
        model.addAttribute("arrange", arrange); 
        
        return "allplace/search_result";
    }

    @GetMapping("/searchByDate")
    public String searchFestivalByDate(
            @RequestParam(value = "eventStartDate", required = false) String eventStartDate,
            @RequestParam(value = "arrange", defaultValue = "A") String arrange, 
            Model model) {
        
        if (eventStartDate == null || eventStartDate.isEmpty()) {
            eventStartDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        
        log.info("[Controller] 축제 날짜 검색: " + eventStartDate);
        
        TourApiResponseVO apiResponse = allPlaceService.searchFestival(eventStartDate, arrange);
        List<PlaceDTO> savedPlaceList = new ArrayList<>();
        
        if (apiResponse != null && 
            apiResponse.getResponse().getBody().getItems() != null &&
            apiResponse.getResponse().getBody().getItems().getItem() != null) {
            
            List<TourItemVO> apiItems = apiResponse.getResponse().getBody().getItems().getItem();
            log.info("[Controller] API 검색 결과 " + apiItems.size() + "건");
            
            for (TourItemVO item : apiItems) {
                PlaceDTO savedPlace = allPlaceService.addPlaceOnDemand(item);
                if (savedPlace != null) {
                    savedPlaceList.add(savedPlace);
                }
            }
        } else {
            log.info("[Controller] API 검색 결과 없음");
        }
        
        model.addAttribute("keyword", eventStartDate + " 축제 검색 결과"); 
        model.addAttribute("placeList", savedPlaceList); 
        model.addAttribute("arrange", arrange); 
        
        return "allplace/search_result";
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
        
        WeatherVO weather = weatherService.getTodayWeather(place.getLatitude(), place.getLongitude());
        
        model.addAttribute("place", place);
        model.addAttribute("weather", weather);
        
        return "allplace/detail"; 
    }
    
    
    // --- [이 메서드를 새로 추가] ---
    /**
     * 날씨 정보 REST API 엔드포인트
     * (위도/경도를 받아서 WeatherVO를 JSON으로 반환)
     * * @param lat (필수) 위도
     * @param lon (필수) 경도
     * @return
     */
    @GetMapping("/weather")
    @ResponseBody // (중요: 이 메서드는 뷰(JSP/Tiles)를 반환하지 않고, JSON 데이터를 반환합니다)
    public ResponseEntity<WeatherVO> getWeatherByCoords(
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon) {
        
        log.info("[Controller] REST 날씨 요청: lat=" + lat + ", lon=" + lon);
        
        WeatherVO weather = weatherService.getTodayWeather(lat, lon);
        
        if (weather != null) {
            return new ResponseEntity<>(weather, HttpStatus.OK); // (성공: 200 + 날씨 JSON)
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // (실패: 500)
        }
    }
    // --- [여기까지] ---
}