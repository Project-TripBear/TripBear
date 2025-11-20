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

/**
 * AI 기반 여행 경로 생성과 관련된 REST API 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 사용자 선호도를 기반으로 AI 여행 경로를 생성하고 저장하는 기능을 제공합니다.
 * </p>
 */
@RestController
@RequestMapping("/ai")
public class AiRestController {

    @Autowired
    private AiService aiService;

    /**
     * 사용자 선호도를 기반으로 AI 여행 경로를 생성하고 데이터베이스에 저장합니다.
     * <p>
     * 로그인된 사용자만 경로를 생성할 수 있으며, 생성된 경로의 ID를 반환합니다.
     * </p>
     * @param preferences 사용자의 여행 선호도를 담은 {@link AiRouteRequestDTO} 객체
     * @return 생성된 경로 ID와 성공 여부를 담은 {@code ResponseEntity<Map<String, Object>>}
     */
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
