package com.project.trip.mypage.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.trip.mypage.mapper.MyPageMappper;
import com.project.trip.mypage.model.UserDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    @Autowired
    private final MyPageMappper mapper;
	
	@GetMapping("/member/mypage")
	public String mypage() {
		
		return "mypage.member.mypage";
	}
	

	
	@GetMapping("/member/userinfo")
	    public String userInfo(HttpSession session, Model model) {
	        String seq = session.getAttribute("seq").toString();

	        UserDTO dto = new UserDTO();
	        dto = mapper.userInfo(seq);

	        System.out.println(dto);

	        model.addAttribute("dto", dto);

			return "mypage.member.userinfo";
	    }
	
	
}
