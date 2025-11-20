package com.project.trip.AI.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.service.AiService;

@Controller
@RequestMapping("/ai")
public class AiController {
	
	@Autowired
	private AiService aiService;
	
	/**
	 * AI 여행 계획 페이지를 표시합니다.
	 *
	 * @param session HttpSession 현재 사용자 세션
	 * @param model Model 뷰에 데이터를 전달하기 위한 모델
	 * @return String "ai.plan" 뷰 이름을 반환하여 해당 페이지를 렌더링
	 */
	@GetMapping("/plan")
	public String showAiPlanPage(HttpSession session, Model model) {
		
		return "ai.plan";
	}
	
	/**
	 * AI 여행 계획 결과 페이지를 표시합니다.
	 * <p>
	 * 요청된 routeId를 사용하여 AI가 생성한 경로 정보를 조회하고,
	 * 결과가 있으면 결과 페이지로, 없으면 계획 페이지로 리다이렉트합니다.
	 *
	 * @param routeId long 조회할 AI 여행 루트의 ID
	 * @param model Model 뷰에 결과 데이터를 전달하기 위한 모델
	 * @return String 결과가 있으면 "ai.result" 뷰, 없으면 "redirect:/ai/plan"
	 */
	@GetMapping("/result")
	public String showAiResultPage(@RequestParam("routeId") long routeId, Model model) {
		
		if (routeId <= 0) {
			return "redirect:/ai/plan";
		}
		
		RouteDTO resultRoute = aiService.getAiRouteById(routeId);
		
		if (resultRoute != null) {
			
			model.addAttribute("resultRoute", resultRoute);
			return "ai.result";
		} else {
			return "redirect:/ai/plan";
		}
	}

}
