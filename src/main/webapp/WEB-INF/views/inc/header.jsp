<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">

<header id="main-header">
    <div class="header-inner">

        <!-- 좌측 로고 -->
        <div class="header-column">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/" class="logo-wordmark">
                    <span class="logo-trip">Trip</span><span class="logo-bear">Bear</span>
                </a>
            </div>
        </div>

        <!-- 중앙 내비게이션 -->
        <nav class="nav-menu">

            <!-- 여행정보 -->
            <div class="nav-item has-dropdown">
                <a href="#">여행정보</a>
                <div class="sub-menu">
                    <a href="${pageContext.request.contextPath}/allplace/map">관광지 지도</a>
                    <a href="${pageContext.request.contextPath}/info/trend/trend">여행트렌드</a>
                    <a href="#">여행지 뉴스</a>
                    <a href="#">날씨/공기질</a>
                    <a href="#">시기별 축제/행사</a>
                </div>
            </div>

            <!-- 여행루트 -->
            <a href="${pageContext.request.contextPath}/ai/plan"
               class="${activeMenu == 'route' ? 'active' : ''}">여행루트</a>

            <!-- 게시판 -->
            <div class="nav-item has-dropdown">
                <a href="#">게시판</a>
                <div class="sub-menu">
                    <a href="${pageContext.request.contextPath}/routepost/list">여행 루트 추천 게시판</a>
                    <a href="${pageContext.request.contextPath}/reviewboard/list">여행 후기 게시판</a>
                    <a href="${pageContext.request.contextPath}/qna/list">Q&A</a>
                    <a href="${pageContext.request.contextPath}/findboard/list">동행 찾기 게시판</a>
                    <a href="${pageContext.request.contextPath}/hotdeal/list">여행 용품 게시판</a>
                </div>
            </div>

            <!-- 공지사항 -->
            <a href="<c:url value='/notice/list'/>">공지사항</a>

        </nav>

        <!-- 오른쪽: 검색 + 로그인/마이페이지 + 햄버거 -->
        <div class="header-column header-right">

            <!-- 검색 -->
            <form class="search-bar" action="${pageContext.request.contextPath}/search" method="get">
                <input type="text" name="query" placeholder="검색어 입력" autocomplete="off">
                <button type="submit" aria-label="검색">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </button>
            </form>

            <!-- 로그인 / 마이페이지 -->
            <div class="user-info">
                <!-- 미로그인 -->
                <sec:authorize access="isAnonymous()">
                    <a href="${pageContext.request.contextPath}/member/login" class="btn btn-secondary">로그인</a>
                    <a href="${pageContext.request.contextPath}/member/register" class="btn btn-primary">회원가입</a>
                </sec:authorize>

                <!-- 로그인 -->
                <sec:authorize access="isAuthenticated()">
                    <a href="${pageContext.request.contextPath}/member/mypage" class="btn btn-primary">마이페이지</a>
                    <form action="${pageContext.request.contextPath}/member/logout" method="POST" style="display:inline;">
                        <sec:csrfInput/>
                        <button type="submit" class="btn btn-secondary">로그아웃</button>
                    </form>
                </sec:authorize>
            </div>

            <!-- 모바일 햄버거 -->
            <button id="hamburger-btn" class="hamburger-btn" aria-label="메뉴 열기">
                <i class="fa-solid fa-bars"></i>
            </button>
        </div>
    </div>
</header>

<!-- 모바일 전체 패널 -->
<div id="mobile-menu" class="mobile-menu-panel">

    <!-- 모바일 상단 -->
    <div class="menu-header">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/" class="logo-wordmark">
                <span class="logo-trip">Trip</span><span class="logo-bear">Bear</span>
            </a>
        </div>

        <button id="close-menu-btn" class="close-btn" aria-label="메뉴 닫기">
            <i class="fa-solid fa-times"></i>
        </button>
    </div>

    <!-- 모바일 내비 -->
    <nav class="mobile-nav-links">

        <!-- 여행정보 -->
        <div class="mobile-nav-item has-dropdown">
            <a href="#" class="dropdown-toggle">
                여행정보 <i class="fa-solid fa-chevron-down dropdown-arrow"></i>
            </a>
            <div class="mobile-sub-menu">
                <a href="${pageContext.request.contextPath}/allplace/map">관광지 지도</a>
                <a href="${pageContext.request.contextPath}/allplace/trend">여행트렌드</a>
                <a href="${pageContext.request.contextPath}/allplace/news">여행지 뉴스</a>
                <a href="${pageContext.request.contextPath}/allplace/weather">날씨/공기질</a>
                <a href="${pageContext.request.contextPath}/allplace/searchFestival">시기별 축제/행사</a>
            </div>
        </div>

        <!-- 여행루트 -->
        <a href="${pageContext.request.contextPath}/ai/plan" class="mobile-nav-item">여행루트</a>

        <!-- 게시판 -->
        <div class="mobile-nav-item has-dropdown">
            <a href="#" class="dropdown-toggle">
                게시판 <i class="fa-solid fa-chevron-down dropdown-arrow"></i>
            </a>
            <div class="mobile-sub-menu">
                <a href="${pageContext.request.contextPath}/routepost/list">여행 루트 추천</a>
                <a href="${pageContext.request.contextPath}/reviewboard/list">여행 후기</a>
                <a href="${pageContext.request.contextPath}/qna/list">Q&A</a>
                <a href="${pageContext.request.contextPath}/findboard/list">동행 찾기</a>
                <a href="${pageContext.request.contextPath}/board/list">여행 용품</a>
            </div>
        </div>

        <!-- 공지사항 -->
        <a href="<c:url value='/notice/list'/>" class="mobile-nav-item">공지사항</a>
    </nav>

    <!-- 모바일 하단 -->
    <div class="menu-footer">

        <!-- 검색 아이콘 -->
        <a href="#" class="icon-link" aria-label="검색">
            <i class="fa-solid fa-magnifying-glass"></i>
        </a>

        <!-- 로그인/마이페이지 -->
        <sec:authorize access="isAuthenticated()">
            <a href="${pageContext.request.contextPath}/member/mypage"
               class="icon-link profile-link" aria-label="내 프로필">
                <i class="fa-solid fa-user"></i>
            </a>
        </sec:authorize>

        <sec:authorize access="isAnonymous()">
            <a href="${pageContext.request.contextPath}/member/login" class="login-link">로그인</a>
        </sec:authorize>

    </div>
</div>
