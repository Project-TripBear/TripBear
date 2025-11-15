<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">

<div class="route-container user-route-page">
    <header class="route-header">
        <h1 id="route-title" class="route-title">내 여행 경로</h1>
        <p id="route-desc" class="route-subtitle">
            저장된 여행 일정을 확인하고, 순서를 바꾸고, 필요하면 삭제할 수 있어요 🌿
        </p>
    </header>

    <!-- 날짜 선택 버튼 -->
    <div id="day-buttons" class="day-buttons"></div>

    <!-- 지도 영역 -->
    <div id="map" class="route-map"></div>

    <!-- 일정 수정 영역 -->
    <section class="route-section">
        <h2 class="route-section-title route-edit-title">일정 수정</h2>
        <ul id="stop-list" class="stop-list">
            <%-- JS로 stop-item(li) 동적 렌더링 --%>
        </ul>
    </section>

    <!-- 삭제 / 숙소예약 버튼 -->
    <div class="route-footer route-footer-double">
        <button id="delete-route-btn" class="btn delete-btn">
            이 루트를 삭제하기
        </button>

        <button id="reserveBtn" class="btn save-btn">
            🛏️ 숙소 예약하러 가기
        </button>
    </div>
</div>

<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95f06e859388fb23abc3ac05fa370f48&libraries=services"></script>
<script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>
<script>
// ✅ JSP EL 완전 제거
const pathParts = window.location.pathname.split('/');
const contextPath = pathParts.length > 1 ? '/' + pathParts[1] : '';
const userRouteId = new URLSearchParams(window.location.search).get("id");
let routeData = null;


let map, stopsByDay = {}, mapElements = {}, currentDay = 1;
const routeColors = [
	  "#4A6CF7",
	  "#FF5722",
	  "#9C27B0",
	  "#009688",
	  "#FBC02D",
	  "#E91E63",
	  "#795548"
	];



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

async function drawRoute(start, end, index) {
	  try {
	    const url = contextPath + "/api/mobility/directions?originX=" +
	      start.userRouteLong + "&originY=" + start.userRouteLat +
	      "&destX=" + end.userRouteLong + "&destY=" + end.userRouteLat;

	    const res = await fetch(url);
	    if (!res.ok) return null;

	    const json = await res.json();
	    console.log("Mobility API 응답:", json);

	    let path = [];
	    const roads = json.routes?.[0]?.sections?.[0]?.roads;
	    if (!roads) return null;

	    roads.forEach(r => {
	      for (let i = 0; i < r.vertexes.length; i += 2) {
	        path.push(new kakao.maps.LatLng(r.vertexes[i + 1], r.vertexes[i]));
	      }
	    });

	    const line = new kakao.maps.Polyline({
	      path,
	      strokeWeight: 4,
	      strokeColor: routeColors[index % routeColors.length],
	      strokeOpacity: 0.9
	    });

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

function renderDayButtons() {
	  const btnBox = document.getElementById("day-buttons");
	  btnBox.innerHTML = "";

	  Object.keys(stopsByDay).sort((a,b)=>a-b).forEach(day => {
	    const btn = document.createElement("button");
	    btn.className = "day-btn";
	    btn.textContent = "Day " + day;
	    btn.dataset.day = day;
	    btnBox.appendChild(btn);
	  });

	  btnBox.onclick = async (e) => {
	    if (!e.target.matches(".day-btn")) return;
	    document.querySelectorAll(".day-btn").forEach(b => b.classList.remove("active"));
	    e.target.classList.add("active");
	    currentDay = e.target.dataset.day;
	    await displayDay(currentDay);
	  };
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
	  const seg = await drawRoute(stops[j], stops[j + 1], j);
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
	  animation: 150,
	  onEnd: async evt => {
	    const items = [...document.querySelectorAll("#stop-list .travel-card")];

	    // order 재정렬 목록 생성
	    const orderedStops = items.map((li, index) => ({
	      stopId: li.dataset.id,
	      order: index + 1
	    }));

	    // 전체 order 업데이트 API 호출
	    const res = await fetch("trip/api/user/route/stop/reorder", {
	      method: "POST",
	      headers: { "Content-Type": "application/json" },
	      body: JSON.stringify({
	        day: currentDay,
	        stops: orderedStops
	      })
	    });

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
	    (acc[s.userRouteDay] = acc[s.userRouteDay] || []).push(s);
	    return acc;
	  },{});

	  // ★ 정렬 추가
	  Object.keys(stopsByDay).forEach(d => {
	    stopsByDay[d].sort((a,b) => a.userRouteStopOrder - b.userRouteStopOrder);
	  });

	  // ★ Day 버튼 재생성
	  renderDayButtons();

	  // 현재 day가 없어진 경우 첫 day로 이동
	  if (!stopsByDay[day]) {
	    day = Object.keys(stopsByDay)[0];
	  }

	  currentDay = day;

	  // 새로 생성된 day 버튼에 active 추가
	  document.querySelector(`[data-day="${day}"]`)?.classList.add("active");

	  await displayDay(day);
	}


window.addEventListener("DOMContentLoaded", async ()=>{
  const res = await fetch(contextPath + "/api/user/route/" + userRouteId);
  const data = await res.json();
  
  routeData = data; 
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

document.getElementById("reserveBtn").addEventListener("click", () => {

	  if (!routeData) {
	    alert("경로 정보를 불러오지 못했습니다.");
	    return;
	  }

	  var region = routeData.userRouteRegion;
	  var checkin = formatDate(routeData.userRouteStartdate);
	  var checkout = formatDate(routeData.userRouteEnddate);

	  // ❗ 백틱 제거한 형태
	  var url = "/trip/reservation/select-accom"
	          + "?userRouteId=" + routeData.userRouteId
	          + "&region=" + encodeURIComponent(region)
	          + "&checkin=" + checkin
	          + "&checkout=" + checkout;

	  window.location.href = url;
	});

	function formatDate(d) {
	  var date = new Date(d);
	  var month = String(date.getMonth() + 1).padStart(2, "0");
	  var day = String(date.getDate()).padStart(2, "0");
	  return date.getFullYear() + "-" + month + "-" + day;
	}





</script>
</body>
</html>
