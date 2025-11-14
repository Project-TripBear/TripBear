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
                <div class="group-btn" data-type="12">관광지</div>
                <div class="group-btn" data-type="39">음식점</div>
                <div class="group-btn active" data-type="all">전체</div>
            </div>
            <select id="sort-type" style="margin-top:10px; width:100%;">
			    <option value="distance">거리순</option>
			    <option value="name">이름순</option>
			    <option value="popular">인기순</option>
			</select>
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

    /* ===========================
       기본 설정
    ============================ */
    const contextPath = document.getElementById("ctx").dataset.contextPath;

    const map = new kakao.maps.Map(document.getElementById('map'), {
        center: new kakao.maps.LatLng(37.566826, 126.9786567),
        level: 6
    });
    
    const clusterer = new kakao.maps.MarkerClusterer({
        map: map,               // 지도
        averageCenter: true,    // 클러스터 위치를 마커 평균 위치로
        minLevel: 6,            // level 7 이상일 때 개별 마커 보이기
        disableClickZoom: false // 클릭하면 자동 확대
    });

    /* ===========================
       마커 이미지
    ============================ */
    const spotUrl = contextPath + '/resources/img/icon/travel.png';
    const foodUrl = contextPath + '/resources/img/icon/restaurant.png';

    const spotImg = new kakao.maps.MarkerImage(spotUrl, new kakao.maps.Size(30, 35), {offset:new kakao.maps.Point(15, 35)});
    const foodImg = new kakao.maps.MarkerImage(foodUrl, new kakao.maps.Size(30, 35), {offset:new kakao.maps.Point(15, 35)});

    function scaledImg(url, scale = 1.4) {
        let w = 30 * scale;
        let h = 35 * scale;
        return new kakao.maps.MarkerImage(url, new kakao.maps.Size(w, h), {offset:new kakao.maps.Point(w/2, h)});
    }

    const spotImgActive = scaledImg(spotUrl);
    const foodImgActive = scaledImg(foodUrl);

    const myLocImg = new kakao.maps.MarkerImage(
        contextPath + '/resources/img/icon/free-icon-my-location-7233773.png',
        new kakao.maps.Size(30, 30),
        {offset:new kakao.maps.Point(15, 15)}
    );

    /* ===========================
       변수
    ============================ */

    let markers = [];
    let selectedType = "12,39";  // 전체
    let searchKeyword = "";
    let myLocationMarker = null;

    /* ===========================
       검색창 필터링
    ============================ */

    document.getElementById("search-input").addEventListener("input", function () {
        searchKeyword = this.value.trim().toLowerCase();

        closeDetailPanel();
        filterList(searchKeyword);
    });

    function filterList(keyword) {
        const rows = document.querySelectorAll(".place-item");
        rows.forEach(row => {
            const title = row.querySelector("h4").innerText.toLowerCase();
            const addr = row.querySelector("p").innerText.toLowerCase();

            if (title.includes(keyword) || addr.includes(keyword)) {
                row.style.display = "flex";
            } else {
                row.style.display = "none";
            }
        });

        document.getElementById("place-list").scrollTop = 0;
    }
    
    document.getElementById("search-input").addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            let keyword = e.target.value.trim();
            searchGlobal(keyword);
        }
    });

    function searchGlobal(keyword) {

        const geocoder = new kakao.maps.services.Geocoder();

        geocoder.addressSearch(keyword, function(result, status) {

            if (status === kakao.maps.services.Status.OK) {

                const lat = result[0].y;
                const lng = result[0].x;
                const pos = new kakao.maps.LatLng(lat, lng);

                // 지도 이동
                map.setCenter(pos);

                // 이동된 좌표 기준으로 마커 갱신
                updateMarkers();

            } else {
                alert("검색 결과가 없습니다.");
            }
        });
    }

    /* ===========================
       상세 패널
    ============================ */

    function openDetailPanel(place) {
        const panel = document.getElementById("detail-panel");
        document.getElementById("detail-title").innerText = place.name;
        document.getElementById("detail-addr").innerText = place.address || "";
        document.getElementById("detail-view-btn").onclick = () => {
        	window.open(contextPath + "/allplace/view/" + place.placeApiId +
        		    "?contentTypeId=" + place.contentTypeId);
        	console.log("상세보기 URL = " + contextPath + "/allplace/view/" + place.placeApiId);

        };

        panel.classList.add("show");
        panel.classList.remove("hidden");
    }

    function closeDetailPanel() {
        document.getElementById("detail-panel").classList.remove("show");
        document.getElementById("detail-panel").classList.add("hidden");
    }

    document.getElementById("detail-close-btn").onclick = closeDetailPanel;

    /* ===========================
       필터 버튼
    ============================ */

    document.querySelectorAll(".group-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            document.querySelectorAll(".group-btn").forEach(a => a.classList.remove("active"));
            btn.classList.add("active");

            selectedType = (btn.dataset.type === "all" ? "12,39" : btn.dataset.type);

            updateMarkers();
        });
    });

    /* ===========================
       내 위치
    ============================ */

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {
            const lat = pos.coords.latitude;
            const lng = pos.coords.longitude;
            const loc = new kakao.maps.LatLng(lat, lng);

            myLocationMarker = new kakao.maps.Marker({
                position: loc,
                image: myLocImg
            });

            myLocationMarker.setMap(map);
            updateMarkers();

        }, () => updateMarkers());
    } else {
        updateMarkers();
    }

    // 지도 이동 시 내 위치 마커는 지도 중심으로
    kakao.maps.event.addListener(map, "idle", () => {
        if (myLocationMarker) {
            myLocationMarker.setPosition(map.getCenter());
        }

        updateMarkers();  // 자동 갱신
    });

    /* ===========================
       updateMarkers()
    ============================ */

    function updateMarkers() {

        const center = map.getCenter();
        const apiUrl =
            contextPath +
            "/allplace/mapok?lat=" + center.getLat() +
            "&lng=" + center.getLng() +
            "&radius=20000" +
            "&contentTypeId=" + selectedType +
            "&keyword=" + encodeURIComponent(searchKeyword) +
            "&sort=" + document.getElementById("sort-type").value;

        fetch(apiUrl)
            .then(res => res.status === 204 ? [] : res.json())
            .then(list => {

                // 기존 마커 제거
                markers.forEach(m => m.setMap(null));
                markers = [];

                // 클러스터러 초기화
                clusterer.clear();

                const listBox = document.getElementById("place-list");
                listBox.innerHTML = "";

                if (!list || list.length === 0) return;

                const bounds = map.getBounds();
                const sw = bounds.getSouthWest();
                const ne = bounds.getNorthEast();

                const visibleMarkers = []; // 클러스터러에 넣을 마커들

                list.forEach(place => {
                	place.contentTypeId = (place.placeTypeId == 3 ? 39 : 12);
                    const inView =
                        place.latitude >= sw.getLat() &&
                        place.latitude <= ne.getLat() &&
                        place.longitude >= sw.getLng() &&
                        place.longitude <= ne.getLng();

                    if (!inView) return;

                    let img = (place.placeTypeId == 3) ? foodImg : spotImg;
                    let imgActive = (place.placeTypeId == 3) ? foodImgActive : spotImgActive;

                    const marker = new kakao.maps.Marker({
                        position: new kakao.maps.LatLng(place.latitude, place.longitude),
                        image: img
                    });

                    marker.pid = place.placeApiId;
                    marker.normalImg = img;
                    marker.activeImg = imgActive;

                    markers.push(marker);
                    visibleMarkers.push(marker);  // 🔥 클러스터러에 넣을 전용 배열

                    /* hover 이벤트 */
                    kakao.maps.event.addListener(marker, "mouseover", () => {
                        marker.setImage(marker.activeImg);
                        marker.setZIndex(9999);

                        const row = document.querySelector('.place-item[data-pid="' + marker.pid + '"]');
                        if (row) row.classList.add("hover");
                    });

                    kakao.maps.event.addListener(marker, "mouseout", () => {
                        marker.setImage(marker.normalImg);
                        marker.setZIndex(1);

                        const row = document.querySelector('.place-item[data-pid="' + marker.pid + '"]');
                        if (row) row.classList.remove("hover");
                    });

                    /* click 이벤트 */
                    kakao.maps.event.addListener(marker, "click", () => {
                        openDetailPanel(place);
                        highlightRow(place.placeApiId);
                    });

                    /* 리스트 DOM 생성 */
                    const row = document.createElement("div");
                    row.className = "place-item";
                    row.dataset.pid = place.placeApiId;

                    const name = place.name || "이름 없음";
                    const addr = place.address || "";
                    const imgUrl = place.placeMainImageUrl || (contextPath + "/resources/img/icon/noimage.png");

                    row.innerHTML =
                        '<img src="' + imgUrl + '">' +
                        '<div class="place-info">' +
                        '  <h4>' + name + '</h4>' +
                        '  <p>' + addr + '</p>' +
                        '</div>';

                    row.addEventListener("mouseenter", () => {
                        marker.setImage(marker.activeImg);
                        marker.setZIndex(9999);
                    });

                    row.addEventListener("mouseleave", () => {
                        marker.setImage(marker.normalImg);
                        marker.setZIndex(1);
                    });

                    row.addEventListener("click", () => {
                        map.panTo(new kakao.maps.LatLng(place.latitude, place.longitude));
                        openDetailPanel(place);
                        highlightRow(place.placeApiId);
                    });

                    listBox.appendChild(row);
                });

                /* 🔥 클러스터러에 추가 */
                clusterer.addMarkers(visibleMarkers);

                filterList(searchKeyword);
            });
    }


    /* 리스트 강조 */
    function highlightRow(pid) {
        document.querySelectorAll(".place-item").forEach(r => r.classList.remove("active"));
        const row = document.querySelector('.place-item[data-pid="' + pid + '"]');
        if (row) row.classList.add("active");
    }

});
</script>