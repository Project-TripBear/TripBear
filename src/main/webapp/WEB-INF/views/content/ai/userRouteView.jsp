<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>내 저장 여행 경로</title>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95f06e859388fb23abc3ac05fa370f48&libraries=services"></script>

<style>
/* 기존 스타일 그대로 복사 */
.container { max-width: 960px; margin: 0 auto; padding: 20px; }
#map { width: 100%; height: 500px; margin-top: 12px; border-radius: 10px; }
.day-btn { margin: 3px; padding: 7px 14px; border: none; border-radius: 8px; background: #4a6cf7; color: #fff; cursor: pointer; font-weight: 600; transition: 0.2s; }
.day-btn.active { background: #2a48c5; transform: scale(1.05); }
/* ... 이하 동일 ... */
</style>
</head>

<body>
<div class="container">
  <h1 id="route-title">내 여행 경로</h1>
  <p id="route-desc">저장된 여행 일정을 확인할 수 있습니다 🌿</p>

  <div id="day-buttons"></div>
  <div id="map"></div>

  <h3 class="section-title">여행 정보</h3>
  <div id="travel-summary"></div>

  <h3 class="section-title">헬스케어 정보</h3>
  <div id="healthcare-summary"></div>

  <button id="delete-route-btn" class="save-btn" style="background:#E74C3C;">이 루트를 삭제하기</button>
</div>

<script>
var contextPath = "${pageContext.request.contextPath}";
if (!contextPath || contextPath === "" || contextPath === "/") contextPath = "/trip";

var userRouteId = (new URLSearchParams(window.location.search).get("id") || "").trim();

var map;
var stopsByDay = {};
var mapElements = {};

// 지도 초기화
function initMap(lat, lng) {
  map = new kakao.maps.Map(document.getElementById("map"), {
    center: new kakao.maps.LatLng(lat, lng),
    level: 8
  });
}

// 활동별 색상 및 아이콘
function getActivityStyle(code) {
  switch (code) {
    case "ARRIVE": return { color: "#7E57C2", icon: "📍" };
    case "WALK": return { color: "#FF9500", icon: "🚶‍♂️" };
    case "EAT": return { color: "#E74C3C", icon: "🍴" };
    case "VISIT": return { color: "#27AE60", icon: "🏛️" };
    case "RETURN": return { color: "#3498DB", icon: "🏠" };
    default: return { color: "#999", icon: "📌" };
  }
}

function clearMap() {
  Object.values(mapElements).forEach(v => {
    if (v.overlays) v.overlays.forEach(o => o.setMap(null));
    if (v.polylines) v.polylines.forEach(p => p.setMap(null));
  });
  mapElements = {};
  document.getElementById("travel-summary").innerHTML = "";
  document.getElementById("healthcare-summary").innerHTML = "";
}

// 카카오 모빌리티 호출
async function drawRoute(start, end) {
  try {
    const url = window.location.origin + contextPath + "/api/mobility/directions?originX=" +
      start.userRouteLong + "&originY=" + start.userRouteLat +
      "&destX=" + end.userRouteLong + "&destY=" + end.userRouteLat;

    const res = await fetch(url);
    if (!res.ok) throw new Error("HTTP " + res.status);
    const json = await res.json();

    let vertexes = [];
    if (json.routes && json.routes[0]?.sections?.[0]?.roads) {
      json.routes[0].sections[0].roads.forEach(r => vertexes = vertexes.concat(r.vertexes));
    }

    const path = [];
    for (let i = 0; i < vertexes.length; i += 2) {
      path.push(new kakao.maps.LatLng(vertexes[i + 1], vertexes[i]));
    }

    const color = "#007AFF";
    const line = new kakao.maps.Polyline({
      path: path, strokeWeight: 5, strokeColor: color, strokeOpacity: 0.9
    });
    line.setMap(map);

    if (path.length > 0) {
      const mid = path[Math.floor(path.length / 2)];
      const label = new kakao.maps.CustomOverlay({
        position: mid,
        content: '<div class="route-mode-label" style="color:' + color + ';">🚗 이동</div>'
      });
      label.setMap(map);
      return { line: line, label: label };
    }
    return { line: line };
  } catch (err) {
    console.error("❌ Mobility API 오류:", err);
    return null;
  }
}

// 여행 요약
function renderTravel(stops) {
  const box = document.getElementById("travel-summary");
  box.innerHTML = "";
  stops.forEach(s => {
    const info = getActivityStyle(s.activityCode);
    const div = document.createElement("div");
    div.className = "travel-card";
    div.innerHTML =
      "<span class='activity-tag' style='background:" + info.color + ";'>" + s.activityCode + "</span>" +
      "<strong>" + info.icon + " " + (s.userRouteDescription || "(이름 없음)") + "</strong>" +
      "<div class='health-info'>" +
      (s.activityCode === "WALK" ? "도보 구간" :
       s.activityCode === "EAT" ? "식사 장소" :
       s.activityCode === "VISIT" ? "관광 명소" :
       s.activityCode === "RETURN" ? "귀가 경로" : "이동 경로") +
      "</div>";
    box.appendChild(div);
  });
}

// 헬스케어
function renderHealth(stops) {
  const box = document.getElementById("healthcare-summary");
  box.innerHTML = "";
  let totalDist = 0, totalSteps = 0, totalKcal = 0;

  const goalDist = 5, goalSteps = 8000, goalKcal = 500;

  stops.forEach(s => {
    const dist = s.walkingDistanceKm || 0;
    const steps = s.walkingStepsCount || 0;
    const kcal = s.healthcareCaloriesBurned || 0;
    totalDist += dist;
    totalSteps += steps;
    totalKcal += kcal;

    const div = document.createElement("div");
    div.className = "health-card";
    div.innerHTML =
      "<strong>" + (s.userRouteDescription || "(이름 없음)") + "</strong>" +
      "<div class='health-info'>🚶 " + dist.toFixed(2) + " km | 🦶 " + steps + " steps | 🔥 " + kcal + " kcal</div>" +
      "<div class='progress-wrap'>" +
        "<div class='progress-label'>도보 거리</div>" +
        "<div class='progress-bar'><div class='progress-fill' style='background:#FF9500;width:" + Math.min(100, (dist/goalDist)*100) + "%'></div></div>" +
        "<div class='progress-label'>걸음 수</div>" +
        "<div class='progress-bar'><div class='progress-fill' style='background:#27AE60;width:" + Math.min(100, (steps/goalSteps)*100) + "%'></div></div>" +
        "<div class='progress-label'>칼로리 소모</div>" +
        "<div class='progress-bar'><div class='progress-fill' style='background:#E74C3C;width:" + Math.min(100, (kcal/goalKcal)*100) + "%'></div></div>" +
      "</div>";
    box.appendChild(div);
  });

  const total = document.createElement("div");
  total.className = "health-total";
  total.innerHTML = "<strong>총합</strong> " + totalDist.toFixed(2) + " km · " + totalSteps + " steps · " + totalKcal + " kcal";
  box.appendChild(total);
}

// 일차 표시
async function displayDay(day) {
  clearMap();
  const stops = stopsByDay[day];
  if (!stops) return;

  const bounds = new kakao.maps.LatLngBounds();
  const overlays = [], lines = [];

  for (let i = 0; i < stops.length; i++) {
    const s = stops[i];
    const info = getActivityStyle(s.activityCode);
    const latlng = new kakao.maps.LatLng(s.userRouteLat, s.userRouteLong);
    bounds.extend(latlng);
    const overlay = new kakao.maps.CustomOverlay({
      position: latlng,
      content: '<div class="custom-overlay-marker" style="background:' + info.color + ';">' + s.userRouteStopOrder + '</div>',
      yAnchor: 1.2
    });
    overlay.setMap(map);
    overlays.push(overlay);
  }

  for (let j = 0; j < stops.length - 1; j++) {
    const seg = await drawRoute(stops[j], stops[j + 1]);
    if (seg?.line) lines.push(seg.line);
    if (seg?.label) overlays.push(seg.label);
  }

  map.setBounds(bounds);
  renderTravel(stops);
  renderHealth(stops);
  mapElements[day] = { overlays: overlays, polylines: lines };
}

// 초기 로드
window.addEventListener("DOMContentLoaded", async function() {
  try {
    const url = window.location.origin + contextPath + "/api/user/route/" + userRouteId;
    const res = await fetch(url, { headers: { "Accept": "application/json" }});
    if (!res.ok) throw new Error("❌ 사용자 루트 불러오기 실패: " + res.status);
    const data = await res.json();

    // 제목 세팅
    if (data.userRouteTitle) {
      document.getElementById("route-title").textContent = data.userRouteTitle;
      document.getElementById("route-desc").textContent = "'" + data.userRouteTitle + "' 일정입니다 🌿";
    }

    if (!data.stops || data.stops.length === 0) {
      document.getElementById("map").innerHTML = "<h4>저장된 경로 데이터가 없습니다.</h4>";
      return;
    }

    initMap(data.stops[0].userRouteLat, data.stops[0].userRouteLong);

    // 일차별 그룹핑
    stopsByDay = data.stops.reduce((acc, s) => {
      (acc[s.userRouteDay] = acc[s.userRouteDay] || []).push(s);
      return acc;
    }, {});

    const btnBox = document.getElementById("day-buttons");
    Object.keys(stopsByDay).sort((a,b)=>a-b).forEach(day => {
      const btn = document.createElement("button");
      btn.className = "day-btn";
      btn.textContent = "Day " + day;
      btn.dataset.day = day;
      btnBox.appendChild(btn);
    });

    btnBox.addEventListener("click", async e => {
      if (!e.target.matches(".day-btn")) return;
      document.querySelectorAll(".day-btn").forEach(b => b.classList.remove("active"));
      e.target.classList.add("active");
      await displayDay(e.target.dataset.day);
    });

    const firstDay = Object.keys(stopsByDay).sort((a,b)=>a-b)[0];
    document.querySelector('[data-day="' + firstDay + '"]').classList.add("active");
    await displayDay(firstDay);
  } catch (err) {
    console.error("❌ 데이터 로드 오류:", err);
    document.getElementById("map").innerHTML = "<h4>루트를 불러오는 중 오류가 발생했습니다.</h4>";
  }
});

// ✅ 삭제 버튼 기능
document.getElementById("delete-route-btn").addEventListener("click", async function() {
  if (!confirm("이 루트를 정말 삭제하시겠습니까?")) return;
  try {
    const res = await fetch(contextPath + "/api/user/route/" + userRouteId, { method: "DELETE" });
    if (res.ok) {
      alert("루트가 삭제되었습니다.");
      location.href = contextPath + "/mypage"; // ✅ 마이페이지나 루트 목록 페이지로 리다이렉트
    } else {
      alert("삭제 실패 (" + res.status + ")");
    }
  } catch (err) {
    console.error("❌ 삭제 오류:", err);
    alert("삭제 중 오류가 발생했습니다.");
  }
});
</script>

</body>
</html>
