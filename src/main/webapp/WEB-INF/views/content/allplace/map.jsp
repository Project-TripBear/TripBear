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
            <input type="text" id="search-input" placeholder="장소 검색">

            <div class="button-group">
                <div class="group-btn active" data-type="12">관광지</div>
                <div class="group-btn" data-type="39">음식점</div>
                <div class="group-btn" data-type="all">전체</div>
            </div>
        </div>
		<div id="detail-panel" class="detail-panel hidden">
		    <button id="detail-close-btn" class="close-btn">×</button>
		    <h3 id="detail-title"></h3>
		    <p id="detail-addr"></p>
		    <button id="detail-view-btn" class="view-btn">상세 보기</button>
		</div>
        <div class="place-list-container" id="place-list"></div>
    </div>

    <!-- MAP -->
    <div id="map"></div>
</div>

<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=09d09e9035bb509e8f002c6fab6b12ac&libraries=services,clusterer"></script>

<script>
document.addEventListener("DOMContentLoaded", function () {

    const contextPath = document.getElementById("ctx").dataset.contextPath;

    const map = new kakao.maps.Map(document.getElementById('map'), {
        center: new kakao.maps.LatLng(37.566826, 126.9786567),
        level: 7
    });

    const markerImageSpotUrl = contextPath + '/resources/img/icon/travel.png';
    const markerImageFoodUrl = contextPath + '/resources/img/icon/restaurant.png';
    
    /* =====================================================
       ⭐ 이미지들
    ===================================================== */
    const markerImageSpot = new kakao.maps.MarkerImage(
    	    markerImageSpotUrl,
    	    new kakao.maps.Size(30, 35),
    	    { offset: new kakao.maps.Point(15, 35) }
    	);

    	const markerImageSpotActive = getScaledMarkerImage(markerImageSpotUrl); // 확대됨

    	const markerImageFood = new kakao.maps.MarkerImage(
    	    markerImageFoodUrl,
    	    new kakao.maps.Size(30, 35),
    	    { offset: new kakao.maps.Point(15, 35) }
    	);

    	const markerImageFoodActive = getScaledMarkerImage(markerImageFoodUrl);

    const markerImageMyLocation = new kakao.maps.MarkerImage(
        contextPath + '/resources/img/icon/free-icon-my-location-7233773.png',
        new kakao.maps.Size(30, 30),
        { offset: new kakao.maps.Point(15, 15) }
    );
    
    function getScaledMarkerImage(url, scale = 1.3) {
        const baseW = 30;
        const baseH = 35;
        const w = baseW * scale;
        const h = baseH * scale;

        return new kakao.maps.MarkerImage(
            url,
            new kakao.maps.Size(w, h),
            { offset: new kakao.maps.Point(w/2, h) }
        );
    }

    /* =====================================================
       ⭐ 클러스터러
    ===================================================== */
    const clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 7
    });

    let markers = [];
    let idleTimer = null;
    let selectedType = "12,39";
    let searchKeyword = "";
    let sortType = "distance";
    let myLocationMarker = null;

    /* =====================================================
       ⭐ 검색 기능
    ===================================================== */
    document.getElementById("search-input").addEventListener("input", () => {
        searchKeyword = document.getElementById("search-input").value.trim();
        updateMarkers();
    });

    /* =====================================================
       ⭐ 정렬 박스 생성
    ===================================================== */
    const sortSelect = document.createElement("select");
    sortSelect.id = "sort-type";
    sortSelect.style.marginTop = "10px";
    sortSelect.style.width = "100%";
    sortSelect.innerHTML = `
        <option value="distance">거리순</option>
        <option value="name">이름순</option>
        <option value="popular">인기순</option>
    `;
    document.querySelector(".sidebar-header").appendChild(sortSelect);

    sortSelect.addEventListener("change", function () {
        sortType = this.value;
        updateMarkers();
    });

    /* =====================================================
       ⭐ 사이드바 너비 기반 offset 계산
    ===================================================== */
    function getDynamicOffsetX() {
        const sidebar = document.querySelector(".sidebar");
        return sidebar.offsetWidth + 40; // 사이드바 + 여유
    }
    function getDynamicOffsetY() {
        return -40;  // 위로 40px
    }

    function moveCenterOffset(lat, lng) {
        const offsetX = getDynamicOffsetX();
        const offsetY = getDynamicOffsetY();

        const proj = map.getProjection();
        const point = proj.containerPointFromCoords(new kakao.maps.LatLng(lat, lng));
        const newPoint = new kakao.maps.Point(point.x + offsetX, point.y + offsetY);
        const newLatLng = proj.coordsFromContainerPoint(newPoint);

        map.panTo(newLatLng);
    }

    function getOffsetLatLng(latlng) {
        const offsetX = -getDynamicOffsetX();
        const offsetY = getDynamicOffsetY();

        const proj = map.getProjection();
        const point = proj.containerPointFromCoords(latlng);
        const newPoint = new kakao.maps.Point(point.x + offsetX, point.y + offsetY);

        return proj.coordsFromContainerPoint(newPoint);
    }

    /* =====================================================
       ⭐ 상세 패널 제어
    ===================================================== */
    function openDetailPanel(place) {
        const panel = document.getElementById("detail-panel");
        document.getElementById("detail-title").innerText = place.name;
        document.getElementById("detail-addr").innerText = place.address || "";

        document.getElementById("detail-view-btn").onclick = () => {
            window.open(contextPath + "/allplace/view/" + place.placeApiId, "_blank");
        };

        panel.classList.remove("hidden");
    }

    document.getElementById("detail-close-btn").onclick = () => {
        document.getElementById("detail-panel").classList.add("hidden");
    };

    /* =====================================================
       ⭐ 필터 버튼
    ===================================================== */
    document.querySelectorAll(".group-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            document.querySelectorAll(".group-btn").forEach(b => b.classList.remove("active"));
            btn.classList.add("active");

            selectedType = (btn.dataset.type === "all" ? "12,39" : btn.dataset.type);

            updateMarkers();
        });
    });

    /* =====================================================
       ⭐ 내 위치 기능
    ===================================================== */
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {
            const lat = pos.coords.latitude;
            const lng = pos.coords.longitude;
            const loc = new kakao.maps.LatLng(lat, lng);

            myLocationMarker = new kakao.maps.Marker({
                position: loc,
                image: markerImageMyLocation
            });

            myLocationMarker.setMap(map);

            // 초기 오프셋 적용
            moveCenterOffset(lat, lng);

            updateMarkers();
        }, () => updateMarkers());
    } else {
        updateMarkers();
    }

    // 지도 idle 때 매번 보정
    kakao.maps.event.addListener(map, "idle", () => {
        if (myLocationMarker) {
            const center = map.getCenter();
            const adjusted = getOffsetLatLng(center);
            myLocationMarker.setPosition(adjusted);
        }
    });

    /* =====================================================
       ⭐ updateMarkers()
    ===================================================== */
    function updateMarkers() {

        const center = map.getCenter();
        const lat = center.getLat();
        const lng = center.getLng();
        const radius = 6000;

        const apiUrl =
            contextPath +
            "/allplace/mapok?lat=" + lat +
            "&lng=" + lng +
            "&radius=" + radius +
            "&contentTypeId=" + selectedType +
            "&keyword=" + encodeURIComponent(searchKeyword) +
            "&sort=" + sortType;

        fetch(apiUrl)
            .then(res => res.status === 204 ? [] : res.json())
            .then(data => {

                clusterer.clear();
                markers = [];

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

                    marker.pid = place.placeApiId;
                    markers.push(marker);

                    /* ⭐ 마커 hover */
                    kakao.maps.event.addListener(marker, "mouseover", () => {
					    marker.setImage(
					        place.placeTypeId === 3 ? markerImageFoodActive : markerImageSpotActive
					    );
					    marker.setZIndex(9999);
					});
					
					kakao.maps.event.addListener(marker, "mouseout", () => {
					    marker.setImage(
					        place.placeTypeId === 3 ? markerImageFood : markerImageSpot
					    );
					    marker.setZIndex(1);
					});


                    /* ⭐ 마커 click → 리스트 강조 + 패널 */
                    kakao.maps.event.addListener(marker, "click", () => {

                        const row = document.querySelector(`.place-item[data-pid="${place.placeApiId}"]`);
                        if (row) {
                            document.querySelectorAll(".place-item").forEach(i => i.classList.remove("active"));
                            row.classList.add("active");

                            listContainer.scrollTop = row.offsetTop - 10;
                        }

                        openDetailPanel(place);
                    });

                    /* ⭐ 리스트 생성 */
                    const row = document.createElement("div");
                    row.className = "place-item";
                    row.dataset.pid = place.placeApiId;

                    const nameText = place.name;
                    const addressText = place.address || "";
                    let imgUrl = place.placeMainImageUrl;
                    if (!imgUrl || imgUrl === "false") {
                        imgUrl = contextPath + "/resources/img/icon/noimage.png";
                    }

                    row.innerHTML = `
                        <img src="\${imgUrl}">
                        <div class="place-info">
                            <h4>\${nameText}</h4>
                            <p>\${addressText}</p>
                        </div>
                    `;

                    /* ⭐ 리스트 hover → 마커 강조 */
                    row.addEventListener("mouseenter", () => {
                        const m = markers.find(m => m.pid === place.placeApiId);
                        if (m) m.setZIndex(9999);
                    });
                    row.addEventListener("mouseleave", () => {
                        const m = markers.find(m => m.pid === place.placeApiId);
                        if (m) m.setZIndex(1);
                    });

                    /* ⭐ 리스트 click → 지도 이동 + 패널 */
                    row.addEventListener("click", () => {

                        document.querySelectorAll(".place-item").forEach(i => i.classList.remove("active"));
                        row.classList.add("active");

                        map.panTo(new kakao.maps.LatLng(place.latitude, place.longitude));

                        const m = markers.find(m => m.pid === place.placeApiId);
                        if (m) m.setZIndex(9999);

                        openDetailPanel(place);
                    });

                    listContainer.appendChild(row);
                });

                clusterer.addMarkers(markers);
            });

    }

});
</script>