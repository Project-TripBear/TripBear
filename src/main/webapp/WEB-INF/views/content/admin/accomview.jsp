<%-- 파일 경로: /WEB-INF/views/content/admin/accomview.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1><i class="fa-solid fa-magnifying-glass-location"></i> 숙소 상세 정보</h1>

<div class="detail-container">

    <!-- ======================= -->
    <!-- 객실 이미지 (File Upload) -->
    <!-- ======================= -->
    <div class="form-section card">
        <h3 class="form-section-title">대표 객실 이미지 (Room Image)</h3>
        <div class="detail-image-wrapper">
            <c:choose>
                <c:when test="${not empty dto.roomImageUrl}">
                    <img src="${pageContext.request.contextPath}/resources/img/room/${dto.roomImageUrl}"
                         alt="${dto.roomName} 이미지" class="detail-image">
                </c:when>
                <c:otherwise>
                    <div class="detail-image img-error"></div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- ======================= -->
    <!-- 객실 기본 정보 -->
    <!-- ======================= -->
    <div class="form-section card">
        <h3 class="form-section-title">객실 정보 (Room)</h3>
        <div class="detail-grid-2col">
            <div class="detail-item">
                <label>객실명</label>
                <p>${dto.roomName}</p>
            </div>
            <div class="detail-item">
                <label>객실 타입</label>
                <p>${dto.roomType}</p>
            </div>
            <div class="detail-item">
                <label>수용인원</label>
                <p>${dto.capacity} 명</p>
            </div>
            <div class="detail-item">
                <label>1박 요금</label>
                <p><fmt:formatNumber value="${dto.pricePerNight}" pattern="#,###원"/></p>
            </div>
            <div class="detail-item">
                <label>객실 크기</label>
                <p>${dto.roomArea}</p>
            </div>
            <div class="detail-item">
                <label>객실 상태</label>
                <p>
                    <c:if test="${dto.roomStatus == 'y'}">판매중</c:if>
                    <c:if test="${dto.roomStatus == 'n'}">판매 중지</c:if>
                </p>
            </div>
        </div>
    </div>

    <!-- ======================= -->
    <!-- 숙소 정보 (Place + Accom) -->
    <!-- ======================= -->
    <div class="form-section card">
        <h3 class="form-section-title">숙소 정보 (Place & Accom)</h3>

        <!-- 숙소 대표 이미지 표시 (File Upload) -->
        <div class="detail-image-wrapper" style="margin-bottom:20px;">
            <c:choose>
                <c:when test="${not empty dto.placeMainImageUrl}">
                    <img src="${pageContext.request.contextPath}/resources/img/accom/${dto.placeMainImageUrl}"
                         alt="숙소 대표 이미지" class="detail-image">
                </c:when>
                <c:otherwise>
                    <div class="detail-image img-error"></div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="detail-grid-2col">
            <div class="detail-item">
                <label>숙소명</label>
                <p>${dto.placeName}</p>
            </div>
            <div class="detail-item">
                <label>숙소 유형</label>
                <p>${dto.accomType}</p>
            </div>
            <div class="detail-item full-width">
                <label>주소</label>
                <p>${dto.placeAddress}</p>
            </div>
            <div class="detail-item">
                <label>숙소 전화번호</label>
                <p>${dto.accomTel}</p>
            </div>
            <div class="detail-item full-width">
                <label>숙소 설명</label>
                <p>${dto.placeDescription}</p>
            </div>
            <div class="detail-item">
                <label>위도(Lat)</label>
                <p>${dto.placeLat}</p>
            </div>
            <div class="detail-item">
                <label>경도(Lng)</label>
                <p>${dto.placeLng}</p>
            </div>
        </div>
    </div>

    <!-- 버튼 -->
    <div class="button-container">
        <button type="button" class="btn"
                onclick="location.href='${pageContext.request.contextPath}/admin/accom/list'">
            목록으로
        </button>

        <button type="button" class="btn primary"
                onclick="location.href='${pageContext.request.contextPath}/admin/accom/edit?roomId=${dto.roomId}'">
            수정하기
        </button>
    </div>

</div>
