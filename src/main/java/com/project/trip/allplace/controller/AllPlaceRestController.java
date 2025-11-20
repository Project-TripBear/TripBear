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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController; // ⭐

// (필요한 DTO/Service import)
import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourItemVO;
import com.project.trip.allplace.model.WeatherVO;
import com.project.trip.allplace.service.TourApiService;
import com.project.trip.allplace.service.KrWeatherService;

import lombok.extern.log4j.Log4j;

/**
 * 모든 장소(AllPlace)와 관련된 RESTful API 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 날씨 정보 조회, 지도에 표시할 장소 목록 조회, 키워드 기반 장소 검색 등
 * 클라이언트 측에서 비동기적으로 데이터를 요청할 때 사용되는 엔드포인트를 제공합니다.
 * </p>
 */
@Log4j
@RestController // ⭐ @Controller 대신
@RequestMapping("/allplace")
public class AllPlaceRestController {

    @Autowired
    private KrWeatherService krweatherService;

    @Autowired
    private TourApiService tourApiService;
    
 // AllPlaceController.java

    /**
     * 위도와 경도를 기반으로 해당 위치의 현재 날씨 정보를 조회하여 반환합니다.
     *
     * @param lat 조회할 위치의 위도
     * @param lon 조회할 위치의 경도
     * @return ResponseEntity&lt;WeatherVO&gt; 날씨 정보를 담은 {@link WeatherVO} 객체와 HTTP 상태 코드를 포함하는 응답.
     *         성공 시 날씨 정보와 OK(200), 실패 시 INTERNAL_SERVER_ERROR(500).
     */
    @GetMapping("/weatherok")
    public ResponseEntity<WeatherVO> getWeatherByCoords( // [수정 1] 반환 타입을 ResponseEntity<?> -> ResponseEntity<WeatherVO>
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon) {

        log.info("[REST] 날씨 요청 lat=" + lat + ", lon=" + lon);
        
        WeatherVO weather = krweatherService.getTodayWeather(lat, lon); 

        // [수정 2] 로그를 추가하여 service가 null을 반환하는지 확인
        if (weather != null) {
            log.info("[REST] 날씨 조회 성공. JSON으로 반환합니다: " + weather.getSkyStatus());
            return new ResponseEntity<>(weather, HttpStatus.OK);
        } else {
            log.warn("[REST] 날씨 조회 실패 (service returned null)");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 지도에 표시할 장소 목록을 조회하는 REST API입니다.
     * 중심 좌표(위도, 경도)와 반경, 콘텐츠 타입 등을 기반으로 주변 장소를 검색하고,
     * 거리순으로 정렬하여 최대 300개의 결과를 반환합니다.
     *
     * @param lat           중심점의 위도
     * @param lng           중심점의 경도
     * @param radius        검색 반경 (미터 단위, 기본값: 20000m)
     * @param contentTypeId 조회할 콘텐츠 타입 ID (기본값: "12,39" - 관광지, 음식점)
     * @param keyword       선택적인 검색 키워드
     * @return ResponseEntity&lt;List&lt;PlaceDTO&gt; &gt; 장소 목록({@link PlaceDTO})과 HTTP 상태 코드를 포함하는 응답.
     *         결과가 있으면 목록과 OK(200), 없으면 NO_CONTENT(204).
     */
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
    
    /**
     * 키워드를 사용하여 장소를 검색하고, 결과 목록을 JSON으로 반환하는 REST API입니다.
     *
     * @param keyword 검색할 키워드
     * @return ResponseEntity&lt;List&lt;PlaceDTO&gt;&gt; 검색된 장소의 기본 정보 목록({@link PlaceDTO})과 HTTP 상태 코드를 포함하는 응답.
     *         결과가 있으면 목록과 OK(200), 없으면 NO_CONTENT(204).
     */
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

    /**
     * 두 지점(위도, 경도) 간의 거리를 계산합니다.
     * 하버사인 공식을 사용하여 지구 곡률을 고려한 거리를 반환합니다.
     *
     * @param lat1 첫 번째 지점의 위도
     * @param lon1 첫 번째 지점의 경도
     * @param lat2 두 번째 지점의 위도
     * @param lon2 두 번째 지점의 경도
     * @return 두 지점 간의 거리 (킬로미터 단위)
     */
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