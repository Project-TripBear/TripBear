<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<title>트립베어</title>

<main class="home-main">

    <!-- 1) 상단 HERO 영역 -->
    <section class="hero-section">
        <div class="hero-content">
            <div class="hero-badge">🐻 TripBear · AI 여행 루트</div>
            <h1 class="hero-title">내 컨디션에 맞는 <span class="highlight">맞춤 여행루트</span></h1>
            <p class="hero-subtitle">날씨, 이동수단, 헬스케어까지 고려해서 하루 루트를 한 번에 추천해줘요.</p>

            <div class="hero-search">
                <div class="hero-search-input">
                    <input type="text" placeholder="어디로 떠날까요? (도시, 지역명 입력)">
                </div>
                <button type="button" class="btn btn-primary">AI 루트 만들기</button>
            </div>

            <div class="hero-meta">
                <span>✅ 실시간 날씨·미세먼지 반영</span>
                <span>✅ 걷기 / 대중교통 / 렌터카 옵션</span>
            </div>
        </div>

        <div class="hero-visual">
            <div class="hero-card">
                <div class="hero-card-title">오늘의 곰 루트 요약</div>
                <div class="hero-card-sub">컨디션·날씨·예산 맞춰 최적 루트 생성</div>

                <div class="hero-steps">
                    <div class="hero-step">
                        <span class="hero-step-index">1</span>
                        <span>도시 · 일정 · 이동수단 선택</span>
                    </div>
                    <div class="hero-step">
                        <span class="hero-step-index">2</span>
                        <span>헬스케어 / 취향 카드 체크</span>
                    </div>
                    <div class="hero-step">
                        <span class="hero-step-index">3</span>
                        <span>곰이 짜준 루트 확인 & 저장</span>
                    </div>
                </div>

                <div class="hero-status">
                    <span>🟢 지금 추천 가능</span>
                    <span>오늘 생성 루트 24개</span>
                </div>
            </div>
        </div>
    </section>

    <!-- 2) 하이라이트 영역 : 왼쪽 날씨 · 오른쪽 축제 -->
    <section class="home-section home-highlights">
        <div class="home-highlight-grid">

            <!-- 왼쪽 : 오늘의 날씨 -->
            <div class="home-highlight-block">
                <div class="home-section-header small">
                    <h3>오늘의 날씨</h3>
                    <a href="${pageContext.request.contextPath}/weather" class="home-more-link">
                        자세히 보기
                    </a>
                </div>

                <div class="home-weather-hero morning">
                    <div class="weather-left">
                        <div class="weather-label">남양주시 · 아침</div>

                        <div class="weather-temp-row">
                            <span class="weather-temp">6°</span>
                            <span class="weather-feels">체감 2°</span>
                        </div>

                        <p class="weather-desc">
                            오전엔 맑지만 약간 쌀쌀해요.<br>
                            가벼운 외투 하나 챙기면 좋아요.
                        </p>

                        <p class="weather-date">
                            2025. 11. 17 (월) · Morning
                        </p>
                    </div>

                    <ul class="weather-week">
                        <li><span class="day">오늘</span><span class="ico">🌤️</span><span class="t">-2° / 7°</span></li>
                        <li><span class="day">화</span><span class="ico">☀️</span><span class="t">-4° / 6°</span></li>
                        <li><span class="day">수</span><span class="ico">☀️</span><span class="t">-5° / 7°</span></li>
                        <li><span class="day">목</span><span class="ico">⛅</span><span class="t">-1° / 12°</span></li>
                        <li><span class="day">금</span><span class="ico">☀️</span><span class="t">-1° / 11°</span></li>
                    </ul>

                    <div class="weather-sun">🌞</div>
                </div>
            </div>

            <!-- 오른쪽 : 지금 즐기기 좋은 축제 -->
            <div class="home-highlight-block">
                <div class="home-section-header small">
                    <h3>지금 즐기기 좋은 축제</h3>
                    <a href="${pageContext.request.contextPath}/allplace/festival" class="home-more-link">
                        축제 전체 보기
                    </a>
                </div>

                <div class="festival-card">
                    <ul class="festival-list">
                        <li class="festival-item">
                            <div class="festival-main">
                                <span class="festival-title">서울 불빛 축제</span>
                                <span class="festival-location-badge">서울 한강공원</span>
                            </div>
                            <div class="festival-sub">
                                <span class="festival-date">2025.11.20 ~ 2025.11.29</span>
                                <span class="festival-tags">야간 · 라이브 공연</span>
                            </div>
                        </li>

                        <li class="festival-item">
                            <div class="festival-main">
                                <span class="festival-title">부산 겨울바다 페스티벌</span>
                                <span class="festival-location-badge">부산 광안리</span>
                            </div>
                            <div class="festival-sub">
                                <span class="festival-date">2025.12.05 ~ 2025.12.14</span>
                                <span class="festival-tags">푸드트럭 · 불꽃</span>
                            </div>
                        </li>

                        <li class="festival-item">
                            <div class="festival-main">
                                <span class="festival-title">제주 감귤 수확 체험</span>
                                <span class="festival-location-badge">제주 서귀포</span>
                            </div>
                            <div class="festival-sub">
                                <span class="festival-date">2025.11 ~ 2026.01</span>
                                <span class="festival-tags">체험 · 가족여행</span>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>

        </div>
    </section>

</main>
