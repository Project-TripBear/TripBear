package com.project.trip.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

//SessionUrlSavingFilter.java
public class SessionUrlSavingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
	                               FilterChain filterChain) throws ServletException, IOException {
		
		String requestURI = request.getRequestURI();
		
		// 로그인 페이지로 이동하는 경우, 현재 URL 저장
		if (!requestURI.contains("/login") && 
		    !requestURI.contains("/logout") &&
		    !requestURI.contains("/auth") &&
		    !requestURI.contains("/static") &&
		    !requestURI.contains("/script") &&
		    !requestURI.contains("/like") &&
		    !requestURI.contains("/scrap") &&
		    !requestURI.contains("/allplace") &&
		    !requestURI.contains("/myactivitiessummary") &&
		    !requestURI.contains("/css") &&
		    !requestURI.contains("/js") &&
		    !requestURI.contains("/morecomment") &&
			!requestURI.contains("/resources") &&
			!requestURI.contains("/upload") &&  // ★★★ 추가 ★★★
			!requestURI.contains("/findpw") &&
			!requestURI.contains("/findid") &&
			!requestURI.contains("/images")){
			
			String fullUrl = requestURI;
			if (request.getQueryString() != null) {
				fullUrl += "?" + request.getQueryString();
			}
			
			request.getSession().setAttribute("REDIRECT_URL_AFTER_LOGIN", fullUrl);
		}
		
		filterChain.doFilter(request, response);
	}
}
