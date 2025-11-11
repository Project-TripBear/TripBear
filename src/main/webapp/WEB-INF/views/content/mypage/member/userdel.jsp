<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>

</head>
<body>
	<!-- del.jsp -->
	
	<form method="POST" action="/trip/member/userdel">
	<div id="main">
		<h1>탈퇴하기</h1>
		
		<div>
        <label for="pw">비밀번호 입력</label>
        <input type="password" name="pw" id="pw" required>
    </div>
    <div>
        <label for="name">탈퇴사유</label>
        <textarea name="name" id="name" required></textarea>
    </div>
    
    <div class="button-group">
        <button type="button" class="back" onclick="location.href='/trip/member/userinfo';">돌아가기</button>
        <button type="submit" class="del primary">삭제하기</button>
    </div>
    <input type="hidden" name="seq" value="${seq}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</div>
	</form>
		
</body>
</html>