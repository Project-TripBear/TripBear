package com.project.trip.auth;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        AuthenticationException exception) 
            throws IOException {

        String errorMessage = "";
        Throwable cause = exception.getCause(); // 💡 래핑된 원본 예외를 가져옵니다.

        // 1. 원본 예외가 InternalAuthenticationServiceException인지 확인 (DisabledException을 포함하는 경우)
        if (exception instanceof InternalAuthenticationServiceException && cause instanceof DisabledException) {
            // DisabledException에 담긴 정확한 메시지를 사용합니다.
            errorMessage = cause.getMessage(); 
            
        // 2. 혹시나 InternalAuthenticationServiceException 없이 DisabledException이 바로 전달될 경우 (안전 장치)
        } else if (exception instanceof DisabledException) {
            errorMessage = exception.getMessage();
            
        // 3. 그 외 일반적인 ID/PW 오류 (BadCredentialsException 등)
        } else {
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다."; 
        }

        // 💡 URLEncoder를 사용해야 한글 인코딩 오류를 피할 수 있습니다.
        String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString());

        response.sendRedirect(request.getContextPath() + 
                              "/member/login.do?error=true&message=" + encodedMessage);
    }
}