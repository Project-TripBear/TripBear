// 경로: com.project.trip.admin.auth.controller.CustomAdminLoginSuccessHandler.java
package com.project.trip.admin.auth.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 관리자 로그인 성공을 처리하는 핸들러입니다.
 * 로그인 성공 시 `/admin/main` 페이지로 리다이렉트합니다.
 */
public class CustomAdminLoginSuccessHandler implements AuthenticationSuccessHandler {
    /**
     * 인증 성공 시 호출되는 메서드입니다.
     * 관리자 메인 페이지로 리다이렉트합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param authentication 인증된 사용자 정보
     * @throws IOException 리다이렉트 중 발생할 수 있는 입출력 예외
     * @throws ServletException 서블릿 관련 예외
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        response.sendRedirect(request.getContextPath() + "/admin/main");
    }
}