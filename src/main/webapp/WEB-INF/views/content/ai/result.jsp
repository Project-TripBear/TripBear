<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI 추천 여행 루트</title>
    <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
    <%-- (필요) 이 페이지를 꾸밀 CSS (예: result.css) --%>
    <%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/result.css"> --%>
    
    <%-- (필요) 카카오맵 API 로드 (지도를 표시할 경우) --%>
    <%-- <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_APP_KEY"></script> --%>
</head>
<body>
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>
    
    <main class="ai-result-container">
        <c:if test="${empty resultRoute}">
            <div style="text-align: center; padding: 100px 20px;">
                <h2>오류</h2>
                <p>여행 루트를 불러오는 데 실패했습니다. 다시 시도해주세요.</p>
                <a href="<c:url value='/ai/plan.do' />">계획 페이지로 돌아가기</a>
            </div>
        </c:if>

        <c:if test="${not empty resultRoute}">
            <div class="route-header">
                <h1>${resultRoute.ai_route_title}</h1>
                <p>
                    ${resultRoute.ai_route_region} | 
                    ${resultRoute.ai_route_days}일 | 
                    (${resultRoute.ai_route_startdate} ~ ${resultRoute.ai_route_enddate})
                </p>
            </div>
            
            <%-- (필요) 지도를 표시할 DIV --%>
            <%-- <div id="map" style="width:100%;height:400px;border-radius: 15px;"></div> --%>

            <div class="route-timeline">
                <c:forEach var="stop" items="${resultRoute.stops}">
                
                    <%-- (이동 수단 표시) 첫 번째 장소가 아닐 때 --%>
                    <c:if test="${stop.ai_route_stop_order > 1 && not empty stop.transportation_mode}">
                        <div class="transport-leg">
                            <span>↓</span>
                            <span class="mode">${stop.transportation_mode}</span>
                        </div>
                    </c:if>

                    <%-- (경유지 카드) --%>
                    <div class="stop-card">
                        <div class="stop-order">${stop.ai_route_day}일차 - ${stop.ai_route_stop_order}</div>
                        <div class="stop-title">${stop.ai_route_description}</div>
                        <div classs="stop-meta">
                            <span>[${stop.activity_code}]</span>
                            <span>약 ${stop.duration_in_minutes}분 소요</span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </main>

    <%-- (필요) 카카오맵 스크립트 --%>
    <%--
    <script>
    // 1. DTO에서 경유지 좌표 리스트 (lines) 생성
    var mapContainer = document.getElementById('map');
    var mapOption = { 
        center: new kakao.maps.LatLng(${resultRoute.stops[0].ai_route_lat}, ${resultRoute.stops[0].ai_route_long}), // 첫 번째 경유지 중심
        level: 8 
    };
    var map = new kakao.maps.Map(mapContainer, mapOption);

    // 2. 마커와 선 그리기
    var linePath = [];
    <c:forEach var="stop" items="${resultRoute.stops}">
        var latLng = new kakao.maps.LatLng(${stop.ai_route_lat}, ${stop.ai_route_long});
        linePath.push(latLng);

        var marker = new kakao.maps.Marker({
            position: latLng,
            title: "${stop.ai_route_description}"
        });
        marker.setMap(map);
    </c:forEach>
    
    // 3. Polyline 그리기
    var polyline = new kakao.maps.Polyline({
        path: linePath,
        strokeWeight: 5,
        strokeColor: '#FF0000',
        strokeOpacity: 0.7,
        strokeStyle: 'solid'
    });
    polyline.setMap(map);
    </script>
    --%>
</body>
</html>