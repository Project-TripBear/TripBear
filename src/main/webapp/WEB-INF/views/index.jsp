<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<title>트립베어</title>

<main class="home-main">
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
		
		<!-- ========================= -->
		<!-- 🔻 메인 하단 하이라이트 섹션 -->
		<!-- ========================= -->

		<%-- 1. 오늘의 날씨 (하드코딩 예시) --%>
		<section class="home-section home-weather">
		    <div class="home-section-header">
		        <h2>오늘의 날씨</h2>
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
		</section>





		<%-- 2. 지금 핫한 여행지 (하드코딩 카드 3개) --%>
		<section class="home-section home-hotspot">
		    <div class="home-section-header">
		        <h2>지금 핫한 여행지</h2>
		        <a href="${pageContext.request.contextPath}/trip/list" class="home-more-link">
		            더 많은 여행지 보기
		        </a>
		    </div>

		    <div class="home-card-grid">
		        <article class="home-card">
		            <div class="home-card-thumb-wrap">
		                <img src="${pageContext.request.contextPath}/resources/img/home/seongsu.jpg"
		                     alt="서울 성수동 카페 거리" class="home-card-thumb">
		            </div>
		            <div class="home-card-body">
		                <h3 class="home-card-title">서울 성수동 카페 투어</h3>
		                <p class="home-card-meta">서울 · 감성 카페 / 로컬 산책</p>
		                <p class="home-card-desc">
		                    공장 리모델링 카페, 편집숍, 루프탑까지. 주말 감성 데이트 스팟으로 가장 많이 찾는 동네예요.
		                </p>
		                <div class="home-card-tags">
		                    <span>#근교데이트</span>
		                    <span>#요즘핫플</span>
		                </div>
		            </div>
		        </article>

		        <article class="home-card">
		            <div class="home-card-thumb-wrap">
		                <img src="${pageContext.request.contextPath}/resources/img/home/busan.jpg"
		                     alt="부산 해운대 야경" class="home-card-thumb">
		            </div>
		            <div class="home-card-body">
		                <h3 class="home-card-title">부산 해운대 야경 산책</h3>
		                <p class="home-card-meta">부산 · 바다 / 야경</p>
		                <p class="home-card-desc">
		                    엑스더스카이 전망대와 해운대 해변을 한 번에. 밤바다 보면서 산책하기 좋은 루트로 많이 저장돼요.
		                </p>
		                <div class="home-card-tags">
		                    <span>#야경맛집</span>
		                    <span>#바다러버</span>
		                </div>
		            </div>
		        </article>

		        <article class="home-card">
		            <div class="home-card-thumb-wrap">
		                <img src="${pageContext.request.contextPath}/resources/img/home/jeju.jpg"
		                     alt="제주도 드라이브" class="home-card-thumb">
		            </div>
		            <div class="home-card-body">
		                <h3 class="home-card-title">제주 서쪽 감성 드라이브</h3>
		                <p class="home-card-meta">제주 · 드라이브 / 오션뷰</p>
		                <p class="home-card-desc">
		                    애월–한림 라인 따라 카페와 바다를 번갈아 보는 인기 드라이브 코스. 1박 2일 코스로도 딱 좋아요.
		                </p>
		                <div class="home-card-tags">
		                    <span>#드라이브</span>
		                    <span>#오션뷰카페</span>
		                </div>
		            </div>
		        </article>
		    </div>
		</section>


		<%-- 3. 지금 즐기기 좋은 축제 (하드코딩 리스트) --%>
		<section class="home-section home-festival">
		    <div class="home-section-header">
		        <h2>지금 즐기기 좋은 축제</h2>
		        <a href="${pageContext.request.contextPath}/festival/list" class="home-more-link">
		            축제 전체 보기
		        </a>
		    </div>

		    <ul class="home-list">
		        <li class="home-list-item">
		            <div class="home-list-main">
		                <span class="home-list-title">서울 불빛 축제</span>
		                <span class="home-list-badge">서울 한강공원</span>
		            </div>
		            <div class="home-list-sub">
		                <span>2025.11.20 ~ 2025.11.29</span>
		                <span class="home-list-tag">야간 / 라이브 공연</span>
		            </div>
		        </li>

		        <li class="home-list-item">
		            <div class="home-list-main">
		                <span class="home-list-title">부산 겨울바다 페스티벌</span>
		                <span class="home-list-badge">부산 광안리</span>
		            </div>
		            <div class="home-list-sub">
		                <span>2025.12.05 ~ 2025.12.14</span>
		                <span class="home-list-tag">푸드트럭 / 불꽃</span>
		            </div>
		        </li>

		        <li class="home-list-item">
		            <div class="home-list-main">
		                <span class="home-list-title">제주 감귤 수확 체험</span>
		                <span class="home-list-badge">제주 서귀포</span>
		            </div>
		            <div class="home-list-sub">
		                <span>2025.11 ~ 2026.01</span>
		                <span class="home-list-tag">체험 / 가족여행</span>
		            </div>
		        </li>
		    </ul>
		</section>


		<%-- 4. AI 추천 루트 미리보기 (하드코딩 타임라인) --%>
		<section class="home-section home-ai-preview">
		    <div class="home-section-header">
		        <h2>AI가 뽑아준 오늘의 추천 루트</h2>
		        <a href="${pageContext.request.contextPath}/ai/plan" class="home-more-link">
		            직접 루트 뽑아보기
		        </a>
		    </div>

		    <p class="home-ai-text-title">[서울 성수동] 4시간 감성 산책 루트</p>

		    <ol class="home-ai-timeline">
		        <li>
		            <span class="step">1</span>
		            <div class="content">
		                <p class="title">뚝섬 한강공원 피크닉</p>
		                <p class="desc">한강 뷰 보면서 간단한 간식 먹고 천천히 산책 시작.</p>
		            </div>
		        </li>
		        <li>
		            <span class="step">2</span>
		            <div class="content">
		                <p class="title">성수 브런치 카페</p>
		                <p class="desc">공장 리모델링 카페에서 브런치와 커피 한 잔.</p>
		            </div>
		        </li>
		        <li>
		            <span class="step">3</span>
		            <div class="content">
		                <p class="title">편집숍 & 소품샵 둘러보기</p>
		                <p class="desc">로컬 브랜드 구경하면서 간단한 기념품 쇼핑.</p>
		            </div>
		        </li>
		        <li>
		            <span class="step">4</span>
		            <div class="content">
		                <p class="title">루프탑 바에서 마무리</p>
		                <p class="desc">노을 보는 루프탑 바에서 하루 마무리.</p>
		            </div>
		        </li>
		    </ol>
		</section>

	</main>


