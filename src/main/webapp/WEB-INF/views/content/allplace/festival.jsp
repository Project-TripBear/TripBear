<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/allplace/listcard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/allplace/listsidebutton.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/allplace/festivaldate.css">

<main>
    <div class="trend-header">
        <h2>시기별 축제/행사</h2>
        <p class="sub">지금 가장 인기있는 추천 축제/행사입니다.</p>
    </div>

    <div class="trend-container">

        <%-- ▼▼▼ [1. 콘텐츠] (왼쪽) ▼▼▼ --%>
        <section class="trend-content">
            <div class="trend-gallery">
                <c:choose>
                    <c:when test="${not empty trendList}">
                        <c:forEach var="item" items="${trendList}">
                            <div class="trend-card" 
                                 onclick="location.href='${pageContext.request.contextPath}/allplace/view/${item.placeApiId}?contentTypeId=${item.contentTypeId}'">
                                
                                <%-- (카드 이미지 ... ) --%>
                                <c:choose>
                                    <c:when test="${not empty item.placeMainImageUrl}">
                                        <img src="${item.placeMainImageUrl}" alt="${item.name}" class="card-img-top">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/resources/img/icon/noimage.png" alt="이미지 없음" class="card-img-top">
                                    </c:otherwise>
                                </c:choose>
                                
                                <%-- (카드 바디 ... ) --%>
                                <div class="card-body">
                                    <h5 class="card-title"><c:out value="${item.name}" /></h5>
                                    <c:choose>
                                        <c:when test="${not empty item.overview}">
                                            <p class="card-text"><c:out value="${item.overview}" /></p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="card-text"><c:out value="${item.address}" /></p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-result">
                            <p>표시할 축제 정보가 없습니다.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
        <%-- ▲▲▲ 메인 콘텐츠 끝 ▲▲▲ --%>


        <%-- ▼▼▼ [2. 사이드바] (오른쪽) ▼▼▼ --%>
        <aside class="trend-sidebar">
            <div class="sidebar-box">
            
                <%-- [수정] 날짜 선택 UI -> 월 선택 드롭다운 --%>
                <div class="month-search-box">
                    <label for="monthSelect">시기 (월)</label>
                    <select id="monthSelect">
                        <option value="today">전체 (오늘부터)</option>
                        <option value="01">1월</option>
                        <option value="02">2월</option>
                        <option value="03">3월</option>
                        <option value="04">4월</option>
                        <option value="05">5월</option>
                        <option value="06">6월</option>
                        <option value="07">7월</option>
                        <option value="08">8월</option>
                        <option value="09">9월</option>
                        <option value="10">10월</option>
                        <option value="11">11월</option>
                        <option value="12">12월</option>
                    </select>
                </div>
            
                <div class="region-tags">
                    <%-- [수정] <a> 태그의 href="#" 제거, data-loc 속성만 사용 --%>
                    <span data-loc="0" class="tag-btn ${currentLocationId == 0 ? 'active' : ''}">#전체</span>
                    <span data-loc="1" class="tag-btn ${currentLocationId == 1 ? 'active' : ''}">#서울</span>
                    <span data-loc="7" class="tag-btn ${currentLocationId == 7 ? 'active' : ''}">#인천</span>
                    <span data-loc="12" class="tag-btn ${currentLocationId == 12 ? 'active' : ''}">#대전</span>
                    <span data-loc="5" class="tag-btn ${currentLocationId == 5 ? 'active' : ''}">#대구</span>
                    <span data-loc="14" class="tag-btn ${currentLocationId == 14 ? 'active' : ''}">#광주</span>
                    <span data-loc="2" class="tag-btn ${currentLocationId == 2 ? 'active' : ''}">#부산</span>
                    <span data-loc="9" class="tag-btn ${currentLocationId == 9 ? 'active' : ''}">#울산</span>
                    <span data-loc="15" class="tag-btn ${currentLocationId == 15 ? 'active' : ''}">#세종</span>
                    <span data-loc="16" class="tag-btn ${currentLocationId == 16 ? 'active' : ''}">#경기</span>
                    <span data-loc="17" class="tag-btn ${currentLocationId == 17 ? 'active' : ''}">#강원</span>
                    <span data-loc="18" class="tag-btn ${currentLocationId == 18 ? 'active' : ''}">#충북</span>
                    <span data-loc="19" class="tag-btn ${currentLocationId == 19 ? 'active' : ''}">#충남</span>
                    <span data-loc="22" class="tag-btn ${currentLocationId == 22 ? 'active' : ''}">#전북</span>
                    <span data-loc="23" class="tag-btn ${currentLocationId == 23 ? 'active' : ''}">#전남</span>
                    <span data-loc="20" class="tag-btn ${currentLocationId == 20 ? 'active' : ''}">#경북</span>
                    <span data-loc="21" class="tag-btn ${currentLocationId == 21 ? 'active' : ''}">#경남</span>
                    <span data-loc="3" class="tag-btn ${currentLocationId == 3 ? 'active' : ''}">#제주</span>
                </div>
            </div>
        </aside>
        <%-- ▲▲▲ 사이드바 끝 ▲▲▲ --%>

    </div> <%-- .trend-container 끝 --%>
