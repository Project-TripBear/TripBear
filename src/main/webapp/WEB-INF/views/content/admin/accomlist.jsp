<%-- 파일 경로: /WEB-INF/views/content/admin/accomlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 이 페이지에서만 사용하는 외부 라이브러리 (레이아웃에 없음) --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.css" />


<form id="filterForm" method="GET" action="${pageContext.request.contextPath}/admin/accom/list">
    
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
            
            <button type="button" class="btn primary" onclick="location.href='${pageContext.request.contextPath}/admin/accom/add'">
                 <i class="fa-solid fa-plus"></i> 숙소 등록
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
                <h3>숙소 유형</h3>
				<div class="filter-group-checkboxes">
	                <label>
                        <input type="checkbox" name="type" value="호텔" <c:if test="${selectedTypes.contains('호텔')}">checked</c:if>>
                        호텔
                    </label>
                    <label>
                        <input type="checkbox" name="type" value="민박" <c:if test="${selectedTypes.contains('민박')}">checked</c:if>>
                        민박
                    </label>
                    <label>
                        <input type="checkbox" name="type" value="캠핑" <c:if test="${selectedTypes.contains('캠핑')}">checked</c:if>>
                        캠핑
                    </label>
                    <label>
                        <input type="checkbox" name="type" value="펜션" <c:if test="${selectedTypes.contains('펜션')}">checked</c:if>>
                        펜션
                    </label>
                    <label>
                        <input type="checkbox" name="type" value="모텔" <c:if test="${selectedTypes.contains('모텔')}">checked</c:if>>
                        모텔
                    </label>
                    <label>
                        <input type="checkbox" name="type" value="풀빌라" <c:if test="${selectedTypes.contains('풀빌라')}">checked</c:if>>
                        풀빌라
                    </label>
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
                <a href="${pageContext.request.contextPath}/admin/accom/list" class="btn btn-reset"><i class="fa-solid fa-rotate-left"></i> 초기화</a>
                <button type="submit" class="btn primary"><i class="fa-solid fa-check"></i> 적용하기</button>
            </div>
        </div>
    </div>
    </form>

<%-- ==== 새로운 카드형 레이아웃 시작 ==== --%>
<div class="accom-card-grid">
    <c:choose>
        <c:when test="${not empty list}">
            <c:forEach items="${list}" var="dto">
                <div class="accom-card">
                    <div class="accom-image-wrapper">
                        <c:choose>
                          <c:when test="${not empty dto.roomImage}">
						    <img src="${pageContext.request.contextPath}/resources/img/room/${dto.roomImage}"
						         alt="${dto.roomName} 이미지"
						         class="accom-image"
						         onerror="this.parentElement.classList.add('img-error'); this.style.display='none';">
								</c:when>
                            <c:otherwise>
                                <div class="accom-image img-error"></div>
                            </c:otherwise>
                        </c:choose>
                
                        <c:choose>
                            <c:when test="${dto.reserved}">
                                <span class="status-badge reserved">예약 불가</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge available">예약 가능</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="accom-info">
                        <a href="${pageContext.request.contextPath}/admin/accom/view?roomId=${dto.roomId}" class="accom-name-link">
                            <h3 class="accom-name">${dto.accomName} 
                                <span class="accom-type">(${dto.accomType})</span>
                            </h3>
                        </a>
                        <p class="room-name"><i class="fa-solid fa-bed"></i> ${dto.roomName}</p>
                        <p class="accom-detail"><i class="fa-solid fa-user-group"></i> 수용인원: ${dto.capacity}명</p>
                        <p class="accom-price"><i class="fa-solid fa-won-sign"></i> 1박 요금: <fmt:formatNumber value="${dto.pricePerNight}" pattern="#,###원"/></p>
                    </div>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/accom/edit?roomId=${dto.roomId}" class="btn secondary">수정</a>
                        <form method="POST" action="${pageContext.request.contextPath}/admin/accom/delete" onsubmit="return confirm('[${dto.accomName} - ${dto.roomName}] 객실을 정말 삭제하시겠습니까?');">
                            <input type="hidden" name="roomId" value="${dto.roomId}">
                            <button type="submit" class="btn danger">삭제</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="no-results">
                <i class="fa-solid fa-magnifying-glass-minus"></i>
                <p>조건에 맞는 숙소가 없습니다.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<%-- ==== 카드형 레이아웃 끝 ==== --%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.js"></script>
<script>
    const modal = document.getElementById('filterModal');
    function openFilterModal() { modal.style.display = 'flex'; }
    function closeFilterModal() { modal.style.display = 'none'; }
    window.onclick = function(event) {
        if (event.target == modal) { closeFilterModal(); }
    }
    
    // 정렬 폼 제출
    function submitSort(sortValue) {
        document.getElementById('sortInput').value = sortValue;
        document.getElementById('filterForm').submit();
    }
    
    // 가격 슬라이더 로직 (필터 버그 수정 반영)
    const priceSlider = document.getElementById('price-slider');
    const minPriceInput = document.getElementById('minPriceInput');
    const maxPriceInput = document.getElementById('maxPriceInput');
    const priceDisplay = document.getElementById('price-display');
    
    // DB 최대 가격을 안전하게 가져오기 (기본값 2000000)
    const dbMaxPrice = parseInt('<c:out value="${maxPriceFromDB}" default="2000000" />');
    
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
            
            // maxPrice가 DB 최대값과 같을 경우 표시 문구를 '이상'으로 변경
            let maxPriceText = (maxPrice === dbMaxPrice) ? maxPrice.toLocaleString() + '원 이상' : maxPrice.toLocaleString() + '원';
            
            priceDisplay.innerHTML = minPrice.toLocaleString() + '원 - ' + maxPriceText;
            minPriceInput.value = minPrice;
            maxPriceInput.value = maxPrice;
        });
        
        // 초기 로딩 시 가격 디스플레이 업데이트 (슬라이더가 생성된 후)
        priceSlider.noUiSlider.set([currentMinPrice, currentMaxPrice], true); // true는 silent
    }
</script>