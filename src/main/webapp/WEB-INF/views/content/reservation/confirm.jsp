<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="confirm-wrapper">

    <h2 class="confirm-title">예약 정보 확인</h2>

    <!-- 숙소 정보 -->
    <div class="info-card">
        <h3>🏠 숙소 정보</h3>
        <div class="info-content">
            <p><strong>숙소명:</strong> ${data.room.accomName}</p>
            <p><strong>객실명:</strong> ${data.room.roomName}</p>
            <p><strong>인원:</strong> ${data.people}명</p>
            <p><strong>체크인:</strong> ${data.checkin}</p>
            <p><strong>체크아웃:</strong> ${data.checkout}</p>
        </div>
    </div>

    <!-- 차량 정보 (선택했을 때만) -->
    <c:if test="${data.car != null}">
        <div class="info-card">
            <h3>🚗 차량 정보</h3>
            <div class="info-content">
                <p><strong>차량명:</strong> ${data.car.carName}</p>
                <p><strong>차종:</strong> ${data.car.carType}</p>
                <p><strong>연료:</strong> ${data.car.fuelType}</p>
                <p><strong>대여기간:</strong> ${data.checkin} ~ ${data.checkout}</p>
            </div>
        </div>
    </c:if>

    <!-- 총 금액 -->
    <div class="price-section">
        <h3>💰 총 결제 금액</h3>
        <p class="price"><fmt:formatNumber value="${totalPrice}" type="number"/>원</p>
    </div>

    <!-- 버튼 영역 -->
    <div class="btn-box">
        <form action="${pageContext.request.contextPath}/reservation/complete" method="post">
            <input type="hidden" name="roomId" value="${data.room.roomId}">
            <input type="hidden" name="carId" value="${data.car != null ? data.car.carId : ''}">
            <input type="hidden" name="checkin" value="${data.checkin}">
            <input type="hidden" name="checkout" value="${data.checkout}">
            <input type="hidden" name="rentalStart" value="${rentalStart}">
            <input type="hidden" name="rentalEnd" value="${rentalEnd}">
            <input type="hidden" name="people" value="${data.people}">
            <button type="submit" class="confirm-btn">예약 확정하기</button>
        </form>
        <button type="button" class="back-btn" onclick="history.back()">뒤로가기</button>
    </div>
</div>

<style>
.confirm-wrapper {
    max-width: 700px;
    margin: 60px auto;
    padding: 40px;
    border-radius: var(--radius, 20px);
    background: #fff;
    box-shadow: var(--shadow, 0 4px 20px rgba(0,0,0,0.1));
    font-family: 'Noto Sans KR', sans-serif;
}

.confirm-title {
    text-align: center;
    font-size: 1.8rem;
    color: var(--primary, #6C9A8B);
    margin-bottom: 30px;
}

.info-card {
    border: 1px solid #ddd;
    border-radius: 16px;
    padding: 20px;
    margin-bottom: 25px;
    background-color: #f9fafb;
}

.info-card h3 {
    margin-bottom: 10px;
    color: var(--accent, #FF8C69);
}

.info-content p {
    margin: 4px 0;
    color: #333;
}

.price-section {
    text-align: center;
    margin: 30px 0;
}

.price {
    font-size: 1.6rem;
    font-weight: bold;
    color: #333;
}

.btn-box {
    display: flex;
    justify-content: center;
    gap: 20px;
}

.confirm-btn {
    background-color: var(--primary, #6C9A8B);
    color: white;
    border: none;
    border-radius: 10px;
    padding: 12px 28px;
    font-size: 1rem;
    cursor: pointer;
    transition: 0.2s;
}

.confirm-btn:hover {
    background-color: #5b8578;
}

.back-btn {
    background-color: #ddd;
    color: #333;
    border: none;
    border-radius: 10px;
    padding: 12px 28px;
    cursor: pointer;
    transition: 0.2s;
}

.back-btn:hover {
    background-color: #ccc;
}
</style>
