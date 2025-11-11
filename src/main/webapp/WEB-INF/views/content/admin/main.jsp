<%-- 파일 경로: /WEB-INF/views/content/admin/main.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Font Awesome (아이콘용) --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

<h1>관리자 대시보드</h1>

<%-- 
  admin2.css에 정의된 .accom-card-grid 와 유사한 카드 스타일을 사용합니다.
  간단한 통계 카드용 CSS를 <style> 태그로 추가합니다.
--%>
<style>
    .dashboard-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
        gap: 1.5rem;
    }
    .stat-card {
        background-color: var(--content-bg);
        border-radius: var(--border-radius);
        box-shadow: var(--box-shadow);
        padding: 1.5rem;
        display: flex;
        align-items: center;
        gap: 1.5rem;
    }
    .stat-card-icon {
        font-size: 2.5rem;
        color: var(--primary-color);
        width: 60px;
        height: 60px;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: var(--bg-light);
        border-radius: 50%;
    }
    .stat-card-info h3 {
        font-size: 0.9rem;
        font-weight: 600;
        color: var(--text-secondary);
        margin: 0 0 0.5rem 0;
        text-transform: uppercase;
    }
    .stat-card-info p {
        font-size: 2rem;
        font-weight: 700;
        color: var(--text-primary);
        margin: 0;
    }
    .stat-card-info p small {
        font-size: 1rem;
        font-weight: 500;
        color: var(--text-secondary);
    }
</style>

<div class="dashboard-grid">
    
    <div class="stat-card">
        <div class="stat-card-icon">
            <i class="fa-solid fa-users"></i>
        </div>
        <div class="stat-card-info">
            <h3>총 회원 수</h3>
            <p><fmt:formatNumber value="${totalMembers}" /> <small>명</small></p>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-card-icon" style="color: var(--success-color);">
            <i class="fa-solid fa-calendar-check"></i>
        </div>
        <div class="stat-card-info">
            <h3>오늘 예약 건수</h3>
            <p><fmt:formatNumber value="${todayReservations}" /> <small>건</small></p>
        </div>
    </div>
    
    <div class="stat-card">
        <div class="stat-card-icon" style="color: var(--danger-color);">
            <i class="fa-solid fa-triangle-exclamation"></i>
        </div>
        <div class="stat-card-info">
            <h3>대기 중인 신고</h3>
            <p><fmt:formatNumber value="${pendingReports}" /> <small>건</small></p>
        </div>
    </div>

</div>