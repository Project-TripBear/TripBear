<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
  [Tiles Content]
  컨트롤러로부터 'placeList' (List<PlaceDTO>)와 'keyword' (String)를 전달받습니다.
--%>

<head>
    <style>
        .search-result-container { max-width: 1200px; margin: 20px auto; }
        .search-grid { 
            display: grid; 
            /* (참고 이미지 582ad1.jpg 처럼 3열 그리드) */
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); 
            gap: 20px; 
        }
        .place-card { border: 1px solid #ddd; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .place-card img { width: 100%; height: 220px; object-fit: cover; }
        .place-card-content { padding: 15px; }
        .place-card-content h3 { margin: 0 0 10px 0; font-size: 1.2rem; }
        .place-card-content p { font-size: 14px; color: #666; }
    </style>
</head>

<div class="search-result-container">
    <h2>'${keyword}' 검색 결과</h2>
    <p>총 ${placeList.size()}개의 결과가 있습니다.</p>
    
    <hr>
    
    <div class="search-grid">
        <c:choose>
            <c:when test="${not empty placeList}">
                <c:forEach var="place" items="${placeList}">
                    <div class="place-card">
                        <a href="/allplace/detail/${place.placeId}" style="text-decoration: none; color: inherit;">
                            <c:choose>
                                <c:when test="${not empty place.placeMainImageUrl}">
                                    <img src="${place.placeMainImageUrl}" alt="${place.name}">
                                </c:when>
                                <c:otherwise>
                                    <img src="/resources/img/default_image.png" alt="기본 이미지">
                                </c:otherwise>
                            </c:choose>
                            
                            <div class="place-card-content">
                                <h3><c:out value="${place.name}" /></h3>
                                <p><c:out value="${place.address}" /></p>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>검색 결과가 없습니다.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>