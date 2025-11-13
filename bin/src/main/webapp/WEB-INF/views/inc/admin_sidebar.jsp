<%-- admin_sidebar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="sidebar-wrapper bg-dark text-white">
    <ul class="nav flex-column sidebar-nav">
        <li class="nav-item">
            <%-- ★★★ [수정] ${pageContext.request.contextPath} 추가 ★★★ --%>
            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/board/integratedList">통합 게시판</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/user/list">회원 관리</a>
        </li>
    </ul>
</div>