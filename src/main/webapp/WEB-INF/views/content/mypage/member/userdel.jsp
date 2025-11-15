<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css">

</head>
<body>
	<!-- del.jsp -->
	
	<div class="page-userdel-container"> <form method="POST" action="/trip/member/userdel">
	<div id="main">
		<h1>탈퇴하기</h1>
		
		<div class="form-row">         <label for="pw">비밀번호 입력</label>
        <input type="password" name="pw" id="pw" required class="form-control long">
    </div>
    <div class="form-row">
        <label for="reason">탈퇴사유</label>         
<textarea name="name" id="name" required class="form-control long"></textarea>
    </div>
    
    <div class="del-form-actions">
		<button type="submit" class="btn btn-danger">삭제하기</button>         
		<button type="button" class="btn btn-secondary" onclick="location.href='/trip/member/userinfo';">돌아가기</button>    
</div>
    <input type="hidden" name="seq" value="${seq}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</div>
	</form>
</div>
</body>
</html>