package com.project.trip.AI.restcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.service.AiService;
import com.project.trip.mypage.model.CustomUser;
import com.project.trip.mypage.model.UserDTO; 

@RestController
@RequestMapping("/ai")
public class AiRestController {

    @Autowired
    private AiService aiService;

    @PostMapping(
    		value = "/generate",
    		consumes = "application/json",
    		produces = "application/json;charset=UTF-8"
    		)
    public ResponseEntity<Map<String, Object>> generateAiRoute(
            @RequestBody AiRouteRequestDTO preferences) {

        Map<String, Object> response = new HashMap<>();

        try {
        	
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        	UserDTO userInfo = null;
        	
        	if (auth != null && auth.getPrincipal() instanceof CustomUser) {
        		CustomUser loginUser = (CustomUser) auth.getPrincipal();
        		userInfo = loginUser.getUdto();
        	}
        	
            if (userInfo == null) {
                 response.put("success", false);
                 response.put("message", "로그인이 필요합니다. (시큐리티 세션 정보 없음)");
                 return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            long longUserId = Long.parseLong(userInfo.getSeq());
            double userWeight = Double.parseDouble(userInfo.getWeight());
            
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
