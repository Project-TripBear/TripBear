<%-- 파일 경로: /WEB-INF/views/content/admin/editcar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 이 페이지 전용 CSS --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1><i class="fa-solid fa-pen-to-square"></i> 렌터카 정보 수정</h1>

<%-- addcar.jsp와 동일한 form-container 사용 --%>
<div class="form-container">
    <form method="POST" action="${pageContext.request.contextPath}/admin/car/edit" class="form-container" onsubmit="return removeCommasBeforeSubmit(this)">
        
        <%-- 수정 대상 ID를 hidden으로 전송 --%>
        <input type="hidden" name="carId" value="${carDetail.carId}">

        <div class="form-section card">
            <h3 class="form-section-title">1. 차량 기본 정보</h3>
            
            <div class="form-group">
                <label for="carName">차량 이름 (모델명)</label>
                <input type="text" id="carName" name="carName" value="${carDetail.carName}" required>
            </div>
            
            <div class="form-group">
                <label for="carNumber">차량 번호</label>
                <input type="text" id="carNumber" name="carNumber" value="${carDetail.carNumber}" required>
            </div>

            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="carType">차종</label>
                    <select id="carType" name="carType">
                        <%-- addcar.jsp와 옵션 일치 --%>
                        <option value="승용차" <c:if test="${carDetail.carType == '승용차'}">selected</c:if>>승용차</option>
                        <option value="SUV" <c:if test="${carDetail.carType == 'SUV'}">selected</c:if>>SUV</option>
                        <option value="승합차" <c:if test="${carDetail.carType == '승합차'}">selected</c:if>>승합차</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="fuelType">연료 종류</label>
                    <select id="fuelType" name="fuelType">
                        <%-- addcar.jsp와 옵션 일치 --%>
                        <option value="가솔린" <c:if test="${carDetail.fuelType == '가솔린'}">selected</c:if>>가솔린</option>
                        <option value="디젤" <c:if test="${carDetail.fuelType == '디젤'}">selected</c:if>>디젤</option>
                        <option value="전기차" <c:if test="${carDetail.fuelType == '전기차'}">selected</c:if>>전기차</option>
                        <option value="LPG" <c:if test="${carDetail.fuelType == 'LPG'}">selected</c:if>>LPG</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="form-section card">
            <h3 class="form-section-title">2. 차량 상세 정보</h3>
            
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="carSeats">탑승 인원 (명)</label>
                    <input type="number" id="carSeats" name="carSeats" value="${carDetail.carSeats}" required min="1">
                </div>
                <div class="form-group">
                    <label for="pricePerDay">1일 대여 요금 (원)</label>
                    <%-- DTO가 int로 받으므로 DB 값은 JSTL로 포맷팅해서 보여줌 --%>
                    <input type="text" id="pricePerDay" name="pricePerDay" 
                           value="<fmt:formatNumber value="${carDetail.pricePerDay}" pattern="#,###"/>" 
                           required onkeyup="formatPrice(this)" placeholder="숫자만 입력">
                </div>
            </div>
            
            <div class="form-group">
                <label for="carImage">차량 이미지 URL</label>
                <%-- ★★★ name을 "carImage"로 사용 (addcar.jsp도 통일 필요) ★★★ --%>
                <input type="text" id="carImage" name="carImage" value="${carDetail.carImage}" placeholder="https://...">
            </div>
            
            <div class="form-group">
                <label>차량 상태</label>
                <div class="radio-group">
                    <%-- DTO에 carStatus가 'y'/'n'으로 온다고 가정 --%>
                    <label><input type="radio" name="carStatus" value="y" <c:if test="${carDetail.carStatus == 'y'}">checked</c:if>> 대여 가능(y)</label>
                    <label><input type="radio" name="carStatus" value="n" <c:if test="${carDetail.carStatus == 'n'}">checked</c:if>> 정비/예약(n)</label>
                </div>
            </div>
        </div>

        <div class="button-container">
            <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/car/list'">취소</button>
            <button type="submit" class="btn primary">수정하기</button>
        </div>
    </form>
</div>

<script>
    // addcar.jsp와 동일한 스크립트
    function formatPrice(input) {
        let value = input.value.replace(/[^\d]/g, '');
        if (value === '') { input.value = ''; return; }
        input.value = Number(value).toLocaleString('en-US');
    }
    
    function removeCommasBeforeSubmit(form) {
        const priceInput = form.querySelector('#pricePerDay');
        if (priceInput) {
            priceInput.value = priceInput.value.replace(/,/g, '');
        }
        return true; 
    }

    // (추가) 페이지 로드 시 기존 가격에 콤마 적용 (accomedit.jsp와 동일)
    document.addEventListener('DOMContentLoaded', function() {
        const priceInput = document.getElementById('pricePerDay');
        if (priceInput && priceInput.value) {
            // 값이 숫자인지 확인 후 포맷팅
            let numValue = priceInput.value.replace(/,/g, '');
            if (!isNaN(numValue) && numValue.trim() !== '') {
                 priceInput.value = Number(numValue).toLocaleString('en-US');
            }
        }
    });
</script>