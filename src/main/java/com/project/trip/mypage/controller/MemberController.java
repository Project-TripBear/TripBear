package com.project.trip.mypage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.model.UserDTO;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final PasswordEncoder encoder;
	private final MemberMapper mapper;

	
	@GetMapping("/member/register")
	public String register() {
		
		return "mypage.member.register";
	}
	
	@PostMapping("/member/registerok")
	public String registerok(UserDTO dto) {
		
		//System.out.println(dto);
		//암호 > 암호화
		
		dto.setPw((encoder.encode(dto.getPw())));
		
		mapper.add(dto);
		
		return "redirect:/";
	}
	
	
	@PostMapping("/member/idCheck") // (★) 새 URL 매핑
	@ResponseBody // (★) JSON으로 응답
	public Map<String, Integer> idCheck(@RequestParam("id") String id) {
		
		Map<String, Integer> response = new HashMap<>();
		
		int count = mapper.idCheck(id); // (mapper가 중복이면 1, 아니면 0을 반환한다고 가정)
		
		System.out.println("아이디 유효성값 : " + count);
		response.put("result", count);
		return response;
	}
	

	
	@GetMapping("/member/login")
	public String login() {
		
		return "mypage.member.login";
	}
	
	@GetMapping("/member/findid")
	public String findIdForm() {
		
		return "mypage.member.idselect";
	}
	
	@GetMapping("/member/findpw")
	public String findPwForm() {
		
		return "mypage.member.pwselect";
	}
	
	
	
	@GetMapping("/member/myactivitiessummary")
	@ResponseBody
	public Map<String, Integer> getMyActivitiesSummary(Authentication auth) {
	    String username = auth.getName();
	    
	    // username 기반으로 DTO 조회
	    UserDTO dto = mapper.getUserByUsername(username);
	    
	    Map<String, Integer> summary = new HashMap<>();
	    String seq = dto.getSeq();
	    
	    summary.put("boardCount", mapper.getMyBoardCount(seq));
	    summary.put("commentCount", mapper.getMyCommentCount(seq));
	    summary.put("likeCount", mapper.getMyLikeTotalCount(seq));
	    summary.put("scrapCount", mapper.getMyScrapTotalCount(seq));
	    
	    return summary;
	}
	
	

	
	 
	

	
}
