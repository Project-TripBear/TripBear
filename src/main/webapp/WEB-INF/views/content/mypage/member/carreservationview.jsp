<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">	
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css">

</head>
<body>
<!-- view.jsp -->

<%-- <nav class="board-sub-header">
    <div class="sub-header-inner">
        <a href="/trip/member/carreservation">렌트카 예약</a>
        <a href="/trip/member/accomreservation">숙소 예약</a>
    </div>
</nav>

<div id="main">
<h1>차량 예약</h1>

<table id="view" class="borad-table">
<tr>
<th>예약번호</th>
<td>${dto.seq}</td>
</tr>
<tr>
<th>차종</th>
<td>${dto.cartype}</td>
</tr>
<tr>
<th>차량 모델명</th>
<td>${dto.carname}</td>
</tr>
<tr>
<th>유종</th>
<td>${dto.carfueltype}</td>
</tr>
<tr>
<th>차량 대여일</th>
<td>${dto.pickupdate}</td>
</tr>
<tr>
<th>차량 반납일</th>
<td>${dto.dropoffdate}</td>
</tr>
<tr>
<th>차량 픽업 장소</th>
<td>${dto.pickuplocation}</td>
</tr>
<tr>
<th>차량 반납 장소</th>
<td>${dto.dropofflocation}</td>
</tr>
<tr>
<th>총비용</th>
<td>${dto.cartotalprice}</td>
</tr>
<tr>
<th>요청사항</th>
<td>${dto.carnotes}</td>
</tr>
</table>

<div>
<button type="button" class="back" onclick="location.href='/trip/member/carreservation';">돌아가기</button>
<button type="button" class="back" id="btnCancel" onclick="cancel(${dto.carseq});">예약취소하기</button>
</div> --%>

<div class="page-carview-container"> <nav class="board-sub-header">
        <div class="sub-header-inner">
            <a href="/trip/member/carreservation" class="active">렌트카 예약</a>
            <a href="/trip/member/accomreservation">숙소 예약</a>
        </div>
    </nav>

    <div id="main">
        <h1>차량 예약 상세 내역</h1> <table id="view" class="vertical view-detail-table"> <tr><th>예약번호</th><td>${dto.seq}</td></tr>
            <tr><th>차종</th><td>${dto.cartype}</td></tr>
            <tr><th>차량 모델명</th><td>${dto.carname}</td></tr>
            <tr><th>유종</th><td>${dto.carfueltype}</td></tr>
            <tr><th>차량 대여일</th><td>${dto.pickupdate}</td></tr>
            <tr><th>차량 반납일</th><td>${dto.dropoffdate}</td></tr>
            <tr><th>차량 픽업 장소</th><td>${dto.pickuplocation}</td></tr>
            <tr><th>차량 반납 장소</th><td>${dto.dropofflocation}</td></tr>
            
            <tr class="highlight-row"> <th>총비용</th>
                <td><span class="price-value">${dto.cartotalprice}</span> 원</td>
            </tr>
            <tr>
                <th>요청사항</th>
                <td class="notes-cell">${dto.carnotes}</td>
            </tr>
        </table>

        <div class="action-buttons-group"> <button type="button" class="btn btn-secondary" onclick="location.href='/trip/member/carreservation';">돌아가기</button>
            <button type="button" class="btn btn-danger" id="btnCancel" onclick="cancel(${dto.carseq});">예약 취소하기</button> </div>
        
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
function cancel(carseq) {
    
    // 1. 사용자에게 취소 여부를 물어보는 확인 창을 띄웁니다.
    if (confirm("렌트카 예약을 취소하시겠습니까?")) {
        
        // CSRF 토큰 가져오기
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        
        // CSRF 토큰이 없는 경우 처리
        if (!token || !header) {
            console.error("CSRF 토큰을 찾을 수 없습니다.");
            alert("보안 토큰을 찾을 수 없습니다. 페이지를 새로고침 해주세요.");
            return;
        }
        
        // '확인'을 눌렀을 경우에만 ajax 요청을 보냅니다.
        $.ajax({
            type: 'POST',
            url: '/trip/member/carcancel',
            data: { carseq: carseq },
            beforeSend: function(xhr) {
                // CSRF 토큰 설정
                xhr.setRequestHeader(header, token);
            },
            success: function(result) {
                // 2. 서버에서 성공적으로 취소 처리가 완료되면 실행됩니다.
                if (result === 'success') {
                    alert('예약이 성공적으로 취소되었습니다.');
                    location.href = '/trip/member/carreservation'; 
                } else {
                    alert('예약 취소에 실패했습니다.');
                }
            },
            error: function(a, b, c) {
                console.log(a, b, c);
                alert('요청 처리 중 오류가 발생했습니다.');
            }
        });
    }
}
</script>

</body>
</html>