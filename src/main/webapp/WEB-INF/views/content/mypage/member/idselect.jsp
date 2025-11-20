<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css">
	
</head>

<div class="page-findid-container">
	
	<div id="main"> 
		<h1>아이디 찾기</h1>
		
		<form>
		<table class="board-title">
			
			<tr>
				<th>이름</th>
				<td><input type="text" name="name" id="name" required class="short"></td>
			</tr>
			
			<tr>
				<th>이메일</th>
				<td>
					<div class="email-send-box">
						<input type="email" name="email" id="email" required class="long">
						<input type="button" value="인증 메일 보내기" id="btnMail" class="btn-mail-send">
					</div>
					<div class="validation-box">
						<input type="text" id="validNumber" class="short" disabled maxlength="5">
						<input type="button" value="입력하기" id="btnValid" disabled class="btn-valid-check">
						<span id="remainTime" style="display: none;">05:00</span>
					</div>
				</td>
			</tr>
			
		</table>
		
		<div class="action-buttons">
			<button type="button" class="btn btn-primary" id="btnIdSearch">아이디 찾기</button>
			<button type="button" class="btn btn-secondary" onclick="location.href='${pageContext.request.contextPath}/member/login';">돌아가기</button>
		</div>
		
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
	</div>
	
</div>
	
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	

<script>

	let timer = 0;
	let isValid = false; // (★) 이메일 인증 완료 여부

	$('#btnMail').click(() => {
		
		if ($('#email').val().trim() != '') {
			
			// (★) CSRF 토큰과 ContextPath를 '클릭 시점'에 읽어옵니다.
            const contextPath = '${pageContext.request.contextPath}';
			const csrfToken = $('input[name="${_csrf.parameterName}"]').val();
			const csrfHeader = '${_csrf.parameterName}';
			
			let sendData = {
				email: $('#email').val().trim()
			};
			sendData[csrfHeader] = csrfToken;
			
			
			$.ajax({
				type: 'POST',
				url: contextPath + '/member/mail/sendmail', // (★) Spring URL로 변경
				data: sendData,
				dataType: 'json',
				success: function(result) {
					
					if (result.result > 0) {
						
						alert('인증 메일이 발송되었습니다. 5분 안에 입력해주세요.');
						$('#validNumber').prop('disabled', false);
						$('#btnValid').prop('disabled', false);
						$('#remainTime').show();
						
						//타이머 동작
						const remainTime = new Date();
						remainTime.setMinutes(0);
						remainTime.setSeconds(300);
						
						timer = setInterval(() => {
							
							remainTime.setSeconds(remainTime.getSeconds() - 1);
							$('#remainTime').text(
								String(remainTime.getMinutes()).padStart(2, '0')
								+ ':'
								+ String(remainTime.getSeconds()).padStart(2, '0')
							);
							
							if ($('#remainTime').text() == '00:00') {
								
								let delData = {};
								delData[csrfHeader] = csrfToken;
								
								//인증 시간 만료
								$.ajax({
									type: 'POST',
									url: contextPath + '/member/mail/delmail', // (★) Spring URL로 변경
									data: delData,
									dataType: 'json',
									success: function(result) {
																					
										if (result.result > 0) {
											alert('인증 시간이 만료되었습니다.');
											$('#validNumber').val('');
											$('#btnValid').prop('disabled', true);
											$('#validNumber').prop('disabled', true);
											$('#remainTime').hide();
											
											clearInterval(timer);
											timer = 0;
										}
										
									},
									error: function(a,b,c) {
										console.log(a,b,c);
									}
								});
							}
						}, 1000);
						
					} else {
						alert('인증 메일 발송에 실패했습니다.');
					}
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			});
			
		} else {
			alert('이메일을 입력하세요.');
		}		
		
	});
	
	$('#btnValid').click(() => {
		
		// (★) CSRF 토큰, ContextPath 읽기
		const contextPath = '${pageContext.request.contextPath}';
		const csrfToken = $('input[name="${_csrf.parameterName}"]').val();
		const csrfHeader = '${_csrf.parameterName}';
		
		let validData = {
			validNumber: $('#validNumber').val()
		};
		validData[csrfHeader] = csrfToken;

		$.ajax({
			type: 'POST',
			url: contextPath + '/member/mail/validmail', // (★) Spring URL로 변경
			data: validData,
			dataType: 'json',
			success: function(result) {
				
				if (result.result > 0) {
					 alert('인증에 성공했습니다.');
		                isValid = true;
		                
		                clearInterval(timer); 
		                $('#remainTime').hide();
		                $('#validNumber').prop('disabled', true); 
		                $('#btnValid').prop('disabled', true); 
					
				} else {
					alert('인증 번호가 틀립니다.');
				}
				
			},
			error: function(a,b,c) {
				console.log(a,b,c);
			}
		});
		
	});
	
	
	$('#btnIdSearch').click(() => {

	    // 1. 이메일 인증을 완료했는지 먼저 확인합니다.
	    if (!isValid) {
	        alert('이메일 인증을 진행하세요.');
	        return;
	    }

		// (★) CSRF 토큰, ContextPath 읽기
		const contextPath = '${pageContext.request.contextPath}';
		const csrfToken = $('input[name="${_csrf.parameterName}"]').val();
		const csrfHeader = '${_csrf.parameterName}';
		
		// (★) 전송할 데이터 (이름, 이메일, CSRF)
		let findData = {
	        name: $('#name').val(),
	        email: $('#email').val()
	    };
		findData[csrfHeader] = csrfToken;

	    // 2. 서버로 아이디 찾기 AJAX 요청을 보냅니다.
	    $.ajax({
	        type: 'POST',
	        url: contextPath + '/member/mail/findid', // (★) MailController에 만든 Spring URL
	        data: findData,
	        dataType: 'json',
	        success: function(result) {
	            
	            if (result.result == 1) {
	                alert('입력하신 이메일로 아이디 정보를 발송했습니다. 메일을 확인해주세요.');
					// (★) 로그인 페이지 URL에 ContextPath 추가
	                location.href = contextPath + '/member/login';
	            } else {
	                alert('일치하는 회원 정보가 없습니다. 이름과 이메일을 다시 확인해주세요.');
	            }
	        },
	        error: function(a, b, c) {
	            console.log(a, b, c);
	            alert('아이디를 찾는 중 오류가 발생했습니다.');
	        }
	    });

	});

</script>
</body>
</html>