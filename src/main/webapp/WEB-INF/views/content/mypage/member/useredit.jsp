<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css">

</head>
<body>
	<div class="page-useredit-container">
	<div id="main">
		<h1>회원정보 <small>수정하기</small></h1>
		
		<form method="POST" action="/trip/member/useredit" id="editForm">
		<table class="vertical edit-form-table"> <tr>
				<th>암호</th>
				<td><input type="password" name="pw" id="pw" required class="short form-control"></td> </tr>
			<tr>
				<th>암호 확인</th>
				<td><input type="password" id="pw_check" required class="short form-control"></td>
			</tr>
			<tr>
				<th>이름</th>
				<td><input type="text" name="name" id="name" required class="short form-control" value="${dto.name}"></td>
			</tr>
			
			<tr>
				<th>이메일</th>
				<td>
					<div class="input-group email-input-group"> 
						<input type="email" name="email" id="email" required class="long form-control" value="${dto.email}">
						<input type="button" value="인증 메일 보내기" id="btnMail" class="btn btn-secondary btn-small">
					</div>
					<div class="input-group validation-group">
						<input type="text" id="validNumber" class="short form-control" disabled maxlength="5"> 
						<input type="button" value="입력하기" id="btnValid" disabled class="btn btn-secondary btn-small"> 
						<span id="remainTime" class="validation-timer" style="display: none;">05:00</span>
					</div>
				</td>
			</tr>
			
			<tr>
				<th>전화번호</th>
				<td><input type="text" name="phoneNumber" id="phoneNumber" class="long form-control" value="${dto.phoneNumber}"></td>
			</tr>
			<tr>
				<th>닉네임</th>
			<td><input type="text" name="nickName" id="nickName" class="short form-control" value="${dto.nickName}"></td>			
			</tr>
			
			<tr>
   <th>주소</th>
    <td>

      <input type="text" name="address" id="address" 

               placeholder="주소 검색 버튼을 눌러주세요."
readonly class="long form-control input-address" 

               value="${dto.address}">
        
        <button type="button" id="btn-address-search" class="btn btn-secondary btn-small">주소 검색</button>     </td>
</tr>
			
			<tr>
				<th>성별</th>
				<td>
					<label class="radio-label"><input type="radio" name="gender" value="m" required ${dto.gender == 'm' ? 'checked' : ''}> 남자</label>
					<label class="radio-label"><input type="radio" name="gender" value="f" ${dto.gender == 'f' ? 'checked' : ''}> 여자</label>
				</td>
			</tr>


			<tr>
				<th>키</th>
				<td><input type="number" name="height" id="height" class="short form-control" min="0" placeholder="숫자만 입력" value="${dto.height}"> cm</td>
			</tr>
			
			<tr>
				<th>몸무게</th>
				<td><input type="number" name="weight" id="weight" class="short form-control" min="0" placeholder="숫자만 입력" value="${dto.weight}"> kg</td>
			</tr>

			<tr>
				<th>건강목표</th>
				<td><input type="text" name="healthGoals" id="healthGoals" class="long form-control" value="${dto.healthGoals}"></td>
			</tr>
		</table>
		<div class="action-buttons-group"> 
			<button type="submit" class="btn btn-primary">수정하기</button> 
			<button type="button" class="btn btn-secondary" onclick="location.href='/trip/member/userinfo.do';">돌아가기</button>
			</div>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
			
		
		</form>
	</div>
