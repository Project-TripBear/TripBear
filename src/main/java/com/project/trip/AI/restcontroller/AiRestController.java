package com.project.trip.AI.restcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.service.AiService;
import com.project.trip.mypage.model.UserDTO; 

@RestController
public class AiRestController {

    @Autowired
    private AiService aiService;

    @PostMapping("/ai/generate")
    public ResponseEntity<Map<String, Object>> generateAiRoute(
            @RequestBody AiRouteRequestDTO preferences, 
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            UserDTO userInfo = (UserDTO) session.getAttribute("userInfo"); 
            if (userInfo == null) {
                 response.put("success", false);
                 response.put("message", "로그인이 필요합니다. (세션에 userInfo 없음)");
                 return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            long longUserId;
            double userWeight;
            try {
                // UserDTO에서 seq(PK)와 weight(몸무게)를 가져옴
                longUserId = Long.parseLong(userInfo.getSeq());
                userWeight = Double.parseDouble(userInfo.getWeight());
                
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "사용자 정보(seq 또는 weight)를 변환할 수 없습니다.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // AiService 호출 시 longUserId와 userWeight 전달
            RouteDTO savedRoute = aiService.createAndSaveAiRoute(preferences, longUserId, userWeight); 

            response.put("success", true);
            response.put("routeId", savedRoute.getAiRouteId());
            
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "루트 생성 중 서버 오류: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
