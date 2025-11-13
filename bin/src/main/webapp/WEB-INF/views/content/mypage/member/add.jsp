<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- views > content > member > add.jsp -->
<h2>회원 가입</h2>

<form method="POST" action="/trip/member/addok">
	<table class="borad-title">
		<tr>
			<th>아이디</th>
			<td><input type="text" name="id" id="id" required class="short"></td>
		</tr>
		<tr>
			<th>암호</th>
			<td><input type="password" name="pw" id="pw" required
				class="short"></td>
		</tr>
		<tr>
			<th>이름</th>
			<td><input type="text" name="name" id="name" required
				class="short"></td>
		</tr>
		<tr>
			<th>주민등록번호</th>
			<td>
				<div>
					<input type="text" name="ssn" id="ssn" required class="long"
						maxlength="14" placeholder="xxxxxx-xxxxxxx">
				</div>
			</td>
		</tr>
		<tr>
			<th>이메일</th>
			<td>
				<div>
					<input type="email" name="email" id="email" required class="long">
					<input type="button" value="인증 메일 보내기" id="btnMail">
				</div>
				<div style="margin-top: 10px;">
					<input type="text" id="validNumber" class="short" disabled
						maxlength="5"> <input type="button" value="입력하기"
						id="btnValid" disabled> <span id="remainTime"
						style="display: none;">05:00</span>
				</div>
			</td>
		</tr>

		<tr>
			<th>전화번호</th>
			<td><input type="text" name="phoneNumber" id="phoneNumber"
				class="long"></td>
		</tr>
		<tr>
			<th>닉네임</th>
			<td><input type="text" name="nickName" id="nickName"
				class="short"></td>
		</tr>

		<!-- <tr>
				<th>주소</th>
				<td><input type="text" name="address" id="address" class="long"></td>
			</tr> -->

		<tr>
			<th>주소</th>
			<td><input type="text" name="address" id="address"
				placeholder="주소 검색 버튼을 눌러주세요." readonly class="long">
				<button type="button" id="btn-address-search" class="btn-primary">주소
					검색</button></td>
		</tr>

		<tr>
			<th>성별</th>
			<td><label><input type="radio" name="gender" value="m"
					required> 남자</label> <label><input type="radio"
					name="gender" value="f"> 여자</label></td>
		</tr>


		<tr>
			<th>키</th>
			<td><input type="number" name="height" id="height" class="short"
				min="0" placeholder="숫자만 입력">cm</td>
		</tr>

		<tr>
			<th>몸무게</th>
			<td><input type="number" name="weight" id="weight" class="short"
				min="0"  placeholder="숫자만 입력">kg</td>
		</tr>

		<tr>
			<th>건강목표</th>
			<td><input type="text" name="healthGoals" id="healthGoals"
				class="long"></td>
		</tr>
	</table>
	<div>
		<button>가입하기</button>
	</div>
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}">
</form>



<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
	src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script>