</main>

<%-- [수정] 월 선택 드롭다운을 처리하는 새 JavaScript --%>
<script>
document.addEventListener("DOMContentLoaded", function() {
    
    // --- 1. 컨트롤러가 넘겨준 값 받기 ---
    const baseUrl = "${pageContext.request.contextPath}/allplace/festival";
    const yyyyMMdd = "${eventStartDate}"; // 예: "20251116"
    const todayDate = "${todayDate}";     // 예: "20251116"
    const currentLocId = "${currentLocationId}"; // 예: "0"
    
    const currentYear = yyyyMMdd.substring(0, 4); // "2025"
    const currentMonth = yyyyMMdd.substring(4, 6); // "11"
    
    const monthSelect = document.getElementById("monthSelect");

    // --- 2. 페이지 로드 시, 드롭다운에 현재 값 설정 ---
    if (yyyyMMdd === todayDate) {
        monthSelect.value = "today"; // 오늘 날짜면 '전체' 선택
    } else {
        monthSelect.value = currentMonth; // 아니면 해당 월 선택
    }

    // --- 3. '월(月) 선택' 드롭다운을 변경했을 때 ---
    monthSelect.addEventListener("change", function() {
        const selectedMonth = this.value; // "today" 또는 "01", "11" 등
        let newEventStartDate = "";

        if (selectedMonth === "today") {
            newEventStartDate = todayDate;
        } else {
            // 현재 보고 있는 연도(currentYear) + 선택한 월 + 01일
            newEventStartDate = currentYear + selectedMonth + "01";
        }
        
        // 현재 지역(currentLocId) + 새 날짜로 페이지 이동
        location.href = baseUrl + "?locationId=" + currentLocId + "&eventStartDate=" + newEventStartDate;
    });

    // --- 4. '지역 태그'를 클릭했을 때 ---
    const regionTags = document.querySelectorAll(".region-tags .tag-btn");
    regionTags.forEach(tag => {
        tag.style.cursor = "pointer"; // 클릭 가능하게 커서 변경
        tag.addEventListener("click", function() {
            
            const newLocationId = this.getAttribute("data-loc"); // 클릭한 지역 ID
            const selectedMonth = monthSelect.value; // 현재 선택된 월
            let newEventStartDate = "";

            if (selectedMonth === "today") {
                newEventStartDate = todayDate;
            } else {
                newEventStartDate = currentYear + selectedMonth + "01";
            }
            
            // 새 지역 ID + 현재 날짜(월)로 페이지 이동
            location.href = baseUrl + "?locationId=" + newLocationId + "&eventStartDate=" + newEventStartDate;
        });
    });
});
</script>
