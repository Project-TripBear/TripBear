<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>예약 상세</title>
</head>
<body>

	<h2>예약 상세 정보</h2>

	<h3>예약 기본 정보</h3>
	<p>예약번호: ${data.reservation.reservationId}</p>
	<p>기간: ${data.reservation.reservationStartDate} ~
		${data.reservation.reservationEndDate}</p>
	<p>총 금액: ${data.reservation.reservationPrice}</p>

	<hr>

	<h3>숙소 예약 정보</h3>
	<p>숙소명: ${data.accomReservation.accomName}</p>
	<p>객실명: ${data.accomReservation.roomName}</p>
	<p>체크인: ${data.accomReservation.checkinDate}</p>
	<p>체크아웃: ${data.accomReservation.checkoutDate}</p>

	<hr>

	<c:if test="${data.carReservation ne null}">
		<h3>차량 예약 정보</h3>
		<p>차량명: ${data.carReservation.carName}</p>
		<p>대여일자: ${data.carReservation.pickupDate} ~
			${data.carReservation.dropoffDate}</p>
	</c:if>

</body>
</html>