// '주소 검색' 버튼 클릭 이벤트
document.getElementById('btn-address-search').addEventListener('click', function() {
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

/* // 주민등록번호 유효성 검사 함수
function checkSSN(ssn) {
	const ssnStr = ssn.replace('-', ''); // 하이픈(-) 제거
	if (ssnStr.length !== 13) return false;
	const weights = [2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5];
	let sum = 0;
	for (let i = 0; i < 12; i++) {
		sum += parseInt(ssnStr.charAt(i), 10) * weights[i];
	}
	let checkDigit = 11 - (sum % 11);
	if (checkDigit >= 10) checkDigit = checkDigit % 10;
	return checkDigit === parseInt(ssnStr.charAt(12), 10); 

// 주민등록번호 입력 시 자동 하이픈(-) 추가
$('#ssn').on('input', function() {
	let ssn = $(this).val().replace(/[^0-9]/g, '');
	if (ssn.length > 13) ssn = ssn.substring(0, 13);
	if (ssn.length > 6) {
		$(this).val(ssn.substring(0, 6) + '-' + ssn.substring(6));
	} else {
		$(this).val(ssn);
	}
}); */


//(★) 주민등록번호 유효성 검사 함수 (테스트용: 13자리 숫자만 확인)
function checkSSN(ssn) {
	const ssnStr = ssn.replace(/-/g, ''); // 하이픈(-) 모두 제거
	
	// 13자리 숫자인지 정규식으로 확인합니다.
	const regex = /^[0-9]{13}$/; // ^(시작), [0-9]{13}(숫자 13개), $(끝)
	
	return regex.test(ssnStr); // 13자리 숫자가 맞으면 true, 아니면 false
}

// (★) 주민등록번호 입력 시 자동 하이픈(-) 추가
$('#ssn').on('input', function() {
	let ssn = $(this).val().replace(/[^0-9]/g, '');
	if (ssn.length > 13) ssn = ssn.substring(0, 13);
	if (ssn.length > 6) {
		$(this).val(ssn.substring(0, 6) + '-' + ssn.substring(6));
	} else {
		$(this).val(ssn);
	}
});

let timer = 0;
let isEmailValid = false; // 이메일 인증 여부
let isIdValid = false; // 아이디 중복 확인 여부

// (★) 아이디 중복 검사 (경합 상태 해결 버전)
$('#id').on('blur', function() {
	
	const id = $(this).val().trim();
	const $msg = $('#idMessage');
	
	// (★) 가입하기 버튼을 찾습니다. (이 코드는 form 태그 안에 <button>이 하나만 있다고 가정)
	const $submitBtn = $('form button'); 
	
	const contextPath = '${pageContext.request.contextPath}';
	const csrfToken = $('input[name="${_csrf.parameterName}"]').val();
	const csrfHeader = '${_csrf.parameterName}';
	
	if (id === '') {
		$msg.text('');
		isIdValid = false;
		return;
	}
	
	let sendData = { id: id };
	sendData[csrfHeader] = csrfToken;

	$.ajax({
		type: 'POST',
		url: contextPath + '/member/idCheck',
		data: sendData,
		dataType: 'json',
		
		// (★) 1. 요청 시작 시, 버튼을 비활성화하고 "확인 중" 메시지 표시
		beforeSend: function() {
			isIdValid = false; // 일단 false로 리셋
			$msg.text('확인 중...').css('color', 'orange');
			$submitBtn.prop('disabled', true);
		},
		
		success: function(result) {
			if (result.result > 0) {
				$msg.text('이미 사용 중인 아이디입니다.').css('color', 'red');
				isIdValid = false;
			} else {
				$msg.text('사용 가능한 아이디입니다.').css('color', 'green');
				isIdValid = true;
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
			$msg.text('ID 검사 중 오류 발생').css('color', 'red');
			isIdValid = false;
		},
		
		// (★) 2. 요청 완료 시 (성공/실패 모두), 버튼을 다시 활성화
		complete: function() {
			$submitBtn.prop('disabled', false);
		}
	});
});

// (★) 아이디를 다시 수정하면, '유효함' 상태를 리셋
$('#id').on('input', function() {
	if (isIdValid) { // '사용 가능' 상태였다면
		isIdValid = false; // 다시 '미확인' 상태로 변경
		$('#idMessage').text('');
	}
});


$('#btnMail').click(() => {
	// ... (이메일 발송 코드는 동일) ...
		
	if ($('#email').val().trim() != '') {
		
        const contextPath = '${pageContext.request.contextPath}';
		const csrfToken = $('input[name="${_csrf.parameterName}"]').val();
		const csrfHeader = '${_csrf.parameterName}';
		
		let sendData = {
			email: $('#email').val().trim()
		};
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

$('#btnValid').click(() => {
	// ... (인증번호 확인 코드는 동일) ...
	
	const contextPath = '${pageContext.request.contextPath}';
	const csrfToken = $('input[name="${_csrf.parameterName}"]').val();
	const csrfHeader = '${_csrf.parameterName}';

	let validData = {
			validNumber: $('#validNumber').val().trim()
	};
	validData[csrfHeader] = csrfToken;
	
	$.ajax({
		type: 'POST',
		url: contextPath + '/member/mail/validmail',
		data: validData,
		dataType: 'json',
		success: function(result) {
			
			if (result.result > 0) {
				 alert('인증에 성공했습니다.');
	             isEmailValid = true; 
	             
	             clearInterval(timer);
	             $('#remainTime').hide();
	             $('#validNumber').prop('disabled', true); 
	             $('#btnValid').prop('disabled', true);
				
			} else {
				alert('인증 번호가 틀립니다.');
			}
		},
		error: function(a,b,c) { console.log(a,b,c); }
	});
});

// '가입하기' 버튼 클릭 시 최종 유효성 검사
$('form').submit((event) => {
	
	// 1. 아이디 중복 확인
	if (!isIdValid) {
		alert('아이디 중복 확인을 통과하지 못했습니다.');
		event.preventDefault();
		$('#id').focus();
		return false;
	}

	// 2. 이메일 인증 확인
	if (!isEmailValid) {
		alert('이메일 인증을 진행하세요.');
		event.preventDefault(); 
		return false;
	}
	
	// 3. 주민등록번호 유효성 검사
	const ssnVal = $('#ssn').val();
	if (!checkSSN(ssnVal)) {
		alert('주민등록번호가 유효하지 않습니다. 형식을 확인해주세요.');
		event.preventDefault();
		$('#ssn').focus();
		return false;
	}
});

</script>









