<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>내 여행 경로</title>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95f06e859388fb23abc3ac05fa370f48&libraries=services"></script>
<script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>

<style>
body {
  font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
  background: #f5f7fb;
  color: #333;
}
.container {
  max-width: 960px;
  margin: 40px auto;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  padding: 30px 40px;
}
h1 {
  margin-bottom: 6px;
  font-size: 28px;
}
#route-desc { color: #666; margin-bottom: 24px; }
#map { width: 100%; height: 480px; border-radius: 10px; margin-bottom: 20px; }

#day-buttons {
  display: flex; gap: 8px; margin-bottom: 10px; flex-wrap: wrap;
}
.day-btn {
  flex: none; background: #edf0fa; color: #333;
  border: none; border-radius: 6px;
  padding: 6px 14px; font-weight: 600; cursor: pointer;
  transition: 0.2s;
}
.day-btn.active { background: #4a6cf7; color: #fff; }

#stop-list { list-style: none; padding: 0; margin: 0; }
.travel-card {
  background: #f9fafc; border: 1px solid #e0e4ef;
  border-radius: 10px; padding: 14px 16px; margin-bottom: 10px;
  transition: 0.2s;
}
.travel-card:hover { background: #eef2ff; }

.travel-header {
  display: flex; justify-content: space-between; align-items: center;
}
.travel-meta {
  margin-top: 6px; font-size: 14px; color: #666;
  display: flex; gap: 12px; align-items: center;
}
.travel-meta select {
  padding: 4px 8px; border-radius: 6px;
  border: 1px solid #ccc; background: #fff;
}
.travel-meta .mode-select {
  padding: 4px 8px; border-radius: 6px;
  border: 1px solid #ccc; background: #fff;
  font-size: 14px;
}

#delete-route-btn {
  display: block;
  background: #e74c3c; color: #fff;
  border: none; border-radius: 8px;
  padding: 10px 18px; font-weight: 600;
  cursor: pointer; margin: 20px auto 0;
}
#delete-route-btn:hover { background: #d63c2d; }
</style>
</head>

<body>
<div class="container">
  <h1 id="route-title">내 여행 경로</h1>
  <p id="route-desc">저장된 여행 일정을 확인하고 수정할 수 있습니다 🌿</p>

  <div id="day-buttons"></div>
  <div id="map"></div>

  <h3 style="margin-top:24px;">일정 수정</h3>
  <ul id="stop-list"></ul>

  <button id="delete-route-btn">이 루트를 삭제하기</button>

  <button id="reserveBtn" class="save-btn">🛏️ 숙소 예약하러 가기</button>
  
</div>

<script>
// 기본 설정
var userRouteId = new URLSearchParams(window.location.search).get("id");
var contextPath = "/trip";

var map;
var stopsByDay = {};
var currentDay = 1;
var routeData = null;   // ✅ 예약 버튼에서 사용할 데이터

// 지도 객체 저장 배열(마커/라인)
var mapElements = { overlays: [], polylines: [] };

// renderToken (drawRoute 중복 호출 방지)
var renderToken = 0;

var routeColors = [
  "#4A6CF7", "#FF5722", "#9C27B0", "#009688",
  "#FBC02D", "#E91E63", "#795548"
];


// 지도 초기화
function initMap(lat, lng) {
  map = new kakao.maps.Map(document.getElementById("map"), {
    center: new kakao.maps.LatLng(lat, lng),
    level: 8
  });
}


// 액티비티 스타일
function getActivityStyle(code) {
  var styles = {
    ARRIVE: { color: "#7E57C2", icon: "📍" },
    WALK:   { color: "#FF9500", icon: "🚶‍♂️" },
    EAT:    { color: "#E74C3C", icon: "🍴" },
    VISIT:  { color: "#27AE60", icon: "🏛️" },
    RETURN: { color: "#3498DB", icon: "🏠" }
  };
  return styles[code] || { color: "#999", icon: "📌" };
}


// 지도 클리어
function clearMap() {
  mapElements.overlays.forEach(function(o){ o.setMap(null); });
  mapElements.polylines.forEach(function(l){ l.setMap(null); });
  mapElements = { overlays: [], polylines: [] };
}


// 경로 그리기
async function drawRoute(start, end, index, token) {
  try {
    var url =
      contextPath +
      "/api/mobility/directions?originX=" +
      start.userRouteLong +
      "&originY=" + start.userRouteLat +
      "&destX=" + end.userRouteLong +
      "&destY=" + end.userRouteLat;

    var res = await fetch(url);
    if (!res.ok) return null;

    // 이전 렌더링이면 무시
    if (token !== renderToken) return null;

    var json = await res.json();
    var roads = json.routes && json.routes[0] &&
                json.routes[0].sections &&
                json.routes[0].sections[0] &&
                json.routes[0].sections[0].roads;

    if (!roads) return null;

    var path = [];

    roads.forEach(function(r){
      for (var i = 0; i < r.vertexes.length; i += 2) {
        var lat = r.vertexes[i + 1];
        var lng = r.vertexes[i];
        path.push(new kakao.maps.LatLng(lat, lng));
      }
    });

    var line = new kakao.maps.Polyline({
      path: path,
      strokeWeight: 4,
      strokeColor: routeColors[index % routeColors.length],
      strokeOpacity: 0.9
    });

    line.setMap(map);
    return line;

  } catch (e) {
    console.error("Mobility 오류:", e);
    return null;
  }
}


// Day 렌더링
async function displayDay(day) {
  var token = ++renderToken;

  clearMap();

  var stops = stopsByDay[day];
  if (!stops) return;

  var bounds = new kakao.maps.LatLngBounds();

  // 마커/번호 표시
  for (var i = 0; i < stops.length; i++) {
    var s = stops[i];
    var info = getActivityStyle(s.activityCode);
    var latlng = new kakao.maps.LatLng(s.userRouteLat, s.userRouteLong);
    bounds.extend(latlng);

    var overlay = new kakao.maps.CustomOverlay({
      position: latlng,
      content:
        "<div style='background:" +
        info.color +
        ";color:#fff;padding:3px 6px;border-radius:6px;'>" +
        s.userRouteStopOrder +
        "</div>",
      yAnchor: 1.2
    });

    overlay.setMap(map);
    mapElements.overlays.push(overlay);
  }

  // Polyline 표시
  for (var j = 0; j < stops.length - 1; j++) {
    var seg = await drawRoute(stops[j], stops[j + 1], j, token);
    if (seg && token === renderToken) {
      mapElements.polylines.push(seg);
    }
  }

  map.setBounds(bounds);
  renderEditableStops(stops);
}


// Day 버튼 렌더링
function renderDayButtons() {
  var box = document.getElementById("day-buttons");
  box.innerHTML = "";

  Object.keys(stopsByDay)
    .sort(function(a,b){ return a - b; })
    .forEach(function(day){
      var btn = document.createElement("button");
      btn.className = "day-btn";
      btn.textContent = "Day " + day;
      btn.dataset.day = day;
      box.appendChild(btn);
    });

  box.onclick = function(e){
    if (!e.target.matches(".day-btn")) return;

    document.querySelectorAll(".day-btn").forEach(function(b){
      b.classList.remove("active");
    });

    e.target.classList.add("active");
    currentDay = e.target.dataset.day;
    displayDay(currentDay);
  };
}


// 일정 카드 렌더링
function renderEditableStops(stops) {
  var list = document.getElementById("stop-list");
  list.innerHTML = "";

  stops.forEach(function(s){
    var info = getActivityStyle(s.activityCode);
    var li = document.createElement("li");
    li.className = "travel-card";
    li.dataset.id = s.userRouteStopId;

    // Day 옵션 생성
    var dayOptions = "";
    for (var d = 1; d <= 7; d++) {
      dayOptions +=
        "<option value='" + d + "'" +
        (s.userRouteDay == d ? " selected" : "") +
        ">Day " + d + "</option>";
    }

    li.innerHTML =
      "<div class='travel-header'>" +
        "<div>" +
          "<span style='background:" + info.color + ";padding:2px 6px;color:#fff;border-radius:4px;'>" +
            s.activityCode +
          "</span> " +
          info.icon +
          " <strong>" + (s.userRouteDescription || "(이름 없음)") + "</strong>" +
        "</div>" +
      "</div>" +
      "<div class='travel-meta'>" +
        "<span>🕒 " + (s.durationInMinutes || 0) + "분</span>" +
        "<span><label>일차:</label> " +
          "<select class='day-selector' data-stopid='" + s.userRouteStopId + "'>" +
            dayOptions +
          "</select>" +
        "</span>" +
        "<span><label>이동수단:</label> " +
          "<select class='mode-select' data-stopid='" + s.userRouteStopId + "'>" +
            "<option value='WALK' " + (s.transportationMode==="WALK"?"selected":"") + ">도보 🚶</option>" +
            "<option value='CAR' "  + (s.transportationMode==="CAR" ?"selected":"") + ">자동차 🚗</option>" +
            "<option value='BIKE' " + (s.transportationMode==="BIKE"?"selected":"") + ">자전거 🚴</option>" +
          "</select>" +
        "</span>" +
      "</div>";

    list.appendChild(li);
  });

  // Sortable 적용
  Sortable.create(list, {
    animation: 150,
    onEnd: async function(evt){
      var items = document.querySelectorAll("#stop-list .travel-card");

      var orderedStops = [];
      items.forEach(function(li, idx){
        orderedStops.push({
          stopId: li.dataset.id,
          order: idx + 1
        });
      });

      await fetch(
        contextPath + "/api/user/route/stop/bulk/reorder",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            day: currentDay,
            stops: orderedStops
          })
        }
      );

      refreshDay(currentDay);
    }
  });
}


