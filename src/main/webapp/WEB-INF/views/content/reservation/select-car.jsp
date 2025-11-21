<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.text.NumberFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/reservation.css">

<c:set var="userRouteId" value="${param.userRouteId}" />

<!-- 🚗 차량 선택 페이지 -->
<div class="container car-select-container">

    <!-- 필터 영역 -->
    <aside class="filter-section">
        <h2>차량 필터</h2>

        <form id="carFilterForm" action="${pageContext.request.contextPath}/reservation/select-car" method="get">

            <!-- 차종 -->
            <div class="filter-group">
                <label>차종</label>
                <select name="carType" class="form-control">
                    <option value="">전체</option>
                    <c:forEach var="t" items="${filters.carTypes}">
                        <option value="${t}" <c:if test="${param.carType == t}">selected</c:if>>${t}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- 연료 -->
            <div class="filter-group">
                <label>연료</label>
                <select name="fuelType" class="form-control">
                    <option value="">전체</option>
                    <c:forEach var="f" items="${filters.fuelTypes}">
                        <option value="${f}" <c:if test="${param.fuelType == f}">selected</c:if>>${f}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- 인승 -->
            <div class="filter-group">
                <label>인승</label>
                <select name="seats" class="form-control">
                    <option value="">전체</option>
                    <c:forEach var="s" items="${filters.seats}">
                        <option value="${s}" <c:if test="${param.seats == s}">selected</c:if>>${s}인승</option>
                    </c:forEach>
                </select>
            </div>

            <!-- 금액대 -->
            <%
                String priceParam = request.getParameter("maxPrice");
                int displayPrice = 300000; // 기본값
                if (priceParam != null && !priceParam.isBlank()) {
                    try { displayPrice = Integer.parseInt(priceParam); } catch (NumberFormatException e) {}
                }
                NumberFormat nf = NumberFormat.getInstance();
                String formattedPrice = nf.format(displayPrice);
            %>

            <div class="filter-group">
                <label>금액대 (1일 기준)</label>
                <input type="range" id="priceRange" name="maxPrice"
                       min="0" max="500000" step="10000"
                       value="<%= displayPrice %>"
                       oninput="updatePriceLabel(this.value)">
                <span id="priceLabel"><%= formattedPrice %>원 이하</span>
            </div>

            <script>
                function updatePriceLabel(value) {
                    document.getElementById('priceLabel').textContent =
                        Number(value).toLocaleString() + '원 이하';
                }
            </script>

            <!-- hidden 유지 -->
            <input type="hidden" name="region" value="${region}">
            <input type="hidden" name="checkin" value="${checkin}">
            <input type="hidden" name="checkout" value="${checkout}">
            <input type="hidden" name="roomId" value="${roomId}">
            <input type="hidden" name="userRouteId" value="${userRouteId}">

            <button type="submit" class="filter-btn">필터 적용</button>
        </form>

        <!-- 스킵/뒤로가기 -->
        <button class="skip-btn"
    onclick="location.href='${pageContext.request.contextPath}/reservation/confirm
        ?region=${param.region}
        &checkin=${param.checkin}
        &checkout=${param.checkout}
        &roomId=${param.roomId}
        &userRouteId=${userRouteId}'">
		    차량 선택 안 함 →
		</button>



        <button class="skip-btn"
    onclick="location.href='${pageContext.request.contextPath}/reservation/select-accom
        ?region=${region}
        &checkin=${checkin}
        &checkout=${checkout}
        &userRouteId=${userRouteId}'">
		    ← 숙소 선택으로 돌아가기
		</button>

    </aside>

    <!-- 차량 목록 -->
    <main class="car-list-section">

        <div class="car-list-header">
            <h2>차량 선택</h2>
            <p>지역: <strong>${region}</strong> · 기간: <strong>${checkin} ~ ${checkout}</strong></p>
        </div>

        <c:forEach var="c" items="${carList}">
            <div class="car-card">
                <c:choose>
                    <c:when test="${not empty c.carImageUrl}">
                        <img class="car-image" src="${pageContext.request.contextPath}/resources/img/car/${c.carImageUrl}" alt="${c.carName}">
                    </c:when>
                    <c:otherwise>
                        <img class="car-image" src="${pageContext.request.contextPath}/resources/img/car/default-car.jpg" alt="기본 차량 이미지">
                    </c:otherwise>
                </c:choose>

                <div class="car-info">
                    <h3>${c.carName}</h3>
                    <p>${c.carType} | ${c.fuelType} | ${c.seats}인승</p>
                    <p class="car-price">
                        <strong><fmt:formatNumber value="${c.pricePerDay}" type="number"/>원 / 1일</strong>
                    </p>
                </div>

                <!-- 차량 선택 form -->
                <form action="${pageContext.request.contextPath}/reservation/confirm" method="get">
                    <input type="hidden" name="carId" value="${c.carId}">
                    <input type="hidden" name="region" value="${region}">
                    <input type="hidden" name="checkin" value="${checkin}">
                    <input type="hidden" name="checkout" value="${checkout}">
                    <input type="hidden" name="roomId" value="${roomId}">
                    <input type="hidden" name="rentalStart" value="${checkin}">
                    <input type="hidden" name="rentalEnd" value="${checkout}">
                    <input type="hidden" name="userRouteId" value="${userRouteId}">
                    
                    <button type="submit" class="select-btn">이 차량 선택</button>
                </form>
            </div>
        </c:forEach>

        <c:if test="${empty carList}">
            <p class="no-result">선택 가능한 차량이 없습니다.</p>
        </c:if>
    </main>
</div>


