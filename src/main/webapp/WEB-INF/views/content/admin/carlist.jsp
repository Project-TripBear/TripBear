<%-- 파일 경로: /WEB-INF/views/content/admin/carlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<h1>렌터카 관리</h1>

<form id="filterForm" method="GET" action="${contextPath}/admin/car/list">
    <input type="hidden" name="sort" id="sortInput" value="${sortOrder}">
   
    <div class="controls-bar">
        <div class="controls-left">
            <button type="button" class="btn secondary" onclick="openFilterModal()"><i class="fa-solid fa-filter"></i>필터</button>
            <div class="sort-dropdown">
                <button type="button" class="dropbtn">
                    <span id="sort-text">
                        <c:choose>
                            <c:when test="${sortOrder == 'price_asc'}">낮은 가격순</c:when>
                            <c:when test="${sortOrder == 'price_desc'}">높은 가격순</c:when>
                            <c:otherwise>기본 정렬</c:otherwise>
                        </c:choose>
                    </span>
                    <i class="fa-solid fa-caret-down"></i>
                </button>
                <div class="dropdown-content">
                    <a href="#" onclick="submitSort('')">기본 정렬</a>
                    <a href="#" onclick="submitSort('price_asc')">낮은 가격순</a>
                    <a href="#" onclick="submitSort('price_desc')">높은 가격순</a>
                </div>
            </div>
        </div>
        <div class="controls-right">
             <button type="button" class="btn primary" onclick="location.href='${contextPath}/admin/car/add'">
                 <i class="fa-solid fa-plus"></i>차량 등록
             </button>
        </div>
    </div>
    
    <%-- 필터 모달은 생략 (기존과 동일) --%>
    <div id="filterModal" class="modal">
        <div class="modal-content">
             <%-- ... (모달 내용) ... --%>
            <div class="filter-group">
                <h3>연료 종류</h3>
                <label><input type="checkbox" name="fuel" value="가솔린" <c:if test="${selectedFuels.contains('가솔린')}">checked</c:if>> 가솔린</label>
                <label><input type="checkbox" name="fuel" value="디젤" <c:if test="${selectedFuels.contains('디젤')}">checked</c:if>> 디젤</label>
                <label><input type="checkbox" name="fuel" value="전기차" <c:if test="${selectedFuels.contains('전기차')}">checked</c:if>> 전기차</label>
            </div>
            <div class="filter-group">
                <h3>가격 범위 (1일 기준)</h3>
                <div id="price-display" class="price-display"></div>
                <div id="price-slider-container"><div id="price-slider"></div></div>
                <input type="hidden" name="minPrice" id="minPriceInput">
                <input type="hidden" name="maxPrice" id="maxPriceInput">
            </div>
            <div class="modal-actions">
                <a href="${contextPath}/admin/car/list" class="btn btn-reset"><i class="fa-solid fa-rotate-left"></i>초기화</a>
                <button type="submit" class="btn primary"><i class="fa-solid fa-check"></i>적용하기</button>
            </div>
        </div>
    </div>
</form>

<%-- ==== 새로운 카드형 레이아웃 시작: accomlist.jsp와 통일 ==== --%>
<div class="car-card-grid">
    <c:choose>
        <c:when test="${not empty list}">
            <c:forEach items="${list}" var="dto">
                <div class="car-card">
                    <div class="car-image-wrapper">
                        <%-- DTO에 carImageUrl 필드가 있다고 가정하고 사용 --%>
                        <img src="${dto.carImageUrl}" alt="${dto.carName} 이미지" class="car-image" 
                             onerror="this.parentElement.classList.add('img-error'); this.style.display='none';">
                        
                        <%-- 예약 상태 배지 --%>
                        <c:choose>
                            <c:when test="${dto.reserved}">
                                <span class="status-badge reserved">예약 불가</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge available">예약 가능</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="car-info">
                        <h3 class="car-name">${dto.carName} <span class="car-type">(${dto.carType})</span></h3>
                        
                        <p class="car-detail"><i class="fa-solid fa-gas-pump"></i> 연료: ${dto.fuelType}</p>
                        <p class="car-detail"><i class="fa-solid fa-people-group"></i> 인원: ${dto.carSeats}명</p>
                        <p class="car-price"><i class="fa-solid fa-won-sign"></i> 1일 요금: <fmt:formatNumber value="${dto.pricePerDay}" pattern="#,###원"/></p>
                    </div>
                    
                    <%-- 관리 버튼 --%>
                    <div class="action-buttons">
                        <a href="${contextPath}/admin/car/edit?carId=${dto.carId}" class="btn secondary">수정</a>
                        <form method="POST" action="${contextPath}/admin/car/delete" onsubmit="return confirm('[${dto.carName}] 차량을 정말 삭제하시겠습니까?');">
                            <input type="hidden" name="carId" value="${dto.carId}">
                            <button type="submit" class="btn danger">삭제</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="no-results">
                <i class="fa-solid fa-magnifying-glass-minus"></i>
                <p>조건에 맞는 차량이 없습니다.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<%-- ==== 카드형 레이아웃 끝 ==== --%>


<%-- 스크립트는 기존 코드를 유지하고 <form> id와 URL만 수정 --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.js"></script>
<script>
    const modal = document.getElementById('filterModal');
    function openFilterModal() { modal.style.display = 'block'; }
    function closeFilterModal() { modal.style.display = 'none'; }
    window.onclick = function(event) { if (event.target == modal) { modal.style.display = 'none'; } }
    
    function submitSort(sortValue) {
        document.getElementById('sortInput').value = sortValue;
        document.getElementById('filterForm').submit();
    }
    
    const priceSlider = document.getElementById('price-slider');
    const minPriceInput = document.getElementById('minPriceInput');
    const maxPriceInput = document.getElementById('maxPriceInput');
    const priceDisplay = document.getElementById('price-display');

    if (priceSlider) {
        noUiSlider.create(priceSlider, {
            start: [ <c:out value="${minPrice}" default="0"/>, <c:out value="${maxPrice == 9999999 ? 500000 : maxPrice}" default="500000"/> ],
            connect: true, step: 10000,
            range: { 'min': 0, 'max': 500000 },
            format: { to: value => Math.round(value), from: value => Number(value) }
        });

        priceSlider.noUiSlider.on('update', function (values) {
            const [minPrice, maxPrice] = values;
            minPriceInput.value = minPrice;
            maxPriceInput.value = maxPrice;
            const formatter = new Intl.NumberFormat('ko-KR');
            priceDisplay.innerHTML = formatter.format(minPrice) + '원 - ' + formatter.format(maxPrice) + '원';
        });
    }
</script>