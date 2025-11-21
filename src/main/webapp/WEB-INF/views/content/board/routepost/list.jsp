<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="now" class="java.util.Date" />

<div class="board-page">
    <div class="board-header">
        <h1 class="board-title">여행루트 추천 게시판</h1>
        <p class="board-subtitle">나만의 여행 루트를 공유하고 좋아요와 댓글을 받아보세요.</p>
    </div>

    <div class="board-table-wrapper">
        <table class="board-table">
            <thead>
                <tr>
                    <th>No.</th>
                    <th>제목</th>
                    <th>글쓴이</th>
                    <th>작성일</th>
                    <th>조회수</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach var="dto" items="${list}">
                            <tr onclick="location.href='${pageContext.request.contextPath}/routepost/view/${dto.routepostId}'">
                                <td class="numeric">${dto.routepostId}</td>
                                <td class="title">
                                    <a href="${pageContext.request.contextPath}/routepost/view/${dto.routepostId}">
                                        ${dto.routepostTitle}
                                    </a>
                                    <c:if test="${dto.commentCount > 0}">
                                        <span class="comment-count">[${dto.commentCount}]</span>
                                    </c:if>
                                </td>
                                <td>${dto.nickname}</td>
                                <td>
								    <%-- 1. String -> Date 변환 (형식에 맞게 pattern 꼭 수정할 것) --%>
								    <fmt:parseDate value="${dto.routepostRegdate}"
								                   pattern="yyyy-MM-dd HH:mm:ss"
								                   var="regDate" />
								
								    <%-- 2. now, regDate 둘 다 Date 이므로 .time 사용 가능 --%>
								    <c:set var="diffSeconds" value="${(now.time - regDate.time) / 1000}" />
								    <c:set var="diffMinutes" value="${diffSeconds / 60}" />
								    <c:set var="diffHours" value="${diffSeconds / 3600}" />
								    <c:set var="diffDays" value="${diffSeconds / 86400}" />
								
								    <c:choose>
								        <%-- 0: 1분 미만 --%>
								        <c:when test="${diffMinutes < 1}">
								            방금 전
								        </c:when>
								
								        <%-- 1: 1시간 미만 --%>
								        <c:when test="${diffMinutes < 60}">
								            <fmt:formatNumber value="${diffMinutes}" maxFractionDigits="0" />분 전
								        </c:when>
								
								        <%-- 2: 1일 미만 --%>
								        <c:when test="${diffHours < 24}">
								            <fmt:formatNumber value="${diffHours}" maxFractionDigits="0" />시간 전
								        </c:when>
								
								        <%-- 3: 3일 이하 --%>
								        <c:when test="${diffDays <= 3}">
								            <fmt:formatNumber value="${diffDays}" maxFractionDigits="0" />일 전
								        </c:when>
								
								        <%-- 4: 3일 초과 → 날짜만 --%>
								        <c:otherwise>
								            <fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd" />
								        </c:otherwise>
								    </c:choose>
								</td>

                                <td class="numeric">${dto.routepostViewCount}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7">
                                <div class="board-empty">등록된 게시글이 없습니다.</div>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <div class="board-actions">
        <form class="board-search" action="${pageContext.request.contextPath}/routepost/list" method="get">
            <input type="text" name="search" value="${param.search}" placeholder="검색어 입력">
            <button type="submit">검색</button>
        </form>
        <div class="board-button-group">
            <button type="button" class="btn btn-primary"
                    onclick="location.href='${pageContext.request.contextPath}/routepost/add'">
                글쓰기
            </button>
        </div>
    </div>

    <div class="board-pagination">
        <c:if test="${totalPages > 1}">
            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <strong>${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/routepost/list?page=${i}&search=${param.search}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:if>
    </div>
</div>
