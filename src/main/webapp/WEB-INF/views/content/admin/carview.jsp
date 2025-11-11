<%-- 파일 경로: /WEB-INF/views/content/admin/carview.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 이 페이지 전용 CSS --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1><i class="fa-solid fa-car-side"></i> 렌터카 상세 정보</h1>

<div class="detail-container">
    
    <div class="form-section card">
        <h3 class="form-section-title">차량 대표 이미지</h3>
        <div class="detail-image-wrapper">
            <c:choose>
                <c:when test="${not empty carDetail.carImage}">
                     <%-- DTO의 carImage 필드 사용 --%>
                    <img src="${carDetail.carImage}" alt="${carDetail.carName} 이미지" class="detail-image"
                         onerror="this.parentElement.classList.add('img-error'); this.style.display='none';">
                </c:when>
                <c:otherwise>
                    <%-- admin2.css의 .img-error::before (\f5e5) 아이콘이 표시됩니다 --%>
                    <div class="detail-image img-error"></div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="form-section card">
        <h3 class="form-section-title">차량 기본 정보</h3>
        <div class="detail-grid-2col">
            <div class="detail-item">
                <label>차량 이름 (모델명)</label>
                <p>${carDetail.carName}</p>
            </div>
            <div class="detail-item">
                <label>차량 번호</label>
                <p>${carDetail.carNumber}</p>
            </div>
            <div class="detail-item">
                <label>차종</label>
                <p>${carDetail.carType}</p>
            </div>
            <div class="detail-item">
                <label>연료 종류</label>
                <p>${carDetail.fuelType}</p>
            </div>
        </div>
    </div>
    
    <div class="form-section card">
        <h3 class="form-section-title">차량 상세 정보</h3>
        <div class="detail-grid-2col">
            <div class="detail-item">
                <label>탑승 인원</label>
                <p>${carDetail.carSeats} 명</p>
            </div>
            <div class="detail-item">
                <label>1일 대여 요금</label>
                <p><fmt:formatNumber value="${carDetail.pricePerDay}" pattern="#,###원"/></p>
            </div>
            <div class="detail-item full-width">
                <label>차량 이미지 URL</label>
                <p>${carDetail.carImage}</p>
            </div>
            <div class="detail-item">
                <label>차량 상태 (carStatus)</label>
                <p>
                    <%-- DTO에 carStatus가 'y'/'n'으로 온다고 가정 --%>
                    <c:if test="${carDetail.carStatus == 'y'}">대여 가능</c:if>
                    <c:if test="${carDetail.carStatus == 'n'}">정비/예약</c:if>
                    <c:if test="${empty carDetail.carStatus}">알 수 없음</c:if>
                </p>
            </div>
        </div>
    </div>
       
    <div class="button-container">
        <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/car/list'">목록으로</button>
        <button type="button" class="btn primary" onclick="location.href='${pageContext.request.contextPath}/admin/car/edit?carId=${carDetail.carId}'">수정하기</button>
    </div>
</div>