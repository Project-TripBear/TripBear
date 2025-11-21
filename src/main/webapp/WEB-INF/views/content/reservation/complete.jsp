<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/reservation.css">

<div class="complete-wrapper">
    <h2 class="complete-title">예약이 완료되었습니다 🎉</h2>

    <div class="complete-info">
        <p><strong>예약번호:</strong> ${reservationId}</p>
        <p>${region} / ${checkin} ~ ${checkout}</p>
    </div>

    <div class="btn-box">
        <a href="${pageContext.request.contextPath}/member/carreservation" class="link-btn">내 예약 내역 보기</a>
        <a href="${pageContext.request.contextPath}/" class="link-btn">홈으로 가기</a>
    </div>
</div>
