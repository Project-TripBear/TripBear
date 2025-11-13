<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- ★ contextPath 안전 전달 -->
<div id="ctx" data-context-path="${pageContext.request.contextPath}"></div>

<!-- ★ 사이드바/지도 CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mapsidebar.css">

<div class="map-container">

    <!-- LEFT SIDEBAR -->
    <div class="sidebar">

        <div class="sidebar-header">
            <input type="text" id="search-input" placeholder="장소 검색 (개발 예정)">

            <div class="button-group">
                <div class="group-btn active" data-type="12">관광지</div>
                <div class="group-btn" data-type="39">음식점</div>
                <div class="group-btn" data-type="all">전체</div>
            </div>
        </div>

        <div class="place-list-container" id="place-list"></div>
    </div>

    <!-- MAP -->
    <div id="map"></div>
</div>

<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=09d09e9035bb509e8f002c6fab6b12ac&libraries=services,clusterer"></script>

<script>
document.addEventListener("DOMContentLoaded", function () {

    /* =====================================================
       ⭐ contextPath — 절대 안전하게 가져오기
       ===================================================== */
    const contextPath = document.getElementById("ctx").dataset.contextPath;
    console.log("🔥 contextPath =", contextPath);

    const mapContainer = document.getElementById('map');

    const map = new kakao.maps.Map(mapContainer, {
        center: new kakao.maps.LatLng(37.566826, 126.9786567),
        level: 7
    });

    /* ---------- Marker Images ---------- */
    const markerImageSpot = new kakao.maps.MarkerImage(
        contextPath + '/resources/img/icon/travel.png',
        new kakao.maps.Size(30, 35),
        { offset: new kakao.maps.Point(15, 35) }
    );

    const markerImageFood = new kakao.maps.MarkerImage(
        contextPath + '/resources/img/icon/restaurant.png',
        new kakao.maps.Size(30, 35),
        { offset: new kakao.maps.Point(15, 35) }
    );

    const markerImageMyLocation = new kakao.maps.MarkerImage(
        contextPath + '/resources/img/icon/free-icon-my-location-7233773.png',
        new kakao.maps.Size(30, 30),
        { offset: new kakao.maps.Point(15, 15) }
    );

    /* ---------- Clusterer ---------- */
    const clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 7
    });

    let markers = [];
    let idleTimer = null;
    let selectedType = "12,39";

    /* ---------- 필터 버튼 ---------- */
    document.querySelectorAll(".group-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            document.querySelectorAll(".group-btn").forEach(b => b.classList.remove("active"));
            btn.classList.add("active");

            let type = btn.dataset.type;
            selectedType = (type === "all" ? "12,39" : type);

            updateMarkers();
        });
    });

    /* ---------- 내 위치 ---------- */
    let myLocationMarker = null;

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {

            const loc = new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude);

            myLocationMarker = new kakao.maps.Marker({
                position: loc,
                image: markerImageMyLocation
            });

            myLocationMarker.setMap(map);
            map.setCenter(loc);

            updateMarkers();
        }, () => updateMarkers());
    } else {
        updateMarkers();
    }

    /* ---------- idle throttle ---------- */
    kakao.maps.event.addListener(map, "idle", function () {
        if (idleTimer) clearTimeout(idleTimer);
        idleTimer = setTimeout(updateMarkers, 150);
    });

    /* =====================================================
       ⭐ updateMarkers — 지도 + 사이드바 핵심 기능
       ===================================================== */
    function updateMarkers() {

        const center = map.getCenter();
        const lat = center.getLat();
        const lng = center.getLng();
        const radius = 3000;

        const apiUrl =
            contextPath +
            "/allplace/mapok?lat=" + lat +
            "&lng=" + lng +
            "&radius=" + radius +
            "&contentTypeId=" + selectedType;

        console.log("🔥 FETCH =>", apiUrl);

        fetch(apiUrl)
            .then(res => res.status === 204 ? [] : res.json())
            .then(data => {

                /* 지도 초기화 */
                clusterer.clear();
                markers = [];

                /* 리스트 초기화 */
                const listContainer = document.getElementById("place-list");
                listContainer.innerHTML = "";

                if (!data || data.length === 0) {
                    listContainer.innerHTML = "<p style='padding:15px;color:#666;'>데이터 없음</p>";
                    return;
                }
                data.forEach(place => {
                	 
                    if (!place.latitude || !place.longitude) return;

                    let markerImg =
                        place.placeTypeId === 3 ? markerImageFood : markerImageSpot;

                    const marker = new kakao.maps.Marker({
                        position: new kakao.maps.LatLng(place.latitude, place.longitude),
                        image: markerImg
                    });

                    markers.push(marker);

                    kakao.maps.event.addListener(marker, "click", () => {
                        window.open(contextPath + "/allplace/view/" + place.placeApiId, "_blank");
                    });

                    /* ---------- 리스트 생성 ---------- */
                    const nameText = (place.name && place.name !== "false") ? place.name : "이름 없음";
                    const addressText = (place.address && place.address !== "false") ? place.address : "";

                    /* ---------- 이미지 처리 ---------- */
                    let imgUrl = place.placeMainImageUrl;
                    if (!imgUrl || imgUrl === "false") {
                        imgUrl = contextPath + "/resources/img/icon/noimage.png";
                    }

                    const row = document.createElement("div");
                    row.className = "place-item";

                    row.innerHTML = `
                        <img src="\${imgUrl}">
                        <div class="place-info">
                            <h4>\${nameText}</h4>
                            <p>\${addressText}</p>
                        </div>
                    `;


                    row.addEventListener("click", () => {
                        window.open(contextPath + "/allplace/view/" + place.placeApiId, "_blank");
                    });

                    listContainer.appendChild(row);


                });

                clusterer.addMarkers(markers);
            })
            .catch(err => console.error("❌ API ERROR:", err));
    }
});
</script>