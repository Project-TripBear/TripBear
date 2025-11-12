<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
  [Tiles Content]
  컨트롤러로부터 받는 데이터 없이, JSP 자체적으로 AJAX 호출을 수행합니다.
--%>

<head>
    <style>
        .weather-page-container { max-width: 900px; margin: 20px auto; }
        .region-buttons { 
            display: flex; 
            flex-wrap: wrap; 
            gap: 10px;
            margin-bottom: 20px;
        }
        .region-buttons button { 
            margin: 5px; 
            padding: 10px 15px; 
            font-size: 16px; 
            border: 1px solid #ccc;
            background: #fff;
            cursor: pointer;
            border-radius: 20px;
        }
        .region-buttons button:hover { background: #f0f0f0; }
        #weather-result-display { 
            margin-top: 20px; 
            background: #f4f8ff; 
            padding: 20px; 
            border-radius: 8px; 
            min-height: 150px; 
            font-size: 1.1rem;
        }
    </style>
</head>

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
document.addEventListener("DOMContentLoaded", function() {
    const buttons = document.querySelectorAll(".weather-btn");
    const display = document.getElementById("weather-result-display");

    buttons.forEach(function(button) {
        button.addEventListener("click", function() {
            const lat = this.getAttribute("data-lat");
            const lon = this.getAttribute("data-lon");
            const name = this.getAttribute("data-name");
            
            display.innerHTML = `<p>${name}의 날씨를 조회 중...</p>`;

            // [중요] '/allplace/weatherok' API 호출
            fetch(`/allplace/weatherok?lat=${lat}&lon=${lon}`)
                .then(response => {
                    if (!response.ok) throw new Error('Network response was not ok');
                    return response.json();
                })
                .then(weather => {
                    // WeatherVO 객체 (weather.skyStatus, weather.temperature 등)
                    display.innerHTML = `
                        <h3>${name} 현재 날씨</h3>
                        <p>
                            <strong>상태:</strong> ${weather.skyStatus} <br>
                            <strong>기온:</strong> ${weather.temperature}°C <br>
                            <strong>강수:</strong> ${weather.rainAmount} <br>
                            <strong>습도:</strong> ${weather.humidity}%
                        </p>
                        <small>(기준: ${weather.baseDate} ${weather.baseTime})</small>
                    `;
                })
                .catch(error => {
                    console.error('날씨 조회 실패:', error);
                    display.innerHTML = `<p>${name}의 날씨를 조회하는 데 실패했습니다.</p>`;
                });
        });
    });
</script>