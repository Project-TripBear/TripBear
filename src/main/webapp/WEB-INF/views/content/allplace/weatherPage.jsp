<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

    <div id="weather-result-display">
        <p>지역을 선택해주세요.</p>
    </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", () => {

    const contextPath = "${contextPath}";
    const buttons = document.querySelectorAll(".weather-btn");
    const display = document.getElementById("weather-result-display");

    function fetchWeather(lat, lon, name) {

        if (!lat || !lon) {
            display.innerHTML = `<p>${name}: 좌표 정보 없음</p>`;
            return;
        }

        fetch(`${contextPath}/allplace/weatherok?lat=\${lat}&lon=\${lon}`)
            .then(res => {
                if (!res.ok) throw new Error("Network error");
                return res.json();
            })
            .then(weather => {
                display.innerHTML = `
                    <h3>${name} 현재 날씨</h3>
                    <p>
                        <strong>상태:</strong> ${weather.skyStatus}<br>
                        <strong>기온:</strong> ${weather.temperature}°C<br>
                        <strong>강수:</strong> ${weather.rainAmount}<br>
                        <strong>습도:</strong> ${weather.humidity}%
                    </p>
                    <small>(기준: ${weather.baseDate} ${weather.baseTime})</small>
                `;
            })
            .catch(err => {
                console.error("날씨 조회 실패:", err);
                display.innerHTML = `<p>${name}의 날씨 조회 실패.</p>`;
            });
    }

    buttons.forEach(btn => {
        btn.addEventListener("click", function() {
            fetchWeather(this.dataset.lat, this.dataset.lon, this.dataset.name);
        });
    });

});
</script>
