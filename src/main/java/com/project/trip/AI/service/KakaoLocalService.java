package com.project.trip.AI.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

/**
 * 카카오 로컬 검색 API와 연동하여 장소 검색 기능을 제공하는 서비스 클래스입니다.
 * <p>
 * 키워드를 기반으로 장소를 검색하고, 카카오 API의 원본 JSON 응답을 반환합니다.
 * </p>
 */
@Service
public class KakaoLocalService {

    // 🔥 반드시 네 REST API KEY 넣기
    private final String KAKAO_REST_KEY = "KakaoAK 43d0c1f4f0c2ff0bd368637ae2fa9b7a";

    // 🔥 카카오 원본 JSON 그대로 반환
    /**
     * 키워드를 사용하여 카카오 로컬 검색 API를 호출하고, 원본 JSON 응답을 문자열로 반환합니다.
     *
     * @param query 검색할 키워드
     * @return 카카오 로컬 검색 API의 원본 JSON 응답 문자열
     * @throws Exception API 호출 또는 응답 처리 중 발생할 수 있는 예외
     */
    public String searchKeywordRaw(String query) throws Exception {

        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query="
                + URLEncoder.encode(query, "UTF-8");

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("Authorization", KAKAO_REST_KEY);
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")
        );

        return br.lines().collect(Collectors.joining());
    }
}
