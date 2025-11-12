<%-- 파일 경로: /WEB-INF/views/content/admin/accomadd.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- 이 페이지 전용 CSS --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1><i class="fa-solid fa-plus"></i> 신규 숙소 등록</h1>

<div class="form-container">
    <form method="POST" action="${pageContext.request.contextPath}/admin/accom/add" onsubmit="return removeCommasBeforeSubmit(this)">
         
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
            
            <%-- ★★★ [삭제] 위도/경도 수동 입력란 제거 ★★★ --%>
            <%-- 
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="placeLat">위도 (Latitude)</label>
                    <input type="number" ... name="placeLat" ...>
                </div>
                <div class="form-group">
                    <label for="placeLng">경도 (Longitude)</label>
                    <input type="number" ... name="placeLng" ...>
                </div>
            </div> 
            --%>
            
            <%-- ★★★ [삭제] 지역 ID / 장소 유형 ID 수동 입력란 제거 ★★★ --%>
            <%--
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="placeLocationId">지역 ID (place_Location_id)</label>
                    <input type="number" id="placeLocationId" name="placeLocationId" value="1" required>
                </div>
                <div class="form-group">
                    <label for="placeTypeId">장소 유형 ID (place_type_id)</label>
                    <input type="number" id="placeTypeId" name="placeTypeId" value="1" required>
                </div>
            </div>
            --%>

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
            
            <div class="form-group">
                <label for="placeMainImageUrl">숙소 대표 이미지 URL</label>
                <input type="text" id="placeMainImageUrl" name="placeMainImageUrl" placeholder="https://...">
            </div>
            <div class="form-group">
                <label for="placeDescription">숙소 설명</label>
                <textarea id="placeDescription" name="placeDescription" rows="4"></textarea>
            </div>
        </div>

        <div class="form-section card">
            <h3 class="form-section-title">3. 기본 객실 정보 (Room)</h3>
            
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="roomName">객실명</label>
                    <input type="text" id="roomName" name="roomName" placeholder="예: 스탠다드 더블룸" required>
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
                    <input type="text" id="pricePerNight" name="pricePerNight" required onkeyup="formatPrice(this)" placeholder="숫자만 입력">
                </div>
            </div>
            
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="roomArea">객실 크기</label>
                    <input type="text" id="roomArea" name="roomArea" placeholder="예: 25평 (72㎡)">
                </div>
                <div class="form-group">
                    <label for="roomImageUrl">객실 이미지 URL</label>
                    <input type="text" id="roomImageUrl" name="roomImageUrl" placeholder="https://...">
                </div>
            </div>
            
             <div class="form-group">
                <label>객실 상태 (기본값 'y')</label>
                <div class="radio-group">
                    <label><input type="radio" name="roomStatus" value="y" checked> 판매중(y)</label>
                    <label><input type="radio" name="roomStatus" value="n"> 중지(n)</label>
                </div>
            </div>
        </div>
       
        <div class="button-container">
            <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/accom/list'">취소</button>
            <button type="submit" class="btn primary">등록하기</button>
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
</script>