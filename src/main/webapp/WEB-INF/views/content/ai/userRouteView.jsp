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
</div>

<script>
// ✅ JSP EL 완전 제거
const pathParts = window.location.pathname.split('/');
const contextPath = pathParts.length > 1 ? '/' + pathParts[1] : '';
const userRouteId = new URLSearchParams(window.location.search).get("id");

let map, stopsByDay = {}, mapElements = {}, currentDay = 1;

function initMap(lat, lng) {
  map = new kakao.maps.Map(document.getElementById("map"), {
    center: new kakao.maps.LatLng(lat, lng), level: 8
  });
}

function getActivityStyle(code) {
  const styles = {
    ARRIVE: { color: "#7E57C2", icon: "📍" },
    WALK: { color: "#FF9500", icon: "🚶‍♂️" },
    EAT: { color: "#E74C3C", icon: "🍴" },
    VISIT: { color: "#27AE60", icon: "🏛️" },
    RETURN: { color: "#3498DB", icon: "🏠" },
  };
  return styles[code] || { color: "#999", icon: "📌" };
}

async function drawRoute(start, end) {
  try {
    const url = contextPath + "/api/mobility/directions?originX=" +
      start.userRouteLong + "&originY=" + start.userRouteLat +
      "&destX=" + end.userRouteLong + "&destY=" + end.userRouteLat;
    const res = await fetch(url);
    if (!res.ok) return null;
    const json = await res.json();
    let path = [];
    const roads = json.routes?.[0]?.sections?.[0]?.roads;
    if (roads) roads.forEach(r => {
      for (let i = 0; i < r.vertexes.length; i += 2)
        path.push(new kakao.maps.LatLng(r.vertexes[i + 1], r.vertexes[i]));
    });
    const line = new kakao.maps.Polyline({ path, strokeWeight: 4, strokeColor: "#4a6cf7" });
    line.setMap(map);
    return line;
  } catch (e) {
    console.error("Mobility API 오류", e);
    return null;
  }
}

function clearMap() {
  Object.values(mapElements).forEach(v => {
    v.overlays?.forEach(o => o.setMap(null));
    v.lines?.forEach(p => p.setMap(null));
  });
  mapElements = {};
}

async function displayDay(day) {
  clearMap();
  const stops = stopsByDay[day];
  if (!stops) return;
  const bounds = new kakao.maps.LatLngBounds();
  const overlays = [], lines = [];

  stops.forEach((s, i) => {
    const info = getActivityStyle(s.activityCode);
    const latlng = new kakao.maps.LatLng(s.userRouteLat, s.userRouteLong);
    bounds.extend(latlng);
    const overlay = new kakao.maps.CustomOverlay({
      position: latlng,
      content: "<div style='background:" + info.color + ";color:#fff;padding:3px 6px;border-radius:6px;'>" + s.userRouteStopOrder + "</div>",
      yAnchor: 1.2
    });
    overlay.setMap(map);
    overlays.push(overlay);
  });

  for (let j = 0; j < stops.length - 1; j++) {
    const seg = await drawRoute(stops[j], stops[j + 1]);
    if (seg) lines.push(seg);
  }

  map.setBounds(bounds);
  mapElements[day] = { overlays, lines };
  renderEditableStops(stops);
}

