<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="route-container ai-route-page">
    <header class="route-header">
        <h1 id="route-title" class="route-title">AI 추천 여행 경로</h1>
        <p id="route-desc" class="route-subtitle">
            AI가 분석한 최적의 일정과 활동 요약을 한눈에 확인하세요 🧭
        </p>
    </header>

    <!-- 날짜 선택 버튼 -->
    <div id="day-buttons" class="day-buttons"></div>

    <!-- 지도 영역 -->
    <div id="map" class="route-map"></div>

    <!-- 여행 정보 요약 -->
    <section class="route-section">
        <h2 class="route-section-title">여행 정보</h2>
        <div id="travel-summary" class="summary-list"></div>
    </section>

    <!-- 헬스케어 요약 -->
    <section class="route-section">
        <h2 class="route-section-title">헬스케어 정보</h2>
        <div id="healthcare-summary" class="summary-list"></div>
    </section>

    <!-- 저장 버튼 -->
    <div class="route-footer">
        <button id="save-route-btn" class="btn save-btn">
            이 경로를 내 루트로 저장하기
        </button>
    </div>
</div>

<%-- ✅ JS는 여기에서만 --%>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95f06e859388fb23abc3ac05fa370f48&libraries=services"></script>
<script>
console.log("로그인 사용자:", "${pageContext.request.userPrincipal != null ? pageContext.request.userPrincipal.name : '비로그인'}");

// ✅ 공통 변수 설정
var contextPath = "${pageContext.request.contextPath}";
if (!contextPath || contextPath === "" || contextPath === "/") contextPath = "/trip";

var aiRouteId = (new URLSearchParams(window.location.search).get("id") || "").trim();
var map, stopsByDay = {}, mapElements = {};

// ✅ 지도 초기화
function initMap(lat, lng) {
  map = new kakao.maps.Map(document.getElementById("map"), {
    center: new kakao.maps.LatLng(lat, lng),
    level: 8
  });
}

// ✅ 활동별 스타일
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

// ✅ 지도 클리어
function clearMap() {
  Object.values(mapElements).forEach(v=>{
    if (v.markers) v.markers.forEach(m=>m.setMap(null));
    if (v.overlays) v.overlays.forEach(o=>o.setMap(null));
    if (v.polylines) v.polylines.forEach(p=>p.setMap(null));
  });
  mapElements = {};
  document.getElementById("travel-summary").innerHTML = "";
  document.getElementById("healthcare-summary").innerHTML = "";
}

// ✅ 경로 그리기
const routeColors = [
  "#4A6CF7",
  "#FF5722",
  "#9C27B0",
  "#009688",
  "#FBC02D",
  "#E91E63",
  "#795548"
];

//✅ AI 루트 경로 그리기 (index 색상 적용)
async function drawRoute(start, end, index) {
  try {
    const url = window.location.origin + contextPath +
      "/api/mobility/directions?originX=" + start.aiRouteLong +
      "&originY=" + start.aiRouteLat +
      "&destX=" + end.aiRouteLong +
      "&destY=" + end.aiRouteLat;

    const res = await fetch(url);
    if (!res.ok) return null;

    const json = await res.json();
    console.log("AI Mobility 응답:", json);

    let path = [];
    const roads = json.routes?.[0]?.sections?.[0]?.roads;
    if (!roads) return null;

    roads.forEach(r => {
      for (let i = 0; i < r.vertexes.length; i += 2) {
        path.push(new kakao.maps.LatLng(r.vertexes[i + 1], r.vertexes[i]));
      }
    });

    const polyline = new kakao.maps.Polyline({
      path,
      strokeWeight: 4,
      strokeColor: routeColors[index % routeColors.length],
      strokeOpacity: 0.9
    });

    polyline.setMap(map);
    return polyline;

  } catch (err) {
    console.error("❌ drawRoute 오류:", err);
    return null;
  }
}



// ✅ 여행정보 렌더링
function renderTravel(stops) {
  var box = document.getElementById("travel-summary");
  box.innerHTML = "";
  stops.forEach(s=>{
    var info = getActivityStyle(s.activityCode);
    var div = document.createElement("div");
    div.className = "travel-card";
    div.innerHTML =
      "<span class='activity-tag' style='background:" + info.color + ";'>" + s.activityCode + "</span>" +
      "<strong>" + info.icon + " " + (s.aiRouteDescription || "(이름 없음)") + "</strong>" +
      "<div class='health-info'>" +
      (s.activityCode === "WALK" ? "도보 이동 구간" :
       s.activityCode === "EAT" ? "식사 장소" :
       s.activityCode === "VISIT" ? "관광 명소" :
       s.activityCode === "RETURN" ? "귀가 경로" : "이동 경로") +
      "</div>";
    box.appendChild(div);
  });
}

