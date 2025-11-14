<%-- 파일 경로: /WEB-INF/views/inc/admin_sidebar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- ★★★ [수정] Bootstrap 클래스 (bg-dark text-white) 제거 ★★★ --%>
<div class="sidebar-wrapper">
    <%-- ul의 nav/flex-column/sidebar-nav는 Bootstrap과 저희 CSS 모두에 영향을 줍니다. 그대로 유지합니다. --%>
    <ul class="nav flex-column sidebar-nav">
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/main">
                <i class="fa-solid fa-chart-line"></i> 대시보드
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/board/integratedList">
                <i class="fa-solid fa-clipboard-list"></i> 통합 게시판
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/user/list">
                <i class="fa-solid fa-users"></i> 회원 관리
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/accom/list">
                <i class="fa-solid fa-bed"></i> 숙소/렌터카
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/report/list">
                <i class="fa-solid fa-triangle-exclamation"></i> 신고 관리
            </a>
        </li>
    </ul>
</div>