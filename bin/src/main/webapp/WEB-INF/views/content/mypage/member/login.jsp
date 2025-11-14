<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<!-- login.jsp -->
<h2>로그인</h2>
<c:if test="${param.error != null && param.message != null}">
    <script>
        // 💡 URL 인코딩된 메시지를 JavaScript에서 디코딩하여 사용합니다.
        var message = decodeURIComponent('${param.message}');
        
        alert(message);
        
        // 메시지를 띄운 후 URL에서 파라미터를 제거하여 깔끔하게 만듭니다. (선택 사항)
        if (window.history.replaceState) {
            window.history.replaceState(null, null, window.location.pathname);
        }
    </script>
</c:if>
<form method="POST" action="/trip/login">
 <table class="vertical content">
	<tr>
		<th>아이디</th>
		<td><input type="text" name="username" required class="short"></td>
	</tr>
	<tr>
		<th>암호</th>
		<td><input type="password" name="password" required class="short"></td>
	</tr>
</table>
<div>
	<button>로그인</button>
</div>
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>

<div class="find-links">
			<button type="button" onclick="location.href='/trip/member/findid';">아이디 찾기</button>
			<button type="button" onclick="location.href='/trip/member/findpw';">비밀번호 찾기</button>
		</div>

