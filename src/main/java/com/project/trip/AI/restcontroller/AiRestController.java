package com.project.trip.AI.restcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.service.AiService;
import com.project.trip.mypage.model.UserDTO;

@RestController
@RequestMapping("/ai")
public class AiRestController {
	
	@Autowired
	private AiService aiService;
	
	@PostMapping("/genderate")
	public ResponseEntity<Map<String, Object>> generateAiRoute (
			@RequestBody AiRouteRequestDTO airouterequest,
			HttpSession session){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			UserDTO userInfo = (UserDTO) session.getAttribute("userInfo");
			
			if (userInfo == null) {
				response.put("success", false);
				response.put("message", "로그인이 필요합니다.");
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
				
			long longUserId;
			
			try {
				
				longUserId = Long.parseLong(userInfo.getSeq());
				
			} catch (NumberFormatException e) {
				
				response.put("success", false);
                response.put("message", "사용자 ID(seq)를 숫자로 변환할 수 없습니다: " + userInfo.getSeq());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                
			}
			
			RouteDTO savedRoute = aiService.createAndSaveAiRoute(airouterequest, longUserId);
			
			response.put("success", true);
			response.put("routeId", savedRoute.getAiRouteId());
			
			return new ResponseEntity<>(response,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("sucess", false);
			response.put("message", "루트 생성 중 서버 오류가 발생했습니다." + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
