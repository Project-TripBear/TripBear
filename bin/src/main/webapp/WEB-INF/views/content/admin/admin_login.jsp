<%-- 파일 경로: /WEB-INF/views/content/admin/admin_login.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="login-card">
    <h1 class="login-title">관리자 로그인</h1>
    
    <form class="login-form" method="POST" action="${pageContext.request.contextPath}/admin/login_process">
        
        <div class="form-group">
            <label for="admin_username">아이디</label>
            <input type="text" id="admin_username" name="admin_username" required>
        </div>
        
        <div class="form-group">
            <label for="admin_password">비밀번호</label>
            <input type="password" id="admin_password" name="admin_password" required>
        </div>
        
        <c:if test="${not empty param.error}">
            <div style="color: red; margin-bottom: 1rem; text-align: center; font-weight: 600;">
                아이디 또는 비밀번호가<br>일치하지 않습니다.
            </div>
        </c:if>

        <button type="submit" class="btn primary btn-login">로그인</button>
        
    </form>
</div>