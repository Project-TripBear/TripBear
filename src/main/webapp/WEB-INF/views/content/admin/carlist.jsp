<%-- 파일 경로: /WEB-INF/views/content/admin/carlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.css" />

<c:if test="${not empty msg}">
    <div class="alert alert-success" role="alert" style="margin-bottom: 1.5rem;">
        <i class="fa-solid fa-circle-check"></i> ${msg}
    </div>
</c:if>


<form id="filterForm" method="GET" action="${pageContext.request.contextPath}/admin/car/list">
    
    <input type="hidden" name="sort" id="sortInput" value="${sortOrder}">
    
    <div class="controls-bar">
        <div></div> 
        
        <div class="controls-right">
            <div class="sort-dropdown">
                <button type="button" class="dropbtn">
                    <i class="fa-solid fa-arrow-down-short-wide" style="margin-right: 5px;"></i>
                    <span id="sort-text">
                        <c:choose>
                            <c:when test="${sortOrder == 'price_asc'}">가격: 낮은순</c:when>
                            <c:when test="${sortOrder == 'price_desc'}">가격: 높은순</c:when>
                            <c:otherwise>등록일: 최신순</c:otherwise>
                        </c:choose>
                    </span>
                </button>
                <div class="dropdown-content">
                    <a href="#" onclick="submitSort('')"><i class="fa-solid fa-calendar-days"></i> 등록일: 최신순</a>
                    <a href="#" onclick="submitSort('price_asc')"><i class="fa-solid fa-arrow-up-1-9"></i> 가격: 낮은순</a>
                    <a href="#" onclick="submitSort('price_desc')"><i class="fa-solid fa-arrow-down-9-1"></i> 가격: 높은순</a>
                </div>
            </div>
            
            <button type="button" class="btn secondary" onclick="openFilterModal()">
                <i class="fa-solid fa-filter"></i> 필터
            </button>
            
            <button type="button" class="btn primary" onclick="location.href='${pageContext.request.contextPath}/admin/car/add'">
             <i class="fa-solid fa-plus"></i> 렌터카 등록
             </button>
        </div>
    </div>

    <div id="filterModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>상세 검색 필터</h2>
               <span class="close-button" onclick="closeFilterModal()">&times;</span>
            </div>
            
            <div class="filter-group">
                <h3>연료 유형</h3>
                <div class="filter-group-checkboxes">
                    <label><input type="checkbox" name="fuel" value="가솔린" <c:if test="${selectedFuels.contains('가솔린')}">checked</c:if>> 가솔린</label>
                    <label><input type="checkbox" name="fuel" value="디젤" <c:if test="${selectedFuels.contains('디젤')}">checked</c:if>> 디젤</label>
                    <label><input type="checkbox" name="fuel" value="LPG" <c:if test="${selectedFuels.contains('LPG')}">checked</c:if>> LPG</label>
                    <label><input type="checkbox" name="fuel" value="전기" <c:if test="${selectedFuels.contains('전기')}">checked</c:if>> 전기</label>
                </div>
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
            
            <div class="modal-actions filter-footer-actions">
                <a href="${pageContext.request.contextPath}/admin/car/list" class="btn btn-reset"><i class="fa-solid fa-rotate-left"></i> 초기화</a>
                <button type="submit" class="btn primary"><i class="fa-solid fa-check"></i> 적용하기</button>
            </div>
        </div>
    </div>
	
</form>

<section id="car-list-section">
    <div class="car-card-grid">

        <c:choose>
            <c:when test="${not empty list}">
                <c:forEach items="${list}" var="car">

                    <div class="car-card">

                        <a href="${pageContext.request.contextPath}/admin/car/view?carId=${car.carId}" class="car-image-link">
                            <div class="car-image-wrapper">

                                <c:choose>
                                    <c:when test="${not empty car.carImage}">
                                        <img src="${pageContext.request.contextPath}/resources/img/car/${car.carImage}"
                                             alt="${car.carName} 이미지"
                                             class="car-image"
                                             onerror="this.parentElement.classList.add('img-error'); this.style.display='none';">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="img-error"></div>
                                    </c:otherwise>
                                </c:choose>

                                <c:choose>
                                    <c:when test="${car.carStatus == 'n'}">
                                        <span class="status-badge reserved">예약 불가</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge available">예약 가능</span>
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </a>

                        <div class="car-info">
                            <a href="${pageContext.request.contextPath}/admin/car/view?carId=${car.carId}" class="accom-name-link">
                                <h3 class="car-name">
                                    ${car.carName}
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
                            <form method="POST"
                                  action="${pageContext.request.contextPath}/admin/car/delete"
                                  onsubmit="return confirm('[${car.carName}] 차량을 정말 삭제하시겠습니까?');">
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
</section>
	
<%-- 5. 스크립트 --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.js"></script>
<script>
    // --- 모달/정렬 스크립트 ---
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
    
    // --- 가격 슬라이더 스크립트 (숙소 목록의 버그 수정 로직 반영) ---
    const priceSlider = document.getElementById('price-slider');
    const minPriceInput = document.getElementById('minPriceInput');
    const maxPriceInput = document.getElementById('maxPriceInput');
    const priceDisplay = document.getElementById('price-display');
    
    // DB 최대 가격을 안전하게 가져오기 (기본값 500000)
    const dbMaxPrice = parseInt('<c:out value="${maxPriceFromDB}" default="500000" />');
    
    // 현재 적용된 최소/최대 가격을 안전하게 가져오기 (값이 없을 경우 기본값 설정)
    const currentMinPrice = parseInt('<c:out value="${minPrice}" default="0"/>');
    // maxPrice가 null이면 dbMaxPrice로 설정
    const currentMaxPrice = parseInt('<c:out value="${maxPrice}" default="${maxPriceFromDB}"/>');
    
    if (priceSlider) {
        noUiSlider.create(priceSlider, {
            start: [ 
                currentMinPrice, 
                currentMaxPrice
            ],
            connect: true,
            step: 10000,
            range: { 'min': 0, 'max': dbMaxPrice }
        });
        
        priceSlider.noUiSlider.on('update', function (values, handle) {
            const minPrice = parseInt(values[0]);
            const maxPrice = parseInt(values[1]);
            
            // maxPrice가 DB 최대값과 같을 경우 표시 문구를 '이상'으로 변경할 수 있음
            let maxPriceText = (maxPrice === dbMaxPrice) ? maxPrice.toLocaleString() + '원 이상' : maxPrice.toLocaleString() + '원';
            
            priceDisplay.innerHTML = minPrice.toLocaleString() + '원 - ' + maxPriceText;
            
            minPriceInput.value = minPrice;
            maxPriceInput.value = maxPrice;
        });
        
        // 초기 로딩 시 가격 디스플레이 업데이트 (슬라이더가 생성된 후)
        priceSlider.noUiSlider.set([currentMinPrice, currentMaxPrice], true);
    }
</script>