<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- [추가] contextPath를 JSP 변수로 설정 --%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<link rel="stylesheet" href="${contextPath}/resources/css/allplace/weather.css" />

<div class="weather-page-container">
    <h2>전국 날씨 정보</h2>
    <p>확인하고 싶은 지역의 버튼을 클릭하세요.</p>

    <div class="region-buttons">
	    <button class="weather-btn" data-lat="37.566826" data-lon="126.9786567" data-name="서울">서울</button>
	    <button class="weather-btn" data-lat="37.4562557" data-lon="126.7052062" data-name="인천">인천</button>
	    <button class="weather-btn" data-lat="37.751853" data-lon="128.8760574" data-name="강릉">강릉</button>
	    <button class="weather-btn" data-lat="36.3504119" data-lon="127.3845475" data-name="대전">대전</button>
	    <button class="weather-btn" data-lat="35.8714354" data-lon="128.601445" data-name="대구">대구</button>
	    <button class="weather-btn" data-lat="35.1595454" data-lon="126.8526012" data-name="광주">광주</button>
	    <button class="weather-btn" data-lat="35.179816" data-lon="129.0750223" data-name="부산">부산</button>
	    <button class="weather-btn" data-lat="35.5383773" data-lon="129.3113596" data-name="울산">울산</button>
	    <button class="weather-btn" data-lat="33.4996213" data-lon="126.5311884" data-name="제주">제주</button>
	</div>

    
    <div id="weather-message-display" style="text-align: center; padding: 20px;">
        <p>지역을 선택해주세요.</p>
    </div>

    <div id="weather-result-display" class="weather-card" style="display:none;">

    <h3 id="weather-location-name" class="weather-location"></h3>

    <div class="weather-main">
        <div class="weather-temp">
            <span id="weather-temp"></span>°C
        </div>
        <div id="weather-status" class="weather-status"></div>
    </div>

    <div class="weather-info">
        <div class="info-item">
            <span class="label">강수량</span>
            <span id="weather-rain"></span>
        </div>
        <div class="info-item">
            <span class="label">습도</span>
            <span id="weather-humidity"></span>% 
        </div>
        <div class="info-item">
            <span class="label">기준시간</span>
            <span id="weather-base"></span>
        </div>
    </div>

</div>

    </div>

<script>
document.addEventListener("DOMContentLoaded", function() {
    
    // (1) JSP의 contextPath를 JavaScript 변수로 가져옵니다.
    const contextPath = "${contextPath}"; // 404 방지

    // (2) 업데이트할 HTML 요소들을 미리 찾아둡니다.
    // (오류가 났던 부분: 이 ID들이 HTML에 없었습니다.)
    const buttons = document.querySelectorAll(".weather-btn");
    const messageDisplay = document.getElementById("weather-message-display");
    const weatherDisplay = document.getElementById("weather-result-display");
    
    const locationNameEl = document.getElementById("weather-location-name");
    const statusEl = document.getElementById("weather-status");
    const tempEl = document.getElementById("weather-temp");
    const rainEl = document.getElementById("weather-rain");
    const humidityEl = document.getElementById("weather-humidity");
    const baseEl = document.getElementById("weather-base");

    /**
     * (3) 날씨를 조회하고 화면을 업데이트하는 공통 함수
     */
    function fetchWeather(lat, lon, name) {
        
        // [오류 수정] 요소를 찾았는지(null이 아닌지) 확인 후 innerHTML 변경
        if (messageDisplay) {
            messageDisplay.innerHTML = `<p>${name}의 날씨를 조회 중...</p>`;
            messageDisplay.style.display = 'block';
        }
        if (weatherDisplay) {
            weatherDisplay.style.display = 'none';
        }

        // (수정) contextPath를 포함한 URL로 fetch
        fetch(`${contextPath}/allplace/weatherok?lat=\${lat}&lon=\${lon}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json(); 
            })
            .then(weather => {
                // (null 체크 추가)
                if (locationNameEl) locationNameEl.textContent = name;
                if (statusEl) statusEl.textContent = weather.skyStatus;
                if (tempEl) tempEl.textContent = weather.temperature;
                if (rainEl) rainEl.textContent = weather.rainAmount;
                if (humidityEl) humidityEl.textContent = weather.humidity;
                if (baseEl) baseEl.textContent = `${weather.baseDate} ${weather.baseTime}`;
                
                if (weatherDisplay) weatherDisplay.style.display = 'block';
                if (messageDisplay) messageDisplay.style.display = 'none';
            })
            .catch(error => {
                console.error('날씨 조회 실패:', error);
                if (messageDisplay) {
                    messageDisplay.innerHTML = `<p>${name}의 날씨를 조회하는 데 실패했습니다. (콘솔을 확인하세요)</p>`;
                }
            });
    }

    // (4) 모든 버튼에 클릭 이벤트 할당
    buttons.forEach(function(button) {
    button.addEventListener("click", function() {

        // 기존 active 제거
        buttons.forEach(btn => btn.classList.remove("active"));

        // 현재 버튼 active
        this.classList.add("active");

        const lat = this.getAttribute("data-lat");
        const lon = this.getAttribute("data-lon");
        const name = this.getAttribute("data-name");

        fetchWeather(lat, lon, name);
    });
});


    // (5) '내 위치'로 자동 조회
    if (navigator.geolocation) {
        if (messageDisplay) messageDisplay.innerHTML = `<p>현재 위치의 날씨를 조회 중...</p>`;
        navigator.geolocation.getCurrentPosition(
            function(position) { // (성공)
                fetchWeather(position.coords.latitude, position.coords.longitude, "현재 위치");
            },
            function(error) { // (거부 또는 실패)
                if (messageDisplay) messageDisplay.innerHTML = `<p>위치를 가져올 수 없습니다. 지역 버튼을 선택해주세요.</p>`;
            }
        );
    } else {
        if (messageDisplay) messageDisplay.innerHTML = `<p>브라우저가 위치 정보를 지원하지 않습니다. 지역 버튼을 선택해주세요.</p>`;
    }
});
</script>