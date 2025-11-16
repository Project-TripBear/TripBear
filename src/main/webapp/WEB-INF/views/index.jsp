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
                <a href="${pageContext.request.contextPath}/ai/plan" class="btn btn-primary hero-cta">
                    <span>AI 루트 만들기</span>
                    <span class="hero-cta-arrow">→</span>
                </a>
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
                    <div class="home-weather-top">
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

                        <div class="weather-right">
                            <div class="weather-detail">
                                <div class="weather-detail-item">
                                    <span class="label">습도</span>
                                    <span class="value">48%</span>
                                </div>
                                <div class="weather-detail-item">
                                    <span class="label">미세먼지</span>
                                    <span class="value good">좋음</span>
                                </div>
                                <div class="weather-detail-item">
                                    <span class="label">UV</span>
                                    <span class="value">2 낮음</span>
                                </div>
                                <div class="weather-detail-item">
                                    <span class="label">체크 포인트</span>
                                    <span class="value">산책 · 야외활동 O</span>
                                </div>
                            </div>
                        </div>
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

    <!-- 3) 지역별 루트 & 실시간 순위 -->
    <section class="home-section region-section">
        <div class="home-section-header">
            <div>
                <h2 class="home-section-title">지역별 추천 루트</h2>
                <p class="home-section-sub">이번 주 여행자들이 가장 많이 찾는 지역별 시그니처 동선을 만나보세요.</p>
            </div>
            <div class="home-section-link">
                <a href="${pageContext.request.contextPath}/ai/plan">AI 루트 전체 보기</a>
            </div>
        </div>

        <div class="region-grid">
            <article class="region-card seoul">
                <div class="region-card-header">
                    <span class="region-badge">서울 · 1일 코스</span>
                    <span class="region-temp">7° 맑음</span>
                </div>
                <h3>한강부터 익선동까지 감성 가득 루트</h3>
                <p class="region-desc">아침 요가 · 브런치 · 감성카페 · 야경까지 하루에 담았어요.</p>
                <div class="region-tags">
                    <span>#호캉스</span>
                    <span>#브런치</span>
                    <span>#야경산책</span>
                </div>
                <div class="region-card-meta">
                    <span>AI 만족도 94%</span>
                    <button class="region-cta" onclick="location.href='${pageContext.request.contextPath}/ai/plan'">루트 만들기</button>
                </div>
            </article>

            <article class="region-card busan">
                <div class="region-card-header">
                    <span class="region-badge">부산 · 주말 여행</span>
                    <span class="region-temp">12° 흐림</span>
                </div>
                <h3>서핑 · 미식 · 밤바다 감성 버스킹</h3>
                <p class="region-desc">기장 해안 드라이브와 남포동 골목 투어까지 이어지는 루트.</p>
                <div class="region-tags">
                    <span>#바다</span>
                    <span>#푸드트립</span>
                    <span>#버스킹</span>
                </div>
                <div class="region-card-meta">
                    <span>추천 이유 · 걷기 40% · 대중교통 60%</span>
                    <button class="region-cta" onclick="location.href='${pageContext.request.contextPath}/ai/plan'">지금 생성</button>
                </div>
            </article>

            <article class="region-card jeju">
                <div class="region-card-header">
                    <span class="region-badge">제주 · 2일 코스</span>
                    <span class="region-temp">10° 흐림</span>
                </div>
                <h3>서쪽 드라이브와 감귤 수확 체험</h3>
                <p class="region-desc">오름 트래킹과 로컬카페를 잇는 드라이브 중심 루트.</p>
                <div class="region-tags">
                    <span>#드라이브</span>
                    <span>#감귤체험</span>
                    <span>#힐링</span>
                </div>
                <div class="region-card-meta">
                    <span>렌터카 · 커플 여행 선호 1위</span>
                    <button class="region-cta" onclick="location.href='${pageContext.request.contextPath}/ai/plan'">AI 제안 받기</button>
                </div>
            </article>
        </div>
    </section>

    <section class="home-section trend-section">
        <div class="trend-grid">
            <div class="trend-panel">
                <div class="home-section-header small">
                    <h3>현재 뜨는 지역 순위</h3>
                    <span class="home-section-sub">실시간 생성 루트 기준</span>
                </div>

                <ol class="trend-rank-list">
                    <li>
                        <span class="rank-num">01</span>
                        <div>
                            <p class="rank-title">강릉 · 커피 투어</p>
                            <p class="rank-meta">따뜻한 겨울바다 + 카페 5선</p>
                        </div>
                        <span class="rank-change up">▲ +3</span>
                    </li>
                    <li>
                        <span class="rank-num">02</span>
                        <div>
                            <p class="rank-title">여수 · 미식 여행</p>
                            <p class="rank-meta">굴 · 게장 제철 시즌</p>
                        </div>
                        <span class="rank-change same">—</span>
                    </li>
                    <li>
                        <span class="rank-num">03</span>
                        <div>
                            <p class="rank-title">순천 · 정원 힐링</p>
                            <p class="rank-meta">억새 · 갈대장관</p>
                        </div>
                        <span class="rank-change up">▲ +1</span>
                    </li>
                    <li>
                        <span class="rank-num">04</span>
                        <div>
                            <p class="rank-title">경주 · 야경 투어</p>
                            <p class="rank-meta">동궁과 월지 루미나리에</p>
                        </div>
                        <span class="rank-change down">▼ -1</span>
                    </li>
                    <li>
                        <span class="rank-num">05</span>
                        <div>
                            <p class="rank-title">담양 · 감성 드라이브</p>
                            <p class="rank-meta">메타세쿼이아 뷰</p>
                        </div>
                        <span class="rank-change up">▲ +2</span>
                    </li>
                </ol>
            </div>

            <div class="trend-panel highlight">
                <div class="trend-panel-body">
                    <p class="trend-panel-label">AI 추천 TIP</p>
                    <h3>이번 주엔 <span>눈 덜 오는 남해안</span>으로 떠나보세요</h3>
                    <p class="trend-panel-desc">따뜻한 남해권으로 이동하면 야외 활동 지수가 32% 상승하고, 교통 정체도 적어요. 나만의 루트를 지금 바로 생성해 보세요.</p>
                    <a href="${pageContext.request.contextPath}/ai/plan" class="trend-panel-cta">맞춤 루트 만들기</a>
                </div>
                <div class="trend-panel-pattern"></div>
            </div>
        </div>
    </section>

</main>
