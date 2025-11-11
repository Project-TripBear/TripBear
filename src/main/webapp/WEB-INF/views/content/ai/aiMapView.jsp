<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>AI 추천 여행 경로</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/aiMapView.css">

<!-- ✅ Kakao Maps SDK -->
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95f06e859388fb23abc3ac05fa370f48&libraries=services"></script>

<style>
.container { max-width: 900px; margin: 0 auto; padding: 20px; }
#map { width: 100%; height: 500px; margin-top: 10px; border-radius: 8px; }
#day-buttons { text-align: center; margin-top: 20px; }
.day-btn { margin: 3px; padding: 6px 12px; border: none; border-radius: 6px; background: #4a6cf7; color: white; cursor: pointer; }
.day-btn.active { background: #2a48c5; }

.health-summary {
  display: flex; flex-direction: column; gap: 8px;
  margin-top: 20px; padding: 10px;
  background: #f6f8ff; border-radius: 8px;
}
.health-card {
  background: white; border-radius: 8px;
  padding: 8px 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
.health-info { color: #555; font-size: 14px; margin-top: 4px; }
.health-total { font-weight: bold; color: #333; margin-top: 10px; text-align: center; }
.custom-overlay-marker {
  background: #4a6cf7; color: white;
  font-size: 13px; font-weight: bold;
  border-radius: 50%; width: 25px; height: 25px;
  display: flex; justify-content: center; align-items: center;
  border: 2px solid white;
}
.save-btn {
  margin-top: 20px;
  padding: 10px 18px;
  background: #4a6cf7;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  cursor: pointer;
  transition: 0.2s;
}
.save-btn:hover { background: #2a48c5; }

</style>
</head>

<body>
<div class="container">
  <h1>AI 추천 여행 경로</h1>
  <p>일차별로 경로와 건강 활동 기록을 확인하세요.</p>
  <div id="day-buttons"></div>
  <div id="map"></div>
  <div id="healthcare-summary" class="health-summary"></div>
</div>
<button id="save-route-btn" class="save-btn">이 경로를 내 루트로 저장하기</button>


<script>
const KAKAO_KEY = "95f06e859388fb23abc3ac05fa370f48";
const contextPath = "${pageContext.request.contextPath}";
const aiRouteId = "${param.id}";
let map;
let stopsByDay = {};
let mapElements = {};

// 🗺 지도 초기화
function initMap(centerLat, centerLng) {
  const container = document.getElementById('map');
  map = new kakao.maps.Map(container, {
    center: new kakao.maps.LatLng(centerLat, centerLng),
    level: 8
  });
}

// 🚶 헬스케어 카드 렌더링
function renderHealthCareSummary(stops) {
  const container = document.getElementById('healthcare-summary');
  container.innerHTML = '';

  let totalDistance = 0, totalSteps = 0, totalCalories = 0;

  stops.forEach(stop => {
    totalDistance += stop.walkingDistanceKm || 0;
    totalSteps += stop.walkingStepsCount || 0;
    totalCalories += stop.healthcareCaloriesBurned || 0;

    const card = document.createElement('div');
    card.className = 'health-card';
    card.innerHTML = `
      <div class="health-stop-name"><strong>${stop.aiRouteDescription || '(이름 없음)'}</strong></div>
      <div class="health-info">
        🚶‍♂️ ${stop.walkingDistanceKm?.toFixed(2) ?? 0} km |
        🦶 ${stop.walkingStepsCount ?? 0} steps |
        🔥 ${stop.healthcareCaloriesBurned ?? 0} kcal
      </div>
    `;
    container.appendChild(card);
  });

  const summary = document.createElement('div');
  summary.className = 'health-total';
  summary.innerHTML = `
    <strong>총합</strong>  
    <span>${totalDistance.toFixed(2)} km</span> · 
    <span>${totalSteps} steps</span> · 
    <span>${totalCalories} kcal</span>
  `;
  container.appendChild(summary);
}

// Mobility API 호출 + 도로 경로 표시
async function drawMobilityRoute(start, end, mode) {
  try {
    const res = await fetch(
      `https://apis-navi.kakaomobility.com/v1/directions?origin=${start.aiRouteLong},${start.aiRouteLat}&destination=${end.aiRouteLong},${end.aiRouteLat}&priority=TIME`,
      { headers: { "Authorization": "KakaoAK " + KAKAO_KEY } }
    );

    const json = await res.json();
    if (!json.routes?.[0]?.sections?.[0]?.roads) return null;

    const vertexes = json.routes[0].sections[0].roads.flatMap(r => r.vertexes);
    const path = [];
    for (let j = 0; j < vertexes.length; j += 2) {
      path.push(new kakao.maps.LatLng(vertexes[j + 1], vertexes[j]));
    }

    const color = mode === "CAR" ? "#007AFF" : mode === "BICYCLE" ? "#34C759" : "#FF9500";
    return new kakao.maps.Polyline({
      path, strokeWeight: 4, strokeColor: color, strokeOpacity: 0.9
    });

  } catch (err) {
    console.error("❌ Mobility API 오류:", err);
    return null;
  }
}

// Day별 지도 표시
async function displayDay(day, mode = 'CAR') {
  clearMap();
  const stops = stopsByDay[day];
  if (!stops || stops.length === 0) return;

  const bounds = new kakao.maps.LatLngBounds();
  const markers = [];

  for (const stop of stops) {
    const latlng = new kakao.maps.LatLng(stop.aiRouteLat, stop.aiRouteLong);
    bounds.extend(latlng);

    const marker = new kakao.maps.Marker({ position: latlng });
    marker.setMap(map);

    const overlay = new kakao.maps.CustomOverlay({
      position: latlng,
      content: `<div class="custom-overlay-marker">${stop.aiRouteStopOrder}</div>`,
      yAnchor: 1.2
    });
    overlay.setMap(map);
    markers.push({ marker, overlay });
  }

  // 구간별 Mobility API 호출
  const polylines = [];
  for (let i = 0; i < stops.length - 1; i++) {
    const polyline = await drawMobilityRoute(stops[i], stops[i + 1], mode);
    if (polyline) { polyline.setMap(map); polylines.push(polyline); }
  }

  mapElements[day] = { markers, polylines };
  map.setBounds(bounds);
  renderHealthCareSummary(stops);
}

// 지도 초기화 함수
function clearMap() {
  Object.values(mapElements).forEach(({ markers, polylines }) => {
    markers?.forEach(m => {
      m.marker.setMap(null);
      m.overlay.setMap(null);
    });
    polylines?.forEach(p => p.setMap(null));
  });
  mapElements = {};
}

// 초기 데이터 로드
window.addEventListener('DOMContentLoaded', async () => {
  try {
    const res = await fetch(`${contextPath}/ai/route/${aiRouteId}`);
    const data = await res.json();
    if (!data || !data.stops || data.stops.length === 0) {
      document.getElementById('map').innerHTML = "<h4>경로 데이터가 없습니다.</h4>";
      return;
    }

    // 지도 생성
    initMap(data.stops[0].aiRouteLat, data.stops[0].aiRouteLong);

    // 일차별 그룹화
    stopsByDay = data.stops.reduce((acc, stop) => {
      (acc[stop.aiRouteDay] = acc[stop.aiRouteDay] || []).push(stop);
      return acc;
    }, {});

    // 버튼 생성
    const btnContainer = document.getElementById('day-buttons');
    Object.keys(stopsByDay).sort((a, b) => a - b).forEach(day => {
      const btn = document.createElement('button');
      btn.className = 'day-btn';
      btn.textContent = `Day ${day}`;
      btn.dataset.day = day;
      btnContainer.appendChild(btn);
    });

    // 버튼 클릭 이벤트
    btnContainer.addEventListener('click', async e => {
      if (e.target.matches('.day-btn')) {
        document.querySelectorAll('.day-btn').forEach(b => b.classList.remove('active'));
        e.target.classList.add('active');
        await displayDay(e.target.dataset.day, 'CAR');
      }
    });

    // 첫 번째 Day 자동 표시
    const firstDay = Object.keys(stopsByDay).sort((a, b) => a - b)[0];
    document.querySelector(`[data-day="${firstDay}"]`)?.classList.add('active');
    await displayDay(firstDay, 'CAR');
    
 	// "이 경로를 내 루트로 저장하기" 버튼
    const saveBtn = document.getElementById('save-route-btn');
    saveBtn.addEventListener('click', async () => {
      if (!confirm('이 경로를 내 루트로 저장하시겠습니까?')) return;

      saveBtn.disabled = true;
      saveBtn.textContent = '저장 중...';

      try {
    	  const res = await fetch(`${contextPath}/ai/saveUserRoute`, {
    		  method: 'POST',
    		  headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    		  body: new URLSearchParams({ aiRouteId })
    		});


        const result = await res.json();

        if (res.ok && result.success) {
          alert('루트가 성공적으로 저장되었습니다!');
          window.location.href = `${contextPath}/route/userRouteView?id=${result.newUserRouteId}`;
        } else {
          throw new Error(result.message || '저장 중 오류가 발생했습니다.');
        }

      } catch (err) {
        console.error('❌ 저장 오류:', err);
        alert('경로 저장 중 문제가 발생했습니다.');
      } finally {
        saveBtn.disabled = false;
        saveBtn.textContent = '이 경로를 내 루트로 저장하기';
      }
    });


  } catch (err) {
    console.error("❌ 데이터 로드 오류:", err);
    document.getElementById('map').innerHTML = "<h4>지도를 불러오는 중 오류가 발생했습니다.</h4>";
  }
});
</script>
</body>
</html>
