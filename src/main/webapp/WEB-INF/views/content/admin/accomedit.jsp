<%-- 파일 경로: /WEB-INF/views/content/admin/accomedit.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1><i class="fa-solid fa-pen-to-square"></i> 숙소 정보 수정</h1>

<div class="form-container">

    <form method="POST"
          action="${pageContext.request.contextPath}/admin/accom/edit"
          enctype="multipart/form-data"
          onsubmit="return removeCommasBeforeSubmit(this)">

        <!-- CSRF Token -->
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

        <!-- 수정 대상 ID -->
        <input type="hidden" name="placeId" value="${dto.placeId}">
        <input type="hidden" name="accomId" value="${dto.accomId}">
        <input type="hidden" name="roomId" value="${dto.roomId}">

        <!-- 기존 이미지 유지용 hidden -->
        <input type="hidden" name="oldPlaceImage" value="${dto.placeMainImageUrl}">
        <input type="hidden" name="oldRoomImage" value="${dto.roomImageUrl}">

        <!-- ============ 1. 숙소 위치 정보 ============ -->
        <div class="form-section card">
            <h3 class="form-section-title">1. 숙소 위치 정보 (Place)</h3>

            <div class="form-group">
                <label for="placeName">숙소명</label>
                <input type="text" id="placeName" name="placeName" required value="${dto.placeName}">
            </div>

            <div class="form-group">
                <label for="placeAddress">주소</label>
                <input type="text" id="placeAddress" name="placeAddress" required value="${dto.placeAddress}">
            </div>

        </div>

        <!-- ============ 2. 숙소 상세 정보 ============ -->
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
                    <input type="tel" id="accomTel" name="accomTel"
                           value="${dto.accomTel}" placeholder="예: 02-1234-5678">
                </div>
            </div>

            <!-- 기존 이미지 미리보기 -->
            <div class="form-group">
                <label>현재 숙소 대표 이미지</label><br>
                <img src="/resources/img/accom/${dto.placeMainImageUrl}" width="200">
            </div>

            <!-- 파일 업로드로 교체 -->
            <div class="form-group">
                <label for="placeMainImageFile">숙소 대표 이미지 변경</label>
                <input type="file" id="placeMainImageFile" name="placeMainImageFile" accept="image/*">
            </div>

            <div class="form-group">
                <label for="placeDescription">숙소 설명</label>
                <textarea id="placeDescription" name="placeDescription" rows="4">${dto.placeDescription}</textarea>
            </div>

        </div>

        <!-- ============ 3. 객실 정보 ============ -->
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
                    <label for="capacity">수용 인원</label>
                    <input type="number" id="capacity" name="capacity"
                           value="${dto.capacity}" required min="1">
                </div>

                <div class="form-group">
                    <label for="pricePerNight">1박 요금 (원)</label>
                    <input type="text" id="pricePerNight" name="pricePerNight"
                           value="${dto.pricePerNight}" onkeyup="formatPrice(this)">
                </div>
            </div>

            <div class="form-group">
                <label>현재 객실 이미지</label><br>
                <img src="/resources/img/room/${dto.roomImageUrl}" width="200">
            </div>

            <div class="form-group">
                <label for="roomImageFile">객실 이미지 변경</label>
                <input type="file" id="roomImageFile" name="roomImageFile" accept="image/*">
            </div>

            <div class="form-group">
                <label>객실 상태</label>
                <div class="radio-group">
                    <label><input type="radio" name="roomStatus" value="y"
                                  <c:if test="${dto.roomStatus == 'y'}">checked</c:if>> 판매중(y)</label>
                    <label><input type="radio" name="roomStatus" value="n"
                                  <c:if test="${dto.roomStatus == 'n'}">checked</c:if>> 중지(n)</label>
                </div>
            </div>
        </div>

        <div class="button-container">
            <button type="button" class="btn"
                    onclick="location.href='${pageContext.request.contextPath}/admin/accom/list'">취소</button>
            <button type="submit" class="btn primary">수정하기</button>
        </div>

    </form>

</div>

<script>
    function formatPrice(input) {
        let value = input.value.replace(/[^\d]/g, '');
        input.value = value ? Number(value).toLocaleString('en-US') : '';
    }

    function removeCommasBeforeSubmit(form) {
        const priceInput = form.querySelector('#pricePerNight');
        if (priceInput) priceInput.value = priceInput.value.replace(/,/g, '');
        return true;
    }
</script>