// ✅ 헬스정보 렌더링
function renderHealth(stops) {
  var box = document.getElementById("healthcare-summary");
  box.innerHTML = "";
  var totalDist = 0, totalSteps = 0, totalKcal = 0;

  var goalDist = 5, goalSteps = 8000, goalKcal = 500;

  stops.forEach(s=>{
    totalDist += s.walkingDistanceKm || 0;
    totalSteps += s.walkingStepsCount || 0;
    totalKcal += s.healthcareCaloriesBurned || 0;

    var dist = s.walkingDistanceKm || 0;
    var steps = s.walkingStepsCount || 0;
    var kcal = s.healthcareCaloriesBurned || 0;

    var div = document.createElement("div");
    div.className = "health-card";
    div.innerHTML =
      "<strong>" + (s.aiRouteDescription || "(이름 없음)") + "</strong>" +
      "<div class='health-info'>🚶‍♂️ " + dist.toFixed(2) + " km | 🦶 " + steps + " steps | 🔥 " + kcal + " kcal</div>" +
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

  var total = document.createElement("div");
  total.className = "health-total";
  total.innerHTML = "<strong>총합</strong> " + totalDist.toFixed(2) + " km · " + totalSteps + " steps · " + totalKcal + " kcal";
  box.appendChild(total);
}

// ✅ 일차별 표시
async function displayDay(day) {
  clearMap();
  var stops = stopsByDay[day];
  if (!stops) return;

  var bounds = new kakao.maps.LatLngBounds();
  var overlays = [], lines = [];

  for (let i = 0; i < stops.length; i++) {
    var s = stops[i];
    var info = getActivityStyle(s.activityCode);
    var latlng = new kakao.maps.LatLng(s.aiRouteLat, s.aiRouteLong);
    bounds.extend(latlng);
    var overlay = new kakao.maps.CustomOverlay({
      position: latlng,
      content: '<div class="custom-overlay-marker" style="background:' + info.color + ';">' + s.aiRouteStopOrder + '</div>',
      yAnchor: 1.2
    });
    overlay.setMap(map);
    overlays.push(overlay);
  }

  for (let j = 0; j < stops.length - 1; j++) {
	  var seg = await drawRoute(stops[j], stops[j + 1], j);
    if (seg?.line) lines.push(seg.line);
    if (seg?.label) overlays.push(seg.label);
  }

  map.setBounds(bounds);
  renderTravel(stops);
  renderHealth(stops);
  mapElements[day] = { overlays, polylines: lines };
}

// ✅ 페이지 로드 시 실행
window.addEventListener("DOMContentLoaded", async function() {
  try {
    const url = window.location.origin + contextPath + "/api/ai/route/" + aiRouteId;
    const res = await fetch(url, { headers: { "Accept": "application/json" }});
    if (!res.ok) throw new Error("AI 경로 불러오기 실패: " + res.status);
    const data = await res.json();

    if (data.ai_route_title) {
      document.getElementById("route-title").textContent = data.ai_route_title;
      document.getElementById("route-desc").textContent = "AI가 추천한 '" + data.ai_route_title + "' 일정입니다. 🧳";
    }

    if (!data.stops || data.stops.length === 0) {
      document.getElementById("map").innerHTML = "<h4>경로 데이터가 없습니다.</h4>";
      return;
    }

    initMap(data.stops[0].aiRouteLat, data.stops[0].aiRouteLong);
    stopsByDay = data.stops.reduce((acc, s)=>{
      (acc[s.aiRouteDay] = acc[s.aiRouteDay] || []).push(s);
      return acc;
    }, {});

    const btnBox = document.getElementById("day-buttons");
    Object.keys(stopsByDay).sort((a,b)=>a-b).forEach(day=>{
      const btn = document.createElement("button");
      btn.className = "day-btn";
      btn.textContent = "Day " + day;
      btn.dataset.day = day;
      btnBox.appendChild(btn);
    });

    btnBox.addEventListener("click", async e=>{
      if (!e.target.matches(".day-btn")) return;
      document.querySelectorAll(".day-btn").forEach(b=>b.classList.remove("active"));
      e.target.classList.add("active");
      await displayDay(e.target.dataset.day);
    });

    const firstDay = Object.keys(stopsByDay).sort((a,b)=>a-b)[0];
    document.querySelector('[data-day="' + firstDay + '"]').classList.add("active");
    await displayDay(firstDay);
  } catch (err) {
    console.error("❌ 데이터 로드 오류:", err);
  }

  // ✅ 저장 버튼 클릭 이벤트 (CSRF 포함)
  const btn = document.getElementById("save-route-btn");
  if (btn) {
    btn.addEventListener("click", async () => {
      if (!confirm("이 AI 경로를 저장할까요?")) return;

      const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
      const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

      console.log("요청 URL:", contextPath + "/ai/saveUserRoute");
      console.log("CSRF 토큰:", token);

      try {
        const res = await fetch(contextPath + "/ai/saveUserRoute", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            [header]: token
          },
          body: new URLSearchParams({ aiRouteId })
        });

        const text = await res.text();
        console.log("📩 서버 응답 원본:", text);

        let data;
        try {
          data = JSON.parse(text);
        } catch (e) {
          console.error("❌ JSON 파싱 실패:", e);
          alert("서버 응답이 JSON이 아닙니다. 로그인/권한 문제일 수 있습니다.");
          return;
        }

        if (data.success) {
          alert("저장 완료!");
          location.href = contextPath + "/user/route/view?id=" + data.newUserRouteId;
        } else {
          alert("저장 실패: " + data.message);
        }

      } catch (err) {
        console.error("❌ 저장 중 오류:", err);
        alert("요청 중 오류가 발생했습니다.");
      }
    });
  } else {
    console.warn("⚠️ save-route-btn 버튼을 찾을 수 없습니다.");
  }
});
</script>
