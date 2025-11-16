<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="board-page">
    <div class="board-header">
        <h1 class="board-title">여행 후기 게시판</h1>
        <p class="board-subtitle">생생한 여행기를 읽고 댓글과 좋아요로 공감해 주세요.</p>
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
                            <tr onclick="location.href='${pageContext.request.contextPath}/review/view/${dto.reviewPostId}'">
                                <td class="numeric">${dto.reviewPostId}</td>
                                <td class="title">
                                    <a href="${pageContext.request.contextPath}/review/view/${dto.reviewPostId}">
                                        ${dto.reviewBoardTitle}
                                    </a>
                                </td>
                                <td>${dto.nickname}</td>
                                <td>${dto.reviewBoardRegdate}</td>
                                <td class="numeric">${dto.commentCount}</td>
                                <td class="numeric">${dto.likeCount}</td>
                                <td class="numeric">${dto.reviewBoardCount}</td>
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
        <form class="board-search" action="${pageContext.request.contextPath}/review/list" method="get">
            <input type="text" name="search" value="${param.search}" placeholder="검색어를 입력하세요">
            <button type="submit">검색</button>
        </form>
        <div class="board-button-group">
            <button type="button" class="btn btn-primary"
                    onclick="location.href='${pageContext.request.contextPath}/review/add'">글쓰기</button>
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
                        <a href="${pageContext.request.contextPath}/review/list?page=${i}&search=${param.search}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:if>
    </div>
</div>