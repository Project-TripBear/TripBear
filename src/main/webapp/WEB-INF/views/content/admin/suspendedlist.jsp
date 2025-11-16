<%-- 파일 경로: /WEB-INF/views/content/admin/suspendedlist.jsp (수정본) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- ★ 1. contextPath 변수 추가 ★ --%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<h1>정지된 회원 목록</h1>

<div class="board-nav-tabs mb-4">
    <ul class="nav nav-tabs admin-tab-style">
        <li class="nav-item">
            <%-- "전체 회원" 탭 --%>
            <a class="nav-link" 
               href="${contextPath}/admin/user/list">전체 회원</a>
        </li>
        <li class="nav-item">
            <%-- "정지된 회원" 탭 (현재 페이지) --%>
            <a class="nav-link active" 
               href="${contextPath}/admin/user/suspendedlist">정지된 회원</a>
        </li>
    </ul>
</div>
<table class="admin-table">
    <thead>
        <tr>
            <th>정지 번호</th>
            <th>닉네임</th>
            <th>정지 사유</th>
            <th>정지 시작일</th>
            <th>정지 종료일</th>
            <th>관리</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${suspendedlist}" var="user">
            <tr>
                <td>${user.memsuspendedId}</td>
                <td>${user.nickname}</td>
                <td>${user.suspendedReason}</td>
                <td><fmt:formatDate value="${user.suspendedStartDate}" pattern="yyyy-MM-dd"/></td>
                <td><fmt:formatDate value="${user.suspendedEndDate}" pattern="yyyy-MM-dd"/></td>
                <td>
                   <%-- ★ 3. 복구 폼에 CSRF 토큰 추가 (보안) ★ --%>
                   <form method="POST" action="${contextPath}/admin/user/restore" onsubmit="return confirm('[${user.nickname}] 회원을 정말로 복구하시겠습니까?');">
                        
                        <%-- CSRF 토큰 (POST 방식이므로 필수) --%>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        
                        <input type="hidden" name="userId" value="${user.userId}">
                        <button type="submit" class="btn primary" style="padding: 5px 10px;">복구</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty suspendedlist}">
            <tr>
                <td colspan="6" style="text-align: center;">정지된 회원이 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>