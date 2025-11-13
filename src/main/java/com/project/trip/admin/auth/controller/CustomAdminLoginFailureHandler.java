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

// 로그인 실패 시 /admin/login으로 메시지와 함께 리다이렉트
public class CustomAdminLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        
        String errorMessage = "관리자 아이디 또는 비밀번호가 일치하지 않습니다.";
        String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString());
        response.sendRedirect(request.getContextPath() + "/admin/login?error=true&message=" + encodedMessage);
    }
}