// Day 변경 / 이동수단 변경 이벤트
document.addEventListener("change", async function(e){

  // Day 변경
  if (e.target.matches(".day-selector")) {
    var stopId = e.target.dataset.stopid;
    var newDay = e.target.value;

    await fetch(
      contextPath +
      "/api/user/route/stop/" +
      stopId +
      "/day?day=" + newDay,
      { method: "PATCH" }
    );

    refreshDay(newDay);
  }

  // 이동수단 변경
  if (e.target.matches(".mode-select")) {
    var stopId2 = e.target.dataset.stopid;
    var mode = e.target.value;

    await fetch(
      contextPath +
      "/api/user/route/stop/" +
      stopId2 +
      "/mode?mode=" + mode,
      { method: "PATCH" }
    );

    refreshDay(currentDay);
  }

});


// Day 새로고침
async function refreshDay(day){
  var res = await fetch(contextPath + "/api/user/route/" + userRouteId);
  var data = await res.json();

  routeData = data;  // ✅ 예약 버튼에서도 쓰도록 유지

  stopsByDay = data.stops.reduce(function(acc, s){
    if (!acc[s.userRouteDay]) acc[s.userRouteDay] = [];
    acc[s.userRouteDay].push(s);
    return acc;
  }, {});

  Object.keys(stopsByDay).forEach(function(d){
    stopsByDay[d].sort(function(a,b){
      return a.userRouteStopOrder - b.userRouteStopOrder;
    });
  });

  renderDayButtons();

  if (!stopsByDay[day]) {
    day = Object.keys(stopsByDay)[0];
  }

  currentDay = day;
  var activeBtn = document.querySelector("[data-day='" + day + "']");
  if (activeBtn) activeBtn.classList.add("active");

  displayDay(day);
}


