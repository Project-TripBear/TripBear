<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/routepost.css">

<div class="routepost-list-page">

    <div class="inner">

        <h1 class="board-title">여행루트 추천 게시판</h1>

        <table class="routepost-table">
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
                                <td class="title">${dto.routepostTitle}</td>
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

        <!-- footer -->
        <div class="routepost-list-footer">

            <!-- 검색 -->
            <form class="routepost-search-form" action="${pageContext.request.contextPath}/routepost/list" method="get">
                <input type="text" name="search" value="${param.search}" placeholder="검색어 입력">
                <button type="submit">검색</button>
            </form>

            <!-- 글쓰기 -->
            <div class="routepost-write-btn">
                <button type="button"
                        onclick="location.href='${pageContext.request.contextPath}/routepost/add'">
                    글쓰기
                </button>
            </div>

            <!-- 페이지바 -->
            <div class="routepost-pagebar">
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${i == page}">
                            <strong>${i}</strong>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/routepost/list?page=${i}&search=${param.search}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>

        </div>

    </div>
</div>
