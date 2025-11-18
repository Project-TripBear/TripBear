package com.project.trip.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Spring Security 접근 거부(Access Denied) 처리를 담당하는 핸들러입니다.
 * <p>
 * 사용자가 접근 권한이 없는 리소스에 접근을 시도하여 {@link AccessDeniedException}이 발생했을 때 호출됩니다.
 * 현재 구현은 단순히 애플리케이션의 홈페이지로 리다이렉트하는 동작을 수행합니다.
 * </p>
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * 접근 거부 예외가 발생했을 때 호출되는 메소드입니다.
	 * <p>
	 * 이 메소드는 403 Forbidden 오류와 관련된 추가적인 로직을 수행할 수 있으며,
	 * 현재는 사용자를 애플리케이션의 루트 경로로 리다이렉트합니다.
	 * </p>
	 * @param request HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @param accessDeniedException 발생한 접근 거부 예외
	 * @throws IOException 입출력 오류 발생 시
	 * @throws ServletException 서블릿 관련 오류 발생 시
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.access.AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		
		//403  발생 > handle() 호출
		
		//403 오류 발생과 연관된 여러가지 업무를 진행
		System.out.println("403 오류 관련 처리 진행..");
		
		response.sendRedirect("/trip/");
		
		
	}

	// @Override
	// public void handle(HttpServletRequest request, HttpServletResponse response,
	// 		org.springframework.security.access.AccessDeniedException accessDeniedException)
	// 		throws IOException, ServletException {
	// 	// TODO Auto-generated method stub
	// 	throw new UnsupportedOperationException("Unimplemented method 'handle'");
	// }
	
	
	
}