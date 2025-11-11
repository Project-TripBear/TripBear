<%-- admin_header.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header id="admin-header-tuned">
    <div class="header-inner">
        <nav class="nav-menu">
            <a href="${pageContext.request.contextPath}/admin/report.do">신고 관리</a>
            
            <%-- ▼▼▼ [핵심 수정] .do 확장자를 제거하고 Controller 경로와 일치시킵니다. ▼▼▼ --%>
            <a href="${pageContext.request.contextPath}/admin/user/list">회원 관리</a>
            
			<%-- "예약 관리" 링크 수정 --%>
			<a href="${pageContext.request.contextPath}/admin/accom/list">예약 관리</a>            
			<a href="${pageContext.request.contextPath}/admin/stats/member.do">통계 관리</a>
        </nav>
        </div>
</header>