function renderEditableStops(stops) {
  const list = document.getElementById("stop-list");
  list.innerHTML = "";
  stops.forEach(s => {
    const info = getActivityStyle(s.activityCode);
    const li = document.createElement("li");
    li.className = "travel-card";
    li.dataset.id = s.userRouteStopId;

    let dayOptions = "";
    for (let d = 1; d <= 7; d++) {
      dayOptions += "<option value='" + d + "'" + (s.userRouteDay == d ? " selected" : "") + ">Day " + d + "</option>";
    }

    li.innerHTML =
      "<div class='travel-header'>" +
        "<div><span style='background:" + info.color + ";padding:2px 6px;color:#fff;border-radius:4px;'>" + s.activityCode + "</span> " +
        info.icon + " <strong>" + (s.userRouteDescription || "(이름 없음)") + "</strong></div>" +
      "</div>" +
      "<div class='travel-meta'>" +
        "<span>🕒 " + (s.durationInMinutes || 0) + "분</span>" +
        "<span><label>일차:</label> <select class='day-selector' data-stopid='" + s.userRouteStopId + "'>" + dayOptions + "</select></span>" +
        "<span><label>이동수단:</label> <select class='mode-select' data-stopid='" + s.userRouteStopId + "'>" +
          "<option value='WALK' " + (s.transportationMode==='WALK'?'selected':'') + ">도보 🚶</option>" +
          "<option value='CAR' " + (s.transportationMode==='CAR'?'selected':'') + ">자동차 🚗</option>" +
          "<option value='BIKE' " + (s.transportationMode==='BIKE'?'selected':'') + ">자전거 🚴</option>" +
        "</select></span>" +
      "</div>";

    list.appendChild(li);
  });

  Sortable.create(list, {
    animation:150,
    onEnd: async evt => {
      const stopId = evt.item.dataset.id;
      const newOrder = evt.newIndex + 1;
      const res = await fetch(contextPath + "/api/user/route/stop/" + stopId + "?day=" + currentDay + "&order=" + newOrder, {method:"PATCH"});
      if (res.ok) await refreshDay(currentDay);
    }
  });
}

document.addEventListener("change", async e => {
  if (e.target.matches(".day-selector")) {
    const stopId = e.target.dataset.stopid;
    const newDay = e.target.value;
    const res = await fetch(contextPath + "/api/user/route/stop/" + stopId + "?day=" + newDay + "&order=1", {method:"PATCH"});
    if (res.ok) await refreshDay(newDay);
  }
  if (e.target.matches(".mode-select")) {
    const stopId = e.target.dataset.stopid;
    const mode = e.target.value;
    const res = await fetch(contextPath + "/api/user/route/stop/" + stopId + "/mode?mode=" + mode, {method:"PATCH"});
    if (res.ok) await refreshDay(currentDay);
  }
});

async function refreshDay(day){
  const res = await fetch(contextPath + "/api/user/route/" + userRouteId);
  const data = await res.json();
  stopsByDay = data.stops.reduce((acc,s)=>{
    (acc[s.userRouteDay]=acc[s.userRouteDay]||[]).push(s);
    return acc;
  },{});
  await displayDay(day);
}

window.addEventListener("DOMContentLoaded", async ()=>{
  const res = await fetch(contextPath + "/api/user/route/" + userRouteId);
  const data = await res.json();
  if (!data.stops || data.stops.length === 0) {
    document.getElementById("map").innerHTML = "<h4>저장된 경로가 없습니다.</h4>";
    return;
  }
  document.getElementById("route-title").textContent = data.userRouteTitle;
  initMap(data.stops[0].userRouteLat, data.stops[0].userRouteLong);

  stopsByDay = data.stops.reduce((acc,s)=>{
    (acc[s.userRouteDay]=acc[s.userRouteDay]||[]).push(s);
    return acc;
  },{});

  const btnBox = document.getElementById("day-buttons");
  Object.keys(stopsByDay).sort((a,b)=>a-b).forEach(day=>{
    const btn=document.createElement("button");
    btn.className="day-btn";
    btn.textContent="Day "+day;
    btn.dataset.day=day;
    btnBox.appendChild(btn);
  });
  btnBox.addEventListener("click",async e=>{
    if(!e.target.matches(".day-btn"))return;
    document.querySelectorAll(".day-btn").forEach(b=>b.classList.remove("active"));
    e.target.classList.add("active");
    currentDay=e.target.dataset.day;
    await displayDay(currentDay);
  });

  const firstDay=Object.keys(stopsByDay).sort((a,b)=>a-b)[0];
  document.querySelector('[data-day="'+firstDay+'"]').classList.add("active");
  currentDay=firstDay;
  await displayDay(firstDay);
});

document.getElementById("delete-route-btn").addEventListener("click", async ()=>{
  if(!confirm("이 루트를 삭제할까요?")) return;
  const res = await fetch(contextPath + "/api/user/route/" + userRouteId, {method:"DELETE"});
  if(res.ok){
    alert("삭제 완료");
    location.href=contextPath+"/mypage";
  } else alert("삭제 실패");
});
</script>
</body>
</html>
