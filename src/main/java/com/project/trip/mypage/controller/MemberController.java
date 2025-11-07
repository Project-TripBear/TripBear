package com.project.trip.mypage.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.trip.mypage.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final PasswordEncoder encoder;
	private final MemberMapper mapper;

	/*
	 * @GetMapping("/member/add") public String add() {
	 * 
	 * return "member.add"; }
	 * 
	 * @PostMapping("/member/addok") public String addok(UserDTO dto) {
	 * 
	 * //System.out.println(dto); //암호 > 암호화
	 * 
	 * dto.setPw((encoder.encode(dto.getPw())));
	 * 
	 * mapper.add(dto);
	 * 
	 * return "redirect:/"; }
	 */
	
	@GetMapping("/member/login")
	public String login() {
		
		return "mypage.member.login";
	}
	
}
