<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/notice.css">



<!-- list.jsp -->

<main>
<div class="notice-container">

	<div class="notice-title">
			<h2>공지사항</h2>
	</div>
		
	<table class="notice-table">
		<thead>
			<tr>
				<th>No.</th>
				<th>게시글</th>
				<th>글쓴이</th>
				<th>작성일</th>
				<th>조회수</th>
			</tr>
		</thead>
		<tbody>
			<jsp:useBean id="now" class="java.util.Date"/>
		
			<c:if test="${empty list}">
			<tr>
				<td colspan="5">게시물이 없습니다.</td>
			</tr>
			</c:if>	
				
			<c:forEach items="${list}" var="notice">
			<tr>
				<td>${notice.noticePostId}</td>
				<td class="title">
					<a href="<c:url value='/notice/view?id=${notice.noticePostId}'/>">${notice.noticeHeader}</a>
				</td>
				<td>관리자</td>
				<td>
					<c:set var="regDate" value="${notice.noticeRegdate}" />
					
					<c:set var="diffSeconds" value="${(now.time - regDate.time) / 1000}" />
					<c:set var="diffMinutes" value="${diffSeconds / 60}" />
					<c:set var="diffHours" value="${diffSeconds / 3600}" />
					<c:set var="diffDays" value="${diffSeconds / 86400}" />
					
					
					<c:choose>
						<%-- 규칙 0: 1분 미만일 경우 --%>
						<c:when test="${diffMinutes < 1}">
							방금 전
						</c:when>
						<%-- 규칙 1: 1시간 미만일 경우 (1분 ~ 59분 전) --%>
						<c:when test="${diffMinutes < 60}">
							<fmt:formatNumber value="${diffMinutes}" maxFractionDigits="0"/>분 전
						</c:when>
						<%-- 규칙 2: 1일 미만일 경우 (1시간 ~ 23시간 전) --%>
						<c:when test="${diffHours < 24}">
							<fmt:formatNumber value="${diffHours}" maxFractionDigits="0"/>시간 전
						</c:when>
						<%-- 규칙 3: 3일 이하일 경우 (1일 ~ 3일 전) --%>
						<c:when test="${diffDays <= 3}">
							<fmt:formatNumber value="${diffDays}" maxFractionDigits="0"/>일 전
						</c:when>
						<%-- 규칙 4: 3일 초과 시 --%>
						<c:otherwise>
							<fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd"/>
						</c:otherwise>
					</c:choose>	
				</td>
				
                <td>${notice.noticeViewCount}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
		
		<div class="notice-footer">
			<div class="search-box">
				<form method="GET" action="<c:url value='/notice/list'/>">
					<input type="text" name="search" placeholder="Search">
					<button type="submit">검색</button>
				</form>
			</div>
			<div class="pagination">
				${pagebar}
			</div>
		
			<div class="table-options">
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<button type="button" class="btn btn-primary" 
						onclick="location.href='<c:url value='/admin/notice/add'/>';">글쓰기</button> 
                        <%-- 👆 경로를 절대 경로 '/admin/notice/add'로 수정 --%>
				</sec:authorize>
			</div>
		</div>
	</div>
</main>
