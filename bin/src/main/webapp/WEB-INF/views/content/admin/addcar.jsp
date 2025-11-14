<%-- 파일 경로: /WEB-INF/views/content/admin/addcar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- ★★★ [추가] JSTL 선언 ★★★ --%>

<%-- 이 페이지 전용 CSS --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1><i class="fa-solid fa-car"></i> 신규 렌터카 등록</h1>

<%-- accomadd.jsp와 동일한 form-container, form-section 클래스 사용 --%>
<div class="form-container">
    <form method="POST" action="${pageContext.request.contextPath}/admin/car/add" class="form-container" onsubmit="return removeCommasBeforeSubmit(this)">
        
        <div class="form-section card">
            <h3 class="form-section-title">1. 차량 기본 정보</h3>
            
            <%-- ★★★ 컨트롤러에서 받은 ${locations}로 드롭다운 생성 ★★★ --%>
            <div class="form-group">
                <label for="placeLocationId">등록 지역 (차고지)</label>
                <select id="placeLocationId" name="placeLocationId" required>
                    <option value="">-- 지역 선택 --</option>
                    <c:forEach items="${locations}" var="loc">
                        <%-- 
                          XML에서 "id", "name"으로 별칭을 줬습니다.
                          (만약 안나오면 loc.ID / loc.NAME 대문자로 변경)
                        --%>
                        <option value="${loc.id}">${loc.name}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="carName">차량 이름 (모델명)</label>
                <input type="text" id="carName" name="carName" placeholder="예: 더 뉴 아반떼" required>
            </div>
            
            <div class="form-group">
                <label for="carNumber">차량 번호</label>
                <input type="text" id="carNumber" name="carNumber" placeholder="예: 12가 3456" required>
            </div>

            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="carType">차종</label>
                    <select id="carType" name="carType">
                        <option value="승용차" selected>승용차</option>
                        <option value="SUV">SUV</option>
                        <option value="승합차">승합차</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="fuelType">연료 종류</label>
                    <select id="fuelType" name="fuelType">
                        <option value="가솔린" selected>가솔린</option>
                        <option value="디젤">디젤</option>
                        <option value="전기차">전기차</option>
                        <option value="LPG">LPG</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="form-section card">
            <h3 class="form-section-title">2. 차량 상세 정보</h3>
            
            <div class="form-grid-2col">
                <div class="form-group">
                    <label for="carSeats">탑승 인원 (명)</label>
                    <input type="number" id="carSeats" name="carSeats" value="5" required min="1">
                </div>
                <div class="form-group">
                    <label for="pricePerDay">1일 대여 요금 (원)</label>
                    <input type="text" id="pricePerDay" name="pricePerDay" required onkeyup="formatPrice(this)" placeholder="숫자만 입력">
                </div>
            </div>
            
            <div class="form-group">
                <label for="carImage">차량 이미지 URL</label>
                <input type="text" id="carImage" name="carImage" placeholder="https://...">
            </div>
            
            <div class="form-group">
                <label>차량 상태 (기본값 'y')</label>
                <div class="radio-group">
                    <label><input type="radio" name="carStatus" value="y" checked> 대여 가능(y)</label>
                    <label><input type="radio" name="carStatus" value="n"> 정비/예약(n)</label>
                </div>
            </div>
        </div>

        <div class="button-container">
            <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/car/list'">취소</button>
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
        const priceInput = form.querySelector('#pricePerDay');
        if (priceInput) {
            priceInput.value = priceInput.value.replace(/,/g, ''); 
        }
        return true; 
    }
</script>	