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
	
	@GetMapping("/plan")
	public String showAiPlanPage(HttpSession session, Model model) {
		
		return "ai.plan";
	}
	
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
