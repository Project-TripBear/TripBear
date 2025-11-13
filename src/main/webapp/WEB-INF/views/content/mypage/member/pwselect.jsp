<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
</head>
<body>
	<div class="page-findpw-container">
	
	<div id="main"> 
		<h1>비밀번호 찾기</h1>
		
		<form>
		<table class="board-title">
			
			<tr>
				<th>아이디</th>
				<td><input type="text" name="id" id="id" required class="short"></td>
			</tr>
			
			<tr>
				<th>이메일</th>
				<td>
					<div class="email-box"> <input type="email" name="email" id="email" required class="long">
					</div>
					</td>
			</tr>
			
		</table>
		
		<div class="action-buttons">
		    <button type="button" class="btn btn-primary" id="btnPwSearch">임시 비밀번호 발급</button>
    <button type="button" class="btn btn-secondary" onclick="location.href='${pageContext.request.contextPath}/member/login';">돌아가기</button>
</div>
		
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
	</div>
	
</div>
	
	
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
	
	// (★) 이메일 인증 관련 JS 코드 (btnMail, btnValid, timer 등) 모두 제거

	
	$('#btnPwSearch').click(() => {
		
		// (★) CSRF 토큰과 ContextPath를 '클릭 시점'에 읽어옵니다.
		const contextPath = '${pageContext.request.contextPath}';
		const csrfToken = $('input[name="${_csrf.parameterName}"]').val();
		const csrfHeader = '${_csrf.parameterName}';

		const id = $('#id').val().trim();
		const email = $('#email').val().trim();
		
		// 1. 아이디와 이메일을 입력했는지 확인
		if (id === '' || email === '') {
	        alert('아이디와 이메일을 모두 입력하세요.');
	        return;
	    }

		// (★) 전송할 데이터 (아이디, 이메일, CSRF 토큰)
		let findData = {
	        id: id,
	        email: email
	    };
		findData[csrfHeader] = csrfToken;

	    // 2. 서버로 비밀번호 찾기 AJAX 요청을 보냅니다.
	    $.ajax({
	        type: 'POST',
	        url: contextPath + '/member/mail/findpw', // (★) MailController에 만든 Spring URL
	        data: findData,
	        dataType: 'json',
	        success: function(result) {
	            
	            // 3. 서버로부터 받은 결과(result)에 따라 안내창을 띄웁니다.
	            if (result.result == 1) {
	                // 성공
	                alert('입력하신 이메일로 임시 비밀번호를 발송했습니다. 메일을 확인해주세요.');
	                location.href = contextPath + '/member/login'; // 로그인 페이지로 이동
	            } else {
	                // 실패
	                alert('일치하는 회원 정보가 없거나 사용이 불가능한 계정입니다. 다시 확인해주세요.');
	            }
	        },
	        error: function(a, b, c) {
	            console.log(a, b, c);
	            alert('비밀번호를 찾는 중 오류가 발생했습니다.');
	        }
	    });

	});

</script>
</body>
</html>