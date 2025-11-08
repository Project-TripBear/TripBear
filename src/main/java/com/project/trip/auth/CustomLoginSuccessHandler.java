package com.project.trip.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	//로그인 직전 접속했던 기록 제공
	private HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		System.out.println("로그인을 성공하셨습니다.");
		
		
		
		//1.모든 사용자 > 시작 페이지로 이동
		//response.sendRedirect("/java/index.do");
		
//		//2.권한별 조치
//		//- 회원 > "member.do"
//		//- 관리자 > "admin.do"
//		
//		//현재 로그인을 성공한 유저의 권한??
//		List<String> roleNames = new ArrayList<String>();
//		
//		//System.out.println(authentication);
//		
//		authentication.getAuthorities().forEach(authority -> {
//			//System.out.println(authority);
//			roleNames.add(authority.getAuthority());
//		});
//		
//		if (roleNames.contains("ROLE_ADMIN")) {
//			response.sendRedirect("/java/admin.do");
//			return;
//		}
//		
//		if (roleNames.contains("ROLE_MEMBER")) {
//			response.sendRedirect("/java/member.do");
//			return;
//		}
//		
//		
//		
//		
//		response.sendRedirect("/java/index.do");
//		
//		
//		
//		
//	
		//3. 로그인 전 요청했던 URL로 이동하기
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		if (savedRequest != null) {
			//로그인 전 URL 존재
			response.sendRedirect(savedRequest.getRedirectUrl());
		} else {
			response.sendRedirect("/trip/"); //***
			
		}
	
	
}
}