package com.project.trip.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메인 페이지와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Controller
public class MainController {

	/**
	 * 애플리케이션의 메인 페이지를 반환합니다.
	 *
	 * @return 메인 페이지의 뷰 이름
	 */
	@GetMapping("/")
	public String index() {
		
		return "main";
		
	}
	
}
