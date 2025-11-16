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

  // 코드 그룹핑
  const map = {

    // ====== 이동 관련 ======
    "ARRIVE":      { color: "#7E57C2", icon: "📍" },
    "RETURN":      { color: "#3498DB", icon: "🏠" },

    // 걷기 유형별
    "WALK":        { color: "#FF9500", icon: "🚶‍♂️" },
    "WALK_SLOW":   { color: "#FFB74D", icon: "🚶‍♂️" },
    "WALK_NORMAL": { color: "#FB8C00", icon: "🚶‍♂️" },

    // 등산/하이킹
    "HIKE_LIGHT":  { color: "#8BC34A", icon: "🥾" },

    // ====== 먹는 활동 ======
    "EAT":         { color: "#E74C3C", icon: "🍴" },
    "EATING":      { color: "#E57373", icon: "🍽️" },

    // ====== 관광, 둘러보기 ======
    "VISIT":       { color: "#27AE60", icon: "🏛️" },
    "VIEWING":     { color: "#66BB6A", icon: "🌄" },

    // ====== 쇼핑 ======
    "SHOPPING":    { color: "#9C27B0", icon: "🛍️" }
  };

  // 매칭 없을 경우 기본값
  return map[code] || { color: "#999", icon: "📌" };
}


// ✅ 지도 클리어
function clearMap() {
  if (mapElements.overlays) {
    mapElements.overlays.forEach(o => o.setMap(null));
  }
  if (mapElements.polylines) {
    mapElements.polylines.forEach(p => p.setMap(null));
  }

  mapElements.overlays = [];
  mapElements.polylines = [];
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
    const section = json.routes && json.routes[0] && json.routes[0].sections && json.routes[0].sections[0];
    if (!section) return null;

    // 거리/시간 계산
    var distanceM = section.distance || 0;
    var durationS = section.duration || 0;

    var distanceKm = (distanceM / 1000).toFixed(2);
    var durationMin = Math.round(durationS / 60);

    var infoText =
      "거리: " + distanceKm + " km<br>" +
      "예상 시간: " + durationMin + "분";

    // Polyline 생성
    let path = [];
    const roads = section.roads;
    if (!roads) return null;

    roads.forEach(function(r) {
      for (var i = 0; i < r.vertexes.length; i += 2) {
        path.push(new kakao.maps.LatLng(r.vertexes[i + 1], r.vertexes[i]));
      }
    });

    var polyline = new kakao.maps.Polyline({
      path: path,
      strokeWeight: 4,
      strokeColor: routeColors[index % routeColors.length],
      strokeOpacity: 0.9
    });

    polyline.setMap(map);

    //🔥 "마우스 따라다니는" 말풍선 전용 오버레이
    var tooltip = new kakao.maps.CustomOverlay({
      map: null,
      position: null,
      content:
        "<div style='background:white;border:1px solid #aaa;padding:6px 10px;" +
        "font-size:12px;border-radius:6px;white-space:nowrap;" +
        "box-shadow:0 2px 4px rgba(0,0,0,0.2);'>" +
        infoText +
        "</div>",
      yAnchor: 2.0    // ⬅️ 마우스 위쪽에 붙게 만드는 핵심 옵션
    });

    // 🔥 마우스 올라가면 툴팁 활성화
    kakao.maps.event.addListener(polyline, "mouseover", function(mouseEvent) {
      tooltip.setMap(map);
      tooltip.setPosition(mouseEvent.latLng);
    });

    // 🔥 마우스 움직이는 동안 계속 툴팁 위치 업데이트
    kakao.maps.event.addListener(polyline, "mousemove", function(mouseEvent) {
      tooltip.setPosition(mouseEvent.latLng);
    });

    // 🔥 마우스 벗어나면 숨김
    kakao.maps.event.addListener(polyline, "mouseout", function() {
      tooltip.setMap(null);
    });

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

  // 활동별 간단 설명
  var shortDescMap = {
    "VIEWING": "주변 명소를 둘러보는 일정입니다.",
    "VISIT": "관광 명소 방문 일정입니다.",
    "EATING": "식사와 휴식을 위한 추천 장소입니다.",
    "EAT": "식사 장소입니다.",
    "WALK_SLOW": "여유롭게 걷는 도보 구간입니다.",
    "WALK_NORMAL": "보통 속도로 이동하는 도보 구간입니다.",
    "WALK": "도보 이동이 포함된 일정입니다.",
    "SHOPPING": "기념품 또는 쇼핑을 즐기는 일정입니다.",
    "HIKE_LIGHT": "가벼운 하이킹 코스입니다.",
    "ARRIVE": "일정의 시작 지점입니다.",
    "RETURN": "숙소 또는 출발지로 돌아가는 경로입니다."
  };

  stops.forEach(function(s) {

    var info = getActivityStyle(s.activityCode);
    var desc = shortDescMap[s.activityCode] || "여행 일정의 일부입니다.";

    var div = document.createElement("div");
    div.className = "travel-card";

    var html = "";
    html += "<span class='activity-tag' style='background:" + info.color + ";'>" + s.activityCode + "</span>";
    html += "<strong>" + info.icon + " " + (s.aiRouteDescription || "(이름 없음)") + "</strong>";
    html += "<div class='health-info'>" + desc + "</div>";

    div.innerHTML = html;
    box.appendChild(div);
  });
}



//🔥 사용자 체중(kg)
const USER_WEIGHT = 69;

function renderHealth(stops) {
  var box = document.getElementById("healthcare-summary");
  box.innerHTML = "";

  var totalDist = 0, totalSteps = 0, totalKcal = 0;
  var goalDist = 5, goalSteps = 8000, goalKcal = 500;

  var hasHealthData = false;

  stops.forEach(s => {

    var dist = s.walkingDistanceKm || 0;
    var steps = s.walkingStepsCount || 0;
    var kcal = s.healthcareCaloriesBurned || 0;

    // 🔥 DB 칼로리가 없으면 자동 계산
    if (kcal === 0 && (dist > 0 || steps > 0)) {

      // 거리 기반 kcal
      let kcal_distance = dist * USER_WEIGHT * 1.036;

      // 스텝 기반 kcal (평균 0.045 kcal per step)
      let kcal_steps = steps * 0.045;

      // 평균값 (너무 치우치지 않게)
      kcal = ((kcal_distance + kcal_steps) / 2);

      // 소수점 2자리
      kcal = Number(kcal.toFixed(2));
    }

    // 세 값 모두 0이면 헬스카드 숨김
    if (dist === 0 && steps === 0 && kcal === 0) return;

    hasHealthData = true;

    totalDist += dist;
    totalSteps += steps;
    totalKcal += kcal;

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

  if (!hasHealthData) {
    box.innerHTML = "<div class='health-info'>헬스케어 정보가 없는 일정입니다.</div>";
    return;
  }

  var total = document.createElement("div");
  total.className = "health-total";
  total.innerHTML =
    "<strong>총합</strong> " +
    totalDist.toFixed(2) + " km · " +
    totalSteps + " steps · " +
    totalKcal.toFixed(2) + " kcal";

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
	  if (seg) lines.push(seg);
  }

  map.setBounds(bounds);
  renderTravel(stops);
  renderHealth(stops);
  mapElements.overlays = overlays;
  mapElements.polylines = lines;

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
</body>
</html>