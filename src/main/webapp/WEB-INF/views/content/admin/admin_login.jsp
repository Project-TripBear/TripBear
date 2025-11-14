<%-- 경로: /WEB-INF/views/content/admin/admin_login.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<h2>관리자 로그인</h2>

<%-- 로그인 실패 시 메시지 표시 (FailureHandler와 연동) --%>
<c:if test="${param.error != null && param.message != null}">
    <script>
        var message = decodeURIComponent('${param.message}');
        alert(message);
        // URL에서 에러 파라미터 제거
        if (window.history.replaceState) {
            window.history.replaceState(null, null, window.location.pathname);
        }
    </script>
</c:if>

<%-- 
    (중요) action 경로는 security-context.xml의 
    login-processing-url과 일치해야 합니다. (예: /trip/admin/login_proc)
--%>
<form method="POST" action="<c:url value='/admin/login_proc'/>">
 
 <table class="vertical content">
	<tr>
		<th>관리자 ID</th>
		<td><input type="text" name="username" required class="short"></td>
	</tr>
	<tr>
		<th>암호</th>
		<td><input type="password" name="password" required class="short"></td>
	</tr>
</table>
<div>
	<button type="submit">로그인</button>
</div>

<%-- CSRF 토큰 (security-context.xml에 CSRF가 켜져있다면 필수) --%>
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>