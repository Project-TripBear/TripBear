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

/**
 * Spring Security 로그인 성공 후 처리를 담당하는 핸들러입니다.
 * <p>
 * 사용자가 성공적으로 로그인했을 때, 이전에 접근하려던 페이지, 세션에 저장된 URL,
 * 또는 Referer 헤더를 기반으로 리다이렉트할 URL을 결정합니다.
 * 모든 조건에 해당하지 않을 경우, 미리 정의된 기본 URL로 리다이렉트합니다.
 * </p>
 */
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
	private String defaultTargetUrl = "/trip/"; // 기본 리다이렉트 URL
	
	/**
	 * 사용자가 성공적으로 인증되었을 때 호출되는 메소드입니다.
	 * <p>
	 * 이 메소드는 로그인 성공 후 사용자를 어디로 리다이렉트할지 결정하고 해당 URL로 이동시킵니다.
	 * </p>
	 * @param request HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @param authentication 인증된 사용자 정보를 담고 있는 {@link Authentication} 객체
	 * @throws IOException 입출력 오류 발생 시
	 * @throws ServletException 서블릿 관련 오류 발생 시
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		System.out.println("로그인을 성공하셨습니다.");
		
		String targetUrl = determineTargetUrl(request, response);
		
		// 사용된 SavedRequest 제거
		requestCache.removeRequest(request, response);
		
		response.sendRedirect(targetUrl);
	}
	
	/**
	 * 로그인 성공 후 사용자를 리다이렉트할 대상 URL을 결정합니다.
	 * <p>
	 * 다음 우선순위에 따라 URL을 결정합니다:
	 * <ol>
	 *     <li>Spring Security의 {@link SavedRequest}에 저장된 원래 요청 URL</li>
	 *     <li>세션에 "REDIRECT_URL_AFTER_LOGIN" 속성으로 저장된 이전 URL</li>
	 *     <li>HTTP Referer 헤더 (로그인 관련 페이지가 아닌 경우)</li>
	 *     <li>미리 정의된 기본 대상 URL ({@code defaultTargetUrl})</li>
	 * </ol>
	 * </p>
	 * @param request HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @return 리다이렉트할 최종 URL
	 */
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