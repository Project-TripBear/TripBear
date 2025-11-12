<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
  [Tiles Content]
  컨트롤러로부터 'trendList' (List<PlaceDTO>)를 전달받습니다.
  [주의] Service/Mapper에 trendList를 조회하는 로직을 구현해야 합니다.
--%>

<head>
    <style>
        .trend-container { max-width: 900px; margin: 20px auto; }
        .trend-item { 
            display: flex; 
            border-bottom: 1px solid #eee; 
            padding: 20px 0; 
            align-items: center;
        }
        .trend-item-img { 
            width: 180px; 
            height: 180px; 
            object-fit: cover; 
            border-radius: 8px; 
            margin-right: 25px; 
        }
        .trend-item-content h3 { margin: 0 0 10px 0; font-size: 1.4rem; }
        .trend-item-content p { color: #555; }
        .trend-item-hashtags { margin-top: 15px; color: #007bff; }
    </style>
</head>

<div class="trend-container">
    <h2>#여행트렌드</h2>
    <p>최근 인기있는 여행지 목록입니다.</p>

    <div class="trend-list">
        <c:choose>
            <c:when test="${not empty trendList}">
                <c:forEach var="place" items="${trendList}">
                    <div class="trend-item">
                        <c:if test="${not empty place.placeMainImageUrl}">
                            <img src="${place.placeMainImageUrl}" alt="${place.name}" class="trend-item-img">
                        </c:if>
                        
                        <div class="trend-item-content">
                             <h3>
                                <a href="/allplace/detail/${place.placeId}">
                                    <c:out value="${place.name}" />
                                </a>
                            </h3>
                            <p><c:out value="${place.address}" /></p>
                            
                            <div class="trend-item-hashtags">
                                #임시 #해시태그 #정보
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>표시할 트렌드 정보가 없습니다. (Service/Mapper 구현 필요)</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>