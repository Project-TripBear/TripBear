package com.project.trip.AI.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

@Service
public class KakaoLocalService {

    // 🔥 반드시 네 REST API KEY 넣기
    private final String KAKAO_REST_KEY = "KakaoAK 43d0c1f4f0c2ff0bd368637ae2fa9b7a";

    // 🔥 카카오 원본 JSON 그대로 반환
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
