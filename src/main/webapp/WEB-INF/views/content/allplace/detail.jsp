<%@ page language="java" contentType="text/HTML; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
  [Tiles Content]
  컨트롤러로부터 'place' (PlaceDTO) 객체만 전달받습니다.
(날씨 제거됨)
--%>

<%-- (삭제) <head> ... <style> ... </style> </head> --%>

<div class="detail-container">

    <h1><c:out value="${place.name}" /></h1>
    <p class="address"><c:out value="${place.address}" /></p>

    <c:if test="${not empty place.placeMainImageUrl}">
        <img src="${place.placeMainImageUrl}" alt="${place.name}" class="detail-main-image">
    </c:if>

    <div class="details-content">
        <c:choose>
            <%-- 2-1.
관광지 (touristSpotDetail) --%>
            <c:when test="${not empty place.touristSpotDetail}">
                <h2>관광지 정보</h2>
                <ul>
                    <li><strong>운영시간:</strong> <c:out value="${place.touristSpotDetail.openingHours}" default="정보 없음" /></li>
                    <li><strong>입장료:</strong> <c:out value="${place.touristSpotDetail.admissionFee}" default="정보 없음" 
/></li>
                    <li><strong>연락처:</strong> <c:out value="${place.touristSpotDetail.contactInfo}" default="정보 없음" /></li>
                </ul>
            </c:when>
            
            <%-- 2-2.
축제/행사 (eventDetail) --%>
            <c:when test="${not empty place.eventDetail}">
                <h2>축제/행사 정보</h2>
                <ul>
                    <li><strong>시작일:</strong> <c:out value="${place.eventDetail.eventStart}" default="정보 없음" /></li>
                    <li><strong>종료일:</strong> <c:out value="${place.eventDetail.eventEnd}" default="정보 없음" 
/></li>
                    <li><strong>홈페이지:</strong> <a href="${place.eventDetail.eventLink}" target="_blank">바로가기</a></li>
                </ul>
            </c:when>

            <%-- 2-3.
음식점 (restaurantDetail) --%>
            <c:when test="${not empty place.restaurantDetail}">
                <h2>음식점 정보</h2>
                <ul>
                    <li><strong>취급메뉴:</strong> <c:out value="${place.restaurantDetail.restaurantCategory}" default="정보 없음" /></li>
                    <li><strong>대표메뉴:</strong> <c:out value="${place.restaurantDetail.restaurantPrice}" default="정보 없음" 
/></li>
                    <li><strong>전화번호:</strong> <c:out value="${place.restaurantDetail.restaurantCall}" default="정보 없음" /></li>
                </ul>
            </c:when>
        </c:choose>
    </div>

    </div>