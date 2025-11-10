package com.project.trip.mypage.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.Getter;

@Controller
public class MainController {

	@GetMapping("/")
	public String index() {
		
		return "index";
		
	}
	
}
