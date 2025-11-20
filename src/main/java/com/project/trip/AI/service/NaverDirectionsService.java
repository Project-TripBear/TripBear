package com.project.trip.AI.service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 네이버 길찾기 API와 연동하여 경로 정보를 제공하는 서비스 클래스입니다.
 * <p>
 * 출발지와 목적지, 교통수단을 기반으로 네이버 길찾기 API를 호출하고,
 * 그 결과를 {@link JsonNode} 형태로 반환합니다.
 * </p>
 */
@Service
public class NaverDirectionsService {

    private final String CLIENT_ID = "ygwzmxedq7";
    private final String CLIENT_SECRET = "izf7nbg8C3K00WPz3LbJyNCWX8rcqGiENasmwdhq";

    /**
     * 네이버 길찾기 API를 호출하여 출발지와 목적지 간의 경로 정보를 요청합니다.
     *
     * @param start 출발지 좌표 (예: "127.12345,37.54321")
     * @param goal 목적지 좌표 (예: "127.67890,37.98765")
     * @param mode 교통수단 (예: "driving", "walking", "bicycle", "transit")
     * @return 네이버 길찾기 API의 응답을 담은 {@link JsonNode} 객체
     * @throws Exception API 호출 또는 응답 처리 중 발생할 수 있는 예외
     */
    public JsonNode requestRoute(String start, String goal, String mode) throws Exception {

        String option = convertMode(mode);

        String apiUrl = "https://maps.apigw.ntruss.com/map-direction/v1/driving"
                + "?start=" + start
                + "&goal=" + goal
                + "&option=" + option;

        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
        conn.setRequestProperty("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

        int status = conn.getResponseCode();

        BufferedReader br = (status == 200)
                ? new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))
                : new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));

        String body = br.lines().collect(Collectors.joining());
        br.close();

        System.out.println("=== NAVER DIRECTIONS RESPONSE ===");
        System.out.println("status = " + status);
        System.out.println("body   = " + body);
        System.out.println("================================");

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(body);
    }

    /**
     * 클라이언트에서 전달된 교통수단 모드를 네이버 길찾기 API에서 사용하는 옵션으로 변환합니다.
     *
     * @param mode 클라이언트에서 전달된 교통수단 모드 (예: "walking", "bicycle", "transit")
     * @return 네이버 길찾기 API에서 사용하는 옵션 문자열 (예: "pedestrian", "bicycle", "pubtrans")
     */
    private String convertMode(String mode) {
        if (mode == null) return "trafast";
        switch (mode) {
            case "walking": return "pedestrian";
            case "bicycle": return "bicycle";
            case "transit": return "pubtrans";
            default: return "trafast";
        }
    }
}
