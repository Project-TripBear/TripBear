<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/reservation.css">


<c:set var="userRouteId" value="${param.userRouteId}" />

<div class="confirm-wrapper">
	<h2 class="confirm-title">예약 정보 확인</h2>

	<div class="confirm-grid">
		<!-- 왼쪽 정보 -->
		<div class="info-column fadeUp">
			<!-- 숙소 정보 -->
			<div class="info-card with-image">
				<div class="info-text">
					<h3>🏠 숙소 정보</h3>
					<p><strong>숙소명:</strong> ${data.room.accomName}</p>
					<p><strong>객실명:</strong> ${data.room.roomName}</p>
					<p><strong>체크인:</strong> ${param.checkin}</p>
					<p><strong>체크아웃:</strong> ${param.checkout}</p>
				</div>
				<div class="info-img">
					<c:choose>
						<c:when test="${not empty data.room.imageUrl}">
							<img src="${pageContext.request.contextPath}/resources/img/room/${data.room.imageUrl}"
							     alt="${data.room.imageUrl}">
						</c:when>
						<c:otherwise>
							<img src="${pageContext.request.contextPath}/resources/img/room/default-hotel.jpg"
							     alt="기본 숙소 이미지">
						</c:otherwise>
					</c:choose>
				</div>
			</div>

			<!-- 차량 정보 -->
			<c:if test="${data.car != null}">
				<div class="info-card with-image fadeUp">
					<div class="info-text">
						<h3>🚗 차량 정보</h3>
						<p><strong>차량명:</strong> ${data.car.carName}</p>
						<p><strong>차종:</strong> ${data.car.carType}</p>
						<p><strong>연료:</strong> ${data.car.fuelType}</p>
						<p><strong>대여기간:</strong> ${data.checkin} ~ ${data.checkout}</p>
					</div>
					<div class="info-img">
						<c:choose>
							<c:when test="${not empty data.car.carImageUrl}">
								<img src="${pageContext.request.contextPath}/resources/img/car/${data.car.carImageUrl}"
								     alt="${data.car.carName}">
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/resources/img/car/default-car.jpg"
								     alt="기본 차량 이미지">
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:if>
		</div>

		<!-- 오른쪽 결제/확정 -->
		<div class="action-column fadeUp">
			<form action="${pageContext.request.contextPath}/reservation/complete" method="post">
				<!-- CSRF -->
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

				<!-- 숙소 요청사항 -->
				<h3 class="section-title">🏠 숙소 요청사항</h3>
				<textarea name="accomNotes" placeholder="숙소 측에 전달할 요청사항을 입력하세요.
(예: 고층 객실 선호, 조식 포함 등)" rows="3"></textarea>

				<!-- 차량 요청사항 및 픽업 선택 -->
				<c:if test="${data.car != null}">
					<h3 class="section-title">🚘 렌터카 대여 정보</h3>

					<div class="pickup-section">
						<label>픽업 장소</label>
						<select name="pickupLocation">
							<option value="서울역">서울역</option>
							<option value="인천공항">인천공항</option>
							<option value="부산역">부산역</option>
							<option value="제주공항">제주공항</option>
						</select>

						<label>반납 장소</label>
						<select name="dropoffLocation">
							<option value="서울역">서울역</option>
							<option value="인천공항">인천공항</option>
							<option value="부산역">부산역</option>
							<option value="제주공항">제주공항</option>
						</select>
					</div>

					<label for="carNotes">차량 요청사항</label>
					<textarea id="carNotes" name="carNotes"
						placeholder="렌터카 업체에 전달할 요청사항을 입력하세요. (예: 네비게이션 포함, 유아용 카시트 필요 등)"
						rows="3"></textarea>
				</c:if>

				<!-- 결제 금액 -->
				<div class="price-box">
					<p>💰 총 결제 금액</p>
					<h2><fmt:formatNumber value="${totalPrice}" type="number" /> 원</h2>
				</div>

				<!-- hidden inputs -->
				<input type="hidden" name="region" value="${param.region}">
				<input type="hidden" name="checkin" value="${param.checkin}">
				<input type="hidden" name="checkout" value="${param.checkout}">
				<input type="hidden" name="roomId" value="${param.roomId}">
				<input type="hidden" name="rentalStart" value="${param.rentalStart}">
				<input type="hidden" name="rentalEnd" value="${param.rentalEnd}">
				<input type="hidden" name="carId" value="${param.carId}">
				<input type="hidden" name="userRouteId" value="${userRouteId}">
				
				
				<!-- 버튼 -->
				<div class="btn-wrap">
					<button type="submit" class="confirm-btn">예약 확정하기</button>
					<button type="button" class="back-btn" onclick="history.back()">뒤로가기</button>
				</div>
			</form>
		</div>
	</div>
</div>

