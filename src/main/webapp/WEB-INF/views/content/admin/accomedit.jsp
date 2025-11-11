<%-- 파일 경로: /WEB-INF/views/content/admin/accomedit.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 이 페이지 전용 CSS --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1><i class="fa-solid fa-pen-to-square"></i> 숙소 정보 수정</h1>

<div class="form-container">
    <form method="POST" action="${pageContext.request.contextPath}/admin/accom/edit" onsubmit="return removeCommasBeforeSubmit(this)">
        
        <%-- ★★★ 핵심: 수정 대상 ID들을 hidden으로 전송 ★★★ --%>
        <input type="hidden" name="placeId" value="${dto.placeId}">
        <input type="hidden" name="accomId" value="${dto.accomId}">
        <input type="hidden" name="roomId" value="${dto.roomId}">

        <div class="form-section card">
            <h3 class="form-section-title">1. 숙소 위치 정보 (Place)</h3>
            
            <div class="form-group">
                <label for="placeName">숙소명</label>
                <input type="text" id="placeName" name="placeName" required value="${dto.placeName}">
            </div>
            <div class="form-group">
                <label for="placeAddress">주소 (이 주소로 좌표가 자동 변환됩니다)</label>
                <input type="text" id="placeAddress" name="placeAddress" required value="${dto.placeAddress}">
            </div>
            
            <%-- (위도/경도, 지역/장소ID는 Service에서 자동 처리되므로 숨김) --%>
        </div>

        <div class="form-section card">
            <h3 class="form-section-title">2. 숙소 상세 정보 (Accom)</h3>
            
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="accomType">숙소 유형</label>
                    <select id="accomType" name="accomType">
                        <option value="호텔" <c:if test="${dto.accomType == '호텔'}">selected</c:if>>호텔</option>
                        <option value="민박" <c:if test="${dto.accomType == '민박'}">selected</c:if>>민박</option>
                        <option value="펜션" <c:if test="${dto.accomType == '펜션'}">selected</c:if>>펜션</option>
                        <option value="캠핑" <c:if test="${dto.accomType == '캠핑'}">selected</c:if>>캠핑</option>
                        <option value="모텔" <c:if test="${dto.accomType == '모텔'}">selected</c:if>>모텔</option>
                        <option value="풀빌라" <c:if test="${dto.accomType == '풀빌라'}">selected</c:if>>풀빌라</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="accomTel">숙소 전화번호</label>
                    <input type="tel" id="accomTel" name="accomTel" value="${dto.accomTel}" placeholder="예: 02-1234-5678">
                </div>
            </div>
            
            <div class="form-group">
                <label for="placeMainImageUrl">숙소 대표 이미지 URL</label>
                <input type="text" id="placeMainImageUrl" name="placeMainImageUrl" value="${dto.placeMainImageUrl}" placeholder="https://...">
            </div>
            <div class="form-group">
                <label for="placeDescription">숙소 설명</label>
                <textarea id="placeDescription" name="placeDescription" rows="4">${dto.placeDescription}</textarea>
            </div>
        </div>

        <div class="form-section card">
            <h3 class="form-section-title">3. 객실 정보 (Room)</h3>
            
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="roomName">객실명</label>
                    <input type="text" id="roomName" name="roomName" value="${dto.roomName}" required>
                </div>
                <div class="form-group">
                    <label for="roomType">객실 타입</label>
                    <input type="text" id="roomType" name="roomType" value="${dto.roomType}" required>
                </div>
            </div>
            
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="capacity">수용인원 (명)</label>
                    <input type="number" id="capacity" name="capacity" value="${dto.capacity}" required min="1">
                </div>
                <div class="form-group">
                     <label for="pricePerNight">1박 요금 (원)</label>
                    <input type="text" id="pricePerNight" name="pricePerNight" value="${dto.pricePerNight}" required onkeyup="formatPrice(this)">
                </div>
            </div>
            
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="roomArea">객실 크기</label>
                    <input type="text" id="roomArea" name="roomArea" value="${dto.roomArea}" placeholder="예: 25평 (72㎡)">
                </div>
                <div class="form-group">
                    <label for="roomImageUrl">객실 이미지 URL</label>
                    <input type="text" id="roomImageUrl" name="roomImageUrl" value="${dto.roomImageUrl}" placeholder="https://...">
                </div>
            </div>
            
             <div class="form-group">
                <label>객실 상태</label>
                <div class="radio-group">
                    <label><input type="radio" name="roomStatus" value="y" <c:if test="${dto.roomStatus == 'y'}">checked</c:if>> 판매중(y)</label>
                    <label><input type="radio" name="roomStatus" value="n" <c:if test="${dto.roomStatus == 'n'}">checked</c:if>> 중지(n)</label>
                </div>
            </div>
        </div>
       
        <div class="button-container">
            <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/accom/list'">취소</button>
            <button type="submit" class="btn primary">수정하기</button>
        </div>
    </form>
</div>

<script>
    function formatPrice(input) {
        let value = input.value.replace(/[^\d]/g, ''); 
        if (value === '') { input.value = ''; return; }
        input.value = Number(value).toLocaleString('en-US');
    }
    function removeCommasBeforeSubmit(form) {
        const priceInput = form.querySelector('#pricePerNight');
        if (priceInput) {
            priceInput.value = priceInput.value.replace(/,/g, ''); 
        }
        return true; 
    }
    // 페이지 로드 시 기존 가격에 콤마 적용
    document.addEventListener('DOMContentLoaded', function() {
        const priceInput = document.getElementById('pricePerNight');
        if (priceInput && priceInput.value) {
            formatPrice(priceInput);
        }
    });
</script>