<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>AI 추천 여행 경로</title>

<!-- ✅ Spring Security CSRF 토큰 -->
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<!-- ✅ Kakao Map SDK -->
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95f06e859388fb23abc3ac05fa370f48&libraries=services"></script>

<style>
.container { max-width: 960px; margin: 0 auto; padding: 20px; }
#map { width: 100%; height: 500px; margin-top: 12px; border-radius: 10px; }
#day-buttons { text-align: center; margin: 20px 0 10px; }
.day-btn { margin: 3px; padding: 7px 14px; border: none; border-radius: 8px; background: #4a6cf7; color: #fff; cursor: pointer; font-weight: 600; transition: 0.2s; }
.day-btn.active { background: #2a48c5; transform: scale(1.05); }

.section-title { font-weight: bold; margin-top: 25px; font-size: 17px; color: #222; }
.travel-card, .health-card {
  background: #fff; border-radius: 10px; padding: 12px 16px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.08); margin-bottom: 10px;
}
.activity-tag {
  display:inline-block; color:white; font-size:11px; font-weight:bold;
  padding:3px 8px; border-radius:8px; margin-right:8px;
}
.health-info { font-size: 13px; color:#555; margin-top:4px; }
.health-total { text-align:center; margin-top:10px; font-weight:700; color:#333; background:#f5f7ff; border-radius:8px; padding:10px; }

.custom-overlay-marker {
  color:white; font-size:13px; font-weight:bold;
  border-radius:50%; width:26px; height:26px;
  display:flex; justify-content:center; align-items:center;
  border:2px solid white; box-shadow:0 2px 5px rgba(0,0,0,0.3);
}
.route-mode-label {
  background:white; padding:2px 8px; border-radius:6px;
  font-weight:bold; font-size:12px; box-shadow:0 1px 3px rgba(0,0,0,0.2);
}
.save-btn { margin-top:20px; padding:10px 18px; border:none; background:#4a6cf7; color:white; border-radius:8px; cursor:pointer; font-weight:600; }
.save-btn:hover { background:#2a48c5; }

.progress-wrap { margin-top:8px; }
.progress-label { font-size:12px; color:#555; margin-bottom:4px; }
.progress-bar { height:8px; background:#e6e9ff; border-radius:6px; overflow:hidden; }
.progress-fill { height:100%; border-radius:6px; }

.icon { font-size:18px; margin-right:6px; }
</style>
</head>

<body>
<div class="container">
  <h1 id="route-title">AI 추천 여행 경로</h1>
  <p id="route-desc">AI가 분석한 최적의 일정과 활동 요약을 한눈에 확인하세요 🧭</p>

  <div id="day-buttons"></div>
  <div id="map"></div>

  <h3 class="section-title">여행 정보</h3>
  <div id="travel-summary"></div>

  <h3 class="section-title">헬스케어 정보</h3>
  <div id="healthcare-summary"></div>

  <button id="save-route-btn" class="save-btn">이 경로를 내 루트로 저장하기</button>
</div>

<script>
/* ================================
   🔥 전역 변수
================================ */
console.log("로그인 사용자:", "${pageContext.request.userPrincipal != null ? pageContext.request.userPrincipal.name : '비로그인'}");

var contextPath = "${pageContext.request.contextPath}";
if (!contextPath || contextPath === "" || contextPath === "/") contextPath = "/trip";

var aiRouteId = (new URLSearchParams(window.location.search).get("id") || "").trim();

var map;
var stopsByDay = {};
var mapElements = {};   // day 별 { overlays, polylines }

/* ================================
   🔥 전역 함수 1: 지도 초기화
================================ */
function initMap(lat, lng) {
  map = new kakao.maps.Map(document.getElementById("map"), {
    center: new kakao.maps.LatLng(lat, lng),
    level: 8
  });
}

/* ================================
   🔥 전역 함수 2: 지도 요소 삭제
================================ */
function clearMap() {
  Object.values(mapElements).forEach(v => {
    if (v.overlays) v.overlays.forEach(o => o.setMap(null));
    if (v.polylines) v.polylines.forEach(p => p.setMap(null));
  });
  mapElements = {};

  document.getElementById("travel-summary").innerHTML = "";
  document.getElementById("healthcare-summary").innerHTML = "";
}

/* ================================
   🔥 전역 함수 3: 카카오 로컬 검색 → 좌표
================================ */
async function getCoordsByKeyword(name) {
  if (!name) return null;

  try {
    const res = await fetch(contextPath + "/api/local/keyword?query=" + encodeURIComponent(name));
    if (!res.ok) return null;
    const json = await res.json();

    if (!json.documents || json.documents.length === 0) return null;

    return {
      lat: parseFloat(json.documents[0].y),
      lng: parseFloat(json.documents[0].x),
      name: json.documents[0].place_name
    };
  } catch (e) {
    console.error("좌표 검색 실패:", name, e);
    return null;
  }
}

/* ================================
   🔥 전역 함수 4: 네이버 경로 호출
================================ */
async function drawNaverRoute(start, end, mode) {
  try {
    const url = contextPath + "/api/naver/directions"
              + "?start=" + start.lng + "," + start.lat
              + "&goal=" + end.lng + "," + end.lat
              + "&mode=" + mode;

    const res = await fetch(url);
    if (!res.ok) return null;

    const data = await res.json();

    const section =
      data.route?.traoptimal?.[0] ||
      data.route?.trawalking?.[0] ||
      data.route?.tradriving?.[0] ||
      data.route?.transit?.[0];

    if (!section || !section.path) return null;

    const path = section.path.map(p => new kakao.maps.LatLng(p[1], p[0]));

    const colors = {
      driving: "#4A6CF7",
      walking: "#FF9500",
      bicycle: "#27AE60",
      transit: "#9C27B0"
    };

    const poly = new kakao.maps.Polyline({
      path,
      strokeWeight: 5,
      strokeColor: colors[mode] || "#4A6CF7",
      strokeOpacity: 0.9
    });

    poly.setMap(map);
    return poly;

  } catch (err) {
    console.error("네이버 경로 오류:", err);
    return null;
  }
}

/* ================================
   🔥 전역 함수 5: DB 이동수단 → 네이버 mode 매핑
================================ */
function convertMode(mode) {
  if (!mode) return "driving"; // null → CAR로 처리

  const m = {
    CAR: "driving",
    WALK: "walking",
    BIKE: "bicycle",
    PUBLIC_TRANSPORT: "transit"
  };
  return m[mode] || "driving";
}

/* ================================
   🔥 전역 함수 6: 두 좌표 사이 segment polyline
================================ */
async function drawSegment(startStop, endStop) {

  if (!startStop.realLat || !startStop.realLng) {
    const sCoord = await getCoordsByKeyword(startStop.aiRouteDescription);
    if (sCoord) {
      startStop.realLat = sCoord.lat;
      startStop.realLng = sCoord.lng;
    }
  }

  if (!endStop.realLat || !endStop.realLng) {
    const eCoord = await getCoordsByKeyword(endStop.aiRouteDescription);
    if (eCoord) {
      endStop.realLat = eCoord.lat;
      endStop.realLng = eCoord.lng;
    }
  }

  if (!startStop.realLat || !endStop.realLat) return null;

  const start = { lat: startStop.realLat, lng: startStop.realLng };
  const end   = { lat: endStop.realLat,  lng: endStop.realLng };

  const mode = convertMode(startStop.transportationMode);

  return await drawNaverRoute(start, end, mode);
}

/* ================================
   🔥 전역 함수 7: 하루 일정 표시
================================ */
async function displayDay(day) {

  clearMap();

  const stops = stopsByDay[day];
  if (!stops) return;

  const bounds = new kakao.maps.LatLngBounds();
  const overlays = [];
  const polylines = [];

  // 마커 표시
  for (let i = 0; i < stops.length; i++) {
    const s = stops[i];

    if (!s.realLat || !s.realLng) {
      const c = await getCoordsByKeyword(s.aiRouteDescription);
      if (c) {
        s.realLat = c.lat;
        s.realLng = c.lng;
      } else {
        console.warn("좌표 없음 → 스킵", s.aiRouteDescription);
        continue;
      }
    }

    const latlng = new kakao.maps.LatLng(s.realLat, s.realLng);
    bounds.extend(latlng);

    const overlay = new kakao.maps.CustomOverlay({
      position: latlng,
      content:
        '<div class="custom-overlay-marker" style="background:#4A6CF7;">'
        + s.aiRouteStopOrder +
        '</div>',
      yAnchor: 1.2
    });

    overlay.setMap(map);
    overlays.push(overlay);
  }

  // 구간 polyline
  for (let i = 0; i < stops.length - 1; i++) {
    const line = await drawSegment(stops[i], stops[i + 1]);
    if (line) polylines.push(line);
  }

  map.setBounds(bounds);

  mapElements[day] = { overlays, polylines };
}

/* ================================
   🔥 페이지 로드 시 실행  
================================ */
window.addEventListener("DOMContentLoaded", async function() {

  try {
    const url = window.location.origin + contextPath + "/api/ai/route/" + aiRouteId;
    const res = await fetch(url, { headers: { "Accept": "application/json" }});
    const data = await res.json();

    // 제목
    document.getElementById("route-title").textContent = data.ai_route_title;

    // Day 그룹화
    stopsByDay = data.stops.reduce((acc, s) => {
      (acc[s.aiRouteDay] = acc[s.aiRouteDay] || []).push(s);
      return acc;
    }, {});

    // Day 버튼 생성
    const btnBox = document.getElementById("day-buttons");
    btnBox.innerHTML = "";

    const days = Object.keys(stopsByDay).sort((a,b) => a - b);

    days.forEach(day => {
      const btn = document.createElement("button");
      btn.className = "day-btn";
      btn.dataset.day = day;
      btn.textContent = "Day " + day;
      btnBox.appendChild(btn);
    });

    // 버튼 클릭 이벤트
    btnBox.addEventListener("click", async e => {
      if (!e.target.matches(".day-btn")) return;

      document.querySelectorAll(".day-btn").forEach(b => b.classList.remove("active"));
      e.target.classList.add("active");

      await displayDay(e.target.dataset.day);
    });

    // 지도 초기화 (기본 서울)
    initMap(37.5665, 126.9780);

    // 첫번째 day 자동 선택
    if (days.length > 0) {
      const firstDay = days[0];
      const firstBtn = document.querySelector(`[data-day="${firstDay}"]`);

      if (firstBtn) {
        firstBtn.classList.add("active");
        await displayDay(firstDay);
      }
    }

  } catch (err) {
    console.error("데이터 로드 오류:", err);
  }
});
</script>


</body>
</html>
