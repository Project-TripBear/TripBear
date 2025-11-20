// 경로: com.project.trip.admin.auth.controller.CustomAdminLoginFailureHandler.java
package com.project.trip.admin.auth.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 관리자 로그인 실패를 처리하는 핸들러입니다.
 * 로그인 실패 시 에러 메시지와 함께 `/admin/login` 페이지로 리다이렉트합니다.
 */
public class CustomAdminLoginFailureHandler implements AuthenticationFailureHandler {
    /**
     * 인증 실패 시 호출되는 메서드입니다.
     * 에러 메시지를 인코딩하여 `/admin/login` 페이지로 리다이렉트합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param exception 발생한 인증 예외
     * @throws IOException 리다이렉트 중 발생할 수 있는 입출력 예외
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        
        String errorMessage = "관리자 아이디 또는 비밀번호가 일치하지 않습니다.";
        String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString());
        response.sendRedirect(request.getContextPath() + "/admin/login?error=true&message=" + encodedMessage);
    }
}