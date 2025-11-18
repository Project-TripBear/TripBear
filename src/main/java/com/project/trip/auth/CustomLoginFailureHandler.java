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

/**
 * Spring Security 로그인 실패 후 처리를 담당하는 핸들러입니다.
 * <p>
 * 사용자가 로그인에 실패했을 때, 실패 원인(예: 아이디/비밀번호 불일치, 계정 비활성화/차단)에 따라
 * 적절한 오류 메시지를 생성하고, 로그인 페이지로 리다이렉트하면서 해당 메시지를 전달합니다.
 * </p>
 */
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    /**
     * 사용자가 인증에 실패했을 때 호출되는 메소드입니다.
     * <p>
     * 발생한 {@link AuthenticationException}의 종류에 따라 사용자에게 보여줄 오류 메시지를 결정하고,
     * 해당 메시지를 URL 인코딩하여 로그인 페이지로 리다이렉트합니다.
     * </p>
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param exception 발생한 인증 예외
     * @throws IOException 입출력 오류 발생 시
     */
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