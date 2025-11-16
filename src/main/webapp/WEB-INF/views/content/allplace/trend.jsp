<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
  [Tiles Content]
  CSS: resources/css/trendcard.css (공통 레이아웃에서 로드)
  Data: Controller가 "trendList" (List<PlaceDTO>)를 전달합니다.
--%>

<%-- [중요] 이 CSS 링크가 있어야 합니다. --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/allplace.css">

<main>
    <div class="trend-header">
        <h2>여행 트렌드</h2>
        <p class="sub">지금 가장 인기있는 추천 여행지입니다.</p>
    </div>

    <%-- [수정] 사이드바와 카드 갤러리를 감싸는 컨테이너 --%>
    <div class="trend-container">

        <%-- ▼▼▼ [1. 콘텐츠] 메인 콘텐츠(카드 갤러리)를 먼저 배치 (왼쪽) ▼▼▼ --%>
        <section class="trend-content">
            <%-- [수정] .trend-gallery를 .trend-content로 감쌌습니다. --%>
            <div class="trend-gallery">
                <c:choose>
                    <c:when test="${not empty trendList}">
                        <c:forEach var="item" items="${trendList}">
                        
                            <div class="trend-card" 
                                 onclick="location.href='${pageContext.request.contextPath}/allplace/view/${item.placeApiId}?contentTypeId=${item.contentTypeId}'">
                                
                                <c:choose>
                                    <c:when test="${not empty item.placeMainImageUrl}">
                                        <img src="${item.placeMainImageUrl}" alt="${item.name}" class="card-img-top">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/resources/img/icon/noimage.png" alt="이미지 없음" class="card-img-top">
                                    </c:otherwise>
                                </c:choose>
                                
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
                            <p>표시할 트렌드 정보가 없습니다.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
        <%-- ▲▲▲ 메인 콘텐츠 끝 ▲▲▲ --%>


        <%-- ▼▼▼ [2. 사이드바] 사이드바(태그 버튼)를 나중에 배치 (오른쪽) ▼▼▼ --%>
        <aside class="trend-sidebar">
            <%-- [추가] 버튼 영역을 감싸는 박스 --%>
            <div class="sidebar-box">
                <div class="region-tags">
                
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=0" 
                       class="tag-btn ${currentLocationId == 0 ? 'active' : ''}">#전체</a>
                    
                    <%-- 광역시/특별시 --%>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=1" 
                       class="tag-btn ${currentLocationId == 1 ? 'active' : ''}">#서울</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=7" 
                       class="tag-btn ${currentLocationId == 7 ? 'active' : ''}">#인천</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=12" 
                       class="tag-btn ${currentLocationId == 12 ? 'active' : ''}">#대전</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=5" 
                       class="tag-btn ${currentLocationId == 5 ? 'active' : ''}">#대구</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=14" 
                       class="tag-btn ${currentLocationId == 14 ? 'active' : ''}">#광주</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=2" 
                       class="tag-btn ${currentLocationId == 2 ? 'active' : ''}">#부산</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=9" 
                       class="tag-btn ${currentLocationId == 9 ? 'active' : ''}">#울산</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=15" 
                       class="tag-btn ${currentLocationId == 15 ? 'active' : ''}">#세종</a>
                       
                    <%-- 8도 + 제주 --%>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=16" 
                       class="tag-btn ${currentLocationId == 16 ? 'active' : ''}">#경기</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=17" 
                       class="tag-btn ${currentLocationId == 17 ? 'active' : ''}">#강원</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=18" 
                       class="tag-btn ${currentLocationId == 18 ? 'active' : ''}">#충북</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=19" 
                       class="tag-btn ${currentLocationId == 19 ? 'active' : ''}">#충남</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=22" 
                       class="tag-btn ${currentLocationId == 22 ? 'active' : ''}">#전북</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=23" 
                       class="tag-btn ${currentLocationId == 23 ? 'active' : ''}">#전남</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=20" 
                       class="tag-btn ${currentLocationId == 20 ? 'active' : ''}">#경북</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=21" 
                       class="tag-btn ${currentLocationId == 21 ? 'active' : ''}">#경남</a>
                    <a href="${pageContext.request.contextPath}/allplace/trend?locationId=3" 
                       class="tag-btn ${currentLocationId == 3 ? 'active' : ''}">#제주</a>
                </div>
            </div>
        </aside>
        <%-- ▲▲▲ 사이드바 끝 ▲▲▲ --%>

    </div> <%-- .trend-container 끝 --%>
</main>