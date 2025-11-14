<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>

</head>
<body>
	<div class="page-useredit-container">
	<div id="main">
		<h1>개인정보 <small>수정하기</small></h1>
		
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
				<div class="input-group"> <input type="email" name="email" id="email" required class="long form-control" value="${dto.email}">
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
					<label class="radio-label"><input type="radio" name="gender" value="m" required> 남자</label>
					<label class="radio-label"><input type="radio" name="gender" value="f"> 여자</label>
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
			<button type="submit" class="btn btn-primary">수정하기</button> </div>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
			<button type="button" class="btn btn-secondary" onclick="location.href='/trip/member/userinfo.do';">돌아가기</button>
		
		</form>
	</div>
</div>
	
	
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script>
    // '주소 검색' 버튼 클릭 이벤트
    document.getElementById('btn-address-search').addEventListener('click', function() {
        new daum.Postcode({
            oncomplete: function(data) {
                let addr = ''; // 주소 변수

                // 사용자가 도로명 주소를 선택했을 경우
                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우
                    addr = data.jibunAddress;
                }

                // 검색된 주소를 '주소' input에 넣기
                document.getElementById("address").value = addr;
            }
        }).open();
    });

    
    // ============== [ 추가된 스크립트 ] ==============
    // '수정하기' 폼 제출 시 비밀번호 일치 확인
    document.getElementById('editForm').addEventListener('submit', function(event) {
    	
        var pw = document.getElementById('pw').value;
        var pwCheck = document.getElementById('pw_check').value;

        // 비밀번호 필드가 비어있지 않고, 두 비밀번호가 일치하지 않을 경우
        // (비밀번호를 변경하지 않을 경우(둘 다 빈칸)도 고려해야 하지만,
        //  현재 pw 필드에 'required'가 있으므로 빈칸 제출은 안 됩니다.)
        if (pw !== pwCheck) {
            alert('비밀번호가 일치하지 않습니다. 다시 확인해주세요.');
            document.getElementById('pw_check').focus(); // 확인 필드에 포커스
            event.preventDefault(); // 폼 제출을 막습니다.
        }
        
    });
    // ============================================
    
</script>
</body>
</html>