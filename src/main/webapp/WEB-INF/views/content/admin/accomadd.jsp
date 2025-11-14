<%-- 파일 경로: /WEB-INF/views/content/admin/accomadd.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1><i class="fa-solid fa-plus"></i> 신규 숙소 등록</h1>

<div class="form-container">

    <form method="POST"
          action="${pageContext.request.contextPath}/admin/accom/add"
          enctype="multipart/form-data"
          onsubmit="return removeCommasBeforeSubmit(this)">

        <!-- CSRF Token -->
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

        <!-- ============================= -->
        <!-- 1. 숙소 위치 정보 (Place) -->
        <!-- ============================= -->
        <div class="form-section card">
            <h3 class="form-section-title">1. 숙소 위치 정보 (Place)</h3>

            <div class="form-group">
                <label for="placeName">숙소명</label>
                <input type="text" id="placeName" name="placeName" required>
            </div>

            <div class="form-group">
                <label for="placeAddress">주소</label>
                <input type="text" id="placeAddress" name="placeAddress" required>
            </div>
        </div>

        <!-- ============================= -->
        <!-- 2. 숙소 상세 정보 (Accom) -->
        <!-- ============================= -->
        <div class="form-section card">
            <h3 class="form-section-title">2. 숙소 상세 정보 (Accom)</h3>

            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="accomType">숙소 유형</label>
                    <select id="accomType" name="accomType">
                        <option value="호텔">호텔</option>
                        <option value="민박">민박</option>
                        <option value="펜션">펜션</option>
                        <option value="캠핑">캠핑</option>
                        <option value="모텔">모텔</option>
                        <option value="풀빌라">풀빌라</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="accomTel">숙소 전화번호</label>
                    <input type="tel" id="accomTel" name="accomTel" placeholder="예: 02-1234-5678">
                </div>
            </div>

            <!-- 파일 업로드로 교체 -->
            <div class="form-group">
                <label for="placeMainImageFile">숙소 대표 이미지 업로드</label>
                <input type="file" id="placeMainImageFile" name="placeMainImageFile" accept="image/*">
            </div>

            <div class="form-group">
                <label for="placeDescription">숙소 설명</label>
                <textarea id="placeDescription" name="placeDescription" rows="4"></textarea>
            </div>
        </div>

        <!-- ============================= -->
        <!-- 3. 기본 객실 정보 (Room) -->
        <!-- ============================= -->
        <div class="form-section card">
            <h3 class="form-section-title">3. 기본 객실 정보 (Room)</h3>

            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="roomName">객실명</label>
                    <input type="text" id="roomName" name="roomName" required>
                </div>

                <div class="form-group">
                    <label for="roomType">객실 타입</label>
                    <input type="text" id="roomType" name="roomType" value="스탠다드" required>
                </div>
            </div>

            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="capacity">수용인원 (명)</label>
                    <input type="number" id="capacity" name="capacity" value="2" required min="1">
                </div>

                <div class="form-group">
                    <label for="pricePerNight">1박 요금 (원)</label>
                    <input type="text" id="pricePerNight" name="pricePerNight"
                           onkeyup="formatPrice(this)" placeholder="숫자만 입력" required>
                </div>
            </div>

            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="roomArea">객실 크기</label>
                    <input type="text" id="roomArea" name="roomArea" placeholder="예: 25평 (72㎡)">
                </div>

                <div class="form-group">
                    <label for="roomImageFile">객실 이미지 업로드</label>
                    <input type="file" id="roomImageFile" name="roomImageFile" accept="image/*">
                </div>
            </div>

            <div class="form-group">
                <label>객실 상태</label>
                <div class="radio-group">
                    <label><input type="radio" name="roomStatus" value="y" checked> 판매중(y)</label>
                    <label><input type="radio" name="roomStatus" value="n"> 중지(n)</label>
                </div>
            </div>
        </div>

        <div class="button-container">
            <button type="button" class="btn"
                    onclick="location.href='${pageContext.request.contextPath}/admin/accom/list'">취소</button>
            <button type="submit" class="btn primary">등록하기</button>
        </div>

    </form>
</div>

<script>
    function formatPrice(input) {
        let v = input.value.replace(/[^\d]/g, '');
        input.value = v ? Number(v).toLocaleString('en-US') : '';
    }

    function removeCommasBeforeSubmit(form) {
        const price = form.querySelector('#pricePerNight');
        if (price) price.value = price.value.replace(/,/g, '');
        return true;
    }
</script>
