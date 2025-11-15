<%-- 파일 경로: /WEB-INF/views/inc/admin_header.jsp --%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header id="admin-header-tuned">
    <div class="header-inner">
        <div class="header-column">
             <div class="logo">
                <a href="${pageContext.request.contextPath}/admin/main">관리자 시스템</a>
             </div>
        </div>
        <%-- ★★★ [수정] 숙소/렌터카를 개별 메뉴로 분리 ★★★ --%>
        <nav class="nav-menu">
            <a href="${pageContext.request.contextPath}/admin/main">대시보드</a>
            <a href="${pageContext.request.contextPath}/admin/board/integratedList">통합 게시판</a>
            <a href="${pageContext.request.contextPath}/admin/user/list">회원 관리</a>
            <a href="${pageContext.request.contextPath}/admin/accom/list">숙소 관리</a>          
            <a href="${pageContext.request.contextPath}/admin/car/list">렌터카 관리</a>
            <a href="${pageContext.request.contextPath}/admin/report/list">신고 관리</a>
        </nav>
		<form name="logoutForm" action="<c:url value='/admin/logout'/>" method="POST" style="margin: 0;">
		        <%-- 1. CSRF 토큰 (필수) --%>
		        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		        
		        <%-- 2. 로그아웃 버튼 (admin2.css의 스타일 적용) --%>
		        <button type="submit" class="btn-logout">로그아웃</button>
		    </form>
    </div>
</header>