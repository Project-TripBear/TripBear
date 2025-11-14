<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/routepost.css">


<h2 class="board-title">여행루트 추천 게시판</h2>

<section class="board-list">
    <table>
        <thead>
            <tr>
                <th>No.</th>
                <th>게시글</th>
                <th>글쓴이</th>
                <th>작성일</th>
                <th>추천수</th>
                <th>조회수</th>
            </tr>
        </thead>

        <tbody>
            <c:choose>
                <c:when test="${not empty list}">
                    <c:forEach var="dto" items="${list}">
                        <tr onclick="location.href='${pageContext.request.contextPath}/routepost/view/${dto.routepostId}'">
                            <td>${dto.routepostId}</td>
                            <td>${dto.routepostTitle}</td>
                            <td>${dto.nickname}</td>
                            <td>${dto.routepostRegdate}</td>
                            <td>${dto.likeCount}</td>
                            <td>${dto.routepostViewCount}</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr><td colspan="6">등록된 게시글이 없습니다.</td></tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</section>

<div class="board-bottom">
    <div class="search-box">
        <form action="${pageContext.request.contextPath}/routepost/list" method="get">
            <input type="text" name="search" value="${param.search}" placeholder="Search">
            <button type="submit">🔍</button>
        </form>
    </div>

    <div class="paging">
        <c:if test="${page > 1}">
            <a href="${pageContext.request.contextPath}/routepost/list?page=${page - 1}&search=${param.search}">이전</a>
        </c:if>

         <c:forEach var="i" begin="1" end="${totalPages}">
        <c:choose>
            <c:when test="${i == page}">
                <strong>[${i}]</strong>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/routepost/list?page=${i}&search=${param.search}">${i}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>

		    <c:if test="${page < totalPage}">
		        <a href="${pageContext.request.contextPath}/routepost/list?page=${page + 1}&search=${param.search}">다음</a>
		    </c:if>
    </div>

    <div class="write-btn">
        <button type="button"
                onclick="location.href='${pageContext.request.contextPath}/routepost/add'">
            글쓰기
        </button>
    </div>
</div>
