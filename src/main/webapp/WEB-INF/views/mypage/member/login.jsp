<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<!-- login.jsp -->
<h2>로그인</h2>

<form method="POST" action="${pageContext.request.contextPath}/login">
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