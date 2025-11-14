	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
	<%-- 1. 이 페이지 전용 CSS/JS 로드 --%>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.css" />
	
	<h1>렌터카 관리</h1>
	
	<%-- 2. 필터링 및 정렬 폼 (accomlist.jsp와 동일한 구조) --%>
	<form id="filterForm" method="GET" action="${pageContext.request.contextPath}/admin/car/list">
	    
	    <input type="hidden" name="sort" id="sortInput" value="${sortOrder}">
	    
	    <div class="controls-bar">
	        <div class="controls-left">
	            <button type="button" class="btn secondary" onclick="openFilterModal()">
	                <i class="fa-solid fa-filter"></i> 필터
	            </button>
	            
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
	             <button type="button" class="btn primary" onclick="location.href='${pageContext.request.contextPath}/admin/car/add'">
	                 <i class="fa-solid fa-plus"></i> 렌터카 등록
	             </button>
	        </div>
	    </div>
	
	    <%-- 3. 필터 모달 (accomlist.jsp와 동일한 구조) --%>
	    <div id="filterModal" class="modal">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h2>상세 검색 필터</h2>
	                <span class="close-button" onclick="closeFilterModal()">&times;</span>
	            </div>
	            
	            <div class="filter-group">
	                <h3>연료 유형</h3>
	                <label><input type="checkbox" name="fuel" value="휘발유" <c:if test="${selectedFuels.contains('휘발유')}">checked</c:if>> 휘발유</label>
	                <label><input type="checkbox" name="fuel" value="경유" <c:if test="${selectedFuels.contains('경유')}">checked</c:if>> 경유</label>
	                <label><input type="checkbox" name="fuel" value="LPG" <c:if test="${selectedFuels.contains('LPG')}">checked</c:if>> LPG</label>
	                <label><input type="checkbox" name="fuel" value="전기" <c:if test="${selectedFuels.contains('전기')}">checked</c:if>> 전기</label>
	            </div>
	            <div class="filter-group">
	                <h3>가격 범위 (1박 기준)</h3>
	                <div id="price-display" class="price-display"></div>
	                <div id="price-slider-container">
	                    <div id="price-slider"></div>
	                </div>
	                <input type="hidden" name="minPrice" id="minPriceInput" value="${minPrice}">
	                <input type="hidden" name="maxPrice" id="maxPriceInput" value="${maxPrice}">
	            </div>
	            <div class="modal-actions">
	                <a href="${pageContext.request.contextPath}/admin/car/list" class="btn btn-reset"><i class="fa-solid fa-rotate-left"></i> 초기화</a>
	                <button type="submit" class="btn primary"><i class="fa-solid fa-check"></i> 적용하기</button>
	            </div>
	        </div>
	    </div>
	</form>
	
<%-- 4. 렌터카 카드형 그리드 (CSS 파일의 .car-card-grid 스타일 적용) --%>
<div class="car-card-grid">
    <c:choose>
        <c:when test="${not empty list}">
            <c:forEach items="${list}" var="car">
                <div class="car-card">
                    <div class="car-image-wrapper">
                        <c:choose>
                            <c:when test="${not empty car.carImage}">
                                <%-- 
                                  ★★★ [수정] ★★★
                                  DB에 저장된 '풀 URL'을 바로 src에 사용합니다.
                                  (앞에 /resources/upload/car/ 경로를 삭제)
                                --%>
                                <img src="${car.carImage}" 
                                     alt="${car.carName} 이미지" 
                                     class="car-image"
                                     onerror="this.parentElement.classList.add('img-error'); this.style.display='none';">
                            </c:when>
                            <c:otherwise>
                                <%-- 이미지가 없는 경우 CSS Fallback 적용 --%>
                                <div class="car-image-wrapper img-error"></div>
                            </c:otherwise>
                        </c:choose>
                        
                        <%-- 
                          [수정 2] 배지 로직은 carStatus를 사용하는 것이 맞습니다. (유지)
                        --%>
                        <c:choose>
                            <c:when test="${car.carStatus == 'n'}"> 
                                <span class="status-badge reserved">예약 불가</span>
                            </c:when>
                            <c:otherwise> 
                                <span class="status-badge available">예약 가능</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="car-info">
                        <a href="${pageContext.request.contextPath}/admin/car/view?carId=${car.carId}" class="accom-name-link">
                            <h3 class="car-name">${car.carName} 
                                <span class="car-type">(${car.carType})</span>
                            </h3>
                        </a>
                        
                        <p class="car-detail"><i class="fa-solid fa-gas-pump"></i> 연료: ${car.fuelType}</p>
                        <p class="car-detail"><i class="fa-solid fa-user-group"></i> 좌석: ${car.carSeats}인승</p>
                        
                        <p class="car-price"><i class="fa-solid fa-won-sign"></i> 
                            <fmt:formatNumber value="${car.pricePerDay}" pattern="#,###원"/> / 일
                        </p>
                    </div>

                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/car/edit?carId=${car.carId}" class="btn secondary">수정</a>
                        <form method="POST" action="${pageContext.request.contextPath}/admin/car/delete" onsubmit="return confirm('[${car.carName}] 차량을 정말 삭제하시겠습니까?');">
                            <input type="hidden" name="carId" value="${car.carId}">
                            <button type="submit" class="btn danger">삭제</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="no-results">
                <i class="fa-solid fa-magnifying-glass-minus"></i>
                <p>조건에 맞는 렌터카가 없습니다.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
	
	<%-- 5. 스크립트 (accomlist.jsp와 동일) --%>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.js"></script>
	<script>
	    // (이전과 동일한 스크립트 내용)
	    const modal = document.getElementById('filterModal');
	    function openFilterModal() { modal.style.display = 'flex'; }
	    function closeFilterModal() { modal.style.display = 'none'; }
	    window.onclick = function(event) {
	        if (event.target == modal) { closeFilterModal(); }
	    }
	    function submitSort(sortValue) {
	        document.getElementById('sortInput').value = sortValue;
	        document.getElementById('filterForm').submit();
	    }
	    
	    // 가격 슬라이더
	    const priceSlider = document.getElementById('price-slider');
	    const minPriceInput = document.getElementById('minPriceInput');
	    const maxPriceInput = document.getElementById('maxPriceInput');
	    const priceDisplay = d	
	 </script>