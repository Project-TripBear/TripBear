package com.project.trip.allplace.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController; // ⭐

// (필요한 DTO/Service import)
import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourItemVO;
import com.project.trip.allplace.model.WeatherVO;
import com.project.trip.allplace.service.TourApiService;
import com.project.trip.allplace.service.KrWeatherService;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController // ⭐ @Controller 대신 @RestController
@RequestMapping("/allplace")
public class AllPlaceRestController {

    @Autowired
    private KrWeatherService weatherService;

    @Autowired
    private TourApiService tourApiService;
    
    @GetMapping("/weatherok")
    public ResponseEntity<WeatherVO> getWeatherByCoords(
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon) {

        log.info("[REST] REST 날씨 요청: lat=" + lat + ", lon=" + lon);
        WeatherVO weather = weatherService.getTodayWeather(lat, lon);

        return (weather != null)
                ? new ResponseEntity<>(weather, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/mapok")
    public ResponseEntity<List<PlaceDTO>> getSpotsForMapOk(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam(value = "radius", defaultValue = "20000") double radius,
            @RequestParam(value = "contentTypeId", defaultValue = "12,39") String contentTypeId,
            @RequestParam(value = "keyword", required = false) String keyword){

        log.info("[REST] /mapok 요청 lat=" + lat + ", lng=" + lng);

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

            String img = clean(item.getFirstImage());
            dto.setPlaceMainImageUrl(img);

            switch (item.getContentTypeId()) {
                case "12": dto.setPlaceTypeId(1L); break;
                case "15": dto.setPlaceTypeId(2L); break;
                case "39": dto.setPlaceTypeId(3L); break;
                default: dto.setPlaceTypeId(1L);
            }
            dto.setContentTypeId(item.getContentTypeId());
            double distance = calcDistance(lat, lng, dLat, dLon);
            dto.setDistance(distance);
            out.add(dto);
        }
        
        out.sort(Comparator.comparingDouble(PlaceDTO::getDistance));
        if (out.size() > 300)
            out = out.subList(0, 300);

        return new ResponseEntity<>(out, HttpStatus.OK);
    }
    
    @GetMapping("/searchLocation")
    public ResponseEntity<List<PlaceDTO>> searchLocation(@RequestParam("keyword") String keyword) {

        log.info("[REST] /searchLocation (키워드) 요청: " + keyword);
        TourApiResponseVO api = tourApiService.searchByKeyword(keyword, "A", "12,39");

        if (api == null ||
            api.getResponse() == null ||
            api.getResponse().getBody() == null ||
            api.getResponse().getBody().getItems() == null ||
            api.getResponse().getBody().getItems().getItem() == null) {

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<TourItemVO> items = api.getResponse().getBody().getItems().getItem();
        List<PlaceDTO> result = new ArrayList<>();

        for (TourItemVO item : items) {
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
            result.add(dto);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
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
}