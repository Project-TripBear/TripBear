package com.project.trip.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
	private String defaultTargetUrl = "/trip/";
	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		System.out.println("로그인을 성공하셨습니다.");
		
		String targetUrl = determineTargetUrl(request, response);
		
		// 사용된 SavedRequest 제거
		requestCache.removeRequest(request, response);
		
		response.sendRedirect(targetUrl);
	}
	
	private String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		
		// 1순위: SavedRequest (Spring Security가 저장한 원래 요청 URL)
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			System.out.println("SavedRequest URL: " + targetUrl);
			return targetUrl;
		}
		
		// 2순위: 세션에 저장된 이전 URL (직접 저장한 경우)
		HttpSession session = request.getSession(false);
		if (session != null) {
			String redirectUrl = (String) session.getAttribute("REDIRECT_URL_AFTER_LOGIN");
			if (redirectUrl != null && !redirectUrl.isEmpty()) {
				session.removeAttribute("REDIRECT_URL_AFTER_LOGIN");
				System.out.println("Session saved URL: " + redirectUrl);
				return redirectUrl;
			}
		}
		
		// 3순위: Referer 헤더
		String referer = request.getHeader("referer");
		if (referer != null && !referer.isEmpty()) {
			// 로그인 관련 URL이 아닌 경우에만 사용
			if (!referer.contains("/login") && 
			    !referer.contains("/logout") &&
			    !referer.contains("/auth")) {
				System.out.println("Referer URL: " + referer);
				return referer;
			}
		}
		
		// 4순위: 기본 URL
		System.out.println("Default URL: " + defaultTargetUrl);
		return defaultTargetUrl;
	}
}