<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 
    [Tiles Content - AJAX 방식]
    이 파일은 레이아웃의 'content' 부분에 삽입됩니다.
    <head>나 <body> 태그가 없습니다.
--%>

<%-- 1. 이 페이지에서만 사용할 스타일 --%>
<style>
    .map_wrap {position:relative;width:100%;height:70vh;}
    .info_window {padding:5px; width: 220px; font-size: 12px; line-height: 1.4;}
    .info_window_title {font-weight: bold; font-size: 14px;}
    .info_window_addr {color: #666;}
    .info_window_link {color: blue;}
</style>

<%-- 2. 실제 지도가 표시될 영역 --%>
<div class="map_wrap">
    <div id="map" style="width:100%;height:100%;position:relative;overflow:hidden;"></div> 
</div>

<%-- 3. 카카오맵 API 스크립트 로드 (header.jsp에 없으면 유지) --%>
<script type="text/javascript" 
        src="//dapi.kakao.com/v2/maps/sdk.js?appkey=09d09e9035bb509e8f002c6fab6b12ac&libraries=services"></script>

<%-- 4. 지도 및 AJAX 로직 --%>
<script>
document.addEventListener("DOMContentLoaded", function() {
    const contextPath = "<%= request.getContextPath() %>"; // 항상 "/trip"
    console.log("contextPath:", contextPath);

    const mapContainer = document.getElementById('map');
    const mapOption = {
        center: new kakao.maps.LatLng(37.566826, 126.9786567),
        level: 7
    };
    const map = new kakao.maps.Map(mapContainer, mapOption);
    const markers = [];

    // ✅ 문자열 보간 대신 연결로 수정
    const apiUrl = contextPath + "/allplace/mapok?areaCode=1&contentTypeId=12";
    console.log("fetch URL:", apiUrl);

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) throw new Error('데이터 로드 실패: ' + response.status);
            if (response.status === 204) return [];
            return response.json();
        })
        .then(placeData => {
            console.log("받은 데이터:", placeData);
            if (!placeData || placeData.length === 0) {
                console.warn("표시할 관광지 데이터가 없습니다.");
                return;
            }

            placeData.forEach(place => {
                if (!place.latitude || !place.longitude) return;

                const marker = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(place.latitude, place.longitude)
                });
                marker.setMap(map);

                const iwContent = `
                    <div class="info_window">
                        <img src="${place.placeMainImageUrl || ''}" width="100%" height="80" style="border-radius:6px;"><br>
                        <strong class="info_window_title">${place.name}</strong><br>
                        <span class="info_window_addr">${place.address}</span><br>
                        <a href="${contextPath}/allplace/detail/${place.placeId}" target="_blank" class="info_window_link">상세보기</a>
                    </div>`;

                const infowindow = new kakao.maps.InfoWindow({ content: iwContent, removable: true });
                kakao.maps.event.addListener(marker, 'click', () => infowindow.open(map, marker));
            });

            if (placeData[0]) {
                map.setCenter(new kakao.maps.LatLng(placeData[0].latitude, placeData[0].longitude));
            }
        })
        .catch(error => console.error('지도 데이터 요청 오류:', error));
});
</script>
