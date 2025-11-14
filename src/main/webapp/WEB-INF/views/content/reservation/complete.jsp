<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="complete-wrapper">
    <h2 class="complete-title">예약이 완료되었습니다 🎉</h2>

    <div class="complete-info">
        <p><strong>예약번호:</strong> ${reservationId}</p>
        <p>${region} / ${checkin} ~ ${checkout}</p>
    </div>

    <div class="btn-box">
        <a href="${pageContext.request.contextPath}/member/carreservation" class="link-btn">내 예약 내역 보기</a>
        <a href="${pageContext.request.contextPath}/main" class="link-btn">홈으로 가기</a>
    </div>
</div>

<style>
.complete-wrapper {
    max-width: 700px;
    margin: 60px auto;
    padding: 40px;
    border-radius: var(--radius, 20px);
    background: #fff;
    box-shadow: var(--shadow, 0 4px 20px rgba(0,0,0,0.1));
    font-family: 'Noto Sans KR', sans-serif;
    text-align: center;
}

.complete-title {
    font-size: 1.8rem;
    color: var(--primary, #6C9A8B);
    margin-bottom: 30px;
}

.complete-info {
    border-top: 1px solid #eee;
    border-bottom: 1px solid #eee;
    padding: 20px 0;
    margin-bottom: 30px;
    color: #333;
    font-size: 1.1rem;
}

.btn-box {
    display: flex;
    justify-content: center;
    gap: 20px;
}

.link-btn {
    display: inline-block;
    background-color: var(--primary, #6C9A8B);
    color: white;
    border-radius: 10px;
    padding: 12px 28px;
    text-decoration: none;
    font-weight: 500;
    transition: 0.2s;
}

.link-btn:hover {
    background-color: #5b8578;
}
</style>
