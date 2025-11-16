<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
  [Tiles Content]
  CSS: resources/css/trendcard.css (공통 레이아웃에서 로드)
  Data: Controller가 "placeList" (List<PlaceDTO>)와 "keyword"를 전달합니다.
--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/allplace/trendcard.css">
<main>
    <%-- .trend-header 클래스 공통 사용 --%>
    <div class="trend-header">
        <h2>'<c:out value="${keyword}"/>' 검색 결과</h2>
        <p class="sub">총 ${placeList.size()}개의 결과가 있습니다.</p>
    </div>

    <%-- .trend-gallery 클래스 공통 사용 --%>
    <div class="trend-gallery">
    <c:choose>
        <c:when test="${not empty placeList}">
            <c:forEach var="item" items="${placeList}">
                
                <%-- 
                  [수정]
                  1. .trend-card 클래스 사용
                  2. onclick 버그 수정 (view/ + contentTypeId)
                --%>
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
                        <p class="card-text"><c:out value="${item.address}" /></p>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <%-- .no-result 클래스 공통 사용 --%>
            <div class="no-result">
                <p>검색 결과가 없습니다.</p>
            </div>
        </c:otherwise>
    </c:choose>
    </div>
</main>