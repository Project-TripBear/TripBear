<%-- 경로: /WEB-INF/views/content/admin/admin_login.jsp (디자인 수정본) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div class="login-container">
    <div class="login-card">

        <h1>
            <%-- admin2.css에 Font Awesome이 포함되어 있어 아이콘을 사용합니다. --%>
            <i class="fas fa-paw"></i> TripBear
        </h1>
        <h2>관리자 페이지</h2>

        <%-- 로그인 실패 시 메시지 표시 (기존 로직 유지) --%>
        <c:if test="${param.error != null && param.message != null}">
            <script>
                var message = decodeURIComponent('${param.message}');
                alert(message);
                if (window.history.replaceState) {
                    window.history.replaceState(null, null, window.location.pathname);
                }
            </script>
        </c:if>

        <%-- 
            action 경로는 security-context.xml의 login-processing-url과 일치시킵니다.
            (예: /trip/admin/login_proc)
        --%>
        <form method="POST" action="<c:url value='/admin/login_proc'/>">
            
            <%-- 1. 아이디 입력 필드 --%>
            <div class="form-group">
                <label for="username">관리자 ID</label>
                <input type="text" id="username" name="username" required 
                       class="form-control" placeholder="아이디를 입력하세요">
            </div>
            
            <%-- 2. 비밀번호 입력 필드 --%>
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required 
                       class="form-control" placeholder="비밀번호를 입력하세요">
            </div>

            <%-- 3. CSRF 토큰 (필수) --%>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

            <%-- 4. 로그인 버튼 --%>
            <%-- admin2.css의 .btn.primary 스타일을 사용합니다. --%>
            <button type="submit" class="btn primary full-width">로그인</button>
        </form>

    </div>
</div>