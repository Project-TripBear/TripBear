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

@Service
public class NaverDirectionsService {

    private final String CLIENT_ID = "ygwzmxedq7";
    private final String CLIENT_SECRET = "izf7nbg8C3K00WPz3LbJyNCWX8rcqGiENasmwdhq";

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
