<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
                    <th>댓글</th>
                    <th>좋아요</th>
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
                                </td>
                                <td>${dto.nickname}</td>
                                <td>${dto.routepostRegdate}</td>
                                <td class="numeric">${dto.commentCount}</td>
                                <td class="numeric">${dto.likeCount}</td>
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
