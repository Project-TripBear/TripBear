<%-- 파일 경로: /WEB-INF/views/content/admin/main.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>


<c:if test="${not empty msg}">
    <div class="alert alert-success" style="margin-bottom: 1.5rem;"><i class="fa-solid fa-circle-check"></i> ${msg}</div>
</c:if>

<%-- 핵심 지표 요약 카드 (4개) --%>
<div class="dashboard-grid-4col">
    <%-- ... (4개 카드 내용 유지) ... --%>
    <div class="stat-card summary-card">
        <div class="icon-wrapper bg-primary"><i class="fa-solid fa-users fa-2x"></i></div>
        <div class="stat-content">
            <p class="stat-label">총 회원 수</p>
            <p class="stat-value"><fmt:formatNumber value="${stats.totalMembers}" pattern="#,###" /></p>
        </div>
    </div>
    <div class="stat-card summary-card">
        <div class="icon-wrapper bg-success"><i class="fa-solid fa-user-plus fa-2x"></i></div>
        <div class="stat-content">
            <p class="stat-label">오늘의 신규 회원</p>
            <p class="stat-value"><fmt:formatNumber value="${stats.newMembersToday}" pattern="#,###" /></p>
        </div>
    </div>
    <div class="stat-card summary-card">
        <div class="icon-wrapper bg-warning"><i class="fa-solid fa-bell fa-2x"></i></div>
        <div class="stat-content">
            <p class="stat-label">신규 신고 (대기)</p>
            <p class="stat-value"><fmt:formatNumber value="${stats.pendingReportsCount}" pattern="#,###" /></p>
            <c:if test="${stats.pendingReportsCount > 0}">
                <a href="${pageContext.request.contextPath}/admin/report/list" class="action-link">바로 처리하기 <i class="fa-solid fa-arrow-right-long"></i></a>
            </c:if>
        </div>
    </div>
    <div class="stat-card summary-card">
        <div class="icon-wrapper bg-info"><i class="fa-solid fa-bed fa-2x"></i></div>
        <div class="stat-content">
            <p class="stat-label">오늘의 신규 숙소/렌터카</p>
            <p class="stat-value"><fmt:formatNumber value="${stats.newAccomCarCountToday}" pattern="#,###" /></p>
        </div>
    </div>
</div>

<%-- 1. ★★★ [위치 교환] 실시간 신고 내역 (테이블)이 맨 위로 올라옵니다. ★★★ --%>
<div class="stat-card full-width" style="margin-top: 2rem;">
    <h3 class="stat-card-title">🚨 최신 신고 내역 (실시간)</h3>
    <table class="admin-table simple-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>제목</th>
                <th>유형</th>
                <th>신고자</th>
                <th>신고일</th>
                <th>상태</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty latestReports}">
                    <c:forEach items="${latestReports}" var="report" end="5">
                        <tr>
                            <td>${report.reportId}</td>
                            <td><a href="${pageContext.request.contextPath}/admin/report/list" class="text-link">${report.postTitle}</a></td>
                            <td>${report.reportTargetType}</td>
                            <td>${report.reporterNickname}</td>
                            <td><fmt:formatDate value="${report.reportRegdate}" pattern="yyyy-MM-dd"/></td>
                            <td><span class="status-badge pending">대기</span></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr><td colspan="6" style="text-align: center;">대기 중인 최신 신고 내역이 없습니다.</td></tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
    <div class="card-footer-action">
        <a href="${pageContext.request.contextPath}/admin/report/list" class="text-link">모든 신고 처리하기 <i class="fa-solid fa-angle-right"></i></a>
    </div>
</div>


<%-- 2. ★★★ [위치 교환] 월별 방문자 수 추이 (차트)가 그 다음으로 이동합니다. ★★★ --%>
<div class="stat-card chart-container" style="margin-top: 1.5rem;">
    <h3 class="stat-card-title">월별 방문자 수 추이</h3>
    <canvas id="monthlyVisitorsChart"></canvas>
</div>


