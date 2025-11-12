<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">

</head>
<body>
	<div id="main">
<div class="page-userinfo-container"> <div id="main">
		<h1>회원정보</h1>
		
		<table class="vertical user-view-table"> <tr><th>아이디</th><td>${dto.id}</td></tr>
			<tr><th>이름</th><td>${dto.name}</td></tr>
			<tr><th>전화번호</th><td>${dto.phoneNumber}</td></tr>
			<tr><th>닉네임</th><td>${dto.nickName}</td></tr>
			<tr><th>이메일</th><td>${dto.email}</td></tr>
			<tr><th>주소</th><td>${dto.address}</td></tr>
			<tr><th>성별</th><td>${dto.gender}</td></tr>
			<tr><th>키</th><td>${dto.height}</td></tr>
			<tr><th>몸무게</th><td>${dto.weight}</td></tr>
			<tr><th>건강목표</th><td>${dto.healthGoals}</td></tr>
		</table>
	
		<div class="action-buttons-group"> 
		    <button type="button" class="btn btn-secondary" onclick="location.href='/trip/member/mypage';">돌아가기</button>
			<button type="button" onclick="location.href='/trip/member/useredit';" class="btn btn-primary">수정하기</button>
			<button type="button" onclick="location.href='/trip/member/userdel.do';" class="btn btn-danger">회원탈퇴</button> </div>
	</div>
</div>
	
	
	<script 
	src="https://code.jquery.com/jquery-3.7.1.js"></script>
	<script src="https://bit.ly/4cMuheh"></script>
	<script>
		
	</script>
</body>
</html>