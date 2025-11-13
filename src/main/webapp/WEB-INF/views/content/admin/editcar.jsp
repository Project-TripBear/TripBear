<%-- 파일 경로: /WEB-INF/views/content/admin/editcar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1><i class="fa-solid fa-pen-to-square"></i> 렌터카 정보 수정</h1>

<div class="form-container">

<form method="POST"
      action="${pageContext.request.contextPath}/admin/car/edit"
      enctype="multipart/form-data"
      onsubmit="return removeCommasBeforeSubmit(this)">

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="carId" value="${carDetail.carId}">

    <div class="form-section card">
        <h3 class="form-section-title">1. 차량 기본 정보</h3>

        <div class="form-group">
            <label for="placeLocationId">등록 지역 (차고지)</label>
            <select id="placeLocationId" name="placeLocationId" required>
                <option value="">-- 지역 선택 --</option>
                <c:forEach items="${locations}" var="loc">
                    <option value="${loc.id}"
                        <c:if test="${carDetail.placeLocationId == loc.id}">selected</c:if>>
                        ${loc.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="carName">차량 이름 (모델명)</label>
            <input type="text" id="carName" name="carName" required value="${carDetail.carName}">
        </div>

        <div class="form-group">
            <label for="carNumber">차량 번호</label>
            <input type="text" id="carNumber" name="carNumber" required value="${carDetail.carNumber}">
        </div>

        <div class="form-grid-2col">
            <div class="form-group">
                <label for="carType">차종</label>
                <select id="carType" name="carType">
                    <option value="승용차" <c:if test="${carDetail.carType == '승용차'}">selected</c:if>>승용차</option>
                    <option value="SUV" <c:if test="${carDetail.carType == 'SUV'}">selected</c:if>>SUV</option>
                    <option value="승합차" <c:if test="${carDetail.carType == '승합차'}">selected</c:if>>승합차</option>
                </select>
            </div>

            <div class="form-group">
                <label for="fuelType">연료 종류</label>
                <select id="fuelType" name="fuelType">
                    <option value="가솔린" <c:if test="${carDetail.fuelType == '가솔린'}">selected</c:if>>가솔린</option>
                    <option value="디젤" <c:if test="${carDetail.fuelType == '디젤'}">selected</c:if>>디젤</option>
                    <option value="전기차" <c:if test="${carDetail.fuelType == '전기차'}">selected</c:if>>전기차</option>
                    <option value="LPG" <c:if test="${carDetail.fuelType == 'LPG'}">selected</c:if>>LPG</option>
                    <option value="하이브리드" <c:if test="${carDetail.fuelType == '하이브리드'}">selected</c:if>>하이브리드</option>
                </select>
            </div>
        </div>
    </div>


    <div class="form-section card">
        <h3 class="form-section-title">2. 차량 상세 정보</h3>

        <div class="form-grid-2col">
            <div class="form-group">
                <label for="carSeats">탑승 인원 (명)</label>
                <input type="number" id="carSeats" name="carSeats" required value="${carDetail.carSeats}">
            </div>

            <div class="form-group">
                <label for="pricePerDay">1일 대여 요금 (원)</label>
                <input type="text" id="pricePerDay" name="pricePerDay" required
                       value="${carDetail.pricePerDay}"
                       onkeyup="formatPrice(this)" placeholder="숫자만 입력">
            </div>
        </div>

        <!-- 기존 이미지 표시 -->
        <div class="form-group">
            <label>현재 차량 이미지</label><br>
            <img src="${pageContext.request.contextPath}/resources/img/car/${carDetail.carImage}"
                 alt="현재 차량 이미지"
                 style="width:180px; height:auto; border:1px solid #ddd; border-radius:8px;">
        </div>

        <!-- 파일 업로드 입력 -->
        <div class="form-group">
            <label for="carImageFile">차량 이미지 변경</label>
            <input type="file" id="carImageFile" name="carImageFile" accept="image/*">
        </div>

        <!-- 기존 파일명 백업 -->
        <input type="hidden" name="originImage" value="${carDetail.carImage}">

        <div class="form-group">
            <label>차량 상태</label>
            <div class="radio-group">
                <label><input type="radio" name="carStatus" value="y"
                    <c:if test="${carDetail.carStatus == 'y'}">checked</c:if>> 대여 가능(y)</label>

                <label><input type="radio" name="carStatus" value="n"
                    <c:if test="${carDetail.carStatus == 'n'}">checked</c:if>> 정비/예약(n)</label>
            </div>
        </div>
    </div>

    <div class="button-container">
        <button type="button" class="btn"
                onclick="location.href='${pageContext.request.contextPath}/admin/car/list'">취소</button>
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
    const priceInput = form.querySelector('#pricePerDay');
    if (priceInput) {
        priceInput.value = priceInput.value.replace(/,/g, '');
    }
    return true;
}

document.addEventListener('DOMContentLoaded', function() {
    const priceInput = document.getElementById('pricePerDay');
    if (priceInput && priceInput.value) {
        formatPrice(priceInput);
    }
});
</script>