<%-- 3. 게시판, 인기 여행지, 기타 목록은 유지 --%>
<div class="dashboard-grid-3col" style="margin-top: 1.5rem;">
    
    <%-- 3-1. 게시판 활동 분포 (도넛 차트) --%>
    <div class="stat-card chart-container">
        <h3 class="stat-card-title">게시판 활동 분포</h3>
        <canvas id="boardActivityChart"></canvas>
    </div>
    
    <%-- 3-2. 인기 여행지 (Hot Place) --%>
    <div class="stat-card">
        <h3 class="stat-card-title">인기 여행지 (Hot Place)</h3>
        <ul class="simple-list" style="margin-top: 0.5rem;">
            <li><span style="font-weight: bold; color: var(--danger-color);">1. 제주 성산일출봉</span><span class="list-count">조회 5,100</span></li>
            <li><span style="font-weight: bold; color: #ffc107;">2. 부산 해운대</span><span class="list-count">조회 4,300</span></li>
            <li><span style="font-weight: bold; color: #17A2B8;">3. 강릉 안목해변</span><span class="list-count">조회 3,900</span></li>
            <li><span style="font-weight: bold;">4. 경주 불국사</span><span class="list-count">조회 3,200</span></li>
            <li><span style="font-weight: bold;">5. 서울 남산타워</span><span class="list-count">조회 2,800</span></li>
        </ul>
        <div class="card-footer-action">
            <a href="${pageContext.request.contextPath}/allplace/map" class="text-link">모두 보기 <i class="fa-solid fa-angle-right"></i></a>
        </div>
    </div>
    
    <%-- 3-3. 인기 게시글 --%>
    <div class="stat-card">
        <h3 class="stat-card-title">인기 게시글 (주간)</h3>
        <ul class="simple-list">
            <li><a href="#" class="text-link">제주 한 달 살기 숙소 추천</a><span class="list-count">1,200</span></li>
            <li><a href="#" class="text-link">부산 여행 루트 공유 (해운대, 광안리)</a><span class="list-count">980</span></li>
            <li><a href="#" class="text-link">가족끼리 가기 좋은 렌터카 추천</a><span class="list-count">750</span></li>
        </ul>
        <div class="card-footer-action">
            <a href="${pageContext.request.contextPath}/admin/board/integratedList" class="text-link">더 보기 <i class="fa-solid fa-angle-right"></i></a>
        </div>
    </div>
</div>

<div class="dashboard-grid-2col" style="margin-top: 1.5rem;">
    <%-- 4. 인기 숙소/렌터카 --%>
    <div class="stat-card">
        <h3 class="stat-card-title">인기 숙소/렌터카</h3>
        <ul class="simple-list">
            <li><a href="#" class="text-link">제주도 '더 쉼' 풀빌라</a><span class="list-count">250 예약</span></li>
            <li><a href="#" class="text-link">부산 해운대 비치 호텔</a><span class="list-count">180 예약</span></li>
            <li><a href="#" class="text-link">현대 쏘나타 (제주 공항점)</a><span class="list-count">120 예약</span></li>
        </ul>
        <div class="card-footer-action">
            <a href="${pageContext.request.contextPath}/admin/accom/list" class="text-link">더 보기 <i class="fa-solid fa-angle-right"></i></a>
        </div>
    </div>

    <%-- 5. ★★★ 빈 카드 또는 기타 통계용 공간 ★★★ --%>
    <div class="stat-card">
        <h3 class="stat-card-title">전체 회원 및 활동 요약</h3>
        <ul class="simple-list">
            <li><span style="font-weight: bold;">총 게시물 수:</span> 5,200 건</li>
            <li><span style="font-weight: bold;">총 예약 건수:</span> 1,800 건</li>
            <li><span style="font-weight: bold;">오늘 접속 수:</span> ${stats.visitorsToday} 명</li>
        </ul>
        <p style="text-align: center; margin-top: 1.5rem; color: var(--text-secondary); font-size: 0.9rem;">(데이터는 임시값/가정치입니다)</p>
    </div>
</div>


<script>
    // ... (차트 스크립트 유지) ...
    // 월별 방문자 수 (꺾은선 그래프)
    const monthlyVisitorsCtx = document.getElementById('monthlyVisitorsChart').getContext('2d');
    new Chart(monthlyVisitorsCtx, {
        type: 'line',
        data: {
            labels: ['1월', '2월', '3월', '4월', '5월', '6월'],
            datasets: [{
                label: '방문자 수',
                data: [15000, 18000, 22000, 25000, 23000, 27000],
                borderColor: 'var(--primary-color)',
                backgroundColor: 'rgba(52, 58, 64, 0.1)',
                fill: true,
                tension: 0.4,
                pointBackgroundColor: 'var(--primary-color)',
                pointBorderColor: 'var(--primary-color)'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: { mode: 'index', intersect: false }
            },
            scales: {
                x: { grid: { display: false } },
                y: { beginAtZero: true, grid: { color: 'var(--border-color-light)' } }
            }
        }
    });

    // 게시판 활동 분포 (도넛 차트)
    const boardActivityCtx = document.getElementById('boardActivityChart').getContext('2d');
    new Chart(boardActivityCtx, {
        type: 'doughnut',
        data: {
            labels: ['여행 후기', 'Q&A', '여행 루트', '자유 게시판'],
            datasets: [{
                label: '게시물 수',
                data: [40, 25, 20, 15],
                backgroundColor: ['#007BFF', '#28A745', '#FFC107', '#6c757d'],
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom', labels: { boxWidth: 12 } },
                tooltip: { callbacks: { label: function(tooltipItem) { return tooltipItem.label + ': ' + tooltipItem.raw + '%'; } } }
            }
        }
    });
</script>