<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>내 루트 보기</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/userRouteView.css">
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95f06e859388fb23abc3ac05fa370f48&libraries=services"></script>
<script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>

<style>
.container { max-width: 900px; margin: 0 auto; padding: 20px; }
#map { width: 100%; height: 500px; margin: 15px 0; border-radius: 8px; }
.day-btn { margin: 3px; padding: 6px 12px; border: none; border-radius: 6px; background: #4a6cf7; color: white; cursor: pointer; }
.day-btn.active { background: #2a48c5; }
.save-btn, .edit-btn {
  padding: 10px 16px; border: none; border-radius: 8px; color: white; cursor: pointer;
}
.save-btn { background: #4a6cf7; }
.edit-btn { background: #34C759; margin-right: 8px; }
.save-btn:disabled { background: #999; }
.sortable-list { list-style: none; padding: 0; margin: 0; }
.sortable-list li {
  display: flex; justify-content: space-between; align-items: center;
  background: #fff; border-radius: 8px; padding: 8px 12px; margin: 6px 0;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
.custom-overlay-marker {
  background: #4a6cf7; color: white; font-weight: bold;
  border-radius: 50%; width: 25px; height: 25px;
  display: flex; justify-content: center; align-items: center;
  border: 2px solid white;
}
</style>
</head>

<body>
<div class="container">
  <h1>내 여행 루트</h1>
  <p>루트를 보고, 필요하면 순서와 이동수단을 수정하세요.</p>

  <div id="day-buttons"></div>
  <div id="map"></div>

  <div id="stop-list"></div>

  <div style="margin-top: 20px;">
    <button id="edit-btn" class="edit-btn">수정하기</button>
    <button id="save-btn" class="save-btn" disabled>저장하기</button>
  </div>
</div>

<script>
const contextPath = "${pageContext.request.contextPath}";
const userRouteId = "${param.id}";
const KAKAO_KEY = "95f06e859388fb23abc3ac05fa370f48";

let map, stopsByDay = {}, mapElements = {};
let currentDay = 1;
let isEditing = false;

// 지도 초기화
function initMap(lat, lng) {
  map = new kakao.maps.Map(document.getElementById('map'), {
    center: new kakao.maps.LatLng(lat, lng),
    level: 8
  });
}

// 지도 초기화
function clearMap() {
  Object.values(mapElements).forEach(({ markers, polylines }) => {
    markers?.forEach(m => { m.marker.setMap(null); m.overlay.setMap(null); });
    polylines?.forEach(p => p.setMap(null));
  });
  mapElements = {};
}

// Mobility API
async function drawMobilityRoute(start, end, mode) {
  const modeParam = mode === 'WALK' ? 'WALK' : mode === 'BICYCLE' ? 'BICYCLE' : 'CAR';
  const url = `https://apis-navi.kakaomobility.com/v1/directions?origin=${start.userRouteLong},${start.userRouteLat}&destination=${end.userRouteLong},${end.userRouteLat}&priority=TIME`;

  try {
    const res = await fetch(url, {
      headers: { Authorization: "KakaoAK " + KAKAO_KEY }
    });
    const data = await res.json();
    const roads = data.routes?.[0]?.sections?.[0]?.roads;
    if (!roads) return null;

    const vertexes = roads.flatMap(r => r.vertexes);
    const path = [];
    for (let i = 0; i < vertexes.length; i += 2)
      path.push(new kakao.maps.LatLng(vertexes[i + 1], vertexes[i]));

    const color = mode === "CAR" ? "#007AFF" : mode === "BICYCLE" ? "#34C759" : "#FF9500";
    return new kakao.maps.Polyline({
      path, strokeWeight: 4, strokeColor: color, strokeOpacity: 0.9
    });
  } catch (err) {
    console.error("❌ Mobility API 오류:", err);
    return null;
  }
}

// Mobility 기반 폴리라인
async function drawMobilityPolylines(stops) {
  const polylines = [];
  for (let i = 0; i < stops.length - 1; i++) {
    const mode = stops[i].transportationMode || 'CAR';
    const poly = await drawMobilityRoute(stops[i], stops[i + 1], mode);
    if (poly) poly.setMap(map);
    polylines.push(poly);
  }
  return polylines;
}

// Day별 지도 표시
async function displayDay(day) {
  clearMap();
  const stops = stopsByDay[day];
  if (!stops) return;

  const bounds = new kakao.maps.LatLngBounds();
  const markers = [];

  stops.forEach((stop, i) => {
    const latlng = new kakao.maps.LatLng(stop.userRouteLat, stop.userRouteLong);
    bounds.extend(latlng);

    const marker = new kakao.maps.Marker({ position: latlng });
    marker.setMap(map);

    const overlay = new kakao.maps.CustomOverlay({
      position: latlng,
      content: `<div class="custom-overlay-marker">${i + 1}</div>`,
      yAnchor: 1.2
    });
    overlay.setMap(map);
    markers.push({ marker, overlay });
  });

  const polylines = await drawMobilityPolylines(stops);
  mapElements[day] = { markers, polylines };
  map.setBounds(bounds);
  renderStopList(day, stops);
}

// 리스트 렌더링
function renderStopList(day, stops) {
  const container = document.getElementById('stop-list');
  container.innerHTML = `<h3>Day ${day}</h3>`;

  const ul = document.createElement('ul');
  ul.id = `stop-list-day-${day}`;
  ul.className = 'sortable-list';

  stops.forEach((stop, idx) => {
    const li = document.createElement('li');
    li.dataset.id = stop.userRouteStopId;
    li.innerHTML = `
      <span class="stop-order">${idx + 1}</span>
      <span class="stop-name">${stop.userRouteDescription}</span>
      <select class="transport-mode" ${!isEditing ? 'disabled' : ''}>
        <option value="CAR" ${stop.transportationMode === 'CAR' ? 'selected' : ''}>🚗</option>
        <option value="WALK" ${stop.transportationMode === 'WALK' ? 'selected' : ''}>🚶‍♂️</option>
        <option value="BICYCLE" ${stop.transportationMode === 'BICYCLE' ? 'selected' : ''}>🚴‍♀️</option>
      </select>
    `;
    ul.appendChild(li);
  });
  container.appendChild(ul);

  if (isEditing) {
    new Sortable(ul, {
      animation: 150,
      onEnd: e => {
        const newOrder = [...ul.children].map((li, i) => ({
          userRouteStopId: li.dataset.id,
          newOrder: i + 1
        }));
        stopsByDay[day].forEach((stop, i) => stop.userRouteStopOrder = newOrder[i].newOrder);
        // 비동기 갱신 (폴리라인은 직선)
        redrawTemporaryPolyline(stopsByDay[day]);
      }
    });
  }
}

// 수정 모드용 직선 연결 (빠른 미리보기)
function redrawTemporaryPolyline(stops) {
  clearMap();
  const path = stops.map(s => new kakao.maps.LatLng(s.userRouteLat, s.userRouteLong));
  const polyline = new kakao.maps.Polyline({
    path, strokeWeight: 3, strokeColor: "#888", strokeStyle: 'dash', strokeOpacity: 0.7
  });
  polyline.setMap(map);
  map.setBounds(new kakao.maps.LatLngBounds(path[0], path[path.length - 1]));
}

// 수정 버튼
document.getElementById('edit-btn').addEventListener('click', () => {
  isEditing = true;
  document.getElementById('save-btn').disabled = false;
  displayDay(currentDay);
});

// 저장 버튼
document.getElementById('save-btn').addEventListener('click', async () => {
  const updates = [];
  Object.keys(stopsByDay).forEach(day => {
    stopsByDay[day].forEach(stop => {
      updates.push({
        id: stop.userRouteStopId,
        order: stop.userRouteStopOrder,
        mode: document.querySelector(`li[data-id="${stop.userRouteStopId}"] select`).value
      });
    });
  });

  try {
    const res = await fetch(`${contextPath}/ai/updateUserRouteOrder`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updates)
    });
    if (res.ok) {
      alert('변경 내용이 저장되었습니다!');
      isEditing = false;
      document.getElementById('save-btn').disabled = true;
      // 🔁 저장 후 동기적으로 Mobility 기반 경로 다시 표시
      await displayDay(currentDay);
    } else {
      alert('저장 중 오류 발생');
    }
  } catch (err) {
    console.error(err);
    alert('서버 통신 오류');
  }
});

// 초기 로드
window.addEventListener('DOMContentLoaded', async () => {
  try {
    const res = await fetch(`${contextPath}/ai/userRoute/${userRouteId}`);
    const data = await res.json();
    if (!data || !data.stops || data.stops.length === 0) {
      document.getElementById('map').innerHTML = "<h4>경로 데이터가 없습니다.</h4>";
      return;
    }

    initMap(data.stops[0].userRouteLat, data.stops[0].userRouteLong);
    stopsByDay = data.stops.reduce((acc, s) => {
      (acc[s.userRouteDay] = acc[s.userRouteDay] || []).push(s);
      return acc;
    }, {});

    const btns = document.getElementById('day-buttons');
    Object.keys(stopsByDay).sort((a,b)=>a-b).forEach(day=>{
      const btn = document.createElement('button');
      btn.className = 'day-btn';
      btn.textContent = `Day ${day}`;
      btn.onclick = async ()=>{
        document.querySelectorAll('.day-btn').forEach(b=>b.classList.remove('active'));
        btn.classList.add('active');
        currentDay = day;
        await displayDay(day);
      };
      btns.appendChild(btn);
    });

    const first = Object.keys(stopsByDay)[0];
    document.querySelector('.day-btn')?.classList.add('active');
    await displayDay(first);

  } catch (err) {
    console.error(err);
    document.getElementById('map').innerHTML = "<h4>데이터 로드 오류</h4>";
  }
});
</script>
</body>
</html>
