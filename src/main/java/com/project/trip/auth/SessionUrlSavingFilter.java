package com.project.trip.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 사용자가 로그인하기 전에 접근하려던 URL을 세션에 저장하는 필터입니다.
 * <p>
 * 이 필터는 {@link OncePerRequestFilter}를 상속받아 요청당 한 번만 실행되며,
 * 로그인, 로그아웃, 정적 리소스 등 특정 URL 패턴을 제외한 모든 요청의 URL을
 * 세션 속성 "REDIRECT_URL_AFTER_LOGIN"에 저장합니다.
 * 이는 로그인 성공 후 사용자를 원래 접근하려던 페이지로 리다이렉트하기 위해 사용됩니다.
 * </p>
 */
public class SessionUrlSavingFilter extends OncePerRequestFilter {

	/**
	 * HTTP 요청을 필터링하여 로그인 전 URL을 세션에 저장합니다.
	 * <p>
	 * 로그인, 로그아웃, 인증 관련 페이지, 정적 리소스 등은 저장 대상에서 제외됩니다.
	 * </p>
	 * @param request HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @param filterChain 필터 체인
	 * @throws ServletException 서블릿 관련 오류 발생 시
	 * @throws IOException 입출력 오류 발생 시
	 */
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
