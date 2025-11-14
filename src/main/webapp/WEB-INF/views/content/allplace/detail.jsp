<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/detail.css" />


<div class="detail-container">

    <h1 class="place-title">${place.name}</h1>
    <div class="place-address">${place.address}</div>

    <img class="main-img"
         src="${place.placeMainImageUrl != null ? place.placeMainImageUrl : '/resources/img/icon/noimage.png'}" />

    <div id="detail-map"></div>

    <!-- 기본 정보 -->
    <div class="info-box">
        <h3>기본 정보</h3>
        <p><b>주소</b> ${place.address}</p>
        <b>전화번호</b>
    <c:if test="${not empty place.touristSpotDetail.contactInfo}">
        ${place.touristSpotDetail.contactInfo}
    </c:if>
    <c:if test="${not empty place.restaurantDetail.restaurantCall}">
        ${place.restaurantDetail.restaurantCall}
    </c:if>
    </div>

    <!-- 관광지/축제/음식점 -->
    <c:choose>
        <c:when test="${place.placeTypeId == 1}">
    <div class="info-box">
        <h3>관광지 정보</h3>
        <p><b>개요</b> ${place.touristSpotDetail.spotOverinfo}</p>
        <p><b>운영시간</b> ${place.touristSpotDetail.openingHours}</p>
        <p><b>휴무일</b> ${place.touristSpotDetail.restDay}</p>
        <p><b>주차</b> ${place.touristSpotDetail.parkingInfo}</p>
    </div>
</c:when>

        <c:when test="${place.placeTypeId == 2}">
            <div class="info-box">
                <h3>축제/행사 정보</h3>
                <p><b>기간</b> ${place.eventDetail.eventStart} ~ ${place.eventDetail.eventEnd}</p>
                <p><b>내용</b> ${place.eventDetail.eventInfo}</p>
                <p><b>홈페이지</b> <a href="${place.eventDetail.eventLink}" target="_blank">${place.eventDetail.eventLink}</a></p>
            </div>
        </c:when>

        <c:when test="${place.placeTypeId == 3}">
            <div class="info-box">
                <h3>음식점 정보</h3>
                <p><b>메뉴</b> ${place.restaurantDetail.restaurantCategory}</p>
                <p><b>가격</b> ${place.restaurantDetail.restaurantPrice}</p>
                <p><b>영업시간</b> ${place.restaurantDetail.restaurantOpenTime}</p>
                <p><b> 문의 </b> ${place.restaurantDetail.restaurantCall}</p>
            </div>
        </c:when>
    </c:choose>

    <!-- 해시태그 -->
    <div class="info-box">
        <h3>해시태그</h3>

        <div id="tag-list">
            <c:forEach items="${place.hashtags}" var="tag">
                <span class="tag">#${tag}</span>
            </c:forEach>
        </div>

        <div class="tag-input-box">
            <input id="tag-input" placeholder="태그 입력" />
            <button id="tag-add-btn">추가</button>
        </div>
    </div>

    <!-- 주변 추천 -->
    <c:if test="${not empty recommendList}">
        <div class="info-box">
            <h3>주변 추천 장소</h3>

            <div class="recommend-grid">
               <c:forEach items="${recommendList}" var="p">
				    <%-- 
				      1. onclick에 contextPath 추가 
				    --%>
				    <div class="recommend-item" 
				         onclick="location.href='${pageContext.request.contextPath}/allplace/detail/${p.placeId}'">
				        
				        <%-- 
				          2. <img> 태그에 contextPath 추가 및 NULL 검사 
				        --%>
				        <c:choose>
				            <c:when test="${not empty p.placeMainImageUrl}">
				                <img src="${pageContext.request.contextPath}${p.placeMainImageUrl}" />
				            </c:when>
				            <c:otherwise>
				                <img src="${pageContext.request.contextPath}/resources/img/icon/noimage.png" />
				            </c:otherwise>
				        </c:choose>
				
				        <h4>${p.name}</h4>
				        <p>${p.address}</p>
				    </div>
				</c:forEach>
            </div>
        </div>
    </c:if>

</div>


<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=09d09e9035bb509e8f002c6fab6b12ac"></script>
<script>
    const lat = ${place.latitude};
    const lon = ${place.longitude};

    const map = new kakao.maps.Map(document.getElementById("detail-map"), {
        center: new kakao.maps.LatLng(lat, lon),
        level: 4
    });

    new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(lat, lon)
    });

    /* ---------------------------
       해시태그 추가 AJAX
    --------------------------- */
    document.getElementById("tag-add-btn").onclick = function () {
        const tag = document.getElementById("tag-input").value.trim();
        if (tag == "") return;

        fetch("${pageContext.request.contextPath}/allplace/addTag?placeId=${place.placeId}&tag=" + encodeURIComponent(tag))
            .then(r => r.text())
            .then(res => {
                const list = document.getElementById("tag-list");
                let span = document.createElement("span");
                span.className = "tag";
                span.innerText = "#" + tag;
                list.appendChild(span);

                document.getElementById("tag-input").value = "";
            });
    };
</script>
