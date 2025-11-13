// 경로: com.project.trip.admin.auth.controller.CustomAdminLoginSuccessHandler.java
package com.project.trip.admin.auth.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

// 로그인 성공 시 /admin/main으로 강제 이동
public class CustomAdminLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        response.sendRedirect(request.getContextPath() + "/admin/main");
    }
}