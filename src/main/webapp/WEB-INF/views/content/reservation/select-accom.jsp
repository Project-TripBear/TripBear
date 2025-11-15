<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/reservation.css">

<c:set var="userRouteId" value="${param.userRouteId}" />

<div class="select-accom-page">

<div class="page-inner">
<div class="select-layout">
	<!-- Left: Map -->
	<div class="map-wrap">
		<div id="map"></div>
	</div>

	<!-- Right: Cards -->
	<div class="cards">
		<div class="toolbar">
			<div>
				<span class="chip" id="showAll">모두 보기</span>
			</div>
			<div class="sub">
				지역: <b>${region}</b> · 기간: <b>${checkin}</b> ~ <b>${checkout}</b>
			</div>
		</div>

		<c:choose>
			<c:when test="${empty rooms}">
				<div class="empty">해당 지역의 숙소가 없습니다.</div>
			</c:when>
			<c:otherwise>
				<div id="roomList">
					<c:forEach var="r" items="${rooms}">
						<div class="card" data-room-id="${r.roomId}" data-accom-id="${r.accomId}">
							<c:choose>
					           <c:when test="${not empty r.imageUrl}">
					               <img class="thumb" src="${pageContext.request.contextPath}/resources/img/room/${r.imageUrl}">
					           </c:when>
					           <c:otherwise>
					               <img class="thumb" src="${pageContext.request.contextPath}/resources/img/room/default-hotel.jpg" alt="기본 숙소 이미지">
					           </c:otherwise>
					        </c:choose>

							<div class="meta">
								<h4>${r.accomName}·${r.roomName}</h4>
								<div class="sub">${r.address}</div>
								<div class="price">
									<fmt:formatNumber value="${r.pricePerNight}" pattern="#,###" />
									원 / 1박
								</div>
							</div>
							<div class="actions">
								<!-- 다음 단계: 차량 선택 페이지로 이동 -->
								<form method="get"
									action="${pageContext.request.contextPath}/reservation/select-car">
									<input type="hidden" name="userRouteId" value="${userRouteId}" />
									<input type="hidden" name="region" value="${region}" /> 
									<input type="hidden" name="checkin" value="${checkin}" /> 
									<input type="hidden" name="checkout" value="${checkout}" /> 
									<input type="hidden" name="roomId" value="${r.roomId}" />
									
									<button class="btn primary" type="submit">이 방 선택</button>
								</form>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	
</div>
</div>
</div>

<!-- Kakao Map SDK (키 바꿔줘!) -->
<script type="text/javascript"
	src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=0e065a782a80bd99c2c88184e66bff5a&libraries=services"></script>

<script>

const rooms = JSON.parse('${roomsJson}');

window.kakao.maps.load(function(){

    const mapEl = document.getElementById('map');
    const map = new kakao.maps.Map(mapEl, {
        center: new kakao.maps.LatLng(35.1796, 129.0756),
        level: 7
    });

    const roomListEl = document.getElementById('roomList');
    const showAllBtn = document.getElementById('showAll');

    function filterToAccom(accomId) {
        const cards = roomListEl.querySelectorAll('.card');
        cards.forEach(c=>{
            c.style.display = (c.dataset.accomId == accomId) ? 'flex' : 'none';
        });
    }

    function showAll() {
        const cards = roomListEl.querySelectorAll('.card');
        cards.forEach(c=> c.style.display='flex');
    }

    // 모두보기 버튼 연결
    showAllBtn.addEventListener('click', showAll);

    rooms.forEach(r=>{
        const marker = new kakao.maps.Marker({
            position : new kakao.maps.LatLng(r.lat, r.lng),
            map : map
        });

        kakao.maps.event.addListener(marker,'click',()=>{
        	filterToAccom(r.accomId);
        });
    });
    
    const markerMap = {};  // roomId -> marker 저장

    rooms.forEach(r=>{
        const marker = new kakao.maps.Marker({
            position : new kakao.maps.LatLng(r.lat, r.lng),
            map : map
        });
        markerMap[r.roomId] = marker; // 저장

        kakao.maps.event.addListener(marker,'click',()=>{
            filterToAccom(r.accomId);
            map.setCenter(marker.getPosition());
        });

    });

    // 카드 클릭 → 마커 중심 이동
    document.querySelectorAll('.card').forEach(card=>{
        card.addEventListener('click', ()=>{
            const id = card.dataset.roomId;
            map.setCenter(markerMap[id].getPosition());
        });
    });

});
</script>

