package com.project.trip.AI.restcontroller;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/mobility")
public class KakaoMobilityController {

    private static final String REST_API_KEY = "43d0c1f4f0c2ff0bd368637ae2fa9b7a";

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