// 초기 로딩
window.addEventListener("DOMContentLoaded", async function(){
  var res = await fetch(contextPath + "/api/user/route/" + userRouteId);
  var data = await res.json();

  routeData = data;   // ✅ 최초에도 세팅

  if (!data.stops || data.stops.length === 0) {
    document.getElementById("map").innerHTML = "<h4>저장된 경로가 없습니다.</h4>";
    return;
  }

  initMap(data.stops[0].userRouteLat, data.stops[0].userRouteLong);

  stopsByDay = data.stops.reduce(function(acc, s){
    if (!acc[s.userRouteDay]) acc[s.userRouteDay] = [];
    acc[s.userRouteDay].push(s);
    return acc;
  }, {});

  renderDayButtons();

  var firstDay = Object.keys(stopsByDay).sort(function(a,b){ return a - b; })[0];
  var btn = document.querySelector("[data-day='" + firstDay + "']");
  if (btn) btn.classList.add("active");

  displayDay(firstDay);
});


// 삭제 기능
document.getElementById("delete-route-btn").addEventListener("click", async function(){
  if (!confirm("이 루트를 삭제할까요?")) return;

  var res = await fetch(contextPath + "/api/user/route/" + userRouteId, {
    method: "DELETE"
  });

  if (res.ok) {
    alert("삭제 완료");
    window.location.href = contextPath + "/mypage";
  } else {
    alert("삭제 실패");
  }
});


// 숙소 예약
document.getElementById("reserveBtn").addEventListener("click", function(){
  if (!routeData) {
    alert("경로 정보를 불러오지 못했습니다.");
    return;
  }

  var region = routeData.userRouteRegion;
  var checkin = formatDate(routeData.userRouteStartdate);
  var checkout = formatDate(routeData.userRouteEnddate);

  var url =
    contextPath + "/reservation/select-accom" +
    "?userRouteId=" + routeData.userRouteId +
    "&region=" + encodeURIComponent(region) +
    "&checkin=" + checkin +
    "&checkout=" + checkout;

  window.location.href = url;
});


function formatDate(d) {
  var date = new Date(d);
  var month = String(date.getMonth() + 1).padStart(2, "0");
  var day = String(date.getDate() + 0).padStart(2, "0");
  return date.getFullYear() + "-" + month + "-" + day;
}
</script>



</body>
</html>