</div>
	
	
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script>
    // '주소 검색' 버튼 클릭 이벤트 (기존 코드)
    document.getElementById('btn-address-search').addEventListener('click', 
function() {
        new daum.Postcode({
            oncomplete: function(data) {
                let addr = '';
                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else { 
                    addr = data.jibunAddress;
                }
                document.getElementById("address").value = addr;
            }
        }).open();
    });

	// ============== [ (★) 추가된 스크립트 ] ==============
	
	// (★) 수정 페이지용 이메일 인증 로직
	const originalEmail = "${dto.email}"; // 원본 이메일 저장
	let isEmailValid = true; // (★) 수정 페이지에서는 기본값을 true로 설정
	let timer = 0;
	
	const contextPath = '${pageContext.request.contextPath}';
	const csrfToken = $('input[name="${_csrf.parameterName}"]').val();
	const csrfHeader = '${_csrf.parameterName}';

	// (★) 이메일 입력창에 변경이 감지되면
	$('#email').on('input', function() {
		if ($(this).val() !== originalEmail) {
			// 이메일이 원본과 달라지면, 인증 상태를 false(미인증)로 변경
			isEmailValid = false;
			$('#btnMail').prop('disabled', false); // 인증 버튼 활성화
		} else {
			// 이메일이 원본과 다시 같아지면, 인증 상태를 true(인증됨)로 변경
			isEmailValid = true;
			
			// 진행 중이던 인증 작업 초기화 (선택적)
			clearInterval(timer);
			timer = 0;
			$('#remainTime').hide();
			$('#validNumber').val('').prop('disabled', true);
			$('#btnValid').prop('disabled', true);
		}
	});

	// '인증 메일 보내기' 버튼 클릭 (register.jsp와 동일)
	$('#btnMail').click(() => {
		if ($('#email').val().trim() != '') {
			
			let sendData = { email: $('#email').val().trim() };
			sendData[csrfHeader] = csrfToken;
			
			$.ajax({
				url: contextPath + '/member/mail/sendmail',
				type: 'POST',
				data: sendData,
				dataType: 'json',
				success: function(result) {
					if (result.result > 0) {
						alert('인증 메일이 발송되었습니다. 5분 안에 입력해주세요.');
						$('#validNumber').prop('disabled', false);
						$('#btnValid').prop('disabled', false);
						$('#remainTime').show();
						
						const remainTime = new Date();
						remainTime.setMinutes(0);
						remainTime.setSeconds(300); // 5분
						
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
								$.ajax({
									type: 'POST',
									url: contextPath + '/member/mail/delmail',
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
									error: function(a,b,c) { console.log(a,b,c); }
								});
							}
						}, 1000);
					} else {
						alert('인증 메일 발송에 실패했습니다.');
					}
				},
				error: function(a,b,c) { console.log(a,b,c); }
			});
		} else {
			alert('이메일을 입력하세요.');
		}		
	});

	// '입력하기' (인증번호 확인) 버튼 클릭 (register.jsp와 동일)
	$('#btnValid').click(() => {
		let validData = { validNumber: $('#validNumber').val().trim() };
		validData[csrfHeader] = csrfToken;
		
		$.ajax({
			type: 'POST',
			url: contextPath + '/member/mail/validmail',
			data: validData,
			dataType: 'json',
			success: function(result) {
				if (result.result > 0) {
					 alert('인증에 성공했습니다.');
	             isEmailValid = true; // (★) 인증 성공 시 true로 변경
	             
	             clearInterval(timer);
	             $('#remainTime').hide();
	             $('#validNumber').prop('disabled', true); 
	             $('#btnValid').prop('disabled', true);
	                // (★) 인증 성공 시 이메일 수정을 막는 것도 좋습니다.
	                // $('#email').prop('readonly', true); 
				} else {
					alert('인증 번호가 틀립니다.');
				}
			},
			error: function(a,b,c) { console.log(a,b,c); }
		});
	});
	// ============================================


    // (★) 폼 제출 시 유효성 검사 (기존 비밀번호 검사 + 이메일 검사 병합)
    // 기존 vanilla JS -> jQuery 방식으로 변경
    $('#editForm').submit(function(event) {
    	
        // 1. 비밀번호 일치 확인 (기존 로직)
        var pw = $('#pw').val();
        var pwCheck = $('#pw_check').val();

        // (useredit.jsp) pw는 'required'
        if (pw !== pwCheck) {
            alert('비밀번호가 일치하지 않습니다. 다시 확인해주세요.');
            $('#pw_check').focus(); // 확인 필드에 포커스
            event.preventDefault(); // 폼 제출을 막습니다.
            return false;
        }
        
        // 2. (★) 이메일 인증 확인 (수정된 로직)
        if (!isEmailValid) {
			alert('이메일이 변경되었습니다. 이메일 인증을 진행하세요.');
			event.preventDefault();
			$('#email').focus();
			return false;
		}
        
    });
    
</script>
</body>
</html>