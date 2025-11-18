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


/**
 * 회원 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * 회원가입, 로그인, 아이디 중복 확인, 아이디/비밀번호 찾기 폼, 마이페이지 활동 요약 등의 기능을 제공합니다.
 */
@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final PasswordEncoder encoder;
	private final MemberMapper mapper;

	
	/**
	 * 회원가입 폼 페이지를 반환합니다.
	 *
	 * @return 회원가입 폼 페이지의 뷰 이름
	 */
	@GetMapping("/member/register")
	public String register() {
		
		return "mypage.member.register";
	}
	
	/**
	 * 회원가입 요청을 처리합니다.
	 * 사용자 비밀번호를 암호화하여 데이터베이스에 저장합니다.
	 *
	 * @param dto 회원가입 정보를 담은 {@link UserDTO} 객체
	 * @return 메인 페이지로 리다이렉트
	 */
	@PostMapping("/member/registerok")
	public String registerok(UserDTO dto) {
		
		//System.out.println(dto);
		//암호 > 암호화
		
		dto.setPw((encoder.encode(dto.getPw())));
		
		mapper.add(dto);
		
		return "redirect:/";
	}
	
	
	/**
	 * 아이디 중복 확인을 처리합니다.
	 *
	 * @param id 중복 여부를 확인할 사용자 아이디
	 * @return 중복 여부 (1: 중복, 0: 사용 가능)를 담은 {@code Map<String, Integer>}
	 */
	@PostMapping("/member/idCheck") // (★) 새 URL 매핑
	@ResponseBody // (★) JSON으로 응답
	public Map<String, Integer> idCheck(@RequestParam("id") String id) {
		
		Map<String, Integer> response = new HashMap<>();
		
		int count = mapper.idCheck(id); // (mapper가 중복이면 1, 아니면 0을 반환한다고 가정)
		
		System.out.println("아이디 유효성값 : " + count);
		response.put("result", count);
		return response;
	}
	

	
	/**
	 * 로그인 폼 페이지를 반환합니다.
	 *
	 * @return 로그인 폼 페이지의 뷰 이름
	 */
	@GetMapping("/member/login")
	public String login() {
		
		return "mypage.member.login";
	}
	
	/**
	 * 아이디 찾기 폼 페이지를 반환합니다.
	 *
	 * @return 아이디 찾기 폼 페이지의 뷰 이름
	 */
	@GetMapping("/member/findid")
	public String findIdForm() {
		
		return "mypage.member.idselect";
	}
	
	/**
	 * 비밀번호 찾기 폼 페이지를 반환합니다.
	 *
	 * @return 비밀번호 찾기 폼 페이지의 뷰 이름
	 */
	@GetMapping("/member/findpw")
	public String findPwForm() {
		
		return "mypage.member.pwselect";
	}
	
	
	
	/**
	 * 현재 로그인한 사용자의 활동 요약 정보를 반환합니다.
	 * 게시글 수, 댓글 수, 좋아요 수, 스크랩 수를 포함합니다.
	 *
	 * @param auth Spring Security의 Authentication 객체
	 * @return 활동 요약 정보를 담은 {@code Map<String, Integer>}
	 */
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
