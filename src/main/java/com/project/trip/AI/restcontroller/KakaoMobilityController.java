package com.project.trip.AI.restcontroller;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * 카카오모빌리티 API를 사용하여 길찾기(Directions) 정보를 제공하는 REST 컨트롤러입니다.
 * <p>
 * 출발지와 목적지의 좌표를 받아 최적의 경로 정보를 반환합니다.
 * </p>
 */
@RestController
@RequestMapping("/api/mobility")
public class KakaoMobilityController {

    private static final String REST_API_KEY = "43d0c1f4f0c2ff0bd368637ae2fa9b7a";

    /**
     * 카카오모빌리티 길찾기 API를 호출하여 경로 정보를 반환합니다.
     *
     * @param originX 출발지 경도
     * @param originY 출발지 위도
     * @param destX 목적지 경도
     * @param destY 목적지 위도
     * @return 카카오모빌리티 API 응답을 담은 {@code ResponseEntity<String>}
     */
    @GetMapping("/directions")
    public ResponseEntity<String> getDirections(@RequestParam double originX,
                                                @RequestParam double originY,
                                                @RequestParam double destX,
                                                @RequestParam double destY) {

        String url = "https://apis-navi.kakaomobility.com/v1/directions"
                + "?origin=" + originX + "," + originY
                + "&destination=" + destX + "," + destY
                + "&priority=TIME";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + REST_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return ResponseEntity.ok(response.getBody());
    }
}
